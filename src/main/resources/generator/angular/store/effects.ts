import {Injectable} from '@angular/core';
import {Actions, Effect} from '@ngrx/effects';
import 'rxjs/add/operator/switchMap';
import 'rxjs/add/operator/withLatestFrom';
import {HttpClient} from '@angular/common/http';
import {Store} from '@ngrx/store';
import {${Utils.upperCamel(entity.name)}ActionConstants} from './actions/action.constant';
import {Fetch${Utils.upperCamel(entity.name)}Action} from './actions/fetch.action';
import {${Utils.upperCamel(entity.name)}Service} from '../${Utils.lowerHyphen(entity.name)}.service';
import {${Utils.upperCamel(entity.name)}FeatureStateInterface} from './interfaces/feature-state.interface';

@Injectable()
export class ${Utils.upperCamel(entity.name)}Effects {
    @Effect()
    ${Utils.lowerCamel(entity.name)}Fetch = this.actions$
        .ofType(${Utils.upperCamel(entity.name)}ActionConstants.FETCH_${Utils.upperCamel(entity.name)}S)
        .switchMap((action: Fetch${Utils.upperCamel(entity.name)}Action) => {

            console.log('${Utils.upperCamel(entity.name)}Actions.FETCH_${Utils.upperCamel(entity.name)}S');
            return this.${Utils.lowerCamel(entity.name)}Service.getAll();
        })
        .map(
            (${Utils.lowerCamel(entity.name)}s) => ({
                // type: ${Utils.upperCamel(entity.name)}ActionConstants.SET_${Utils.upperCamel(entity.name)}S,
                type: ${Utils.upperCamel(entity.name)}ActionConstants.SET_${Utils.upperCamel(entity.name)}S,
                payload: ${Utils.lowerCamel(entity.name)}s.results
            }));
    //
    // @Effect({dispatch: false})
    // ${Utils.lowerCamel(entity.name)}Store = this.actions$
    //     .ofType(${Utils.upperCamel(entity.name)}ActionConstants.STORE_${Utils.upperCamel(entity.name)}S)
    //     .withLatestFrom(this.store.select('${Utils.lowerCamel(entity.name)}s'))
    //     .switchMap(([action, state]) => {
    //         const req = new HttpRequest('PUT', 'https://ng-${Utils.lowerCamel(entity.name)}-book-3adbb.firebaseio.com/${Utils.lowerCamel(entity.name)}s.json', state.${Utils.lowerCamel(entity.name)}s, {reportProgress: true});
    //         return this.httpClient.request(req);
    //     });

    constructor(private actions$: Actions,
                private httpClient: HttpClient,
                private ${Utils.lowerCamel(entity.name)}Service: ${Utils.upperCamel(entity.name)}Service,
                private store: Store<${Utils.upperCamel(entity.name)}FeatureStateInterface>) {
    }

}
