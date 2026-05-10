package speiger.src.collections.bytes.maps.abstracts;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

import speiger.src.collections.bytes.collections.ByteIterator;
import speiger.src.collections.bytes.functions.consumer.ByteIntConsumer;
import speiger.src.collections.bytes.functions.function.Byte2IntFunction;
import speiger.src.collections.bytes.functions.function.ByteIntUnaryOperator;
import speiger.src.collections.bytes.maps.interfaces.Byte2IntMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2IntOrderedMap;
import speiger.src.collections.bytes.sets.ByteOrderedSet;
import speiger.src.collections.ints.collections.IntOrderedCollection;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
import speiger.src.collections.bytes.sets.AbstractByteSet;
import speiger.src.collections.bytes.sets.ByteSet;
import speiger.src.collections.bytes.utils.maps.Byte2IntMaps;
import speiger.src.collections.ints.collections.AbstractIntCollection;
import speiger.src.collections.ints.collections.IntCollection;
import speiger.src.collections.ints.collections.IntIterator;
import speiger.src.collections.ints.functions.function.IntIntUnaryOperator;
import speiger.src.collections.ints.functions.IntSupplier;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Base Implementation of a Type Specific Map to reduce boxing/unboxing
 */
public abstract class AbstractByte2IntMap extends AbstractMap<Byte, Integer> implements Byte2IntMap
{
	protected int defaultReturnValue = -1;
	
	@Override
	public int getDefaultReturnValue() {
		return defaultReturnValue;
	}
	
	@Override
	public AbstractByte2IntMap setDefaultReturnValue(int v) {
		defaultReturnValue = v;
		return this;
	}
	
	protected ObjectIterable<Byte2IntMap.Entry> getFastIterable(Byte2IntMap map) {
		return Byte2IntMaps.fastIterable(map);
	}
	
	protected ObjectIterator<Byte2IntMap.Entry> getFastIterator(Byte2IntMap map) {
		return Byte2IntMaps.fastIterator(map);
	}
	
	@Override
	public Byte2IntMap copy() { 
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public Integer put(Byte key, Integer value) {
		return Integer.valueOf(put(key.byteValue(), value.intValue()));
	}
	
	@Override
	public void addToAll(Byte2IntMap m) {
		for(Byte2IntMap.Entry entry : getFastIterable(m))
			addTo(entry.getByteKey(), entry.getIntValue());
	}
	
	@Override
	public void putAll(Byte2IntMap m) {
		for(ObjectIterator<Byte2IntMap.Entry> iter = getFastIterator(m);iter.hasNext();) {
			Byte2IntMap.Entry entry = iter.next();
			put(entry.getByteKey(), entry.getIntValue());
		}
	}
	
	@Override
	public void putAll(Map<? extends Byte, ? extends Integer> m)
	{
		if(m instanceof Byte2IntMap) putAll((Byte2IntMap)m);
		else super.putAll(m);
	}
	
	@Override
	public void putAll(byte[] keys, int[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);
	}
	
	@Override
	public void putAll(Byte[] keys, Integer[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i].byteValue(), values[i].intValue());		
	}
	
	@Override
	public void putAllIfAbsent(Byte2IntMap m) {
		for(Byte2IntMap.Entry entry : getFastIterable(m))
			putIfAbsent(entry.getByteKey(), entry.getIntValue());
	}
	
	
	@Override
	public boolean containsKey(byte key) {
		for(ByteIterator iter = keySet().iterator();iter.hasNext();)
			if(iter.nextByte() == key) return true;
		return false;
	}
	
	@Override
	public boolean containsValue(int value) {
		for(IntIterator iter = values().iterator();iter.hasNext();)
			if(iter.nextInt() == value) return true;
		return false;
	}
	
	@Override
	public boolean replace(byte key, int oldValue, int newValue) {
		int curValue = get(key);
		if (curValue != oldValue || (curValue == getDefaultReturnValue() && !containsKey(key))) {
			return false;
		}
		put(key, newValue);
		return true;
	}

	@Override
	public int replace(byte key, int value) {
		int curValue;
		if ((curValue = get(key)) != getDefaultReturnValue() || containsKey(key)) {
			curValue = put(key, value);
		}
		return curValue;
	}
	
