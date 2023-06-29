package speiger.src.collections.longs.maps.abstracts;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

import speiger.src.collections.longs.collections.LongIterator;
import speiger.src.collections.longs.functions.consumer.LongIntConsumer;
import speiger.src.collections.longs.functions.function.Long2IntFunction;
import speiger.src.collections.longs.functions.function.LongIntUnaryOperator;
import speiger.src.collections.longs.maps.interfaces.Long2IntMap;
import speiger.src.collections.longs.sets.AbstractLongSet;
import speiger.src.collections.longs.sets.LongSet;
import speiger.src.collections.longs.utils.maps.Long2IntMaps;
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
public abstract class AbstractLong2IntMap extends AbstractMap<Long, Integer> implements Long2IntMap
{
	protected int defaultReturnValue = 0;
	
	@Override
	public int getDefaultReturnValue() {
		return defaultReturnValue;
	}
	
	@Override
	public AbstractLong2IntMap setDefaultReturnValue(int v) {
		defaultReturnValue = v;
		return this;
	}
	
	protected ObjectIterable<Long2IntMap.Entry> getFastIterable(Long2IntMap map) {
		return Long2IntMaps.fastIterable(map);
	}
	
	protected ObjectIterator<Long2IntMap.Entry> getFastIterator(Long2IntMap map) {
		return Long2IntMaps.fastIterator(map);
	}
	
