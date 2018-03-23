package com.apress.controller;

import java.net.URI;

import javax.inject.Inject;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.apress.domain.Poll;
import com.apress.repository.PollRepository;

@RestController
public class PollController {
	
	@Inject
	private PollRepository pollRepository;
	
	
	
	// purpose: a GET request on the /polls endpoint provides a collection of all of the poll available in the QuickPolls application
	@RequestMapping(value="/polls", method=RequestMethod.GET) // @RequestMapping declares the URI and allowed HTTP method
	public ResponseEntity<Iterable<Poll>> getAllPolls() {		// getAllPolls() uses ResponseEntity as its return type, indicating that the return value is the complete HTTP response
		
		// 1. Method begins with reading all of the polls using the PollRepository
		Iterable<Poll> allPolls = pollRepository.findAll();	
		
		// 2. Create an instance of ResponseEntity and pass in Poll data and the HttpStatus.OK status value
		return new ResponseEntity<>(pollRepository.findAll(), HttpStatus.OK);
		
		// 3. Result: the Poll data becomes part of the response body and OK (code 200) becomes the response status code 
	}
	
	
	
	
	@RequestMapping(value="/polls", method=RequestMethod.POST)
	public ResponseEntity<?> createPoll(@RequestBody Poll poll) {  // the @RequestBody annotation tells Spring that the entire request body needs to be converted to an instance of Poll
	        
		// 1. Delegate the Poll persistence to PollRepository's save method
		poll = pollRepository.save(poll);
		
		// 3. Set the location header for the newly created resource 
		HttpHeaders responseHeaders = new HttpHeaders();
		
		// 4. Generate new URI
		URI newPollUri = ServletUriComponentsBuilder
				.fromCurrentRequest()			// Preps the builder by copying info such as host, scheme, port, etc. from the HttpServletRequest
				.path("/{id}")					// appends the passed-in path parameter to the existing path in the builder, ex: in the createPolls method, this results in http://localhost:8080/polls/{id}
				.buildAndExpand(poll.getId())	// replaces any path variables ({id}) with the passed-in value
				.toUri();						// invoke the toUri method on the UriComponents class to generate the final URI
		responseHeaders.setLocation(newPollUri);
		
		// 5. Create a new ResponseEntity with status CREATED(201) and return it
	        return new ResponseEntity<>(null, HttpStatus.CREATED);
	}
	
	@RequestMapping(value="/polls/{pollId}", method=RequestMethod.GET)
	public ResponseEntity<?> getPoll(@PathVariable Long pollId) {
		Poll p = pollRepository.findOne(pollId);
		return new ResponseEntity<> (p, HttpStatus.OK);
	}
	
	@RequestMapping(value="/polls/{pollId}", method=RequestMethod.PUT)
	public ResponseEntity<?> updatePoll(@RequestBody Poll poll, @PathVariable Long pollId) {
	        // Save the entity
	        Poll p = pollRepository.save(poll);
	        return new ResponseEntity<>(HttpStatus.OK);
	}
	@RequestMapping(value="/polls/{pollId}", method=RequestMethod.DELETE)
	public ResponseEntity<?> deletePoll(@PathVariable Long pollId) {
	        pollRepository.delete(pollId);
	        return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	

}
