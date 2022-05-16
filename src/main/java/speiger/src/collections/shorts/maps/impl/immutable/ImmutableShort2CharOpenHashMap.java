package speiger.src.collections.shorts.maps.impl.immutable;

import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.BiFunction;

import speiger.src.collections.shorts.collections.ShortBidirectionalIterator;
import speiger.src.collections.shorts.functions.ShortConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectShortConsumer;
import speiger.src.collections.shorts.functions.function.Short2BooleanFunction;
import speiger.src.collections.shorts.functions.consumer.ShortCharConsumer;
import speiger.src.collections.shorts.functions.function.Short2CharFunction;
import speiger.src.collections.shorts.functions.function.ShortCharUnaryOperator;
import speiger.src.collections.shorts.functions.function.ShortShortUnaryOperator;
import speiger.src.collections.shorts.lists.ShortListIterator;
import speiger.src.collections.shorts.maps.interfaces.Short2CharOrderedMap;
import speiger.src.collections.shorts.maps.abstracts.AbstractShort2CharMap;
import speiger.src.collections.shorts.maps.interfaces.Short2CharMap;
import speiger.src.collections.shorts.sets.ShortOrderedSet;
import speiger.src.collections.shorts.utils.maps.Short2CharMaps;
import speiger.src.collections.shorts.utils.ShortArrays;
import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;
import speiger.src.collections.objects.functions.function.Object2BooleanFunction;
import speiger.src.collections.objects.functions.consumer.ObjectCharConsumer;
import speiger.src.collections.chars.functions.function.Char2BooleanFunction;
import speiger.src.collections.shorts.sets.AbstractShortSet;
import speiger.src.collections.shorts.sets.ShortSet;
import speiger.src.collections.chars.collections.AbstractCharCollection;
import speiger.src.collections.chars.collections.CharCollection;
import speiger.src.collections.chars.collections.CharIterator;
import speiger.src.collections.chars.functions.CharSupplier;
import speiger.src.collections.chars.functions.function.CharCharUnaryOperator;
import speiger.src.collections.chars.functions.CharConsumer;
import speiger.src.collections.chars.lists.CharListIterator;
import speiger.src.collections.chars.utils.CharArrays;
import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;
import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.HashUtil;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Type Specific Custom implementation of the HashMap
 * Instead of using Wrapper Object Arrays for storing keys and values there is dedicated arrays for storing keys and values.
 * Extra to that there is a couple quality of life functions provided
 */
public class ImmutableShort2CharOpenHashMap extends AbstractShort2CharMap implements Short2CharOrderedMap
{
	/** The Backing keys array */
	protected transient short[] keys;
	/** The Backing values array */
	protected transient char[] values;
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
	protected transient FastEntrySet entrySet;
	/** KeySet cache */
	protected transient ShortSet keySet;
	/** Values cache */
	protected transient CharCollection valuesC;
	
	/** Amount of Elements stored in the HashMap */
	protected int size;
	
