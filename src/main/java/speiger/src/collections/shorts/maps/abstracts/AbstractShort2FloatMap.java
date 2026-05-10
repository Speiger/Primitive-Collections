package speiger.src.collections.shorts.maps.abstracts;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

import speiger.src.collections.shorts.collections.ShortIterator;
import speiger.src.collections.shorts.functions.consumer.ShortFloatConsumer;
import speiger.src.collections.shorts.functions.function.Short2FloatFunction;
import speiger.src.collections.shorts.functions.function.ShortFloatUnaryOperator;
import speiger.src.collections.shorts.maps.interfaces.Short2FloatMap;
import speiger.src.collections.shorts.maps.interfaces.Short2FloatOrderedMap;
import speiger.src.collections.shorts.sets.ShortOrderedSet;
import speiger.src.collections.floats.collections.FloatOrderedCollection;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
import speiger.src.collections.shorts.sets.AbstractShortSet;
import speiger.src.collections.shorts.sets.ShortSet;
import speiger.src.collections.shorts.utils.maps.Short2FloatMaps;
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
public abstract class AbstractShort2FloatMap extends AbstractMap<Short, Float> implements Short2FloatMap
{
	protected float defaultReturnValue = -1F;
	
	@Override
	public float getDefaultReturnValue() {
		return defaultReturnValue;
	}
	
	@Override
	public AbstractShort2FloatMap setDefaultReturnValue(float v) {
		defaultReturnValue = v;
		return this;
	}
	
	protected ObjectIterable<Short2FloatMap.Entry> getFastIterable(Short2FloatMap map) {
		return Short2FloatMaps.fastIterable(map);
	}
	
	protected ObjectIterator<Short2FloatMap.Entry> getFastIterator(Short2FloatMap map) {
		return Short2FloatMaps.fastIterator(map);
	}
	
	@Override
	public Short2FloatMap copy() { 
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public Float put(Short key, Float value) {
		return Float.valueOf(put(key.shortValue(), value.floatValue()));
	}
	
	@Override
	public void addToAll(Short2FloatMap m) {
		for(Short2FloatMap.Entry entry : getFastIterable(m))
			addTo(entry.getShortKey(), entry.getFloatValue());
	}
	
	@Override
	public void putAll(Short2FloatMap m) {
		for(ObjectIterator<Short2FloatMap.Entry> iter = getFastIterator(m);iter.hasNext();) {
			Short2FloatMap.Entry entry = iter.next();
			put(entry.getShortKey(), entry.getFloatValue());
		}
	}
	
	@Override
	public void putAll(Map<? extends Short, ? extends Float> m)
	{
		if(m instanceof Short2FloatMap) putAll((Short2FloatMap)m);
		else super.putAll(m);
	}
	
	@Override
	public void putAll(short[] keys, float[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);
	}
	
	@Override
	public void putAll(Short[] keys, Float[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i].shortValue(), values[i].floatValue());		
	}
	
	@Override
	public void putAllIfAbsent(Short2FloatMap m) {
		for(Short2FloatMap.Entry entry : getFastIterable(m))
			putIfAbsent(entry.getShortKey(), entry.getFloatValue());
	}
	
	
	@Override
	public boolean containsKey(short key) {
		for(ShortIterator iter = keySet().iterator();iter.hasNext();)
			if(iter.nextShort() == key) return true;
		return false;
	}
	
	@Override
	public boolean containsValue(float value) {
		for(FloatIterator iter = values().iterator();iter.hasNext();)
			if(Float.floatToIntBits(iter.nextFloat()) == Float.floatToIntBits(value)) return true;
		return false;
	}
	
	@Override
	public boolean replace(short key, float oldValue, float newValue) {
		float curValue = get(key);
		if (Float.floatToIntBits(curValue) != Float.floatToIntBits(oldValue) || (Float.floatToIntBits(curValue) == Float.floatToIntBits(getDefaultReturnValue()) && !containsKey(key))) {
			return false;
		}
		put(key, newValue);
		return true;
	}

	@Override
	public float replace(short key, float value) {
		float curValue;
		if (Float.floatToIntBits((curValue = get(key))) != Float.floatToIntBits(getDefaultReturnValue()) || containsKey(key)) {
			curValue = put(key, value);
		}
		return curValue;
	}
	
