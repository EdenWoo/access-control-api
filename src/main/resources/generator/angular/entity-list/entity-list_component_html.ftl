<#setting classic_compatible=true>

<!-- MAIN CONTENT -->
<div id="content">
    <ngx-loading [show]="loading"></ngx-loading>

    <!-- widget grid -->
    <sa-widgets-grid>
        <div class="row">

            <article class="col-sm-12 col-md-12 col-lg-12">
            <#--<sa-widget [editbutton]="false" color="greenLight">-->
                <div sa-widget class="" color="blueDark">
                    <header>
                        <span class="widget-icon"> <i class="fa fa-table"></i> </span>
                        <h2>${Utils.spacedCapital(entity.name)} Table</h2>
                    </header>
                    <div>
                        <div class="widget-body no-padding">
                            <div class=""
                                 style="padding: 6px 7px 6px !important; border-bottom: 1px solid #ccc; background: #fafafa; margin-bottom: 0px;">
                                <a class="btn btn-default" (click)="reset()"><i class="fa fa-filter"></i>Reset
                                    Filter</a>
                                <a class="btn btn-default" routerLink="/pages/${Utils.lowerHyphen(entity.name)}/${Utils.lowerHyphen(entity.name)}-add">
                                    <i class="fa fa-plus"></i>Add
                                </a>
                                <a (click)="importByExcel()" class="btn btn-default">
                                    <i class="fa fa-cloud-upload" aria-hidden="true"></i>
                                    Import By Excel
                                </a>
                                <a (click)="download()" class="btn btn-default">
                                    <i class="fa fa-cloud-download" aria-hidden="true"></i>
                                    Export
                                </a>
                            </div>
                            <div class="table-responsive" style="margin-top: -6px;">

                                <table class="table table-bordered table-striped table-condensed table-hover smart-form has-tickbox dataTable no-footer">

                                    <thead>
                                    <tr [formGroup]="searchForm" class="searchForm">
                                        <th></th>
                                        <#list entity.listFields as f>
                                            <#if f.hiddenInList>

                                            <#else>
                                                <th class="hasinput" [ngStyle]="{width:'10%'}">
                                                    <input formControlName="${Utils.lowerCamel(f.name)}" type="text"
                                                           class="form-control"
                                                           placeholder="${Utils.spacedCapital(f.name)}"/>
                                                </th>
                                            </#if>
                                        </#list>
                                        <th></th>
                                    </tr>


                                    <!--<tr>-->
                                    <!--<th *ngFor="let c of sortOprions.sortColumns" [ngClass]="getSortClass(c)"-->
                                    <!--(click)="changeSort(c)">{{c.columnDisplay}}-->
                                    <!--</th>-->
                                    <!--<th>Action</th>-->
                                    <!--</tr>-->

                                    <!--Without Sorting-->
                                    <tr *ngIf="false">
                                        <th>
                                            <label class="checkbox" style="margin-bottom: 20px;">
                                                <input type="checkbox"
                                                       [(ngModel)]="isSelectAll"
                                                       [checked]="isSelectAll === true"
                                                       (ngModelChange)="selectAll()"
                                                       name="checkbox-inline">
                                                <i></i>
                                            </label>
                                        </th>
                                        <#list entity.listFields as f>
                                            <#if f.hiddenInList>

                                            <#elseif false>

                                            <#else>
                                            <th>${Utils.lowerCamel(f.name)}</th>
                                            </#if>
                                        </#list>
                                        <th>Action</th>
                                    </tr>

                                    <!--With Sorting-->
                                    <tr>
                                        <th>
                                            <label class="checkbox" style="margin-bottom: 20px;">
                                                <input type="checkbox"
                                                       [(ngModel)]="isSelectAll"
                                                       [checked]="isSelectAll === true"
                                                       (ngModelChange)="selectAll()"
                                                       name="checkbox-inline">
                                                <i></i>
                                            </label>
                                        </th>
                                        <th *ngFor="let c of sortOprions.sortColumns"
                                            [ngClass]="getSortClass(c)"
                                            (click)="changeSort(c)">{{c.columnDisplay}}
                                        </th>
                                        <th>Action</th>
                                    </tr>

                                    </thead>
                                    <tbody>

                                    <tr *ngFor="let item of listElements | paginate: { itemsPerPage: paging.pageSize,
                                                      currentPage: paging.pageNumber,
                                                      totalItems: paging.totalSize }">
                                        <td>
                                            <label class="checkbox">
                                                <input [(ngModel)]="item.isSelected"
                                                       [checked]="item.isSelected === true"
                                                       (ngModelChange)="select(item)"
                                                       type="checkbox"
                                                       name="checkbox-inline"
                                                       value="true">
                                                <i></i>
                                            </label>
                                        </td>
                                        <#list entity.listFields as f>
                                            <#if f.hiddenInList>

                                            <#elseif f.type.name == "Boolean" || f.type.name == "boolean">
                                            <td>
                                            <span *ngIf="item.${Utils.lowerCamel(f.name)}">Yes</span>
                                            <span *ngIf="!item.${Utils.lowerCamel(f.name)}">No</span>
                                            </td>
                                            <#elseif f.type.name == "ZonedDateTime">
                                            <td>{{item.${Utils.lowerCamel(f.name)}?(item.${Utils.lowerCamel(f.name)}*1000 | date):''}}</td>

                                            <#elseif false>
                                                <td>{{item.${Utils.lowerCamel(f.name)}.${Utils.lowerCamel(f.type.element)}}}</td>

                                            <#elseif f.display?size != 0>
                                                <td>{{
                                                    <#list f.display as d>
                                                        <#if d != ''>
                                                        item.${Utils.lowerCamel(f.name)}?.${d} + ' '<#if d_has_next> +</#if>
                                                        <#else>
                                                        item.${Utils.lowerCamel(f.name)}
                                                        </#if>
                                                    </#list>
                                                    }}
                                                </td>
                                            <#else>
                                            <td><limit-length-td-component
                                                    [text]="item.${Utils.lowerCamel(f.name)}">
                                            </limit-length-td-component></td>
                                            </#if>
                                        </#list>
                                        <td><a routerLink="/pages/${Utils.lowerHyphen(entity.name)}/${Utils.lowerHyphen(entity.name)}-edit/{{item.id}}"><i
                                                class="fa fa-edit"></i></a></td>
                                    </tr>
                                    </tbody>
                                </table>
                                <div class="dt-toolbar-footer">
                                    <div class="col-sm-6 col-xs-12">
                                        <div class="pull-left pagination-detail">
                                            <span class="pagination-info">Show</span>
                                            <span class="page-list">
                                                  <span class="btn-group dropup">
                                                    <select class="form-control"
                                                            (ngModelChange)="onPageSizeChange($event)"
                                                            [(ngModel)]="paging.pageSize">
                                                        <option [ngValue]="i"
                                                                *ngFor="let i of [12,24,36,100]">{{i}}</option>
                                                    </select>
                                                  </span> records/page, <span
                                                    class="theme-color">{{paging.totalSize}}</span> records in total
                                              </span>
                                        </div>
                                    </div>
                                    <div class="col-sm-6 col-xs-12">
                                        <div class="dataTables_paginate paging_simple_numbers pagination pagination-sm"
                                             id="datatable_fixed_column_paginate">
                                            <pagination-controls
                                                    (pageChange)="pageChanged($event)"
                                                    maxSize="9"
                                                    directionLinks="true"
                                                    autoHide="false"
                                                    previousLabel="Previous"
                                                    nextLabel="Next"
                                                    screenReaderPaginationLabel="Pagination"
                                                    screenReaderPageLabel="page"
                                                    screenReaderCurrentLabel="You're on page">
                                            </pagination-controls>
                                        </div>
                                    </div>
                                </div>
                            </div>

                        </div>
                    </div>
                <#--</sa-widget>-->
                </div>
            </article>
        </div>
    </sa-widgets-grid>
