* 
git submodule add  git@bitbucket.org:cfgglobal/access-control-api.git  access-control-api


gradle
    compile project(':access-control-api')
    
    
    include "access-control-api"
    include "trader-api"
    
    project(':access-control-api').projectDir = "$rootDir/access-control-api" as File
    project(':trader-api').projectDir = "$rootDir/trader-api" as File
    
    

