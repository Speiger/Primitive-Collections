package speiger.src.collections.ints.maps.abstracts;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

import speiger.src.collections.ints.collections.IntIterator;
import speiger.src.collections.ints.functions.consumer.IntDoubleConsumer;
import speiger.src.collections.ints.functions.function.Int2DoubleFunction;
import speiger.src.collections.ints.functions.function.IntDoubleUnaryOperator;
import speiger.src.collections.ints.maps.interfaces.Int2DoubleMap;
import speiger.src.collections.ints.sets.AbstractIntSet;
import speiger.src.collections.ints.sets.IntSet;
import speiger.src.collections.ints.utils.maps.Int2DoubleMaps;
import speiger.src.collections.doubles.collections.AbstractDoubleCollection;
import speiger.src.collections.doubles.collections.DoubleCollection;
import speiger.src.collections.doubles.collections.DoubleIterator;
import speiger.src.collections.doubles.functions.function.DoubleDoubleUnaryOperator;
import speiger.src.collections.doubles.functions.DoubleSupplier;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Base Implementation of a Type Specific Map to reduce boxing/unboxing
 */
public abstract class AbstractInt2DoubleMap extends AbstractMap<Integer, Double> implements Int2DoubleMap
{
	protected double defaultReturnValue = 0D;
	
	@Override
	public double getDefaultReturnValue() {
		return defaultReturnValue;
	}
	
	@Override
	public AbstractInt2DoubleMap setDefaultReturnValue(double v) {
		defaultReturnValue = v;
		return this;
	}
	
