import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {BaseComponent} from '../../../shared-module/bases/base-component/base.component';
import {HelperService} from '../../../services/helper.service';

import {${Utils.upperCamel(entity.name)}Model} from '../${Utils.lowerHyphen(entity.name)}.model';
import {${Utils.upperCamel(entity.name)}Service} from '../${Utils.lowerHyphen(entity.name)}.service';

@Component({
    selector: 'sa-${Utils.lowerHyphen(entity.name)}-list',
    templateUrl: './${Utils.lowerHyphen(entity.name)}-list.component.html',
})
export class ${Utils.upperCamel(entity.name)}ListComponent extends BaseComponent implements OnInit {
public searchForm: FormGroup;
public searchCondition: string;
public loading: boolean;

constructor(private formBuilder: FormBuilder,
private ${Utils.lowerCamel(entity.name)}Service: ${Utils.upperCamel(entity.name)}Service,
private helperService: HelperService) {
super();
}

ngOnInit() {
this.refresh();
this.buildSearchFrom();
this.debounceSearchForm();
}

refresh() {
this.loading = true;
this.${Utils.lowerCamel(entity.name)}Service.getAllByPaging(this.searchCondition, this.paging).subscribe((resp: any) => {
console.log(resp);
this.listElements = resp.content;
this.paging.totalSize = resp.totalElements;
this.loading = false;
}, err => {
this.loading = false;
});
}

/**
* ----- search form -----
*/
buildSearchFrom() {
this.searchForm = this.formBuilder.group({
        <#list entity.fields as f>
        ${Utils.lowerCamel(f.name)}: ['', [Validators.required]],
        </#list>
});
}

debounceSearchForm() {
this.searchCondition = '';
this.searchForm.valueChanges.debounceTime(500).subscribe((form: any) => {
if (form) {
this.searchCondition = this.helperService.generateQueryString(form);
}
this.refresh();
});
}

reset() {
this.searchForm.reset();
}
}