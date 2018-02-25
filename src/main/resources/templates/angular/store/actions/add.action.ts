import {Action} from '@ngrx/store';
import {${Utils.upperCamel(entity.name)}Model} from '../../${Utils.lowerHyphen(entity.name)}.model';
import {${Utils.upperCamel(entity.name)}ActionConstants} from './${Utils.lowerHyphen(entity.name)}-action.constant';

export class Add${Utils.upperCamel(entity.name)}Action implements Action {
    readonly type = ${Utils.upperCamel(entity.name)}ActionConstants.SET_${Utils.upperCamel(entity.name)}S;

    constructor(public payload: ${Utils.upperCamel(entity.name)}Model[]) {
    }
}
