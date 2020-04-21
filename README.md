android-firebase-sample
=========
## Analytics : AnalyticsHelper.kt
> 이벤트 로그 트래킹용으로 사용한다.   
> 콘솔에서 제공되는 Dashboard, Events 등이 모두 이 로그를 통해 구성된다.   
> 비용을 추가로 지불하면 BigQuery에 접근하여 유용한 정보들을 분석 할 수 있다.   
> Funnels, A/B Test에도 유용하게 사용 할 수 있으며, In-App Messaging 기능을 사용할 때 필요하다.   
> Sample 프로젝트에서 몇 가지 로그 트래킹 추가하였다.   

## Authentication : AuthenticationHelper.kt
> 익명 로그인만 추가하였다.   
> auth를 이용해서 Firestore read,write 권한 부여해야함 TBD   

## Crashlytics : CrashlyticsHelper.kt
> 오류 확인을 위해 사용한다.   
> User Property를 설정해서 특정 유저를 트래킹 할 수 있다.   

## Storeage : FireStorageHelper.kt
> 게시판에 첨부 이미지 업로드용으로 사용한다.   

## Firestore : FireStoreHelper.kt
> 맵에 노출하기 위한 Poi 정보들을 저장한다.   
> 게시판 글, 댓글, 대댓글 정보들을 저장한다.   

## Dynamic Link : DynamicLinkHelper.kt
> 앱 정보를 공유하는데 사용한다.   

## Remote Config : RemoteConfigHelper.kt
> 버전 정보, 앱 타이틀, 연락처 정보 등 환경 설정 정보를 로드하는데 사용한다.   

## Cloud Functions : functions/index.js
> 게시물에서 댓글이 작성 되었을 때 댓글 카운트를 업데이트 하는 용도로 사용한다.

## Messaging Service : MapFirebaseMessagingService.kt
> 푸시 토큰 발급
+ 
+ Cloud Functions : 댓글 카운드 (TODO)
+ 그리고 Map
