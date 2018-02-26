import {Ingredient} from '../../shared/ingredient.model';
import {${Utils.upperCamel(entity.name)}Actions} from './actions/actions';
import {${Utils.upperCamel(entity.name)}ActionConstants} from './actions/action.constant';
import {${Utils.upperCamel(entity.name)}InitialStateConstant} from './initial-state.constant';

export function ${Utils.lowerCamel(entity.name)}sReducer(state = ${Utils.upperCamel(entity.name)}InitialStateConstant, action: ${Utils.upperCamel(entity.name)}Actions) {
    switch (action.type) {
        case (${Utils.upperCamel(entity.name)}ActionConstants.SET_${Utils.upperCamel(entity.name)}S):
            console.log('${Utils.upperCamel(entity.name)}Actions.SET_${Utils.upperCamel(entity.name)}S');
            return {
                ...state,
                ${Utils.lowerCamel(entity.name)}s: [...action.payload]
            };
        // case (${Utils.upperCamel(entity.name)}ActionConstants.ADD_${Utils.upperCamel(entity.name)}):
        //     return {
        //         ...state,
        //         ${Utils.lowerCamel(entity.name)}s: [...state.${Utils.lowerCamel(entity.name)}s, action.payload]
        //     };
        case (${Utils.upperCamel(entity.name)}ActionConstants.UPDATE_${Utils.upperCamel(entity.name)}):
            const ${Utils.lowerCamel(entity.name)}= state.${Utils.lowerCamel(entity.name)}s[action.payload.index];
            const updated${Utils.upperCamel(entity.name)}= {
                ...${Utils.lowerCamel(entity.name)},
                ...action.payload.updated${Utils.upperCamel(entity.name)}
            };
            const ${Utils.lowerCamel(entity.name)}s = [...state.${Utils.lowerCamel(entity.name)}s];
            ${Utils.lowerCamel(entity.name)}s[action.payload.index] = updated${Utils.upperCamel(entity.name)};
            return {
                ...state,
                ${Utils.lowerCamel(entity.name)}s: ${Utils.lowerCamel(entity.name)}s
            };
        case (${Utils.upperCamel(entity.name)}ActionConstants.DELETE_${Utils.upperCamel(entity.name)}):
            const old${Utils.upperCamel(entity.name)}s = [...state.${Utils.lowerCamel(entity.name)}s];
            old${Utils.upperCamel(entity.name)}s.splice(action.payload, 1);
            return {
                ...state,
                ${Utils.lowerCamel(entity.name)}s: old${Utils.upperCamel(entity.name)}s
            };
        default:
            return state;
    }
}
