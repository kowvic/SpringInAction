# jdbc => JPA로 변경

- 기존의 repository 구현체는 다 지우고 crudRepository를 확장시켰다.

- schema.sql, data.sql을 지운다.

- Ingredient 데이터를 메인 매소드 아래에 CommandLineRunner dataLoade()메소드로 애플 시작과 동시에 입력한다.

- domain에 @Entity를 입력하여 엔티티로 선언한다.

-  SandWich는 Ingredient와 다대다 설정을 주었다. 하나의 sandwich에 많은 ingredient가 있고, 하나의 ingredient는 여러 sandwich에 들어가기 때문이다.

- Order는 SandWich, Ingredient와의 관계를 다대다 설정했다. 하나의 order에는 여러 sandwich가 들어갈 수 있고, sandwich 하나는 여러 order에 들어갈 수 있기 때문이다.
  (책대로 따라하는 것이지만 sandwich재료가 같다고 해도 이름을 직접 지정하기 때문에 완전히 같은 sandwich가 나올 수 있을지 의문이다.)

- save(), findById 등만 입력하면 이미 구현된 메소드들이 알아서 실행해준다.

- JPA는 범용적인 데이터 저장에 유용하다.

- 기본적인 데이터 이상을 저장하는 요구사항에는 JPA리포지터리를 커스터마이즈해야한다.
