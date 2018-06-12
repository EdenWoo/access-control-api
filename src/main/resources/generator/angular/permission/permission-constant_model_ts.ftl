export class PermissionConstant {
<#list project.entities as e>
    INDEX_${Utils.upperUderscore(e.name)}: string;
    READ_${Utils.upperUderscore(e.name)}: string;
    CREATE_${Utils.upperUderscore(e.name)}: string;
    DELETE_${Utils.upperUderscore(e.name)}: string;
    UPDATE_${Utils.upperUderscore(e.name)}: string;
    EXPORT_${Utils.upperUderscore(e.name)}: string;
    IMPORT_${Utils.upperUderscore(e.name)}: string;
</#list>

}
