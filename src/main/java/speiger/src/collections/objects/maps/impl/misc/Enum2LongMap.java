package speiger.src.collections.objects.maps.impl.misc;

import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

import speiger.src.collections.longs.collections.AbstractLongCollection;
import speiger.src.collections.longs.collections.LongCollection;
import speiger.src.collections.longs.collections.LongIterator;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.longs.functions.LongSupplier;
import speiger.src.collections.longs.functions.function.LongLongUnaryOperator;
import speiger.src.collections.objects.functions.consumer.ObjectLongConsumer;
import speiger.src.collections.objects.functions.function.ObjectLongUnaryOperator;
import speiger.src.collections.objects.functions.function.Object2LongFunction;
import speiger.src.collections.objects.utils.maps.Object2LongMaps;
import speiger.src.collections.objects.maps.abstracts.AbstractObject2LongMap;
import speiger.src.collections.objects.maps.interfaces.Object2LongMap;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectSet;

/**
 * A Type Specific EnumMap implementation that allows for Primitive Values.
 * Unlike javas implementation this one does not jump around between a single long or long array implementation based around the enum size
 * This will cause a bit more memory usage but allows for a simpler implementation.
 * @param <T> the type of elements maintained by this Collection
 */
public class Enum2LongMap<T extends Enum<T>> extends AbstractObject2LongMap<T>
{
	/** Enum Type that is being used */
	protected Class<T> keyType;
	/** The Backing keys array. */
	protected transient T[] keys;
	/** The Backing values array */
	protected transient long[] values;
	/** The Backing array that indicates which index is present or not */
	protected transient long[] present;
	/** Amount of Elements stored in the ArrayMap */
	protected int size = 0;
	/** EntrySet cache */
	protected transient ObjectSet<Object2LongMap.Entry<T>> entrySet;
	/** KeySet cache */
	protected transient ObjectSet<T> keySet;
	/** Values cache */
	protected transient LongCollection valuesC;
	
	protected Enum2LongMap() {
		
	}
	/**
	 * Default Constructor
	 * @param keyType the type of Enum that should be used
	 */
	public Enum2LongMap(Class<T> keyType) {
		this.keyType = keyType;
		keys = getKeyUniverse(keyType);
		values = new long[keys.length];
		present = new long[((keys.length - 1) >> 6) + 1];
	}
	
