package speiger.src.collections.objects.maps.impl.misc;

import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;


import speiger.src.collections.booleans.collections.AbstractBooleanCollection;
import speiger.src.collections.booleans.collections.BooleanCollection;
import speiger.src.collections.booleans.collections.BooleanIterator;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.booleans.functions.BooleanSupplier;
import speiger.src.collections.booleans.functions.function.BooleanBooleanUnaryOperator;
import speiger.src.collections.objects.functions.consumer.ObjectBooleanConsumer;
import speiger.src.collections.objects.functions.function.ObjectBooleanUnaryOperator;
import speiger.src.collections.objects.maps.abstracts.AbstractObject2BooleanMap;
import speiger.src.collections.objects.maps.interfaces.Object2BooleanMap;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectSet;

/**
 * A Type Specific EnumMap implementation that allows for Primitive Values.
 * Unlike javas implementation this one does not jump around between a single long or long array implementation based around the enum size
 * This will cause a bit more memory usage but allows for a simpler implementation.
 * @param <T> the keyType of elements maintained by this Collection
 */
public class Enum2BooleanMap<T extends Enum<T>> extends AbstractObject2BooleanMap<T>
{
	/** Enum Type that is being used */
	protected Class<T> keyType;
	/** The Backing keys array. */
	protected transient T[] keys;
	/** The Backing values array */
	protected transient boolean[] values;
	/** The Backing array that indicates which index is present or not */
	protected transient long[] present;
	/** Amount of Elements stored in the ArrayMap */
	protected int size = 0;
	/** EntrySet cache */
	protected transient ObjectSet<Object2BooleanMap.Entry<T>> entrySet;
	/** KeySet cache */
	protected transient ObjectSet<T> keySet;
	/** Values cache */
	protected transient BooleanCollection valuesC;
	
	protected Enum2BooleanMap() {
		
	}
	/**
	 * Default Constructor
	 * @param keyType the type of Enum that should be used
	 */
	public Enum2BooleanMap(Class<T> keyType) {
		this.keyType = keyType;
		keys = getKeyUniverse(keyType);
		values = new boolean[keys.length];
		present = new long[((keys.length - 1) >> 6) + 1];
	}
	
