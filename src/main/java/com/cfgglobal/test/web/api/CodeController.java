package com.cfgglobal.test.web.api;

import com.cfgglobal.test.base.FreemarkerBuilderUtil;
import com.cfgglobal.test.config.app.ApplicationProperties;
import com.cfgglobal.test.service.PermissionService;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import org.joor.Reflect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
@RequestMapping("/v1/code")
@EnableConfigurationProperties(ApplicationProperties.class)

public class CodeController {


    @Autowired
    ApplicationProperties applicationProperties;
    @Autowired
    private PermissionService permissionService;

    @Autowired
    private FreemarkerBuilderUtil freemarkerBuilderUtil;

    @GetMapping(value = "/permission_constant", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<String> init(ModelMap modelMap) throws IOException {
        List<Map<String, String>> list = getPermissionMap();
        java.util.Map<String, Object> map = new java.util.HashMap<>();
        map.put("permissions", list.map(Map::toJavaMap).distinctBy(e -> e.get("key")).asJava());
        modelMap.putAll(map);
        return ResponseEntity.ok(freemarkerBuilderUtil.build("/code/permission.ftl", map));
    }


    @GetMapping(value = "/permission-constant_model_ts", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<String> model(ModelMap modelMap) throws IOException {


        List<Map<String, String>> list = getPermissionMap();
        java.util.Map<String, Object> map = new java.util.HashMap<>();
        map.put("permissions", list.map(Map::toJavaMap).distinctBy(e -> e.get("key")).asJava());
        modelMap.putAll(map);
        return ResponseEntity.ok(freemarkerBuilderUtil.build("/code/permission-constant_model_ts.ftl", map));
    }

    private List<Map<String, String>> getPermissionMap() {
        return permissionService.findAll()
                .map(e ->
                        HashMap.of("key", List.of(e.getAuthKey().split(" ")).mkString("_").toUpperCase().replace("-", "_"),
                                "value", e.getAuthKey()));
    }


    @GetMapping(value = "/entity-model_ts", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<String> model(String entityName, ModelMap modelMap) throws IOException {
        modelMap = codeFetch(entityName, modelMap);

        return ResponseEntity.ok(freemarkerBuilderUtil.build("/code/entity-model_ts.ftl", modelMap));

    }

    @GetMapping(value = "/entity-list-component_ts", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<String> component(String entityName, ModelMap modelMap) throws IOException {
        modelMap = codeFetch(entityName, modelMap);

        return ResponseEntity.ok(freemarkerBuilderUtil.build("/code/entity-list-component_ts.ftl", modelMap));

    }

    @GetMapping(value = "/entity-form_ts", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<String> form(String entityName, ModelMap modelMap) throws IOException {

        modelMap = codeFetch(entityName, modelMap);
        return ResponseEntity.ok(freemarkerBuilderUtil.build("/code/entity-form_ts.ftl", modelMap));

    }

    @GetMapping(value = "/entity-form-component_ts", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<String> formComponent(String entityName, ModelMap modelMap) throws IOException {

        modelMap = codeFetch(entityName, modelMap);
        return ResponseEntity.ok(freemarkerBuilderUtil.build("/code/entity-form-component_ts.ftl", modelMap));
    }

    @GetMapping(value = "/entity-list_ts", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<String> list(String entityName, ModelMap modelMap) throws IOException {

        modelMap = codeFetch(entityName, modelMap);
        return ResponseEntity.ok(freemarkerBuilderUtil.build("/code/entity-list_ts.ftl", modelMap).toString());

    }

    @GetMapping(value = "/entity-service_ts", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<String> entityService(String entityName, ModelMap modelMap) throws IOException {

        modelMap = codeFetch(entityName, modelMap);
        return ResponseEntity.ok(freemarkerBuilderUtil.build("/code/entity-service_ts.ftl", modelMap).toString());

    }

    private ModelMap codeFetch(String entityName, ModelMap modelMap) {
        if (entityName != null) {
            Class clazz = Reflect.on(applicationProperties.getUserClass()).get();
            String name = clazz.getPackage().getName() + "." + entityName;
            System.out.println("entity:  " + name);

            modelMap.put("entity", getReplace(name).toLowerCase());

            Reflect reflect = null;
            try {
                reflect = Reflect.on(name);
            } catch (Exception e) {
                reflect = Reflect.on("com.cfgglobal.ccfx.domain." + entityName);
            }
            Class entityClass = reflect.get();
            List fields = List.of(entityClass.getDeclaredFields())
                    .map(e -> HashMap.of(
                            "name", getReplace(e.getName()),
                            "type", getTypeReplace(e.getType().getSimpleName())
                    ).toJavaMap());
            modelMap.put("fields", fields.toJavaList());
            System.out.println(modelMap);
        }
        return modelMap;
    }


    private String getReplace(String e) {
        Class clazz = Reflect.on(applicationProperties.getUserClass()).get();
        return e.replace(clazz.getPackage().getName() + ".", "").replace("com.cfgglobal.ccfx.domain.", "");
    }

    private String getTypeReplace(String e) {
        return e.replace("LocalDate", "number")
                .replace("String", "string")
                .replace("int", "number")
                .replace("Integer", "number")
                .replace("ZonedDateTime", "string")
                .replace("byte", "number")
                .replace("long", "number")
                .replace("Boolean", "boolean");
    }

}