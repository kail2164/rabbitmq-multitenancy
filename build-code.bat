cd .\common\
call .\mvnw clean install -Dmaven.test.skip=true
cd ..\account\
call .\mvnw clean package
cd ..\product\
call .\mvnw clean package
