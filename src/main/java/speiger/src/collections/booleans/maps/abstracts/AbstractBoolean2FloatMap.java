package speiger.src.collections.booleans.maps.abstracts;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

import speiger.src.collections.booleans.collections.BooleanIterator;
import speiger.src.collections.booleans.functions.consumer.BooleanFloatConsumer;
import speiger.src.collections.booleans.functions.function.Boolean2FloatFunction;
import speiger.src.collections.booleans.functions.function.BooleanFloatUnaryOperator;
import speiger.src.collections.booleans.maps.interfaces.Boolean2FloatMap;
import speiger.src.collections.booleans.sets.AbstractBooleanSet;
import speiger.src.collections.booleans.sets.BooleanSet;
import speiger.src.collections.booleans.utils.maps.Boolean2FloatMaps;
import speiger.src.collections.floats.collections.AbstractFloatCollection;
import speiger.src.collections.floats.collections.FloatCollection;
import speiger.src.collections.floats.collections.FloatIterator;
import speiger.src.collections.floats.functions.function.FloatFloatUnaryOperator;
import speiger.src.collections.floats.functions.FloatSupplier;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Base Implementation of a Type Specific Map to reduce boxing/unboxing
 */
public abstract class AbstractBoolean2FloatMap extends AbstractMap<Boolean, Float> implements Boolean2FloatMap
{
	protected float defaultReturnValue = 0F;
	
	@Override
	public float getDefaultReturnValue() {
		return defaultReturnValue;
	}
	
	@Override
	public AbstractBoolean2FloatMap setDefaultReturnValue(float v) {
		defaultReturnValue = v;
		return this;
	}
	
