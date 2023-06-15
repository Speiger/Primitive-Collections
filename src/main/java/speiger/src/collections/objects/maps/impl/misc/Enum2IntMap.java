package speiger.src.collections.objects.maps.impl.misc;

import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;


import speiger.src.collections.ints.collections.AbstractIntCollection;
import speiger.src.collections.ints.collections.IntCollection;
import speiger.src.collections.ints.collections.IntIterator;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.ints.functions.IntSupplier;
import speiger.src.collections.ints.functions.function.IntIntUnaryOperator;
import speiger.src.collections.objects.functions.consumer.ObjectIntConsumer;
import speiger.src.collections.objects.functions.function.ObjectIntUnaryOperator;
import speiger.src.collections.objects.functions.function.ToIntFunction;
import speiger.src.collections.objects.maps.abstracts.AbstractObject2IntMap;
import speiger.src.collections.objects.maps.interfaces.Object2IntMap;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectSet;

/**
 * A Type Specific EnumMap implementation that allows for Primitive Values.
 * Unlike javas implementation this one does not jump around between a single long or long array implementation based around the enum size
 * This will cause a bit more memory usage but allows for a simpler implementation.
 * @param <T> the keyType of elements maintained by this Collection
 */
public class Enum2IntMap<T extends Enum<T>> extends AbstractObject2IntMap<T>
{
	/** Enum Type that is being used */
	protected Class<T> keyType;
	/** The Backing keys array. */
	protected transient T[] keys;
	/** The Backing values array */
	protected transient int[] values;
	/** The Backing array that indicates which index is present or not */
	protected transient long[] present;
	/** Amount of Elements stored in the ArrayMap */
	protected int size = 0;
	/** EntrySet cache */
	protected transient ObjectSet<Object2IntMap.Entry<T>> entrySet;
	/** KeySet cache */
	protected transient ObjectSet<T> keySet;
	/** Values cache */
	protected transient IntCollection valuesC;
	
	protected Enum2IntMap() {
		
	}
	/**
	 * Default Constructor
	 * @param keyType the type of Enum that should be used
	 */
	public Enum2IntMap(Class<T> keyType) {
		this.keyType = keyType;
		keys = getKeyUniverse(keyType);
		values = new int[keys.length];
		present = new long[((keys.length - 1) >> 6) + 1];
	}
	
