package speiger.src.collections.longs.maps.abstracts;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

import speiger.src.collections.longs.collections.LongIterator;
import speiger.src.collections.longs.functions.consumer.LongObjectConsumer;
import speiger.src.collections.longs.functions.function.LongFunction;
import speiger.src.collections.longs.functions.function.LongObjectUnaryOperator;
import speiger.src.collections.longs.maps.interfaces.Long2ObjectMap;
import speiger.src.collections.longs.maps.interfaces.Long2ObjectOrderedMap;
import speiger.src.collections.longs.sets.LongOrderedSet;
import speiger.src.collections.objects.collections.ObjectOrderedCollection;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
import speiger.src.collections.longs.sets.AbstractLongSet;
import speiger.src.collections.longs.sets.LongSet;
import speiger.src.collections.longs.utils.maps.Long2ObjectMaps;
import speiger.src.collections.objects.collections.AbstractObjectCollection;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;
import speiger.src.collections.objects.functions.ObjectSupplier;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Base Implementation of a Type Specific Map to reduce boxing/unboxing
 * @param <V> the keyType of elements maintained by this Collection
 */
public abstract class AbstractLong2ObjectMap<V> extends AbstractMap<Long, V> implements Long2ObjectMap<V>
{
	protected V defaultReturnValue = null;
	
	@Override
	public V getDefaultReturnValue() {
		return defaultReturnValue;
	}
	
	@Override
	public AbstractLong2ObjectMap<V> setDefaultReturnValue(V v) {
		defaultReturnValue = v;
		return this;
	}
	
	protected ObjectIterable<Long2ObjectMap.Entry<V>> getFastIterable(Long2ObjectMap<V> map) {
		return Long2ObjectMaps.fastIterable(map);
	}
	
	protected ObjectIterator<Long2ObjectMap.Entry<V>> getFastIterator(Long2ObjectMap<V> map) {
		return Long2ObjectMaps.fastIterator(map);
	}
	
	@Override
	public Long2ObjectMap<V> copy() { 
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public V put(Long key, V value) {
		return put(key.longValue(), value);
	}
	
	@Override
	public void putAll(Long2ObjectMap<V> m) {
		for(ObjectIterator<Long2ObjectMap.Entry<V>> iter = getFastIterator(m);iter.hasNext();) {
			Long2ObjectMap.Entry<V> entry = iter.next();
			put(entry.getLongKey(), entry.getValue());
		}
	}
	
	@Override
	public void putAll(Map<? extends Long, ? extends V> m)
	{
		if(m instanceof Long2ObjectMap) putAll((Long2ObjectMap<V>)m);
		else super.putAll(m);
	}
	
	@Override
	public void putAll(long[] keys, V[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);
	}
	
	@Override
	public void putAll(Long[] keys, V[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i].longValue(), values[i]);		
	}
	
	@Override
	public void putAllIfAbsent(Long2ObjectMap<V> m) {
		for(Long2ObjectMap.Entry<V> entry : getFastIterable(m))
			putIfAbsent(entry.getLongKey(), entry.getValue());
	}
	
	
	@Override
	public boolean containsKey(long key) {
		for(LongIterator iter = keySet().iterator();iter.hasNext();)
			if(iter.nextLong() == key) return true;
		return false;
	}
	
	@Override
	public boolean containsValue(Object value) {
		for(ObjectIterator<V> iter = values().iterator();iter.hasNext();)
			if(Objects.equals(value, iter.next())) return true;
		return false;
	}
	
	@Override
	public boolean replace(long key, V oldValue, V newValue) {
		V curValue = get(key);
		if (!Objects.equals(curValue, oldValue) || (Objects.equals(curValue, getDefaultReturnValue()) && !containsKey(key))) {
			return false;
		}
		put(key, newValue);
		return true;
	}

	@Override
	public V replace(long key, V value) {
		V curValue;
		if (!Objects.equals((curValue = get(key)), getDefaultReturnValue()) || containsKey(key)) {
			curValue = put(key, value);
		}
		return curValue;
	}
	
	@Override
	public void replaceObjects(Long2ObjectMap<V> m) {
		for(Long2ObjectMap.Entry<V> entry : getFastIterable(m))
			replace(entry.getLongKey(), entry.getValue());
	}
	
