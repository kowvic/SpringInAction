# 스프링 시큐리티

## 스프링 시큐리티 활성화하기

1. pom.xml에서 spring-boot-starter-security를 추가한다.
애플리케이션을 실행하면 스프링 시큐리티에서 제공하는 Http 기본 인증상자가 등장한다.

``` xml
<dependency>
            <!--ctrl+space -> edit starter -> security 검색  -->
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-security</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.security</groupId>
			<artifactId>spring-security-test</artifactId>
			<scope>test</scope>
```

![스프링 시큐리티 Http기본 인증 대화상자](./img/2020-06-26 193432.png)

2. 보안 스타터를 프로젝트 빌드 파일에 추가만 했을 때는 다음위 보안 구성이 제공된다.

- 모든 HTTP 요청 경로는 인증(authenticaion)되어야 한다.
- 어떤 특정 역할이나 권한이 없다.
- 로그인 페이지가 따로 없다.
- 스프링 시큐리티의 HTTP 기본 인증을 사용해서 인증된다.
- 사용자는 하나만 있으며, 이름은 user이다. 비밀번호는 암호화해 준다.(콘솔에 표시)

3. 보안을 제대로 구축하려면 더 많은 작업이 필요하며, 최소한 다음 기능을 할 수 있도록 스프링 시큐리티를 구성해야 한다.

- 직접 만든 로그인 페이지로 인증한다.
- 다수의 사용자를 제공하며, 새로운 고객이 사용자로 등록할 수 있는 페이지가 있어야 한다.
- 서로 다른 HTTP 요청 경로마다 서로 다른 보안 규칙을 적용한다.

```java
package com.subway.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	//HTTP보안을 구성하는 메서드
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()//시큐리티 처리에 HttpServletRequest를 이용하는 것을 의미한다.
				.antMatchers("/design", "/orders")// 특정 경로를 지정한다.
					.access("hasRole('ROLE_USER')")// role('user')를 사용해도 된다. 권한명칭은 변경 가능(UESR 부분)

				.antMatchers("/", "/**")
					.access("permitAll")//특정 경로에 모두 접근권한
			.and()
				.httpBasic();

	}

	//사용자 인증 정보를 구성하는 메서드.
	//사용자 스토어를 여기에서 구성한다.
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {//
		auth.inMemoryAuthentication()
			.withUser("user1")
			//noop은 비밀번호을 암호화 하지 않는다는 뜻이다.
			//암호화하지 않으면 스프링에서 접근거부 에러403가 뜬다.
			//인메모리에서는 noop으로 해도 이용가능
			.password("{noop}password1")
			.authorities("ROLE_USER")
			.and()
			.withUser("user2")
			.password("{noop}password2")
			.authorities("ROLE_USER");
	}

}	
```

4. 인증과 인가
- 인증 Authntication은 '증명하다'라는 의미로 **로그인 하는 과정**을 의미한다.
- 인가 Authorization은 '권한부여'나 '허가'와 같은 의미. **허용(Access)**하는 것을 의미한다.
- 윕에서 인증은 해당 URL을 보안 절차를 거친 사용자들만 접근할 수 있다는 의미이고
- 웹에서 인가는 URL의 접근한 사용자가 특정한 자격이 있다는 것을 의미한다.


## 사용자 스토어
- 한 명 이상의 사용자를 처리할 수 있도록 사용자 정보를 유지, 관리하는 곳이다.
- 스프링 시큐리티에서는 인메모리 사용자 스토어, JDBC기반 사용자 스토어,
LDAP기반 사용자 스토어, 커스텀 사용자 스토어가 있다.


1. 인메모리 사용자 스토어 생성
```java
//위에 작성한 configure(AuthenticationManagerBuilder)와 같다.
//inMemoryAuthentication()에서 유저명, 페스워드, 인가 등을 and()를 이용해 여러 번 입력한다.
//테스트 목적이나 간단한 애플리케이션에서 편리하나 사용자 정보 추가나 변경이 어렵다.
protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()//인메모리 사용자 스토어이다.
			.withUser("user1")
			.password("{noop}password1")
			.authorities("ROLE_USER")
			.and()
			.withUser("user2")
			.password("{noop}password2")
			.authorities("ROLE_USER");
	}

```

