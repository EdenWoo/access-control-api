import {Injectable, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BaseService} from '../../shared-module/bases/base.service';
@Injectable()
export class ${Utils.upperCamel(entity.name)}Service extends BaseService implements OnInit {

    constructor(public http: HttpClient) {
        super('${Utils.lowerHyphen(entity.name)}', http);
<#assign embededStr = "">
    <#list entity.fields as f>
        <#if (f.addDynamicMany || f.attachment || f.type.name == "Entity" || f.type.name == "List") && !f.hiddenInList>
            <#assign embededStr += '${Utils.lowerHyphen(f.name)},'>
        </#if>

    </#list>
    <#if embededStr != "">
        this.embeddedStr = '&embedded=${embededStr}';
    </#if>
    }

    ngOnInit(): void {
    }

}