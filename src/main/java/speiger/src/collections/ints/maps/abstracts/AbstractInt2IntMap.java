package speiger.src.collections.ints.maps.abstracts;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

import speiger.src.collections.ints.collections.IntIterator;
import speiger.src.collections.ints.functions.consumer.IntIntConsumer;
import speiger.src.collections.ints.functions.function.Int2IntFunction;
import speiger.src.collections.ints.functions.function.IntIntUnaryOperator;
import speiger.src.collections.ints.maps.interfaces.Int2IntMap;
import speiger.src.collections.ints.sets.AbstractIntSet;
import speiger.src.collections.ints.sets.IntSet;
import speiger.src.collections.ints.utils.maps.Int2IntMaps;
import speiger.src.collections.ints.collections.AbstractIntCollection;
import speiger.src.collections.ints.collections.IntCollection;
import speiger.src.collections.ints.functions.IntSupplier;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Base Implementation of a Type Specific Map to reduce boxing/unboxing
 */
public abstract class AbstractInt2IntMap extends AbstractMap<Integer, Integer> implements Int2IntMap
{
	protected int defaultReturnValue = 0;
	
	@Override
	public int getDefaultReturnValue() {
		return defaultReturnValue;
	}
	
	@Override
	public AbstractInt2IntMap setDefaultReturnValue(int v) {
		defaultReturnValue = v;
		return this;
	}
	
