import {NgModule} from '@angular/core';
import {SmartadminDatatableModule} from '../../shared/ui/datatable/smartadmin-datatable.module';
import {SmartadminModule} from '../../shared/smartadmin.module';
import {CommonModule} from '@angular/common';
import {SmartadminInputModule} from '../../shared/forms/input/smartadmin-input.module';
import {RouterModule, Routes} from '@angular/router';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {SharedModule} from '../../shared-module/shared.module';
import {NgxPaginationModule} from 'ngx-pagination';
import {LoadingModule} from 'ngx-loading';
import {ConfirmationPopoverModule} from 'angular-confirmation-popover';
import {${Utils.upperCamel(entity.name)}ListComponent} from './${Utils.lowerHyphen(entity.name)}-list/${Utils.lowerHyphen(entity.name)}-list.component';
import {${Utils.upperCamel(entity.name)}FormComponent} from './${Utils.lowerHyphen(entity.name)}-form/${Utils.lowerHyphen(entity.name)}-form.component';

export const routes: Routes = [
    {path: '', redirectTo: 'list', pathMatch: 'full'},
    {path: 'list', component: ${Utils.upperCamel(entity.name)}ListComponent, pathMatch: 'full'},
    {path: 'add', component: ${Utils.upperCamel(entity.name)}FormComponent, pathMatch: 'full'},
    {path: 'edit/:id', component: ${Utils.upperCamel(entity.name)}FormComponent, pathMatch: 'full'},
];


@NgModule({
    declarations: [
        ${Utils.upperCamel(entity.name)}ListComponent,
        ${Utils.upperCamel(entity.name)}FormComponent
    ],
    imports: [
        SmartadminModule,
        SmartadminDatatableModule,
        RouterModule.forChild(routes),
        CommonModule,
        SmartadminInputModule,
        FormsModule,
        ReactiveFormsModule,
        SharedModule,
        NgxPaginationModule,
        LoadingModule,
        ConfirmationPopoverModule
    ],
})
export class ${Utils.upperCamel(entity.name)}Module {
}
