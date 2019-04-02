package com.pixshare.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.Builder.Default;
//import java
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Entity
@Table(name="userdetails")
@ToString
public class UserDetails implements Serializable {
	
	/**
	 *  Serial Version ID.
	 */
	private static final long serialVersionUID = -2109728770232503588L;
	
	@Id
	@GeneratedValue
	private long id;
	
	private String userName;
	
	private String password;
	
	private String email;
	
	private String mobileNumber;
	
	@Column(columnDefinition="tinyint(1) default 0")
	@NotNull
	private boolean isAuthenticated;
	
	private String profileImage;
	
}
