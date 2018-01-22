package com.cfgglobal.test.config.json;

import com.cfgglobal.test.config.app.ApplicationProperties;
import com.cfgglobal.test.domain.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.querydsl.core.types.Path;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.jackson.datatype.VavrModule;
import org.joor.Reflect;

public class CustomerJsonSerializer {


    private ObjectMapper objectMapper = new ObjectMapper();
    private JacksonJsonFilter jacksonFilter = new JacksonJsonFilter();

    public void filter(Class<?> clazz, List<Path> include, List<Path> exclude) {
        objectMapper.registerModule(new VavrModule());
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new JavaTimeModule());
        // objectMapper.findAndRegisterModules();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        if (clazz == null) {
            return;
        }

        jacksonFilter.include(clazz, include.map(e -> e.getMetadata().getName()).toJavaArray(String.class));

        jacksonFilter.exclude(clazz, exclude.map(e -> e.getMetadata().getName()).toJavaArray(String.class));

        objectMapper.addMixIn(clazz, jacksonFilter.getClass());
    }

    public String toJson(Object object) throws JsonProcessingException {
        objectMapper.setFilterProvider(jacksonFilter);
        return objectMapper.writeValueAsString(object);
    }

    public void filter(JsonConfigItem json) {
        Class<?> type = json.getType();
        Option<Class> dto = JsonConfig.getDto(type);
        List<Path> include = json.include;
        if (type == User.class) {
            Class extendedUser = Reflect.on(ApplicationProperties.myUserClass).get();
            include = include.appendAll(JsonConfig.firstLevel(extendedUser).map(MockPath::create));
        }
        if (dto.isDefined()) {
            List<Path> dtoFields = JsonConfig.firstLevel(dto.get()).map(MockPath::create);
            this.filter(dto.get(), include.appendAll(dtoFields), json.exclude);
            this.filter(json.type, include.appendAll(dtoFields), json.exclude);
        } else {
            this.filter(json.type, include, json.exclude);
        }

        if (type == User.class) {
            Class extendedUser = Reflect.on(ApplicationProperties.myUserClass).get();
            this.filter(extendedUser, include, json.exclude);
        }

    }
}