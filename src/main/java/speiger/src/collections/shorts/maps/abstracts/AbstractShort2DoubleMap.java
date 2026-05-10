package speiger.src.collections.shorts.maps.abstracts;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

import speiger.src.collections.shorts.collections.ShortIterator;
import speiger.src.collections.shorts.functions.consumer.ShortDoubleConsumer;
import speiger.src.collections.shorts.functions.function.Short2DoubleFunction;
import speiger.src.collections.shorts.functions.function.ShortDoubleUnaryOperator;
import speiger.src.collections.shorts.maps.interfaces.Short2DoubleMap;
import speiger.src.collections.shorts.maps.interfaces.Short2DoubleOrderedMap;
import speiger.src.collections.shorts.sets.ShortOrderedSet;
import speiger.src.collections.doubles.collections.DoubleOrderedCollection;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
import speiger.src.collections.shorts.sets.AbstractShortSet;
import speiger.src.collections.shorts.sets.ShortSet;
import speiger.src.collections.shorts.utils.maps.Short2DoubleMaps;
import speiger.src.collections.doubles.collections.AbstractDoubleCollection;
import speiger.src.collections.doubles.collections.DoubleCollection;
import speiger.src.collections.doubles.collections.DoubleIterator;
import speiger.src.collections.doubles.functions.function.DoubleDoubleUnaryOperator;
import speiger.src.collections.doubles.functions.DoubleSupplier;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Base Implementation of a Type Specific Map to reduce boxing/unboxing
 */
public abstract class AbstractShort2DoubleMap extends AbstractMap<Short, Double> implements Short2DoubleMap
{
	protected double defaultReturnValue = -1D;
	
	@Override
	public double getDefaultReturnValue() {
		return defaultReturnValue;
	}
	
	@Override
	public AbstractShort2DoubleMap setDefaultReturnValue(double v) {
		defaultReturnValue = v;
		return this;
	}
	
	protected ObjectIterable<Short2DoubleMap.Entry> getFastIterable(Short2DoubleMap map) {
		return Short2DoubleMaps.fastIterable(map);
	}
	
	protected ObjectIterator<Short2DoubleMap.Entry> getFastIterator(Short2DoubleMap map) {
		return Short2DoubleMaps.fastIterator(map);
	}
	
	@Override
	public Short2DoubleMap copy() { 
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public Double put(Short key, Double value) {
		return Double.valueOf(put(key.shortValue(), value.doubleValue()));
	}
	
	@Override
	public void addToAll(Short2DoubleMap m) {
		for(Short2DoubleMap.Entry entry : getFastIterable(m))
			addTo(entry.getShortKey(), entry.getDoubleValue());
	}
	
	@Override
	public void putAll(Short2DoubleMap m) {
		for(ObjectIterator<Short2DoubleMap.Entry> iter = getFastIterator(m);iter.hasNext();) {
			Short2DoubleMap.Entry entry = iter.next();
			put(entry.getShortKey(), entry.getDoubleValue());
		}
	}
	
	@Override
	public void putAll(Map<? extends Short, ? extends Double> m)
	{
		if(m instanceof Short2DoubleMap) putAll((Short2DoubleMap)m);
		else super.putAll(m);
	}
	
	@Override
	public void putAll(short[] keys, double[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);
	}
	
	@Override
	public void putAll(Short[] keys, Double[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i].shortValue(), values[i].doubleValue());		
	}
	
	@Override
	public void putAllIfAbsent(Short2DoubleMap m) {
		for(Short2DoubleMap.Entry entry : getFastIterable(m))
			putIfAbsent(entry.getShortKey(), entry.getDoubleValue());
	}
	
	
	@Override
	public boolean containsKey(short key) {
		for(ShortIterator iter = keySet().iterator();iter.hasNext();)
			if(iter.nextShort() == key) return true;
		return false;
	}
	
