import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {NgxPopperModule} from 'ngx-popper';
import {NgxPaginationModule} from 'ngx-pagination';
import {DropzoneModule} from 'ngx-dropzone-wrapper';
import {SelectModule} from 'ng2-select';
import {NouisliderModule} from 'ng2-nouislider';
import {NguiDatetimePickerModule} from '@ngui/datetime-picker';
import {QuillModule} from 'ngx-quill';
import {ValidationErrorComponent} from './bases/validation-error/validation-error.component';
import {MyDropZoneComponent} from './my-drop-zone-component/my-drop-zone.component';
import {MultiSelectSharedModule} from '../shared-fearure-modules/multi-select-shared-module/multi-select-shared.module';
import {NgSelectSharedModule} from '../shared-fearure-modules/ng-select-shared-module/ng-select-shared.module';
import {LimitLengthTdComponent} from './limit-length-td-component/limit-length-td.component';
<#list project.entities as e>
import {${Utils.upperCamel(e.name)}SubformComponent} from './subform-components/${Utils.lowerHyphen(e.name)}/${Utils.lowerHyphen(e.name)}-subform.component';
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
        SelectModule,
        MultiSelectSharedModule,
        NgSelectSharedModule,
        NgxPopperModule,
        QuillModule,
        NguiDatetimePickerModule,
        NouisliderModule
    ],
    declarations: [
        ValidationErrorComponent,
        MyDropZoneComponent,
        LimitLengthTdComponent,
        <#list project.entities as e>
${Utils.upperCamel(e.name)}SubformComponent,
    </#list>
    ],
    exports: [
        ValidationErrorComponent,
        MyDropZoneComponent,
        LimitLengthTdComponent,
        <#list project.entities as e>
${Utils.upperCamel(e.name)}SubformComponent,
    </#list>
    ]
})
export class SharedModule {
}
