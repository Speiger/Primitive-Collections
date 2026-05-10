package speiger.src.collections.objects.maps.abstracts;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.functions.consumer.ObjectIntConsumer;
import speiger.src.collections.objects.functions.function.ToIntFunction;
import speiger.src.collections.objects.functions.function.ObjectIntUnaryOperator;
import speiger.src.collections.objects.maps.interfaces.Object2IntMap;
import speiger.src.collections.objects.maps.interfaces.Object2IntOrderedMap;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
import speiger.src.collections.ints.collections.IntOrderedCollection;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.utils.maps.Object2IntMaps;
import speiger.src.collections.ints.collections.AbstractIntCollection;
import speiger.src.collections.ints.collections.IntCollection;
import speiger.src.collections.ints.collections.IntIterator;
import speiger.src.collections.ints.functions.function.IntIntUnaryOperator;
import speiger.src.collections.ints.functions.IntSupplier;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Base Implementation of a Type Specific Map to reduce boxing/unboxing
 * @param <T> the keyType of elements maintained by this Collection
 */
public abstract class AbstractObject2IntMap<T> extends AbstractMap<T, Integer> implements Object2IntMap<T>
{
	protected int defaultReturnValue = -1;
	
	@Override
	public int getDefaultReturnValue() {
		return defaultReturnValue;
	}
	
	@Override
	public AbstractObject2IntMap<T> setDefaultReturnValue(int v) {
		defaultReturnValue = v;
		return this;
	}
	
	protected ObjectIterable<Object2IntMap.Entry<T>> getFastIterable(Object2IntMap<T> map) {
		return Object2IntMaps.fastIterable(map);
	}
	
	protected ObjectIterator<Object2IntMap.Entry<T>> getFastIterator(Object2IntMap<T> map) {
		return Object2IntMaps.fastIterator(map);
	}
	
	@Override
	public Object2IntMap<T> copy() { 
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public Integer put(T key, Integer value) {
		return Integer.valueOf(put(key, value.intValue()));
	}
	
	@Override
	public void addToAll(Object2IntMap<T> m) {
		for(Object2IntMap.Entry<T> entry : getFastIterable(m))
			addTo(entry.getKey(), entry.getIntValue());
	}
	
	@Override
	public void putAll(Object2IntMap<T> m) {
		for(ObjectIterator<Object2IntMap.Entry<T>> iter = getFastIterator(m);iter.hasNext();) {
			Object2IntMap.Entry<T> entry = iter.next();
			put(entry.getKey(), entry.getIntValue());
		}
	}
	
	@Override
	public void putAll(Map<? extends T, ? extends Integer> m)
	{
		if(m instanceof Object2IntMap) putAll((Object2IntMap<T>)m);
		else super.putAll(m);
	}
	
	@Override
	public void putAll(T[] keys, int[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);
	}
	
	@Override
	public void putAll(T[] keys, Integer[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i].intValue());		
	}
	
	@Override
	public void putAllIfAbsent(Object2IntMap<T> m) {
		for(Object2IntMap.Entry<T> entry : getFastIterable(m))
			putIfAbsent(entry.getKey(), entry.getIntValue());
	}
	
	
	@Override
	public boolean containsKey(Object key) {
		for(ObjectIterator<T> iter = keySet().iterator();iter.hasNext();)
			if(Objects.equals(key, iter.next())) return true;
		return false;
	}
	
	@Override
	public boolean containsValue(int value) {
		for(IntIterator iter = values().iterator();iter.hasNext();)
			if(iter.nextInt() == value) return true;
		return false;
	}
	
	@Override
	public boolean replace(T key, int oldValue, int newValue) {
		int curValue = getInt(key);
		if (curValue != oldValue || (curValue == getDefaultReturnValue() && !containsKey(key))) {
			return false;
		}
		put(key, newValue);
		return true;
	}

	@Override
	public int replace(T key, int value) {
		int curValue;
		if ((curValue = getInt(key)) != getDefaultReturnValue() || containsKey(key)) {
			curValue = put(key, value);
		}
		return curValue;
	}
	
