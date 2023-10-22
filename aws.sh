git reset --hard HEAD
git pull
./gradlew build -x test
pkill -f 'java -jar'
nohup java -jar -Xmx512m -Xms512m build/libs/kipris-search-0.0.1-SNAPSHOT.jar &
