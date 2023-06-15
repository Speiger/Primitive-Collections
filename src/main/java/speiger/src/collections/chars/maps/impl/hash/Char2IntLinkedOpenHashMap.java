package speiger.src.collections.chars.maps.impl.hash;

import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.IntPredicate;

import speiger.src.collections.chars.collections.CharBidirectionalIterator;
import speiger.src.collections.chars.functions.CharConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectCharConsumer;
import speiger.src.collections.ints.functions.consumer.IntCharConsumer;
import speiger.src.collections.ints.functions.consumer.IntObjectConsumer;
import speiger.src.collections.ints.functions.consumer.IntIntConsumer;
import speiger.src.collections.chars.functions.function.CharPredicate;
import speiger.src.collections.chars.functions.consumer.CharIntConsumer;
import speiger.src.collections.chars.functions.function.CharCharUnaryOperator;
import speiger.src.collections.chars.lists.CharListIterator;
import speiger.src.collections.chars.maps.interfaces.Char2IntMap;
import speiger.src.collections.chars.maps.interfaces.Char2IntOrderedMap;
import speiger.src.collections.chars.sets.AbstractCharSet;
import speiger.src.collections.chars.sets.CharOrderedSet;
import speiger.src.collections.ints.collections.AbstractIntCollection;
import speiger.src.collections.ints.collections.IntCollection;
import speiger.src.collections.ints.collections.IntIterator;
import speiger.src.collections.ints.functions.function.IntIntUnaryOperator;
import speiger.src.collections.ints.functions.IntConsumer;
import speiger.src.collections.ints.lists.IntListIterator;
import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectIntConsumer;

import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;
import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
import speiger.src.collections.utils.HashUtil;

/**
 * A Type Specific LinkedHashMap implementation that uses specific arrays to create links between nodes to remove the wrapping of elements
 * to greatly reduce memory usage. In Addition adding some helper methods to move around elements.
 * This implementation of SortedMap does not support SubMaps of any kind. It implements the interface due to sortability and first/last access
 */
public class Char2IntLinkedOpenHashMap extends Char2IntOpenHashMap implements Char2IntOrderedMap
{
	/** The Backing array for links between nodes. Left 32 Bits => Previous Entry, Right 32 Bits => Next Entry */
	protected transient long[] links;
	/** The First Index in the Map */
	protected int firstIndex = -1;
	/** The Last Index in the Map */
	protected int lastIndex = -1;
	
