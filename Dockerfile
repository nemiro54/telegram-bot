FROM amazoncorretto:18
ARG JAR_FILE=target/*.jar
ENV BOT_NAME=0
ENV BOT_TOKEN=0
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-D bot.username=${BOT_NAME}", "-D bot.token=${BOT_TOKEN}", "-jar", "/app.jar"]