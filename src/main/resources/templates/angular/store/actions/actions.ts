import {Add${Utils.upperCamel(entity.name)} Action} from './add-${Utils.lowerHyphen(entity.name)}.action';
import {Delete${Utils.upperCamel(entity.name)} Action} from './delete-${Utils.lowerHyphen(entity.name)}.action';
import {Fetch${Utils.upperCamel(entity.name)} Action} from './fetch-${Utils.lowerHyphen(entity.name)}.action';
import {Set${Utils.upperCamel(entity.name)} Action} from './set-${Utils.lowerHyphen(entity.name)}.action';
import {Update${Utils.upperCamel(entity.name)} Action} from './update-${Utils.lowerHyphen(entity.name)}.action';
import {Store${Utils.upperCamel(entity.name)} Action} from './store-${Utils.lowerHyphen(entity.name)}.action';

export type ${Utils.upperCamel(entity.name)} Actions = Set${Utils.upperCamel(entity.name)} Action |
    Add${Utils.upperCamel(entity.name)} Action |
    Update${Utils.upperCamel(entity.name)} Action |
    Delete${Utils.upperCamel(entity.name)} Action |
    Store${Utils.upperCamel(entity.name)} Action |
    Fetch${Utils.upperCamel(entity.name)} Action;
