package com.pixshare.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pixshare.entity.Connections;
import com.pixshare.entity.PendingConnectionRequest;
 
public interface ConnectionsRequest extends JpaRepository<PendingConnectionRequest, Long>{
	
	@Query(value="select id  from pending_connection_request where from_id=:fromId AND to_id=:toId",nativeQuery=true)
	Optional<Long> findByRequestId(@Param("fromId")long fromId,@Param("toId")long toId);
	
	List<PendingConnectionRequest> findAllByFromId(Long id);

}
