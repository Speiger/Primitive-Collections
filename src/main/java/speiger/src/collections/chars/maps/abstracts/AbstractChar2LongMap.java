package speiger.src.collections.chars.maps.abstracts;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

import speiger.src.collections.chars.collections.CharIterator;
import speiger.src.collections.chars.functions.consumer.CharLongConsumer;
import speiger.src.collections.chars.functions.function.Char2LongFunction;
import speiger.src.collections.chars.functions.function.CharLongUnaryOperator;
import speiger.src.collections.chars.maps.interfaces.Char2LongMap;
import speiger.src.collections.chars.sets.AbstractCharSet;
import speiger.src.collections.chars.sets.CharSet;
import speiger.src.collections.chars.utils.maps.Char2LongMaps;
import speiger.src.collections.longs.collections.AbstractLongCollection;
import speiger.src.collections.longs.collections.LongCollection;
import speiger.src.collections.longs.collections.LongIterator;
import speiger.src.collections.longs.functions.function.LongLongUnaryOperator;
import speiger.src.collections.longs.functions.LongSupplier;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Base Implementation of a Type Specific Map to reduce boxing/unboxing
 */
public abstract class AbstractChar2LongMap extends AbstractMap<Character, Long> implements Char2LongMap
{
	protected long defaultReturnValue = 0L;
	
	@Override
	public long getDefaultReturnValue() {
		return defaultReturnValue;
	}
	
	@Override
	public AbstractChar2LongMap setDefaultReturnValue(long v) {
		defaultReturnValue = v;
		return this;
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
		for(Char2LongMap.Entry entry : Char2LongMaps.fastIterable(m))
			addTo(entry.getCharKey(), entry.getLongValue());
	}
	
	@Override
	public void putAll(Char2LongMap m) {
		for(ObjectIterator<Char2LongMap.Entry> iter = Char2LongMaps.fastIterator(m);iter.hasNext();) {
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
		for(int i = 0;i<size;i++) put(keys[i], values[i]);		
	}
	
	@Override
	public void putAllIfAbsent(Char2LongMap m) {
		for(Char2LongMap.Entry entry : Char2LongMaps.fastIterable(m))
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
		for(Char2LongMap.Entry entry : Char2LongMaps.fastIterable(m))
			replace(entry.getCharKey(), entry.getLongValue());
	}
	
	@Override
	public void replaceLongs(CharLongUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(ObjectIterator<Char2LongMap.Entry> iter = Char2LongMaps.fastIterator(this);iter.hasNext();) {
			Char2LongMap.Entry entry = iter.next();
			entry.setValue(mappingFunction.applyAsLong(entry.getCharKey(), entry.getLongValue()));
		}
	}

	@Override
	public long computeLong(char key, CharLongUnaryOperator mappingFunction) {
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
	public long computeLongIfAbsent(char key, Char2LongFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		long value;
		if((value = get(key)) == getDefaultReturnValue() || !containsKey(key)) {
			long newValue = mappingFunction.get(key);
			if(newValue != getDefaultReturnValue()) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public long supplyLongIfAbsent(char key, LongSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		long value;
		if((value = get(key)) == getDefaultReturnValue() || !containsKey(key)) {
			long newValue = valueProvider.getLong();
			if(newValue != getDefaultReturnValue()) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public long computeLongIfPresent(char key, CharLongUnaryOperator mappingFunction) {
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
		for(Char2LongMap.Entry entry : Char2LongMaps.fastIterable(m)) {
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
	public void forEach(CharLongConsumer action) {
		Objects.requireNonNull(action);
		for(ObjectIterator<Char2LongMap.Entry> iter = Char2LongMaps.fastIterator(this);iter.hasNext();) {
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
					ObjectIterator<Char2LongMap.Entry> iter = Char2LongMaps.fastIterator(AbstractChar2LongMap.this);
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
					ObjectIterator<Char2LongMap.Entry> iter = Char2LongMaps.fastIterator(AbstractChar2LongMap.this);
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
		ObjectIterator<Char2LongMap.Entry> iter = Char2LongMaps.fastIterator(this);
		while(iter.hasNext()) hash += iter.next().hashCode();
		return hash;
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