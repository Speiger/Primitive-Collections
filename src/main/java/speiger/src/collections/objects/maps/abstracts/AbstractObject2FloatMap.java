package speiger.src.collections.objects.maps.abstracts;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.functions.consumer.ObjectFloatConsumer;
import speiger.src.collections.objects.functions.function.ToFloatFunction;
import speiger.src.collections.objects.functions.function.ObjectFloatUnaryOperator;
import speiger.src.collections.objects.maps.interfaces.Object2FloatMap;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.utils.maps.Object2FloatMaps;
import speiger.src.collections.floats.collections.AbstractFloatCollection;
import speiger.src.collections.floats.collections.FloatCollection;
import speiger.src.collections.floats.collections.FloatIterator;
import speiger.src.collections.floats.functions.function.FloatFloatUnaryOperator;
import speiger.src.collections.floats.functions.FloatSupplier;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Base Implementation of a Type Specific Map to reduce boxing/unboxing
 * @param <T> the keyType of elements maintained by this Collection
 */
public abstract class AbstractObject2FloatMap<T> extends AbstractMap<T, Float> implements Object2FloatMap<T>
{
	protected float defaultReturnValue = 0F;
	
	@Override
	public float getDefaultReturnValue() {
		return defaultReturnValue;
	}
	
	@Override
	public AbstractObject2FloatMap<T> setDefaultReturnValue(float v) {
		defaultReturnValue = v;
		return this;
	}
	
	protected ObjectIterable<Object2FloatMap.Entry<T>> getFastIterable(Object2FloatMap<T> map) {
		return Object2FloatMaps.fastIterable(map);
	}
	
	protected ObjectIterator<Object2FloatMap.Entry<T>> getFastIterator(Object2FloatMap<T> map) {
		return Object2FloatMaps.fastIterator(map);
	}
	
