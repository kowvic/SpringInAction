
# 사용자 인증 커스터마이징

[스프링 시큐리티 정리글](https://coding-start.tistory.com/153)
## 보안 용어 확인
- 접근 주체(Principal) : 보호된 리소스에 접근하는 대상
- 인증(Authentication) : 보호된 리소스에 접근한 대상에 대해 이 유저가 누구인지, 애플리케이션의 작업을 수행해도 되는 주체인지 확인하는 과정(ex) Form기반 Login)
- 인가(Authorize) : 해당 리소스에 대해 접근 가능한 권한을 가지고 있는지 확인하는 과정(after Authentication, 인증 이후)
- 권한 : 어떠한 리소스에 대한 접근 제한, 모든 리소스는 접근 제어 권한이 걸려있다. 즉 인가 과정에서 해당 리소스에 대한 제한된 최소한의 권한을 가졌는지 확인한다.

## (1) 사용자(USER) 도메인 객체와 퍼시스턴스 정의하기
1. 사용자를 나타내는 User클래스를 만든다.
2. 사용자의 이름과(userId) 비밀번호(password) 외에 전체 이름, 주소, 전화번호를 주문 폼에 미리 보여준다.

```java
//User.java
//파일의 위치가 메인메소드가 있는 패키지(com.Subway)라는 것이 신기하다. 예제의 잘못인지 확인한다.
@Entity
@Data
@NoArgsConstructor(access=AccessLevel.PRIVATE, force=true)
@RequiredArgsConstructor
    // UserDetails 인터페이스를 구현한 클래스는 기본 사용자 정보를 프레임워크에 제공한다.
    // 해당 사용자에게 부여된 권한과 해당 사용자 계정을 사용할 수 있는지 여부 등이다.
public class User implements UserDetails{
	private static final long serialVersionUID = 1L;
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private final String username;
	private final String password;
	private final String fullname;
	private final String street;
	private final String city;
	private final String state;
	private final String zip;
	private final String phoneNumber;
	
    //Collection<>getAuthorities()는 해당 사용자에게 부여된 권한을 저장한 컬렉션을 반환한다.
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
        //사용자 권한목록을 리턴한다.
		return Arrays.asList
				(new SimpleGrantedAuthority("ROLE_USER"));
	}

    //is로 시작하고 Expired로 끝나는 다양한 메소드들은 해당 사용자 계정의 활성화 비활성화 여부를 나타내는 boolean 값을 반환한다.
    //지금은 사용자를 비활성화할 필요가 없으므로 모두 true로 리턴한다.
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
## (2) 리포지터리 인터페이스 정의(CRUD)
1. CrudRepository를 상속받은 UserRepository를 만든다.
2. DSL을 이용해 User를 리턴하는 메소드를 정의한다.

```java
//UserReporitory.java
package com.subway.data;

import org.springframework.data.repository.CrudRepository;

import com.subway.User;

public interface UserRepository extends CrudRepository<User, Long>{
	//사용자 이름, 즉, id로 User를 찾기 위해 사용자 명세 서비스에서 사용될 것이다.
    //스프링 데이터 JPA는 UserRepository 인터페이스의 구현체(클래스)를 런타임시에 자동으로 생성한다.
    User findByUsername(String username);
}
```

## (3) 사용자 명세 서비스 생성하기(Custom)

1. 스프링 시큐리티의 UserDetailsService는 다음과 같이 간단한 인터페이스다.
   
```java
public interface UserDetailsService{
    UserDetails loadUserByUsername(String username)
        throws UsernameNotFoundException;
}
//사용자 이름을 인자로 받아 UserDetails를 반환한다.
//사용자 이름이 null이면 UsernameNotFoundException을 발생시킨다.
```

2. User 클래스에서 UserDetails를 구현하고, UserRepository에서 findByUsername()메서드를 제공하므로 이것들을 이용해 UserDetailsService 인터페이스를 구현할 수 있다.

```java
//UserRepositoryUserDetailsService.java
package com.subway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.subway.User;
import com.subway.data.UserRepository;

//@Service애노테이션 지정. 스프링이 컴포넌트 검색을 해준다는 것을 나타낸다.
@Service
//UserDetailsService 구현하는 클래스
public class UserRepositoryUserDetailsService implements UserDetailsService{

	private UserRepository userRepo;
	
