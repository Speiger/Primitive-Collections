package speiger.src.collections.bytes.maps.abstracts;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

import speiger.src.collections.bytes.collections.ByteIterator;
import speiger.src.collections.bytes.functions.consumer.ByteDoubleConsumer;
import speiger.src.collections.bytes.functions.function.Byte2DoubleFunction;
import speiger.src.collections.bytes.functions.function.ByteDoubleUnaryOperator;
import speiger.src.collections.bytes.maps.interfaces.Byte2DoubleMap;
import speiger.src.collections.bytes.sets.AbstractByteSet;
import speiger.src.collections.bytes.sets.ByteSet;
import speiger.src.collections.bytes.utils.maps.Byte2DoubleMaps;
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
public abstract class AbstractByte2DoubleMap extends AbstractMap<Byte, Double> implements Byte2DoubleMap
{
	protected double defaultReturnValue = 0D;
	
	@Override
	public double getDefaultReturnValue() {
		return defaultReturnValue;
	}
	
	@Override
	public AbstractByte2DoubleMap setDefaultReturnValue(double v) {
		defaultReturnValue = v;
		return this;
	}
	
	protected ObjectIterable<Byte2DoubleMap.Entry> getFastIterable(Byte2DoubleMap map) {
		return Byte2DoubleMaps.fastIterable(map);
	}
	
	protected ObjectIterator<Byte2DoubleMap.Entry> getFastIterator(Byte2DoubleMap map) {
		return Byte2DoubleMaps.fastIterator(map);
	}
	