	/**
	 * Helper constructor that allow to create a EnumMap from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the EnumMap
	 * @param values the values that should be put into the EnumMap.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Enum2BooleanMap(T[] keys, Boolean[] values) {
		if(keys.length <= 0) throw new IllegalArgumentException("Empty Array are not allowed");
		if(keys.length != values.length) throw new IllegalArgumentException("Keys and Values have to be the same size");
		keyType = keys[0].getDeclaringClass();
		this.keys = getKeyUniverse(keyType);
		this.values = new boolean[this.keys.length];
		present = new long[((this.keys.length - 1) >> 6) + 1];
		putAll(keys, values);
	}
	
	/**
	 * Helper constructor that allow to create a EnumMap from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Enum2BooleanMap(T[] keys, boolean[] values) {
		if(keys.length <= 0) throw new IllegalArgumentException("Empty Array are not allowed");
		if(keys.length != values.length) throw new IllegalArgumentException("Keys and Values have to be the same size");
		keyType = keys[0].getDeclaringClass();
		this.keys = getKeyUniverse(keyType);
		this.values = new boolean[this.keys.length];
		present = new long[((this.keys.length - 1) >> 6) + 1];
		putAll(keys, values);		
	}
	
	/**
	 * A Helper constructor that allows to create a EnumMap with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 */
	public Enum2BooleanMap(Map<? extends T, ? extends Boolean> map) {
		if(map instanceof Enum2BooleanMap) {
			Enum2BooleanMap<T> enumMap = (Enum2BooleanMap<T>)map;
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
			this.values = new boolean[keys.length];
			present = new long[((keys.length - 1) >> 6) + 1];
			putAll(map);
		}
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new EnumMap with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 */
	public Enum2BooleanMap(Object2BooleanMap<T> map) {
		if(map instanceof Enum2BooleanMap) {
			Enum2BooleanMap<T> enumMap = (Enum2BooleanMap<T>)map;
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
			this.values = new boolean[keys.length];
			present = new long[((keys.length - 1) >> 6) + 1];
			putAll(map);
		}
	}
	
	@Override
	public boolean put(T key, boolean value) {
		int index = key.ordinal();
		if(isSet(index)) {
			boolean result = values[index];
			values[index] = value;
			return result;
		}
		set(index);
		values[index] = value;
		return getDefaultReturnValue();
	}
	
	@Override
	public boolean putIfAbsent(T key, boolean value) {
		int index = key.ordinal();
		if(isSet(index)) {
			if(values[index] == getDefaultReturnValue()) {
				boolean oldValue = values[index];
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
	public boolean containsKey(Object key) {
		if(!keyType.isInstance(key)) return false;
		return isSet(((T)key).ordinal());
	}
	
	@Override
	public boolean containsValue(boolean value) {
		for(int i = 0;i<values.length;i++)
			if(isSet(i) && value == values[i]) return true;
		return false;
	}
	
	@Override
	public Boolean remove(Object key) {
		if(!keyType.isInstance(key)) return getDefaultReturnValue();
		int index = ((T)key).ordinal();
		if(!isSet(index)) return getDefaultReturnValue();
		clear(index);
		boolean result = values[index];
		values[index] = false;
		return result;
	}
	
	@Override
	public boolean rem(T key) {
		if(!keyType.isInstance(key)) return getDefaultReturnValue();
		int index = key.ordinal();
		if(!isSet(index)) return getDefaultReturnValue();
		clear(index);
		boolean result = values[index];
		values[index] = false;
		return result;
	}

	@Override
	public boolean remOrDefault(T key, boolean defaultValue) {
		int index = key.ordinal();
		if(!isSet(index)) return defaultValue;
		clear(index);
		boolean result = values[index];
		values[index] = false;
		return result;
	}
	
	@Override
	public boolean remove(T key, boolean value) {
		int index = key.ordinal();
		if(!isSet(index) || value != values[index]) return false;
		clear(index);
		values[index] = false;
		return true;
	}
	
	@Override
	public boolean getBoolean(T key) {
		if(!keyType.isInstance(key)) return getDefaultReturnValue();
		int index = key.ordinal();
		return isSet(index) ? values[index] : getDefaultReturnValue();
	}

	@Override
	public boolean getOrDefault(T key, boolean defaultValue) {
		if(!keyType.isInstance(key)) return defaultValue;
		int index = key.ordinal();
		return isSet(index) ? values[index] : defaultValue;
	}
	
	@Override
	public Enum2BooleanMap<T> copy() {
		Enum2BooleanMap<T> map = new Enum2BooleanMap<>(keyType);
		map.size = size;
		System.arraycopy(present, 0, map.present, 0, Math.min(present.length, map.present.length));
		System.arraycopy(values, 0, map.values, 0, Math.min(values.length, map.values.length));
		return map;
	}
	
	@Override
	public ObjectSet<Object2BooleanMap.Entry<T>> object2BooleanEntrySet() {
		if(entrySet == null) entrySet = new EntrySet();
		return entrySet;
	}
	
	@Override
	public ObjectSet<T> keySet() {
		if(keySet == null) keySet = new KeySet();
		return keySet;
	}
	
	@Override
	public BooleanCollection values() {
		if(valuesC == null) valuesC = new Values();
		return valuesC;
	}
	
	@Override
	public void forEach(ObjectBooleanConsumer<T> action) {
		if(size() <= 0) return;
		for(int i = 0,m=keys.length;i<m;i++) {
			if(isSet(i)) action.accept(keys[i], values[i]);
		}
	}
	
	@Override
	public boolean replace(T key, boolean oldValue, boolean newValue) {
		int index = key.ordinal();
		if(!isSet(index) || values[index] != oldValue) return false;
		values[index] = newValue;
		return true;
	}
	
	@Override
	public boolean replace(T key, boolean value) {
		int index = key.ordinal();
		if(!isSet(index)) return getDefaultReturnValue();
		boolean oldValue = values[index];
		values[index] = value;
		return oldValue;
	}
	
	@Override
	public boolean computeBoolean(T key, ObjectBooleanUnaryOperator<T> mappingFunction) {
		int index = key.ordinal();
		if(!isSet(index)) {
			boolean newValue = mappingFunction.applyAsBoolean(key, getDefaultReturnValue());
			set(index);
			values[index] = newValue;
			return newValue;
		}
		boolean newValue = mappingFunction.applyAsBoolean(key, values[index]);
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public boolean computeBooleanNonDefault(T key, ObjectBooleanUnaryOperator<T> mappingFunction) {
		int index = key.ordinal();
		if(!isSet(index)) {
			boolean newValue = mappingFunction.applyAsBoolean(key, getDefaultReturnValue());
			if(newValue == getDefaultReturnValue()) return newValue;
			set(index);
			values[index] = newValue;
			return newValue;
		}
		boolean newValue = mappingFunction.applyAsBoolean(key, values[index]);
		if(newValue == getDefaultReturnValue()) {
			clear(index);
			values[index] = false;
			return newValue;
		}
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public boolean computeBooleanIfAbsent(T key, Predicate<T> mappingFunction) {
		int index = key.ordinal();
		if(!isSet(index)) {
			boolean newValue = mappingFunction.test(key);
			set(index);
			values[index] = newValue;			
			return newValue;
		}
		boolean newValue = values[index];
		return newValue;
	}
	
	@Override
	public boolean computeBooleanIfAbsentNonDefault(T key, Predicate<T> mappingFunction) {
		int index = key.ordinal();
		if(!isSet(index)) {
			boolean newValue = mappingFunction.test(key);
			if(newValue == getDefaultReturnValue()) return newValue;
			set(index);
			values[index] = newValue;			
			return newValue;
		}
		boolean newValue = values[index];
		if(newValue == getDefaultReturnValue()) {
			newValue = mappingFunction.test(key);
			if(newValue == getDefaultReturnValue()) return newValue;
			values[index] = newValue;
		}
		return newValue;
	}
	
	@Override
	public boolean supplyBooleanIfAbsent(T key, BooleanSupplier valueProvider) {
		int index = key.ordinal();
		if(!isSet(index)) {
			boolean newValue = valueProvider.getAsBoolean();
			set(index);
			values[index] = newValue;			
			return newValue;
		}
		boolean newValue = values[index];
		return newValue;
	}
	
	@Override
	public boolean supplyBooleanIfAbsentNonDefault(T key, BooleanSupplier valueProvider) {
		int index = key.ordinal();
		if(!isSet(index)) {
			boolean newValue = valueProvider.getAsBoolean();
			if(newValue == getDefaultReturnValue()) return newValue;
			set(index);
			values[index] = newValue;			
			return newValue;
		}
		boolean newValue = values[index];
		if(newValue == getDefaultReturnValue()) {
			newValue = valueProvider.getAsBoolean();
			if(newValue == getDefaultReturnValue()) return newValue;
			values[index] = newValue;
		}
		return newValue;
	}
	
	@Override
	public boolean computeBooleanIfPresent(T key, ObjectBooleanUnaryOperator<T> mappingFunction) {
		int index = key.ordinal();
		if(!isSet(index)) return getDefaultReturnValue();
		boolean newValue = mappingFunction.applyAsBoolean(key, values[index]);
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public boolean computeBooleanIfPresentNonDefault(T key, ObjectBooleanUnaryOperator<T> mappingFunction) {
		int index = key.ordinal();
		if(!isSet(index) || values[index] == getDefaultReturnValue()) return getDefaultReturnValue();
		boolean newValue = mappingFunction.applyAsBoolean(key, values[index]);
		if(newValue == getDefaultReturnValue()) {
			clear(index);
			values[index] = false;
			return newValue;
		}
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public boolean mergeBoolean(T key, boolean value, BooleanBooleanUnaryOperator mappingFunction) {
		int index = key.ordinal();
		boolean newValue = !isSet(index) || values[index] == getDefaultReturnValue() ? value : mappingFunction.applyAsBoolean(values[index], value);
		if(newValue == getDefaultReturnValue()) {
			if(isSet(index)) {
				clear(index);
				values[index] = false;
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
	public void mergeAllBoolean(Object2BooleanMap<T> m, BooleanBooleanUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Object2BooleanMap.Entry<T> entry : getFastIterable(m)) {
			T key = entry.getKey();
			int index = key.ordinal();
			boolean newValue = !isSet(index) || values[index] == getDefaultReturnValue() ? entry.getBooleanValue() : mappingFunction.applyAsBoolean(values[index], entry.getBooleanValue());
			if(newValue == getDefaultReturnValue()) {
				if(isSet(index)) {
					clear(index);
					values[index] = false;
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
		Arrays.fill(values, false);
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
	
	class EntrySet extends AbstractObjectSet<Object2BooleanMap.Entry<T>> {
		
		@Override
		public boolean contains(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Object2BooleanMap.Entry) {
					Object2BooleanMap.Entry<T> entry = (Object2BooleanMap.Entry<T>)o;
					if(!keyType.isInstance(entry.getKey())) return false;
					int index = ((T)entry.getKey()).ordinal();
					if(index >= 0 && Enum2BooleanMap.this.isSet(index)) return entry.getBooleanValue() == Enum2BooleanMap.this.values[index];
				}
				else {
					Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
					if(!keyType.isInstance(entry.getKey())) return false;
					int index = ((T)entry.getKey()).ordinal();
					if(index >= 0 && Enum2BooleanMap.this.isSet(index)) return Objects.equals(entry.getValue(), Boolean.valueOf(Enum2BooleanMap.this.values[index]));
				}
			}
			return false;
		}
		
		@Override
		public boolean remove(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Object2BooleanMap.Entry) {
					Object2BooleanMap.Entry<T> entry = (Object2BooleanMap.Entry<T>)o;
					return Enum2BooleanMap.this.remove(entry.getKey(), entry.getBooleanValue());
				}
				Map.Entry<?, ?> entry = (java.util.Map.Entry<?, ?>)o;
				return Enum2BooleanMap.this.remove(entry.getKey(), entry.getValue());
			}
			return false;
		}
		
		@Override
		public void forEach(Consumer<? super Object2BooleanMap.Entry<T>> action) {
			if(size() <= 0) return;
			for(int i = 0,m=keys.length;i<m;i++) {
				if(isSet(i)) action.accept(new ValueMapEntry(i));
			}
		}
		
		@Override
		public ObjectIterator<Object2BooleanMap.Entry<T>> iterator() {
			return new EntryIterator();
		}

		@Override
		public int size() {
			return Enum2BooleanMap.this.size();
		}
		
		@Override
		public void clear() {
			Enum2BooleanMap.this.clear();
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
			Enum2BooleanMap.this.remove(o);
			return size != size();
		}
		
		@Override
		public ObjectIterator<T> iterator() {
			return new KeyIterator();
		}
		
		@Override
		public int size() {
			return Enum2BooleanMap.this.size();
		}
		
		@Override
		public void clear() {
			Enum2BooleanMap.this.clear();
		}
	}
	
	class Values extends AbstractBooleanCollection {

		@Override
		public boolean add(boolean o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean contains(Object e) { return containsValue(e); }
		
		@Override
		public BooleanIterator iterator() {
			return new ValueIterator();
		}

		@Override
		public int size() {
			return Enum2BooleanMap.this.size();
		}
		
		@Override
		public void clear() {
			Enum2BooleanMap.this.clear();
		}
	}
	
	class EntryIterator extends MapIterator implements ObjectIterator<Object2BooleanMap.Entry<T>> {
		@Override
		public Object2BooleanMap.Entry<T> next() {
			return new ValueMapEntry(nextEntry());
		}
	}
	
	class KeyIterator extends MapIterator implements ObjectIterator<T> {
		@Override
		public T next() {
			return keys[nextEntry()];
		}
	}
	
	class ValueIterator extends MapIterator implements BooleanIterator {
		@Override
		public boolean nextBoolean() {
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
			values[lastReturnValue] = false;
			lastReturnValue = -1;
		}
	}
	
	protected class ValueMapEntry extends MapEntry {
		protected T key;
		protected boolean value;
		
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
		public boolean getBooleanValue() {
			return value;
		}
		
		@Override
		public boolean setValue(boolean value) {
			this.value = value;
			return super.setValue(value);
		}
	}
	
	protected class MapEntry implements Object2BooleanMap.Entry<T>, Map.Entry<T, Boolean> {
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
		public boolean getBooleanValue() {
			return values[index];
		}

		@Override
		public boolean setValue(boolean value) {
			boolean oldValue = values[index];
			values[index] = value;
			return oldValue;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Map.Entry) {
				if(obj instanceof Object2BooleanMap.Entry) {
					Object2BooleanMap.Entry<T> entry = (Object2BooleanMap.Entry<T>)obj;
					return Objects.equals(getKey(), entry.getKey()) && getBooleanValue() == entry.getBooleanValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return value instanceof Boolean && Objects.equals(getKey(), key) && getBooleanValue() == ((Boolean)value).booleanValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Objects.hashCode(getKey()) ^ Boolean.hashCode(getBooleanValue());
		}
		
		@Override
		public String toString() {
			return Objects.toString(getKey()) + "=" + Boolean.toString(getBooleanValue());
		}
	}
}