import {PermissionConstant} from '../models/admin/permission-constant.model'; </br>

export class PermissionConstants {</br>
    public static constants: PermissionConstant = {</br>
     <#list permissions as p>
        ${p.key}: '${p.value}', </br>
     </#list>

    }
}

