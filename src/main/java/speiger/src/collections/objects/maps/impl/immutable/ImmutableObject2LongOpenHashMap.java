package speiger.src.collections.objects.maps.impl.immutable;

import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.BiFunction;

import speiger.src.collections.objects.functions.consumer.ObjectLongConsumer;
import speiger.src.collections.objects.functions.function.Object2LongFunction;
import speiger.src.collections.objects.functions.function.ObjectLongUnaryOperator;
import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.maps.interfaces.Object2LongOrderedMap;
import speiger.src.collections.objects.maps.abstracts.AbstractObject2LongMap;
import speiger.src.collections.objects.maps.interfaces.Object2LongMap;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
import speiger.src.collections.objects.utils.maps.Object2LongMaps;
import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;
import speiger.src.collections.objects.functions.function.Object2BooleanFunction;
import speiger.src.collections.longs.functions.function.Long2BooleanFunction;
import speiger.src.collections.longs.collections.AbstractLongCollection;
import speiger.src.collections.longs.collections.LongCollection;
import speiger.src.collections.longs.collections.LongIterator;
import speiger.src.collections.longs.functions.LongSupplier;
import speiger.src.collections.longs.functions.function.LongLongUnaryOperator;
import speiger.src.collections.longs.functions.LongConsumer;
import speiger.src.collections.longs.lists.LongListIterator;
import speiger.src.collections.longs.utils.LongArrays;
import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.HashUtil;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Type Specific Custom implementation of the HashMap
 * Instead of using Wrapper Object Arrays for storing keys and values there is dedicated arrays for storing keys and values.
 * Extra to that there is a couple quality of life functions provided
 * @param <T> the type of elements maintained by this Collection
 */
public class ImmutableObject2LongOpenHashMap<T> extends AbstractObject2LongMap<T> implements Object2LongOrderedMap<T>
{
	/** The Backing keys array */
	protected transient T[] keys;
	/** The Backing values array */
	protected transient long[] values;
	/** The Backing array for links between nodes. Left 32 Bits => Previous Entry, Right 32 Bits => Next Entry */
	protected transient long[] links;
	/** If a null value is present */
	protected transient boolean containsNull;
	/** Index of the Null Value */
	protected transient int nullIndex;
	/** Max Index that is allowed to be searched through nullIndex - 1 */
	protected transient int mask;
	/** The First Index in the Map */
	protected int firstIndex = -1;
	/** The Last Index in the Map */
	protected int lastIndex = -1;
	/** EntrySet cache */
	protected transient FastEntrySet<T> entrySet;
	/** KeySet cache */
	protected transient ObjectSet<T> keySet;
	/** Values cache */
	protected transient LongCollection valuesC;
	
	/** Amount of Elements stored in the HashMap */
	protected int size;
	
