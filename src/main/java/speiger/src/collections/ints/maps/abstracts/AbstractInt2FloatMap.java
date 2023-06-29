package speiger.src.collections.ints.maps.abstracts;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

import speiger.src.collections.ints.collections.IntIterator;
import speiger.src.collections.ints.functions.consumer.IntFloatConsumer;
import speiger.src.collections.ints.functions.function.Int2FloatFunction;
import speiger.src.collections.ints.functions.function.IntFloatUnaryOperator;
import speiger.src.collections.ints.maps.interfaces.Int2FloatMap;
import speiger.src.collections.ints.sets.AbstractIntSet;
import speiger.src.collections.ints.sets.IntSet;
import speiger.src.collections.ints.utils.maps.Int2FloatMaps;
import speiger.src.collections.floats.collections.AbstractFloatCollection;
import speiger.src.collections.floats.collections.FloatCollection;
import speiger.src.collections.floats.collections.FloatIterator;
import speiger.src.collections.floats.functions.function.FloatFloatUnaryOperator;
import speiger.src.collections.floats.functions.FloatSupplier;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Base Implementation of a Type Specific Map to reduce boxing/unboxing
 */
public abstract class AbstractInt2FloatMap extends AbstractMap<Integer, Float> implements Int2FloatMap
{
	protected float defaultReturnValue = 0F;
	
	@Override
	public float getDefaultReturnValue() {
		return defaultReturnValue;
	}
	
	@Override
	public AbstractInt2FloatMap setDefaultReturnValue(float v) {
		defaultReturnValue = v;
		return this;
	}
	
	protected ObjectIterable<Int2FloatMap.Entry> getFastIterable(Int2FloatMap map) {
		return Int2FloatMaps.fastIterable(map);
	}
	
	protected ObjectIterator<Int2FloatMap.Entry> getFastIterator(Int2FloatMap map) {
		return Int2FloatMaps.fastIterator(map);
	}
	
