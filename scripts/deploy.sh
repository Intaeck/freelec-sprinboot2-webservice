#!/bin/bash

REPOSITORY=/home/ec2-user/app/step2
PROJECT_NAME=freelec-sprinboot2-webservice

echo "> Biuld 파일복사"
cp $REPOSITORY/zip/*.jar $REPOSITORY/

echo "> 현재 구동 중인 애플리케이션 pid 확인"
CURRENT_PID=$(pgrep -fl freelec-sprinboot2-webservice | grep jar | awk '{print $1}')
echo "> 현재 구동 중인 애플리케이션 pid: $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
  echo "> 현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다"
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> 새 애플리케이션 배포"
JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)
echo "> JAR Name: $JAR_NAME"
echo "> JAR_NAME에 실행권한 추가"
chmod +x $JAR_NAME

echo "> $JAR_NAME 실행"
nohup java -jar \
-Dspring.config.location=classpath:/application.properties,classpath:/application-real.properties,/home/ec2-user/app/application-oauth.properties,/home/ec2-user/app/application-real-db.properties \
-Dspring.profiles.active=real \
$JAR_NAME > $REPOSITORY/nohup.out 2>&1 &
#nohup 실행 시 CodeDeploy는 무한대기를 하는 이슈를 해결하기 위해 nohup.out 파일을 표준 입출력용으로 별도로 사용
#이렇게 하지 않으면 nohup.out 파일이 생기지 않고, CodeDeploy로그에 표준입출력이 출력됨
#nohup이 끝나기 전까지 CodeDeploy도 끝나지 않으니 꼭 이렇게 해야함

# 2>&1은 stderr(표준에러)를 stdout(표준출력)으로 redirection하라는 명령어.
#- 0: stdin / 1: stdout / 2: stderr
#- 해당 파일을 실행하면서 발생한 에러를 표준출력으로 리다이렉트. 즉 로그 파일에 에러 또한 출력문자열로 저장될 것