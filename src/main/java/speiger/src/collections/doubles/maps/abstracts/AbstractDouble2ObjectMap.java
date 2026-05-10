package speiger.src.collections.doubles.maps.abstracts;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

import speiger.src.collections.doubles.collections.DoubleIterator;
import speiger.src.collections.doubles.functions.consumer.DoubleObjectConsumer;
import speiger.src.collections.doubles.functions.function.DoubleFunction;
import speiger.src.collections.doubles.functions.function.DoubleObjectUnaryOperator;
import speiger.src.collections.doubles.maps.interfaces.Double2ObjectMap;
import speiger.src.collections.doubles.maps.interfaces.Double2ObjectOrderedMap;
import speiger.src.collections.doubles.sets.DoubleOrderedSet;
import speiger.src.collections.objects.collections.ObjectOrderedCollection;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
import speiger.src.collections.doubles.sets.AbstractDoubleSet;
import speiger.src.collections.doubles.sets.DoubleSet;
import speiger.src.collections.doubles.utils.maps.Double2ObjectMaps;
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
public abstract class AbstractDouble2ObjectMap<V> extends AbstractMap<Double, V> implements Double2ObjectMap<V>
{
	protected V defaultReturnValue = null;
	
	@Override
	public V getDefaultReturnValue() {
		return defaultReturnValue;
	}
	
	@Override
	public AbstractDouble2ObjectMap<V> setDefaultReturnValue(V v) {
		defaultReturnValue = v;
		return this;
	}
	
	protected ObjectIterable<Double2ObjectMap.Entry<V>> getFastIterable(Double2ObjectMap<V> map) {
		return Double2ObjectMaps.fastIterable(map);
	}
	
	protected ObjectIterator<Double2ObjectMap.Entry<V>> getFastIterator(Double2ObjectMap<V> map) {
		return Double2ObjectMaps.fastIterator(map);
	}
	
	@Override
	public Double2ObjectMap<V> copy() { 
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public V put(Double key, V value) {
		return put(key.doubleValue(), value);
	}
	
	@Override
	public void putAll(Double2ObjectMap<V> m) {
		for(ObjectIterator<Double2ObjectMap.Entry<V>> iter = getFastIterator(m);iter.hasNext();) {
			Double2ObjectMap.Entry<V> entry = iter.next();
			put(entry.getDoubleKey(), entry.getValue());
		}
	}
	
	@Override
	public void putAll(Map<? extends Double, ? extends V> m)
	{
		if(m instanceof Double2ObjectMap) putAll((Double2ObjectMap<V>)m);
		else super.putAll(m);
	}
	
	@Override
	public void putAll(double[] keys, V[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);
	}
	
	@Override
	public void putAll(Double[] keys, V[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i].doubleValue(), values[i]);		
	}
	
	@Override
	public void putAllIfAbsent(Double2ObjectMap<V> m) {
		for(Double2ObjectMap.Entry<V> entry : getFastIterable(m))
			putIfAbsent(entry.getDoubleKey(), entry.getValue());
	}
	
	
	@Override
	public boolean containsKey(double key) {
		for(DoubleIterator iter = keySet().iterator();iter.hasNext();)
			if(Double.doubleToLongBits(iter.nextDouble()) == Double.doubleToLongBits(key)) return true;
		return false;
	}
	
	@Override
	public boolean containsValue(Object value) {
		for(ObjectIterator<V> iter = values().iterator();iter.hasNext();)
			if(Objects.equals(value, iter.next())) return true;
		return false;
	}
	
	@Override
	public boolean replace(double key, V oldValue, V newValue) {
		V curValue = get(key);
		if (!Objects.equals(curValue, oldValue) || (Objects.equals(curValue, getDefaultReturnValue()) && !containsKey(key))) {
			return false;
		}
		put(key, newValue);
		return true;
	}

	@Override
	public V replace(double key, V value) {
		V curValue;
		if (!Objects.equals((curValue = get(key)), getDefaultReturnValue()) || containsKey(key)) {
			curValue = put(key, value);
		}
		return curValue;
	}
	
	@Override
	public void replaceObjects(Double2ObjectMap<V> m) {
		for(Double2ObjectMap.Entry<V> entry : getFastIterable(m))
			replace(entry.getDoubleKey(), entry.getValue());
	}
	
	@Override
	public void replaceObjects(DoubleObjectUnaryOperator<V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(ObjectIterator<Double2ObjectMap.Entry<V>> iter = getFastIterator(this);iter.hasNext();) {
			Double2ObjectMap.Entry<V> entry = iter.next();
			entry.setValue(mappingFunction.apply(entry.getDoubleKey(), entry.getValue()));
		}
	}

