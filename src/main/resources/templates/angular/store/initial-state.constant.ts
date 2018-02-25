import {${Utils.upperCamel(entity.name)}StateInterface} from './interfaces/state.interface';

export const ${Utils.upperCamel(entity.name)}InitialStateConstant: ${Utils.upperCamel(entity.name)}StateInterface = {
    ${Utils.lowerCamel(entity.name)}s: []
};