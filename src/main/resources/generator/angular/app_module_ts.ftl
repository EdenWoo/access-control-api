import {NgModule, ApplicationRef} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {Http} from '@angular/http';

import {FormsModule} from '@angular/forms';
import {StoreModule} from '@ngrx/store';
import {EffectsModule} from '@ngrx/effects';
import {environment} from '../environments/environment';
import {StoreDevtoolsModule} from '@ngrx/store-devtools';
import {ToastContainerModule, ToastNoAnimationModule, ToastrModule, ToastrService} from 'ngx-toastr';
import {ConfirmationPopoverModule} from 'angular-confirmation-popover';

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

import {DropzoneConfigInterface, DropzoneModule} from 'ngx-dropzone-wrapper';

import {Constants} from './constants/app.constant';

import {HelperService} from './services/helper.service';
import {MyNotifyService} from './services/my-notify.service';
import {AuthenticationService} from './services/authentication.service';
import {HTTP_INTERCEPTORS, HttpClient, HttpClientModule} from '@angular/common/http';
import {AuthInterceptor} from './services/auth.interceptor';
import {RespInterceptor} from 'app/services/resp.interceptor';
import {DownloadService} from './services/download.service';
import {AuthGuard} from './core/guards/auth.guard';

<#list project.entities as e>
import {${Utils.upperCamel(e.name)}Service} from './pages/${Utils.lowerHyphen(e.name)}/${Utils.lowerHyphen(e.name)}.service';
</#list>

import {TranslateLoader, TranslateModule} from '@ngx-translate/core';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';

export function createTranslateLoader(http: HttpClient) {
    return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

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

const DROPZONE_CONFIG: DropzoneConfigInterface = {
    // Change this to your upload POST address:
    url: Constants.API_ENDPOINT + 'v1/attachment/upload',
    maxFilesize: 50,
    // acceptedFiles: 'image/*, application/pdf, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,application/vnd.ms-excel',
    acceptedFiles: '',
    addRemoveLinks: true,
    clickable: true
};

/**
 * `AppModule` is the main entry point into Angular2's bootstraping process
 */
@NgModule({
    bootstrap: [AppComponent],
    declarations: [
        AppComponent,

    ],
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        FormsModule,
        ConfirmationPopoverModule.forRoot({
            confirmButtonType: 'danger' // set defaults here
        }),
        ModalModule.forRoot(),
        DropzoneModule.forRoot(DROPZONE_CONFIG),

        ToastrModule.forRoot({
            timeOut: 5000,
            positionClass: 'toast-top-right',
            preventDuplicates: true,
        }),
        // ToastContainerModule.forRoot(),
        ToastNoAnimationModule,
        CoreModule,
        SmartadminLayoutModule,

        routing,
         TranslateModule.forRoot({
                    loader: {
                        provide: TranslateLoader,
                        useFactory: (createTranslateLoader),
                        deps: [HttpClient]
                    }
                }),
    ],
    exports: [],
    providers: [ // expose our Services and Providers into Angular's dependency injection
        // ENV_PROVIDERS,
AuthGuard,
        APP_PROVIDERS,
AuthenticationService,
{provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true},
        {provide: HTTP_INTERCEPTORS, useClass: RespInterceptor, multi: true},
        AppReadyEvent,
        HelperService,
DownloadService,
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

