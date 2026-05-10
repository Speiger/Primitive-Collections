package speiger.src.collections.chars.maps.abstracts;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

import speiger.src.collections.chars.collections.CharIterator;
import speiger.src.collections.chars.functions.consumer.CharCharConsumer;
import speiger.src.collections.chars.functions.function.CharUnaryOperator;
import speiger.src.collections.chars.functions.function.CharCharUnaryOperator;
import speiger.src.collections.chars.maps.interfaces.Char2CharMap;
import speiger.src.collections.chars.maps.interfaces.Char2CharOrderedMap;
import speiger.src.collections.chars.sets.CharOrderedSet;
import speiger.src.collections.chars.collections.CharOrderedCollection;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
import speiger.src.collections.chars.sets.AbstractCharSet;
import speiger.src.collections.chars.sets.CharSet;
import speiger.src.collections.chars.utils.maps.Char2CharMaps;
import speiger.src.collections.chars.collections.AbstractCharCollection;
import speiger.src.collections.chars.collections.CharCollection;
import speiger.src.collections.chars.functions.CharSupplier;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Base Implementation of a Type Specific Map to reduce boxing/unboxing
 */
public abstract class AbstractChar2CharMap extends AbstractMap<Character, Character> implements Char2CharMap
{
	protected char defaultReturnValue = (char)-1;
	
	@Override
	public char getDefaultReturnValue() {
		return defaultReturnValue;
	}
	
	@Override
	public AbstractChar2CharMap setDefaultReturnValue(char v) {
		defaultReturnValue = v;
		return this;
	}
	
	protected ObjectIterable<Char2CharMap.Entry> getFastIterable(Char2CharMap map) {
		return Char2CharMaps.fastIterable(map);
	}
	
	protected ObjectIterator<Char2CharMap.Entry> getFastIterator(Char2CharMap map) {
		return Char2CharMaps.fastIterator(map);
	}
	
	@Override
	public Char2CharMap copy() { 
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public Character put(Character key, Character value) {
		return Character.valueOf(put(key.charValue(), value.charValue()));
	}
	
	@Override
	public void addToAll(Char2CharMap m) {
		for(Char2CharMap.Entry entry : getFastIterable(m))
			addTo(entry.getCharKey(), entry.getCharValue());
	}
	
	@Override
	public void putAll(Char2CharMap m) {
		for(ObjectIterator<Char2CharMap.Entry> iter = getFastIterator(m);iter.hasNext();) {
			Char2CharMap.Entry entry = iter.next();
			put(entry.getCharKey(), entry.getCharValue());
		}
	}
	
	@Override
	public void putAll(Map<? extends Character, ? extends Character> m)
	{
		if(m instanceof Char2CharMap) putAll((Char2CharMap)m);
		else super.putAll(m);
	}
	
	@Override
	public void putAll(char[] keys, char[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);
	}
	
	@Override
	public void putAll(Character[] keys, Character[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i].charValue(), values[i].charValue());		
	}
	
	@Override
	public void putAllIfAbsent(Char2CharMap m) {
		for(Char2CharMap.Entry entry : getFastIterable(m))
			putIfAbsent(entry.getCharKey(), entry.getCharValue());
	}
	
	
	@Override
	public boolean containsKey(char key) {
		for(CharIterator iter = keySet().iterator();iter.hasNext();)
			if(iter.nextChar() == key) return true;
		return false;
	}
	
	@Override
	public boolean containsValue(char value) {
		for(CharIterator iter = values().iterator();iter.hasNext();)
			if(iter.nextChar() == value) return true;
		return false;
	}
	
	@Override
	public boolean replace(char key, char oldValue, char newValue) {
		char curValue = get(key);
		if (curValue != oldValue || (curValue == getDefaultReturnValue() && !containsKey(key))) {
			return false;
		}
		put(key, newValue);
		return true;
	}

	@Override
	public char replace(char key, char value) {
		char curValue;
		if ((curValue = get(key)) != getDefaultReturnValue() || containsKey(key)) {
			curValue = put(key, value);
		}
		return curValue;
	}
	
	@Override
	public void replaceChars(Char2CharMap m) {
		for(Char2CharMap.Entry entry : getFastIterable(m))
			replace(entry.getCharKey(), entry.getCharValue());
	}
	
	@Override
	public void replaceChars(CharCharUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(ObjectIterator<Char2CharMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Char2CharMap.Entry entry = iter.next();
			entry.setValue(mappingFunction.applyAsChar(entry.getCharKey(), entry.getCharValue()));
		}
	}

	@Override
	public char computeChar(char key, CharCharUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		char newValue = mappingFunction.applyAsChar(key, get(key));
		put(key, newValue);
		return newValue;
	}
	
