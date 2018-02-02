<!-- MAIN CONTENT -->
<div id="content">
    <!--<ngx-loading [show]="loading"></ngx-loading>-->

    <!-- widget grid -->
    <sa-widgets-grid>
        <div class="row">

            <article class="col-sm-12 col-md-12 col-lg-12">
                <sa-widget [editbutton]="false" color="greenLight">
                    <header>
                        <span class="widget-icon"> <i class="fa fa-table"></i> </span>
                        <h2>${Utils.upperCamel(entity.name)} Table</h2>
                        {#
                        <!--<h2>{{selectedRole !== undefined ? selectedRole.name.get_camel(): ''}}</h2>-->
                        #}
                    </header>
                    <div>
                        <div class="widget-body no-padding">
                            <div class=""
                                 style="padding: 6px 7px 6px !important; border-bottom: 1px solid #ccc; background: #fafafa; margin-bottom: 0px;">
                                <a class="btn btn-default" (click)="reset()"><i class="fa fa-filter"></i>Reset Filter</a>
                                <a class="btn btn-default" routerLink="/dbc-pages/{{class_model.name.get_camel()}}/add">
                                    <i class="fa fa-plus"></i>Add
                                </a>
                            </div>

                            <div class="table-responsive">

                                <table class="table table-bordered table-striped table-condensed table-hover smart-form has-tickbox">

                                    <thead>
                                    <tr [formGroup]="searchForm" class="searchForm">
                                        <th></th>
                                        <#list entity.fields as f>
                                        <th class="hasinput" [ngStyle]="{width:'8%'}">
                                            <input formControlName="${Utils.lowerCamel(f.name)}" type="text" class="form-control"
                                                   placeholder="${Utils.lowerCamel(f.name)}"/>
                                        </th>
                                        </#list>
                                        <th></th>
                                    </tr>

                                    {#
                                    <!--<tr>-->
                                    <!--<th *ngFor="let c of sortOprions.sortColumns" [ngClass]="getSortClass(c)"-->
                                    <!--(click)="changeSort(c)">{{c.columnDisplay}}-->
                                    <!--</th>-->
                                    <!--<th>Action</th>-->
                                    <!--</tr>-->
                                    #}

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
                                        <#list entity.fields as f>
                                        <th>${Utils.lowerCamel(f.name)}</th>
                                        </#list>
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
                                        <#list entity.fields as f>
                                        <td>{{item.${Utils.lowerCamel(f.name)} }}</td>
                                        </#list>
                                        <td> <a routerLink="/pages/{{class_model.name.get_camel()}}s/edit/{{item.id}}"><i
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
                </sa-widget>

            </article>
        </div>
    </sa-widgets-grid>
</div>