	@Override
	public Boolean2FloatMap copy() { 
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public Float put(Boolean key, Float value) {
		return Float.valueOf(put(key.booleanValue(), value.floatValue()));
	}
	
	@Override
	public void addToAll(Boolean2FloatMap m) {
		for(Boolean2FloatMap.Entry entry : Boolean2FloatMaps.fastIterable(m))
			addTo(entry.getBooleanKey(), entry.getFloatValue());
	}
	
	@Override
	public void putAll(Boolean2FloatMap m) {
		for(ObjectIterator<Boolean2FloatMap.Entry> iter = Boolean2FloatMaps.fastIterator(m);iter.hasNext();) {
			Boolean2FloatMap.Entry entry = iter.next();
			put(entry.getBooleanKey(), entry.getFloatValue());
		}
	}
	
	@Override
	public void putAll(Map<? extends Boolean, ? extends Float> m)
	{
		if(m instanceof Boolean2FloatMap) putAll((Boolean2FloatMap)m);
		else super.putAll(m);
	}
	
	@Override
	public void putAll(boolean[] keys, float[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);
	}
	
	@Override
	public void putAll(Boolean[] keys, Float[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);		
	}
	
	@Override
	public void putAllIfAbsent(Boolean2FloatMap m) {
		for(Boolean2FloatMap.Entry entry : Boolean2FloatMaps.fastIterable(m))
			putIfAbsent(entry.getBooleanKey(), entry.getFloatValue());
	}
	
	
	@Override
	public boolean containsKey(boolean key) {
		for(BooleanIterator iter = keySet().iterator();iter.hasNext();)
			if(iter.nextBoolean() == key) return true;
		return false;
	}
	
	@Override
	public boolean containsValue(float value) {
		for(FloatIterator iter = values().iterator();iter.hasNext();)
			if(Float.floatToIntBits(iter.nextFloat()) == Float.floatToIntBits(value)) return true;
		return false;
	}
	
	@Override
	public boolean replace(boolean key, float oldValue, float newValue) {
		float curValue = get(key);
		if (Float.floatToIntBits(curValue) != Float.floatToIntBits(oldValue) || (Float.floatToIntBits(curValue) == Float.floatToIntBits(getDefaultReturnValue()) && !containsKey(key))) {
			return false;
		}
		put(key, newValue);
		return true;
	}

	@Override
	public float replace(boolean key, float value) {
		float curValue;
		if (Float.floatToIntBits((curValue = get(key))) != Float.floatToIntBits(getDefaultReturnValue()) || containsKey(key)) {
			curValue = put(key, value);
		}
		return curValue;
	}
	
	@Override
	public void replaceFloats(Boolean2FloatMap m) {
		for(Boolean2FloatMap.Entry entry : Boolean2FloatMaps.fastIterable(m))
			replace(entry.getBooleanKey(), entry.getFloatValue());
	}
	
	@Override
	public void replaceFloats(BooleanFloatUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(ObjectIterator<Boolean2FloatMap.Entry> iter = Boolean2FloatMaps.fastIterator(this);iter.hasNext();) {
			Boolean2FloatMap.Entry entry = iter.next();
			entry.setValue(mappingFunction.applyAsFloat(entry.getBooleanKey(), entry.getFloatValue()));
		}
	}

	@Override
	public float computeFloat(boolean key, BooleanFloatUnaryOperator mappingFunction) {
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
	public float computeFloatIfAbsent(boolean key, Boolean2FloatFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		float value;
		if((value = get(key)) == getDefaultReturnValue() || !containsKey(key)) {
			float newValue = mappingFunction.get(key);
			if(Float.floatToIntBits(newValue) != Float.floatToIntBits(getDefaultReturnValue())) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public float supplyFloatIfAbsent(boolean key, FloatSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		float value;
		if((value = get(key)) == getDefaultReturnValue() || !containsKey(key)) {
			float newValue = valueProvider.getFloat();
			if(Float.floatToIntBits(newValue) != Float.floatToIntBits(getDefaultReturnValue())) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public float computeFloatIfPresent(boolean key, BooleanFloatUnaryOperator mappingFunction) {
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
	public float mergeFloat(boolean key, float value, FloatFloatUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		float oldValue = get(key);
		float newValue = Float.floatToIntBits(oldValue) == Float.floatToIntBits(getDefaultReturnValue()) ? value : mappingFunction.applyAsFloat(oldValue, value);
		if(Float.floatToIntBits(newValue) == Float.floatToIntBits(getDefaultReturnValue())) remove(key);
		else put(key, newValue);
		return newValue;
	}
	
	@Override
	public void mergeAllFloat(Boolean2FloatMap m, FloatFloatUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Boolean2FloatMap.Entry entry : Boolean2FloatMaps.fastIterable(m)) {
			boolean key = entry.getBooleanKey();
			float oldValue = get(key);
			float newValue = Float.floatToIntBits(oldValue) == Float.floatToIntBits(getDefaultReturnValue()) ? entry.getFloatValue() : mappingFunction.applyAsFloat(oldValue, entry.getFloatValue());
			if(Float.floatToIntBits(newValue) == Float.floatToIntBits(getDefaultReturnValue())) remove(key);
			else put(key, newValue);
		}
	}
	
	@Override
	public Float get(Object key) {
		return Float.valueOf(key instanceof Boolean ? get(((Boolean)key).booleanValue()) : getDefaultReturnValue());
	}
	
	@Override
	public Float getOrDefault(Object key, Float defaultValue) {
		return Float.valueOf(key instanceof Boolean ? getOrDefault(((Boolean)key).booleanValue(), defaultValue.floatValue()) : getDefaultReturnValue());
	}
	
	@Override
	public float getOrDefault(boolean key, float defaultValue) {
		float value = get(key);
		return Float.floatToIntBits(value) != Float.floatToIntBits(getDefaultReturnValue()) || containsKey(key) ? value : defaultValue;
	}
	
	@Override
	public void forEach(BooleanFloatConsumer action) {
		Objects.requireNonNull(action);
		for(ObjectIterator<Boolean2FloatMap.Entry> iter = Boolean2FloatMaps.fastIterator(this);iter.hasNext();) {
			Boolean2FloatMap.Entry entry = iter.next();
			action.accept(entry.getBooleanKey(), entry.getFloatValue());
		}
	}

	@Override
	public BooleanSet keySet() {
		return new AbstractBooleanSet() {
			@Override
			public boolean remove(boolean o) {
				return Float.floatToIntBits(AbstractBoolean2FloatMap.this.remove(o)) != Float.floatToIntBits(getDefaultReturnValue());
			}
			
			@Override
			public boolean add(boolean o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public BooleanIterator iterator() {
				return new BooleanIterator() {
					ObjectIterator<Boolean2FloatMap.Entry> iter = Boolean2FloatMaps.fastIterator(AbstractBoolean2FloatMap.this);
					@Override
					public boolean hasNext() {
						return iter.hasNext();
					}

					@Override
					public boolean nextBoolean() {
						return iter.next().getBooleanKey();
					}
					
					@Override
					public void remove() {
						iter.remove();
					}
				};
			}
			
			@Override
			public int size() {
				return AbstractBoolean2FloatMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractBoolean2FloatMap.this.clear();
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
				return AbstractBoolean2FloatMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractBoolean2FloatMap.this.clear();
			}
			
			@Override
			public FloatIterator iterator() {
				return new FloatIterator() {
					ObjectIterator<Boolean2FloatMap.Entry> iter = Boolean2FloatMaps.fastIterator(AbstractBoolean2FloatMap.this);
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
	public ObjectSet<Map.Entry<Boolean, Float>> entrySet() {
		return (ObjectSet)boolean2FloatEntrySet();
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o instanceof Map) {
			if(size() != ((Map<?, ?>)o).size()) return false;
			if(o instanceof Boolean2FloatMap) return boolean2FloatEntrySet().containsAll(((Boolean2FloatMap)o).boolean2FloatEntrySet());
			return boolean2FloatEntrySet().containsAll(((Map<?, ?>)o).entrySet());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		ObjectIterator<Boolean2FloatMap.Entry> iter = Boolean2FloatMaps.fastIterator(this);
		while(iter.hasNext()) hash += iter.next().hashCode();
		return hash;
	}
	
	/**
	 * A Simple Type Specific Entry class to reduce boxing/unboxing
	 */
	public static class BasicEntry implements Boolean2FloatMap.Entry {
		protected boolean key;
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
		public BasicEntry(Boolean key, Float value) {
			this.key = key.booleanValue();
			this.value = value.floatValue();
		}
		
		/**
		 * A Type Specific Constructor
		 * @param key the key of a entry
		 * @param value the value of a entry 
		 */
		public BasicEntry(boolean key, float value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * A Helper method for fast replacing values
		 * @param key the key that should be replaced
		 * @param value the value that should be replaced
		 */
		public void set(boolean key, float value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public boolean getBooleanKey() {
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
				if(obj instanceof Boolean2FloatMap.Entry) {
					Boolean2FloatMap.Entry entry = (Boolean2FloatMap.Entry)obj;
					return key == entry.getBooleanKey() && Float.floatToIntBits(value) == Float.floatToIntBits(entry.getFloatValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Boolean && value instanceof Float && this.key == ((Boolean)key).booleanValue() && Float.floatToIntBits(this.value) == Float.floatToIntBits(((Float)value).floatValue());
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Boolean.hashCode(key) ^ Float.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Boolean.toString(key) + "=" + Float.toString(value);
		}
	}
}