package com.learn2gether.controllers;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.learn2gether.dao.CustomerRepository;
import com.learn2gether.domain.Address;
import com.learn2gether.domain.Course;
import com.learn2gether.domain.Customer;
import com.learn2gether.domain.CustomerImage;
import com.learn2gether.exception.CustomerNotFoundException;
import com.learn2gether.exception.InvalidCustomerRequestException;
import com.learn2gether.services.FileArchiveService;



/**
 * Customer Controller exposes a series of RESTful endpoints
 */
@RestController
public class CustomerController {

	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private FileArchiveService fileArchiveService; 
	 
	
	@CrossOrigin(origins = {"http://localhost:3000","http://localhost:4200", "http://learn2gether.co.in"})
	@RequestMapping(value="/customers", method=RequestMethod.POST)
	public ResponseEntity<String> handleFileUpload(@RequestParam(value="firstName", required=true) String firstName,
            									   @RequestParam(value="lastName", required=true) String lastName,
            									   @RequestParam(value="dateOfBirth", required=true) @DateTimeFormat(pattern="yyyy-MM-dd") Date dateOfBirth,
            									   @RequestParam(value="street") String street,
            									   @RequestParam(value="town") String town,
            									   @RequestParam(value="state") String state,
            									   @RequestParam(value="postcode", required=true) String postcode,
            									   @RequestParam(value="country", required=true) String country,
            									   @RequestParam(value="email", required=true) String email,
            									   @RequestParam(value="phone", required=true) String phone,
            									   @RequestParam(value="password", required=true) String password,
            									   @RequestParam("image") MultipartFile image) {
	String message = "";
	try {
		System.out.println("here");
	//store(file);
	
	CustomerImage customerImage = fileArchiveService.saveFileToS3(image);        	
	Customer customer = new Customer(firstName, lastName, dateOfBirth, customerImage, 
										new Address(street, town, country, postcode));
	
	customerRepository.save(customer);
	message = "You successfully uploaded " + image.getOriginalFilename() + "!";
                 
	
	return ResponseEntity.status(HttpStatus.OK).body(message);
	} catch (Exception e) {
		e.printStackTrace();
		
	message = "Fail to upload Profile Picture" + image.getOriginalFilename() + "!";
	return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
	}
	}
	
	public void store(MultipartFile file) throws Exception{
		try {
		System.out.println(file.getOriginalFilename());
		System.out.println(Paths.get("ProfilePictureStore").toUri());
		Files.copy(file.getInputStream(), Paths.get("ProfilePictureStore").resolve(file.getOriginalFilename()));
		} catch (Exception e) {
			System.out.println(e);
		throw new RuntimeException("FAIL!");
		}
	}

	/*@CrossOrigin(origins = "http://localhost:4200")// Call  from Local Angular
	@PostMapping("/customers")
    public ResponseEntity<String> createCustomer(            
            @RequestParam(value="firstName", required=true) String firstName,
            @RequestParam(value="lastName", required=true) String lastName,
            @RequestParam(value="dateOfBirth", required=true) @DateTimeFormat(pattern="yyyy-MM-dd") Date dateOfBirth,
            @RequestParam(value="street", required=true) String street,
            @RequestParam(value="town", required=true) String town,
            @RequestParam(value="county", required=true) String county,
            @RequestParam(value="postcode", required=true) String postcode,
            @RequestParam(value="image", required=true) MultipartFile image) {
        
        	CustomerImage customerImage = fileArchiveService.saveFileToS3(image);        	
        	Customer customer = new Customer(firstName, lastName, dateOfBirth, customerImage, 
        										new Address(street, town, county, postcode));
        	
        	customerRepository.save(customer);
        	String message = "You successfully uploaded " + image.getOriginalFilename() + "!";
        	return ResponseEntity.status(HttpStatus.OK).body(message);             
    }*/
	
	/**
	 * Get customer using id. Returns HTTP 404 if customer not found
	 * 
	 * @param customerId
	 * @return retrieved customer
	 */
	@RequestMapping(value = "/customers/{customerId}", method = RequestMethod.GET)
	public Customer getCustomer(@PathVariable("customerId") Long customerId) {
		
		/* validate customer Id parameter */
		if (null==customerId) {
			throw new InvalidCustomerRequestException();
		}
		
		Customer customer = customerRepository.findOne(customerId);
		
		if(null==customer){
			throw new CustomerNotFoundException();
		}
		
		return customer;
	}
	
	/**
	 * Gets all customers.
	 *
	 * @return the customers
	 */
	@CrossOrigin(origins = {"http://localhost:3000","http://localhost:4200", "http://learn2gether.co.in"})
	@RequestMapping(value="/customers", method=RequestMethod.GET)
	public List<Customer> getCustomers() {
		
		return (List<Customer>) customerRepository.findAll();
	}
	
	/**
	 * Deletes the customer with given customer id if it exists and returns HTTP204.
	 *
	 * @param customerId the customer id
	 */
	@RequestMapping(value = "/customers/{customerId}", method = RequestMethod.DELETE)
	public void removeCustomer(@PathVariable("customerId") Long customerId, HttpServletResponse httpResponse) {

		if(customerRepository.exists(customerId)){
			Customer customer = customerRepository.findOne(customerId);
			//fileArchiveService.deleteImageFromS3(customer.getCustomerImage());
	 	    customerRepository.delete(customer);	
		}
		
		httpResponse.setStatus(HttpStatus.NO_CONTENT.value());
	}

}