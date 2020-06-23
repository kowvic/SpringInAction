#  JPA 리퍼지터리 커스터마이징

- 스프링 데이터는 일종의 DSL(Domain Seecific Language)를 정의하고 있어서 퍼시스턴스에 관한 내용이 리퍼지터리 메서드의 시그니처에 포함된다.
​
- 리퍼지터리 구현체를 생성할 때, 스프링 데이터는 해당 리퍼지터리 인터페이스에 정의된메서드를 찾아 메서드 이름을 분석한다. 그리고 저장되는 객체(order)의 컨텍스트에서 메서드의 용도가 무엇인지 파악한다.

```java
//CrudRepository에서 제공하는 Crud연산에 추가하여, 
//특정 zip(우편번호)코드로 배달된 모든 주문 데이터도 데이터베이스로부터 가져와야 한다고 하자.
//이것을 CrudRepository에서 메서드를 선언하면 쉽게 해결해준다.

public interface OrderRepository 
	extends CrudRepository<Order, Long>{
	
	List<Order> findByDeliveryZip(String deliveryZip);
	
}

//좀 더 복잡한 예시
//지정된 일자 범위 내에서 특정 zip코드로 배달된 모든 주문을 쿼리해야 한다고 가정해보자.

public interface OrderRepository 
extends CrudRepository<Order, Long>{

List<Order> findByDeliveryZip(String deliveryZip);

List<Order> readOrdersByDeliveryZipAndPlacedAtBetween(
     String deliveryZip, Date startDate, Date endDate);

}
```
|readOrdersByDeliveryZipAndPlacedAtBetween|
|------|---|
|read|이 메서드가 데이터를 읽을 것이라는 뜻(get이나 find도 여기에 허용된다.)
개체의 수를 의미하는 정수를 반환하길 원하면 count를 쓰면 된다.|
|orders|처리대상의 단어는 생략하거나 임의로 지정할 수 있다.(메소드를 읽기 쉽게 표현하면 된다.)
CrudRepository에 매개변수로 지정된 타입 Order를 읽는다.|
|By|일치 여부의 확인에 사용될 속성의 시작을 나타낸다.|
|DeliveryZip|메서드 첫 번째 속성은 메서드의 첫 번째 인자로 받은 값과 반드시 같아야 한다.|
|between|deliveryZip의 값이 메서드의 마지막 두 개 인자로 전달된 값(startDate, endDate) 사이에 포함하는 것이어야 함을 나타낸다.
특정 값과 일치하는 값을 찾는 Equals도 있다.|


- 더욱 복잡한 쿼리의 경우 메서드 이름만으로 감당하기는 어렵다. 이럴떄 @Query()어노테이션을 사용하면 좋다.(어떤 쿼리를 수행할 때도 사용 가능하다.)
```java
// 더욱 복잡한 예시
// 배달한 도시들 중 '시에틀'로 배달된 주문을 읽는다.
@Query("Order o where o.deliveryCity='seattle'")
List<Order> readOrderDeliveredInSeattle();

```