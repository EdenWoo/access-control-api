

<!-- MAIN CONTENT -->
<div id="content">
    <!--<div class="row">-->
    <!--<sa-big-breadcrumbs [breadCrumbs]="breadCrumbs" icon="pencil-square-o"-->
    <!--class="col-xs-12 col-sm-9 col-md-9 col-lg-9"></sa-big-breadcrumbs>-->

    <!--&lt;!&ndash;<div class="col-xs-12 col-sm-3 col-md-3 col-lg-3">&ndash;&gt;-->
    <!--&lt;!&ndash;&lt;!&ndash; Button trigger modal &ndash;&gt;&ndash;&gt;-->
    <!--&lt;!&ndash;<a (click)="mdModal.show()" class="btn btn-success btn-lg pull-right header-btn hidden-mobile"><i&ndash;&gt;-->
    <!--&lt;!&ndash;class="fa fa-circle-arrow-up fa-lg"></i> Launch form modal</a>&ndash;&gt;-->
    <!--&lt;!&ndash;</div>&ndash;&gt;-->
    <!--</div>-->


    <!-- widget grid -->

    <sa-widgets-grid>
        <ngx-loading [show]="loading"></ngx-loading>


        <!-- START ROW -->

        <div class="row">

            <!-- NEW COL START -->
            <article class="col-sm-12 col-md-12 col-lg-12">

                <!-- Widget ID (each widget will need unique ID)-->
                <div sa-widget [editbutton]="false" [custombutton]="false">

                    <header>
                        <span class="widget-icon"> <i class="fa fa-edit"></i> </span>

                        <h2>${Utils.upperCamel(entity.name)} Form</h2>

                    </header>
                    <div>
                        <!-- widget content -->
                        <div class="widget-body no-padding">

                            <form [formGroup]="myForm" novalidate (ngSubmit)="onSubmit(myForm)" id="checkout-form"
                                  class="smart-form">

                                <fieldset>

                                    <#assign row=0>
                                    <#list entity.fields as f>

                                        <#if (row % 2) == 0 && !f.primaryKey>
                                        <div class="row">
                                        </#if>

                                        <#if f.primaryKey>
                                        <input type="text"
                                               style="display: none;"
                                               formControlName="${Utils.lowerCamel(f.name)}"
                                               [(ngModel)]="${Utils.lowerCamel(entity.name)}.${Utils.lowerCamel(f.name)}"
                                               name="${Utils.lowerCamel(f.name)}"
                                               placeholder="">

                                        <#elseif f.type?? && f.type.name == 'List'>
                                        <!---------------------------------------------------->
                                        <!-----if is multi select and ouput single entity----->
                                        <input type="text"
                                               style="display: none;"
                                               formControlName="${Utils.lowerCamel(f.name)}"
                                               [(ngModel)]="${Utils.lowerCamel(entity.name)}.${Utils.lowerCamel(f.name)}"
                                               name="${Utils.lowerCamel(f.name)}"
                                               placeholder="">

                                        <section class="col col-6">
                                            <label class="select">
                                                ${f.type.element}<span style="color: red">*</span>
                                                <${Utils.lowerHyphen(f.type.element)}-multi-select
                                                        (datasSelected)="${Utils.lowerCamel(f.type.element)}Selected($event)"
                                                        [multiple]="false"
                                                        [outputArray]="false"
                                                ></${Utils.lowerHyphen(f.type.element)}-multi-select>
                                                <validation-error
                                                        [control]="myForm.get('${Utils.lowerCamel(f.type.element)}')"></validation-error>
                                            </label>
                                        </section>
                                        <!-----if is multi select and ouput single entity----->
                                        <!---------------------------------------------------->


                                        <#elseif f.type?? && f.type.name == 'Entity'&& f.type.element == 'Attachment'>
                                        <!---------------------------------------------------->
                                        <!--------------------if is dropzone------------------>
                                        <input type="text"
                                               style="display: none;"
                                               formControlName="${Utils.lowerCamel(f.name)}"
                                               [(ngModel)]="${Utils.lowerCamel(entity.name)}.${Utils.lowerCamel(f.name)}"
                                               name="${Utils.lowerCamel(f.name)}"
                                               placeholder="">

                                        <label>${f.name}</label>
                                        <div class="row dropzone-area">
                                            <div class="" role="content">
                                                <!-- widget content -->
                                                <div class="widget-body">

                                                    <my-drop-zone-component [maxFiles]="10"
                                                                            [subject]="${Utils.lowerCamel(f.name)}ImageSubject"
                                                                            (fileObjectsChanged)="${Utils.lowerCamel(f.name)}FileObjectsChanged($event)">
                                                    </my-drop-zone-component>
                                                    <validation-error
                                                            [control]="myForm.get('${Utils.lowerCamel(f.name)}')"></validation-error>

                                                </div>
                                                <!-- end widget content -->

                                            </div>
                                        </div>
                                        <!-----if is multi select and ouput single entity----->
                                        <!---------------------------------------------------->

                                        <#elseif f.switch>
                                        <!---------------------------------------------------->
                                        <!-------------------if is switch------------------->
                                        <section class="col col-6">
                                            <label class="toggle">
                                                ${f.name}<span style="color: red">*</span>
                                                <input type="checkbox"
                                                       formControlName="${Utils.lowerCamel(f.name)}"
                                                       [(ngModel)]="${Utils.lowerCamel(entity.name)}.${Utils.lowerCamel(f.name)}"
                                                       name="checkbox-toggle"
                                                       checked="checked">
                                                <i data-swchon-text="YES" data-swchoff-text="NO"></i>
                                            <validation-error
                                                    [control]="myForm.get('${Utils.lowerCamel(f.name)}')"></validation-error>
                                            </label>
                                        </section>
                                        <!-----if is multi select and ouput single entity----->
                                        <!---------------------------------------------------->


                                        <#else>
                                        <!--------------------------------------------------------->
                                        <!--------------------else is normal field----------------->
                                          <section class="col col-6">
                                              <label class="input">
                                                  ${f.name}<span style="color: red">*</span>
                                                  <input type="text"
                                                         formControlName="${Utils.lowerCamel(f.name)}"
                                                         [(ngModel)]="${Utils.lowerCamel(entity.name)}.${Utils.lowerCamel(f.name)}"
                                                         name="${Utils.lowerCamel(f.name)}"
                                                         placeholder="">
                                                  <validation-error
                                                          [control]="myForm.get('${Utils.lowerCamel(f.name)}')"></validation-error>
                                              </label>
                                          </section>
                                        <!--------------------else is normal field----------------->
                                        <!--------------------------------------------------------->

                                        </#if>

                                        <#if ((row % 2) == 1 && !f.primaryKey) || (!f_has_next)>
                                        </div>
                                        </#if>

                                        <#if !f.primaryKey>
                                            <#assign row = row + 1>
                                        </#if>
                                    </#list>

                                </fieldset>
                                <footer>
                                    <button type="submit" class="btn btn-primary">
                                        Submit
                                    </button>
                                </footer>
                            </form>

                        </div>
                        <!-- end widget content -->

                    </div>
                    <!-- end widget div -->

                </div>
                <!-- end widget -->

                <!-- Widget ID (each widget will need unique ID)-->

                <!-- end widget -->

            </article>
            <!-- END COL -->

        </div>

        <!-- END ROW -->

    </sa-widgets-grid>

    <!-- end widget grid -->


</div>