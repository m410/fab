package org.m410.fabricate.service.internal.task;

import org.m410.fabricate.builder.BuildContext;
import org.m410.fabricate.builder.Task;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * @author m410
 */
public class CleanTask implements Task {

    @Override
    public String getName() {
        return "Clean";
    }

    @Override
    public String getDescription() {
        return "Remove Project Artifacts";
    }

    @Override
    public void execute(BuildContext context) throws Exception {
        final Path path = FileSystems.getDefault().getPath(context.getBuild().getTargetDir());

        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                if (exc == null) {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
                else {
                    throw exc;
                }
            }
        });
    }
}
