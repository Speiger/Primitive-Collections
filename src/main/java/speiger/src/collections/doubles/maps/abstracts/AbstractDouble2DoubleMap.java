package speiger.src.collections.doubles.maps.abstracts;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

import speiger.src.collections.doubles.collections.DoubleIterator;
import speiger.src.collections.doubles.functions.consumer.DoubleDoubleConsumer;
import speiger.src.collections.doubles.functions.function.DoubleUnaryOperator;
import speiger.src.collections.doubles.functions.function.DoubleDoubleUnaryOperator;
import speiger.src.collections.doubles.maps.interfaces.Double2DoubleMap;
import speiger.src.collections.doubles.maps.interfaces.Double2DoubleOrderedMap;
import speiger.src.collections.doubles.sets.DoubleOrderedSet;
import speiger.src.collections.doubles.collections.DoubleOrderedCollection;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
import speiger.src.collections.doubles.sets.AbstractDoubleSet;
import speiger.src.collections.doubles.sets.DoubleSet;
import speiger.src.collections.doubles.utils.maps.Double2DoubleMaps;
import speiger.src.collections.doubles.collections.AbstractDoubleCollection;
import speiger.src.collections.doubles.collections.DoubleCollection;
import speiger.src.collections.doubles.functions.DoubleSupplier;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Base Implementation of a Type Specific Map to reduce boxing/unboxing
 */
public abstract class AbstractDouble2DoubleMap extends AbstractMap<Double, Double> implements Double2DoubleMap
{
	protected double defaultReturnValue = -1D;
	
	@Override
	public double getDefaultReturnValue() {
		return defaultReturnValue;
	}
	
	@Override
	public AbstractDouble2DoubleMap setDefaultReturnValue(double v) {
		defaultReturnValue = v;
		return this;
	}
	
	protected ObjectIterable<Double2DoubleMap.Entry> getFastIterable(Double2DoubleMap map) {
		return Double2DoubleMaps.fastIterable(map);
	}
	
	protected ObjectIterator<Double2DoubleMap.Entry> getFastIterator(Double2DoubleMap map) {
		return Double2DoubleMaps.fastIterator(map);
	}
	
	@Override
	public Double2DoubleMap copy() { 
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public Double put(Double key, Double value) {
		return Double.valueOf(put(key.doubleValue(), value.doubleValue()));
	}
	
	@Override
	public void addToAll(Double2DoubleMap m) {
		for(Double2DoubleMap.Entry entry : getFastIterable(m))
			addTo(entry.getDoubleKey(), entry.getDoubleValue());
	}
	
	@Override
	public void putAll(Double2DoubleMap m) {
		for(ObjectIterator<Double2DoubleMap.Entry> iter = getFastIterator(m);iter.hasNext();) {
			Double2DoubleMap.Entry entry = iter.next();
			put(entry.getDoubleKey(), entry.getDoubleValue());
		}
	}
	
	@Override
	public void putAll(Map<? extends Double, ? extends Double> m)
	{
		if(m instanceof Double2DoubleMap) putAll((Double2DoubleMap)m);
		else super.putAll(m);
	}
	
	@Override
	public void putAll(double[] keys, double[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);
	}
	
	@Override
	public void putAll(Double[] keys, Double[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i].doubleValue(), values[i].doubleValue());		
	}
	
	@Override
	public void putAllIfAbsent(Double2DoubleMap m) {
		for(Double2DoubleMap.Entry entry : getFastIterable(m))
			putIfAbsent(entry.getDoubleKey(), entry.getDoubleValue());
	}
	
	
	@Override
	public boolean containsKey(double key) {
		for(DoubleIterator iter = keySet().iterator();iter.hasNext();)
			if(Double.doubleToLongBits(iter.nextDouble()) == Double.doubleToLongBits(key)) return true;
		return false;
	}
	
	@Override
	public boolean containsValue(double value) {
		for(DoubleIterator iter = values().iterator();iter.hasNext();)
			if(Double.doubleToLongBits(iter.nextDouble()) == Double.doubleToLongBits(value)) return true;
		return false;
	}
	
	@Override
	public boolean replace(double key, double oldValue, double newValue) {
		double curValue = get(key);
		if (Double.doubleToLongBits(curValue) != Double.doubleToLongBits(oldValue) || (Double.doubleToLongBits(curValue) == Double.doubleToLongBits(getDefaultReturnValue()) && !containsKey(key))) {
			return false;
		}
		put(key, newValue);
		return true;
	}

