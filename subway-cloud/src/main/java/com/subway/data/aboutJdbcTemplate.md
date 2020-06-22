# JDBCTemplate 사용법
1. JdbcIngredientRepository.java
```java
@Repository
public class JdbcIngredientRepository implements IngredientRepository{
	private JdbcTemplate jdbc;
	
	@Autowired
	public JdbcIngredientRepository(JdbcTemplate jdbc) {
		this.jdbc=jdbc;
	}

	@Override
	public Iterable<Ingredient> findAll() {
		//query(sql명령어, RowMapper 인터페이스를 구현한 mapRowToIngredient메소드)
        //Ingredient테이블에서 값들을 가져온다.
		return jdbc.query("select id, name, type from Ingredient",
				this::mapRowToIngredient);
	}

	@Override
	public Ingredient findById(String id) {
		//query(sql명령어, RowMapper 인터페이스를 구현한 mapRowToIngredient메소드, 쿼리에서 요구하는 매개변수(where id)
        //Ingredient테이블에서 특정 값만 가져온다.
		return jdbc.queryForObject("select id, name, type from Ingredient where id=?",
				this::mapRowToIngredient, id);
	}
	

	//spring의 RowMapper 인터페이스를 구현한 메소드
	//쿼리로 생성된 ResultSet 객체의 행 개수만큼 호출되며
	//ResultSet의 모든 행을 각각 Ingredient객체 id, name, type으로 생성하고 List에 저장 후 반환한다.)
	private Ingredient mapRowToIngredient(ResultSet rs, int rowNum) throws SQLException{
		return new Ingredient(rs.getString("id"), rs.getString("name"),
				Ingredient.Type.valueOf(rs.getString("type")));
	}

	@Override
	public Ingredient save(Ingredient ingredient) {
		jdbc.update("insert into Ingredient(id, name, type) values(?, ?, ?)",
				ingredient.getId(),
				ingredient.getName(),
				ingredient.getType().toString());
		return ingredient;
		
	}
}
```

2. JdbcSandWichRepository.java
```java
@Repository
public class JdbcSandWichRepository implements SandWichRepository {

	private JdbcTemplate jdbc;
	
	public JdbcSandWichRepository(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}


	@Override
	public SandWich save(SandWich sandwich) {
		//SandWich의 id와 createdAt을 생성하고 sandwich테이블에 name과 createdAt과 id가 저장되며
        //id값을 리턴한다.
		long sandwichId = saveSandWichInfo(sandwich);
		sandwich.setId(sandwichId);
		
		//design.html에서 받아온 sandwich가 가지고 있는 ingredients를
        //for문으로 ingredient값을 하나씩 꺼낸 다음
		//id와 묶어서 sandwich_ingredient에 저장한다.
		for(Ingredient ingredient : sandwich.getIngredients()) {
			saveIngredientToSandWich(ingredient, sandwichId);
		}
		
		return sandwich;
	}
	
	private long saveSandWichInfo(SandWich sandwich) {
		//createdAt 생성
		sandwich.setCreatedAt(new Date());
		
		//insert문 생성, design.html에서 받아온 name과 createdAt이 저장된다.
		PreparedStatementCreator psc = new PreparedStatementCreatorFactory(
				"insert into SandWich(name, createdAt) values(?, ?)",
				Types.VARCHAR, Types.TIMESTAMP
				).newPreparedStatementCreator(
						Arrays.asList(
								sandwich.getName(),
								//new date로 저장한 값을 가져와 (년월일시분초) 밀리세컨드초로 변경한 다음(12345678초)
								//Timestamp 형식으로 변환
								new Timestamp(sandwich.getCreatedAt().getTime())));
		
		//keyholer인터페이스. sql 삽입문에 실행될 때 자동으로 생성되는 키를 담고 있는 인터페이스이다.
		 
		KeyHolder keyholder = new GeneratedKeyHolder();
		jdbc.update(psc, keyholder);
		
		//keyholder의 키값을 가져와 정수타입으로 변환한 값이 id가 된다.
		return keyholder.getKey().longValue();
		
	}

	private void saveIngredientToSandWich(Ingredient ingredient, long sandwichId) {
        //sandwich객체의 id값과 ingredient의 id값이 저장된다.
		jdbc.update("insert into SandWich_Ingredients(sandwich, ingredient)"+
				"values(?,?)", sandwichId, ingredient.getId());

	}



}

```

3. JdbcOrderRepository.java
```java
@Repository
public class JdbcOrderRepository implements OrderRepository{
	private SimpleJdbcInsert orderInserter;
	private SimpleJdbcInsert orderSandWichInserter;
	private ObjectMapper objectMapper;

	@Autowired
	public JdbcOrderRepository(JdbcTemplate jdbc) {
        //jdbcTemplate를 래핑한 simpleJdbcInsert 2개 생성
        //Map 객체 속성을 쉽게 저장하기 위해 Jackson의 ObjectMapper 사용
		this.orderInserter = new SimpleJdbcInsert(jdbc)
				.withTableName("SandWich_Order")
				.usingGeneratedKeyColumns("id");//db에서 id값 자동생성
		
		this.orderSandWichInserter = new SimpleJdbcInsert(jdbc)
				.withTableName("SandWich_Order_SandWiches");
		
		this.objectMapper = new ObjectMapper();
	}

	@Override
	public Order save(Order order) {
        //order의 placedAt과 orderId값 생성해 db에 저장
		order.setPlacedAt(new Date());
		long orderId = saveOrderDetails(order);
		order.setId(orderId);
		List<SandWich> sandwiches = order.getSandwiches();
		
		for(SandWich sandwich : sandwiches) {
			saveSandWichToOrder(sandwich, orderId);
		}
		return order;
	}

	private long saveOrderDetails(Order order) {
		//order 객체를 Map으로 변환
        //order 객체의 필드값이 많으므로 일일일 put(키, 값)을 쓰기보다
        //convertValue(order, Map.class)로 쉽게 변환한다.
        @SuppressWarnings("unchecked")
		Map<String, Object> values = 
			objectMapper.convertValue(order,  Map.class);
        
        //convertValue로 변환한 data값은 long으로 바뀐다.
        //테이블의 placedAt의 타입인 timestamp와는 맞지 않으니 직접 바꿔준다.
		values.put("placedAt", order.getPlacedAt());
		
		long orderId = 
				orderInserter
				.executeAndReturnKey(values)// values값이 Order테이블에 저장되고 db에서 id값을 반환한다.
				.longValue();
		return orderId;
	}

	private void saveSandWichToOrder(SandWich sandwich, long orderId) {
		Map<String, Object> values = new HashMap<>();
        //테이블에 들어갈 열 속성이 2개 뿐이므로 직접 입력한다.
		values.put("sandwichOrder", orderId);
		values.put("sandwich", sandwich.getId());
		orderSandWichInserter.execute(values);
		
	}
}

```