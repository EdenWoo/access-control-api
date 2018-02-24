import {${Utils.upperCamel(entity.name)} Model} from '../../${Utils.lowerHyphen(entity.name)}.model';
import {AppStateInterface} from '../../../../ngrx/app-store/app-state.interface';

export interface ${Utils.upperCamel(entity.name)} FeatureStateInterface extends AppStateInterface {
    ${Utils.lowerCamel(entity.name)}s: ${Utils.upperCamel(entity.name)} Model[];
}

