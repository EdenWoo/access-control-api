package com.cfgglobal.generator.task.processor;

import com.cfgglobal.generator.entity.CodeEntity;
import com.cfgglobal.generator.entity.CodeProject;
import com.cfgglobal.generator.entity.Task;
import com.cfgglobal.generator.task.service.TaskService;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

public class MultipleTaskProcessor implements ITaskProcessor {
    @Override
    public List<String> run(CodeProject codeProject, Task task, Map<String, Object> context) {
        List<String> paths = Lists.newArrayList();
        for (CodeEntity codeEntity : codeProject.getEntities()) {
            context.put("entity", codeEntity);
            codeProject.getTemplateEngine().put("entity", codeEntity);
            paths.add(TaskService.processTemplate(codeProject, task, context));
        }
        return paths;

    }
}
