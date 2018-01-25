package com.cfgglobal.test.web.api;

import com.cfgglobal.test.domain.VisitRecord;
import com.cfgglobal.test.service.VisitRecordService;
import com.cfgglobal.test.web.base.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/v1/visit-record")
public class VisitRecordController extends BaseController {

    @Autowired
    private VisitRecordService visitRecordService;

    @GetMapping
    public ResponseEntity<Page<VisitRecord>> index(Pageable pageable) {
        return ResponseEntity.ok(visitRecordService.findAll(pageable));
    }
}