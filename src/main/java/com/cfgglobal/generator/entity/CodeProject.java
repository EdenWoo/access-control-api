package com.cfgglobal.generator.entity;

import com.cfgglobal.generator.core.ScriptHelper;
import com.cfgglobal.generator.core.TemplateHelper;
import lombok.Data;

import java.io.File;
import java.util.List;

@Data
public class CodeProject {
    private static final String FS = File.separator;

    String packageName;

    String templatePath;

    String targetPath;

    ScriptHelper scriptHelper;

    TemplateHelper templateEngine;

    List<CodeEntity> entities;

    List<Class> utilClasses;


}
