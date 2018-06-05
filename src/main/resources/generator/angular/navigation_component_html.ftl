<aside id="left-panel">

    <!-- User info -->
    <sa-login-info></sa-login-info>
    <!-- end user info -->

    <nav>
        <ul saSmartMenu>
            <li *ngFor="let nav of navigations">
                <a [routerLink]="nav.routerLink" [title]="nav.title"><i [class]="nav.icon"></i> <span
                        class="menu-item-parent">{{nav.display | translate}}</span></a>
            </li>
           <#list project.entities as e>
                <li>
                    <a routerLink="/pages/${Utils.lowerHyphen(e.name)}/${Utils.lowerHyphen(e.name)}-list" title="${Utils.upperCamel(e.name)}"><i class="fa fa-lg fa-fw fa-chevron-right"></i> <span
                            class="menu-item-parent">{{'${Utils.spacedCapital(e.name)}' | translate}}</span></a>
                </li>
            </#list>

        </ul>

    </nav>

    <sa-minify-menu></sa-minify-menu>

</aside>
