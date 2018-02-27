import {NgModule, ApplicationRef} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {Http} from '@angular/http';

import {FormsModule} from '@angular/forms';
import {StoreModule} from '@ngrx/store';
import {AppReducerConstants} from './app-store/app-reducers.constant';
import {EffectsModule} from '@ngrx/effects';
import {environment} from '../environments/environment';
import {StoreDevtoolsModule} from '@ngrx/store-devtools';
/*
 * Platform and Environment providers/directives/pipes
 */
import {routing} from './app.routing'
// App is our top level component
import {AppComponent} from './app.component';
import {APP_RESOLVER_PROVIDERS} from './app.resolver';
import {AppState, InternalStateType} from './app.service';

// Core providers
import {CoreModule} from './core/core.module';
import {SmartadminLayoutModule} from './shared/layout/layout.module';


import {ModalModule} from 'ngx-bootstrap/modal';
import {AppReadyEvent} from './app-ready.component';

import {HelperService} from './services/helper.service';
import {MyNotifyService} from './services/my-notify.service';
import {AuthenticationService} from './services/authentication.service';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {AuthInterceptor} from './services/auth.interceptor';
import {RespInterceptor} from 'app/services/resp.interceptor';
<#list project.entities as e>
import {${Utils.upperCamel(e.name)}Service} from './pages/${Utils.lowerHyphen(e.name)}/${Utils.lowerHyphen(e.name)}.service';
</#list>


// Application wide providers
const APP_PROVIDERS = [
    ...APP_RESOLVER_PROVIDERS,
    AppState
];

type StoreType = {
    state: InternalStateType,
    restoreInputValues: () => void,
    disposeOldHosts: () => void
};

/**
 * `AppModule` is the main entry point into Angular2's bootstraping process
 */
@NgModule({
    bootstrap: [AppComponent],
    declarations: [
        AppComponent,

    ],
    imports: [ // import Angular's modules
        BrowserModule,
        BrowserAnimationsModule,
        FormsModule,

        ModalModule.forRoot(),


        CoreModule,
        SmartadminLayoutModule,

        routing,

        StoreModule.forRoot(AppReducerConstants), // eargly loading not lazy loading.
        EffectsModule.forRoot([]),
        !environment.production ? StoreDevtoolsModule.instrument() : []
    ],
    exports: [],
    providers: [ // expose our Services and Providers into Angular's dependency injection
        // ENV_PROVIDERS,
        APP_PROVIDERS,
AuthenticationService,
{provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true},
        {provide: HTTP_INTERCEPTORS, useClass: RespInterceptor, multi: true},
        AppReadyEvent,
        HelperService,
MyNotifyService,
<#list project.entities as e>
    ${Utils.upperCamel(e.name)}Service,
</#list>
    ]
})
export class AppModule {
    constructor(public appRef: ApplicationRef, public appState: AppState) {
    }


}

