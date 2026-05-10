package speiger.src.collections.floats.maps.abstracts;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

import speiger.src.collections.floats.collections.FloatIterator;
import speiger.src.collections.floats.functions.consumer.FloatShortConsumer;
import speiger.src.collections.floats.functions.function.Float2ShortFunction;
import speiger.src.collections.floats.functions.function.FloatShortUnaryOperator;
import speiger.src.collections.floats.maps.interfaces.Float2ShortMap;
import speiger.src.collections.floats.maps.interfaces.Float2ShortOrderedMap;
import speiger.src.collections.floats.sets.FloatOrderedSet;
import speiger.src.collections.shorts.collections.ShortOrderedCollection;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
import speiger.src.collections.floats.sets.AbstractFloatSet;
import speiger.src.collections.floats.sets.FloatSet;
import speiger.src.collections.floats.utils.maps.Float2ShortMaps;
import speiger.src.collections.shorts.collections.AbstractShortCollection;
import speiger.src.collections.shorts.collections.ShortCollection;
import speiger.src.collections.shorts.collections.ShortIterator;
import speiger.src.collections.shorts.functions.function.ShortShortUnaryOperator;
import speiger.src.collections.shorts.functions.ShortSupplier;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Base Implementation of a Type Specific Map to reduce boxing/unboxing
 */
public abstract class AbstractFloat2ShortMap extends AbstractMap<Float, Short> implements Float2ShortMap
{
	protected short defaultReturnValue = (short)-1;
	
	@Override
	public short getDefaultReturnValue() {
		return defaultReturnValue;
	}
	
	@Override
	public AbstractFloat2ShortMap setDefaultReturnValue(short v) {
		defaultReturnValue = v;
		return this;
	}
	
	protected ObjectIterable<Float2ShortMap.Entry> getFastIterable(Float2ShortMap map) {
		return Float2ShortMaps.fastIterable(map);
	}
	
	protected ObjectIterator<Float2ShortMap.Entry> getFastIterator(Float2ShortMap map) {
		return Float2ShortMaps.fastIterator(map);
	}
	
	@Override
	public Float2ShortMap copy() { 
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public Short put(Float key, Short value) {
		return Short.valueOf(put(key.floatValue(), value.shortValue()));
	}
	
	@Override
	public void addToAll(Float2ShortMap m) {
		for(Float2ShortMap.Entry entry : getFastIterable(m))
			addTo(entry.getFloatKey(), entry.getShortValue());
	}
	
	@Override
	public void putAll(Float2ShortMap m) {
		for(ObjectIterator<Float2ShortMap.Entry> iter = getFastIterator(m);iter.hasNext();) {
			Float2ShortMap.Entry entry = iter.next();
			put(entry.getFloatKey(), entry.getShortValue());
		}
	}
	
	@Override
	public void putAll(Map<? extends Float, ? extends Short> m)
	{
		if(m instanceof Float2ShortMap) putAll((Float2ShortMap)m);
		else super.putAll(m);
	}
	
	@Override
	public void putAll(float[] keys, short[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);
	}
	
	@Override
	public void putAll(Float[] keys, Short[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i].floatValue(), values[i].shortValue());		
	}
	
	@Override
	public void putAllIfAbsent(Float2ShortMap m) {
		for(Float2ShortMap.Entry entry : getFastIterable(m))
			putIfAbsent(entry.getFloatKey(), entry.getShortValue());
	}
	
	
	@Override
	public boolean containsKey(float key) {
		for(FloatIterator iter = keySet().iterator();iter.hasNext();)
			if(Float.floatToIntBits(iter.nextFloat()) == Float.floatToIntBits(key)) return true;
		return false;
	}
	
	@Override
	public boolean containsValue(short value) {
		for(ShortIterator iter = values().iterator();iter.hasNext();)
			if(iter.nextShort() == value) return true;
		return false;
	}
	
	@Override
	public boolean replace(float key, short oldValue, short newValue) {
		short curValue = get(key);
		if (curValue != oldValue || (curValue == getDefaultReturnValue() && !containsKey(key))) {
			return false;
		}
		put(key, newValue);
		return true;
	}

	@Override
	public short replace(float key, short value) {
		short curValue;
		if ((curValue = get(key)) != getDefaultReturnValue() || containsKey(key)) {
			curValue = put(key, value);
		}
		return curValue;
	}
	
