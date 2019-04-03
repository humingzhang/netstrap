package io.netstrap.common.factory;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 通用类工厂
 *
 * @author minghu.zhang
 * @date 2018/11/02
 */
@Log4j2
public class ClassFactory {
    /**
     * 类工厂单例对象
     */
    private static ClassFactory factory;

    /**
     * 类路径下的所有类
     */
    private List<Class<?>> classes = new ArrayList<>();

    /**
     * 初始化类工厂
     */
    private ClassFactory(String... packages) {
        for (String pkg : packages) {
            ClassReader.getClasses(pkg, classes);
        }
    }

    /**
     * 获取类工厂
     */
    public static ClassFactory getInstance(String... packages) {
        if (Objects.isNull(factory)) {
            synchronized (ClassFactory.class) {
                if (Objects.isNull(factory)) {
                    factory = new ClassFactory(packages);
                }
            }
        }

        return factory;
    }


    /**
     * 通过注解获取类列表
     */
    public List<Class<?>> getClassByAnnotation(Class<? extends Annotation> annotation) {

        List<Class<?>> annotationList = new ArrayList<>();

        if (Objects.isNull(annotation) || Objects.isNull(factory)) {
            return annotationList;
        }

        for (Class<?> clz : classes) {
            if (clz.isAnnotationPresent(annotation)) {
                annotationList.add(clz);
            }
        }

        return annotationList;
    }

    /**
     * 通过包获取类列表
     */
    public List<Class<?>> getClassByPackage(String packages) {

        List<Class<?>> packaging = new ArrayList<>();

        if (Objects.isNull(packages) || Objects.isNull(factory)) {
            return packaging;
        }

        for (Class<?> clz : classes) {
            String pkg = clz.getPackage().getName();
            if (pkg.startsWith(packages.trim())) {
                packaging.add(clz);
            }
        }

        return packaging;
    }

    /**
     * 通过接口获取类列表
     */
    public List<Class<?>> getClassByInterface(Class<?> interfaces) {

        List<Class<?>> subclasses = new ArrayList<>();

        if (Objects.isNull(interfaces) || !interfaces.isInterface()
                || Objects.isNull(factory)) {
            return subclasses;
        }

        if (interfaces.isInterface()) {
            // 获取当前的包名
            for (Class<?> clz : classes) {
                // 判断是否是同一个接口
                if (interfaces.isAssignableFrom(clz)) {
                    // 本身不加入进去
                    if (!interfaces.equals(clz)) {
                        subclasses.add(clz);
                    }
                }
            }
        }

        return subclasses;
    }


}
