package com.feng.mybatis.objectfactory;

import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.reflection.factory.DefaultObjectFactory;

/**
 * @author fengsy
 * @date 7/26/21
 * @Description
 */
public class MyObjectFactory extends DefaultObjectFactory {
    @Override
    public Object create(Class type) {
        // System.out.println("object factory create an object of type: " + type);
        return super.create(type);
    }

    @Override
    public <T> T create(Class<T> type, List<Class<?>> constructorArgTypes, List<Object> constructorArgs) {
        return super.create(type, constructorArgTypes, constructorArgs);
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
    }

    @Override
    public <T> boolean isCollection(Class<T> type) {
        return Collection.class.isAssignableFrom(type);
    }
}
