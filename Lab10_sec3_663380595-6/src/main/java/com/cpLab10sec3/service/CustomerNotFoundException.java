package com.cpLab10sec3.service;

public class CustomerNotFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CustomerNotFoundException(long id) { 
		super("Could not find Customer :"+id);
    }
}
