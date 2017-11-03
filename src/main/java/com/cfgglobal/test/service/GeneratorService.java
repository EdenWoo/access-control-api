package com.cfgglobal.test.service;

import com.cfgglobal.test.dao.RuleDao;
import com.cfgglobal.test.domain.BaseEntity;
import com.cfgglobal.test.domain.Permission;
import com.cfgglobal.test.domain.RolePermission;
import com.cfgglobal.test.domain.Rule;
import com.google.common.collect.Lists;
import io.vavr.API;
import io.vavr.Tuple;
import io.vavr.Tuple3;
import io.vavr.collection.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GeneratorService {

    public static final String DEFAULT_RULE_NAME = "admin";
    public static final String DIGIT = "[\\d]+";
    @Autowired
    private RuleDao ruleDao;

    public List<Permission> genPermission(BaseEntity entity) {
        String regex = "([a-z])([A-Z]+)";
        String replacement = "$1-$2";
        String name = entity.getClass().getSimpleName().replaceAll(regex, replacement).toLowerCase();
        String endPoint = "/" + name.toLowerCase();

        List<Tuple3<String, String, String>> list = API.List(
                Tuple.of("Index " + name, "GET", "/v" + DIGIT + endPoint),
                Tuple.of("Create " + name, "POST", "/v" + DIGIT + endPoint),
                Tuple.of("Read " + name, "GET", "/v" + DIGIT + endPoint + "/" + DIGIT),
                Tuple.of("Update " + name, "PUT", "/v" + DIGIT + endPoint + "/" + DIGIT),
                Tuple.of("Update " + name, "PATCH", "/v" + DIGIT + endPoint + "/" + DIGIT),
                Tuple.of("Delete " + name, "DELETE", "/v" + DIGIT + endPoint + "/" + DIGIT));

        return list.map(e -> new Permission().setAuthKey(e._1)
                .setEntity(name)
                .setAuthKey(e._1)
                .setMenuUrl(e._1)
                .setHttpMethod(e._2)
                .setAuthUris(e._3));
    }

    public List<RolePermission> assignPermission(List<Permission> permissions, String permissionRule) {
        Rule rule = ruleDao.findByName(permissionRule)
                .getOrElseThrow(() -> new IllegalArgumentException(DEFAULT_RULE_NAME));
        return permissions.map(permission -> new RolePermission().setPermission(permission).setRules(Lists.newArrayList(rule)));
    }

}
