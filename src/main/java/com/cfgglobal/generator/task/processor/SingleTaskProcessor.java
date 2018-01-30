package com.cfgglobal.generator.task.processor;

import com.cfgglobal.generator.entity.CodeProject;
import com.cfgglobal.generator.entity.Task;
import com.cfgglobal.generator.task.service.TaskService;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

public class SingleTaskProcessor implements ITaskProcessor {
    @Override
    public List<String> run(CodeProject codeProject, Task task, Map<String, Object> context) {
        return Lists.newArrayList(TaskService.processTemplate(codeProject, task, context));

    }
}
