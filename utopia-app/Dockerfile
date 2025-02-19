# 基础镜像
FROM openjdk:8

# 作者
LABEL maintainer="Aseubel"

# 配置

# 时区
ENV TZ=PRC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 添加应用
ADD target/utopia-app.jar /utopia-app.jar

ENTRYPOINT ["sh","-c","java -jar $JAVA_OPTS /utopia-app.jar $PARAMS"]