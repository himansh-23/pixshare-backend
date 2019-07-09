package com.pixshare.dao;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pixshare.entity.ImageDetails;

public interface ImageRepository extends JpaRepository<ImageDetails, Long>{

	public List<ImageDetails> findAllByAuthor(List<String> authors);
}
