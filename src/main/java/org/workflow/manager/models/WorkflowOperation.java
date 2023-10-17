package org.workflow.manager.models;

public interface WorkflowOperation {
   void operate(final Runnable runnable);
   void shutDown();
}
