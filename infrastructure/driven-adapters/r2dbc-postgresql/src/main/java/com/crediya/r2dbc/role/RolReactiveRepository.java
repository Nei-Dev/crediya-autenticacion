package com.crediya.r2dbc.role;

import com.crediya.r2dbc.entities.RoleData;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface RolReactiveRepository extends ReactiveCrudRepository<RoleData, Long>, ReactiveQueryByExampleExecutor<RoleData> {
	
	Mono<RoleData> findByName(String name);
	
}
