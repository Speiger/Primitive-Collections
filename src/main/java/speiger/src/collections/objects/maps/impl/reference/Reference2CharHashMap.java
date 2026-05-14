package speiger.src.collections.objects.maps.impl.reference;

import java.lang.ref.WeakReference;
import java.lang.ref.ReferenceQueue;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import speiger.src.collections.ints.functions.consumer.IntObjectConsumer;
import speiger.src.collections.ints.functions.consumer.IntCharConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectCharConsumer;
import speiger.src.collections.objects.functions.function.ToCharFunction;
import speiger.src.collections.objects.functions.function.ObjectCharUnaryOperator;
import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;
import speiger.src.collections.objects.maps.abstracts.AbstractObject2CharMap;
import speiger.src.collections.objects.maps.interfaces.Object2CharMap;
import speiger.src.collections.chars.collections.AbstractCharCollection;
import speiger.src.collections.chars.collections.CharCollection;
import speiger.src.collections.chars.functions.CharSupplier;
import speiger.src.collections.chars.functions.function.CharCharUnaryOperator;

import speiger.src.collections.chars.collections.CharIterator;
import speiger.src.collections.chars.functions.CharConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;

import speiger.src.collections.chars.functions.function.CharPredicate;
import speiger.src.collections.chars.functions.OptionalChar;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.HashUtil;
import speiger.src.collections.utils.ITrimmable;

/**
 * A Type Specific Custom implementation of the HashMap
 * Instead of using Wrapper Object Arrays for storing keys and values there is dedicated arrays for storing keys and values.
 * Extra to that there is a couple quality of life functions provided
 * @param <T> the keyType of elements maintained by this Collection
 */
public class Reference2CharHashMap<T> extends AbstractObject2CharMap<T> implements ITrimmable
{
	/** The Backing keys array */
	protected transient WeakKey<T>[] keys;
	/** The Backing values array */
	protected transient char[] values;
	/** Minimum array size the HashMap will be */
	protected transient int minCapacity;
	/** Current capacity of the HashMap */
	protected transient int capacity;
	/** Maximum amount of Values that can be stored before the array gets expanded usually 75% */
	protected transient int maxFill;
	/** Max Index that is allowed to be searched through nullIndex - 1 */
	protected transient int mask;
	/** EntrySet cache */
	protected transient FastEntrySet<T> entrySet;
	/** KeySet cache */
	protected transient ObjectSet<T> keySet;
	/** Values cache */
	protected transient CharCollection valuesC;
	
	/** Amount of Elements stored in the HashMap */
	protected int size;
	/** How full the Arrays are allowed to get before resize */
	protected final float loadFactor;
	
	protected final ReferenceQueue<Object> queue = new ReferenceQueue<>();
	
