<fieldset>
    <#list entity.groupedFields as groupedField>
     <div class="row">
        <#list groupedField as f>

            <#if f.type.name?contains("Enum")>
            <!---------------------------------------------------->
            <!-------------------if is enum----------------------->
            <section class="col col-6">
                <label class="select"
                       [ngClass]="{'state-error': (!myForm.controls['${Utils.lowerCamel(f.name)}'].valid && myForm.controls['${Utils.lowerCamel(f.name)}'].touched)}">
                    ${f.type.name?replace("Enum","")}
                    <#if f.required> <span style="color: red">*</span> </#if>
                    <select type="text"
                            formControlName="${Utils.lowerCamel(f.name)}"
                            [(ngModel)]="${Utils.lowerCamel(entity.name)}.${Utils.lowerCamel(f.name)}"
                            name="industry"
                            placeholder="">
                        <option [ngValue]="" selected disabled>Please Select
                        </option>
                        <option [ngValue]="i" *ngFor="let i of ${Utils.lowerCamel(f.type.name)}List">{{i}}
                        </option>
                    </select>
                    <i class="override"></i>
                    <validation-error
                            [control]="myForm.get('${Utils.lowerCamel(f.name)}')"></validation-error>
                </label>
            </section>
            <!-------------------if is enum----------------------->
            <!---------------------------------------------------->

            <#elseif f.selectMany>
            <!---------------------------------------------------->
            <!-----if is multi select and ouput single entity----->
            <input type="text"
                   style="display: none;"
                   formControlName="${Utils.lowerCamel(f.name)}"
                   [(ngModel)]="${Utils.lowerCamel(entity.name)}.${Utils.lowerCamel(f.name)}"
                   name="${Utils.lowerCamel(f.name)}"
                   placeholder="">

            <section class="col col-6">
                <div class="select"
                     [ngClass]="{'state-error': (!myForm.controls['${Utils.lowerCamel(f.name)}'].valid && myForm.controls['${Utils.lowerCamel(f.name)}'].touched)}">
                    ${Utils.spacedCapital(f.type.element)}

                   <#if f.required> <span style="color: red">*</span> </#if>
                    <common-multi-select
                            [refreshSubject]="${Utils.lowerCamel(f.type.element)}RefreshSubject"
                            [datasForLoadSubject]="${Utils.lowerCamel(f.type.element)}ForLoadSubject"
                            (datasSelected)="${Utils.lowerCamel(f.type.element)}Selected($event)"
                            [multiple]="true"
                            [service]="${Utils.lowerCamel(f.type.element)}Service"
                    ></common-multi-select>
                    <validation-error
                            [control]="myForm.get('${Utils.lowerCamel(f.name)}')"></validation-error>
                </div>
            </section>
            <!-----if is multi select and ouput single entity----->
            <!---------------------------------------------------->

            <#elseif f.selectOne>
            <!---------------------------------------------------->
            <!-----if is multi select and ouput single entity----->
            <input type="text"
                   style="display: none;"
                   formControlName="${Utils.lowerCamel(f.name)}"
                   [(ngModel)]="${Utils.lowerCamel(entity.name)}.${Utils.lowerCamel(f.name)}"
                   name="${Utils.lowerCamel(f.name)}"
                   placeholder="">

            <section class="col col-6">
                <div class="select"
                     [ngClass]="{'state-error': (!myForm.controls['${Utils.lowerCamel(f.name)}'].valid && myForm.controls['${Utils.lowerCamel(f.name)}'].touched)}">
                    ${Utils.spacedCapital(f.type.element)}

                   <#if f.required> <span style="color: red">*</span> </#if>
                    <common-multi-select
                            [refreshSubject]="${Utils.lowerCamel(f.type.element)}RefreshSubject"
                            [datasForLoadSubject]="${Utils.lowerCamel(f.type.element)}ForLoadSubject"
                            (datasSelected)="${Utils.lowerCamel(f.type.element)}Selected($event)"
                            [multiple]="false"
                            [service]="${Utils.lowerCamel(f.type.element)}Service"
                    ></common-multi-select>
                    <validation-error
                            [control]="myForm.get('${Utils.lowerCamel(f.name)}')"></validation-error>
                </div>
            </section>
            <!-----if is multi select and ouput single entity----->
            <!---------------------------------------------------->


            <#elseif f.type.name == "Boolean" || f.type.name == "boolean">
            <!---------------------------------------------------->
            <!-------------------if is switch------------------->
            <section class="col col-6">
                <label class="toggle"
                       [ngClass]="{'state-error': (!myForm.controls['${Utils.lowerCamel(f.name)}'].valid && myForm.controls['${Utils.lowerCamel(f.name)}'].touched)}">
                    ${Utils.spacedCapital(f.name)}<#if f.required> <span style="color: red">*</span> </#if>
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

            <#elseif f.type.name == "ZonedDateTime">
            <!--------------------------------------------------------->
            <!--------------------ZonedDateTime field----------------->
            <input type="date"
                   style="display: none;"
                   formControlName="${Utils.lowerCamel(f.name)}"
                   [(ngModel)]="${Utils.lowerCamel(entity.name)}.${Utils.lowerCamel(f.name)}"
                   name="${Utils.lowerCamel(f.name)}"
                   placeholder="">
              <section class="col col-6">
                  <label class="input"
                         [ngClass]="{'state-error': (!myForm.controls['${Utils.lowerCamel(f.name)}'].valid && myForm.controls['${Utils.lowerCamel(f.name)}'].touched)}">
                      ${Utils.spacedCapital(f.name)}<#if f.required> <span style="color: red">*</span> </#if>
                      <input ngui-datetime-picker
                             date-format="YYYY-MM-DD"
                             year="2014"
                             month="12"
                             day="31"
                             min-date="currentDate"
                             date-only="true"
                             placeholder="Please Choose Date"
                             formControlName="${Utils.lowerCamel(f.name)}"
                             [(ngModel)]="${Utils.lowerCamel(entity.name)}.${Utils.lowerCamel(f.name)}"
                             [close-on-select]="true"/>
                      <validation-error
                              [control]="myForm.get('${Utils.lowerCamel(f.name)}')"></validation-error>
                  </label>
              </section>
            <!--------------------ZonedDateTime field----------------->
            <!--------------------------------------------------------->

            <#elseif f.addDynamicMany>
            <!---------------------------------------------------->
            <!-------------if is add dynamic many entity---------->
            <div class="">
                <div formArrayName="${Utils.lowerCamel(f.name)}" id="${Utils.lowerCamel(f.name)}">
                    <div *ngFor="let ${Utils.lowerCamel(f.name)} of get${Utils.upperCamel(f.name)}FormData().controls; let i=index"
                         [ngClass]="get${Utils.upperCamel(f.name)}FormData().controls.length > 0 ? 'my-subform-border' : ''">
                        <div [formGroupName]="i">
                            <${Utils.lowerHyphen(f.type.element)}-subform-component
                            [group]="get${Utils.upperCamel(f.name)}FormData().controls[i]"
                            [${Utils.lowerCamel(f.type.element)}]="get${Utils.upperCamel(f.name)}(i)">
                        </${Utils.lowerHyphen(f.type.element)}-subform-component>

                        <div class="row">
                            <section class="col col-offset-8 col-4 align-center" style="margin-top: 20px;">
                                <a class="btn btn-danger"
                                   (click)="remove${Utils.upperCamel(f.name)}FormControl(i)">
                                    <i class="fa fa-remove"></i>
                                    Remove
                                </a>
                            </section>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <section class="col col-6 align-center">
                        <a class="btn btn-success btn-sm"
                           (click)="add${Utils.upperCamel(f.name)}FormControl()">
                            <i class="fa fa-plus"></i>
                            Add ${Utils.upperCamel(f.name)}
                        </a>
                    </section>
                </div>
            </div>
            </div>
            <!-------------if is add dynamic many entity---------->
            <!---------------------------------------------------->

            <#elseif f.richText>
            <div class="" style="margin: 0 15px"
                 [ngClass]="{'state-error': (!myForm.controls['${Utils.lowerCamel(f.name)}'].valid && myForm.controls['${Utils.lowerCamel(f.name)}'].touched)}">
                ${Utils.spacedCapital(f.name)}<#if f.required> <span style="color: red">*</span> </#if>
                <quill-editor formControlName="${Utils.lowerCamel(f.name)}"
                              [(ngModel)]="${Utils.lowerCamel(entity.name)}.${Utils.lowerCamel(f.name)}">
                </quill-editor>
            </div>
            <br>
            <br>

            <#elseif f.attachment>
            <!---------------------------------------------------->
            <!--------------------if is dropzone------------------>
            <input type="text"
                   style="display: none;"
                   formControlName="${Utils.lowerCamel(f.name)}"
                   [(ngModel)]="${Utils.lowerCamel(entity.name)}.${Utils.lowerCamel(f.name)}"
                   name="${Utils.lowerCamel(f.name)}"
                   placeholder="">

            <label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${Utils.spacedCapital(f.name)}
            <#if f.required> <span style="color: red">*</span> </#if>
            </label>
            <div class="row dropzone-area" style="margin: 15px 15px !important">
                <div class="" role="content">
                    <!-- widget content -->
                    <div class="widget-body"
                         [ngClass]="{'state-error': (!myForm.controls['${Utils.lowerCamel(f.name)}'].valid && myForm.controls['${Utils.lowerCamel(f.name)}'].touched)}">

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
            <!--------------------if is dropzone------------------>
            <!---------------------------------------------------->

            <#elseif f.textarea>
            <!--------------------------------------------------------->
            <!-----------------------textarea field-------------------->
           <div class="">
               <section class="col col-12">
                   <label class="textarea"
                          [ngClass]="{'state-error': (!myForm.controls['${Utils.lowerCamel(f.name)}'].valid && myForm.controls['${Utils.lowerCamel(f.name)}'].touched)}">
                       ${Utils.spacedCapital(f.name)}<#if f.required> <span style="color: red">*</span> </#if>
                       <textarea type="text"
                                 rows="4"
                                 cols="200"
                                 formControlName="${Utils.lowerCamel(f.name)}"
                                 [(ngModel)]="${Utils.lowerCamel(entity.name)}.${Utils.lowerCamel(f.name)}"
                                 name="${Utils.lowerCamel(f.name)}"
                                 placeholder="">
                      </textarea>
                       <validation-error
                               [control]="myForm.get('${Utils.lowerCamel(f.name)}')"></validation-error>
                   </label>
               </section>
           </div>
            <!-----------------------textarea field-------------------->
            <!--------------------------------------------------------->

            <#else>
            <!--------------------------------------------------------->
            <!--------------------else is normal field----------------->
              <section class="col col-6">
                  <label class="input"
                         [ngClass]="{'state-error': (!myForm.controls['${Utils.lowerCamel(f.name)}'].valid && myForm.controls['${Utils.lowerCamel(f.name)}'].touched)}">
                      ${Utils.spacedCapital(f.name)}
                      <#if f.required> <span style="color: red">*</span></#if>
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
        </#list>
    </div>
    </#list>

    <#list entity.formHiddenFields as f>
       <input type="text"
              style="display: none;"
              formControlName="${Utils.lowerCamel(f.name)}"
              [(ngModel)]="${Utils.lowerCamel(entity.name)}.${Utils.lowerCamel(f.name)}"
              name="${Utils.lowerCamel(f.name)}"
              placeholder="">
    </#list>
</fieldset>