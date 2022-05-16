package speiger.src.collections.objects.maps.abstracts;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.functions.consumer.ObjectDoubleConsumer;
import speiger.src.collections.objects.functions.function.Object2DoubleFunction;
import speiger.src.collections.objects.functions.function.ObjectDoubleUnaryOperator;
import speiger.src.collections.objects.maps.interfaces.Object2DoubleMap;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.utils.maps.Object2DoubleMaps;
import speiger.src.collections.doubles.collections.AbstractDoubleCollection;
import speiger.src.collections.doubles.collections.DoubleCollection;
import speiger.src.collections.doubles.collections.DoubleIterator;
import speiger.src.collections.doubles.functions.function.DoubleDoubleUnaryOperator;
import speiger.src.collections.doubles.functions.DoubleSupplier;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Base Implementation of a Type Specific Map to reduce boxing/unboxing
 * @param <T> the type of elements maintained by this Collection
 */
public abstract class AbstractObject2DoubleMap<T> extends AbstractMap<T, Double> implements Object2DoubleMap<T>
{
	protected double defaultReturnValue = 0D;
	
	@Override
	public double getDefaultReturnValue() {
		return defaultReturnValue;
	}
	
	@Override
	public AbstractObject2DoubleMap<T> setDefaultReturnValue(double v) {
		defaultReturnValue = v;
		return this;
	}
	
