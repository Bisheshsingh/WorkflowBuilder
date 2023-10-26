package org.workflow.manager.tools;

import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import lombok.NonNull;
import org.workflow.manager.exceptions.BinderException;
import org.workflow.manager.executors.WorkflowExecutor;
import org.workflow.manager.models.ContextObject;
import org.workflow.manager.models.WorkflowConfig;
import org.workflow.manager.models.WorkflowOperation;
import org.workflow.manager.models.WorkflowResponse;
import org.workflow.manager.workflow_operations.AutoThreadOperation;

@SuppressWarnings({"rawtypes", "unchecked"})
public class WorkflowRunner {
    private static final String DEFAULT_LEVEL = "default";
    private static final Module[] DEFAULT_MODULES = {binder -> {}};
    private static final WorkflowOperation DEFAULT_OPERATION = new AutoThreadOperation();

    private static WorkflowRunner instance;
    private WorkflowOperation workflowOperation;
    private Module[] modules;
    private String level;
    private WorkflowConfig config;

    private WorkflowRunner() {
        this.modules = DEFAULT_MODULES;
        this.level = DEFAULT_LEVEL;
        this.workflowOperation = DEFAULT_OPERATION;
    }

    public static WorkflowRunner createInstance() {
        instance = new WorkflowRunner();

        return instance;
    }

    public WorkflowRunner withWorkflowOperation(@NonNull final WorkflowOperation workflowOperation) {
        this.workflowOperation = workflowOperation;
        return instance;
    }

    public WorkflowRunner withModules(@NonNull final Module... modules) {
        this.modules = modules;
        return instance;
    }

    public WorkflowRunner withLevel(@NonNull final String level) {
        this.level = level;
        return instance;
    }

    public <C extends ContextObject> WorkflowRunner withConfig(@NonNull final WorkflowConfig<C> config) {
        this.config = config;
        return instance;
    }

    public <C extends ContextObject> WorkflowResponse run(@NonNull final WorkflowResponse startResponse,
                                                          @NonNull final C context) throws BinderException {

        final WorkflowExecutor<C> workflowExecutor = (WorkflowExecutor<C>) GuiceConfig.getInjector(modules)
                .getInstance(Key.get(new TypeLiteral<WorkflowExecutor<ContextObject>>() {}));

        workflowExecutor.execute(config, context,
                startResponse, level, workflowOperation);

        return workflowExecutor.getWorkflowResponse();
    }
}
