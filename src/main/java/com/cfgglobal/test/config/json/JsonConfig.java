package com.cfgglobal.test.config.json;

import com.cfgglobal.test.config.app.ApplicationProperties;
import com.cfgglobal.test.domain.BaseEntity;
import com.cfgglobal.test.domain.User;
import com.google.common.base.CaseFormat;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.ListPath;
import io.vavr.API;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Traversable;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.joor.Reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static com.google.common.base.CaseFormat.*;
import static io.vavr.API.*;
import static io.vavr.Predicates.isIn;


@Data
public class JsonConfig {


    private static final ThreadLocal<JsonConfig> config = ThreadLocal.withInitial(() -> null);
    private List<JsonConfigItem> list = List.empty();

    public static JsonConfig start() {
        return new JsonConfig();
    }

    public static Option<JsonConfig> get() {
        return Option.of(config.get());
    }

    public static Option<Tuple2<Class, EntityPathBase>> endpoints(String endpoint) {
        return List.of(ApplicationProperties.entityScanPackage)
                .map(packageName -> Option.of(endpoint)
                        .map(e -> {
                            String name = LOWER_HYPHEN.to(UPPER_CAMEL, e);
                            return Tuple.of(packageName + "." + name, packageName + ".Q" + name);
                        })
                        .flatMap(e ->
                                Try.of(() -> e.map(
                                        e1 -> (Class) Reflect.on(e1).get(),
                                        e2 -> (EntityPathBase) Reflect.on(e2).create(StringUtils.substringAfterLast(e2, ".Q")
                                                .toLowerCase()).get())).toOption())).filter(Option::isDefined).toOption()
                .getOrElse(Option.none());


    }

    public static List<Path> firstLevel(EntityPathBase role, boolean audit) {

        List<Path> paths = List.of(role.getClass().getDeclaredFields())
                .removeAll(e -> Modifier.isPrivate(e.getModifiers()))
                .removeAll(e -> ListPath.class.isAssignableFrom(e.getType()))
                .removeAll(e -> EntityPath.class.isAssignableFrom(e.getType()))
                .map(e -> Try.of(() -> (Path) e.get(role)).get());
        if (!audit) {
            paths = paths
                    .removeAll(e -> e.getMetadata().getName().equals("createdAt"))
                    .removeAll(e -> e.getMetadata().getName().equals("updatedAt"));
        }
        return paths;
    }

    public static List<String> firstLevel(Class clazz) {

        return List.of(clazz.getDeclaredFields())
                .removeAll(e -> java.util.List.class.isAssignableFrom(e.getType()))
                .removeAll(e -> BaseEntity.class.isAssignableFrom(e.getType()))
                .map(Field::getName);
    }

