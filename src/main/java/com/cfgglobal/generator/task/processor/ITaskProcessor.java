package com.cfgglobal.generator.task.processor;


import com.cfgglobal.generator.entity.CodeProject;
import com.cfgglobal.generator.entity.Task;

import java.util.List;
import java.util.Map;

public interface ITaskProcessor {
    List<String> run(CodeProject codeProject, Task task, Map<String, Object> context);
}
