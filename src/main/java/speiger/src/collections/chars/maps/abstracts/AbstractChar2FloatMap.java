package speiger.src.collections.chars.maps.abstracts;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

import speiger.src.collections.chars.collections.CharIterator;
import speiger.src.collections.chars.functions.consumer.CharFloatConsumer;
import speiger.src.collections.chars.functions.function.Char2FloatFunction;
import speiger.src.collections.chars.functions.function.CharFloatUnaryOperator;
import speiger.src.collections.chars.maps.interfaces.Char2FloatMap;
import speiger.src.collections.chars.maps.interfaces.Char2FloatOrderedMap;
import speiger.src.collections.chars.sets.CharOrderedSet;
import speiger.src.collections.floats.collections.FloatOrderedCollection;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
import speiger.src.collections.chars.sets.AbstractCharSet;
import speiger.src.collections.chars.sets.CharSet;
import speiger.src.collections.chars.utils.maps.Char2FloatMaps;
import speiger.src.collections.floats.collections.AbstractFloatCollection;
import speiger.src.collections.floats.collections.FloatCollection;
import speiger.src.collections.floats.collections.FloatIterator;
import speiger.src.collections.floats.functions.function.FloatFloatUnaryOperator;
import speiger.src.collections.floats.functions.FloatSupplier;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Base Implementation of a Type Specific Map to reduce boxing/unboxing
 */
public abstract class AbstractChar2FloatMap extends AbstractMap<Character, Float> implements Char2FloatMap
{
	protected float defaultReturnValue = -1F;
	
	@Override
	public float getDefaultReturnValue() {
		return defaultReturnValue;
	}
	
	@Override
	public AbstractChar2FloatMap setDefaultReturnValue(float v) {
		defaultReturnValue = v;
		return this;
	}
	
	protected ObjectIterable<Char2FloatMap.Entry> getFastIterable(Char2FloatMap map) {
		return Char2FloatMaps.fastIterable(map);
	}
	
	protected ObjectIterator<Char2FloatMap.Entry> getFastIterator(Char2FloatMap map) {
		return Char2FloatMaps.fastIterator(map);
	}
	
	@Override
	public Char2FloatMap copy() { 
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public Float put(Character key, Float value) {
		return Float.valueOf(put(key.charValue(), value.floatValue()));
	}
	
	@Override
	public void addToAll(Char2FloatMap m) {
		for(Char2FloatMap.Entry entry : getFastIterable(m))
			addTo(entry.getCharKey(), entry.getFloatValue());
	}
	
	@Override
	public void putAll(Char2FloatMap m) {
		for(ObjectIterator<Char2FloatMap.Entry> iter = getFastIterator(m);iter.hasNext();) {
			Char2FloatMap.Entry entry = iter.next();
			put(entry.getCharKey(), entry.getFloatValue());
		}
	}
	
	@Override
	public void putAll(Map<? extends Character, ? extends Float> m)
	{
		if(m instanceof Char2FloatMap) putAll((Char2FloatMap)m);
		else super.putAll(m);
	}
	
	@Override
	public void putAll(char[] keys, float[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i], values[i]);
	}
	
	@Override
	public void putAll(Character[] keys, Float[] values, int offset, int size) {
		SanityChecks.checkArrayCapacity(keys.length, offset, size);
		SanityChecks.checkArrayCapacity(values.length, offset, size);
		for(int i = 0;i<size;i++) put(keys[i].charValue(), values[i].floatValue());		
	}
	
	@Override
	public void putAllIfAbsent(Char2FloatMap m) {
		for(Char2FloatMap.Entry entry : getFastIterable(m))
			putIfAbsent(entry.getCharKey(), entry.getFloatValue());
	}
	
	
	@Override
	public boolean containsKey(char key) {
		for(CharIterator iter = keySet().iterator();iter.hasNext();)
			if(iter.nextChar() == key) return true;
		return false;
	}
	
	@Override
	public boolean containsValue(float value) {
		for(FloatIterator iter = values().iterator();iter.hasNext();)
			if(Float.floatToIntBits(iter.nextFloat()) == Float.floatToIntBits(value)) return true;
		return false;
	}
	
	@Override
	public boolean replace(char key, float oldValue, float newValue) {
		float curValue = get(key);
		if (Float.floatToIntBits(curValue) != Float.floatToIntBits(oldValue) || (Float.floatToIntBits(curValue) == Float.floatToIntBits(getDefaultReturnValue()) && !containsKey(key))) {
			return false;
		}
		put(key, newValue);
		return true;
	}

	@Override
	public float replace(char key, float value) {
		float curValue;
		if (Float.floatToIntBits((curValue = get(key))) != Float.floatToIntBits(getDefaultReturnValue()) || containsKey(key)) {
			curValue = put(key, value);
		}
		return curValue;
	}
	