	@Override
	public void replaceShorts(Float2ShortMap m) {
		for(Float2ShortMap.Entry entry : getFastIterable(m))
			replace(entry.getFloatKey(), entry.getShortValue());
	}
	
	@Override
	public void replaceShorts(FloatShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(ObjectIterator<Float2ShortMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Float2ShortMap.Entry entry = iter.next();
			entry.setValue(mappingFunction.applyAsShort(entry.getFloatKey(), entry.getShortValue()));
		}
	}

	@Override
	public short computeShort(float key, FloatShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		short newValue = mappingFunction.applyAsShort(key, get(key));
		put(key, newValue);
		return newValue;
	}
	
	@Override
	public short computeShortIfAbsent(float key, Float2ShortFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(!containsKey(key)) {
			short newValue = mappingFunction.applyAsShort(key);
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public short supplyShortIfAbsent(float key, ShortSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		if(!containsKey(key)) {
			short newValue = valueProvider.getAsShort();
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public short computeShortIfPresent(float key, FloatShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(containsKey(key)) {
			short newValue = mappingFunction.applyAsShort(key, get(key));
			put(key, newValue);
			return newValue;
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public short computeShortNonDefault(float key, FloatShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		short value = get(key);
		short newValue = mappingFunction.applyAsShort(key, value);
		if(newValue == getDefaultReturnValue()) {
			if(value != getDefaultReturnValue() || containsKey(key)) {
				remove(key);
				return getDefaultReturnValue();
			}
			return getDefaultReturnValue();
		}
		put(key, newValue);
		return newValue;
	}
	
	@Override
	public short computeShortIfAbsentNonDefault(float key, Float2ShortFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		short value;
		if((value = get(key)) == getDefaultReturnValue() || !containsKey(key)) {
			short newValue = mappingFunction.applyAsShort(key);
			if(newValue != getDefaultReturnValue()) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public short supplyShortIfAbsentNonDefault(float key, ShortSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		short value;
		if((value = get(key)) == getDefaultReturnValue() || !containsKey(key)) {
			short newValue = valueProvider.getAsShort();
			if(newValue != getDefaultReturnValue()) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public short computeShortIfPresentNonDefault(float key, FloatShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		short value;
		if((value = get(key)) != getDefaultReturnValue() || containsKey(key)) {
			short newValue = mappingFunction.applyAsShort(key, value);
			if(newValue != getDefaultReturnValue()) {
				put(key, newValue);
				return newValue;
			}
			remove(key);
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public short mergeShort(float key, short value, ShortShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		short oldValue = get(key);
		short newValue = oldValue == getDefaultReturnValue() ? value : mappingFunction.applyAsShort(oldValue, value);
		if(newValue == getDefaultReturnValue()) remove(key);
		else put(key, newValue);
		return newValue;
	}
	
	@Override
	public void mergeAllShort(Float2ShortMap m, ShortShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Float2ShortMap.Entry entry : getFastIterable(m)) {
			float key = entry.getFloatKey();
			short oldValue = get(key);
			short newValue = oldValue == getDefaultReturnValue() ? entry.getShortValue() : mappingFunction.applyAsShort(oldValue, entry.getShortValue());
			if(newValue == getDefaultReturnValue()) remove(key);
			else put(key, newValue);
		}
	}
	
	@Override
	public Short get(Object key) {
		return Short.valueOf(key instanceof Float ? get(((Float)key).floatValue()) : getDefaultReturnValue());
	}
	
	@Override
	public Short getOrDefault(Object key, Short defaultValue) {
		return Short.valueOf(key instanceof Float ? getOrDefault(((Float)key).floatValue(), defaultValue.shortValue()) : getDefaultReturnValue());
	}
	
	@Override
	public short getOrDefault(float key, short defaultValue) {
		short value = get(key);
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	
	@Override
	public Short remove(Object key) {
		return key instanceof Float ? Short.valueOf(remove(((Float)key).floatValue())) : Short.valueOf(getDefaultReturnValue());
	}
	
	@Override
	public void forEach(FloatShortConsumer action) {
		Objects.requireNonNull(action);
		for(ObjectIterator<Float2ShortMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Float2ShortMap.Entry entry = iter.next();
			action.accept(entry.getFloatKey(), entry.getShortValue());
		}
	}

	@Override
	public FloatSet keySet() {
		return new AbstractFloatSet() {
			@Override
			public boolean remove(float o) {
				return AbstractFloat2ShortMap.this.remove(o) != getDefaultReturnValue();
			}
			
			@Override
			public boolean add(float o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public FloatIterator iterator() {
				return new FloatIterator() {
					ObjectIterator<Float2ShortMap.Entry> iter = getFastIterator(AbstractFloat2ShortMap.this);
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
				return AbstractFloat2ShortMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractFloat2ShortMap.this.clear();
			}
		};
	}

	@Override
	public ShortCollection values() {
		return new AbstractShortCollection() {
			@Override
			public boolean add(short o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public int size() {
				return AbstractFloat2ShortMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractFloat2ShortMap.this.clear();
			}
			
			@Override
			public ShortIterator iterator() {
				return new ShortIterator() {
					ObjectIterator<Float2ShortMap.Entry> iter = getFastIterator(AbstractFloat2ShortMap.this);
					@Override
					public boolean hasNext() {
						return iter.hasNext();
					}
					
					@Override
					public short nextShort() {
						return iter.next().getShortValue();
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
	public ObjectSet<Map.Entry<Float, Short>> entrySet() {
		return (ObjectSet)float2ShortEntrySet();
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o instanceof Map) {
			if(size() != ((Map<?, ?>)o).size()) return false;
			if(o instanceof Float2ShortMap) return float2ShortEntrySet().containsAll(((Float2ShortMap)o).float2ShortEntrySet());
			return float2ShortEntrySet().containsAll(((Map<?, ?>)o).entrySet());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		ObjectIterator<Float2ShortMap.Entry> iter = getFastIterator(this);
		while(iter.hasNext()) hash += iter.next().hashCode();
		return hash;
	}
	
	public static class ReversedFloat2ShortOrderedMap extends AbstractFloat2ShortMap implements Float2ShortOrderedMap {
		Float2ShortOrderedMap map;
		
		public ReversedFloat2ShortOrderedMap(Float2ShortOrderedMap map) {
			this.map = map;
		}
		@Override
		public AbstractFloat2ShortMap setDefaultReturnValue(short v) {
			map.setDefaultReturnValue(v);
			return this;
		}
		@Override
		public short getDefaultReturnValue() { return map.getDefaultReturnValue(); }
		@Override
		public Float2ShortOrderedMap copy() { throw new UnsupportedOperationException(); }
		@Override
		public short put(float key, short value) { return map.put(key, value); }
		@Override
		public short putIfAbsent(float key, short value) { return map.putIfAbsent(key, value); }
		@Override
		public short addTo(float key, short value) { return map.addTo(key, value); }
		@Override
		public short subFrom(float key, short value) { return map.subFrom(key, value); }
		@Override
		public short remove(float key) { return map.remove(key); }
		@Override
		public boolean remove(float key, short value) { return map.remove(key, value); }
		@Override
		public short removeOrDefault(float key, short defaultValue) { return map.removeOrDefault(key, defaultValue); }
		@Override
		public boolean containsKey(float key) { return map.containsKey(key); }
		@Override
		public boolean containsValue(short value) { return map.containsValue(value); }
		@Override
		public boolean replace(float key, short oldValue, short newValue) { return map.replace(key, oldValue, newValue); }
		@Override
		public short replace(float key, short value) { return map.replace(key, value); }
		@Override
		public void replaceShorts(Float2ShortMap m) { map.replaceShorts(m); }
		@Override
		public void replaceShorts(FloatShortUnaryOperator mappingFunction) { map.replaceShorts(mappingFunction); }
		@Override
		public short computeShort(float key, FloatShortUnaryOperator mappingFunction) { return map.computeShort(key, mappingFunction); }
		@Override
		public short computeShortIfAbsent(float key, Float2ShortFunction mappingFunction) { return map.computeShortIfAbsent(key, mappingFunction); }
		@Override
		public short supplyShortIfAbsent(float key, ShortSupplier valueProvider) { return map.supplyShortIfAbsent(key, valueProvider); }
		@Override
		public short computeShortIfPresent(float key, FloatShortUnaryOperator mappingFunction) { return map.computeShortIfPresent(key, mappingFunction); }
		@Override
		public short computeShortNonDefault(float key, FloatShortUnaryOperator mappingFunction) { return map.computeShortNonDefault(key, mappingFunction); }
		@Override
		public short computeShortIfAbsentNonDefault(float key, Float2ShortFunction mappingFunction) { return map.computeShortIfAbsentNonDefault(key, mappingFunction); }
		@Override
		public short supplyShortIfAbsentNonDefault(float key, ShortSupplier valueProvider) { return map.supplyShortIfAbsentNonDefault(key, valueProvider); }
		@Override
		public short computeShortIfPresentNonDefault(float key, FloatShortUnaryOperator mappingFunction) { return map.computeShortIfPresentNonDefault(key, mappingFunction); }
		@Override
		public short mergeShort(float key, short value, ShortShortUnaryOperator mappingFunction) { return map.mergeShort(key, value, mappingFunction); }
		@Override
		public short getOrDefault(float key, short defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public short get(float key) { return map.get(key); }
		@Override
		public short putAndMoveToFirst(float key, short value) { return map.putAndMoveToLast(key, value); }
		@Override
		public short putAndMoveToLast(float key, short value) { return map.putAndMoveToFirst(key, value); }
		@Override
		public short putFirst(float key, short value) { return map.putLast(key, value); }
		@Override
		public short putLast(float key, short value) { return map.putFirst(key, value); }
		@Override
		public boolean moveToFirst(float key) { return map.moveToLast(key); }
		@Override
		public boolean moveToLast(float key) { return map.moveToFirst(key); }
		@Override
		public short getAndMoveToFirst(float key) { return map.getAndMoveToLast(key); }
		@Override
		public short getAndMoveToLast(float key) { return map.getAndMoveToFirst(key); }
		@Override
		public float firstFloatKey() { return map.lastFloatKey(); }
		@Override
		public float pollFirstFloatKey() { return map.pollLastFloatKey(); }
		@Override
		public float lastFloatKey() { return map.firstFloatKey(); }
		@Override
		public float pollLastFloatKey() { return map.pollFirstFloatKey(); }
		@Override
		public short firstShortValue() { return map.lastShortValue(); }
		@Override
		public short lastShortValue() { return map.firstShortValue(); }
		@Override
		public Float2ShortMap.Entry firstEntry() { return map.lastEntry(); }
		@Override
		public Float2ShortMap.Entry lastEntry() { return map.firstEntry(); }
		@Override
		public Float2ShortMap.Entry pollFirstEntry() { return map.pollLastEntry(); }
		@Override
		public Float2ShortMap.Entry pollLastEntry() { return map.pollFirstEntry(); }
		@Override
		public ObjectOrderedSet<Float2ShortMap.Entry> float2ShortEntrySet() { return new AbstractObjectSet.ReversedObjectOrderedSet<>(map.float2ShortEntrySet()); }
		@Override
		public FloatOrderedSet keySet() { return new AbstractFloatSet.ReversedFloatOrderedSet(map.keySet()); }
		@Override
		public ShortOrderedCollection values() { return map.values().reversed(); }
		@Override
		public Float2ShortOrderedMap reversed() { return map; }
	}

	
	/**
	 * A Simple Type Specific Entry class to reduce boxing/unboxing
	 */
	public static class BasicEntry implements Float2ShortMap.Entry {
		protected float key;
		protected short value;
		
		/**
		 * A basic Empty constructor
		 */
		public BasicEntry() {}
		/**
		 * A Boxed Constructor for supporting java variants
		 * @param key the key of a entry
		 * @param value the value of a entry
		 */
		public BasicEntry(Float key, Short value) {
			this.key = key.floatValue();
			this.value = value.shortValue();
		}
		
		/**
		 * A Type Specific Constructor
		 * @param key the key of a entry
		 * @param value the value of a entry 
		 */
		public BasicEntry(float key, short value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * A Helper method for fast replacing values
		 * @param key the key that should be replaced
		 * @param value the value that should be replaced
		 */
		public void set(float key, short value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public float getFloatKey() {
			return key;
		}

		@Override
		public short getShortValue() {
			return value;
		}

		@Override
		public short setValue(short value) {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Map.Entry) {
				if(obj instanceof Float2ShortMap.Entry) {
					Float2ShortMap.Entry entry = (Float2ShortMap.Entry)obj;
					return Float.floatToIntBits(key) == Float.floatToIntBits(entry.getFloatKey()) && value == entry.getShortValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Float && value instanceof Short && Float.floatToIntBits(this.key) == Float.floatToIntBits(((Float)key).floatValue()) && this.value == ((Short)value).shortValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Float.hashCode(key) ^ Short.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Float.toString(key) + "=" + Short.toString(value);
		}
	}
}