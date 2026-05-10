package speiger.src.collections.longs.maps.abstracts;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

import speiger.src.collections.longs.collections.LongIterator;
import speiger.src.collections.longs.functions.consumer.LongShortConsumer;
import speiger.src.collections.longs.functions.function.Long2ShortFunction;
import speiger.src.collections.longs.functions.function.LongShortUnaryOperator;
import speiger.src.collections.longs.maps.interfaces.Long2ShortMap;
import speiger.src.collections.longs.maps.interfaces.Long2ShortOrderedMap;
import speiger.src.collections.longs.sets.LongOrderedSet;
import speiger.src.collections.shorts.collections.ShortOrderedCollection;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
import speiger.src.collections.longs.sets.AbstractLongSet;
import speiger.src.collections.longs.sets.LongSet;
import speiger.src.collections.longs.utils.maps.Long2ShortMaps;
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
public abstract class AbstractLong2ShortMap extends AbstractMap<Long, Short> implements Long2ShortMap
{
	protected short defaultReturnValue = (short)-1;
	
	@Override
	public short getDefaultReturnValue() {
		return defaultReturnValue;
	}
	
	@Override
	public AbstractLong2ShortMap setDefaultReturnValue(short v) {
		defaultReturnValue = v;
		return this;
	}
	
	protected ObjectIterable<Long2ShortMap.Entry> getFastIterable(Long2ShortMap map) {
		return Long2ShortMaps.fastIterable(map);
	}
	
	protected ObjectIterator<Long2ShortMap.Entry> getFastIterator(Long2ShortMap map) {
		return Long2ShortMaps.fastIterator(map);
	}
	
	@Override
	public Long2ShortMap copy() { 
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public Short put(Long key, Short value) {
		return Short.valueOf(put(key.longValue(), value.shortValue()));
	}
	
	@Override
	public void addToAll(Long2ShortMap m) {
		for(Long2ShortMap.Entry entry : getFastIterable(m))
			addTo(entry.getLongKey(), entry.getShortValue());
	}
	
	@Override
	public void putAll(Long2ShortMap m) {
		for(ObjectIterator<Long2ShortMap.Entry> iter = getFastIterator(m);iter.hasNext();) {
			Long2ShortMap.Entry entry = iter.next();
			put(entry.getLongKey(), entry.getShortValue());
		}
	}
	
	@Override
	public void putAll(Map<? extends Long, ? extends Short> m)
	{
		if(m instanceof Long2ShortMap) putAll((Long2ShortMap)m);
		else super.putAll(m);
	}
	
	@Override
	public void putAll(long[] keys, short[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);
	}
	
	@Override
	public void putAll(Long[] keys, Short[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i].longValue(), values[i].shortValue());		
	}
	
	@Override
	public void putAllIfAbsent(Long2ShortMap m) {
		for(Long2ShortMap.Entry entry : getFastIterable(m))
			putIfAbsent(entry.getLongKey(), entry.getShortValue());
	}
	
	
	@Override
	public boolean containsKey(long key) {
		for(LongIterator iter = keySet().iterator();iter.hasNext();)
			if(iter.nextLong() == key) return true;
		return false;
	}
	
	@Override
	public boolean containsValue(short value) {
		for(ShortIterator iter = values().iterator();iter.hasNext();)
			if(iter.nextShort() == value) return true;
		return false;
	}
	
	@Override
	public boolean replace(long key, short oldValue, short newValue) {
		short curValue = get(key);
		if (curValue != oldValue || (curValue == getDefaultReturnValue() && !containsKey(key))) {
			return false;
		}
		put(key, newValue);
		return true;
	}

	@Override
	public short replace(long key, short value) {
		short curValue;
		if ((curValue = get(key)) != getDefaultReturnValue() || containsKey(key)) {
			curValue = put(key, value);
		}
		return curValue;
	}
	
	@Override
	public void replaceShorts(Long2ShortMap m) {
		for(Long2ShortMap.Entry entry : getFastIterable(m))
			replace(entry.getLongKey(), entry.getShortValue());
	}
	
	@Override
	public void replaceShorts(LongShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(ObjectIterator<Long2ShortMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Long2ShortMap.Entry entry = iter.next();
			entry.setValue(mappingFunction.applyAsShort(entry.getLongKey(), entry.getShortValue()));
		}
	}

	@Override
	public short computeShort(long key, LongShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		short newValue = mappingFunction.applyAsShort(key, get(key));
		put(key, newValue);
		return newValue;
	}
	
