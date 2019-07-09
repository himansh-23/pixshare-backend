package com.pixshare.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Connections implements Serializable {

	@Id
	@GeneratedValue
	private Long id;
	
	private Long sourceUser;
	
	private Long connectedUser;
}