</div>

<div bsModal #lgImportByExcelModal="bs-modal" class="modal fade" tabindex="-1" role="dialog"
     (onHide)="onImportByExcelModalHide()"
     aria-labelledby="myLargeModalLabel"
     aria-hidden="true">
    <div class="modal-dialog modal-md">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" (click)="lgImportByExcelModal.hide()" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">Import By Excel</h4>
            </div>
            <div class="modal-body" style="margin: 0 auto !important; width: 250px !important;">
                <!--<textarea name="" id="" cols="120" rows="20" [(ngModel)]="importMerchantString"></textarea>-->
                <my-drop-zone-component [url]="excelUploadUrl"
                                        [maxFiles]="1"
                                        [autoProcessQueue]="false"
                                        [uploadAndImportSubject]="uploadAndImportSubject"
                                        [resetDropzoneSubject]="resetDropzoneSubject"
                                        (fileUploaded)="onFileUploaded($event)">
                </my-drop-zone-component>
            </div>
            <div class="modal-footer">
                <div class="">
                    <footer>
                        <button type="submit" class="btn btn-success" (click)="importByExceliConfirm()">Confirm
                        </button>
                        <button type="submit" class="btn btn-primary" (click)="downloadExcelTemplate()">Download
                            Template
                        </button>
                        <button type="button" class="btn btn-default" (click)="lgImportByExcelModal.hide()">Cancel
                        </button>

                    </footer>
                </div>
            </div>
        </div>
    </div>
</div>