#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

function switch_proxy() {
  IDLE_PORT=$(find_idle_port)

  echo "> 전환할 port: $IDLE_PORT"
  echo "> Port 전환"
  #하나의 문장을 만들어 파이프라인(|)으로 넘겨주기 위해 echo 사용
  #엔진엑스가 변경할 프록시 주소를 생성(쌍따옴표 사용 - 미사용시 $service_url을 인식하지 못하고 변수를 찾게됨)
  #앞에서 넘겨준 문장을 | 로 연결하여 service-url.inc에 덮어씀
  echo "set \$service_url http://127.0.0.1:${IDLE_PORT};" | sudo tee /etc/nginx/conf.d/service-url.inc

  echo "> 엔진엑스 Reload"
  #restart는 잠시 끊김현상이 있지만 reload는 끊김없이 다시 불러옴 (but 중요 설정들은 반영되지 않으므로 restart 사용해야함)
  #여기서는 외부의 설정파일인 service-url을 다시 불러오는 것이라 reload도로 가능함
  sudo service nginx reload
}