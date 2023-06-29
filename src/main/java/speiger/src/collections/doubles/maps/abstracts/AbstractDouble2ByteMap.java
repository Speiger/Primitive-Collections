package speiger.src.collections.doubles.maps.abstracts;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

import speiger.src.collections.doubles.collections.DoubleIterator;
import speiger.src.collections.doubles.functions.consumer.DoubleByteConsumer;
import speiger.src.collections.doubles.functions.function.Double2ByteFunction;
import speiger.src.collections.doubles.functions.function.DoubleByteUnaryOperator;
import speiger.src.collections.doubles.maps.interfaces.Double2ByteMap;
import speiger.src.collections.doubles.sets.AbstractDoubleSet;
import speiger.src.collections.doubles.sets.DoubleSet;
import speiger.src.collections.doubles.utils.maps.Double2ByteMaps;
import speiger.src.collections.bytes.collections.AbstractByteCollection;
import speiger.src.collections.bytes.collections.ByteCollection;
import speiger.src.collections.bytes.collections.ByteIterator;
import speiger.src.collections.bytes.functions.function.ByteByteUnaryOperator;
import speiger.src.collections.bytes.functions.ByteSupplier;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Base Implementation of a Type Specific Map to reduce boxing/unboxing
 */
public abstract class AbstractDouble2ByteMap extends AbstractMap<Double, Byte> implements Double2ByteMap
{
	protected byte defaultReturnValue = (byte)0;
	
	@Override
	public byte getDefaultReturnValue() {
		return defaultReturnValue;
	}
	
	@Override
	public AbstractDouble2ByteMap setDefaultReturnValue(byte v) {
		defaultReturnValue = v;
		return this;
	}
	
	protected ObjectIterable<Double2ByteMap.Entry> getFastIterable(Double2ByteMap map) {
		return Double2ByteMaps.fastIterable(map);
	}
	
	protected ObjectIterator<Double2ByteMap.Entry> getFastIterator(Double2ByteMap map) {
		return Double2ByteMaps.fastIterator(map);
	}
	