	@Override
	public short computeShortIfAbsent(long key, Long2ShortFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(!containsKey(key)) {
			short newValue = mappingFunction.applyAsShort(key);
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public short supplyShortIfAbsent(long key, ShortSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		if(!containsKey(key)) {
			short newValue = valueProvider.getAsShort();
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public short computeShortIfPresent(long key, LongShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(containsKey(key)) {
			short newValue = mappingFunction.applyAsShort(key, get(key));
			put(key, newValue);
			return newValue;
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public short computeShortNonDefault(long key, LongShortUnaryOperator mappingFunction) {
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
	public short computeShortIfAbsentNonDefault(long key, Long2ShortFunction mappingFunction) {
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
	public short supplyShortIfAbsentNonDefault(long key, ShortSupplier valueProvider) {
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
	public short computeShortIfPresentNonDefault(long key, LongShortUnaryOperator mappingFunction) {
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
	public short mergeShort(long key, short value, ShortShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		short oldValue = get(key);
		short newValue = oldValue == getDefaultReturnValue() ? value : mappingFunction.applyAsShort(oldValue, value);
		if(newValue == getDefaultReturnValue()) remove(key);
		else put(key, newValue);
		return newValue;
	}
	
	@Override
	public void mergeAllShort(Long2ShortMap m, ShortShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Long2ShortMap.Entry entry : getFastIterable(m)) {
			long key = entry.getLongKey();
			short oldValue = get(key);
			short newValue = oldValue == getDefaultReturnValue() ? entry.getShortValue() : mappingFunction.applyAsShort(oldValue, entry.getShortValue());
			if(newValue == getDefaultReturnValue()) remove(key);
			else put(key, newValue);
		}
	}
	
	@Override
	public Short get(Object key) {
		return Short.valueOf(key instanceof Long ? get(((Long)key).longValue()) : getDefaultReturnValue());
	}
	
	@Override
	public Short getOrDefault(Object key, Short defaultValue) {
		return Short.valueOf(key instanceof Long ? getOrDefault(((Long)key).longValue(), defaultValue.shortValue()) : getDefaultReturnValue());
	}
	
	@Override
	public short getOrDefault(long key, short defaultValue) {
		short value = get(key);
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	
	@Override
	public Short remove(Object key) {
		return key instanceof Long ? Short.valueOf(remove(((Long)key).longValue())) : Short.valueOf(getDefaultReturnValue());
	}
	
	@Override
	public void forEach(LongShortConsumer action) {
		Objects.requireNonNull(action);
		for(ObjectIterator<Long2ShortMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Long2ShortMap.Entry entry = iter.next();
			action.accept(entry.getLongKey(), entry.getShortValue());
		}
	}

	@Override
	public LongSet keySet() {
		return new AbstractLongSet() {
			@Override
			public boolean remove(long o) {
				return AbstractLong2ShortMap.this.remove(o) != getDefaultReturnValue();
			}
			
			@Override
			public boolean add(long o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public LongIterator iterator() {
				return new LongIterator() {
					ObjectIterator<Long2ShortMap.Entry> iter = getFastIterator(AbstractLong2ShortMap.this);
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
				return AbstractLong2ShortMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractLong2ShortMap.this.clear();
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
				return AbstractLong2ShortMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractLong2ShortMap.this.clear();
			}
			
			@Override
			public ShortIterator iterator() {
				return new ShortIterator() {
					ObjectIterator<Long2ShortMap.Entry> iter = getFastIterator(AbstractLong2ShortMap.this);
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
	public ObjectSet<Map.Entry<Long, Short>> entrySet() {
		return (ObjectSet)long2ShortEntrySet();
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o instanceof Map) {
			if(size() != ((Map<?, ?>)o).size()) return false;
			if(o instanceof Long2ShortMap) return long2ShortEntrySet().containsAll(((Long2ShortMap)o).long2ShortEntrySet());
			return long2ShortEntrySet().containsAll(((Map<?, ?>)o).entrySet());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		ObjectIterator<Long2ShortMap.Entry> iter = getFastIterator(this);
		while(iter.hasNext()) hash += iter.next().hashCode();
		return hash;
	}
	
	public static class ReversedLong2ShortOrderedMap extends AbstractLong2ShortMap implements Long2ShortOrderedMap {
		Long2ShortOrderedMap map;
		
		public ReversedLong2ShortOrderedMap(Long2ShortOrderedMap map) {
			this.map = map;
		}
		@Override
		public AbstractLong2ShortMap setDefaultReturnValue(short v) {
			map.setDefaultReturnValue(v);
			return this;
		}
		@Override
		public short getDefaultReturnValue() { return map.getDefaultReturnValue(); }
		@Override
		public Long2ShortOrderedMap copy() { throw new UnsupportedOperationException(); }
		@Override
		public short put(long key, short value) { return map.put(key, value); }
		@Override
		public short putIfAbsent(long key, short value) { return map.putIfAbsent(key, value); }
		@Override
		public short addTo(long key, short value) { return map.addTo(key, value); }
		@Override
		public short subFrom(long key, short value) { return map.subFrom(key, value); }
		@Override
		public short remove(long key) { return map.remove(key); }
		@Override
		public boolean remove(long key, short value) { return map.remove(key, value); }
		@Override
		public short removeOrDefault(long key, short defaultValue) { return map.removeOrDefault(key, defaultValue); }
		@Override
		public boolean containsKey(long key) { return map.containsKey(key); }
		@Override
		public boolean containsValue(short value) { return map.containsValue(value); }
		@Override
		public boolean replace(long key, short oldValue, short newValue) { return map.replace(key, oldValue, newValue); }
		@Override
		public short replace(long key, short value) { return map.replace(key, value); }
		@Override
		public void replaceShorts(Long2ShortMap m) { map.replaceShorts(m); }
		@Override
		public void replaceShorts(LongShortUnaryOperator mappingFunction) { map.replaceShorts(mappingFunction); }
		@Override
		public short computeShort(long key, LongShortUnaryOperator mappingFunction) { return map.computeShort(key, mappingFunction); }
		@Override
		public short computeShortIfAbsent(long key, Long2ShortFunction mappingFunction) { return map.computeShortIfAbsent(key, mappingFunction); }
		@Override
		public short supplyShortIfAbsent(long key, ShortSupplier valueProvider) { return map.supplyShortIfAbsent(key, valueProvider); }
		@Override
		public short computeShortIfPresent(long key, LongShortUnaryOperator mappingFunction) { return map.computeShortIfPresent(key, mappingFunction); }
		@Override
		public short computeShortNonDefault(long key, LongShortUnaryOperator mappingFunction) { return map.computeShortNonDefault(key, mappingFunction); }
		@Override
		public short computeShortIfAbsentNonDefault(long key, Long2ShortFunction mappingFunction) { return map.computeShortIfAbsentNonDefault(key, mappingFunction); }
		@Override
		public short supplyShortIfAbsentNonDefault(long key, ShortSupplier valueProvider) { return map.supplyShortIfAbsentNonDefault(key, valueProvider); }
		@Override
		public short computeShortIfPresentNonDefault(long key, LongShortUnaryOperator mappingFunction) { return map.computeShortIfPresentNonDefault(key, mappingFunction); }
		@Override
		public short mergeShort(long key, short value, ShortShortUnaryOperator mappingFunction) { return map.mergeShort(key, value, mappingFunction); }
		@Override
		public short getOrDefault(long key, short defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public short get(long key) { return map.get(key); }
		@Override
		public short putAndMoveToFirst(long key, short value) { return map.putAndMoveToLast(key, value); }
		@Override
		public short putAndMoveToLast(long key, short value) { return map.putAndMoveToFirst(key, value); }
		@Override
		public short putFirst(long key, short value) { return map.putLast(key, value); }
		@Override
		public short putLast(long key, short value) { return map.putFirst(key, value); }
		@Override
		public boolean moveToFirst(long key) { return map.moveToLast(key); }
		@Override
		public boolean moveToLast(long key) { return map.moveToFirst(key); }
		@Override
		public short getAndMoveToFirst(long key) { return map.getAndMoveToLast(key); }
		@Override
		public short getAndMoveToLast(long key) { return map.getAndMoveToFirst(key); }
		@Override
		public long firstLongKey() { return map.lastLongKey(); }
		@Override
		public long pollFirstLongKey() { return map.pollLastLongKey(); }
		@Override
		public long lastLongKey() { return map.firstLongKey(); }
		@Override
		public long pollLastLongKey() { return map.pollFirstLongKey(); }
		@Override
		public short firstShortValue() { return map.lastShortValue(); }
		@Override
		public short lastShortValue() { return map.firstShortValue(); }
		@Override
		public Long2ShortMap.Entry firstEntry() { return map.lastEntry(); }
		@Override
		public Long2ShortMap.Entry lastEntry() { return map.firstEntry(); }
		@Override
		public Long2ShortMap.Entry pollFirstEntry() { return map.pollLastEntry(); }
		@Override
		public Long2ShortMap.Entry pollLastEntry() { return map.pollFirstEntry(); }
		@Override
		public ObjectOrderedSet<Long2ShortMap.Entry> long2ShortEntrySet() { return new AbstractObjectSet.ReversedObjectOrderedSet<>(map.long2ShortEntrySet()); }
		@Override
		public LongOrderedSet keySet() { return new AbstractLongSet.ReversedLongOrderedSet(map.keySet()); }
		@Override
		public ShortOrderedCollection values() { return map.values().reversed(); }
		@Override
		public Long2ShortOrderedMap reversed() { return map; }
	}

	
	/**
	 * A Simple Type Specific Entry class to reduce boxing/unboxing
	 */
	public static class BasicEntry implements Long2ShortMap.Entry {
		protected long key;
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
		public BasicEntry(Long key, Short value) {
			this.key = key.longValue();
			this.value = value.shortValue();
		}
		
		/**
		 * A Type Specific Constructor
		 * @param key the key of a entry
		 * @param value the value of a entry 
		 */
		public BasicEntry(long key, short value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * A Helper method for fast replacing values
		 * @param key the key that should be replaced
		 * @param value the value that should be replaced
		 */
		public void set(long key, short value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public long getLongKey() {
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
				if(obj instanceof Long2ShortMap.Entry) {
					Long2ShortMap.Entry entry = (Long2ShortMap.Entry)obj;
					return key == entry.getLongKey() && value == entry.getShortValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Long && value instanceof Short && this.key == ((Long)key).longValue() && this.value == ((Short)value).shortValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Long.hashCode(key) ^ Short.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Long.toString(key) + "=" + Short.toString(value);
		}
	}
}