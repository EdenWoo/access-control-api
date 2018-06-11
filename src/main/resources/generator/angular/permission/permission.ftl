import {PermissionConstant} from '../models/admin/permission-constant.model'; </br>

export class PermissionConstants {
    public static constants: PermissionConstant = {
     <#list permissions as p>
        ${p.key}: '${p.value}',
     </#list>

    }
}

