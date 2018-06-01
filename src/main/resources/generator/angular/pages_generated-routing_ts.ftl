import {AuthGuard} from '../core/guards/auth.guard';

export const PagesGeneratedRoutes = [
<#list project.entities as e>
    {path: '${Utils.lowerHyphen(e.name)}', loadChildren: 'app/pages/${Utils.lowerHyphen(e.name)}/${Utils.lowerHyphen(e.name)}.module#${Utils.upperCamel(e.name)}Module' ,canActivate: [AuthGuard]},
</#list>
]