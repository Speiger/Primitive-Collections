package speiger.src.collections.ints.maps.abstracts;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

import speiger.src.collections.ints.collections.IntIterator;
import speiger.src.collections.ints.functions.consumer.IntLongConsumer;
import speiger.src.collections.ints.functions.function.Int2LongFunction;
import speiger.src.collections.ints.functions.function.IntLongUnaryOperator;
import speiger.src.collections.ints.maps.interfaces.Int2LongMap;
import speiger.src.collections.ints.maps.interfaces.Int2LongOrderedMap;
import speiger.src.collections.ints.sets.IntOrderedSet;
import speiger.src.collections.longs.collections.LongOrderedCollection;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
import speiger.src.collections.ints.sets.AbstractIntSet;
import speiger.src.collections.ints.sets.IntSet;
import speiger.src.collections.ints.utils.maps.Int2LongMaps;
import speiger.src.collections.longs.collections.AbstractLongCollection;
import speiger.src.collections.longs.collections.LongCollection;
import speiger.src.collections.longs.collections.LongIterator;
import speiger.src.collections.longs.functions.function.LongLongUnaryOperator;
import speiger.src.collections.longs.functions.LongSupplier;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Base Implementation of a Type Specific Map to reduce boxing/unboxing
 */
public abstract class AbstractInt2LongMap extends AbstractMap<Integer, Long> implements Int2LongMap
{
	protected long defaultReturnValue = -1L;
	
	@Override
	public long getDefaultReturnValue() {
		return defaultReturnValue;
	}
	
	@Override
	public AbstractInt2LongMap setDefaultReturnValue(long v) {
		defaultReturnValue = v;
		return this;
	}
	
	protected ObjectIterable<Int2LongMap.Entry> getFastIterable(Int2LongMap map) {
		return Int2LongMaps.fastIterable(map);
	}
	
	protected ObjectIterator<Int2LongMap.Entry> getFastIterator(Int2LongMap map) {
		return Int2LongMaps.fastIterator(map);
	}
	
	@Override
	public Int2LongMap copy() { 
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public Long put(Integer key, Long value) {
		return Long.valueOf(put(key.intValue(), value.longValue()));
	}
	
	@Override
	public void addToAll(Int2LongMap m) {
		for(Int2LongMap.Entry entry : getFastIterable(m))
			addTo(entry.getIntKey(), entry.getLongValue());
	}
	
	@Override
	public void putAll(Int2LongMap m) {
		for(ObjectIterator<Int2LongMap.Entry> iter = getFastIterator(m);iter.hasNext();) {
			Int2LongMap.Entry entry = iter.next();
			put(entry.getIntKey(), entry.getLongValue());
		}
	}
	
	@Override
	public void putAll(Map<? extends Integer, ? extends Long> m)
	{
		if(m instanceof Int2LongMap) putAll((Int2LongMap)m);
		else super.putAll(m);
	}
	
	@Override
	public void putAll(int[] keys, long[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);
	}
	
	@Override
	public void putAll(Integer[] keys, Long[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i].intValue(), values[i].longValue());		
	}
	
	@Override
	public void putAllIfAbsent(Int2LongMap m) {
		for(Int2LongMap.Entry entry : getFastIterable(m))
			putIfAbsent(entry.getIntKey(), entry.getLongValue());
	}
	
	
	@Override
	public boolean containsKey(int key) {
		for(IntIterator iter = keySet().iterator();iter.hasNext();)
			if(iter.nextInt() == key) return true;
		return false;
	}
	
	@Override
	public boolean containsValue(long value) {
		for(LongIterator iter = values().iterator();iter.hasNext();)
			if(iter.nextLong() == value) return true;
		return false;
	}
	
	@Override
	public boolean replace(int key, long oldValue, long newValue) {
		long curValue = get(key);
		if (curValue != oldValue || (curValue == getDefaultReturnValue() && !containsKey(key))) {
			return false;
		}
		put(key, newValue);
		return true;
	}

	@Override
	public long replace(int key, long value) {
		long curValue;
		if ((curValue = get(key)) != getDefaultReturnValue() || containsKey(key)) {
			curValue = put(key, value);
		}
		return curValue;
	}
	
