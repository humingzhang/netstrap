package io.netstrap.common.factory;

import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 读取类加载器中指定的类
 *
 * @author minghu.zhang
 * @date 2018/11/06
 */
@Log4j2
public class ClassReader {

    /**
     * 获取某个包下的类
     */
    public static void getClasses(String packageName, List<Class<?>> classes) {
        try {
            Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(packageName.replace(".", "/"));
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                // 获取jar
                String protocol = url.getProtocol();
                if (protocol.equals("file")) {
                    // 获取包的物理路径
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    readClassFromFile(packageName, filePath, true, classes);
                } else if (protocol.equals("jar")) {
                    readClassFromJar(url, packageName, classes);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从文件读取Class
     */
    private static void readClassFromFile(String packageName, String filePath, final boolean recursive, List<Class<?>> classes) {
        // 获取此包的目录 建立一个File
        File dir = new File(filePath);
        // 如果不存在或者 也不是目录就直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        // 如果存在 就获取包下的所有文件 包括目录
        File[] difFiles = dir.listFiles((file) -> {
            // 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
            return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
        });
        // 循环所有文件
        for (File file : difFiles) {
            // 如果是目录 则继续扫描
            if (file.isDirectory()) {
                readClassFromFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive,
                        classes);
            } else {
                // 如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    // 添加到集合中去
                    if (packageName.startsWith(".")) {
                        packageName = packageName.substring(1);
                    }

                    if (packageName.indexOf("/") != -1) {
                        packageName = packageName.replace("/", ".");
                    }
                    Class<?> clz = Class.forName(packageName + '.' + className);
                    if (!classes.contains(clz)) {
                        classes.add(clz);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 从jar包读取class
     */
    private static void readClassFromJar(URL url, String packageName, List<Class<?>> classes) throws IOException, ClassNotFoundException {
        JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
        // 从此jar包 得到一个枚举类
        Enumeration<JarEntry> entries = jar.entries();
        // 同样的进行循环迭代
        while (entries.hasMoreElements()) {
            // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
            JarEntry entry = entries.nextElement();
            String name = entry.getName();
            // 如果是以/开头的
            if (name.charAt(0) == '/') {
                // 获取后面的字符串
                name = name.substring(1);
            }

            if (name.endsWith(".class") && !entry.isDirectory()) {
                name = name.replace("/", ".").substring(0, name.length() - 6);
                if (name.startsWith(packageName)) {
                    Class<?> clz = Class.forName(name);
                    if (!classes.contains(clz)) {
                        classes.add(clz);
                    }
                }
            }
        }
    }
}