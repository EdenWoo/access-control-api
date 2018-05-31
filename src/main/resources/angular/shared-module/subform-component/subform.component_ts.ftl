import {AfterViewInit, ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {FormGroup} from '@angular/forms';
import {Subject} from 'rxjs/Subject';
import {${Utils.upperCamel(entity.name)}Model} from '../../../pages/${Utils.lowerHyphen(entity.name)}/${Utils.lowerHyphen(entity.name)}.model';
import {ImageModel} from '../../../models/bases/image.model';
<#list entity.fields as f>
    <#if f.selectOne && f.type.element != entity.name>
    import {${Utils.upperCamel(f.type.element)}Service} from '../../../pages/${Utils.lowerHyphen(f.type.element)}/${Utils.lowerHyphen(f.type.element)}.service';
    </#if>
</#list>
import {${Utils.upperCamel(entity.name)}Service} from '../../../pages/${Utils.lowerHyphen(entity.name)}/${Utils.lowerHyphen(entity.name)}.service';

@Component({
    selector: '${Utils.lowerHyphen(entity.name)}-subform-component',
    templateUrl: '${Utils.lowerHyphen(entity.name)}-subform.component.html'
})
export class ${Utils.upperCamel(entity.name)}SubformComponent implements OnInit, AfterViewInit {

    @Input('group')
    public myForm: FormGroup;

    @Input('${Utils.lowerCamel(entity.name)}')
    public ${Utils.lowerCamel(entity.name)}: ${Utils.upperCamel(entity.name)}Model;

    // public ${Utils.lowerCamel(entity.name)}s: CompanyModel[];
    // public imageSubject: Subject<ImageModel[]> = new Subject<ImageModel[]>();


// =====================================================================
// =============================Dropzone Variable=======================
<#list entity.fields as f>
    <#if f.attachment>
        public ${Utils.lowerCamel(f.name)}ImageSubject: Subject<ImageModel[] | any> = new Subject<ImageModel[] | any>();
    </#if>
</#list>
// ============================Dropzone Variable========================
// =====================================================================

// =================================================================================
// =============================multi select subject Variable=======================
<#list entity.fields as f>
    <#if f.selectOne>
        public ${Utils.lowerCamel(f.type.element)}RefreshSubject = new Subject<any>();
        public ${Utils.lowerCamel(f.type.element)}ForLoadSubject = new Subject<any>();
    </#if>
</#list>
// ============================multi select subject Variable========================
// =================================================================================


// =================================================================================
// ===================================EnumList Variable=============================
<#list entity.fields as f>
    <#if f.type.name?contains("Enum")>
    public ${Utils.lowerCamel(f.type.name)}List: any;
    </#if>
</#list>
// ===================================EnumList Variable=============================
// =================================================================================


    constructor(public ref: ChangeDetectorRef,
public ${Utils.lowerCamel(entity.name)}Service: ${Utils.upperCamel(entity.name)}Service,
<#list entity.fields as f>
<#if f.selectOne && f.type.element != entity.name>
    public ${Utils.lowerCamel(f.type.element)}Service: ${Utils.upperCamel(f.type.element)}Service,
</#if>
</#list>) {
    }

    ngOnInit(): void {
        if (!this.${Utils.lowerCamel(entity.name)}) {
            this.${Utils.lowerCamel(entity.name)} = new ${Utils.upperCamel(entity.name)}Model();
        }
        // this.getCompanies();
this.getEnumList();
    }

    ngAfterViewInit() {
        // if (this.${Utils.lowerCamel(entity.name)}) {
        //     this.imageSubject.next(this.${Utils.lowerCamel(entity.name)}.brandImg);
        // }
    }

    equals(r1: any, r2: any) {
        if (r1 && r2) {
            return r1.pk === r2.pk;
        }
    }

    // getCompanies() {
    //     this.companyService.getAllFromStore().subscribe(resp => {
    //         console.log(resp);
    //         this.companies = resp.companys;
    //     });
    // }

    filesChanged($event) {
        console.log($event);
        // this.brandTag.brandImg = $event;
    }

// =====================================================================
// ===========================getEnumList===============================
getEnumList(){
<#list entity.fields as f>
    <#if f.type.name?contains("Enum")>
this.${Utils.lowerCamel(entity.name)}Service.getEnumList('${Utils.lowerCamel(f.type.name)}').subscribe((resp: any) => {
console.log(resp);
this.${Utils.lowerCamel(f.type.name)}List = resp;
});
    </#if>
</#list>
    }
// ===========================getEnumList===============================
// =====================================================================

// =====================================================================
// =======================Multi Select Event============================
<#list entity.fields as f>
    <#if f.selectOne>
        ${Utils.lowerCamel(f.type.element)}Selected($event) {
        this.${Utils.lowerCamel(entity.name)}.${Utils.lowerCamel(f.name)} = $event;
    }
    </#if>

</#list>
// =======================Multi Select Event============================
// =====================================================================

// =====================================================================
// =============================Dropzone================================
emitDropzoneFiles(){

<#list entity.fields as f>
    <#if f.attachment>
        this.${Utils.lowerCamel(f.name)}ImageSubject.next(this.${Utils.lowerCamel(entity.name)}.${Utils.lowerCamel(f.name)});
    </#if>
</#list>

}


<#list entity.fields as f>
    <#if f.attachment>
        ${Utils.lowerCamel(f.name)}FileObjectsChanged($event) {
        this.${Utils.lowerCamel(entity.name)}.${Utils.lowerCamel(f.name)} = $event;
    }
    </#if>
</#list>
// ============================Dropzone=================================
// =====================================================================

}
