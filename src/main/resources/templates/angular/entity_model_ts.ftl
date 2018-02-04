export class ${Utils.upperCamel(entity.name)}Model {
<#list entity.fields as f>
    ${Utils.lowerCamel(f.name)}: string;
</#list>
id: number;
}