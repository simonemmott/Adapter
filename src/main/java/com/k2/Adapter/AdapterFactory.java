package com.k2.Adapter;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.k2.Adapter.annotation.Adapts;
import com.k2.Util.classes.ClassUtil;

/**
 * The AdapterFactory marshals maintains an instance of @Apdats 'Adapter' classes read to adapt instances of one classes into implementations of
 * another interface.
 * 
 * @author simon
 *
 */
public class AdapterFactory {

	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * Create an AdapterFactory scanning the list of packages and their sub packages for classes annotated with @Adapts that implement the Adapter
	 * interface.
	 * 
	 * Each class annotated with the @Adapts annotation that also implements the Adapter interface has an instance created using the public zero arg constructor
	 * and is registered with the AdapterFacgtory to be retrieved as required to provide an adaption of a suitable object instance.
	 * @param packageNames	The package names to be scanned for Adapters
	 * @return	The AdapterFactory instance loaded with the instances of 'Adapter' found in the list of packages to scan
	 */
	@SuppressWarnings("unchecked")
	public static AdapterFactory register(String ... packageNames ) {
		AdapterFactory af = new AdapterFactory();
		for (String packageName : packageNames) {
			for (Class<?> cls : ClassUtil.getClasses(packageName, Adapts.class)) {
				boolean ok = false;
				for (Class<?> iFace : cls.getInterfaces()) {
					if (Adapter.class.isAssignableFrom(iFace)) {
						ok=true;
						break;
					}
				}
				if (ok) {
					try {
						af.registerAdapter((Adapter<?,?>)cls.newInstance());
					} catch (InstantiationException | IllegalAccessException e) {
						logger.warn("The class {} annotated with @Adapts does not implement Adapter. Adapter not registered");
					}
				}
			}
		}
		return af;
	}

	/**
	 * Create and returns an instance of AdapterFactory that has been loaded with instances of the list of adapeter classes
	 * @param adapterClasses		The list of classes implementing the Adapter interface that are to be instantiated and registed with the resultant AdapterFactory
	 * @return	The instance of AdapterFactory loaded with instances of the given Adapter classes
	 */
	@SuppressWarnings("unchecked")
	public static AdapterFactory register(Class<? extends Adapter<?,?>> ... adapterClasses ) {
		return new AdapterFactory(adapterClasses);
	}

	private Map<Class<?>,Map<Class<?>, Adapter<?,?>>>repository = new HashMap<Class<?>,Map<Class<?>, Adapter<?,?>>>();
	
	/**
	 * The method registers the given adapter with the instance of AdapterFactory
	 * @param adapter	The adapter to register
	 */
	private void  registerAdapter(Adapter<?,?> adapter) {
		Map<Class<?>, Adapter<?, ?>> fromClassRepo = repository.get(adapter.adaptFrom());
		if (fromClassRepo == null) {
			fromClassRepo = new HashMap<Class<?>, Adapter<?,?>>();
			repository.put(adapter.adaptFrom(), fromClassRepo);
		}
		fromClassRepo.put(adapter.adaptTo(), adapter);
	}
	
	/**
	 * Create an instance of AdapaterFactory for the given list of Adapter classes
	 * @param adapterClasses		The list of Adapter classes to be instantiated and registered with the AdapterFactory instance
	 */
	@SuppressWarnings("unchecked")
	private AdapterFactory(Class<? extends Adapter<?,?>> ... adapterClasses) {
		for (Class<? extends Adapter<?,?>> adapterClass : adapterClasses) {
			try {
				registerAdapter(adapterClass.newInstance());
			} catch (InstantiationException | IllegalAccessException e) {
				throw new AdapterError("The adapter class {} does not have a publicly available zero arg constructor.", adapterClass.getName());
			}
		}
	}

	/**
	 * Check whether this AdapterFactory has an adapter to to convert an instance of one class into an instance of another interface
	 * 
	 * NOTE An unchecked 'AdapterError' is thrown if there is no adapter registered capable of performing the required adaption
	 * @param fromClass	The class to be adapter
	 * @param toInterface	The interface for which an adaption is required
	 * @return	True if this AdapterFactory is capable of performing the adaption
	 */
	public boolean hasAdapter(Class<?> fromClass, Class<?> toInterface) {
		Map<Class<?>, Adapter<?, ?>> fromClassRepo = repository.get(fromClass);
		if (fromClassRepo == null)
			return false;
		return fromClassRepo.containsKey(toInterface);
	}
	
	/**
	 * Get an instance of the adapter class that adapts instances of the from class to implementations of the to interface 
	 * @param fromClass		The class of the instances to be adapted with the resultant adapter
	 * @param toInterface	The interface that instances of the from class are to be adapted to
	 * @return				An instance of 'Adapter' that adapts instances of FromClass to implementations of toInterface
	 */
	@SuppressWarnings("unchecked")
	public <F,T> Adapter<F,T> adapterFor(Class<F> fromClass, Class<T> toInterface) {
		try {
			return (Adapter<F, T>) repository.get(fromClass).get(toInterface);
		} catch (NullPointerException npe) {
			throw new AdapterError("No adapter available to addapt to {} from {}", toInterface.getName(), fromClass.getName());
		}
	}
	
	/**
	 * Adapt the given object instance into an implementation of the required interfac
	 * 
 	 * NOTE An unchecked 'AdapterError' is thrown if there is no adapter registered capable of performing the required adaption
	 * @param from			The object instance to be adapted
	 * @param toInterface	The interface that is to be implemented by the adaption
	 * @return				The adapted instance
	 */
	@SuppressWarnings("unchecked")
	public <F,T> T adapt(F from, Class<T> toInterface) {
		return adapterFor((Class<F>)from.getClass(), toInterface).adapt(from);
	}

}
