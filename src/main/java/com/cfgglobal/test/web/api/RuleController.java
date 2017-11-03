package com.cfgglobal.test.web.api;

import com.cfgglobal.test.base.JpaBeanUtil;
import com.cfgglobal.test.domain.Rule;
import com.cfgglobal.test.service.rule.RuleService;
import com.cfgglobal.test.web.base.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

@Slf4j
@RestController
@RequestMapping(value = "/v1/rule")
public class RuleController extends BaseController {

    @Autowired
    private RuleService ruleService;

    @GetMapping
    public ResponseEntity<Page<Rule>> index(Pageable pageable) {
        return ResponseEntity.ok(ruleService.findAll(pageable));
        //return ResponseEntity.ok(ruleService.findBySecurity(request.getMethod(), request.getRequestURI(), HashMap.ofAll(request.getParameterMap()), pageable));

    }


    @GetMapping("{id}")
    public ResponseEntity<Rule> get(@PathVariable Long id) {
        return ResponseEntity.ok(ruleService.findOne(id));
    }

    @PostMapping
    public ResponseEntity<Rule> save(@RequestBody Rule rule, HttpServletRequest request) {
        return ResponseEntity.created(URI.create(request.getRequestURI())).body(rule);
    }


    @PutMapping("{id}")
    public ResponseEntity<Rule> save(@PathVariable Long id, @RequestBody Rule rule) {
        Rule oldRule = ruleService.findOne(id);
        JpaBeanUtil.copyNonNullProperties(rule, oldRule);
        ruleService.save(oldRule);
        return ResponseEntity.ok(oldRule);
    }


    @DeleteMapping
    public ResponseEntity delete(@PathVariable Long id, HttpServletRequest request) {
        ruleService.deleteBySecurity(id, request.getMethod(), request.getRequestURI());
        return ResponseEntity.ok().build();
    }

}