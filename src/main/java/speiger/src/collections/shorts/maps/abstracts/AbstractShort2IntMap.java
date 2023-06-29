package speiger.src.collections.shorts.maps.abstracts;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

import speiger.src.collections.shorts.collections.ShortIterator;
import speiger.src.collections.shorts.functions.consumer.ShortIntConsumer;
import speiger.src.collections.shorts.functions.function.Short2IntFunction;
import speiger.src.collections.shorts.functions.function.ShortIntUnaryOperator;
import speiger.src.collections.shorts.maps.interfaces.Short2IntMap;
import speiger.src.collections.shorts.sets.AbstractShortSet;
import speiger.src.collections.shorts.sets.ShortSet;
import speiger.src.collections.shorts.utils.maps.Short2IntMaps;
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
public abstract class AbstractShort2IntMap extends AbstractMap<Short, Integer> implements Short2IntMap
{
	protected int defaultReturnValue = 0;
	
	@Override
	public int getDefaultReturnValue() {
		return defaultReturnValue;
	}
	
	@Override
	public AbstractShort2IntMap setDefaultReturnValue(int v) {
		defaultReturnValue = v;
		return this;
	}
	
	protected ObjectIterable<Short2IntMap.Entry> getFastIterable(Short2IntMap map) {
		return Short2IntMaps.fastIterable(map);
	}
	
	protected ObjectIterator<Short2IntMap.Entry> getFastIterator(Short2IntMap map) {
		return Short2IntMaps.fastIterator(map);
	}
	
