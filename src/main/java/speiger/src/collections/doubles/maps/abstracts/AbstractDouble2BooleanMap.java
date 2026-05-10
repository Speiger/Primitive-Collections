package speiger.src.collections.doubles.maps.abstracts;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.DoublePredicate;

import speiger.src.collections.doubles.collections.DoubleIterator;
import speiger.src.collections.doubles.functions.consumer.DoubleBooleanConsumer;
import speiger.src.collections.doubles.functions.function.DoubleBooleanUnaryOperator;
import speiger.src.collections.doubles.maps.interfaces.Double2BooleanMap;
import speiger.src.collections.doubles.maps.interfaces.Double2BooleanOrderedMap;
import speiger.src.collections.doubles.sets.DoubleOrderedSet;
import speiger.src.collections.booleans.collections.BooleanOrderedCollection;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
import speiger.src.collections.doubles.sets.AbstractDoubleSet;
import speiger.src.collections.doubles.sets.DoubleSet;
import speiger.src.collections.doubles.utils.maps.Double2BooleanMaps;
import speiger.src.collections.booleans.collections.AbstractBooleanCollection;
import speiger.src.collections.booleans.collections.BooleanCollection;
import speiger.src.collections.booleans.collections.BooleanIterator;
import speiger.src.collections.booleans.functions.function.BooleanBooleanUnaryOperator;
import speiger.src.collections.booleans.functions.BooleanSupplier;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Base Implementation of a Type Specific Map to reduce boxing/unboxing
 */
public abstract class AbstractDouble2BooleanMap extends AbstractMap<Double, Boolean> implements Double2BooleanMap
{
	protected boolean defaultReturnValue = false;
	
	@Override
	public boolean getDefaultReturnValue() {
		return defaultReturnValue;
	}
	
	@Override
	public AbstractDouble2BooleanMap setDefaultReturnValue(boolean v) {
		defaultReturnValue = v;
		return this;
	}
	
	protected ObjectIterable<Double2BooleanMap.Entry> getFastIterable(Double2BooleanMap map) {
		return Double2BooleanMaps.fastIterable(map);
	}
	
	protected ObjectIterator<Double2BooleanMap.Entry> getFastIterator(Double2BooleanMap map) {
		return Double2BooleanMaps.fastIterator(map);
	}
	
	@Override
	public Double2BooleanMap copy() { 
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public Boolean put(Double key, Boolean value) {
		return Boolean.valueOf(put(key.doubleValue(), value.booleanValue()));
	}
	
	@Override
	public void putAll(Double2BooleanMap m) {
		for(ObjectIterator<Double2BooleanMap.Entry> iter = getFastIterator(m);iter.hasNext();) {
			Double2BooleanMap.Entry entry = iter.next();
			put(entry.getDoubleKey(), entry.getBooleanValue());
		}
	}
	
	@Override
	public void putAll(Map<? extends Double, ? extends Boolean> m)
	{
		if(m instanceof Double2BooleanMap) putAll((Double2BooleanMap)m);
		else super.putAll(m);
	}
	
	@Override
	public void putAll(double[] keys, boolean[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);
	}
	
	@Override
	public void putAll(Double[] keys, Boolean[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i].doubleValue(), values[i].booleanValue());		
	}
	
	@Override
	public void putAllIfAbsent(Double2BooleanMap m) {
		for(Double2BooleanMap.Entry entry : getFastIterable(m))
			putIfAbsent(entry.getDoubleKey(), entry.getBooleanValue());
	}
	
	
	@Override
	public boolean containsKey(double key) {
		for(DoubleIterator iter = keySet().iterator();iter.hasNext();)
			if(Double.doubleToLongBits(iter.nextDouble()) == Double.doubleToLongBits(key)) return true;
		return false;
	}
	
	@Override
	public boolean containsValue(boolean value) {
		for(BooleanIterator iter = values().iterator();iter.hasNext();)
			if(iter.nextBoolean() == value) return true;
		return false;
	}
	
	@Override
	public boolean replace(double key, boolean oldValue, boolean newValue) {
		boolean curValue = get(key);
		if (curValue != oldValue || (curValue == getDefaultReturnValue() && !containsKey(key))) {
			return false;
		}
		put(key, newValue);
		return true;
	}

	@Override
	public boolean replace(double key, boolean value) {
		boolean curValue;
		if ((curValue = get(key)) != getDefaultReturnValue() || containsKey(key)) {
			curValue = put(key, value);
		}
		return curValue;
	}
	
