package org.workflow.manager.tools;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;

public class GuiceConfig {
    private static Module module;
    private static Injector injector;

    private static Module getModule() {
        if(module == null) {
            return binder -> {};
        }

        return module;
    }

    public static <T> T getInstance(Class<T> clazz) {
        if (injector == null) {
            injector = Guice.createInjector(getModule());
        }

        return injector.getInstance(clazz);
    }

    public static <T> T getInstance(Class<T> clazz, Module... overrideModules) {
        module = Modules.override(getModule()).with(overrideModules);
        injector = Guice.createInjector(module);

        return injector.getInstance(clazz);
    }

    public static Injector getInjector() {
        if (injector == null) {
            injector = Guice.createInjector(getModule());
        }

        return injector;
    }

    public static Injector getInjector(Module... overrideModules) {
        module = Modules.override(getModule()).with(overrideModules);
        injector = Guice.createInjector(module);

        return injector;
    }
}
