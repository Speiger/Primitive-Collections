package speiger.src.collections.bytes.maps.abstracts;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

import speiger.src.collections.bytes.collections.ByteIterator;
import speiger.src.collections.bytes.functions.consumer.ByteShortConsumer;
import speiger.src.collections.bytes.functions.function.Byte2ShortFunction;
import speiger.src.collections.bytes.functions.function.ByteShortUnaryOperator;
import speiger.src.collections.bytes.maps.interfaces.Byte2ShortMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2ShortOrderedMap;
import speiger.src.collections.bytes.sets.ByteOrderedSet;
import speiger.src.collections.shorts.collections.ShortOrderedCollection;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
import speiger.src.collections.bytes.sets.AbstractByteSet;
import speiger.src.collections.bytes.sets.ByteSet;
import speiger.src.collections.bytes.utils.maps.Byte2ShortMaps;
import speiger.src.collections.shorts.collections.AbstractShortCollection;
import speiger.src.collections.shorts.collections.ShortCollection;
import speiger.src.collections.shorts.collections.ShortIterator;
import speiger.src.collections.shorts.functions.function.ShortShortUnaryOperator;
import speiger.src.collections.shorts.functions.ShortSupplier;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Base Implementation of a Type Specific Map to reduce boxing/unboxing
 */
public abstract class AbstractByte2ShortMap extends AbstractMap<Byte, Short> implements Byte2ShortMap
{
	protected short defaultReturnValue = (short)-1;
	
	@Override
	public short getDefaultReturnValue() {
		return defaultReturnValue;
	}
	
	@Override
	public AbstractByte2ShortMap setDefaultReturnValue(short v) {
		defaultReturnValue = v;
		return this;
	}
	
	protected ObjectIterable<Byte2ShortMap.Entry> getFastIterable(Byte2ShortMap map) {
		return Byte2ShortMaps.fastIterable(map);
	}
	
	protected ObjectIterator<Byte2ShortMap.Entry> getFastIterator(Byte2ShortMap map) {
		return Byte2ShortMaps.fastIterator(map);
	}
	
	@Override
	public Byte2ShortMap copy() { 
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public Short put(Byte key, Short value) {
		return Short.valueOf(put(key.byteValue(), value.shortValue()));
	}
	
	@Override
	public void addToAll(Byte2ShortMap m) {
		for(Byte2ShortMap.Entry entry : getFastIterable(m))
			addTo(entry.getByteKey(), entry.getShortValue());
	}
	
	@Override
	public void putAll(Byte2ShortMap m) {
		for(ObjectIterator<Byte2ShortMap.Entry> iter = getFastIterator(m);iter.hasNext();) {
			Byte2ShortMap.Entry entry = iter.next();
			put(entry.getByteKey(), entry.getShortValue());
		}
	}
	
	@Override
	public void putAll(Map<? extends Byte, ? extends Short> m)
	{
		if(m instanceof Byte2ShortMap) putAll((Byte2ShortMap)m);
		else super.putAll(m);
	}
	
	@Override
	public void putAll(byte[] keys, short[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);
	}
	
	@Override
	public void putAll(Byte[] keys, Short[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i].byteValue(), values[i].shortValue());		
	}
	
	@Override
	public void putAllIfAbsent(Byte2ShortMap m) {
		for(Byte2ShortMap.Entry entry : getFastIterable(m))
			putIfAbsent(entry.getByteKey(), entry.getShortValue());
	}
	
	
	@Override
	public boolean containsKey(byte key) {
		for(ByteIterator iter = keySet().iterator();iter.hasNext();)
			if(iter.nextByte() == key) return true;
		return false;
	}
	
	@Override
	public boolean containsValue(short value) {
		for(ShortIterator iter = values().iterator();iter.hasNext();)
			if(iter.nextShort() == value) return true;
		return false;
	}
	
	@Override
	public boolean replace(byte key, short oldValue, short newValue) {
		short curValue = get(key);
		if (curValue != oldValue || (curValue == getDefaultReturnValue() && !containsKey(key))) {
			return false;
		}
		put(key, newValue);
		return true;
	}

	@Override
	public short replace(byte key, short value) {
		short curValue;
		if ((curValue = get(key)) != getDefaultReturnValue() || containsKey(key)) {
			curValue = put(key, value);
		}
		return curValue;
	}
	
