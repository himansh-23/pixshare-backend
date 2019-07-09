package com.pixshare.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.pixshare.entity.UserDetails;

public interface UserRepository extends JpaRepository<UserDetails, Long>{
	
	Optional<UserDetails> findByEmail(String email);
	
	List<UserDetails> findByIdNotIn(List<Long> id,Pageable page);
}
