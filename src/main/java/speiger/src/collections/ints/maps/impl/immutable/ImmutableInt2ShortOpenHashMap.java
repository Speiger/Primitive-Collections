package speiger.src.collections.ints.maps.impl.immutable;

import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.IntPredicate;

import speiger.src.collections.ints.collections.IntBidirectionalIterator;
import speiger.src.collections.ints.functions.IntConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectIntConsumer;
import speiger.src.collections.ints.functions.consumer.IntIntConsumer;
import speiger.src.collections.ints.functions.consumer.IntObjectConsumer;
import speiger.src.collections.ints.functions.consumer.IntShortConsumer;
import speiger.src.collections.ints.functions.function.Int2ShortFunction;
import speiger.src.collections.ints.functions.function.IntShortUnaryOperator;
import speiger.src.collections.ints.functions.function.IntIntUnaryOperator;
import speiger.src.collections.ints.lists.IntListIterator;
import speiger.src.collections.ints.maps.interfaces.Int2ShortOrderedMap;
import speiger.src.collections.ints.maps.abstracts.AbstractInt2ShortMap;
import speiger.src.collections.ints.maps.interfaces.Int2ShortMap;
import speiger.src.collections.ints.sets.IntOrderedSet;
import speiger.src.collections.ints.utils.IntArrays;
import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectShortConsumer;

import speiger.src.collections.shorts.functions.function.ShortPredicate;
import speiger.src.collections.ints.sets.AbstractIntSet;
import speiger.src.collections.shorts.collections.AbstractShortCollection;
import speiger.src.collections.shorts.collections.ShortCollection;
import speiger.src.collections.shorts.collections.ShortIterator;
import speiger.src.collections.shorts.functions.ShortSupplier;
import speiger.src.collections.shorts.functions.function.ShortShortUnaryOperator;
import speiger.src.collections.shorts.functions.ShortConsumer;
import speiger.src.collections.shorts.lists.ShortListIterator;
import speiger.src.collections.shorts.utils.ShortArrays;
import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;
import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
import speiger.src.collections.utils.HashUtil;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Type Specific Custom implementation of the HashMap
 * Instead of using Wrapper Object Arrays for storing keys and values there is dedicated arrays for storing keys and values.
 * Extra to that there is a couple quality of life functions provided
 */
public class ImmutableInt2ShortOpenHashMap extends AbstractInt2ShortMap implements Int2ShortOrderedMap
{
	/** The Backing keys array */
	protected transient int[] keys;
	/** The Backing values array */
	protected transient short[] values;
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
	protected transient FastOrderedSet entrySet;
	/** KeySet cache */
	protected transient IntOrderedSet keySet;
	/** Values cache */
	protected transient ShortCollection valuesC;
	
	/** Amount of Elements stored in the HashMap */
	protected int size;
	
