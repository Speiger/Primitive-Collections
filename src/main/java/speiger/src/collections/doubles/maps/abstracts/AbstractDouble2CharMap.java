package speiger.src.collections.doubles.maps.abstracts;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

import speiger.src.collections.doubles.collections.DoubleIterator;
import speiger.src.collections.doubles.functions.consumer.DoubleCharConsumer;
import speiger.src.collections.doubles.functions.function.Double2CharFunction;
import speiger.src.collections.doubles.functions.function.DoubleCharUnaryOperator;
import speiger.src.collections.doubles.maps.interfaces.Double2CharMap;
import speiger.src.collections.doubles.sets.AbstractDoubleSet;
import speiger.src.collections.doubles.sets.DoubleSet;
import speiger.src.collections.doubles.utils.maps.Double2CharMaps;
import speiger.src.collections.chars.collections.AbstractCharCollection;
import speiger.src.collections.chars.collections.CharCollection;
import speiger.src.collections.chars.collections.CharIterator;
import speiger.src.collections.chars.functions.function.CharCharUnaryOperator;
import speiger.src.collections.chars.functions.CharSupplier;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Base Implementation of a Type Specific Map to reduce boxing/unboxing
 */
public abstract class AbstractDouble2CharMap extends AbstractMap<Double, Character> implements Double2CharMap
{
	protected char defaultReturnValue = (char)0;
	
	@Override
	public char getDefaultReturnValue() {
		return defaultReturnValue;
	}
	
	@Override
	public AbstractDouble2CharMap setDefaultReturnValue(char v) {
		defaultReturnValue = v;
		return this;
	}
	
