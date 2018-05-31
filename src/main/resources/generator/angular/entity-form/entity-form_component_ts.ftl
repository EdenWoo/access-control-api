
<#setting classic_compatible=true>
import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormArray, FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {Subscription} from 'rxjs/Subscription';
import {ActivatedRoute, Router} from '@angular/router';
import {Location} from '@angular/common';
import {AutoUnsubscribe} from 'ngx-auto-unsubscribe';
import {Subject} from 'rxjs/Subject';

import {FormBaseComponent} from '../../../shared-module/bases/form-base-component/form-base.component';
import {MyNotifyService} from '../../../services/my-notify.service';
import {ImageModel} from '../../../models/bases/image.model';

import {${Utils.upperCamel(entity.name)}Model} from '../${Utils.lowerHyphen(entity.name)}.model';
import {${Utils.upperCamel(entity.name)}Service} from '../${Utils.lowerHyphen(entity.name)}.service';
import {ValidationPattern} from '../../../shared-module/bases/validation-error/validation.pattern';
declare var $: any;

<#list entity.fields as f>
    <#if f.selectOne && f.type.element != entity.name>
    import {${Utils.upperCamel(f.type.element)}Service} from '../../${Utils.lowerHyphen(f.type.element)}/${Utils.lowerHyphen(f.type.element)}.service';
    </#if>
</#list>

