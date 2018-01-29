package com.cfgglobal.generator.entity;


import com.cfgglobal.generator.task.processor.ITaskProcessor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joor.Reflect;

import java.util.List;
import java.util.Map;

@Data
@Slf4j
public class Task {
    private Integer id;
    private String name;
    private String taskType;
    private String folder;
    private String filename;
    private String templatePath;

    public List<String> run(CodeProject codeProject, Map<String, Object> root) {
        log.debug("task " + this.getName() + " run");
        List<String> paths = taskProcessor(this.getTaskType()).run(codeProject, this, root);
        log.debug("task " + this.getName() + " end");
        return paths;
    }

    private ITaskProcessor taskProcessor(String taskType) {
        return Reflect.on(ITaskProcessor.class.getPackage().getName() + "."
                + StringUtils.capitalize(taskType) +
                "TaskProcessor").create().get();
    }
}
