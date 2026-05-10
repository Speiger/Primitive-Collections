package speiger.src.collections.shorts.maps.abstracts;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

import speiger.src.collections.shorts.collections.ShortIterator;
import speiger.src.collections.shorts.functions.consumer.ShortLongConsumer;
import speiger.src.collections.shorts.functions.function.Short2LongFunction;
import speiger.src.collections.shorts.functions.function.ShortLongUnaryOperator;
import speiger.src.collections.shorts.maps.interfaces.Short2LongMap;
import speiger.src.collections.shorts.maps.interfaces.Short2LongOrderedMap;
import speiger.src.collections.shorts.sets.ShortOrderedSet;
import speiger.src.collections.longs.collections.LongOrderedCollection;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
import speiger.src.collections.shorts.sets.AbstractShortSet;
import speiger.src.collections.shorts.sets.ShortSet;
import speiger.src.collections.shorts.utils.maps.Short2LongMaps;
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
public abstract class AbstractShort2LongMap extends AbstractMap<Short, Long> implements Short2LongMap
{
	protected long defaultReturnValue = -1L;
	
	@Override
	public long getDefaultReturnValue() {
		return defaultReturnValue;
	}
	
	@Override
	public AbstractShort2LongMap setDefaultReturnValue(long v) {
		defaultReturnValue = v;
		return this;
	}
	
	protected ObjectIterable<Short2LongMap.Entry> getFastIterable(Short2LongMap map) {
		return Short2LongMaps.fastIterable(map);
	}
	
	protected ObjectIterator<Short2LongMap.Entry> getFastIterator(Short2LongMap map) {
		return Short2LongMaps.fastIterator(map);
	}
	
	@Override
	public Short2LongMap copy() { 
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public Long put(Short key, Long value) {
		return Long.valueOf(put(key.shortValue(), value.longValue()));
	}
	
	@Override
	public void addToAll(Short2LongMap m) {
		for(Short2LongMap.Entry entry : getFastIterable(m))
			addTo(entry.getShortKey(), entry.getLongValue());
	}
	
	@Override
	public void putAll(Short2LongMap m) {
		for(ObjectIterator<Short2LongMap.Entry> iter = getFastIterator(m);iter.hasNext();) {
			Short2LongMap.Entry entry = iter.next();
			put(entry.getShortKey(), entry.getLongValue());
		}
	}
	
	@Override
	public void putAll(Map<? extends Short, ? extends Long> m)
	{
		if(m instanceof Short2LongMap) putAll((Short2LongMap)m);
		else super.putAll(m);
	}
	
	@Override
	public void putAll(short[] keys, long[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);
	}
	
	@Override
	public void putAll(Short[] keys, Long[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i].shortValue(), values[i].longValue());		
	}
	
	@Override
	public void putAllIfAbsent(Short2LongMap m) {
		for(Short2LongMap.Entry entry : getFastIterable(m))
			putIfAbsent(entry.getShortKey(), entry.getLongValue());
	}
	
	
	@Override
	public boolean containsKey(short key) {
		for(ShortIterator iter = keySet().iterator();iter.hasNext();)
			if(iter.nextShort() == key) return true;
		return false;
	}
	
	@Override
	public boolean containsValue(long value) {
		for(LongIterator iter = values().iterator();iter.hasNext();)
			if(iter.nextLong() == value) return true;
		return false;
	}
	
	@Override
	public boolean replace(short key, long oldValue, long newValue) {
		long curValue = get(key);
		if (curValue != oldValue || (curValue == getDefaultReturnValue() && !containsKey(key))) {
			return false;
		}
		put(key, newValue);
		return true;
	}

	@Override
	public long replace(short key, long value) {
		long curValue;
		if ((curValue = get(key)) != getDefaultReturnValue() || containsKey(key)) {
			curValue = put(key, value);
		}
		return curValue;
	}
	
	@Override
	public void replaceLongs(Short2LongMap m) {
		for(Short2LongMap.Entry entry : getFastIterable(m))
			replace(entry.getShortKey(), entry.getLongValue());
	}
	
	@Override
	public void replaceLongs(ShortLongUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(ObjectIterator<Short2LongMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Short2LongMap.Entry entry = iter.next();
			entry.setValue(mappingFunction.applyAsLong(entry.getShortKey(), entry.getLongValue()));
		}
	}

	@Override
	public long computeLong(short key, ShortLongUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		long newValue = mappingFunction.applyAsLong(key, get(key));
		put(key, newValue);
		return newValue;
	}
	
