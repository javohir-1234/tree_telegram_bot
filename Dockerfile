FROM openjdk:17
ARG jarFile=./target/telegram-telegram.jar
ADD ${jarFile} file

ENTRYPOINT ["java", "-jar", "file"]

LABEL maintainer="Java <javohirilyasov26@gmail.com>"
LABEL version="1.0"
LABEL description="Telegram App"
