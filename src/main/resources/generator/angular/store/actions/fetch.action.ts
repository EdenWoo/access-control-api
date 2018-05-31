import {Action} from '@ngrx/store';
import {${Utils.upperCamel(entity.name)}ActionConstants} from './action.constant';

export class Fetch${Utils.upperCamel(entity.name)}Action implements Action {
    // readonly type = ${Utils.upperCamel(entity.name)}ActionConstants.FETCH_ADMINUSERS;
    readonly type = ${Utils.upperCamel(entity.name)}ActionConstants.FETCH_${Utils.upperCamel(entity.name)}S;

    constructor(public payload: { }) {
    }
}
