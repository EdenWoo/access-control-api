package com.cfgglobal.test.web.api;

import com.cfgglobal.test.base.FreemarkerBuilderUtil;
import com.cfgglobal.test.service.PermissionService;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
@RequestMapping("/v1/code")
public class CodeController {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private FreemarkerBuilderUtil freemarkerBuilderUtil;

    @GetMapping(value = "/permission_constant",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<String> init(ModelMap modelMap) throws IOException {
        List<Map<String, String>> list = getPermissionMap();
        java.util.Map<String, Object> map = new java.util.HashMap<>();
        map.put("permissions", list.map(Map::toJavaMap).distinctBy(e -> e.get("key")).asJava());
        modelMap.putAll(map);
        return ResponseEntity.ok(freemarkerBuilderUtil.build("/code/permission.ftl", map));
    }


    @GetMapping(value = "/permission-constant_model_ts",produces = "text/html;charset=UTF-8")
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
}