package com.pixshare.dao;
import org.springframework.data.jpa.repository.JpaRepository;

import com.pixshare.entity.ImageDetails;

public interface ImageRepository extends JpaRepository<ImageDetails, Long>{

}