	@Override
	public long computeLongIfAbsent(short key, Short2LongFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(!containsKey(key)) {
			long newValue = mappingFunction.applyAsLong(key);
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public long supplyLongIfAbsent(short key, LongSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		if(!containsKey(key)) {
			long newValue = valueProvider.getAsLong();
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public long computeLongIfPresent(short key, ShortLongUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(containsKey(key)) {
			long newValue = mappingFunction.applyAsLong(key, get(key));
			put(key, newValue);
			return newValue;
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public long computeLongNonDefault(short key, ShortLongUnaryOperator mappingFunction) {
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
	public long computeLongIfAbsentNonDefault(short key, Short2LongFunction mappingFunction) {
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
	public long supplyLongIfAbsentNonDefault(short key, LongSupplier valueProvider) {
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
	public long computeLongIfPresentNonDefault(short key, ShortLongUnaryOperator mappingFunction) {
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
	public long mergeLong(short key, long value, LongLongUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		long oldValue = get(key);
		long newValue = oldValue == getDefaultReturnValue() ? value : mappingFunction.applyAsLong(oldValue, value);
		if(newValue == getDefaultReturnValue()) remove(key);
		else put(key, newValue);
		return newValue;
	}
	
	@Override
	public void mergeAllLong(Short2LongMap m, LongLongUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Short2LongMap.Entry entry : getFastIterable(m)) {
			short key = entry.getShortKey();
			long oldValue = get(key);
			long newValue = oldValue == getDefaultReturnValue() ? entry.getLongValue() : mappingFunction.applyAsLong(oldValue, entry.getLongValue());
			if(newValue == getDefaultReturnValue()) remove(key);
			else put(key, newValue);
		}
	}
	
	@Override
	public Long get(Object key) {
		return Long.valueOf(key instanceof Short ? get(((Short)key).shortValue()) : getDefaultReturnValue());
	}
	
	@Override
	public Long getOrDefault(Object key, Long defaultValue) {
		return Long.valueOf(key instanceof Short ? getOrDefault(((Short)key).shortValue(), defaultValue.longValue()) : getDefaultReturnValue());
	}
	
	@Override
	public long getOrDefault(short key, long defaultValue) {
		long value = get(key);
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	
	@Override
	public Long remove(Object key) {
		return key instanceof Short ? Long.valueOf(remove(((Short)key).shortValue())) : Long.valueOf(getDefaultReturnValue());
	}
	
	@Override
	public void forEach(ShortLongConsumer action) {
		Objects.requireNonNull(action);
		for(ObjectIterator<Short2LongMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Short2LongMap.Entry entry = iter.next();
			action.accept(entry.getShortKey(), entry.getLongValue());
		}
	}

	@Override
	public ShortSet keySet() {
		return new AbstractShortSet() {
			@Override
			public boolean remove(short o) {
				return AbstractShort2LongMap.this.remove(o) != getDefaultReturnValue();
			}
			
			@Override
			public boolean add(short o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public ShortIterator iterator() {
				return new ShortIterator() {
					ObjectIterator<Short2LongMap.Entry> iter = getFastIterator(AbstractShort2LongMap.this);
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
				return AbstractShort2LongMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractShort2LongMap.this.clear();
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
				return AbstractShort2LongMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractShort2LongMap.this.clear();
			}
			
			@Override
			public LongIterator iterator() {
				return new LongIterator() {
					ObjectIterator<Short2LongMap.Entry> iter = getFastIterator(AbstractShort2LongMap.this);
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
	public ObjectSet<Map.Entry<Short, Long>> entrySet() {
		return (ObjectSet)short2LongEntrySet();
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o instanceof Map) {
			if(size() != ((Map<?, ?>)o).size()) return false;
			if(o instanceof Short2LongMap) return short2LongEntrySet().containsAll(((Short2LongMap)o).short2LongEntrySet());
			return short2LongEntrySet().containsAll(((Map<?, ?>)o).entrySet());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		ObjectIterator<Short2LongMap.Entry> iter = getFastIterator(this);
		while(iter.hasNext()) hash += iter.next().hashCode();
		return hash;
	}
	
	public static class ReversedShort2LongOrderedMap extends AbstractShort2LongMap implements Short2LongOrderedMap {
		Short2LongOrderedMap map;
		
		public ReversedShort2LongOrderedMap(Short2LongOrderedMap map) {
			this.map = map;
		}
		@Override
		public AbstractShort2LongMap setDefaultReturnValue(long v) {
			map.setDefaultReturnValue(v);
			return this;
		}
		@Override
		public long getDefaultReturnValue() { return map.getDefaultReturnValue(); }
		@Override
		public Short2LongOrderedMap copy() { throw new UnsupportedOperationException(); }
		@Override
		public long put(short key, long value) { return map.put(key, value); }
		@Override
		public long putIfAbsent(short key, long value) { return map.putIfAbsent(key, value); }
		@Override
		public long addTo(short key, long value) { return map.addTo(key, value); }
		@Override
		public long subFrom(short key, long value) { return map.subFrom(key, value); }
		@Override
		public long remove(short key) { return map.remove(key); }
		@Override
		public boolean remove(short key, long value) { return map.remove(key, value); }
		@Override
		public long removeOrDefault(short key, long defaultValue) { return map.removeOrDefault(key, defaultValue); }
		@Override
		public boolean containsKey(short key) { return map.containsKey(key); }
		@Override
		public boolean containsValue(long value) { return map.containsValue(value); }
		@Override
		public boolean replace(short key, long oldValue, long newValue) { return map.replace(key, oldValue, newValue); }
		@Override
		public long replace(short key, long value) { return map.replace(key, value); }
		@Override
		public void replaceLongs(Short2LongMap m) { map.replaceLongs(m); }
		@Override
		public void replaceLongs(ShortLongUnaryOperator mappingFunction) { map.replaceLongs(mappingFunction); }
		@Override
		public long computeLong(short key, ShortLongUnaryOperator mappingFunction) { return map.computeLong(key, mappingFunction); }
		@Override
		public long computeLongIfAbsent(short key, Short2LongFunction mappingFunction) { return map.computeLongIfAbsent(key, mappingFunction); }
		@Override
		public long supplyLongIfAbsent(short key, LongSupplier valueProvider) { return map.supplyLongIfAbsent(key, valueProvider); }
		@Override
		public long computeLongIfPresent(short key, ShortLongUnaryOperator mappingFunction) { return map.computeLongIfPresent(key, mappingFunction); }
		@Override
		public long computeLongNonDefault(short key, ShortLongUnaryOperator mappingFunction) { return map.computeLongNonDefault(key, mappingFunction); }
		@Override
		public long computeLongIfAbsentNonDefault(short key, Short2LongFunction mappingFunction) { return map.computeLongIfAbsentNonDefault(key, mappingFunction); }
		@Override
		public long supplyLongIfAbsentNonDefault(short key, LongSupplier valueProvider) { return map.supplyLongIfAbsentNonDefault(key, valueProvider); }
		@Override
		public long computeLongIfPresentNonDefault(short key, ShortLongUnaryOperator mappingFunction) { return map.computeLongIfPresentNonDefault(key, mappingFunction); }
		@Override
		public long mergeLong(short key, long value, LongLongUnaryOperator mappingFunction) { return map.mergeLong(key, value, mappingFunction); }
		@Override
		public long getOrDefault(short key, long defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public long get(short key) { return map.get(key); }
		@Override
		public long putAndMoveToFirst(short key, long value) { return map.putAndMoveToLast(key, value); }
		@Override
		public long putAndMoveToLast(short key, long value) { return map.putAndMoveToFirst(key, value); }
		@Override
		public long putFirst(short key, long value) { return map.putLast(key, value); }
		@Override
		public long putLast(short key, long value) { return map.putFirst(key, value); }
		@Override
		public boolean moveToFirst(short key) { return map.moveToLast(key); }
		@Override
		public boolean moveToLast(short key) { return map.moveToFirst(key); }
		@Override
		public long getAndMoveToFirst(short key) { return map.getAndMoveToLast(key); }
		@Override
		public long getAndMoveToLast(short key) { return map.getAndMoveToFirst(key); }
		@Override
		public short firstShortKey() { return map.lastShortKey(); }
		@Override
		public short pollFirstShortKey() { return map.pollLastShortKey(); }
		@Override
		public short lastShortKey() { return map.firstShortKey(); }
		@Override
		public short pollLastShortKey() { return map.pollFirstShortKey(); }
		@Override
		public long firstLongValue() { return map.lastLongValue(); }
		@Override
		public long lastLongValue() { return map.firstLongValue(); }
		@Override
		public Short2LongMap.Entry firstEntry() { return map.lastEntry(); }
		@Override
		public Short2LongMap.Entry lastEntry() { return map.firstEntry(); }
		@Override
		public Short2LongMap.Entry pollFirstEntry() { return map.pollLastEntry(); }
		@Override
		public Short2LongMap.Entry pollLastEntry() { return map.pollFirstEntry(); }
		@Override
		public ObjectOrderedSet<Short2LongMap.Entry> short2LongEntrySet() { return new AbstractObjectSet.ReversedObjectOrderedSet<>(map.short2LongEntrySet()); }
		@Override
		public ShortOrderedSet keySet() { return new AbstractShortSet.ReversedShortOrderedSet(map.keySet()); }
		@Override
		public LongOrderedCollection values() { return map.values().reversed(); }
		@Override
		public Short2LongOrderedMap reversed() { return map; }
	}

	
	/**
	 * A Simple Type Specific Entry class to reduce boxing/unboxing
	 */
	public static class BasicEntry implements Short2LongMap.Entry {
		protected short key;
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
		public BasicEntry(Short key, Long value) {
			this.key = key.shortValue();
			this.value = value.longValue();
		}
		
		/**
		 * A Type Specific Constructor
		 * @param key the key of a entry
		 * @param value the value of a entry 
		 */
		public BasicEntry(short key, long value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * A Helper method for fast replacing values
		 * @param key the key that should be replaced
		 * @param value the value that should be replaced
		 */
		public void set(short key, long value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public short getShortKey() {
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
				if(obj instanceof Short2LongMap.Entry) {
					Short2LongMap.Entry entry = (Short2LongMap.Entry)obj;
					return key == entry.getShortKey() && value == entry.getLongValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Short && value instanceof Long && this.key == ((Short)key).shortValue() && this.value == ((Long)value).longValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Short.hashCode(key) ^ Long.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Short.toString(key) + "=" + Long.toString(value);
		}
	}
}