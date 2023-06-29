package speiger.src.collections.objects.maps.impl.misc;

import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;


import speiger.src.collections.shorts.collections.AbstractShortCollection;
import speiger.src.collections.shorts.collections.ShortCollection;
import speiger.src.collections.shorts.collections.ShortIterator;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.shorts.functions.ShortSupplier;
import speiger.src.collections.shorts.functions.function.ShortShortUnaryOperator;
import speiger.src.collections.objects.functions.consumer.ObjectShortConsumer;
import speiger.src.collections.objects.functions.function.ObjectShortUnaryOperator;
import speiger.src.collections.objects.functions.function.ToShortFunction;
import speiger.src.collections.objects.maps.abstracts.AbstractObject2ShortMap;
import speiger.src.collections.objects.maps.interfaces.Object2ShortMap;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectSet;

/**
 * A Type Specific EnumMap implementation that allows for Primitive Values.
 * Unlike javas implementation this one does not jump around between a single long or long array implementation based around the enum size
 * This will cause a bit more memory usage but allows for a simpler implementation.
 * @param <T> the keyType of elements maintained by this Collection
 */
public class Enum2ShortMap<T extends Enum<T>> extends AbstractObject2ShortMap<T>
{
	/** Enum Type that is being used */
	protected Class<T> keyType;
	/** The Backing keys array. */
	protected transient T[] keys;
	/** The Backing values array */
	protected transient short[] values;
	/** The Backing array that indicates which index is present or not */
	protected transient long[] present;
	/** Amount of Elements stored in the ArrayMap */
	protected int size = 0;
	/** EntrySet cache */
	protected transient ObjectSet<Object2ShortMap.Entry<T>> entrySet;
	/** KeySet cache */
	protected transient ObjectSet<T> keySet;
	/** Values cache */
	protected transient ShortCollection valuesC;
	
	protected Enum2ShortMap() {
		
	}
	/**
	 * Default Constructor
	 * @param keyType the type of Enum that should be used
	 */
	public Enum2ShortMap(Class<T> keyType) {
		this.keyType = keyType;
		keys = getKeyUniverse(keyType);
		values = new short[keys.length];
		present = new long[((keys.length - 1) >> 6) + 1];
	}
	
