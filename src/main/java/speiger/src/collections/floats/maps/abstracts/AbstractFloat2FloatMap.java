package speiger.src.collections.floats.maps.abstracts;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

import speiger.src.collections.floats.collections.FloatIterator;
import speiger.src.collections.floats.functions.consumer.FloatFloatConsumer;
import speiger.src.collections.floats.functions.function.FloatUnaryOperator;
import speiger.src.collections.floats.functions.function.FloatFloatUnaryOperator;
import speiger.src.collections.floats.maps.interfaces.Float2FloatMap;
import speiger.src.collections.floats.maps.interfaces.Float2FloatOrderedMap;
import speiger.src.collections.floats.sets.FloatOrderedSet;
import speiger.src.collections.floats.collections.FloatOrderedCollection;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
import speiger.src.collections.floats.sets.AbstractFloatSet;
import speiger.src.collections.floats.sets.FloatSet;
import speiger.src.collections.floats.utils.maps.Float2FloatMaps;
import speiger.src.collections.floats.collections.AbstractFloatCollection;
import speiger.src.collections.floats.collections.FloatCollection;
import speiger.src.collections.floats.functions.FloatSupplier;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Base Implementation of a Type Specific Map to reduce boxing/unboxing
 */
public abstract class AbstractFloat2FloatMap extends AbstractMap<Float, Float> implements Float2FloatMap
{
	protected float defaultReturnValue = -1F;
	
	@Override
	public float getDefaultReturnValue() {
		return defaultReturnValue;
	}
	
	@Override
	public AbstractFloat2FloatMap setDefaultReturnValue(float v) {
		defaultReturnValue = v;
		return this;
	}
	
	protected ObjectIterable<Float2FloatMap.Entry> getFastIterable(Float2FloatMap map) {
		return Float2FloatMaps.fastIterable(map);
	}
	
	protected ObjectIterator<Float2FloatMap.Entry> getFastIterator(Float2FloatMap map) {
		return Float2FloatMaps.fastIterator(map);
	}
	
	@Override
	public Float2FloatMap copy() { 
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public Float put(Float key, Float value) {
		return Float.valueOf(put(key.floatValue(), value.floatValue()));
	}
	
	@Override
	public void addToAll(Float2FloatMap m) {
		for(Float2FloatMap.Entry entry : getFastIterable(m))
			addTo(entry.getFloatKey(), entry.getFloatValue());
	}
	
	@Override
	public void putAll(Float2FloatMap m) {
		for(ObjectIterator<Float2FloatMap.Entry> iter = getFastIterator(m);iter.hasNext();) {
			Float2FloatMap.Entry entry = iter.next();
			put(entry.getFloatKey(), entry.getFloatValue());
		}
	}
	
	@Override
	public void putAll(Map<? extends Float, ? extends Float> m)
	{
		if(m instanceof Float2FloatMap) putAll((Float2FloatMap)m);
		else super.putAll(m);
	}
	
	@Override
	public void putAll(float[] keys, float[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);
	}
	
	@Override
	public void putAll(Float[] keys, Float[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i].floatValue(), values[i].floatValue());		
	}
	
	@Override
	public void putAllIfAbsent(Float2FloatMap m) {
		for(Float2FloatMap.Entry entry : getFastIterable(m))
			putIfAbsent(entry.getFloatKey(), entry.getFloatValue());
	}
	
	
	@Override
	public boolean containsKey(float key) {
		for(FloatIterator iter = keySet().iterator();iter.hasNext();)
			if(Float.floatToIntBits(iter.nextFloat()) == Float.floatToIntBits(key)) return true;
		return false;
	}
	
	@Override
	public boolean containsValue(float value) {
		for(FloatIterator iter = values().iterator();iter.hasNext();)
			if(Float.floatToIntBits(iter.nextFloat()) == Float.floatToIntBits(value)) return true;
		return false;
	}
	
	@Override
	public boolean replace(float key, float oldValue, float newValue) {
		float curValue = get(key);
		if (Float.floatToIntBits(curValue) != Float.floatToIntBits(oldValue) || (Float.floatToIntBits(curValue) == Float.floatToIntBits(getDefaultReturnValue()) && !containsKey(key))) {
			return false;
		}
		put(key, newValue);
		return true;
	}

