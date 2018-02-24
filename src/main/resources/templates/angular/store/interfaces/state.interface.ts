import {${Utils.upperCamel(entity.name)} Model} from '../../${Utils.lowerHyphen(entity.name)}.model';

export interface ${Utils.upperCamel(entity.name)} StateInterface {
    ${Utils.lowerCamel(entity.name)}s: ${Utils.upperCamel(entity.name)} Model[];

}