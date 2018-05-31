import {ModuleWithProviders} from '@angular/core'
import {RouterModule, Routes} from '@angular/router';
import {PagesComponent} from './pages.component';
import {AuthGuard} from '../core/guards/auth.guard';

export const routes: Routes = [
    {
        path: '',
        component: PagesComponent,
        children: [
            // {path: '', redirectTo: '', pathMatch: 'full'},
            <#list project.entities as e>
            {path: '${Utils.lowerHyphen(e.name)}', loadChildren: 'app/pages/${Utils.lowerHyphen(e.name)}/${Utils.lowerHyphen(e.name)}.module#${Utils.upperCamel(e.name)}Module' ,canActivate: [AuthGuard]},

            </#list>
        ]
    }
];

export const routing = RouterModule.forChild(routes)