	@Override
	public Byte2DoubleMap copy() { 
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public Double put(Byte key, Double value) {
		return Double.valueOf(put(key.byteValue(), value.doubleValue()));
	}
	
	@Override
	public void addToAll(Byte2DoubleMap m) {
		for(Byte2DoubleMap.Entry entry : getFastIterable(m))
			addTo(entry.getByteKey(), entry.getDoubleValue());
	}
	
	@Override
	public void putAll(Byte2DoubleMap m) {
		for(ObjectIterator<Byte2DoubleMap.Entry> iter = getFastIterator(m);iter.hasNext();) {
			Byte2DoubleMap.Entry entry = iter.next();
			put(entry.getByteKey(), entry.getDoubleValue());
		}
	}
	
	@Override
	public void putAll(Map<? extends Byte, ? extends Double> m)
	{
		if(m instanceof Byte2DoubleMap) putAll((Byte2DoubleMap)m);
		else super.putAll(m);
	}
	
	@Override
	public void putAll(byte[] keys, double[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);
	}
	
	@Override
	public void putAll(Byte[] keys, Double[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);		
	}
	
	@Override
	public void putAllIfAbsent(Byte2DoubleMap m) {
		for(Byte2DoubleMap.Entry entry : getFastIterable(m))
			putIfAbsent(entry.getByteKey(), entry.getDoubleValue());
	}
	
	
	@Override
	public boolean containsKey(byte key) {
		for(ByteIterator iter = keySet().iterator();iter.hasNext();)
			if(iter.nextByte() == key) return true;
		return false;
	}
	
	@Override
	public boolean containsValue(double value) {
		for(DoubleIterator iter = values().iterator();iter.hasNext();)
			if(Double.doubleToLongBits(iter.nextDouble()) == Double.doubleToLongBits(value)) return true;
		return false;
	}
	
	@Override
	public boolean replace(byte key, double oldValue, double newValue) {
		double curValue = get(key);
		if (Double.doubleToLongBits(curValue) != Double.doubleToLongBits(oldValue) || (Double.doubleToLongBits(curValue) == Double.doubleToLongBits(getDefaultReturnValue()) && !containsKey(key))) {
			return false;
		}
		put(key, newValue);
		return true;
	}

	@Override
	public double replace(byte key, double value) {
		double curValue;
		if (Double.doubleToLongBits((curValue = get(key))) != Double.doubleToLongBits(getDefaultReturnValue()) || containsKey(key)) {
			curValue = put(key, value);
		}
		return curValue;
	}
	
	@Override
	public void replaceDoubles(Byte2DoubleMap m) {
		for(Byte2DoubleMap.Entry entry : getFastIterable(m))
			replace(entry.getByteKey(), entry.getDoubleValue());
	}
	
	@Override
	public void replaceDoubles(ByteDoubleUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(ObjectIterator<Byte2DoubleMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Byte2DoubleMap.Entry entry = iter.next();
			entry.setValue(mappingFunction.applyAsDouble(entry.getByteKey(), entry.getDoubleValue()));
		}
	}

	@Override
	public double computeDouble(byte key, ByteDoubleUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		double newValue = mappingFunction.applyAsDouble(key, get(key));
		put(key, newValue);
		return newValue;
	}
	
	@Override
	public double computeDoubleIfAbsent(byte key, Byte2DoubleFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(!containsKey(key)) {
			double newValue = mappingFunction.applyAsDouble(key);
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public double supplyDoubleIfAbsent(byte key, DoubleSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		if(!containsKey(key)) {
			double newValue = valueProvider.getAsDouble();
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public double computeDoubleIfPresent(byte key, ByteDoubleUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(containsKey(key)) {
			double newValue = mappingFunction.applyAsDouble(key, get(key));
			put(key, newValue);
			return newValue;
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public double computeDoubleNonDefault(byte key, ByteDoubleUnaryOperator mappingFunction) {
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
	public double computeDoubleIfAbsentNonDefault(byte key, Byte2DoubleFunction mappingFunction) {
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
	public double supplyDoubleIfAbsentNonDefault(byte key, DoubleSupplier valueProvider) {
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
	public double computeDoubleIfPresentNonDefault(byte key, ByteDoubleUnaryOperator mappingFunction) {
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
	public double mergeDouble(byte key, double value, DoubleDoubleUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		double oldValue = get(key);
		double newValue = Double.doubleToLongBits(oldValue) == Double.doubleToLongBits(getDefaultReturnValue()) ? value : mappingFunction.applyAsDouble(oldValue, value);
		if(Double.doubleToLongBits(newValue) == Double.doubleToLongBits(getDefaultReturnValue())) remove(key);
		else put(key, newValue);
		return newValue;
	}
	
	@Override
	public void mergeAllDouble(Byte2DoubleMap m, DoubleDoubleUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Byte2DoubleMap.Entry entry : getFastIterable(m)) {
			byte key = entry.getByteKey();
			double oldValue = get(key);
			double newValue = Double.doubleToLongBits(oldValue) == Double.doubleToLongBits(getDefaultReturnValue()) ? entry.getDoubleValue() : mappingFunction.applyAsDouble(oldValue, entry.getDoubleValue());
			if(Double.doubleToLongBits(newValue) == Double.doubleToLongBits(getDefaultReturnValue())) remove(key);
			else put(key, newValue);
		}
	}
	
	@Override
	public Double get(Object key) {
		return Double.valueOf(key instanceof Byte ? get(((Byte)key).byteValue()) : getDefaultReturnValue());
	}
	
	@Override
	public Double getOrDefault(Object key, Double defaultValue) {
		return Double.valueOf(key instanceof Byte ? getOrDefault(((Byte)key).byteValue(), defaultValue.doubleValue()) : getDefaultReturnValue());
	}
	
	@Override
	public double getOrDefault(byte key, double defaultValue) {
		double value = get(key);
		return Double.doubleToLongBits(value) != Double.doubleToLongBits(getDefaultReturnValue()) || containsKey(key) ? value : defaultValue;
	}
	
	
	@Override
	public Double remove(Object key) {
		return key instanceof Byte ? Double.valueOf(remove(((Byte)key).byteValue())) : Double.valueOf(getDefaultReturnValue());
	}
	
	@Override
	public void forEach(ByteDoubleConsumer action) {
		Objects.requireNonNull(action);
		for(ObjectIterator<Byte2DoubleMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Byte2DoubleMap.Entry entry = iter.next();
			action.accept(entry.getByteKey(), entry.getDoubleValue());
		}
	}

	@Override
	public ByteSet keySet() {
		return new AbstractByteSet() {
			@Override
			public boolean remove(byte o) {
				return Double.doubleToLongBits(AbstractByte2DoubleMap.this.remove(o)) != Double.doubleToLongBits(getDefaultReturnValue());
			}
			
			@Override
			public boolean add(byte o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public ByteIterator iterator() {
				return new ByteIterator() {
					ObjectIterator<Byte2DoubleMap.Entry> iter = getFastIterator(AbstractByte2DoubleMap.this);
					@Override
					public boolean hasNext() {
						return iter.hasNext();
					}

					@Override
					public byte nextByte() {
						return iter.next().getByteKey();
					}
					
					@Override
					public void remove() {
						iter.remove();
					}
				};
			}
			
			@Override
			public int size() {
				return AbstractByte2DoubleMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractByte2DoubleMap.this.clear();
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
				return AbstractByte2DoubleMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractByte2DoubleMap.this.clear();
			}
			
			@Override
			public DoubleIterator iterator() {
				return new DoubleIterator() {
					ObjectIterator<Byte2DoubleMap.Entry> iter = getFastIterator(AbstractByte2DoubleMap.this);
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
	public ObjectSet<Map.Entry<Byte, Double>> entrySet() {
		return (ObjectSet)byte2DoubleEntrySet();
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o instanceof Map) {
			if(size() != ((Map<?, ?>)o).size()) return false;
			if(o instanceof Byte2DoubleMap) return byte2DoubleEntrySet().containsAll(((Byte2DoubleMap)o).byte2DoubleEntrySet());
			return byte2DoubleEntrySet().containsAll(((Map<?, ?>)o).entrySet());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		ObjectIterator<Byte2DoubleMap.Entry> iter = getFastIterator(this);
		while(iter.hasNext()) hash += iter.next().hashCode();
		return hash;
	}
	
	/**
	 * A Simple Type Specific Entry class to reduce boxing/unboxing
	 */
	public static class BasicEntry implements Byte2DoubleMap.Entry {
		protected byte key;
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
		public BasicEntry(Byte key, Double value) {
			this.key = key.byteValue();
			this.value = value.doubleValue();
		}
		
		/**
		 * A Type Specific Constructor
		 * @param key the key of a entry
		 * @param value the value of a entry 
		 */
		public BasicEntry(byte key, double value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * A Helper method for fast replacing values
		 * @param key the key that should be replaced
		 * @param value the value that should be replaced
		 */
		public void set(byte key, double value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public byte getByteKey() {
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
				if(obj instanceof Byte2DoubleMap.Entry) {
					Byte2DoubleMap.Entry entry = (Byte2DoubleMap.Entry)obj;
					return key == entry.getByteKey() && Double.doubleToLongBits(value) == Double.doubleToLongBits(entry.getDoubleValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Byte && value instanceof Double && this.key == ((Byte)key).byteValue() && Double.doubleToLongBits(this.value) == Double.doubleToLongBits(((Double)value).doubleValue());
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Byte.hashCode(key) ^ Double.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Byte.toString(key) + "=" + Double.toString(value);
		}
	}
}