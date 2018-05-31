import {ActionReducerMap} from '@ngrx/store';
import {AppStateInterface} from './app-state.interface';

<#list project.entities as e>
import {${Utils.lowerCamel(e.name)}sReducer} from '../pages/${Utils.lowerHyphen(e.name)}/store/reducers';
</#list>

export const AppReducerConstants: ActionReducerMap<AppStateInterface> = {
    <#list project.entities as e>
        ${Utils.lowerCamel(e.name)}List: ${Utils.lowerCamel(e.name)}sReducer,
    </#list>
};
