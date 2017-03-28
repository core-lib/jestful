package org.qfox.jestful.commons;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by yangchangpei on 17/3/28.
 */
public abstract class Config {
    private static Map<String, Properties> cache = new HashMap<String, Properties>();

    protected Config() {
        Class<?> clazz = this.getClass();
        Properties properties = load(clazz);

        for (String name : properties.stringPropertyNames()) {
            Class<?> c = clazz;
            while (c != Object.class) {
                try {
                    Field field = c.getDeclaredField(name);
                    if (Modifier.isFinal(field.getModifiers())) {
                        break;
                    }
                    if (field.getType() != String.class) {
                        break;
                    }
                    field.setAccessible(true);
                    field.set(this, properties.getProperty(name));
                    break;
                } catch (NoSuchFieldException e) {
                    c = c.getSuperclass();
                } catch (IllegalAccessException e) {
                    break;
                }
            }
        }

        try {
            PropertyDescriptor[] descriptors = Introspector.getBeanInfo(clazz).getPropertyDescriptors();
            for (PropertyDescriptor descriptor : descriptors) {
                if ("class".equals(descriptor.getName())) {
                    continue;
                }

                Method setter = descriptor.getWriteMethod();
                if (setter == null || setter.getParameterTypes().length != 1 || setter.getParameterTypes()[0] != String.class) {
                    continue;
                }

                String name = descriptor.getName();
                if (!properties.containsKey(name)) {
                    continue;
                }

                String value = properties.getProperty(name);
                setter.invoke(this, value);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Properties load(Class<?> clazz) throws IllegalArgumentException {
        String path = "/" + clazz.getName().replace('.', '/') + ".properties";
        Properties properties = cache.get(path);
        if (properties != null) {
            return properties;
        }
        synchronized (clazz) {
            if (properties != null) {
                return properties;
            }
            InputStream in = null;
            try {
                URL resource = clazz.getResource(path);
                if (resource == null) {
                    throw new FileNotFoundException(path);
                }
                properties = new Properties();
                properties.load(in = resource.openStream());
                cache.put(path, properties);
                return properties;
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            } finally {
                IOUtils.close(in);
            }
        }
    }

}