	@Override
	public double replace(double key, double value) {
		double curValue;
		if (Double.doubleToLongBits((curValue = get(key))) != Double.doubleToLongBits(getDefaultReturnValue()) || containsKey(key)) {
			curValue = put(key, value);
		}
		return curValue;
	}
	
	@Override
	public void replaceDoubles(Double2DoubleMap m) {
		for(Double2DoubleMap.Entry entry : getFastIterable(m))
			replace(entry.getDoubleKey(), entry.getDoubleValue());
	}
	
	@Override
	public void replaceDoubles(DoubleDoubleUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(ObjectIterator<Double2DoubleMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Double2DoubleMap.Entry entry = iter.next();
			entry.setValue(mappingFunction.applyAsDouble(entry.getDoubleKey(), entry.getDoubleValue()));
		}
	}

	@Override
	public double computeDouble(double key, DoubleDoubleUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		double newValue = mappingFunction.applyAsDouble(key, get(key));
		put(key, newValue);
		return newValue;
	}
	
	@Override
	public double computeDoubleIfAbsent(double key, DoubleUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(!containsKey(key)) {
			double newValue = mappingFunction.applyAsDouble(key);
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public double supplyDoubleIfAbsent(double key, DoubleSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		if(!containsKey(key)) {
			double newValue = valueProvider.getAsDouble();
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public double computeDoubleIfPresent(double key, DoubleDoubleUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(containsKey(key)) {
			double newValue = mappingFunction.applyAsDouble(key, get(key));
			put(key, newValue);
			return newValue;
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public double computeDoubleNonDefault(double key, DoubleDoubleUnaryOperator mappingFunction) {
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
	public double computeDoubleIfAbsentNonDefault(double key, DoubleUnaryOperator mappingFunction) {
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
	public double supplyDoubleIfAbsentNonDefault(double key, DoubleSupplier valueProvider) {
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
	public double computeDoubleIfPresentNonDefault(double key, DoubleDoubleUnaryOperator mappingFunction) {
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
	public double mergeDouble(double key, double value, DoubleDoubleUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		double oldValue = get(key);
		double newValue = Double.doubleToLongBits(oldValue) == Double.doubleToLongBits(getDefaultReturnValue()) ? value : mappingFunction.applyAsDouble(oldValue, value);
		if(Double.doubleToLongBits(newValue) == Double.doubleToLongBits(getDefaultReturnValue())) remove(key);
		else put(key, newValue);
		return newValue;
	}
	
	@Override
	public void mergeAllDouble(Double2DoubleMap m, DoubleDoubleUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Double2DoubleMap.Entry entry : getFastIterable(m)) {
			double key = entry.getDoubleKey();
			double oldValue = get(key);
			double newValue = Double.doubleToLongBits(oldValue) == Double.doubleToLongBits(getDefaultReturnValue()) ? entry.getDoubleValue() : mappingFunction.applyAsDouble(oldValue, entry.getDoubleValue());
			if(Double.doubleToLongBits(newValue) == Double.doubleToLongBits(getDefaultReturnValue())) remove(key);
			else put(key, newValue);
		}
	}
	
	@Override
	public Double get(Object key) {
		return Double.valueOf(key instanceof Double ? get(((Double)key).doubleValue()) : getDefaultReturnValue());
	}
	
	@Override
	public Double getOrDefault(Object key, Double defaultValue) {
		return Double.valueOf(key instanceof Double ? getOrDefault(((Double)key).doubleValue(), defaultValue.doubleValue()) : getDefaultReturnValue());
	}
	
	@Override
	public double getOrDefault(double key, double defaultValue) {
		double value = get(key);
		return Double.doubleToLongBits(value) != Double.doubleToLongBits(getDefaultReturnValue()) || containsKey(key) ? value : defaultValue;
	}
	
	
	@Override
	public Double remove(Object key) {
		return key instanceof Double ? Double.valueOf(remove(((Double)key).doubleValue())) : Double.valueOf(getDefaultReturnValue());
	}
	
	@Override
	public void forEach(DoubleDoubleConsumer action) {
		Objects.requireNonNull(action);
		for(ObjectIterator<Double2DoubleMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Double2DoubleMap.Entry entry = iter.next();
			action.accept(entry.getDoubleKey(), entry.getDoubleValue());
		}
	}

	@Override
	public DoubleSet keySet() {
		return new AbstractDoubleSet() {
			@Override
			public boolean remove(double o) {
				return Double.doubleToLongBits(AbstractDouble2DoubleMap.this.remove(o)) != Double.doubleToLongBits(getDefaultReturnValue());
			}
			
			@Override
			public boolean add(double o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public DoubleIterator iterator() {
				return new DoubleIterator() {
					ObjectIterator<Double2DoubleMap.Entry> iter = getFastIterator(AbstractDouble2DoubleMap.this);
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
				return AbstractDouble2DoubleMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractDouble2DoubleMap.this.clear();
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
				return AbstractDouble2DoubleMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractDouble2DoubleMap.this.clear();
			}
			
			@Override
			public DoubleIterator iterator() {
				return new DoubleIterator() {
					ObjectIterator<Double2DoubleMap.Entry> iter = getFastIterator(AbstractDouble2DoubleMap.this);
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
	public ObjectSet<Map.Entry<Double, Double>> entrySet() {
		return (ObjectSet)double2DoubleEntrySet();
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o instanceof Map) {
			if(size() != ((Map<?, ?>)o).size()) return false;
			if(o instanceof Double2DoubleMap) return double2DoubleEntrySet().containsAll(((Double2DoubleMap)o).double2DoubleEntrySet());
			return double2DoubleEntrySet().containsAll(((Map<?, ?>)o).entrySet());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		ObjectIterator<Double2DoubleMap.Entry> iter = getFastIterator(this);
		while(iter.hasNext()) hash += iter.next().hashCode();
		return hash;
	}
	
	public static class ReversedDouble2DoubleOrderedMap extends AbstractDouble2DoubleMap implements Double2DoubleOrderedMap {
		Double2DoubleOrderedMap map;
		
		public ReversedDouble2DoubleOrderedMap(Double2DoubleOrderedMap map) {
			this.map = map;
		}
		@Override
		public AbstractDouble2DoubleMap setDefaultReturnValue(double v) {
			map.setDefaultReturnValue(v);
			return this;
		}
		@Override
		public double getDefaultReturnValue() { return map.getDefaultReturnValue(); }
		@Override
		public Double2DoubleOrderedMap copy() { throw new UnsupportedOperationException(); }
		@Override
		public double put(double key, double value) { return map.put(key, value); }
		@Override
		public double putIfAbsent(double key, double value) { return map.putIfAbsent(key, value); }
		@Override
		public double addTo(double key, double value) { return map.addTo(key, value); }
		@Override
		public double subFrom(double key, double value) { return map.subFrom(key, value); }
		@Override
		public double remove(double key) { return map.remove(key); }
		@Override
		public boolean remove(double key, double value) { return map.remove(key, value); }
		@Override
		public double removeOrDefault(double key, double defaultValue) { return map.removeOrDefault(key, defaultValue); }
		@Override
		public boolean containsKey(double key) { return map.containsKey(key); }
		@Override
		public boolean containsValue(double value) { return map.containsValue(value); }
		@Override
		public boolean replace(double key, double oldValue, double newValue) { return map.replace(key, oldValue, newValue); }
		@Override
		public double replace(double key, double value) { return map.replace(key, value); }
		@Override
		public void replaceDoubles(Double2DoubleMap m) { map.replaceDoubles(m); }
		@Override
		public void replaceDoubles(DoubleDoubleUnaryOperator mappingFunction) { map.replaceDoubles(mappingFunction); }
		@Override
		public double computeDouble(double key, DoubleDoubleUnaryOperator mappingFunction) { return map.computeDouble(key, mappingFunction); }
		@Override
		public double computeDoubleIfAbsent(double key, DoubleUnaryOperator mappingFunction) { return map.computeDoubleIfAbsent(key, mappingFunction); }
		@Override
		public double supplyDoubleIfAbsent(double key, DoubleSupplier valueProvider) { return map.supplyDoubleIfAbsent(key, valueProvider); }
		@Override
		public double computeDoubleIfPresent(double key, DoubleDoubleUnaryOperator mappingFunction) { return map.computeDoubleIfPresent(key, mappingFunction); }
		@Override
		public double computeDoubleNonDefault(double key, DoubleDoubleUnaryOperator mappingFunction) { return map.computeDoubleNonDefault(key, mappingFunction); }
		@Override
		public double computeDoubleIfAbsentNonDefault(double key, DoubleUnaryOperator mappingFunction) { return map.computeDoubleIfAbsentNonDefault(key, mappingFunction); }
		@Override
		public double supplyDoubleIfAbsentNonDefault(double key, DoubleSupplier valueProvider) { return map.supplyDoubleIfAbsentNonDefault(key, valueProvider); }
		@Override
		public double computeDoubleIfPresentNonDefault(double key, DoubleDoubleUnaryOperator mappingFunction) { return map.computeDoubleIfPresentNonDefault(key, mappingFunction); }
		@Override
		public double mergeDouble(double key, double value, DoubleDoubleUnaryOperator mappingFunction) { return map.mergeDouble(key, value, mappingFunction); }
		@Override
		public double getOrDefault(double key, double defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public double get(double key) { return map.get(key); }
		@Override
		public double putAndMoveToFirst(double key, double value) { return map.putAndMoveToLast(key, value); }
		@Override
		public double putAndMoveToLast(double key, double value) { return map.putAndMoveToFirst(key, value); }
		@Override
		public double putFirst(double key, double value) { return map.putLast(key, value); }
		@Override
		public double putLast(double key, double value) { return map.putFirst(key, value); }
		@Override
		public boolean moveToFirst(double key) { return map.moveToLast(key); }
		@Override
		public boolean moveToLast(double key) { return map.moveToFirst(key); }
		@Override
		public double getAndMoveToFirst(double key) { return map.getAndMoveToLast(key); }
		@Override
		public double getAndMoveToLast(double key) { return map.getAndMoveToFirst(key); }
		@Override
		public double firstDoubleKey() { return map.lastDoubleKey(); }
		@Override
		public double pollFirstDoubleKey() { return map.pollLastDoubleKey(); }
		@Override
		public double lastDoubleKey() { return map.firstDoubleKey(); }
		@Override
		public double pollLastDoubleKey() { return map.pollFirstDoubleKey(); }
		@Override
		public double firstDoubleValue() { return map.lastDoubleValue(); }
		@Override
		public double lastDoubleValue() { return map.firstDoubleValue(); }
		@Override
		public Double2DoubleMap.Entry firstEntry() { return map.lastEntry(); }
		@Override
		public Double2DoubleMap.Entry lastEntry() { return map.firstEntry(); }
		@Override
		public Double2DoubleMap.Entry pollFirstEntry() { return map.pollLastEntry(); }
		@Override
		public Double2DoubleMap.Entry pollLastEntry() { return map.pollFirstEntry(); }
		@Override
		public ObjectOrderedSet<Double2DoubleMap.Entry> double2DoubleEntrySet() { return new AbstractObjectSet.ReversedObjectOrderedSet<>(map.double2DoubleEntrySet()); }
		@Override
		public DoubleOrderedSet keySet() { return new AbstractDoubleSet.ReversedDoubleOrderedSet(map.keySet()); }
		@Override
		public DoubleOrderedCollection values() { return map.values().reversed(); }
		@Override
		public Double2DoubleOrderedMap reversed() { return map; }
	}

	
	/**
	 * A Simple Type Specific Entry class to reduce boxing/unboxing
	 */
	public static class BasicEntry implements Double2DoubleMap.Entry {
		protected double key;
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
		public BasicEntry(Double key, Double value) {
			this.key = key.doubleValue();
			this.value = value.doubleValue();
		}
		
		/**
		 * A Type Specific Constructor
		 * @param key the key of a entry
		 * @param value the value of a entry 
		 */
		public BasicEntry(double key, double value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * A Helper method for fast replacing values
		 * @param key the key that should be replaced
		 * @param value the value that should be replaced
		 */
		public void set(double key, double value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public double getDoubleKey() {
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
				if(obj instanceof Double2DoubleMap.Entry) {
					Double2DoubleMap.Entry entry = (Double2DoubleMap.Entry)obj;
					return Double.doubleToLongBits(key) == Double.doubleToLongBits(entry.getDoubleKey()) && Double.doubleToLongBits(value) == Double.doubleToLongBits(entry.getDoubleValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Double && value instanceof Double && Double.doubleToLongBits(this.key) == Double.doubleToLongBits(((Double)key).doubleValue()) && Double.doubleToLongBits(this.value) == Double.doubleToLongBits(((Double)value).doubleValue());
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Double.hashCode(key) ^ Double.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Double.toString(key) + "=" + Double.toString(value);
		}
	}
}