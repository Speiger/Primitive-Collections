package speiger.src.collections.chars.maps.abstracts;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

import speiger.src.collections.chars.collections.CharIterator;
import speiger.src.collections.chars.functions.consumer.CharLongConsumer;
import speiger.src.collections.chars.functions.function.Char2LongFunction;
import speiger.src.collections.chars.functions.function.CharLongUnaryOperator;
import speiger.src.collections.chars.maps.interfaces.Char2LongMap;
import speiger.src.collections.chars.maps.interfaces.Char2LongOrderedMap;
import speiger.src.collections.chars.sets.CharOrderedSet;
import speiger.src.collections.longs.collections.LongOrderedCollection;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
import speiger.src.collections.chars.sets.AbstractCharSet;
import speiger.src.collections.chars.sets.CharSet;
import speiger.src.collections.chars.utils.maps.Char2LongMaps;
import speiger.src.collections.longs.collections.AbstractLongCollection;
import speiger.src.collections.longs.collections.LongCollection;
import speiger.src.collections.longs.collections.LongIterator;
import speiger.src.collections.longs.functions.function.LongLongUnaryOperator;
import speiger.src.collections.longs.functions.LongSupplier;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Base Implementation of a Type Specific Map to reduce boxing/unboxing
 */
public abstract class AbstractChar2LongMap extends AbstractMap<Character, Long> implements Char2LongMap
{
	protected long defaultReturnValue = -1L;
	
	@Override
	public long getDefaultReturnValue() {
		return defaultReturnValue;
	}
	
	@Override
	public AbstractChar2LongMap setDefaultReturnValue(long v) {
		defaultReturnValue = v;
		return this;
	}
	
	protected ObjectIterable<Char2LongMap.Entry> getFastIterable(Char2LongMap map) {
		return Char2LongMaps.fastIterable(map);
	}
	
	protected ObjectIterator<Char2LongMap.Entry> getFastIterator(Char2LongMap map) {
		return Char2LongMaps.fastIterator(map);
	}
	
	@Override
	public Char2LongMap copy() { 
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public Long put(Character key, Long value) {
		return Long.valueOf(put(key.charValue(), value.longValue()));
	}
	
	@Override
	public void addToAll(Char2LongMap m) {
		for(Char2LongMap.Entry entry : getFastIterable(m))
			addTo(entry.getCharKey(), entry.getLongValue());
	}
	
	@Override
	public void putAll(Char2LongMap m) {
		for(ObjectIterator<Char2LongMap.Entry> iter = getFastIterator(m);iter.hasNext();) {
			Char2LongMap.Entry entry = iter.next();
			put(entry.getCharKey(), entry.getLongValue());
		}
	}
	
	@Override
	public void putAll(Map<? extends Character, ? extends Long> m)
	{
		if(m instanceof Char2LongMap) putAll((Char2LongMap)m);
		else super.putAll(m);
	}
	
	@Override
	public void putAll(char[] keys, long[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);
	}
	
	@Override
	public void putAll(Character[] keys, Long[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i].charValue(), values[i].longValue());		
	}
	
	@Override
	public void putAllIfAbsent(Char2LongMap m) {
		for(Char2LongMap.Entry entry : getFastIterable(m))
			putIfAbsent(entry.getCharKey(), entry.getLongValue());
	}
	
	
	@Override
	public boolean containsKey(char key) {
		for(CharIterator iter = keySet().iterator();iter.hasNext();)
			if(iter.nextChar() == key) return true;
		return false;
	}
	
	@Override
	public boolean containsValue(long value) {
		for(LongIterator iter = values().iterator();iter.hasNext();)
			if(iter.nextLong() == value) return true;
		return false;
	}
	
	@Override
	public boolean replace(char key, long oldValue, long newValue) {
		long curValue = get(key);
		if (curValue != oldValue || (curValue == getDefaultReturnValue() && !containsKey(key))) {
			return false;
		}
		put(key, newValue);
		return true;
	}

	@Override
	public long replace(char key, long value) {
		long curValue;
		if ((curValue = get(key)) != getDefaultReturnValue() || containsKey(key)) {
			curValue = put(key, value);
		}
		return curValue;
	}
	
