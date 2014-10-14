package org.m410.fab.garden;

import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:mifortin@deloitte.com">Michael Fortin</a>
 */
public class ReflectTest {

    static interface Type {
       String hi();
    }

    static class TypeImpl implements Type {
        @Override public String hi() {
            return "hello";
        }
    }

    @SuppressWarnings("unchecked")
    static class Factory {
        public static <T> T wrap(T object) {
            try {
                Method m = Factory.class.getMethod("wrap",Object.class);
                m.getGenericParameterTypes();

                final ClassLoader loader = Thread.currentThread().getContextClassLoader();
                return (T) Proxy.newProxyInstance(loader, null, null);
            }
            catch (NoSuchMethodException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    @Test public void detectType() {
//        Type t = Factory.<Type>wrap(new TypeImpl());
//        assertNotNull(t);
    }
}
