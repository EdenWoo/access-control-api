<#setting classic_compatible=true>
export class ${Utils.upperCamel(entity.name)}Model {
<#assign containsId = false>
<#list entity.fields as f>
    <#if f.name?contains('id') || f.name?contains('Id')>
        <#assign containsId = true>
    </#if>

    <#if f.type.name == "Boolean" || f.type.name == "boolean">
        ${Utils.lowerCamel(f.name)} = false;
    <#else>
        ${Utils.lowerCamel(f.name)}: any;
    </#if>

</#list>
<#if containsId ==  false>
   id: string | any;
</#if>
}