	@Override
	public Double2CharMap copy() { 
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public Character put(Double key, Character value) {
		return Character.valueOf(put(key.doubleValue(), value.charValue()));
	}
	
	@Override
	public void addToAll(Double2CharMap m) {
		for(Double2CharMap.Entry entry : Double2CharMaps.fastIterable(m))
			addTo(entry.getDoubleKey(), entry.getCharValue());
	}
	
	@Override
	public void putAll(Double2CharMap m) {
		for(ObjectIterator<Double2CharMap.Entry> iter = Double2CharMaps.fastIterator(m);iter.hasNext();) {
			Double2CharMap.Entry entry = iter.next();
			put(entry.getDoubleKey(), entry.getCharValue());
		}
	}
	
	@Override
	public void putAll(Map<? extends Double, ? extends Character> m)
	{
		if(m instanceof Double2CharMap) putAll((Double2CharMap)m);
		else super.putAll(m);
	}
	
	@Override
	public void putAll(double[] keys, char[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);
	}
	
	@Override
	public void putAll(Double[] keys, Character[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);		
	}
	
	@Override
	public void putAllIfAbsent(Double2CharMap m) {
		for(Double2CharMap.Entry entry : Double2CharMaps.fastIterable(m))
			putIfAbsent(entry.getDoubleKey(), entry.getCharValue());
	}
	
	
	@Override
	public boolean containsKey(double key) {
		for(DoubleIterator iter = keySet().iterator();iter.hasNext();)
			if(Double.doubleToLongBits(iter.nextDouble()) == Double.doubleToLongBits(key)) return true;
		return false;
	}
	
	@Override
	public boolean containsValue(char value) {
		for(CharIterator iter = values().iterator();iter.hasNext();)
			if(iter.nextChar() == value) return true;
		return false;
	}
	
	@Override
	public boolean replace(double key, char oldValue, char newValue) {
		char curValue = get(key);
		if (curValue != oldValue || (curValue == getDefaultReturnValue() && !containsKey(key))) {
			return false;
		}
		put(key, newValue);
		return true;
	}

	@Override
	public char replace(double key, char value) {
		char curValue;
		if ((curValue = get(key)) != getDefaultReturnValue() || containsKey(key)) {
			curValue = put(key, value);
		}
		return curValue;
	}
	
	@Override
	public void replaceChars(Double2CharMap m) {
		for(Double2CharMap.Entry entry : Double2CharMaps.fastIterable(m))
			replace(entry.getDoubleKey(), entry.getCharValue());
	}
	
	@Override
	public void replaceChars(DoubleCharUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(ObjectIterator<Double2CharMap.Entry> iter = Double2CharMaps.fastIterator(this);iter.hasNext();) {
			Double2CharMap.Entry entry = iter.next();
			entry.setValue(mappingFunction.applyAsChar(entry.getDoubleKey(), entry.getCharValue()));
		}
	}

	@Override
	public char computeChar(double key, DoubleCharUnaryOperator mappingFunction) {
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
	public char computeCharIfAbsent(double key, Double2CharFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		char value;
		if((value = get(key)) == getDefaultReturnValue() || !containsKey(key)) {
			char newValue = mappingFunction.get(key);
			if(newValue != getDefaultReturnValue()) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public char supplyCharIfAbsent(double key, CharSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		char value;
		if((value = get(key)) == getDefaultReturnValue() || !containsKey(key)) {
			char newValue = valueProvider.getChar();
			if(newValue != getDefaultReturnValue()) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public char computeCharIfPresent(double key, DoubleCharUnaryOperator mappingFunction) {
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
	public char mergeChar(double key, char value, CharCharUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		char oldValue = get(key);
		char newValue = oldValue == getDefaultReturnValue() ? value : mappingFunction.applyAsChar(oldValue, value);
		if(newValue == getDefaultReturnValue()) remove(key);
		else put(key, newValue);
		return newValue;
	}
	
	@Override
	public void mergeAllChar(Double2CharMap m, CharCharUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Double2CharMap.Entry entry : Double2CharMaps.fastIterable(m)) {
			double key = entry.getDoubleKey();
			char oldValue = get(key);
			char newValue = oldValue == getDefaultReturnValue() ? entry.getCharValue() : mappingFunction.applyAsChar(oldValue, entry.getCharValue());
			if(newValue == getDefaultReturnValue()) remove(key);
			else put(key, newValue);
		}
	}
	
	@Override
	public Character get(Object key) {
		return Character.valueOf(key instanceof Double ? get(((Double)key).doubleValue()) : getDefaultReturnValue());
	}
	
	@Override
	public Character getOrDefault(Object key, Character defaultValue) {
		return Character.valueOf(key instanceof Double ? getOrDefault(((Double)key).doubleValue(), defaultValue.charValue()) : getDefaultReturnValue());
	}
	
	@Override
	public char getOrDefault(double key, char defaultValue) {
		char value = get(key);
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	@Override
	public void forEach(DoubleCharConsumer action) {
		Objects.requireNonNull(action);
		for(ObjectIterator<Double2CharMap.Entry> iter = Double2CharMaps.fastIterator(this);iter.hasNext();) {
			Double2CharMap.Entry entry = iter.next();
			action.accept(entry.getDoubleKey(), entry.getCharValue());
		}
	}

	@Override
	public DoubleSet keySet() {
		return new AbstractDoubleSet() {
			@Override
			public boolean remove(double o) {
				return AbstractDouble2CharMap.this.remove(o) != getDefaultReturnValue();
			}
			
			@Override
			public boolean add(double o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public DoubleIterator iterator() {
				return new DoubleIterator() {
					ObjectIterator<Double2CharMap.Entry> iter = Double2CharMaps.fastIterator(AbstractDouble2CharMap.this);
					@Override
					public boolean hasNext() {
						return iter.hasNext();
					}

					@Override
					public double nextDouble() {
						return iter.next().getDoubleKey();
					}
					
					@Override
					public void remove() {
						iter.remove();
					}
				};
			}
			
			@Override
			public int size() {
				return AbstractDouble2CharMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractDouble2CharMap.this.clear();
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
				return AbstractDouble2CharMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractDouble2CharMap.this.clear();
			}
			
			@Override
			public CharIterator iterator() {
				return new CharIterator() {
					ObjectIterator<Double2CharMap.Entry> iter = Double2CharMaps.fastIterator(AbstractDouble2CharMap.this);
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
	public ObjectSet<Map.Entry<Double, Character>> entrySet() {
		return (ObjectSet)double2CharEntrySet();
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o instanceof Map) {
			if(size() != ((Map<?, ?>)o).size()) return false;
			if(o instanceof Double2CharMap) return double2CharEntrySet().containsAll(((Double2CharMap)o).double2CharEntrySet());
			return double2CharEntrySet().containsAll(((Map<?, ?>)o).entrySet());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		ObjectIterator<Double2CharMap.Entry> iter = Double2CharMaps.fastIterator(this);
		while(iter.hasNext()) hash += iter.next().hashCode();
		return hash;
	}
	
	/**
	 * A Simple Type Specific Entry class to reduce boxing/unboxing
	 */
	public static class BasicEntry implements Double2CharMap.Entry {
		protected double key;
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
		public BasicEntry(Double key, Character value) {
			this.key = key.doubleValue();
			this.value = value.charValue();
		}
		
		/**
		 * A Type Specific Constructor
		 * @param key the key of a entry
		 * @param value the value of a entry 
		 */
		public BasicEntry(double key, char value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * A Helper method for fast replacing values
		 * @param key the key that should be replaced
		 * @param value the value that should be replaced
		 */
		public void set(double key, char value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public double getDoubleKey() {
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
				if(obj instanceof Double2CharMap.Entry) {
					Double2CharMap.Entry entry = (Double2CharMap.Entry)obj;
					return Double.doubleToLongBits(key) == Double.doubleToLongBits(entry.getDoubleKey()) && value == entry.getCharValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Double && value instanceof Character && Double.doubleToLongBits(this.key) == Double.doubleToLongBits(((Double)key).doubleValue()) && this.value == ((Character)value).charValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Double.hashCode(key) ^ Character.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Double.toString(key) + "=" + Character.toString(value);
		}
	}
}