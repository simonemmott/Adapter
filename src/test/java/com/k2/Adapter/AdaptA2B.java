package com.k2.Adapter;

import com.k2.Adapter.annotation.Adapts;;

@Adapts
public class AdaptA2B implements Adapter<A, B> {
	
	private class Adaption implements B  {
		
		private A a;
		
		Adaption(A a) {
			this.a = a;
		}

		@Override
		public Long getLongNumber() { return Long.valueOf(a.getNumber()); }

		@Override
		public String getTitle() { return "Title: "+a.getName(); }

		@Override
		public long getTime() { return (a.getDate()==null)?0:a.getDate().getTime(); }
		
	}

	@Override
	public Class<A> adaptFrom() { return A.class; }

	@Override
	public Class<B> adaptTo() { return B.class; }

	@Override
	public B adapt(A from) { return new Adaption(from); }

	@Override
	public Class<? extends B> getAdapter() { return Adaption.class; }

}
