package speiger.src.collections.chars.maps.abstracts;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

import speiger.src.collections.chars.collections.CharIterator;
import speiger.src.collections.chars.functions.consumer.CharShortConsumer;
import speiger.src.collections.chars.functions.function.Char2ShortFunction;
import speiger.src.collections.chars.functions.function.CharShortUnaryOperator;
import speiger.src.collections.chars.maps.interfaces.Char2ShortMap;
import speiger.src.collections.chars.maps.interfaces.Char2ShortOrderedMap;
import speiger.src.collections.chars.sets.CharOrderedSet;
import speiger.src.collections.shorts.collections.ShortOrderedCollection;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
import speiger.src.collections.chars.sets.AbstractCharSet;
import speiger.src.collections.chars.sets.CharSet;
import speiger.src.collections.chars.utils.maps.Char2ShortMaps;
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
public abstract class AbstractChar2ShortMap extends AbstractMap<Character, Short> implements Char2ShortMap
{
	protected short defaultReturnValue = (short)-1;
	
	@Override
	public short getDefaultReturnValue() {
		return defaultReturnValue;
	}
	
	@Override
	public AbstractChar2ShortMap setDefaultReturnValue(short v) {
		defaultReturnValue = v;
		return this;
	}
	
	protected ObjectIterable<Char2ShortMap.Entry> getFastIterable(Char2ShortMap map) {
		return Char2ShortMaps.fastIterable(map);
	}
	
	protected ObjectIterator<Char2ShortMap.Entry> getFastIterator(Char2ShortMap map) {
		return Char2ShortMaps.fastIterator(map);
	}
	
	@Override
	public Char2ShortMap copy() { 
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public Short put(Character key, Short value) {
		return Short.valueOf(put(key.charValue(), value.shortValue()));
	}
	
	@Override
	public void addToAll(Char2ShortMap m) {
		for(Char2ShortMap.Entry entry : getFastIterable(m))
			addTo(entry.getCharKey(), entry.getShortValue());
	}
	
	@Override
	public void putAll(Char2ShortMap m) {
		for(ObjectIterator<Char2ShortMap.Entry> iter = getFastIterator(m);iter.hasNext();) {
			Char2ShortMap.Entry entry = iter.next();
			put(entry.getCharKey(), entry.getShortValue());
		}
	}
	
	@Override
	public void putAll(Map<? extends Character, ? extends Short> m)
	{
		if(m instanceof Char2ShortMap) putAll((Char2ShortMap)m);
		else super.putAll(m);
	}
	
	@Override
	public void putAll(char[] keys, short[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);
	}
	
	@Override
	public void putAll(Character[] keys, Short[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i].charValue(), values[i].shortValue());		
	}
	
	@Override
	public void putAllIfAbsent(Char2ShortMap m) {
		for(Char2ShortMap.Entry entry : getFastIterable(m))
			putIfAbsent(entry.getCharKey(), entry.getShortValue());
	}
	
	
	@Override
	public boolean containsKey(char key) {
		for(CharIterator iter = keySet().iterator();iter.hasNext();)
			if(iter.nextChar() == key) return true;
		return false;
	}
	
	@Override
	public boolean containsValue(short value) {
		for(ShortIterator iter = values().iterator();iter.hasNext();)
			if(iter.nextShort() == value) return true;
		return false;
	}
	
	@Override
	public boolean replace(char key, short oldValue, short newValue) {
		short curValue = get(key);
		if (curValue != oldValue || (curValue == getDefaultReturnValue() && !containsKey(key))) {
			return false;
		}
		put(key, newValue);
		return true;
	}

	@Override
	public short replace(char key, short value) {
		short curValue;
		if ((curValue = get(key)) != getDefaultReturnValue() || containsKey(key)) {
			curValue = put(key, value);
		}
		return curValue;
	}
	
