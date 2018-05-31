
// in init form control

${Utils.lowerCamel(entity.name)}s: this.formBuiler.array([])



// ================================put in subform's parent form========================================
// ===============================${Utils.upperCamel(entity.name)} SubForm============================
load${Utils.upperCamel(entity.name)}Form() {
    const ${Utils.lowerCamel(entity.name)}Ctrls = (<FormArray>this.myForm.controls['${Utils.lowerCamel(entity.name)}s']).controls;

    if (${Utils.lowerCamel(entity.name)}Ctrls && this.brandCompany.brand.${Utils.lowerCamel(entity.name)}s) {
        while (${Utils.lowerCamel(entity.name)}Ctrls.length < this.brandCompany.brand.${Utils.lowerCamel(entity.name)}s.length) {
            this.add${Utils.upperCamel(entity.name)}FormControl();
        }
    }
    // this.ref.markForCheck();
    // this.ref.detectChanges();
}

add${Utils.upperCamel(entity.name)}FormControl() {
    const control = <FormArray>this.myForm.controls['${Utils.lowerCamel(entity.name)}s'];
    const addrCtrl = this.init${Utils.upperCamel(entity.name)}FormControl();
    control.push(addrCtrl);
}

remove${Utils.upperCamel(entity.name)}FormControl(i: number) {
    const control = <FormArray> this.myForm.controls['${Utils.lowerCamel(entity.name)}s'];
    control.removeAt(i);
}

init${Utils.upperCamel(entity.name)}FormControl() {
   return this.formBuiler.group({
      <#list entity.fields as f>
          <#assign validationStr = "">
          <#assign finalValidationStr = "">
          <#assign ansycValidationStr = "">
          <#assign finalAnsycValidationStr = "">
          <#if f.required>
              <#assign validationStr += 'Validators.required,'>
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
              <#assign ansycValidationStr = "ValidationPattern.duplicateValidate('${f.name}', this.${Utils.lowerCamel(entity.name)}Service)">
          </#if>
      
      
          <#if f.type.name == "Int" || f.type.name == "Double">
      
          </#if>
      
      
          <#if validationStr?? && validationStr!= "">
              <#assign finalValidationStr += "validators:[${validationStr}],">
          </#if>
      
          <#if ansycValidationStr != "">
              <#assign finalAnsycValidationStr += "asyncValidators:[${ansycValidationStr}],">
          </#if>
      
          // ${Utils.lowerCamel(f.name)}: ['', [${validationStr}], ${ansycValidationStr}],
      
          ${Utils.lowerCamel(f.name)}: new FormControl('', {
                      ${finalValidationStr}
                      ${finalAnsycValidationStr}
                      updateOn: 'blur'
                  }),
      
      </#list>
    });
}

get${Utils.upperCamel(entity.name)}(i: number): ${Utils.upperCamel(entity.name)}Model {
    if (this.isEdit && this.model && this.model.${Utils.lowerCamel(entity.name)}s) {
        return this.model.${Utils.lowerCamel(entity.name)}s[i];
    }
}

get${Utils.upperCamel(entity.name)}FormData() {
    return <FormArray>this.myForm.get('${Utils.lowerCamel(entity.name)}s');
}

// ===============================${Utils.upperCamel(entity.name)} SubForm============================
// ========================================================================

//====================put in parent html ===================

 <!--<div class="row"></div>-->
<div class="row">
<div formArrayName="${Utils.lowerCamel(entity.name)}s" id="${Utils.lowerCamel(entity.name)}s">
    <div *ngFor="let ${Utils.lowerCamel(entity.name)} of get${Utils.upperCamel(entity.name)}FormData().controls; let i=index" [ngClass]="get${Utils.upperCamel(entity.name)}FormData().controls.length > 0 ? 'my-subform-border' : ''">
        <div [formGroupName]="i">
            <${Utils.lowerHyphen(entity.name)}-subform-component
                    [group]="get${Utils.upperCamel(entity.name)}FormData().controls[i]"
                    [${Utils.lowerCamel(entity.name)}]="get${Utils.upperCamel(entity.name)}(i)"></${Utils.lowerHyphen(entity.name)}-subform-component>

            <div class="row">
                <section class="col col-offset-8 col-4 align-center" style="margin-top: 20px;">
                    <a class="btn btn-danger"
                       (click)="remove${Utils.upperCamel(entity.name)}FormControl(i)">
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
               (click)="add${Utils.upperCamel(entity.name)}FormControl()">
                <i class="fa fa-plus"></i>
                Add ${Utils.upperCamel(entity.name)}
            </a>
        </section>
    </div>
</div>
</div>
// ========================================================================
