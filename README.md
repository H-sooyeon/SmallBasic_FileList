# 구문 구조를 제안하는 스몰베이직 코딩 교육 환경

![image](https://github.com/H-sooyeon/SmallBasic_FileList/assets/56586470/99718043-80a1-4ff5-b2ff-423b2d174b11)

교육용 프로그래밍 언어 스몰베이직 코딩 환경에서 텍스트 기반 구문 구조완성을 제공한다. </br>
커뮤니티에서 개발한 샘플 스몰베이직 프로그램들에서 미리 수집한 구문 후보를 데이터베이스화하고, 구문 후보의 빈도에 따라 정렬하여 제공한다. 

</br>

**추가 관련 레퍼지터리**
- https://github.com/H-sooyeon/SmallBasicDataCollection
- https://github.com/H-sooyeon/MySmallBasic
- https://github.com/H-sooyeon/YapbConfigManager

</br>

# 시스템 구조

![image](https://github.com/H-sooyeon/SmallBasic_FileList/assets/56586470/1b8645bb-7d7c-483e-8d18-8d9b843e26c1)
스몰베이직 편집기는 스몰베이직을 편집하고 실행하면서 현재 커서 위치를 데이터베이스 서버에 전달하여 구문 완성 후보 목록을 받아온다. </br>
데이터베이스 서버는 스몰베이직 커뮤니티 프로그램들로부터 수집한 구문 후보들과 빈도를 저장하고 있다. </br>
이 데이터베이스는 프로그램에서 가능한 모든 위치에 대해 구문 후보 목록으로 매핑한다. </br>
각 구문 후보는 빈도 속성을 갖는다.

</br>

# 파일 설명
**1. TextExtract.java**
</br>
Microsoft로부터 제공받은 예제 프로그램 id로 smallbasic 웹에서 코드를 뽑아 .sb 파일로 추출하여 smallbasic-list에 저장한다.

</br>

**2. SyntaxCompletionDataManager.java**
</br>
smallbasic data로 하나의 파일인 datacollection.txt로 HashMap 자료구조를 만든다.
상태(state)와 그에 대한 구문구조 문자열로 하나의 map을 만든다.
key는 상태(state), value는 Pair(구문 구조 문자열, 빈도)로 이루어져 있다.

</br>

**3. PerformanceAnalysis.java**
</br>
성능 분석을 위한 파일
test program에서 찾을 수 있는 구문 구조가 smallbasic program map에서 찾을 수 있는지 확인할 수 있다.
Found, NotFound로 구분하여 출력한다.

</br>
