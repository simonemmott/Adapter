package com.k2.Adapter;

/**
 * The Adapeter interface defines the method that must be implemented by Adapters marshaled by the AdapterFactory
 * 
 * An Adapter implementation is a class that is capable of providing an adaption of an instance of the from class
 * F to an instance of the to interface T
 * 
 * @author simon
 *
 * @param <F>	The type of the instance to be adapted by this adapter
 * @param <T>	The type of the adaption of the instance to be adapted
 */
public interface Adapter<F,T> {
	/**
	 * @return 	The type of the instances that can be adapted by this Adapter.
	 */
	public Class<F> adaptFrom();
	/**
	 * @return	The type of the adaption to be created from the instance to be adapted
	 */
	public Class<T> adaptTo();
	/**
	 * 
	 * @param from	The instance to be adapted
	 * @return	The adaption of the instance to be adapted
	 */
	public T adapt(F from);
	/**
	 * 
	 * @return	The concrete implementation of type of the adaption that will be used to adapt the instance of the type to be adapted.
	 */
	public Class<? extends T> getAdapter();

}
