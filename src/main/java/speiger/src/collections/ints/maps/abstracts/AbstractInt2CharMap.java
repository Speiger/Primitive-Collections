package speiger.src.collections.ints.maps.abstracts;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

import speiger.src.collections.ints.collections.IntIterator;
import speiger.src.collections.ints.functions.consumer.IntCharConsumer;
import speiger.src.collections.ints.functions.function.Int2CharFunction;
import speiger.src.collections.ints.functions.function.IntCharUnaryOperator;
import speiger.src.collections.ints.maps.interfaces.Int2CharMap;
import speiger.src.collections.ints.maps.interfaces.Int2CharOrderedMap;
import speiger.src.collections.ints.sets.IntOrderedSet;
import speiger.src.collections.chars.collections.CharOrderedCollection;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
import speiger.src.collections.ints.sets.AbstractIntSet;
import speiger.src.collections.ints.sets.IntSet;
import speiger.src.collections.ints.utils.maps.Int2CharMaps;
import speiger.src.collections.chars.collections.AbstractCharCollection;
import speiger.src.collections.chars.collections.CharCollection;
import speiger.src.collections.chars.collections.CharIterator;
import speiger.src.collections.chars.functions.function.CharCharUnaryOperator;
import speiger.src.collections.chars.functions.CharSupplier;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Base Implementation of a Type Specific Map to reduce boxing/unboxing
 */
public abstract class AbstractInt2CharMap extends AbstractMap<Integer, Character> implements Int2CharMap
{
	protected char defaultReturnValue = (char)-1;
	
	@Override
	public char getDefaultReturnValue() {
		return defaultReturnValue;
	}
	
	@Override
	public AbstractInt2CharMap setDefaultReturnValue(char v) {
		defaultReturnValue = v;
		return this;
	}
	
	protected ObjectIterable<Int2CharMap.Entry> getFastIterable(Int2CharMap map) {
		return Int2CharMaps.fastIterable(map);
	}
	
	protected ObjectIterator<Int2CharMap.Entry> getFastIterator(Int2CharMap map) {
		return Int2CharMaps.fastIterator(map);
	}
	
	@Override
	public Int2CharMap copy() { 
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public Character put(Integer key, Character value) {
		return Character.valueOf(put(key.intValue(), value.charValue()));
	}
	
	@Override
	public void addToAll(Int2CharMap m) {
		for(Int2CharMap.Entry entry : getFastIterable(m))
			addTo(entry.getIntKey(), entry.getCharValue());
	}
	
	@Override
	public void putAll(Int2CharMap m) {
		for(ObjectIterator<Int2CharMap.Entry> iter = getFastIterator(m);iter.hasNext();) {
			Int2CharMap.Entry entry = iter.next();
			put(entry.getIntKey(), entry.getCharValue());
		}
	}
	
	@Override
	public void putAll(Map<? extends Integer, ? extends Character> m)
	{
		if(m instanceof Int2CharMap) putAll((Int2CharMap)m);
		else super.putAll(m);
	}
	
	@Override
	public void putAll(int[] keys, char[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);
	}
	
	@Override
	public void putAll(Integer[] keys, Character[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i].intValue(), values[i].charValue());		
	}
	
	@Override
	public void putAllIfAbsent(Int2CharMap m) {
		for(Int2CharMap.Entry entry : getFastIterable(m))
			putIfAbsent(entry.getIntKey(), entry.getCharValue());
	}
	
	
	@Override
	public boolean containsKey(int key) {
		for(IntIterator iter = keySet().iterator();iter.hasNext();)
			if(iter.nextInt() == key) return true;
		return false;
	}
	
	@Override
	public boolean containsValue(char value) {
		for(CharIterator iter = values().iterator();iter.hasNext();)
			if(iter.nextChar() == value) return true;
		return false;
	}
	
	@Override
	public boolean replace(int key, char oldValue, char newValue) {
		char curValue = get(key);
		if (curValue != oldValue || (curValue == getDefaultReturnValue() && !containsKey(key))) {
			return false;
		}
		put(key, newValue);
		return true;
	}

	@Override
	public char replace(int key, char value) {
		char curValue;
		if ((curValue = get(key)) != getDefaultReturnValue() || containsKey(key)) {
			curValue = put(key, value);
		}
		return curValue;
	}
	
