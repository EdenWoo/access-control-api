import {Injectable, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BaseService} from '../../shared-module/bases/base.service';

@Injectable()
export class ${Utils.upperCamel(entity.name)}Service extends BaseService implements OnInit {

    constructor(public http: HttpClient) {
        super('${Utils.lowerHyphen(entity.name)}', http);
    }

    ngOnInit(): void {
    }
}