package speiger.src.collections.chars.maps.abstracts;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

import speiger.src.collections.chars.collections.CharIterator;
import speiger.src.collections.chars.functions.consumer.CharDoubleConsumer;
import speiger.src.collections.chars.functions.function.Char2DoubleFunction;
import speiger.src.collections.chars.functions.function.CharDoubleUnaryOperator;
import speiger.src.collections.chars.maps.interfaces.Char2DoubleMap;
import speiger.src.collections.chars.sets.AbstractCharSet;
import speiger.src.collections.chars.sets.CharSet;
import speiger.src.collections.chars.utils.maps.Char2DoubleMaps;
import speiger.src.collections.doubles.collections.AbstractDoubleCollection;
import speiger.src.collections.doubles.collections.DoubleCollection;
import speiger.src.collections.doubles.collections.DoubleIterator;
import speiger.src.collections.doubles.functions.function.DoubleDoubleUnaryOperator;
import speiger.src.collections.doubles.functions.DoubleSupplier;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Base Implementation of a Type Specific Map to reduce boxing/unboxing
 */
public abstract class AbstractChar2DoubleMap extends AbstractMap<Character, Double> implements Char2DoubleMap
{
	protected double defaultReturnValue = 0D;
	
	@Override
	public double getDefaultReturnValue() {
		return defaultReturnValue;
	}
	
	@Override
	public AbstractChar2DoubleMap setDefaultReturnValue(double v) {
		defaultReturnValue = v;
		return this;
	}
	
	protected ObjectIterable<Char2DoubleMap.Entry> getFastIterable(Char2DoubleMap map) {
		return Char2DoubleMaps.fastIterable(map);
	}
	
	protected ObjectIterator<Char2DoubleMap.Entry> getFastIterator(Char2DoubleMap map) {
		return Char2DoubleMaps.fastIterator(map);
	}
	
