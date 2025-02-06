# Используем официальный образ OpenJDK 17
FROM openjdk:17-alpine

# Установка необходимых пакетов
RUN apk update && apk add curl unzip

# Копируем исходные коды в контейнер
COPY target/Weather-0.0.1-SNAPSHOT.jar /app.jar

# Установка переменных окружения
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Экспортируем порт для доступа к приложению
EXPOSE 8081

# Запуск приложения
CMD ["java", "-jar", "/app.jar"]