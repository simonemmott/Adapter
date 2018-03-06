# Adapter - v0.1.0
The Adapter project provides a simple Factory implementation to manage Adapter classes and supply instances of adapter classes as required.

Adapter classes are classes that are annotated with the `@Adapts` annotation and implements the 'Adaptor` interface. Adapter classes must
have a zero arg constructor.

The AdapterFactory provides the following static methods to create AdapterFactories

| Method                | Description |
|-----------------------|-------------|
| `register(String...)` | Create an AdapterFactory scanning the list of given package names for Adapters |
| `register(Class...)`  | Create an AdapterFactory for the given list of Adapter classes |

The AdapterFactory can either provide an adaption directly using the `adapt(Object, Class)` method or can supply an Adapter instance using the `adapterFor(Class, Class)` method that can be retained by the caller to provide adaptions using the `adapt(Object)` method of the Adapter interface.

Javadoc documentation of this project can be found [here](https://simonemmott.github.io/Adapter/index.html)

If the AdapterFactory is asked to adapt a class into an interface for which no adapter is registered or asked to supply such an adapter then the unchecked "AdapterError" is thrown.

### License

[GNU GENERAL PUBLIC LICENSE v3](http://fsf.org/)

## Basic Example

The AdapterFactory can easily generate adaptions of object instances.

This code:
```java
AdapterFactory af = AdapterFactory.register("com.k2.Adapter");

A a = new A(10, "Hello", new Date(123456789));

B b = a2b.adapt(a);
```
Produces the adaption `b` of object instance `a` implementing the interface `B` using the implementation of `Adapter` annotated with `@Adapts` in the
package named "com.k2.Adapter" or any of its sub packages.

The adapter factory holds a register of adapter instances ready to adapt an instance of one class into an implementation of another interface. The concrete implementation of the `Adapter` interface need to be individually developed but not specifically included in code that uses the AdapterFactory
so long as the AdapterFactory is created for the package containing the implementations of `Adapter`


## Getting Started

Download a jar file containing the latest version or fork this project and install in your IDE

Maven users can add this project using the following additions to the pom.xml file.
```maven
<dependencies>
    ...
    <dependency>
        <groupId>com.k2</groupId>
        <artifactId>Adapter</artifactId>
        <version>0.1.0</version>
    </dependency>
    ...
</dependencies>
```

## Working With Adapters

To work with the Adapter project the developer must include the above dependency in their project and develop implementations of the `Adapter` interface as required.

The code below shows an example of a basic `Adapter` implementation.

```java
@Adapts		// <-- (1) 
public class AdaptA2B implements Adapter<A, B> {	// <-- (2) 
	
	private class Adaption implements B  {	// <-- (3) 
		
		private A a;
		
		Adaption(A a) {
			this.a = a;
		}

		@Override	// <-- (4) 
		public Long getLongNumber() { return Long.valueOf(a.getNumber()); }

		@Override	// <-- (4) 
		public String getTitle() { return "Title: "+a.getName(); }

		@Override	// <-- (4) 
		public long getTime() { return (a.getDate()==null)?0:a.getDate().getTime(); }
		
	}

	@Override	// <-- (5) 
	public Class<A> adaptFrom() { return A.class; }

	@Override	// <-- (6) 
	public Class<B> adaptTo() { return B.class; }

	@Override	// <-- (7) 
	public B adapt(A from) { return new Adaption(from); }

	@Override	// <-- (8) 
	public Class<? extends B> getAdapter() { return Adaption.class; }

}
```

The adapter above adapts implementations of type `A` into implementations of the interface `B`

1.	Implementations of the `Adapter` interface must be annotated with the `@Adapts` annotation in order to be found using the `register(String...)` static method of the AdapterFactory.
1.	The implementation of `Adapter` specifies it's from and to classes as generic type arguments.
1.	The class `Adaption` providing the adaption can be a private internal class of the `Adapter` implementation or can be an ordinary public class
1.	The adaption class implements all the public methods of the adapt to interface
1.	The adapter class provides a method to inform the caller the class that can be adapted.
1.	The adapter class provides a method to inform the caller the class that will be adapted to
1.	The `adapt(Object)` method generates an adaption of the given object
1.	The adapter class provides a method to supply the class that performs the adaption

The adapter classes are as simple as they need to be and are automatically registered with the AdapterFactory if they are located in the packages with which the factory was created.

AdapterFactories can be created with a specific list of `Adapter` classes.

```java
AdapterFactory af = AdapterFactory.register(AdaptA2B.class, AdaptC2D.class);
```

produces an AdapterFactory for the given adapter classes. In this construct the adapter classes do not have to be annotated with the `@Adapts` annotation  but it make sense to do so.

The AdapterFactory can provide an instance of an adapter to adapt an instance of a given class into an instance of a given interface.

```java
AdapterFactory af = AdapterFactory.register("com.k2.Adapter");

Adapter<A,B> a2b = af.adapterFor(A.class, B.class);
```

The so supplied adapter instance can be cached by the caller, can called repeatedly for different instances of object `A` and is thread-safe so long as the class implementing the adaption is also thread-safe.

Retrieved adapters can be used to generate adaptions using the `adapt(Object)` method

```java
A a = new A(10, "Hello", new Date(123456789));

B b = a2b.adapt(a);
```

An AdapterFactory can inform its caller whether or not it has an adapter to adapt a given class into a given interface.

```java
AdapterFactory af = AdapterFactory.register("com.k2.Adapter");

A a = new A(10, "Hello", new Date(123456789));

B b = (af.hasAdapter(a.getClass(), B.class) ? af.adapt(a, B.class) : null;
```






















