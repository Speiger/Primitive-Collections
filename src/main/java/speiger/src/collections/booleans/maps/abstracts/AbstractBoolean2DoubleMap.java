package speiger.src.collections.booleans.maps.abstracts;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

import speiger.src.collections.booleans.collections.BooleanIterator;
import speiger.src.collections.booleans.functions.consumer.BooleanDoubleConsumer;
import speiger.src.collections.booleans.functions.function.Boolean2DoubleFunction;
import speiger.src.collections.booleans.functions.function.BooleanDoubleUnaryOperator;
import speiger.src.collections.booleans.maps.interfaces.Boolean2DoubleMap;
import speiger.src.collections.booleans.sets.AbstractBooleanSet;
import speiger.src.collections.booleans.sets.BooleanSet;
import speiger.src.collections.booleans.utils.maps.Boolean2DoubleMaps;
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
public abstract class AbstractBoolean2DoubleMap extends AbstractMap<Boolean, Double> implements Boolean2DoubleMap
{
	protected double defaultReturnValue = 0D;
	
	@Override
	public double getDefaultReturnValue() {
		return defaultReturnValue;
	}
	
	@Override
	public AbstractBoolean2DoubleMap setDefaultReturnValue(double v) {
		defaultReturnValue = v;
		return this;
	}
	
