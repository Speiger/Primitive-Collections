package speiger.src.collections.objects.maps.abstracts;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.functions.consumer.ObjectByteConsumer;
import speiger.src.collections.objects.functions.function.ToByteFunction;
import speiger.src.collections.objects.functions.function.ObjectByteUnaryOperator;
import speiger.src.collections.objects.maps.interfaces.Object2ByteMap;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.utils.maps.Object2ByteMaps;
import speiger.src.collections.bytes.collections.AbstractByteCollection;
import speiger.src.collections.bytes.collections.ByteCollection;
import speiger.src.collections.bytes.collections.ByteIterator;
import speiger.src.collections.bytes.functions.function.ByteByteUnaryOperator;
import speiger.src.collections.bytes.functions.ByteSupplier;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Base Implementation of a Type Specific Map to reduce boxing/unboxing
 * @param <T> the keyType of elements maintained by this Collection
 */
public abstract class AbstractObject2ByteMap<T> extends AbstractMap<T, Byte> implements Object2ByteMap<T>
{
	protected byte defaultReturnValue = (byte)0;
	
	@Override
	public byte getDefaultReturnValue() {
		return defaultReturnValue;
	}
	
	@Override
	public AbstractObject2ByteMap<T> setDefaultReturnValue(byte v) {
		defaultReturnValue = v;
		return this;
	}
	
	protected ObjectIterable<Object2ByteMap.Entry<T>> getFastIterable(Object2ByteMap<T> map) {
		return Object2ByteMaps.fastIterable(map);
	}
	
	protected ObjectIterator<Object2ByteMap.Entry<T>> getFastIterator(Object2ByteMap<T> map) {
		return Object2ByteMaps.fastIterator(map);
	}
	
