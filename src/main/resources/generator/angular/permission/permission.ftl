import {PermissionConstant} from '../models/bases/permission-constant.model';

export class PermissionConstants {
    public static constants: PermissionConstant = {
    <#list project.entities as e>
        INDEX_${Utils.upperUderscore(e.name)}: 'Index ${Utils.lowerHyphen(e.name)}',
        READ_${Utils.upperUderscore(e.name)}: 'Read ${Utils.lowerHyphen(e.name)}',
        CREATE_${Utils.upperUderscore(e.name)}: 'Create ${Utils.lowerHyphen(e.name)}',
        DELETE_${Utils.upperUderscore(e.name)}: 'Delete ${Utils.lowerHyphen(e.name)}',
        UPDATE_${Utils.upperUderscore(e.name)}: 'Update ${Utils.lowerHyphen(e.name)}',
        EXPORT_${Utils.upperUderscore(e.name)}: 'Export ${Utils.lowerHyphen(e.name)}',
        IMPORT_${Utils.upperUderscore(e.name)}: 'Import ${Utils.lowerHyphen(e.name)}',
    </#list>
    }
}

