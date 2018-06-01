<#list project.entities as e>
    import {${Utils.upperCamel(e.name)}StateInterface} from '../pages/${Utils.lowerHyphen(e.name)}/store/interfaces/state.interface';
 </#list>

export interface AppStateInterface {
    <#list project.entities as e>
        ${Utils.lowerCamel(e.name)}List: ${Utils.upperCamel(e.name)}StateInterface,
    </#list>
}
