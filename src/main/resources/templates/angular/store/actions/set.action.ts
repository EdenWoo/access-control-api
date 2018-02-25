import {Action} from '@ngrx/store';
import {${Utils.upperCamel(entity.name)}ActionConstants} from './admin-user-action.constants';
import {${Utils.upperCamel(entity.name)}Model} from '../../${Utils.lowerHyphen(entity.name)}.model';

export class Set${Utils.upperCamel(entity.name)}Action implements Action {
    readonly type = ${Utils.upperCamel(entity.name)}ActionConstants.SET_${Utils.upperCamel(entity.name)}S;

    constructor(public payload: ${Utils.upperCamel(entity.name)}Model[]) {
    }
}
