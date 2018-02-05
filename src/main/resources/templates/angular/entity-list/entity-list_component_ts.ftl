import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {HelperService} from '../../../services/helper.service';
import {ListBaseComponent} from '../../../shared-module/bases/list-base-component/list-base.component';
import {Router} from '@angular/router';

import {${Utils.upperCamel(entity.name)}Model} from '../${Utils.lowerHyphen(entity.name)}.model';
import {${Utils.upperCamel(entity.name)}Service} from '../${Utils.lowerHyphen(entity.name)}.service';

@Component({
    selector: 'sa-${Utils.lowerHyphen(entity.name)}-list',
    templateUrl: './${Utils.lowerHyphen(entity.name)}-list.component.html',
})
export class ${Utils.upperCamel(entity.name)}ListComponent extends ListBaseComponent implements OnInit {
public searchForm: FormGroup;
public searchCondition: string;
public loading: boolean;

constructor(private formBuilder: FormBuilder,
public ${Utils.lowerCamel(entity.name)}Service: ${Utils.upperCamel(entity.name)}Service,
public router: Router,
public helperService: HelperService) {
super(router, helperService);
}

ngOnInit() {
this.refresh();
this.buildSearchFrom();
this.debounceSearchForm();
}

refresh() {
this.loading = true;
const searchStr = this.helperService.getSearchConditionByRouter(this.router);
this.${Utils.lowerCamel(entity.name)}Service.getAllByPaging(searchStr, this.paging).subscribe((resp: any) => {
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
}