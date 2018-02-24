import {Action} from '@ngrx/store';
import {${Utils.upperCamel(entity.name)} ActionConstants} from './${Utils.lowerHyphen(entity.name)}-action.constant';

export class Delete${Utils.upperCamel(entity.name)} Action implements Action {
    readonly type = ${Utils.upperCamel(entity.name)} ActionConstants.DELETE_${Utils.upperCamel(entity.name)} ;

    constructor(public payload: number) {
    }
}
