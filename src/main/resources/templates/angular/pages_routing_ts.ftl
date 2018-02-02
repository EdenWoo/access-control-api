import {ModuleWithProviders} from '@angular/core'
import {RouterModule, Routes} from '@angular/router';
import {PagesComponent} from './pages.component';

export const routes: Routes = [
    {
        path: '',
        component: PagesComponent,
        children: [
            // {path: '', redirectTo: '', pathMatch: 'full'},
            <#list project.entities as e>
            {path: '${Utils.lowerCamel(e.name)}', loadChildren: 'app/pages/${Utils.lowerCamel(e.name)}/${Utils.lowerCamel(e.name)}.module#${Utils.upperCamel(e.name)}Module'},
            </#list>
        ]
    }
];

export const routing = RouterModule.forChild(routes)
