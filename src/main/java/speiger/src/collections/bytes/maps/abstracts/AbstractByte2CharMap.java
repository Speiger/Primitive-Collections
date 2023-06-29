package speiger.src.collections.bytes.maps.abstracts;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

import speiger.src.collections.bytes.collections.ByteIterator;
import speiger.src.collections.bytes.functions.consumer.ByteCharConsumer;
import speiger.src.collections.bytes.functions.function.Byte2CharFunction;
import speiger.src.collections.bytes.functions.function.ByteCharUnaryOperator;
import speiger.src.collections.bytes.maps.interfaces.Byte2CharMap;
import speiger.src.collections.bytes.sets.AbstractByteSet;
import speiger.src.collections.bytes.sets.ByteSet;
import speiger.src.collections.bytes.utils.maps.Byte2CharMaps;
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
public abstract class AbstractByte2CharMap extends AbstractMap<Byte, Character> implements Byte2CharMap
{
	protected char defaultReturnValue = (char)0;
	
	@Override
	public char getDefaultReturnValue() {
		return defaultReturnValue;
	}
	
	@Override
	public AbstractByte2CharMap setDefaultReturnValue(char v) {
		defaultReturnValue = v;
		return this;
	}
	
	protected ObjectIterable<Byte2CharMap.Entry> getFastIterable(Byte2CharMap map) {
		return Byte2CharMaps.fastIterable(map);
	}
	
	protected ObjectIterator<Byte2CharMap.Entry> getFastIterator(Byte2CharMap map) {
		return Byte2CharMaps.fastIterator(map);
	}
	
