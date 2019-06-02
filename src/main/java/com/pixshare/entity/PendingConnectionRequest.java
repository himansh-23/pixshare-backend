package com.pixshare.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Generated;

import lombok.Data;

@Entity
@Data
public class PendingConnectionRequest implements Serializable {

	@Id
	@GeneratedValue
	private Long id;
	private Long fromId;
	private Long toId;
}
