package speiger.src.collections.objects.maps.abstracts;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.functions.consumer.ObjectCharConsumer;
import speiger.src.collections.objects.functions.function.Object2CharFunction;
import speiger.src.collections.objects.functions.function.ObjectCharUnaryOperator;
import speiger.src.collections.objects.maps.interfaces.Object2CharMap;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.utils.maps.Object2CharMaps;
import speiger.src.collections.chars.collections.AbstractCharCollection;
import speiger.src.collections.chars.collections.CharCollection;
import speiger.src.collections.chars.collections.CharIterator;
import speiger.src.collections.chars.functions.function.CharCharUnaryOperator;
import speiger.src.collections.chars.functions.CharSupplier;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Base Implementation of a Type Specific Map to reduce boxing/unboxing
 * @param <T> the type of elements maintained by this Collection
 */
public abstract class AbstractObject2CharMap<T> extends AbstractMap<T, Character> implements Object2CharMap<T>
{
	protected char defaultReturnValue = (char)0;
	
	@Override
	public char getDefaultReturnValue() {
		return defaultReturnValue;
	}
	
	@Override
	public AbstractObject2CharMap<T> setDefaultReturnValue(char v) {
		defaultReturnValue = v;
		return this;
	}
	
	@Override
	public Object2CharMap<T> copy() { 
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public Character put(T key, Character value) {
		return Character.valueOf(put(key, value.charValue()));
	}
	
	@Override
	public void addToAll(Object2CharMap<T> m) {
		for(Object2CharMap.Entry<T> entry : Object2CharMaps.fastIterable(m))
			addTo(entry.getKey(), entry.getCharValue());
	}
	
	@Override
	public void putAll(Object2CharMap<T> m) {
		for(ObjectIterator<Object2CharMap.Entry<T>> iter = Object2CharMaps.fastIterator(m);iter.hasNext();) {
			Object2CharMap.Entry<T> entry = iter.next();
			put(entry.getKey(), entry.getCharValue());
		}
	}
	
	@Override
	public void putAll(Map<? extends T, ? extends Character> m)
	{
		if(m instanceof Object2CharMap) putAll((Object2CharMap<T>)m);
		else super.putAll(m);
	}
	
	@Override
	public void putAll(T[] keys, char[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);
	}
	
	@Override
	public void putAll(T[] keys, Character[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);		
	}
	
	@Override
	public void putAllIfAbsent(Object2CharMap<T> m) {
		for(Object2CharMap.Entry<T> entry : Object2CharMaps.fastIterable(m))
			putIfAbsent(entry.getKey(), entry.getCharValue());
	}
	
	
	@Override
	public boolean containsKey(Object key) {
		for(ObjectIterator<T> iter = keySet().iterator();iter.hasNext();)
			if(Objects.equals(key, iter.next())) return true;
		return false;
	}
	
	@Override
	public boolean containsValue(char value) {
		for(CharIterator iter = values().iterator();iter.hasNext();)
			if(iter.nextChar() == value) return true;
		return false;
	}
	
	@Override
	public boolean replace(T key, char oldValue, char newValue) {
		char curValue = getChar(key);
		if (curValue != oldValue || (curValue == getDefaultReturnValue() && !containsKey(key))) {
			return false;
		}
		put(key, newValue);
		return true;
	}

	@Override
	public char replace(T key, char value) {
		char curValue;
		if ((curValue = getChar(key)) != getDefaultReturnValue() || containsKey(key)) {
			curValue = put(key, value);
		}
		return curValue;
	}
	
	@Override
	public void replaceChars(Object2CharMap<T> m) {
		for(Object2CharMap.Entry<T> entry : Object2CharMaps.fastIterable(m))
			replace(entry.getKey(), entry.getCharValue());
	}
	
	@Override
	public void replaceChars(ObjectCharUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(ObjectIterator<Object2CharMap.Entry<T>> iter = Object2CharMaps.fastIterator(this);iter.hasNext();) {
			Object2CharMap.Entry<T> entry = iter.next();
			entry.setValue(mappingFunction.applyAsChar(entry.getKey(), entry.getCharValue()));
		}
	}

	@Override
	public char computeChar(T key, ObjectCharUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		char value = getChar(key);
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
	public char computeCharIfAbsent(T key, Object2CharFunction<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		char value;
		if((value = getChar(key)) == getDefaultReturnValue() || !containsKey(key)) {
			char newValue = mappingFunction.getChar(key);
			if(newValue != getDefaultReturnValue()) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public char supplyCharIfAbsent(T key, CharSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		char value;
		if((value = getChar(key)) == getDefaultReturnValue() || !containsKey(key)) {
			char newValue = valueProvider.getChar();
			if(newValue != getDefaultReturnValue()) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public char computeCharIfPresent(T key, ObjectCharUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		char value;
		if((value = getChar(key)) != getDefaultReturnValue() || containsKey(key)) {
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
	public char mergeChar(T key, char value, CharCharUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		char oldValue = getChar(key);
		char newValue = oldValue == getDefaultReturnValue() ? value : mappingFunction.applyAsChar(oldValue, value);
		if(newValue == getDefaultReturnValue()) remove(key);
		else put(key, newValue);
		return newValue;
	}
	
	@Override
	public void mergeAllChar(Object2CharMap<T> m, CharCharUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Object2CharMap.Entry<T> entry : Object2CharMaps.fastIterable(m)) {
			T key = entry.getKey();
			char oldValue = getChar(key);
			char newValue = oldValue == getDefaultReturnValue() ? entry.getCharValue() : mappingFunction.applyAsChar(oldValue, entry.getCharValue());
			if(newValue == getDefaultReturnValue()) remove(key);
			else put(key, newValue);
		}
	}
	
	@Override
	public Character get(Object key) {
		return Character.valueOf(getChar((T)key));
	}
	
	@Override
	public Character getOrDefault(Object key, Character defaultValue) {
		Character value = get(key);
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	@Override
	public void forEach(ObjectCharConsumer<T> action) {
		Objects.requireNonNull(action);
		for(ObjectIterator<Object2CharMap.Entry<T>> iter = Object2CharMaps.fastIterator(this);iter.hasNext();) {
			Object2CharMap.Entry<T> entry = iter.next();
			action.accept(entry.getKey(), entry.getCharValue());
		}
	}

	@Override
	public ObjectSet<T> keySet() {
		return new AbstractObjectSet<T>() {
			@Override
			public boolean remove(Object o) {
				return AbstractObject2CharMap.this.remove(o) != getDefaultReturnValue();
			}
			
			@Override
			public boolean add(T o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public ObjectIterator<T> iterator() {
				return new ObjectIterator<T>() {
					ObjectIterator<Object2CharMap.Entry<T>> iter = Object2CharMaps.fastIterator(AbstractObject2CharMap.this);
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
				return AbstractObject2CharMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractObject2CharMap.this.clear();
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
				return AbstractObject2CharMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractObject2CharMap.this.clear();
			}
			
			@Override
			public CharIterator iterator() {
				return new CharIterator() {
					ObjectIterator<Object2CharMap.Entry<T>> iter = Object2CharMaps.fastIterator(AbstractObject2CharMap.this);
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
	public ObjectSet<Map.Entry<T, Character>> entrySet() {
		return (ObjectSet)object2CharEntrySet();
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o instanceof Map) {
			if(size() != ((Map<?, ?>)o).size()) return false;
			if(o instanceof Object2CharMap) return object2CharEntrySet().containsAll(((Object2CharMap<T>)o).object2CharEntrySet());
			return object2CharEntrySet().containsAll(((Map<?, ?>)o).entrySet());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		ObjectIterator<Object2CharMap.Entry<T>> iter = Object2CharMaps.fastIterator(this);
		while(iter.hasNext()) hash += iter.next().hashCode();
		return hash;
	}
	
	/**
	 * A Simple Type Specific Entry class to reduce boxing/unboxing
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class BasicEntry<T> implements Object2CharMap.Entry<T> {
		protected T key;
		protected char value;
		
		/**
		 * A basic Empty constructor
		 */
		public BasicEntry() {}
		/**
		 * A Type Specific Constructor
		 * @param key the key of a entry
		 * @param value the value of a entry 
		 */
		public BasicEntry(T key, char value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * A Helper method for fast replacing values
		 * @param key the key that should be replaced
		 * @param value the value that should be replaced
		 */
		public void set(T key, char value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public T getKey() {
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
				if(obj instanceof Object2CharMap.Entry) {
					Object2CharMap.Entry<T> entry = (Object2CharMap.Entry<T>)obj;
					return Objects.equals(key, entry.getKey()) && value == entry.getCharValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return value instanceof Character && Objects.equals(this.key, key) && this.value == ((Character)value).charValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Objects.hashCode(key) ^ Character.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Objects.toString(key) + "=" + Character.toString(value);
		}
	}
}