	@Override
	public boolean containsValue(double value) {
		for(DoubleIterator iter = values().iterator();iter.hasNext();)
			if(Double.doubleToLongBits(iter.nextDouble()) == Double.doubleToLongBits(value)) return true;
		return false;
	}
	
	@Override
	public boolean replace(short key, double oldValue, double newValue) {
		double curValue = get(key);
		if (Double.doubleToLongBits(curValue) != Double.doubleToLongBits(oldValue) || (Double.doubleToLongBits(curValue) == Double.doubleToLongBits(getDefaultReturnValue()) && !containsKey(key))) {
			return false;
		}
		put(key, newValue);
		return true;
	}

	@Override
	public double replace(short key, double value) {
		double curValue;
		if (Double.doubleToLongBits((curValue = get(key))) != Double.doubleToLongBits(getDefaultReturnValue()) || containsKey(key)) {
			curValue = put(key, value);
		}
		return curValue;
	}
	
	@Override
	public void replaceDoubles(Short2DoubleMap m) {
		for(Short2DoubleMap.Entry entry : getFastIterable(m))
			replace(entry.getShortKey(), entry.getDoubleValue());
	}
	
	@Override
	public void replaceDoubles(ShortDoubleUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(ObjectIterator<Short2DoubleMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Short2DoubleMap.Entry entry = iter.next();
			entry.setValue(mappingFunction.applyAsDouble(entry.getShortKey(), entry.getDoubleValue()));
		}
	}

	@Override
	public double computeDouble(short key, ShortDoubleUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		double newValue = mappingFunction.applyAsDouble(key, get(key));
		put(key, newValue);
		return newValue;
	}
	