	@Override
	public float replace(float key, float value) {
		float curValue;
		if (Float.floatToIntBits((curValue = get(key))) != Float.floatToIntBits(getDefaultReturnValue()) || containsKey(key)) {
			curValue = put(key, value);
		}
		return curValue;
	}
	
	@Override
	public void replaceFloats(Float2FloatMap m) {
		for(Float2FloatMap.Entry entry : getFastIterable(m))
			replace(entry.getFloatKey(), entry.getFloatValue());
	}
	
	@Override
	public void replaceFloats(FloatFloatUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(ObjectIterator<Float2FloatMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Float2FloatMap.Entry entry = iter.next();
			entry.setValue(mappingFunction.applyAsFloat(entry.getFloatKey(), entry.getFloatValue()));
		}
	}

	@Override
	public float computeFloat(float key, FloatFloatUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		float newValue = mappingFunction.applyAsFloat(key, get(key));
		put(key, newValue);
		return newValue;
	}
	
	@Override
	public float computeFloatIfAbsent(float key, FloatUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(!containsKey(key)) {
			float newValue = mappingFunction.applyAsFloat(key);
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public float supplyFloatIfAbsent(float key, FloatSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		if(!containsKey(key)) {
			float newValue = valueProvider.getAsFloat();
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public float computeFloatIfPresent(float key, FloatFloatUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(containsKey(key)) {
			float newValue = mappingFunction.applyAsFloat(key, get(key));
			put(key, newValue);
			return newValue;
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public float computeFloatNonDefault(float key, FloatFloatUnaryOperator mappingFunction) {
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
	public float computeFloatIfAbsentNonDefault(float key, FloatUnaryOperator mappingFunction) {
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
	public float supplyFloatIfAbsentNonDefault(float key, FloatSupplier valueProvider) {
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
	public float computeFloatIfPresentNonDefault(float key, FloatFloatUnaryOperator mappingFunction) {
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
	public float mergeFloat(float key, float value, FloatFloatUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		float oldValue = get(key);
		float newValue = Float.floatToIntBits(oldValue) == Float.floatToIntBits(getDefaultReturnValue()) ? value : mappingFunction.applyAsFloat(oldValue, value);
		if(Float.floatToIntBits(newValue) == Float.floatToIntBits(getDefaultReturnValue())) remove(key);
		else put(key, newValue);
		return newValue;
	}
	
	@Override
	public void mergeAllFloat(Float2FloatMap m, FloatFloatUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Float2FloatMap.Entry entry : getFastIterable(m)) {
			float key = entry.getFloatKey();
			float oldValue = get(key);
			float newValue = Float.floatToIntBits(oldValue) == Float.floatToIntBits(getDefaultReturnValue()) ? entry.getFloatValue() : mappingFunction.applyAsFloat(oldValue, entry.getFloatValue());
			if(Float.floatToIntBits(newValue) == Float.floatToIntBits(getDefaultReturnValue())) remove(key);
			else put(key, newValue);
		}
	}
	
	@Override
	public Float get(Object key) {
		return Float.valueOf(key instanceof Float ? get(((Float)key).floatValue()) : getDefaultReturnValue());
	}
	
	@Override
	public Float getOrDefault(Object key, Float defaultValue) {
		return Float.valueOf(key instanceof Float ? getOrDefault(((Float)key).floatValue(), defaultValue.floatValue()) : getDefaultReturnValue());
	}
	
	@Override
	public float getOrDefault(float key, float defaultValue) {
		float value = get(key);
		return Float.floatToIntBits(value) != Float.floatToIntBits(getDefaultReturnValue()) || containsKey(key) ? value : defaultValue;
	}
	
	
	@Override
	public Float remove(Object key) {
		return key instanceof Float ? Float.valueOf(remove(((Float)key).floatValue())) : Float.valueOf(getDefaultReturnValue());
	}
	
	@Override
	public void forEach(FloatFloatConsumer action) {
		Objects.requireNonNull(action);
		for(ObjectIterator<Float2FloatMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Float2FloatMap.Entry entry = iter.next();
			action.accept(entry.getFloatKey(), entry.getFloatValue());
		}
	}

	@Override
	public FloatSet keySet() {
		return new AbstractFloatSet() {
			@Override
			public boolean remove(float o) {
				return Float.floatToIntBits(AbstractFloat2FloatMap.this.remove(o)) != Float.floatToIntBits(getDefaultReturnValue());
			}
			
			@Override
			public boolean add(float o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public FloatIterator iterator() {
				return new FloatIterator() {
					ObjectIterator<Float2FloatMap.Entry> iter = getFastIterator(AbstractFloat2FloatMap.this);
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
				return AbstractFloat2FloatMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractFloat2FloatMap.this.clear();
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
				return AbstractFloat2FloatMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractFloat2FloatMap.this.clear();
			}
			
			@Override
			public FloatIterator iterator() {
				return new FloatIterator() {
					ObjectIterator<Float2FloatMap.Entry> iter = getFastIterator(AbstractFloat2FloatMap.this);
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
	public ObjectSet<Map.Entry<Float, Float>> entrySet() {
		return (ObjectSet)float2FloatEntrySet();
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o instanceof Map) {
			if(size() != ((Map<?, ?>)o).size()) return false;
			if(o instanceof Float2FloatMap) return float2FloatEntrySet().containsAll(((Float2FloatMap)o).float2FloatEntrySet());
			return float2FloatEntrySet().containsAll(((Map<?, ?>)o).entrySet());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		ObjectIterator<Float2FloatMap.Entry> iter = getFastIterator(this);
		while(iter.hasNext()) hash += iter.next().hashCode();
		return hash;
	}
	
	public static class ReversedFloat2FloatOrderedMap extends AbstractFloat2FloatMap implements Float2FloatOrderedMap {
		Float2FloatOrderedMap map;
		
		public ReversedFloat2FloatOrderedMap(Float2FloatOrderedMap map) {
			this.map = map;
		}
		@Override
		public AbstractFloat2FloatMap setDefaultReturnValue(float v) {
			map.setDefaultReturnValue(v);
			return this;
		}
		@Override
		public float getDefaultReturnValue() { return map.getDefaultReturnValue(); }
		@Override
		public Float2FloatOrderedMap copy() { throw new UnsupportedOperationException(); }
		@Override
		public float put(float key, float value) { return map.put(key, value); }
		@Override
		public float putIfAbsent(float key, float value) { return map.putIfAbsent(key, value); }
		@Override
		public float addTo(float key, float value) { return map.addTo(key, value); }
		@Override
		public float subFrom(float key, float value) { return map.subFrom(key, value); }
		@Override
		public float remove(float key) { return map.remove(key); }
		@Override
		public boolean remove(float key, float value) { return map.remove(key, value); }
		@Override
		public float removeOrDefault(float key, float defaultValue) { return map.removeOrDefault(key, defaultValue); }
		@Override
		public boolean containsKey(float key) { return map.containsKey(key); }
		@Override
		public boolean containsValue(float value) { return map.containsValue(value); }
		@Override
		public boolean replace(float key, float oldValue, float newValue) { return map.replace(key, oldValue, newValue); }
		@Override
		public float replace(float key, float value) { return map.replace(key, value); }
		@Override
		public void replaceFloats(Float2FloatMap m) { map.replaceFloats(m); }
		@Override
		public void replaceFloats(FloatFloatUnaryOperator mappingFunction) { map.replaceFloats(mappingFunction); }
		@Override
		public float computeFloat(float key, FloatFloatUnaryOperator mappingFunction) { return map.computeFloat(key, mappingFunction); }
		@Override
		public float computeFloatIfAbsent(float key, FloatUnaryOperator mappingFunction) { return map.computeFloatIfAbsent(key, mappingFunction); }
		@Override
		public float supplyFloatIfAbsent(float key, FloatSupplier valueProvider) { return map.supplyFloatIfAbsent(key, valueProvider); }
		@Override
		public float computeFloatIfPresent(float key, FloatFloatUnaryOperator mappingFunction) { return map.computeFloatIfPresent(key, mappingFunction); }
		@Override
		public float computeFloatNonDefault(float key, FloatFloatUnaryOperator mappingFunction) { return map.computeFloatNonDefault(key, mappingFunction); }
		@Override
		public float computeFloatIfAbsentNonDefault(float key, FloatUnaryOperator mappingFunction) { return map.computeFloatIfAbsentNonDefault(key, mappingFunction); }
		@Override
		public float supplyFloatIfAbsentNonDefault(float key, FloatSupplier valueProvider) { return map.supplyFloatIfAbsentNonDefault(key, valueProvider); }
		@Override
		public float computeFloatIfPresentNonDefault(float key, FloatFloatUnaryOperator mappingFunction) { return map.computeFloatIfPresentNonDefault(key, mappingFunction); }
		@Override
		public float mergeFloat(float key, float value, FloatFloatUnaryOperator mappingFunction) { return map.mergeFloat(key, value, mappingFunction); }
		@Override
		public float getOrDefault(float key, float defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public float get(float key) { return map.get(key); }
		@Override
		public float putAndMoveToFirst(float key, float value) { return map.putAndMoveToLast(key, value); }
		@Override
		public float putAndMoveToLast(float key, float value) { return map.putAndMoveToFirst(key, value); }
		@Override
		public float putFirst(float key, float value) { return map.putLast(key, value); }
		@Override
		public float putLast(float key, float value) { return map.putFirst(key, value); }
		@Override
		public boolean moveToFirst(float key) { return map.moveToLast(key); }
		@Override
		public boolean moveToLast(float key) { return map.moveToFirst(key); }
		@Override
		public float getAndMoveToFirst(float key) { return map.getAndMoveToLast(key); }
		@Override
		public float getAndMoveToLast(float key) { return map.getAndMoveToFirst(key); }
		@Override
		public float firstFloatKey() { return map.lastFloatKey(); }
		@Override
		public float pollFirstFloatKey() { return map.pollLastFloatKey(); }
		@Override
		public float lastFloatKey() { return map.firstFloatKey(); }
		@Override
		public float pollLastFloatKey() { return map.pollFirstFloatKey(); }
		@Override
		public float firstFloatValue() { return map.lastFloatValue(); }
		@Override
		public float lastFloatValue() { return map.firstFloatValue(); }
		@Override
		public Float2FloatMap.Entry firstEntry() { return map.lastEntry(); }
		@Override
		public Float2FloatMap.Entry lastEntry() { return map.firstEntry(); }
		@Override
		public Float2FloatMap.Entry pollFirstEntry() { return map.pollLastEntry(); }
		@Override
		public Float2FloatMap.Entry pollLastEntry() { return map.pollFirstEntry(); }
		@Override
		public ObjectOrderedSet<Float2FloatMap.Entry> float2FloatEntrySet() { return new AbstractObjectSet.ReversedObjectOrderedSet<>(map.float2FloatEntrySet()); }
		@Override
		public FloatOrderedSet keySet() { return new AbstractFloatSet.ReversedFloatOrderedSet(map.keySet()); }
		@Override
		public FloatOrderedCollection values() { return map.values().reversed(); }
		@Override
		public Float2FloatOrderedMap reversed() { return map; }
	}

	
	/**
	 * A Simple Type Specific Entry class to reduce boxing/unboxing
	 */
	public static class BasicEntry implements Float2FloatMap.Entry {
		protected float key;
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
		public BasicEntry(Float key, Float value) {
			this.key = key.floatValue();
			this.value = value.floatValue();
		}
		
		/**
		 * A Type Specific Constructor
		 * @param key the key of a entry
		 * @param value the value of a entry 
		 */
		public BasicEntry(float key, float value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * A Helper method for fast replacing values
		 * @param key the key that should be replaced
		 * @param value the value that should be replaced
		 */
		public void set(float key, float value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public float getFloatKey() {
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
				if(obj instanceof Float2FloatMap.Entry) {
					Float2FloatMap.Entry entry = (Float2FloatMap.Entry)obj;
					return Float.floatToIntBits(key) == Float.floatToIntBits(entry.getFloatKey()) && Float.floatToIntBits(value) == Float.floatToIntBits(entry.getFloatValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Float && value instanceof Float && Float.floatToIntBits(this.key) == Float.floatToIntBits(((Float)key).floatValue()) && Float.floatToIntBits(this.value) == Float.floatToIntBits(((Float)value).floatValue());
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Float.hashCode(key) ^ Float.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Float.toString(key) + "=" + Float.toString(value);
		}
	}
}