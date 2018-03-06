package com.k2.Adapter;

import static org.junit.Assert.*;

import java.lang.invoke.MethodHandles;
import java.util.Date;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdapterTest {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@SuppressWarnings("unchecked")
	@Test
	public void createAdapterFactoryTest() {
		AdapterFactory af = AdapterFactory.register(AdaptA2B.class);
		
		assertNotNull(af);
    }

	@SuppressWarnings("unchecked")
	@Test
	public void adapterTest() {
		AdapterFactory af = AdapterFactory.register(AdaptA2B.class);
		
		A a = new A(10, "Hello", new Date(123456789));
		
		Adapter<A,B> a2b = af.adapterFor(A.class, B.class);
		
		assertNotNull(a2b);
		
		assertTrue(af.hasAdapter(A.class, B.class));
		
		B b = a2b.adapt(a);
		
		assertEquals(Long.valueOf(10), b.getLongNumber());
		assertEquals("Title: Hello", b.getTitle());
		assertEquals(123456789L, b.getTime());
		
    }

	@Test
	public void adaptFromPackageScanTest() {
		AdapterFactory af = AdapterFactory.register("com.k2.Adapter");
		
		A a = new A(10, "Hello", new Date(123456789));
		
		Adapter<A,B> a2b = af.adapterFor(A.class, B.class);
		
		assertNotNull(a2b);
		
		B b = a2b.adapt(a);
		
		assertEquals(Long.valueOf(10), b.getLongNumber());
		assertEquals("Title: Hello", b.getTitle());
		assertEquals(123456789L, b.getTime());
		
    }

	@SuppressWarnings("unchecked")
	@Test
	public void adaptionTest() {
		AdapterFactory af = AdapterFactory.register(AdaptA2B.class);
		
		A a = new A(10, "Hello", new Date(123456789));
		
		B b = af.adapt(a, B.class);
		
		assertNotNull(b);
		
		assertEquals(Long.valueOf(10), b.getLongNumber());
		assertEquals("Title: Hello", b.getTitle());
		assertEquals(123456789L, b.getTime());
		
    }

}
