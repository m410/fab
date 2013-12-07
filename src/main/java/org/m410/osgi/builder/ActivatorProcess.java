package org.m410.osgi.builder;

/**
 *  - clean
 *     Clean Generated Artifacts
 *     [pre-clean]
 * - clean-cache
 *     Deletes build artifacts and deletes the build cache.
 *     [clean, pre-clean]
 * - clean-task
 *     Deletes all build artifacts.
 *     [pre-clean]
 * - compile
 *     Compile source code.
 *     [process-resources, dependency, pre-resolve, init]
 * - compile-task
 *     Compile the source code
 *     [process-resources]
 * - compile-test
 *     Runs unit testing on the application.
 *     [process-test-resources, init]
 * - dependency
 *     Downloads application dependencies and makes them available for compilation and packaging.
 *     [pre-resolve, init]
 * - dependency-report
 *     Generate Ivy Dependency Report for application dependencies.
 *     [init, pre-doc]
 * - dependency-task
 *     Download Dependencies.
 *     [pre-resolve]
 * - doc
 *     Generate ScalaDoc documemtation.
 *     [pre-doc, init]
 * - doc-package
 *     Jar up javadoc or scaladoc.
 *     [package-task, pre-package, persistence-xml]
 * - doc-task
 *     Generate ScalaDoc.
 *     [pre-doc]
 * - export-dependencies
 *     Create a lib directory with all the application dependencies
 *     [init]
 * - info
 *     Dump Configuration Information.
 *     [init]
 * - init
 *     Initialize the WebApp Configuration.  Called before most tasks.
 *     []
 * - init-task
 *     Initialize the Application Configuration.
 *     []
 * - install-mvn
 *     Publish the artifacts to a remote repository
 *     [init-task]
 * - install-task
 *     Publish the artifacts to a local ivy repository
 *     [init-task]
 * - jetty8
 *     Run Jetty
 *     [init]
 * - package
 *     This is the default task, it compiles and packages the application.
 *     [package-task, pre-package, persistence-xml, test, test-compile, process-test-resources, compile, process-resources, dependency, pre-resolve, init]
 * - package-task
 *     Package the project.
 *     [pre-package, persistence-xml]
 * - persistence-xml
 *     Generate A JPA persistence xml config
 *     []
 * - post-package
 *     Post package cleanup.
 *     [package-task, pre-package, persistence-xml]
 * - pre-clean
 *     Pre-process resources
 *     []
 * - pre-doc
 *     Pre Documemtation
 *     []
 * - pre-package
 *     Pre Package resources.
 *     [persistence-xml]
 * - pre-resolve
 *     Pre download preperation.
 *     []
 * - process-resources
 *     Process Resources before compilation.
 *     []
 * - process-test-resources
 *     Pre Compile processing
 *     []
 * - publish
 *     Once the packaging of the application is complete this will upload to a remote repository.
 *     [init-task, package, package-task, pre-package, persistence-xml, test, test-compile, process-test-resources, compile, process-resources, dependency, pre-resolve, init]
 * - publish
 *     Publish the artifacts to a remote repository
 *     [init-task]
 * - source-package
 *     Post source to a jar.
 *     [package-task, pre-package, persistence-xml]
 * - test
 *     Runs unit testing on the application.
 *     [test-compile, process-test-resources, compile, process-resources, dependency, pre-resolve, init]
 * - test-compile
 *     Pre Compile processing
 *     [process-test-resources]
 * - test-task
 *     Pre Compile processing
 *     [test-compile, process-test-resources]
 *
 * @author Michael Fortin
 */
public class ActivatorProcess {

    // read configuration file
    // load profile

    // get list or modules bundles.
    // install and start all

    // get all tasks
    // build task trees

    // execute task
    // or
    // list tasks
    // or
    // task help

    

    // stop remove module bundles
}
