## add

### in git
git submodule add https://github.com/b1412/access-control-api.git access-control-api

### in gradle
    
   compile project(':access-control-api') 
   include "access-control-api"
   include "trader-api"
   project(':access-control-api').projectDir = "$rootDir/access-control-api" as File
   project(':trader-api').projectDir = "$rootDir/trader-api" as File
    
    



## remove
git submodule deinit -f -- access-control-api 
rm -rf .git/modules/access-control-api 
git rm  access-control-api 