	@Override
	public void replaceObjects(LongObjectUnaryOperator<V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(ObjectIterator<Long2ObjectMap.Entry<V>> iter = getFastIterator(this);iter.hasNext();) {
			Long2ObjectMap.Entry<V> entry = iter.next();
			entry.setValue(mappingFunction.apply(entry.getLongKey(), entry.getValue()));
		}
	}

	@Override
	public V compute(long key, LongObjectUnaryOperator<V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		V value = get(key);
		V newValue = mappingFunction.apply(key, value);
		if(Objects.equals(newValue, getDefaultReturnValue())) {
			if(!Objects.equals(value, getDefaultReturnValue()) || containsKey(key)) {
				remove(key);
				return getDefaultReturnValue();
			}
			return getDefaultReturnValue();
		}
		put(key, newValue);
		return newValue;
	}
	
	@Override
	public V computeIfAbsent(long key, LongFunction<V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		V value;
		if((value = get(key)) == getDefaultReturnValue() || !containsKey(key)) {
			V newValue = mappingFunction.apply(key);
			if(!Objects.equals(newValue, getDefaultReturnValue())) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public V supplyIfAbsent(long key, ObjectSupplier<V> valueProvider) {
		Objects.requireNonNull(valueProvider);
		V value;
		if((value = get(key)) == getDefaultReturnValue() || !containsKey(key)) {
			V newValue = valueProvider.get();
			if(!Objects.equals(newValue, getDefaultReturnValue())) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public V computeIfPresent(long key, LongObjectUnaryOperator<V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		V value;
		if(!Objects.equals((value = get(key)), getDefaultReturnValue()) || containsKey(key)) {
			V newValue = mappingFunction.apply(key, value);
			if(!Objects.equals(newValue, getDefaultReturnValue())) {
				put(key, newValue);
				return newValue;
			}
			remove(key);
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public V merge(long key, V value, ObjectObjectUnaryOperator<V, V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		V oldValue = get(key);
		V newValue = Objects.equals(oldValue, getDefaultReturnValue()) ? value : mappingFunction.apply(oldValue, value);
		if(Objects.equals(newValue, getDefaultReturnValue())) remove(key);
		else put(key, newValue);
		return newValue;
	}
	
	@Override
	public void mergeAll(Long2ObjectMap<V> m, ObjectObjectUnaryOperator<V, V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Long2ObjectMap.Entry<V> entry : getFastIterable(m)) {
			long key = entry.getLongKey();
			V oldValue = get(key);
			V newValue = Objects.equals(oldValue, getDefaultReturnValue()) ? entry.getValue() : mappingFunction.apply(oldValue, entry.getValue());
			if(Objects.equals(newValue, getDefaultReturnValue())) remove(key);
			else put(key, newValue);
		}
	}
	
	@Override
	public V get(Object key) {
		return key instanceof Long ? get(((Long)key).longValue()) : getDefaultReturnValue();
	}
	
	@Override
	public V getOrDefault(Object key, V defaultValue) {
		return key instanceof Long ? getOrDefault(((Long)key).longValue(), defaultValue) : getDefaultReturnValue();
	}
	
	@Override
	public V getOrDefault(long key, V defaultValue) {
		V value = get(key);
		return !Objects.equals(value, getDefaultReturnValue()) || containsKey(key) ? value : defaultValue;
	}
	
	
	@Override
	public V remove(Object key) {
		return key instanceof Long ? remove(((Long)key).longValue()) : getDefaultReturnValue();
	}
	
	@Override
	public void forEach(LongObjectConsumer<V> action) {
		Objects.requireNonNull(action);
		for(ObjectIterator<Long2ObjectMap.Entry<V>> iter = getFastIterator(this);iter.hasNext();) {
			Long2ObjectMap.Entry<V> entry = iter.next();
			action.accept(entry.getLongKey(), entry.getValue());
		}
	}

	@Override
	public LongSet keySet() {
		return new AbstractLongSet() {
			@Override
			public boolean remove(long o) {
				return !Objects.equals(AbstractLong2ObjectMap.this.remove(o), getDefaultReturnValue());
			}
			
			@Override
			public boolean add(long o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public LongIterator iterator() {
				return new LongIterator() {
					ObjectIterator<Long2ObjectMap.Entry<V>> iter = getFastIterator(AbstractLong2ObjectMap.this);
					@Override
					public boolean hasNext() {
						return iter.hasNext();
					}

					@Override
					public long nextLong() {
						return iter.next().getLongKey();
					}
					
					@Override
					public void remove() {
						iter.remove();
					}
				};
			}
			
			@Override
			public int size() {
				return AbstractLong2ObjectMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractLong2ObjectMap.this.clear();
			}
		};
	}

	@Override
	public ObjectCollection<V> values() {
		return new AbstractObjectCollection<V>() {
			@Override
			public boolean add(V o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public int size() {
				return AbstractLong2ObjectMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractLong2ObjectMap.this.clear();
			}
			
			@Override
			public ObjectIterator<V> iterator() {
				return new ObjectIterator<V>() {
					ObjectIterator<Long2ObjectMap.Entry<V>> iter = getFastIterator(AbstractLong2ObjectMap.this);
					@Override
					public boolean hasNext() {
						return iter.hasNext();
					}
					
					@Override
					public V next() {
						return iter.next().getValue();
					}
					
					@Override
					public void remove() {
						iter.remove();
					}
				};
			}
		};
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public ObjectSet<Map.Entry<Long, V>> entrySet() {
		return (ObjectSet)long2ObjectEntrySet();
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o instanceof Map) {
			if(size() != ((Map<?, ?>)o).size()) return false;
			if(o instanceof Long2ObjectMap) return long2ObjectEntrySet().containsAll(((Long2ObjectMap<V>)o).long2ObjectEntrySet());
			return long2ObjectEntrySet().containsAll(((Map<?, ?>)o).entrySet());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		ObjectIterator<Long2ObjectMap.Entry<V>> iter = getFastIterator(this);
		while(iter.hasNext()) hash += iter.next().hashCode();
		return hash;
	}
	
	public static class ReversedLong2ObjectOrderedMap<V> extends AbstractLong2ObjectMap<V> implements Long2ObjectOrderedMap<V> {
		Long2ObjectOrderedMap<V> map;
		
		public ReversedLong2ObjectOrderedMap(Long2ObjectOrderedMap<V> map) {
			this.map = map;
		}
		@Override
		public AbstractLong2ObjectMap<V> setDefaultReturnValue(V v) {
			map.setDefaultReturnValue(v);
			return this;
		}
		@Override
		public V getDefaultReturnValue() { return map.getDefaultReturnValue(); }
		@Override
		public Long2ObjectOrderedMap<V> copy() { throw new UnsupportedOperationException(); }
		@Override
		public V put(long key, V value) { return map.put(key, value); }
		@Override
		public V putIfAbsent(long key, V value) { return map.putIfAbsent(key, value); }
		@Override
		public V remove(long key) { return map.remove(key); }
		@Override
		public boolean remove(long key, V value) { return map.remove(key, value); }
		@Override
		public V removeOrDefault(long key, V defaultValue) { return map.removeOrDefault(key, defaultValue); }
		@Override
		public boolean containsKey(long key) { return map.containsKey(key); }
		@Override
		public boolean containsValue(Object value) { return map.containsValue(value); }
		@Override
		public boolean replace(long key, V oldValue, V newValue) { return map.replace(key, oldValue, newValue); }
		@Override
		public V replace(long key, V value) { return map.replace(key, value); }
		@Override
		public void replaceObjects(Long2ObjectMap<V> m) { map.replaceObjects(m); }
		@Override
		public void replaceObjects(LongObjectUnaryOperator<V> mappingFunction) { map.replaceObjects(mappingFunction); }
		@Override
		public V compute(long key, LongObjectUnaryOperator<V> mappingFunction) { return map.compute(key, mappingFunction); }
		@Override
		public V computeIfAbsent(long key, LongFunction<V> mappingFunction) { return map.computeIfAbsent(key, mappingFunction); }
		@Override
		public V supplyIfAbsent(long key, ObjectSupplier<V> valueProvider) { return map.supplyIfAbsent(key, valueProvider); }
		@Override
		public V computeIfPresent(long key, LongObjectUnaryOperator<V> mappingFunction) { return map.computeIfPresent(key, mappingFunction); }
		@Override
		public V merge(long key, V value, ObjectObjectUnaryOperator<V, V> mappingFunction) { return map.merge(key, value, mappingFunction); }
		@Override
		public V getOrDefault(long key, V defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public V get(long key) { return map.get(key); }
		@Override
		public V putAndMoveToFirst(long key, V value) { return map.putAndMoveToLast(key, value); }
		@Override
		public V putAndMoveToLast(long key, V value) { return map.putAndMoveToFirst(key, value); }
		@Override
		public V putFirst(long key, V value) { return map.putLast(key, value); }
		@Override
		public V putLast(long key, V value) { return map.putFirst(key, value); }
		@Override
		public boolean moveToFirst(long key) { return map.moveToLast(key); }
		@Override
		public boolean moveToLast(long key) { return map.moveToFirst(key); }
		@Override
		public V getAndMoveToFirst(long key) { return map.getAndMoveToLast(key); }
		@Override
		public V getAndMoveToLast(long key) { return map.getAndMoveToFirst(key); }
		@Override
		public long firstLongKey() { return map.lastLongKey(); }
		@Override
		public long pollFirstLongKey() { return map.pollLastLongKey(); }
		@Override
		public long lastLongKey() { return map.firstLongKey(); }
		@Override
		public long pollLastLongKey() { return map.pollFirstLongKey(); }
		@Override
		public V firstValue() { return map.lastValue(); }
		@Override
		public V lastValue() { return map.firstValue(); }
		@Override
		public Long2ObjectMap.Entry<V> firstEntry() { return map.lastEntry(); }
		@Override
		public Long2ObjectMap.Entry<V> lastEntry() { return map.firstEntry(); }
		@Override
		public Long2ObjectMap.Entry<V> pollFirstEntry() { return map.pollLastEntry(); }
		@Override
		public Long2ObjectMap.Entry<V> pollLastEntry() { return map.pollFirstEntry(); }
		@Override
		public ObjectOrderedSet<Long2ObjectMap.Entry<V>> long2ObjectEntrySet() { return new AbstractObjectSet.ReversedObjectOrderedSet<>(map.long2ObjectEntrySet()); }
		@Override
		public LongOrderedSet keySet() { return new AbstractLongSet.ReversedLongOrderedSet(map.keySet()); }
		@Override
		public ObjectOrderedCollection<V> values() { return map.values().reversed(); }
		@Override
		public Long2ObjectOrderedMap<V> reversed() { return map; }
	}

	
	/**
	 * A Simple Type Specific Entry class to reduce boxing/unboxing
	 * @param <V> the keyType of elements maintained by this Collection
	 */
	public static class BasicEntry<V> implements Long2ObjectMap.Entry<V> {
		protected long key;
		protected V value;
		
		/**
		 * A basic Empty constructor
		 */
		public BasicEntry() {}
		/**
		 * A Boxed Constructor for supporting java variants
		 * @param key the key of a entry
		 * @param value the value of a entry
		 */
		public BasicEntry(Long key, V value) {
			this.key = key.longValue();
			this.value = value;
		}
		
		/**
		 * A Type Specific Constructor
		 * @param key the key of a entry
		 * @param value the value of a entry 
		 */
		public BasicEntry(long key, V value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * A Helper method for fast replacing values
		 * @param key the key that should be replaced
		 * @param value the value that should be replaced
		 */
		public void set(long key, V value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public long getLongKey() {
			return key;
		}

		@Override
		public V getValue() {
			return value;
		}

		@Override
		public V setValue(V value) {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Map.Entry) {
				if(obj instanceof Long2ObjectMap.Entry) {
					Long2ObjectMap.Entry<V> entry = (Long2ObjectMap.Entry<V>)obj;
					return key == entry.getLongKey() && Objects.equals(value, entry.getValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Long && this.key == ((Long)key).longValue() && Objects.equals(this.value, value);
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Long.hashCode(key) ^ Objects.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Long.toString(key) + "=" + Objects.toString(value);
		}
	}
}