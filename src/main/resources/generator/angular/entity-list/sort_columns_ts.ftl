import {Sort} from '../../../models/bases/sort.model';

export class SortColumns {
    public static Columns = [
<#list entity.listFields as f>
    <#if f.hiddenInList>

    <#elseif false>

    <#else>
        new Sort({
            isAsc: false,
            columnDisplay: '${Utils.spacedCapital(f.name)}',
            columnModel: '${Utils.lowerCamel(f.name)}',
            isSortable: true,
            isActive: false
        }),
    </#if>
</#list>
    ];
}
