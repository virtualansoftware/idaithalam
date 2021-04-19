package io.virtualan.idaithalam.contract;

import java.net.URL;
import java.net.URLClassLoader;

/**
     * The type Execution classloader.
     */
public class ExecutionClassloader extends URLClassLoader {

        /**
         * Instantiates a new Execution classloader.
         *
         * @param urls        the urls
         * @param classLoader the class loader
         */
        public ExecutionClassloader(URL[] urls, ClassLoader classLoader) {
            super(urls, classLoader);
        }

        @Override
        public Class loadClass(String name, boolean resolve) throws ClassNotFoundException {
            return super.loadClass(name, resolve);
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            return super.findClass(name);
        }
    }

