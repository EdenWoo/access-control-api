import {Action} from '@ngrx/store';
import {${Utils.upperCamel(entity.name)} ActionConstants} from './${Utils.lowerHyphen(entity.name)}-action.constants';

export class Store${Utils.upperCamel(entity.name)} Action implements Action {
    readonly type = ${Utils.upperCamel(entity.name)} ActionConstants.STORE_${Utils.upperCamel(entity.name)} S;
    public payload: any;
}