	@Override
	public Short2IntMap copy() { 
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public Integer put(Short key, Integer value) {
		return Integer.valueOf(put(key.shortValue(), value.intValue()));
	}
	
	@Override
	public void addToAll(Short2IntMap m) {
		for(Short2IntMap.Entry entry : getFastIterable(m))
			addTo(entry.getShortKey(), entry.getIntValue());
	}
	
	@Override
	public void putAll(Short2IntMap m) {
		for(ObjectIterator<Short2IntMap.Entry> iter = getFastIterator(m);iter.hasNext();) {
			Short2IntMap.Entry entry = iter.next();
			put(entry.getShortKey(), entry.getIntValue());
		}
	}
	
	@Override
	public void putAll(Map<? extends Short, ? extends Integer> m)
	{
		if(m instanceof Short2IntMap) putAll((Short2IntMap)m);
		else super.putAll(m);
	}
	
	@Override
	public void putAll(short[] keys, int[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);
	}
	
	@Override
	public void putAll(Short[] keys, Integer[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);		
	}
	
	@Override
	public void putAllIfAbsent(Short2IntMap m) {
		for(Short2IntMap.Entry entry : getFastIterable(m))
			putIfAbsent(entry.getShortKey(), entry.getIntValue());
	}
	
	
	@Override
	public boolean containsKey(short key) {
		for(ShortIterator iter = keySet().iterator();iter.hasNext();)
			if(iter.nextShort() == key) return true;
		return false;
	}
	
	@Override
	public boolean containsValue(int value) {
		for(IntIterator iter = values().iterator();iter.hasNext();)
			if(iter.nextInt() == value) return true;
		return false;
	}
	
	@Override
	public boolean replace(short key, int oldValue, int newValue) {
		int curValue = get(key);
		if (curValue != oldValue || (curValue == getDefaultReturnValue() && !containsKey(key))) {
			return false;
		}
		put(key, newValue);
		return true;
	}

	@Override
	public int replace(short key, int value) {
		int curValue;
		if ((curValue = get(key)) != getDefaultReturnValue() || containsKey(key)) {
			curValue = put(key, value);
		}
		return curValue;
	}
	
	@Override
	public void replaceInts(Short2IntMap m) {
		for(Short2IntMap.Entry entry : getFastIterable(m))
			replace(entry.getShortKey(), entry.getIntValue());
	}
	
	@Override
	public void replaceInts(ShortIntUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(ObjectIterator<Short2IntMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Short2IntMap.Entry entry = iter.next();
			entry.setValue(mappingFunction.applyAsInt(entry.getShortKey(), entry.getIntValue()));
		}
	}

	@Override
	public int computeInt(short key, ShortIntUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int newValue = mappingFunction.applyAsInt(key, get(key));
		put(key, newValue);
		return newValue;
	}
	
	@Override
	public int computeIntIfAbsent(short key, Short2IntFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(!containsKey(key)) {
			int newValue = mappingFunction.applyAsInt(key);
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public int supplyIntIfAbsent(short key, IntSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		if(!containsKey(key)) {
			int newValue = valueProvider.getAsInt();
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public int computeIntIfPresent(short key, ShortIntUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(containsKey(key)) {
			int newValue = mappingFunction.applyAsInt(key, get(key));
			put(key, newValue);
			return newValue;
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public int computeIntNonDefault(short key, ShortIntUnaryOperator mappingFunction) {
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
	public int computeIntIfAbsentNonDefault(short key, Short2IntFunction mappingFunction) {
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
	public int supplyIntIfAbsentNonDefault(short key, IntSupplier valueProvider) {
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
	public int computeIntIfPresentNonDefault(short key, ShortIntUnaryOperator mappingFunction) {
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
	public int mergeInt(short key, int value, IntIntUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int oldValue = get(key);
		int newValue = oldValue == getDefaultReturnValue() ? value : mappingFunction.applyAsInt(oldValue, value);
		if(newValue == getDefaultReturnValue()) remove(key);
		else put(key, newValue);
		return newValue;
	}
	
	@Override
	public void mergeAllInt(Short2IntMap m, IntIntUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Short2IntMap.Entry entry : getFastIterable(m)) {
			short key = entry.getShortKey();
			int oldValue = get(key);
			int newValue = oldValue == getDefaultReturnValue() ? entry.getIntValue() : mappingFunction.applyAsInt(oldValue, entry.getIntValue());
			if(newValue == getDefaultReturnValue()) remove(key);
			else put(key, newValue);
		}
	}
	
	@Override
	public Integer get(Object key) {
		return Integer.valueOf(key instanceof Short ? get(((Short)key).shortValue()) : getDefaultReturnValue());
	}
	
	@Override
	public Integer getOrDefault(Object key, Integer defaultValue) {
		return Integer.valueOf(key instanceof Short ? getOrDefault(((Short)key).shortValue(), defaultValue.intValue()) : getDefaultReturnValue());
	}
	
	@Override
	public int getOrDefault(short key, int defaultValue) {
		int value = get(key);
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	
	@Override
	public Integer remove(Object key) {
		return key instanceof Short ? Integer.valueOf(remove(((Short)key).shortValue())) : Integer.valueOf(getDefaultReturnValue());
	}
	
	@Override
	public void forEach(ShortIntConsumer action) {
		Objects.requireNonNull(action);
		for(ObjectIterator<Short2IntMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Short2IntMap.Entry entry = iter.next();
			action.accept(entry.getShortKey(), entry.getIntValue());
		}
	}

	@Override
	public ShortSet keySet() {
		return new AbstractShortSet() {
			@Override
			public boolean remove(short o) {
				return AbstractShort2IntMap.this.remove(o) != getDefaultReturnValue();
			}
			
			@Override
			public boolean add(short o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public ShortIterator iterator() {
				return new ShortIterator() {
					ObjectIterator<Short2IntMap.Entry> iter = getFastIterator(AbstractShort2IntMap.this);
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
				return AbstractShort2IntMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractShort2IntMap.this.clear();
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
				return AbstractShort2IntMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractShort2IntMap.this.clear();
			}
			
			@Override
			public IntIterator iterator() {
				return new IntIterator() {
					ObjectIterator<Short2IntMap.Entry> iter = getFastIterator(AbstractShort2IntMap.this);
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
	public ObjectSet<Map.Entry<Short, Integer>> entrySet() {
		return (ObjectSet)short2IntEntrySet();
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o instanceof Map) {
			if(size() != ((Map<?, ?>)o).size()) return false;
			if(o instanceof Short2IntMap) return short2IntEntrySet().containsAll(((Short2IntMap)o).short2IntEntrySet());
			return short2IntEntrySet().containsAll(((Map<?, ?>)o).entrySet());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		ObjectIterator<Short2IntMap.Entry> iter = getFastIterator(this);
		while(iter.hasNext()) hash += iter.next().hashCode();
		return hash;
	}
	
	/**
	 * A Simple Type Specific Entry class to reduce boxing/unboxing
	 */
	public static class BasicEntry implements Short2IntMap.Entry {
		protected short key;
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
		public BasicEntry(Short key, Integer value) {
			this.key = key.shortValue();
			this.value = value.intValue();
		}
		
		/**
		 * A Type Specific Constructor
		 * @param key the key of a entry
		 * @param value the value of a entry 
		 */
		public BasicEntry(short key, int value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * A Helper method for fast replacing values
		 * @param key the key that should be replaced
		 * @param value the value that should be replaced
		 */
		public void set(short key, int value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public short getShortKey() {
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
				if(obj instanceof Short2IntMap.Entry) {
					Short2IntMap.Entry entry = (Short2IntMap.Entry)obj;
					return key == entry.getShortKey() && value == entry.getIntValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Short && value instanceof Integer && this.key == ((Short)key).shortValue() && this.value == ((Integer)value).intValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Short.hashCode(key) ^ Integer.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Short.toString(key) + "=" + Integer.toString(value);
		}
	}
}