	@Override
	public Int2DoubleMap copy() { 
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public Double put(Integer key, Double value) {
		return Double.valueOf(put(key.intValue(), value.doubleValue()));
	}
	
	@Override
	public void addToAll(Int2DoubleMap m) {
		for(Int2DoubleMap.Entry entry : Int2DoubleMaps.fastIterable(m))
			addTo(entry.getIntKey(), entry.getDoubleValue());
	}
	
	@Override
	public void putAll(Int2DoubleMap m) {
		for(ObjectIterator<Int2DoubleMap.Entry> iter = Int2DoubleMaps.fastIterator(m);iter.hasNext();) {
			Int2DoubleMap.Entry entry = iter.next();
			put(entry.getIntKey(), entry.getDoubleValue());
		}
	}
	
	@Override
	public void putAll(Map<? extends Integer, ? extends Double> m)
	{
		if(m instanceof Int2DoubleMap) putAll((Int2DoubleMap)m);
		else super.putAll(m);
	}
	
	@Override
	public void putAll(int[] keys, double[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);
	}
	
	@Override
	public void putAll(Integer[] keys, Double[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);		
	}
	
	@Override
	public void putAllIfAbsent(Int2DoubleMap m) {
		for(Int2DoubleMap.Entry entry : Int2DoubleMaps.fastIterable(m))
			putIfAbsent(entry.getIntKey(), entry.getDoubleValue());
	}
	
	
	@Override
	public boolean containsKey(int key) {
		for(IntIterator iter = keySet().iterator();iter.hasNext();)
			if(iter.nextInt() == key) return true;
		return false;
	}
	
	@Override
	public boolean containsValue(double value) {
		for(DoubleIterator iter = values().iterator();iter.hasNext();)
			if(Double.doubleToLongBits(iter.nextDouble()) == Double.doubleToLongBits(value)) return true;
		return false;
	}
	
	@Override
	public boolean replace(int key, double oldValue, double newValue) {
		double curValue = get(key);
		if (Double.doubleToLongBits(curValue) != Double.doubleToLongBits(oldValue) || (Double.doubleToLongBits(curValue) == Double.doubleToLongBits(getDefaultReturnValue()) && !containsKey(key))) {
			return false;
		}
		put(key, newValue);
		return true;
	}

	@Override
	public double replace(int key, double value) {
		double curValue;
		if (Double.doubleToLongBits((curValue = get(key))) != Double.doubleToLongBits(getDefaultReturnValue()) || containsKey(key)) {
			curValue = put(key, value);
		}
		return curValue;
	}
	
	@Override
	public void replaceDoubles(Int2DoubleMap m) {
		for(Int2DoubleMap.Entry entry : Int2DoubleMaps.fastIterable(m))
			replace(entry.getIntKey(), entry.getDoubleValue());
	}
	
	@Override
	public void replaceDoubles(IntDoubleUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(ObjectIterator<Int2DoubleMap.Entry> iter = Int2DoubleMaps.fastIterator(this);iter.hasNext();) {
			Int2DoubleMap.Entry entry = iter.next();
			entry.setValue(mappingFunction.applyAsDouble(entry.getIntKey(), entry.getDoubleValue()));
		}
	}

	@Override
	public double computeDouble(int key, IntDoubleUnaryOperator mappingFunction) {
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
	public double computeDoubleIfAbsent(int key, Int2DoubleFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		double value;
		if((value = get(key)) == getDefaultReturnValue() || !containsKey(key)) {
			double newValue = mappingFunction.get(key);
			if(Double.doubleToLongBits(newValue) != Double.doubleToLongBits(getDefaultReturnValue())) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public double supplyDoubleIfAbsent(int key, DoubleSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		double value;
		if((value = get(key)) == getDefaultReturnValue() || !containsKey(key)) {
			double newValue = valueProvider.getDouble();
			if(Double.doubleToLongBits(newValue) != Double.doubleToLongBits(getDefaultReturnValue())) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public double computeDoubleIfPresent(int key, IntDoubleUnaryOperator mappingFunction) {
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
	public double mergeDouble(int key, double value, DoubleDoubleUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		double oldValue = get(key);
		double newValue = Double.doubleToLongBits(oldValue) == Double.doubleToLongBits(getDefaultReturnValue()) ? value : mappingFunction.applyAsDouble(oldValue, value);
		if(Double.doubleToLongBits(newValue) == Double.doubleToLongBits(getDefaultReturnValue())) remove(key);
		else put(key, newValue);
		return newValue;
	}
	
	@Override
	public void mergeAllDouble(Int2DoubleMap m, DoubleDoubleUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Int2DoubleMap.Entry entry : Int2DoubleMaps.fastIterable(m)) {
			int key = entry.getIntKey();
			double oldValue = get(key);
			double newValue = Double.doubleToLongBits(oldValue) == Double.doubleToLongBits(getDefaultReturnValue()) ? entry.getDoubleValue() : mappingFunction.applyAsDouble(oldValue, entry.getDoubleValue());
			if(Double.doubleToLongBits(newValue) == Double.doubleToLongBits(getDefaultReturnValue())) remove(key);
			else put(key, newValue);
		}
	}
	
	@Override
	public Double get(Object key) {
		return Double.valueOf(key instanceof Integer ? get(((Integer)key).intValue()) : getDefaultReturnValue());
	}
	
	@Override
	public Double getOrDefault(Object key, Double defaultValue) {
		return Double.valueOf(key instanceof Integer ? getOrDefault(((Integer)key).intValue(), defaultValue.doubleValue()) : getDefaultReturnValue());
	}
	
	@Override
	public double getOrDefault(int key, double defaultValue) {
		double value = get(key);
		return Double.doubleToLongBits(value) != Double.doubleToLongBits(getDefaultReturnValue()) || containsKey(key) ? value : defaultValue;
	}
	
	@Override
	public void forEach(IntDoubleConsumer action) {
		Objects.requireNonNull(action);
		for(ObjectIterator<Int2DoubleMap.Entry> iter = Int2DoubleMaps.fastIterator(this);iter.hasNext();) {
			Int2DoubleMap.Entry entry = iter.next();
			action.accept(entry.getIntKey(), entry.getDoubleValue());
		}
	}

	@Override
	public IntSet keySet() {
		return new AbstractIntSet() {
			@Override
			public boolean remove(int o) {
				return Double.doubleToLongBits(AbstractInt2DoubleMap.this.remove(o)) != Double.doubleToLongBits(getDefaultReturnValue());
			}
			
			@Override
			public boolean add(int o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public IntIterator iterator() {
				return new IntIterator() {
					ObjectIterator<Int2DoubleMap.Entry> iter = Int2DoubleMaps.fastIterator(AbstractInt2DoubleMap.this);
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
				return AbstractInt2DoubleMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractInt2DoubleMap.this.clear();
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
				return AbstractInt2DoubleMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractInt2DoubleMap.this.clear();
			}
			
			@Override
			public DoubleIterator iterator() {
				return new DoubleIterator() {
					ObjectIterator<Int2DoubleMap.Entry> iter = Int2DoubleMaps.fastIterator(AbstractInt2DoubleMap.this);
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
	public ObjectSet<Map.Entry<Integer, Double>> entrySet() {
		return (ObjectSet)int2DoubleEntrySet();
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o instanceof Map) {
			if(size() != ((Map<?, ?>)o).size()) return false;
			if(o instanceof Int2DoubleMap) return int2DoubleEntrySet().containsAll(((Int2DoubleMap)o).int2DoubleEntrySet());
			return int2DoubleEntrySet().containsAll(((Map<?, ?>)o).entrySet());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		ObjectIterator<Int2DoubleMap.Entry> iter = Int2DoubleMaps.fastIterator(this);
		while(iter.hasNext()) hash += iter.next().hashCode();
		return hash;
	}
	
	/**
	 * A Simple Type Specific Entry class to reduce boxing/unboxing
	 */
	public static class BasicEntry implements Int2DoubleMap.Entry {
		protected int key;
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
		public BasicEntry(Integer key, Double value) {
			this.key = key.intValue();
			this.value = value.doubleValue();
		}
		
		/**
		 * A Type Specific Constructor
		 * @param key the key of a entry
		 * @param value the value of a entry 
		 */
		public BasicEntry(int key, double value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * A Helper method for fast replacing values
		 * @param key the key that should be replaced
		 * @param value the value that should be replaced
		 */
		public void set(int key, double value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public int getIntKey() {
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
				if(obj instanceof Int2DoubleMap.Entry) {
					Int2DoubleMap.Entry entry = (Int2DoubleMap.Entry)obj;
					return key == entry.getIntKey() && Double.doubleToLongBits(value) == Double.doubleToLongBits(entry.getDoubleValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Integer && value instanceof Double && this.key == ((Integer)key).intValue() && Double.doubleToLongBits(this.value) == Double.doubleToLongBits(((Double)value).doubleValue());
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Integer.hashCode(key) ^ Double.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Integer.toString(key) + "=" + Double.toString(value);
		}
	}
}