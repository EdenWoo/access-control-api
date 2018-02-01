export class ${Utils.upperCamel(entity.name)}Model {
<#list entity.fields as f>
    ${Utils.lowerCamel(f.entity.name)}: {{member.typescript_type}};
</#list>
}