	/**
	 * Helper constructor that allow to create a EnumMap from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the EnumMap
	 * @param values the values that should be put into the EnumMap.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Enum2ShortMap(T[] keys, Short[] values) {
		if(keys.length <= 0) throw new IllegalArgumentException("Empty Array are not allowed");
		if(keys.length != values.length) throw new IllegalArgumentException("Keys and Values have to be the same size");
		keyType = keys[0].getDeclaringClass();
		this.keys = getKeyUniverse(keyType);
		this.values = new short[this.keys.length];
		present = new long[((this.keys.length - 1) >> 6) + 1];
		putAll(keys, values);
	}
	
	/**
	 * Helper constructor that allow to create a EnumMap from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Enum2ShortMap(T[] keys, short[] values) {
		if(keys.length <= 0) throw new IllegalArgumentException("Empty Array are not allowed");
		if(keys.length != values.length) throw new IllegalArgumentException("Keys and Values have to be the same size");
		keyType = keys[0].getDeclaringClass();
		this.keys = getKeyUniverse(keyType);
		this.values = new short[this.keys.length];
		present = new long[((this.keys.length - 1) >> 6) + 1];
		putAll(keys, values);		
	}
	
	/**
	 * A Helper constructor that allows to create a EnumMap with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 */
	public Enum2ShortMap(Map<? extends T, ? extends Short> map) {
		if(map instanceof Enum2ShortMap) {
			Enum2ShortMap<T> enumMap = (Enum2ShortMap<T>)map;
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
			this.values = new short[keys.length];
			present = new long[((keys.length - 1) >> 6) + 1];
			putAll(map);
		}
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new EnumMap with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 */
	public Enum2ShortMap(Object2ShortMap<T> map) {
		if(map instanceof Enum2ShortMap) {
			Enum2ShortMap<T> enumMap = (Enum2ShortMap<T>)map;
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
			this.values = new short[keys.length];
			present = new long[((keys.length - 1) >> 6) + 1];
			putAll(map);
		}
	}
	
	@Override
	public short put(T key, short value) {
		int index = key.ordinal();
		if(isSet(index)) {
			short result = values[index];
			values[index] = value;
			return result;
		}
		set(index);
		values[index] = value;
		return getDefaultReturnValue();
	}
	
	@Override
	public short putIfAbsent(T key, short value) {
		int index = key.ordinal();
		if(isSet(index)) {
			if(values[index] == getDefaultReturnValue()) {
				short oldValue = values[index];
				values[index] = value;
				return oldValue;
			}
			return values[index];
		}
		set(index);
		values[index] = value;
		return getDefaultReturnValue();
	}
	
	@Override
	public short addTo(T key, short value) {
		int index = key.ordinal();
		if(isSet(index)) {
			short result = values[index];
			values[index] += value;
			return result;
		}
		set(index);
		values[index] = value;
		return getDefaultReturnValue();
	}
	
	@Override
	public short subFrom(T key, short value) {
		int index = key.ordinal();
		if(!isSet(index)) return getDefaultReturnValue();
		short oldValue = values[index];
		values[index] -= value;
		if(value < 0 ? (values[index] >= getDefaultReturnValue()) : (values[index] <= getDefaultReturnValue())) {
			clear(index);
			values[index] = (short)0;
		}
		return oldValue;
	}
	@Override
	public boolean containsKey(Object key) {
		if(!keyType.isInstance(key)) return false;
		return isSet(((T)key).ordinal());
	}
	
	@Override
	public boolean containsValue(short value) {
		for(int i = 0;i<values.length;i++)
			if(isSet(i) && value == values[i]) return true;
		return false;
	}
	
	@Override
	public Short remove(Object key) {
		if(!keyType.isInstance(key)) return getDefaultReturnValue();
		int index = ((T)key).ordinal();
		if(!isSet(index)) return getDefaultReturnValue();
		clear(index);
		short result = values[index];
		values[index] = (short)0;
		return result;
	}
	
	@Override
	public short rem(T key) {
		if(!keyType.isInstance(key)) return getDefaultReturnValue();
		int index = key.ordinal();
		if(!isSet(index)) return getDefaultReturnValue();
		clear(index);
		short result = values[index];
		values[index] = (short)0;
		return result;
	}

	@Override
	public short remOrDefault(T key, short defaultValue) {
		int index = key.ordinal();
		if(!isSet(index)) return defaultValue;
		clear(index);
		short result = values[index];
		values[index] = (short)0;
		return result;
	}
	
	@Override
	public boolean remove(T key, short value) {
		int index = key.ordinal();
		if(!isSet(index) || value != values[index]) return false;
		clear(index);
		values[index] = (short)0;
		return true;
	}
	
	@Override
	public short getShort(T key) {
		if(!keyType.isInstance(key)) return getDefaultReturnValue();
		int index = key.ordinal();
		return isSet(index) ? values[index] : getDefaultReturnValue();
	}

	@Override
	public short getOrDefault(T key, short defaultValue) {
		if(!keyType.isInstance(key)) return defaultValue;
		int index = key.ordinal();
		return isSet(index) ? values[index] : defaultValue;
	}
	
	@Override
	public Enum2ShortMap<T> copy() {
		Enum2ShortMap<T> map = new Enum2ShortMap<>(keyType);
		map.size = size;
		System.arraycopy(present, 0, map.present, 0, Math.min(present.length, map.present.length));
		System.arraycopy(values, 0, map.values, 0, Math.min(values.length, map.values.length));
		return map;
	}
	
	@Override
	public ObjectSet<Object2ShortMap.Entry<T>> object2ShortEntrySet() {
		if(entrySet == null) entrySet = new EntrySet();
		return entrySet;
	}
	
	@Override
	public ObjectSet<T> keySet() {
		if(keySet == null) keySet = new KeySet();
		return keySet;
	}
	
	@Override
	public ShortCollection values() {
		if(valuesC == null) valuesC = new Values();
		return valuesC;
	}
	
	@Override
	public void forEach(ObjectShortConsumer<T> action) {
		if(size() <= 0) return;
		for(int i = 0,m=keys.length;i<m;i++) {
			if(isSet(i)) action.accept(keys[i], values[i]);
		}
	}
	
	@Override
	public boolean replace(T key, short oldValue, short newValue) {
		int index = key.ordinal();
		if(!isSet(index) || values[index] != oldValue) return false;
		values[index] = newValue;
		return true;
	}
	
	@Override
	public short replace(T key, short value) {
		int index = key.ordinal();
		if(!isSet(index)) return getDefaultReturnValue();
		short oldValue = values[index];
		values[index] = value;
		return oldValue;
	}
	
	@Override
	public short computeShort(T key, ObjectShortUnaryOperator<T> mappingFunction) {
		int index = key.ordinal();
		if(!isSet(index)) {
			short newValue = mappingFunction.applyAsShort(key, getDefaultReturnValue());
			set(index);
			values[index] = newValue;
			return newValue;
		}
		short newValue = mappingFunction.applyAsShort(key, values[index]);
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public short computeShortIfAbsent(T key, ToShortFunction<T> mappingFunction) {
		int index = key.ordinal();
		if(!isSet(index)) {
			short newValue = mappingFunction.applyAsShort(key);
			set(index);
			values[index] = newValue;			
			return newValue;
		}
		short newValue = values[index];
		return newValue;
	}
		
	@Override
	public short supplyShortIfAbsent(T key, ShortSupplier valueProvider) {
		int index = key.ordinal();
		if(!isSet(index)) {
			short newValue = valueProvider.getAsShort();
			set(index);
			values[index] = newValue;			
			return newValue;
		}
		short newValue = values[index];
		return newValue;
	}
	
	@Override
	public short computeShortIfPresent(T key, ObjectShortUnaryOperator<T> mappingFunction) {
		int index = key.ordinal();
		if(!isSet(index)) return getDefaultReturnValue();
		short newValue = mappingFunction.applyAsShort(key, values[index]);
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public short computeShortNonDefault(T key, ObjectShortUnaryOperator<T> mappingFunction) {
		int index = key.ordinal();
		if(!isSet(index)) {
			short newValue = mappingFunction.applyAsShort(key, getDefaultReturnValue());
			if(newValue == getDefaultReturnValue()) return newValue;
			set(index);
			values[index] = newValue;
			return newValue;
		}
		short newValue = mappingFunction.applyAsShort(key, values[index]);
		if(newValue == getDefaultReturnValue()) {
			clear(index);
			values[index] = (short)0;
			return newValue;
		}
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public short computeShortIfAbsentNonDefault(T key, ToShortFunction<T> mappingFunction) {
		int index = key.ordinal();
		if(!isSet(index)) {
			short newValue = mappingFunction.applyAsShort(key);
			if(newValue == getDefaultReturnValue()) return newValue;
			set(index);
			values[index] = newValue;			
			return newValue;
		}
		short newValue = values[index];
		if(newValue == getDefaultReturnValue()) {
			newValue = mappingFunction.applyAsShort(key);
			if(newValue == getDefaultReturnValue()) return newValue;
			values[index] = newValue;
		}
		return newValue;
	}
	
	@Override
	public short supplyShortIfAbsentNonDefault(T key, ShortSupplier valueProvider) {
		int index = key.ordinal();
		if(!isSet(index)) {
			short newValue = valueProvider.getAsShort();
			if(newValue == getDefaultReturnValue()) return newValue;
			set(index);
			values[index] = newValue;			
			return newValue;
		}
		short newValue = values[index];
		if(newValue == getDefaultReturnValue()) {
			newValue = valueProvider.getAsShort();
			if(newValue == getDefaultReturnValue()) return newValue;
			values[index] = newValue;
		}
		return newValue;
	}
	
	@Override
	public short computeShortIfPresentNonDefault(T key, ObjectShortUnaryOperator<T> mappingFunction) {
		int index = key.ordinal();
		if(!isSet(index) || values[index] == getDefaultReturnValue()) return getDefaultReturnValue();
		short newValue = mappingFunction.applyAsShort(key, values[index]);
		if(newValue == getDefaultReturnValue()) {
			clear(index);
			values[index] = (short)0;
			return newValue;
		}
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public short mergeShort(T key, short value, ShortShortUnaryOperator mappingFunction) {
		int index = key.ordinal();
		short newValue = !isSet(index) || values[index] == getDefaultReturnValue() ? value : mappingFunction.applyAsShort(values[index], value);
		if(newValue == getDefaultReturnValue()) {
			if(isSet(index)) {
				clear(index);
				values[index] = (short)0;
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
	public void mergeAllShort(Object2ShortMap<T> m, ShortShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Object2ShortMap.Entry<T> entry : getFastIterable(m)) {
			T key = entry.getKey();
			int index = key.ordinal();
			short newValue = !isSet(index) || values[index] == getDefaultReturnValue() ? entry.getShortValue() : mappingFunction.applyAsShort(values[index], entry.getShortValue());
			if(newValue == getDefaultReturnValue()) {
				if(isSet(index)) {
					clear(index);
					values[index] = (short)0;
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
		Arrays.fill(values, (short)0);
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
	
	class EntrySet extends AbstractObjectSet<Object2ShortMap.Entry<T>> {
		
		@Override
		public boolean contains(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Object2ShortMap.Entry) {
					Object2ShortMap.Entry<T> entry = (Object2ShortMap.Entry<T>)o;
					if(!keyType.isInstance(entry.getKey())) return false;
					int index = ((T)entry.getKey()).ordinal();
					if(index >= 0 && Enum2ShortMap.this.isSet(index)) return entry.getShortValue() == Enum2ShortMap.this.values[index];
				}
				else {
					Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
					if(!keyType.isInstance(entry.getKey())) return false;
					int index = ((T)entry.getKey()).ordinal();
					if(index >= 0 && Enum2ShortMap.this.isSet(index)) return Objects.equals(entry.getValue(), Short.valueOf(Enum2ShortMap.this.values[index]));
				}
			}
			return false;
		}
		
		@Override
		public boolean remove(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Object2ShortMap.Entry) {
					Object2ShortMap.Entry<T> entry = (Object2ShortMap.Entry<T>)o;
					return Enum2ShortMap.this.remove(entry.getKey(), entry.getShortValue());
				}
				Map.Entry<?, ?> entry = (java.util.Map.Entry<?, ?>)o;
				return Enum2ShortMap.this.remove(entry.getKey(), entry.getValue());
			}
			return false;
		}
		
		@Override
		public void forEach(Consumer<? super Object2ShortMap.Entry<T>> action) {
			if(size() <= 0) return;
			for(int i = 0,m=keys.length;i<m;i++) {
				if(isSet(i)) action.accept(new ValueMapEntry(i));
			}
		}
		
		@Override
		public ObjectIterator<Object2ShortMap.Entry<T>> iterator() {
			return new EntryIterator();
		}

		@Override
		public int size() {
			return Enum2ShortMap.this.size();
		}
		
		@Override
		public void clear() {
			Enum2ShortMap.this.clear();
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
			Enum2ShortMap.this.remove(o);
			return size != size();
		}
		
		@Override
		public ObjectIterator<T> iterator() {
			return new KeyIterator();
		}
		
		@Override
		public int size() {
			return Enum2ShortMap.this.size();
		}
		
		@Override
		public void clear() {
			Enum2ShortMap.this.clear();
		}
	}
	
	class Values extends AbstractShortCollection {

		@Override
		public boolean add(short o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean contains(Object e) { return containsValue(e); }
		
		@Override
		public ShortIterator iterator() {
			return new ValueIterator();
		}

		@Override
		public int size() {
			return Enum2ShortMap.this.size();
		}
		
		@Override
		public void clear() {
			Enum2ShortMap.this.clear();
		}
	}
	
	class EntryIterator extends MapIterator implements ObjectIterator<Object2ShortMap.Entry<T>> {
		@Override
		public Object2ShortMap.Entry<T> next() {
			return new ValueMapEntry(nextEntry());
		}
	}
	
	class KeyIterator extends MapIterator implements ObjectIterator<T> {
		@Override
		public T next() {
			return keys[nextEntry()];
		}
	}
	
	class ValueIterator extends MapIterator implements ShortIterator {
		@Override
		public short nextShort() {
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
			values[lastReturnValue] = (short)0;
			lastReturnValue = -1;
		}
	}
	
	protected class ValueMapEntry extends MapEntry {
		protected T key;
		protected short value;
		
		public ValueMapEntry(int index) {
			super(index);
			key = keys[index];
			value = values[index];
		}
		
		@Override
		public T getKey() {
			return key;
		}

		@Override
		public short getShortValue() {
			return value;
		}
		
		@Override
		public short setValue(short value) {
			this.value = value;
			return super.setValue(value);
		}
	}
	
	protected class MapEntry implements Object2ShortMap.Entry<T>, Map.Entry<T, Short> {
		public int index = -1;
		
		public MapEntry() {}
		public MapEntry(int index) {
			this.index = index;
		}
		
		void set(int index) {
			this.index = index;
		}
		
		@Override
		public T getKey() {
			return keys[index];
		}

		@Override
		public short getShortValue() {
			return values[index];
		}

		@Override
		public short setValue(short value) {
			short oldValue = values[index];
			values[index] = value;
			return oldValue;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Map.Entry) {
				if(obj instanceof Object2ShortMap.Entry) {
					Object2ShortMap.Entry<T> entry = (Object2ShortMap.Entry<T>)obj;
					return Objects.equals(getKey(), entry.getKey()) && getShortValue() == entry.getShortValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return value instanceof Short && Objects.equals(getKey(), key) && getShortValue() == ((Short)value).shortValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Objects.hashCode(getKey()) ^ Short.hashCode(getShortValue());
		}
		
		@Override
		public String toString() {
			return Objects.toString(getKey()) + "=" + Short.toString(getShortValue());
		}
	}
}