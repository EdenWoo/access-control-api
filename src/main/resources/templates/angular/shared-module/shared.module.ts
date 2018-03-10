import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {NgxPaginationModule} from 'ngx-pagination';
import {DropzoneModule} from 'ngx-dropzone-wrapper';
import {SelectModule} from 'ng2-select';
import {ValidationErrorComponent} from './bases/validation-error/validation-error.component';
import {MyDropZoneComponent} from './my-drop-zone-component/my-drop-zone.component';

<#list project.entities as e>
import {${Utils.upperCamel(e.name)}MultiSelectComponent} from './multi-select/${Utils.lowerHyphen(e.name)}/${Utils.lowerHyphen(e.name)}-multi-select.component';
</#list>
// shared module
// do not provide services in Shared Modules! Especially not if you plan to use them in Lazy Loaded Modules!
@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        NgxPaginationModule,
        DropzoneModule,
        SelectModule
    ],
    declarations: [
        // the pipe should declare once some where and only once.
        // BankAccountComponent,
        // ValidationErrorComponent,
        // MyDropZoneComponent,
        // OrgDropZoneComponent
        ValidationErrorComponent,
        MyDropZoneComponent,
        <#list project.entities as e>
            ${Utils.upperCamel(e.name)}MultiSelectComponent,
        </#list>
    ],
    exports: [
        // BankAccountComponent,
        // ValidationErrorComponent,
        // MyDropZoneComponent,
        // OrgDropZoneComponent
        ValidationErrorComponent,
        MyDropZoneComponent,
        <#list project.entities as e>
            ${Utils.upperCamel(e.name)}MultiSelectComponent,
        </#list>
    ]
})
export class SharedModule {
}
