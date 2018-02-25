import {Injectable, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BaseService} from '../../shared-module/bases/base.service';

import {Store} from '@ngrx/store';
import {${Utils.upperCamel(entity.name)}FeatureStateInterface} from './store/interfaces/feature-state.interface';
import {Fetch${Utils.upperCamel(entity.name)}Action} from './store/actions/fetch-${Utils.lowerHyphen(entity.name)} .action';

@Injectable()
export class ${Utils.upperCamel(entity.name)}Service extends BaseService implements OnInit {

    constructor(public http: HttpClient,
public store: Store<${Utils.upperCamel(entity.name)}FeatureStateInterface>) {
        super('${Utils.lowerHyphen(entity.name)}', http);
    }

    ngOnInit(): void {
    }

fetchAll() {
        this.store.dispatch(new Fetch${Utils.upperCamel(entity.name)}Action({}));
    }

    getAllFromStore() {
        return this.store.select(o => o.${Utils.lowerCamel(entity.name)}List);
    }
}