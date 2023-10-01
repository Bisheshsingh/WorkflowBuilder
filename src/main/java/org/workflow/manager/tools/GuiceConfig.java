package org.workflow.manager.tools;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import java.util.List;

public class GuiceConfig {
    private static GuiceConfig config;
    private static Injector injector;

    public GuiceConfig() {
        this(binder -> {
        });
    }

    public GuiceConfig(final List<Module> modules) {
        injector = Guice.createInjector(modules);
    }

    public GuiceConfig(final Module module) {
        injector = Guice.createInjector(module);
    }

    public <T> T getInstance(Class<T> clazz) {
        if (config == null) {
            config = new GuiceConfig();
        }

        return injector.getInstance(clazz);
    }

    public static GuiceConfig init(final Module module) {
        if (config == null) {
            config = new GuiceConfig(module);
        } else {
            injector = Guice.createInjector(module);
        }

        return config;
    }

    public static GuiceConfig init(final List<Module> modules) {
        if (config == null) {
            config = new GuiceConfig(modules);
        } else {
            injector = Guice.createInjector(modules);
        }

        return config;
    }

    public Injector getInjector() {
        if (config == null) {
            config = new GuiceConfig();
        }

        return injector;
    }
}