	/**
	 * Default Constructor
	 */
	public Reference2CharHashMap() {
		this(HashUtil.DEFAULT_MIN_CAPACITY, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * Constructor that defines the minimum capacity
	 * @param minCapacity the minimum capacity the HashMap is allowed to be.
	 * @throws IllegalStateException if the minimum capacity is negative
	 */
	public Reference2CharHashMap(int minCapacity) {
		this(minCapacity, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * Constructor that defines the minimum capacity and load factor
	 * @param minCapacity the minimum capacity the HashMap is allowed to be.
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the minimum capacity is negative
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public Reference2CharHashMap(int minCapacity, float loadFactor) {
		if(minCapacity < 0)	throw new IllegalStateException("Minimum Capacity is negative. This is not allowed");
		if(loadFactor <= 0 || loadFactor >= 1F) throw new IllegalStateException("Load Factor is not between 0 and 1");
		this.loadFactor = loadFactor;
		this.minCapacity = capacity = HashUtil.arraySize(minCapacity, loadFactor);
		mask = this.capacity - 1;
		maxFill = Math.min((int)Math.ceil(mask * loadFactor), mask);
		keys = new WeakKey[this.capacity];
		values = new char[this.capacity];
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Reference2CharHashMap(T[] keys, Character[] values) {
		this(keys, values, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public Reference2CharHashMap(T[] keys, Character[] values, float loadFactor) {
		this(keys.length, loadFactor);
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i], values[i].charValue());
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Reference2CharHashMap(T[] keys, char[] values) {
		this(keys, values, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public Reference2CharHashMap(T[] keys, char[] values, float loadFactor) {
		this(keys.length, loadFactor);
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i], values[i]);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 */
	public Reference2CharHashMap(Map<? extends T, ? extends Character> map) {
		this(map, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public Reference2CharHashMap(Map<? extends T, ? extends Character> map, float loadFactor) {
		this(map.size(), loadFactor);
		putAll(map);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 */
	public Reference2CharHashMap(Object2CharMap<T> map) {
		this(map, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
 	 */
	public Reference2CharHashMap(Object2CharMap<T> map, float loadFactor) {
		this(map.size(), loadFactor);
		putAll(map);
	}
	
	@Override
	public char put(T key, char value) {
		int slot = findIndex(key);
		if(slot < 0) {
			insert(-slot-1, key, value);
			return getDefaultReturnValue();
		}
		char oldValue = values[slot];
		values[slot] = value;
		return oldValue;
	}
	
	@Override
	public char putIfAbsent(T key, char value) {
		int slot = findIndex(key);
		if(slot < 0) {
			insert(-slot-1, key, value);
			return getDefaultReturnValue();
		}
		else if(values[slot] == getDefaultReturnValue()) {
			char oldValue = values[slot];
			values[slot] = value;
			return oldValue;
		}
		return values[slot];
	}
	
	@Override
	public char addTo(T key, char value) {
		int slot = findIndex(key);
		if(slot < 0) {
			insert(-slot-1, key, value);
			return getDefaultReturnValue();
		}
		char oldValue = values[slot];
		values[slot] += value;
		return oldValue;
	}
	
	@Override
	public char subFrom(T key, char value) {
		int slot = findIndex(key);
		if(slot < 0) return getDefaultReturnValue();
		char oldValue = values[slot];
		values[slot] -= value;
		if(value < 0 ? (values[slot] >= getDefaultReturnValue()) : (values[slot] <= getDefaultReturnValue())) removeIndex(slot);
		return oldValue;
	}
	@Override
	public boolean containsKey(Object key) {
		return findIndex(key) >= 0;
	}
	
	@Override
	public boolean containsValue(char value) {
		for(int i = capacity-1;i >= 0;i--)
			if(keys[i] != null && values[i] == value) return true;
		return false;
	}
	
	@Override
	@Deprecated
	public boolean containsValue(Object value) {
		for(int i = capacity-1;i >= 0;i--)
			if(keys[i] != null && ((value == null && values[i] == getDefaultReturnValue()) || Objects.equals(value, Character.valueOf(values[i])))) return true;
		return false;
	}
	
	@Override
	public char rem(T key) {
		int slot = findIndex(key);
		if(slot < 0) return getDefaultReturnValue();
		return removeIndex(slot);
	}
	
	@Override
	public char remOrDefault(T key, char defaultValue) {
		int slot = findIndex(key);
		if(slot < 0) return defaultValue;
		return removeIndex(slot);
	}
	
	@Override
	public Character remove(Object key) {
		int slot = findIndex(key);
		if(slot < 0) return Character.valueOf(getDefaultReturnValue());
		return removeIndex(slot);
	}
	
	@Override
	public boolean remove(T key, char value) {
		removeStaleEntries();
		Objects.requireNonNull(key);
		int pos = HashUtil.mix(Objects.hashCode(key)) & mask;
		WeakKey<T> current = keys[pos];
		if(current == null) return false;
		if(Objects.equals(current.get(), key) && value == values[pos]) {
			removeIndex(pos);
			return true;
		}
		while(true) {
			if((current = keys[pos = (++pos & mask)]) == null) return false;
			else if(Objects.equals(current.get(), key) && value == values[pos]) {
				removeIndex(pos);
				return true;
			}
		}
	}
	
	@Override
	public boolean remove(Object key, Object value) {
		removeStaleEntries();
		Objects.requireNonNull(key);
		Objects.requireNonNull(value);
		int pos = HashUtil.mix(key.hashCode()) & mask;
		WeakKey<T> current = keys[pos];
		if(current == null) return false;
		if(Objects.equals(key, current.get()) && Objects.equals(value, Character.valueOf(values[pos]))) {
			removeIndex(pos);
			return true;
		}
		while(true) {
			if((current = keys[pos = (++pos & mask)]) == null) return false;
			else if(Objects.equals(key, current.get()) && Objects.equals(value, Character.valueOf(values[pos]))){
				removeIndex(pos);
				return true;
			}
		}
	}
	
	@Override
	public char getChar(T key) {
		int slot = findIndex(key);
		return slot < 0 ? getDefaultReturnValue() : values[slot];
	}
	
	@Override
	public Character get(Object key) {
		int slot = findIndex(key);
		return Character.valueOf(slot < 0 ? getDefaultReturnValue() : values[slot]);
	}
	
	@Override
	public char getOrDefault(T key, char defaultValue) {
		int slot = findIndex(key);
		return slot < 0 ? defaultValue : values[slot];
	}
	
	@Override
	public Reference2CharHashMap<T> copy() {
		Reference2CharHashMap<T> map = new Reference2CharHashMap<>(0, loadFactor);
		map.minCapacity = minCapacity;
		map.capacity = capacity;
		map.mask = mask;
		map.maxFill = maxFill;
		map.size = size;
		map.keys = new WeakKey[keys.length];
		for(int i = 0,m=keys.length;i<m;i++) {
			if(keys[i] != null) {
				map.keys[i] = new WeakKey<>(keys[i].get(), map.queue).index(i);
			}
		}
		map.values = Arrays.copyOf(values, values.length);
		return map;
	}
	
	@Override
	public ObjectSet<Object2CharMap.Entry<T>> object2CharEntrySet() {
		if(entrySet == null) entrySet = new MapEntrySet();
		return entrySet;
	}
	
	@Override
	public ObjectSet<T> keySet() {
		if(keySet == null) keySet = new KeySet();
		return keySet;
	}
	
	@Override
	public CharCollection values() {
		if(valuesC == null) valuesC = new Values();
		return valuesC;
	}
	
	@Override
	public void forEach(ObjectCharConsumer<T> action) {
		removeStaleEntries();
		if(size() <= 0) return;
		for(int i = capacity-1;i>=0;i--) {
			if(keys[i] != null) action.accept(keys[i].get(), values[i]);
		}
	}
	
	@Override
	public boolean replace(T key, char oldValue, char newValue) {
		int index = findIndex(key);
		if(index < 0 || values[index] != oldValue) return false;
		values[index] = newValue;
		return true;
	}
	
	@Override
	public char replace(T key, char value) {
		int index = findIndex(key);
		if(index < 0) return getDefaultReturnValue();
		char oldValue = values[index];
		values[index] = value;
		return oldValue;
	}
	
	@Override
	public char computeChar(T key, ObjectCharUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index < 0) {
			char newValue = mappingFunction.applyAsChar(key, getDefaultReturnValue());
			insert(-index-1, key, newValue);
			return newValue;
		}
		char newValue = mappingFunction.applyAsChar(key, values[index]);
		values[index] = newValue;
		return newValue;
	}
		
	@Override
	public char computeCharIfAbsent(T key, ToCharFunction<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index < 0) {
			char newValue = mappingFunction.applyAsChar(key);
			insert(-index-1, key, newValue);
			return newValue;
		}
		char newValue = values[index];
		return newValue;
	}
	
	@Override
	public char supplyCharIfAbsent(T key, CharSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		int index = findIndex(key);
		if(index < 0) {
			char newValue = valueProvider.getAsChar();
			insert(-index-1, key, newValue);
			return newValue;
		}
		char newValue = values[index];
		return newValue;
	}
		
	@Override
	public char computeCharIfPresent(T key, ObjectCharUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index < 0) return getDefaultReturnValue();
		char newValue = mappingFunction.applyAsChar(key, values[index]);
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public char computeCharNonDefault(T key, ObjectCharUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index < 0) {
			char newValue = mappingFunction.applyAsChar(key, getDefaultReturnValue());
			if(newValue == getDefaultReturnValue()) return newValue;
			insert(-index-1, key, newValue);
			return newValue;
		}
		char newValue = mappingFunction.applyAsChar(key, values[index]);
		if(newValue == getDefaultReturnValue()) {
			removeIndex(index);
			return newValue;
		}
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public char computeCharIfAbsentNonDefault(T key, ToCharFunction<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index < 0) {
			char newValue = mappingFunction.applyAsChar(key);
			if(newValue == getDefaultReturnValue()) return newValue;
			insert(-index-1, key, newValue);
			return newValue;
		}
		char newValue = values[index];
		if(newValue == getDefaultReturnValue()) {
			newValue = mappingFunction.applyAsChar(key);
			if(newValue == getDefaultReturnValue()) return newValue;
			values[index] = newValue;
		}
		return newValue;
	}
	
	@Override
	public char supplyCharIfAbsentNonDefault(T key, CharSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		int index = findIndex(key);
		if(index < 0) {
			char newValue = valueProvider.getAsChar();
			if(newValue == getDefaultReturnValue()) return newValue;
			insert(-index-1, key, newValue);
			return newValue;
		}
		char newValue = values[index];
		if(newValue == getDefaultReturnValue()) {
			newValue = valueProvider.getAsChar();
			if(newValue == getDefaultReturnValue()) return newValue;
			values[index] = newValue;
		}
		return newValue;
	}
	
	@Override
	public char computeCharIfPresentNonDefault(T key, ObjectCharUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index < 0 || values[index] == getDefaultReturnValue()) return getDefaultReturnValue();
		char newValue = mappingFunction.applyAsChar(key, values[index]);
		if(newValue == getDefaultReturnValue()) {
			removeIndex(index);
			return newValue;
		}
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public char mergeChar(T key, char value, CharCharUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		char newValue = index < 0 || values[index] == getDefaultReturnValue() ? value : mappingFunction.applyAsChar(values[index], value);
		if(newValue == getDefaultReturnValue()) {
			if(index >= 0)
				removeIndex(index);
		}
		else if(index < 0) insert(-index-1, key, newValue);
		else values[index] = newValue;
		return newValue;
	}
	
	@Override
	public void mergeAllChar(Object2CharMap<T> m, CharCharUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Object2CharMap.Entry<T> entry : getFastIterable(m)) {
			T key = entry.getKey();
			int index = findIndex(key);
			char newValue = index < 0 || values[index] == getDefaultReturnValue() ? entry.getCharValue() : mappingFunction.applyAsChar(values[index], entry.getCharValue());
			if(newValue == getDefaultReturnValue()) {
				if(index >= 0)
					removeIndex(index);
			}
			else if(index < 0) insert(-index-1, key, newValue);
			else values[index] = newValue;
		}
	}
	
	@Override
	public int size() { return size; }
	
	@Override
	public void clear() {
		if(size == 0) return;
		size = 0;
		for(int i = 0,m=keys.length;i<m;i++) {
			if(keys[i] != null) {
				keys[i].index(-1);
				keys[i] = null;
			}
		}
		Arrays.fill(values, (char)0);
	}
	
	@Override
	public boolean trim(int size) {
		int request = Math.max(minCapacity, HashUtil.nextPowerOfTwo((int)Math.ceil(size / loadFactor)));
		if(request >= capacity || this.size >= Math.min((int)Math.ceil(request * loadFactor), request - 1)) return false;
		try {
			rehash(request);
		}
		catch(OutOfMemoryError noMemory) { return false; }
		return true;
	}
	
	@Override
	public void clearAndTrim(int size) {
		int request = Math.max(minCapacity, HashUtil.nextPowerOfTwo((int)Math.ceil(size / loadFactor)));
		if(request >= capacity) {
			clear();
			return;
		}
		capacity = request;
		mask = request-1;
		maxFill = Math.min((int)Math.ceil(capacity * loadFactor), capacity - 1);
		for(int i = 0,m=keys.length;i<m;i++) {
			if(keys[i] != null) keys[i].index(-1);
		}
		keys = new WeakKey[request + 1];
		values = new char[request + 1];
		this.size = 0;
	}
	
	protected int findIndex(Object key) {
		removeStaleEntries();
		Objects.requireNonNull(key);
		int pos = HashUtil.mix(key.hashCode()) & mask;
		WeakKey<T> current = keys[pos];
		if(current != null) {
			if(Objects.equals(key, current.get())) return pos;
			while((current = keys[pos = (++pos & mask)]) != null)
				if(Objects.equals(key, current.get())) return pos;
		}
		return -(pos + 1);
	}
	
	protected char removeIndex(int pos) {
		char value = values[pos];
		keys[pos].index(-1);
		keys[pos] = null;
		values[pos] = (char)0;
		size--;
		onNodeRemoved(pos);
		shiftKeys(pos);
		if(capacity > minCapacity && size < maxFill / 4 && capacity > HashUtil.DEFAULT_MIN_CAPACITY) rehash(capacity / 2);
		return value;
	}
	
	protected void insert(int slot, T key, char value) {
		keys[slot] = new WeakKey<>(key, queue).index(slot);
		values[slot] = value;
		onNodeAdded(slot);
		if(size++ >= maxFill) rehash(HashUtil.arraySize(size+1, loadFactor));
	}
	
	protected void rehash(int newSize) {
		int newMask = newSize - 1;
		WeakKey<T>[] newKeys = new WeakKey[newSize + 1];
		char[] newValues = new char[newSize + 1];
		for(int i = capacity, pos = 0, j = (size);j-- != 0;) {
			while(true) {
				if(--i < 0) throw new ConcurrentModificationException("Map was modified during rehash");
				if(keys[i] != null) break;
			}
			if(newKeys[pos = HashUtil.mix(keys[i].hash()) & newMask] != null)
				while(newKeys[pos = (++pos & newMask)] != null);
			newKeys[pos] = keys[i].index(pos);
			newValues[pos] = values[i];
		}
		capacity = newSize;
		mask = newMask;
		maxFill = Math.min((int)Math.ceil(capacity * loadFactor), capacity - 1);
		keys = newKeys;
		values = newValues;
	}
	
	protected void onNodeAdded(int pos) {
		
	}
	
	protected void onNodeRemoved(int pos) {
		
	}
	
	protected void onNodeMoved(int from, int to) {
		
	}
	
	protected void shiftKeys(int startPos) {
		int slot, last;
		WeakKey<T> current;
		while(true) {
			startPos = ((last = startPos) + 1) & mask;
			while(true){
				if((current = keys[startPos]) == null) {
					keys[last] = null;
					values[last] = (char)0;
					return;
				}
				slot = HashUtil.mix(current.hash()) & mask;
				if(last <= startPos ? (last >= slot || slot > startPos) : (last >= slot && slot > startPos)) break;
				startPos = ++startPos & mask;
			}
			keys[last] = current.index(last);
			values[last] = values[startPos];
			onNodeMoved(startPos, last);
		}
	}
	
	private void removeStaleEntries() {
		for(Object o; (o = queue.poll()) != null;) {
			synchronized(queue) {
				int index = ((WeakKey<T>)o).index();
				if(index == -1) continue;
				removeIndex(index);
			}
		}
	}
	
	protected static class WeakKey<T> extends WeakReference<T> {
		int index;
		int hash;
		
		public WeakKey(T referent, ReferenceQueue<? super T> q) {
			super(referent, q);
			this.hash = Objects.hashCode(referent);
		}
		
		public WeakKey<T> index(int index) {
			this.index = index;
			return this;
		}
		
		public int hash() { return hash; }
		public int index() { return index; }
	}
	
	protected class ValueMapEntry extends MapEntry {
		protected T key;
		protected char value;
		
		public ValueMapEntry(int index) {
			super(index);
			key = keys[index].get();
			value = values[index];
		}
		
		@Override
		public T getKey() {
			return key;
		}

		@Override
		public char getCharValue() {
			return value;
		}
		
		@Override
		public char setValue(char value) {
			this.value = value;
			return super.setValue(value);
		}
	}
	
	protected class MapEntry implements Object2CharMap.Entry<T>, Map.Entry<T, Character> {
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
			return keys[index].get();
		}

		@Override
		public char getCharValue() {
			return values[index];
		}

		@Override
		public char setValue(char value) {
			char oldValue = values[index];
			values[index] = value;
			return oldValue;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Map.Entry) {
				if(obj instanceof Object2CharMap.Entry) {
					Object2CharMap.Entry<T> entry = (Object2CharMap.Entry<T>)obj;
					return Objects.equals(getKey(), entry.getKey()) && getCharValue() == entry.getCharValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return value instanceof Character && Objects.equals(getKey(), key) && getCharValue() == ((Character)value).charValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Objects.hashCode(getKey()) ^ Character.hashCode(getCharValue());
		}
		
		@Override
		public String toString() {
			return Objects.toString(getKey()) + "=" + Character.toString(getCharValue());
		}
	}
	
	private final class MapEntrySet extends AbstractObjectSet<Object2CharMap.Entry<T>> implements Object2CharMap.FastEntrySet<T> {
		@Override
		public ObjectIterator<Object2CharMap.Entry<T>> fastIterator() {
			return new FastEntryIterator();
		}
		
		@Override
		public ObjectIterator<Object2CharMap.Entry<T>> iterator() {
			return new EntryIterator();
		}
		
		@Override
		public void forEach(Consumer<? super Object2CharMap.Entry<T>> action) {
			for(int i = capacity-1;i>=0;i--)
				if(keys[i] != null) action.accept(new ValueMapEntry(i));
		}
		
		@Override
		public void fastForEach(Consumer<? super Object2CharMap.Entry<T>> action) {
			MapEntry entry = new MapEntry();
			for(int i = capacity-1;i>=0;i--) {
				if(keys[i] != null) {
					entry.set(i);
					action.accept(entry);
				}
			}
		}
		
		@Override
		public void forEachIndexed(IntObjectConsumer<Object2CharMap.Entry<T>> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			for(int i = capacity-1, index = 0;i>=0;i--) {
				if(keys[i] != null) action.accept(index++, new ValueMapEntry(i));
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, Object2CharMap.Entry<T>> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			for(int i = capacity-1;i>=0;i--) {
				if(keys[i] != null) action.accept(input, new ValueMapEntry(i));
			}
		}
		
		@Override
		public boolean matchesAny(Predicate<Object2CharMap.Entry<T>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			MapEntry entry = new MapEntry();
			for(int i = capacity-1;i>=0;i--) {
				if(keys[i] != null) {
					entry.set(i);
					if(filter.test(entry)) return true;
				}
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Predicate<Object2CharMap.Entry<T>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			MapEntry entry = new MapEntry();
			for(int i = capacity-1;i>=0;i--) {
				if(keys[i] != null) {
					entry.set(i);
					if(filter.test(entry)) return false;
				}
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Predicate<Object2CharMap.Entry<T>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			MapEntry entry = new MapEntry();
			for(int i = capacity-1;i>=0;i--) {
				if(keys[i] != null) {
					entry.set(i);
					if(!filter.test(entry)) return false;
				}
			}
			return true;
		}
		
		@Override
		public <E> E reduce(E identity, BiFunction<E, Object2CharMap.Entry<T>, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			for(int i = capacity-1;i>=0;i--) {
				if(keys[i] == null) continue;
				state = operator.apply(state, new ValueMapEntry(i));
			}
			return state;
		}
		
		@Override
		public Optional<Object2CharMap.Entry<T>> reduce(ObjectObjectUnaryOperator<Object2CharMap.Entry<T>, Object2CharMap.Entry<T>> operator) {
			Objects.requireNonNull(operator);
			Object2CharMap.Entry<T> state = null;
			boolean empty = true;
			for(int i = capacity-1;i>=0;i--) {
				if(keys[i] == null) continue;
				if(empty) {
					empty = false;
					state = new ValueMapEntry(i);
					continue;
				}
				state = operator.apply(state, new ValueMapEntry(i));
			}
			return empty ? Optional.empty() : Optional.ofNullable(state);
		}
		
		@Override
		public Optional<Object2CharMap.Entry<T>> findFirst(Predicate<Object2CharMap.Entry<T>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return Optional.empty();
			MapEntry entry = new MapEntry();
			for(int i = capacity-1;i>=0;i--) {
				if(keys[i] != null) {
					entry.set(i);
					if(filter.test(entry)) return Optional.ofNullable(entry);
				}
			}
			return Optional.empty();
		}
		
		@Override
		public int count(Predicate<Object2CharMap.Entry<T>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			MapEntry entry = new MapEntry();
			int result = 0;
			for(int i = capacity-1;i>=0;i--) {
				if(keys[i] != null) {
					entry.set(i);
					if(filter.test(entry)) result++;
				}
			}
			return result;
		}
		
		@Override
		public int size() {
			return Reference2CharHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Reference2CharHashMap.this.clear();
		}
		
		@Override
		public boolean contains(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Object2CharMap.Entry) {
					Object2CharMap.Entry<T> entry = (Object2CharMap.Entry<T>)o;
					int index = Reference2CharHashMap.this.findIndex(entry.getKey());
					if(index >= 0) return entry.getCharValue() == Reference2CharHashMap.this.values[index];
				}
				else {
					Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
					int index = Reference2CharHashMap.this.findIndex(entry.getKey());
					if(index >= 0) return Objects.equals(entry.getValue(), Character.valueOf(Reference2CharHashMap.this.values[index]));
				}
			}
			return false;
		}
		
		@Override
		public boolean remove(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Object2CharMap.Entry) {
					Object2CharMap.Entry<T> entry = (Object2CharMap.Entry<T>)o;
					return Reference2CharHashMap.this.remove(entry.getKey(), entry.getCharValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
				return Reference2CharHashMap.this.remove(entry.getKey(), entry.getValue());
			}
			return false;
		}
	}
	
	private final class KeySet extends AbstractObjectSet<T> {
		@Override
		public boolean contains(Object e) {
			return containsKey(e);
		}
		
		@Override
		public boolean remove(Object o) {
			int oldSize = size;
			Reference2CharHashMap.this.remove(o);
			return size != oldSize;
		}
		
		@Override
		public boolean add(T o) {
			throw new UnsupportedOperationException();
		}

		@Override
		public ObjectIterator<T> iterator() {
			return new KeyIterator();
		}
		
		@Override
		public int size() {
			return Reference2CharHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Reference2CharHashMap.this.clear();
		}
		
		@Override
		public KeySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(Consumer<? super T> action) {
			for(int i = capacity-1;i>=0;i--)
				if(keys[i] != null) action.accept(keys[i].get());
		}
		
		@Override
		public void forEachIndexed(IntObjectConsumer<T> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			for(int i = capacity-1, index = 0;i>=0;i--) {
				if(keys[i] != null) action.accept(index++, keys[i].get());
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, T> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			for(int i = capacity-1;i>=0;i--) {
				if(keys[i] != null) action.accept(input, keys[i].get());
			}
		}
		
		@Override
		public boolean matchesAny(Predicate<T> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			for(int i = capacity-1;i>=0;i--) {
				if(keys[i] != null && filter.test(keys[i].get())) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Predicate<T> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			for(int i = capacity-1;i>=0;i--) {
				if(keys[i] != null && filter.test(keys[i].get())) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Predicate<T> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			for(int i = capacity-1;i>=0;i--) {
				if(keys[i] != null && !filter.test(keys[i].get())) return false;
			}
			return true;
		}
		
		@Override
		public <E> E reduce(E identity, BiFunction<E, T, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			for(int i = capacity-1;i>=0;i--) {
				if(keys[i] == null) continue;
				state = operator.apply(state, keys[i].get());
			}
			return state;
		}
		
		@Override
		public Optional<T> reduce(ObjectObjectUnaryOperator<T, T> operator) {
			Objects.requireNonNull(operator);
			T state = null;
			boolean empty = true;
			for(int i = capacity-1;i>=0;i--) {
				if(keys[i] == null) continue;
				if(empty) {
					empty = false;
					state = keys[i].get();
					continue;
				}
				state = operator.apply(state, keys[i].get());
			}
			return empty ? Optional.empty() : Optional.ofNullable(state);
		}
		
		@Override
		public Optional<T> findFirst(Predicate<T> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return Optional.empty();
			for(int i = capacity-1;i>=0;i--) {
				if(keys[i] != null && filter.test(keys[i].get())) return Optional.ofNullable(keys[i].get());
			}
			return Optional.empty();
		}
		
		@Override
		public int count(Predicate<T> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			int result = 0;
			for(int i = capacity-1;i>=0;i--) {
				if(keys[i] != null && filter.test(keys[i].get())) result++;
			}
			return result;
		}
	}
	
	private class Values extends AbstractCharCollection {
		@Override
		public boolean contains(char e) {
			return containsValue(e);
		}
		
		@Override
		public boolean add(char o) {
			throw new UnsupportedOperationException();
		}

		@Override
		public CharIterator iterator() {
			return new ValueIterator();
		}
		
		@Override
		public int size() {
			return Reference2CharHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Reference2CharHashMap.this.clear();
		}
		
		@Override
		public void forEach(CharConsumer action) {
			for(int i = capacity-1;i>=0;i--)
				if(keys[i] != null) action.accept(values[i]);
		}
		
		@Override
		public void forEachIndexed(IntCharConsumer action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			for(int i = capacity-1, index = 0;i>=0;i--) {
				if(keys[i] != null) action.accept(index++, values[i]);
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectCharConsumer<E> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			for(int i = capacity-1;i>=0;i--) {
				if(keys[i] != null) action.accept(input, values[i]);
			}
		}
		
		@Override
		public boolean matchesAny(CharPredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			for(int i = capacity-1;i>=0;i--) {
				if(keys[i] != null && filter.test(values[i])) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(CharPredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			for(int i = capacity-1;i>=0;i--) {
				if(keys[i] != null && filter.test(values[i])) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(CharPredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			for(int i = capacity-1;i>=0;i--) {
				if(keys[i] != null && !filter.test(values[i])) return false;
			}
			return true;
		}
		
		@Override
		public char reduce(char identity, CharCharUnaryOperator operator) {
			Objects.requireNonNull(operator);
			char state = identity;
			for(int i = capacity-1;i>=0;i--) {
				if(keys[i] == null) continue;
				state = operator.applyAsChar(state, values[i]);
			}
			return state;
		}
		
		@Override
		public OptionalChar reduce(CharCharUnaryOperator operator) {
			Objects.requireNonNull(operator);
			char state = (char)0;
			boolean empty = true;
			for(int i = capacity-1;i>=0;i--) {
				if(keys[i] == null) continue;
				if(empty) {
					empty = false;
					state = values[i];
					continue;
				}
				state = operator.applyAsChar(state, values[i]);
			}
			return empty ? OptionalChar.empty() : OptionalChar.of(state);
		}
		
		@Override
		public OptionalChar findFirst(CharPredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return OptionalChar.empty();
			for(int i = capacity-1;i>=0;i--) {
				if(keys[i] != null && filter.test(values[i])) return OptionalChar.of(values[i]);
			}
			return OptionalChar.empty();
		}
		
		@Override
		public int count(CharPredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			int result = 0;
			for(int i = capacity-1;i>=0;i--) {
				if(keys[i] != null && filter.test(values[i])) result++;
			}
			return result;
		}
	}
	
	private class FastEntryIterator extends MapIterator implements ObjectIterator<Object2CharMap.Entry<T>> {
		MapEntry entry = new MapEntry();
		@Override
		public Object2CharMap.Entry<T> next() {
			entry.index = nextEntry();
			return entry;
		}
	}
	
	private class EntryIterator extends MapIterator implements ObjectIterator<Object2CharMap.Entry<T>> {
		MapEntry entry;
		@Override
		public Object2CharMap.Entry<T> next() {
			return entry = new ValueMapEntry(nextEntry());
		}
		
		@Override
		public void remove() {
			super.remove();
			entry.index = -1;
		}
	}
	
	private class KeyIterator extends MapIterator implements ObjectIterator<T> {
		@Override
		public T next() {
			return keys[nextEntry()].get();
		}
	}
	
	private class ValueIterator extends MapIterator implements CharIterator {
		@Override
		public char nextChar() {
			return values[nextEntry()];
		}
	}
	
	private class MapIterator {
		int pos = capacity;
		int returnedPos = -1;
		int lastReturned = -1;
		int nextIndex = Integer.MIN_VALUE;
		WeakKey<T>[] wrapped = null;
		int wrappedIndex = 0;
		
		public boolean hasNext() {
			if(nextIndex == Integer.MIN_VALUE) {
				while(true) {
					if(--pos < 0) {
						if(wrapped == null || wrappedIndex <= -pos - 1) break;
						nextIndex = -pos - 1;
						break;
					}
					if(keys[pos] != null){
						nextIndex = pos;
						break;
					}
				}
			}
			return nextIndex != Integer.MIN_VALUE;
		}
		
		public int nextEntry() {
			if(!hasNext()) throw new NoSuchElementException();
			returnedPos = pos;
			if(nextIndex < 0){
				lastReturned = Integer.MAX_VALUE;
				int value = wrapped[nextIndex].index();
				if(value < 0) throw new IllegalStateException("Entry ["+nextIndex+"] was removed during Iteration");
				nextIndex = Integer.MIN_VALUE;
				return value;
			}
			int value = (lastReturned = nextIndex);
			nextIndex = Integer.MIN_VALUE;
			return value;
		}
		
		public void remove() {
			if(lastReturned == -1) throw new IllegalStateException();
			if(returnedPos >= 0) shiftKeys(returnedPos);
			else {
				Reference2CharHashMap.this.remove(wrapped[-returnedPos - 1].get());
				lastReturned = -1;
				return;
			}
			size--;
			lastReturned = -1;
		}
		
		private void shiftKeys(int startPos) {
			int slot, last;
			WeakKey<T> current;
			while(true) {
				startPos = ((last = startPos) + 1) & mask;
				while(true){
					if((current = keys[startPos]) == null) {
						keys[last] = null;
						values[last] = (char)0;
						return;
					}
					slot = HashUtil.mix(current.hash()) & mask;
					if(last <= startPos ? (last >= slot || slot > startPos) : (last >= slot && slot > startPos)) break;
					startPos = ++startPos & mask;
				}
				if(startPos < last) addWrapper(keys[startPos]);
				keys[last] = current.index(last);
				values[last] = values[startPos];
			}
		}
		
		private void addWrapper(WeakKey<T> value) {
			if(wrapped == null) wrapped = new WeakKey[2];
			else if(wrappedIndex >= wrapped.length) {
				WeakKey<T>[] newArray = new WeakKey[wrapped.length * 2];
				System.arraycopy(wrapped, 0, newArray, 0, wrapped.length);
				wrapped = newArray;
			}
			wrapped[wrappedIndex++] = value;
		}
	}
}