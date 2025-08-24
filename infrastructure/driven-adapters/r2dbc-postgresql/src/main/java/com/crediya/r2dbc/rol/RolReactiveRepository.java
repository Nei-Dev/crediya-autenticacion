package com.crediya.r2dbc.rol;

import com.crediya.r2dbc.entities.RolData;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface RolReactiveRepository extends ReactiveCrudRepository<RolData, Long>, ReactiveQueryByExampleExecutor<RolData> {
	
	Mono<RolData> findByNombre(String nombre);
	
}