	@Override
	public double computeDoubleIfAbsent(short key, Short2DoubleFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(!containsKey(key)) {
			double newValue = mappingFunction.applyAsDouble(key);
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public double supplyDoubleIfAbsent(short key, DoubleSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		if(!containsKey(key)) {
			double newValue = valueProvider.getAsDouble();
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public double computeDoubleIfPresent(short key, ShortDoubleUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(containsKey(key)) {
			double newValue = mappingFunction.applyAsDouble(key, get(key));
			put(key, newValue);
			return newValue;
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public double computeDoubleNonDefault(short key, ShortDoubleUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		double value = get(key);
		double newValue = mappingFunction.applyAsDouble(key, value);
		if(Double.doubleToLongBits(newValue) == Double.doubleToLongBits(getDefaultReturnValue())) {
			if(Double.doubleToLongBits(value) != Double.doubleToLongBits(getDefaultReturnValue()) || containsKey(key)) {
				remove(key);
				return getDefaultReturnValue();
			}
			return getDefaultReturnValue();
		}
		put(key, newValue);
		return newValue;
	}
	
	@Override
	public double computeDoubleIfAbsentNonDefault(short key, Short2DoubleFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		double value;
		if((value = get(key)) == getDefaultReturnValue() || !containsKey(key)) {
			double newValue = mappingFunction.applyAsDouble(key);
			if(Double.doubleToLongBits(newValue) != Double.doubleToLongBits(getDefaultReturnValue())) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public double supplyDoubleIfAbsentNonDefault(short key, DoubleSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		double value;
		if((value = get(key)) == getDefaultReturnValue() || !containsKey(key)) {
			double newValue = valueProvider.getAsDouble();
			if(Double.doubleToLongBits(newValue) != Double.doubleToLongBits(getDefaultReturnValue())) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public double computeDoubleIfPresentNonDefault(short key, ShortDoubleUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		double value;
		if(Double.doubleToLongBits((value = get(key))) != Double.doubleToLongBits(getDefaultReturnValue()) || containsKey(key)) {
			double newValue = mappingFunction.applyAsDouble(key, value);
			if(Double.doubleToLongBits(newValue) != Double.doubleToLongBits(getDefaultReturnValue())) {
				put(key, newValue);
				return newValue;
			}
			remove(key);
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public double mergeDouble(short key, double value, DoubleDoubleUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		double oldValue = get(key);
		double newValue = Double.doubleToLongBits(oldValue) == Double.doubleToLongBits(getDefaultReturnValue()) ? value : mappingFunction.applyAsDouble(oldValue, value);
		if(Double.doubleToLongBits(newValue) == Double.doubleToLongBits(getDefaultReturnValue())) remove(key);
		else put(key, newValue);
		return newValue;
	}
	
	@Override
	public void mergeAllDouble(Short2DoubleMap m, DoubleDoubleUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Short2DoubleMap.Entry entry : getFastIterable(m)) {
			short key = entry.getShortKey();
			double oldValue = get(key);
			double newValue = Double.doubleToLongBits(oldValue) == Double.doubleToLongBits(getDefaultReturnValue()) ? entry.getDoubleValue() : mappingFunction.applyAsDouble(oldValue, entry.getDoubleValue());
			if(Double.doubleToLongBits(newValue) == Double.doubleToLongBits(getDefaultReturnValue())) remove(key);
			else put(key, newValue);
		}
	}
	
	@Override
	public Double get(Object key) {
		return Double.valueOf(key instanceof Short ? get(((Short)key).shortValue()) : getDefaultReturnValue());
	}
	
	@Override
	public Double getOrDefault(Object key, Double defaultValue) {
		return Double.valueOf(key instanceof Short ? getOrDefault(((Short)key).shortValue(), defaultValue.doubleValue()) : getDefaultReturnValue());
	}
	
	@Override
	public double getOrDefault(short key, double defaultValue) {
		double value = get(key);
		return Double.doubleToLongBits(value) != Double.doubleToLongBits(getDefaultReturnValue()) || containsKey(key) ? value : defaultValue;
	}
	
	
	@Override
	public Double remove(Object key) {
		return key instanceof Short ? Double.valueOf(remove(((Short)key).shortValue())) : Double.valueOf(getDefaultReturnValue());
	}
	
	@Override
	public void forEach(ShortDoubleConsumer action) {
		Objects.requireNonNull(action);
		for(ObjectIterator<Short2DoubleMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Short2DoubleMap.Entry entry = iter.next();
			action.accept(entry.getShortKey(), entry.getDoubleValue());
		}
	}

	@Override
	public ShortSet keySet() {
		return new AbstractShortSet() {
			@Override
			public boolean remove(short o) {
				return Double.doubleToLongBits(AbstractShort2DoubleMap.this.remove(o)) != Double.doubleToLongBits(getDefaultReturnValue());
			}
			
			@Override
			public boolean add(short o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public ShortIterator iterator() {
				return new ShortIterator() {
					ObjectIterator<Short2DoubleMap.Entry> iter = getFastIterator(AbstractShort2DoubleMap.this);
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
				return AbstractShort2DoubleMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractShort2DoubleMap.this.clear();
			}
		};
	}

	@Override
	public DoubleCollection values() {
		return new AbstractDoubleCollection() {
			@Override
			public boolean add(double o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public int size() {
				return AbstractShort2DoubleMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractShort2DoubleMap.this.clear();
			}
			
			@Override
			public DoubleIterator iterator() {
				return new DoubleIterator() {
					ObjectIterator<Short2DoubleMap.Entry> iter = getFastIterator(AbstractShort2DoubleMap.this);
					@Override
					public boolean hasNext() {
						return iter.hasNext();
					}
					
					@Override
					public double nextDouble() {
						return iter.next().getDoubleValue();
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
	public ObjectSet<Map.Entry<Short, Double>> entrySet() {
		return (ObjectSet)short2DoubleEntrySet();
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o instanceof Map) {
			if(size() != ((Map<?, ?>)o).size()) return false;
			if(o instanceof Short2DoubleMap) return short2DoubleEntrySet().containsAll(((Short2DoubleMap)o).short2DoubleEntrySet());
			return short2DoubleEntrySet().containsAll(((Map<?, ?>)o).entrySet());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		ObjectIterator<Short2DoubleMap.Entry> iter = getFastIterator(this);
		while(iter.hasNext()) hash += iter.next().hashCode();
		return hash;
	}
	
	public static class ReversedShort2DoubleOrderedMap extends AbstractShort2DoubleMap implements Short2DoubleOrderedMap {
		Short2DoubleOrderedMap map;
		
		public ReversedShort2DoubleOrderedMap(Short2DoubleOrderedMap map) {
			this.map = map;
		}
		@Override
		public AbstractShort2DoubleMap setDefaultReturnValue(double v) {
			map.setDefaultReturnValue(v);
			return this;
		}
		@Override
		public double getDefaultReturnValue() { return map.getDefaultReturnValue(); }
		@Override
		public Short2DoubleOrderedMap copy() { throw new UnsupportedOperationException(); }
		@Override
		public double put(short key, double value) { return map.put(key, value); }
		@Override
		public double putIfAbsent(short key, double value) { return map.putIfAbsent(key, value); }
		@Override
		public double addTo(short key, double value) { return map.addTo(key, value); }
		@Override
		public double subFrom(short key, double value) { return map.subFrom(key, value); }
		@Override
		public double remove(short key) { return map.remove(key); }
		@Override
		public boolean remove(short key, double value) { return map.remove(key, value); }
		@Override
		public double removeOrDefault(short key, double defaultValue) { return map.removeOrDefault(key, defaultValue); }
		@Override
		public boolean containsKey(short key) { return map.containsKey(key); }
		@Override
		public boolean containsValue(double value) { return map.containsValue(value); }
		@Override
		public boolean replace(short key, double oldValue, double newValue) { return map.replace(key, oldValue, newValue); }
		@Override
		public double replace(short key, double value) { return map.replace(key, value); }
		@Override
		public void replaceDoubles(Short2DoubleMap m) { map.replaceDoubles(m); }
		@Override
		public void replaceDoubles(ShortDoubleUnaryOperator mappingFunction) { map.replaceDoubles(mappingFunction); }
		@Override
		public double computeDouble(short key, ShortDoubleUnaryOperator mappingFunction) { return map.computeDouble(key, mappingFunction); }
		@Override
		public double computeDoubleIfAbsent(short key, Short2DoubleFunction mappingFunction) { return map.computeDoubleIfAbsent(key, mappingFunction); }
		@Override
		public double supplyDoubleIfAbsent(short key, DoubleSupplier valueProvider) { return map.supplyDoubleIfAbsent(key, valueProvider); }
		@Override
		public double computeDoubleIfPresent(short key, ShortDoubleUnaryOperator mappingFunction) { return map.computeDoubleIfPresent(key, mappingFunction); }
		@Override
		public double computeDoubleNonDefault(short key, ShortDoubleUnaryOperator mappingFunction) { return map.computeDoubleNonDefault(key, mappingFunction); }
		@Override
		public double computeDoubleIfAbsentNonDefault(short key, Short2DoubleFunction mappingFunction) { return map.computeDoubleIfAbsentNonDefault(key, mappingFunction); }
		@Override
		public double supplyDoubleIfAbsentNonDefault(short key, DoubleSupplier valueProvider) { return map.supplyDoubleIfAbsentNonDefault(key, valueProvider); }
		@Override
		public double computeDoubleIfPresentNonDefault(short key, ShortDoubleUnaryOperator mappingFunction) { return map.computeDoubleIfPresentNonDefault(key, mappingFunction); }
		@Override
		public double mergeDouble(short key, double value, DoubleDoubleUnaryOperator mappingFunction) { return map.mergeDouble(key, value, mappingFunction); }
		@Override
		public double getOrDefault(short key, double defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public double get(short key) { return map.get(key); }
		@Override
		public double putAndMoveToFirst(short key, double value) { return map.putAndMoveToLast(key, value); }
		@Override
		public double putAndMoveToLast(short key, double value) { return map.putAndMoveToFirst(key, value); }
		@Override
		public double putFirst(short key, double value) { return map.putLast(key, value); }
		@Override
		public double putLast(short key, double value) { return map.putFirst(key, value); }
		@Override
		public boolean moveToFirst(short key) { return map.moveToLast(key); }
		@Override
		public boolean moveToLast(short key) { return map.moveToFirst(key); }
		@Override
		public double getAndMoveToFirst(short key) { return map.getAndMoveToLast(key); }
		@Override
		public double getAndMoveToLast(short key) { return map.getAndMoveToFirst(key); }
		@Override
		public short firstShortKey() { return map.lastShortKey(); }
		@Override
		public short pollFirstShortKey() { return map.pollLastShortKey(); }
		@Override
		public short lastShortKey() { return map.firstShortKey(); }
		@Override
		public short pollLastShortKey() { return map.pollFirstShortKey(); }
		@Override
		public double firstDoubleValue() { return map.lastDoubleValue(); }
		@Override
		public double lastDoubleValue() { return map.firstDoubleValue(); }
		@Override
		public Short2DoubleMap.Entry firstEntry() { return map.lastEntry(); }
		@Override
		public Short2DoubleMap.Entry lastEntry() { return map.firstEntry(); }
		@Override
		public Short2DoubleMap.Entry pollFirstEntry() { return map.pollLastEntry(); }
		@Override
		public Short2DoubleMap.Entry pollLastEntry() { return map.pollFirstEntry(); }
		@Override
		public ObjectOrderedSet<Short2DoubleMap.Entry> short2DoubleEntrySet() { return new AbstractObjectSet.ReversedObjectOrderedSet<>(map.short2DoubleEntrySet()); }
		@Override
		public ShortOrderedSet keySet() { return new AbstractShortSet.ReversedShortOrderedSet(map.keySet()); }
		@Override
		public DoubleOrderedCollection values() { return map.values().reversed(); }
		@Override
		public Short2DoubleOrderedMap reversed() { return map; }
	}

	
	/**
	 * A Simple Type Specific Entry class to reduce boxing/unboxing
	 */
	public static class BasicEntry implements Short2DoubleMap.Entry {
		protected short key;
		protected double value;
		
		/**
		 * A basic Empty constructor
		 */
		public BasicEntry() {}
		/**
		 * A Boxed Constructor for supporting java variants
		 * @param key the key of a entry
		 * @param value the value of a entry
		 */
		public BasicEntry(Short key, Double value) {
			this.key = key.shortValue();
			this.value = value.doubleValue();
		}
		
		/**
		 * A Type Specific Constructor
		 * @param key the key of a entry
		 * @param value the value of a entry 
		 */
		public BasicEntry(short key, double value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * A Helper method for fast replacing values
		 * @param key the key that should be replaced
		 * @param value the value that should be replaced
		 */
		public void set(short key, double value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public short getShortKey() {
			return key;
		}

		@Override
		public double getDoubleValue() {
			return value;
		}

		@Override
		public double setValue(double value) {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Map.Entry) {
				if(obj instanceof Short2DoubleMap.Entry) {
					Short2DoubleMap.Entry entry = (Short2DoubleMap.Entry)obj;
					return key == entry.getShortKey() && Double.doubleToLongBits(value) == Double.doubleToLongBits(entry.getDoubleValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Short && value instanceof Double && this.key == ((Short)key).shortValue() && Double.doubleToLongBits(this.value) == Double.doubleToLongBits(((Double)value).doubleValue());
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Short.hashCode(key) ^ Double.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Short.toString(key) + "=" + Double.toString(value);
		}
	}
}