	/**
	 * Helper constructor for copying the Map
	 */
	protected ImmutableObject2LongOpenHashMap() {}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public ImmutableObject2LongOpenHashMap(T[] keys, Long[] values) {
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
	public ImmutableObject2LongOpenHashMap(T[] keys, Long[] values, float loadFactor) {
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		init(keys, LongArrays.unwrap(values), 0, keys.length, loadFactor);
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public ImmutableObject2LongOpenHashMap(T[] keys, long[] values) {
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
	public ImmutableObject2LongOpenHashMap(T[] keys, long[] values, float loadFactor) {
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		init(keys, values, 0, keys.length, loadFactor);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 */
	public ImmutableObject2LongOpenHashMap(Map<? extends T, ? extends Long> map) {
		this(map, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public ImmutableObject2LongOpenHashMap(Map<? extends T, ? extends Long> map, float loadFactor) {
		T[] keys = (T[])new Object[map.size()];
		long[] values = new long[keys.length];
		int index = 0;
		for(Map.Entry<? extends T, ? extends Long> entry : map.entrySet()) {
			keys[index] = entry.getKey();
			values[index] = entry.getValue().longValue();
			index++;
		}
		init(keys, values, 0, index, loadFactor);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 */
	public ImmutableObject2LongOpenHashMap(Object2LongMap<T> map) {
		this(map, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
 	 */
	public ImmutableObject2LongOpenHashMap(Object2LongMap<T> map, float loadFactor) {
		T[] keys = (T[])new Object[map.size()];
		long[] values = new long[keys.length];
		int index = 0;
		for(Object2LongMap.Entry<T> entry : Object2LongMaps.fastIterable(map)) {
			keys[index] = entry.getKey();
			values[index] = entry.getLongValue();
			index++;
		}
		init(keys, values, 0, index, loadFactor);
	}
	
	protected void init(T[] a, long[] b, int offset, int length, float loadFactor) {
		SanityChecks.checkArrayCapacity(a.length, offset, length);
		int newSize = HashUtil.arraySize(length+1, loadFactor);
		int newMask = newSize - 1;
		T[] newKeys = (T[])new Object[newSize + 1];
		long[] newValues = new long[newSize + 1];
		long[] newLinks = new long[newSize + 1];
		int prev = -1;
		for(int i = offset,m=offset+length;i<m;i++)
		{
			T o = a[i];
			if(o == null) {
				if(!containsNull) {
					size++;
					if(prev != -1) {
						newLinks[prev] ^= ((newLinks[prev] ^ (newSize & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
						newLinks[newSize] ^= ((newLinks[newSize] ^ ((prev & 0xFFFFFFFFL) << 32)) & 0xFFFFFFFF00000000L);
						prev = newSize;
					}
					else {
						prev = firstIndex = newSize;
						newLinks[newSize] = -1L;
					}
				}
				containsNull = true;
				newValues[newSize] = b[i];
				continue;
			}
			boolean found = true;
			int pos = HashUtil.mix(Objects.hashCode(o)) & newMask;
			T current = newKeys[pos];
			if(current != null) {
				if(Objects.equals(current, o)) {
					newValues[pos] = b[i];
					continue;
				}
				while((current = newKeys[pos = (++pos & newMask)]) != null) {
					if(Objects.equals(current, o)) {
						found = false;
						newValues[pos] = b[i];
						break;
					}
				}
			}
			if(found) {
				size++;
				newKeys[pos] = o;
				newValues[pos] = b[i];
				if(prev != -1) {
					newLinks[prev] ^= ((newLinks[prev] ^ (pos & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
					newLinks[pos] ^= ((newLinks[pos] ^ ((prev & 0xFFFFFFFFL) << 32)) & 0xFFFFFFFF00000000L);
					prev = pos;
				}
				else {
					prev = firstIndex = pos;
					newLinks[pos] = -1L;
				}
			}
		}
		nullIndex = newSize;
		mask = newMask;
		keys = newKeys;
		values = newValues;
		links = newLinks;
		lastIndex = prev;
		if(prev != -1) newLinks[prev] |= 0xFFFFFFFFL;
	}
	
	@Override
	public long put(T key, long value) { throw new UnsupportedOperationException(); }
	@Override
	public long putIfAbsent(T key, long value) { throw new UnsupportedOperationException(); }	
	@Override
	public long addTo(T key, long value) { throw new UnsupportedOperationException(); }
	@Override
	public long subFrom(T key, long value) { throw new UnsupportedOperationException(); }	
	@Override
	public long putAndMoveToFirst(T key, long value) { throw new UnsupportedOperationException(); }
	@Override
	public long putAndMoveToLast(T key, long value) { throw new UnsupportedOperationException(); }
	@Override
	public boolean moveToFirst(T key) { throw new UnsupportedOperationException(); }
	@Override
	public boolean moveToLast(T key) { throw new UnsupportedOperationException(); }
	@Override
	public long getAndMoveToFirst(T key) { throw new UnsupportedOperationException(); }
	@Override
	public long getAndMoveToLast(T key) { throw new UnsupportedOperationException(); }
	
	@Override
	public boolean containsKey(Object key) {
		return findIndex(key) >= 0;
	}
	
	@Override
	public boolean containsValue(long value) {
		int index = firstIndex;
		while(index != -1) {
			if(values[index] == value) return true;
			index = (int)links[index];
		}
		return false;
	}
	
	@Override
	@Deprecated
	public boolean containsValue(Object value) {
		int index = firstIndex;
		while(index != -1) {
			if((value == null && values[index] == getDefaultReturnValue()) || Objects.equals(value, Long.valueOf(values[index]))) return true;
			index = (int)links[index];
		}
		return false;
	}
	
	@Override
	public long rem(T key) { throw new UnsupportedOperationException(); }
	
	@Override
	public long remOrDefault(T key, long defaultValue) { throw new UnsupportedOperationException(); }
	
	@Override
	@Deprecated
	public Long remove(Object key) { throw new UnsupportedOperationException(); }
	
	@Override
	public boolean remove(T key, long value) { throw new UnsupportedOperationException(); }
	
	@Override
	public boolean remove(Object key, Object value) { throw new UnsupportedOperationException(); }
	
	@Override
	public long getLong(T key) {
		int slot = findIndex(key);
		return slot < 0 ? getDefaultReturnValue() : values[slot];
	}
	
	@Override
	public Long get(Object key) {
		int slot = findIndex(key);
		return Long.valueOf(slot < 0 ? getDefaultReturnValue() : values[slot]);
	}
	
	@Override
	public long getOrDefault(T key, long defaultValue) {
		int slot = findIndex(key);
		return slot < 0 ? defaultValue : values[slot];
	}
	
	@Override
	public T firstKey() {
		if(size == 0) throw new NoSuchElementException();
		return keys[firstIndex];
	}
	
	@Override
	public T pollFirstKey() { throw new UnsupportedOperationException(); }
	
	@Override
	public T lastKey() {
		if(size == 0) throw new NoSuchElementException();
		return keys[lastIndex];
	}
	
	@Override
	public T pollLastKey() { throw new UnsupportedOperationException(); }
	
	@Override
	public long firstLongValue() {
		if(size == 0) throw new NoSuchElementException();
		return values[firstIndex];
	}
	
	@Override
	public long lastLongValue() {
		if(size == 0) throw new NoSuchElementException();
		return values[lastIndex];
	}	

	@Override
	public ObjectSet<Object2LongMap.Entry<T>> object2LongEntrySet() {
		if(entrySet == null) entrySet = new MapEntrySet();
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
	public ImmutableObject2LongOpenHashMap<T> copy() {
		ImmutableObject2LongOpenHashMap<T> map = new ImmutableObject2LongOpenHashMap<>();
		map.mask = mask;
		map.nullIndex = nullIndex;
		map.containsNull = containsNull;
		map.size = size;
		map.keys = Arrays.copyOf(keys, keys.length);
		map.values = Arrays.copyOf(values, values.length);
		map.links = Arrays.copyOf(links, links.length);
		map.firstIndex = firstIndex;
		map.lastIndex = lastIndex;
		return map;
	}
	
	@Override
	public void forEach(ObjectLongConsumer<T> action) {
		int index = firstIndex;
		while(index != -1){
			action.accept(keys[index], values[index]);
			index = (int)links[index];
		}
	}
	
	@Override
	public boolean replace(T key, long oldValue, long newValue) { throw new UnsupportedOperationException(); }
	
	@Override
	public long replace(T key, long value) { throw new UnsupportedOperationException(); }
	
	@Override
	public long computeLong(T key, ObjectLongUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
	
	@Override
	public long computeLongIfAbsent(T key, Object2LongFunction<T> mappingFunction) { throw new UnsupportedOperationException(); }
	
	@Override
	public long supplyLongIfAbsent(T key, LongSupplier valueProvider) { throw new UnsupportedOperationException(); }
	
	@Override
	public long computeLongIfPresent(T key, ObjectLongUnaryOperator<T> mappingFunction) { throw new UnsupportedOperationException(); }
	
	@Override
	public long mergeLong(T key, long value, LongLongUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
	
	@Override
	public void mergeAllLong(Object2LongMap<T> m, LongLongUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
	
	@Override
	public int size() { return size; }
	
	@Override
	public void clear() { throw new UnsupportedOperationException(); }
	
	protected int findIndex(Object key) {
		if(key == null) return containsNull ? nullIndex : -(nullIndex + 1);
		int pos = HashUtil.mix(key.hashCode()) & mask;
		T current = keys[pos];
		if(current != null) {
			if(Objects.equals(key, current)) return pos;
			while((current = keys[pos = (++pos & mask)]) != null)
				if(Objects.equals(key, current)) return pos;
		}
		return -(pos + 1);
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
		public long setValue(long value) { throw new UnsupportedOperationException(); }
		
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
	
	private class MapEntrySet extends AbstractObjectSet<Object2LongMap.Entry<T>> implements Object2LongOrderedMap.FastOrderedSet<T> {
		@Override
		public boolean addAndMoveToFirst(Object2LongMap.Entry<T> o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Object2LongMap.Entry<T> o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean moveToFirst(Object2LongMap.Entry<T> o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean moveToLast(Object2LongMap.Entry<T> o) { throw new UnsupportedOperationException(); }
		
		@Override
		public Object2LongMap.Entry<T> first() {
			return new BasicEntry<>(firstKey(), firstLongValue());
		}
		
		@Override
		public Object2LongMap.Entry<T> last() {
			return new BasicEntry<>(lastKey(), lastLongValue());
		}
		
		@Override
		public Object2LongMap.Entry<T> pollFirst() { throw new UnsupportedOperationException(); }
		
		@Override
		public Object2LongMap.Entry<T> pollLast() { throw new UnsupportedOperationException(); }
		
		@Override
		public ObjectBidirectionalIterator<Object2LongMap.Entry<T>> iterator() {
			return new EntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Object2LongMap.Entry<T>> iterator(Object2LongMap.Entry<T> fromElement) {
			return new EntryIterator(fromElement.getKey());
		}
		
		@Override
		public ObjectBidirectionalIterator<Object2LongMap.Entry<T>> fastIterator() {
			return new FastEntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Object2LongMap.Entry<T>> fastIterator(T fromElement) {
			return new FastEntryIterator(fromElement);
		}
		
		@Override
		public MapEntrySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(Consumer<? super Object2LongMap.Entry<T>> action) {
			int index = firstIndex;
			while(index != -1){
				action.accept(new BasicEntry<>(keys[index], values[index]));
				index = (int)links[index];
			}
		}
		
		@Override
		public void fastForEach(Consumer<? super Object2LongMap.Entry<T>> action) {
			BasicEntry<T> entry = new BasicEntry<>();
			int index = firstIndex;
			while(index != -1){
				entry.set(keys[index], values[index]);
				action.accept(entry);
				index = (int)links[index];
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, Object2LongMap.Entry<T>> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			int index = firstIndex;
			while(index != -1) {
				action.accept(input, new BasicEntry<>(keys[index], values[index]));
				index = (int)links[index];
			}
		}
		
		@Override
		public boolean matchesAny(Object2BooleanFunction<Object2LongMap.Entry<T>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			BasicEntry<T> entry = new BasicEntry<>();
			int index = firstIndex;
			while(index != -1) {
				entry.set(keys[index], values[index]);
				if(filter.getBoolean(entry)) return true;
				index = (int)links[index];
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Object2BooleanFunction<Object2LongMap.Entry<T>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			BasicEntry<T> entry = new BasicEntry<>();
			int index = firstIndex;
			while(index != -1) {
				entry.set(keys[index], values[index]);
				if(filter.getBoolean(entry)) return false;
				index = (int)links[index];
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Object2BooleanFunction<Object2LongMap.Entry<T>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			BasicEntry<T> entry = new BasicEntry<>();
			int index = firstIndex;
			while(index != -1) {
				entry.set(keys[index], values[index]);
				if(!filter.getBoolean(entry)) return false;
				index = (int)links[index];
			}
			return true;
		}
		
		@Override
		public <E> E reduce(E identity, BiFunction<E, Object2LongMap.Entry<T>, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			int index = firstIndex;
			while(index != -1) {
				state = operator.apply(state, new BasicEntry<>(keys[index], values[index]));
				index = (int)links[index];
			}
			return state;
		}
		
		@Override
		public Object2LongMap.Entry<T> reduce(ObjectObjectUnaryOperator<Object2LongMap.Entry<T>, Object2LongMap.Entry<T>> operator) {
			Objects.requireNonNull(operator);
			Object2LongMap.Entry<T> state = null;
			boolean empty = true;
			int index = firstIndex;
			while(index != -1) {
				if(empty) {
					empty = false;
					state = new BasicEntry<>(keys[index], values[index]);
					index = (int)links[index];
					continue;
				}
				state = operator.apply(state, new BasicEntry<>(keys[index], values[index]));
				index = (int)links[index];
			}
			return state;
		}
		
		@Override
		public Object2LongMap.Entry<T> findFirst(Object2BooleanFunction<Object2LongMap.Entry<T>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return null;
			BasicEntry<T> entry = new BasicEntry<>();
			int index = firstIndex;
			while(index != -1) {
				entry.set(keys[index], values[index]);
				if(filter.getBoolean(entry)) return entry;
				index = (int)links[index];
			}
			return null;
		}
		
		@Override
		public int count(Object2BooleanFunction<Object2LongMap.Entry<T>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			BasicEntry<T> entry = new BasicEntry<>();
			int result = 0;
			int index = firstIndex;
			while(index != -1) {
				entry.set(keys[index], values[index]);
				if(filter.getBoolean(entry)) result++;
				index = (int)links[index];
			}
			return result;
		}
		
		@Override
		@Deprecated
		public boolean contains(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Object2LongMap.Entry) {
					Object2LongMap.Entry<T> entry = (Object2LongMap.Entry<T>)o;
					int index = ImmutableObject2LongOpenHashMap.this.findIndex(entry.getKey());
					if(index >= 0) return entry.getLongValue() == ImmutableObject2LongOpenHashMap.this.values[index];
				}
				else {
					Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
					int index = ImmutableObject2LongOpenHashMap.this.findIndex(entry.getKey());
					if(index >= 0) return Objects.equals(entry.getValue(), Long.valueOf(ImmutableObject2LongOpenHashMap.this.values[index]));
				}
			}
			return false;
		}
		
		@Override
		@Deprecated
		public boolean remove(Object o) { throw new UnsupportedOperationException(); }
		
		@Override
		public int size() {
			return ImmutableObject2LongOpenHashMap.this.size();
		}
		
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
	}
	
	private final class KeySet extends AbstractObjectSet<T> implements ObjectOrderedSet<T> {
		@Override
		@Deprecated
		public boolean contains(Object e) {
			return containsKey(e);
		}
		
		@Override
		public boolean remove(Object o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean add(T o) {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean addAndMoveToFirst(T o) { throw new UnsupportedOperationException(); }

		@Override
		public boolean addAndMoveToLast(T o) { throw new UnsupportedOperationException(); }

		@Override
		public boolean moveToFirst(T o) { throw new UnsupportedOperationException(); }

		@Override
		public boolean moveToLast(T o) { throw new UnsupportedOperationException(); }
		
		@Override
		public ObjectListIterator<T> iterator() {
			return new KeyIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<T> iterator(T fromElement) {
			return new KeyIterator(fromElement);
		}
		
		@Override
		public KeySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public int size() {
			return ImmutableObject2LongOpenHashMap.this.size();
		}
		
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		
		@Override
		public T first() {
			return firstKey();
		}
		
		@Override
		public T pollFirst() { throw new UnsupportedOperationException(); }

		@Override
		public T last() {
			return lastKey();
		}

		@Override
		public T pollLast() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(Consumer<? super T> action) {
			int index = firstIndex;
			while(index != -1){
				action.accept(keys[index]);
				index = (int)links[index];
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, T> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			int index = firstIndex;
			while(index != -1){
				action.accept(input, keys[index]);
				index = (int)links[index];
			}
		}
		
		@Override
		public boolean matchesAny(Object2BooleanFunction<T> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			int index = firstIndex;
			while(index != -1){
				if(filter.getBoolean(keys[index])) return true;
				index = (int)links[index];
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Object2BooleanFunction<T> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			int index = firstIndex;
			while(index != -1){
				if(filter.getBoolean(keys[index])) return false;
				index = (int)links[index];
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Object2BooleanFunction<T> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			int index = firstIndex;
			while(index != -1){
				if(!filter.getBoolean(keys[index])) return false;
				index = (int)links[index];
			}
			return true;
		}
		
		@Override
		public <E> E reduce(E identity, BiFunction<E, T, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			int index = firstIndex;
			while(index != -1) {
				state = operator.apply(state, keys[index]);
				index = (int)links[index];
			}
			return state;
		}
		
		@Override
		public T reduce(ObjectObjectUnaryOperator<T, T> operator) {
			Objects.requireNonNull(operator);
			T state = null;
			boolean empty = true;
			int index = firstIndex;
			while(index != -1) {
				if(empty) {
					empty = false;
					state = keys[index];
					index = (int)links[index];
					continue;
				}
				state = operator.apply(state, keys[index]);
				index = (int)links[index];
			}
			return state;
		}
		
		@Override
		public T findFirst(Object2BooleanFunction<T> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return null;
			int index = firstIndex;
			while(index != -1){
				if(filter.getBoolean(keys[index])) return keys[index];
				index = (int)links[index];
			}
			return null;
		}
		
		@Override
		public int count(Object2BooleanFunction<T> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			int index = firstIndex;
			int result = 0;
			while(index != -1){
				if(filter.getBoolean(keys[index])) result++;
				index = (int)links[index];
			}
			return result;
		}
	}
	
	private class Values extends AbstractLongCollection {
		@Override
		public boolean contains(long e) {
			return containsValue(e);
		}
		
		@Override
		public boolean add(long o) {
			throw new UnsupportedOperationException();
		}

		@Override
		public LongIterator iterator() {
			return new ValueIterator();
		}
		
		@Override
		public int size() {
			return ImmutableObject2LongOpenHashMap.this.size();
		}
		
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(LongConsumer action) {
			int index = firstIndex;
			while(index != -1){
				action.accept(values[index]);
				index = (int)links[index];
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectLongConsumer<E> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			int index = firstIndex;
			while(index != -1){
				action.accept(input, values[index]);
				index = (int)links[index];
			}
		}
		
		@Override
		public boolean matchesAny(Long2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			int index = firstIndex;
			while(index != -1){
				if(filter.get(values[index])) return true;
				index = (int)links[index];
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Long2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			int index = firstIndex;
			while(index != -1){
				if(filter.get(values[index])) return false;
				index = (int)links[index];
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Long2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			int index = firstIndex;
			while(index != -1){
				if(!filter.get(values[index])) return false;
				index = (int)links[index];
			}
			return true;
		}
		
		@Override
		public long reduce(long identity, LongLongUnaryOperator operator) {
			Objects.requireNonNull(operator);
			long state = identity;
			int index = firstIndex;
			while(index != -1) {
				state = operator.applyAsLong(state, values[index]);
				index = (int)links[index];
			}
			return state;
		}
		
		@Override
		public long reduce(LongLongUnaryOperator operator) {
			Objects.requireNonNull(operator);
			long state = 0L;
			boolean empty = true;
			int index = firstIndex;
			while(index != -1) {
				if(empty) {
					empty = false;
					state = values[index];
					index = (int)links[index];
					continue;
				}
				state = operator.applyAsLong(state, values[index]);
				index = (int)links[index];
			}
			return state;
		}
		
		@Override
		public long findFirst(Long2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0L;
			int index = firstIndex;
			while(index != -1){
				if(filter.get(values[index])) return values[index];
				index = (int)links[index];
			}
			return 0L;
		}
		
		@Override
		public int count(Long2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			int result = 0;
			int index = firstIndex;
			while(index != -1){
				if(filter.get(values[index])) result++;
				index = (int)links[index];
			}
			return result;
		}
	}
	
	private class FastEntryIterator extends MapIterator implements ObjectListIterator<Object2LongMap.Entry<T>> {
		MapEntry entry = new MapEntry();
		
		public FastEntryIterator() {}
		public FastEntryIterator(T from) {
			super(from);
		}
		
		@Override
		public Object2LongMap.Entry<T> next() {
			entry.index = nextEntry();
			return entry;
		}
		
		@Override
		public Object2LongMap.Entry<T> previous() {
			entry.index = previousEntry();
			return entry;
		}
		
		@Override
		public void set(Object2LongMap.Entry<T> entry) { throw new UnsupportedOperationException(); }

		@Override
		public void add(Object2LongMap.Entry<T> entry) { throw new UnsupportedOperationException(); }
	}
	
	private class EntryIterator extends MapIterator implements ObjectListIterator<Object2LongMap.Entry<T>> {
		
		public EntryIterator() {}
		public EntryIterator(T from) {
			super(from);
		}
		
		@Override
		public Object2LongMap.Entry<T> next() {
			return new MapEntry(nextEntry());
		}
		
		@Override
		public Object2LongMap.Entry<T> previous() {
			return new MapEntry(previousEntry());
		}
		
		@Override
		public void remove() { throw new UnsupportedOperationException(); }

		@Override
		public void set(Object2LongMap.Entry<T> entry) { throw new UnsupportedOperationException(); }

		@Override
		public void add(Object2LongMap.Entry<T> entry) { throw new UnsupportedOperationException(); }
	}
	
	private class KeyIterator extends MapIterator implements ObjectListIterator<T> {
		
		public KeyIterator() {}
		public KeyIterator(T from) {
			super(from);
		}
		
		@Override
		public T previous() {
			return keys[previousEntry()];
		}

		@Override
		public T next() {
			return keys[nextEntry()];
		}
		
		@Override
		public void set(T e) { throw new UnsupportedOperationException(); }
		@Override
		public void add(T e) { throw new UnsupportedOperationException(); }
	}
	
	private class ValueIterator extends MapIterator implements LongListIterator {
		public ValueIterator() {}
		
		@Override
		public long previousLong() {
			return values[previousEntry()];
		}

		@Override
		public long nextLong() {
			return values[nextEntry()];
		}

		@Override
		public void set(long e) { throw new UnsupportedOperationException(); }

		@Override
		public void add(long e) { throw new UnsupportedOperationException(); }
		
	}
	
	private class MapIterator {
		int previous = -1;
		int next = -1;
		int current = -1;
		int index = 0;
		
		MapIterator() {
			next = firstIndex;
		}
		
		MapIterator(T from) {
			if(from == null) {
				if(containsNull) {
					next = (int) links[nullIndex];
					previous = nullIndex;
				}
				else throw new NoSuchElementException("The null element is not in the set");
			}
			else if(keys[lastIndex] == from) {
				previous = lastIndex;
				index = size;
			}
			else {
				int pos = HashUtil.mix(Objects.hashCode(from)) & mask;
				while(keys[pos] != null) {
					if(Objects.equals(keys[pos], from)) {
						next = (int)links[pos];
						previous = pos;
						break;
					}
					pos = ++pos & mask;
				}
				if(previous == -1 && next == -1)
					throw new NoSuchElementException("The element was not found");
			}
		}
		
		public boolean hasNext() {
			return next != -1;
		}

		public boolean hasPrevious() {
			return previous != -1;
		}
		
		public int nextIndex() {
			ensureIndexKnown();
			return index;
		}
		
		public int previousIndex() {
			ensureIndexKnown();
			return index - 1;
		}
		
		public void remove() { throw new UnsupportedOperationException(); }
		
		public int previousEntry() {
			if(!hasPrevious()) throw new NoSuchElementException();
			current = previous;
			previous = (int)(links[current] >> 32);
			next = current;
			if(index >= 0) index--;
			return current;
		}
		
		public int nextEntry() {
			if(!hasNext()) throw new NoSuchElementException();
			current = next;
			next = (int)(links[current]);
			previous = current;
			if(index >= 0) index++;
			return current;
		}
		
		private void ensureIndexKnown() {
			if(index == -1) {
				if(previous == -1) {
					index = 0;
				}
				else if(next == -1) {
					index = size;
				}
				else {
					index = 1;
					for(int pos = firstIndex;pos != previous;pos = (int)links[pos], index++);
				}
			}
		}
	}
}