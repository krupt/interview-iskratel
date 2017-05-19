package ru.iskratel.server.service;

import java.net.URL;
import java.net.URLClassLoader;

public class UpdatableInRuntimeClassLoader extends URLClassLoader {

    public UpdatableInRuntimeClassLoader(URLClassLoader classLoader) {
        super(classLoader.getURLs());
    }

    @Override
    public void addURL(URL url) {
        super.addURL(url);
    }
}
