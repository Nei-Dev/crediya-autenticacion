package com.crediya.r2dbc.user;

import com.crediya.r2dbc.entities.UserData;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserReactiveRepository extends ReactiveCrudRepository<UserData, Long>, ReactiveQueryByExampleExecutor<UserData> {
	
	Mono<UserData> findByEmail(String email);
	
	Mono<UserData> findByIdentification(String identification);

}