	/**
	 * Helper constructor that allow to create a EnumMap from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the EnumMap
	 * @param values the values that should be put into the EnumMap.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Enum2LongMap(T[] keys, Long[] values) {
		if(keys.length <= 0) throw new IllegalArgumentException("Empty Array are not allowed");
		if(keys.length != values.length) throw new IllegalArgumentException("Keys and Values have to be the same size");
		keyType = keys[0].getDeclaringClass();
		this.keys = getKeyUniverse(keyType);
		this.values = new long[keys.length];
		present = new long[((keys.length - 1) >> 6) + 1];
		putAll(keys, values);
	}
	
	/**
	 * Helper constructor that allow to create a EnumMap from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Enum2LongMap(T[] keys, long[] values) {
		if(keys.length <= 0) throw new IllegalArgumentException("Empty Array are not allowed");
		if(keys.length != values.length) throw new IllegalArgumentException("Keys and Values have to be the same size");
		keyType = keys[0].getDeclaringClass();
		this.keys = getKeyUniverse(keyType);
		this.values = new long[keys.length];
		present = new long[((keys.length - 1) >> 6) + 1];
		putAll(keys, values);		
	}
	
	/**
	 * A Helper constructor that allows to create a EnumMap with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 */
	public Enum2LongMap(Map<? extends T, ? extends Long> map) {
		if(map instanceof Enum2LongMap) {
			Enum2LongMap<T> enumMap = (Enum2LongMap<T>)map;
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
			this.values = new long[keys.length];
			present = new long[((keys.length - 1) >> 6) + 1];
			putAll(map);
		}
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new EnumMap with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 */
	public Enum2LongMap(Object2LongMap<T> map) {
		if(map instanceof Enum2LongMap) {
			Enum2LongMap<T> enumMap = (Enum2LongMap<T>)map;
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
			this.values = new long[keys.length];
			present = new long[((keys.length - 1) >> 6) + 1];
			putAll(map);
		}
	}
	
	@Override
	public long put(T key, long value) {
		int index = key.ordinal();
		if(isSet(index)) {
			long result = values[index];
			values[index] = value;
			return result;
		}
		set(index);
		values[index] = value;
		return getDefaultReturnValue();
	}
	
	@Override
	public long putIfAbsent(T key, long value) {
		int index = key.ordinal();
		if(isSet(index)) return values[index];
		set(index);
		values[index] = value;
		return getDefaultReturnValue();
	}
	
	@Override
	public long addTo(T key, long value) {
		int index = key.ordinal();
		if(isSet(index)) {
			long result = values[index];
			values[index] += value;
			return result;
		}
		set(index);
		values[index] = value;
		return getDefaultReturnValue();
	}
	
	@Override
	public long subFrom(T key, long value) {
		int index = key.ordinal();
		if(!isSet(index)) return getDefaultReturnValue();
		long oldValue = values[index];
		values[index] -= value;
		if(value < 0 ? (values[index] >= getDefaultReturnValue()) : (values[index] <= getDefaultReturnValue())) {
			clear(index);
			values[index] = 0L;
		}
		return oldValue;
	}
	@Override
	public boolean containsKey(Object key) {
		if(!keyType.isInstance(key)) return false;
		return isSet(((T)key).ordinal());
	}
	
	@Override
	public boolean containsValue(long value) {
		for(int i = 0;i<values.length;i++)
			if(isSet(i) && value == values[i]) return true;
		return false;
	}
	
	@Override
	public Long remove(Object key) {
		if(!keyType.isInstance(key)) return getDefaultReturnValue();
		int index = ((T)key).ordinal();
		if(!isSet(index)) return getDefaultReturnValue();
		clear(index);
		long result = values[index];
		values[index] = 0L;
		return result;
	}
	
	@Override
	public long rem(T key) {
		if(!keyType.isInstance(key)) return getDefaultReturnValue();
		int index = key.ordinal();
		if(!isSet(index)) return getDefaultReturnValue();
		clear(index);
		long result = values[index];
		values[index] = 0L;
		return result;
	}

	@Override
	public long remOrDefault(T key, long defaultValue) {
		int index = key.ordinal();
		if(!isSet(index)) return defaultValue;
		clear(index);
		long result = values[index];
		values[index] = 0L;
		return result;
	}
	
	@Override
	public boolean remove(T key, long value) {
		int index = key.ordinal();
		if(!isSet(index) || value != values[index]) return false;
		clear(index);
		values[index] = 0L;
		return true;
	}
	
	@Override
	public long getLong(T key) {
		if(!keyType.isInstance(key)) return getDefaultReturnValue();
		int index = key.ordinal();
		return isSet(index) ? values[index] : getDefaultReturnValue();
	}

	@Override
	public long getOrDefault(T key, long defaultValue) {
		if(!keyType.isInstance(key)) return defaultValue;
		int index = key.ordinal();
		return isSet(index) ? values[index] : defaultValue;
	}
	
	@Override
	public Enum2LongMap<T> copy() {
		Enum2LongMap<T> map = new Enum2LongMap<>(keyType);
		map.size = size;
		System.arraycopy(present, 0, map.present, 0, Math.min(present.length, map.present.length));
		System.arraycopy(values, 0, map.values, 0, Math.min(values.length, map.values.length));
		return map;
	}
	
	@Override
	public ObjectSet<Object2LongMap.Entry<T>> object2LongEntrySet() {
		if(entrySet == null) entrySet = new EntrySet();
		return entrySet;
	}
	
	@Override
	public ObjectSet<T> keySet() {
		if(keySet == null) keySet = new KeySet();
		return keySet;
	}
	
	@Override
	public LongCollection values() {
		if(valuesC == null) valuesC = new Values();
		return valuesC;
	}
	
	@Override
	public void forEach(ObjectLongConsumer<T> action) {
		if(size() <= 0) return;
		for(int i = 0,m=keys.length;i<m;i++) {
			if(isSet(i)) action.accept(keys[i], values[i]);
		}
	}
	
	@Override
	public boolean replace(T key, long oldValue, long newValue) {
		int index = key.ordinal();
		if(!isSet(index) || values[index] != oldValue) return false;
		values[index] = newValue;
		return true;
	}
	
	@Override
	public long replace(T key, long value) {
		int index = key.ordinal();
		if(!isSet(index)) return getDefaultReturnValue();
		long oldValue = values[index];
		values[index] = value;
		return oldValue;
	}
	
	@Override
	public long computeLong(T key, ObjectLongUnaryOperator<T> mappingFunction) {
		int index = key.ordinal();
		if(!isSet(index)) {
			long newValue = mappingFunction.applyAsLong(key, getDefaultReturnValue());
			if(newValue == getDefaultReturnValue()) return newValue;
			set(index);
			values[index] = newValue;
			return newValue;
		}
		long newValue = mappingFunction.applyAsLong(key, values[index]);
		if(newValue == getDefaultReturnValue()) {
			clear(index);
			values[index] = 0L;
			return newValue;
		}
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public long computeLongIfAbsent(T key, Object2LongFunction<T> mappingFunction) {
		int index = key.ordinal();
		if(!isSet(index)) {
			long newValue = mappingFunction.getLong(key);
			if(newValue == getDefaultReturnValue()) return newValue;
			set(index);
			values[index] = newValue;			
			return newValue;
		}
		long newValue = values[index];
		if(newValue == getDefaultReturnValue()) {
			newValue = mappingFunction.getLong(key);
			if(newValue == getDefaultReturnValue()) return newValue;
			values[index] = newValue;
		}
		return newValue;
	}
	
	@Override
	public long supplyLongIfAbsent(T key, LongSupplier valueProvider) {
		int index = key.ordinal();
		if(!isSet(index)) {
			long newValue = valueProvider.getLong();
			if(newValue == getDefaultReturnValue()) return newValue;
			set(index);
			values[index] = newValue;			
			return newValue;
		}
		long newValue = values[index];
		if(newValue == getDefaultReturnValue()) {
			newValue = valueProvider.getLong();
			if(newValue == getDefaultReturnValue()) return newValue;
			values[index] = newValue;
		}
		return newValue;
	}
	
	@Override
	public long computeLongIfPresent(T key, ObjectLongUnaryOperator<T> mappingFunction) {
		int index = key.ordinal();
		if(!isSet(index) || values[index] == getDefaultReturnValue()) return getDefaultReturnValue();
		long newValue = mappingFunction.applyAsLong(key, values[index]);
		if(newValue == getDefaultReturnValue()) {
			clear(index);
			values[index] = 0L;
			return newValue;
		}
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public long mergeLong(T key, long value, LongLongUnaryOperator mappingFunction) {
		int index = key.ordinal();
		long newValue = !isSet(index) || values[index] == getDefaultReturnValue() ? value : mappingFunction.applyAsLong(values[index], value);
		if(newValue == getDefaultReturnValue()) {
			if(isSet(index)) {
				clear(index);
				values[index] = 0L;
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
	public void mergeAllLong(Object2LongMap<T> m, LongLongUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Object2LongMap.Entry<T> entry : Object2LongMaps.fastIterable(m)) {
			T key = entry.getKey();
			int index = key.ordinal();
			long newValue = !isSet(index) || values[index] == getDefaultReturnValue() ? entry.getLongValue() : mappingFunction.applyAsLong(values[index], entry.getLongValue());
			if(newValue == getDefaultReturnValue()) {
				if(isSet(index)) {
					clear(index);
					values[index] = 0L;
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
		Arrays.fill(values, 0L);
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
	
	class EntrySet extends AbstractObjectSet<Object2LongMap.Entry<T>> {
		
		@Override
		public boolean contains(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Object2LongMap.Entry) {
					Object2LongMap.Entry<T> entry = (Object2LongMap.Entry<T>)o;
					if(!keyType.isInstance(entry.getKey())) return false;
					int index = ((T)entry.getKey()).ordinal();
					if(index >= 0 && Enum2LongMap.this.isSet(index)) return entry.getLongValue() == Enum2LongMap.this.values[index];
				}
				else {
					Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
					if(!keyType.isInstance(entry.getKey())) return false;
					int index = ((T)entry.getKey()).ordinal();
					if(index >= 0 && Enum2LongMap.this.isSet(index)) return Objects.equals(entry.getValue(), Long.valueOf(Enum2LongMap.this.values[index]));
				}
			}
			return false;
		}
		
		@Override
		public boolean remove(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Object2LongMap.Entry) {
					Object2LongMap.Entry<T> entry = (Object2LongMap.Entry<T>)o;
					return Enum2LongMap.this.remove(entry.getKey(), entry.getLongValue());
				}
				Map.Entry<?, ?> entry = (java.util.Map.Entry<?, ?>)o;
				return Enum2LongMap.this.remove(entry.getKey(), entry.getValue());
			}
			return false;
		}
		
		@Override
		public ObjectIterator<Object2LongMap.Entry<T>> iterator() {
			return new EntryIterator();
		}

		@Override
		public int size() {
			return Enum2LongMap.this.size();
		}
		
		@Override
		public void clear() {
			Enum2LongMap.this.clear();
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
			Enum2LongMap.this.remove(o);
			return size != size();
		}
		
		@Override
		public ObjectIterator<T> iterator() {
			return new KeyIterator();
		}
		
		@Override
		public int size() {
			return Enum2LongMap.this.size();
		}
		
		@Override
		public void clear() {
			Enum2LongMap.this.clear();
		}
	}
	
	class Values extends AbstractLongCollection {

		@Override
		public boolean add(long o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean contains(Object e) { return containsValue(e); }
		
		@Override
		public LongIterator iterator() {
			return new ValueIterator();
		}

		@Override
		public int size() {
			return Enum2LongMap.this.size();
		}
		
		@Override
		public void clear() {
			Enum2LongMap.this.clear();
		}
	}
	
	class EntryIterator extends MapIterator implements ObjectIterator<Object2LongMap.Entry<T>> {
		@Override
		public Object2LongMap.Entry<T> next() {
			return new MapEntry(nextEntry());
		}
	}
	
	class KeyIterator extends MapIterator implements ObjectIterator<T> {
		@Override
		public T next() {
			return keys[nextEntry()];
		}
	}
	
	class ValueIterator extends MapIterator implements LongIterator {
		@Override
		public long nextLong() {
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
			values[lastReturnValue] = 0L;
			lastReturnValue = -1;
		}
	}
	
	protected class MapEntry implements Object2LongMap.Entry<T>, Map.Entry<T, Long> {
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
		public long getLongValue() {
			return values[index];
		}

		@Override
		public long setValue(long value) {
			long oldValue = values[index];
			values[index] = value;
			return oldValue;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Map.Entry) {
				if(obj instanceof Object2LongMap.Entry) {
					Object2LongMap.Entry<T> entry = (Object2LongMap.Entry<T>)obj;
					return Objects.equals(keys[index], entry.getKey()) && values[index] == entry.getLongValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return value instanceof Long && Objects.equals(keys[index], key) && values[index] == ((Long)value).longValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Objects.hashCode(keys[index]) ^ Long.hashCode(values[index]);
		}
		
		@Override
		public String toString() {
			return Objects.toString(keys[index]) + "=" + Long.toString(values[index]);
		}
	}
}