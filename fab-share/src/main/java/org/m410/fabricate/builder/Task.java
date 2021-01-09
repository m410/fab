package org.m410.fabricate.builder;

import java.io.Serializable;

/**
 * Should be immutable
 *
 * @author Michael Fortin
 */
public interface Task extends Serializable {
    String getName(); // must be unique
    String getDescription();
    void execute(BuildContext context) throws Exception;
}
