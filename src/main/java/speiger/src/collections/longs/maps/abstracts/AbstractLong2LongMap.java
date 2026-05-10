package speiger.src.collections.longs.maps.abstracts;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

import speiger.src.collections.longs.collections.LongIterator;
import speiger.src.collections.longs.functions.consumer.LongLongConsumer;
import speiger.src.collections.longs.functions.function.LongUnaryOperator;
import speiger.src.collections.longs.functions.function.LongLongUnaryOperator;
import speiger.src.collections.longs.maps.interfaces.Long2LongMap;
import speiger.src.collections.longs.maps.interfaces.Long2LongOrderedMap;
import speiger.src.collections.longs.sets.LongOrderedSet;
import speiger.src.collections.longs.collections.LongOrderedCollection;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
import speiger.src.collections.longs.sets.AbstractLongSet;
import speiger.src.collections.longs.sets.LongSet;
import speiger.src.collections.longs.utils.maps.Long2LongMaps;
import speiger.src.collections.longs.collections.AbstractLongCollection;
import speiger.src.collections.longs.collections.LongCollection;
import speiger.src.collections.longs.functions.LongSupplier;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Base Implementation of a Type Specific Map to reduce boxing/unboxing
 */
public abstract class AbstractLong2LongMap extends AbstractMap<Long, Long> implements Long2LongMap
{
	protected long defaultReturnValue = -1L;
	
	@Override
	public long getDefaultReturnValue() {
		return defaultReturnValue;
	}
	
	@Override
	public AbstractLong2LongMap setDefaultReturnValue(long v) {
		defaultReturnValue = v;
		return this;
	}
	
	protected ObjectIterable<Long2LongMap.Entry> getFastIterable(Long2LongMap map) {
		return Long2LongMaps.fastIterable(map);
	}
	
	protected ObjectIterator<Long2LongMap.Entry> getFastIterator(Long2LongMap map) {
		return Long2LongMaps.fastIterator(map);
	}
	
	@Override
	public Long2LongMap copy() { 
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public Long put(Long key, Long value) {
		return Long.valueOf(put(key.longValue(), value.longValue()));
	}
	
	@Override
	public void addToAll(Long2LongMap m) {
		for(Long2LongMap.Entry entry : getFastIterable(m))
			addTo(entry.getLongKey(), entry.getLongValue());
	}
	
	@Override
	public void putAll(Long2LongMap m) {
		for(ObjectIterator<Long2LongMap.Entry> iter = getFastIterator(m);iter.hasNext();) {
			Long2LongMap.Entry entry = iter.next();
			put(entry.getLongKey(), entry.getLongValue());
		}
	}
	
	@Override
	public void putAll(Map<? extends Long, ? extends Long> m)
	{
		if(m instanceof Long2LongMap) putAll((Long2LongMap)m);
		else super.putAll(m);
	}
	
	@Override
	public void putAll(long[] keys, long[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);
	}
	
	@Override
	public void putAll(Long[] keys, Long[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i].longValue(), values[i].longValue());		
	}
	
	@Override
	public void putAllIfAbsent(Long2LongMap m) {
		for(Long2LongMap.Entry entry : getFastIterable(m))
			putIfAbsent(entry.getLongKey(), entry.getLongValue());
	}
	
	
	@Override
	public boolean containsKey(long key) {
		for(LongIterator iter = keySet().iterator();iter.hasNext();)
			if(iter.nextLong() == key) return true;
		return false;
	}
	
	@Override
	public boolean containsValue(long value) {
		for(LongIterator iter = values().iterator();iter.hasNext();)
			if(iter.nextLong() == value) return true;
		return false;
	}
	
	@Override
	public boolean replace(long key, long oldValue, long newValue) {
		long curValue = get(key);
		if (curValue != oldValue || (curValue == getDefaultReturnValue() && !containsKey(key))) {
			return false;
		}
		put(key, newValue);
		return true;
	}

	@Override
	public long replace(long key, long value) {
		long curValue;
		if ((curValue = get(key)) != getDefaultReturnValue() || containsKey(key)) {
			curValue = put(key, value);
		}
		return curValue;
	}
	
	@Override
	public void replaceLongs(Long2LongMap m) {
		for(Long2LongMap.Entry entry : getFastIterable(m))
			replace(entry.getLongKey(), entry.getLongValue());
	}
	
	@Override
	public void replaceLongs(LongLongUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(ObjectIterator<Long2LongMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Long2LongMap.Entry entry = iter.next();
			entry.setValue(mappingFunction.applyAsLong(entry.getLongKey(), entry.getLongValue()));
		}
	}

	@Override
	public long computeLong(long key, LongLongUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		long newValue = mappingFunction.applyAsLong(key, get(key));
		put(key, newValue);
		return newValue;
	}
	
