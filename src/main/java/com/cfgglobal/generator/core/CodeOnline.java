package com.cfgglobal.generator.core;


import com.cfgglobal.generator.entity.CodeEntity;
import com.cfgglobal.generator.entity.CodeField;
import com.cfgglobal.generator.entity.CodeProject;
import com.cfgglobal.generator.entity.Task;
import com.cfgglobal.generator.ext.Utils;
import com.cfgglobal.generator.metadata.FieldFeature;
import com.cfgglobal.generator.script.DefaultScriptHelper;
import com.cfgglobal.generator.task.service.TaskService;
import com.cfgglobal.generator.template.FreeMarkerHelper;
import com.cfgglobal.test.base.ClassSearcher;
import com.cfgglobal.test.base.PathKit;
import com.cfgglobal.test.domain.BaseEntity;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.File;
import java.lang.annotation.Annotation;
import java.util.List;

@Slf4j
public class CodeOnline {

    public static void main(String[] args) {
        //CaseFormat.LOWER_CAMEL.converterTo(CaseFormat.LOWER_HYPHEN).convert()
        CodeProject codeProject = new CodeProject();
        List<Class> classes = Lists.newArrayList();
        //classes.add(TaskService)
        classes.add(Utils.class);
        codeProject.setUtilClasses(classes);

        codeProject.setPackageName("com.cfgglobal.test");

        codeProject.setTemplatePath(new File(PathKit.getRootClassPath()).getParent() + "/resources/templates");

        codeProject.setTargetPath(PathKit.getRootClassPath() + "/target");

        codeProject.setScriptHelper(new DefaultScriptHelper("groovy"));

        codeProject.setTemplateEngine(new FreeMarkerHelper(codeProject.getTemplatePath()));

        List<Class<?>> entities = ClassSearcher.of(BaseEntity.class)
                //.libDir("/Users/leon/IdeaProjects/collinson-backend/collinson-base/build/libs")
                //.includeAllJarsInLib(true)
                .search();
        List<CodeEntity> codeEntities = io.vavr.collection.List.ofAll(entities).map(e -> {
            CodeEntity codeEntity = new CodeEntity();
            codeEntity.setName(e.getSimpleName());
            List<CodeField> fields = io.vavr.collection.List.of(e.getDeclaredFields())
                    .map(f -> {
                        CodeField codeField = new CodeField();
                        codeField.setName(f.getName());
                        Class<?> type = f.getType();
                        if (List.class.isAssignableFrom(type)) {
                            codeField.setType("List");
                        } else if (BaseEntity.class.isAssignableFrom(type)) {
                            codeField.setType("Entity");
                        } else {
                            codeField.setType(type.getSimpleName());
                        }
                        Annotation[] annotations = f.getDeclaredAnnotations();
                        io.vavr.collection.List.of(annotations)
                                .forEach(annotation -> {
                                    if (annotation instanceof Column) {
                                        Column column = (Column) annotation;
                                        codeField.setUnique(column.unique());
                                        codeField.setLength(column.length());
                                        codeField.setRequired(!column.nullable());
                                        codeField.setScale(column.scale());
                                    } else if (annotation instanceof FieldFeature) {
                                        FieldFeature fieldFeature = (FieldFeature) annotation;
                                        codeField.setSearchable(fieldFeature.searchable());
                                        codeField.setSortable(fieldFeature.sortable());
                                    } else if (annotation instanceof Id) {
                                        codeField.setPrimaryKey(true);
                                    } else {

                                    }
                                });
                        return codeField;
                    }).toJavaList();
            codeEntity.setFields(fields);
            return codeEntity;
        }).asJavaMutable();

        codeProject.setEntities(codeEntities);
        List<Task> tasks = Lists.newArrayList();

        Task daoTask = new Task();
        daoTask.setName("DAO");
        daoTask.setFolder("\"src/main/java/\"+project.packageName.replaceAll(\"\\\\.\",\"/\")+\"/\"+\"dao\"");
        daoTask.setTaskType("multiple");
        daoTask.setFilename("entity.name+\"Dao.java\"");
        daoTask.setTemplatePath("\"java/dao.ftl\"");
        tasks.add(daoTask);

        Task serviceTask = new Task();
        serviceTask.setName("SERVICE");
        serviceTask.setFolder("\"src/main/java/\"+project.packageName.replaceAll(\"\\\\.\",\"/\")+\"/\"+\"service\"");
        serviceTask.setTaskType("multiple");
        serviceTask.setFilename("entity.name+\"Service.java\"");
        serviceTask.setTemplatePath("\"java/service.ftl\"");
        tasks.add(serviceTask);

//        Task testTask = new Task();
//        testTask.setName("TEST");
//        testTask.setFolder("\"angular\"");
//        testTask.setTaskType("multiple");
//        testTask.setFilename("entity.name+\"Test.ts\"");
//        testTask.setTemplatePath("\"angular/test.ftl\"");
//        tasks.add(testTask);

        // entity-form.component.ts
        Task entityFormComponentTs = new Task();
        entityFormComponentTs.setName("entity-form.component.ts");
        entityFormComponentTs.setFolder("\"angular/\"+  com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name) + \"/\" +  com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name) + \"-form\" ");
        entityFormComponentTs.setTaskType("multiple");
        entityFormComponentTs.setFilename("com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name) + \"-form.component.ts \" ");
        entityFormComponentTs.setTemplatePath("\"angular/entity-form/entity-form_component_ts.ftl\"");
        tasks.add(entityFormComponentTs);

        // entity-form.component.html
        Task entityFormComponentHtml = new Task();
        entityFormComponentHtml.setName("entity-form.component.html");
        entityFormComponentHtml.setFolder("\"angular/\"+  com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name) + \"/\" +  com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name) + \"-form\" ");
        entityFormComponentHtml.setTaskType("multiple");
        entityFormComponentHtml.setFilename("com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name) + \"-form.component.html \" ");
        entityFormComponentHtml.setTemplatePath("\"angular/entity-form/entity-form_component_html.ftl\"");
        tasks.add(entityFormComponentHtml);


        // entity-list.component.ts
        Task entityListComponentTs = new Task();
        entityListComponentTs.setName("entity-list.component.ts");
        entityListComponentTs.setFolder("\"angular/\"+  com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name) + \"/\" +  com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name) + \"-list\" ");
        entityListComponentTs.setTaskType("multiple");
        entityListComponentTs.setFilename("com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name) + \"-list.component.ts \" ");
        entityListComponentTs.setTemplatePath("\"angular/entity-list/entity-list_component_ts.ftl\"");
        tasks.add(entityListComponentTs);

        // entity-list.component.html
        Task entityListComponentHtml = new Task();
        entityListComponentHtml.setName("entity-list.component.html");
        entityListComponentHtml.setFolder("\"angular/\"+  com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name) + \"/\" +  com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name) + \"-list\" ");
        entityListComponentHtml.setTaskType("multiple");
        entityListComponentHtml.setFilename("com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name) + \"-list.component.html \" ");
        entityListComponentHtml.setTemplatePath("\"angular/entity-list/entity-list_component_html.ftl\"");
        tasks.add(entityListComponentHtml);

        //service.html
        Task entityService = new Task();
        entityService.setName("entity-service.component.html");
        entityService.setFolder("\"angular/\"+  com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name) ");
        entityService.setTaskType("multiple");
        entityService.setFilename("com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name) + \".service.ts \" ");
        entityService.setTemplatePath("\"angular/entity_service_ts.ftl\"");
        tasks.add(entityService);

        //module.html
        Task entityModule = new Task();
        entityModule.setName("entity-module.component.html");
        entityModule.setFolder("\"angular/\"+  com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name) ");
        entityModule.setTaskType("multiple");
        entityModule.setFilename("com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name) + \".module.ts \" ");
        entityModule.setTemplatePath("\"angular/entity_module_ts.ftl\"");
        tasks.add(entityModule);

        //model.html
        Task entityModel = new Task();
        entityModel.setName("entity-model.component.html");
        entityModel.setFolder("\"angular/\"+  com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name) ");
        entityModel.setTaskType("multiple");
        entityModel.setFilename("com.cfgglobal.generator.ext.Utils.lowerHyphen(entity.name) + \".model.ts \" ");
        entityModel.setTemplatePath("\"angular/entity_model_ts.ftl\"");
        tasks.add(entityModel);


        //pages_routing.ts
        Task pageRouting = new Task();
        pageRouting.setName("pages.routing.ts");
        pageRouting.setFolder("\"angular\"");
        pageRouting.setTaskType("multiple");
        pageRouting.setFilename("pages.routing.ts");
        pageRouting.setTemplatePath("\"angular/pages_routing_ts.ftl\"");
        tasks.add(pageRouting);


        for (Task t : tasks) {
            TaskService.processTask(codeProject, t);
        }
    }


}
