package speiger.src.collections.objects.maps.impl.misc;

import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

import speiger.src.collections.floats.collections.AbstractFloatCollection;
import speiger.src.collections.floats.collections.FloatCollection;
import speiger.src.collections.floats.collections.FloatIterator;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.floats.functions.FloatSupplier;
import speiger.src.collections.floats.functions.function.FloatFloatUnaryOperator;
import speiger.src.collections.objects.functions.consumer.ObjectFloatConsumer;
import speiger.src.collections.objects.functions.function.ObjectFloatUnaryOperator;
import speiger.src.collections.objects.functions.function.Object2FloatFunction;
import speiger.src.collections.objects.utils.maps.Object2FloatMaps;
import speiger.src.collections.objects.maps.abstracts.AbstractObject2FloatMap;
import speiger.src.collections.objects.maps.interfaces.Object2FloatMap;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectSet;

/**
 * A Type Specific EnumMap implementation that allows for Primitive Values.
 * Unlike javas implementation this one does not jump around between a single long or long array implementation based around the enum size
 * This will cause a bit more memory usage but allows for a simpler implementation.
 * @param <T> the type of elements maintained by this Collection
 */
public class Enum2FloatMap<T extends Enum<T>> extends AbstractObject2FloatMap<T>
{
	/** Enum Type that is being used */
	protected Class<T> keyType;
	/** The Backing keys array. */
	protected transient T[] keys;
	/** The Backing values array */
	protected transient float[] values;
	/** The Backing array that indicates which index is present or not */
	protected transient long[] present;
	/** Amount of Elements stored in the ArrayMap */
	protected int size = 0;
	/** EntrySet cache */
	protected transient ObjectSet<Object2FloatMap.Entry<T>> entrySet;
	/** KeySet cache */
	protected transient ObjectSet<T> keySet;
	/** Values cache */
	protected transient FloatCollection valuesC;
	
	protected Enum2FloatMap() {
		
	}
	/**
	 * Default Constructor
	 * @param keyType the type of Enum that should be used
	 */
	public Enum2FloatMap(Class<T> keyType) {
		this.keyType = keyType;
		keys = getKeyUniverse(keyType);
		values = new float[keys.length];
		present = new long[((keys.length - 1) >> 6) + 1];
	}
	