	@Override
	public void replaceLongs(Int2LongMap m) {
		for(Int2LongMap.Entry entry : getFastIterable(m))
			replace(entry.getIntKey(), entry.getLongValue());
	}
	
	@Override
	public void replaceLongs(IntLongUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(ObjectIterator<Int2LongMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Int2LongMap.Entry entry = iter.next();
			entry.setValue(mappingFunction.applyAsLong(entry.getIntKey(), entry.getLongValue()));
		}
	}

	@Override
	public long computeLong(int key, IntLongUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		long newValue = mappingFunction.applyAsLong(key, get(key));
		put(key, newValue);
		return newValue;
	}
	
	@Override
	public long computeLongIfAbsent(int key, Int2LongFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(!containsKey(key)) {
			long newValue = mappingFunction.applyAsLong(key);
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public long supplyLongIfAbsent(int key, LongSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		if(!containsKey(key)) {
			long newValue = valueProvider.getAsLong();
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public long computeLongIfPresent(int key, IntLongUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(containsKey(key)) {
			long newValue = mappingFunction.applyAsLong(key, get(key));
			put(key, newValue);
			return newValue;
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public long computeLongNonDefault(int key, IntLongUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		long value = get(key);
		long newValue = mappingFunction.applyAsLong(key, value);
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
	public long computeLongIfAbsentNonDefault(int key, Int2LongFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		long value;
		if((value = get(key)) == getDefaultReturnValue() || !containsKey(key)) {
			long newValue = mappingFunction.applyAsLong(key);
			if(newValue != getDefaultReturnValue()) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public long supplyLongIfAbsentNonDefault(int key, LongSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		long value;
		if((value = get(key)) == getDefaultReturnValue() || !containsKey(key)) {
			long newValue = valueProvider.getAsLong();
			if(newValue != getDefaultReturnValue()) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public long computeLongIfPresentNonDefault(int key, IntLongUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		long value;
		if((value = get(key)) != getDefaultReturnValue() || containsKey(key)) {
			long newValue = mappingFunction.applyAsLong(key, value);
			if(newValue != getDefaultReturnValue()) {
				put(key, newValue);
				return newValue;
			}
			remove(key);
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public long mergeLong(int key, long value, LongLongUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		long oldValue = get(key);
		long newValue = oldValue == getDefaultReturnValue() ? value : mappingFunction.applyAsLong(oldValue, value);
		if(newValue == getDefaultReturnValue()) remove(key);
		else put(key, newValue);
		return newValue;
	}
	
	@Override
	public void mergeAllLong(Int2LongMap m, LongLongUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Int2LongMap.Entry entry : getFastIterable(m)) {
			int key = entry.getIntKey();
			long oldValue = get(key);
			long newValue = oldValue == getDefaultReturnValue() ? entry.getLongValue() : mappingFunction.applyAsLong(oldValue, entry.getLongValue());
			if(newValue == getDefaultReturnValue()) remove(key);
			else put(key, newValue);
		}
	}
	
	@Override
	public Long get(Object key) {
		return Long.valueOf(key instanceof Integer ? get(((Integer)key).intValue()) : getDefaultReturnValue());
	}
	
	@Override
	public Long getOrDefault(Object key, Long defaultValue) {
		return Long.valueOf(key instanceof Integer ? getOrDefault(((Integer)key).intValue(), defaultValue.longValue()) : getDefaultReturnValue());
	}
	
	@Override
	public long getOrDefault(int key, long defaultValue) {
		long value = get(key);
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	
	@Override
	public Long remove(Object key) {
		return key instanceof Integer ? Long.valueOf(remove(((Integer)key).intValue())) : Long.valueOf(getDefaultReturnValue());
	}
	
	@Override
	public void forEach(IntLongConsumer action) {
		Objects.requireNonNull(action);
		for(ObjectIterator<Int2LongMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Int2LongMap.Entry entry = iter.next();
			action.accept(entry.getIntKey(), entry.getLongValue());
		}
	}

	@Override
	public IntSet keySet() {
		return new AbstractIntSet() {
			@Override
			public boolean remove(int o) {
				return AbstractInt2LongMap.this.remove(o) != getDefaultReturnValue();
			}
			
			@Override
			public boolean add(int o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public IntIterator iterator() {
				return new IntIterator() {
					ObjectIterator<Int2LongMap.Entry> iter = getFastIterator(AbstractInt2LongMap.this);
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
				return AbstractInt2LongMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractInt2LongMap.this.clear();
			}
		};
	}

	@Override
	public LongCollection values() {
		return new AbstractLongCollection() {
			@Override
			public boolean add(long o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public int size() {
				return AbstractInt2LongMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractInt2LongMap.this.clear();
			}
			
			@Override
			public LongIterator iterator() {
				return new LongIterator() {
					ObjectIterator<Int2LongMap.Entry> iter = getFastIterator(AbstractInt2LongMap.this);
					@Override
					public boolean hasNext() {
						return iter.hasNext();
					}
					
					@Override
					public long nextLong() {
						return iter.next().getLongValue();
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
	public ObjectSet<Map.Entry<Integer, Long>> entrySet() {
		return (ObjectSet)int2LongEntrySet();
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o instanceof Map) {
			if(size() != ((Map<?, ?>)o).size()) return false;
			if(o instanceof Int2LongMap) return int2LongEntrySet().containsAll(((Int2LongMap)o).int2LongEntrySet());
			return int2LongEntrySet().containsAll(((Map<?, ?>)o).entrySet());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		ObjectIterator<Int2LongMap.Entry> iter = getFastIterator(this);
		while(iter.hasNext()) hash += iter.next().hashCode();
		return hash;
	}
	
	public static class ReversedInt2LongOrderedMap extends AbstractInt2LongMap implements Int2LongOrderedMap {
		Int2LongOrderedMap map;
		
		public ReversedInt2LongOrderedMap(Int2LongOrderedMap map) {
			this.map = map;
		}
		@Override
		public AbstractInt2LongMap setDefaultReturnValue(long v) {
			map.setDefaultReturnValue(v);
			return this;
		}
		@Override
		public long getDefaultReturnValue() { return map.getDefaultReturnValue(); }
		@Override
		public Int2LongOrderedMap copy() { throw new UnsupportedOperationException(); }
		@Override
		public long put(int key, long value) { return map.put(key, value); }
		@Override
		public long putIfAbsent(int key, long value) { return map.putIfAbsent(key, value); }
		@Override
		public long addTo(int key, long value) { return map.addTo(key, value); }
		@Override
		public long subFrom(int key, long value) { return map.subFrom(key, value); }
		@Override
		public long remove(int key) { return map.remove(key); }
		@Override
		public boolean remove(int key, long value) { return map.remove(key, value); }
		@Override
		public long removeOrDefault(int key, long defaultValue) { return map.removeOrDefault(key, defaultValue); }
		@Override
		public boolean containsKey(int key) { return map.containsKey(key); }
		@Override
		public boolean containsValue(long value) { return map.containsValue(value); }
		@Override
		public boolean replace(int key, long oldValue, long newValue) { return map.replace(key, oldValue, newValue); }
		@Override
		public long replace(int key, long value) { return map.replace(key, value); }
		@Override
		public void replaceLongs(Int2LongMap m) { map.replaceLongs(m); }
		@Override
		public void replaceLongs(IntLongUnaryOperator mappingFunction) { map.replaceLongs(mappingFunction); }
		@Override
		public long computeLong(int key, IntLongUnaryOperator mappingFunction) { return map.computeLong(key, mappingFunction); }
		@Override
		public long computeLongIfAbsent(int key, Int2LongFunction mappingFunction) { return map.computeLongIfAbsent(key, mappingFunction); }
		@Override
		public long supplyLongIfAbsent(int key, LongSupplier valueProvider) { return map.supplyLongIfAbsent(key, valueProvider); }
		@Override
		public long computeLongIfPresent(int key, IntLongUnaryOperator mappingFunction) { return map.computeLongIfPresent(key, mappingFunction); }
		@Override
		public long computeLongNonDefault(int key, IntLongUnaryOperator mappingFunction) { return map.computeLongNonDefault(key, mappingFunction); }
		@Override
		public long computeLongIfAbsentNonDefault(int key, Int2LongFunction mappingFunction) { return map.computeLongIfAbsentNonDefault(key, mappingFunction); }
		@Override
		public long supplyLongIfAbsentNonDefault(int key, LongSupplier valueProvider) { return map.supplyLongIfAbsentNonDefault(key, valueProvider); }
		@Override
		public long computeLongIfPresentNonDefault(int key, IntLongUnaryOperator mappingFunction) { return map.computeLongIfPresentNonDefault(key, mappingFunction); }
		@Override
		public long mergeLong(int key, long value, LongLongUnaryOperator mappingFunction) { return map.mergeLong(key, value, mappingFunction); }
		@Override
		public long getOrDefault(int key, long defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public long get(int key) { return map.get(key); }
		@Override
		public long putAndMoveToFirst(int key, long value) { return map.putAndMoveToLast(key, value); }
		@Override
		public long putAndMoveToLast(int key, long value) { return map.putAndMoveToFirst(key, value); }
		@Override
		public long putFirst(int key, long value) { return map.putLast(key, value); }
		@Override
		public long putLast(int key, long value) { return map.putFirst(key, value); }
		@Override
		public boolean moveToFirst(int key) { return map.moveToLast(key); }
		@Override
		public boolean moveToLast(int key) { return map.moveToFirst(key); }
		@Override
		public long getAndMoveToFirst(int key) { return map.getAndMoveToLast(key); }
		@Override
		public long getAndMoveToLast(int key) { return map.getAndMoveToFirst(key); }
		@Override
		public int firstIntKey() { return map.lastIntKey(); }
		@Override
		public int pollFirstIntKey() { return map.pollLastIntKey(); }
		@Override
		public int lastIntKey() { return map.firstIntKey(); }
		@Override
		public int pollLastIntKey() { return map.pollFirstIntKey(); }
		@Override
		public long firstLongValue() { return map.lastLongValue(); }
		@Override
		public long lastLongValue() { return map.firstLongValue(); }
		@Override
		public Int2LongMap.Entry firstEntry() { return map.lastEntry(); }
		@Override
		public Int2LongMap.Entry lastEntry() { return map.firstEntry(); }
		@Override
		public Int2LongMap.Entry pollFirstEntry() { return map.pollLastEntry(); }
		@Override
		public Int2LongMap.Entry pollLastEntry() { return map.pollFirstEntry(); }
		@Override
		public ObjectOrderedSet<Int2LongMap.Entry> int2LongEntrySet() { return new AbstractObjectSet.ReversedObjectOrderedSet<>(map.int2LongEntrySet()); }
		@Override
		public IntOrderedSet keySet() { return new AbstractIntSet.ReversedIntOrderedSet(map.keySet()); }
		@Override
		public LongOrderedCollection values() { return map.values().reversed(); }
		@Override
		public Int2LongOrderedMap reversed() { return map; }
	}

	
	/**
	 * A Simple Type Specific Entry class to reduce boxing/unboxing
	 */
	public static class BasicEntry implements Int2LongMap.Entry {
		protected int key;
		protected long value;
		
		/**
		 * A basic Empty constructor
		 */
		public BasicEntry() {}
		/**
		 * A Boxed Constructor for supporting java variants
		 * @param key the key of a entry
		 * @param value the value of a entry
		 */
		public BasicEntry(Integer key, Long value) {
			this.key = key.intValue();
			this.value = value.longValue();
		}
		
		/**
		 * A Type Specific Constructor
		 * @param key the key of a entry
		 * @param value the value of a entry 
		 */
		public BasicEntry(int key, long value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * A Helper method for fast replacing values
		 * @param key the key that should be replaced
		 * @param value the value that should be replaced
		 */
		public void set(int key, long value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public int getIntKey() {
			return key;
		}

		@Override
		public long getLongValue() {
			return value;
		}

		@Override
		public long setValue(long value) {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Map.Entry) {
				if(obj instanceof Int2LongMap.Entry) {
					Int2LongMap.Entry entry = (Int2LongMap.Entry)obj;
					return key == entry.getIntKey() && value == entry.getLongValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Integer && value instanceof Long && this.key == ((Integer)key).intValue() && this.value == ((Long)value).longValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Integer.hashCode(key) ^ Long.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Integer.toString(key) + "=" + Long.toString(value);
		}
	}
}