	@Override
	public Byte2CharMap copy() { 
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public Character put(Byte key, Character value) {
		return Character.valueOf(put(key.byteValue(), value.charValue()));
	}
	
	@Override
	public void addToAll(Byte2CharMap m) {
		for(Byte2CharMap.Entry entry : getFastIterable(m))
			addTo(entry.getByteKey(), entry.getCharValue());
	}
	
	@Override
	public void putAll(Byte2CharMap m) {
		for(ObjectIterator<Byte2CharMap.Entry> iter = getFastIterator(m);iter.hasNext();) {
			Byte2CharMap.Entry entry = iter.next();
			put(entry.getByteKey(), entry.getCharValue());
		}
	}
	
	@Override
	public void putAll(Map<? extends Byte, ? extends Character> m)
	{
		if(m instanceof Byte2CharMap) putAll((Byte2CharMap)m);
		else super.putAll(m);
	}
	
	@Override
	public void putAll(byte[] keys, char[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);
	}
	
	@Override
	public void putAll(Byte[] keys, Character[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);		
	}
	
	@Override
	public void putAllIfAbsent(Byte2CharMap m) {
		for(Byte2CharMap.Entry entry : getFastIterable(m))
			putIfAbsent(entry.getByteKey(), entry.getCharValue());
	}
	
	
	@Override
	public boolean containsKey(byte key) {
		for(ByteIterator iter = keySet().iterator();iter.hasNext();)
			if(iter.nextByte() == key) return true;
		return false;
	}
	
	@Override
	public boolean containsValue(char value) {
		for(CharIterator iter = values().iterator();iter.hasNext();)
			if(iter.nextChar() == value) return true;
		return false;
	}
	
	@Override
	public boolean replace(byte key, char oldValue, char newValue) {
		char curValue = get(key);
		if (curValue != oldValue || (curValue == getDefaultReturnValue() && !containsKey(key))) {
			return false;
		}
		put(key, newValue);
		return true;
	}

	@Override
	public char replace(byte key, char value) {
		char curValue;
		if ((curValue = get(key)) != getDefaultReturnValue() || containsKey(key)) {
			curValue = put(key, value);
		}
		return curValue;
	}
	
	@Override
	public void replaceChars(Byte2CharMap m) {
		for(Byte2CharMap.Entry entry : getFastIterable(m))
			replace(entry.getByteKey(), entry.getCharValue());
	}
	
	@Override
	public void replaceChars(ByteCharUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(ObjectIterator<Byte2CharMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Byte2CharMap.Entry entry = iter.next();
			entry.setValue(mappingFunction.applyAsChar(entry.getByteKey(), entry.getCharValue()));
		}
	}

	@Override
	public char computeChar(byte key, ByteCharUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		char newValue = mappingFunction.applyAsChar(key, get(key));
		put(key, newValue);
		return newValue;
	}
	
	@Override
	public char computeCharIfAbsent(byte key, Byte2CharFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(!containsKey(key)) {
			char newValue = mappingFunction.applyAsChar(key);
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public char supplyCharIfAbsent(byte key, CharSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		if(!containsKey(key)) {
			char newValue = valueProvider.getAsChar();
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public char computeCharIfPresent(byte key, ByteCharUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(containsKey(key)) {
			char newValue = mappingFunction.applyAsChar(key, get(key));
			put(key, newValue);
			return newValue;
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public char computeCharNonDefault(byte key, ByteCharUnaryOperator mappingFunction) {
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
	public char computeCharIfAbsentNonDefault(byte key, Byte2CharFunction mappingFunction) {
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
	public char supplyCharIfAbsentNonDefault(byte key, CharSupplier valueProvider) {
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
	public char computeCharIfPresentNonDefault(byte key, ByteCharUnaryOperator mappingFunction) {
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
	public char mergeChar(byte key, char value, CharCharUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		char oldValue = get(key);
		char newValue = oldValue == getDefaultReturnValue() ? value : mappingFunction.applyAsChar(oldValue, value);
		if(newValue == getDefaultReturnValue()) remove(key);
		else put(key, newValue);
		return newValue;
	}
	
	@Override
	public void mergeAllChar(Byte2CharMap m, CharCharUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Byte2CharMap.Entry entry : getFastIterable(m)) {
			byte key = entry.getByteKey();
			char oldValue = get(key);
			char newValue = oldValue == getDefaultReturnValue() ? entry.getCharValue() : mappingFunction.applyAsChar(oldValue, entry.getCharValue());
			if(newValue == getDefaultReturnValue()) remove(key);
			else put(key, newValue);
		}
	}
	
	@Override
	public Character get(Object key) {
		return Character.valueOf(key instanceof Byte ? get(((Byte)key).byteValue()) : getDefaultReturnValue());
	}
	
	@Override
	public Character getOrDefault(Object key, Character defaultValue) {
		return Character.valueOf(key instanceof Byte ? getOrDefault(((Byte)key).byteValue(), defaultValue.charValue()) : getDefaultReturnValue());
	}
	
	@Override
	public char getOrDefault(byte key, char defaultValue) {
		char value = get(key);
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	
	@Override
	public Character remove(Object key) {
		return key instanceof Byte ? Character.valueOf(remove(((Byte)key).byteValue())) : Character.valueOf(getDefaultReturnValue());
	}
	
	@Override
	public void forEach(ByteCharConsumer action) {
		Objects.requireNonNull(action);
		for(ObjectIterator<Byte2CharMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Byte2CharMap.Entry entry = iter.next();
			action.accept(entry.getByteKey(), entry.getCharValue());
		}
	}

	@Override
	public ByteSet keySet() {
		return new AbstractByteSet() {
			@Override
			public boolean remove(byte o) {
				return AbstractByte2CharMap.this.remove(o) != getDefaultReturnValue();
			}
			
			@Override
			public boolean add(byte o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public ByteIterator iterator() {
				return new ByteIterator() {
					ObjectIterator<Byte2CharMap.Entry> iter = getFastIterator(AbstractByte2CharMap.this);
					@Override
					public boolean hasNext() {
						return iter.hasNext();
					}

					@Override
					public byte nextByte() {
						return iter.next().getByteKey();
					}
					
					@Override
					public void remove() {
						iter.remove();
					}
				};
			}
			
			@Override
			public int size() {
				return AbstractByte2CharMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractByte2CharMap.this.clear();
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
				return AbstractByte2CharMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractByte2CharMap.this.clear();
			}
			
			@Override
			public CharIterator iterator() {
				return new CharIterator() {
					ObjectIterator<Byte2CharMap.Entry> iter = getFastIterator(AbstractByte2CharMap.this);
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
	public ObjectSet<Map.Entry<Byte, Character>> entrySet() {
		return (ObjectSet)byte2CharEntrySet();
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o instanceof Map) {
			if(size() != ((Map<?, ?>)o).size()) return false;
			if(o instanceof Byte2CharMap) return byte2CharEntrySet().containsAll(((Byte2CharMap)o).byte2CharEntrySet());
			return byte2CharEntrySet().containsAll(((Map<?, ?>)o).entrySet());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		ObjectIterator<Byte2CharMap.Entry> iter = getFastIterator(this);
		while(iter.hasNext()) hash += iter.next().hashCode();
		return hash;
	}
	
	/**
	 * A Simple Type Specific Entry class to reduce boxing/unboxing
	 */
	public static class BasicEntry implements Byte2CharMap.Entry {
		protected byte key;
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
		public BasicEntry(Byte key, Character value) {
			this.key = key.byteValue();
			this.value = value.charValue();
		}
		
		/**
		 * A Type Specific Constructor
		 * @param key the key of a entry
		 * @param value the value of a entry 
		 */
		public BasicEntry(byte key, char value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * A Helper method for fast replacing values
		 * @param key the key that should be replaced
		 * @param value the value that should be replaced
		 */
		public void set(byte key, char value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public byte getByteKey() {
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
				if(obj instanceof Byte2CharMap.Entry) {
					Byte2CharMap.Entry entry = (Byte2CharMap.Entry)obj;
					return key == entry.getByteKey() && value == entry.getCharValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Byte && value instanceof Character && this.key == ((Byte)key).byteValue() && this.value == ((Character)value).charValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Byte.hashCode(key) ^ Character.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Byte.toString(key) + "=" + Character.toString(value);
		}
	}
}