	@Override
	public void replaceFloats(Short2FloatMap m) {
		for(Short2FloatMap.Entry entry : getFastIterable(m))
			replace(entry.getShortKey(), entry.getFloatValue());
	}
	
	@Override
	public void replaceFloats(ShortFloatUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(ObjectIterator<Short2FloatMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Short2FloatMap.Entry entry = iter.next();
			entry.setValue(mappingFunction.applyAsFloat(entry.getShortKey(), entry.getFloatValue()));
		}
	}

	@Override
	public float computeFloat(short key, ShortFloatUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		float newValue = mappingFunction.applyAsFloat(key, get(key));
		put(key, newValue);
		return newValue;
	}
	
	@Override
	public float computeFloatIfAbsent(short key, Short2FloatFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(!containsKey(key)) {
			float newValue = mappingFunction.applyAsFloat(key);
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public float supplyFloatIfAbsent(short key, FloatSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		if(!containsKey(key)) {
			float newValue = valueProvider.getAsFloat();
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public float computeFloatIfPresent(short key, ShortFloatUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(containsKey(key)) {
			float newValue = mappingFunction.applyAsFloat(key, get(key));
			put(key, newValue);
			return newValue;
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public float computeFloatNonDefault(short key, ShortFloatUnaryOperator mappingFunction) {
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
	public float computeFloatIfAbsentNonDefault(short key, Short2FloatFunction mappingFunction) {
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
	public float supplyFloatIfAbsentNonDefault(short key, FloatSupplier valueProvider) {
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
	public float computeFloatIfPresentNonDefault(short key, ShortFloatUnaryOperator mappingFunction) {
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
	public float mergeFloat(short key, float value, FloatFloatUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		float oldValue = get(key);
		float newValue = Float.floatToIntBits(oldValue) == Float.floatToIntBits(getDefaultReturnValue()) ? value : mappingFunction.applyAsFloat(oldValue, value);
		if(Float.floatToIntBits(newValue) == Float.floatToIntBits(getDefaultReturnValue())) remove(key);
		else put(key, newValue);
		return newValue;
	}
	
	@Override
	public void mergeAllFloat(Short2FloatMap m, FloatFloatUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Short2FloatMap.Entry entry : getFastIterable(m)) {
			short key = entry.getShortKey();
			float oldValue = get(key);
			float newValue = Float.floatToIntBits(oldValue) == Float.floatToIntBits(getDefaultReturnValue()) ? entry.getFloatValue() : mappingFunction.applyAsFloat(oldValue, entry.getFloatValue());
			if(Float.floatToIntBits(newValue) == Float.floatToIntBits(getDefaultReturnValue())) remove(key);
			else put(key, newValue);
		}
	}
	
	@Override
	public Float get(Object key) {
		return Float.valueOf(key instanceof Short ? get(((Short)key).shortValue()) : getDefaultReturnValue());
	}
	
	@Override
	public Float getOrDefault(Object key, Float defaultValue) {
		return Float.valueOf(key instanceof Short ? getOrDefault(((Short)key).shortValue(), defaultValue.floatValue()) : getDefaultReturnValue());
	}
	
	@Override
	public float getOrDefault(short key, float defaultValue) {
		float value = get(key);
		return Float.floatToIntBits(value) != Float.floatToIntBits(getDefaultReturnValue()) || containsKey(key) ? value : defaultValue;
	}
	
	
	@Override
	public Float remove(Object key) {
		return key instanceof Short ? Float.valueOf(remove(((Short)key).shortValue())) : Float.valueOf(getDefaultReturnValue());
	}
	
	@Override
	public void forEach(ShortFloatConsumer action) {
		Objects.requireNonNull(action);
		for(ObjectIterator<Short2FloatMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Short2FloatMap.Entry entry = iter.next();
			action.accept(entry.getShortKey(), entry.getFloatValue());
		}
	}

	@Override
	public ShortSet keySet() {
		return new AbstractShortSet() {
			@Override
			public boolean remove(short o) {
				return Float.floatToIntBits(AbstractShort2FloatMap.this.remove(o)) != Float.floatToIntBits(getDefaultReturnValue());
			}
			
			@Override
			public boolean add(short o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public ShortIterator iterator() {
				return new ShortIterator() {
					ObjectIterator<Short2FloatMap.Entry> iter = getFastIterator(AbstractShort2FloatMap.this);
					@Override
					public boolean hasNext() {
						return iter.hasNext();
					}

					@Override
					public short nextShort() {
						return iter.next().getShortKey();
					}
					
					@Override
					public void remove() {
						iter.remove();
					}
				};
			}
			
			@Override
			public int size() {
				return AbstractShort2FloatMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractShort2FloatMap.this.clear();
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
				return AbstractShort2FloatMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractShort2FloatMap.this.clear();
			}
			
			@Override
			public FloatIterator iterator() {
				return new FloatIterator() {
					ObjectIterator<Short2FloatMap.Entry> iter = getFastIterator(AbstractShort2FloatMap.this);
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
	public ObjectSet<Map.Entry<Short, Float>> entrySet() {
		return (ObjectSet)short2FloatEntrySet();
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o instanceof Map) {
			if(size() != ((Map<?, ?>)o).size()) return false;
			if(o instanceof Short2FloatMap) return short2FloatEntrySet().containsAll(((Short2FloatMap)o).short2FloatEntrySet());
			return short2FloatEntrySet().containsAll(((Map<?, ?>)o).entrySet());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		ObjectIterator<Short2FloatMap.Entry> iter = getFastIterator(this);
		while(iter.hasNext()) hash += iter.next().hashCode();
		return hash;
	}
	
	public static class ReversedShort2FloatOrderedMap extends AbstractShort2FloatMap implements Short2FloatOrderedMap {
		Short2FloatOrderedMap map;
		
		public ReversedShort2FloatOrderedMap(Short2FloatOrderedMap map) {
			this.map = map;
		}
		@Override
		public AbstractShort2FloatMap setDefaultReturnValue(float v) {
			map.setDefaultReturnValue(v);
			return this;
		}
		@Override
		public float getDefaultReturnValue() { return map.getDefaultReturnValue(); }
		@Override
		public Short2FloatOrderedMap copy() { throw new UnsupportedOperationException(); }
		@Override
		public float put(short key, float value) { return map.put(key, value); }
		@Override
		public float putIfAbsent(short key, float value) { return map.putIfAbsent(key, value); }
		@Override
		public float addTo(short key, float value) { return map.addTo(key, value); }
		@Override
		public float subFrom(short key, float value) { return map.subFrom(key, value); }
		@Override
		public float remove(short key) { return map.remove(key); }
		@Override
		public boolean remove(short key, float value) { return map.remove(key, value); }
		@Override
		public float removeOrDefault(short key, float defaultValue) { return map.removeOrDefault(key, defaultValue); }
		@Override
		public boolean containsKey(short key) { return map.containsKey(key); }
		@Override
		public boolean containsValue(float value) { return map.containsValue(value); }
		@Override
		public boolean replace(short key, float oldValue, float newValue) { return map.replace(key, oldValue, newValue); }
		@Override
		public float replace(short key, float value) { return map.replace(key, value); }
		@Override
		public void replaceFloats(Short2FloatMap m) { map.replaceFloats(m); }
		@Override
		public void replaceFloats(ShortFloatUnaryOperator mappingFunction) { map.replaceFloats(mappingFunction); }
		@Override
		public float computeFloat(short key, ShortFloatUnaryOperator mappingFunction) { return map.computeFloat(key, mappingFunction); }
		@Override
		public float computeFloatIfAbsent(short key, Short2FloatFunction mappingFunction) { return map.computeFloatIfAbsent(key, mappingFunction); }
		@Override
		public float supplyFloatIfAbsent(short key, FloatSupplier valueProvider) { return map.supplyFloatIfAbsent(key, valueProvider); }
		@Override
		public float computeFloatIfPresent(short key, ShortFloatUnaryOperator mappingFunction) { return map.computeFloatIfPresent(key, mappingFunction); }
		@Override
		public float computeFloatNonDefault(short key, ShortFloatUnaryOperator mappingFunction) { return map.computeFloatNonDefault(key, mappingFunction); }
		@Override
		public float computeFloatIfAbsentNonDefault(short key, Short2FloatFunction mappingFunction) { return map.computeFloatIfAbsentNonDefault(key, mappingFunction); }
		@Override
		public float supplyFloatIfAbsentNonDefault(short key, FloatSupplier valueProvider) { return map.supplyFloatIfAbsentNonDefault(key, valueProvider); }
		@Override
		public float computeFloatIfPresentNonDefault(short key, ShortFloatUnaryOperator mappingFunction) { return map.computeFloatIfPresentNonDefault(key, mappingFunction); }
		@Override
		public float mergeFloat(short key, float value, FloatFloatUnaryOperator mappingFunction) { return map.mergeFloat(key, value, mappingFunction); }
		@Override
		public float getOrDefault(short key, float defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public float get(short key) { return map.get(key); }
		@Override
		public float putAndMoveToFirst(short key, float value) { return map.putAndMoveToLast(key, value); }
		@Override
		public float putAndMoveToLast(short key, float value) { return map.putAndMoveToFirst(key, value); }
		@Override
		public float putFirst(short key, float value) { return map.putLast(key, value); }
		@Override
		public float putLast(short key, float value) { return map.putFirst(key, value); }
		@Override
		public boolean moveToFirst(short key) { return map.moveToLast(key); }
		@Override
		public boolean moveToLast(short key) { return map.moveToFirst(key); }
		@Override
		public float getAndMoveToFirst(short key) { return map.getAndMoveToLast(key); }
		@Override
		public float getAndMoveToLast(short key) { return map.getAndMoveToFirst(key); }
		@Override
		public short firstShortKey() { return map.lastShortKey(); }
		@Override
		public short pollFirstShortKey() { return map.pollLastShortKey(); }
		@Override
		public short lastShortKey() { return map.firstShortKey(); }
		@Override
		public short pollLastShortKey() { return map.pollFirstShortKey(); }
		@Override
		public float firstFloatValue() { return map.lastFloatValue(); }
		@Override
		public float lastFloatValue() { return map.firstFloatValue(); }
		@Override
		public Short2FloatMap.Entry firstEntry() { return map.lastEntry(); }
		@Override
		public Short2FloatMap.Entry lastEntry() { return map.firstEntry(); }
		@Override
		public Short2FloatMap.Entry pollFirstEntry() { return map.pollLastEntry(); }
		@Override
		public Short2FloatMap.Entry pollLastEntry() { return map.pollFirstEntry(); }
		@Override
		public ObjectOrderedSet<Short2FloatMap.Entry> short2FloatEntrySet() { return new AbstractObjectSet.ReversedObjectOrderedSet<>(map.short2FloatEntrySet()); }
		@Override
		public ShortOrderedSet keySet() { return new AbstractShortSet.ReversedShortOrderedSet(map.keySet()); }
		@Override
		public FloatOrderedCollection values() { return map.values().reversed(); }
		@Override
		public Short2FloatOrderedMap reversed() { return map; }
	}

	
	/**
	 * A Simple Type Specific Entry class to reduce boxing/unboxing
	 */
	public static class BasicEntry implements Short2FloatMap.Entry {
		protected short key;
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
		public BasicEntry(Short key, Float value) {
			this.key = key.shortValue();
			this.value = value.floatValue();
		}
		
		/**
		 * A Type Specific Constructor
		 * @param key the key of a entry
		 * @param value the value of a entry 
		 */
		public BasicEntry(short key, float value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * A Helper method for fast replacing values
		 * @param key the key that should be replaced
		 * @param value the value that should be replaced
		 */
		public void set(short key, float value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public short getShortKey() {
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
				if(obj instanceof Short2FloatMap.Entry) {
					Short2FloatMap.Entry entry = (Short2FloatMap.Entry)obj;
					return key == entry.getShortKey() && Float.floatToIntBits(value) == Float.floatToIntBits(entry.getFloatValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Short && value instanceof Float && this.key == ((Short)key).shortValue() && Float.floatToIntBits(this.value) == Float.floatToIntBits(((Float)value).floatValue());
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Short.hashCode(key) ^ Float.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Short.toString(key) + "=" + Float.toString(value);
		}
	}
}