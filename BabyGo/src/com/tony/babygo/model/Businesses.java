package com.tony.babygo.model;

import java.io.Serializable;
import java.util.List;

public class Businesses implements Serializable {
	private static final long serialVersionUID = 1L;

	private String status;

	private int total_count;

	private int count;

	private List<Business> businesses;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getTotal_count() {
		return total_count;
	}

	public void setTotal_count(int total_count) {
		this.total_count = total_count;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<Business> getBusinesses() {
		return businesses;
	}

	public void setBusinesses(List<Business> businesses) {
		this.businesses = businesses;
	}
	
	
}