	@Override
	public void replaceChars(Int2CharMap m) {
		for(Int2CharMap.Entry entry : getFastIterable(m))
			replace(entry.getIntKey(), entry.getCharValue());
	}
	
	@Override
	public void replaceChars(IntCharUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(ObjectIterator<Int2CharMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Int2CharMap.Entry entry = iter.next();
			entry.setValue(mappingFunction.applyAsChar(entry.getIntKey(), entry.getCharValue()));
		}
	}

	@Override
	public char computeChar(int key, IntCharUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		char newValue = mappingFunction.applyAsChar(key, get(key));
		put(key, newValue);
		return newValue;
	}
	
	@Override
	public char computeCharIfAbsent(int key, Int2CharFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(!containsKey(key)) {
			char newValue = mappingFunction.applyAsChar(key);
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public char supplyCharIfAbsent(int key, CharSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		if(!containsKey(key)) {
			char newValue = valueProvider.getAsChar();
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public char computeCharIfPresent(int key, IntCharUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(containsKey(key)) {
			char newValue = mappingFunction.applyAsChar(key, get(key));
			put(key, newValue);
			return newValue;
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public char computeCharNonDefault(int key, IntCharUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		char value = get(key);
		char newValue = mappingFunction.applyAsChar(key, value);
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
	public char computeCharIfAbsentNonDefault(int key, Int2CharFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		char value;
		if((value = get(key)) == getDefaultReturnValue() || !containsKey(key)) {
			char newValue = mappingFunction.applyAsChar(key);
			if(newValue != getDefaultReturnValue()) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public char supplyCharIfAbsentNonDefault(int key, CharSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		char value;
		if((value = get(key)) == getDefaultReturnValue() || !containsKey(key)) {
			char newValue = valueProvider.getAsChar();
			if(newValue != getDefaultReturnValue()) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public char computeCharIfPresentNonDefault(int key, IntCharUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		char value;
		if((value = get(key)) != getDefaultReturnValue() || containsKey(key)) {
			char newValue = mappingFunction.applyAsChar(key, value);
			if(newValue != getDefaultReturnValue()) {
				put(key, newValue);
				return newValue;
			}
			remove(key);
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public char mergeChar(int key, char value, CharCharUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		char oldValue = get(key);
		char newValue = oldValue == getDefaultReturnValue() ? value : mappingFunction.applyAsChar(oldValue, value);
		if(newValue == getDefaultReturnValue()) remove(key);
		else put(key, newValue);
		return newValue;
	}
	
	@Override
	public void mergeAllChar(Int2CharMap m, CharCharUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Int2CharMap.Entry entry : getFastIterable(m)) {
			int key = entry.getIntKey();
			char oldValue = get(key);
			char newValue = oldValue == getDefaultReturnValue() ? entry.getCharValue() : mappingFunction.applyAsChar(oldValue, entry.getCharValue());
			if(newValue == getDefaultReturnValue()) remove(key);
			else put(key, newValue);
		}
	}
	
	@Override
	public Character get(Object key) {
		return Character.valueOf(key instanceof Integer ? get(((Integer)key).intValue()) : getDefaultReturnValue());
	}
	
	@Override
	public Character getOrDefault(Object key, Character defaultValue) {
		return Character.valueOf(key instanceof Integer ? getOrDefault(((Integer)key).intValue(), defaultValue.charValue()) : getDefaultReturnValue());
	}
	
	@Override
	public char getOrDefault(int key, char defaultValue) {
		char value = get(key);
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	
	@Override
	public Character remove(Object key) {
		return key instanceof Integer ? Character.valueOf(remove(((Integer)key).intValue())) : Character.valueOf(getDefaultReturnValue());
	}
	
	@Override
	public void forEach(IntCharConsumer action) {
		Objects.requireNonNull(action);
		for(ObjectIterator<Int2CharMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Int2CharMap.Entry entry = iter.next();
			action.accept(entry.getIntKey(), entry.getCharValue());
		}
	}

	@Override
	public IntSet keySet() {
		return new AbstractIntSet() {
			@Override
			public boolean remove(int o) {
				return AbstractInt2CharMap.this.remove(o) != getDefaultReturnValue();
			}
			
			@Override
			public boolean add(int o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public IntIterator iterator() {
				return new IntIterator() {
					ObjectIterator<Int2CharMap.Entry> iter = getFastIterator(AbstractInt2CharMap.this);
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
				return AbstractInt2CharMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractInt2CharMap.this.clear();
			}
		};
	}

	@Override
	public CharCollection values() {
		return new AbstractCharCollection() {
			@Override
			public boolean add(char o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public int size() {
				return AbstractInt2CharMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractInt2CharMap.this.clear();
			}
			
			@Override
			public CharIterator iterator() {
				return new CharIterator() {
					ObjectIterator<Int2CharMap.Entry> iter = getFastIterator(AbstractInt2CharMap.this);
					@Override
					public boolean hasNext() {
						return iter.hasNext();
					}
					
					@Override
					public char nextChar() {
						return iter.next().getCharValue();
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
	public ObjectSet<Map.Entry<Integer, Character>> entrySet() {
		return (ObjectSet)int2CharEntrySet();
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o instanceof Map) {
			if(size() != ((Map<?, ?>)o).size()) return false;
			if(o instanceof Int2CharMap) return int2CharEntrySet().containsAll(((Int2CharMap)o).int2CharEntrySet());
			return int2CharEntrySet().containsAll(((Map<?, ?>)o).entrySet());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		ObjectIterator<Int2CharMap.Entry> iter = getFastIterator(this);
		while(iter.hasNext()) hash += iter.next().hashCode();
		return hash;
	}
	
	public static class ReversedInt2CharOrderedMap extends AbstractInt2CharMap implements Int2CharOrderedMap {
		Int2CharOrderedMap map;
		
		public ReversedInt2CharOrderedMap(Int2CharOrderedMap map) {
			this.map = map;
		}
		@Override
		public AbstractInt2CharMap setDefaultReturnValue(char v) {
			map.setDefaultReturnValue(v);
			return this;
		}
		@Override
		public char getDefaultReturnValue() { return map.getDefaultReturnValue(); }
		@Override
		public Int2CharOrderedMap copy() { throw new UnsupportedOperationException(); }
		@Override
		public char put(int key, char value) { return map.put(key, value); }
		@Override
		public char putIfAbsent(int key, char value) { return map.putIfAbsent(key, value); }
		@Override
		public char addTo(int key, char value) { return map.addTo(key, value); }
		@Override
		public char subFrom(int key, char value) { return map.subFrom(key, value); }
		@Override
		public char remove(int key) { return map.remove(key); }
		@Override
		public boolean remove(int key, char value) { return map.remove(key, value); }
		@Override
		public char removeOrDefault(int key, char defaultValue) { return map.removeOrDefault(key, defaultValue); }
		@Override
		public boolean containsKey(int key) { return map.containsKey(key); }
		@Override
		public boolean containsValue(char value) { return map.containsValue(value); }
		@Override
		public boolean replace(int key, char oldValue, char newValue) { return map.replace(key, oldValue, newValue); }
		@Override
		public char replace(int key, char value) { return map.replace(key, value); }
		@Override
		public void replaceChars(Int2CharMap m) { map.replaceChars(m); }
		@Override
		public void replaceChars(IntCharUnaryOperator mappingFunction) { map.replaceChars(mappingFunction); }
		@Override
		public char computeChar(int key, IntCharUnaryOperator mappingFunction) { return map.computeChar(key, mappingFunction); }
		@Override
		public char computeCharIfAbsent(int key, Int2CharFunction mappingFunction) { return map.computeCharIfAbsent(key, mappingFunction); }
		@Override
		public char supplyCharIfAbsent(int key, CharSupplier valueProvider) { return map.supplyCharIfAbsent(key, valueProvider); }
		@Override
		public char computeCharIfPresent(int key, IntCharUnaryOperator mappingFunction) { return map.computeCharIfPresent(key, mappingFunction); }
		@Override
		public char computeCharNonDefault(int key, IntCharUnaryOperator mappingFunction) { return map.computeCharNonDefault(key, mappingFunction); }
		@Override
		public char computeCharIfAbsentNonDefault(int key, Int2CharFunction mappingFunction) { return map.computeCharIfAbsentNonDefault(key, mappingFunction); }
		@Override
		public char supplyCharIfAbsentNonDefault(int key, CharSupplier valueProvider) { return map.supplyCharIfAbsentNonDefault(key, valueProvider); }
		@Override
		public char computeCharIfPresentNonDefault(int key, IntCharUnaryOperator mappingFunction) { return map.computeCharIfPresentNonDefault(key, mappingFunction); }
		@Override
		public char mergeChar(int key, char value, CharCharUnaryOperator mappingFunction) { return map.mergeChar(key, value, mappingFunction); }
		@Override
		public char getOrDefault(int key, char defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public char get(int key) { return map.get(key); }
		@Override
		public char putAndMoveToFirst(int key, char value) { return map.putAndMoveToLast(key, value); }
		@Override
		public char putAndMoveToLast(int key, char value) { return map.putAndMoveToFirst(key, value); }
		@Override
		public char putFirst(int key, char value) { return map.putLast(key, value); }
		@Override
		public char putLast(int key, char value) { return map.putFirst(key, value); }
		@Override
		public boolean moveToFirst(int key) { return map.moveToLast(key); }
		@Override
		public boolean moveToLast(int key) { return map.moveToFirst(key); }
		@Override
		public char getAndMoveToFirst(int key) { return map.getAndMoveToLast(key); }
		@Override
		public char getAndMoveToLast(int key) { return map.getAndMoveToFirst(key); }
		@Override
		public int firstIntKey() { return map.lastIntKey(); }
		@Override
		public int pollFirstIntKey() { return map.pollLastIntKey(); }
		@Override
		public int lastIntKey() { return map.firstIntKey(); }
		@Override
		public int pollLastIntKey() { return map.pollFirstIntKey(); }
		@Override
		public char firstCharValue() { return map.lastCharValue(); }
		@Override
		public char lastCharValue() { return map.firstCharValue(); }
		@Override
		public Int2CharMap.Entry firstEntry() { return map.lastEntry(); }
		@Override
		public Int2CharMap.Entry lastEntry() { return map.firstEntry(); }
		@Override
		public Int2CharMap.Entry pollFirstEntry() { return map.pollLastEntry(); }
		@Override
		public Int2CharMap.Entry pollLastEntry() { return map.pollFirstEntry(); }
		@Override
		public ObjectOrderedSet<Int2CharMap.Entry> int2CharEntrySet() { return new AbstractObjectSet.ReversedObjectOrderedSet<>(map.int2CharEntrySet()); }
		@Override
		public IntOrderedSet keySet() { return new AbstractIntSet.ReversedIntOrderedSet(map.keySet()); }
		@Override
		public CharOrderedCollection values() { return map.values().reversed(); }
		@Override
		public Int2CharOrderedMap reversed() { return map; }
	}

	
	/**
	 * A Simple Type Specific Entry class to reduce boxing/unboxing
	 */
	public static class BasicEntry implements Int2CharMap.Entry {
		protected int key;
		protected char value;
		
		/**
		 * A basic Empty constructor
		 */
		public BasicEntry() {}
		/**
		 * A Boxed Constructor for supporting java variants
		 * @param key the key of a entry
		 * @param value the value of a entry
		 */
		public BasicEntry(Integer key, Character value) {
			this.key = key.intValue();
			this.value = value.charValue();
		}
		
		/**
		 * A Type Specific Constructor
		 * @param key the key of a entry
		 * @param value the value of a entry 
		 */
		public BasicEntry(int key, char value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * A Helper method for fast replacing values
		 * @param key the key that should be replaced
		 * @param value the value that should be replaced
		 */
		public void set(int key, char value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public int getIntKey() {
			return key;
		}

		@Override
		public char getCharValue() {
			return value;
		}

		@Override
		public char setValue(char value) {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Map.Entry) {
				if(obj instanceof Int2CharMap.Entry) {
					Int2CharMap.Entry entry = (Int2CharMap.Entry)obj;
					return key == entry.getIntKey() && value == entry.getCharValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Integer && value instanceof Character && this.key == ((Integer)key).intValue() && this.value == ((Character)value).charValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Integer.hashCode(key) ^ Character.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Integer.toString(key) + "=" + Character.toString(value);
		}
	}
}