	/**
	 * Default Constructor
	 */
	public Char2IntLinkedOpenHashMap() {
		this(HashUtil.DEFAULT_MIN_CAPACITY, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * Constructor that defines the minimum capacity
	 * @param minCapacity the minimum capacity the HashMap is allowed to be.
	 * @throws IllegalStateException if the minimum capacity is negative
	 */
	public Char2IntLinkedOpenHashMap(int minCapacity) {
		this(minCapacity, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * Constructor that defines the minimum capacity and load factor
	 * @param minCapacity the minimum capacity the HashMap is allowed to be.
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the minimum capacity is negative
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public Char2IntLinkedOpenHashMap(int minCapacity, float loadFactor) {
		super(minCapacity, loadFactor);
		links = new long[nullIndex + 1];
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Char2IntLinkedOpenHashMap(Character[] keys, Integer[] values) {
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
	public Char2IntLinkedOpenHashMap(Character[] keys, Integer[] values, float loadFactor) {
		this(keys.length, loadFactor);
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i].charValue(), values[i].intValue());
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Char2IntLinkedOpenHashMap(char[] keys, int[] values) {
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
	public Char2IntLinkedOpenHashMap(char[] keys, int[] values, float loadFactor) {
		this(keys.length, loadFactor);
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i], values[i]);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 */
	public Char2IntLinkedOpenHashMap(Map<? extends Character, ? extends Integer> map) {
		this(map, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public Char2IntLinkedOpenHashMap(Map<? extends Character, ? extends Integer> map, float loadFactor) {
		this(map.size(), loadFactor);
		putAll(map);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 */
	public Char2IntLinkedOpenHashMap(Char2IntMap map) {
		this(map, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
 	 */
	public Char2IntLinkedOpenHashMap(Char2IntMap map, float loadFactor) {
		this(map.size(), loadFactor);
		putAll(map);
	}
	
	@Override
	public int putAndMoveToFirst(char key, int value) {
		if(key == (char)0) {
			if(containsNull) {
				int lastValue = values[nullIndex];
				values[nullIndex] = value;
				moveToFirstIndex(nullIndex);
				return lastValue;
			}
			values[nullIndex] = value;
			containsNull = true;
			onNodeAdded(nullIndex);
			moveToFirstIndex(nullIndex);
		}
		else {
			int pos = HashUtil.mix(Character.hashCode(key)) & mask;
			while(keys[pos] != (char)0) {
				if(keys[pos] == key) {
					int lastValue = values[pos];
					values[pos] = value;
					moveToFirstIndex(pos);
					return lastValue;
				}
				pos = ++pos & mask;
			}
			keys[pos] = key;
			values[pos] = value;
			onNodeAdded(pos);
			moveToFirstIndex(pos);
		}
		if(size++ >= maxFill) rehash(HashUtil.arraySize(size+1, loadFactor));
		return getDefaultReturnValue();
	}
	
	@Override
	public int putAndMoveToLast(char key, int value) {
		if(key == (char)0) {
			if(containsNull) {
				int lastValue = values[nullIndex];
				values[nullIndex] = value;
				moveToLastIndex(nullIndex);
				return lastValue;
			}
			values[nullIndex] = value;
			containsNull = true;
			onNodeAdded(nullIndex);
			moveToLastIndex(nullIndex);
		}
		else {
			int pos = HashUtil.mix(Character.hashCode(key)) & mask;
			while(keys[pos] != (char)0) {
				if(keys[pos] == key) {
					int lastValue = values[pos];
					values[pos] = value;
					moveToLastIndex(pos);
					return lastValue;
				}
				pos = ++pos & mask;
			}
			keys[pos] = key;
			values[pos] = value;
			onNodeAdded(pos);
			moveToLastIndex(pos);
		}
		if(size++ >= maxFill) rehash(HashUtil.arraySize(size+1, loadFactor));
		return getDefaultReturnValue();
	}
	
	@Override
	public boolean moveToFirst(char key) {
		if(isEmpty() || firstCharKey() == key) return false;
		if(key == (char)0) {
			if(containsNull) {
				moveToFirstIndex(nullIndex);
				return true;
			}
		}
		else {
			int pos = HashUtil.mix(Character.hashCode(key)) & mask;
			while(keys[pos] != (char)0) {
				if(keys[pos] == key) {
					moveToFirstIndex(pos);
					return true;
				}
				pos = ++pos & mask;
			}
		}
		return false;
	}
	
	@Override
	public boolean moveToLast(char key) {
		if(isEmpty() || lastCharKey() == key) return false;
		if(key == (char)0) {
			if(containsNull) {
				moveToLastIndex(nullIndex);
				return true;
			}
		}
		else {
			int pos = HashUtil.mix(Character.hashCode(key)) & mask;
			while(keys[pos] != (char)0) {
				if(keys[pos] == key) {
					moveToLastIndex(pos);
					return true;
				}
				pos = ++pos & mask;
			}
		}
		return false;
	}
	
	@Override
	public int getAndMoveToFirst(char key) {
		int index = findIndex(key);
		if(index < 0) return getDefaultReturnValue();
		moveToFirstIndex(index);
		return values[index];
	}
	
	@Override
	public int getAndMoveToLast(char key) {
		int index = findIndex(key);
		if(index < 0) return getDefaultReturnValue();
		moveToLastIndex(index);
		return values[index];
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
	public Char2IntLinkedOpenHashMap copy() {
		Char2IntLinkedOpenHashMap map = new Char2IntLinkedOpenHashMap(0, loadFactor);
		map.minCapacity = minCapacity;
		map.mask = mask;
		map.maxFill = maxFill;
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
	public char firstCharKey() {
		if(size == 0) throw new NoSuchElementException();
		return keys[firstIndex];
	}
	
	@Override
	public char pollFirstCharKey() {
		if(size == 0) throw new NoSuchElementException();
		int pos = firstIndex;
		onNodeRemoved(pos);
		char result = keys[pos];
		size--;
		if(result == (char)0) {
			containsNull = false;
			keys[nullIndex] = (char)0;
			values[nullIndex] = 0;
		}
		else shiftKeys(pos);
		if(nullIndex > minCapacity && size < maxFill / 4 && nullIndex > HashUtil.DEFAULT_MIN_CAPACITY) rehash(nullIndex / 2);
		return result;
	}
	
	@Override
	public char lastCharKey() {
		if(size == 0) throw new NoSuchElementException();
		return keys[lastIndex];
	}
	
	@Override
	public char pollLastCharKey() {
		if(size == 0) throw new NoSuchElementException();
		int pos = lastIndex;
		onNodeRemoved(pos);
		char result = keys[pos];
		size--;
		if(result == (char)0) {
			containsNull = false;
			keys[nullIndex] = (char)0;
			values[nullIndex] = 0;
		}
		else shiftKeys(pos);
		if(nullIndex > minCapacity && size < maxFill / 4 && nullIndex > HashUtil.DEFAULT_MIN_CAPACITY) rehash(nullIndex / 2);
		return result;
	}
	
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
	public ObjectOrderedSet<Char2IntMap.Entry> char2IntEntrySet() {
		if(entrySet == null) entrySet = new MapEntrySet();
		return (ObjectOrderedSet<Char2IntMap.Entry>)entrySet;
	}
	
	@Override
	public CharOrderedSet keySet() {
		if(keySet == null) keySet = new KeySet();
		return (CharOrderedSet)keySet;
	}
	
	@Override
	public IntCollection values() {
		if(valuesC == null) valuesC = new Values();
		return valuesC;
	}
	
	@Override
	public void forEach(CharIntConsumer action) {
		int index = firstIndex;
		while(index != -1){
			action.accept(keys[index], values[index]);
			index = (int)links[index];
		}
	}
	
	@Override
	public void clear() {
		super.clear();
		firstIndex = lastIndex = -1;
	}
	
	@Override
	public void clearAndTrim(int size) {
		int request = Math.max(minCapacity, HashUtil.nextPowerOfTwo((int)Math.ceil(size / loadFactor)));
		if(request >= nullIndex) {
			clear();
			return;
		}
		nullIndex = request;
		mask = request-1;
		maxFill = Math.min((int)Math.ceil(nullIndex * loadFactor), nullIndex - 1);
		keys = new char[request + 1];
		values = new int[request + 1];
		links = new long[request + 1];
		firstIndex = lastIndex = -1;
		this.size = 0;
		containsNull = false;
	}
	
	protected void moveToFirstIndex(int startPos) {
		if(size == 1 || firstIndex == startPos) return;
		if(lastIndex == startPos) {
			lastIndex = (int)(links[startPos] >>> 32);
			links[lastIndex] |= 0xFFFFFFFFL;
		}
		else {
			long link = links[startPos];
			int prev = (int)(link >>> 32);
			int next = (int)link;
			links[prev] ^= ((links[prev] ^ (link & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
			links[next] ^= ((links[next] ^ (link & 0xFFFFFFFF00000000L)) & 0xFFFFFFFF00000000L);
		}
		links[firstIndex] ^= ((links[firstIndex] ^ ((startPos & 0xFFFFFFFFL) << 32)) & 0xFFFFFFFF00000000L);
		links[startPos] = 0xFFFFFFFF00000000L | (firstIndex & 0xFFFFFFFFL);
		firstIndex = startPos;
	}
	
	protected void moveToLastIndex(int startPos) {
		if(size == 1 || lastIndex == startPos) return;
		if(firstIndex == startPos) {
			firstIndex = (int)links[startPos];
			links[lastIndex] |= 0xFFFFFFFF00000000L;
		}
		else {
			long link = links[startPos];
			int prev = (int)(link >>> 32);
			int next = (int)link;
			links[prev] ^= ((links[prev] ^ (link & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
			links[next] ^= ((links[next] ^ (link & 0xFFFFFFFF00000000L)) & 0xFFFFFFFF00000000L);
		}
		links[lastIndex] ^= ((links[lastIndex] ^ (startPos & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
		links[startPos] = ((lastIndex & 0xFFFFFFFFL) << 32) | 0xFFFFFFFFL;
		lastIndex = startPos;
	}
	
	@Override
	protected void onNodeAdded(int pos) {
		if(size == 0) {
			firstIndex = lastIndex = pos;
			links[pos] = -1L;
		}
		else {
			links[lastIndex] ^= ((links[lastIndex] ^ (pos & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
			links[pos] = ((lastIndex & 0xFFFFFFFFL) << 32) | 0xFFFFFFFFL;
			lastIndex = pos;
		}
	}
	
	@Override
	protected void onNodeRemoved(int pos) {
		if(size == 0) firstIndex = lastIndex = -1;
		else if(firstIndex == pos) {
			firstIndex = (int)links[pos];
			if(0 <= firstIndex) links[firstIndex] |= 0xFFFFFFFF00000000L;
		}
		else if(lastIndex == pos) {
			lastIndex = (int)(links[pos] >>> 32);
			if(0 <= lastIndex) links[lastIndex] |= 0xFFFFFFFFL;
		}
		else {
			long link = links[pos];
			int prev = (int)(link >>> 32);
			int next = (int)link;
			links[prev] ^= ((links[prev] ^ (link & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
			links[next] ^= ((links[next] ^ (link & 0xFFFFFFFF00000000L)) & 0xFFFFFFFF00000000L);
		}
	}
	
	@Override
	protected void onNodeMoved(int from, int to) {
		if(size == 1) {
			firstIndex = lastIndex = to;
			links[to] = -1L;
		}
		else if(firstIndex == from) {
			firstIndex = to;
			links[(int)links[from]] ^= ((links[(int)links[from]] ^ ((to & 0xFFFFFFFFL) << 32)) & 0xFFFFFFFF00000000L);
			links[to] = links[from];
		}
		else if(lastIndex == from) {
			lastIndex = to;
			links[(int)(links[from] >>> 32)] ^= ((links[(int)(links[from] >>> 32)] ^ (to & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
			links[to] = links[from];
		}
		else {
			long link = links[from];
			int prev = (int)(link >>> 32);
			int next = (int)link;
			links[prev] ^= ((links[prev] ^ (to & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
			links[next] ^= ((links[next] ^ ((to & 0xFFFFFFFFL) << 32)) & 0xFFFFFFFF00000000L);
			links[to] = link;
		}
	}
	
	@Override
	protected void rehash(int newSize) {
		int newMask = newSize - 1;
		char[] newKeys = new char[newSize + 1];
		int[] newValues = new int[newSize + 1];
		long[] newLinks = new long[newSize + 1];
		int i = firstIndex, prev = -1, newPrev = -1, pos;
		firstIndex = -1;
		for(int j = size; j-- != 0;) {
			if(keys[i] == (char)0) pos = newSize;
			else {
				pos = HashUtil.mix(Character.hashCode(keys[i])) & newMask;
				while(newKeys[pos] != (char)0) pos = ++pos & newMask;
			}
			newKeys[pos] = keys[i];
			newValues[pos] = values[i];
			if(prev != -1) {
				newLinks[newPrev] ^= ((newLinks[newPrev] ^ (pos & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
				newLinks[pos] ^= ((newLinks[pos] ^ ((newPrev & 0xFFFFFFFFL) << 32)) & 0xFFFFFFFF00000000L);
				newPrev = pos;
			}
			else {
				newPrev = firstIndex = pos;
				newLinks[pos] = -1L;
			}
			i = (int)links[prev = i];
		}
		links = newLinks;
		lastIndex = newPrev;
		if(newPrev != -1) newLinks[newPrev] |= 0xFFFFFFFFL;
		nullIndex = newSize;
		mask = newMask;
		maxFill = Math.min((int)Math.ceil(nullIndex * loadFactor), nullIndex - 1);
		keys = newKeys;
		values = newValues;
	}
	
	private class MapEntrySet extends AbstractObjectSet<Char2IntMap.Entry> implements Char2IntOrderedMap.FastOrderedSet {
		@Override
		public boolean addAndMoveToFirst(Char2IntMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Char2IntMap.Entry o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean moveToFirst(Char2IntMap.Entry o) {
			return Char2IntLinkedOpenHashMap.this.moveToFirst(o.getCharKey());
		}
		
		@Override
		public boolean moveToLast(Char2IntMap.Entry o) {
			return Char2IntLinkedOpenHashMap.this.moveToLast(o.getCharKey());
		}
		
		@Override
		public Char2IntMap.Entry first() {
			return new BasicEntry(firstCharKey(), firstIntValue());
		}
		
		@Override
		public Char2IntMap.Entry last() {
			return new BasicEntry(lastCharKey(), lastIntValue());
		}
		
		@Override
		public Char2IntMap.Entry pollFirst() {
			BasicEntry entry = new BasicEntry(firstCharKey(), firstIntValue());
			pollFirstCharKey();
			return entry;
		}
		
		@Override
		public Char2IntMap.Entry pollLast() {
			BasicEntry entry = new BasicEntry(lastCharKey(), lastIntValue());
			pollLastCharKey();
			return entry;
		}
		
		@Override
		public ObjectBidirectionalIterator<Char2IntMap.Entry> iterator() {
			return new EntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Char2IntMap.Entry> iterator(Char2IntMap.Entry fromElement) {
			return new EntryIterator(fromElement.getCharKey());
		}
		
		@Override
		public ObjectBidirectionalIterator<Char2IntMap.Entry> fastIterator() {
			return new FastEntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Char2IntMap.Entry> fastIterator(char fromElement) {
			return new FastEntryIterator(fromElement);
		}
		
		@Override
		public MapEntrySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(Consumer<? super Char2IntMap.Entry> action) {
			int index = firstIndex;
			while(index != -1){
				action.accept(new ValueMapEntry(index));
				index = (int)links[index];
			}
		}
		
		@Override
		public void fastForEach(Consumer<? super Char2IntMap.Entry> action) {
			MapEntry entry = new MapEntry();
			int index = firstIndex;
			while(index != -1){
				entry.set(index);
				action.accept(entry);
				index = (int)links[index];
			}
		}
		
		@Override
		public void forEachIndexed(IntObjectConsumer<Char2IntMap.Entry> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			int count = 0;
			int index = firstIndex;
			while(index != -1) {
				action.accept(count++, new ValueMapEntry(index));
				index = (int)links[index];
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, Char2IntMap.Entry> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			int index = firstIndex;
			while(index != -1) {
				action.accept(input, new ValueMapEntry(index));
				index = (int)links[index];
			}
		}
		
		@Override
		public boolean matchesAny(Predicate<Char2IntMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			MapEntry entry = new MapEntry();
			int index = firstIndex;
			while(index != -1) {
				entry.set(index);
				if(filter.test(entry)) return true;
				index = (int)links[index];
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Predicate<Char2IntMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			MapEntry entry = new MapEntry();
			int index = firstIndex;
			while(index != -1) {
				entry.set(index);
				if(filter.test(entry)) return false;
				index = (int)links[index];
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Predicate<Char2IntMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			MapEntry entry = new MapEntry();
			int index = firstIndex;
			while(index != -1) {
				entry.set(index);
				if(!filter.test(entry)) return false;
				index = (int)links[index];
			}
			return true;
		}
		
		@Override
		public <E> E reduce(E identity, BiFunction<E, Char2IntMap.Entry, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			int index = firstIndex;
			while(index != -1) {
				state = operator.apply(state, new ValueMapEntry(index));
				index = (int)links[index];
			}
			return state;
		}
		
		@Override
		public Char2IntMap.Entry reduce(ObjectObjectUnaryOperator<Char2IntMap.Entry, Char2IntMap.Entry> operator) {
			Objects.requireNonNull(operator);
			Char2IntMap.Entry state = null;
			boolean empty = true;
			int index = firstIndex;
			while(index != -1) {
				if(empty) {
					empty = false;
					state = new ValueMapEntry(index);
					index = (int)links[index];
					continue;
				}
				state = operator.apply(state, new ValueMapEntry(index));
				index = (int)links[index];
			}
			return state;
		}
		
		@Override
		public Char2IntMap.Entry findFirst(Predicate<Char2IntMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return null;
			MapEntry entry = new MapEntry();
			int index = firstIndex;
			while(index != -1) {
				entry.set(index);
				if(filter.test(entry)) return entry;
				index = (int)links[index];
			}
			return null;
		}
		
		@Override
		public int count(Predicate<Char2IntMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			int result = 0;
			MapEntry entry = new MapEntry();
			int index = firstIndex;
			while(index != -1) {
				entry.set(index);
				if(filter.test(entry)) result++;
				index = (int)links[index];
			}
			return result;
		}
		
		@Override
		@Deprecated
		public boolean contains(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Char2IntMap.Entry) {
					Char2IntMap.Entry entry = (Char2IntMap.Entry)o;
					int index = Char2IntLinkedOpenHashMap.this.findIndex(entry.getCharKey());
					if(index >= 0) return entry.getIntValue() == Char2IntLinkedOpenHashMap.this.values[index];
				}
				else {
					Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
					int index = Char2IntLinkedOpenHashMap.this.findIndex(entry.getKey());
					if(index >= 0) return Objects.equals(entry.getValue(), Integer.valueOf(Char2IntLinkedOpenHashMap.this.values[index]));
				}
			}
			return false;
		}
		
		@Override
		@Deprecated
		public boolean remove(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Char2IntMap.Entry) {
					Char2IntMap.Entry entry = (Char2IntMap.Entry)o;
					return Char2IntLinkedOpenHashMap.this.remove(entry.getCharKey(), entry.getIntValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
				return Char2IntLinkedOpenHashMap.this.remove(entry.getKey(), entry.getValue());
			}
			return false;
		}
		
		@Override
		public int size() {
			return Char2IntLinkedOpenHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Char2IntLinkedOpenHashMap.this.clear();
		}
	}
	
	private final class KeySet extends AbstractCharSet implements CharOrderedSet {
		@Override
		public boolean contains(char e) {
			return containsKey(e);
		}
		
		@Override
		public boolean remove(char o) {
			int oldSize = size;
			Char2IntLinkedOpenHashMap.this.remove(o);
			return size != oldSize;
		}
		
		@Override
		public boolean add(char o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAndMoveToFirst(char o) { throw new UnsupportedOperationException(); }

		@Override
		public boolean addAndMoveToLast(char o) { throw new UnsupportedOperationException(); }

		@Override
		public boolean moveToFirst(char o) {
			return Char2IntLinkedOpenHashMap.this.moveToFirst(o);
		}

		@Override
		public boolean moveToLast(char o) {
			return Char2IntLinkedOpenHashMap.this.moveToLast(o);
		}
		
		@Override
		public CharListIterator iterator() {
			return new KeyIterator();
		}
		
		@Override
		public CharBidirectionalIterator iterator(char fromElement) {
			return new KeyIterator(fromElement);
		}
		
		@Override
		public KeySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public int size() {
			return Char2IntLinkedOpenHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Char2IntLinkedOpenHashMap.this.clear();
		}
		
		@Override
		public char firstChar() {
			return firstCharKey();
		}
		
		@Override
		public char pollFirstChar() {
			return pollFirstCharKey();
		}

		@Override
		public char lastChar() {
			return lastCharKey();
		}

		@Override
		public char pollLastChar() {
			return pollLastCharKey();
		}
		
		@Override
		public void forEach(CharConsumer action) {
			int index = firstIndex;
			while(index != -1){
				action.accept(keys[index]);
				index = (int)links[index];
			}
		}
		
		@Override
		public void forEachIndexed(IntCharConsumer action) {
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
		public <E> void forEach(E input, ObjectCharConsumer<E> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			int index = firstIndex;
			while(index != -1){
				action.accept(input, keys[index]);
				index = (int)links[index];
			}
		}
		
		@Override
		public boolean matchesAny(CharPredicate filter) {
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
		public boolean matchesNone(CharPredicate filter) {
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
		public boolean matchesAll(CharPredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			int index = firstIndex;
			while(index != -1) {
				if(!filter.test(keys[index])) return false;
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
				state = operator.applyAsChar(state, keys[index]);
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
					state = keys[index];
					index = (int)links[index];
					continue;
				}
				state = operator.applyAsChar(state, keys[index]);
				index = (int)links[index];
			}
			return state;
		}
		
		@Override
		public char findFirst(CharPredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return (char)0;
			int index = firstIndex;
			while(index != -1){
				if(filter.test(keys[index])) return keys[index];
				index = (int)links[index];
			}
			return (char)0;
		}
		
		@Override
		public int count(CharPredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			int result = 0;
			int index = firstIndex;
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
			return Char2IntLinkedOpenHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Char2IntLinkedOpenHashMap.this.clear();
		}
		
		@Override
		public void forEach(IntConsumer action) {
			Objects.requireNonNull(action);
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
			while(index != -1) {
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
			while(index != -1) {
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
	
	private class FastEntryIterator extends MapIterator implements ObjectListIterator<Char2IntMap.Entry> {
		MapEntry entry = new MapEntry();
		
		public FastEntryIterator() {}
		public FastEntryIterator(char from) {
			super(from);
		}
		
		@Override
		public Char2IntMap.Entry next() {
			entry.index = nextEntry();
			return entry;
		}
		
		@Override
		public Char2IntMap.Entry previous() {
			entry.index = previousEntry();
			return entry;
		}
		
		@Override
		public void set(Char2IntMap.Entry entry) { throw new UnsupportedOperationException(); }

		@Override
		public void add(Char2IntMap.Entry entry) { throw new UnsupportedOperationException(); }
	}
	
	private class EntryIterator extends MapIterator implements ObjectListIterator<Char2IntMap.Entry> {
		MapEntry entry;
		
		public EntryIterator() {}
		public EntryIterator(char from) {
			super(from);
		}
		
		@Override
		public Char2IntMap.Entry next() {
			return entry = new ValueMapEntry(nextEntry());
		}
		
		@Override
		public Char2IntMap.Entry previous() {
			return entry = new ValueMapEntry(previousEntry());
		}
		
		@Override
		public void remove() {
			super.remove();
			entry.index = -1;
		}

		@Override
		public void set(Char2IntMap.Entry entry) { throw new UnsupportedOperationException(); }

		@Override
		public void add(Char2IntMap.Entry entry) { throw new UnsupportedOperationException(); }
	}
	
	private class KeyIterator extends MapIterator implements CharListIterator {
		
		public KeyIterator() {}
		public KeyIterator(char from) {
			super(from);
		}
		
		@Override
		public char previousChar() {
			return keys[previousEntry()];
		}

		@Override
		public char nextChar() {
			return keys[nextEntry()];
		}
		
		@Override
		public void set(char e) { throw new UnsupportedOperationException(); }
		@Override
		public void add(char e) { throw new UnsupportedOperationException(); }
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
		
		MapIterator(char from) {
			if(from == (char)0) {
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
				int pos = HashUtil.mix(Character.hashCode(from)) & mask;
				while(keys[pos] != (char)0) {
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
		
		public void remove() {
			if(current == -1) throw new IllegalStateException();
			ensureIndexKnown();
			if(current == previous) {
				index--;
				previous = (int)(links[current] >>> 32);
			}
			else next = (int)links[current];
			size--;
			if(previous == -1) firstIndex = next;
			else links[previous] ^= ((links[previous] ^ (next & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
			
			if(next == -1) lastIndex = previous;
			else links[next] ^= ((links[next] ^ ((previous & 0xFFFFFFFFL) << 32)) & 0xFFFFFFFF00000000L);
			if(current == nullIndex) {
				current = -1;
				containsNull = false;
				keys[nullIndex] = (char)0;
				values[nullIndex] = 0;
			}
			else {
				int slot, last, startPos = current;
				current = -1;
				char current;
				while(true) {
					startPos = ((last = startPos) + 1) & mask;
					while(true){
						if((current = keys[startPos]) == (char)0) {
							keys[last] = (char)0;
							values[last] = 0;
							return;
						}
						slot = HashUtil.mix(Character.hashCode(current)) & mask;
						if(last <= startPos ? (last >= slot || slot > startPos) : (last >= slot && slot > startPos)) break;
						startPos = ++startPos & mask;
					}
					keys[last] = current;
					values[last] = values[startPos];
					if(next == startPos) next = last;
					if(previous == startPos) previous = last;
					onNodeMoved(startPos, last);
				}
			}
		}
		
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