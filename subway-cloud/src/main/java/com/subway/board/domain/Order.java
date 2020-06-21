
package com.subway.board.domain;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.CreditCardNumber;

import lombok.Data;

@Data
public class Order {
	private long id;
	
	private Date placedAt;
	
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

	private List<SandWich> sandwiches = new ArrayList<>();
	
	public void addDesign(SandWich design) {
		this.sandwiches.add(design);
		
	}
}
