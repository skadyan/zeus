package com.cumulativeminds.zeus.infra;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;

public class GeneratePojoAtRuntime {

    public static Class<?> generate(String className, Map<String, Class<?>> properties) throws NotFoundException,
            CannotCompileException {
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.makeClass(className);
        cc.addInterface(resolveCtClass(Serializable.class));
        for (Entry<String, Class<?>> entry : properties.entrySet()) {

            CtField ctField = new CtField(resolveCtClass(entry.getValue()), entry.getKey(), cc);
            cc.addField(ctField);

            // add getter
            cc.addMethod(generateGetter(ctField));
            // add setter
            cc.addMethod(generateSetter(ctField));

        }

        return cc.toClass();
    }

    private static CtMethod generateGetter(CtField ctField)
            throws CannotCompileException {
        String fieldName = ctField.getName();
        String getterName = "get" + fieldName.substring(0, 1).toUpperCase()
                + fieldName.substring(1);

        return CtNewMethod.getter(getterName, ctField);
    }

    private static CtMethod generateSetter(CtField ctField)
            throws CannotCompileException {
        String fieldName = ctField.getName();
        String setterName = "set" + fieldName.substring(0, 1).toUpperCase()
                + fieldName.substring(1);

        return CtNewMethod.setter(setterName, ctField);
    }

    private static CtClass resolveCtClass(Class<?> clazz) throws NotFoundException {
        ClassPool pool = ClassPool.getDefault();
        return pool.get(clazz.getName());
    }

    public static void main(String[] args) throws Throwable {
        Map<String, Class<?>> props = new HashMap<String, Class<?>>();
        props.put("foo", Integer.class);
        props.put("bar", String.class);

        Class<?> clazz = generate(
                "net.javaforge.blog.javassist.Pojo$Generated", props);

        Object obj = clazz.newInstance();

        System.out.println("Clazz: " + clazz);
        System.out.println("Object: " + obj);
        System.out.println("Serializable? " + (obj instanceof Serializable));

        for (final Method method : clazz.getDeclaredMethods()) {
            System.out.println(method);
        }

        // set property "bar"
        clazz.getMethod("setBar", String.class).invoke(obj, "Hello World!");

        // get property "bar"
        String result = (String) clazz.getMethod("getBar").invoke(obj);
        System.out.println("Value for bar: " + result);
    }
}
