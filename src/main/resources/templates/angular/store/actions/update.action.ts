import {Action} from '@ngrx/store';
import {${Utils.upperCamel(entity.name)}ActionConstants} from './action.constants';
import {${Utils.upperCamel(entity.name)}Model} from '../../${Utils.lowerHyphen(entity.name)}.model';

export class Update${Utils.upperCamel(entity.name)}Action implements Action {
    readonly type = ${Utils.upperCamel(entity.name)}ActionConstants.UPDATE_${Utils.upperCamel(entity.name)};

    constructor(public payload: { index: number, updated${Utils.upperCamel(entity.name)}: ${Utils.upperCamel(entity.name)}Model }) {
    }
}
