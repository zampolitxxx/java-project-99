### Hexlet tests and linter status:
[![Actions Status](https://github.com/zampolitxxx/java-project-99/actions/workflows/hexlet-check.yml/badge.svg)](https://github.com/zampolitxxx/java-project-99/actions)

*[Follow me on Render](https://task-manager-application.onrender.com/)

### Codeclimate badges
[![Maintainability](https://api.codeclimate.com/v1/badges/e2196581e1847365f2b8/maintainability)](https://codeclimate.com/github/zampolitxxx/java-project-99/maintainability)

[![Test Coverage](https://api.codeclimate.com/v1/badges/e2196581e1847365f2b8/test_coverage)](https://codeclimate.com/github/zampolitxxx/java-project-99/test_coverage)

### Local development

### Run
```shell
export SPRING_PROFILES_ACTIVE=development
make generate_rsa
```

### Build
```shell
./gradlew --no-daemon dependencies
./gradlew --no-daemon build
```

### Run
```shell
java -jar build/libs/app-0.0.1-SNAPSHOT.jar
```

### Build docker
```shell
docker build -t task-manager-app .
```

### Run docker
```shell
docker run -dp 127.0.0.1:8080:8080 task-manager-app
```
### Deploy on render
Need set enviroment variables:

* DATABASE_URL - DB Url in format: jdbc:postgresql://host:port/databaseName
* DATABASE_PASSWORD - DB password
* DATABASE_USERNAME - DB login
* SPRING_PROFILES_ACTIVE = production

Also, need add 2 secret files:
* private.pem - RSA private key.
* public.pem - RSA publick key.