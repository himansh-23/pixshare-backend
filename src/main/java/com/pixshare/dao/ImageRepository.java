package com.pixshare.dao;
<<<<<<< HEAD
import java.util.List;

=======
>>>>>>> 21d132bece26e620e27d92953d72f53070708004
import org.springframework.data.jpa.repository.JpaRepository;

import com.pixshare.entity.ImageDetails;

public interface ImageRepository extends JpaRepository<ImageDetails, Long>{

<<<<<<< HEAD
	public List<ImageDetails> findAllByAuthor(List<String> authors);
=======
>>>>>>> 21d132bece26e620e27d92953d72f53070708004
}
