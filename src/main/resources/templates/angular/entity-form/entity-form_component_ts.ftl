import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Subscription} from 'rxjs/Subscription';
import {Router} from '@angular/router';
import {AutoUnsubscribe} from 'ngx-auto-unsubscribe';
import {Location} from '@angular/common';

@Component({
selector: 'sa-${entity.name}-form',
templateUrl: './${entity.name}-form.component.html',
})
@AutoUnsubscribe()
export class ${entity.name}FormComponent extends BaseComponent implements OnInit {
public loading: boolean;
public myForm: FormGroup;
public id: number;
public isEdit = false;
public subscription: Subscription;
public ${entity.name}: ${entity.name}Model = new ${entity.name}Model();

constructor(public formBuiler: FormBuilder,
public ref: ChangeDetectorRef,
public router: Router,
public location: Location,
public myNotifyService: MyNotifyService,
public ${entity.name}Service: ${entity.name}Service,
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
${f.name}: ['', [Validators.required]],
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
this.${entity.name}Service.get(this.id).subscribe((resp: any) => {
this.loading = false;
console.log(resp);
this.${entity.name} = resp;
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

onSubmit({value, valid}: { value: ${entity.name}Model, valid: boolean }) {

if (!this.isEdit) {
this.${entity.name}Service.add(value).subscribe((resp: any) => {
console.log(resp);
this.goBack();
}, err => {
console.log(err);
this.myNotifyService.notifyFail(err.error.error);
})
} else {
this.${entity.name}Service.update(this.${entity.name}.id, value).subscribe((resp: any) => {
console.log(resp);
this.myNotifyService.notifySuccess('The ${entity.name} is successfully updated.');
this.goBack();
}, err => {
console.log(err);
this.myNotifyService.notifyFail(err.error.error);
})
}
}
}