	@Override
	public Boolean2DoubleMap copy() { 
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public Double put(Boolean key, Double value) {
		return Double.valueOf(put(key.booleanValue(), value.doubleValue()));
	}
	
	@Override
	public void addToAll(Boolean2DoubleMap m) {
		for(Boolean2DoubleMap.Entry entry : Boolean2DoubleMaps.fastIterable(m))
			addTo(entry.getBooleanKey(), entry.getDoubleValue());
	}
	
	@Override
	public void putAll(Boolean2DoubleMap m) {
		for(ObjectIterator<Boolean2DoubleMap.Entry> iter = Boolean2DoubleMaps.fastIterator(m);iter.hasNext();) {
			Boolean2DoubleMap.Entry entry = iter.next();
			put(entry.getBooleanKey(), entry.getDoubleValue());
		}
	}
	
	@Override
	public void putAll(Map<? extends Boolean, ? extends Double> m)
	{
		if(m instanceof Boolean2DoubleMap) putAll((Boolean2DoubleMap)m);
		else super.putAll(m);
	}
	
	@Override
	public void putAll(boolean[] keys, double[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);
	}
	
	@Override
	public void putAll(Boolean[] keys, Double[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);		
	}
	
	@Override
	public void putAllIfAbsent(Boolean2DoubleMap m) {
		for(Boolean2DoubleMap.Entry entry : Boolean2DoubleMaps.fastIterable(m))
			putIfAbsent(entry.getBooleanKey(), entry.getDoubleValue());
	}
	
	
	@Override
	public boolean containsKey(boolean key) {
		for(BooleanIterator iter = keySet().iterator();iter.hasNext();)
			if(iter.nextBoolean() == key) return true;
		return false;
	}
	
	@Override
	public boolean containsValue(double value) {
		for(DoubleIterator iter = values().iterator();iter.hasNext();)
			if(Double.doubleToLongBits(iter.nextDouble()) == Double.doubleToLongBits(value)) return true;
		return false;
	}
	
	@Override
	public boolean replace(boolean key, double oldValue, double newValue) {
		double curValue = get(key);
		if (Double.doubleToLongBits(curValue) != Double.doubleToLongBits(oldValue) || (Double.doubleToLongBits(curValue) == Double.doubleToLongBits(getDefaultReturnValue()) && !containsKey(key))) {
			return false;
		}
		put(key, newValue);
		return true;
	}

	@Override
	public double replace(boolean key, double value) {
		double curValue;
		if (Double.doubleToLongBits((curValue = get(key))) != Double.doubleToLongBits(getDefaultReturnValue()) || containsKey(key)) {
			curValue = put(key, value);
		}
		return curValue;
	}
	
	@Override
	public void replaceDoubles(Boolean2DoubleMap m) {
		for(Boolean2DoubleMap.Entry entry : Boolean2DoubleMaps.fastIterable(m))
			replace(entry.getBooleanKey(), entry.getDoubleValue());
	}
	
	@Override
	public void replaceDoubles(BooleanDoubleUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(ObjectIterator<Boolean2DoubleMap.Entry> iter = Boolean2DoubleMaps.fastIterator(this);iter.hasNext();) {
			Boolean2DoubleMap.Entry entry = iter.next();
			entry.setValue(mappingFunction.applyAsDouble(entry.getBooleanKey(), entry.getDoubleValue()));
		}
	}

	@Override
	public double computeDouble(boolean key, BooleanDoubleUnaryOperator mappingFunction) {
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
	public double computeDoubleIfAbsent(boolean key, Boolean2DoubleFunction mappingFunction) {
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
	public double supplyDoubleIfAbsent(boolean key, DoubleSupplier valueProvider) {
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
	public double computeDoubleIfPresent(boolean key, BooleanDoubleUnaryOperator mappingFunction) {
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
	public double mergeDouble(boolean key, double value, DoubleDoubleUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		double oldValue = get(key);
		double newValue = Double.doubleToLongBits(oldValue) == Double.doubleToLongBits(getDefaultReturnValue()) ? value : mappingFunction.applyAsDouble(oldValue, value);
		if(Double.doubleToLongBits(newValue) == Double.doubleToLongBits(getDefaultReturnValue())) remove(key);
		else put(key, newValue);
		return newValue;
	}
	
	@Override
	public void mergeAllDouble(Boolean2DoubleMap m, DoubleDoubleUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Boolean2DoubleMap.Entry entry : Boolean2DoubleMaps.fastIterable(m)) {
			boolean key = entry.getBooleanKey();
			double oldValue = get(key);
			double newValue = Double.doubleToLongBits(oldValue) == Double.doubleToLongBits(getDefaultReturnValue()) ? entry.getDoubleValue() : mappingFunction.applyAsDouble(oldValue, entry.getDoubleValue());
			if(Double.doubleToLongBits(newValue) == Double.doubleToLongBits(getDefaultReturnValue())) remove(key);
			else put(key, newValue);
		}
	}
	
	@Override
	public Double get(Object key) {
		return Double.valueOf(key instanceof Boolean ? get(((Boolean)key).booleanValue()) : getDefaultReturnValue());
	}
	
	@Override
	public Double getOrDefault(Object key, Double defaultValue) {
		return Double.valueOf(key instanceof Boolean ? getOrDefault(((Boolean)key).booleanValue(), defaultValue.doubleValue()) : getDefaultReturnValue());
	}
	
	@Override
	public double getOrDefault(boolean key, double defaultValue) {
		double value = get(key);
		return Double.doubleToLongBits(value) != Double.doubleToLongBits(getDefaultReturnValue()) || containsKey(key) ? value : defaultValue;
	}
	
	@Override
	public void forEach(BooleanDoubleConsumer action) {
		Objects.requireNonNull(action);
		for(ObjectIterator<Boolean2DoubleMap.Entry> iter = Boolean2DoubleMaps.fastIterator(this);iter.hasNext();) {
			Boolean2DoubleMap.Entry entry = iter.next();
			action.accept(entry.getBooleanKey(), entry.getDoubleValue());
		}
	}

	@Override
	public BooleanSet keySet() {
		return new AbstractBooleanSet() {
			@Override
			public boolean remove(boolean o) {
				return Double.doubleToLongBits(AbstractBoolean2DoubleMap.this.remove(o)) != Double.doubleToLongBits(getDefaultReturnValue());
			}
			
			@Override
			public boolean add(boolean o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public BooleanIterator iterator() {
				return new BooleanIterator() {
					ObjectIterator<Boolean2DoubleMap.Entry> iter = Boolean2DoubleMaps.fastIterator(AbstractBoolean2DoubleMap.this);
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
				return AbstractBoolean2DoubleMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractBoolean2DoubleMap.this.clear();
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
				return AbstractBoolean2DoubleMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractBoolean2DoubleMap.this.clear();
			}
			
			@Override
			public DoubleIterator iterator() {
				return new DoubleIterator() {
					ObjectIterator<Boolean2DoubleMap.Entry> iter = Boolean2DoubleMaps.fastIterator(AbstractBoolean2DoubleMap.this);
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
	public ObjectSet<Map.Entry<Boolean, Double>> entrySet() {
		return (ObjectSet)boolean2DoubleEntrySet();
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o instanceof Map) {
			if(size() != ((Map<?, ?>)o).size()) return false;
			if(o instanceof Boolean2DoubleMap) return boolean2DoubleEntrySet().containsAll(((Boolean2DoubleMap)o).boolean2DoubleEntrySet());
			return boolean2DoubleEntrySet().containsAll(((Map<?, ?>)o).entrySet());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		ObjectIterator<Boolean2DoubleMap.Entry> iter = Boolean2DoubleMaps.fastIterator(this);
		while(iter.hasNext()) hash += iter.next().hashCode();
		return hash;
	}
	
	/**
	 * A Simple Type Specific Entry class to reduce boxing/unboxing
	 */
	public static class BasicEntry implements Boolean2DoubleMap.Entry {
		protected boolean key;
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
		public BasicEntry(Boolean key, Double value) {
			this.key = key.booleanValue();
			this.value = value.doubleValue();
		}
		
		/**
		 * A Type Specific Constructor
		 * @param key the key of a entry
		 * @param value the value of a entry 
		 */
		public BasicEntry(boolean key, double value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * A Helper method for fast replacing values
		 * @param key the key that should be replaced
		 * @param value the value that should be replaced
		 */
		public void set(boolean key, double value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public boolean getBooleanKey() {
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
				if(obj instanceof Boolean2DoubleMap.Entry) {
					Boolean2DoubleMap.Entry entry = (Boolean2DoubleMap.Entry)obj;
					return key == entry.getBooleanKey() && Double.doubleToLongBits(value) == Double.doubleToLongBits(entry.getDoubleValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Boolean && value instanceof Double && this.key == ((Boolean)key).booleanValue() && Double.doubleToLongBits(this.value) == Double.doubleToLongBits(((Double)value).doubleValue());
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Boolean.hashCode(key) ^ Double.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Boolean.toString(key) + "=" + Double.toString(value);
		}
	}
}