	@Override
	public V compute(double key, DoubleObjectUnaryOperator<V> mappingFunction) {
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
	public V computeIfAbsent(double key, DoubleFunction<V> mappingFunction) {
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
	public V supplyIfAbsent(double key, ObjectSupplier<V> valueProvider) {
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
	public V computeIfPresent(double key, DoubleObjectUnaryOperator<V> mappingFunction) {
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
	public V merge(double key, V value, ObjectObjectUnaryOperator<V, V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		V oldValue = get(key);
		V newValue = Objects.equals(oldValue, getDefaultReturnValue()) ? value : mappingFunction.apply(oldValue, value);
		if(Objects.equals(newValue, getDefaultReturnValue())) remove(key);
		else put(key, newValue);
		return newValue;
	}
	
	@Override
	public void mergeAll(Double2ObjectMap<V> m, ObjectObjectUnaryOperator<V, V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Double2ObjectMap.Entry<V> entry : getFastIterable(m)) {
			double key = entry.getDoubleKey();
			V oldValue = get(key);
			V newValue = Objects.equals(oldValue, getDefaultReturnValue()) ? entry.getValue() : mappingFunction.apply(oldValue, entry.getValue());
			if(Objects.equals(newValue, getDefaultReturnValue())) remove(key);
			else put(key, newValue);
		}
	}
	
	@Override
	public V get(Object key) {
		return key instanceof Double ? get(((Double)key).doubleValue()) : getDefaultReturnValue();
	}
	
	@Override
	public V getOrDefault(Object key, V defaultValue) {
		return key instanceof Double ? getOrDefault(((Double)key).doubleValue(), defaultValue) : getDefaultReturnValue();
	}
	
	@Override
	public V getOrDefault(double key, V defaultValue) {
		V value = get(key);
		return !Objects.equals(value, getDefaultReturnValue()) || containsKey(key) ? value : defaultValue;
	}
	
	
	@Override
	public V remove(Object key) {
		return key instanceof Double ? remove(((Double)key).doubleValue()) : getDefaultReturnValue();
	}
	
	@Override
	public void forEach(DoubleObjectConsumer<V> action) {
		Objects.requireNonNull(action);
		for(ObjectIterator<Double2ObjectMap.Entry<V>> iter = getFastIterator(this);iter.hasNext();) {
			Double2ObjectMap.Entry<V> entry = iter.next();
			action.accept(entry.getDoubleKey(), entry.getValue());
		}
	}

	@Override
	public DoubleSet keySet() {
		return new AbstractDoubleSet() {
			@Override
			public boolean remove(double o) {
				return !Objects.equals(AbstractDouble2ObjectMap.this.remove(o), getDefaultReturnValue());
			}
			
			@Override
			public boolean add(double o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public DoubleIterator iterator() {
				return new DoubleIterator() {
					ObjectIterator<Double2ObjectMap.Entry<V>> iter = getFastIterator(AbstractDouble2ObjectMap.this);
					@Override
					public boolean hasNext() {
						return iter.hasNext();
					}

					@Override
					public double nextDouble() {
						return iter.next().getDoubleKey();
					}
					
					@Override
					public void remove() {
						iter.remove();
					}
				};
			}
			
			@Override
			public int size() {
				return AbstractDouble2ObjectMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractDouble2ObjectMap.this.clear();
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
				return AbstractDouble2ObjectMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractDouble2ObjectMap.this.clear();
			}
			
			@Override
			public ObjectIterator<V> iterator() {
				return new ObjectIterator<V>() {
					ObjectIterator<Double2ObjectMap.Entry<V>> iter = getFastIterator(AbstractDouble2ObjectMap.this);
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
	public ObjectSet<Map.Entry<Double, V>> entrySet() {
		return (ObjectSet)double2ObjectEntrySet();
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o instanceof Map) {
			if(size() != ((Map<?, ?>)o).size()) return false;
			if(o instanceof Double2ObjectMap) return double2ObjectEntrySet().containsAll(((Double2ObjectMap<V>)o).double2ObjectEntrySet());
			return double2ObjectEntrySet().containsAll(((Map<?, ?>)o).entrySet());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		ObjectIterator<Double2ObjectMap.Entry<V>> iter = getFastIterator(this);
		while(iter.hasNext()) hash += iter.next().hashCode();
		return hash;
	}
	
	public static class ReversedDouble2ObjectOrderedMap<V> extends AbstractDouble2ObjectMap<V> implements Double2ObjectOrderedMap<V> {
		Double2ObjectOrderedMap<V> map;
		
		public ReversedDouble2ObjectOrderedMap(Double2ObjectOrderedMap<V> map) {
			this.map = map;
		}
		@Override
		public AbstractDouble2ObjectMap<V> setDefaultReturnValue(V v) {
			map.setDefaultReturnValue(v);
			return this;
		}
		@Override
		public V getDefaultReturnValue() { return map.getDefaultReturnValue(); }
		@Override
		public Double2ObjectOrderedMap<V> copy() { throw new UnsupportedOperationException(); }
		@Override
		public V put(double key, V value) { return map.put(key, value); }
		@Override
		public V putIfAbsent(double key, V value) { return map.putIfAbsent(key, value); }
		@Override
		public V remove(double key) { return map.remove(key); }
		@Override
		public boolean remove(double key, V value) { return map.remove(key, value); }
		@Override
		public V removeOrDefault(double key, V defaultValue) { return map.removeOrDefault(key, defaultValue); }
		@Override
		public boolean containsKey(double key) { return map.containsKey(key); }
		@Override
		public boolean containsValue(Object value) { return map.containsValue(value); }
		@Override
		public boolean replace(double key, V oldValue, V newValue) { return map.replace(key, oldValue, newValue); }
		@Override
		public V replace(double key, V value) { return map.replace(key, value); }
		@Override
		public void replaceObjects(Double2ObjectMap<V> m) { map.replaceObjects(m); }
		@Override
		public void replaceObjects(DoubleObjectUnaryOperator<V> mappingFunction) { map.replaceObjects(mappingFunction); }
		@Override
		public V compute(double key, DoubleObjectUnaryOperator<V> mappingFunction) { return map.compute(key, mappingFunction); }
		@Override
		public V computeIfAbsent(double key, DoubleFunction<V> mappingFunction) { return map.computeIfAbsent(key, mappingFunction); }
		@Override
		public V supplyIfAbsent(double key, ObjectSupplier<V> valueProvider) { return map.supplyIfAbsent(key, valueProvider); }
		@Override
		public V computeIfPresent(double key, DoubleObjectUnaryOperator<V> mappingFunction) { return map.computeIfPresent(key, mappingFunction); }
		@Override
		public V merge(double key, V value, ObjectObjectUnaryOperator<V, V> mappingFunction) { return map.merge(key, value, mappingFunction); }
		@Override
		public V getOrDefault(double key, V defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public V get(double key) { return map.get(key); }
		@Override
		public V putAndMoveToFirst(double key, V value) { return map.putAndMoveToLast(key, value); }
		@Override
		public V putAndMoveToLast(double key, V value) { return map.putAndMoveToFirst(key, value); }
		@Override
		public V putFirst(double key, V value) { return map.putLast(key, value); }
		@Override
		public V putLast(double key, V value) { return map.putFirst(key, value); }
		@Override
		public boolean moveToFirst(double key) { return map.moveToLast(key); }
		@Override
		public boolean moveToLast(double key) { return map.moveToFirst(key); }
		@Override
		public V getAndMoveToFirst(double key) { return map.getAndMoveToLast(key); }
		@Override
		public V getAndMoveToLast(double key) { return map.getAndMoveToFirst(key); }
		@Override
		public double firstDoubleKey() { return map.lastDoubleKey(); }
		@Override
		public double pollFirstDoubleKey() { return map.pollLastDoubleKey(); }
		@Override
		public double lastDoubleKey() { return map.firstDoubleKey(); }
		@Override
		public double pollLastDoubleKey() { return map.pollFirstDoubleKey(); }
		@Override
		public V firstValue() { return map.lastValue(); }
		@Override
		public V lastValue() { return map.firstValue(); }
		@Override
		public Double2ObjectMap.Entry<V> firstEntry() { return map.lastEntry(); }
		@Override
		public Double2ObjectMap.Entry<V> lastEntry() { return map.firstEntry(); }
		@Override
		public Double2ObjectMap.Entry<V> pollFirstEntry() { return map.pollLastEntry(); }
		@Override
		public Double2ObjectMap.Entry<V> pollLastEntry() { return map.pollFirstEntry(); }
		@Override
		public ObjectOrderedSet<Double2ObjectMap.Entry<V>> double2ObjectEntrySet() { return new AbstractObjectSet.ReversedObjectOrderedSet<>(map.double2ObjectEntrySet()); }
		@Override
		public DoubleOrderedSet keySet() { return new AbstractDoubleSet.ReversedDoubleOrderedSet(map.keySet()); }
		@Override
		public ObjectOrderedCollection<V> values() { return map.values().reversed(); }
		@Override
		public Double2ObjectOrderedMap<V> reversed() { return map; }
	}

	
	/**
	 * A Simple Type Specific Entry class to reduce boxing/unboxing
	 * @param <V> the keyType of elements maintained by this Collection
	 */
	public static class BasicEntry<V> implements Double2ObjectMap.Entry<V> {
		protected double key;
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
		public BasicEntry(Double key, V value) {
			this.key = key.doubleValue();
			this.value = value;
		}
		
		/**
		 * A Type Specific Constructor
		 * @param key the key of a entry
		 * @param value the value of a entry 
		 */
		public BasicEntry(double key, V value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * A Helper method for fast replacing values
		 * @param key the key that should be replaced
		 * @param value the value that should be replaced
		 */
		public void set(double key, V value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public double getDoubleKey() {
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
				if(obj instanceof Double2ObjectMap.Entry) {
					Double2ObjectMap.Entry<V> entry = (Double2ObjectMap.Entry<V>)obj;
					return Double.doubleToLongBits(key) == Double.doubleToLongBits(entry.getDoubleKey()) && Objects.equals(value, entry.getValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Double && Double.doubleToLongBits(this.key) == Double.doubleToLongBits(((Double)key).doubleValue()) && Objects.equals(this.value, value);
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Double.hashCode(key) ^ Objects.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Double.toString(key) + "=" + Objects.toString(value);
		}
	}
}