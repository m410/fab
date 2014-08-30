package org.m410.fab.garden.support;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

/**
 * @author <a href="mailto:mifortin@deloitte.com">Michael Fortin</a>
 */
public class Reflect {
    public static Class[] classTypeArgs(Object o) {
        return ((Class[])((ParameterizedType)o.getClass().getGenericSuperclass())
                .getActualTypeArguments());

    }

    public static Class[] methodTypeArgs(Method o) {
        return o.getParameterTypes();

    }
}
