package com.pixshare.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.pixshare.entity.Connections;
public interface PersonConnections extends JpaRepository<Connections, Long> {
	
	List<Connections> findAllBySourceUser(Long id);
	
	List<Connections> findAllByConnectedUser(Long id);

}