	@Override
	public void replaceBooleans(Double2BooleanMap m) {
		for(Double2BooleanMap.Entry entry : getFastIterable(m))
			replace(entry.getDoubleKey(), entry.getBooleanValue());
	}
	
	@Override
	public void replaceBooleans(DoubleBooleanUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(ObjectIterator<Double2BooleanMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Double2BooleanMap.Entry entry = iter.next();
			entry.setValue(mappingFunction.applyAsBoolean(entry.getDoubleKey(), entry.getBooleanValue()));
		}
	}

	@Override
	public boolean computeBoolean(double key, DoubleBooleanUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		boolean newValue = mappingFunction.applyAsBoolean(key, get(key));
		put(key, newValue);
		return newValue;
	}
	
	@Override
	public boolean computeBooleanIfAbsent(double key, DoublePredicate mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(!containsKey(key)) {
			boolean newValue = mappingFunction.test(key);
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public boolean supplyBooleanIfAbsent(double key, BooleanSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		if(!containsKey(key)) {
			boolean newValue = valueProvider.getAsBoolean();
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public boolean computeBooleanIfPresent(double key, DoubleBooleanUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(containsKey(key)) {
			boolean newValue = mappingFunction.applyAsBoolean(key, get(key));
			put(key, newValue);
			return newValue;
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public boolean computeBooleanNonDefault(double key, DoubleBooleanUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		boolean value = get(key);
		boolean newValue = mappingFunction.applyAsBoolean(key, value);
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
	public boolean computeBooleanIfAbsentNonDefault(double key, DoublePredicate mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		boolean value;
		if((value = get(key)) == getDefaultReturnValue() || !containsKey(key)) {
			boolean newValue = mappingFunction.test(key);
			if(newValue != getDefaultReturnValue()) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public boolean supplyBooleanIfAbsentNonDefault(double key, BooleanSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		boolean value;
		if((value = get(key)) == getDefaultReturnValue() || !containsKey(key)) {
			boolean newValue = valueProvider.getAsBoolean();
			if(newValue != getDefaultReturnValue()) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public boolean computeBooleanIfPresentNonDefault(double key, DoubleBooleanUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		boolean value;
		if((value = get(key)) != getDefaultReturnValue() || containsKey(key)) {
			boolean newValue = mappingFunction.applyAsBoolean(key, value);
			if(newValue != getDefaultReturnValue()) {
				put(key, newValue);
				return newValue;
			}
			remove(key);
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public boolean mergeBoolean(double key, boolean value, BooleanBooleanUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		boolean oldValue = get(key);
		boolean newValue = oldValue == getDefaultReturnValue() ? value : mappingFunction.applyAsBoolean(oldValue, value);
		if(newValue == getDefaultReturnValue()) remove(key);
		else put(key, newValue);
		return newValue;
	}
	
	@Override
	public void mergeAllBoolean(Double2BooleanMap m, BooleanBooleanUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Double2BooleanMap.Entry entry : getFastIterable(m)) {
			double key = entry.getDoubleKey();
			boolean oldValue = get(key);
			boolean newValue = oldValue == getDefaultReturnValue() ? entry.getBooleanValue() : mappingFunction.applyAsBoolean(oldValue, entry.getBooleanValue());
			if(newValue == getDefaultReturnValue()) remove(key);
			else put(key, newValue);
		}
	}
	
	@Override
	public Boolean get(Object key) {
		return Boolean.valueOf(key instanceof Double ? get(((Double)key).doubleValue()) : getDefaultReturnValue());
	}
	
	@Override
	public Boolean getOrDefault(Object key, Boolean defaultValue) {
		return Boolean.valueOf(key instanceof Double ? getOrDefault(((Double)key).doubleValue(), defaultValue.booleanValue()) : getDefaultReturnValue());
	}
	
	@Override
	public boolean getOrDefault(double key, boolean defaultValue) {
		boolean value = get(key);
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	
	@Override
	public Boolean remove(Object key) {
		return key instanceof Double ? Boolean.valueOf(remove(((Double)key).doubleValue())) : Boolean.valueOf(getDefaultReturnValue());
	}
	
	@Override
	public void forEach(DoubleBooleanConsumer action) {
		Objects.requireNonNull(action);
		for(ObjectIterator<Double2BooleanMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Double2BooleanMap.Entry entry = iter.next();
			action.accept(entry.getDoubleKey(), entry.getBooleanValue());
		}
	}

	@Override
	public DoubleSet keySet() {
		return new AbstractDoubleSet() {
			@Override
			public boolean remove(double o) {
				return AbstractDouble2BooleanMap.this.remove(o) != getDefaultReturnValue();
			}
			
			@Override
			public boolean add(double o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public DoubleIterator iterator() {
				return new DoubleIterator() {
					ObjectIterator<Double2BooleanMap.Entry> iter = getFastIterator(AbstractDouble2BooleanMap.this);
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
				return AbstractDouble2BooleanMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractDouble2BooleanMap.this.clear();
			}
		};
	}

	@Override
	public BooleanCollection values() {
		return new AbstractBooleanCollection() {
			@Override
			public boolean add(boolean o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public int size() {
				return AbstractDouble2BooleanMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractDouble2BooleanMap.this.clear();
			}
			
			@Override
			public BooleanIterator iterator() {
				return new BooleanIterator() {
					ObjectIterator<Double2BooleanMap.Entry> iter = getFastIterator(AbstractDouble2BooleanMap.this);
					@Override
					public boolean hasNext() {
						return iter.hasNext();
					}
					
					@Override
					public boolean nextBoolean() {
						return iter.next().getBooleanValue();
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
	public ObjectSet<Map.Entry<Double, Boolean>> entrySet() {
		return (ObjectSet)double2BooleanEntrySet();
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o instanceof Map) {
			if(size() != ((Map<?, ?>)o).size()) return false;
			if(o instanceof Double2BooleanMap) return double2BooleanEntrySet().containsAll(((Double2BooleanMap)o).double2BooleanEntrySet());
			return double2BooleanEntrySet().containsAll(((Map<?, ?>)o).entrySet());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		ObjectIterator<Double2BooleanMap.Entry> iter = getFastIterator(this);
		while(iter.hasNext()) hash += iter.next().hashCode();
		return hash;
	}
	
	public static class ReversedDouble2BooleanOrderedMap extends AbstractDouble2BooleanMap implements Double2BooleanOrderedMap {
		Double2BooleanOrderedMap map;
		
		public ReversedDouble2BooleanOrderedMap(Double2BooleanOrderedMap map) {
			this.map = map;
		}
		@Override
		public AbstractDouble2BooleanMap setDefaultReturnValue(boolean v) {
			map.setDefaultReturnValue(v);
			return this;
		}
		@Override
		public boolean getDefaultReturnValue() { return map.getDefaultReturnValue(); }
		@Override
		public Double2BooleanOrderedMap copy() { throw new UnsupportedOperationException(); }
		@Override
		public boolean put(double key, boolean value) { return map.put(key, value); }
		@Override
		public boolean putIfAbsent(double key, boolean value) { return map.putIfAbsent(key, value); }
		@Override
		public boolean remove(double key) { return map.remove(key); }
		@Override
		public boolean remove(double key, boolean value) { return map.remove(key, value); }
		@Override
		public boolean removeOrDefault(double key, boolean defaultValue) { return map.removeOrDefault(key, defaultValue); }
		@Override
		public boolean containsKey(double key) { return map.containsKey(key); }
		@Override
		public boolean containsValue(boolean value) { return map.containsValue(value); }
		@Override
		public boolean replace(double key, boolean oldValue, boolean newValue) { return map.replace(key, oldValue, newValue); }
		@Override
		public boolean replace(double key, boolean value) { return map.replace(key, value); }
		@Override
		public void replaceBooleans(Double2BooleanMap m) { map.replaceBooleans(m); }
		@Override
		public void replaceBooleans(DoubleBooleanUnaryOperator mappingFunction) { map.replaceBooleans(mappingFunction); }
		@Override
		public boolean computeBoolean(double key, DoubleBooleanUnaryOperator mappingFunction) { return map.computeBoolean(key, mappingFunction); }
		@Override
		public boolean computeBooleanIfAbsent(double key, DoublePredicate mappingFunction) { return map.computeBooleanIfAbsent(key, mappingFunction); }
		@Override
		public boolean supplyBooleanIfAbsent(double key, BooleanSupplier valueProvider) { return map.supplyBooleanIfAbsent(key, valueProvider); }
		@Override
		public boolean computeBooleanIfPresent(double key, DoubleBooleanUnaryOperator mappingFunction) { return map.computeBooleanIfPresent(key, mappingFunction); }
		@Override
		public boolean computeBooleanNonDefault(double key, DoubleBooleanUnaryOperator mappingFunction) { return map.computeBooleanNonDefault(key, mappingFunction); }
		@Override
		public boolean computeBooleanIfAbsentNonDefault(double key, DoublePredicate mappingFunction) { return map.computeBooleanIfAbsentNonDefault(key, mappingFunction); }
		@Override
		public boolean supplyBooleanIfAbsentNonDefault(double key, BooleanSupplier valueProvider) { return map.supplyBooleanIfAbsentNonDefault(key, valueProvider); }
		@Override
		public boolean computeBooleanIfPresentNonDefault(double key, DoubleBooleanUnaryOperator mappingFunction) { return map.computeBooleanIfPresentNonDefault(key, mappingFunction); }
		@Override
		public boolean mergeBoolean(double key, boolean value, BooleanBooleanUnaryOperator mappingFunction) { return map.mergeBoolean(key, value, mappingFunction); }
		@Override
		public boolean getOrDefault(double key, boolean defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public boolean get(double key) { return map.get(key); }
		@Override
		public boolean putAndMoveToFirst(double key, boolean value) { return map.putAndMoveToLast(key, value); }
		@Override
		public boolean putAndMoveToLast(double key, boolean value) { return map.putAndMoveToFirst(key, value); }
		@Override
		public boolean putFirst(double key, boolean value) { return map.putLast(key, value); }
		@Override
		public boolean putLast(double key, boolean value) { return map.putFirst(key, value); }
		@Override
		public boolean moveToFirst(double key) { return map.moveToLast(key); }
		@Override
		public boolean moveToLast(double key) { return map.moveToFirst(key); }
		@Override
		public boolean getAndMoveToFirst(double key) { return map.getAndMoveToLast(key); }
		@Override
		public boolean getAndMoveToLast(double key) { return map.getAndMoveToFirst(key); }
		@Override
		public double firstDoubleKey() { return map.lastDoubleKey(); }
		@Override
		public double pollFirstDoubleKey() { return map.pollLastDoubleKey(); }
		@Override
		public double lastDoubleKey() { return map.firstDoubleKey(); }
		@Override
		public double pollLastDoubleKey() { return map.pollFirstDoubleKey(); }
		@Override
		public boolean firstBooleanValue() { return map.lastBooleanValue(); }
		@Override
		public boolean lastBooleanValue() { return map.firstBooleanValue(); }
		@Override
		public Double2BooleanMap.Entry firstEntry() { return map.lastEntry(); }
		@Override
		public Double2BooleanMap.Entry lastEntry() { return map.firstEntry(); }
		@Override
		public Double2BooleanMap.Entry pollFirstEntry() { return map.pollLastEntry(); }
		@Override
		public Double2BooleanMap.Entry pollLastEntry() { return map.pollFirstEntry(); }
		@Override
		public ObjectOrderedSet<Double2BooleanMap.Entry> double2BooleanEntrySet() { return new AbstractObjectSet.ReversedObjectOrderedSet<>(map.double2BooleanEntrySet()); }
		@Override
		public DoubleOrderedSet keySet() { return new AbstractDoubleSet.ReversedDoubleOrderedSet(map.keySet()); }
		@Override
		public BooleanOrderedCollection values() { return map.values().reversed(); }
		@Override
		public Double2BooleanOrderedMap reversed() { return map; }
	}

	
	/**
	 * A Simple Type Specific Entry class to reduce boxing/unboxing
	 */
	public static class BasicEntry implements Double2BooleanMap.Entry {
		protected double key;
		protected boolean value;
		
		/**
		 * A basic Empty constructor
		 */
		public BasicEntry() {}
		/**
		 * A Boxed Constructor for supporting java variants
		 * @param key the key of a entry
		 * @param value the value of a entry
		 */
		public BasicEntry(Double key, Boolean value) {
			this.key = key.doubleValue();
			this.value = value.booleanValue();
		}
		
		/**
		 * A Type Specific Constructor
		 * @param key the key of a entry
		 * @param value the value of a entry 
		 */
		public BasicEntry(double key, boolean value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * A Helper method for fast replacing values
		 * @param key the key that should be replaced
		 * @param value the value that should be replaced
		 */
		public void set(double key, boolean value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public double getDoubleKey() {
			return key;
		}

		@Override
		public boolean getBooleanValue() {
			return value;
		}

		@Override
		public boolean setValue(boolean value) {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Map.Entry) {
				if(obj instanceof Double2BooleanMap.Entry) {
					Double2BooleanMap.Entry entry = (Double2BooleanMap.Entry)obj;
					return Double.doubleToLongBits(key) == Double.doubleToLongBits(entry.getDoubleKey()) && value == entry.getBooleanValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Double && value instanceof Boolean && Double.doubleToLongBits(this.key) == Double.doubleToLongBits(((Double)key).doubleValue()) && this.value == ((Boolean)value).booleanValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Double.hashCode(key) ^ Boolean.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Double.toString(key) + "=" + Boolean.toString(value);
		}
	}
}