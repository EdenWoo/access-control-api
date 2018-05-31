import {Component, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {HelperService} from '../../../services/helper.service';
import {ListBaseComponent} from '../../../shared-module/bases/list-base-component/list-base.component';
import {Router} from '@angular/router';
import {Subject} from 'rxjs/Subject';

import {${Utils.upperCamel(entity.name)}Model} from '../${Utils.lowerHyphen(entity.name)}.model';
import {${Utils.upperCamel(entity.name)}Service} from '../${Utils.lowerHyphen(entity.name)}.service';

import {SortColumns} from './sort.columns';
import {Sorts} from '../../../models/bases/sorts.model';
import {DownloadService} from '../../../services/download.service';

@Component({
    selector: 'sa-${Utils.lowerHyphen(entity.name)}-list',
    templateUrl: './${Utils.lowerHyphen(entity.name)}-list.component.html',
})
export class ${Utils.upperCamel(entity.name)}ListComponent extends ListBaseComponent implements OnInit {
public searchForm: FormGroup;
public searchCondition: string;
public loading: boolean;

@ViewChild('lgImportByExcelModal') lgImportByExcelModal: any;
public excelUploadUrl: string;
public uploadAndImportSubject: Subject<string> = new Subject<string>();
public resetDropzoneSubject: Subject<string> = new Subject();
public exportUrl: string;

constructor(private formBuilder: FormBuilder,
public ${Utils.lowerCamel(entity.name)}Service: ${Utils.upperCamel(entity.name)}Service,
public router: Router,
public downloadService: DownloadService,
public helperService: HelperService) {
super(router, helperService);
this.sortOprions.sortColumns = SortColumns.Columns;
}

ngOnInit() {
this.excelUploadUrl = this.${Utils.lowerCamel(entity.name)}Service.getExcelUploadUrl();
this.refresh();
this.buildSearchFrom();
this.debounceSearchForm();
}

download() {
this.downloadService.downloadFile(this.exportUrl, '${Utils.lowerHyphen(entity.name)}-list' + Date.now().toString() + '.xls');
}

/**
* ----- modal functions BEGIN-----
*/
onImportByExcelModalHide() {
    this.lgImportByExcelModal.hide()
}

onFileUploaded(status) {
    this.refresh();
    this.lgImportByExcelModal.hide()
}

importByExceliConfirm() {
    this.uploadAndImportSubject.next('true');
}

importByExcel() {
    this.lgImportByExcelModal.show();
}

downloadExcelTemplate() {
this.downloadService.downloadFile(this.${Utils.lowerCamel(entity.name)}Service.getDownloadTemplateUrl(), '${Utils.lowerHyphen(entity.name)}-list-template' + Date.now().toString() + '.xls');
}
/**
* ----- modal functions END-----
*/

refresh() {
this.loading = true;
const searchStr = this.helperService.getSearchConditionByRouter(this.router);
this.exportUrl = this.${Utils.lowerCamel(entity.name)}Service.getUrl() + '/excel?' + this.${Utils.lowerCamel(entity.name)}Service.getSearchUrl(searchStr, this.paging, this.sortOprions);
this.exportUrl = this.exportUrl.replace('?&', '?');
this.${Utils.lowerCamel(entity.name)}Service.getAllByPaging(searchStr, this.paging, this.sortOprions).subscribe((resp: any) => {
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
        <#list entity.listFields as f>
        ${Utils.lowerCamel(f.name)}: ['', [Validators.required]],
        </#list>
});
}
}