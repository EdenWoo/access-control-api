package com.cfgglobal.test.service.rule;

import com.cfgglobal.test.domain.Rule;
import com.cfgglobal.test.service.base.BaseService;
import com.cfgglobal.test.service.rule.access.AccessRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;


@Service
@Slf4j
public class RuleService extends BaseService<Rule, Long> {
    @Autowired
    private List<AccessRule> accessRules;

    public AccessRule findAccessRules(String ruleName) {
        log.debug("ruleName {}", ruleName);
        return io.vavr.collection.List.ofAll(accessRules)
                .filter(accessRule -> accessRule.getRuleName().equals(ruleName))
                .getOrElseThrow(() -> new IllegalArgumentException(MessageFormat.format(" rule [{0}] not found, " +
                        "avaliable rules {1}", ruleName, findAll().map(Rule::getName))));
    }
}
