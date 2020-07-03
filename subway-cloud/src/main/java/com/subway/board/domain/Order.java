
package com.subway.board.domain;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.subway.User;

import lombok.Data;

@Data
@Entity
//order객체가 SandWich_Order에 저장되도록 한다.
//order는 SQL의 예약어이므로 @Table로 태이블명을 직접 지정해주어야 한다.
@Table(name="SandWich_Order")
public class Order implements Serializable{
	
	private static final long serialVersionUID =1L;
	
	@Id @GeneratedValue
	private long id;
	
	private Date placedAt;
	
	@ManyToOne
	private User user;
	
	@NotBlank(message="name is required")
	private String deliveryName;
	
	@NotBlank(message="street is required")
	private String deliveryStreet;
	
	@NotBlank(message="city is required")
	private String deliveryCity;
	
	@NotBlank(message="state is required")
	private String deliveryState;
	
	@NotBlank(message="zip is required")
	private String deliveryZip;
	
	//@CreditCardNumber(message="Not a valid credit card number")
	private String ccNumber;
	
	@Pattern(regexp="^(0[1-9]|1[0-2])([\\/])([1-9][0-9])$",
			message="must be formatted MM/YY")
	private String ccExpiration;
	
	@Digits(integer=3, fraction=0, message="Invalid CVV")
	private String ccCVV;

	@ManyToMany(targetEntity=SandWich.class)
	private List<SandWich> sandwiches = new ArrayList<>();
	
	public void addDesign(SandWich design) {
		this.sandwiches.add(design);
	}
	
	@PrePersist
	void placedAt() {
		this.placedAt = new Date();
	}
}