	@Override
	public Int2FloatMap copy() { 
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public Float put(Integer key, Float value) {
		return Float.valueOf(put(key.intValue(), value.floatValue()));
	}
	
	@Override
	public void addToAll(Int2FloatMap m) {
		for(Int2FloatMap.Entry entry : getFastIterable(m))
			addTo(entry.getIntKey(), entry.getFloatValue());
	}
	
	@Override
	public void putAll(Int2FloatMap m) {
		for(ObjectIterator<Int2FloatMap.Entry> iter = getFastIterator(m);iter.hasNext();) {
			Int2FloatMap.Entry entry = iter.next();
			put(entry.getIntKey(), entry.getFloatValue());
		}
	}
	
	@Override
	public void putAll(Map<? extends Integer, ? extends Float> m)
	{
		if(m instanceof Int2FloatMap) putAll((Int2FloatMap)m);
		else super.putAll(m);
	}
	
	@Override
	public void putAll(int[] keys, float[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);
	}
	
	@Override
	public void putAll(Integer[] keys, Float[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);		
	}
	
	@Override
	public void putAllIfAbsent(Int2FloatMap m) {
		for(Int2FloatMap.Entry entry : getFastIterable(m))
			putIfAbsent(entry.getIntKey(), entry.getFloatValue());
	}
	
	
	@Override
	public boolean containsKey(int key) {
		for(IntIterator iter = keySet().iterator();iter.hasNext();)
			if(iter.nextInt() == key) return true;
		return false;
	}
	
	@Override
	public boolean containsValue(float value) {
		for(FloatIterator iter = values().iterator();iter.hasNext();)
			if(Float.floatToIntBits(iter.nextFloat()) == Float.floatToIntBits(value)) return true;
		return false;
	}
	
	@Override
	public boolean replace(int key, float oldValue, float newValue) {
		float curValue = get(key);
		if (Float.floatToIntBits(curValue) != Float.floatToIntBits(oldValue) || (Float.floatToIntBits(curValue) == Float.floatToIntBits(getDefaultReturnValue()) && !containsKey(key))) {
			return false;
		}
		put(key, newValue);
		return true;
	}

	@Override
	public float replace(int key, float value) {
		float curValue;
		if (Float.floatToIntBits((curValue = get(key))) != Float.floatToIntBits(getDefaultReturnValue()) || containsKey(key)) {
			curValue = put(key, value);
		}
		return curValue;
	}
	
	@Override
	public void replaceFloats(Int2FloatMap m) {
		for(Int2FloatMap.Entry entry : getFastIterable(m))
			replace(entry.getIntKey(), entry.getFloatValue());
	}
	
	@Override
	public void replaceFloats(IntFloatUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(ObjectIterator<Int2FloatMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Int2FloatMap.Entry entry = iter.next();
			entry.setValue(mappingFunction.applyAsFloat(entry.getIntKey(), entry.getFloatValue()));
		}
	}

	@Override
	public float computeFloat(int key, IntFloatUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		float newValue = mappingFunction.applyAsFloat(key, get(key));
		put(key, newValue);
		return newValue;
	}
	
	@Override
	public float computeFloatIfAbsent(int key, Int2FloatFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(!containsKey(key)) {
			float newValue = mappingFunction.applyAsFloat(key);
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public float supplyFloatIfAbsent(int key, FloatSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		if(!containsKey(key)) {
			float newValue = valueProvider.getAsFloat();
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public float computeFloatIfPresent(int key, IntFloatUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(containsKey(key)) {
			float newValue = mappingFunction.applyAsFloat(key, get(key));
			put(key, newValue);
			return newValue;
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public float computeFloatNonDefault(int key, IntFloatUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		float value = get(key);
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
	public float computeFloatIfAbsentNonDefault(int key, Int2FloatFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		float value;
		if((value = get(key)) == getDefaultReturnValue() || !containsKey(key)) {
			float newValue = mappingFunction.applyAsFloat(key);
			if(Float.floatToIntBits(newValue) != Float.floatToIntBits(getDefaultReturnValue())) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public float supplyFloatIfAbsentNonDefault(int key, FloatSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		float value;
		if((value = get(key)) == getDefaultReturnValue() || !containsKey(key)) {
			float newValue = valueProvider.getAsFloat();
			if(Float.floatToIntBits(newValue) != Float.floatToIntBits(getDefaultReturnValue())) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public float computeFloatIfPresentNonDefault(int key, IntFloatUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		float value;
		if(Float.floatToIntBits((value = get(key))) != Float.floatToIntBits(getDefaultReturnValue()) || containsKey(key)) {
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
	public float mergeFloat(int key, float value, FloatFloatUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		float oldValue = get(key);
		float newValue = Float.floatToIntBits(oldValue) == Float.floatToIntBits(getDefaultReturnValue()) ? value : mappingFunction.applyAsFloat(oldValue, value);
		if(Float.floatToIntBits(newValue) == Float.floatToIntBits(getDefaultReturnValue())) remove(key);
		else put(key, newValue);
		return newValue;
	}
	
	@Override
	public void mergeAllFloat(Int2FloatMap m, FloatFloatUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Int2FloatMap.Entry entry : getFastIterable(m)) {
			int key = entry.getIntKey();
			float oldValue = get(key);
			float newValue = Float.floatToIntBits(oldValue) == Float.floatToIntBits(getDefaultReturnValue()) ? entry.getFloatValue() : mappingFunction.applyAsFloat(oldValue, entry.getFloatValue());
			if(Float.floatToIntBits(newValue) == Float.floatToIntBits(getDefaultReturnValue())) remove(key);
			else put(key, newValue);
		}
	}
	
	@Override
	public Float get(Object key) {
		return Float.valueOf(key instanceof Integer ? get(((Integer)key).intValue()) : getDefaultReturnValue());
	}
	
	@Override
	public Float getOrDefault(Object key, Float defaultValue) {
		return Float.valueOf(key instanceof Integer ? getOrDefault(((Integer)key).intValue(), defaultValue.floatValue()) : getDefaultReturnValue());
	}
	
	@Override
	public float getOrDefault(int key, float defaultValue) {
		float value = get(key);
		return Float.floatToIntBits(value) != Float.floatToIntBits(getDefaultReturnValue()) || containsKey(key) ? value : defaultValue;
	}
	
	
	@Override
	public Float remove(Object key) {
		return key instanceof Integer ? Float.valueOf(remove(((Integer)key).intValue())) : Float.valueOf(getDefaultReturnValue());
	}
	
	@Override
	public void forEach(IntFloatConsumer action) {
		Objects.requireNonNull(action);
		for(ObjectIterator<Int2FloatMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Int2FloatMap.Entry entry = iter.next();
			action.accept(entry.getIntKey(), entry.getFloatValue());
		}
	}

	@Override
	public IntSet keySet() {
		return new AbstractIntSet() {
			@Override
			public boolean remove(int o) {
				return Float.floatToIntBits(AbstractInt2FloatMap.this.remove(o)) != Float.floatToIntBits(getDefaultReturnValue());
			}
			
			@Override
			public boolean add(int o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public IntIterator iterator() {
				return new IntIterator() {
					ObjectIterator<Int2FloatMap.Entry> iter = getFastIterator(AbstractInt2FloatMap.this);
					@Override
					public boolean hasNext() {
						return iter.hasNext();
					}

					@Override
					public int nextInt() {
						return iter.next().getIntKey();
					}
					
					@Override
					public void remove() {
						iter.remove();
					}
				};
			}
			
			@Override
			public int size() {
				return AbstractInt2FloatMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractInt2FloatMap.this.clear();
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
				return AbstractInt2FloatMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractInt2FloatMap.this.clear();
			}
			
			@Override
			public FloatIterator iterator() {
				return new FloatIterator() {
					ObjectIterator<Int2FloatMap.Entry> iter = getFastIterator(AbstractInt2FloatMap.this);
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
	public ObjectSet<Map.Entry<Integer, Float>> entrySet() {
		return (ObjectSet)int2FloatEntrySet();
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o instanceof Map) {
			if(size() != ((Map<?, ?>)o).size()) return false;
			if(o instanceof Int2FloatMap) return int2FloatEntrySet().containsAll(((Int2FloatMap)o).int2FloatEntrySet());
			return int2FloatEntrySet().containsAll(((Map<?, ?>)o).entrySet());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		ObjectIterator<Int2FloatMap.Entry> iter = getFastIterator(this);
		while(iter.hasNext()) hash += iter.next().hashCode();
		return hash;
	}
	
	/**
	 * A Simple Type Specific Entry class to reduce boxing/unboxing
	 */
	public static class BasicEntry implements Int2FloatMap.Entry {
		protected int key;
		protected float value;
		
		/**
		 * A basic Empty constructor
		 */
		public BasicEntry() {}
		/**
		 * A Boxed Constructor for supporting java variants
		 * @param key the key of a entry
		 * @param value the value of a entry
		 */
		public BasicEntry(Integer key, Float value) {
			this.key = key.intValue();
			this.value = value.floatValue();
		}
		
		/**
		 * A Type Specific Constructor
		 * @param key the key of a entry
		 * @param value the value of a entry 
		 */
		public BasicEntry(int key, float value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * A Helper method for fast replacing values
		 * @param key the key that should be replaced
		 * @param value the value that should be replaced
		 */
		public void set(int key, float value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public int getIntKey() {
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
				if(obj instanceof Int2FloatMap.Entry) {
					Int2FloatMap.Entry entry = (Int2FloatMap.Entry)obj;
					return key == entry.getIntKey() && Float.floatToIntBits(value) == Float.floatToIntBits(entry.getFloatValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Integer && value instanceof Float && this.key == ((Integer)key).intValue() && Float.floatToIntBits(this.value) == Float.floatToIntBits(((Float)value).floatValue());
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Integer.hashCode(key) ^ Float.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Integer.toString(key) + "=" + Float.toString(value);
		}
	}
}