    //1. 생성자를 통해서 UserRepository의 인스턴스가 주입된다.
	@Autowired
	public UserRepositoryUserDetailsService(UserRepository userRepo) {
		this.userRepo = userRepo;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	//2. loadByUsername()에서 주입된 UserRepository 인스턴스의 findByUsername()을 호출하여 User를 찾는다.
    	User user = userRepo.findByUsername(username);
    
    //3. user의 값이 null이 아니면 User를 리턴하고, null이면 UsernameFoundException을 리턴한다.
		if(user!= null) {
			return user;
		}
		throw new UsernameNotFoundException("User '"+username+"' not found");
	}

}
```

3. 이제 이 UserRepositoryUserDetailsService를 스프링 시큐리티에 구성(configure)하면 된다.
```java
//SecurityConfig.java
package com.subway.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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
	//configure(HttpSecurity http)생략...

    //1. UserDetailsService를 인스턴스로 주입한다.
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //2. AuthenticationManagerBuilder에 인스턴스를 인자로 받는 userDetailsService를 호출한다.
    	auth.userDetailsService(userDetailsService)
	
	}
}
```

## (4) 비밀번호 암호화하기(PasswordEncoder)

~(3)까지 작성했다면 
1. UserDetailsService 인터페이스의에서 loadloadUserByUsername(String username)를 실행하면
2. UserDetailsService 구현체에서 UesrReporitory로 찾아온 User값을 loadloadUserByUsername에서 User값의 유무를 확인하고 UserDetails 타입으로 User를 리턴한다.
3. SecurityConfig 클래스로 자동 주입된 UserDetailsService를 configure()메소드의 AuthenticationManagerBuilder에서 userDetailsService()메소드의 인자로 받는다.

4. 여기에 비밀번호를 암호화하는 PasswordEncoder 빈을 선언하여 configure()메소드에 주입한다.

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	//configure(HttpSecurity http)생략...
@Autowired
	private UserDetailsService userDetailsService;
	
    //1. @Bean에서 생성한 BCryptPasswordEncoder 인스턴스가 스프링 애플리케이션 컨텍스트에 등록 관리되며,
    //   이 인스턴스가 애플리케이션 컨텍스트로 주입되어 반환된다.
	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
			.passwordEncoder(encoder());
	}
```


## (5) 사용자 등록하기

1. 스프링 시큐리티는 사용자 등록 절차를 개입하지 않는다. 따라서 이를 처리하기 위한 RegistrationController클래스를 만든다.

2. 사용자 정보를 등록하기 위해 주입한 UserRepo의 save()메소드의 인자로 Registration 객체의 toUesr(passwordEncoder)메소드를 인자로 받는다.

```java
//RegistrationController.java 
//시큐리티 관련 파일들은 전부 security 패키치 안에 들어있다.
package com.subway.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.subway.data.UserRepository;

@Controller//컴포넌트 검색됨
@RequestMapping("/register")//URL 요청/register를 처리한다.
public class RegistrationController {
	private UserRepository userRepo;

    //이 PasswordEncoder는 SecurityConfig클래스에 추가했던 PasswordEncoder 빈과 똑같은 것이다.
    //BCryptPasswordEncoder 객체로 볼 수 있다.
	private PasswordEncoder passwordEncoder;
	
	public RegistrationController(UserRepository userRepo, PasswordEncoder passwordEncoder) {
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
	}
	
    //get 방식의 /register 요청에 registration.html을 반환한다.
	@GetMapping
	public String registerForm() {
		return "registration";
	}
	
    // post 방식의 /register 요청(등록정보 보내기)에 반응하여 DB저장 및 login 페이지로 리다이렉트한다.
	@PostMapping
	public String processRegistration(RegistrationForm form) {
        //RegistrationForm의 toUser는 User의 생성자를 인스턴스하는데, passwordEncoder를 인자로 받는다.
		userRepo.save(form.toUser(passwordEncoder));
		return "redirect:/login";
	}

}
```
```java
//RegistrationForm.java
package com.subway.security;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.subway.User;

import lombok.Data;
//Registration.html에서 입력한 form의 id와 같은 이름을 클래스로 가진다.(input의 name들도 필드와 같다.)
//post형식의 /register요청 시 실행되는 메소드의 인자로 들어가며 폼에서 넘어온 값과 바인딩된다.
@Data
public class RegistrationForm {
	private String username;
	private String password;
	private String fullname;
	private String street;
	private String city;
	private String state;
	private String zip;
	private String phone;
	
    //UserRepo.save에 실제 들어가는 메소드로, User 생성자를 리턴한다.
    //PasswordEncoder.encode()로 패스워드를 암호화하여 User의 Password에 저장한다.
	public User toUser(PasswordEncoder passwordEncoder) {
		return new User(
				username, passwordEncoder.encode(password),
				fullname, street, city, state, zip, phone);
	}
}

```