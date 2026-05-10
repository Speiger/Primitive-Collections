package speiger.src.collections.objects.maps.abstracts;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.functions.consumer.ObjectShortConsumer;
import speiger.src.collections.objects.functions.function.ToShortFunction;
import speiger.src.collections.objects.functions.function.ObjectShortUnaryOperator;
import speiger.src.collections.objects.maps.interfaces.Object2ShortMap;
import speiger.src.collections.objects.maps.interfaces.Object2ShortOrderedMap;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
import speiger.src.collections.shorts.collections.ShortOrderedCollection;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.utils.maps.Object2ShortMaps;
import speiger.src.collections.shorts.collections.AbstractShortCollection;
import speiger.src.collections.shorts.collections.ShortCollection;
import speiger.src.collections.shorts.collections.ShortIterator;
import speiger.src.collections.shorts.functions.function.ShortShortUnaryOperator;
import speiger.src.collections.shorts.functions.ShortSupplier;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Base Implementation of a Type Specific Map to reduce boxing/unboxing
 * @param <T> the keyType of elements maintained by this Collection
 */
public abstract class AbstractObject2ShortMap<T> extends AbstractMap<T, Short> implements Object2ShortMap<T>
{
	protected short defaultReturnValue = (short)-1;
	
	@Override
	public short getDefaultReturnValue() {
		return defaultReturnValue;
	}
	
	@Override
	public AbstractObject2ShortMap<T> setDefaultReturnValue(short v) {
		defaultReturnValue = v;
		return this;
	}
	
	protected ObjectIterable<Object2ShortMap.Entry<T>> getFastIterable(Object2ShortMap<T> map) {
		return Object2ShortMaps.fastIterable(map);
	}
	
	protected ObjectIterator<Object2ShortMap.Entry<T>> getFastIterator(Object2ShortMap<T> map) {
		return Object2ShortMaps.fastIterator(map);
	}
	
	@Override
	public Object2ShortMap<T> copy() { 
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public Short put(T key, Short value) {
		return Short.valueOf(put(key, value.shortValue()));
	}
	
	@Override
	public void addToAll(Object2ShortMap<T> m) {
		for(Object2ShortMap.Entry<T> entry : getFastIterable(m))
			addTo(entry.getKey(), entry.getShortValue());
	}
	
	@Override
	public void putAll(Object2ShortMap<T> m) {
		for(ObjectIterator<Object2ShortMap.Entry<T>> iter = getFastIterator(m);iter.hasNext();) {
			Object2ShortMap.Entry<T> entry = iter.next();
			put(entry.getKey(), entry.getShortValue());
		}
	}
	
	@Override
	public void putAll(Map<? extends T, ? extends Short> m)
	{
		if(m instanceof Object2ShortMap) putAll((Object2ShortMap<T>)m);
		else super.putAll(m);
	}
	
	@Override
	public void putAll(T[] keys, short[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);
	}
	
	@Override
	public void putAll(T[] keys, Short[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i].shortValue());		
	}
	
	@Override
	public void putAllIfAbsent(Object2ShortMap<T> m) {
		for(Object2ShortMap.Entry<T> entry : getFastIterable(m))
			putIfAbsent(entry.getKey(), entry.getShortValue());
	}
	
	
	@Override
	public boolean containsKey(Object key) {
		for(ObjectIterator<T> iter = keySet().iterator();iter.hasNext();)
			if(Objects.equals(key, iter.next())) return true;
		return false;
	}
	
	@Override
	public boolean containsValue(short value) {
		for(ShortIterator iter = values().iterator();iter.hasNext();)
			if(iter.nextShort() == value) return true;
		return false;
	}
	
	@Override
	public boolean replace(T key, short oldValue, short newValue) {
		short curValue = getShort(key);
		if (curValue != oldValue || (curValue == getDefaultReturnValue() && !containsKey(key))) {
			return false;
		}
		put(key, newValue);
		return true;
	}

