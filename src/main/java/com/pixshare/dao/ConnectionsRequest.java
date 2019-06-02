package com.pixshare.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pixshare.entity.PendingConnectionRequest;

public interface ConnectionsRequest extends JpaRepository<PendingConnectionRequest, Long>{
	
	
}
