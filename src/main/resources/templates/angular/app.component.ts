import {Component, ViewContainerRef} from '@angular/core';

<#list project.entities as e>
import {${Utils.upperCamel(e.name)}Service} from './pages/${Utils.lowerHyphen(e.name)}/${Utils.lowerHyphen(e.name)}.service';
 </#list>

@Component({
  selector: 'app-root',
  template: '<router-outlet></router-outlet>'
})
export class AppComponent {
  public title = 'app works!';

  public constructor(private viewContainerRef: ViewContainerRef,
                    <#list project.entities as e>
                      public ${Utils.lowerCamel(e.name)}Service: ${Utils.upperCamel(e.name)}Service,
                    </#list>
                    ) {
      <#list project.entities as e>
      this.${Utils.lowerCamel(e.name)}Service.fetchAll();
      </#list>

  }

}
