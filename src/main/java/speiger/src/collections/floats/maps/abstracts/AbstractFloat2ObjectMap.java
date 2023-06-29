package speiger.src.collections.floats.maps.abstracts;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

import speiger.src.collections.floats.collections.FloatIterator;
import speiger.src.collections.floats.functions.consumer.FloatObjectConsumer;
import speiger.src.collections.floats.functions.function.FloatFunction;
import speiger.src.collections.floats.functions.function.FloatObjectUnaryOperator;
import speiger.src.collections.floats.maps.interfaces.Float2ObjectMap;
import speiger.src.collections.floats.sets.AbstractFloatSet;
import speiger.src.collections.floats.sets.FloatSet;
import speiger.src.collections.floats.utils.maps.Float2ObjectMaps;
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
public abstract class AbstractFloat2ObjectMap<V> extends AbstractMap<Float, V> implements Float2ObjectMap<V>
{
	protected V defaultReturnValue = null;
	
	@Override
	public V getDefaultReturnValue() {
		return defaultReturnValue;
	}
	
	@Override
	public AbstractFloat2ObjectMap<V> setDefaultReturnValue(V v) {
		defaultReturnValue = v;
		return this;
	}
	
	protected ObjectIterable<Float2ObjectMap.Entry<V>> getFastIterable(Float2ObjectMap<V> map) {
		return Float2ObjectMaps.fastIterable(map);
	}
	
	protected ObjectIterator<Float2ObjectMap.Entry<V>> getFastIterator(Float2ObjectMap<V> map) {
		return Float2ObjectMaps.fastIterator(map);
	}
	
