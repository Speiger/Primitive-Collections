package speiger.src.collections.objects.maps.impl.misc;

import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

import speiger.src.collections.objects.collections.AbstractObjectCollection;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.functions.ObjectSupplier;
import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;
import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;
import speiger.src.collections.objects.functions.function.Object2ObjectFunction;
import speiger.src.collections.objects.utils.maps.Object2ObjectMaps;
import speiger.src.collections.objects.maps.abstracts.AbstractObject2ObjectMap;
import speiger.src.collections.objects.maps.interfaces.Object2ObjectMap;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectSet;

/**
 * A Type Specific EnumMap implementation that allows for Primitive Values.
 * Unlike javas implementation this one does not jump around between a single long or long array implementation based around the enum size
 * This will cause a bit more memory usage but allows for a simpler implementation.
 * @param <T> the type of elements maintained by this Collection
 * @param <V> the type of elements maintained by this Collection
 */
public class Enum2ObjectMap<T extends Enum<T>, V> extends AbstractObject2ObjectMap<T, V>
{
	/** Enum Type that is being used */
	protected Class<T> keyType;
	/** The Backing keys array. */
	protected transient T[] keys;
	/** The Backing values array */
	protected transient V[] values;
	/** The Backing array that indicates which index is present or not */
	protected transient long[] present;
	/** Amount of Elements stored in the ArrayMap */
	protected int size = 0;
	/** EntrySet cache */
	protected transient ObjectSet<Object2ObjectMap.Entry<T, V>> entrySet;
	/** KeySet cache */
	protected transient ObjectSet<T> keySet;
	/** Values cache */
	protected transient ObjectCollection<V> valuesC;
	
	protected Enum2ObjectMap() {
		
	}
	/**
	 * Default Constructor
	 * @param keyType the type of Enum that should be used
	 */
	public Enum2ObjectMap(Class<T> keyType) {
		this.keyType = keyType;
		keys = getKeyUniverse(keyType);
		values = (V[])new Object[keys.length];
		present = new long[((keys.length - 1) >> 6) + 1];
	}
	
