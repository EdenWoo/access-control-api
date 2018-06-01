 <!--######################### HTML ###############################-->
 <input type="text"
       style="display: none;"
       formControlName=" ${Utils.lowerCamel(entity.name)}"
       [(ngModel)]="model. ${Utils.lowerCamel(entity.name)}"
       name=" ${Utils.lowerCamel(entity.name)}"
       placeholder="" />

<section class="col col-6">
    <label class="select">
        ${Utils.upperCamel(entity.name)}<span style="color: red">*</span>
        <label style="font-size:10px !important;"
               type="button"
               class="btn clear1 pull-right btn-primary btn-xs">Add new ${Utils.upperCamel(entity.name)}
            </label>
        <${Utils.lowerHyphen(entity.name)}-multi-select
                (datasSelected)=" ${Utils.lowerCamel(entity.name)}Selected($event)"
                [multiple]="false"
        ></${Utils.lowerHyphen(entity.name)}-multi-select>
        <validation-error
                [control]="myForm.get(' ${Utils.lowerCamel(entity.name)}')"></validation-error>
    </label>
</section>


 <!--######################### component ###############################-->

 ${Utils.lowerCamel(entity.name)}Selected($event) {
        this.model. ${Utils.lowerCamel(entity.name)} = $event;
    }