	@Override
	public Float2ObjectMap<V> copy() { 
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public V put(Float key, V value) {
		return put(key.floatValue(), value);
	}
	
	@Override
	public void putAll(Float2ObjectMap<V> m) {
		for(ObjectIterator<Float2ObjectMap.Entry<V>> iter = getFastIterator(m);iter.hasNext();) {
			Float2ObjectMap.Entry<V> entry = iter.next();
			put(entry.getFloatKey(), entry.getValue());
		}
	}
	
	@Override
	public void putAll(Map<? extends Float, ? extends V> m)
	{
		if(m instanceof Float2ObjectMap) putAll((Float2ObjectMap<V>)m);
		else super.putAll(m);
	}
	
	@Override
	public void putAll(float[] keys, V[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);
	}
	
	@Override
	public void putAll(Float[] keys, V[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);		
	}
	
	@Override
	public void putAllIfAbsent(Float2ObjectMap<V> m) {
		for(Float2ObjectMap.Entry<V> entry : getFastIterable(m))
			putIfAbsent(entry.getFloatKey(), entry.getValue());
	}
	
	
	@Override
	public boolean containsKey(float key) {
		for(FloatIterator iter = keySet().iterator();iter.hasNext();)
			if(Float.floatToIntBits(iter.nextFloat()) == Float.floatToIntBits(key)) return true;
		return false;
	}
	
	@Override
	public boolean containsValue(Object value) {
		for(ObjectIterator<V> iter = values().iterator();iter.hasNext();)
			if(Objects.equals(value, iter.next())) return true;
		return false;
	}
	
	@Override
	public boolean replace(float key, V oldValue, V newValue) {
		V curValue = get(key);
		if (!Objects.equals(curValue, oldValue) || (Objects.equals(curValue, getDefaultReturnValue()) && !containsKey(key))) {
			return false;
		}
		put(key, newValue);
		return true;
	}

	@Override
	public V replace(float key, V value) {
		V curValue;
		if (!Objects.equals((curValue = get(key)), getDefaultReturnValue()) || containsKey(key)) {
			curValue = put(key, value);
		}
		return curValue;
	}
	
	@Override
	public void replaceObjects(Float2ObjectMap<V> m) {
		for(Float2ObjectMap.Entry<V> entry : getFastIterable(m))
			replace(entry.getFloatKey(), entry.getValue());
	}
	
	@Override
	public void replaceObjects(FloatObjectUnaryOperator<V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(ObjectIterator<Float2ObjectMap.Entry<V>> iter = getFastIterator(this);iter.hasNext();) {
			Float2ObjectMap.Entry<V> entry = iter.next();
			entry.setValue(mappingFunction.apply(entry.getFloatKey(), entry.getValue()));
		}
	}

	@Override
	public V compute(float key, FloatObjectUnaryOperator<V> mappingFunction) {
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
	public V computeIfAbsent(float key, FloatFunction<V> mappingFunction) {
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
	public V supplyIfAbsent(float key, ObjectSupplier<V> valueProvider) {
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
	public V computeIfPresent(float key, FloatObjectUnaryOperator<V> mappingFunction) {
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
	public V merge(float key, V value, ObjectObjectUnaryOperator<V, V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		V oldValue = get(key);
		V newValue = Objects.equals(oldValue, getDefaultReturnValue()) ? value : mappingFunction.apply(oldValue, value);
		if(Objects.equals(newValue, getDefaultReturnValue())) remove(key);
		else put(key, newValue);
		return newValue;
	}
	
	@Override
	public void mergeAll(Float2ObjectMap<V> m, ObjectObjectUnaryOperator<V, V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Float2ObjectMap.Entry<V> entry : getFastIterable(m)) {
			float key = entry.getFloatKey();
			V oldValue = get(key);
			V newValue = Objects.equals(oldValue, getDefaultReturnValue()) ? entry.getValue() : mappingFunction.apply(oldValue, entry.getValue());
			if(Objects.equals(newValue, getDefaultReturnValue())) remove(key);
			else put(key, newValue);
		}
	}
	
	@Override
	public V get(Object key) {
		return key instanceof Float ? get(((Float)key).floatValue()) : getDefaultReturnValue();
	}
	
	@Override
	public V getOrDefault(Object key, V defaultValue) {
		return key instanceof Float ? getOrDefault(((Float)key).floatValue(), defaultValue) : getDefaultReturnValue();
	}
	
	@Override
	public V getOrDefault(float key, V defaultValue) {
		V value = get(key);
		return !Objects.equals(value, getDefaultReturnValue()) || containsKey(key) ? value : defaultValue;
	}
	
	
	@Override
	public V remove(Object key) {
		return key instanceof Float ? remove(((Float)key).floatValue()) : getDefaultReturnValue();
	}
	
	@Override
	public void forEach(FloatObjectConsumer<V> action) {
		Objects.requireNonNull(action);
		for(ObjectIterator<Float2ObjectMap.Entry<V>> iter = getFastIterator(this);iter.hasNext();) {
			Float2ObjectMap.Entry<V> entry = iter.next();
			action.accept(entry.getFloatKey(), entry.getValue());
		}
	}

	@Override
	public FloatSet keySet() {
		return new AbstractFloatSet() {
			@Override
			public boolean remove(float o) {
				return !Objects.equals(AbstractFloat2ObjectMap.this.remove(o), getDefaultReturnValue());
			}
			
			@Override
			public boolean add(float o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public FloatIterator iterator() {
				return new FloatIterator() {
					ObjectIterator<Float2ObjectMap.Entry<V>> iter = getFastIterator(AbstractFloat2ObjectMap.this);
					@Override
					public boolean hasNext() {
						return iter.hasNext();
					}

					@Override
					public float nextFloat() {
						return iter.next().getFloatKey();
					}
					
					@Override
					public void remove() {
						iter.remove();
					}
				};
			}
			
			@Override
			public int size() {
				return AbstractFloat2ObjectMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractFloat2ObjectMap.this.clear();
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
				return AbstractFloat2ObjectMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractFloat2ObjectMap.this.clear();
			}
			
			@Override
			public ObjectIterator<V> iterator() {
				return new ObjectIterator<V>() {
					ObjectIterator<Float2ObjectMap.Entry<V>> iter = getFastIterator(AbstractFloat2ObjectMap.this);
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
	public ObjectSet<Map.Entry<Float, V>> entrySet() {
		return (ObjectSet)float2ObjectEntrySet();
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o instanceof Map) {
			if(size() != ((Map<?, ?>)o).size()) return false;
			if(o instanceof Float2ObjectMap) return float2ObjectEntrySet().containsAll(((Float2ObjectMap<V>)o).float2ObjectEntrySet());
			return float2ObjectEntrySet().containsAll(((Map<?, ?>)o).entrySet());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		ObjectIterator<Float2ObjectMap.Entry<V>> iter = getFastIterator(this);
		while(iter.hasNext()) hash += iter.next().hashCode();
		return hash;
	}
	
	/**
	 * A Simple Type Specific Entry class to reduce boxing/unboxing
	 * @param <V> the keyType of elements maintained by this Collection
	 */
	public static class BasicEntry<V> implements Float2ObjectMap.Entry<V> {
		protected float key;
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
		public BasicEntry(Float key, V value) {
			this.key = key.floatValue();
			this.value = value;
		}
		
		/**
		 * A Type Specific Constructor
		 * @param key the key of a entry
		 * @param value the value of a entry 
		 */
		public BasicEntry(float key, V value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * A Helper method for fast replacing values
		 * @param key the key that should be replaced
		 * @param value the value that should be replaced
		 */
		public void set(float key, V value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public float getFloatKey() {
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
				if(obj instanceof Float2ObjectMap.Entry) {
					Float2ObjectMap.Entry<V> entry = (Float2ObjectMap.Entry<V>)obj;
					return Float.floatToIntBits(key) == Float.floatToIntBits(entry.getFloatKey()) && Objects.equals(value, entry.getValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Float && Float.floatToIntBits(this.key) == Float.floatToIntBits(((Float)key).floatValue()) && Objects.equals(this.value, value);
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Float.hashCode(key) ^ Objects.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Float.toString(key) + "=" + Objects.toString(value);
		}
	}
}