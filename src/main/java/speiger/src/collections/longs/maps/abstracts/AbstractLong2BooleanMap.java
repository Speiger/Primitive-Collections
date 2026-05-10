package speiger.src.collections.longs.maps.abstracts;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.LongPredicate;

import speiger.src.collections.longs.collections.LongIterator;
import speiger.src.collections.longs.functions.consumer.LongBooleanConsumer;
import speiger.src.collections.longs.functions.function.LongBooleanUnaryOperator;
import speiger.src.collections.longs.maps.interfaces.Long2BooleanMap;
import speiger.src.collections.longs.maps.interfaces.Long2BooleanOrderedMap;
import speiger.src.collections.longs.sets.LongOrderedSet;
import speiger.src.collections.booleans.collections.BooleanOrderedCollection;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
import speiger.src.collections.longs.sets.AbstractLongSet;
import speiger.src.collections.longs.sets.LongSet;
import speiger.src.collections.longs.utils.maps.Long2BooleanMaps;
import speiger.src.collections.booleans.collections.AbstractBooleanCollection;
import speiger.src.collections.booleans.collections.BooleanCollection;
import speiger.src.collections.booleans.collections.BooleanIterator;
import speiger.src.collections.booleans.functions.function.BooleanBooleanUnaryOperator;
import speiger.src.collections.booleans.functions.BooleanSupplier;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Base Implementation of a Type Specific Map to reduce boxing/unboxing
 */
public abstract class AbstractLong2BooleanMap extends AbstractMap<Long, Boolean> implements Long2BooleanMap
{
	protected boolean defaultReturnValue = false;
	
	@Override
	public boolean getDefaultReturnValue() {
		return defaultReturnValue;
	}
	
	@Override
	public AbstractLong2BooleanMap setDefaultReturnValue(boolean v) {
		defaultReturnValue = v;
		return this;
	}
	
	protected ObjectIterable<Long2BooleanMap.Entry> getFastIterable(Long2BooleanMap map) {
		return Long2BooleanMaps.fastIterable(map);
	}
	
	protected ObjectIterator<Long2BooleanMap.Entry> getFastIterator(Long2BooleanMap map) {
		return Long2BooleanMaps.fastIterator(map);
	}
	
	@Override
	public Long2BooleanMap copy() { 
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public Boolean put(Long key, Boolean value) {
		return Boolean.valueOf(put(key.longValue(), value.booleanValue()));
	}
	
	@Override
	public void putAll(Long2BooleanMap m) {
		for(ObjectIterator<Long2BooleanMap.Entry> iter = getFastIterator(m);iter.hasNext();) {
			Long2BooleanMap.Entry entry = iter.next();
			put(entry.getLongKey(), entry.getBooleanValue());
		}
	}
	
	@Override
	public void putAll(Map<? extends Long, ? extends Boolean> m)
	{
		if(m instanceof Long2BooleanMap) putAll((Long2BooleanMap)m);
		else super.putAll(m);
	}
	
	@Override
	public void putAll(long[] keys, boolean[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);
	}
	
	@Override
	public void putAll(Long[] keys, Boolean[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i].longValue(), values[i].booleanValue());		
	}
	
	@Override
	public void putAllIfAbsent(Long2BooleanMap m) {
		for(Long2BooleanMap.Entry entry : getFastIterable(m))
			putIfAbsent(entry.getLongKey(), entry.getBooleanValue());
	}
	
	
	@Override
	public boolean containsKey(long key) {
		for(LongIterator iter = keySet().iterator();iter.hasNext();)
			if(iter.nextLong() == key) return true;
		return false;
	}
	
	@Override
	public boolean containsValue(boolean value) {
		for(BooleanIterator iter = values().iterator();iter.hasNext();)
			if(iter.nextBoolean() == value) return true;
		return false;
	}
	
	@Override
	public boolean replace(long key, boolean oldValue, boolean newValue) {
		boolean curValue = get(key);
		if (curValue != oldValue || (curValue == getDefaultReturnValue() && !containsKey(key))) {
			return false;
		}
		put(key, newValue);
		return true;
	}

	@Override
	public boolean replace(long key, boolean value) {
		boolean curValue;
		if ((curValue = get(key)) != getDefaultReturnValue() || containsKey(key)) {
			curValue = put(key, value);
		}
		return curValue;
	}
	
	@Override
	public void replaceBooleans(Long2BooleanMap m) {
		for(Long2BooleanMap.Entry entry : getFastIterable(m))
			replace(entry.getLongKey(), entry.getBooleanValue());
	}
	
