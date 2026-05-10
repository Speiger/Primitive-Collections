package speiger.src.collections.shorts.maps.abstracts;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

import speiger.src.collections.shorts.collections.ShortIterator;
import speiger.src.collections.shorts.functions.consumer.ShortShortConsumer;
import speiger.src.collections.shorts.functions.function.ShortUnaryOperator;
import speiger.src.collections.shorts.functions.function.ShortShortUnaryOperator;
import speiger.src.collections.shorts.maps.interfaces.Short2ShortMap;
import speiger.src.collections.shorts.maps.interfaces.Short2ShortOrderedMap;
import speiger.src.collections.shorts.sets.ShortOrderedSet;
import speiger.src.collections.shorts.collections.ShortOrderedCollection;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
import speiger.src.collections.shorts.sets.AbstractShortSet;
import speiger.src.collections.shorts.sets.ShortSet;
import speiger.src.collections.shorts.utils.maps.Short2ShortMaps;
import speiger.src.collections.shorts.collections.AbstractShortCollection;
import speiger.src.collections.shorts.collections.ShortCollection;
import speiger.src.collections.shorts.functions.ShortSupplier;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Base Implementation of a Type Specific Map to reduce boxing/unboxing
 */
public abstract class AbstractShort2ShortMap extends AbstractMap<Short, Short> implements Short2ShortMap
{
	protected short defaultReturnValue = (short)-1;
	
	@Override
	public short getDefaultReturnValue() {
		return defaultReturnValue;
	}
	
	@Override
	public AbstractShort2ShortMap setDefaultReturnValue(short v) {
		defaultReturnValue = v;
		return this;
	}
	
	protected ObjectIterable<Short2ShortMap.Entry> getFastIterable(Short2ShortMap map) {
		return Short2ShortMaps.fastIterable(map);
	}
	
	protected ObjectIterator<Short2ShortMap.Entry> getFastIterator(Short2ShortMap map) {
		return Short2ShortMaps.fastIterator(map);
	}
	
	@Override
	public Short2ShortMap copy() { 
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public Short put(Short key, Short value) {
		return Short.valueOf(put(key.shortValue(), value.shortValue()));
	}
	
	@Override
	public void addToAll(Short2ShortMap m) {
		for(Short2ShortMap.Entry entry : getFastIterable(m))
			addTo(entry.getShortKey(), entry.getShortValue());
	}
	
	@Override
	public void putAll(Short2ShortMap m) {
		for(ObjectIterator<Short2ShortMap.Entry> iter = getFastIterator(m);iter.hasNext();) {
			Short2ShortMap.Entry entry = iter.next();
			put(entry.getShortKey(), entry.getShortValue());
		}
	}
	
	@Override
	public void putAll(Map<? extends Short, ? extends Short> m)
	{
		if(m instanceof Short2ShortMap) putAll((Short2ShortMap)m);
		else super.putAll(m);
	}
	
	@Override
	public void putAll(short[] keys, short[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);
	}
	
	@Override
	public void putAll(Short[] keys, Short[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i].shortValue(), values[i].shortValue());		
	}
	
	@Override
	public void putAllIfAbsent(Short2ShortMap m) {
		for(Short2ShortMap.Entry entry : getFastIterable(m))
			putIfAbsent(entry.getShortKey(), entry.getShortValue());
	}
	
	
	@Override
	public boolean containsKey(short key) {
		for(ShortIterator iter = keySet().iterator();iter.hasNext();)
			if(iter.nextShort() == key) return true;
		return false;
	}
	
	@Override
	public boolean containsValue(short value) {
		for(ShortIterator iter = values().iterator();iter.hasNext();)
			if(iter.nextShort() == value) return true;
		return false;
	}
	
	@Override
	public boolean replace(short key, short oldValue, short newValue) {
		short curValue = get(key);
		if (curValue != oldValue || (curValue == getDefaultReturnValue() && !containsKey(key))) {
			return false;
		}
		put(key, newValue);
		return true;
	}

