package com.pixshare.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class ImageDetails implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6039810328544086259L;
	@Id
	@GeneratedValue
	private Long id;
	
	private String imageName;
	private String author;

}