@Component({
selector: 'sa-${Utils.lowerHyphen(entity.name)}-form',
templateUrl: './${Utils.lowerHyphen(entity.name)}-form.component.html',
})
@AutoUnsubscribe()
export class ${Utils.upperCamel(entity.name)}FormComponent extends FormBaseComponent implements OnInit {
public loading: boolean;
public myForm: FormGroup;
public id: number;
public isEdit = false;
public subscription: Subscription;
public ${Utils.lowerCamel(entity.name)}: ${Utils.upperCamel(entity.name)}Model = new ${Utils.upperCamel(entity.name)}Model();
public currentDate = new Date();
// =====================================================================
// =============================Dropzone Variable=======================
<#list entity.fields as f>
    <#if f.attachment>
        public ${Utils.lowerCamel(f.name)}ImageSubject: Subject<ImageModel[] | any> = new Subject<ImageModel[] | any>();
    </#if>
</#list>
// ============================Dropzone Variable========================
// =====================================================================

// =================================================================================
// =============================multi select subject Variable=======================
<#list entity.fields as f>
    <#if f.selectOne>
        public ${Utils.lowerCamel(f.type.element)}RefreshSubject = new Subject<any>();
        public ${Utils.lowerCamel(f.type.element)}ForLoadSubject = new Subject<any>();
    </#if>
</#list>
// ============================multi select subject Variable========================
// =================================================================================


// =================================================================================
// ===================================EnumList Variable=============================
<#list entity.fields as f>
    <#if f.type.name?contains("Enum")>
    public ${Utils.lowerCamel(f.type.name)}List: any;
    </#if>
</#list>
// ===================================EnumList Variable=============================
// =================================================================================


constructor(public formBuiler: FormBuilder,
public ref: ChangeDetectorRef,
public router: Router,
public location: Location,
public myNotifyService: MyNotifyService,
public ${Utils.lowerCamel(entity.name)}Service: ${Utils.upperCamel(entity.name)}Service,
<#list entity.fields as f>
    <#if f.selectOne && f.type.element != entity.name>
        public ${Utils.lowerCamel(f.type.element)}Service: ${Utils.upperCamel(f.type.element)}Service,
    </#if>
</#list>
public activatedRoute: ActivatedRoute) {
super(activatedRoute, location);
}

ngOnInit() {
this.getEnumList();
this.getRouteParemeter();
this.getQueryParams();
//this.initFormControl();
}

initFormControl() {
this.myForm = this.formBuiler.group({
<#list entity.fields as f>
    <#assign validationStr = "">
    <#assign finalValidationStr = "">
    <#assign ansycValidationStr = "">
    <#assign finalAnsycValidationStr = "">
    <#if f.required>
        <#assign validationStr += 'Validators.required,'>
    </#if>
    <#if f.pattern>
        <#assign validationStr += 'ValidationPattern.commonRegexp(/' + f.pattern + '/),'>
    </#if>
    <#if f.sizeMin??>
        <#assign validationStr += 'Validators.minLength(${f.sizeMin}),'>
    </#if>
    <#if f.sizeMax??>
        <#assign validationStr += 'Validators.maxLength(${f.sizeMax}),'>
    </#if>
    <#if f.rangeMin??>
        <#assign validationStr += 'Validators.min(${f.rangeMin?string.number}),'>
    </#if>
    <#if f.rangeMax??>
        <#assign validationStr += 'Validators.max(${f.rangeMax?long?c}),'>
    </#if>
    <#if f.unique>
        <#assign ansycValidationStr = "ValidationPattern.duplicateValidate(this.id, '${f.name}', this.${Utils.lowerCamel(entity.name)}Service)">
    </#if>
    <#if f.type.name == "Int" || f.type.name == "Double">
    </#if>
    <#if validationStr?? && validationStr!= "">
        <#assign finalValidationStr += "validators:[${validationStr}],">
    </#if>
    <#if ansycValidationStr != "">
        <#assign finalAnsycValidationStr += "asyncValidators:[${ansycValidationStr}],updateOn: 'blur'">
    </#if>
    <#if f.addDynamicMany>
        ${Utils.lowerCamel(f.name)}: this.formBuiler.array([]),
    <#else>
        ${Utils.lowerCamel(f.name)}: new FormControl('', {
        ${finalValidationStr}
        ${finalAnsycValidationStr}
            }),
    </#if>
</#list>
});
}

getEnumList(){
<#list entity.fields as f>
<#if f.type.name?contains("Enum")>
this.${Utils.lowerCamel(entity.name)}Service.getEnumList('${Utils.upperCamel(f.type.name)}').subscribe((resp: any) => {
console.log(resp);
this.${Utils.lowerCamel(f.type.name)}List = resp;
});
</#if>
</#list>
    }

getItem() {
this.loading = true;
this.${Utils.lowerCamel(entity.name)}Service.get(this.id).subscribe((resp: any) => {
this.loading = false;
console.log(resp);
this.${Utils.lowerCamel(entity.name)} = resp;
this.emitDropzoneFiles();
this.loadDatas();
}, err => {
this.loading = false;
});
}


loadDatas(){
<#list entity.fields as f>
    <#if f.selectOne>
    if (this.${Utils.lowerCamel(entity.name)}.${Utils.lowerCamel(f.name)}) {
                this.${Utils.lowerCamel(f.type.element)}ForLoadSubject.next(this.${Utils.lowerCamel(entity.name)}.${Utils.lowerCamel(f.name)});
            }
    </#if>
</#list>
    }

onSubmit({value, valid}: { value: ${Utils.upperCamel(entity.name)}Model, valid: boolean }) {
if(valid){
if (!this.isEdit) {
this. ${Utils.lowerCamel(entity.name)}Service.add(value).subscribe((resp: any) => {
console.log(resp);
this.goBack();
}, err => {
console.log(err);
this.myNotifyService.notifyFail(err.error.error);
})
} else {
this.${Utils.lowerCamel(entity.name)}Service.update(this.${Utils.lowerCamel(entity.name)}.id, value).subscribe((resp: any) => {
console.log(resp);
this.myNotifyService.notifySuccess('The ${Utils.lowerCamel(entity.name)} is successfully updated.');
this.goBack();
}, err => {
console.log(err);
this.myNotifyService.notifyFail(err.error.error);
})
}
}else {
console.log(this.myForm);
ValidationPattern.validateAllFormFields(this.myForm);
setTimeout(t => {
                $('html, body').animate({
                    scrollTop: $('.text-danger:visible:first').offset().top - 100
                }, 1000);
            });
this.myNotifyService.notifyFail('Some fields are invalid, please check.');
}
}

// =====================================================================
// =======================Multi Select Event============================
<#list entity.fields as f>
    <#if f.selectOne>
        ${Utils.lowerCamel(f.type.element)}Selected($event) {
            if($event){
                this.${Utils.lowerCamel(entity.name)}.${Utils.lowerCamel(f.name)} = $event;
            }
        }
    </#if>
</#list>
// =======================Multi Select Event============================
// =====================================================================

// =====================================================================
// =============================Dropzone================================
emitDropzoneFiles(){

<#list entity.fields as f>
    <#if f.attachment>
        this.${Utils.lowerCamel(f.name)}ImageSubject.next(this.${Utils.lowerCamel(entity.name)}.${Utils.lowerCamel(f.name)});
    </#if>
</#list>

}


<#list entity.fields as f>
    <#if f.attachment>
        ${Utils.lowerCamel(f.name)}FileObjectsChanged($event) {
        this.${Utils.lowerCamel(entity.name)}.${Utils.lowerCamel(f.name)} = $event;
    }
    </#if>
</#list>
// ============================Dropzone=================================
// =====================================================================

// ===========================================================================
// =============================daymically add================================
<#list entity.fields as f>
<#if f.addDynamicMany>
    <#assign elementEntity = Utils.findCodeEntity(entities,f.type.element)/>


// ================================put in subform's parent form========================================
// ===============================${Utils.upperCamel(entity.name)} SubForm============================
load${Utils.upperCamel(f.name)}Form() {
    const ${Utils.lowerCamel(f.name)}Ctrls = (<FormArray>this.myForm.controls['${Utils.lowerCamel(f.name)}']).controls;

    if (${Utils.lowerCamel(f.name)}Ctrls && this.${Utils.lowerCamel(entity.name)}.${Utils.lowerCamel(f.name)}) {
        while (${Utils.lowerCamel(f.name)}Ctrls.length < this.${Utils.lowerCamel(entity.name)}.${Utils.lowerCamel(f.name)}.length) {
            this.add${Utils.upperCamel(f.name)}FormControl();
        }
    }
    // this.ref.markForCheck();
    // this.ref.detectChanges();
}

add${Utils.upperCamel(f.name)}FormControl() {
    const control = <FormArray>this.myForm.controls['${Utils.lowerCamel(f.name)}'];
    const addrCtrl = this.init${Utils.upperCamel(f.name)}FormControl();
    control.push(addrCtrl);
}

remove${Utils.upperCamel(f.name)}FormControl(i: number) {
    const control = <FormArray> this.myForm.controls['${Utils.lowerCamel(f.name)}'];
    control.removeAt(i);
}

init${Utils.upperCamel(f.name)}FormControl() {
   return this.formBuiler.group({
    <#list elementEntity.fields as ff>
        <#assign validationStr = "">
        <#assign finalValidationStr = "">
        <#assign ansycValidationStr = "">
        <#assign finalAnsycValidationStr = "">
        <#if ff.required>
            <#assign validationStr += 'Validators.required,'>
        </#if>
        <#if ff.sizeMin??>
            <#assign validationStr += 'Validators.minLength(${ff.sizeMin}),'>
        </#if>
        <#if ff.sizeMax??>
            <#assign validationStr += 'Validators.maxLength(${ff.sizeMax}),'>
        </#if>
        <#if ff.rangeMin??>
            <#assign validationStr += 'Validators.min(${ff.rangeMin?string.number}),'>
        </#if>
        <#if ff.rangeMax??>
            <#assign validationStr += 'Validators.max(${ff.rangeMax?long?c}),'>
        </#if>
        <#if ff.unique>
            <#assign ansycValidationStr = "ValidationPattern.duplicateValidate('${ff.name}', this.${Utils.lowerCamel(entity.name)}Service)">
        </#if>
        <#if ff.type.name == "Int" || ff.type.name == "Double">
        </#if>
        <#if validationStr?? && validationStr!= "">
            <#assign finalValidationStr += "validators:[${validationStr}],">
        </#if>
        <#if ansycValidationStr != "">
            <#assign finalAnsycValidationStr += "asyncValidators:[${ansycValidationStr}],">
        </#if>
        ${Utils.lowerCamel(ff.name)}: new FormControl('', {
        ${finalValidationStr}
        ${finalAnsycValidationStr}}),
    </#list>
    });
}

get${Utils.upperCamel(f.name)}(i: number) {
    if (this.isEdit && this.${Utils.lowerCamel(entity.name)} && this.${Utils.lowerCamel(entity.name)}.${Utils.lowerCamel(f.name)}) {
        return this.${Utils.lowerCamel(entity.name)}.${Utils.lowerCamel(f.name)}[i];
    }
}

get${Utils.upperCamel(f.name)}FormData() {
    return <FormArray>this.myForm.get('${Utils.lowerCamel(f.name)}');
}

// ===============================${Utils.upperCamel(entity.name)} SubForm============================
// ========================================================================
</#if>
</#list>

// ============================daymically add=================================
// ===========================================================================



}
