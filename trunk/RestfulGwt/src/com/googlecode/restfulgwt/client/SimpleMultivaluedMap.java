package com.googlecode.restfulgwt.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SimpleMultivaluedMap<K,V> extends HashMap<K,List<V>> implements MultivaluedMap<K,V> {

	private static final long serialVersionUID = 1L;

	@Override
	public void add(K arg0, V arg1) {
		List<V> list = get(arg0);
		if(list == null) {
			list = new ArrayList<V>();
			put(arg0,list);
		}
		list.add(arg1);
	}

	@Override
	public V getFirst(K arg0) {
		List<V> list = get(arg0);
		return list == null || list.isEmpty() ? null : list.get(0);
	}

	@Override
	public void putSingle(K arg0, V arg1) {
		ArrayList<V> list = new ArrayList<V>();
		list.add(arg1);
		put(arg0,list);
	}


}