	@Override
	public Long2IntMap copy() { 
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public Integer put(Long key, Integer value) {
		return Integer.valueOf(put(key.longValue(), value.intValue()));
	}
	
	@Override
	public void addToAll(Long2IntMap m) {
		for(Long2IntMap.Entry entry : getFastIterable(m))
			addTo(entry.getLongKey(), entry.getIntValue());
	}
	
	@Override
	public void putAll(Long2IntMap m) {
		for(ObjectIterator<Long2IntMap.Entry> iter = getFastIterator(m);iter.hasNext();) {
			Long2IntMap.Entry entry = iter.next();
			put(entry.getLongKey(), entry.getIntValue());
		}
	}
	
	@Override
	public void putAll(Map<? extends Long, ? extends Integer> m)
	{
		if(m instanceof Long2IntMap) putAll((Long2IntMap)m);
		else super.putAll(m);
	}
	
	@Override
	public void putAll(long[] keys, int[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);
	}
	
	@Override
	public void putAll(Long[] keys, Integer[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);		
	}
	
	@Override
	public void putAllIfAbsent(Long2IntMap m) {
		for(Long2IntMap.Entry entry : getFastIterable(m))
			putIfAbsent(entry.getLongKey(), entry.getIntValue());
	}
	
	
	@Override
	public boolean containsKey(long key) {
		for(LongIterator iter = keySet().iterator();iter.hasNext();)
			if(iter.nextLong() == key) return true;
		return false;
	}
	
	@Override
	public boolean containsValue(int value) {
		for(IntIterator iter = values().iterator();iter.hasNext();)
			if(iter.nextInt() == value) return true;
		return false;
	}
	
	@Override
	public boolean replace(long key, int oldValue, int newValue) {
		int curValue = get(key);
		if (curValue != oldValue || (curValue == getDefaultReturnValue() && !containsKey(key))) {
			return false;
		}
		put(key, newValue);
		return true;
	}

	@Override
	public int replace(long key, int value) {
		int curValue;
		if ((curValue = get(key)) != getDefaultReturnValue() || containsKey(key)) {
			curValue = put(key, value);
		}
		return curValue;
	}
	
	@Override
	public void replaceInts(Long2IntMap m) {
		for(Long2IntMap.Entry entry : getFastIterable(m))
			replace(entry.getLongKey(), entry.getIntValue());
	}
	
	@Override
	public void replaceInts(LongIntUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(ObjectIterator<Long2IntMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Long2IntMap.Entry entry = iter.next();
			entry.setValue(mappingFunction.applyAsInt(entry.getLongKey(), entry.getIntValue()));
		}
	}

	@Override
	public int computeInt(long key, LongIntUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int newValue = mappingFunction.applyAsInt(key, get(key));
		put(key, newValue);
		return newValue;
	}
	
	@Override
	public int computeIntIfAbsent(long key, Long2IntFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(!containsKey(key)) {
			int newValue = mappingFunction.applyAsInt(key);
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public int supplyIntIfAbsent(long key, IntSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		if(!containsKey(key)) {
			int newValue = valueProvider.getAsInt();
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public int computeIntIfPresent(long key, LongIntUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(containsKey(key)) {
			int newValue = mappingFunction.applyAsInt(key, get(key));
			put(key, newValue);
			return newValue;
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public int computeIntNonDefault(long key, LongIntUnaryOperator mappingFunction) {
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
	public int computeIntIfAbsentNonDefault(long key, Long2IntFunction mappingFunction) {
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
	public int supplyIntIfAbsentNonDefault(long key, IntSupplier valueProvider) {
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
	public int computeIntIfPresentNonDefault(long key, LongIntUnaryOperator mappingFunction) {
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
	public int mergeInt(long key, int value, IntIntUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int oldValue = get(key);
		int newValue = oldValue == getDefaultReturnValue() ? value : mappingFunction.applyAsInt(oldValue, value);
		if(newValue == getDefaultReturnValue()) remove(key);
		else put(key, newValue);
		return newValue;
	}
	
	@Override
	public void mergeAllInt(Long2IntMap m, IntIntUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Long2IntMap.Entry entry : getFastIterable(m)) {
			long key = entry.getLongKey();
			int oldValue = get(key);
			int newValue = oldValue == getDefaultReturnValue() ? entry.getIntValue() : mappingFunction.applyAsInt(oldValue, entry.getIntValue());
			if(newValue == getDefaultReturnValue()) remove(key);
			else put(key, newValue);
		}
	}
	
	@Override
	public Integer get(Object key) {
		return Integer.valueOf(key instanceof Long ? get(((Long)key).longValue()) : getDefaultReturnValue());
	}
	
	@Override
	public Integer getOrDefault(Object key, Integer defaultValue) {
		return Integer.valueOf(key instanceof Long ? getOrDefault(((Long)key).longValue(), defaultValue.intValue()) : getDefaultReturnValue());
	}
	
	@Override
	public int getOrDefault(long key, int defaultValue) {
		int value = get(key);
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	
	@Override
	public Integer remove(Object key) {
		return key instanceof Long ? Integer.valueOf(remove(((Long)key).longValue())) : Integer.valueOf(getDefaultReturnValue());
	}
	
	@Override
	public void forEach(LongIntConsumer action) {
		Objects.requireNonNull(action);
		for(ObjectIterator<Long2IntMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Long2IntMap.Entry entry = iter.next();
			action.accept(entry.getLongKey(), entry.getIntValue());
		}
	}

	@Override
	public LongSet keySet() {
		return new AbstractLongSet() {
			@Override
			public boolean remove(long o) {
				return AbstractLong2IntMap.this.remove(o) != getDefaultReturnValue();
			}
			
			@Override
			public boolean add(long o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public LongIterator iterator() {
				return new LongIterator() {
					ObjectIterator<Long2IntMap.Entry> iter = getFastIterator(AbstractLong2IntMap.this);
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
				return AbstractLong2IntMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractLong2IntMap.this.clear();
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
				return AbstractLong2IntMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractLong2IntMap.this.clear();
			}
			
			@Override
			public IntIterator iterator() {
				return new IntIterator() {
					ObjectIterator<Long2IntMap.Entry> iter = getFastIterator(AbstractLong2IntMap.this);
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
	public ObjectSet<Map.Entry<Long, Integer>> entrySet() {
		return (ObjectSet)long2IntEntrySet();
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o instanceof Map) {
			if(size() != ((Map<?, ?>)o).size()) return false;
			if(o instanceof Long2IntMap) return long2IntEntrySet().containsAll(((Long2IntMap)o).long2IntEntrySet());
			return long2IntEntrySet().containsAll(((Map<?, ?>)o).entrySet());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		ObjectIterator<Long2IntMap.Entry> iter = getFastIterator(this);
		while(iter.hasNext()) hash += iter.next().hashCode();
		return hash;
	}
	
	/**
	 * A Simple Type Specific Entry class to reduce boxing/unboxing
	 */
	public static class BasicEntry implements Long2IntMap.Entry {
		protected long key;
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
		public BasicEntry(Long key, Integer value) {
			this.key = key.longValue();
			this.value = value.intValue();
		}
		
		/**
		 * A Type Specific Constructor
		 * @param key the key of a entry
		 * @param value the value of a entry 
		 */
		public BasicEntry(long key, int value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * A Helper method for fast replacing values
		 * @param key the key that should be replaced
		 * @param value the value that should be replaced
		 */
		public void set(long key, int value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public long getLongKey() {
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
				if(obj instanceof Long2IntMap.Entry) {
					Long2IntMap.Entry entry = (Long2IntMap.Entry)obj;
					return key == entry.getLongKey() && value == entry.getIntValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Long && value instanceof Integer && this.key == ((Long)key).longValue() && this.value == ((Integer)value).intValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Long.hashCode(key) ^ Integer.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Long.toString(key) + "=" + Integer.toString(value);
		}
	}
}