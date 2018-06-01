<#list project.entities as e>
import {${Utils.upperCamel(e.name)}SubformComponent} from './subform-components/${Utils.lowerHyphen(e.name)}/${Utils.lowerHyphen(e.name)}-subform.component';
</#list>

export const SharedGeneratedComponents = [
<#list project.entities as e>
    ${Utils.upperCamel(e.name)}SubformComponent,
</#list>
];
