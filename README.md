docker run -p 6379:6379 redis

mvnw install
java -jar -Dspring.profiles.active=deploy-local -DREDIS_HOST=localhost -DREDIS_PORT=6379 -DDATA_INPUT_FOLDER=C:/desenv/input/ -DDATA_OUTPUT_FOLDER=C:/desenv/output/ sales-report-0.0.1-SNAPSHOT.jar

docker build -t company/sales-report .
docker run -v c:/desenv/input/:/tmp/input/ -v c:/desenv/output/:/tmp/output/ -e REDIS_PORT=6379 -e REDIS_HOST=192.168.0.8 -t company/sales-report

docker run -v c:/desenv/input/:/tmp/input/ -v c:/desenv/output/:/tmp/output/ -e REDIS_PORT=6379 -e REDIS_HOST=192.168.0.8 company/sales-report