	@Override
	public Object2FloatMap<T> copy() { 
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public Float put(T key, Float value) {
		return Float.valueOf(put(key, value.floatValue()));
	}
	
	@Override
	public void addToAll(Object2FloatMap<T> m) {
		for(Object2FloatMap.Entry<T> entry : getFastIterable(m))
			addTo(entry.getKey(), entry.getFloatValue());
	}
	
	@Override
	public void putAll(Object2FloatMap<T> m) {
		for(ObjectIterator<Object2FloatMap.Entry<T>> iter = getFastIterator(m);iter.hasNext();) {
			Object2FloatMap.Entry<T> entry = iter.next();
			put(entry.getKey(), entry.getFloatValue());
		}
	}
	
	@Override
	public void putAll(Map<? extends T, ? extends Float> m)
	{
		if(m instanceof Object2FloatMap) putAll((Object2FloatMap<T>)m);
		else super.putAll(m);
	}
	
	@Override
	public void putAll(T[] keys, float[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);
	}
	
	@Override
	public void putAll(T[] keys, Float[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);		
	}
	
	@Override
	public void putAllIfAbsent(Object2FloatMap<T> m) {
		for(Object2FloatMap.Entry<T> entry : getFastIterable(m))
			putIfAbsent(entry.getKey(), entry.getFloatValue());
	}
	
	
	@Override
	public boolean containsKey(Object key) {
		for(ObjectIterator<T> iter = keySet().iterator();iter.hasNext();)
			if(Objects.equals(key, iter.next())) return true;
		return false;
	}
	
	@Override
	public boolean containsValue(float value) {
		for(FloatIterator iter = values().iterator();iter.hasNext();)
			if(Float.floatToIntBits(iter.nextFloat()) == Float.floatToIntBits(value)) return true;
		return false;
	}
	
	@Override
	public boolean replace(T key, float oldValue, float newValue) {
		float curValue = getFloat(key);
		if (Float.floatToIntBits(curValue) != Float.floatToIntBits(oldValue) || (Float.floatToIntBits(curValue) == Float.floatToIntBits(getDefaultReturnValue()) && !containsKey(key))) {
			return false;
		}
		put(key, newValue);
		return true;
	}

	@Override
	public float replace(T key, float value) {
		float curValue;
		if (Float.floatToIntBits((curValue = getFloat(key))) != Float.floatToIntBits(getDefaultReturnValue()) || containsKey(key)) {
			curValue = put(key, value);
		}
		return curValue;
	}
	
	@Override
	public void replaceFloats(Object2FloatMap<T> m) {
		for(Object2FloatMap.Entry<T> entry : getFastIterable(m))
			replace(entry.getKey(), entry.getFloatValue());
	}
	
	@Override
	public void replaceFloats(ObjectFloatUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(ObjectIterator<Object2FloatMap.Entry<T>> iter = getFastIterator(this);iter.hasNext();) {
			Object2FloatMap.Entry<T> entry = iter.next();
			entry.setValue(mappingFunction.applyAsFloat(entry.getKey(), entry.getFloatValue()));
		}
	}

	@Override
	public float computeFloat(T key, ObjectFloatUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		float newValue = mappingFunction.applyAsFloat(key, getFloat(key));
		put(key, newValue);
		return newValue;
	}
	
	@Override
	public float computeFloatIfAbsent(T key, ToFloatFunction<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(!containsKey(key)) {
			float newValue = mappingFunction.applyAsFloat(key);
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public float supplyFloatIfAbsent(T key, FloatSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		if(!containsKey(key)) {
			float newValue = valueProvider.getAsFloat();
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public float computeFloatIfPresent(T key, ObjectFloatUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(containsKey(key)) {
			float newValue = mappingFunction.applyAsFloat(key, getFloat(key));
			put(key, newValue);
			return newValue;
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public float computeFloatNonDefault(T key, ObjectFloatUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		float value = getFloat(key);
		float newValue = mappingFunction.applyAsFloat(key, value);
		if(Float.floatToIntBits(newValue) == Float.floatToIntBits(getDefaultReturnValue())) {
			if(Float.floatToIntBits(value) != Float.floatToIntBits(getDefaultReturnValue()) || containsKey(key)) {
				remove(key);
				return getDefaultReturnValue();
			}
			return getDefaultReturnValue();
		}
		put(key, newValue);
		return newValue;
	}
	
	@Override
	public float computeFloatIfAbsentNonDefault(T key, ToFloatFunction<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		float value;
		if((value = getFloat(key)) == getDefaultReturnValue() || !containsKey(key)) {
			float newValue = mappingFunction.applyAsFloat(key);
			if(Float.floatToIntBits(newValue) != Float.floatToIntBits(getDefaultReturnValue())) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public float supplyFloatIfAbsentNonDefault(T key, FloatSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		float value;
		if((value = getFloat(key)) == getDefaultReturnValue() || !containsKey(key)) {
			float newValue = valueProvider.getAsFloat();
			if(Float.floatToIntBits(newValue) != Float.floatToIntBits(getDefaultReturnValue())) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public float computeFloatIfPresentNonDefault(T key, ObjectFloatUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		float value;
		if(Float.floatToIntBits((value = getFloat(key))) != Float.floatToIntBits(getDefaultReturnValue()) || containsKey(key)) {
			float newValue = mappingFunction.applyAsFloat(key, value);
			if(Float.floatToIntBits(newValue) != Float.floatToIntBits(getDefaultReturnValue())) {
				put(key, newValue);
				return newValue;
			}
			remove(key);
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public float mergeFloat(T key, float value, FloatFloatUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		float oldValue = getFloat(key);
		float newValue = Float.floatToIntBits(oldValue) == Float.floatToIntBits(getDefaultReturnValue()) ? value : mappingFunction.applyAsFloat(oldValue, value);
		if(Float.floatToIntBits(newValue) == Float.floatToIntBits(getDefaultReturnValue())) remove(key);
		else put(key, newValue);
		return newValue;
	}
	
	@Override
	public void mergeAllFloat(Object2FloatMap<T> m, FloatFloatUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Object2FloatMap.Entry<T> entry : getFastIterable(m)) {
			T key = entry.getKey();
			float oldValue = getFloat(key);
			float newValue = Float.floatToIntBits(oldValue) == Float.floatToIntBits(getDefaultReturnValue()) ? entry.getFloatValue() : mappingFunction.applyAsFloat(oldValue, entry.getFloatValue());
			if(Float.floatToIntBits(newValue) == Float.floatToIntBits(getDefaultReturnValue())) remove(key);
			else put(key, newValue);
		}
	}
	
	@Override
	public Float get(Object key) {
		return Float.valueOf(getFloat((T)key));
	}
	
	@Override
	public Float getOrDefault(Object key, Float defaultValue) {
		Float value = get(key);
		return Float.floatToIntBits(value) != Float.floatToIntBits(getDefaultReturnValue()) || containsKey(key) ? value : defaultValue;
	}
	
	
	@Override
	public Float remove(Object key) {
		return Float.valueOf(rem((T)key));
	}
	
	@Override
	public void forEach(ObjectFloatConsumer<T> action) {
		Objects.requireNonNull(action);
		for(ObjectIterator<Object2FloatMap.Entry<T>> iter = getFastIterator(this);iter.hasNext();) {
			Object2FloatMap.Entry<T> entry = iter.next();
			action.accept(entry.getKey(), entry.getFloatValue());
		}
	}

	@Override
	public ObjectSet<T> keySet() {
		return new AbstractObjectSet<T>() {
			@Override
			public boolean remove(Object o) {
				if(AbstractObject2FloatMap.this.containsKey(o)) {
					AbstractObject2FloatMap.this.remove(o);
					return true;
				}
				return false;
			}
			
			@Override
			public boolean add(T o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public ObjectIterator<T> iterator() {
				return new ObjectIterator<T>() {
					ObjectIterator<Object2FloatMap.Entry<T>> iter = getFastIterator(AbstractObject2FloatMap.this);
					@Override
					public boolean hasNext() {
						return iter.hasNext();
					}

					@Override
					public T next() {
						return iter.next().getKey();
					}
					
					@Override
					public void remove() {
						iter.remove();
					}
				};
			}
			
			@Override
			public int size() {
				return AbstractObject2FloatMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractObject2FloatMap.this.clear();
			}
		};
	}

	@Override
	public FloatCollection values() {
		return new AbstractFloatCollection() {
			@Override
			public boolean add(float o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public int size() {
				return AbstractObject2FloatMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractObject2FloatMap.this.clear();
			}
			
			@Override
			public FloatIterator iterator() {
				return new FloatIterator() {
					ObjectIterator<Object2FloatMap.Entry<T>> iter = getFastIterator(AbstractObject2FloatMap.this);
					@Override
					public boolean hasNext() {
						return iter.hasNext();
					}
					
					@Override
					public float nextFloat() {
						return iter.next().getFloatValue();
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
	public ObjectSet<Map.Entry<T, Float>> entrySet() {
		return (ObjectSet)object2FloatEntrySet();
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o instanceof Map) {
			if(size() != ((Map<?, ?>)o).size()) return false;
			if(o instanceof Object2FloatMap) return object2FloatEntrySet().containsAll(((Object2FloatMap<T>)o).object2FloatEntrySet());
			return object2FloatEntrySet().containsAll(((Map<?, ?>)o).entrySet());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		ObjectIterator<Object2FloatMap.Entry<T>> iter = getFastIterator(this);
		while(iter.hasNext()) hash += iter.next().hashCode();
		return hash;
	}
	
	/**
	 * A Simple Type Specific Entry class to reduce boxing/unboxing
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class BasicEntry<T> implements Object2FloatMap.Entry<T> {
		protected T key;
		protected float value;
		
		/**
		 * A basic Empty constructor
		 */
		public BasicEntry() {}
		/**
		 * A Type Specific Constructor
		 * @param key the key of a entry
		 * @param value the value of a entry 
		 */
		public BasicEntry(T key, float value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * A Helper method for fast replacing values
		 * @param key the key that should be replaced
		 * @param value the value that should be replaced
		 */
		public void set(T key, float value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public T getKey() {
			return key;
		}

		@Override
		public float getFloatValue() {
			return value;
		}

		@Override
		public float setValue(float value) {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Map.Entry) {
				if(obj instanceof Object2FloatMap.Entry) {
					Object2FloatMap.Entry<T> entry = (Object2FloatMap.Entry<T>)obj;
					return Objects.equals(key, entry.getKey()) && Float.floatToIntBits(value) == Float.floatToIntBits(entry.getFloatValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return value instanceof Float && Objects.equals(this.key, key) && Float.floatToIntBits(this.value) == Float.floatToIntBits(((Float)value).floatValue());
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Objects.hashCode(key) ^ Float.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Objects.toString(key) + "=" + Float.toString(value);
		}
	}
}