	/**
	 * Helper constructor for copying the Map
	 */
	protected ImmutableInt2ShortOpenHashMap() {}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public ImmutableInt2ShortOpenHashMap(Integer[] keys, Short[] values) {
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
	public ImmutableInt2ShortOpenHashMap(Integer[] keys, Short[] values, float loadFactor) {
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		init(IntArrays.unwrap(keys), ShortArrays.unwrap(values), 0, keys.length, loadFactor);
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public ImmutableInt2ShortOpenHashMap(int[] keys, short[] values) {
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
	public ImmutableInt2ShortOpenHashMap(int[] keys, short[] values, float loadFactor) {
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		init(keys, values, 0, keys.length, loadFactor);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 */
	public ImmutableInt2ShortOpenHashMap(Map<? extends Integer, ? extends Short> map) {
		this(map, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public ImmutableInt2ShortOpenHashMap(Map<? extends Integer, ? extends Short> map, float loadFactor) {
		int[] keys = new int[map.size()];
		short[] values = new short[keys.length];
		int index = 0;
		for(Map.Entry<? extends Integer, ? extends Short> entry : map.entrySet()) {
			keys[index] = entry.getKey().intValue();
			values[index] = entry.getValue().shortValue();
			index++;
		}
		init(keys, values, 0, index, loadFactor);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 */
	public ImmutableInt2ShortOpenHashMap(Int2ShortMap map) {
		this(map, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
 	 */
	public ImmutableInt2ShortOpenHashMap(Int2ShortMap map, float loadFactor) {
		int[] keys = new int[map.size()];
		short[] values = new short[keys.length];
		int index = 0;
		for(Int2ShortMap.Entry entry : getFastIterable(map)) {
			keys[index] = entry.getIntKey();
			values[index] = entry.getShortValue();
			index++;
		}
		init(keys, values, 0, index, loadFactor);
	}
	
	protected void init(int[] a, short[] b, int offset, int length, float loadFactor) {
		SanityChecks.checkArrayCapacity(a.length, offset, length);
		int newSize = HashUtil.arraySize(length+1, loadFactor);
		int newMask = newSize - 1;
		int[] newKeys = new int[newSize + 1];
		short[] newValues = new short[newSize + 1];
		long[] newLinks = new long[newSize + 1];
		int prev = -1;
		for(int i = offset,m=offset+length;i<m;i++)
		{
			int o = a[i];
			if(o == 0) {
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
			int pos = HashUtil.mix(Integer.hashCode(o)) & newMask;
			int current = newKeys[pos];
			if(current != 0) {
				if(current == o) {
					newValues[pos] = b[i];
					continue;
				}
				while((current = newKeys[pos = (++pos & newMask)]) != 0) {
					if(current == o) {
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
	public short put(int key, short value) { throw new UnsupportedOperationException(); }
	@Override
	public short putIfAbsent(int key, short value) { throw new UnsupportedOperationException(); }	
	@Override
	public short addTo(int key, short value) { throw new UnsupportedOperationException(); }
	@Override
	public short subFrom(int key, short value) { throw new UnsupportedOperationException(); }	
	@Override
	public short putAndMoveToFirst(int key, short value) { throw new UnsupportedOperationException(); }
	@Override
	public short putAndMoveToLast(int key, short value) { throw new UnsupportedOperationException(); }
	@Override
	public boolean moveToFirst(int key) { throw new UnsupportedOperationException(); }
	@Override
	public boolean moveToLast(int key) { throw new UnsupportedOperationException(); }
	@Override
	public short getAndMoveToFirst(int key) { throw new UnsupportedOperationException(); }
	@Override
	public short getAndMoveToLast(int key) { throw new UnsupportedOperationException(); }
	
	@Override
	public boolean containsKey(int key) {
		return findIndex(key) >= 0;
	}
	
	@Override
	@Deprecated
	public boolean containsKey(Object key) {
		return findIndex(key) >= 0;
	}
	
	@Override
	public boolean containsValue(short value) {
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
			if((value == null && values[index] == getDefaultReturnValue()) || Objects.equals(value, Short.valueOf(values[index]))) return true;
			index = (int)links[index];
		}
		return false;
	}
	
	@Override
	public short remove(int key) { throw new UnsupportedOperationException(); }
	
	@Override
	public short removeOrDefault(int key, short defaultValue) { throw new UnsupportedOperationException(); }
	
	@Override
	@Deprecated
	public Short remove(Object key) { throw new UnsupportedOperationException(); }
	
	@Override
	public boolean remove(int key, short value) { throw new UnsupportedOperationException(); }
	
	@Override
	public boolean remove(Object key, Object value) { throw new UnsupportedOperationException(); }
	
	@Override
	public short get(int key) {
		int slot = findIndex(key);
		return slot < 0 ? getDefaultReturnValue() : values[slot];
	}
	
	@Override
	public Short get(Object key) {
		int slot = findIndex(key);
		return Short.valueOf(slot < 0 ? getDefaultReturnValue() : values[slot]);
	}
	
	@Override
	public short getOrDefault(int key, short defaultValue) {
		int slot = findIndex(key);
		return slot < 0 ? defaultValue : values[slot];
	}
	
	@Override
	public int firstIntKey() {
		if(size == 0) throw new NoSuchElementException();
		return keys[firstIndex];
	}
	
	@Override
	public int pollFirstIntKey() { throw new UnsupportedOperationException(); }
	
	@Override
	public int lastIntKey() {
		if(size == 0) throw new NoSuchElementException();
		return keys[lastIndex];
	}
	
	@Override
	public int pollLastIntKey() { throw new UnsupportedOperationException(); }
	
	@Override
	public short firstShortValue() {
		if(size == 0) throw new NoSuchElementException();
		return values[firstIndex];
	}
	
	@Override
	public short lastShortValue() {
		if(size == 0) throw new NoSuchElementException();
		return values[lastIndex];
	}	

	@Override
	public ObjectOrderedSet<Int2ShortMap.Entry> int2ShortEntrySet() {
		if(entrySet == null) entrySet = new MapEntrySet();
		return entrySet;
	}
	
	@Override
	public IntOrderedSet keySet() {
		if(keySet == null) keySet = new KeySet();
		return keySet;
	}
	
	@Override
	public ShortCollection values() {
		if(valuesC == null) valuesC = new Values();
		return valuesC;
	}
	
	@Override
	public ImmutableInt2ShortOpenHashMap copy() {
		ImmutableInt2ShortOpenHashMap map = new ImmutableInt2ShortOpenHashMap();
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
	public void forEach(IntShortConsumer action) {
		int index = firstIndex;
		while(index != -1){
			action.accept(keys[index], values[index]);
			index = (int)links[index];
		}
	}
	
	@Override
	public boolean replace(int key, short oldValue, short newValue) { throw new UnsupportedOperationException(); }
	
	@Override
	public short replace(int key, short value) { throw new UnsupportedOperationException(); }
	
	@Override
	public short computeShort(int key, IntShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
	@Override
	public short computeShortIfAbsent(int key, Int2ShortFunction mappingFunction) { throw new UnsupportedOperationException(); }
	@Override
	public short supplyShortIfAbsent(int key, ShortSupplier valueProvider) { throw new UnsupportedOperationException(); }
	@Override
	public short computeShortIfPresent(int key, IntShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
	@Override
	public short computeShortNonDefault(int key, IntShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
	@Override
	public short computeShortIfAbsentNonDefault(int key, Int2ShortFunction mappingFunction) { throw new UnsupportedOperationException(); }
	@Override
	public short supplyShortIfAbsentNonDefault(int key, ShortSupplier valueProvider) { throw new UnsupportedOperationException(); }
	@Override
	public short computeShortIfPresentNonDefault(int key, IntShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
	@Override
	public short mergeShort(int key, short value, ShortShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
	
	@Override
	public void mergeAllShort(Int2ShortMap m, ShortShortUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
	
	@Override
	public int size() { return size; }
	
	@Override
	public void clear() { throw new UnsupportedOperationException(); }
	
	protected int findIndex(int key) {
		if(key == 0) return containsNull ? nullIndex : -(nullIndex + 1);
		int pos = HashUtil.mix(Integer.hashCode(key)) & mask;
		int current = keys[pos];
		if(current != 0) {
			if(current == key) return pos;
			while((current = keys[pos = (++pos & mask)]) != 0)
				if(current == key) return pos;
		}
		return -(pos + 1);
	}
	
	protected int findIndex(Object key) {
		if(key == null) return containsNull ? nullIndex : -(nullIndex + 1);
		if(((Integer)key).intValue() == 0) return containsNull ? nullIndex : -(nullIndex + 1);
		int pos = HashUtil.mix(key.hashCode()) & mask;
		int current = keys[pos];
		if(current != 0) {
			if(Objects.equals(key, Integer.valueOf(current))) return pos;
			while((current = keys[pos = (++pos & mask)]) != 0)
				if(Objects.equals(key, Integer.valueOf(current))) return pos;
		}
		return -(pos + 1);
	}
	
	protected class MapEntry implements Int2ShortMap.Entry, Map.Entry<Integer, Short> {
		public int index = -1;
		
		public MapEntry() {}
		public MapEntry(int index) {
			this.index = index;
		}

		@Override
		public int getIntKey() {
			return keys[index];
		}

		@Override
		public short getShortValue() {
			return values[index];
		}

		@Override
		public short setValue(short value) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Map.Entry) {
				if(obj instanceof Int2ShortMap.Entry) {
					Int2ShortMap.Entry entry = (Int2ShortMap.Entry)obj;
					return keys[index] == entry.getIntKey() && values[index] == entry.getShortValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				if(!(key instanceof Integer)) return false;
				Object value = entry.getValue();
				return key instanceof Integer && value instanceof Short && keys[index] == ((Integer)key).intValue() && values[index] == ((Short)value).shortValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Integer.hashCode(keys[index]) ^ Short.hashCode(values[index]);
		}
		
		@Override
		public String toString() {
			return Integer.toString(keys[index]) + "=" + Short.toString(values[index]);
		}
	}
	
	private class MapEntrySet extends AbstractObjectSet<Int2ShortMap.Entry> implements Int2ShortOrderedMap.FastOrderedSet {
		@Override
		public boolean addAndMoveToFirst(Int2ShortMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Int2ShortMap.Entry o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean moveToFirst(Int2ShortMap.Entry o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean moveToLast(Int2ShortMap.Entry o) { throw new UnsupportedOperationException(); }
		
		@Override
		public Int2ShortMap.Entry first() {
			return new BasicEntry(firstIntKey(), firstShortValue());
		}
		
		@Override
		public Int2ShortMap.Entry last() {
			return new BasicEntry(lastIntKey(), lastShortValue());
		}
		
		@Override
		public Int2ShortMap.Entry pollFirst() { throw new UnsupportedOperationException(); }
		
		@Override
		public Int2ShortMap.Entry pollLast() { throw new UnsupportedOperationException(); }
		
		@Override
		public ObjectBidirectionalIterator<Int2ShortMap.Entry> iterator() {
			return new EntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Int2ShortMap.Entry> iterator(Int2ShortMap.Entry fromElement) {
			return new EntryIterator(fromElement.getIntKey());
		}
		
		@Override
		public ObjectBidirectionalIterator<Int2ShortMap.Entry> fastIterator() {
			return new FastEntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Int2ShortMap.Entry> fastIterator(int fromElement) {
			return new FastEntryIterator(fromElement);
		}
		
		@Override
		public MapEntrySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(Consumer<? super Int2ShortMap.Entry> action) {
			int index = firstIndex;
			while(index != -1){
				action.accept(new BasicEntry(keys[index], values[index]));
				index = (int)links[index];
			}
		}
		
		@Override
		public void fastForEach(Consumer<? super Int2ShortMap.Entry> action) {
			BasicEntry entry = new BasicEntry();
			int index = firstIndex;
			while(index != -1){
				entry.set(keys[index], values[index]);
				action.accept(entry);
				index = (int)links[index];
			}
		}
		
		@Override
		public void forEachIndexed(IntObjectConsumer<Int2ShortMap.Entry> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			int count = 0;
			int index = firstIndex;
			while(index != -1) {
				action.accept(count++, new BasicEntry(keys[index], values[index]));
				index = (int)links[index];
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, Int2ShortMap.Entry> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			int index = firstIndex;
			while(index != -1) {
				action.accept(input, new BasicEntry(keys[index], values[index]));
				index = (int)links[index];
			}
		}
		
		@Override
		public boolean matchesAny(Predicate<Int2ShortMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			BasicEntry entry = new BasicEntry();
			int index = firstIndex;
			while(index != -1) {
				entry.set(keys[index], values[index]);
				if(filter.test(entry)) return true;
				index = (int)links[index];
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Predicate<Int2ShortMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			BasicEntry entry = new BasicEntry();
			int index = firstIndex;
			while(index != -1) {
				entry.set(keys[index], values[index]);
				if(filter.test(entry)) return false;
				index = (int)links[index];
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Predicate<Int2ShortMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			BasicEntry entry = new BasicEntry();
			int index = firstIndex;
			while(index != -1) {
				entry.set(keys[index], values[index]);
				if(!filter.test(entry)) return false;
				index = (int)links[index];
			}
			return true;
		}
		
		@Override
		public <E> E reduce(E identity, BiFunction<E, Int2ShortMap.Entry, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			int index = firstIndex;
			while(index != -1) {
				state = operator.apply(state, new BasicEntry(keys[index], values[index]));
				index = (int)links[index];
			}
			return state;
		}
		
		@Override
		public Int2ShortMap.Entry reduce(ObjectObjectUnaryOperator<Int2ShortMap.Entry, Int2ShortMap.Entry> operator) {
			Objects.requireNonNull(operator);
			Int2ShortMap.Entry state = null;
			boolean empty = true;
			int index = firstIndex;
			while(index != -1) {
				if(empty) {
					empty = false;
					state = new BasicEntry(keys[index], values[index]);
					index = (int)links[index];
					continue;
				}
				state = operator.apply(state, new BasicEntry(keys[index], values[index]));
				index = (int)links[index];
			}
			return state;
		}
		
		@Override
		public Int2ShortMap.Entry findFirst(Predicate<Int2ShortMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return null;
			BasicEntry entry = new BasicEntry();
			int index = firstIndex;
			while(index != -1) {
				entry.set(keys[index], values[index]);
				if(filter.test(entry)) return entry;
				index = (int)links[index];
			}
			return null;
		}
		
		@Override
		public int count(Predicate<Int2ShortMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			BasicEntry entry = new BasicEntry();
			int result = 0;
			int index = firstIndex;
			while(index != -1) {
				entry.set(keys[index], values[index]);
				if(filter.test(entry)) result++;
				index = (int)links[index];
			}
			return result;
		}
		
		@Override
		@Deprecated
		public boolean contains(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Int2ShortMap.Entry) {
					Int2ShortMap.Entry entry = (Int2ShortMap.Entry)o;
					int index = ImmutableInt2ShortOpenHashMap.this.findIndex(entry.getIntKey());
					if(index >= 0) return entry.getShortValue() == ImmutableInt2ShortOpenHashMap.this.values[index];
				}
				else {
					Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
					int index = ImmutableInt2ShortOpenHashMap.this.findIndex(entry.getKey());
					if(index >= 0) return Objects.equals(entry.getValue(), Short.valueOf(ImmutableInt2ShortOpenHashMap.this.values[index]));
				}
			}
			return false;
		}
		
		@Override
		@Deprecated
		public boolean remove(Object o) { throw new UnsupportedOperationException(); }
		
		@Override
		public int size() {
			return ImmutableInt2ShortOpenHashMap.this.size();
		}
		
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
	}
	
	private final class KeySet extends AbstractIntSet implements IntOrderedSet {
		@Override
		public boolean contains(int e) {
			return containsKey(e);
		}
		
		@Override
		public boolean remove(int o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean add(int o) {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean addAndMoveToFirst(int o) { throw new UnsupportedOperationException(); }

		@Override
		public boolean addAndMoveToLast(int o) { throw new UnsupportedOperationException(); }

		@Override
		public boolean moveToFirst(int o) { throw new UnsupportedOperationException(); }

		@Override
		public boolean moveToLast(int o) { throw new UnsupportedOperationException(); }
		
		@Override
		public IntListIterator iterator() {
			return new KeyIterator();
		}
		
		@Override
		public IntBidirectionalIterator iterator(int fromElement) {
			return new KeyIterator(fromElement);
		}
		
		@Override
		public KeySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public int size() {
			return ImmutableInt2ShortOpenHashMap.this.size();
		}
		
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		
		@Override
		public int firstInt() {
			return firstIntKey();
		}
		
		@Override
		public int pollFirstInt() { throw new UnsupportedOperationException(); }

		@Override
		public int lastInt() {
			return lastIntKey();
		}

		@Override
		public int pollLastInt() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(IntConsumer action) {
			int index = firstIndex;
			while(index != -1){
				action.accept(keys[index]);
				index = (int)links[index];
			}
		}
		
		@Override
		public void forEachIndexed(IntIntConsumer action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			int count = 0;
			int index = firstIndex;
			while(index != -1){
				action.accept(count++, keys[index]);
				index = (int)links[index];
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectIntConsumer<E> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			int index = firstIndex;
			while(index != -1){
				action.accept(input, keys[index]);
				index = (int)links[index];
			}
		}
		
		@Override
		public boolean matchesAny(IntPredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			int index = firstIndex;
			while(index != -1){
				if(filter.test(keys[index])) return true;
				index = (int)links[index];
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(IntPredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			int index = firstIndex;
			while(index != -1){
				if(filter.test(keys[index])) return false;
				index = (int)links[index];
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(IntPredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			int index = firstIndex;
			while(index != -1){
				if(!filter.test(keys[index])) return false;
				index = (int)links[index];
			}
			return true;
		}
		
		@Override
		public int reduce(int identity, IntIntUnaryOperator operator) {
			Objects.requireNonNull(operator);
			int state = identity;
			int index = firstIndex;
			while(index != -1) {
				state = operator.applyAsInt(state, keys[index]);
				index = (int)links[index];
			}
			return state;
		}
		
		@Override
		public int reduce(IntIntUnaryOperator operator) {
			Objects.requireNonNull(operator);
			int state = 0;
			boolean empty = true;
			int index = firstIndex;
			while(index != -1) {
				if(empty) {
					empty = false;
					state = keys[index];
					index = (int)links[index];
					continue;
				}
				state = operator.applyAsInt(state, keys[index]);
				index = (int)links[index];
			}
			return state;
		}
		
		@Override
		public int findFirst(IntPredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			int index = firstIndex;
			while(index != -1){
				if(filter.test(keys[index])) return keys[index];
				index = (int)links[index];
			}
			return 0;
		}
		
		@Override
		public int count(IntPredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			int index = firstIndex;
			int result = 0;
			while(index != -1){
				if(filter.test(keys[index])) result++;
				index = (int)links[index];
			}
			return result;
		}
	}
	
	private class Values extends AbstractShortCollection {
		@Override
		public boolean contains(short e) {
			return containsValue(e);
		}
		
		@Override
		public boolean add(short o) {
			throw new UnsupportedOperationException();
		}

		@Override
		public ShortIterator iterator() {
			return new ValueIterator();
		}
		
		@Override
		public int size() {
			return ImmutableInt2ShortOpenHashMap.this.size();
		}
		
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(ShortConsumer action) {
			int index = firstIndex;
			while(index != -1){
				action.accept(values[index]);
				index = (int)links[index];
			}
		}
		
		@Override
		public void forEachIndexed(IntShortConsumer action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			int count = 0;
			int index = firstIndex;
			while(index != -1){
				action.accept(count++, values[index]);
				index = (int)links[index];
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectShortConsumer<E> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			int index = firstIndex;
			while(index != -1){
				action.accept(input, values[index]);
				index = (int)links[index];
			}
		}
		
		@Override
		public boolean matchesAny(ShortPredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			int index = firstIndex;
			while(index != -1){
				if(filter.test(values[index])) return true;
				index = (int)links[index];
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(ShortPredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			int index = firstIndex;
			while(index != -1){
				if(filter.test(values[index])) return false;
				index = (int)links[index];
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(ShortPredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			int index = firstIndex;
			while(index != -1){
				if(!filter.test(values[index])) return false;
				index = (int)links[index];
			}
			return true;
		}
		
		@Override
		public short reduce(short identity, ShortShortUnaryOperator operator) {
			Objects.requireNonNull(operator);
			short state = identity;
			int index = firstIndex;
			while(index != -1) {
				state = operator.applyAsShort(state, values[index]);
				index = (int)links[index];
			}
			return state;
		}
		
		@Override
		public short reduce(ShortShortUnaryOperator operator) {
			Objects.requireNonNull(operator);
			short state = (short)0;
			boolean empty = true;
			int index = firstIndex;
			while(index != -1) {
				if(empty) {
					empty = false;
					state = values[index];
					index = (int)links[index];
					continue;
				}
				state = operator.applyAsShort(state, values[index]);
				index = (int)links[index];
			}
			return state;
		}
		
		@Override
		public short findFirst(ShortPredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return (short)0;
			int index = firstIndex;
			while(index != -1){
				if(filter.test(values[index])) return values[index];
				index = (int)links[index];
			}
			return (short)0;
		}
		
		@Override
		public int count(ShortPredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			int result = 0;
			int index = firstIndex;
			while(index != -1){
				if(filter.test(values[index])) result++;
				index = (int)links[index];
			}
			return result;
		}
	}
	
	private class FastEntryIterator extends MapIterator implements ObjectListIterator<Int2ShortMap.Entry> {
		MapEntry entry = new MapEntry();
		
		public FastEntryIterator() {}
		public FastEntryIterator(int from) {
			super(from);
		}
		
		@Override
		public Int2ShortMap.Entry next() {
			entry.index = nextEntry();
			return entry;
		}
		
		@Override
		public Int2ShortMap.Entry previous() {
			entry.index = previousEntry();
			return entry;
		}
		
		@Override
		public void set(Int2ShortMap.Entry entry) { throw new UnsupportedOperationException(); }

		@Override
		public void add(Int2ShortMap.Entry entry) { throw new UnsupportedOperationException(); }
	}
	
	private class EntryIterator extends MapIterator implements ObjectListIterator<Int2ShortMap.Entry> {
		
		public EntryIterator() {}
		public EntryIterator(int from) {
			super(from);
		}
		
		@Override
		public Int2ShortMap.Entry next() {
			return new MapEntry(nextEntry());
		}
		
		@Override
		public Int2ShortMap.Entry previous() {
			return new MapEntry(previousEntry());
		}
		
		@Override
		public void remove() { throw new UnsupportedOperationException(); }

		@Override
		public void set(Int2ShortMap.Entry entry) { throw new UnsupportedOperationException(); }

		@Override
		public void add(Int2ShortMap.Entry entry) { throw new UnsupportedOperationException(); }
	}
	
	private class KeyIterator extends MapIterator implements IntListIterator {
		
		public KeyIterator() {}
		public KeyIterator(int from) {
			super(from);
		}
		
		@Override
		public int previousInt() {
			return keys[previousEntry()];
		}

		@Override
		public int nextInt() {
			return keys[nextEntry()];
		}
		
		@Override
		public void set(int e) { throw new UnsupportedOperationException(); }
		@Override
		public void add(int e) { throw new UnsupportedOperationException(); }
	}
	
	private class ValueIterator extends MapIterator implements ShortListIterator {
		public ValueIterator() {}
		
		@Override
		public short previousShort() {
			return values[previousEntry()];
		}

		@Override
		public short nextShort() {
			return values[nextEntry()];
		}

		@Override
		public void set(short e) { throw new UnsupportedOperationException(); }

		@Override
		public void add(short e) { throw new UnsupportedOperationException(); }
		
	}
	
	private class MapIterator {
		int previous = -1;
		int next = -1;
		int current = -1;
		int index = 0;
		
		MapIterator() {
			next = firstIndex;
		}
		
		MapIterator(int from) {
			if(from == 0) {
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
				int pos = HashUtil.mix(Integer.hashCode(from)) & mask;
				while(keys[pos] != 0) {
					if(keys[pos] == from) {
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