	@Override
	public Char2DoubleMap copy() { 
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public Double put(Character key, Double value) {
		return Double.valueOf(put(key.charValue(), value.doubleValue()));
	}
	
	@Override
	public void addToAll(Char2DoubleMap m) {
		for(Char2DoubleMap.Entry entry : getFastIterable(m))
			addTo(entry.getCharKey(), entry.getDoubleValue());
	}
	
	@Override
	public void putAll(Char2DoubleMap m) {
		for(ObjectIterator<Char2DoubleMap.Entry> iter = getFastIterator(m);iter.hasNext();) {
			Char2DoubleMap.Entry entry = iter.next();
			put(entry.getCharKey(), entry.getDoubleValue());
		}
	}
	
	@Override
	public void putAll(Map<? extends Character, ? extends Double> m)
	{
		if(m instanceof Char2DoubleMap) putAll((Char2DoubleMap)m);
		else super.putAll(m);
	}
	
	@Override
	public void putAll(char[] keys, double[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);
	}
	
	@Override
	public void putAll(Character[] keys, Double[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);		
	}
	
	@Override
	public void putAllIfAbsent(Char2DoubleMap m) {
		for(Char2DoubleMap.Entry entry : getFastIterable(m))
			putIfAbsent(entry.getCharKey(), entry.getDoubleValue());
	}
	
	
	@Override
	public boolean containsKey(char key) {
		for(CharIterator iter = keySet().iterator();iter.hasNext();)
			if(iter.nextChar() == key) return true;
		return false;
	}
	
	@Override
	public boolean containsValue(double value) {
		for(DoubleIterator iter = values().iterator();iter.hasNext();)
			if(Double.doubleToLongBits(iter.nextDouble()) == Double.doubleToLongBits(value)) return true;
		return false;
	}
	
	@Override
	public boolean replace(char key, double oldValue, double newValue) {
		double curValue = get(key);
		if (Double.doubleToLongBits(curValue) != Double.doubleToLongBits(oldValue) || (Double.doubleToLongBits(curValue) == Double.doubleToLongBits(getDefaultReturnValue()) && !containsKey(key))) {
			return false;
		}
		put(key, newValue);
		return true;
	}

	@Override
	public double replace(char key, double value) {
		double curValue;
		if (Double.doubleToLongBits((curValue = get(key))) != Double.doubleToLongBits(getDefaultReturnValue()) || containsKey(key)) {
			curValue = put(key, value);
		}
		return curValue;
	}
	
	@Override
	public void replaceDoubles(Char2DoubleMap m) {
		for(Char2DoubleMap.Entry entry : getFastIterable(m))
			replace(entry.getCharKey(), entry.getDoubleValue());
	}
	
	@Override
	public void replaceDoubles(CharDoubleUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(ObjectIterator<Char2DoubleMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Char2DoubleMap.Entry entry = iter.next();
			entry.setValue(mappingFunction.applyAsDouble(entry.getCharKey(), entry.getDoubleValue()));
		}
	}

	@Override
	public double computeDouble(char key, CharDoubleUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		double newValue = mappingFunction.applyAsDouble(key, get(key));
		put(key, newValue);
		return newValue;
	}
	
	@Override
	public double computeDoubleIfAbsent(char key, Char2DoubleFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(!containsKey(key)) {
			double newValue = mappingFunction.applyAsDouble(key);
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public double supplyDoubleIfAbsent(char key, DoubleSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		if(!containsKey(key)) {
			double newValue = valueProvider.getAsDouble();
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public double computeDoubleIfPresent(char key, CharDoubleUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(containsKey(key)) {
			double newValue = mappingFunction.applyAsDouble(key, get(key));
			put(key, newValue);
			return newValue;
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public double computeDoubleNonDefault(char key, CharDoubleUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		double value = get(key);
		double newValue = mappingFunction.applyAsDouble(key, value);
		if(Double.doubleToLongBits(newValue) == Double.doubleToLongBits(getDefaultReturnValue())) {
			if(Double.doubleToLongBits(value) != Double.doubleToLongBits(getDefaultReturnValue()) || containsKey(key)) {
				remove(key);
				return getDefaultReturnValue();
			}
			return getDefaultReturnValue();
		}
		put(key, newValue);
		return newValue;
	}
	
	@Override
	public double computeDoubleIfAbsentNonDefault(char key, Char2DoubleFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		double value;
		if((value = get(key)) == getDefaultReturnValue() || !containsKey(key)) {
			double newValue = mappingFunction.applyAsDouble(key);
			if(Double.doubleToLongBits(newValue) != Double.doubleToLongBits(getDefaultReturnValue())) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public double supplyDoubleIfAbsentNonDefault(char key, DoubleSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		double value;
		if((value = get(key)) == getDefaultReturnValue() || !containsKey(key)) {
			double newValue = valueProvider.getAsDouble();
			if(Double.doubleToLongBits(newValue) != Double.doubleToLongBits(getDefaultReturnValue())) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public double computeDoubleIfPresentNonDefault(char key, CharDoubleUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		double value;
		if(Double.doubleToLongBits((value = get(key))) != Double.doubleToLongBits(getDefaultReturnValue()) || containsKey(key)) {
			double newValue = mappingFunction.applyAsDouble(key, value);
			if(Double.doubleToLongBits(newValue) != Double.doubleToLongBits(getDefaultReturnValue())) {
				put(key, newValue);
				return newValue;
			}
			remove(key);
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public double mergeDouble(char key, double value, DoubleDoubleUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		double oldValue = get(key);
		double newValue = Double.doubleToLongBits(oldValue) == Double.doubleToLongBits(getDefaultReturnValue()) ? value : mappingFunction.applyAsDouble(oldValue, value);
		if(Double.doubleToLongBits(newValue) == Double.doubleToLongBits(getDefaultReturnValue())) remove(key);
		else put(key, newValue);
		return newValue;
	}
	
	@Override
	public void mergeAllDouble(Char2DoubleMap m, DoubleDoubleUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Char2DoubleMap.Entry entry : getFastIterable(m)) {
			char key = entry.getCharKey();
			double oldValue = get(key);
			double newValue = Double.doubleToLongBits(oldValue) == Double.doubleToLongBits(getDefaultReturnValue()) ? entry.getDoubleValue() : mappingFunction.applyAsDouble(oldValue, entry.getDoubleValue());
			if(Double.doubleToLongBits(newValue) == Double.doubleToLongBits(getDefaultReturnValue())) remove(key);
			else put(key, newValue);
		}
	}
	
	@Override
	public Double get(Object key) {
		return Double.valueOf(key instanceof Character ? get(((Character)key).charValue()) : getDefaultReturnValue());
	}
	
	@Override
	public Double getOrDefault(Object key, Double defaultValue) {
		return Double.valueOf(key instanceof Character ? getOrDefault(((Character)key).charValue(), defaultValue.doubleValue()) : getDefaultReturnValue());
	}
	
	@Override
	public double getOrDefault(char key, double defaultValue) {
		double value = get(key);
		return Double.doubleToLongBits(value) != Double.doubleToLongBits(getDefaultReturnValue()) || containsKey(key) ? value : defaultValue;
	}
	
	
	@Override
	public Double remove(Object key) {
		return key instanceof Character ? Double.valueOf(remove(((Character)key).charValue())) : Double.valueOf(getDefaultReturnValue());
	}
	
	@Override
	public void forEach(CharDoubleConsumer action) {
		Objects.requireNonNull(action);
		for(ObjectIterator<Char2DoubleMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Char2DoubleMap.Entry entry = iter.next();
			action.accept(entry.getCharKey(), entry.getDoubleValue());
		}
	}

	@Override
	public CharSet keySet() {
		return new AbstractCharSet() {
			@Override
			public boolean remove(char o) {
				return Double.doubleToLongBits(AbstractChar2DoubleMap.this.remove(o)) != Double.doubleToLongBits(getDefaultReturnValue());
			}
			
			@Override
			public boolean add(char o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public CharIterator iterator() {
				return new CharIterator() {
					ObjectIterator<Char2DoubleMap.Entry> iter = getFastIterator(AbstractChar2DoubleMap.this);
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
				return AbstractChar2DoubleMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractChar2DoubleMap.this.clear();
			}
		};
	}

	@Override
	public DoubleCollection values() {
		return new AbstractDoubleCollection() {
			@Override
			public boolean add(double o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public int size() {
				return AbstractChar2DoubleMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractChar2DoubleMap.this.clear();
			}
			
			@Override
			public DoubleIterator iterator() {
				return new DoubleIterator() {
					ObjectIterator<Char2DoubleMap.Entry> iter = getFastIterator(AbstractChar2DoubleMap.this);
					@Override
					public boolean hasNext() {
						return iter.hasNext();
					}
					
					@Override
					public double nextDouble() {
						return iter.next().getDoubleValue();
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
	public ObjectSet<Map.Entry<Character, Double>> entrySet() {
		return (ObjectSet)char2DoubleEntrySet();
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o instanceof Map) {
			if(size() != ((Map<?, ?>)o).size()) return false;
			if(o instanceof Char2DoubleMap) return char2DoubleEntrySet().containsAll(((Char2DoubleMap)o).char2DoubleEntrySet());
			return char2DoubleEntrySet().containsAll(((Map<?, ?>)o).entrySet());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		ObjectIterator<Char2DoubleMap.Entry> iter = getFastIterator(this);
		while(iter.hasNext()) hash += iter.next().hashCode();
		return hash;
	}
	
	/**
	 * A Simple Type Specific Entry class to reduce boxing/unboxing
	 */
	public static class BasicEntry implements Char2DoubleMap.Entry {
		protected char key;
		protected double value;
		
		/**
		 * A basic Empty constructor
		 */
		public BasicEntry() {}
		/**
		 * A Boxed Constructor for supporting java variants
		 * @param key the key of a entry
		 * @param value the value of a entry
		 */
		public BasicEntry(Character key, Double value) {
			this.key = key.charValue();
			this.value = value.doubleValue();
		}
		
		/**
		 * A Type Specific Constructor
		 * @param key the key of a entry
		 * @param value the value of a entry 
		 */
		public BasicEntry(char key, double value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * A Helper method for fast replacing values
		 * @param key the key that should be replaced
		 * @param value the value that should be replaced
		 */
		public void set(char key, double value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public char getCharKey() {
			return key;
		}

		@Override
		public double getDoubleValue() {
			return value;
		}

		@Override
		public double setValue(double value) {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Map.Entry) {
				if(obj instanceof Char2DoubleMap.Entry) {
					Char2DoubleMap.Entry entry = (Char2DoubleMap.Entry)obj;
					return key == entry.getCharKey() && Double.doubleToLongBits(value) == Double.doubleToLongBits(entry.getDoubleValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Character && value instanceof Double && this.key == ((Character)key).charValue() && Double.doubleToLongBits(this.value) == Double.doubleToLongBits(((Double)value).doubleValue());
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Character.hashCode(key) ^ Double.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Character.toString(key) + "=" + Double.toString(value);
		}
	}
}