	/**
	 * Helper constructor for copying the Map
	 */
	protected ImmutableShort2CharOpenHashMap() {}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public ImmutableShort2CharOpenHashMap(Short[] keys, Character[] values) {
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
	public ImmutableShort2CharOpenHashMap(Short[] keys, Character[] values, float loadFactor) {
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		init(ShortArrays.unwrap(keys), CharArrays.unwrap(values), 0, keys.length, loadFactor);
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public ImmutableShort2CharOpenHashMap(short[] keys, char[] values) {
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
	public ImmutableShort2CharOpenHashMap(short[] keys, char[] values, float loadFactor) {
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		init(keys, values, 0, keys.length, loadFactor);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 */
	public ImmutableShort2CharOpenHashMap(Map<? extends Short, ? extends Character> map) {
		this(map, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public ImmutableShort2CharOpenHashMap(Map<? extends Short, ? extends Character> map, float loadFactor) {
		short[] keys = new short[map.size()];
		char[] values = new char[keys.length];
		int index = 0;
		for(Map.Entry<? extends Short, ? extends Character> entry : map.entrySet()) {
			keys[index] = entry.getKey().shortValue();
			values[index] = entry.getValue().charValue();
			index++;
		}
		init(keys, values, 0, index, loadFactor);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 */
	public ImmutableShort2CharOpenHashMap(Short2CharMap map) {
		this(map, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
 	 */
	public ImmutableShort2CharOpenHashMap(Short2CharMap map, float loadFactor) {
		short[] keys = new short[map.size()];
		char[] values = new char[keys.length];
		int index = 0;
		for(Short2CharMap.Entry entry : Short2CharMaps.fastIterable(map)) {
			keys[index] = entry.getShortKey();
			values[index] = entry.getCharValue();
			index++;
		}
		init(keys, values, 0, index, loadFactor);
	}
	
	protected void init(short[] a, char[] b, int offset, int length, float loadFactor) {
		SanityChecks.checkArrayCapacity(a.length, offset, length);
		int newSize = HashUtil.arraySize(length+1, loadFactor);
		int newMask = newSize - 1;
		short[] newKeys = new short[newSize + 1];
		char[] newValues = new char[newSize + 1];
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
	public char put(short key, char value) { throw new UnsupportedOperationException(); }
	@Override
	public char putIfAbsent(short key, char value) { throw new UnsupportedOperationException(); }	
	@Override
	public char addTo(short key, char value) { throw new UnsupportedOperationException(); }
	@Override
	public char subFrom(short key, char value) { throw new UnsupportedOperationException(); }	
	@Override
	public char putAndMoveToFirst(short key, char value) { throw new UnsupportedOperationException(); }
	@Override
	public char putAndMoveToLast(short key, char value) { throw new UnsupportedOperationException(); }
	@Override
	public boolean moveToFirst(short key) { throw new UnsupportedOperationException(); }
	@Override
	public boolean moveToLast(short key) { throw new UnsupportedOperationException(); }
	@Override
	public char getAndMoveToFirst(short key) { throw new UnsupportedOperationException(); }
	@Override
	public char getAndMoveToLast(short key) { throw new UnsupportedOperationException(); }
	
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
	public boolean containsValue(char value) {
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
			if((value == null && values[index] == getDefaultReturnValue()) || Objects.equals(value, Character.valueOf(values[index]))) return true;
			index = (int)links[index];
		}
		return false;
	}
	
	@Override
	public char remove(short key) { throw new UnsupportedOperationException(); }
	
	@Override
	public char removeOrDefault(short key, char defaultValue) { throw new UnsupportedOperationException(); }
	
	@Override
	@Deprecated
	public Character remove(Object key) { throw new UnsupportedOperationException(); }
	
	@Override
	public boolean remove(short key, char value) { throw new UnsupportedOperationException(); }
	
	@Override
	public boolean remove(Object key, Object value) { throw new UnsupportedOperationException(); }
	
	@Override
	public char get(short key) {
		int slot = findIndex(key);
		return slot < 0 ? getDefaultReturnValue() : values[slot];
	}
	
	@Override
	public Character get(Object key) {
		int slot = findIndex(key);
		return Character.valueOf(slot < 0 ? getDefaultReturnValue() : values[slot]);
	}
	
	@Override
	public char getOrDefault(short key, char defaultValue) {
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
	public char firstCharValue() {
		if(size == 0) throw new NoSuchElementException();
		return values[firstIndex];
	}
	
	@Override
	public char lastCharValue() {
		if(size == 0) throw new NoSuchElementException();
		return values[lastIndex];
	}	

	@Override
	public ObjectSet<Short2CharMap.Entry> short2CharEntrySet() {
		if(entrySet == null) entrySet = new MapEntrySet();
		return entrySet;
	}
	
	@Override
	public ShortSet keySet() {
		if(keySet == null) keySet = new KeySet();
		return keySet;
	}
	
	@Override
	public CharCollection values() {
		if(valuesC == null) valuesC = new Values();
		return valuesC;
	}
	
	@Override
	public ImmutableShort2CharOpenHashMap copy() {
		ImmutableShort2CharOpenHashMap map = new ImmutableShort2CharOpenHashMap();
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
	public void forEach(ShortCharConsumer action) {
		int index = firstIndex;
		while(index != -1){
			action.accept(keys[index], values[index]);
			index = (int)links[index];
		}
	}
	
	@Override
	public boolean replace(short key, char oldValue, char newValue) { throw new UnsupportedOperationException(); }
	
	@Override
	public char replace(short key, char value) { throw new UnsupportedOperationException(); }
	
	@Override
	public char computeChar(short key, ShortCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
	
	@Override
	public char computeCharIfAbsent(short key, Short2CharFunction mappingFunction) { throw new UnsupportedOperationException(); }
	
	@Override
	public char supplyCharIfAbsent(short key, CharSupplier valueProvider) { throw new UnsupportedOperationException(); }
	
	@Override
	public char computeCharIfPresent(short key, ShortCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
	
	@Override
	public char mergeChar(short key, char value, CharCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
	
	@Override
	public void mergeAllChar(Short2CharMap m, CharCharUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
	
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
		int pos = HashUtil.mix(key.hashCode()) & mask;
		short current = keys[pos];
		if(current != (short)0) {
			if(Objects.equals(key, Short.valueOf(current))) return pos;
			while((current = keys[pos = (++pos & mask)]) != (short)0)
				if(Objects.equals(key, Short.valueOf(current))) return pos;
		}
		return -(pos + 1);
	}
	
	protected class MapEntry implements Short2CharMap.Entry, Map.Entry<Short, Character> {
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
		public char getCharValue() {
			return values[index];
		}

		@Override
		public char setValue(char value) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Map.Entry) {
				if(obj instanceof Short2CharMap.Entry) {
					Short2CharMap.Entry entry = (Short2CharMap.Entry)obj;
					return keys[index] == entry.getShortKey() && values[index] == entry.getCharValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				if(!(key instanceof Short)) return false;
				Object value = entry.getValue();
				return key instanceof Short && value instanceof Character && keys[index] == ((Short)key).shortValue() && values[index] == ((Character)value).charValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Short.hashCode(keys[index]) ^ Character.hashCode(values[index]);
		}
		
		@Override
		public String toString() {
			return Short.toString(keys[index]) + "=" + Character.toString(values[index]);
		}
	}
	
	private class MapEntrySet extends AbstractObjectSet<Short2CharMap.Entry> implements Short2CharOrderedMap.FastOrderedSet {
		@Override
		public boolean addAndMoveToFirst(Short2CharMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Short2CharMap.Entry o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean moveToFirst(Short2CharMap.Entry o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean moveToLast(Short2CharMap.Entry o) { throw new UnsupportedOperationException(); }
		
		@Override
		public Short2CharMap.Entry first() {
			return new BasicEntry(firstShortKey(), firstCharValue());
		}
		
		@Override
		public Short2CharMap.Entry last() {
			return new BasicEntry(lastShortKey(), lastCharValue());
		}
		
		@Override
		public Short2CharMap.Entry pollFirst() { throw new UnsupportedOperationException(); }
		
		@Override
		public Short2CharMap.Entry pollLast() { throw new UnsupportedOperationException(); }
		
		@Override
		public ObjectBidirectionalIterator<Short2CharMap.Entry> iterator() {
			return new EntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Short2CharMap.Entry> iterator(Short2CharMap.Entry fromElement) {
			return new EntryIterator(fromElement.getShortKey());
		}
		
		@Override
		public ObjectBidirectionalIterator<Short2CharMap.Entry> fastIterator() {
			return new FastEntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Short2CharMap.Entry> fastIterator(short fromElement) {
			return new FastEntryIterator(fromElement);
		}
		
		@Override
		public MapEntrySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(Consumer<? super Short2CharMap.Entry> action) {
			int index = firstIndex;
			while(index != -1){
				action.accept(new BasicEntry(keys[index], values[index]));
				index = (int)links[index];
			}
		}
		
		@Override
		public void fastForEach(Consumer<? super Short2CharMap.Entry> action) {
			BasicEntry entry = new BasicEntry();
			int index = firstIndex;
			while(index != -1){
				entry.set(keys[index], values[index]);
				action.accept(entry);
				index = (int)links[index];
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, Short2CharMap.Entry> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			int index = firstIndex;
			while(index != -1) {
				action.accept(input, new BasicEntry(keys[index], values[index]));
				index = (int)links[index];
			}
		}
		
		@Override
		public boolean matchesAny(Object2BooleanFunction<Short2CharMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			BasicEntry entry = new BasicEntry();
			int index = firstIndex;
			while(index != -1) {
				entry.set(keys[index], values[index]);
				if(filter.getBoolean(entry)) return true;
				index = (int)links[index];
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Object2BooleanFunction<Short2CharMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			BasicEntry entry = new BasicEntry();
			int index = firstIndex;
			while(index != -1) {
				entry.set(keys[index], values[index]);
				if(filter.getBoolean(entry)) return false;
				index = (int)links[index];
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Object2BooleanFunction<Short2CharMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			BasicEntry entry = new BasicEntry();
			int index = firstIndex;
			while(index != -1) {
				entry.set(keys[index], values[index]);
				if(!filter.getBoolean(entry)) return false;
				index = (int)links[index];
			}
			return true;
		}
		
		@Override
		public <E> E reduce(E identity, BiFunction<E, Short2CharMap.Entry, E> operator) {
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
		public Short2CharMap.Entry reduce(ObjectObjectUnaryOperator<Short2CharMap.Entry, Short2CharMap.Entry> operator) {
			Objects.requireNonNull(operator);
			Short2CharMap.Entry state = null;
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
		public Short2CharMap.Entry findFirst(Object2BooleanFunction<Short2CharMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return null;
			BasicEntry entry = new BasicEntry();
			int index = firstIndex;
			while(index != -1) {
				entry.set(keys[index], values[index]);
				if(filter.getBoolean(entry)) return entry;
				index = (int)links[index];
			}
			return null;
		}
		
		@Override
		public int count(Object2BooleanFunction<Short2CharMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			BasicEntry entry = new BasicEntry();
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
				if(o instanceof Short2CharMap.Entry) {
					Short2CharMap.Entry entry = (Short2CharMap.Entry)o;
					int index = ImmutableShort2CharOpenHashMap.this.findIndex(entry.getShortKey());
					if(index >= 0) return entry.getCharValue() == ImmutableShort2CharOpenHashMap.this.values[index];
				}
				else {
					Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
					int index = ImmutableShort2CharOpenHashMap.this.findIndex(entry.getKey());
					if(index >= 0) return Objects.equals(entry.getValue(), Character.valueOf(ImmutableShort2CharOpenHashMap.this.values[index]));
				}
			}
			return false;
		}
		
		@Override
		@Deprecated
		public boolean remove(Object o) { throw new UnsupportedOperationException(); }
		
		@Override
		public int size() {
			return ImmutableShort2CharOpenHashMap.this.size();
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
			return ImmutableShort2CharOpenHashMap.this.size();
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
		public boolean matchesAny(Short2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			int index = firstIndex;
			while(index != -1){
				if(filter.get(keys[index])) return true;
				index = (int)links[index];
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Short2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			int index = firstIndex;
			while(index != -1){
				if(filter.get(keys[index])) return false;
				index = (int)links[index];
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Short2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			int index = firstIndex;
			while(index != -1){
				if(!filter.get(keys[index])) return false;
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
		public short findFirst(Short2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return (short)0;
			int index = firstIndex;
			while(index != -1){
				if(filter.get(keys[index])) return keys[index];
				index = (int)links[index];
			}
			return (short)0;
		}
		
		@Override
		public int count(Short2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			int index = firstIndex;
			int result = 0;
			while(index != -1){
				if(filter.get(keys[index])) result++;
				index = (int)links[index];
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
			return ImmutableShort2CharOpenHashMap.this.size();
		}
		
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(CharConsumer action) {
			int index = firstIndex;
			while(index != -1){
				action.accept(values[index]);
				index = (int)links[index];
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectCharConsumer<E> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			int index = firstIndex;
			while(index != -1){
				action.accept(input, values[index]);
				index = (int)links[index];
			}
		}
		
		@Override
		public boolean matchesAny(Char2BooleanFunction filter) {
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
		public boolean matchesNone(Char2BooleanFunction filter) {
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
		public boolean matchesAll(Char2BooleanFunction filter) {
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
		public char reduce(char identity, CharCharUnaryOperator operator) {
			Objects.requireNonNull(operator);
			char state = identity;
			int index = firstIndex;
			while(index != -1) {
				state = operator.applyAsChar(state, values[index]);
				index = (int)links[index];
			}
			return state;
		}
		
		@Override
		public char reduce(CharCharUnaryOperator operator) {
			Objects.requireNonNull(operator);
			char state = (char)0;
			boolean empty = true;
			int index = firstIndex;
			while(index != -1) {
				if(empty) {
					empty = false;
					state = values[index];
					index = (int)links[index];
					continue;
				}
				state = operator.applyAsChar(state, values[index]);
				index = (int)links[index];
			}
			return state;
		}
		
		@Override
		public char findFirst(Char2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return (char)0;
			int index = firstIndex;
			while(index != -1){
				if(filter.get(values[index])) return values[index];
				index = (int)links[index];
			}
			return (char)0;
		}
		
		@Override
		public int count(Char2BooleanFunction filter) {
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
	
	private class FastEntryIterator extends MapIterator implements ObjectListIterator<Short2CharMap.Entry> {
		MapEntry entry = new MapEntry();
		
		public FastEntryIterator() {}
		public FastEntryIterator(short from) {
			super(from);
		}
		
		@Override
		public Short2CharMap.Entry next() {
			entry.index = nextEntry();
			return entry;
		}
		
		@Override
		public Short2CharMap.Entry previous() {
			entry.index = previousEntry();
			return entry;
		}
		
		@Override
		public void set(Short2CharMap.Entry entry) { throw new UnsupportedOperationException(); }

		@Override
		public void add(Short2CharMap.Entry entry) { throw new UnsupportedOperationException(); }
	}
	
	private class EntryIterator extends MapIterator implements ObjectListIterator<Short2CharMap.Entry> {
		
		public EntryIterator() {}
		public EntryIterator(short from) {
			super(from);
		}
		
		@Override
		public Short2CharMap.Entry next() {
			return new MapEntry(nextEntry());
		}
		
		@Override
		public Short2CharMap.Entry previous() {
			return new MapEntry(previousEntry());
		}
		
		@Override
		public void remove() { throw new UnsupportedOperationException(); }

		@Override
		public void set(Short2CharMap.Entry entry) { throw new UnsupportedOperationException(); }

		@Override
		public void add(Short2CharMap.Entry entry) { throw new UnsupportedOperationException(); }
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
	
	private class ValueIterator extends MapIterator implements CharListIterator {
		public ValueIterator() {}
		
		@Override
		public char previousChar() {
			return values[previousEntry()];
		}

		@Override
		public char nextChar() {
			return values[nextEntry()];
		}

		@Override
		public void set(char e) { throw new UnsupportedOperationException(); }

		@Override
		public void add(char e) { throw new UnsupportedOperationException(); }
		
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