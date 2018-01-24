package com.cfgglobal.test.config.json;

import com.cfgglobal.test.config.app.ApplicationProperties;
import com.cfgglobal.test.domain.BaseEntity;
import com.cfgglobal.test.domain.User;
import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.ListPath;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Traversable;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joor.Reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.MessageFormat;
import java.util.stream.Collectors;

import static com.google.common.base.CaseFormat.*;
import static io.vavr.API.*;
import static io.vavr.Predicates.isIn;


@Data
@Slf4j
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
        String endpoint = JsonConfig.getRootEndpoint(uri);

        Option<Tuple2<Class, EntityPathBase>> rootElement = endpoints(endpoint);
        if (rootElement.isEmpty()) {
            return Option.none();
        }
        JsonConfig jsonConfig = JsonConfig.start();
        Map<Class, List<Path>> fieldsInRequest = getFields(rootElement.get()._1, fields);
        jsonConfig.include(rootElement.get()._1, fieldsInRequest.getOrElse(rootElement.get()._1, JsonConfig.firstLevel(rootElement.get()._2, "AUDITING".equals(fields))));


        if (!embeddedEntity.isEmpty()) {
            checkEmbedded(embeddedEntity);
            embeddedEntity.sortBy(Traversable::size)
                    .forEach(e -> {
                        System.out.println("embedded " + e);
                        if (e.size() < 2) {
                            // rootEntity embeddedNode:String
                            String embeddedNode = e.head();
                            addEmbedded(jsonConfig, fieldsInRequest, rootElement, embeddedNode);
                        } else {
                            System.out.println("nested nodes");
                            //去除最后2级，倒数第二级已经在之前的循环中加入了第一层，只需要把嵌套节点追加进去，最后一层add first level
                            String lastNode = e.last();
                            System.out.printf(lastNode);
                            String lastParentNode = e.dropRight(1).last();
                            System.out.println(lastParentNode);

                            Option<Tuple2<Class, EntityPathBase>> parentElement = endpoints(lastParentNode);
                            if (parentElement.isEmpty() && lastParentNode.endsWith("s")) {
                                parentElement = endpoints(lastParentNode.substring(0, lastParentNode.length() - 1));
                            }
                            addEmbedded(jsonConfig, fieldsInRequest, parentElement, lastNode);
                        }


                    });
        }
        return Option.of(jsonConfig);
    }

    private static void addEmbedded(JsonConfig jsonConfig, Map<Class, List<Path>> fieldsInRequest, Option<Tuple2<Class, EntityPathBase>> rootElement, String embeddedNode) {
        EntityPath rootEntity = rootElement.get()._2;
        Path embeddedEntityPath = Try.of(() -> (Path) Reflect.on(rootEntity).get(LOWER_HYPHEN.to(LOWER_CAMEL, embeddedNode))).getOrElse(() -> {
            if (rootEntity.getType() == User.class) {//Special process for User class
                Class userClazz = Reflect.on(ApplicationProperties.myUserClass).get();
                return Reflect.on(JsonConfig.toQ(userClazz)).get(LOWER_HYPHEN.to(LOWER_CAMEL, embeddedNode));
            } else {
                return null;
            }
        });

        if (embeddedEntityPath == null) {
            throw new IllegalArgumentException(MessageFormat.format("Invalid embedded [{0}],does not exist on entity [{1}],avaliable embedded [{2}]." +
                    "Metadata is based on QueryDSL's Q object, not javabean. " +
                    "Run `gradle clean build` to generate QueryDSL Q Object.", embeddedNode, rootEntity.toString(), getAvailableEmbeddedPaths(rootEntity)
                    .map(e->LOWER_CAMEL.converterTo(LOWER_HYPHEN).convert(e)).mkString(",")));
        }
        jsonConfig.include(rootElement.get()._1, embeddedEntityPath); //追加？

        Option<Tuple2<Class, EntityPathBase>> nextEntity = endpoints(getNext(embeddedEntityPath));
        jsonConfig.include(nextEntity.get()._1, fieldsInRequest.getOrElse(nextEntity.get()._1, JsonConfig.firstLevel(nextEntity.get()._2, false)));
    }

    private static List<String> getAvailableEmbeddedPaths(EntityPath rootEntity) {
        return List.of(rootEntity.getClass().getFields())
                .filter(re -> ListPath.class.isAssignableFrom(re.getType()) || EntityPath.class.isAssignableFrom(re.getType()))
                .map(Field::getName);
    }


    private static String getNext(Path embeddedEntityPath) {
        String next;
        if (embeddedEntityPath instanceof ListPath) {
            next = ((Class) Reflect.on(embeddedEntityPath).get("elementType")).getSimpleName();
        } else {
            next = embeddedEntityPath.getType().getSimpleName();
        }

        next = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, next);
        return next;
    }

    private static void checkEmbedded(List<List<String>> embeddedEntity) {
        //payees,payees.bankaccounts, payees.attachments 父亲节点必须手动加入
        embeddedEntity.forEach(e -> {
            if (e.size() >= 2) {
                for (int i = 1; i < e.size(); i++) {
                    List<String> parentNode = e.subSequence(0, i);
                    if (!embeddedEntity.contains(parentNode)) {
                        throw new IllegalArgumentException(MessageFormat.
                                format("Invalid embedded [{1}].[{0}] should be added for embedded [{1}] ", parentNode.mkString("."), e.mkString(".")));
                    }
                }
            }
        });
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

    public static Map<Class, List<Path>> getFields(Class clazz, String fields) {

        if ("AUDITING".equals(fields)) {
            return Map();
        }
        List<HashMap<Class, List<Path>>> map = Option.of(fields)
                .map(e -> e.split(","))
                .map(List::of).getOrElse(List.empty())
                .map(e -> {
                    String[] results = e.split("\\.");
                    if (results.length == 1) {
                        String field = results[0];
                        return HashMap.of(clazz, List.of((Path) MockPath.create(field)));
                    } else if (results.length == 2) {
                        String className = results[0];
                        String field = results[1];
                        //Class nestedClass = Reflect.on(clazz).get(className);
                        Class nestedClass = Lists.newArrayList(clazz.getDeclaredFields()).stream().filter(e2 -> e2.getName().equals(className)).collect(Collectors.toList()).get(0).getType();
                        return HashMap.of(nestedClass, List.of((Path) MockPath.create(field)));
                    } else {
                        throw new IllegalArgumentException();
                    }
                });
        if (map.isEmpty()) {
            return Map();
        } else {
            return map.reduce((m1, m2) -> m1.merge(m2, List::appendAll));
        }

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