	@Override
	public void replaceShorts(Char2ShortMap m) {
		for(Char2ShortMap.Entry entry : getFastIterable(m))
			replace(entry.getCharKey(), entry.getShortValue());
	}
	
	@Override
	public void replaceShorts(CharShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(ObjectIterator<Char2ShortMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Char2ShortMap.Entry entry = iter.next();
			entry.setValue(mappingFunction.applyAsShort(entry.getCharKey(), entry.getShortValue()));
		}
	}

	@Override
	public short computeShort(char key, CharShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		short newValue = mappingFunction.applyAsShort(key, get(key));
		put(key, newValue);
		return newValue;
	}
	
	@Override
	public short computeShortIfAbsent(char key, Char2ShortFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(!containsKey(key)) {
			short newValue = mappingFunction.applyAsShort(key);
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public short supplyShortIfAbsent(char key, ShortSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		if(!containsKey(key)) {
			short newValue = valueProvider.getAsShort();
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public short computeShortIfPresent(char key, CharShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(containsKey(key)) {
			short newValue = mappingFunction.applyAsShort(key, get(key));
			put(key, newValue);
			return newValue;
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public short computeShortNonDefault(char key, CharShortUnaryOperator mappingFunction) {
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
	public short computeShortIfAbsentNonDefault(char key, Char2ShortFunction mappingFunction) {
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
	public short supplyShortIfAbsentNonDefault(char key, ShortSupplier valueProvider) {
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
	public short computeShortIfPresentNonDefault(char key, CharShortUnaryOperator mappingFunction) {
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
	public short mergeShort(char key, short value, ShortShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		short oldValue = get(key);
		short newValue = oldValue == getDefaultReturnValue() ? value : mappingFunction.applyAsShort(oldValue, value);
		if(newValue == getDefaultReturnValue()) remove(key);
		else put(key, newValue);
		return newValue;
	}
	
	@Override
	public void mergeAllShort(Char2ShortMap m, ShortShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Char2ShortMap.Entry entry : getFastIterable(m)) {
			char key = entry.getCharKey();
			short oldValue = get(key);
			short newValue = oldValue == getDefaultReturnValue() ? entry.getShortValue() : mappingFunction.applyAsShort(oldValue, entry.getShortValue());
			if(newValue == getDefaultReturnValue()) remove(key);
			else put(key, newValue);
		}
	}
	
	@Override
	public Short get(Object key) {
		return Short.valueOf(key instanceof Character ? get(((Character)key).charValue()) : getDefaultReturnValue());
	}
	
	@Override
	public Short getOrDefault(Object key, Short defaultValue) {
		return Short.valueOf(key instanceof Character ? getOrDefault(((Character)key).charValue(), defaultValue.shortValue()) : getDefaultReturnValue());
	}
	
	@Override
	public short getOrDefault(char key, short defaultValue) {
		short value = get(key);
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	
	@Override
	public Short remove(Object key) {
		return key instanceof Character ? Short.valueOf(remove(((Character)key).charValue())) : Short.valueOf(getDefaultReturnValue());
	}
	
	@Override
	public void forEach(CharShortConsumer action) {
		Objects.requireNonNull(action);
		for(ObjectIterator<Char2ShortMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Char2ShortMap.Entry entry = iter.next();
			action.accept(entry.getCharKey(), entry.getShortValue());
		}
	}

	@Override
	public CharSet keySet() {
		return new AbstractCharSet() {
			@Override
			public boolean remove(char o) {
				return AbstractChar2ShortMap.this.remove(o) != getDefaultReturnValue();
			}
			
			@Override
			public boolean add(char o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public CharIterator iterator() {
				return new CharIterator() {
					ObjectIterator<Char2ShortMap.Entry> iter = getFastIterator(AbstractChar2ShortMap.this);
					@Override
					public boolean hasNext() {
						return iter.hasNext();
					}

					@Override
					public char nextChar() {
						return iter.next().getCharKey();
					}
					
					@Override
					public void remove() {
						iter.remove();
					}
				};
			}
			
			@Override
			public int size() {
				return AbstractChar2ShortMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractChar2ShortMap.this.clear();
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
				return AbstractChar2ShortMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractChar2ShortMap.this.clear();
			}
			
			@Override
			public ShortIterator iterator() {
				return new ShortIterator() {
					ObjectIterator<Char2ShortMap.Entry> iter = getFastIterator(AbstractChar2ShortMap.this);
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
	public ObjectSet<Map.Entry<Character, Short>> entrySet() {
		return (ObjectSet)char2ShortEntrySet();
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o instanceof Map) {
			if(size() != ((Map<?, ?>)o).size()) return false;
			if(o instanceof Char2ShortMap) return char2ShortEntrySet().containsAll(((Char2ShortMap)o).char2ShortEntrySet());
			return char2ShortEntrySet().containsAll(((Map<?, ?>)o).entrySet());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		ObjectIterator<Char2ShortMap.Entry> iter = getFastIterator(this);
		while(iter.hasNext()) hash += iter.next().hashCode();
		return hash;
	}
	
	public static class ReversedChar2ShortOrderedMap extends AbstractChar2ShortMap implements Char2ShortOrderedMap {
		Char2ShortOrderedMap map;
		
		public ReversedChar2ShortOrderedMap(Char2ShortOrderedMap map) {
			this.map = map;
		}
		@Override
		public AbstractChar2ShortMap setDefaultReturnValue(short v) {
			map.setDefaultReturnValue(v);
			return this;
		}
		@Override
		public short getDefaultReturnValue() { return map.getDefaultReturnValue(); }
		@Override
		public Char2ShortOrderedMap copy() { throw new UnsupportedOperationException(); }
		@Override
		public short put(char key, short value) { return map.put(key, value); }
		@Override
		public short putIfAbsent(char key, short value) { return map.putIfAbsent(key, value); }
		@Override
		public short addTo(char key, short value) { return map.addTo(key, value); }
		@Override
		public short subFrom(char key, short value) { return map.subFrom(key, value); }
		@Override
		public short remove(char key) { return map.remove(key); }
		@Override
		public boolean remove(char key, short value) { return map.remove(key, value); }
		@Override
		public short removeOrDefault(char key, short defaultValue) { return map.removeOrDefault(key, defaultValue); }
		@Override
		public boolean containsKey(char key) { return map.containsKey(key); }
		@Override
		public boolean containsValue(short value) { return map.containsValue(value); }
		@Override
		public boolean replace(char key, short oldValue, short newValue) { return map.replace(key, oldValue, newValue); }
		@Override
		public short replace(char key, short value) { return map.replace(key, value); }
		@Override
		public void replaceShorts(Char2ShortMap m) { map.replaceShorts(m); }
		@Override
		public void replaceShorts(CharShortUnaryOperator mappingFunction) { map.replaceShorts(mappingFunction); }
		@Override
		public short computeShort(char key, CharShortUnaryOperator mappingFunction) { return map.computeShort(key, mappingFunction); }
		@Override
		public short computeShortIfAbsent(char key, Char2ShortFunction mappingFunction) { return map.computeShortIfAbsent(key, mappingFunction); }
		@Override
		public short supplyShortIfAbsent(char key, ShortSupplier valueProvider) { return map.supplyShortIfAbsent(key, valueProvider); }
		@Override
		public short computeShortIfPresent(char key, CharShortUnaryOperator mappingFunction) { return map.computeShortIfPresent(key, mappingFunction); }
		@Override
		public short computeShortNonDefault(char key, CharShortUnaryOperator mappingFunction) { return map.computeShortNonDefault(key, mappingFunction); }
		@Override
		public short computeShortIfAbsentNonDefault(char key, Char2ShortFunction mappingFunction) { return map.computeShortIfAbsentNonDefault(key, mappingFunction); }
		@Override
		public short supplyShortIfAbsentNonDefault(char key, ShortSupplier valueProvider) { return map.supplyShortIfAbsentNonDefault(key, valueProvider); }
		@Override
		public short computeShortIfPresentNonDefault(char key, CharShortUnaryOperator mappingFunction) { return map.computeShortIfPresentNonDefault(key, mappingFunction); }
		@Override
		public short mergeShort(char key, short value, ShortShortUnaryOperator mappingFunction) { return map.mergeShort(key, value, mappingFunction); }
		@Override
		public short getOrDefault(char key, short defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public short get(char key) { return map.get(key); }
		@Override
		public short putAndMoveToFirst(char key, short value) { return map.putAndMoveToLast(key, value); }
		@Override
		public short putAndMoveToLast(char key, short value) { return map.putAndMoveToFirst(key, value); }
		@Override
		public short putFirst(char key, short value) { return map.putLast(key, value); }
		@Override
		public short putLast(char key, short value) { return map.putFirst(key, value); }
		@Override
		public boolean moveToFirst(char key) { return map.moveToLast(key); }
		@Override
		public boolean moveToLast(char key) { return map.moveToFirst(key); }
		@Override
		public short getAndMoveToFirst(char key) { return map.getAndMoveToLast(key); }
		@Override
		public short getAndMoveToLast(char key) { return map.getAndMoveToFirst(key); }
		@Override
		public char firstCharKey() { return map.lastCharKey(); }
		@Override
		public char pollFirstCharKey() { return map.pollLastCharKey(); }
		@Override
		public char lastCharKey() { return map.firstCharKey(); }
		@Override
		public char pollLastCharKey() { return map.pollFirstCharKey(); }
		@Override
		public short firstShortValue() { return map.lastShortValue(); }
		@Override
		public short lastShortValue() { return map.firstShortValue(); }
		@Override
		public Char2ShortMap.Entry firstEntry() { return map.lastEntry(); }
		@Override
		public Char2ShortMap.Entry lastEntry() { return map.firstEntry(); }
		@Override
		public Char2ShortMap.Entry pollFirstEntry() { return map.pollLastEntry(); }
		@Override
		public Char2ShortMap.Entry pollLastEntry() { return map.pollFirstEntry(); }
		@Override
		public ObjectOrderedSet<Char2ShortMap.Entry> char2ShortEntrySet() { return new AbstractObjectSet.ReversedObjectOrderedSet<>(map.char2ShortEntrySet()); }
		@Override
		public CharOrderedSet keySet() { return new AbstractCharSet.ReversedCharOrderedSet(map.keySet()); }
		@Override
		public ShortOrderedCollection values() { return map.values().reversed(); }
		@Override
		public Char2ShortOrderedMap reversed() { return map; }
	}

	
	/**
	 * A Simple Type Specific Entry class to reduce boxing/unboxing
	 */
	public static class BasicEntry implements Char2ShortMap.Entry {
		protected char key;
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
		public BasicEntry(Character key, Short value) {
			this.key = key.charValue();
			this.value = value.shortValue();
		}
		
		/**
		 * A Type Specific Constructor
		 * @param key the key of a entry
		 * @param value the value of a entry 
		 */
		public BasicEntry(char key, short value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * A Helper method for fast replacing values
		 * @param key the key that should be replaced
		 * @param value the value that should be replaced
		 */
		public void set(char key, short value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public char getCharKey() {
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
				if(obj instanceof Char2ShortMap.Entry) {
					Char2ShortMap.Entry entry = (Char2ShortMap.Entry)obj;
					return key == entry.getCharKey() && value == entry.getShortValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Character && value instanceof Short && this.key == ((Character)key).charValue() && this.value == ((Short)value).shortValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Character.hashCode(key) ^ Short.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Character.toString(key) + "=" + Short.toString(value);
		}
	}
}