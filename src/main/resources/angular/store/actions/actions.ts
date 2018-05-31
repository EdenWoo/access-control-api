import {Add${Utils.upperCamel(entity.name)}Action} from './add.action';
import {Delete${Utils.upperCamel(entity.name)}Action} from './delete.action';
import {Fetch${Utils.upperCamel(entity.name)}Action} from './fetch.action';
import {Set${Utils.upperCamel(entity.name)}Action} from './set.action';
import {Update${Utils.upperCamel(entity.name)}Action} from './update.action';
import {Store${Utils.upperCamel(entity.name)}Action} from './store.action';

export type ${Utils.upperCamel(entity.name)}Actions = Set${Utils.upperCamel(entity.name)}Action |
    Add${Utils.upperCamel(entity.name)}Action |
    Update${Utils.upperCamel(entity.name)}Action |
    Delete${Utils.upperCamel(entity.name)}Action |
    Store${Utils.upperCamel(entity.name)}Action |
    Fetch${Utils.upperCamel(entity.name)}Action;