	/**
	 * Helper constructor that allow to create a EnumMap from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the EnumMap
	 * @param values the values that should be put into the EnumMap.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Enum2FloatMap(T[] keys, Float[] values) {
		if(keys.length <= 0) throw new IllegalArgumentException("Empty Array are not allowed");
		if(keys.length != values.length) throw new IllegalArgumentException("Keys and Values have to be the same size");
		keyType = keys[0].getDeclaringClass();
		this.keys = getKeyUniverse(keyType);
		this.values = new float[keys.length];
		present = new long[((keys.length - 1) >> 6) + 1];
		putAll(keys, values);
	}
	
	/**
	 * Helper constructor that allow to create a EnumMap from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Enum2FloatMap(T[] keys, float[] values) {
		if(keys.length <= 0) throw new IllegalArgumentException("Empty Array are not allowed");
		if(keys.length != values.length) throw new IllegalArgumentException("Keys and Values have to be the same size");
		keyType = keys[0].getDeclaringClass();
		this.keys = getKeyUniverse(keyType);
		this.values = new float[keys.length];
		present = new long[((keys.length - 1) >> 6) + 1];
		putAll(keys, values);		
	}
	
	/**
	 * A Helper constructor that allows to create a EnumMap with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 */
	public Enum2FloatMap(Map<? extends T, ? extends Float> map) {
		if(map instanceof Enum2FloatMap) {
			Enum2FloatMap<T> enumMap = (Enum2FloatMap<T>)map;
			keyType = enumMap.keyType;
			keys = enumMap.keys;
			values = enumMap.values.clone();
			present = enumMap.present.clone();
			size = enumMap.size;
		}
		else if(map.isEmpty()) throw new IllegalArgumentException("Empty Maps are not allowed");
		else {
			keyType = map.keySet().iterator().next().getDeclaringClass();
			this.keys = getKeyUniverse(keyType);
			this.values = new float[keys.length];
			present = new long[((keys.length - 1) >> 6) + 1];
			putAll(map);
		}
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new EnumMap with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 */
	public Enum2FloatMap(Object2FloatMap<T> map) {
		if(map instanceof Enum2FloatMap) {
			Enum2FloatMap<T> enumMap = (Enum2FloatMap<T>)map;
			keyType = enumMap.keyType;
			keys = enumMap.keys;
			values = enumMap.values.clone();
			present = enumMap.present.clone();
			size = enumMap.size;
		}
		else if(map.isEmpty()) throw new IllegalArgumentException("Empty Maps are not allowed");
		else {
			keyType = map.keySet().iterator().next().getDeclaringClass();
			this.keys = getKeyUniverse(keyType);
			this.values = new float[keys.length];
			present = new long[((keys.length - 1) >> 6) + 1];
			putAll(map);
		}
	}
	
	@Override
	public float put(T key, float value) {
		int index = key.ordinal();
		if(isSet(index)) {
			float result = values[index];
			values[index] = value;
			return result;
		}
		set(index);
		values[index] = value;
		return getDefaultReturnValue();
	}
	
	@Override
	public float putIfAbsent(T key, float value) {
		int index = key.ordinal();
		if(isSet(index)) return values[index];
		set(index);
		values[index] = value;
		return getDefaultReturnValue();
	}
	
	@Override
	public float addTo(T key, float value) {
		int index = key.ordinal();
		if(isSet(index)) {
			float result = values[index];
			values[index] += value;
			return result;
		}
		set(index);
		values[index] = value;
		return getDefaultReturnValue();
	}
	
	@Override
	public float subFrom(T key, float value) {
		int index = key.ordinal();
		if(!isSet(index)) return getDefaultReturnValue();
		float oldValue = values[index];
		values[index] -= value;
		if(value < 0 ? (values[index] >= getDefaultReturnValue()) : (values[index] <= getDefaultReturnValue())) {
			clear(index);
			values[index] = 0F;
		}
		return oldValue;
	}
	@Override
	public boolean containsKey(Object key) {
		if(!keyType.isInstance(key)) return false;
		return isSet(((T)key).ordinal());
	}
	
	@Override
	public boolean containsValue(float value) {
		for(int i = 0;i<values.length;i++)
			if(isSet(i) && Float.floatToIntBits(value) == Float.floatToIntBits(values[i])) return true;
		return false;
	}
	
	@Override
	public Float remove(Object key) {
		if(!keyType.isInstance(key)) return getDefaultReturnValue();
		int index = ((T)key).ordinal();
		if(!isSet(index)) return getDefaultReturnValue();
		clear(index);
		float result = values[index];
		values[index] = 0F;
		return result;
	}
	
	@Override
	public float rem(T key) {
		if(!keyType.isInstance(key)) return getDefaultReturnValue();
		int index = key.ordinal();
		if(!isSet(index)) return getDefaultReturnValue();
		clear(index);
		float result = values[index];
		values[index] = 0F;
		return result;
	}

	@Override
	public float remOrDefault(T key, float defaultValue) {
		int index = key.ordinal();
		if(!isSet(index)) return defaultValue;
		clear(index);
		float result = values[index];
		values[index] = 0F;
		return result;
	}
	
	@Override
	public boolean remove(T key, float value) {
		int index = key.ordinal();
		if(!isSet(index) || Float.floatToIntBits(value) != Float.floatToIntBits(values[index])) return false;
		clear(index);
		values[index] = 0F;
		return true;
	}
	
	@Override
	public float getFloat(T key) {
		if(!keyType.isInstance(key)) return getDefaultReturnValue();
		int index = key.ordinal();
		return isSet(index) ? values[index] : getDefaultReturnValue();
	}

	@Override
	public float getOrDefault(T key, float defaultValue) {
		if(!keyType.isInstance(key)) return defaultValue;
		int index = key.ordinal();
		return isSet(index) ? values[index] : defaultValue;
	}
	
	@Override
	public Enum2FloatMap<T> copy() {
		Enum2FloatMap<T> map = new Enum2FloatMap<>(keyType);
		map.size = size;
		System.arraycopy(present, 0, map.present, 0, Math.min(present.length, map.present.length));
		System.arraycopy(values, 0, map.values, 0, Math.min(values.length, map.values.length));
		return map;
	}
	
	@Override
	public ObjectSet<Object2FloatMap.Entry<T>> object2FloatEntrySet() {
		if(entrySet == null) entrySet = new EntrySet();
		return entrySet;
	}
	
	@Override
	public ObjectSet<T> keySet() {
		if(keySet == null) keySet = new KeySet();
		return keySet;
	}
	
	@Override
	public FloatCollection values() {
		if(valuesC == null) valuesC = new Values();
		return valuesC;
	}
	
	@Override
	public void forEach(ObjectFloatConsumer<T> action) {
		if(size() <= 0) return;
		for(int i = 0,m=keys.length;i<m;i++) {
			if(isSet(i)) action.accept(keys[i], values[i]);
		}
	}
	
	@Override
	public boolean replace(T key, float oldValue, float newValue) {
		int index = key.ordinal();
		if(!isSet(index) || values[index] != oldValue) return false;
		values[index] = newValue;
		return true;
	}
	
	@Override
	public float replace(T key, float value) {
		int index = key.ordinal();
		if(!isSet(index)) return getDefaultReturnValue();
		float oldValue = values[index];
		values[index] = value;
		return oldValue;
	}
	
	@Override
	public float computeFloat(T key, ObjectFloatUnaryOperator<T> mappingFunction) {
		int index = key.ordinal();
		if(!isSet(index)) {
			float newValue = mappingFunction.applyAsFloat(key, getDefaultReturnValue());
			if(Float.floatToIntBits(newValue) == Float.floatToIntBits(getDefaultReturnValue())) return newValue;
			set(index);
			values[index] = newValue;
			return newValue;
		}
		float newValue = mappingFunction.applyAsFloat(key, values[index]);
		if(Float.floatToIntBits(newValue) == Float.floatToIntBits(getDefaultReturnValue())) {
			clear(index);
			values[index] = 0F;
			return newValue;
		}
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public float computeFloatIfAbsent(T key, Object2FloatFunction<T> mappingFunction) {
		int index = key.ordinal();
		if(!isSet(index)) {
			float newValue = mappingFunction.getFloat(key);
			if(Float.floatToIntBits(newValue) == Float.floatToIntBits(getDefaultReturnValue())) return newValue;
			set(index);
			values[index] = newValue;			
			return newValue;
		}
		float newValue = values[index];
		if(Float.floatToIntBits(newValue) == Float.floatToIntBits(getDefaultReturnValue())) {
			newValue = mappingFunction.getFloat(key);
			if(Float.floatToIntBits(newValue) == Float.floatToIntBits(getDefaultReturnValue())) return newValue;
			values[index] = newValue;
		}
		return newValue;
	}
	
	@Override
	public float supplyFloatIfAbsent(T key, FloatSupplier valueProvider) {
		int index = key.ordinal();
		if(!isSet(index)) {
			float newValue = valueProvider.getFloat();
			if(Float.floatToIntBits(newValue) == Float.floatToIntBits(getDefaultReturnValue())) return newValue;
			set(index);
			values[index] = newValue;			
			return newValue;
		}
		float newValue = values[index];
		if(Float.floatToIntBits(newValue) == Float.floatToIntBits(getDefaultReturnValue())) {
			newValue = valueProvider.getFloat();
			if(Float.floatToIntBits(newValue) == Float.floatToIntBits(getDefaultReturnValue())) return newValue;
			values[index] = newValue;
		}
		return newValue;
	}
	
	@Override
	public float computeFloatIfPresent(T key, ObjectFloatUnaryOperator<T> mappingFunction) {
		int index = key.ordinal();
		if(!isSet(index) || Float.floatToIntBits(values[index]) == Float.floatToIntBits(getDefaultReturnValue())) return getDefaultReturnValue();
		float newValue = mappingFunction.applyAsFloat(key, values[index]);
		if(Float.floatToIntBits(newValue) == Float.floatToIntBits(getDefaultReturnValue())) {
			clear(index);
			values[index] = 0F;
			return newValue;
		}
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public float mergeFloat(T key, float value, FloatFloatUnaryOperator mappingFunction) {
		int index = key.ordinal();
		float newValue = !isSet(index) || Float.floatToIntBits(values[index]) == Float.floatToIntBits(getDefaultReturnValue()) ? value : mappingFunction.applyAsFloat(values[index], value);
		if(Float.floatToIntBits(newValue) == Float.floatToIntBits(getDefaultReturnValue())) {
			if(isSet(index)) {
				clear(index);
				values[index] = 0F;
			}
		}
		else if(!isSet(index)) {
			set(index);
			values[index] = newValue;
		}
		else values[index] = newValue;
		return newValue;
	}
	
	@Override
	public void mergeAllFloat(Object2FloatMap<T> m, FloatFloatUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Object2FloatMap.Entry<T> entry : Object2FloatMaps.fastIterable(m)) {
			T key = entry.getKey();
			int index = key.ordinal();
			float newValue = !isSet(index) || Float.floatToIntBits(values[index]) == Float.floatToIntBits(getDefaultReturnValue()) ? entry.getFloatValue() : mappingFunction.applyAsFloat(values[index], entry.getFloatValue());
			if(Float.floatToIntBits(newValue) == Float.floatToIntBits(getDefaultReturnValue())) {
				if(isSet(index)) {
					clear(index);
					values[index] = 0F;
				}
			}
			else if(!isSet(index)) {
				set(index);
				values[index] = newValue;
			}
			else values[index] = newValue;
		}
	}
	
	@Override
	public void clear() {
		if(size == 0) return;
		size = 0;
		Arrays.fill(present, 0L);
		Arrays.fill(values, 0F);
	}
	
	@Override
	public int size() {
		return size;
	}
	
	protected void onNodeAdded(int index) {
		
	}
	
	protected void onNodeRemoved(int index)  {
		
	}
	
	protected void set(int index) {
		onNodeAdded(index);
		present[index >> 6] |= (1L << index); 
		size++;
	}
	protected void clear(int index) { 
		size--;
		present[index >> 6] &= ~(1L << index);
		onNodeRemoved(index);
	}
	protected boolean isSet(int index) { return (present[index >> 6] & (1L << index)) != 0; }
	
	protected static <K extends Enum<K>> K[] getKeyUniverse(Class<K> keyType) {
		return keyType.getEnumConstants();
	}
	
	class EntrySet extends AbstractObjectSet<Object2FloatMap.Entry<T>> {
		
		@Override
		public boolean contains(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Object2FloatMap.Entry) {
					Object2FloatMap.Entry<T> entry = (Object2FloatMap.Entry<T>)o;
					if(!keyType.isInstance(entry.getKey())) return false;
					int index = ((T)entry.getKey()).ordinal();
					if(index >= 0 && Enum2FloatMap.this.isSet(index)) return Float.floatToIntBits(entry.getFloatValue()) == Float.floatToIntBits(Enum2FloatMap.this.values[index]);
				}
				else {
					Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
					if(!keyType.isInstance(entry.getKey())) return false;
					int index = ((T)entry.getKey()).ordinal();
					if(index >= 0 && Enum2FloatMap.this.isSet(index)) return Objects.equals(entry.getValue(), Float.valueOf(Enum2FloatMap.this.values[index]));
				}
			}
			return false;
		}
		
		@Override
		public boolean remove(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Object2FloatMap.Entry) {
					Object2FloatMap.Entry<T> entry = (Object2FloatMap.Entry<T>)o;
					return Enum2FloatMap.this.remove(entry.getKey(), entry.getFloatValue());
				}
				Map.Entry<?, ?> entry = (java.util.Map.Entry<?, ?>)o;
				return Enum2FloatMap.this.remove(entry.getKey(), entry.getValue());
			}
			return false;
		}
		
		@Override
		public ObjectIterator<Object2FloatMap.Entry<T>> iterator() {
			return new EntryIterator();
		}

		@Override
		public int size() {
			return Enum2FloatMap.this.size();
		}
		
		@Override
		public void clear() {
			Enum2FloatMap.this.clear();
		}
	}
	
	class KeySet extends AbstractObjectSet<T> {
		
		@Override
		public boolean contains(Object o) {
			return containsKey(o);
		}
		
		@Override
		public boolean remove(Object o) {
			int size = size();
			Enum2FloatMap.this.remove(o);
			return size != size();
		}
		
		@Override
		public ObjectIterator<T> iterator() {
			return new KeyIterator();
		}
		
		@Override
		public int size() {
			return Enum2FloatMap.this.size();
		}
		
		@Override
		public void clear() {
			Enum2FloatMap.this.clear();
		}
	}
	
	class Values extends AbstractFloatCollection {

		@Override
		public boolean add(float o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean contains(Object e) { return containsValue(e); }
		
		@Override
		public FloatIterator iterator() {
			return new ValueIterator();
		}

		@Override
		public int size() {
			return Enum2FloatMap.this.size();
		}
		
		@Override
		public void clear() {
			Enum2FloatMap.this.clear();
		}
	}
	
	class EntryIterator extends MapIterator implements ObjectIterator<Object2FloatMap.Entry<T>> {
		@Override
		public Object2FloatMap.Entry<T> next() {
			return new MapEntry(nextEntry());
		}
	}
	
	class KeyIterator extends MapIterator implements ObjectIterator<T> {
		@Override
		public T next() {
			return keys[nextEntry()];
		}
	}
	
	class ValueIterator extends MapIterator implements FloatIterator {
		@Override
		public float nextFloat() {
			return values[nextEntry()];
		}
	}
	
	class MapIterator {
		int index;
		int lastReturnValue = -1;
		int nextIndex = -1;
		
		public boolean hasNext() {
			if(nextIndex == -1 && index < values.length) {
				while(index < values.length && !isSet(index++));
				nextIndex = index-1;
				if(!isSet(nextIndex)) nextIndex = -1;
			}
			return nextIndex != -1;
		}
		
		public int nextEntry() {
			if(!hasNext()) throw new NoSuchElementException();
			lastReturnValue = nextIndex;
			nextIndex = -1;
			return lastReturnValue;
		}
		
		public void remove() {
			if(lastReturnValue == -1) throw new IllegalStateException();
			clear(lastReturnValue);
			values[lastReturnValue] = 0F;
			lastReturnValue = -1;
		}
	}
	
	protected class MapEntry implements Object2FloatMap.Entry<T>, Map.Entry<T, Float> {
		public int index = -1;
		
		public MapEntry() {}
		public MapEntry(int index) {
			this.index = index;
		}

		@Override
		public T getKey() {
			return keys[index];
		}

		@Override
		public float getFloatValue() {
			return values[index];
		}

		@Override
		public float setValue(float value) {
			float oldValue = values[index];
			values[index] = value;
			return oldValue;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Map.Entry) {
				if(obj instanceof Object2FloatMap.Entry) {
					Object2FloatMap.Entry<T> entry = (Object2FloatMap.Entry<T>)obj;
					return Objects.equals(keys[index], entry.getKey()) && Float.floatToIntBits(values[index]) == Float.floatToIntBits(entry.getFloatValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return value instanceof Float && Objects.equals(keys[index], key) && Float.floatToIntBits(values[index]) == Float.floatToIntBits(((Float)value).floatValue());
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Objects.hashCode(keys[index]) ^ Float.hashCode(values[index]);
		}
		
		@Override
		public String toString() {
			return Objects.toString(keys[index]) + "=" + Float.toString(values[index]);
		}
	}
}