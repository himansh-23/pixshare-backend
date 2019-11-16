package com.pixshare.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pixshare.entity.ImageDetails;

public interface ImageRepository extends JpaRepository<ImageDetails, Long>{

	public List<ImageDetails> findAllByAuthorIn(List<String> authors);
	
//	@Query(value="select * from image_details where author =:email",nativeQuery=true) @Param("email")
	public List<ImageDetails> findAllByAuthor(String email);

}
