package speiger.src.collections.objects.maps.impl.misc;

import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

import speiger.src.collections.bytes.collections.AbstractByteCollection;
import speiger.src.collections.bytes.collections.ByteCollection;
import speiger.src.collections.bytes.collections.ByteIterator;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.bytes.functions.ByteSupplier;
import speiger.src.collections.bytes.functions.function.ByteByteUnaryOperator;
import speiger.src.collections.objects.functions.consumer.ObjectByteConsumer;
import speiger.src.collections.objects.functions.function.ObjectByteUnaryOperator;
import speiger.src.collections.objects.functions.function.Object2ByteFunction;
import speiger.src.collections.objects.utils.maps.Object2ByteMaps;
import speiger.src.collections.objects.maps.abstracts.AbstractObject2ByteMap;
import speiger.src.collections.objects.maps.interfaces.Object2ByteMap;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectSet;

/**
 * A Type Specific EnumMap implementation that allows for Primitive Values.
 * Unlike javas implementation this one does not jump around between a single long or long array implementation based around the enum size
 * This will cause a bit more memory usage but allows for a simpler implementation.
 * @param <T> the type of elements maintained by this Collection
 */
public class Enum2ByteMap<T extends Enum<T>> extends AbstractObject2ByteMap<T>
{
	/** Enum Type that is being used */
	protected Class<T> keyType;
	/** The Backing keys array. */
	protected transient T[] keys;
	/** The Backing values array */
	protected transient byte[] values;
	/** The Backing array that indicates which index is present or not */
	protected transient long[] present;
	/** Amount of Elements stored in the ArrayMap */
	protected int size = 0;
	/** EntrySet cache */
	protected transient ObjectSet<Object2ByteMap.Entry<T>> entrySet;
	/** KeySet cache */
	protected transient ObjectSet<T> keySet;
	/** Values cache */
	protected transient ByteCollection valuesC;
	
	protected Enum2ByteMap() {
		
	}
	/**
	 * Default Constructor
	 * @param keyType the type of Enum that should be used
	 */
	public Enum2ByteMap(Class<T> keyType) {
		this.keyType = keyType;
		keys = getKeyUniverse(keyType);
		values = new byte[keys.length];
		present = new long[((keys.length - 1) >> 6) + 1];
	}
	
