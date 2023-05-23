# 📦 물류 VOC / 배상 관리 API

물류 시스템에서 VOC / 배상 관리 API 서버 입니다.

## 🛞 프로세스
1. Voc 접수 및 등록 : **귀책 [운송사 | 고객사] 등록 필수**
    - 운송사의 귀책 :
        - 기사님이 배송을 잘못 보낸 경우
        - 기사님의 실수로 배송이 늦어지는 경우
    - 고객사의 귀책 : 운송사의 기사님이 정상 배송을 했다는 전제
        - 고객사에서 물건을 누락해서 보낸 경우
2. 배상 청구 등록 (해당 VoC 건에 대해)
    - 운송사의 귀책 : 기사님에게 패널티(비용 청구) 접수 및 발급
        1. 패널티 접수 후 기사님게 패널티 발급(APP PUSH)
        2. 기사님이 APP 에서 내용 확인 후 귀책 인정 및 사인을 하면?
           <br>(패널티에 대한 이의제기 가능)
        3. 배상 시스템에 패널티 비용 등록 후 기사님의 월급에서 차감

## 📄 API 문서
![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white)

**👉 링크** : https://documenter.getpostman.com/view/19291348/2s93m4Y39Q

## 🛠️ 기술 스택
![Spring](https://img.shields.io/badge/spring&nbsp;2.7.9-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Java](https://img.shields.io/badge/java&nbsp;11-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle&nbsp;7.6.1-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white)
![MySQL](https://img.shields.io/badge/mysql-%2300f.svg?style=for-the-badge&logo=mysql&logoColor=white)

## 🎨 스키마 다이어그램
![스키마 다이어그램.png](docs%2Fimages%2F%EC%8A%A4%ED%82%A4%EB%A7%88%20%EB%8B%A4%EC%9D%B4%EC%96%B4%EA%B7%B8%EB%9E%A8.png)

## 🏙️ 클래스 다이어그램
![클래스 다이어그램.png](docs%2Fimages%2F%ED%81%B4%EB%9E%98%EC%8A%A4%20%EB%8B%A4%EC%9D%B4%EC%96%B4%EA%B7%B8%EB%9E%A8.png)