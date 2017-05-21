package ru.iskratel.server.service;

import java.net.URL;
import java.net.URLClassLoader;

class UpdatableInRuntimeClassLoader extends URLClassLoader {

    UpdatableInRuntimeClassLoader(URLClassLoader classLoader) {
        super(classLoader.getURLs());
    }

    /**
     * Increase visibility-level
     */
    @Override
    public void addURL(URL url) {
        super.addURL(url);
    }
}
