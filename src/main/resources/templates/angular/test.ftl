package name ${project.packageName}

entity.name   ${entity.name}
entity.name.uppperCamel2LowerHyphen   ${Utils.hyphen(entity.name)}
entity.name.uppercase   ${entity.name}


<#list project.entities as e>
     {path: '${Utils.lowerCamel(e.name)}', loadChildren: 'app/pages/${Utils.lowerCamel(e.name)}/${Utils.lowerCamel(e.name)}.module#${Utils.upperCamel(entity.name)}Module'},
</#list>