	@Override
	public void replaceFloats(Char2FloatMap m) {
		for(Char2FloatMap.Entry entry : getFastIterable(m))
			replace(entry.getCharKey(), entry.getFloatValue());
	}
	
	@Override
	public void replaceFloats(CharFloatUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(ObjectIterator<Char2FloatMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Char2FloatMap.Entry entry = iter.next();
			entry.setValue(mappingFunction.applyAsFloat(entry.getCharKey(), entry.getFloatValue()));
		}
	}

	@Override
	public float computeFloat(char key, CharFloatUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		float newValue = mappingFunction.applyAsFloat(key, get(key));
		put(key, newValue);
		return newValue;
	}
	
	@Override
	public float computeFloatIfAbsent(char key, Char2FloatFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(!containsKey(key)) {
			float newValue = mappingFunction.applyAsFloat(key);
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public float supplyFloatIfAbsent(char key, FloatSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		if(!containsKey(key)) {
			float newValue = valueProvider.getAsFloat();
			put(key, newValue);
			return newValue;
		}
		return get(key);
	}
	
	@Override
	public float computeFloatIfPresent(char key, CharFloatUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		if(containsKey(key)) {
			float newValue = mappingFunction.applyAsFloat(key, get(key));
			put(key, newValue);
			return newValue;
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public float computeFloatNonDefault(char key, CharFloatUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		float value = get(key);
		float newValue = mappingFunction.applyAsFloat(key, value);
		if(Float.floatToIntBits(newValue) == Float.floatToIntBits(getDefaultReturnValue())) {
			if(Float.floatToIntBits(value) != Float.floatToIntBits(getDefaultReturnValue()) || containsKey(key)) {
				remove(key);
				return getDefaultReturnValue();
			}
			return getDefaultReturnValue();
		}
		put(key, newValue);
		return newValue;
	}
	
	@Override
	public float computeFloatIfAbsentNonDefault(char key, Char2FloatFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		float value;
		if((value = get(key)) == getDefaultReturnValue() || !containsKey(key)) {
			float newValue = mappingFunction.applyAsFloat(key);
			if(Float.floatToIntBits(newValue) != Float.floatToIntBits(getDefaultReturnValue())) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public float supplyFloatIfAbsentNonDefault(char key, FloatSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		float value;
		if((value = get(key)) == getDefaultReturnValue() || !containsKey(key)) {
			float newValue = valueProvider.getAsFloat();
			if(Float.floatToIntBits(newValue) != Float.floatToIntBits(getDefaultReturnValue())) {
				put(key, newValue);
				return newValue;
			}
		}
		return value;
	}
	
	@Override
	public float computeFloatIfPresentNonDefault(char key, CharFloatUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		float value;
		if(Float.floatToIntBits((value = get(key))) != Float.floatToIntBits(getDefaultReturnValue()) || containsKey(key)) {
			float newValue = mappingFunction.applyAsFloat(key, value);
			if(Float.floatToIntBits(newValue) != Float.floatToIntBits(getDefaultReturnValue())) {
				put(key, newValue);
				return newValue;
			}
			remove(key);
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public float mergeFloat(char key, float value, FloatFloatUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		float oldValue = get(key);
		float newValue = Float.floatToIntBits(oldValue) == Float.floatToIntBits(getDefaultReturnValue()) ? value : mappingFunction.applyAsFloat(oldValue, value);
		if(Float.floatToIntBits(newValue) == Float.floatToIntBits(getDefaultReturnValue())) remove(key);
		else put(key, newValue);
		return newValue;
	}
	
	@Override
	public void mergeAllFloat(Char2FloatMap m, FloatFloatUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Char2FloatMap.Entry entry : getFastIterable(m)) {
			char key = entry.getCharKey();
			float oldValue = get(key);
			float newValue = Float.floatToIntBits(oldValue) == Float.floatToIntBits(getDefaultReturnValue()) ? entry.getFloatValue() : mappingFunction.applyAsFloat(oldValue, entry.getFloatValue());
			if(Float.floatToIntBits(newValue) == Float.floatToIntBits(getDefaultReturnValue())) remove(key);
			else put(key, newValue);
		}
	}
	
	@Override
	public Float get(Object key) {
		return Float.valueOf(key instanceof Character ? get(((Character)key).charValue()) : getDefaultReturnValue());
	}
	
	@Override
	public Float getOrDefault(Object key, Float defaultValue) {
		return Float.valueOf(key instanceof Character ? getOrDefault(((Character)key).charValue(), defaultValue.floatValue()) : getDefaultReturnValue());
	}
	
	@Override
	public float getOrDefault(char key, float defaultValue) {
		float value = get(key);
		return Float.floatToIntBits(value) != Float.floatToIntBits(getDefaultReturnValue()) || containsKey(key) ? value : defaultValue;
	}
	
	
	@Override
	public Float remove(Object key) {
		return key instanceof Character ? Float.valueOf(remove(((Character)key).charValue())) : Float.valueOf(getDefaultReturnValue());
	}
	
	@Override
	public void forEach(CharFloatConsumer action) {
		Objects.requireNonNull(action);
		for(ObjectIterator<Char2FloatMap.Entry> iter = getFastIterator(this);iter.hasNext();) {
			Char2FloatMap.Entry entry = iter.next();
			action.accept(entry.getCharKey(), entry.getFloatValue());
		}
	}

	@Override
	public CharSet keySet() {
		return new AbstractCharSet() {
			@Override
			public boolean remove(char o) {
				return Float.floatToIntBits(AbstractChar2FloatMap.this.remove(o)) != Float.floatToIntBits(getDefaultReturnValue());
			}
			
			@Override
			public boolean add(char o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public CharIterator iterator() {
				return new CharIterator() {
					ObjectIterator<Char2FloatMap.Entry> iter = getFastIterator(AbstractChar2FloatMap.this);
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
				return AbstractChar2FloatMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractChar2FloatMap.this.clear();
			}
		};
	}

	@Override
	public FloatCollection values() {
		return new AbstractFloatCollection() {
			@Override
			public boolean add(float o) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public int size() {
				return AbstractChar2FloatMap.this.size();
			}
			
			@Override
			public void clear() {
				AbstractChar2FloatMap.this.clear();
			}
			
			@Override
			public FloatIterator iterator() {
				return new FloatIterator() {
					ObjectIterator<Char2FloatMap.Entry> iter = getFastIterator(AbstractChar2FloatMap.this);
					@Override
					public boolean hasNext() {
						return iter.hasNext();
					}
					
					@Override
					public float nextFloat() {
						return iter.next().getFloatValue();
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
	public ObjectSet<Map.Entry<Character, Float>> entrySet() {
		return (ObjectSet)char2FloatEntrySet();
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(o instanceof Map) {
			if(size() != ((Map<?, ?>)o).size()) return false;
			if(o instanceof Char2FloatMap) return char2FloatEntrySet().containsAll(((Char2FloatMap)o).char2FloatEntrySet());
			return char2FloatEntrySet().containsAll(((Map<?, ?>)o).entrySet());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		ObjectIterator<Char2FloatMap.Entry> iter = getFastIterator(this);
		while(iter.hasNext()) hash += iter.next().hashCode();
		return hash;
	}
	
	public static class ReversedChar2FloatOrderedMap extends AbstractChar2FloatMap implements Char2FloatOrderedMap {
		Char2FloatOrderedMap map;
		
		public ReversedChar2FloatOrderedMap(Char2FloatOrderedMap map) {
			this.map = map;
		}
		@Override
		public AbstractChar2FloatMap setDefaultReturnValue(float v) {
			map.setDefaultReturnValue(v);
			return this;
		}
		@Override
		public float getDefaultReturnValue() { return map.getDefaultReturnValue(); }
		@Override
		public Char2FloatOrderedMap copy() { throw new UnsupportedOperationException(); }
		@Override
		public float put(char key, float value) { return map.put(key, value); }
		@Override
		public float putIfAbsent(char key, float value) { return map.putIfAbsent(key, value); }
		@Override
		public float addTo(char key, float value) { return map.addTo(key, value); }
		@Override
		public float subFrom(char key, float value) { return map.subFrom(key, value); }
		@Override
		public float remove(char key) { return map.remove(key); }
		@Override
		public boolean remove(char key, float value) { return map.remove(key, value); }
		@Override
		public float removeOrDefault(char key, float defaultValue) { return map.removeOrDefault(key, defaultValue); }
		@Override
		public boolean containsKey(char key) { return map.containsKey(key); }
		@Override
		public boolean containsValue(float value) { return map.containsValue(value); }
		@Override
		public boolean replace(char key, float oldValue, float newValue) { return map.replace(key, oldValue, newValue); }
		@Override
		public float replace(char key, float value) { return map.replace(key, value); }
		@Override
		public void replaceFloats(Char2FloatMap m) { map.replaceFloats(m); }
		@Override
		public void replaceFloats(CharFloatUnaryOperator mappingFunction) { map.replaceFloats(mappingFunction); }
		@Override
		public float computeFloat(char key, CharFloatUnaryOperator mappingFunction) { return map.computeFloat(key, mappingFunction); }
		@Override
		public float computeFloatIfAbsent(char key, Char2FloatFunction mappingFunction) { return map.computeFloatIfAbsent(key, mappingFunction); }
		@Override
		public float supplyFloatIfAbsent(char key, FloatSupplier valueProvider) { return map.supplyFloatIfAbsent(key, valueProvider); }
		@Override
		public float computeFloatIfPresent(char key, CharFloatUnaryOperator mappingFunction) { return map.computeFloatIfPresent(key, mappingFunction); }
		@Override
		public float computeFloatNonDefault(char key, CharFloatUnaryOperator mappingFunction) { return map.computeFloatNonDefault(key, mappingFunction); }
		@Override
		public float computeFloatIfAbsentNonDefault(char key, Char2FloatFunction mappingFunction) { return map.computeFloatIfAbsentNonDefault(key, mappingFunction); }
		@Override
		public float supplyFloatIfAbsentNonDefault(char key, FloatSupplier valueProvider) { return map.supplyFloatIfAbsentNonDefault(key, valueProvider); }
		@Override
		public float computeFloatIfPresentNonDefault(char key, CharFloatUnaryOperator mappingFunction) { return map.computeFloatIfPresentNonDefault(key, mappingFunction); }
		@Override
		public float mergeFloat(char key, float value, FloatFloatUnaryOperator mappingFunction) { return map.mergeFloat(key, value, mappingFunction); }
		@Override
		public float getOrDefault(char key, float defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public float get(char key) { return map.get(key); }
		@Override
		public float putAndMoveToFirst(char key, float value) { return map.putAndMoveToLast(key, value); }
		@Override
		public float putAndMoveToLast(char key, float value) { return map.putAndMoveToFirst(key, value); }
		@Override
		public float putFirst(char key, float value) { return map.putLast(key, value); }
		@Override
		public float putLast(char key, float value) { return map.putFirst(key, value); }
		@Override
		public boolean moveToFirst(char key) { return map.moveToLast(key); }
		@Override
		public boolean moveToLast(char key) { return map.moveToFirst(key); }
		@Override
		public float getAndMoveToFirst(char key) { return map.getAndMoveToLast(key); }
		@Override
		public float getAndMoveToLast(char key) { return map.getAndMoveToFirst(key); }
		@Override
		public char firstCharKey() { return map.lastCharKey(); }
		@Override
		public char pollFirstCharKey() { return map.pollLastCharKey(); }
		@Override
		public char lastCharKey() { return map.firstCharKey(); }
		@Override
		public char pollLastCharKey() { return map.pollFirstCharKey(); }
		@Override
		public float firstFloatValue() { return map.lastFloatValue(); }
		@Override
		public float lastFloatValue() { return map.firstFloatValue(); }
		@Override
		public Char2FloatMap.Entry firstEntry() { return map.lastEntry(); }
		@Override
		public Char2FloatMap.Entry lastEntry() { return map.firstEntry(); }
		@Override
		public Char2FloatMap.Entry pollFirstEntry() { return map.pollLastEntry(); }
		@Override
		public Char2FloatMap.Entry pollLastEntry() { return map.pollFirstEntry(); }
		@Override
		public ObjectOrderedSet<Char2FloatMap.Entry> char2FloatEntrySet() { return new AbstractObjectSet.ReversedObjectOrderedSet<>(map.char2FloatEntrySet()); }
		@Override
		public CharOrderedSet keySet() { return new AbstractCharSet.ReversedCharOrderedSet(map.keySet()); }
		@Override
		public FloatOrderedCollection values() { return map.values().reversed(); }
		@Override
		public Char2FloatOrderedMap reversed() { return map; }
	}

	
	/**
	 * A Simple Type Specific Entry class to reduce boxing/unboxing
	 */
	public static class BasicEntry implements Char2FloatMap.Entry {
		protected char key;
		protected float value;
		
		/**
		 * A basic Empty constructor
		 */
		public BasicEntry() {}
		/**
		 * A Boxed Constructor for supporting java variants
		 * @param key the key of a entry
		 * @param value the value of a entry
		 */
		public BasicEntry(Character key, Float value) {
			this.key = key.charValue();
			this.value = value.floatValue();
		}
		
		/**
		 * A Type Specific Constructor
		 * @param key the key of a entry
		 * @param value the value of a entry 
		 */
		public BasicEntry(char key, float value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * A Helper method for fast replacing values
		 * @param key the key that should be replaced
		 * @param value the value that should be replaced
		 */
		public void set(char key, float value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public char getCharKey() {
			return key;
		}

		@Override
		public float getFloatValue() {
			return value;
		}

		@Override
		public float setValue(float value) {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Map.Entry) {
				if(obj instanceof Char2FloatMap.Entry) {
					Char2FloatMap.Entry entry = (Char2FloatMap.Entry)obj;
					return key == entry.getCharKey() && Float.floatToIntBits(value) == Float.floatToIntBits(entry.getFloatValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Character && value instanceof Float && this.key == ((Character)key).charValue() && Float.floatToIntBits(this.value) == Float.floatToIntBits(((Float)value).floatValue());
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Character.hashCode(key) ^ Float.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Character.toString(key) + "=" + Float.toString(value);
		}
	}
}