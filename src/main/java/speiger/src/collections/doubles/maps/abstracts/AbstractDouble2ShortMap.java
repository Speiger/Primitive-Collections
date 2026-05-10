package speiger.src.collections.doubles.maps.abstracts;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

import speiger.src.collections.doubles.collections.DoubleIterator;
import speiger.src.collections.doubles.functions.consumer.DoubleShortConsumer;
import speiger.src.collections.doubles.functions.function.Double2ShortFunction;
import speiger.src.collections.doubles.functions.function.DoubleShortUnaryOperator;
import speiger.src.collections.doubles.maps.interfaces.Double2ShortMap;
import speiger.src.collections.doubles.maps.interfaces.Double2ShortOrderedMap;
import speiger.src.collections.doubles.sets.DoubleOrderedSet;
import speiger.src.collections.shorts.collections.ShortOrderedCollection;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
import speiger.src.collections.doubles.sets.AbstractDoubleSet;
import speiger.src.collections.doubles.sets.DoubleSet;
import speiger.src.collections.doubles.utils.maps.Double2ShortMaps;
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
public abstract class AbstractDouble2ShortMap extends AbstractMap<Double, Short> implements Double2ShortMap
{
	protected short defaultReturnValue = (short)-1;
	
	@Override
	public short getDefaultReturnValue() {
		return defaultReturnValue;
	}
	
	@Override
	public AbstractDouble2ShortMap setDefaultReturnValue(short v) {
		defaultReturnValue = v;
		return this;
	}
	
	protected ObjectIterable<Double2ShortMap.Entry> getFastIterable(Double2ShortMap map) {
		return Double2ShortMaps.fastIterable(map);
	}
	
	protected ObjectIterator<Double2ShortMap.Entry> getFastIterator(Double2ShortMap map) {
		return Double2ShortMaps.fastIterator(map);
	}
	
	@Override
	public Double2ShortMap copy() { 
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public Short put(Double key, Short value) {
		return Short.valueOf(put(key.doubleValue(), value.shortValue()));
	}
	
	@Override
	public void addToAll(Double2ShortMap m) {
		for(Double2ShortMap.Entry entry : getFastIterable(m))
			addTo(entry.getDoubleKey(), entry.getShortValue());
	}
	
	@Override
	public void putAll(Double2ShortMap m) {
		for(ObjectIterator<Double2ShortMap.Entry> iter = getFastIterator(m);iter.hasNext();) {
			Double2ShortMap.Entry entry = iter.next();
			put(entry.getDoubleKey(), entry.getShortValue());
		}
	}
	
	@Override
	public void putAll(Map<? extends Double, ? extends Short> m)
	{
		if(m instanceof Double2ShortMap) putAll((Double2ShortMap)m);
		else super.putAll(m);
	}
	
	@Override
	public void putAll(double[] keys, short[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);
	}
	
	@Override
	public void putAll(Double[] keys, Short[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i].doubleValue(), values[i].shortValue());		
	}
	
	@Override
	public void putAllIfAbsent(Double2ShortMap m) {
		for(Double2ShortMap.Entry entry : getFastIterable(m))
			putIfAbsent(entry.getDoubleKey(), entry.getShortValue());
	}
	
	
	@Override
	public boolean containsKey(double key) {
		for(DoubleIterator iter = keySet().iterator();iter.hasNext();)
			if(Double.doubleToLongBits(iter.nextDouble()) == Double.doubleToLongBits(key)) return true;
		return false;
	}
	
	@Override
	public boolean containsValue(short value) {
		for(ShortIterator iter = values().iterator();iter.hasNext();)
			if(iter.nextShort() == value) return true;
		return false;
	}
	
	@Override
	public boolean replace(double key, short oldValue, short newValue) {
		short curValue = get(key);
		if (curValue != oldValue || (curValue == getDefaultReturnValue() && !containsKey(key))) {
			return false;
		}
		put(key, newValue);
		return true;
	}

