## 💻 UMC Android Study ✏️

 ### 📥 프로젝트 시작하기

 - 자신이 원하는 위치에 자신의 파트 레파지토리 중 자신의 깃허브 닉네임 branch를 클론해줍니다

     🖥️ `git clone -b {브랜치이름} —-single-branch {저장소 url}`

     ps. 작업 도중 생길 문제를 대비해서 저희는 본인의 브랜치만 클론 받는 방식으로 진행하려 합니다. 하지만 나중에 프로젝트를 진행할 때는 main branch를 클론 받아, 특정 branch에서 작업하는 방식으로 진행이 될 것입니다. 이때에는 반드시 main branch가 아닌 자신이 작업하기로한 branch에서만 작업하셔야 합니다‼️

 - 자신의 깃허브 닉네임 폴더 내부를 경로로 미션 진행하시면 됩니다

 ### 📌 commit 하기

 - 파트 별로 작업을 끝냈거나 더 작은 단위로 작업을 끝낼 때마다 add 후에 commit을 해 주는 게 좋아요
 - 반드시 자신의 브랜치에서만 commit을 해주세요‼️
 - commit 형식
     - [Feat]: 새로운 기능 추가
     - [Fix]: 버그 수정
     - [Docs]: 문서 수정
     - [Style]: 코드 포맷팅, 세미콜론 누락, 코드 변경이 없는 경우
     - [Refactor]: 코드 리펙토링
     - [Test]: 테스트 코드, 리펙토링 테스트 코드 추가
     - [Chore]: 빌드 업무 수정, 패키지 매니저 수정

     🖥️ `git add 자신이 수정한 파일명`

     🖥️ `git commit -m “[Feat(닉네임/이름)] ㅇㅇ기능 추가”`


 ### 📤 Github 레파지토리에 적용하기

 - 해당 주차의 강의를 듣거나 작업을 모두 끝냈다면, 프로젝트 변경 사항을 remote repository(github repository)에 push 합니다.

     🖥️ `git push origin 본인의 브랜치명(깃허브 닉네임)`

 - push를 완료했다면 스터디 repository에서 pull request를 진행합니다

     (자신의 브랜치에 push 후에 github 페이지 들어오면 아마 뜰거에요!)

 - PR(Pull Request)시 메세지 제목은 다음과 같이 `[이름/닉네임] ㅇ주차 정리내용 제출.` 라고 적은 후 `create pull request` 버튼을 눌러주세요
 - PR : `base: [master]` <- `compare: [본인 github 아이디]`

 ❤️ 본인의 폴더안에 있는 [README.md](http://README.md) 파일은 자유롭게 사용해주세요🙂

 ‼️ **본인의 폴더와 동일한 위치**에 있는 [README.md](http://README.md) 파일은 🚫절대 수정🚫하지 말아주세요 

 ❤️ 깃을 사용하며 오류가 나거나 진행이 안되더라도 혼자 스스로 구글링 해보며 해결해보세요!

 ❤️ 깃 사용법 참고) [https://umc-smwu.notion.site/Git-2aea3e9a04724a08881b8e61e306e5b1](https://www.notion.so/Git-2aea3e9a04724a08881b8e61e306e5b1)
