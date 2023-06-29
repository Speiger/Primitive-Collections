package speiger.src.collections.shorts.maps.impl.immutable;

import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.IntPredicate;

import speiger.src.collections.shorts.collections.ShortBidirectionalIterator;
import speiger.src.collections.shorts.functions.ShortConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectShortConsumer;
import speiger.src.collections.ints.functions.consumer.IntShortConsumer;
import speiger.src.collections.ints.functions.consumer.IntObjectConsumer;
import speiger.src.collections.ints.functions.consumer.IntIntConsumer;
import speiger.src.collections.shorts.functions.function.ShortPredicate;
import speiger.src.collections.shorts.functions.consumer.ShortIntConsumer;
import speiger.src.collections.shorts.functions.function.Short2IntFunction;
import speiger.src.collections.shorts.functions.function.ShortIntUnaryOperator;
import speiger.src.collections.shorts.functions.function.ShortShortUnaryOperator;
import speiger.src.collections.shorts.lists.ShortListIterator;
import speiger.src.collections.shorts.maps.interfaces.Short2IntOrderedMap;
import speiger.src.collections.shorts.maps.abstracts.AbstractShort2IntMap;
import speiger.src.collections.shorts.maps.interfaces.Short2IntMap;
import speiger.src.collections.shorts.sets.ShortOrderedSet;
import speiger.src.collections.shorts.utils.ShortArrays;
import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectIntConsumer;

import speiger.src.collections.shorts.sets.AbstractShortSet;
import speiger.src.collections.ints.collections.AbstractIntCollection;
import speiger.src.collections.ints.collections.IntCollection;
import speiger.src.collections.ints.collections.IntIterator;
import speiger.src.collections.ints.functions.IntSupplier;
import speiger.src.collections.ints.functions.function.IntIntUnaryOperator;
import speiger.src.collections.ints.functions.IntConsumer;
import speiger.src.collections.ints.lists.IntListIterator;
import speiger.src.collections.ints.utils.IntArrays;
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
public class ImmutableShort2IntOpenHashMap extends AbstractShort2IntMap implements Short2IntOrderedMap
{
	/** The Backing keys array */
	protected transient short[] keys;
	/** The Backing values array */
	protected transient int[] values;
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
	protected transient ShortOrderedSet keySet;
	/** Values cache */
	protected transient IntCollection valuesC;
	
	/** Amount of Elements stored in the HashMap */
	protected int size;
	