	@Override
	public short replace(double key, short value) {
		short curValue;
		if ((curValue = get(key)) != getDefaultReturnValue() || containsKey(key)) {
			curValue = put(key, value);
		}
		return curValue;
	}
	
	@Override
	public void replaceShorts(Double2ShortMap m) {
		for(Double2ShortMap.Entry entry : getFastIterable(m))
			replace(entry.getDoubleKey(), entry.getShortValue());
	}
	
	@Override
	public void replaceShorts(DoubleShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(ObjectIterator<Double2ShortMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Double2ShortMap.Entry entry = iter.next();
			entry.setValue(mappingFunction.applyAsShort(entry.getDoubleKey(), entry.getShortValue()));
		}
	}

	@Override
	public short computeShort(double key, DoubleShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		short newValue = mappingFunction.applyAsShort(key, get(key));
		put(key, newValue);
		return newValue;
	}
	
	@Override
	public short computeShortIfAbsent(double key, Double2ShortFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(!containsKey(key)) {
			short newValue = mappingFunction.applyAsShort(key);
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public short supplyShortIfAbsent(double key, ShortSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		if(!containsKey(key)) {
			short newValue = valueProvider.getAsShort();
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public short computeShortIfPresent(double key, DoubleShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(containsKey(key)) {
			short newValue = mappingFunction.applyAsShort(key, get(key));
			put(key, newValue);
			return newValue;
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public short computeShortNonDefault(double key, DoubleShortUnaryOperator mappingFunction) {
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
	public short computeShortIfAbsentNonDefault(double key, Double2ShortFunction mappingFunction) {
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
	public short supplyShortIfAbsentNonDefault(double key, ShortSupplier valueProvider) {
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
	public short computeShortIfPresentNonDefault(double key, DoubleShortUnaryOperator mappingFunction) {
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
	public short mergeShort(double key, short value, ShortShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		short oldValue = get(key);
		short newValue = oldValue == getDefaultReturnValue() ? value : mappingFunction.applyAsShort(oldValue, value);
		if(newValue == getDefaultReturnValue()) remove(key);
		else put(key, newValue);
		return newValue;
	}
	
	@Override
	public void mergeAllShort(Double2ShortMap m, ShortShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Double2ShortMap.Entry entry : getFastIterable(m)) {
			double key = entry.getDoubleKey();
			short oldValue = get(key);
			short newValue = oldValue == getDefaultReturnValue() ? entry.getShortValue() : mappingFunction.applyAsShort(oldValue, entry.getShortValue());
			if(newValue == getDefaultReturnValue()) remove(key);
			else put(key, newValue);
		}
	}
	
	@Override
	public Short get(Object key) {
		return Short.valueOf(key instanceof Double ? get(((Double)key).doubleValue()) : getDefaultReturnValue());
	}
	
	@Override
	public Short getOrDefault(Object key, Short defaultValue) {
		return Short.valueOf(key instanceof Double ? getOrDefault(((Double)key).doubleValue(), defaultValue.shortValue()) : getDefaultReturnValue());
	}
	
	@Override
	public short getOrDefault(double key, short defaultValue) {
		short value = get(key);
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	
	@Override
	public Short remove(Object key) {
		return key instanceof Double ? Short.valueOf(remove(((Double)key).doubleValue())) : Short.valueOf(getDefaultReturnValue());
	}
	
	@Override
	public void forEach(DoubleShortConsumer action) {
		Objects.requireNonNull(action);
		for(ObjectIterator<Double2ShortMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Double2ShortMap.Entry entry = iter.next();
			action.accept(entry.getDoubleKey(), entry.getShortValue());
		}
	}

	@Override
	public DoubleSet keySet() {
		return new AbstractDoubleSet() {
			@Override
			public boolean remove(double o) {
				return AbstractDouble2ShortMap.this.remove(o) != getDefaultReturnValue();
			}
			
			@Override
			public boolean add(double o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public DoubleIterator iterator() {
				return new DoubleIterator() {
					ObjectIterator<Double2ShortMap.Entry> iter = getFastIterator(AbstractDouble2ShortMap.this);
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
				return AbstractDouble2ShortMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractDouble2ShortMap.this.clear();
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
				return AbstractDouble2ShortMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractDouble2ShortMap.this.clear();
			}
			
			@Override
			public ShortIterator iterator() {
				return new ShortIterator() {
					ObjectIterator<Double2ShortMap.Entry> iter = getFastIterator(AbstractDouble2ShortMap.this);
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
	public ObjectSet<Map.Entry<Double, Short>> entrySet() {
		return (ObjectSet)double2ShortEntrySet();
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o instanceof Map) {
			if(size() != ((Map<?, ?>)o).size()) return false;
			if(o instanceof Double2ShortMap) return double2ShortEntrySet().containsAll(((Double2ShortMap)o).double2ShortEntrySet());
			return double2ShortEntrySet().containsAll(((Map<?, ?>)o).entrySet());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		ObjectIterator<Double2ShortMap.Entry> iter = getFastIterator(this);
		while(iter.hasNext()) hash += iter.next().hashCode();
		return hash;
	}
	
	public static class ReversedDouble2ShortOrderedMap extends AbstractDouble2ShortMap implements Double2ShortOrderedMap {
		Double2ShortOrderedMap map;
		
		public ReversedDouble2ShortOrderedMap(Double2ShortOrderedMap map) {
			this.map = map;
		}
		@Override
		public AbstractDouble2ShortMap setDefaultReturnValue(short v) {
			map.setDefaultReturnValue(v);
			return this;
		}
		@Override
		public short getDefaultReturnValue() { return map.getDefaultReturnValue(); }
		@Override
		public Double2ShortOrderedMap copy() { throw new UnsupportedOperationException(); }
		@Override
		public short put(double key, short value) { return map.put(key, value); }
		@Override
		public short putIfAbsent(double key, short value) { return map.putIfAbsent(key, value); }
		@Override
		public short addTo(double key, short value) { return map.addTo(key, value); }
		@Override
		public short subFrom(double key, short value) { return map.subFrom(key, value); }
		@Override
		public short remove(double key) { return map.remove(key); }
		@Override
		public boolean remove(double key, short value) { return map.remove(key, value); }
		@Override
		public short removeOrDefault(double key, short defaultValue) { return map.removeOrDefault(key, defaultValue); }
		@Override
		public boolean containsKey(double key) { return map.containsKey(key); }
		@Override
		public boolean containsValue(short value) { return map.containsValue(value); }
		@Override
		public boolean replace(double key, short oldValue, short newValue) { return map.replace(key, oldValue, newValue); }
		@Override
		public short replace(double key, short value) { return map.replace(key, value); }
		@Override
		public void replaceShorts(Double2ShortMap m) { map.replaceShorts(m); }
		@Override
		public void replaceShorts(DoubleShortUnaryOperator mappingFunction) { map.replaceShorts(mappingFunction); }
		@Override
		public short computeShort(double key, DoubleShortUnaryOperator mappingFunction) { return map.computeShort(key, mappingFunction); }
		@Override
		public short computeShortIfAbsent(double key, Double2ShortFunction mappingFunction) { return map.computeShortIfAbsent(key, mappingFunction); }
		@Override
		public short supplyShortIfAbsent(double key, ShortSupplier valueProvider) { return map.supplyShortIfAbsent(key, valueProvider); }
		@Override
		public short computeShortIfPresent(double key, DoubleShortUnaryOperator mappingFunction) { return map.computeShortIfPresent(key, mappingFunction); }
		@Override
		public short computeShortNonDefault(double key, DoubleShortUnaryOperator mappingFunction) { return map.computeShortNonDefault(key, mappingFunction); }
		@Override
		public short computeShortIfAbsentNonDefault(double key, Double2ShortFunction mappingFunction) { return map.computeShortIfAbsentNonDefault(key, mappingFunction); }
		@Override
		public short supplyShortIfAbsentNonDefault(double key, ShortSupplier valueProvider) { return map.supplyShortIfAbsentNonDefault(key, valueProvider); }
		@Override
		public short computeShortIfPresentNonDefault(double key, DoubleShortUnaryOperator mappingFunction) { return map.computeShortIfPresentNonDefault(key, mappingFunction); }
		@Override
		public short mergeShort(double key, short value, ShortShortUnaryOperator mappingFunction) { return map.mergeShort(key, value, mappingFunction); }
		@Override
		public short getOrDefault(double key, short defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public short get(double key) { return map.get(key); }
		@Override
		public short putAndMoveToFirst(double key, short value) { return map.putAndMoveToLast(key, value); }
		@Override
		public short putAndMoveToLast(double key, short value) { return map.putAndMoveToFirst(key, value); }
		@Override
		public short putFirst(double key, short value) { return map.putLast(key, value); }
		@Override
		public short putLast(double key, short value) { return map.putFirst(key, value); }
		@Override
		public boolean moveToFirst(double key) { return map.moveToLast(key); }
		@Override
		public boolean moveToLast(double key) { return map.moveToFirst(key); }
		@Override
		public short getAndMoveToFirst(double key) { return map.getAndMoveToLast(key); }
		@Override
		public short getAndMoveToLast(double key) { return map.getAndMoveToFirst(key); }
		@Override
		public double firstDoubleKey() { return map.lastDoubleKey(); }
		@Override
		public double pollFirstDoubleKey() { return map.pollLastDoubleKey(); }
		@Override
		public double lastDoubleKey() { return map.firstDoubleKey(); }
		@Override
		public double pollLastDoubleKey() { return map.pollFirstDoubleKey(); }
		@Override
		public short firstShortValue() { return map.lastShortValue(); }
		@Override
		public short lastShortValue() { return map.firstShortValue(); }
		@Override
		public Double2ShortMap.Entry firstEntry() { return map.lastEntry(); }
		@Override
		public Double2ShortMap.Entry lastEntry() { return map.firstEntry(); }
		@Override
		public Double2ShortMap.Entry pollFirstEntry() { return map.pollLastEntry(); }
		@Override
		public Double2ShortMap.Entry pollLastEntry() { return map.pollFirstEntry(); }
		@Override
		public ObjectOrderedSet<Double2ShortMap.Entry> double2ShortEntrySet() { return new AbstractObjectSet.ReversedObjectOrderedSet<>(map.double2ShortEntrySet()); }
		@Override
		public DoubleOrderedSet keySet() { return new AbstractDoubleSet.ReversedDoubleOrderedSet(map.keySet()); }
		@Override
		public ShortOrderedCollection values() { return map.values().reversed(); }
		@Override
		public Double2ShortOrderedMap reversed() { return map; }
	}

	
	/**
	 * A Simple Type Specific Entry class to reduce boxing/unboxing
	 */
	public static class BasicEntry implements Double2ShortMap.Entry {
		protected double key;
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
		public BasicEntry(Double key, Short value) {
			this.key = key.doubleValue();
			this.value = value.shortValue();
		}
		
		/**
		 * A Type Specific Constructor
		 * @param key the key of a entry
		 * @param value the value of a entry 
		 */
		public BasicEntry(double key, short value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * A Helper method for fast replacing values
		 * @param key the key that should be replaced
		 * @param value the value that should be replaced
		 */
		public void set(double key, short value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public double getDoubleKey() {
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
				if(obj instanceof Double2ShortMap.Entry) {
					Double2ShortMap.Entry entry = (Double2ShortMap.Entry)obj;
					return Double.doubleToLongBits(key) == Double.doubleToLongBits(entry.getDoubleKey()) && value == entry.getShortValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Double && value instanceof Short && Double.doubleToLongBits(this.key) == Double.doubleToLongBits(((Double)key).doubleValue()) && this.value == ((Short)value).shortValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Double.hashCode(key) ^ Short.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Double.toString(key) + "=" + Short.toString(value);
		}
	}
}