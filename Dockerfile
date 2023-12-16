FROM openjdk:17
ARG jarFile=./target/telegram_tree_bot-0.0.1-SNAPSHOT.jar
ADD ${jarFile} file
ENTRYPOINT ["java", "-jar", "file"]