	@Override
	public short replace(short key, short value) {
		short curValue;
		if ((curValue = get(key)) != getDefaultReturnValue() || containsKey(key)) {
			curValue = put(key, value);
		}
		return curValue;
	}
	
	@Override
	public void replaceShorts(Short2ShortMap m) {
		for(Short2ShortMap.Entry entry : getFastIterable(m))
			replace(entry.getShortKey(), entry.getShortValue());
	}
	
	@Override
	public void replaceShorts(ShortShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(ObjectIterator<Short2ShortMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Short2ShortMap.Entry entry = iter.next();
			entry.setValue(mappingFunction.applyAsShort(entry.getShortKey(), entry.getShortValue()));
		}
	}

	@Override
	public short computeShort(short key, ShortShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		short newValue = mappingFunction.applyAsShort(key, get(key));
		put(key, newValue);
		return newValue;
	}
	
	@Override
	public short computeShortIfAbsent(short key, ShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(!containsKey(key)) {
			short newValue = mappingFunction.applyAsShort(key);
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public short supplyShortIfAbsent(short key, ShortSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		if(!containsKey(key)) {
			short newValue = valueProvider.getAsShort();
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public short computeShortIfPresent(short key, ShortShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(containsKey(key)) {
			short newValue = mappingFunction.applyAsShort(key, get(key));
			put(key, newValue);
			return newValue;
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public short computeShortNonDefault(short key, ShortShortUnaryOperator mappingFunction) {
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
	public short computeShortIfAbsentNonDefault(short key, ShortUnaryOperator mappingFunction) {
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
	public short supplyShortIfAbsentNonDefault(short key, ShortSupplier valueProvider) {
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
	public short computeShortIfPresentNonDefault(short key, ShortShortUnaryOperator mappingFunction) {
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
	public short mergeShort(short key, short value, ShortShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		short oldValue = get(key);
		short newValue = oldValue == getDefaultReturnValue() ? value : mappingFunction.applyAsShort(oldValue, value);
		if(newValue == getDefaultReturnValue()) remove(key);
		else put(key, newValue);
		return newValue;
	}
	
	@Override
	public void mergeAllShort(Short2ShortMap m, ShortShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Short2ShortMap.Entry entry : getFastIterable(m)) {
			short key = entry.getShortKey();
			short oldValue = get(key);
			short newValue = oldValue == getDefaultReturnValue() ? entry.getShortValue() : mappingFunction.applyAsShort(oldValue, entry.getShortValue());
			if(newValue == getDefaultReturnValue()) remove(key);
			else put(key, newValue);
		}
	}
	
	@Override
	public Short get(Object key) {
		return Short.valueOf(key instanceof Short ? get(((Short)key).shortValue()) : getDefaultReturnValue());
	}
	
	@Override
	public Short getOrDefault(Object key, Short defaultValue) {
		return Short.valueOf(key instanceof Short ? getOrDefault(((Short)key).shortValue(), defaultValue.shortValue()) : getDefaultReturnValue());
	}
	
	@Override
	public short getOrDefault(short key, short defaultValue) {
		short value = get(key);
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	
	@Override
	public Short remove(Object key) {
		return key instanceof Short ? Short.valueOf(remove(((Short)key).shortValue())) : Short.valueOf(getDefaultReturnValue());
	}
	
	@Override
	public void forEach(ShortShortConsumer action) {
		Objects.requireNonNull(action);
		for(ObjectIterator<Short2ShortMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Short2ShortMap.Entry entry = iter.next();
			action.accept(entry.getShortKey(), entry.getShortValue());
		}
	}

	@Override
	public ShortSet keySet() {
		return new AbstractShortSet() {
			@Override
			public boolean remove(short o) {
				return AbstractShort2ShortMap.this.remove(o) != getDefaultReturnValue();
			}
			
			@Override
			public boolean add(short o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public ShortIterator iterator() {
				return new ShortIterator() {
					ObjectIterator<Short2ShortMap.Entry> iter = getFastIterator(AbstractShort2ShortMap.this);
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
				return AbstractShort2ShortMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractShort2ShortMap.this.clear();
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
				return AbstractShort2ShortMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractShort2ShortMap.this.clear();
			}
			
			@Override
			public ShortIterator iterator() {
				return new ShortIterator() {
					ObjectIterator<Short2ShortMap.Entry> iter = getFastIterator(AbstractShort2ShortMap.this);
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
	public ObjectSet<Map.Entry<Short, Short>> entrySet() {
		return (ObjectSet)short2ShortEntrySet();
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o instanceof Map) {
			if(size() != ((Map<?, ?>)o).size()) return false;
			if(o instanceof Short2ShortMap) return short2ShortEntrySet().containsAll(((Short2ShortMap)o).short2ShortEntrySet());
			return short2ShortEntrySet().containsAll(((Map<?, ?>)o).entrySet());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		ObjectIterator<Short2ShortMap.Entry> iter = getFastIterator(this);
		while(iter.hasNext()) hash += iter.next().hashCode();
		return hash;
	}
	
	public static class ReversedShort2ShortOrderedMap extends AbstractShort2ShortMap implements Short2ShortOrderedMap {
		Short2ShortOrderedMap map;
		
		public ReversedShort2ShortOrderedMap(Short2ShortOrderedMap map) {
			this.map = map;
		}
		@Override
		public AbstractShort2ShortMap setDefaultReturnValue(short v) {
			map.setDefaultReturnValue(v);
			return this;
		}
		@Override
		public short getDefaultReturnValue() { return map.getDefaultReturnValue(); }
		@Override
		public Short2ShortOrderedMap copy() { throw new UnsupportedOperationException(); }
		@Override
		public short put(short key, short value) { return map.put(key, value); }
		@Override
		public short putIfAbsent(short key, short value) { return map.putIfAbsent(key, value); }
		@Override
		public short addTo(short key, short value) { return map.addTo(key, value); }
		@Override
		public short subFrom(short key, short value) { return map.subFrom(key, value); }
		@Override
		public short remove(short key) { return map.remove(key); }
		@Override
		public boolean remove(short key, short value) { return map.remove(key, value); }
		@Override
		public short removeOrDefault(short key, short defaultValue) { return map.removeOrDefault(key, defaultValue); }
		@Override
		public boolean containsKey(short key) { return map.containsKey(key); }
		@Override
		public boolean containsValue(short value) { return map.containsValue(value); }
		@Override
		public boolean replace(short key, short oldValue, short newValue) { return map.replace(key, oldValue, newValue); }
		@Override
		public short replace(short key, short value) { return map.replace(key, value); }
		@Override
		public void replaceShorts(Short2ShortMap m) { map.replaceShorts(m); }
		@Override
		public void replaceShorts(ShortShortUnaryOperator mappingFunction) { map.replaceShorts(mappingFunction); }
		@Override
		public short computeShort(short key, ShortShortUnaryOperator mappingFunction) { return map.computeShort(key, mappingFunction); }
		@Override
		public short computeShortIfAbsent(short key, ShortUnaryOperator mappingFunction) { return map.computeShortIfAbsent(key, mappingFunction); }
		@Override
		public short supplyShortIfAbsent(short key, ShortSupplier valueProvider) { return map.supplyShortIfAbsent(key, valueProvider); }
		@Override
		public short computeShortIfPresent(short key, ShortShortUnaryOperator mappingFunction) { return map.computeShortIfPresent(key, mappingFunction); }
		@Override
		public short computeShortNonDefault(short key, ShortShortUnaryOperator mappingFunction) { return map.computeShortNonDefault(key, mappingFunction); }
		@Override
		public short computeShortIfAbsentNonDefault(short key, ShortUnaryOperator mappingFunction) { return map.computeShortIfAbsentNonDefault(key, mappingFunction); }
		@Override
		public short supplyShortIfAbsentNonDefault(short key, ShortSupplier valueProvider) { return map.supplyShortIfAbsentNonDefault(key, valueProvider); }
		@Override
		public short computeShortIfPresentNonDefault(short key, ShortShortUnaryOperator mappingFunction) { return map.computeShortIfPresentNonDefault(key, mappingFunction); }
		@Override
		public short mergeShort(short key, short value, ShortShortUnaryOperator mappingFunction) { return map.mergeShort(key, value, mappingFunction); }
		@Override
		public short getOrDefault(short key, short defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public short get(short key) { return map.get(key); }
		@Override
		public short putAndMoveToFirst(short key, short value) { return map.putAndMoveToLast(key, value); }
		@Override
		public short putAndMoveToLast(short key, short value) { return map.putAndMoveToFirst(key, value); }
		@Override
		public short putFirst(short key, short value) { return map.putLast(key, value); }
		@Override
		public short putLast(short key, short value) { return map.putFirst(key, value); }
		@Override
		public boolean moveToFirst(short key) { return map.moveToLast(key); }
		@Override
		public boolean moveToLast(short key) { return map.moveToFirst(key); }
		@Override
		public short getAndMoveToFirst(short key) { return map.getAndMoveToLast(key); }
		@Override
		public short getAndMoveToLast(short key) { return map.getAndMoveToFirst(key); }
		@Override
		public short firstShortKey() { return map.lastShortKey(); }
		@Override
		public short pollFirstShortKey() { return map.pollLastShortKey(); }
		@Override
		public short lastShortKey() { return map.firstShortKey(); }
		@Override
		public short pollLastShortKey() { return map.pollFirstShortKey(); }
		@Override
		public short firstShortValue() { return map.lastShortValue(); }
		@Override
		public short lastShortValue() { return map.firstShortValue(); }
		@Override
		public Short2ShortMap.Entry firstEntry() { return map.lastEntry(); }
		@Override
		public Short2ShortMap.Entry lastEntry() { return map.firstEntry(); }
		@Override
		public Short2ShortMap.Entry pollFirstEntry() { return map.pollLastEntry(); }
		@Override
		public Short2ShortMap.Entry pollLastEntry() { return map.pollFirstEntry(); }
		@Override
		public ObjectOrderedSet<Short2ShortMap.Entry> short2ShortEntrySet() { return new AbstractObjectSet.ReversedObjectOrderedSet<>(map.short2ShortEntrySet()); }
		@Override
		public ShortOrderedSet keySet() { return new AbstractShortSet.ReversedShortOrderedSet(map.keySet()); }
		@Override
		public ShortOrderedCollection values() { return map.values().reversed(); }
		@Override
		public Short2ShortOrderedMap reversed() { return map; }
	}

	
	/**
	 * A Simple Type Specific Entry class to reduce boxing/unboxing
	 */
	public static class BasicEntry implements Short2ShortMap.Entry {
		protected short key;
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
		public BasicEntry(Short key, Short value) {
			this.key = key.shortValue();
			this.value = value.shortValue();
		}
		
		/**
		 * A Type Specific Constructor
		 * @param key the key of a entry
		 * @param value the value of a entry 
		 */
		public BasicEntry(short key, short value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * A Helper method for fast replacing values
		 * @param key the key that should be replaced
		 * @param value the value that should be replaced
		 */
		public void set(short key, short value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public short getShortKey() {
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
				if(obj instanceof Short2ShortMap.Entry) {
					Short2ShortMap.Entry entry = (Short2ShortMap.Entry)obj;
					return key == entry.getShortKey() && value == entry.getShortValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Short && value instanceof Short && this.key == ((Short)key).shortValue() && this.value == ((Short)value).shortValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Short.hashCode(key) ^ Short.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Short.toString(key) + "=" + Short.toString(value);
		}
	}
}