	@Override
	public char computeCharIfAbsent(char key, CharUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(!containsKey(key)) {
			char newValue = mappingFunction.applyAsChar(key);
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public char supplyCharIfAbsent(char key, CharSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		if(!containsKey(key)) {
			char newValue = valueProvider.getAsChar();
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public char computeCharIfPresent(char key, CharCharUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(containsKey(key)) {
			char newValue = mappingFunction.applyAsChar(key, get(key));
			put(key, newValue);
			return newValue;
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public char computeCharNonDefault(char key, CharCharUnaryOperator mappingFunction) {
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
	public char computeCharIfAbsentNonDefault(char key, CharUnaryOperator mappingFunction) {
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
	public char supplyCharIfAbsentNonDefault(char key, CharSupplier valueProvider) {
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
	public char computeCharIfPresentNonDefault(char key, CharCharUnaryOperator mappingFunction) {
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
	public char mergeChar(char key, char value, CharCharUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		char oldValue = get(key);
		char newValue = oldValue == getDefaultReturnValue() ? value : mappingFunction.applyAsChar(oldValue, value);
		if(newValue == getDefaultReturnValue()) remove(key);
		else put(key, newValue);
		return newValue;
	}
	
	@Override
	public void mergeAllChar(Char2CharMap m, CharCharUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Char2CharMap.Entry entry : getFastIterable(m)) {
			char key = entry.getCharKey();
			char oldValue = get(key);
			char newValue = oldValue == getDefaultReturnValue() ? entry.getCharValue() : mappingFunction.applyAsChar(oldValue, entry.getCharValue());
			if(newValue == getDefaultReturnValue()) remove(key);
			else put(key, newValue);
		}
	}
	
	@Override
	public Character get(Object key) {
		return Character.valueOf(key instanceof Character ? get(((Character)key).charValue()) : getDefaultReturnValue());
	}
	
	@Override
	public Character getOrDefault(Object key, Character defaultValue) {
		return Character.valueOf(key instanceof Character ? getOrDefault(((Character)key).charValue(), defaultValue.charValue()) : getDefaultReturnValue());
	}
	
	@Override
	public char getOrDefault(char key, char defaultValue) {
		char value = get(key);
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	
	@Override
	public Character remove(Object key) {
		return key instanceof Character ? Character.valueOf(remove(((Character)key).charValue())) : Character.valueOf(getDefaultReturnValue());
	}
	
	@Override
	public void forEach(CharCharConsumer action) {
		Objects.requireNonNull(action);
		for(ObjectIterator<Char2CharMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Char2CharMap.Entry entry = iter.next();
			action.accept(entry.getCharKey(), entry.getCharValue());
		}
	}

	@Override
	public CharSet keySet() {
		return new AbstractCharSet() {
			@Override
			public boolean remove(char o) {
				return AbstractChar2CharMap.this.remove(o) != getDefaultReturnValue();
			}
			
			@Override
			public boolean add(char o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public CharIterator iterator() {
				return new CharIterator() {
					ObjectIterator<Char2CharMap.Entry> iter = getFastIterator(AbstractChar2CharMap.this);
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
				return AbstractChar2CharMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractChar2CharMap.this.clear();
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
				return AbstractChar2CharMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractChar2CharMap.this.clear();
			}
			
			@Override
			public CharIterator iterator() {
				return new CharIterator() {
					ObjectIterator<Char2CharMap.Entry> iter = getFastIterator(AbstractChar2CharMap.this);
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
	public ObjectSet<Map.Entry<Character, Character>> entrySet() {
		return (ObjectSet)char2CharEntrySet();
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o instanceof Map) {
			if(size() != ((Map<?, ?>)o).size()) return false;
			if(o instanceof Char2CharMap) return char2CharEntrySet().containsAll(((Char2CharMap)o).char2CharEntrySet());
			return char2CharEntrySet().containsAll(((Map<?, ?>)o).entrySet());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		ObjectIterator<Char2CharMap.Entry> iter = getFastIterator(this);
		while(iter.hasNext()) hash += iter.next().hashCode();
		return hash;
	}
	
	public static class ReversedChar2CharOrderedMap extends AbstractChar2CharMap implements Char2CharOrderedMap {
		Char2CharOrderedMap map;
		
		public ReversedChar2CharOrderedMap(Char2CharOrderedMap map) {
			this.map = map;
		}
		@Override
		public AbstractChar2CharMap setDefaultReturnValue(char v) {
			map.setDefaultReturnValue(v);
			return this;
		}
		@Override
		public char getDefaultReturnValue() { return map.getDefaultReturnValue(); }
		@Override
		public Char2CharOrderedMap copy() { throw new UnsupportedOperationException(); }
		@Override
		public char put(char key, char value) { return map.put(key, value); }
		@Override
		public char putIfAbsent(char key, char value) { return map.putIfAbsent(key, value); }
		@Override
		public char addTo(char key, char value) { return map.addTo(key, value); }
		@Override
		public char subFrom(char key, char value) { return map.subFrom(key, value); }
		@Override
		public char remove(char key) { return map.remove(key); }
		@Override
		public boolean remove(char key, char value) { return map.remove(key, value); }
		@Override
		public char removeOrDefault(char key, char defaultValue) { return map.removeOrDefault(key, defaultValue); }
		@Override
		public boolean containsKey(char key) { return map.containsKey(key); }
		@Override
		public boolean containsValue(char value) { return map.containsValue(value); }
		@Override
		public boolean replace(char key, char oldValue, char newValue) { return map.replace(key, oldValue, newValue); }
		@Override
		public char replace(char key, char value) { return map.replace(key, value); }
		@Override
		public void replaceChars(Char2CharMap m) { map.replaceChars(m); }
		@Override
		public void replaceChars(CharCharUnaryOperator mappingFunction) { map.replaceChars(mappingFunction); }
		@Override
		public char computeChar(char key, CharCharUnaryOperator mappingFunction) { return map.computeChar(key, mappingFunction); }
		@Override
		public char computeCharIfAbsent(char key, CharUnaryOperator mappingFunction) { return map.computeCharIfAbsent(key, mappingFunction); }
		@Override
		public char supplyCharIfAbsent(char key, CharSupplier valueProvider) { return map.supplyCharIfAbsent(key, valueProvider); }
		@Override
		public char computeCharIfPresent(char key, CharCharUnaryOperator mappingFunction) { return map.computeCharIfPresent(key, mappingFunction); }
		@Override
		public char computeCharNonDefault(char key, CharCharUnaryOperator mappingFunction) { return map.computeCharNonDefault(key, mappingFunction); }
		@Override
		public char computeCharIfAbsentNonDefault(char key, CharUnaryOperator mappingFunction) { return map.computeCharIfAbsentNonDefault(key, mappingFunction); }
		@Override
		public char supplyCharIfAbsentNonDefault(char key, CharSupplier valueProvider) { return map.supplyCharIfAbsentNonDefault(key, valueProvider); }
		@Override
		public char computeCharIfPresentNonDefault(char key, CharCharUnaryOperator mappingFunction) { return map.computeCharIfPresentNonDefault(key, mappingFunction); }
		@Override
		public char mergeChar(char key, char value, CharCharUnaryOperator mappingFunction) { return map.mergeChar(key, value, mappingFunction); }
		@Override
		public char getOrDefault(char key, char defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public char get(char key) { return map.get(key); }
		@Override
		public char putAndMoveToFirst(char key, char value) { return map.putAndMoveToLast(key, value); }
		@Override
		public char putAndMoveToLast(char key, char value) { return map.putAndMoveToFirst(key, value); }
		@Override
		public char putFirst(char key, char value) { return map.putLast(key, value); }
		@Override
		public char putLast(char key, char value) { return map.putFirst(key, value); }
		@Override
		public boolean moveToFirst(char key) { return map.moveToLast(key); }
		@Override
		public boolean moveToLast(char key) { return map.moveToFirst(key); }
		@Override
		public char getAndMoveToFirst(char key) { return map.getAndMoveToLast(key); }
		@Override
		public char getAndMoveToLast(char key) { return map.getAndMoveToFirst(key); }
		@Override
		public char firstCharKey() { return map.lastCharKey(); }
		@Override
		public char pollFirstCharKey() { return map.pollLastCharKey(); }
		@Override
		public char lastCharKey() { return map.firstCharKey(); }
		@Override
		public char pollLastCharKey() { return map.pollFirstCharKey(); }
		@Override
		public char firstCharValue() { return map.lastCharValue(); }
		@Override
		public char lastCharValue() { return map.firstCharValue(); }
		@Override
		public Char2CharMap.Entry firstEntry() { return map.lastEntry(); }
		@Override
		public Char2CharMap.Entry lastEntry() { return map.firstEntry(); }
		@Override
		public Char2CharMap.Entry pollFirstEntry() { return map.pollLastEntry(); }
		@Override
		public Char2CharMap.Entry pollLastEntry() { return map.pollFirstEntry(); }
		@Override
		public ObjectOrderedSet<Char2CharMap.Entry> char2CharEntrySet() { return new AbstractObjectSet.ReversedObjectOrderedSet<>(map.char2CharEntrySet()); }
		@Override
		public CharOrderedSet keySet() { return new AbstractCharSet.ReversedCharOrderedSet(map.keySet()); }
		@Override
		public CharOrderedCollection values() { return map.values().reversed(); }
		@Override
		public Char2CharOrderedMap reversed() { return map; }
	}

	
	/**
	 * A Simple Type Specific Entry class to reduce boxing/unboxing
	 */
	public static class BasicEntry implements Char2CharMap.Entry {
		protected char key;
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
		public BasicEntry(Character key, Character value) {
			this.key = key.charValue();
			this.value = value.charValue();
		}
		
		/**
		 * A Type Specific Constructor
		 * @param key the key of a entry
		 * @param value the value of a entry 
		 */
		public BasicEntry(char key, char value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * A Helper method for fast replacing values
		 * @param key the key that should be replaced
		 * @param value the value that should be replaced
		 */
		public void set(char key, char value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public char getCharKey() {
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
				if(obj instanceof Char2CharMap.Entry) {
					Char2CharMap.Entry entry = (Char2CharMap.Entry)obj;
					return key == entry.getCharKey() && value == entry.getCharValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Character && value instanceof Character && this.key == ((Character)key).charValue() && this.value == ((Character)value).charValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Character.hashCode(key) ^ Character.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Character.toString(key) + "=" + Character.toString(value);
		}
	}
}