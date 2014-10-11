package org.m410.fab.garden.support;

import org.m410.fab.garden.ContextJavaCompiler;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.StandardWatchEventKinds.*;

/**
 * @author <a href="mailto:mifortin@deloitte.com">Michael Fortin</a>
 */
public final class SourceMonitor {
    private final Object statusLock = new Object();
    private final Object compilerLock = new Object();
    private Status status = Status.Ok;
    private List<ReloadingEventListener> listeners = new ArrayList<>();
    private AppFactory appFactory;

    public SourceMonitor(File sourceBaseDir, ContextJavaCompiler contextJavaCompiler,AppFactory appFactory)
            throws IOException, InterruptedException {
        final Path sourcePath = sourceBaseDir.toPath();
        this.appFactory = appFactory;

        final SourceCompiler sourceCompiler = new SourceCompiler(compilerLock, sourceBaseDir, contextJavaCompiler);
        new Thread(sourceCompiler, "sourceCompiler").start();

        final SourceWatcher fileWatcher = new SourceWatcher(sourcePath);
        new Thread(fileWatcher, "SourceWatcher").start();
    }

    public AppFactory getAppFactory() {
        return appFactory;
    }

    public Status getStatus() {
        synchronized (statusLock) {

            if(status == Status.Compiling || status == Status.Ok || status == Status.Failed)
                return status;

            synchronized (compilerLock) {
                compilerLock.notify();
            }
            this.status = Status.Compiling;
            return status;
        }
    }

    public void addReloadingListener(ReloadingEventListener e) {
        listeners.add(e);
    }

    void setStatus(final Status s) {
        synchronized (statusLock) {
            this.status = s;
        }
    }



    public void renderStatusPage(ServletRequest req, ServletResponse res) throws IOException {
        final String page = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "<meta charset=\"utf-8\">\n" +
                "<meta http-equiv=\"refresh\" content=\"4\">\n" +
                "<title>Brzy Recompiling</title>\n" +
                "<style>\n" +
                "body {margin:0 auto;text-align:center;padding:4em 2em;\n" +
                " font-family:\"Helvetica Neue\", Arial, Helvetica, sans-serif;color:#333}\n" +
                "</style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Recompiling Source</h1>\n" +
                "</body>\n" +
                "</html>\n" +
                "\n";

        res.getOutputStream().write(page.getBytes("UTF-8"));
    }

    public void fireEvent(ReloadingEvent reloadingEvent) {
        listeners.stream().forEach(e->e.onChange(reloadingEvent));
    }

    public enum Status {
        Ok,
        Modified,
        Compiling,
        Failed
    }

    class SourceCompiler implements Runnable {
        final File sourceBaseDir;
        final Object lock;
        final ContextJavaCompiler compiler;

        public SourceCompiler(Object lock, File sourceBaseDir, ContextJavaCompiler compiler) {
            this.lock = lock;
            this.sourceBaseDir = sourceBaseDir;
            this.compiler = compiler;
        }

        @Override
        public void run() {
            while(true) {
                try {
                    synchronized (lock) {
                        lock.wait();
                        fireEvent(new ReloadingEvent(true));
                        setStatus(Status.Compiling);
                        ContextJavaCompiler.Status s = compiler.compile();

                        if(s.isOk()){

                            try {
                                final AppRef appRef = appFactory.make();
                                fireEvent(new ReloadingEvent(appRef.getClassLoader(),appRef.getApplication()));
                                setStatus(Status.Ok);
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                                setStatus(Status.Failed);
                            }
                        }
                        else {
                            // fire failed event
                            setStatus(Status.Failed);
                        }
                    }
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }

                setStatus(Status.Ok);
            }
        }
    }

    class SourceWatcher implements Runnable {
        final Path path;
        final WatchService watchService;

        public SourceWatcher(Path path) throws IOException {
            this.path = path;
            watchService = FileSystems.getDefault().newWatchService();
            Files.walkFileTree(path, new Visitor(watchService));
        }

        @Override
        public void run() {
            try {
                WatchKey key = watchService.take();

                for (WatchEvent event : key.pollEvents()) {
                    System.out.printf("##### Received %s event for file: %s\n", event.kind(), event.context());

                    setStatus(Status.Modified);
                    key.reset();
                    key = watchService.take();
                }

            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Stopping thread");
        }
    }

    class Visitor implements  FileVisitor<Path> {
        final WatchService watchService;

        Visitor(WatchService watchService) {
            this.watchService = watchService;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            dir.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY, OVERFLOW);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            return FileVisitResult.CONTINUE;
        }
    }

}