	/**
	 * Helper constructor that allow to create a EnumMap from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the EnumMap
	 * @param values the values that should be put into the EnumMap.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Enum2IntMap(T[] keys, Integer[] values) {
		if(keys.length <= 0) throw new IllegalArgumentException("Empty Array are not allowed");
		if(keys.length != values.length) throw new IllegalArgumentException("Keys and Values have to be the same size");
		keyType = keys[0].getDeclaringClass();
		this.keys = getKeyUniverse(keyType);
		this.values = new int[this.keys.length];
		present = new long[((this.keys.length - 1) >> 6) + 1];
		putAll(keys, values);
	}
	
	/**
	 * Helper constructor that allow to create a EnumMap from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Enum2IntMap(T[] keys, int[] values) {
		if(keys.length <= 0) throw new IllegalArgumentException("Empty Array are not allowed");
		if(keys.length != values.length) throw new IllegalArgumentException("Keys and Values have to be the same size");
		keyType = keys[0].getDeclaringClass();
		this.keys = getKeyUniverse(keyType);
		this.values = new int[this.keys.length];
		present = new long[((this.keys.length - 1) >> 6) + 1];
		putAll(keys, values);		
	}
	
	/**
	 * A Helper constructor that allows to create a EnumMap with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 */
	public Enum2IntMap(Map<? extends T, ? extends Integer> map) {
		if(map instanceof Enum2IntMap) {
			Enum2IntMap<T> enumMap = (Enum2IntMap<T>)map;
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
			this.values = new int[keys.length];
			present = new long[((keys.length - 1) >> 6) + 1];
			putAll(map);
		}
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new EnumMap with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 */
	public Enum2IntMap(Object2IntMap<T> map) {
		if(map instanceof Enum2IntMap) {
			Enum2IntMap<T> enumMap = (Enum2IntMap<T>)map;
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
			this.values = new int[keys.length];
			present = new long[((keys.length - 1) >> 6) + 1];
			putAll(map);
		}
	}
	
	@Override
	public int put(T key, int value) {
		int index = key.ordinal();
		if(isSet(index)) {
			int result = values[index];
			values[index] = value;
			return result;
		}
		set(index);
		values[index] = value;
		return getDefaultReturnValue();
	}
	
	@Override
	public int putIfAbsent(T key, int value) {
		int index = key.ordinal();
		if(isSet(index)) {
			if(values[index] == getDefaultReturnValue()) {
				int oldValue = values[index];
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
	public int addTo(T key, int value) {
		int index = key.ordinal();
		if(isSet(index)) {
			int result = values[index];
			values[index] += value;
			return result;
		}
		set(index);
		values[index] = value;
		return getDefaultReturnValue();
	}
	
	@Override
	public int subFrom(T key, int value) {
		int index = key.ordinal();
		if(!isSet(index)) return getDefaultReturnValue();
		int oldValue = values[index];
		values[index] -= value;
		if(value < 0 ? (values[index] >= getDefaultReturnValue()) : (values[index] <= getDefaultReturnValue())) {
			clear(index);
			values[index] = 0;
		}
		return oldValue;
	}
	@Override
	public boolean containsKey(Object key) {
		if(!keyType.isInstance(key)) return false;
		return isSet(((T)key).ordinal());
	}
	
	@Override
	public boolean containsValue(int value) {
		for(int i = 0;i<values.length;i++)
			if(isSet(i) && value == values[i]) return true;
		return false;
	}
	
	@Override
	public Integer remove(Object key) {
		if(!keyType.isInstance(key)) return getDefaultReturnValue();
		int index = ((T)key).ordinal();
		if(!isSet(index)) return getDefaultReturnValue();
		clear(index);
		int result = values[index];
		values[index] = 0;
		return result;
	}
	
	@Override
	public int rem(T key) {
		if(!keyType.isInstance(key)) return getDefaultReturnValue();
		int index = key.ordinal();
		if(!isSet(index)) return getDefaultReturnValue();
		clear(index);
		int result = values[index];
		values[index] = 0;
		return result;
	}

	@Override
	public int remOrDefault(T key, int defaultValue) {
		int index = key.ordinal();
		if(!isSet(index)) return defaultValue;
		clear(index);
		int result = values[index];
		values[index] = 0;
		return result;
	}
	
	@Override
	public boolean remove(T key, int value) {
		int index = key.ordinal();
		if(!isSet(index) || value != values[index]) return false;
		clear(index);
		values[index] = 0;
		return true;
	}
	
	@Override
	public int getInt(T key) {
		if(!keyType.isInstance(key)) return getDefaultReturnValue();
		int index = key.ordinal();
		return isSet(index) ? values[index] : getDefaultReturnValue();
	}

	@Override
	public int getOrDefault(T key, int defaultValue) {
		if(!keyType.isInstance(key)) return defaultValue;
		int index = key.ordinal();
		return isSet(index) ? values[index] : defaultValue;
	}
	
	@Override
	public Enum2IntMap<T> copy() {
		Enum2IntMap<T> map = new Enum2IntMap<>(keyType);
		map.size = size;
		System.arraycopy(present, 0, map.present, 0, Math.min(present.length, map.present.length));
		System.arraycopy(values, 0, map.values, 0, Math.min(values.length, map.values.length));
		return map;
	}
	
	@Override
	public ObjectSet<Object2IntMap.Entry<T>> object2IntEntrySet() {
		if(entrySet == null) entrySet = new EntrySet();
		return entrySet;
	}
	
	@Override
	public ObjectSet<T> keySet() {
		if(keySet == null) keySet = new KeySet();
		return keySet;
	}
	
	@Override
	public IntCollection values() {
		if(valuesC == null) valuesC = new Values();
		return valuesC;
	}
	
	@Override
	public void forEach(ObjectIntConsumer<T> action) {
		if(size() <= 0) return;
		for(int i = 0,m=keys.length;i<m;i++) {
			if(isSet(i)) action.accept(keys[i], values[i]);
		}
	}
	
	@Override
	public boolean replace(T key, int oldValue, int newValue) {
		int index = key.ordinal();
		if(!isSet(index) || values[index] != oldValue) return false;
		values[index] = newValue;
		return true;
	}
	
	@Override
	public int replace(T key, int value) {
		int index = key.ordinal();
		if(!isSet(index)) return getDefaultReturnValue();
		int oldValue = values[index];
		values[index] = value;
		return oldValue;
	}
	
	@Override
	public int computeInt(T key, ObjectIntUnaryOperator<T> mappingFunction) {
		int index = key.ordinal();
		if(!isSet(index)) {
			int newValue = mappingFunction.applyAsInt(key, getDefaultReturnValue());
			set(index);
			values[index] = newValue;
			return newValue;
		}
		int newValue = mappingFunction.applyAsInt(key, values[index]);
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public int computeIntNonDefault(T key, ObjectIntUnaryOperator<T> mappingFunction) {
		int index = key.ordinal();
		if(!isSet(index)) {
			int newValue = mappingFunction.applyAsInt(key, getDefaultReturnValue());
			if(newValue == getDefaultReturnValue()) return newValue;
			set(index);
			values[index] = newValue;
			return newValue;
		}
		int newValue = mappingFunction.applyAsInt(key, values[index]);
		if(newValue == getDefaultReturnValue()) {
			clear(index);
			values[index] = 0;
			return newValue;
		}
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public int computeIntIfAbsent(T key, ToIntFunction<T> mappingFunction) {
		int index = key.ordinal();
		if(!isSet(index)) {
			int newValue = mappingFunction.applyAsInt(key);
			set(index);
			values[index] = newValue;			
			return newValue;
		}
		int newValue = values[index];
		return newValue;
	}
	
	@Override
	public int computeIntIfAbsentNonDefault(T key, ToIntFunction<T> mappingFunction) {
		int index = key.ordinal();
		if(!isSet(index)) {
			int newValue = mappingFunction.applyAsInt(key);
			if(newValue == getDefaultReturnValue()) return newValue;
			set(index);
			values[index] = newValue;			
			return newValue;
		}
		int newValue = values[index];
		if(newValue == getDefaultReturnValue()) {
			newValue = mappingFunction.applyAsInt(key);
			if(newValue == getDefaultReturnValue()) return newValue;
			values[index] = newValue;
		}
		return newValue;
	}
	
	@Override
	public int supplyIntIfAbsent(T key, IntSupplier valueProvider) {
		int index = key.ordinal();
		if(!isSet(index)) {
			int newValue = valueProvider.getAsInt();
			set(index);
			values[index] = newValue;			
			return newValue;
		}
		int newValue = values[index];
		return newValue;
	}
	
	@Override
	public int supplyIntIfAbsentNonDefault(T key, IntSupplier valueProvider) {
		int index = key.ordinal();
		if(!isSet(index)) {
			int newValue = valueProvider.getAsInt();
			if(newValue == getDefaultReturnValue()) return newValue;
			set(index);
			values[index] = newValue;			
			return newValue;
		}
		int newValue = values[index];
		if(newValue == getDefaultReturnValue()) {
			newValue = valueProvider.getAsInt();
			if(newValue == getDefaultReturnValue()) return newValue;
			values[index] = newValue;
		}
		return newValue;
	}
	
	@Override
	public int computeIntIfPresent(T key, ObjectIntUnaryOperator<T> mappingFunction) {
		int index = key.ordinal();
		if(!isSet(index)) return getDefaultReturnValue();
		int newValue = mappingFunction.applyAsInt(key, values[index]);
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public int computeIntIfPresentNonDefault(T key, ObjectIntUnaryOperator<T> mappingFunction) {
		int index = key.ordinal();
		if(!isSet(index) || values[index] == getDefaultReturnValue()) return getDefaultReturnValue();
		int newValue = mappingFunction.applyAsInt(key, values[index]);
		if(newValue == getDefaultReturnValue()) {
			clear(index);
			values[index] = 0;
			return newValue;
		}
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public int mergeInt(T key, int value, IntIntUnaryOperator mappingFunction) {
		int index = key.ordinal();
		int newValue = !isSet(index) || values[index] == getDefaultReturnValue() ? value : mappingFunction.applyAsInt(values[index], value);
		if(newValue == getDefaultReturnValue()) {
			if(isSet(index)) {
				clear(index);
				values[index] = 0;
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
	public void mergeAllInt(Object2IntMap<T> m, IntIntUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Object2IntMap.Entry<T> entry : getFastIterable(m)) {
			T key = entry.getKey();
			int index = key.ordinal();
			int newValue = !isSet(index) || values[index] == getDefaultReturnValue() ? entry.getIntValue() : mappingFunction.applyAsInt(values[index], entry.getIntValue());
			if(newValue == getDefaultReturnValue()) {
				if(isSet(index)) {
					clear(index);
					values[index] = 0;
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
		Arrays.fill(values, 0);
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
	
	class EntrySet extends AbstractObjectSet<Object2IntMap.Entry<T>> {
		
		@Override
		public boolean contains(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Object2IntMap.Entry) {
					Object2IntMap.Entry<T> entry = (Object2IntMap.Entry<T>)o;
					if(!keyType.isInstance(entry.getKey())) return false;
					int index = ((T)entry.getKey()).ordinal();
					if(index >= 0 && Enum2IntMap.this.isSet(index)) return entry.getIntValue() == Enum2IntMap.this.values[index];
				}
				else {
					Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
					if(!keyType.isInstance(entry.getKey())) return false;
					int index = ((T)entry.getKey()).ordinal();
					if(index >= 0 && Enum2IntMap.this.isSet(index)) return Objects.equals(entry.getValue(), Integer.valueOf(Enum2IntMap.this.values[index]));
				}
			}
			return false;
		}
		
		@Override
		public boolean remove(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Object2IntMap.Entry) {
					Object2IntMap.Entry<T> entry = (Object2IntMap.Entry<T>)o;
					return Enum2IntMap.this.remove(entry.getKey(), entry.getIntValue());
				}
				Map.Entry<?, ?> entry = (java.util.Map.Entry<?, ?>)o;
				return Enum2IntMap.this.remove(entry.getKey(), entry.getValue());
			}
			return false;
		}
		
		@Override
		public void forEach(Consumer<? super Object2IntMap.Entry<T>> action) {
			if(size() <= 0) return;
			for(int i = 0,m=keys.length;i<m;i++) {
				if(isSet(i)) action.accept(new ValueMapEntry(i));
			}
		}
		
		@Override
		public ObjectIterator<Object2IntMap.Entry<T>> iterator() {
			return new EntryIterator();
		}

		@Override
		public int size() {
			return Enum2IntMap.this.size();
		}
		
		@Override
		public void clear() {
			Enum2IntMap.this.clear();
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
			Enum2IntMap.this.remove(o);
			return size != size();
		}
		
		@Override
		public ObjectIterator<T> iterator() {
			return new KeyIterator();
		}
		
		@Override
		public int size() {
			return Enum2IntMap.this.size();
		}
		
		@Override
		public void clear() {
			Enum2IntMap.this.clear();
		}
	}
	
	class Values extends AbstractIntCollection {

		@Override
		public boolean add(int o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean contains(Object e) { return containsValue(e); }
		
		@Override
		public IntIterator iterator() {
			return new ValueIterator();
		}

		@Override
		public int size() {
			return Enum2IntMap.this.size();
		}
		
		@Override
		public void clear() {
			Enum2IntMap.this.clear();
		}
	}
	
	class EntryIterator extends MapIterator implements ObjectIterator<Object2IntMap.Entry<T>> {
		@Override
		public Object2IntMap.Entry<T> next() {
			return new ValueMapEntry(nextEntry());
		}
	}
	
	class KeyIterator extends MapIterator implements ObjectIterator<T> {
		@Override
		public T next() {
			return keys[nextEntry()];
		}
	}
	
	class ValueIterator extends MapIterator implements IntIterator {
		@Override
		public int nextInt() {
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
			values[lastReturnValue] = 0;
			lastReturnValue = -1;
		}
	}
	
	protected class ValueMapEntry extends MapEntry {
		protected T key;
		protected int value;
		
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
		public int getIntValue() {
			return value;
		}
		
		@Override
		public int setValue(int value) {
			this.value = value;
			return super.setValue(value);
		}
	}
	
	protected class MapEntry implements Object2IntMap.Entry<T>, Map.Entry<T, Integer> {
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
		public int getIntValue() {
			return values[index];
		}

		@Override
		public int setValue(int value) {
			int oldValue = values[index];
			values[index] = value;
			return oldValue;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Map.Entry) {
				if(obj instanceof Object2IntMap.Entry) {
					Object2IntMap.Entry<T> entry = (Object2IntMap.Entry<T>)obj;
					return Objects.equals(getKey(), entry.getKey()) && getIntValue() == entry.getIntValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return value instanceof Integer && Objects.equals(getKey(), key) && getIntValue() == ((Integer)value).intValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Objects.hashCode(getKey()) ^ Integer.hashCode(getIntValue());
		}
		
		@Override
		public String toString() {
			return Objects.toString(getKey()) + "=" + Integer.toString(getIntValue());
		}
	}
}