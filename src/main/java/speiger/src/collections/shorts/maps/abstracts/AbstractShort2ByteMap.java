package speiger.src.collections.shorts.maps.abstracts;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

import speiger.src.collections.shorts.collections.ShortIterator;
import speiger.src.collections.shorts.functions.consumer.ShortByteConsumer;
import speiger.src.collections.shorts.functions.function.Short2ByteFunction;
import speiger.src.collections.shorts.functions.function.ShortByteUnaryOperator;
import speiger.src.collections.shorts.maps.interfaces.Short2ByteMap;
import speiger.src.collections.shorts.sets.AbstractShortSet;
import speiger.src.collections.shorts.sets.ShortSet;
import speiger.src.collections.shorts.utils.maps.Short2ByteMaps;
import speiger.src.collections.bytes.collections.AbstractByteCollection;
import speiger.src.collections.bytes.collections.ByteCollection;
import speiger.src.collections.bytes.collections.ByteIterator;
import speiger.src.collections.bytes.functions.function.ByteByteUnaryOperator;
import speiger.src.collections.bytes.functions.ByteSupplier;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Base Implementation of a Type Specific Map to reduce boxing/unboxing
 */
public abstract class AbstractShort2ByteMap extends AbstractMap<Short, Byte> implements Short2ByteMap
{
	protected byte defaultReturnValue = (byte)0;
	
	@Override
	public byte getDefaultReturnValue() {
		return defaultReturnValue;
	}
	
	@Override
	public AbstractShort2ByteMap setDefaultReturnValue(byte v) {
		defaultReturnValue = v;
		return this;
	}
	
