package com.cfgglobal.test.web.api;

import com.cfgglobal.test.domain.EmailLog;
import com.cfgglobal.test.enums.TaskStatus;
import com.cfgglobal.test.service.EmailLogService;
import com.cfgglobal.test.web.api.vo.EmailLogMapper;
import com.cfgglobal.test.web.api.vo.EmailLogVo;
import com.cfgglobal.test.web.base.BaseController;
import io.vavr.collection.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/v1/email-log")
public class EmailLogController extends BaseController {


    @Autowired
    private EmailLogService emailLogService;

    @GetMapping
    public ResponseEntity page(Pageable pageable, HttpServletRequest request) {
        HashMap<String, String[]> params = HashMap.ofAll(request.getParameterMap());
        return ResponseEntity.ok(emailLogService.findByRequestParameters(params, pageable));
    }


    @GetMapping("{id}")
    public ResponseEntity<EmailLogVo> get(@PathVariable Long id) {
        EmailLog email = emailLogService.findOne(id);
        EmailLogVo vo = EmailLogMapper.INSTANCE.toDto(email);
        vo.setHtml(new String(vo.getContent()));
        return ResponseEntity.ok(vo);
    }

    @PutMapping("{id}/resend")
    public ResponseEntity resend(String ids) {
        handle(ids, id -> {
            EmailLog emailLog = emailLogService.findOne(id);
            emailLog.setStatus(TaskStatus.TODO);
            emailLogService.save(emailLog);
        });


        return ResponseEntity.ok(ids);
    }

}