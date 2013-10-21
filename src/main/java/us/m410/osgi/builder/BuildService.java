package us.m410.osgi.builder;

/**
 * Document Me..
 *
 * @author Michael Fortin
 */
public interface BuildService {

    Task getTaskByName(String name);

    void addToStart(Task task);

    void addToPreDependencies(Task task);
    void addToDependencies(Task task);
    void addToPostDependencies(Task task);

    void addToPreCompile(Task task);
    void addToCompile(Task task);
    void addToPostCompile(Task task);

    void addToPreCompileTest(Task task);
    void addToCompileTest(Task task);
    void addToPostCompileTest(Task task);

    void addToPreTestRun(Task task);
    void addToTestRun(Task task);
    void addToPostTestRun(Task task);

    void addToPreAssemble(Task task);
    void addToAssemble(Task task);
    void addToPostAssemble(Task task);

    void addToPreDoc(Task task);
    void addToDoc(Task task);
    void addToPostDoc(Task task);

    void addUnbound(Task task);

    void addToEnd(Task task);
}
