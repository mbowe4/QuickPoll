package com.apress.repository;

import org.springframework.data.repository.CrudRepository;
import com.apress.domain.Poll;

public interface PollRepository extends CrudRepository<Poll, Long> {

//	Poll findOne(Long pollId);
//
//	void delete(Long pollId);
//	

}
