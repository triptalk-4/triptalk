#!/bin/bash
REPOSITORY=/home/ec2-user/app/build/libs
PROJECT_NAME=triptalk

echo "> 현재 구동중인 애플리케이션 pid 확인" >> /home/ec2-user/app/deploy.log

CURRENT_PID=$(pgrep -fl $PROJECT_NAME)

echo "현재 구동중인 어플리케이션 pid: $CURRENT_PID" >> /home/ec2-user/app/deploy.log

if [ -z "$CURRENT_PID" ]; then
    echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다." >> /home/ec2-user/app/deploy.log
else
    echo "> kill -15 $CURRENT_PID"
    kill -15 $CURRENT_PID
    sleep 5
fi

echo "> 새 어플리케이션 배포" >> /home/ec2-user/app/deploy.log

JAR_NAME=$(ls -tr $REPOSITORY/*SNAPSHOT.jar)

echo "> JAR Name: $JAR_NAME"

echo "> $JAR_NAME 에 실행권한 추가"

chmod +x $JAR_NAME

echo "> $JAR_NAME 실행" >> /home/ec2-user/app/deploy.log

nohup java -jar \
    -Dspring.config.location=/home/ec2-user/config/application.yml \
    $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &