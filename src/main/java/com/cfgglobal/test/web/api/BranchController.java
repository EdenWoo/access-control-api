package com.cfgglobal.test.web.api;

import com.cfgglobal.test.base.JpaBeanUtil;
import com.cfgglobal.test.domain.Branch;
import com.cfgglobal.test.service.BranchService;
import com.cfgglobal.test.web.base.BaseController;
import io.vavr.collection.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/v1/branch")
@Slf4j
public class BranchController extends BaseController {


    @Autowired
    private BranchService branchService;

    @GetMapping
    public ResponseEntity page(Pageable pageable, HttpServletRequest request) {
        Page<Branch> page = branchService.findBySecurity(request.getMethod(), request.getRequestURI(), HashMap.ofAll(request.getParameterMap()), pageable);
        return ResponseEntity.ok(page);


    }

    @GetMapping("{id}")
    public ResponseEntity<Branch> get(@PathVariable Long id) {
        return ResponseEntity.ok(branchService.findOne(id));
    }

    @PostMapping
    public ResponseEntity<Branch> save(@RequestBody Branch branch) {
        return ResponseEntity.ok(branchService.save(branch));

    }


    @PutMapping("{id}")
    public ResponseEntity save(@PathVariable Long id, @RequestBody Branch branch) {
        Branch oldBranch = branchService.findOne(id);
        JpaBeanUtil.copyNonNullProperties(branch, oldBranch);
        return ResponseEntity.ok(branchService.save(oldBranch));
    }


    @DeleteMapping("delete")
    public ResponseEntity delete(@PathVariable Long id, HttpServletRequest request) {
        branchService.deleteBySecurity(id, request.getMethod(), request.getRequestURI());
        return ResponseEntity.noContent().build();
    }


}