2. JDBC 기반의 사용자 스토어
```java

//1. 메서드 작성
//2. 스키마, 데이터를 추가하는 sql문을 src/main/resources 아래에 추가
//3. 사용자 비밀번호 암호화
	@Autowired
	DataSource dataSource;

protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication()
			.dataSource(dataSource);

		//이 메서드를 실행하면 다음과 같은 코드가 실행된다.(사용자 정보 저장 테이블 생성)
		//public static final String DEF_USERS_BY_USERNAME_QUERY =
		// "select username, password, enabled" +
		// "from users" +
		// "where username=?";
		// ....(생략)
		// 사용자 정보는 users 테이블, 권한은 authorities 테이블에, 그룹 사용자는 group_members 테이블에
		// 그룹 권한은 group_authorities 테이블에 있다. 
	}
		
```
```sql
--schema.sql 생성. 스프링 시큐리티에 사전 지정된 사용자 및 권한 테이블과 동일한 테이블 생성
	drop table if exists users;
	drop table if exists authorities;
	drop index if exists ix_auth_username;

	create table if not exists users(
		username varchar2(50) not null primary key,
		password varchar2(50) not null,
		enabled char(1) default '1'
	);

	create table if not exists authorities(
		username varchar2(50) not null,
		authority varchar2(50) not null,
		constraint fk_authorities_users
			foreign key(username) references users(username)
	);

	create unique index ix_auth_username
		on authorities(username, authority);
```
```sql
--data.sql
	insert into users(username, password) values('user1', 'password1');
	insert into users(username, password) values('user2', 'password2');

	insert into authorities(username, authority) values('user1', 'ROLE_USER');
	insert into authorities(username, authority) values('user2', 'ROLE_USER');
	commit;

```
- 이대로 애플리케이션을 실행하고 로그인을 하면 에러가 뜬다. **PasswordEncoder**를 사용해서 비밀번호를 암호화해야 하기 때문이다.
- 그러나 user 테이블의 password는 암호화하지 않은 데이터가 저장되어 있기 때문에 암호화를 하면 로그인 정보와 db 정보가 불일치하게 된다.
- 암호화하지 않는 PasswordEncoder를 임시로 사용한다.

```java
	@Autowired
	DataSource dataSource;

protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	//userByUsernameQuery과 authoritiesByUsernameQuery을 사용하여 사용자 정보와 권한 쿼리만을 대체 하였다.
	//groupAuthoritiesByUsername()도 호출하여 그룹 권한 쿼리도 대체할 수 있다.
		auth.jdbcAuthentication()
				.dataSource(dataSource)
				.userByUsernameQuery("select username, password, enabled from users"+"where username=?")
				.authoritiesByUsernameQuery("select username, authority from authorities"+"where username=?")
				//passwordEncoder 인터페이스를 구현하는 어떤 객체도 인자로 받는다.
				//암호화 알고리즘을 구현한 스프링 시큐리티의 모듈에는 다음과 같은 구현 클래스가 포함되어 있다.
				// BCryptPasswordEncoder : bcrypt를 해싱 암호화한다.
				// NoOpPasswordEncoder : 암호화하지 않는다.
				// Pbkdf2PasswordEncoder : PBKDF2를 암호화한다.
				// ScryptPasswordEncoder : scrypt를 해싱 암호화한다.
				// StandardPasswordEncoder : SHA - 256을 해싱 암호화한다.
				.passwordEncoder(new BCryptPasswordEncoder());
}
```

- 스프링 시큐리티의 기본 SQL 쿼리를 우리 것으로(테이블이나 열의 이름이 다를 때) 대체할 떄는 다음의 사항을 지켜야 한다.
	- 매개변수(where절)는 하나이며 username이어야 한다.
	- 사용자 정보 인증 쿼리에서는 username, password, enabled 열의 값을 반환해야 한다.
	- 사용자 권한 쿼리에서는 해당 사용자 이름username과 부여된 권한authrity을 호함하는 0 또는 다수의 행을 반환할 수 있다.
	- 그리고 그룹 권한 쿼리에서는 각각 그룹 id, 그룹 이름group_name, 권한authority열을 갖는 0 또는 다수의 행을 반환할 수 있다.

- 여기서 애플리케이션을 실행하여 로그인을 하면 에러표시 없이 로그인 대화상자만 다시 나타날 것이다. 이번에는 db에 저장된 비밀번호가 암호화 되지 않았기에 암호화로 넘어간 비밀번호와 맞지 않아서 생기는 문제이다.