	@Override
	public short replace(T key, short value) {
		short curValue;
		if ((curValue = getShort(key)) != getDefaultReturnValue() || containsKey(key)) {
			curValue = put(key, value);
		}
		return curValue;
	}
	
	@Override
	public void replaceShorts(Object2ShortMap<T> m) {
		for(Object2ShortMap.Entry<T> entry : getFastIterable(m))
			replace(entry.getKey(), entry.getShortValue());
	}
	
	@Override
	public void replaceShorts(ObjectShortUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(ObjectIterator<Object2ShortMap.Entry<T>> iter = getFastIterator(this);iter.hasNext();) {
			Object2ShortMap.Entry<T> entry = iter.next();
			entry.setValue(mappingFunction.applyAsShort(entry.getKey(), entry.getShortValue()));
		}
	}

	@Override
	public short computeShort(T key, ObjectShortUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		short newValue = mappingFunction.applyAsShort(key, getShort(key));
		put(key, newValue);
		return newValue;
	}
	
	@Override
	public short computeShortIfAbsent(T key, ToShortFunction<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(!containsKey(key)) {
			short newValue = mappingFunction.applyAsShort(key);
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public short supplyShortIfAbsent(T key, ShortSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		if(!containsKey(key)) {
			short newValue = valueProvider.getAsShort();
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public short computeShortIfPresent(T key, ObjectShortUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(containsKey(key)) {
			short newValue = mappingFunction.applyAsShort(key, getShort(key));
			put(key, newValue);
			return newValue;
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public short computeShortNonDefault(T key, ObjectShortUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		short value = getShort(key);
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
	public short computeShortIfAbsentNonDefault(T key, ToShortFunction<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		short value;
		if((value = getShort(key)) == getDefaultReturnValue() || !containsKey(key)) {
			short newValue = mappingFunction.applyAsShort(key);
			if(newValue != getDefaultReturnValue()) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public short supplyShortIfAbsentNonDefault(T key, ShortSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		short value;
		if((value = getShort(key)) == getDefaultReturnValue() || !containsKey(key)) {
			short newValue = valueProvider.getAsShort();
			if(newValue != getDefaultReturnValue()) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public short computeShortIfPresentNonDefault(T key, ObjectShortUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		short value;
		if((value = getShort(key)) != getDefaultReturnValue() || containsKey(key)) {
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
	public short mergeShort(T key, short value, ShortShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		short oldValue = getShort(key);
		short newValue = oldValue == getDefaultReturnValue() ? value : mappingFunction.applyAsShort(oldValue, value);
		if(newValue == getDefaultReturnValue()) remove(key);
		else put(key, newValue);
		return newValue;
	}
	
	@Override
	public void mergeAllShort(Object2ShortMap<T> m, ShortShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Object2ShortMap.Entry<T> entry : getFastIterable(m)) {
			T key = entry.getKey();
			short oldValue = getShort(key);
			short newValue = oldValue == getDefaultReturnValue() ? entry.getShortValue() : mappingFunction.applyAsShort(oldValue, entry.getShortValue());
			if(newValue == getDefaultReturnValue()) remove(key);
			else put(key, newValue);
		}
	}
	
	@Override
	public Short get(Object key) {
		return Short.valueOf(getShort((T)key));
	}
	
	@Override
	public Short getOrDefault(Object key, Short defaultValue) {
		Short value = get(key);
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	
	@Override
	public Short remove(Object key) {
		return Short.valueOf(rem((T)key));
	}
	
	@Override
	public void forEach(ObjectShortConsumer<T> action) {
		Objects.requireNonNull(action);
		for(ObjectIterator<Object2ShortMap.Entry<T>> iter = getFastIterator(this);iter.hasNext();) {
			Object2ShortMap.Entry<T> entry = iter.next();
			action.accept(entry.getKey(), entry.getShortValue());
		}
	}

	@Override
	public ObjectSet<T> keySet() {
		return new AbstractObjectSet<T>() {
			@Override
			public boolean remove(Object o) {
				if(AbstractObject2ShortMap.this.containsKey(o)) {
					AbstractObject2ShortMap.this.remove(o);
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
					ObjectIterator<Object2ShortMap.Entry<T>> iter = getFastIterator(AbstractObject2ShortMap.this);
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
				return AbstractObject2ShortMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractObject2ShortMap.this.clear();
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
				return AbstractObject2ShortMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractObject2ShortMap.this.clear();
			}
			
			@Override
			public ShortIterator iterator() {
				return new ShortIterator() {
					ObjectIterator<Object2ShortMap.Entry<T>> iter = getFastIterator(AbstractObject2ShortMap.this);
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
	public ObjectSet<Map.Entry<T, Short>> entrySet() {
		return (ObjectSet)object2ShortEntrySet();
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o instanceof Map) {
			if(size() != ((Map<?, ?>)o).size()) return false;
			if(o instanceof Object2ShortMap) return object2ShortEntrySet().containsAll(((Object2ShortMap<T>)o).object2ShortEntrySet());
			return object2ShortEntrySet().containsAll(((Map<?, ?>)o).entrySet());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		ObjectIterator<Object2ShortMap.Entry<T>> iter = getFastIterator(this);
		while(iter.hasNext()) hash += iter.next().hashCode();
		return hash;
	}
	
	public static class ReversedObject2ShortOrderedMap<T> extends AbstractObject2ShortMap<T> implements Object2ShortOrderedMap<T> {
		Object2ShortOrderedMap<T> map;
		
		public ReversedObject2ShortOrderedMap(Object2ShortOrderedMap<T> map) {
			this.map = map;
		}
		@Override
		public AbstractObject2ShortMap<T> setDefaultReturnValue(short v) {
			map.setDefaultReturnValue(v);
			return this;
		}
		@Override
		public short getDefaultReturnValue() { return map.getDefaultReturnValue(); }
		@Override
		public Object2ShortOrderedMap<T> copy() { throw new UnsupportedOperationException(); }
		@Override
		public short put(T key, short value) { return map.put(key, value); }
		@Override
		public short putIfAbsent(T key, short value) { return map.putIfAbsent(key, value); }
		@Override
		public short addTo(T key, short value) { return map.addTo(key, value); }
		@Override
		public short subFrom(T key, short value) { return map.subFrom(key, value); }
		@Override
		public short rem(T key) { return map.rem(key); }
		@Override
		public boolean remove(T key, short value) { return map.remove(key, value); }
		@Override
		public short remOrDefault(T key, short defaultValue) { return map.remOrDefault(key, defaultValue); }
		@Override
		public boolean containsKey(Object key) { return map.containsKey(key); }
		@Override
		public boolean containsValue(short value) { return map.containsValue(value); }
		@Override
		public boolean replace(T key, short oldValue, short newValue) { return map.replace(key, oldValue, newValue); }
		@Override
		public short replace(T key, short value) { return map.replace(key, value); }
		@Override
		public void replaceShorts(Object2ShortMap<T> m) { map.replaceShorts(m); }
		@Override
		public void replaceShorts(ObjectShortUnaryOperator<T> mappingFunction) { map.replaceShorts(mappingFunction); }
		@Override
		public short computeShort(T key, ObjectShortUnaryOperator<T> mappingFunction) { return map.computeShort(key, mappingFunction); }
		@Override
		public short computeShortIfAbsent(T key, ToShortFunction<T> mappingFunction) { return map.computeShortIfAbsent(key, mappingFunction); }
		@Override
		public short supplyShortIfAbsent(T key, ShortSupplier valueProvider) { return map.supplyShortIfAbsent(key, valueProvider); }
		@Override
		public short computeShortIfPresent(T key, ObjectShortUnaryOperator<T> mappingFunction) { return map.computeShortIfPresent(key, mappingFunction); }
		@Override
		public short computeShortNonDefault(T key, ObjectShortUnaryOperator<T> mappingFunction) { return map.computeShortNonDefault(key, mappingFunction); }
		@Override
		public short computeShortIfAbsentNonDefault(T key, ToShortFunction<T> mappingFunction) { return map.computeShortIfAbsentNonDefault(key, mappingFunction); }
		@Override
		public short supplyShortIfAbsentNonDefault(T key, ShortSupplier valueProvider) { return map.supplyShortIfAbsentNonDefault(key, valueProvider); }
		@Override
		public short computeShortIfPresentNonDefault(T key, ObjectShortUnaryOperator<T> mappingFunction) { return map.computeShortIfPresentNonDefault(key, mappingFunction); }
		@Override
		public short mergeShort(T key, short value, ShortShortUnaryOperator mappingFunction) { return map.mergeShort(key, value, mappingFunction); }
		@Override
		public short getOrDefault(T key, short defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public short getShort(T key) { return map.getShort(key); }
		@Override
		public short putAndMoveToFirst(T key, short value) { return map.putAndMoveToLast(key, value); }
		@Override
		public short putAndMoveToLast(T key, short value) { return map.putAndMoveToFirst(key, value); }
		@Override
		public short putFirst(T key, short value) { return map.putLast(key, value); }
		@Override
		public short putLast(T key, short value) { return map.putFirst(key, value); }
		@Override
		public boolean moveToFirst(T key) { return map.moveToLast(key); }
		@Override
		public boolean moveToLast(T key) { return map.moveToFirst(key); }
		@Override
		public short getAndMoveToFirst(T key) { return map.getAndMoveToLast(key); }
		@Override
		public short getAndMoveToLast(T key) { return map.getAndMoveToFirst(key); }
		@Override
		public T firstKey() { return map.lastKey(); }
		@Override
		public T pollFirstKey() { return map.pollLastKey(); }
		@Override
		public T lastKey() { return map.firstKey(); }
		@Override
		public T pollLastKey() { return map.pollFirstKey(); }
		@Override
		public short firstShortValue() { return map.lastShortValue(); }
		@Override
		public short lastShortValue() { return map.firstShortValue(); }
		@Override
		public Object2ShortMap.Entry<T> firstEntry() { return map.lastEntry(); }
		@Override
		public Object2ShortMap.Entry<T> lastEntry() { return map.firstEntry(); }
		@Override
		public Object2ShortMap.Entry<T> pollFirstEntry() { return map.pollLastEntry(); }
		@Override
		public Object2ShortMap.Entry<T> pollLastEntry() { return map.pollFirstEntry(); }
		@Override
		public ObjectOrderedSet<Object2ShortMap.Entry<T>> object2ShortEntrySet() { return new AbstractObjectSet.ReversedObjectOrderedSet<>(map.object2ShortEntrySet()); }
		@Override
		public ObjectOrderedSet<T> keySet() { return new AbstractObjectSet.ReversedObjectOrderedSet<>(map.keySet()); }
		@Override
		public ShortOrderedCollection values() { return map.values().reversed(); }
		@Override
		public Object2ShortOrderedMap<T> reversed() { return map; }
	}

	
	/**
	 * A Simple Type Specific Entry class to reduce boxing/unboxing
	 * @param <T> the keyType of elements maintained by this Collection
	 */
	public static class BasicEntry<T> implements Object2ShortMap.Entry<T> {
		protected T key;
		protected short value;
		
		/**
		 * A basic Empty constructor
		 */
		public BasicEntry() {}
		/**
		 * A Type Specific Constructor
		 * @param key the key of a entry
		 * @param value the value of a entry 
		 */
		public BasicEntry(T key, short value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * A Helper method for fast replacing values
		 * @param key the key that should be replaced
		 * @param value the value that should be replaced
		 */
		public void set(T key, short value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public T getKey() {
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
				if(obj instanceof Object2ShortMap.Entry) {
					Object2ShortMap.Entry<T> entry = (Object2ShortMap.Entry<T>)obj;
					return Objects.equals(key, entry.getKey()) && value == entry.getShortValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return value instanceof Short && Objects.equals(this.key, key) && this.value == ((Short)value).shortValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Objects.hashCode(key) ^ Short.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Objects.toString(key) + "=" + Short.toString(value);
		}
	}
}