	@Override
	public void replaceInts(Object2IntMap<T> m) {
		for(Object2IntMap.Entry<T> entry : getFastIterable(m))
			replace(entry.getKey(), entry.getIntValue());
	}
	
	@Override
	public void replaceInts(ObjectIntUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(ObjectIterator<Object2IntMap.Entry<T>> iter = getFastIterator(this);iter.hasNext();) {
			Object2IntMap.Entry<T> entry = iter.next();
			entry.setValue(mappingFunction.applyAsInt(entry.getKey(), entry.getIntValue()));
		}
	}

	@Override
	public int computeInt(T key, ObjectIntUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int newValue = mappingFunction.applyAsInt(key, getInt(key));
		put(key, newValue);
		return newValue;
	}
	
	@Override
	public int computeIntIfAbsent(T key, ToIntFunction<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(!containsKey(key)) {
			int newValue = mappingFunction.applyAsInt(key);
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public int supplyIntIfAbsent(T key, IntSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		if(!containsKey(key)) {
			int newValue = valueProvider.getAsInt();
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public int computeIntIfPresent(T key, ObjectIntUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(containsKey(key)) {
			int newValue = mappingFunction.applyAsInt(key, getInt(key));
			put(key, newValue);
			return newValue;
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public int computeIntNonDefault(T key, ObjectIntUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int value = getInt(key);
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
	public int computeIntIfAbsentNonDefault(T key, ToIntFunction<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int value;
		if((value = getInt(key)) == getDefaultReturnValue() || !containsKey(key)) {
			int newValue = mappingFunction.applyAsInt(key);
			if(newValue != getDefaultReturnValue()) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public int supplyIntIfAbsentNonDefault(T key, IntSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		int value;
		if((value = getInt(key)) == getDefaultReturnValue() || !containsKey(key)) {
			int newValue = valueProvider.getAsInt();
			if(newValue != getDefaultReturnValue()) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public int computeIntIfPresentNonDefault(T key, ObjectIntUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int value;
		if((value = getInt(key)) != getDefaultReturnValue() || containsKey(key)) {
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
	public int mergeInt(T key, int value, IntIntUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int oldValue = getInt(key);
		int newValue = oldValue == getDefaultReturnValue() ? value : mappingFunction.applyAsInt(oldValue, value);
		if(newValue == getDefaultReturnValue()) remove(key);
		else put(key, newValue);
		return newValue;
	}
	
	@Override
	public void mergeAllInt(Object2IntMap<T> m, IntIntUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Object2IntMap.Entry<T> entry : getFastIterable(m)) {
			T key = entry.getKey();
			int oldValue = getInt(key);
			int newValue = oldValue == getDefaultReturnValue() ? entry.getIntValue() : mappingFunction.applyAsInt(oldValue, entry.getIntValue());
			if(newValue == getDefaultReturnValue()) remove(key);
			else put(key, newValue);
		}
	}
	
	@Override
	public Integer get(Object key) {
		return Integer.valueOf(getInt((T)key));
	}
	
	@Override
	public Integer getOrDefault(Object key, Integer defaultValue) {
		Integer value = get(key);
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	
	@Override
	public Integer remove(Object key) {
		return Integer.valueOf(rem((T)key));
	}
	
	@Override
	public void forEach(ObjectIntConsumer<T> action) {
		Objects.requireNonNull(action);
		for(ObjectIterator<Object2IntMap.Entry<T>> iter = getFastIterator(this);iter.hasNext();) {
			Object2IntMap.Entry<T> entry = iter.next();
			action.accept(entry.getKey(), entry.getIntValue());
		}
	}

	@Override
	public ObjectSet<T> keySet() {
		return new AbstractObjectSet<T>() {
			@Override
			public boolean remove(Object o) {
				if(AbstractObject2IntMap.this.containsKey(o)) {
					AbstractObject2IntMap.this.remove(o);
					return true;
				}
				return false;
			}
			
			@Override
			public boolean add(T o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public ObjectIterator<T> iterator() {
				return new ObjectIterator<T>() {
					ObjectIterator<Object2IntMap.Entry<T>> iter = getFastIterator(AbstractObject2IntMap.this);
					@Override
					public boolean hasNext() {
						return iter.hasNext();
					}

					@Override
					public T next() {
						return iter.next().getKey();
					}
					
					@Override
					public void remove() {
						iter.remove();
					}
				};
			}
			
			@Override
			public int size() {
				return AbstractObject2IntMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractObject2IntMap.this.clear();
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
				return AbstractObject2IntMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractObject2IntMap.this.clear();
			}
			
			@Override
			public IntIterator iterator() {
				return new IntIterator() {
					ObjectIterator<Object2IntMap.Entry<T>> iter = getFastIterator(AbstractObject2IntMap.this);
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
	public ObjectSet<Map.Entry<T, Integer>> entrySet() {
		return (ObjectSet)object2IntEntrySet();
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o instanceof Map) {
			if(size() != ((Map<?, ?>)o).size()) return false;
			if(o instanceof Object2IntMap) return object2IntEntrySet().containsAll(((Object2IntMap<T>)o).object2IntEntrySet());
			return object2IntEntrySet().containsAll(((Map<?, ?>)o).entrySet());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		ObjectIterator<Object2IntMap.Entry<T>> iter = getFastIterator(this);
		while(iter.hasNext()) hash += iter.next().hashCode();
		return hash;
	}
	
	public static class ReversedObject2IntOrderedMap<T> extends AbstractObject2IntMap<T> implements Object2IntOrderedMap<T> {
		Object2IntOrderedMap<T> map;
		
		public ReversedObject2IntOrderedMap(Object2IntOrderedMap<T> map) {
			this.map = map;
		}
		@Override
		public AbstractObject2IntMap<T> setDefaultReturnValue(int v) {
			map.setDefaultReturnValue(v);
			return this;
		}
		@Override
		public int getDefaultReturnValue() { return map.getDefaultReturnValue(); }
		@Override
		public Object2IntOrderedMap<T> copy() { throw new UnsupportedOperationException(); }
		@Override
		public int put(T key, int value) { return map.put(key, value); }
		@Override
		public int putIfAbsent(T key, int value) { return map.putIfAbsent(key, value); }
		@Override
		public int addTo(T key, int value) { return map.addTo(key, value); }
		@Override
		public int subFrom(T key, int value) { return map.subFrom(key, value); }
		@Override
		public int rem(T key) { return map.rem(key); }
		@Override
		public boolean remove(T key, int value) { return map.remove(key, value); }
		@Override
		public int remOrDefault(T key, int defaultValue) { return map.remOrDefault(key, defaultValue); }
		@Override
		public boolean containsKey(Object key) { return map.containsKey(key); }
		@Override
		public boolean containsValue(int value) { return map.containsValue(value); }
		@Override
		public boolean replace(T key, int oldValue, int newValue) { return map.replace(key, oldValue, newValue); }
		@Override
		public int replace(T key, int value) { return map.replace(key, value); }
		@Override
		public void replaceInts(Object2IntMap<T> m) { map.replaceInts(m); }
		@Override
		public void replaceInts(ObjectIntUnaryOperator<T> mappingFunction) { map.replaceInts(mappingFunction); }
		@Override
		public int computeInt(T key, ObjectIntUnaryOperator<T> mappingFunction) { return map.computeInt(key, mappingFunction); }
		@Override
		public int computeIntIfAbsent(T key, ToIntFunction<T> mappingFunction) { return map.computeIntIfAbsent(key, mappingFunction); }
		@Override
		public int supplyIntIfAbsent(T key, IntSupplier valueProvider) { return map.supplyIntIfAbsent(key, valueProvider); }
		@Override
		public int computeIntIfPresent(T key, ObjectIntUnaryOperator<T> mappingFunction) { return map.computeIntIfPresent(key, mappingFunction); }
		@Override
		public int computeIntNonDefault(T key, ObjectIntUnaryOperator<T> mappingFunction) { return map.computeIntNonDefault(key, mappingFunction); }
		@Override
		public int computeIntIfAbsentNonDefault(T key, ToIntFunction<T> mappingFunction) { return map.computeIntIfAbsentNonDefault(key, mappingFunction); }
		@Override
		public int supplyIntIfAbsentNonDefault(T key, IntSupplier valueProvider) { return map.supplyIntIfAbsentNonDefault(key, valueProvider); }
		@Override
		public int computeIntIfPresentNonDefault(T key, ObjectIntUnaryOperator<T> mappingFunction) { return map.computeIntIfPresentNonDefault(key, mappingFunction); }
		@Override
		public int mergeInt(T key, int value, IntIntUnaryOperator mappingFunction) { return map.mergeInt(key, value, mappingFunction); }
		@Override
		public int getOrDefault(T key, int defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public int getInt(T key) { return map.getInt(key); }
		@Override
		public int putAndMoveToFirst(T key, int value) { return map.putAndMoveToLast(key, value); }
		@Override
		public int putAndMoveToLast(T key, int value) { return map.putAndMoveToFirst(key, value); }
		@Override
		public int putFirst(T key, int value) { return map.putLast(key, value); }
		@Override
		public int putLast(T key, int value) { return map.putFirst(key, value); }
		@Override
		public boolean moveToFirst(T key) { return map.moveToLast(key); }
		@Override
		public boolean moveToLast(T key) { return map.moveToFirst(key); }
		@Override
		public int getAndMoveToFirst(T key) { return map.getAndMoveToLast(key); }
		@Override
		public int getAndMoveToLast(T key) { return map.getAndMoveToFirst(key); }
		@Override
		public T firstKey() { return map.lastKey(); }
		@Override
		public T pollFirstKey() { return map.pollLastKey(); }
		@Override
		public T lastKey() { return map.firstKey(); }
		@Override
		public T pollLastKey() { return map.pollFirstKey(); }
		@Override
		public int firstIntValue() { return map.lastIntValue(); }
		@Override
		public int lastIntValue() { return map.firstIntValue(); }
		@Override
		public Object2IntMap.Entry<T> firstEntry() { return map.lastEntry(); }
		@Override
		public Object2IntMap.Entry<T> lastEntry() { return map.firstEntry(); }
		@Override
		public Object2IntMap.Entry<T> pollFirstEntry() { return map.pollLastEntry(); }
		@Override
		public Object2IntMap.Entry<T> pollLastEntry() { return map.pollFirstEntry(); }
		@Override
		public ObjectOrderedSet<Object2IntMap.Entry<T>> object2IntEntrySet() { return new AbstractObjectSet.ReversedObjectOrderedSet<>(map.object2IntEntrySet()); }
		@Override
		public ObjectOrderedSet<T> keySet() { return new AbstractObjectSet.ReversedObjectOrderedSet<>(map.keySet()); }
		@Override
		public IntOrderedCollection values() { return map.values().reversed(); }
		@Override
		public Object2IntOrderedMap<T> reversed() { return map; }
	}

	
	/**
	 * A Simple Type Specific Entry class to reduce boxing/unboxing
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class BasicEntry<T> implements Object2IntMap.Entry<T> {
		protected T key;
		protected int value;
		
		/**
		 * A basic Empty constructor
		 */
		public BasicEntry() {}
		/**
		 * A Type Specific Constructor
		 * @param key the key of a entry
		 * @param value the value of a entry 
		 */
		public BasicEntry(T key, int value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * A Helper method for fast replacing values
		 * @param key the key that should be replaced
		 * @param value the value that should be replaced
		 */
		public void set(T key, int value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public T getKey() {
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
				if(obj instanceof Object2IntMap.Entry) {
					Object2IntMap.Entry<T> entry = (Object2IntMap.Entry<T>)obj;
					return Objects.equals(key, entry.getKey()) && value == entry.getIntValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return value instanceof Integer && Objects.equals(this.key, key) && this.value == ((Integer)value).intValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Objects.hashCode(key) ^ Integer.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Objects.toString(key) + "=" + Integer.toString(value);
		}
	}
}