	/**
	 * Helper constructor that allow to create a EnumMap from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the EnumMap
	 * @param values the values that should be put into the EnumMap.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Enum2ByteMap(T[] keys, Byte[] values) {
		if(keys.length <= 0) throw new IllegalArgumentException("Empty Array are not allowed");
		if(keys.length != values.length) throw new IllegalArgumentException("Keys and Values have to be the same size");
		keyType = keys[0].getDeclaringClass();
		this.keys = getKeyUniverse(keyType);
		this.values = new byte[keys.length];
		present = new long[((keys.length - 1) >> 6) + 1];
		putAll(keys, values);
	}
	
	/**
	 * Helper constructor that allow to create a EnumMap from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Enum2ByteMap(T[] keys, byte[] values) {
		if(keys.length <= 0) throw new IllegalArgumentException("Empty Array are not allowed");
		if(keys.length != values.length) throw new IllegalArgumentException("Keys and Values have to be the same size");
		keyType = keys[0].getDeclaringClass();
		this.keys = getKeyUniverse(keyType);
		this.values = new byte[keys.length];
		present = new long[((keys.length - 1) >> 6) + 1];
		putAll(keys, values);		
	}
	
	/**
	 * A Helper constructor that allows to create a EnumMap with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 */
	public Enum2ByteMap(Map<? extends T, ? extends Byte> map) {
		if(map instanceof Enum2ByteMap) {
			Enum2ByteMap<T> enumMap = (Enum2ByteMap<T>)map;
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
			this.values = new byte[keys.length];
			present = new long[((keys.length - 1) >> 6) + 1];
			putAll(map);
		}
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new EnumMap with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 */
	public Enum2ByteMap(Object2ByteMap<T> map) {
		if(map instanceof Enum2ByteMap) {
			Enum2ByteMap<T> enumMap = (Enum2ByteMap<T>)map;
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
			this.values = new byte[keys.length];
			present = new long[((keys.length - 1) >> 6) + 1];
			putAll(map);
		}
	}
	
	@Override
	public byte put(T key, byte value) {
		int index = key.ordinal();
		if(isSet(index)) {
			byte result = values[index];
			values[index] = value;
			return result;
		}
		set(index);
		values[index] = value;
		return getDefaultReturnValue();
	}
	
	@Override
	public byte putIfAbsent(T key, byte value) {
		int index = key.ordinal();
		if(isSet(index)) return values[index];
		set(index);
		values[index] = value;
		return getDefaultReturnValue();
	}
	
	@Override
	public byte addTo(T key, byte value) {
		int index = key.ordinal();
		if(isSet(index)) {
			byte result = values[index];
			values[index] += value;
			return result;
		}
		set(index);
		values[index] = value;
		return getDefaultReturnValue();
	}
	
	@Override
	public byte subFrom(T key, byte value) {
		int index = key.ordinal();
		if(!isSet(index)) return getDefaultReturnValue();
		byte oldValue = values[index];
		values[index] -= value;
		if(value < 0 ? (values[index] >= getDefaultReturnValue()) : (values[index] <= getDefaultReturnValue())) {
			clear(index);
			values[index] = (byte)0;
		}
		return oldValue;
	}
	@Override
	public boolean containsKey(Object key) {
		if(!keyType.isInstance(key)) return false;
		return isSet(((T)key).ordinal());
	}
	
	@Override
	public boolean containsValue(byte value) {
		for(int i = 0;i<values.length;i++)
			if(isSet(i) && value == values[i]) return true;
		return false;
	}
	
	@Override
	public Byte remove(Object key) {
		if(!keyType.isInstance(key)) return getDefaultReturnValue();
		int index = ((T)key).ordinal();
		if(!isSet(index)) return getDefaultReturnValue();
		clear(index);
		byte result = values[index];
		values[index] = (byte)0;
		return result;
	}
	
	@Override
	public byte rem(T key) {
		if(!keyType.isInstance(key)) return getDefaultReturnValue();
		int index = key.ordinal();
		if(!isSet(index)) return getDefaultReturnValue();
		clear(index);
		byte result = values[index];
		values[index] = (byte)0;
		return result;
	}

	@Override
	public byte remOrDefault(T key, byte defaultValue) {
		int index = key.ordinal();
		if(!isSet(index)) return defaultValue;
		clear(index);
		byte result = values[index];
		values[index] = (byte)0;
		return result;
	}
	
	@Override
	public boolean remove(T key, byte value) {
		int index = key.ordinal();
		if(!isSet(index) || value != values[index]) return false;
		clear(index);
		values[index] = (byte)0;
		return true;
	}
	
	@Override
	public byte getByte(T key) {
		if(!keyType.isInstance(key)) return getDefaultReturnValue();
		int index = key.ordinal();
		return isSet(index) ? values[index] : getDefaultReturnValue();
	}

	@Override
	public byte getOrDefault(T key, byte defaultValue) {
		if(!keyType.isInstance(key)) return defaultValue;
		int index = key.ordinal();
		return isSet(index) ? values[index] : defaultValue;
	}
	
	@Override
	public Enum2ByteMap<T> copy() {
		Enum2ByteMap<T> map = new Enum2ByteMap<>(keyType);
		map.size = size;
		System.arraycopy(present, 0, map.present, 0, Math.min(present.length, map.present.length));
		System.arraycopy(values, 0, map.values, 0, Math.min(values.length, map.values.length));
		return map;
	}
	
	@Override
	public ObjectSet<Object2ByteMap.Entry<T>> object2ByteEntrySet() {
		if(entrySet == null) entrySet = new EntrySet();
		return entrySet;
	}
	
	@Override
	public ObjectSet<T> keySet() {
		if(keySet == null) keySet = new KeySet();
		return keySet;
	}
	
	@Override
	public ByteCollection values() {
		if(valuesC == null) valuesC = new Values();
		return valuesC;
	}
	
	@Override
	public void forEach(ObjectByteConsumer<T> action) {
		if(size() <= 0) return;
		for(int i = 0,m=keys.length;i<m;i++) {
			if(isSet(i)) action.accept(keys[i], values[i]);
		}
	}
	
	@Override
	public boolean replace(T key, byte oldValue, byte newValue) {
		int index = key.ordinal();
		if(!isSet(index) || values[index] != oldValue) return false;
		values[index] = newValue;
		return true;
	}
	
	@Override
	public byte replace(T key, byte value) {
		int index = key.ordinal();
		if(!isSet(index)) return getDefaultReturnValue();
		byte oldValue = values[index];
		values[index] = value;
		return oldValue;
	}
	
	@Override
	public byte computeByte(T key, ObjectByteUnaryOperator<T> mappingFunction) {
		int index = key.ordinal();
		if(!isSet(index)) {
			byte newValue = mappingFunction.applyAsByte(key, getDefaultReturnValue());
			if(newValue == getDefaultReturnValue()) return newValue;
			set(index);
			values[index] = newValue;
			return newValue;
		}
		byte newValue = mappingFunction.applyAsByte(key, values[index]);
		if(newValue == getDefaultReturnValue()) {
			clear(index);
			values[index] = (byte)0;
			return newValue;
		}
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public byte computeByteIfAbsent(T key, Object2ByteFunction<T> mappingFunction) {
		int index = key.ordinal();
		if(!isSet(index)) {
			byte newValue = mappingFunction.getByte(key);
			if(newValue == getDefaultReturnValue()) return newValue;
			set(index);
			values[index] = newValue;			
			return newValue;
		}
		byte newValue = values[index];
		if(newValue == getDefaultReturnValue()) {
			newValue = mappingFunction.getByte(key);
			if(newValue == getDefaultReturnValue()) return newValue;
			values[index] = newValue;
		}
		return newValue;
	}
	
	@Override
	public byte supplyByteIfAbsent(T key, ByteSupplier valueProvider) {
		int index = key.ordinal();
		if(!isSet(index)) {
			byte newValue = valueProvider.getByte();
			if(newValue == getDefaultReturnValue()) return newValue;
			set(index);
			values[index] = newValue;			
			return newValue;
		}
		byte newValue = values[index];
		if(newValue == getDefaultReturnValue()) {
			newValue = valueProvider.getByte();
			if(newValue == getDefaultReturnValue()) return newValue;
			values[index] = newValue;
		}
		return newValue;
	}
	
	@Override
	public byte computeByteIfPresent(T key, ObjectByteUnaryOperator<T> mappingFunction) {
		int index = key.ordinal();
		if(!isSet(index) || values[index] == getDefaultReturnValue()) return getDefaultReturnValue();
		byte newValue = mappingFunction.applyAsByte(key, values[index]);
		if(newValue == getDefaultReturnValue()) {
			clear(index);
			values[index] = (byte)0;
			return newValue;
		}
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public byte mergeByte(T key, byte value, ByteByteUnaryOperator mappingFunction) {
		int index = key.ordinal();
		byte newValue = !isSet(index) || values[index] == getDefaultReturnValue() ? value : mappingFunction.applyAsByte(values[index], value);
		if(newValue == getDefaultReturnValue()) {
			if(isSet(index)) {
				clear(index);
				values[index] = (byte)0;
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
	public void mergeAllByte(Object2ByteMap<T> m, ByteByteUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Object2ByteMap.Entry<T> entry : Object2ByteMaps.fastIterable(m)) {
			T key = entry.getKey();
			int index = key.ordinal();
			byte newValue = !isSet(index) || values[index] == getDefaultReturnValue() ? entry.getByteValue() : mappingFunction.applyAsByte(values[index], entry.getByteValue());
			if(newValue == getDefaultReturnValue()) {
				if(isSet(index)) {
					clear(index);
					values[index] = (byte)0;
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
		Arrays.fill(values, (byte)0);
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
	
	class EntrySet extends AbstractObjectSet<Object2ByteMap.Entry<T>> {
		
		@Override
		public boolean contains(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Object2ByteMap.Entry) {
					Object2ByteMap.Entry<T> entry = (Object2ByteMap.Entry<T>)o;
					if(!keyType.isInstance(entry.getKey())) return false;
					int index = ((T)entry.getKey()).ordinal();
					if(index >= 0 && Enum2ByteMap.this.isSet(index)) return entry.getByteValue() == Enum2ByteMap.this.values[index];
				}
				else {
					Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
					if(!keyType.isInstance(entry.getKey())) return false;
					int index = ((T)entry.getKey()).ordinal();
					if(index >= 0 && Enum2ByteMap.this.isSet(index)) return Objects.equals(entry.getValue(), Byte.valueOf(Enum2ByteMap.this.values[index]));
				}
			}
			return false;
		}
		
		@Override
		public boolean remove(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Object2ByteMap.Entry) {
					Object2ByteMap.Entry<T> entry = (Object2ByteMap.Entry<T>)o;
					return Enum2ByteMap.this.remove(entry.getKey(), entry.getByteValue());
				}
				Map.Entry<?, ?> entry = (java.util.Map.Entry<?, ?>)o;
				return Enum2ByteMap.this.remove(entry.getKey(), entry.getValue());
			}
			return false;
		}
		
		@Override
		public ObjectIterator<Object2ByteMap.Entry<T>> iterator() {
			return new EntryIterator();
		}

		@Override
		public int size() {
			return Enum2ByteMap.this.size();
		}
		
		@Override
		public void clear() {
			Enum2ByteMap.this.clear();
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
			Enum2ByteMap.this.remove(o);
			return size != size();
		}
		
		@Override
		public ObjectIterator<T> iterator() {
			return new KeyIterator();
		}
		
		@Override
		public int size() {
			return Enum2ByteMap.this.size();
		}
		
		@Override
		public void clear() {
			Enum2ByteMap.this.clear();
		}
	}
	
	class Values extends AbstractByteCollection {

		@Override
		public boolean add(byte o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean contains(Object e) { return containsValue(e); }
		
		@Override
		public ByteIterator iterator() {
			return new ValueIterator();
		}

		@Override
		public int size() {
			return Enum2ByteMap.this.size();
		}
		
		@Override
		public void clear() {
			Enum2ByteMap.this.clear();
		}
	}
	
	class EntryIterator extends MapIterator implements ObjectIterator<Object2ByteMap.Entry<T>> {
		@Override
		public Object2ByteMap.Entry<T> next() {
			return new MapEntry(nextEntry());
		}
	}
	
	class KeyIterator extends MapIterator implements ObjectIterator<T> {
		@Override
		public T next() {
			return keys[nextEntry()];
		}
	}
	
	class ValueIterator extends MapIterator implements ByteIterator {
		@Override
		public byte nextByte() {
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
			values[lastReturnValue] = (byte)0;
			lastReturnValue = -1;
		}
	}
	
	protected class MapEntry implements Object2ByteMap.Entry<T>, Map.Entry<T, Byte> {
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
		public byte getByteValue() {
			return values[index];
		}

		@Override
		public byte setValue(byte value) {
			byte oldValue = values[index];
			values[index] = value;
			return oldValue;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Map.Entry) {
				if(obj instanceof Object2ByteMap.Entry) {
					Object2ByteMap.Entry<T> entry = (Object2ByteMap.Entry<T>)obj;
					return Objects.equals(keys[index], entry.getKey()) && values[index] == entry.getByteValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return value instanceof Byte && Objects.equals(keys[index], key) && values[index] == ((Byte)value).byteValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Objects.hashCode(keys[index]) ^ Byte.hashCode(values[index]);
		}
		
		@Override
		public String toString() {
			return Objects.toString(keys[index]) + "=" + Byte.toString(values[index]);
		}
	}
}