	@Override
	public Object2ByteMap<T> copy() { 
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public Byte put(T key, Byte value) {
		return Byte.valueOf(put(key, value.byteValue()));
	}
	
	@Override
	public void addToAll(Object2ByteMap<T> m) {
		for(Object2ByteMap.Entry<T> entry : getFastIterable(m))
			addTo(entry.getKey(), entry.getByteValue());
	}
	
	@Override
	public void putAll(Object2ByteMap<T> m) {
		for(ObjectIterator<Object2ByteMap.Entry<T>> iter = getFastIterator(m);iter.hasNext();) {
			Object2ByteMap.Entry<T> entry = iter.next();
			put(entry.getKey(), entry.getByteValue());
		}
	}
	
	@Override
	public void putAll(Map<? extends T, ? extends Byte> m)
	{
		if(m instanceof Object2ByteMap) putAll((Object2ByteMap<T>)m);
		else super.putAll(m);
	}
	
	@Override
	public void putAll(T[] keys, byte[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);
	}
	
	@Override
	public void putAll(T[] keys, Byte[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);		
	}
	
	@Override
	public void putAllIfAbsent(Object2ByteMap<T> m) {
		for(Object2ByteMap.Entry<T> entry : getFastIterable(m))
			putIfAbsent(entry.getKey(), entry.getByteValue());
	}
	
	
	@Override
	public boolean containsKey(Object key) {
		for(ObjectIterator<T> iter = keySet().iterator();iter.hasNext();)
			if(Objects.equals(key, iter.next())) return true;
		return false;
	}
	
	@Override
	public boolean containsValue(byte value) {
		for(ByteIterator iter = values().iterator();iter.hasNext();)
			if(iter.nextByte() == value) return true;
		return false;
	}
	
	@Override
	public boolean replace(T key, byte oldValue, byte newValue) {
		byte curValue = getByte(key);
		if (curValue != oldValue || (curValue == getDefaultReturnValue() && !containsKey(key))) {
			return false;
		}
		put(key, newValue);
		return true;
	}

	@Override
	public byte replace(T key, byte value) {
		byte curValue;
		if ((curValue = getByte(key)) != getDefaultReturnValue() || containsKey(key)) {
			curValue = put(key, value);
		}
		return curValue;
	}
	
	@Override
	public void replaceBytes(Object2ByteMap<T> m) {
		for(Object2ByteMap.Entry<T> entry : getFastIterable(m))
			replace(entry.getKey(), entry.getByteValue());
	}
	
	@Override
	public void replaceBytes(ObjectByteUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(ObjectIterator<Object2ByteMap.Entry<T>> iter = getFastIterator(this);iter.hasNext();) {
			Object2ByteMap.Entry<T> entry = iter.next();
			entry.setValue(mappingFunction.applyAsByte(entry.getKey(), entry.getByteValue()));
		}
	}

	@Override
	public byte computeByte(T key, ObjectByteUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		byte newValue = mappingFunction.applyAsByte(key, getByte(key));
		put(key, newValue);
		return newValue;
	}
	
	@Override
	public byte computeByteIfAbsent(T key, ToByteFunction<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(!containsKey(key)) {
			byte newValue = mappingFunction.applyAsByte(key);
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public byte supplyByteIfAbsent(T key, ByteSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		if(!containsKey(key)) {
			byte newValue = valueProvider.getAsByte();
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public byte computeByteIfPresent(T key, ObjectByteUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(containsKey(key)) {
			byte newValue = mappingFunction.applyAsByte(key, getByte(key));
			put(key, newValue);
			return newValue;
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public byte computeByteNonDefault(T key, ObjectByteUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		byte value = getByte(key);
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
	public byte computeByteIfAbsentNonDefault(T key, ToByteFunction<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		byte value;
		if((value = getByte(key)) == getDefaultReturnValue() || !containsKey(key)) {
			byte newValue = mappingFunction.applyAsByte(key);
			if(newValue != getDefaultReturnValue()) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public byte supplyByteIfAbsentNonDefault(T key, ByteSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		byte value;
		if((value = getByte(key)) == getDefaultReturnValue() || !containsKey(key)) {
			byte newValue = valueProvider.getAsByte();
			if(newValue != getDefaultReturnValue()) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public byte computeByteIfPresentNonDefault(T key, ObjectByteUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		byte value;
		if((value = getByte(key)) != getDefaultReturnValue() || containsKey(key)) {
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
	public byte mergeByte(T key, byte value, ByteByteUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		byte oldValue = getByte(key);
		byte newValue = oldValue == getDefaultReturnValue() ? value : mappingFunction.applyAsByte(oldValue, value);
		if(newValue == getDefaultReturnValue()) remove(key);
		else put(key, newValue);
		return newValue;
	}
	
	@Override
	public void mergeAllByte(Object2ByteMap<T> m, ByteByteUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Object2ByteMap.Entry<T> entry : getFastIterable(m)) {
			T key = entry.getKey();
			byte oldValue = getByte(key);
			byte newValue = oldValue == getDefaultReturnValue() ? entry.getByteValue() : mappingFunction.applyAsByte(oldValue, entry.getByteValue());
			if(newValue == getDefaultReturnValue()) remove(key);
			else put(key, newValue);
		}
	}
	
	@Override
	public Byte get(Object key) {
		return Byte.valueOf(getByte((T)key));
	}
	
	@Override
	public Byte getOrDefault(Object key, Byte defaultValue) {
		Byte value = get(key);
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	
	@Override
	public Byte remove(Object key) {
		return Byte.valueOf(rem((T)key));
	}
	
	@Override
	public void forEach(ObjectByteConsumer<T> action) {
		Objects.requireNonNull(action);
		for(ObjectIterator<Object2ByteMap.Entry<T>> iter = getFastIterator(this);iter.hasNext();) {
			Object2ByteMap.Entry<T> entry = iter.next();
			action.accept(entry.getKey(), entry.getByteValue());
		}
	}

	@Override
	public ObjectSet<T> keySet() {
		return new AbstractObjectSet<T>() {
			@Override
			public boolean remove(Object o) {
				if(AbstractObject2ByteMap.this.containsKey(o)) {
					AbstractObject2ByteMap.this.remove(o);
					return true;
				}
				return false;
			}
			
			@Override
			public boolean add(T o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public ObjectIterator<T> iterator() {
				return new ObjectIterator<T>() {
					ObjectIterator<Object2ByteMap.Entry<T>> iter = getFastIterator(AbstractObject2ByteMap.this);
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
				return AbstractObject2ByteMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractObject2ByteMap.this.clear();
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
				return AbstractObject2ByteMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractObject2ByteMap.this.clear();
			}
			
			@Override
			public ByteIterator iterator() {
				return new ByteIterator() {
					ObjectIterator<Object2ByteMap.Entry<T>> iter = getFastIterator(AbstractObject2ByteMap.this);
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
	public ObjectSet<Map.Entry<T, Byte>> entrySet() {
		return (ObjectSet)object2ByteEntrySet();
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o instanceof Map) {
			if(size() != ((Map<?, ?>)o).size()) return false;
			if(o instanceof Object2ByteMap) return object2ByteEntrySet().containsAll(((Object2ByteMap<T>)o).object2ByteEntrySet());
			return object2ByteEntrySet().containsAll(((Map<?, ?>)o).entrySet());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		ObjectIterator<Object2ByteMap.Entry<T>> iter = getFastIterator(this);
		while(iter.hasNext()) hash += iter.next().hashCode();
		return hash;
	}
	
	/**
	 * A Simple Type Specific Entry class to reduce boxing/unboxing
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class BasicEntry<T> implements Object2ByteMap.Entry<T> {
		protected T key;
		protected byte value;
		
		/**
		 * A basic Empty constructor
		 */
		public BasicEntry() {}
		/**
		 * A Type Specific Constructor
		 * @param key the key of a entry
		 * @param value the value of a entry 
		 */
		public BasicEntry(T key, byte value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * A Helper method for fast replacing values
		 * @param key the key that should be replaced
		 * @param value the value that should be replaced
		 */
		public void set(T key, byte value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public T getKey() {
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
				if(obj instanceof Object2ByteMap.Entry) {
					Object2ByteMap.Entry<T> entry = (Object2ByteMap.Entry<T>)obj;
					return Objects.equals(key, entry.getKey()) && value == entry.getByteValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return value instanceof Byte && Objects.equals(this.key, key) && this.value == ((Byte)value).byteValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Objects.hashCode(key) ^ Byte.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Objects.toString(key) + "=" + Byte.toString(value);
		}
	}
}