	@Override
	public void replaceInts(Byte2IntMap m) {
		for(Byte2IntMap.Entry entry : getFastIterable(m))
			replace(entry.getByteKey(), entry.getIntValue());
	}
	
	@Override
	public void replaceInts(ByteIntUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(ObjectIterator<Byte2IntMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Byte2IntMap.Entry entry = iter.next();
			entry.setValue(mappingFunction.applyAsInt(entry.getByteKey(), entry.getIntValue()));
		}
	}

	@Override
	public int computeInt(byte key, ByteIntUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int newValue = mappingFunction.applyAsInt(key, get(key));
		put(key, newValue);
		return newValue;
	}
	
	@Override
	public int computeIntIfAbsent(byte key, Byte2IntFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(!containsKey(key)) {
			int newValue = mappingFunction.applyAsInt(key);
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public int supplyIntIfAbsent(byte key, IntSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		if(!containsKey(key)) {
			int newValue = valueProvider.getAsInt();
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public int computeIntIfPresent(byte key, ByteIntUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(containsKey(key)) {
			int newValue = mappingFunction.applyAsInt(key, get(key));
			put(key, newValue);
			return newValue;
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public int computeIntNonDefault(byte key, ByteIntUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int value = get(key);
		int newValue = mappingFunction.applyAsInt(key, value);
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
	public int computeIntIfAbsentNonDefault(byte key, Byte2IntFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int value;
		if((value = get(key)) == getDefaultReturnValue() || !containsKey(key)) {
			int newValue = mappingFunction.applyAsInt(key);
			if(newValue != getDefaultReturnValue()) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public int supplyIntIfAbsentNonDefault(byte key, IntSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		int value;
		if((value = get(key)) == getDefaultReturnValue() || !containsKey(key)) {
			int newValue = valueProvider.getAsInt();
			if(newValue != getDefaultReturnValue()) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public int computeIntIfPresentNonDefault(byte key, ByteIntUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int value;
		if((value = get(key)) != getDefaultReturnValue() || containsKey(key)) {
			int newValue = mappingFunction.applyAsInt(key, value);
			if(newValue != getDefaultReturnValue()) {
				put(key, newValue);
				return newValue;
			}
			remove(key);
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public int mergeInt(byte key, int value, IntIntUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int oldValue = get(key);
		int newValue = oldValue == getDefaultReturnValue() ? value : mappingFunction.applyAsInt(oldValue, value);
		if(newValue == getDefaultReturnValue()) remove(key);
		else put(key, newValue);
		return newValue;
	}
	
	@Override
	public void mergeAllInt(Byte2IntMap m, IntIntUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Byte2IntMap.Entry entry : getFastIterable(m)) {
			byte key = entry.getByteKey();
			int oldValue = get(key);
			int newValue = oldValue == getDefaultReturnValue() ? entry.getIntValue() : mappingFunction.applyAsInt(oldValue, entry.getIntValue());
			if(newValue == getDefaultReturnValue()) remove(key);
			else put(key, newValue);
		}
	}
	
	@Override
	public Integer get(Object key) {
		return Integer.valueOf(key instanceof Byte ? get(((Byte)key).byteValue()) : getDefaultReturnValue());
	}
	
	@Override
	public Integer getOrDefault(Object key, Integer defaultValue) {
		return Integer.valueOf(key instanceof Byte ? getOrDefault(((Byte)key).byteValue(), defaultValue.intValue()) : getDefaultReturnValue());
	}
	
	@Override
	public int getOrDefault(byte key, int defaultValue) {
		int value = get(key);
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	
	@Override
	public Integer remove(Object key) {
		return key instanceof Byte ? Integer.valueOf(remove(((Byte)key).byteValue())) : Integer.valueOf(getDefaultReturnValue());
	}
	
	@Override
	public void forEach(ByteIntConsumer action) {
		Objects.requireNonNull(action);
		for(ObjectIterator<Byte2IntMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Byte2IntMap.Entry entry = iter.next();
			action.accept(entry.getByteKey(), entry.getIntValue());
		}
	}

	@Override
	public ByteSet keySet() {
		return new AbstractByteSet() {
			@Override
			public boolean remove(byte o) {
				return AbstractByte2IntMap.this.remove(o) != getDefaultReturnValue();
			}
			
			@Override
			public boolean add(byte o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public ByteIterator iterator() {
				return new ByteIterator() {
					ObjectIterator<Byte2IntMap.Entry> iter = getFastIterator(AbstractByte2IntMap.this);
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
				return AbstractByte2IntMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractByte2IntMap.this.clear();
			}
		};
	}

	@Override
	public IntCollection values() {
		return new AbstractIntCollection() {
			@Override
			public boolean add(int o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public int size() {
				return AbstractByte2IntMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractByte2IntMap.this.clear();
			}
			
			@Override
			public IntIterator iterator() {
				return new IntIterator() {
					ObjectIterator<Byte2IntMap.Entry> iter = getFastIterator(AbstractByte2IntMap.this);
					@Override
					public boolean hasNext() {
						return iter.hasNext();
					}
					
					@Override
					public int nextInt() {
						return iter.next().getIntValue();
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
	public ObjectSet<Map.Entry<Byte, Integer>> entrySet() {
		return (ObjectSet)byte2IntEntrySet();
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o instanceof Map) {
			if(size() != ((Map<?, ?>)o).size()) return false;
			if(o instanceof Byte2IntMap) return byte2IntEntrySet().containsAll(((Byte2IntMap)o).byte2IntEntrySet());
			return byte2IntEntrySet().containsAll(((Map<?, ?>)o).entrySet());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		ObjectIterator<Byte2IntMap.Entry> iter = getFastIterator(this);
		while(iter.hasNext()) hash += iter.next().hashCode();
		return hash;
	}
	
	public static class ReversedByte2IntOrderedMap extends AbstractByte2IntMap implements Byte2IntOrderedMap {
		Byte2IntOrderedMap map;
		
		public ReversedByte2IntOrderedMap(Byte2IntOrderedMap map) {
			this.map = map;
		}
		@Override
		public AbstractByte2IntMap setDefaultReturnValue(int v) {
			map.setDefaultReturnValue(v);
			return this;
		}
		@Override
		public int getDefaultReturnValue() { return map.getDefaultReturnValue(); }
		@Override
		public Byte2IntOrderedMap copy() { throw new UnsupportedOperationException(); }
		@Override
		public int put(byte key, int value) { return map.put(key, value); }
		@Override
		public int putIfAbsent(byte key, int value) { return map.putIfAbsent(key, value); }
		@Override
		public int addTo(byte key, int value) { return map.addTo(key, value); }
		@Override
		public int subFrom(byte key, int value) { return map.subFrom(key, value); }
		@Override
		public int remove(byte key) { return map.remove(key); }
		@Override
		public boolean remove(byte key, int value) { return map.remove(key, value); }
		@Override
		public int removeOrDefault(byte key, int defaultValue) { return map.removeOrDefault(key, defaultValue); }
		@Override
		public boolean containsKey(byte key) { return map.containsKey(key); }
		@Override
		public boolean containsValue(int value) { return map.containsValue(value); }
		@Override
		public boolean replace(byte key, int oldValue, int newValue) { return map.replace(key, oldValue, newValue); }
		@Override
		public int replace(byte key, int value) { return map.replace(key, value); }
		@Override
		public void replaceInts(Byte2IntMap m) { map.replaceInts(m); }
		@Override
		public void replaceInts(ByteIntUnaryOperator mappingFunction) { map.replaceInts(mappingFunction); }
		@Override
		public int computeInt(byte key, ByteIntUnaryOperator mappingFunction) { return map.computeInt(key, mappingFunction); }
		@Override
		public int computeIntIfAbsent(byte key, Byte2IntFunction mappingFunction) { return map.computeIntIfAbsent(key, mappingFunction); }
		@Override
		public int supplyIntIfAbsent(byte key, IntSupplier valueProvider) { return map.supplyIntIfAbsent(key, valueProvider); }
		@Override
		public int computeIntIfPresent(byte key, ByteIntUnaryOperator mappingFunction) { return map.computeIntIfPresent(key, mappingFunction); }
		@Override
		public int computeIntNonDefault(byte key, ByteIntUnaryOperator mappingFunction) { return map.computeIntNonDefault(key, mappingFunction); }
		@Override
		public int computeIntIfAbsentNonDefault(byte key, Byte2IntFunction mappingFunction) { return map.computeIntIfAbsentNonDefault(key, mappingFunction); }
		@Override
		public int supplyIntIfAbsentNonDefault(byte key, IntSupplier valueProvider) { return map.supplyIntIfAbsentNonDefault(key, valueProvider); }
		@Override
		public int computeIntIfPresentNonDefault(byte key, ByteIntUnaryOperator mappingFunction) { return map.computeIntIfPresentNonDefault(key, mappingFunction); }
		@Override
		public int mergeInt(byte key, int value, IntIntUnaryOperator mappingFunction) { return map.mergeInt(key, value, mappingFunction); }
		@Override
		public int getOrDefault(byte key, int defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public int get(byte key) { return map.get(key); }
		@Override
		public int putAndMoveToFirst(byte key, int value) { return map.putAndMoveToLast(key, value); }
		@Override
		public int putAndMoveToLast(byte key, int value) { return map.putAndMoveToFirst(key, value); }
		@Override
		public int putFirst(byte key, int value) { return map.putLast(key, value); }
		@Override
		public int putLast(byte key, int value) { return map.putFirst(key, value); }
		@Override
		public boolean moveToFirst(byte key) { return map.moveToLast(key); }
		@Override
		public boolean moveToLast(byte key) { return map.moveToFirst(key); }
		@Override
		public int getAndMoveToFirst(byte key) { return map.getAndMoveToLast(key); }
		@Override
		public int getAndMoveToLast(byte key) { return map.getAndMoveToFirst(key); }
		@Override
		public byte firstByteKey() { return map.lastByteKey(); }
		@Override
		public byte pollFirstByteKey() { return map.pollLastByteKey(); }
		@Override
		public byte lastByteKey() { return map.firstByteKey(); }
		@Override
		public byte pollLastByteKey() { return map.pollFirstByteKey(); }
		@Override
		public int firstIntValue() { return map.lastIntValue(); }
		@Override
		public int lastIntValue() { return map.firstIntValue(); }
		@Override
		public Byte2IntMap.Entry firstEntry() { return map.lastEntry(); }
		@Override
		public Byte2IntMap.Entry lastEntry() { return map.firstEntry(); }
		@Override
		public Byte2IntMap.Entry pollFirstEntry() { return map.pollLastEntry(); }
		@Override
		public Byte2IntMap.Entry pollLastEntry() { return map.pollFirstEntry(); }
		@Override
		public ObjectOrderedSet<Byte2IntMap.Entry> byte2IntEntrySet() { return new AbstractObjectSet.ReversedObjectOrderedSet<>(map.byte2IntEntrySet()); }
		@Override
		public ByteOrderedSet keySet() { return new AbstractByteSet.ReversedByteOrderedSet(map.keySet()); }
		@Override
		public IntOrderedCollection values() { return map.values().reversed(); }
		@Override
		public Byte2IntOrderedMap reversed() { return map; }
	}

	
	/**
	 * A Simple Type Specific Entry class to reduce boxing/unboxing
	 */
	public static class BasicEntry implements Byte2IntMap.Entry {
		protected byte key;
		protected int value;
		
		/**
		 * A basic Empty constructor
		 */
		public BasicEntry() {}
		/**
		 * A Boxed Constructor for supporting java variants
		 * @param key the key of a entry
		 * @param value the value of a entry
		 */
		public BasicEntry(Byte key, Integer value) {
			this.key = key.byteValue();
			this.value = value.intValue();
		}
		
		/**
		 * A Type Specific Constructor
		 * @param key the key of a entry
		 * @param value the value of a entry 
		 */
		public BasicEntry(byte key, int value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * A Helper method for fast replacing values
		 * @param key the key that should be replaced
		 * @param value the value that should be replaced
		 */
		public void set(byte key, int value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public byte getByteKey() {
			return key;
		}

		@Override
		public int getIntValue() {
			return value;
		}

		@Override
		public int setValue(int value) {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Map.Entry) {
				if(obj instanceof Byte2IntMap.Entry) {
					Byte2IntMap.Entry entry = (Byte2IntMap.Entry)obj;
					return key == entry.getByteKey() && value == entry.getIntValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Byte && value instanceof Integer && this.key == ((Byte)key).byteValue() && this.value == ((Integer)value).intValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Byte.hashCode(key) ^ Integer.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Byte.toString(key) + "=" + Integer.toString(value);
		}
	}
}