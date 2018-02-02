import {Injectable, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable()
export class ${Utils.upperCamel(entity.name)}Service extends BaseService implements OnInit {

    constructor(public http: HttpClient) {
        super('${Utils.lowerHyphen(entity.name)}', http);
    }

    ngOnInit(): void {
    }
}