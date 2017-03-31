PROJECT_PATH=/root/project/my-code/
APP_PATH=/root/app/

cd $PROJECT_PATH
git reset --hard
git pull

mvn clean install -DskipTests -Ptar

cp /target/*-publish.tar.gz $APP_PATH

tar -xvf $APP_PATH*-publish.tar.gz