	@Override
	public void replaceShorts(Byte2ShortMap m) {
		for(Byte2ShortMap.Entry entry : getFastIterable(m))
			replace(entry.getByteKey(), entry.getShortValue());
	}
	
	@Override
	public void replaceShorts(ByteShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(ObjectIterator<Byte2ShortMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Byte2ShortMap.Entry entry = iter.next();
			entry.setValue(mappingFunction.applyAsShort(entry.getByteKey(), entry.getShortValue()));
		}
	}

	@Override
	public short computeShort(byte key, ByteShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		short newValue = mappingFunction.applyAsShort(key, get(key));
		put(key, newValue);
		return newValue;
	}
	
	@Override
	public short computeShortIfAbsent(byte key, Byte2ShortFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(!containsKey(key)) {
			short newValue = mappingFunction.applyAsShort(key);
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public short supplyShortIfAbsent(byte key, ShortSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		if(!containsKey(key)) {
			short newValue = valueProvider.getAsShort();
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public short computeShortIfPresent(byte key, ByteShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(containsKey(key)) {
			short newValue = mappingFunction.applyAsShort(key, get(key));
			put(key, newValue);
			return newValue;
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public short computeShortNonDefault(byte key, ByteShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		short value = get(key);
		short newValue = mappingFunction.applyAsShort(key, value);
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
	public short computeShortIfAbsentNonDefault(byte key, Byte2ShortFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		short value;
		if((value = get(key)) == getDefaultReturnValue() || !containsKey(key)) {
			short newValue = mappingFunction.applyAsShort(key);
			if(newValue != getDefaultReturnValue()) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public short supplyShortIfAbsentNonDefault(byte key, ShortSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		short value;
		if((value = get(key)) == getDefaultReturnValue() || !containsKey(key)) {
			short newValue = valueProvider.getAsShort();
			if(newValue != getDefaultReturnValue()) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public short computeShortIfPresentNonDefault(byte key, ByteShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		short value;
		if((value = get(key)) != getDefaultReturnValue() || containsKey(key)) {
			short newValue = mappingFunction.applyAsShort(key, value);
			if(newValue != getDefaultReturnValue()) {
				put(key, newValue);
				return newValue;
			}
			remove(key);
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public short mergeShort(byte key, short value, ShortShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		short oldValue = get(key);
		short newValue = oldValue == getDefaultReturnValue() ? value : mappingFunction.applyAsShort(oldValue, value);
		if(newValue == getDefaultReturnValue()) remove(key);
		else put(key, newValue);
		return newValue;
	}
	
	@Override
	public void mergeAllShort(Byte2ShortMap m, ShortShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Byte2ShortMap.Entry entry : getFastIterable(m)) {
			byte key = entry.getByteKey();
			short oldValue = get(key);
			short newValue = oldValue == getDefaultReturnValue() ? entry.getShortValue() : mappingFunction.applyAsShort(oldValue, entry.getShortValue());
			if(newValue == getDefaultReturnValue()) remove(key);
			else put(key, newValue);
		}
	}
	
	@Override
	public Short get(Object key) {
		return Short.valueOf(key instanceof Byte ? get(((Byte)key).byteValue()) : getDefaultReturnValue());
	}
	
	@Override
	public Short getOrDefault(Object key, Short defaultValue) {
		return Short.valueOf(key instanceof Byte ? getOrDefault(((Byte)key).byteValue(), defaultValue.shortValue()) : getDefaultReturnValue());
	}
	
	@Override
	public short getOrDefault(byte key, short defaultValue) {
		short value = get(key);
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	
	@Override
	public Short remove(Object key) {
		return key instanceof Byte ? Short.valueOf(remove(((Byte)key).byteValue())) : Short.valueOf(getDefaultReturnValue());
	}
	
	@Override
	public void forEach(ByteShortConsumer action) {
		Objects.requireNonNull(action);
		for(ObjectIterator<Byte2ShortMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Byte2ShortMap.Entry entry = iter.next();
			action.accept(entry.getByteKey(), entry.getShortValue());
		}
	}

	@Override
	public ByteSet keySet() {
		return new AbstractByteSet() {
			@Override
			public boolean remove(byte o) {
				return AbstractByte2ShortMap.this.remove(o) != getDefaultReturnValue();
			}
			
			@Override
			public boolean add(byte o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public ByteIterator iterator() {
				return new ByteIterator() {
					ObjectIterator<Byte2ShortMap.Entry> iter = getFastIterator(AbstractByte2ShortMap.this);
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
				return AbstractByte2ShortMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractByte2ShortMap.this.clear();
			}
		};
	}

	@Override
	public ShortCollection values() {
		return new AbstractShortCollection() {
			@Override
			public boolean add(short o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public int size() {
				return AbstractByte2ShortMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractByte2ShortMap.this.clear();
			}
			
			@Override
			public ShortIterator iterator() {
				return new ShortIterator() {
					ObjectIterator<Byte2ShortMap.Entry> iter = getFastIterator(AbstractByte2ShortMap.this);
					@Override
					public boolean hasNext() {
						return iter.hasNext();
					}
					
					@Override
					public short nextShort() {
						return iter.next().getShortValue();
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
	public ObjectSet<Map.Entry<Byte, Short>> entrySet() {
		return (ObjectSet)byte2ShortEntrySet();
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o instanceof Map) {
			if(size() != ((Map<?, ?>)o).size()) return false;
			if(o instanceof Byte2ShortMap) return byte2ShortEntrySet().containsAll(((Byte2ShortMap)o).byte2ShortEntrySet());
			return byte2ShortEntrySet().containsAll(((Map<?, ?>)o).entrySet());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		ObjectIterator<Byte2ShortMap.Entry> iter = getFastIterator(this);
		while(iter.hasNext()) hash += iter.next().hashCode();
		return hash;
	}
	
	public static class ReversedByte2ShortOrderedMap extends AbstractByte2ShortMap implements Byte2ShortOrderedMap {
		Byte2ShortOrderedMap map;
		
		public ReversedByte2ShortOrderedMap(Byte2ShortOrderedMap map) {
			this.map = map;
		}
		@Override
		public AbstractByte2ShortMap setDefaultReturnValue(short v) {
			map.setDefaultReturnValue(v);
			return this;
		}
		@Override
		public short getDefaultReturnValue() { return map.getDefaultReturnValue(); }
		@Override
		public Byte2ShortOrderedMap copy() { throw new UnsupportedOperationException(); }
		@Override
		public short put(byte key, short value) { return map.put(key, value); }
		@Override
		public short putIfAbsent(byte key, short value) { return map.putIfAbsent(key, value); }
		@Override
		public short addTo(byte key, short value) { return map.addTo(key, value); }
		@Override
		public short subFrom(byte key, short value) { return map.subFrom(key, value); }
		@Override
		public short remove(byte key) { return map.remove(key); }
		@Override
		public boolean remove(byte key, short value) { return map.remove(key, value); }
		@Override
		public short removeOrDefault(byte key, short defaultValue) { return map.removeOrDefault(key, defaultValue); }
		@Override
		public boolean containsKey(byte key) { return map.containsKey(key); }
		@Override
		public boolean containsValue(short value) { return map.containsValue(value); }
		@Override
		public boolean replace(byte key, short oldValue, short newValue) { return map.replace(key, oldValue, newValue); }
		@Override
		public short replace(byte key, short value) { return map.replace(key, value); }
		@Override
		public void replaceShorts(Byte2ShortMap m) { map.replaceShorts(m); }
		@Override
		public void replaceShorts(ByteShortUnaryOperator mappingFunction) { map.replaceShorts(mappingFunction); }
		@Override
		public short computeShort(byte key, ByteShortUnaryOperator mappingFunction) { return map.computeShort(key, mappingFunction); }
		@Override
		public short computeShortIfAbsent(byte key, Byte2ShortFunction mappingFunction) { return map.computeShortIfAbsent(key, mappingFunction); }
		@Override
		public short supplyShortIfAbsent(byte key, ShortSupplier valueProvider) { return map.supplyShortIfAbsent(key, valueProvider); }
		@Override
		public short computeShortIfPresent(byte key, ByteShortUnaryOperator mappingFunction) { return map.computeShortIfPresent(key, mappingFunction); }
		@Override
		public short computeShortNonDefault(byte key, ByteShortUnaryOperator mappingFunction) { return map.computeShortNonDefault(key, mappingFunction); }
		@Override
		public short computeShortIfAbsentNonDefault(byte key, Byte2ShortFunction mappingFunction) { return map.computeShortIfAbsentNonDefault(key, mappingFunction); }
		@Override
		public short supplyShortIfAbsentNonDefault(byte key, ShortSupplier valueProvider) { return map.supplyShortIfAbsentNonDefault(key, valueProvider); }
		@Override
		public short computeShortIfPresentNonDefault(byte key, ByteShortUnaryOperator mappingFunction) { return map.computeShortIfPresentNonDefault(key, mappingFunction); }
		@Override
		public short mergeShort(byte key, short value, ShortShortUnaryOperator mappingFunction) { return map.mergeShort(key, value, mappingFunction); }
		@Override
		public short getOrDefault(byte key, short defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public short get(byte key) { return map.get(key); }
		@Override
		public short putAndMoveToFirst(byte key, short value) { return map.putAndMoveToLast(key, value); }
		@Override
		public short putAndMoveToLast(byte key, short value) { return map.putAndMoveToFirst(key, value); }
		@Override
		public short putFirst(byte key, short value) { return map.putLast(key, value); }
		@Override
		public short putLast(byte key, short value) { return map.putFirst(key, value); }
		@Override
		public boolean moveToFirst(byte key) { return map.moveToLast(key); }
		@Override
		public boolean moveToLast(byte key) { return map.moveToFirst(key); }
		@Override
		public short getAndMoveToFirst(byte key) { return map.getAndMoveToLast(key); }
		@Override
		public short getAndMoveToLast(byte key) { return map.getAndMoveToFirst(key); }
		@Override
		public byte firstByteKey() { return map.lastByteKey(); }
		@Override
		public byte pollFirstByteKey() { return map.pollLastByteKey(); }
		@Override
		public byte lastByteKey() { return map.firstByteKey(); }
		@Override
		public byte pollLastByteKey() { return map.pollFirstByteKey(); }
		@Override
		public short firstShortValue() { return map.lastShortValue(); }
		@Override
		public short lastShortValue() { return map.firstShortValue(); }
		@Override
		public Byte2ShortMap.Entry firstEntry() { return map.lastEntry(); }
		@Override
		public Byte2ShortMap.Entry lastEntry() { return map.firstEntry(); }
		@Override
		public Byte2ShortMap.Entry pollFirstEntry() { return map.pollLastEntry(); }
		@Override
		public Byte2ShortMap.Entry pollLastEntry() { return map.pollFirstEntry(); }
		@Override
		public ObjectOrderedSet<Byte2ShortMap.Entry> byte2ShortEntrySet() { return new AbstractObjectSet.ReversedObjectOrderedSet<>(map.byte2ShortEntrySet()); }
		@Override
		public ByteOrderedSet keySet() { return new AbstractByteSet.ReversedByteOrderedSet(map.keySet()); }
		@Override
		public ShortOrderedCollection values() { return map.values().reversed(); }
		@Override
		public Byte2ShortOrderedMap reversed() { return map; }
	}

	
	/**
	 * A Simple Type Specific Entry class to reduce boxing/unboxing
	 */
	public static class BasicEntry implements Byte2ShortMap.Entry {
		protected byte key;
		protected short value;
		
		/**
		 * A basic Empty constructor
		 */
		public BasicEntry() {}
		/**
		 * A Boxed Constructor for supporting java variants
		 * @param key the key of a entry
		 * @param value the value of a entry
		 */
		public BasicEntry(Byte key, Short value) {
			this.key = key.byteValue();
			this.value = value.shortValue();
		}
		
		/**
		 * A Type Specific Constructor
		 * @param key the key of a entry
		 * @param value the value of a entry 
		 */
		public BasicEntry(byte key, short value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * A Helper method for fast replacing values
		 * @param key the key that should be replaced
		 * @param value the value that should be replaced
		 */
		public void set(byte key, short value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public byte getByteKey() {
			return key;
		}

		@Override
		public short getShortValue() {
			return value;
		}

		@Override
		public short setValue(short value) {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Map.Entry) {
				if(obj instanceof Byte2ShortMap.Entry) {
					Byte2ShortMap.Entry entry = (Byte2ShortMap.Entry)obj;
					return key == entry.getByteKey() && value == entry.getShortValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Byte && value instanceof Short && this.key == ((Byte)key).byteValue() && this.value == ((Short)value).shortValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Byte.hashCode(key) ^ Short.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Byte.toString(key) + "=" + Short.toString(value);
		}
	}
}