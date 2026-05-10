package speiger.src.collections.ints.maps.abstracts;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.IntPredicate;

import speiger.src.collections.ints.collections.IntIterator;
import speiger.src.collections.ints.functions.consumer.IntBooleanConsumer;
import speiger.src.collections.ints.functions.function.IntBooleanUnaryOperator;
import speiger.src.collections.ints.maps.interfaces.Int2BooleanMap;
import speiger.src.collections.ints.maps.interfaces.Int2BooleanOrderedMap;
import speiger.src.collections.ints.sets.IntOrderedSet;
import speiger.src.collections.booleans.collections.BooleanOrderedCollection;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
import speiger.src.collections.ints.sets.AbstractIntSet;
import speiger.src.collections.ints.sets.IntSet;
import speiger.src.collections.ints.utils.maps.Int2BooleanMaps;
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
public abstract class AbstractInt2BooleanMap extends AbstractMap<Integer, Boolean> implements Int2BooleanMap
{
	protected boolean defaultReturnValue = false;
	
	@Override
	public boolean getDefaultReturnValue() {
		return defaultReturnValue;
	}
	
	@Override
	public AbstractInt2BooleanMap setDefaultReturnValue(boolean v) {
		defaultReturnValue = v;
		return this;
	}
	
	protected ObjectIterable<Int2BooleanMap.Entry> getFastIterable(Int2BooleanMap map) {
		return Int2BooleanMaps.fastIterable(map);
	}
	
	protected ObjectIterator<Int2BooleanMap.Entry> getFastIterator(Int2BooleanMap map) {
		return Int2BooleanMaps.fastIterator(map);
	}
	
	@Override
	public Int2BooleanMap copy() { 
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public Boolean put(Integer key, Boolean value) {
		return Boolean.valueOf(put(key.intValue(), value.booleanValue()));
	}
	
	@Override
	public void putAll(Int2BooleanMap m) {
		for(ObjectIterator<Int2BooleanMap.Entry> iter = getFastIterator(m);iter.hasNext();) {
			Int2BooleanMap.Entry entry = iter.next();
			put(entry.getIntKey(), entry.getBooleanValue());
		}
	}
	
	@Override
	public void putAll(Map<? extends Integer, ? extends Boolean> m)
	{
		if(m instanceof Int2BooleanMap) putAll((Int2BooleanMap)m);
		else super.putAll(m);
	}
	
	@Override
	public void putAll(int[] keys, boolean[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);
	}
	
	@Override
	public void putAll(Integer[] keys, Boolean[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i].intValue(), values[i].booleanValue());		
	}
	
	@Override
	public void putAllIfAbsent(Int2BooleanMap m) {
		for(Int2BooleanMap.Entry entry : getFastIterable(m))
			putIfAbsent(entry.getIntKey(), entry.getBooleanValue());
	}
	
	
	@Override
	public boolean containsKey(int key) {
		for(IntIterator iter = keySet().iterator();iter.hasNext();)
			if(iter.nextInt() == key) return true;
		return false;
	}
	
	@Override
	public boolean containsValue(boolean value) {
		for(BooleanIterator iter = values().iterator();iter.hasNext();)
			if(iter.nextBoolean() == value) return true;
		return false;
	}
	
	@Override
	public boolean replace(int key, boolean oldValue, boolean newValue) {
		boolean curValue = get(key);
		if (curValue != oldValue || (curValue == getDefaultReturnValue() && !containsKey(key))) {
			return false;
		}
		put(key, newValue);
		return true;
	}

	@Override
	public boolean replace(int key, boolean value) {
		boolean curValue;
		if ((curValue = get(key)) != getDefaultReturnValue() || containsKey(key)) {
			curValue = put(key, value);
		}
		return curValue;
	}
	
	@Override
	public void replaceBooleans(Int2BooleanMap m) {
		for(Int2BooleanMap.Entry entry : getFastIterable(m))
			replace(entry.getIntKey(), entry.getBooleanValue());
	}
	