	@Override
	public void replaceBooleans(LongBooleanUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(ObjectIterator<Long2BooleanMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Long2BooleanMap.Entry entry = iter.next();
			entry.setValue(mappingFunction.applyAsBoolean(entry.getLongKey(), entry.getBooleanValue()));
		}
	}

	@Override
	public boolean computeBoolean(long key, LongBooleanUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		boolean newValue = mappingFunction.applyAsBoolean(key, get(key));
		put(key, newValue);
		return newValue;
	}
	
	@Override
	public boolean computeBooleanIfAbsent(long key, LongPredicate mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(!containsKey(key)) {
			boolean newValue = mappingFunction.test(key);
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public boolean supplyBooleanIfAbsent(long key, BooleanSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		if(!containsKey(key)) {
			boolean newValue = valueProvider.getAsBoolean();
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public boolean computeBooleanIfPresent(long key, LongBooleanUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(containsKey(key)) {
			boolean newValue = mappingFunction.applyAsBoolean(key, get(key));
			put(key, newValue);
			return newValue;
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public boolean computeBooleanNonDefault(long key, LongBooleanUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		boolean value = get(key);
		boolean newValue = mappingFunction.applyAsBoolean(key, value);
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
	public boolean computeBooleanIfAbsentNonDefault(long key, LongPredicate mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		boolean value;
		if((value = get(key)) == getDefaultReturnValue() || !containsKey(key)) {
			boolean newValue = mappingFunction.test(key);
			if(newValue != getDefaultReturnValue()) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public boolean supplyBooleanIfAbsentNonDefault(long key, BooleanSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		boolean value;
		if((value = get(key)) == getDefaultReturnValue() || !containsKey(key)) {
			boolean newValue = valueProvider.getAsBoolean();
			if(newValue != getDefaultReturnValue()) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public boolean computeBooleanIfPresentNonDefault(long key, LongBooleanUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		boolean value;
		if((value = get(key)) != getDefaultReturnValue() || containsKey(key)) {
			boolean newValue = mappingFunction.applyAsBoolean(key, value);
			if(newValue != getDefaultReturnValue()) {
				put(key, newValue);
				return newValue;
			}
			remove(key);
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public boolean mergeBoolean(long key, boolean value, BooleanBooleanUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		boolean oldValue = get(key);
		boolean newValue = oldValue == getDefaultReturnValue() ? value : mappingFunction.applyAsBoolean(oldValue, value);
		if(newValue == getDefaultReturnValue()) remove(key);
		else put(key, newValue);
		return newValue;
	}
	
	@Override
	public void mergeAllBoolean(Long2BooleanMap m, BooleanBooleanUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Long2BooleanMap.Entry entry : getFastIterable(m)) {
			long key = entry.getLongKey();
			boolean oldValue = get(key);
			boolean newValue = oldValue == getDefaultReturnValue() ? entry.getBooleanValue() : mappingFunction.applyAsBoolean(oldValue, entry.getBooleanValue());
			if(newValue == getDefaultReturnValue()) remove(key);
			else put(key, newValue);
		}
	}
	
	@Override
	public Boolean get(Object key) {
		return Boolean.valueOf(key instanceof Long ? get(((Long)key).longValue()) : getDefaultReturnValue());
	}
	
	@Override
	public Boolean getOrDefault(Object key, Boolean defaultValue) {
		return Boolean.valueOf(key instanceof Long ? getOrDefault(((Long)key).longValue(), defaultValue.booleanValue()) : getDefaultReturnValue());
	}
	
	@Override
	public boolean getOrDefault(long key, boolean defaultValue) {
		boolean value = get(key);
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	
	@Override
	public Boolean remove(Object key) {
		return key instanceof Long ? Boolean.valueOf(remove(((Long)key).longValue())) : Boolean.valueOf(getDefaultReturnValue());
	}
	
	@Override
	public void forEach(LongBooleanConsumer action) {
		Objects.requireNonNull(action);
		for(ObjectIterator<Long2BooleanMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Long2BooleanMap.Entry entry = iter.next();
			action.accept(entry.getLongKey(), entry.getBooleanValue());
		}
	}

	@Override
	public LongSet keySet() {
		return new AbstractLongSet() {
			@Override
			public boolean remove(long o) {
				return AbstractLong2BooleanMap.this.remove(o) != getDefaultReturnValue();
			}
			
			@Override
			public boolean add(long o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public LongIterator iterator() {
				return new LongIterator() {
					ObjectIterator<Long2BooleanMap.Entry> iter = getFastIterator(AbstractLong2BooleanMap.this);
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
				return AbstractLong2BooleanMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractLong2BooleanMap.this.clear();
			}
		};
	}

	@Override
	public BooleanCollection values() {
		return new AbstractBooleanCollection() {
			@Override
			public boolean add(boolean o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public int size() {
				return AbstractLong2BooleanMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractLong2BooleanMap.this.clear();
			}
			
			@Override
			public BooleanIterator iterator() {
				return new BooleanIterator() {
					ObjectIterator<Long2BooleanMap.Entry> iter = getFastIterator(AbstractLong2BooleanMap.this);
					@Override
					public boolean hasNext() {
						return iter.hasNext();
					}
					
					@Override
					public boolean nextBoolean() {
						return iter.next().getBooleanValue();
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
	public ObjectSet<Map.Entry<Long, Boolean>> entrySet() {
		return (ObjectSet)long2BooleanEntrySet();
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o instanceof Map) {
			if(size() != ((Map<?, ?>)o).size()) return false;
			if(o instanceof Long2BooleanMap) return long2BooleanEntrySet().containsAll(((Long2BooleanMap)o).long2BooleanEntrySet());
			return long2BooleanEntrySet().containsAll(((Map<?, ?>)o).entrySet());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		ObjectIterator<Long2BooleanMap.Entry> iter = getFastIterator(this);
		while(iter.hasNext()) hash += iter.next().hashCode();
		return hash;
	}
	
	public static class ReversedLong2BooleanOrderedMap extends AbstractLong2BooleanMap implements Long2BooleanOrderedMap {
		Long2BooleanOrderedMap map;
		
		public ReversedLong2BooleanOrderedMap(Long2BooleanOrderedMap map) {
			this.map = map;
		}
		@Override
		public AbstractLong2BooleanMap setDefaultReturnValue(boolean v) {
			map.setDefaultReturnValue(v);
			return this;
		}
		@Override
		public boolean getDefaultReturnValue() { return map.getDefaultReturnValue(); }
		@Override
		public Long2BooleanOrderedMap copy() { throw new UnsupportedOperationException(); }
		@Override
		public boolean put(long key, boolean value) { return map.put(key, value); }
		@Override
		public boolean putIfAbsent(long key, boolean value) { return map.putIfAbsent(key, value); }
		@Override
		public boolean remove(long key) { return map.remove(key); }
		@Override
		public boolean remove(long key, boolean value) { return map.remove(key, value); }
		@Override
		public boolean removeOrDefault(long key, boolean defaultValue) { return map.removeOrDefault(key, defaultValue); }
		@Override
		public boolean containsKey(long key) { return map.containsKey(key); }
		@Override
		public boolean containsValue(boolean value) { return map.containsValue(value); }
		@Override
		public boolean replace(long key, boolean oldValue, boolean newValue) { return map.replace(key, oldValue, newValue); }
		@Override
		public boolean replace(long key, boolean value) { return map.replace(key, value); }
		@Override
		public void replaceBooleans(Long2BooleanMap m) { map.replaceBooleans(m); }
		@Override
		public void replaceBooleans(LongBooleanUnaryOperator mappingFunction) { map.replaceBooleans(mappingFunction); }
		@Override
		public boolean computeBoolean(long key, LongBooleanUnaryOperator mappingFunction) { return map.computeBoolean(key, mappingFunction); }
		@Override
		public boolean computeBooleanIfAbsent(long key, LongPredicate mappingFunction) { return map.computeBooleanIfAbsent(key, mappingFunction); }
		@Override
		public boolean supplyBooleanIfAbsent(long key, BooleanSupplier valueProvider) { return map.supplyBooleanIfAbsent(key, valueProvider); }
		@Override
		public boolean computeBooleanIfPresent(long key, LongBooleanUnaryOperator mappingFunction) { return map.computeBooleanIfPresent(key, mappingFunction); }
		@Override
		public boolean computeBooleanNonDefault(long key, LongBooleanUnaryOperator mappingFunction) { return map.computeBooleanNonDefault(key, mappingFunction); }
		@Override
		public boolean computeBooleanIfAbsentNonDefault(long key, LongPredicate mappingFunction) { return map.computeBooleanIfAbsentNonDefault(key, mappingFunction); }
		@Override
		public boolean supplyBooleanIfAbsentNonDefault(long key, BooleanSupplier valueProvider) { return map.supplyBooleanIfAbsentNonDefault(key, valueProvider); }
		@Override
		public boolean computeBooleanIfPresentNonDefault(long key, LongBooleanUnaryOperator mappingFunction) { return map.computeBooleanIfPresentNonDefault(key, mappingFunction); }
		@Override
		public boolean mergeBoolean(long key, boolean value, BooleanBooleanUnaryOperator mappingFunction) { return map.mergeBoolean(key, value, mappingFunction); }
		@Override
		public boolean getOrDefault(long key, boolean defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public boolean get(long key) { return map.get(key); }
		@Override
		public boolean putAndMoveToFirst(long key, boolean value) { return map.putAndMoveToLast(key, value); }
		@Override
		public boolean putAndMoveToLast(long key, boolean value) { return map.putAndMoveToFirst(key, value); }
		@Override
		public boolean putFirst(long key, boolean value) { return map.putLast(key, value); }
		@Override
		public boolean putLast(long key, boolean value) { return map.putFirst(key, value); }
		@Override
		public boolean moveToFirst(long key) { return map.moveToLast(key); }
		@Override
		public boolean moveToLast(long key) { return map.moveToFirst(key); }
		@Override
		public boolean getAndMoveToFirst(long key) { return map.getAndMoveToLast(key); }
		@Override
		public boolean getAndMoveToLast(long key) { return map.getAndMoveToFirst(key); }
		@Override
		public long firstLongKey() { return map.lastLongKey(); }
		@Override
		public long pollFirstLongKey() { return map.pollLastLongKey(); }
		@Override
		public long lastLongKey() { return map.firstLongKey(); }
		@Override
		public long pollLastLongKey() { return map.pollFirstLongKey(); }
		@Override
		public boolean firstBooleanValue() { return map.lastBooleanValue(); }
		@Override
		public boolean lastBooleanValue() { return map.firstBooleanValue(); }
		@Override
		public Long2BooleanMap.Entry firstEntry() { return map.lastEntry(); }
		@Override
		public Long2BooleanMap.Entry lastEntry() { return map.firstEntry(); }
		@Override
		public Long2BooleanMap.Entry pollFirstEntry() { return map.pollLastEntry(); }
		@Override
		public Long2BooleanMap.Entry pollLastEntry() { return map.pollFirstEntry(); }
		@Override
		public ObjectOrderedSet<Long2BooleanMap.Entry> long2BooleanEntrySet() { return new AbstractObjectSet.ReversedObjectOrderedSet<>(map.long2BooleanEntrySet()); }
		@Override
		public LongOrderedSet keySet() { return new AbstractLongSet.ReversedLongOrderedSet(map.keySet()); }
		@Override
		public BooleanOrderedCollection values() { return map.values().reversed(); }
		@Override
		public Long2BooleanOrderedMap reversed() { return map; }
	}

	
	/**
	 * A Simple Type Specific Entry class to reduce boxing/unboxing
	 */
	public static class BasicEntry implements Long2BooleanMap.Entry {
		protected long key;
		protected boolean value;
		
		/**
		 * A basic Empty constructor
		 */
		public BasicEntry() {}
		/**
		 * A Boxed Constructor for supporting java variants
		 * @param key the key of a entry
		 * @param value the value of a entry
		 */
		public BasicEntry(Long key, Boolean value) {
			this.key = key.longValue();
			this.value = value.booleanValue();
		}
		
		/**
		 * A Type Specific Constructor
		 * @param key the key of a entry
		 * @param value the value of a entry 
		 */
		public BasicEntry(long key, boolean value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * A Helper method for fast replacing values
		 * @param key the key that should be replaced
		 * @param value the value that should be replaced
		 */
		public void set(long key, boolean value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public long getLongKey() {
			return key;
		}

		@Override
		public boolean getBooleanValue() {
			return value;
		}

		@Override
		public boolean setValue(boolean value) {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Map.Entry) {
				if(obj instanceof Long2BooleanMap.Entry) {
					Long2BooleanMap.Entry entry = (Long2BooleanMap.Entry)obj;
					return key == entry.getLongKey() && value == entry.getBooleanValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Long && value instanceof Boolean && this.key == ((Long)key).longValue() && this.value == ((Boolean)value).booleanValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Long.hashCode(key) ^ Boolean.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Long.toString(key) + "=" + Boolean.toString(value);
		}
	}
}