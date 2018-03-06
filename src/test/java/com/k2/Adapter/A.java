package com.k2.Adapter;

import java.util.Date;

public class A {

	private int number;
	private String name;
	private Date date;
	
	public A(int number, String name, Date date) {
		this.name = name;
		this.number = number;
		this.date = date;
	}
	
	public int getNumber() { return number; }
	public String getName() { return name; }
	public Date getDate() { return date; }
	
}