	@Override
	public Short2ByteMap copy() { 
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public Byte put(Short key, Byte value) {
		return Byte.valueOf(put(key.shortValue(), value.byteValue()));
	}
	
	@Override
	public void addToAll(Short2ByteMap m) {
		for(Short2ByteMap.Entry entry : Short2ByteMaps.fastIterable(m))
			addTo(entry.getShortKey(), entry.getByteValue());
	}
	
	@Override
	public void putAll(Short2ByteMap m) {
		for(ObjectIterator<Short2ByteMap.Entry> iter = Short2ByteMaps.fastIterator(m);iter.hasNext();) {
			Short2ByteMap.Entry entry = iter.next();
			put(entry.getShortKey(), entry.getByteValue());
		}
	}
	
	@Override
	public void putAll(Map<? extends Short, ? extends Byte> m)
	{
		if(m instanceof Short2ByteMap) putAll((Short2ByteMap)m);
		else super.putAll(m);
	}
	
	@Override
	public void putAll(short[] keys, byte[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);
	}
	
	@Override
	public void putAll(Short[] keys, Byte[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);		
	}
	
	@Override
	public void putAllIfAbsent(Short2ByteMap m) {
		for(Short2ByteMap.Entry entry : Short2ByteMaps.fastIterable(m))
			putIfAbsent(entry.getShortKey(), entry.getByteValue());
	}
	
	
	@Override
	public boolean containsKey(short key) {
		for(ShortIterator iter = keySet().iterator();iter.hasNext();)
			if(iter.nextShort() == key) return true;
		return false;
	}
	
	@Override
	public boolean containsValue(byte value) {
		for(ByteIterator iter = values().iterator();iter.hasNext();)
			if(iter.nextByte() == value) return true;
		return false;
	}
	
	@Override
	public boolean replace(short key, byte oldValue, byte newValue) {
		byte curValue = get(key);
		if (curValue != oldValue || (curValue == getDefaultReturnValue() && !containsKey(key))) {
			return false;
		}
		put(key, newValue);
		return true;
	}

	@Override
	public byte replace(short key, byte value) {
		byte curValue;
		if ((curValue = get(key)) != getDefaultReturnValue() || containsKey(key)) {
			curValue = put(key, value);
		}
		return curValue;
	}
	
	@Override
	public void replaceBytes(Short2ByteMap m) {
		for(Short2ByteMap.Entry entry : Short2ByteMaps.fastIterable(m))
			replace(entry.getShortKey(), entry.getByteValue());
	}
	
	@Override
	public void replaceBytes(ShortByteUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(ObjectIterator<Short2ByteMap.Entry> iter = Short2ByteMaps.fastIterator(this);iter.hasNext();) {
			Short2ByteMap.Entry entry = iter.next();
			entry.setValue(mappingFunction.applyAsByte(entry.getShortKey(), entry.getByteValue()));
		}
	}

	@Override
	public byte computeByte(short key, ShortByteUnaryOperator mappingFunction) {
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
	public byte computeByteIfAbsent(short key, Short2ByteFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		byte value;
		if((value = get(key)) == getDefaultReturnValue() || !containsKey(key)) {
			byte newValue = mappingFunction.get(key);
			if(newValue != getDefaultReturnValue()) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public byte supplyByteIfAbsent(short key, ByteSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		byte value;
		if((value = get(key)) == getDefaultReturnValue() || !containsKey(key)) {
			byte newValue = valueProvider.getByte();
			if(newValue != getDefaultReturnValue()) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public byte computeByteIfPresent(short key, ShortByteUnaryOperator mappingFunction) {
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
	public byte mergeByte(short key, byte value, ByteByteUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		byte oldValue = get(key);
		byte newValue = oldValue == getDefaultReturnValue() ? value : mappingFunction.applyAsByte(oldValue, value);
		if(newValue == getDefaultReturnValue()) remove(key);
		else put(key, newValue);
		return newValue;
	}
	
	@Override
	public void mergeAllByte(Short2ByteMap m, ByteByteUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Short2ByteMap.Entry entry : Short2ByteMaps.fastIterable(m)) {
			short key = entry.getShortKey();
			byte oldValue = get(key);
			byte newValue = oldValue == getDefaultReturnValue() ? entry.getByteValue() : mappingFunction.applyAsByte(oldValue, entry.getByteValue());
			if(newValue == getDefaultReturnValue()) remove(key);
			else put(key, newValue);
		}
	}
	
	@Override
	public Byte get(Object key) {
		return Byte.valueOf(key instanceof Short ? get(((Short)key).shortValue()) : getDefaultReturnValue());
	}
	
	@Override
	public Byte getOrDefault(Object key, Byte defaultValue) {
		return Byte.valueOf(key instanceof Short ? getOrDefault(((Short)key).shortValue(), defaultValue.byteValue()) : getDefaultReturnValue());
	}
	
	@Override
	public byte getOrDefault(short key, byte defaultValue) {
		byte value = get(key);
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	@Override
	public void forEach(ShortByteConsumer action) {
		Objects.requireNonNull(action);
		for(ObjectIterator<Short2ByteMap.Entry> iter = Short2ByteMaps.fastIterator(this);iter.hasNext();) {
			Short2ByteMap.Entry entry = iter.next();
			action.accept(entry.getShortKey(), entry.getByteValue());
		}
	}

	@Override
	public ShortSet keySet() {
		return new AbstractShortSet() {
			@Override
			public boolean remove(short o) {
				return AbstractShort2ByteMap.this.remove(o) != getDefaultReturnValue();
			}
			
			@Override
			public boolean add(short o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public ShortIterator iterator() {
				return new ShortIterator() {
					ObjectIterator<Short2ByteMap.Entry> iter = Short2ByteMaps.fastIterator(AbstractShort2ByteMap.this);
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
				return AbstractShort2ByteMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractShort2ByteMap.this.clear();
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
				return AbstractShort2ByteMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractShort2ByteMap.this.clear();
			}
			
			@Override
			public ByteIterator iterator() {
				return new ByteIterator() {
					ObjectIterator<Short2ByteMap.Entry> iter = Short2ByteMaps.fastIterator(AbstractShort2ByteMap.this);
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
	public ObjectSet<Map.Entry<Short, Byte>> entrySet() {
		return (ObjectSet)short2ByteEntrySet();
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o instanceof Map) {
			if(size() != ((Map<?, ?>)o).size()) return false;
			if(o instanceof Short2ByteMap) return short2ByteEntrySet().containsAll(((Short2ByteMap)o).short2ByteEntrySet());
			return short2ByteEntrySet().containsAll(((Map<?, ?>)o).entrySet());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		ObjectIterator<Short2ByteMap.Entry> iter = Short2ByteMaps.fastIterator(this);
		while(iter.hasNext()) hash += iter.next().hashCode();
		return hash;
	}
	
	/**
	 * A Simple Type Specific Entry class to reduce boxing/unboxing
	 */
	public static class BasicEntry implements Short2ByteMap.Entry {
		protected short key;
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
		public BasicEntry(Short key, Byte value) {
			this.key = key.shortValue();
			this.value = value.byteValue();
		}
		
		/**
		 * A Type Specific Constructor
		 * @param key the key of a entry
		 * @param value the value of a entry 
		 */
		public BasicEntry(short key, byte value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * A Helper method for fast replacing values
		 * @param key the key that should be replaced
		 * @param value the value that should be replaced
		 */
		public void set(short key, byte value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public short getShortKey() {
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
				if(obj instanceof Short2ByteMap.Entry) {
					Short2ByteMap.Entry entry = (Short2ByteMap.Entry)obj;
					return key == entry.getShortKey() && value == entry.getByteValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Short && value instanceof Byte && this.key == ((Short)key).shortValue() && this.value == ((Byte)value).byteValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Short.hashCode(key) ^ Byte.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Short.toString(key) + "=" + Byte.toString(value);
		}
	}
}