	@Override
	public long computeLongIfAbsent(long key, LongUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(!containsKey(key)) {
			long newValue = mappingFunction.applyAsLong(key);
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public long supplyLongIfAbsent(long key, LongSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		if(!containsKey(key)) {
			long newValue = valueProvider.getAsLong();
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public long computeLongIfPresent(long key, LongLongUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(containsKey(key)) {
			long newValue = mappingFunction.applyAsLong(key, get(key));
			put(key, newValue);
			return newValue;
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public long computeLongNonDefault(long key, LongLongUnaryOperator mappingFunction) {
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
	public long computeLongIfAbsentNonDefault(long key, LongUnaryOperator mappingFunction) {
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
	public long supplyLongIfAbsentNonDefault(long key, LongSupplier valueProvider) {
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
	public long computeLongIfPresentNonDefault(long key, LongLongUnaryOperator mappingFunction) {
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
	public long mergeLong(long key, long value, LongLongUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		long oldValue = get(key);
		long newValue = oldValue == getDefaultReturnValue() ? value : mappingFunction.applyAsLong(oldValue, value);
		if(newValue == getDefaultReturnValue()) remove(key);
		else put(key, newValue);
		return newValue;
	}
	
	@Override
	public void mergeAllLong(Long2LongMap m, LongLongUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Long2LongMap.Entry entry : getFastIterable(m)) {
			long key = entry.getLongKey();
			long oldValue = get(key);
			long newValue = oldValue == getDefaultReturnValue() ? entry.getLongValue() : mappingFunction.applyAsLong(oldValue, entry.getLongValue());
			if(newValue == getDefaultReturnValue()) remove(key);
			else put(key, newValue);
		}
	}
	
	@Override
	public Long get(Object key) {
		return Long.valueOf(key instanceof Long ? get(((Long)key).longValue()) : getDefaultReturnValue());
	}
	
	@Override
	public Long getOrDefault(Object key, Long defaultValue) {
		return Long.valueOf(key instanceof Long ? getOrDefault(((Long)key).longValue(), defaultValue.longValue()) : getDefaultReturnValue());
	}
	
	@Override
	public long getOrDefault(long key, long defaultValue) {
		long value = get(key);
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	
	@Override
	public Long remove(Object key) {
		return key instanceof Long ? Long.valueOf(remove(((Long)key).longValue())) : Long.valueOf(getDefaultReturnValue());
	}
	
	@Override
	public void forEach(LongLongConsumer action) {
		Objects.requireNonNull(action);
		for(ObjectIterator<Long2LongMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Long2LongMap.Entry entry = iter.next();
			action.accept(entry.getLongKey(), entry.getLongValue());
		}
	}

	@Override
	public LongSet keySet() {
		return new AbstractLongSet() {
			@Override
			public boolean remove(long o) {
				return AbstractLong2LongMap.this.remove(o) != getDefaultReturnValue();
			}
			
			@Override
			public boolean add(long o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public LongIterator iterator() {
				return new LongIterator() {
					ObjectIterator<Long2LongMap.Entry> iter = getFastIterator(AbstractLong2LongMap.this);
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
				return AbstractLong2LongMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractLong2LongMap.this.clear();
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
				return AbstractLong2LongMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractLong2LongMap.this.clear();
			}
			
			@Override
			public LongIterator iterator() {
				return new LongIterator() {
					ObjectIterator<Long2LongMap.Entry> iter = getFastIterator(AbstractLong2LongMap.this);
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
	public ObjectSet<Map.Entry<Long, Long>> entrySet() {
		return (ObjectSet)long2LongEntrySet();
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o instanceof Map) {
			if(size() != ((Map<?, ?>)o).size()) return false;
			if(o instanceof Long2LongMap) return long2LongEntrySet().containsAll(((Long2LongMap)o).long2LongEntrySet());
			return long2LongEntrySet().containsAll(((Map<?, ?>)o).entrySet());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		ObjectIterator<Long2LongMap.Entry> iter = getFastIterator(this);
		while(iter.hasNext()) hash += iter.next().hashCode();
		return hash;
	}
	
	public static class ReversedLong2LongOrderedMap extends AbstractLong2LongMap implements Long2LongOrderedMap {
		Long2LongOrderedMap map;
		
		public ReversedLong2LongOrderedMap(Long2LongOrderedMap map) {
			this.map = map;
		}
		@Override
		public AbstractLong2LongMap setDefaultReturnValue(long v) {
			map.setDefaultReturnValue(v);
			return this;
		}
		@Override
		public long getDefaultReturnValue() { return map.getDefaultReturnValue(); }
		@Override
		public Long2LongOrderedMap copy() { throw new UnsupportedOperationException(); }
		@Override
		public long put(long key, long value) { return map.put(key, value); }
		@Override
		public long putIfAbsent(long key, long value) { return map.putIfAbsent(key, value); }
		@Override
		public long addTo(long key, long value) { return map.addTo(key, value); }
		@Override
		public long subFrom(long key, long value) { return map.subFrom(key, value); }
		@Override
		public long remove(long key) { return map.remove(key); }
		@Override
		public boolean remove(long key, long value) { return map.remove(key, value); }
		@Override
		public long removeOrDefault(long key, long defaultValue) { return map.removeOrDefault(key, defaultValue); }
		@Override
		public boolean containsKey(long key) { return map.containsKey(key); }
		@Override
		public boolean containsValue(long value) { return map.containsValue(value); }
		@Override
		public boolean replace(long key, long oldValue, long newValue) { return map.replace(key, oldValue, newValue); }
		@Override
		public long replace(long key, long value) { return map.replace(key, value); }
		@Override
		public void replaceLongs(Long2LongMap m) { map.replaceLongs(m); }
		@Override
		public void replaceLongs(LongLongUnaryOperator mappingFunction) { map.replaceLongs(mappingFunction); }
		@Override
		public long computeLong(long key, LongLongUnaryOperator mappingFunction) { return map.computeLong(key, mappingFunction); }
		@Override
		public long computeLongIfAbsent(long key, LongUnaryOperator mappingFunction) { return map.computeLongIfAbsent(key, mappingFunction); }
		@Override
		public long supplyLongIfAbsent(long key, LongSupplier valueProvider) { return map.supplyLongIfAbsent(key, valueProvider); }
		@Override
		public long computeLongIfPresent(long key, LongLongUnaryOperator mappingFunction) { return map.computeLongIfPresent(key, mappingFunction); }
		@Override
		public long computeLongNonDefault(long key, LongLongUnaryOperator mappingFunction) { return map.computeLongNonDefault(key, mappingFunction); }
		@Override
		public long computeLongIfAbsentNonDefault(long key, LongUnaryOperator mappingFunction) { return map.computeLongIfAbsentNonDefault(key, mappingFunction); }
		@Override
		public long supplyLongIfAbsentNonDefault(long key, LongSupplier valueProvider) { return map.supplyLongIfAbsentNonDefault(key, valueProvider); }
		@Override
		public long computeLongIfPresentNonDefault(long key, LongLongUnaryOperator mappingFunction) { return map.computeLongIfPresentNonDefault(key, mappingFunction); }
		@Override
		public long mergeLong(long key, long value, LongLongUnaryOperator mappingFunction) { return map.mergeLong(key, value, mappingFunction); }
		@Override
		public long getOrDefault(long key, long defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public long get(long key) { return map.get(key); }
		@Override
		public long putAndMoveToFirst(long key, long value) { return map.putAndMoveToLast(key, value); }
		@Override
		public long putAndMoveToLast(long key, long value) { return map.putAndMoveToFirst(key, value); }
		@Override
		public long putFirst(long key, long value) { return map.putLast(key, value); }
		@Override
		public long putLast(long key, long value) { return map.putFirst(key, value); }
		@Override
		public boolean moveToFirst(long key) { return map.moveToLast(key); }
		@Override
		public boolean moveToLast(long key) { return map.moveToFirst(key); }
		@Override
		public long getAndMoveToFirst(long key) { return map.getAndMoveToLast(key); }
		@Override
		public long getAndMoveToLast(long key) { return map.getAndMoveToFirst(key); }
		@Override
		public long firstLongKey() { return map.lastLongKey(); }
		@Override
		public long pollFirstLongKey() { return map.pollLastLongKey(); }
		@Override
		public long lastLongKey() { return map.firstLongKey(); }
		@Override
		public long pollLastLongKey() { return map.pollFirstLongKey(); }
		@Override
		public long firstLongValue() { return map.lastLongValue(); }
		@Override
		public long lastLongValue() { return map.firstLongValue(); }
		@Override
		public Long2LongMap.Entry firstEntry() { return map.lastEntry(); }
		@Override
		public Long2LongMap.Entry lastEntry() { return map.firstEntry(); }
		@Override
		public Long2LongMap.Entry pollFirstEntry() { return map.pollLastEntry(); }
		@Override
		public Long2LongMap.Entry pollLastEntry() { return map.pollFirstEntry(); }
		@Override
		public ObjectOrderedSet<Long2LongMap.Entry> long2LongEntrySet() { return new AbstractObjectSet.ReversedObjectOrderedSet<>(map.long2LongEntrySet()); }
		@Override
		public LongOrderedSet keySet() { return new AbstractLongSet.ReversedLongOrderedSet(map.keySet()); }
		@Override
		public LongOrderedCollection values() { return map.values().reversed(); }
		@Override
		public Long2LongOrderedMap reversed() { return map; }
	}

	
	/**
	 * A Simple Type Specific Entry class to reduce boxing/unboxing
	 */
	public static class BasicEntry implements Long2LongMap.Entry {
		protected long key;
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
		public BasicEntry(Long key, Long value) {
			this.key = key.longValue();
			this.value = value.longValue();
		}
		
		/**
		 * A Type Specific Constructor
		 * @param key the key of a entry
		 * @param value the value of a entry 
		 */
		public BasicEntry(long key, long value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * A Helper method for fast replacing values
		 * @param key the key that should be replaced
		 * @param value the value that should be replaced
		 */
		public void set(long key, long value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public long getLongKey() {
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
				if(obj instanceof Long2LongMap.Entry) {
					Long2LongMap.Entry entry = (Long2LongMap.Entry)obj;
					return key == entry.getLongKey() && value == entry.getLongValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Long && value instanceof Long && this.key == ((Long)key).longValue() && this.value == ((Long)value).longValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Long.hashCode(key) ^ Long.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Long.toString(key) + "=" + Long.toString(value);
		}
	}
}