	@Override
	public Int2IntMap copy() { 
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public Integer put(Integer key, Integer value) {
		return Integer.valueOf(put(key.intValue(), value.intValue()));
	}
	
	@Override
	public void addToAll(Int2IntMap m) {
		for(Int2IntMap.Entry entry : Int2IntMaps.fastIterable(m))
			addTo(entry.getIntKey(), entry.getIntValue());
	}
	
	@Override
	public void putAll(Int2IntMap m) {
		for(ObjectIterator<Int2IntMap.Entry> iter = Int2IntMaps.fastIterator(m);iter.hasNext();) {
			Int2IntMap.Entry entry = iter.next();
			put(entry.getIntKey(), entry.getIntValue());
		}
	}
	
	@Override
	public void putAll(Map<? extends Integer, ? extends Integer> m)
	{
		if(m instanceof Int2IntMap) putAll((Int2IntMap)m);
		else super.putAll(m);
	}
	
	@Override
	public void putAll(int[] keys, int[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);
	}
	
	@Override
	public void putAll(Integer[] keys, Integer[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);		
	}
	
	@Override
	public void putAllIfAbsent(Int2IntMap m) {
		for(Int2IntMap.Entry entry : Int2IntMaps.fastIterable(m))
			putIfAbsent(entry.getIntKey(), entry.getIntValue());
	}
	
	
	@Override
	public boolean containsKey(int key) {
		for(IntIterator iter = keySet().iterator();iter.hasNext();)
			if(iter.nextInt() == key) return true;
		return false;
	}
	
	@Override
	public boolean containsValue(int value) {
		for(IntIterator iter = values().iterator();iter.hasNext();)
			if(iter.nextInt() == value) return true;
		return false;
	}
	
	@Override
	public boolean replace(int key, int oldValue, int newValue) {
		int curValue = get(key);
		if (curValue != oldValue || (curValue == getDefaultReturnValue() && !containsKey(key))) {
			return false;
		}
		put(key, newValue);
		return true;
	}

	@Override
	public int replace(int key, int value) {
		int curValue;
		if ((curValue = get(key)) != getDefaultReturnValue() || containsKey(key)) {
			curValue = put(key, value);
		}
		return curValue;
	}
	
	@Override
	public void replaceInts(Int2IntMap m) {
		for(Int2IntMap.Entry entry : Int2IntMaps.fastIterable(m))
			replace(entry.getIntKey(), entry.getIntValue());
	}
	
	@Override
	public void replaceInts(IntIntUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(ObjectIterator<Int2IntMap.Entry> iter = Int2IntMaps.fastIterator(this);iter.hasNext();) {
			Int2IntMap.Entry entry = iter.next();
			entry.setValue(mappingFunction.applyAsInt(entry.getIntKey(), entry.getIntValue()));
		}
	}

	@Override
	public int computeInt(int key, IntIntUnaryOperator mappingFunction) {
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
	public int computeIntIfAbsent(int key, Int2IntFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int value;
		if((value = get(key)) == getDefaultReturnValue() || !containsKey(key)) {
			int newValue = mappingFunction.get(key);
			if(newValue != getDefaultReturnValue()) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public int supplyIntIfAbsent(int key, IntSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		int value;
		if((value = get(key)) == getDefaultReturnValue() || !containsKey(key)) {
			int newValue = valueProvider.getInt();
			if(newValue != getDefaultReturnValue()) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public int computeIntIfPresent(int key, IntIntUnaryOperator mappingFunction) {
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
	public int mergeInt(int key, int value, IntIntUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int oldValue = get(key);
		int newValue = oldValue == getDefaultReturnValue() ? value : mappingFunction.applyAsInt(oldValue, value);
		if(newValue == getDefaultReturnValue()) remove(key);
		else put(key, newValue);
		return newValue;
	}
	
	@Override
	public void mergeAllInt(Int2IntMap m, IntIntUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Int2IntMap.Entry entry : Int2IntMaps.fastIterable(m)) {
			int key = entry.getIntKey();
			int oldValue = get(key);
			int newValue = oldValue == getDefaultReturnValue() ? entry.getIntValue() : mappingFunction.applyAsInt(oldValue, entry.getIntValue());
			if(newValue == getDefaultReturnValue()) remove(key);
			else put(key, newValue);
		}
	}
	
	@Override
	public Integer get(Object key) {
		return Integer.valueOf(key instanceof Integer ? get(((Integer)key).intValue()) : getDefaultReturnValue());
	}
	
	@Override
	public Integer getOrDefault(Object key, Integer defaultValue) {
		return Integer.valueOf(key instanceof Integer ? getOrDefault(((Integer)key).intValue(), defaultValue.intValue()) : getDefaultReturnValue());
	}
	
	@Override
	public int getOrDefault(int key, int defaultValue) {
		int value = get(key);
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	@Override
	public void forEach(IntIntConsumer action) {
		Objects.requireNonNull(action);
		for(ObjectIterator<Int2IntMap.Entry> iter = Int2IntMaps.fastIterator(this);iter.hasNext();) {
			Int2IntMap.Entry entry = iter.next();
			action.accept(entry.getIntKey(), entry.getIntValue());
		}
	}

	@Override
	public IntSet keySet() {
		return new AbstractIntSet() {
			@Override
			public boolean remove(int o) {
				return AbstractInt2IntMap.this.remove(o) != getDefaultReturnValue();
			}
			
			@Override
			public boolean add(int o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public IntIterator iterator() {
				return new IntIterator() {
					ObjectIterator<Int2IntMap.Entry> iter = Int2IntMaps.fastIterator(AbstractInt2IntMap.this);
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
				return AbstractInt2IntMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractInt2IntMap.this.clear();
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
				return AbstractInt2IntMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractInt2IntMap.this.clear();
			}
			
			@Override
			public IntIterator iterator() {
				return new IntIterator() {
					ObjectIterator<Int2IntMap.Entry> iter = Int2IntMaps.fastIterator(AbstractInt2IntMap.this);
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
	public ObjectSet<Map.Entry<Integer, Integer>> entrySet() {
		return (ObjectSet)int2IntEntrySet();
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o instanceof Map) {
			if(size() != ((Map<?, ?>)o).size()) return false;
			if(o instanceof Int2IntMap) return int2IntEntrySet().containsAll(((Int2IntMap)o).int2IntEntrySet());
			return int2IntEntrySet().containsAll(((Map<?, ?>)o).entrySet());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		ObjectIterator<Int2IntMap.Entry> iter = Int2IntMaps.fastIterator(this);
		while(iter.hasNext()) hash += iter.next().hashCode();
		return hash;
	}
	
	/**
	 * A Simple Type Specific Entry class to reduce boxing/unboxing
	 */
	public static class BasicEntry implements Int2IntMap.Entry {
		protected int key;
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
		public BasicEntry(Integer key, Integer value) {
			this.key = key.intValue();
			this.value = value.intValue();
		}
		
		/**
		 * A Type Specific Constructor
		 * @param key the key of a entry
		 * @param value the value of a entry 
		 */
		public BasicEntry(int key, int value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * A Helper method for fast replacing values
		 * @param key the key that should be replaced
		 * @param value the value that should be replaced
		 */
		public void set(int key, int value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public int getIntKey() {
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
				if(obj instanceof Int2IntMap.Entry) {
					Int2IntMap.Entry entry = (Int2IntMap.Entry)obj;
					return key == entry.getIntKey() && value == entry.getIntValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Integer && value instanceof Integer && this.key == ((Integer)key).intValue() && this.value == ((Integer)value).intValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Integer.hashCode(key) ^ Integer.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Integer.toString(key) + "=" + Integer.toString(value);
		}
	}
}