	@Override
	public void replaceBooleans(IntBooleanUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(ObjectIterator<Int2BooleanMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Int2BooleanMap.Entry entry = iter.next();
			entry.setValue(mappingFunction.applyAsBoolean(entry.getIntKey(), entry.getBooleanValue()));
		}
	}

	@Override
	public boolean computeBoolean(int key, IntBooleanUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		boolean newValue = mappingFunction.applyAsBoolean(key, get(key));
		put(key, newValue);
		return newValue;
	}
	
	@Override
	public boolean computeBooleanIfAbsent(int key, IntPredicate mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(!containsKey(key)) {
			boolean newValue = mappingFunction.test(key);
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public boolean supplyBooleanIfAbsent(int key, BooleanSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		if(!containsKey(key)) {
			boolean newValue = valueProvider.getAsBoolean();
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public boolean computeBooleanIfPresent(int key, IntBooleanUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(containsKey(key)) {
			boolean newValue = mappingFunction.applyAsBoolean(key, get(key));
			put(key, newValue);
			return newValue;
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public boolean computeBooleanNonDefault(int key, IntBooleanUnaryOperator mappingFunction) {
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
	public boolean computeBooleanIfAbsentNonDefault(int key, IntPredicate mappingFunction) {
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
	public boolean supplyBooleanIfAbsentNonDefault(int key, BooleanSupplier valueProvider) {
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
	public boolean computeBooleanIfPresentNonDefault(int key, IntBooleanUnaryOperator mappingFunction) {
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
	public boolean mergeBoolean(int key, boolean value, BooleanBooleanUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		boolean oldValue = get(key);
		boolean newValue = oldValue == getDefaultReturnValue() ? value : mappingFunction.applyAsBoolean(oldValue, value);
		if(newValue == getDefaultReturnValue()) remove(key);
		else put(key, newValue);
		return newValue;
	}
	
	@Override
	public void mergeAllBoolean(Int2BooleanMap m, BooleanBooleanUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Int2BooleanMap.Entry entry : getFastIterable(m)) {
			int key = entry.getIntKey();
			boolean oldValue = get(key);
			boolean newValue = oldValue == getDefaultReturnValue() ? entry.getBooleanValue() : mappingFunction.applyAsBoolean(oldValue, entry.getBooleanValue());
			if(newValue == getDefaultReturnValue()) remove(key);
			else put(key, newValue);
		}
	}
	
	@Override
	public Boolean get(Object key) {
		return Boolean.valueOf(key instanceof Integer ? get(((Integer)key).intValue()) : getDefaultReturnValue());
	}
	
	@Override
	public Boolean getOrDefault(Object key, Boolean defaultValue) {
		return Boolean.valueOf(key instanceof Integer ? getOrDefault(((Integer)key).intValue(), defaultValue.booleanValue()) : getDefaultReturnValue());
	}
	
	@Override
	public boolean getOrDefault(int key, boolean defaultValue) {
		boolean value = get(key);
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	
	@Override
	public Boolean remove(Object key) {
		return key instanceof Integer ? Boolean.valueOf(remove(((Integer)key).intValue())) : Boolean.valueOf(getDefaultReturnValue());
	}
	
	@Override
	public void forEach(IntBooleanConsumer action) {
		Objects.requireNonNull(action);
		for(ObjectIterator<Int2BooleanMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Int2BooleanMap.Entry entry = iter.next();
			action.accept(entry.getIntKey(), entry.getBooleanValue());
		}
	}

	@Override
	public IntSet keySet() {
		return new AbstractIntSet() {
			@Override
			public boolean remove(int o) {
				return AbstractInt2BooleanMap.this.remove(o) != getDefaultReturnValue();
			}
			
			@Override
			public boolean add(int o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public IntIterator iterator() {
				return new IntIterator() {
					ObjectIterator<Int2BooleanMap.Entry> iter = getFastIterator(AbstractInt2BooleanMap.this);
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
				return AbstractInt2BooleanMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractInt2BooleanMap.this.clear();
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
				return AbstractInt2BooleanMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractInt2BooleanMap.this.clear();
			}
			
			@Override
			public BooleanIterator iterator() {
				return new BooleanIterator() {
					ObjectIterator<Int2BooleanMap.Entry> iter = getFastIterator(AbstractInt2BooleanMap.this);
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
	public ObjectSet<Map.Entry<Integer, Boolean>> entrySet() {
		return (ObjectSet)int2BooleanEntrySet();
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o instanceof Map) {
			if(size() != ((Map<?, ?>)o).size()) return false;
			if(o instanceof Int2BooleanMap) return int2BooleanEntrySet().containsAll(((Int2BooleanMap)o).int2BooleanEntrySet());
			return int2BooleanEntrySet().containsAll(((Map<?, ?>)o).entrySet());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		ObjectIterator<Int2BooleanMap.Entry> iter = getFastIterator(this);
		while(iter.hasNext()) hash += iter.next().hashCode();
		return hash;
	}
	
	public static class ReversedInt2BooleanOrderedMap extends AbstractInt2BooleanMap implements Int2BooleanOrderedMap {
		Int2BooleanOrderedMap map;
		
		public ReversedInt2BooleanOrderedMap(Int2BooleanOrderedMap map) {
			this.map = map;
		}
		@Override
		public AbstractInt2BooleanMap setDefaultReturnValue(boolean v) {
			map.setDefaultReturnValue(v);
			return this;
		}
		@Override
		public boolean getDefaultReturnValue() { return map.getDefaultReturnValue(); }
		@Override
		public Int2BooleanOrderedMap copy() { throw new UnsupportedOperationException(); }
		@Override
		public boolean put(int key, boolean value) { return map.put(key, value); }
		@Override
		public boolean putIfAbsent(int key, boolean value) { return map.putIfAbsent(key, value); }
		@Override
		public boolean remove(int key) { return map.remove(key); }
		@Override
		public boolean remove(int key, boolean value) { return map.remove(key, value); }
		@Override
		public boolean removeOrDefault(int key, boolean defaultValue) { return map.removeOrDefault(key, defaultValue); }
		@Override
		public boolean containsKey(int key) { return map.containsKey(key); }
		@Override
		public boolean containsValue(boolean value) { return map.containsValue(value); }
		@Override
		public boolean replace(int key, boolean oldValue, boolean newValue) { return map.replace(key, oldValue, newValue); }
		@Override
		public boolean replace(int key, boolean value) { return map.replace(key, value); }
		@Override
		public void replaceBooleans(Int2BooleanMap m) { map.replaceBooleans(m); }
		@Override
		public void replaceBooleans(IntBooleanUnaryOperator mappingFunction) { map.replaceBooleans(mappingFunction); }
		@Override
		public boolean computeBoolean(int key, IntBooleanUnaryOperator mappingFunction) { return map.computeBoolean(key, mappingFunction); }
		@Override
		public boolean computeBooleanIfAbsent(int key, IntPredicate mappingFunction) { return map.computeBooleanIfAbsent(key, mappingFunction); }
		@Override
		public boolean supplyBooleanIfAbsent(int key, BooleanSupplier valueProvider) { return map.supplyBooleanIfAbsent(key, valueProvider); }
		@Override
		public boolean computeBooleanIfPresent(int key, IntBooleanUnaryOperator mappingFunction) { return map.computeBooleanIfPresent(key, mappingFunction); }
		@Override
		public boolean computeBooleanNonDefault(int key, IntBooleanUnaryOperator mappingFunction) { return map.computeBooleanNonDefault(key, mappingFunction); }
		@Override
		public boolean computeBooleanIfAbsentNonDefault(int key, IntPredicate mappingFunction) { return map.computeBooleanIfAbsentNonDefault(key, mappingFunction); }
		@Override
		public boolean supplyBooleanIfAbsentNonDefault(int key, BooleanSupplier valueProvider) { return map.supplyBooleanIfAbsentNonDefault(key, valueProvider); }
		@Override
		public boolean computeBooleanIfPresentNonDefault(int key, IntBooleanUnaryOperator mappingFunction) { return map.computeBooleanIfPresentNonDefault(key, mappingFunction); }
		@Override
		public boolean mergeBoolean(int key, boolean value, BooleanBooleanUnaryOperator mappingFunction) { return map.mergeBoolean(key, value, mappingFunction); }
		@Override
		public boolean getOrDefault(int key, boolean defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public boolean get(int key) { return map.get(key); }
		@Override
		public boolean putAndMoveToFirst(int key, boolean value) { return map.putAndMoveToLast(key, value); }
		@Override
		public boolean putAndMoveToLast(int key, boolean value) { return map.putAndMoveToFirst(key, value); }
		@Override
		public boolean putFirst(int key, boolean value) { return map.putLast(key, value); }
		@Override
		public boolean putLast(int key, boolean value) { return map.putFirst(key, value); }
		@Override
		public boolean moveToFirst(int key) { return map.moveToLast(key); }
		@Override
		public boolean moveToLast(int key) { return map.moveToFirst(key); }
		@Override
		public boolean getAndMoveToFirst(int key) { return map.getAndMoveToLast(key); }
		@Override
		public boolean getAndMoveToLast(int key) { return map.getAndMoveToFirst(key); }
		@Override
		public int firstIntKey() { return map.lastIntKey(); }
		@Override
		public int pollFirstIntKey() { return map.pollLastIntKey(); }
		@Override
		public int lastIntKey() { return map.firstIntKey(); }
		@Override
		public int pollLastIntKey() { return map.pollFirstIntKey(); }
		@Override
		public boolean firstBooleanValue() { return map.lastBooleanValue(); }
		@Override
		public boolean lastBooleanValue() { return map.firstBooleanValue(); }
		@Override
		public Int2BooleanMap.Entry firstEntry() { return map.lastEntry(); }
		@Override
		public Int2BooleanMap.Entry lastEntry() { return map.firstEntry(); }
		@Override
		public Int2BooleanMap.Entry pollFirstEntry() { return map.pollLastEntry(); }
		@Override
		public Int2BooleanMap.Entry pollLastEntry() { return map.pollFirstEntry(); }
		@Override
		public ObjectOrderedSet<Int2BooleanMap.Entry> int2BooleanEntrySet() { return new AbstractObjectSet.ReversedObjectOrderedSet<>(map.int2BooleanEntrySet()); }
		@Override
		public IntOrderedSet keySet() { return new AbstractIntSet.ReversedIntOrderedSet(map.keySet()); }
		@Override
		public BooleanOrderedCollection values() { return map.values().reversed(); }
		@Override
		public Int2BooleanOrderedMap reversed() { return map; }
	}

	
	/**
	 * A Simple Type Specific Entry class to reduce boxing/unboxing
	 */
	public static class BasicEntry implements Int2BooleanMap.Entry {
		protected int key;
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
		public BasicEntry(Integer key, Boolean value) {
			this.key = key.intValue();
			this.value = value.booleanValue();
		}
		
		/**
		 * A Type Specific Constructor
		 * @param key the key of a entry
		 * @param value the value of a entry 
		 */
		public BasicEntry(int key, boolean value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * A Helper method for fast replacing values
		 * @param key the key that should be replaced
		 * @param value the value that should be replaced
		 */
		public void set(int key, boolean value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public int getIntKey() {
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
				if(obj instanceof Int2BooleanMap.Entry) {
					Int2BooleanMap.Entry entry = (Int2BooleanMap.Entry)obj;
					return key == entry.getIntKey() && value == entry.getBooleanValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Integer && value instanceof Boolean && this.key == ((Integer)key).intValue() && this.value == ((Boolean)value).booleanValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Integer.hashCode(key) ^ Boolean.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Integer.toString(key) + "=" + Boolean.toString(value);
		}
	}
}