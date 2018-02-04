import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Subscription} from 'rxjs/Subscription';
import {ActivatedRoute, Router} from '@angular/router';
import {Location} from '@angular/common';
import {AutoUnsubscribe} from 'ngx-auto-unsubscribe';
import {BaseComponent} from '../../../shared-module/bases/base-component/base.component';
import {MyNotifyService} from '../../../services/my-notify.service';

import {${Utils.upperCamel(entity.name)}Model} from '../${Utils.lowerHyphen(entity.name)}.model';
import {${Utils.upperCamel(entity.name)}Service} from '../${Utils.lowerHyphen(entity.name)}.service';

@Component({
selector: 'sa-${Utils.lowerHyphen(entity.name)}-form',
templateUrl: './${Utils.lowerHyphen(entity.name)}-form.component.html',
})
@AutoUnsubscribe()
export class ${Utils.upperCamel(entity.name)}FormComponent extends BaseComponent implements OnInit {
public loading: boolean;
public myForm: FormGroup;
public id: number;
public isEdit = false;
public subscription: Subscription;
public ${Utils.lowerCamel(entity.name)}: ${Utils.upperCamel(entity.name)}Model = new ${Utils.upperCamel(entity.name)}Model();

constructor(public formBuiler: FormBuilder,
public ref: ChangeDetectorRef,
public router: Router,
public location: Location,
public myNotifyService: MyNotifyService,
public ${Utils.lowerCamel(entity.name)}Service: ${Utils.upperCamel(entity.name)}Service,
public activedRoute: ActivatedRoute) {
super();
}

ngOnInit() {
this.getRouteParemeter();
this.getQueryParams();
this.initFormControl();
}

initFormControl() {
this.myForm = this.formBuiler.group({
<#list entity.fields as f>
    ${Utils.lowerCamel(f.name)}: ['', [Validators.required]],
</#list>
});
}

getRouteParemeter() {
this.subscription = this.activedRoute
.params
.subscribe(params => {
console.log(params);
this.id = params['id'];
if (this.id && this.id > 0) {
this.isEdit = true;
this.getItem();
} else {
this.isEdit = false;
}
});
}

getQueryParams() {
this.subscription = this.activedRoute
.queryParams
.subscribe(params => {
});
}

getItem() {
this.loading = true;
this. ${Utils.lowerCamel(entity.name)}Service.get(this.id).subscribe((resp: any) => {
this.loading = false;
console.log(resp);
this. ${Utils.lowerCamel(entity.name)} = resp;
}, err => {
this.loading = false;
});
}

equals(r1: any, r2: any) {
if (r1 && r2) {
return r1.id === r2.id;
}
}

goBack() {
this.location.back();
}

onSubmit({value, valid}: { value: ${Utils.upperCamel(entity.name)}Model, valid: boolean }) {

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
}
}
