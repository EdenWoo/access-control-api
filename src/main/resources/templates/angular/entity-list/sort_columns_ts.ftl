import {Sort} from '../../../models/bases/sort.model';

export class SortColumns {
    public static Columns = [

<#list entity.fields as f>
        new Sort({
            isAsc: false,
            columnDisplay: '${Utils.upperCamel(f.name)}',
            columnModel: '${Utils.lowerCamel(f.name)}',
            isSortable: true,
            isActive: true
        }),
</#list>
    ];
}