	@Override
	public Double2ByteMap copy() { 
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public Byte put(Double key, Byte value) {
		return Byte.valueOf(put(key.doubleValue(), value.byteValue()));
	}
	
	@Override
	public void addToAll(Double2ByteMap m) {
		for(Double2ByteMap.Entry entry : getFastIterable(m))
			addTo(entry.getDoubleKey(), entry.getByteValue());
	}
	
	@Override
	public void putAll(Double2ByteMap m) {
		for(ObjectIterator<Double2ByteMap.Entry> iter = getFastIterator(m);iter.hasNext();) {
			Double2ByteMap.Entry entry = iter.next();
			put(entry.getDoubleKey(), entry.getByteValue());
		}
	}
	
	@Override
	public void putAll(Map<? extends Double, ? extends Byte> m)
	{
		if(m instanceof Double2ByteMap) putAll((Double2ByteMap)m);
		else super.putAll(m);
	}
	
	@Override
	public void putAll(double[] keys, byte[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);
	}
	
	@Override
	public void putAll(Double[] keys, Byte[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);		
	}
	
	@Override
	public void putAllIfAbsent(Double2ByteMap m) {
		for(Double2ByteMap.Entry entry : getFastIterable(m))
			putIfAbsent(entry.getDoubleKey(), entry.getByteValue());
	}
	
	
	@Override
	public boolean containsKey(double key) {
		for(DoubleIterator iter = keySet().iterator();iter.hasNext();)
			if(Double.doubleToLongBits(iter.nextDouble()) == Double.doubleToLongBits(key)) return true;
		return false;
	}
	
	@Override
	public boolean containsValue(byte value) {
		for(ByteIterator iter = values().iterator();iter.hasNext();)
			if(iter.nextByte() == value) return true;
		return false;
	}
	
	@Override
	public boolean replace(double key, byte oldValue, byte newValue) {
		byte curValue = get(key);
		if (curValue != oldValue || (curValue == getDefaultReturnValue() && !containsKey(key))) {
			return false;
		}
		put(key, newValue);
		return true;
	}

	@Override
	public byte replace(double key, byte value) {
		byte curValue;
		if ((curValue = get(key)) != getDefaultReturnValue() || containsKey(key)) {
			curValue = put(key, value);
		}
		return curValue;
	}
	
	@Override
	public void replaceBytes(Double2ByteMap m) {
		for(Double2ByteMap.Entry entry : getFastIterable(m))
			replace(entry.getDoubleKey(), entry.getByteValue());
	}
	
	@Override
	public void replaceBytes(DoubleByteUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(ObjectIterator<Double2ByteMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Double2ByteMap.Entry entry = iter.next();
			entry.setValue(mappingFunction.applyAsByte(entry.getDoubleKey(), entry.getByteValue()));
		}
	}

	@Override
	public byte computeByte(double key, DoubleByteUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		byte newValue = mappingFunction.applyAsByte(key, get(key));
		put(key, newValue);
		return newValue;
	}
	
	@Override
	public byte computeByteIfAbsent(double key, Double2ByteFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(!containsKey(key)) {
			byte newValue = mappingFunction.applyAsByte(key);
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public byte supplyByteIfAbsent(double key, ByteSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		if(!containsKey(key)) {
			byte newValue = valueProvider.getAsByte();
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public byte computeByteIfPresent(double key, DoubleByteUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(containsKey(key)) {
			byte newValue = mappingFunction.applyAsByte(key, get(key));
			put(key, newValue);
			return newValue;
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public byte computeByteNonDefault(double key, DoubleByteUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		byte value = get(key);
		byte newValue = mappingFunction.applyAsByte(key, value);
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
	public byte computeByteIfAbsentNonDefault(double key, Double2ByteFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		byte value;
		if((value = get(key)) == getDefaultReturnValue() || !containsKey(key)) {
			byte newValue = mappingFunction.applyAsByte(key);
			if(newValue != getDefaultReturnValue()) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public byte supplyByteIfAbsentNonDefault(double key, ByteSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		byte value;
		if((value = get(key)) == getDefaultReturnValue() || !containsKey(key)) {
			byte newValue = valueProvider.getAsByte();
			if(newValue != getDefaultReturnValue()) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public byte computeByteIfPresentNonDefault(double key, DoubleByteUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		byte value;
		if((value = get(key)) != getDefaultReturnValue() || containsKey(key)) {
			byte newValue = mappingFunction.applyAsByte(key, value);
			if(newValue != getDefaultReturnValue()) {
				put(key, newValue);
				return newValue;
			}
			remove(key);
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public byte mergeByte(double key, byte value, ByteByteUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		byte oldValue = get(key);
		byte newValue = oldValue == getDefaultReturnValue() ? value : mappingFunction.applyAsByte(oldValue, value);
		if(newValue == getDefaultReturnValue()) remove(key);
		else put(key, newValue);
		return newValue;
	}
	
	@Override
	public void mergeAllByte(Double2ByteMap m, ByteByteUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Double2ByteMap.Entry entry : getFastIterable(m)) {
			double key = entry.getDoubleKey();
			byte oldValue = get(key);
			byte newValue = oldValue == getDefaultReturnValue() ? entry.getByteValue() : mappingFunction.applyAsByte(oldValue, entry.getByteValue());
			if(newValue == getDefaultReturnValue()) remove(key);
			else put(key, newValue);
		}
	}
	
	@Override
	public Byte get(Object key) {
		return Byte.valueOf(key instanceof Double ? get(((Double)key).doubleValue()) : getDefaultReturnValue());
	}
	
	@Override
	public Byte getOrDefault(Object key, Byte defaultValue) {
		return Byte.valueOf(key instanceof Double ? getOrDefault(((Double)key).doubleValue(), defaultValue.byteValue()) : getDefaultReturnValue());
	}
	
	@Override
	public byte getOrDefault(double key, byte defaultValue) {
		byte value = get(key);
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	
	@Override
	public Byte remove(Object key) {
		return key instanceof Double ? Byte.valueOf(remove(((Double)key).doubleValue())) : Byte.valueOf(getDefaultReturnValue());
	}
	
	@Override
	public void forEach(DoubleByteConsumer action) {
		Objects.requireNonNull(action);
		for(ObjectIterator<Double2ByteMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Double2ByteMap.Entry entry = iter.next();
			action.accept(entry.getDoubleKey(), entry.getByteValue());
		}
	}

	@Override
	public DoubleSet keySet() {
		return new AbstractDoubleSet() {
			@Override
			public boolean remove(double o) {
				return AbstractDouble2ByteMap.this.remove(o) != getDefaultReturnValue();
			}
			
			@Override
			public boolean add(double o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public DoubleIterator iterator() {
				return new DoubleIterator() {
					ObjectIterator<Double2ByteMap.Entry> iter = getFastIterator(AbstractDouble2ByteMap.this);
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
				return AbstractDouble2ByteMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractDouble2ByteMap.this.clear();
			}
		};
	}

	@Override
	public ByteCollection values() {
		return new AbstractByteCollection() {
			@Override
			public boolean add(byte o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public int size() {
				return AbstractDouble2ByteMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractDouble2ByteMap.this.clear();
			}
			
			@Override
			public ByteIterator iterator() {
				return new ByteIterator() {
					ObjectIterator<Double2ByteMap.Entry> iter = getFastIterator(AbstractDouble2ByteMap.this);
					@Override
					public boolean hasNext() {
						return iter.hasNext();
					}
					
					@Override
					public byte nextByte() {
						return iter.next().getByteValue();
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
	public ObjectSet<Map.Entry<Double, Byte>> entrySet() {
		return (ObjectSet)double2ByteEntrySet();
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o instanceof Map) {
			if(size() != ((Map<?, ?>)o).size()) return false;
			if(o instanceof Double2ByteMap) return double2ByteEntrySet().containsAll(((Double2ByteMap)o).double2ByteEntrySet());
			return double2ByteEntrySet().containsAll(((Map<?, ?>)o).entrySet());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		ObjectIterator<Double2ByteMap.Entry> iter = getFastIterator(this);
		while(iter.hasNext()) hash += iter.next().hashCode();
		return hash;
	}
	
	/**
	 * A Simple Type Specific Entry class to reduce boxing/unboxing
	 */
	public static class BasicEntry implements Double2ByteMap.Entry {
		protected double key;
		protected byte value;
		
		/**
		 * A basic Empty constructor
		 */
		public BasicEntry() {}
		/**
		 * A Boxed Constructor for supporting java variants
		 * @param key the key of a entry
		 * @param value the value of a entry
		 */
		public BasicEntry(Double key, Byte value) {
			this.key = key.doubleValue();
			this.value = value.byteValue();
		}
		
		/**
		 * A Type Specific Constructor
		 * @param key the key of a entry
		 * @param value the value of a entry 
		 */
		public BasicEntry(double key, byte value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * A Helper method for fast replacing values
		 * @param key the key that should be replaced
		 * @param value the value that should be replaced
		 */
		public void set(double key, byte value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public double getDoubleKey() {
			return key;
		}

		@Override
		public byte getByteValue() {
			return value;
		}

		@Override
		public byte setValue(byte value) {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Map.Entry) {
				if(obj instanceof Double2ByteMap.Entry) {
					Double2ByteMap.Entry entry = (Double2ByteMap.Entry)obj;
					return Double.doubleToLongBits(key) == Double.doubleToLongBits(entry.getDoubleKey()) && value == entry.getByteValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Double && value instanceof Byte && Double.doubleToLongBits(this.key) == Double.doubleToLongBits(((Double)key).doubleValue()) && this.value == ((Byte)value).byteValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Double.hashCode(key) ^ Byte.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Double.toString(key) + "=" + Byte.toString(value);
		}
	}
}