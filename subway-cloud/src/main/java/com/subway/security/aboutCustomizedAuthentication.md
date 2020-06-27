# 사용자 인증 커스터마이징

- 스프링에 내장된 사용자 스토어가 우리 요구를 충족하지 못할 때는 우리가 커스텀 사용자 명세 서비스를 생성하고 구성해야 한다.
- 예를 들어 스프링에 내장된 사용자 스토어에서는 사용자를 인증하는 데 꼭 필요한 정보(이름, 비밀번호, 사용 가능한 사용자인지 나타내는 활성화 여부)만 사용자 정보를 가지고 있다.
- Subway-cloud에서 모든 데이터(샌드위치, 식자재, 주문)의 퍼시스턴스를 처리하기 위해 JPA를 사용했다. 그러나 사용자 정보의 저장은 스프링 데이터 리퍼지터리를 사용하는 것이 더 좋다.

## 사용자 도메인 객체와 퍼시스턴스 정의하기
- 고객이 등록을 할 때는 사용자 이름과 비밀번호, 전체이름, 주소, 전화번호를 제공해야 한다.
- 이 정보는 주문 폼에 미리 보여주기 위해 사용되지만, 이외의 다양한 목적으로도 사용될 수 있다.

```java
//1. 사용자 개체(entity) 
package com.subway;

import java.util.Arrays;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@NoArgsConstructor(access=AccessLevel.PRIVATE, force=true)
@RequiredArgsConstructor
public class User implements UserDetails{ //userDetails 인터페이스 구현. 기본 사용자정보를 프레임워크에 제공한다.

	private static final long serialVersionUID = 1L;
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private final String username;
	private final String password;
	private final String fullname;
	private final String street;
	private final String state;
	private final String zip;
	private final String phoneNumber;
	
	@Override
	//getAuthorities() : 해당 사용자에게 부여된 권한을 저장한 컬렉션을 반환한다.
	public Collection<? extends GrantedAuthority> getAuthorities() {
	
		return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
	}

	//메소드 이름이 is로 시작해서 Expired로 끝나는 다양학 메서드들은
	//해당 사용자 계정의 활성화, 비활성화 여부를 나타낸다.
	@Override
	public boolean isAccountNonExpired() {
		return true;		
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}	
}
```
```java
//2. 리퍼지토리 인터페이스 정의
package com.subway.data;

import org.springframework.data.repository.CrudRepository;

import com.subway.User;

public interface UserRepository extends CrudRepository<User, Long>{
	//사용자 이름, id로 User를 찾기 위해 사용자 명세 서비스에서 사용될 것이다.
	//(UserRepositoryUserDetailsService.class)
	User findByUsername(String username);
}

```
```java
//3. 커스텀 사용자 명세 서비스 정의하기
package com.subway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.subway.User;
import com.subway.data.UserRepository;
@Service // 스프링이 컴포넌트 검색을 해준다.
public class UserRepositoryUserDetailsService implements UserDetailsService{

	private UserRepository userRepo;
    
    //생성자에 UserRepository의 인스턴스가 주입된다.
	@Autowired
	public UserRepositoryUserDetailsService(UserRepository userRepo) {
		this.userRepo = userRepo;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//주입된 UserRepository 인터페이스의 findByUsername을 호출하여 User를 찾는다.
        User user = userRepo.findByUsername(username);
		if(user!= null) {
			return user;
		}
        //findByUsername()이 null을 반환하면 아래 의 Exception을 호출한다(절대 null이 반환되지 않도록 한다.)
		throw new UsernameNotFoundException("User '"+username+"' not found");
	}

}
```

- 이제 SecurityConfig클래스의 confugure() 메서드에 다음과 같이 추가한다.
```java
package com.subway.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/design", "/orders")
					.access("hasRole('ROLE_USER')")
				.antMatchers("/", "/**")
					.access("permitAll")
			.and()
				.httpBasic();
	}
	
	
	@Autowired
	DataSource dataSource;
    //SecurityConfig로 자동 주입된 UserDetailsService
	@Autowired
	private UserDetailsService userDetailsService;
	
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //주입받은 인스턴스를 인자로 전달하여 메서드 호출
        auth.userDetailsService(userDetailsService);
	}
}

```

- 그다음은 비밀번호가 암호화되어 데이터베이스에 저장될 수 있도록 비밀번호 인코더를 구성해야 한다.
 
```java
package com.subway.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/design", "/orders")
					.access("hasRole('ROLE_USER')")
				.antMatchers("/", "/**")
					.access("permitAll")
			.and()
				.httpBasic();
	}
	
	
	@Autowired
	DataSource dataSource;
	@Autowired
	private UserDetailsService userDetailsService;
	
    @Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
			.passwordEncoder(encoder());
            //encoder()에 @Bean 어노테이션이 지정되었으므로, encoder()메서드가 생성한 BCryptPasswordEncoder 인스턴스가 스프링 애플리케이션 컨텍스트에 등록, 관리되며, 이 인스턴스가 애플리케이션 컨텍스트로부터 주입되어 반환한다.
            //이렇게 함으로써 우리가 원하는 종류의 PasswordEncoder 빈 객체를 스프링의 관리하에 사용할 수 있다.
	}
}
```
- 여기까지가 JPA 리퍼지터리에서 사용자 정보를 읽는 커스텀 사용자 명세 서비스를 작성한 것이다. 이제 데이터베이스에 사용자 정보를 저장하기 위해 사용자 등록 페이지를 생성해야 한다.