	@Override
	public void replaceLongs(Char2LongMap m) {
		for(Char2LongMap.Entry entry : getFastIterable(m))
			replace(entry.getCharKey(), entry.getLongValue());
	}
	
	@Override
	public void replaceLongs(CharLongUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(ObjectIterator<Char2LongMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Char2LongMap.Entry entry = iter.next();
			entry.setValue(mappingFunction.applyAsLong(entry.getCharKey(), entry.getLongValue()));
		}
	}

	@Override
	public long computeLong(char key, CharLongUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		long newValue = mappingFunction.applyAsLong(key, get(key));
		put(key, newValue);
		return newValue;
	}
	
	@Override
	public long computeLongIfAbsent(char key, Char2LongFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(!containsKey(key)) {
			long newValue = mappingFunction.applyAsLong(key);
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public long supplyLongIfAbsent(char key, LongSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		if(!containsKey(key)) {
			long newValue = valueProvider.getAsLong();
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public long computeLongIfPresent(char key, CharLongUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(containsKey(key)) {
			long newValue = mappingFunction.applyAsLong(key, get(key));
			put(key, newValue);
			return newValue;
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public long computeLongNonDefault(char key, CharLongUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		long value = get(key);
		long newValue = mappingFunction.applyAsLong(key, value);
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
	public long computeLongIfAbsentNonDefault(char key, Char2LongFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		long value;
		if((value = get(key)) == getDefaultReturnValue() || !containsKey(key)) {
			long newValue = mappingFunction.applyAsLong(key);
			if(newValue != getDefaultReturnValue()) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public long supplyLongIfAbsentNonDefault(char key, LongSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		long value;
		if((value = get(key)) == getDefaultReturnValue() || !containsKey(key)) {
			long newValue = valueProvider.getAsLong();
			if(newValue != getDefaultReturnValue()) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public long computeLongIfPresentNonDefault(char key, CharLongUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		long value;
		if((value = get(key)) != getDefaultReturnValue() || containsKey(key)) {
			long newValue = mappingFunction.applyAsLong(key, value);
			if(newValue != getDefaultReturnValue()) {
				put(key, newValue);
				return newValue;
			}
			remove(key);
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public long mergeLong(char key, long value, LongLongUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		long oldValue = get(key);
		long newValue = oldValue == getDefaultReturnValue() ? value : mappingFunction.applyAsLong(oldValue, value);
		if(newValue == getDefaultReturnValue()) remove(key);
		else put(key, newValue);
		return newValue;
	}
	
	@Override
	public void mergeAllLong(Char2LongMap m, LongLongUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Char2LongMap.Entry entry : getFastIterable(m)) {
			char key = entry.getCharKey();
			long oldValue = get(key);
			long newValue = oldValue == getDefaultReturnValue() ? entry.getLongValue() : mappingFunction.applyAsLong(oldValue, entry.getLongValue());
			if(newValue == getDefaultReturnValue()) remove(key);
			else put(key, newValue);
		}
	}
	
	@Override
	public Long get(Object key) {
		return Long.valueOf(key instanceof Character ? get(((Character)key).charValue()) : getDefaultReturnValue());
	}
	
	@Override
	public Long getOrDefault(Object key, Long defaultValue) {
		return Long.valueOf(key instanceof Character ? getOrDefault(((Character)key).charValue(), defaultValue.longValue()) : getDefaultReturnValue());
	}
	
	@Override
	public long getOrDefault(char key, long defaultValue) {
		long value = get(key);
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	
	@Override
	public Long remove(Object key) {
		return key instanceof Character ? Long.valueOf(remove(((Character)key).charValue())) : Long.valueOf(getDefaultReturnValue());
	}
	
	@Override
	public void forEach(CharLongConsumer action) {
		Objects.requireNonNull(action);
		for(ObjectIterator<Char2LongMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Char2LongMap.Entry entry = iter.next();
			action.accept(entry.getCharKey(), entry.getLongValue());
		}
	}

	@Override
	public CharSet keySet() {
		return new AbstractCharSet() {
			@Override
			public boolean remove(char o) {
				return AbstractChar2LongMap.this.remove(o) != getDefaultReturnValue();
			}
			
			@Override
			public boolean add(char o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public CharIterator iterator() {
				return new CharIterator() {
					ObjectIterator<Char2LongMap.Entry> iter = getFastIterator(AbstractChar2LongMap.this);
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
				return AbstractChar2LongMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractChar2LongMap.this.clear();
			}
		};
	}

	@Override
	public LongCollection values() {
		return new AbstractLongCollection() {
			@Override
			public boolean add(long o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public int size() {
				return AbstractChar2LongMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractChar2LongMap.this.clear();
			}
			
			@Override
			public LongIterator iterator() {
				return new LongIterator() {
					ObjectIterator<Char2LongMap.Entry> iter = getFastIterator(AbstractChar2LongMap.this);
					@Override
					public boolean hasNext() {
						return iter.hasNext();
					}
					
					@Override
					public long nextLong() {
						return iter.next().getLongValue();
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
	public ObjectSet<Map.Entry<Character, Long>> entrySet() {
		return (ObjectSet)char2LongEntrySet();
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o instanceof Map) {
			if(size() != ((Map<?, ?>)o).size()) return false;
			if(o instanceof Char2LongMap) return char2LongEntrySet().containsAll(((Char2LongMap)o).char2LongEntrySet());
			return char2LongEntrySet().containsAll(((Map<?, ?>)o).entrySet());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		ObjectIterator<Char2LongMap.Entry> iter = getFastIterator(this);
		while(iter.hasNext()) hash += iter.next().hashCode();
		return hash;
	}
	
	public static class ReversedChar2LongOrderedMap extends AbstractChar2LongMap implements Char2LongOrderedMap {
		Char2LongOrderedMap map;
		
		public ReversedChar2LongOrderedMap(Char2LongOrderedMap map) {
			this.map = map;
		}
		@Override
		public AbstractChar2LongMap setDefaultReturnValue(long v) {
			map.setDefaultReturnValue(v);
			return this;
		}
		@Override
		public long getDefaultReturnValue() { return map.getDefaultReturnValue(); }
		@Override
		public Char2LongOrderedMap copy() { throw new UnsupportedOperationException(); }
		@Override
		public long put(char key, long value) { return map.put(key, value); }
		@Override
		public long putIfAbsent(char key, long value) { return map.putIfAbsent(key, value); }
		@Override
		public long addTo(char key, long value) { return map.addTo(key, value); }
		@Override
		public long subFrom(char key, long value) { return map.subFrom(key, value); }
		@Override
		public long remove(char key) { return map.remove(key); }
		@Override
		public boolean remove(char key, long value) { return map.remove(key, value); }
		@Override
		public long removeOrDefault(char key, long defaultValue) { return map.removeOrDefault(key, defaultValue); }
		@Override
		public boolean containsKey(char key) { return map.containsKey(key); }
		@Override
		public boolean containsValue(long value) { return map.containsValue(value); }
		@Override
		public boolean replace(char key, long oldValue, long newValue) { return map.replace(key, oldValue, newValue); }
		@Override
		public long replace(char key, long value) { return map.replace(key, value); }
		@Override
		public void replaceLongs(Char2LongMap m) { map.replaceLongs(m); }
		@Override
		public void replaceLongs(CharLongUnaryOperator mappingFunction) { map.replaceLongs(mappingFunction); }
		@Override
		public long computeLong(char key, CharLongUnaryOperator mappingFunction) { return map.computeLong(key, mappingFunction); }
		@Override
		public long computeLongIfAbsent(char key, Char2LongFunction mappingFunction) { return map.computeLongIfAbsent(key, mappingFunction); }
		@Override
		public long supplyLongIfAbsent(char key, LongSupplier valueProvider) { return map.supplyLongIfAbsent(key, valueProvider); }
		@Override
		public long computeLongIfPresent(char key, CharLongUnaryOperator mappingFunction) { return map.computeLongIfPresent(key, mappingFunction); }
		@Override
		public long computeLongNonDefault(char key, CharLongUnaryOperator mappingFunction) { return map.computeLongNonDefault(key, mappingFunction); }
		@Override
		public long computeLongIfAbsentNonDefault(char key, Char2LongFunction mappingFunction) { return map.computeLongIfAbsentNonDefault(key, mappingFunction); }
		@Override
		public long supplyLongIfAbsentNonDefault(char key, LongSupplier valueProvider) { return map.supplyLongIfAbsentNonDefault(key, valueProvider); }
		@Override
		public long computeLongIfPresentNonDefault(char key, CharLongUnaryOperator mappingFunction) { return map.computeLongIfPresentNonDefault(key, mappingFunction); }
		@Override
		public long mergeLong(char key, long value, LongLongUnaryOperator mappingFunction) { return map.mergeLong(key, value, mappingFunction); }
		@Override
		public long getOrDefault(char key, long defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public long get(char key) { return map.get(key); }
		@Override
		public long putAndMoveToFirst(char key, long value) { return map.putAndMoveToLast(key, value); }
		@Override
		public long putAndMoveToLast(char key, long value) { return map.putAndMoveToFirst(key, value); }
		@Override
		public long putFirst(char key, long value) { return map.putLast(key, value); }
		@Override
		public long putLast(char key, long value) { return map.putFirst(key, value); }
		@Override
		public boolean moveToFirst(char key) { return map.moveToLast(key); }
		@Override
		public boolean moveToLast(char key) { return map.moveToFirst(key); }
		@Override
		public long getAndMoveToFirst(char key) { return map.getAndMoveToLast(key); }
		@Override
		public long getAndMoveToLast(char key) { return map.getAndMoveToFirst(key); }
		@Override
		public char firstCharKey() { return map.lastCharKey(); }
		@Override
		public char pollFirstCharKey() { return map.pollLastCharKey(); }
		@Override
		public char lastCharKey() { return map.firstCharKey(); }
		@Override
		public char pollLastCharKey() { return map.pollFirstCharKey(); }
		@Override
		public long firstLongValue() { return map.lastLongValue(); }
		@Override
		public long lastLongValue() { return map.firstLongValue(); }
		@Override
		public Char2LongMap.Entry firstEntry() { return map.lastEntry(); }
		@Override
		public Char2LongMap.Entry lastEntry() { return map.firstEntry(); }
		@Override
		public Char2LongMap.Entry pollFirstEntry() { return map.pollLastEntry(); }
		@Override
		public Char2LongMap.Entry pollLastEntry() { return map.pollFirstEntry(); }
		@Override
		public ObjectOrderedSet<Char2LongMap.Entry> char2LongEntrySet() { return new AbstractObjectSet.ReversedObjectOrderedSet<>(map.char2LongEntrySet()); }
		@Override
		public CharOrderedSet keySet() { return new AbstractCharSet.ReversedCharOrderedSet(map.keySet()); }
		@Override
		public LongOrderedCollection values() { return map.values().reversed(); }
		@Override
		public Char2LongOrderedMap reversed() { return map; }
	}

	
	/**
	 * A Simple Type Specific Entry class to reduce boxing/unboxing
	 */
	public static class BasicEntry implements Char2LongMap.Entry {
		protected char key;
		protected long value;
		
		/**
		 * A basic Empty constructor
		 */
		public BasicEntry() {}
		/**
		 * A Boxed Constructor for supporting java variants
		 * @param key the key of a entry
		 * @param value the value of a entry
		 */
		public BasicEntry(Character key, Long value) {
			this.key = key.charValue();
			this.value = value.longValue();
		}
		
		/**
		 * A Type Specific Constructor
		 * @param key the key of a entry
		 * @param value the value of a entry 
		 */
		public BasicEntry(char key, long value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * A Helper method for fast replacing values
		 * @param key the key that should be replaced
		 * @param value the value that should be replaced
		 */
		public void set(char key, long value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public char getCharKey() {
			return key;
		}

		@Override
		public long getLongValue() {
			return value;
		}

		@Override
		public long setValue(long value) {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Map.Entry) {
				if(obj instanceof Char2LongMap.Entry) {
					Char2LongMap.Entry entry = (Char2LongMap.Entry)obj;
					return key == entry.getCharKey() && value == entry.getLongValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Character && value instanceof Long && this.key == ((Character)key).charValue() && this.value == ((Long)value).longValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Character.hashCode(key) ^ Long.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Character.toString(key) + "=" + Long.toString(value);
		}
	}
}