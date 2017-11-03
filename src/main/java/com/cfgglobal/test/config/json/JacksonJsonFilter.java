package com.cfgglobal.test.config.json;

import com.cfgglobal.test.domain.BaseEntity;
import com.cfgglobal.test.domain.User;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;
import org.joor.Reflect;

import java.util.*;

@Slf4j
@SuppressWarnings("deprecation")
@JsonFilter("JacksonFilter")
public class JacksonJsonFilter extends FilterProvider {

    Map<Class<?>, Set<String>> includeMap = new HashMap<>();
    Map<Class<?>, Set<String>> filterMap = new HashMap<>();

    public void include(Class<?> type, String[] fields) {
        addToMap(includeMap, type, fields);
    }

    public void exclude(Class<?> type, String[] fields) {
        addToMap(filterMap, type, fields);
    }

    private void addToMap(Map<Class<?>, Set<String>> map, Class<?> type, String[] fields) {
        Set<String> fieldSet = map.getOrDefault(type, new HashSet<>());
        fieldSet.addAll(Arrays.asList(fields));
        map.put(type, fieldSet);
    }

    @Override
    public BeanPropertyFilter findFilter(Object filterId) {
        throw new UnsupportedOperationException("Access to deprecated filters not supported");
    }

    @Override
    public PropertyFilter findPropertyFilter(Object filterId, Object valueToFilter) {

        return new SimpleBeanPropertyFilter() {

            @Override
            public void serializeAsField(Object pojo, JsonGenerator jgen, SerializerProvider prov, PropertyWriter writer)
                    throws Exception {
                String name = writer.getName();
                if (apply(pojo.getClass(), name)) {
                   /* System.out.println(valueToFilter.getClass());
                    Object obj = Reflect.on(pojo).get(name);
                    if (isLazy(obj)) {
                        System.out.println("lazy");
                        return;
                    }*/

                    writer.serializeAsField(pojo, jgen, prov);
                } else if (!jgen.canOmitFields()) {
                    writer.serializeAsOmittedField(pojo, jgen, prov);
                }
            }
        };
    }

    public boolean apply(Class<?> type, String name) {
        String simpleName = type.getSimpleName();
        if (simpleName.endsWith("Dto")) {
            type = Reflect.on(BaseEntity.class.getPackage().getName() + "." + StringUtils.substringBefore(simpleName, "Dto")).get();
        }
        if (type.getSuperclass() == User.class) {
            type = User.class;
        }
        Set<String> includeFields = includeMap.get(type);
        Set<String> filterFields = filterMap.get(type);
        if (!includeFields.isEmpty() && includeFields.contains(name)) {
            return true;
        } else if (!filterFields.isEmpty() && !filterFields.contains(name)) {
            return true;
        } else if (!includeFields.isEmpty() && !filterFields.isEmpty()) {
            //return true;
        }
        return false;
    }

    public boolean isLazy(Object value) {

        if (value instanceof HibernateProxy) {//hibernate代理对象
            LazyInitializer initializer = ((HibernateProxy) value).getHibernateLazyInitializer();
            if (initializer.isUninitialized()) {
                return true;
            }
        } else if (value instanceof PersistentCollection) {//实体关联集合一对多等
            PersistentCollection collection = (PersistentCollection) value;
            if (!collection.wasInitialized()) {
                return true;
            }
            Object val = collection.getValue();
            if (val == null) {
                return true;
            }
        }
        return false;
    }

}