	@Override
	public Object2DoubleMap<T> copy() { 
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public Double put(T key, Double value) {
		return Double.valueOf(put(key, value.doubleValue()));
	}
	
	@Override
	public void addToAll(Object2DoubleMap<T> m) {
		for(Object2DoubleMap.Entry<T> entry : Object2DoubleMaps.fastIterable(m))
			addTo(entry.getKey(), entry.getDoubleValue());
	}
	
	@Override
	public void putAll(Object2DoubleMap<T> m) {
		for(ObjectIterator<Object2DoubleMap.Entry<T>> iter = Object2DoubleMaps.fastIterator(m);iter.hasNext();) {
			Object2DoubleMap.Entry<T> entry = iter.next();
			put(entry.getKey(), entry.getDoubleValue());
		}
	}
	
	@Override
	public void putAll(Map<? extends T, ? extends Double> m)
	{
		if(m instanceof Object2DoubleMap) putAll((Object2DoubleMap<T>)m);
		else super.putAll(m);
	}
	
	@Override
	public void putAll(T[] keys, double[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);
	}
	
	@Override
	public void putAll(T[] keys, Double[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);		
	}
	
	@Override
	public void putAllIfAbsent(Object2DoubleMap<T> m) {
		for(Object2DoubleMap.Entry<T> entry : Object2DoubleMaps.fastIterable(m))
			putIfAbsent(entry.getKey(), entry.getDoubleValue());
	}
	
	
	@Override
	public boolean containsKey(Object key) {
		for(ObjectIterator<T> iter = keySet().iterator();iter.hasNext();)
			if(Objects.equals(key, iter.next())) return true;
		return false;
	}
	
	@Override
	public boolean containsValue(double value) {
		for(DoubleIterator iter = values().iterator();iter.hasNext();)
			if(Double.doubleToLongBits(iter.nextDouble()) == Double.doubleToLongBits(value)) return true;
		return false;
	}
	
	@Override
	public boolean replace(T key, double oldValue, double newValue) {
		double curValue = getDouble(key);
		if (Double.doubleToLongBits(curValue) != Double.doubleToLongBits(oldValue) || (Double.doubleToLongBits(curValue) == Double.doubleToLongBits(getDefaultReturnValue()) && !containsKey(key))) {
			return false;
		}
		put(key, newValue);
		return true;
	}

	@Override
	public double replace(T key, double value) {
		double curValue;
		if (Double.doubleToLongBits((curValue = getDouble(key))) != Double.doubleToLongBits(getDefaultReturnValue()) || containsKey(key)) {
			curValue = put(key, value);
		}
		return curValue;
	}
	
	@Override
	public void replaceDoubles(Object2DoubleMap<T> m) {
		for(Object2DoubleMap.Entry<T> entry : Object2DoubleMaps.fastIterable(m))
			replace(entry.getKey(), entry.getDoubleValue());
	}
	
	@Override
	public void replaceDoubles(ObjectDoubleUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(ObjectIterator<Object2DoubleMap.Entry<T>> iter = Object2DoubleMaps.fastIterator(this);iter.hasNext();) {
			Object2DoubleMap.Entry<T> entry = iter.next();
			entry.setValue(mappingFunction.applyAsDouble(entry.getKey(), entry.getDoubleValue()));
		}
	}

	@Override
	public double computeDouble(T key, ObjectDoubleUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		double value = getDouble(key);
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
	public double computeDoubleIfAbsent(T key, Object2DoubleFunction<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		double value;
		if((value = getDouble(key)) == getDefaultReturnValue() || !containsKey(key)) {
			double newValue = mappingFunction.getDouble(key);
			if(Double.doubleToLongBits(newValue) != Double.doubleToLongBits(getDefaultReturnValue())) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public double supplyDoubleIfAbsent(T key, DoubleSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		double value;
		if((value = getDouble(key)) == getDefaultReturnValue() || !containsKey(key)) {
			double newValue = valueProvider.getDouble();
			if(Double.doubleToLongBits(newValue) != Double.doubleToLongBits(getDefaultReturnValue())) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public double computeDoubleIfPresent(T key, ObjectDoubleUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		double value;
		if(Double.doubleToLongBits((value = getDouble(key))) != Double.doubleToLongBits(getDefaultReturnValue()) || containsKey(key)) {
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
	public double mergeDouble(T key, double value, DoubleDoubleUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		double oldValue = getDouble(key);
		double newValue = Double.doubleToLongBits(oldValue) == Double.doubleToLongBits(getDefaultReturnValue()) ? value : mappingFunction.applyAsDouble(oldValue, value);
		if(Double.doubleToLongBits(newValue) == Double.doubleToLongBits(getDefaultReturnValue())) remove(key);
		else put(key, newValue);
		return newValue;
	}
	
	@Override
	public void mergeAllDouble(Object2DoubleMap<T> m, DoubleDoubleUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Object2DoubleMap.Entry<T> entry : Object2DoubleMaps.fastIterable(m)) {
			T key = entry.getKey();
			double oldValue = getDouble(key);
			double newValue = Double.doubleToLongBits(oldValue) == Double.doubleToLongBits(getDefaultReturnValue()) ? entry.getDoubleValue() : mappingFunction.applyAsDouble(oldValue, entry.getDoubleValue());
			if(Double.doubleToLongBits(newValue) == Double.doubleToLongBits(getDefaultReturnValue())) remove(key);
			else put(key, newValue);
		}
	}
	
	@Override
	public Double get(Object key) {
		return Double.valueOf(getDouble((T)key));
	}
	
	@Override
	public Double getOrDefault(Object key, Double defaultValue) {
		Double value = get(key);
		return Double.doubleToLongBits(value) != Double.doubleToLongBits(getDefaultReturnValue()) || containsKey(key) ? value : defaultValue;
	}
	
	@Override
	public void forEach(ObjectDoubleConsumer<T> action) {
		Objects.requireNonNull(action);
		for(ObjectIterator<Object2DoubleMap.Entry<T>> iter = Object2DoubleMaps.fastIterator(this);iter.hasNext();) {
			Object2DoubleMap.Entry<T> entry = iter.next();
			action.accept(entry.getKey(), entry.getDoubleValue());
		}
	}

	@Override
	public ObjectSet<T> keySet() {
		return new AbstractObjectSet<T>() {
			@Override
			public boolean remove(Object o) {
				return Double.doubleToLongBits(AbstractObject2DoubleMap.this.remove(o)) != Double.doubleToLongBits(getDefaultReturnValue());
			}
			
			@Override
			public boolean add(T o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public ObjectIterator<T> iterator() {
				return new ObjectIterator<T>() {
					ObjectIterator<Object2DoubleMap.Entry<T>> iter = Object2DoubleMaps.fastIterator(AbstractObject2DoubleMap.this);
					@Override
					public boolean hasNext() {
						return iter.hasNext();
					}

					@Override
					public T next() {
						return iter.next().getKey();
					}
					
					@Override
					public void remove() {
						iter.remove();
					}
				};
			}
			
			@Override
			public int size() {
				return AbstractObject2DoubleMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractObject2DoubleMap.this.clear();
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
				return AbstractObject2DoubleMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractObject2DoubleMap.this.clear();
			}
			
			@Override
			public DoubleIterator iterator() {
				return new DoubleIterator() {
					ObjectIterator<Object2DoubleMap.Entry<T>> iter = Object2DoubleMaps.fastIterator(AbstractObject2DoubleMap.this);
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
	public ObjectSet<Map.Entry<T, Double>> entrySet() {
		return (ObjectSet)object2DoubleEntrySet();
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o instanceof Map) {
			if(size() != ((Map<?, ?>)o).size()) return false;
			if(o instanceof Object2DoubleMap) return object2DoubleEntrySet().containsAll(((Object2DoubleMap<T>)o).object2DoubleEntrySet());
			return object2DoubleEntrySet().containsAll(((Map<?, ?>)o).entrySet());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		ObjectIterator<Object2DoubleMap.Entry<T>> iter = Object2DoubleMaps.fastIterator(this);
		while(iter.hasNext()) hash += iter.next().hashCode();
		return hash;
	}
	
	/**
	 * A Simple Type Specific Entry class to reduce boxing/unboxing
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class BasicEntry<T> implements Object2DoubleMap.Entry<T> {
		protected T key;
		protected double value;
		
		/**
		 * A basic Empty constructor
		 */
		public BasicEntry() {}
		/**
		 * A Type Specific Constructor
		 * @param key the key of a entry
		 * @param value the value of a entry 
		 */
		public BasicEntry(T key, double value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * A Helper method for fast replacing values
		 * @param key the key that should be replaced
		 * @param value the value that should be replaced
		 */
		public void set(T key, double value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public T getKey() {
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
				if(obj instanceof Object2DoubleMap.Entry) {
					Object2DoubleMap.Entry<T> entry = (Object2DoubleMap.Entry<T>)obj;
					return Objects.equals(key, entry.getKey()) && Double.doubleToLongBits(value) == Double.doubleToLongBits(entry.getDoubleValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return value instanceof Double && Objects.equals(this.key, key) && Double.doubleToLongBits(this.value) == Double.doubleToLongBits(((Double)value).doubleValue());
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Objects.hashCode(key) ^ Double.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Objects.toString(key) + "=" + Double.toString(value);
		}
	}
}