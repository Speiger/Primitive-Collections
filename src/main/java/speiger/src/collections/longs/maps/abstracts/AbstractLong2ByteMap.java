package speiger.src.collections.longs.maps.abstracts;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

import speiger.src.collections.longs.collections.LongIterator;
import speiger.src.collections.longs.functions.consumer.LongByteConsumer;
import speiger.src.collections.longs.functions.function.Long2ByteFunction;
import speiger.src.collections.longs.functions.function.LongByteUnaryOperator;
import speiger.src.collections.longs.maps.interfaces.Long2ByteMap;
import speiger.src.collections.longs.sets.AbstractLongSet;
import speiger.src.collections.longs.sets.LongSet;
import speiger.src.collections.longs.utils.maps.Long2ByteMaps;
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
public abstract class AbstractLong2ByteMap extends AbstractMap<Long, Byte> implements Long2ByteMap
{
	protected byte defaultReturnValue = (byte)0;
	
	@Override
	public byte getDefaultReturnValue() {
		return defaultReturnValue;
	}
	
	@Override
	public AbstractLong2ByteMap setDefaultReturnValue(byte v) {
		defaultReturnValue = v;
		return this;
	}
	
	@Override
	public Long2ByteMap copy() { 
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public Byte put(Long key, Byte value) {
		return Byte.valueOf(put(key.longValue(), value.byteValue()));
	}
	
	@Override
	public void addToAll(Long2ByteMap m) {
		for(Long2ByteMap.Entry entry : Long2ByteMaps.fastIterable(m))
			addTo(entry.getLongKey(), entry.getByteValue());
	}
	
	@Override
	public void putAll(Long2ByteMap m) {
		for(ObjectIterator<Long2ByteMap.Entry> iter = Long2ByteMaps.fastIterator(m);iter.hasNext();) {
			Long2ByteMap.Entry entry = iter.next();
			put(entry.getLongKey(), entry.getByteValue());
		}
	}
	
	@Override
	public void putAll(Map<? extends Long, ? extends Byte> m)
	{
		if(m instanceof Long2ByteMap) putAll((Long2ByteMap)m);
		else super.putAll(m);
	}
	
	@Override
	public void putAll(long[] keys, byte[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);
	}
	
	@Override
	public void putAll(Long[] keys, Byte[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);		
	}
	
	@Override
	public void putAllIfAbsent(Long2ByteMap m) {
		for(Long2ByteMap.Entry entry : Long2ByteMaps.fastIterable(m))
			putIfAbsent(entry.getLongKey(), entry.getByteValue());
	}
	
	
	@Override
	public boolean containsKey(long key) {
		for(LongIterator iter = keySet().iterator();iter.hasNext();)
			if(iter.nextLong() == key) return true;
		return false;
	}
	
	@Override
	public boolean containsValue(byte value) {
		for(ByteIterator iter = values().iterator();iter.hasNext();)
			if(iter.nextByte() == value) return true;
		return false;
	}
	
	@Override
	public boolean replace(long key, byte oldValue, byte newValue) {
		byte curValue = get(key);
		if (curValue != oldValue || (curValue == getDefaultReturnValue() && !containsKey(key))) {
			return false;
		}
		put(key, newValue);
		return true;
	}

	@Override
	public byte replace(long key, byte value) {
		byte curValue;
		if ((curValue = get(key)) != getDefaultReturnValue() || containsKey(key)) {
			curValue = put(key, value);
		}
		return curValue;
	}
	
	@Override
	public void replaceBytes(Long2ByteMap m) {
		for(Long2ByteMap.Entry entry : Long2ByteMaps.fastIterable(m))
			replace(entry.getLongKey(), entry.getByteValue());
	}
	
	@Override
	public void replaceBytes(LongByteUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(ObjectIterator<Long2ByteMap.Entry> iter = Long2ByteMaps.fastIterator(this);iter.hasNext();) {
			Long2ByteMap.Entry entry = iter.next();
			entry.setValue(mappingFunction.applyAsByte(entry.getLongKey(), entry.getByteValue()));
		}
	}

	@Override
	public byte computeByte(long key, LongByteUnaryOperator mappingFunction) {
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
	public byte computeByteIfAbsent(long key, Long2ByteFunction mappingFunction) {
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
	public byte supplyByteIfAbsent(long key, ByteSupplier valueProvider) {
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
	public byte computeByteIfPresent(long key, LongByteUnaryOperator mappingFunction) {
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
	public byte mergeByte(long key, byte value, ByteByteUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		byte oldValue = get(key);
		byte newValue = oldValue == getDefaultReturnValue() ? value : mappingFunction.applyAsByte(oldValue, value);
		if(newValue == getDefaultReturnValue()) remove(key);
		else put(key, newValue);
		return newValue;
	}
	
	@Override
	public void mergeAllByte(Long2ByteMap m, ByteByteUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Long2ByteMap.Entry entry : Long2ByteMaps.fastIterable(m)) {
			long key = entry.getLongKey();
			byte oldValue = get(key);
			byte newValue = oldValue == getDefaultReturnValue() ? entry.getByteValue() : mappingFunction.applyAsByte(oldValue, entry.getByteValue());
			if(newValue == getDefaultReturnValue()) remove(key);
			else put(key, newValue);
		}
	}
	
	@Override
	public Byte get(Object key) {
		return Byte.valueOf(key instanceof Long ? get(((Long)key).longValue()) : getDefaultReturnValue());
	}
	
	@Override
	public Byte getOrDefault(Object key, Byte defaultValue) {
		return Byte.valueOf(key instanceof Long ? getOrDefault(((Long)key).longValue(), defaultValue.byteValue()) : getDefaultReturnValue());
	}
	
	@Override
	public byte getOrDefault(long key, byte defaultValue) {
		byte value = get(key);
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	@Override
	public void forEach(LongByteConsumer action) {
		Objects.requireNonNull(action);
		for(ObjectIterator<Long2ByteMap.Entry> iter = Long2ByteMaps.fastIterator(this);iter.hasNext();) {
			Long2ByteMap.Entry entry = iter.next();
			action.accept(entry.getLongKey(), entry.getByteValue());
		}
	}

	@Override
	public LongSet keySet() {
		return new AbstractLongSet() {
			@Override
			public boolean remove(long o) {
				return AbstractLong2ByteMap.this.remove(o) != getDefaultReturnValue();
			}
			
			@Override
			public boolean add(long o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public LongIterator iterator() {
				return new LongIterator() {
					ObjectIterator<Long2ByteMap.Entry> iter = Long2ByteMaps.fastIterator(AbstractLong2ByteMap.this);
					@Override
					public boolean hasNext() {
						return iter.hasNext();
					}

					@Override
					public long nextLong() {
						return iter.next().getLongKey();
					}
					
					@Override
					public void remove() {
						iter.remove();
					}
				};
			}
			
			@Override
			public int size() {
				return AbstractLong2ByteMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractLong2ByteMap.this.clear();
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
				return AbstractLong2ByteMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractLong2ByteMap.this.clear();
			}
			
			@Override
			public ByteIterator iterator() {
				return new ByteIterator() {
					ObjectIterator<Long2ByteMap.Entry> iter = Long2ByteMaps.fastIterator(AbstractLong2ByteMap.this);
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
	public ObjectSet<Map.Entry<Long, Byte>> entrySet() {
		return (ObjectSet)long2ByteEntrySet();
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o instanceof Map) {
			if(size() != ((Map<?, ?>)o).size()) return false;
			if(o instanceof Long2ByteMap) return long2ByteEntrySet().containsAll(((Long2ByteMap)o).long2ByteEntrySet());
			return long2ByteEntrySet().containsAll(((Map<?, ?>)o).entrySet());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		ObjectIterator<Long2ByteMap.Entry> iter = Long2ByteMaps.fastIterator(this);
		while(iter.hasNext()) hash += iter.next().hashCode();
		return hash;
	}
	
	/**
	 * A Simple Type Specific Entry class to reduce boxing/unboxing
	 */
	public static class BasicEntry implements Long2ByteMap.Entry {
		protected long key;
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
		public BasicEntry(Long key, Byte value) {
			this.key = key.longValue();
			this.value = value.byteValue();
		}
		
		/**
		 * A Type Specific Constructor
		 * @param key the key of a entry
		 * @param value the value of a entry 
		 */
		public BasicEntry(long key, byte value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * A Helper method for fast replacing values
		 * @param key the key that should be replaced
		 * @param value the value that should be replaced
		 */
		public void set(long key, byte value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public long getLongKey() {
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
				if(obj instanceof Long2ByteMap.Entry) {
					Long2ByteMap.Entry entry = (Long2ByteMap.Entry)obj;
					return key == entry.getLongKey() && value == entry.getByteValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Long && value instanceof Byte && this.key == ((Long)key).longValue() && this.value == ((Byte)value).byteValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Long.hashCode(key) ^ Byte.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Long.toString(key) + "=" + Byte.toString(value);
		}
	}
}