    public static Option<JsonConfig> create(String uri, String fields, String embedded) {
        List<List<String>> embeddedEntity = Option.of(embedded)
                .map(e -> e.split(","))
                .map(List::of)
                .getOrElse(List.empty())
                .filter(StringUtils::isNotBlank)
                .map(e -> e.split("\\."))
                .map(List::of)
                .removeAll(List::isEmpty);
        // System.out.println("embedded" + embeddedEntity);
        String endpoint = JsonConfig.getRootEndpoint(uri);
        // System.out.println("endpoint " + endpoint);
        if (embeddedEntity.isEmpty()) {
            Option<Tuple2<Class, EntityPathBase>> tuple2 = endpoints(endpoint);
            if (tuple2.isEmpty()) {
                return Option.none();
            }
            JsonConfig jsonConfig = JsonConfig.start();
            Tuple2<Class, EntityPathBase> tuple = tuple2.get();

            //TODO field
            // getFields(tuple._1,fields);
            jsonConfig.include(tuple._1, JsonConfig.firstLevel(tuple._2, "AUDITING".equals(fields)));

            return Option.of(jsonConfig);
        } else {
            // System.out.println(embeddedEntity);
            Option<Tuple2<Class, EntityPathBase>> rootElement = endpoints(endpoint);
            if (rootElement.isEmpty()) {
                return Option.none();
            }
            EntityPath rootEntity = rootElement.get()._2;
            JsonConfig jsonConfig = JsonConfig.start();

            jsonConfig.include(rootElement.get()._1, JsonConfig.firstLevel(rootElement.get()._2, true));


            embeddedEntity.forEach(e -> {
                Tuple2<String, List<String>> pop = e.pop2();

                Path embbededEntityPath = null;
                try {
                    embbededEntityPath = Reflect.on(rootEntity).get(LOWER_HYPHEN.to(LOWER_CAMEL, pop._1));
                } catch (Exception e2) {
                    if (rootEntity.getType() == User.class) {
                        //TODO fix userClass injection
                        Class userClazz = Reflect.on(ApplicationProperties.myUserClass).get();
                        embbededEntityPath = Reflect.on(JsonConfig.toQ(userClazz)).get(LOWER_HYPHEN.to(LOWER_CAMEL, pop._1));


                    }
                }
                jsonConfig.include(rootElement.get()._1, embbededEntityPath);
                String next = embbededEntityPath.getType().getSimpleName();
                if (embbededEntityPath instanceof ListPath) {
                    next = ((Class) Reflect.on(embbededEntityPath).get("elementType")).getSimpleName();
                    //next = next.substring(0, next.length() - 1);
                }

                next = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, next);
                Option<Tuple2<Class, EntityPathBase>> nextEntity = endpoints(next);
                jsonConfig.include(nextEntity.get()._1, JsonConfig.firstLevel(nextEntity.get()._2, true));


            });
            return Option.of(jsonConfig);
        }
    }

    public static Path toQ(Class clazz) {
        return List.of(ApplicationProperties.entityScanPackage)
                .map(packageName -> {
                    String name = packageName + ".Q" + clazz.getSimpleName();
                    return Try.of(() -> (EntityPathBase) Reflect.on(name.replaceAll("////", ".")).create(StringUtils.substringAfterLast(name, ".Q")).get());

                }).filter(Try::isSuccess).head().get();


    }

    public static Option<Class> getDto(Class aClass) {
        String name = "com.cfgglobal.test.web.api.vo." + aClass.getSimpleName() + "Dto";
        return Try.of(() -> (Class) Reflect.on(name).get()).toOption();
    }

    public static String getRootEndpoint(String uri) {
        uri = uri.replace("/v1/", "");
        return Match(StringUtils.countMatches(uri, "/")).of(
                Case($(isIn(0)), uri),
                Case($(isIn(1)), StringUtils.substringBefore(uri, "/")),
                Case($(isIn(2)), StringUtils.substringAfterLast(uri, "/")),
                Case($(isIn(3)), StringUtils.substringAfterLast(uri, "/"))
        );


    }

    public static List<Path> getFields(Class clazz, String fields) {

      /* List.of(fields.split(","))
        .map(e->{
            String[] results = e.split("\\.");
           // if(results.length)
        });*/

        return API.TODO();
        //name,article.title,article.id,comment.id,comment.user,user.id,user.name
    }


    //FIXME not thread-safe
    public JsonConfig compose(JsonConfig config) {
        list = list.appendAll(config.getList());
        return this;
    }

    public JsonConfig include(Class<?> type, Path... include) {
        Option<JsonConfigItem> jsonConfigItems = list.filter(e -> e.getType() == type).toOption();
        if (jsonConfigItems.isEmpty()) {
            list = list.append(new JsonConfigItem().setType(type).setInclude(List.of(include)));
        } else {
            JsonConfigItem element = jsonConfigItems.get();
            element.setInclude(element.getInclude().appendAll(List.of(include)));
        }
        return this;
    }

    public JsonConfig include(Class<?> type, List<Path> include) {
        list = list.append(new JsonConfigItem().setType(type).setInclude(include));
        return this;
    }

    public JsonConfig include(Class<?> type, List<Path> include, Path... include2) {
        list = list.append(new JsonConfigItem().setType(type).setInclude(include.appendAll(List.of(include2))));
        return this;
    }

    public JsonConfig include(Class<?> type, List<Path>... include) {
        list = list.append(new JsonConfigItem().setType(type).setInclude(List.of(include).flatMap(Traversable::iterator)));
        return this;
    }

    public JsonConfig exclude(Class<?> type, Path... exclude) {
        list = list.append(new JsonConfigItem().setType(type).setExclude(List.of(exclude)));
        return this;
    }

    public JsonConfig exclude(Class<?> type, List<Path> exclude) {
        list = list.append(new JsonConfigItem().setType(type).setExclude(exclude));
        return this;
    }

    public JsonConfig exclude(Class<?> type, List<Path> exclude, Path... exclude2) {
        list = list.append(new JsonConfigItem().setType(type).setExclude(exclude.appendAll(List.of(exclude2))));
        return this;
    }

    public JsonConfig exclude(Class<?> type, List<Path>... exclude) {
        list = list.append(new JsonConfigItem().setType(type).setExclude(List.of(exclude).flatMap(Traversable::iterator)));
        return this;
    }

    public void end() {
        config.set(this);
    }
}