	/**
	 * Helper constructor that allow to create a EnumMap from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Enum2ObjectMap(T[] keys, V[] values) {
		if(keys.length <= 0) throw new IllegalArgumentException("Empty Array are not allowed");
		if(keys.length != values.length) throw new IllegalArgumentException("Keys and Values have to be the same size");
		keyType = keys[0].getDeclaringClass();
		this.keys = getKeyUniverse(keyType);
		this.values = (V[])new Object[keys.length];
		present = new long[((keys.length - 1) >> 6) + 1];
		putAll(keys, values);		
	}
	
	/**
	 * A Helper constructor that allows to create a EnumMap with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 */
	public Enum2ObjectMap(Map<? extends T, ? extends V> map) {
		if(map instanceof Enum2ObjectMap) {
			Enum2ObjectMap<T, V> enumMap = (Enum2ObjectMap<T, V>)map;
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
			this.values = (V[])new Object[keys.length];
			present = new long[((keys.length - 1) >> 6) + 1];
			putAll(map);
		}
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new EnumMap with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 */
	public Enum2ObjectMap(Object2ObjectMap<T, V> map) {
		if(map instanceof Enum2ObjectMap) {
			Enum2ObjectMap<T, V> enumMap = (Enum2ObjectMap<T, V>)map;
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
			this.values = (V[])new Object[keys.length];
			present = new long[((keys.length - 1) >> 6) + 1];
			putAll(map);
		}
	}
	
	@Override
	public V put(T key, V value) {
		int index = key.ordinal();
		if(isSet(index)) {
			V result = values[index];
			values[index] = value;
			return result;
		}
		set(index);
		values[index] = value;
		return getDefaultReturnValue();
	}
	
	@Override
	public V putIfAbsent(T key, V value) {
		int index = key.ordinal();
		if(isSet(index)) return values[index];
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
	public boolean containsValue(Object value) {
		for(int i = 0;i<values.length;i++)
			if(isSet(i) && Objects.equals(value, values[i])) return true;
		return false;
	}
	
	@Override
	public V remove(Object key) {
		if(!keyType.isInstance(key)) return getDefaultReturnValue();
		int index = ((T)key).ordinal();
		if(!isSet(index)) return getDefaultReturnValue();
		clear(index);
		V result = values[index];
		values[index] = null;
		return result;
	}
	
	@Override
	public V rem(T key) {
		if(!keyType.isInstance(key)) return getDefaultReturnValue();
		int index = key.ordinal();
		if(!isSet(index)) return getDefaultReturnValue();
		clear(index);
		V result = values[index];
		values[index] = null;
		return result;
	}

	@Override
	public V remOrDefault(T key, V defaultValue) {
		int index = key.ordinal();
		if(!isSet(index)) return defaultValue;
		clear(index);
		V result = values[index];
		values[index] = null;
		return result;
	}
	
	@Override
	public boolean remove(Object key, Object value) {
		int index = ((T)key).ordinal();
		if(!isSet(index) || !Objects.equals(value, values[index])) return false;
		clear(index);
		values[index] = null;
		return true;
	}
	
	@Override
	public V getObject(T key) {
		if(!keyType.isInstance(key)) return getDefaultReturnValue();
		int index = key.ordinal();
		return isSet(index) ? values[index] : getDefaultReturnValue();
	}

	@Override
	public V getOrDefault(Object key, V defaultValue) {
		if(!keyType.isInstance(key)) return defaultValue;
		int index = ((T)key).ordinal();
		return isSet(index) ? values[index] : defaultValue;
	}
	
	@Override
	public Enum2ObjectMap<T, V> copy() {
		Enum2ObjectMap<T, V> map = new Enum2ObjectMap<>(keyType);
		map.size = size;
		System.arraycopy(present, 0, map.present, 0, Math.min(present.length, map.present.length));
		System.arraycopy(values, 0, map.values, 0, Math.min(values.length, map.values.length));
		return map;
	}
	
	@Override
	public ObjectSet<Object2ObjectMap.Entry<T, V>> object2ObjectEntrySet() {
		if(entrySet == null) entrySet = new EntrySet();
		return entrySet;
	}
	
	@Override
	public ObjectSet<T> keySet() {
		if(keySet == null) keySet = new KeySet();
		return keySet;
	}
	
	@Override
	public ObjectCollection<V> values() {
		if(valuesC == null) valuesC = new Values();
		return valuesC;
	}
	
	@Override
	public void forEach(ObjectObjectConsumer<T, V> action) {
		if(size() <= 0) return;
		for(int i = 0,m=keys.length;i<m;i++) {
			if(isSet(i)) action.accept(keys[i], values[i]);
		}
	}
	
	@Override
	public boolean replace(T key, V oldValue, V newValue) {
		int index = key.ordinal();
		if(!isSet(index) || values[index] != oldValue) return false;
		values[index] = newValue;
		return true;
	}
	
	@Override
	public V replace(T key, V value) {
		int index = key.ordinal();
		if(!isSet(index)) return getDefaultReturnValue();
		V oldValue = values[index];
		values[index] = value;
		return oldValue;
	}
	
	@Override
	public V compute(T key, ObjectObjectUnaryOperator<T, V> mappingFunction) {
		int index = key.ordinal();
		if(!isSet(index)) {
			V newValue = mappingFunction.apply(key, getDefaultReturnValue());
			if(Objects.equals(newValue, getDefaultReturnValue())) return newValue;
			set(index);
			values[index] = newValue;
			return newValue;
		}
		V newValue = mappingFunction.apply(key, values[index]);
		if(Objects.equals(newValue, getDefaultReturnValue())) {
			clear(index);
			values[index] = null;
			return newValue;
		}
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public V computeIfAbsent(T key, Object2ObjectFunction<T, V> mappingFunction) {
		int index = key.ordinal();
		if(!isSet(index)) {
			V newValue = mappingFunction.getObject(key);
			if(Objects.equals(newValue, getDefaultReturnValue())) return newValue;
			set(index);
			values[index] = newValue;			
			return newValue;
		}
		V newValue = values[index];
		if(Objects.equals(newValue, getDefaultReturnValue())) {
			newValue = mappingFunction.getObject(key);
			if(Objects.equals(newValue, getDefaultReturnValue())) return newValue;
			values[index] = newValue;
		}
		return newValue;
	}
	
	@Override
	public V supplyIfAbsent(T key, ObjectSupplier<V> valueProvider) {
		int index = key.ordinal();
		if(!isSet(index)) {
			V newValue = valueProvider.get();
			if(Objects.equals(newValue, getDefaultReturnValue())) return newValue;
			set(index);
			values[index] = newValue;			
			return newValue;
		}
		V newValue = values[index];
		if(Objects.equals(newValue, getDefaultReturnValue())) {
			newValue = valueProvider.get();
			if(Objects.equals(newValue, getDefaultReturnValue())) return newValue;
			values[index] = newValue;
		}
		return newValue;
	}
	
	@Override
	public V computeIfPresent(T key, ObjectObjectUnaryOperator<T, V> mappingFunction) {
		int index = key.ordinal();
		if(!isSet(index) || Objects.equals(values[index], getDefaultReturnValue())) return getDefaultReturnValue();
		V newValue = mappingFunction.apply(key, values[index]);
		if(Objects.equals(newValue, getDefaultReturnValue())) {
			clear(index);
			values[index] = null;
			return newValue;
		}
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public V merge(T key, V value, ObjectObjectUnaryOperator<V, V> mappingFunction) {
		int index = key.ordinal();
		Objects.requireNonNull(value);
		V newValue = !isSet(index) || Objects.equals(values[index], getDefaultReturnValue()) ? value : mappingFunction.apply(values[index], value);
		if(Objects.equals(newValue, getDefaultReturnValue())) {
			if(isSet(index)) {
				clear(index);
				values[index] = null;
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
	public void mergeAll(Object2ObjectMap<T, V> m, ObjectObjectUnaryOperator<V, V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Object2ObjectMap.Entry<T, V> entry : Object2ObjectMaps.fastIterable(m)) {
			T key = entry.getKey();
			int index = key.ordinal();
			V newValue = !isSet(index) || Objects.equals(values[index], getDefaultReturnValue()) ? entry.getValue() : mappingFunction.apply(values[index], entry.getValue());
			if(Objects.equals(newValue, getDefaultReturnValue())) {
				if(isSet(index)) {
					clear(index);
					values[index] = null;
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
		Arrays.fill(values, null);
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
	
	class EntrySet extends AbstractObjectSet<Object2ObjectMap.Entry<T, V>> {
		
		@Override
		public boolean contains(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Object2ObjectMap.Entry) {
					Object2ObjectMap.Entry<T, V> entry = (Object2ObjectMap.Entry<T, V>)o;
					if(!keyType.isInstance(entry.getKey())) return false;
					int index = ((T)entry.getKey()).ordinal();
					if(index >= 0 && Enum2ObjectMap.this.isSet(index)) return Objects.equals(entry.getValue(), Enum2ObjectMap.this.values[index]);
				}
				else {
					Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
					if(!keyType.isInstance(entry.getKey())) return false;
					int index = ((T)entry.getKey()).ordinal();
					if(index >= 0 && Enum2ObjectMap.this.isSet(index)) return Objects.equals(entry.getValue(), Enum2ObjectMap.this.values[index]);
				}
			}
			return false;
		}
		
		@Override
		public boolean remove(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Object2ObjectMap.Entry) {
					Object2ObjectMap.Entry<T, V> entry = (Object2ObjectMap.Entry<T, V>)o;
					return Enum2ObjectMap.this.remove(entry.getKey(), entry.getValue());
				}
				Map.Entry<?, ?> entry = (java.util.Map.Entry<?, ?>)o;
				return Enum2ObjectMap.this.remove(entry.getKey(), entry.getValue());
			}
			return false;
		}
		
		@Override
		public ObjectIterator<Object2ObjectMap.Entry<T, V>> iterator() {
			return new EntryIterator();
		}

		@Override
		public int size() {
			return Enum2ObjectMap.this.size();
		}
		
		@Override
		public void clear() {
			Enum2ObjectMap.this.clear();
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
			Enum2ObjectMap.this.remove(o);
			return size != size();
		}
		
		@Override
		public ObjectIterator<T> iterator() {
			return new KeyIterator();
		}
		
		@Override
		public int size() {
			return Enum2ObjectMap.this.size();
		}
		
		@Override
		public void clear() {
			Enum2ObjectMap.this.clear();
		}
	}
	
	class Values extends AbstractObjectCollection<V> {

		@Override
		public boolean add(V o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean contains(Object e) { return containsValue(e); }
		
		@Override
		public ObjectIterator<V> iterator() {
			return new ValueIterator();
		}

		@Override
		public int size() {
			return Enum2ObjectMap.this.size();
		}
		
		@Override
		public void clear() {
			Enum2ObjectMap.this.clear();
		}
	}
	
	class EntryIterator extends MapIterator implements ObjectIterator<Object2ObjectMap.Entry<T, V>> {
		@Override
		public Object2ObjectMap.Entry<T, V> next() {
			return new MapEntry(nextEntry());
		}
	}
	
	class KeyIterator extends MapIterator implements ObjectIterator<T> {
		@Override
		public T next() {
			return keys[nextEntry()];
		}
	}
	
	class ValueIterator extends MapIterator implements ObjectIterator<V> {
		@Override
		public V next() {
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
			values[lastReturnValue] = null;
			lastReturnValue = -1;
		}
	}
	
	protected class MapEntry implements Object2ObjectMap.Entry<T, V>, Map.Entry<T, V> {
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
		public V getValue() {
			return values[index];
		}

		@Override
		public V setValue(V value) {
			V oldValue = values[index];
			values[index] = value;
			return oldValue;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Map.Entry) {
				if(obj instanceof Object2ObjectMap.Entry) {
					Object2ObjectMap.Entry<T, V> entry = (Object2ObjectMap.Entry<T, V>)obj;
					return Objects.equals(keys[index], entry.getKey()) && Objects.equals(values[index], entry.getValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return Objects.equals(keys[index], key) && Objects.equals(values[index], value);
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Objects.hashCode(keys[index]) ^ Objects.hashCode(values[index]);
		}
		
		@Override
		public String toString() {
			return Objects.toString(keys[index]) + "=" + Objects.toString(values[index]);
		}
	}
}