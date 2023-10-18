package org.workflow.manager.tools;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import java.util.Arrays;
import java.util.List;

public class GuiceConfig {
    private static GuiceConfig config;
    private static Injector injector;

    public GuiceConfig() {
        this(binder -> {});
    }

    public GuiceConfig(final List<Module> modules) {
        injector = Guice.createInjector(modules);
    }

    public GuiceConfig(final Module... modules) {
        injector = Guice.createInjector(modules);
    }

    public <T> T getInstance(Class<T> clazz) {
        if (config == null) {
            config = new GuiceConfig();
        }

        return injector.getInstance(clazz);
    }

    public Injector getInjector() {
        if (config == null) {
            config = new GuiceConfig();
        }

        return injector;
    }

    public static GuiceConfig init(final Module... modules) {
        return init(Arrays.asList(modules));
    }

    public static GuiceConfig init() {
        return init(binder ->{});
    }

    public static GuiceConfig init(final List<Module> modules) {
        if (config == null) {
            config = new GuiceConfig(modules);
        } else {
            injector = Guice.createInjector(modules);
        }

        return config;
    }
}
