package io.netstrap.config;

import io.netstrap.common.factory.ClassFactory;
import io.netstrap.config.policy.ProtocolPolicy;
import io.netstrap.config.stereotype.Configurable;
import io.netstrap.config.stereotype.Prefix;
import io.netstrap.config.stereotype.ProtocolSupport;
import io.netstrap.config.stereotype.Value;
import io.netstrap.config.tool.ConfigReader;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 配置管理
 * @author minghu.zhang
 * @date 2018/11/02
 */
@Component
@Log4j2
public class ConfigHandler {

    /**
     * 获取实现该注解的配置类
     */
    private List<Class<?>> classes;
    /**
     * Spring 上下文
     */
    private ConfigurableApplicationContext context;
    /**
     * 类工厂
     */
    private ClassFactory factory;

    @Autowired
    public ConfigHandler(ConfigurableApplicationContext context) {
        this.context = context;
        this.factory = ClassFactory.getInstance();
    }

    /**
     * 自动设置配置的值
     */
    @PostConstruct
    public void autoSetConfigValue() throws IllegalAccessException, InstantiationException, IOException {

        classes = factory.getClassByAnnotation(Configurable.class);
        ConfigReader configReader = new ConfigReader();
        for (Class<?> clz:classes) {
            final Object instance = context.getBean(clz);
            if(Objects.nonNull(instance)) {
                Map<String, String> properties = getConfigProperties(clz, configReader);
                setBeanProperty(instance,properties);
            }
        }
    }

    /**
     * 设置bean对象的属性值
     */
    private void setBeanProperty(Object instance,Map<String,String> properties) throws InstantiationException, IllegalAccessException {
        Class<?> clz = instance.getClass();
        saveField(instance,clz,properties);
    }

    /**
     * 保存字段值
     */
    private void saveField(Object instance,Class<?> clz,Map<String,String> properties) throws IllegalAccessException, InstantiationException {
        Class<?> superclass = clz.getSuperclass();
        if(!superclass.equals(Object.class)) {
            saveField(instance,superclass,properties);
        }

        String prefix = clz.getSimpleName().replaceAll("[A-Z]", ".$0").toLowerCase();
        //组装Prefix
        {
            String dot = ".";
            if (prefix.startsWith(dot)) {
                prefix = prefix.substring(1);
            }

            if (clz.isAnnotationPresent(Prefix.class)) {
                prefix = clz.getAnnotation(Prefix.class).value();
            }
        }

        for (Field field : clz.getDeclaredFields()) {
            setFieldValue(field,prefix,properties,instance);
        }
    }

    /**
     * 设置值参数
     */
    private void setFieldValue(Field field,String prefix,Map<String,String> properties,Object instance) throws IllegalAccessException, InstantiationException {
        field.setAccessible(true);

        String key = prefix + "." + field.getName();
        Value express = field.getAnnotation(Value.class);
        if (field.isAnnotationPresent(Value.class)) {
            key = parse(express.value());
        }

        String value = properties.get(key);
        if(convertible(field.getType())) {
            if (Objects.nonNull(value)) {
                field.set(instance, ConvertUtils.convert(value, field.getType()));
            }
        } else {
            if(!field.isAnnotationPresent(Autowired.class)     ||
                    !field.isAnnotationPresent(Resource.class) ||
                    !field.isAnnotationPresent(Inject.class)) {
                Object fieldObject = field.getType().newInstance();
                field.set(instance, fieldObject);
                saveField(fieldObject, field.getType(), properties);
            }
        }
    }

    /**
     * 获取表达式值
     */
    public String parse(String express) {
        int minLength = 3;
        if(express.length() <= minLength) {
            throw new RuntimeException("Please pass in a correct value expression. ");
        }

        return express.substring(2,express.length()-1);
    }

    /**
     * 判断clz是否可转换
     */
    private boolean convertible(Class<?> clz) {

        if(clz.equals(Integer.class) || clz.equals(int.class)) {
            return true;
        } else if(clz.equals(Byte.class) || clz.equals(byte.class)) {
            return true;
        } else if(clz.equals(Double.class) || clz.equals(double.class)) {
            return true;
        } else if(clz.equals(Float.class) || clz.equals(float.class)) {
            return true;
        } else if(clz.equals(Character.class) || clz.equals(char.class)) {
            return true;
        } else if(clz.equals(Short.class) || clz.equals(short.class)) {
            return true;
        } else if(clz.equals(Boolean.class) || clz.equals(boolean.class) ) {
            return true;
        } else if(clz.equals(String.class)) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 获取配置属性
     */
    private Map<String,String> getConfigProperties(Class<?> clz,ConfigReader configReader) throws IOException {
        Configurable configurable = clz.getAnnotation(Configurable.class);

        InputStream input = null;
        Map<String, String> properties;
        try {
            ResourceReader reader = ResourceReader.getReader(configurable.protocol());
            input = reader.load(configurable.path());
            properties = configReader.readByInputStream(input);
        } finally {
            if(Objects.nonNull(input)) {
                input.close();
            }
        }

        return properties;
    }

    /**
     * 读取资源内容
     * @author minghu.zhang
     * @date 2018/11/02
     */
    public interface ResourceReader {

        /**
         * 获取资源输入流
         * @param path 资源加载路径
         * @return 获取资源输入流
         */
        InputStream load(String path);

        /**
         * 获取加载器
         * @param protocol 协议策略
         * @return 返回资源阅读器
         */
        static ResourceReader getReader(ProtocolPolicy protocol) {

            List<Class<?>> classes = ClassFactory.getInstance().getClassByInterface(ResourceReader.class);
            try {
                for (Class<?> clz:classes) {
                    if(clz.isAnnotationPresent(ProtocolSupport.class)) {

                        ProtocolSupport support = clz.getAnnotation(ProtocolSupport.class);
                        if(support.protocol().equals(protocol)) {
                            return (ResourceReader) clz.newInstance();
                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }


}