	/**
	 * Helper constructor for copying the Map
	 */
	protected ImmutableShort2IntOpenHashMap() {}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public ImmutableShort2IntOpenHashMap(Short[] keys, Integer[] values) {
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
	public ImmutableShort2IntOpenHashMap(Short[] keys, Integer[] values, float loadFactor) {
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		init(ShortArrays.unwrap(keys), IntArrays.unwrap(values), 0, keys.length, loadFactor);
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public ImmutableShort2IntOpenHashMap(short[] keys, int[] values) {
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
	public ImmutableShort2IntOpenHashMap(short[] keys, int[] values, float loadFactor) {
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		init(keys, values, 0, keys.length, loadFactor);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 */
	public ImmutableShort2IntOpenHashMap(Map<? extends Short, ? extends Integer> map) {
		this(map, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public ImmutableShort2IntOpenHashMap(Map<? extends Short, ? extends Integer> map, float loadFactor) {
		short[] keys = new short[map.size()];
		int[] values = new int[keys.length];
		int index = 0;
		for(Map.Entry<? extends Short, ? extends Integer> entry : map.entrySet()) {
			keys[index] = entry.getKey().shortValue();
			values[index] = entry.getValue().intValue();
			index++;
		}
		init(keys, values, 0, index, loadFactor);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 */
	public ImmutableShort2IntOpenHashMap(Short2IntMap map) {
		this(map, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
 	 */
	public ImmutableShort2IntOpenHashMap(Short2IntMap map, float loadFactor) {
		short[] keys = new short[map.size()];
		int[] values = new int[keys.length];
		int index = 0;
		for(Short2IntMap.Entry entry : getFastIterable(map)) {
			keys[index] = entry.getShortKey();
			values[index] = entry.getIntValue();
			index++;
		}
		init(keys, values, 0, index, loadFactor);
	}
	
	protected void init(short[] a, int[] b, int offset, int length, float loadFactor) {
		SanityChecks.checkArrayCapacity(a.length, offset, length);
		int newSize = HashUtil.arraySize(length+1, loadFactor);
		int newMask = newSize - 1;
		short[] newKeys = new short[newSize + 1];
		int[] newValues = new int[newSize + 1];
		long[] newLinks = new long[newSize + 1];
		int prev = -1;
		for(int i = offset,m=offset+length;i<m;i++)
		{
			short o = a[i];
			if(o == (short)0) {
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
			int pos = HashUtil.mix(Short.hashCode(o)) & newMask;
			short current = newKeys[pos];
			if(current != (short)0) {
				if(current == o) {
					newValues[pos] = b[i];
					continue;
				}
				while((current = newKeys[pos = (++pos & newMask)]) != (short)0) {
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
	public int put(short key, int value) { throw new UnsupportedOperationException(); }
	@Override
	public int putIfAbsent(short key, int value) { throw new UnsupportedOperationException(); }	
	@Override
	public int addTo(short key, int value) { throw new UnsupportedOperationException(); }
	@Override
	public int subFrom(short key, int value) { throw new UnsupportedOperationException(); }	
	@Override
	public int putAndMoveToFirst(short key, int value) { throw new UnsupportedOperationException(); }
	@Override
	public int putAndMoveToLast(short key, int value) { throw new UnsupportedOperationException(); }
	@Override
	public boolean moveToFirst(short key) { throw new UnsupportedOperationException(); }
	@Override
	public boolean moveToLast(short key) { throw new UnsupportedOperationException(); }
	@Override
	public int getAndMoveToFirst(short key) { throw new UnsupportedOperationException(); }
	@Override
	public int getAndMoveToLast(short key) { throw new UnsupportedOperationException(); }
	
	@Override
	public boolean containsKey(short key) {
		return findIndex(key) >= 0;
	}
	
	@Override
	@Deprecated
	public boolean containsKey(Object key) {
		return findIndex(key) >= 0;
	}
	
	@Override
	public boolean containsValue(int value) {
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
			if((value == null && values[index] == getDefaultReturnValue()) || Objects.equals(value, Integer.valueOf(values[index]))) return true;
			index = (int)links[index];
		}
		return false;
	}
	
	@Override
	public int remove(short key) { throw new UnsupportedOperationException(); }
	
	@Override
	public int removeOrDefault(short key, int defaultValue) { throw new UnsupportedOperationException(); }
	
	@Override
	@Deprecated
	public Integer remove(Object key) { throw new UnsupportedOperationException(); }
	
	@Override
	public boolean remove(short key, int value) { throw new UnsupportedOperationException(); }
	
	@Override
	public boolean remove(Object key, Object value) { throw new UnsupportedOperationException(); }
	
	@Override
	public int get(short key) {
		int slot = findIndex(key);
		return slot < 0 ? getDefaultReturnValue() : values[slot];
	}
	
	@Override
	public Integer get(Object key) {
		int slot = findIndex(key);
		return Integer.valueOf(slot < 0 ? getDefaultReturnValue() : values[slot]);
	}
	
	@Override
	public int getOrDefault(short key, int defaultValue) {
		int slot = findIndex(key);
		return slot < 0 ? defaultValue : values[slot];
	}
	
	@Override
	public short firstShortKey() {
		if(size == 0) throw new NoSuchElementException();
		return keys[firstIndex];
	}
	
	@Override
	public short pollFirstShortKey() { throw new UnsupportedOperationException(); }
	
	@Override
	public short lastShortKey() {
		if(size == 0) throw new NoSuchElementException();
		return keys[lastIndex];
	}
	
	@Override
	public short pollLastShortKey() { throw new UnsupportedOperationException(); }
	
	@Override
	public int firstIntValue() {
		if(size == 0) throw new NoSuchElementException();
		return values[firstIndex];
	}
	
	@Override
	public int lastIntValue() {
		if(size == 0) throw new NoSuchElementException();
		return values[lastIndex];
	}	

	@Override
	public ObjectOrderedSet<Short2IntMap.Entry> short2IntEntrySet() {
		if(entrySet == null) entrySet = new MapEntrySet();
		return entrySet;
	}
	
	@Override
	public ShortOrderedSet keySet() {
		if(keySet == null) keySet = new KeySet();
		return keySet;
	}
	
	@Override
	public IntCollection values() {
		if(valuesC == null) valuesC = new Values();
		return valuesC;
	}
	
	@Override
	public ImmutableShort2IntOpenHashMap copy() {
		ImmutableShort2IntOpenHashMap map = new ImmutableShort2IntOpenHashMap();
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
	public void forEach(ShortIntConsumer action) {
		int index = firstIndex;
		while(index != -1){
			action.accept(keys[index], values[index]);
			index = (int)links[index];
		}
	}
	
	@Override
	public boolean replace(short key, int oldValue, int newValue) { throw new UnsupportedOperationException(); }
	
	@Override
	public int replace(short key, int value) { throw new UnsupportedOperationException(); }
	
	@Override
	public int computeInt(short key, ShortIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
	@Override
	public int computeIntIfAbsent(short key, Short2IntFunction mappingFunction) { throw new UnsupportedOperationException(); }
	@Override
	public int supplyIntIfAbsent(short key, IntSupplier valueProvider) { throw new UnsupportedOperationException(); }
	@Override
	public int computeIntIfPresent(short key, ShortIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
	@Override
	public int computeIntNonDefault(short key, ShortIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
	@Override
	public int computeIntIfAbsentNonDefault(short key, Short2IntFunction mappingFunction) { throw new UnsupportedOperationException(); }
	@Override
	public int supplyIntIfAbsentNonDefault(short key, IntSupplier valueProvider) { throw new UnsupportedOperationException(); }
	@Override
	public int computeIntIfPresentNonDefault(short key, ShortIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
	@Override
	public int mergeInt(short key, int value, IntIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
	
	@Override
	public void mergeAllInt(Short2IntMap m, IntIntUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
	
	@Override
	public int size() { return size; }
	
	@Override
	public void clear() { throw new UnsupportedOperationException(); }
	
	protected int findIndex(short key) {
		if(key == (short)0) return containsNull ? nullIndex : -(nullIndex + 1);
		int pos = HashUtil.mix(Short.hashCode(key)) & mask;
		short current = keys[pos];
		if(current != (short)0) {
			if(current == key) return pos;
			while((current = keys[pos = (++pos & mask)]) != (short)0)
				if(current == key) return pos;
		}
		return -(pos + 1);
	}
	
	protected int findIndex(Object key) {
		if(key == null) return containsNull ? nullIndex : -(nullIndex + 1);
		if(((Short)key).shortValue() == (short)0) return containsNull ? nullIndex : -(nullIndex + 1);
		int pos = HashUtil.mix(key.hashCode()) & mask;
		short current = keys[pos];
		if(current != (short)0) {
			if(Objects.equals(key, Short.valueOf(current))) return pos;
			while((current = keys[pos = (++pos & mask)]) != (short)0)
				if(Objects.equals(key, Short.valueOf(current))) return pos;
		}
		return -(pos + 1);
	}
	
	protected class MapEntry implements Short2IntMap.Entry, Map.Entry<Short, Integer> {
		public int index = -1;
		
		public MapEntry() {}
		public MapEntry(int index) {
			this.index = index;
		}

		@Override
		public short getShortKey() {
			return keys[index];
		}

		@Override
		public int getIntValue() {
			return values[index];
		}

		@Override
		public int setValue(int value) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Map.Entry) {
				if(obj instanceof Short2IntMap.Entry) {
					Short2IntMap.Entry entry = (Short2IntMap.Entry)obj;
					return keys[index] == entry.getShortKey() && values[index] == entry.getIntValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				if(!(key instanceof Short)) return false;
				Object value = entry.getValue();
				return key instanceof Short && value instanceof Integer && keys[index] == ((Short)key).shortValue() && values[index] == ((Integer)value).intValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Short.hashCode(keys[index]) ^ Integer.hashCode(values[index]);
		}
		
		@Override
		public String toString() {
			return Short.toString(keys[index]) + "=" + Integer.toString(values[index]);
		}
	}
	
	private class MapEntrySet extends AbstractObjectSet<Short2IntMap.Entry> implements Short2IntOrderedMap.FastOrderedSet {
		@Override
		public boolean addAndMoveToFirst(Short2IntMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Short2IntMap.Entry o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean moveToFirst(Short2IntMap.Entry o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean moveToLast(Short2IntMap.Entry o) { throw new UnsupportedOperationException(); }
		
		@Override
		public Short2IntMap.Entry first() {
			return new BasicEntry(firstShortKey(), firstIntValue());
		}
		
		@Override
		public Short2IntMap.Entry last() {
			return new BasicEntry(lastShortKey(), lastIntValue());
		}
		
		@Override
		public Short2IntMap.Entry pollFirst() { throw new UnsupportedOperationException(); }
		
		@Override
		public Short2IntMap.Entry pollLast() { throw new UnsupportedOperationException(); }
		
		@Override
		public ObjectBidirectionalIterator<Short2IntMap.Entry> iterator() {
			return new EntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Short2IntMap.Entry> iterator(Short2IntMap.Entry fromElement) {
			return new EntryIterator(fromElement.getShortKey());
		}
		
		@Override
		public ObjectBidirectionalIterator<Short2IntMap.Entry> fastIterator() {
			return new FastEntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Short2IntMap.Entry> fastIterator(short fromElement) {
			return new FastEntryIterator(fromElement);
		}
		
		@Override
		public MapEntrySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(Consumer<? super Short2IntMap.Entry> action) {
			int index = firstIndex;
			while(index != -1){
				action.accept(new BasicEntry(keys[index], values[index]));
				index = (int)links[index];
			}
		}
		
		@Override
		public void fastForEach(Consumer<? super Short2IntMap.Entry> action) {
			BasicEntry entry = new BasicEntry();
			int index = firstIndex;
			while(index != -1){
				entry.set(keys[index], values[index]);
				action.accept(entry);
				index = (int)links[index];
			}
		}
		
		@Override
		public void forEachIndexed(IntObjectConsumer<Short2IntMap.Entry> action) {
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
		public <E> void forEach(E input, ObjectObjectConsumer<E, Short2IntMap.Entry> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			int index = firstIndex;
			while(index != -1) {
				action.accept(input, new BasicEntry(keys[index], values[index]));
				index = (int)links[index];
			}
		}
		
		@Override
		public boolean matchesAny(Predicate<Short2IntMap.Entry> filter) {
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
		public boolean matchesNone(Predicate<Short2IntMap.Entry> filter) {
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
		public boolean matchesAll(Predicate<Short2IntMap.Entry> filter) {
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
		public <E> E reduce(E identity, BiFunction<E, Short2IntMap.Entry, E> operator) {
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
		public Short2IntMap.Entry reduce(ObjectObjectUnaryOperator<Short2IntMap.Entry, Short2IntMap.Entry> operator) {
			Objects.requireNonNull(operator);
			Short2IntMap.Entry state = null;
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
		public Short2IntMap.Entry findFirst(Predicate<Short2IntMap.Entry> filter) {
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
		public int count(Predicate<Short2IntMap.Entry> filter) {
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
				if(o instanceof Short2IntMap.Entry) {
					Short2IntMap.Entry entry = (Short2IntMap.Entry)o;
					int index = ImmutableShort2IntOpenHashMap.this.findIndex(entry.getShortKey());
					if(index >= 0) return entry.getIntValue() == ImmutableShort2IntOpenHashMap.this.values[index];
				}
				else {
					Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
					int index = ImmutableShort2IntOpenHashMap.this.findIndex(entry.getKey());
					if(index >= 0) return Objects.equals(entry.getValue(), Integer.valueOf(ImmutableShort2IntOpenHashMap.this.values[index]));
				}
			}
			return false;
		}
		
		@Override
		@Deprecated
		public boolean remove(Object o) { throw new UnsupportedOperationException(); }
		
		@Override
		public int size() {
			return ImmutableShort2IntOpenHashMap.this.size();
		}
		
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
	}
	
	private final class KeySet extends AbstractShortSet implements ShortOrderedSet {
		@Override
		public boolean contains(short e) {
			return containsKey(e);
		}
		
		@Override
		public boolean remove(short o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean add(short o) {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean addAndMoveToFirst(short o) { throw new UnsupportedOperationException(); }

		@Override
		public boolean addAndMoveToLast(short o) { throw new UnsupportedOperationException(); }

		@Override
		public boolean moveToFirst(short o) { throw new UnsupportedOperationException(); }

		@Override
		public boolean moveToLast(short o) { throw new UnsupportedOperationException(); }
		
		@Override
		public ShortListIterator iterator() {
			return new KeyIterator();
		}
		
		@Override
		public ShortBidirectionalIterator iterator(short fromElement) {
			return new KeyIterator(fromElement);
		}
		
		@Override
		public KeySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public int size() {
			return ImmutableShort2IntOpenHashMap.this.size();
		}
		
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		
		@Override
		public short firstShort() {
			return firstShortKey();
		}
		
		@Override
		public short pollFirstShort() { throw new UnsupportedOperationException(); }

		@Override
		public short lastShort() {
			return lastShortKey();
		}

		@Override
		public short pollLastShort() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(ShortConsumer action) {
			int index = firstIndex;
			while(index != -1){
				action.accept(keys[index]);
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
				action.accept(count++, keys[index]);
				index = (int)links[index];
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectShortConsumer<E> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			int index = firstIndex;
			while(index != -1){
				action.accept(input, keys[index]);
				index = (int)links[index];
			}
		}
		
		@Override
		public boolean matchesAny(ShortPredicate filter) {
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
		public boolean matchesNone(ShortPredicate filter) {
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
		public boolean matchesAll(ShortPredicate filter) {
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
		public short reduce(short identity, ShortShortUnaryOperator operator) {
			Objects.requireNonNull(operator);
			short state = identity;
			int index = firstIndex;
			while(index != -1) {
				state = operator.applyAsShort(state, keys[index]);
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
					state = keys[index];
					index = (int)links[index];
					continue;
				}
				state = operator.applyAsShort(state, keys[index]);
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
				if(filter.test(keys[index])) return keys[index];
				index = (int)links[index];
			}
			return (short)0;
		}
		
		@Override
		public int count(ShortPredicate filter) {
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
	
	private class Values extends AbstractIntCollection {
		@Override
		public boolean contains(int e) {
			return containsValue(e);
		}
		
		@Override
		public boolean add(int o) {
			throw new UnsupportedOperationException();
		}

		@Override
		public IntIterator iterator() {
			return new ValueIterator();
		}
		
		@Override
		public int size() {
			return ImmutableShort2IntOpenHashMap.this.size();
		}
		
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(IntConsumer action) {
			int index = firstIndex;
			while(index != -1){
				action.accept(values[index]);
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
				action.accept(count++, values[index]);
				index = (int)links[index];
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectIntConsumer<E> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			int index = firstIndex;
			while(index != -1){
				action.accept(input, values[index]);
				index = (int)links[index];
			}
		}
		
		@Override
		public boolean matchesAny(IntPredicate filter) {
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
		public boolean matchesNone(IntPredicate filter) {
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
		public boolean matchesAll(IntPredicate filter) {
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
		public int reduce(int identity, IntIntUnaryOperator operator) {
			Objects.requireNonNull(operator);
			int state = identity;
			int index = firstIndex;
			while(index != -1) {
				state = operator.applyAsInt(state, values[index]);
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
					state = values[index];
					index = (int)links[index];
					continue;
				}
				state = operator.applyAsInt(state, values[index]);
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
				if(filter.test(values[index])) return values[index];
				index = (int)links[index];
			}
			return 0;
		}
		
		@Override
		public int count(IntPredicate filter) {
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
	
	private class FastEntryIterator extends MapIterator implements ObjectListIterator<Short2IntMap.Entry> {
		MapEntry entry = new MapEntry();
		
		public FastEntryIterator() {}
		public FastEntryIterator(short from) {
			super(from);
		}
		
		@Override
		public Short2IntMap.Entry next() {
			entry.index = nextEntry();
			return entry;
		}
		
		@Override
		public Short2IntMap.Entry previous() {
			entry.index = previousEntry();
			return entry;
		}
		
		@Override
		public void set(Short2IntMap.Entry entry) { throw new UnsupportedOperationException(); }

		@Override
		public void add(Short2IntMap.Entry entry) { throw new UnsupportedOperationException(); }
	}
	
	private class EntryIterator extends MapIterator implements ObjectListIterator<Short2IntMap.Entry> {
		
		public EntryIterator() {}
		public EntryIterator(short from) {
			super(from);
		}
		
		@Override
		public Short2IntMap.Entry next() {
			return new MapEntry(nextEntry());
		}
		
		@Override
		public Short2IntMap.Entry previous() {
			return new MapEntry(previousEntry());
		}
		
		@Override
		public void remove() { throw new UnsupportedOperationException(); }

		@Override
		public void set(Short2IntMap.Entry entry) { throw new UnsupportedOperationException(); }

		@Override
		public void add(Short2IntMap.Entry entry) { throw new UnsupportedOperationException(); }
	}
	
	private class KeyIterator extends MapIterator implements ShortListIterator {
		
		public KeyIterator() {}
		public KeyIterator(short from) {
			super(from);
		}
		
		@Override
		public short previousShort() {
			return keys[previousEntry()];
		}

		@Override
		public short nextShort() {
			return keys[nextEntry()];
		}
		
		@Override
		public void set(short e) { throw new UnsupportedOperationException(); }
		@Override
		public void add(short e) { throw new UnsupportedOperationException(); }
	}
	
	private class ValueIterator extends MapIterator implements IntListIterator {
		public ValueIterator() {}
		
		@Override
		public int previousInt() {
			return values[previousEntry()];
		}

		@Override
		public int nextInt() {
			return values[nextEntry()];
		}

		@Override
		public void set(int e) { throw new UnsupportedOperationException(); }

		@Override
		public void add(int e) { throw new UnsupportedOperationException(); }
		
	}
	
	private class MapIterator {
		int previous = -1;
		int next = -1;
		int current = -1;
		int index = 0;
		
		MapIterator() {
			next = firstIndex;
		}
		
		MapIterator(short from) {
			if(from == (short)0) {
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
				int pos = HashUtil.mix(Short.hashCode(from)) & mask;
				while(keys[pos] != (short)0) {
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