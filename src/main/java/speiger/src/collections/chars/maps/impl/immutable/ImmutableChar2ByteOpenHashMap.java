package speiger.src.collections.chars.maps.impl.immutable;

import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import speiger.src.collections.chars.collections.CharBidirectionalIterator;
import speiger.src.collections.chars.functions.CharConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectCharConsumer;
import speiger.src.collections.ints.functions.consumer.IntCharConsumer;
import speiger.src.collections.ints.functions.consumer.IntObjectConsumer;
import speiger.src.collections.ints.functions.consumer.IntByteConsumer;
import speiger.src.collections.chars.functions.OptionalChar;
import speiger.src.collections.chars.functions.function.CharPredicate;
import speiger.src.collections.chars.functions.consumer.CharByteConsumer;
import speiger.src.collections.chars.functions.function.Char2ByteFunction;
import speiger.src.collections.chars.functions.function.CharByteUnaryOperator;
import speiger.src.collections.chars.functions.function.CharCharUnaryOperator;
import speiger.src.collections.chars.lists.CharListIterator;
import speiger.src.collections.chars.maps.interfaces.Char2ByteOrderedMap;
import speiger.src.collections.chars.maps.abstracts.AbstractChar2ByteMap;
import speiger.src.collections.chars.maps.interfaces.Char2ByteMap;
import speiger.src.collections.chars.sets.CharOrderedSet;
import speiger.src.collections.chars.utils.CharArrays;
import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectByteConsumer;

import speiger.src.collections.bytes.functions.function.BytePredicate;
import speiger.src.collections.bytes.functions.OptionalByte;
import speiger.src.collections.chars.sets.AbstractCharSet;
import speiger.src.collections.bytes.collections.AbstractByteCollection;
import speiger.src.collections.bytes.collections.ByteOrderedCollection;
import speiger.src.collections.bytes.collections.ByteIterator;
import speiger.src.collections.bytes.functions.ByteSupplier;
import speiger.src.collections.bytes.functions.function.ByteByteUnaryOperator;
import speiger.src.collections.bytes.functions.ByteConsumer;
import speiger.src.collections.bytes.lists.ByteListIterator;
import speiger.src.collections.bytes.utils.ByteArrays;
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
public class ImmutableChar2ByteOpenHashMap extends AbstractChar2ByteMap implements Char2ByteOrderedMap
{
	/** The Backing keys array */
	protected transient char[] keys;
	/** The Backing values array */
	protected transient byte[] values;
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
	protected transient CharOrderedSet keySet;
	/** Values cache */
	protected transient ByteOrderedCollection valuesC;
	
	/** Amount of Elements stored in the HashMap */
	protected int size;
	
	/**
	 * Helper constructor for copying the Map
	 */
	protected ImmutableChar2ByteOpenHashMap() {}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public ImmutableChar2ByteOpenHashMap(Character[] keys, Byte[] values) {
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
	public ImmutableChar2ByteOpenHashMap(Character[] keys, Byte[] values, float loadFactor) {
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		init(CharArrays.unwrap(keys), ByteArrays.unwrap(values), 0, keys.length, loadFactor);
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public ImmutableChar2ByteOpenHashMap(char[] keys, byte[] values) {
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
	public ImmutableChar2ByteOpenHashMap(char[] keys, byte[] values, float loadFactor) {
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		init(keys, values, 0, keys.length, loadFactor);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 */
	public ImmutableChar2ByteOpenHashMap(Map<? extends Character, ? extends Byte> map) {
		this(map, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public ImmutableChar2ByteOpenHashMap(Map<? extends Character, ? extends Byte> map, float loadFactor) {
		char[] keys = new char[map.size()];
		byte[] values = new byte[keys.length];
		int index = 0;
		for(Map.Entry<? extends Character, ? extends Byte> entry : map.entrySet()) {
			keys[index] = entry.getKey().charValue();
			values[index] = entry.getValue().byteValue();
			index++;
		}
		init(keys, values, 0, index, loadFactor);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 */
	public ImmutableChar2ByteOpenHashMap(Char2ByteMap map) {
		this(map, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
 	 */
	public ImmutableChar2ByteOpenHashMap(Char2ByteMap map, float loadFactor) {
		char[] keys = new char[map.size()];
		byte[] values = new byte[keys.length];
		int index = 0;
		for(Char2ByteMap.Entry entry : getFastIterable(map)) {
			keys[index] = entry.getCharKey();
			values[index] = entry.getByteValue();
			index++;
		}
		init(keys, values, 0, index, loadFactor);
	}
	
	protected void init(char[] a, byte[] b, int offset, int length, float loadFactor) {
		SanityChecks.checkArrayCapacity(a.length, offset, length);
		int newSize = HashUtil.arraySize(length+1, loadFactor);
		int newMask = newSize - 1;
		char[] newKeys = new char[newSize + 1];
		byte[] newValues = new byte[newSize + 1];
		long[] newLinks = new long[newSize + 1];
		int prev = -1;
		for(int i = offset,m=offset+length;i<m;i++)
		{
			char o = a[i];
			if(o == (char)0) {
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
			int pos = HashUtil.mix(Character.hashCode(o)) & newMask;
			char current = newKeys[pos];
			if(current != (char)0) {
				if(current == o) {
					newValues[pos] = b[i];
					continue;
				}
				while((current = newKeys[pos = (++pos & newMask)]) != (char)0) {
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
	public byte put(char key, byte value) { throw new UnsupportedOperationException(); }
	@Override
	public byte putIfAbsent(char key, byte value) { throw new UnsupportedOperationException(); }	
	@Override
	public byte addTo(char key, byte value) { throw new UnsupportedOperationException(); }
	@Override
	public byte subFrom(char key, byte value) { throw new UnsupportedOperationException(); }	
	@Override
	public byte putAndMoveToFirst(char key, byte value) { throw new UnsupportedOperationException(); }
	@Override
	public byte putAndMoveToLast(char key, byte value) { throw new UnsupportedOperationException(); }
	@Override
	public byte putFirst(char key, byte value) { throw new UnsupportedOperationException(); }
	@Override
	public byte putLast(char key, byte value) { throw new UnsupportedOperationException(); }
	@Override
	public boolean moveToFirst(char key) { throw new UnsupportedOperationException(); }
	@Override
	public boolean moveToLast(char key) { throw new UnsupportedOperationException(); }
	@Override
	public byte getAndMoveToFirst(char key) { throw new UnsupportedOperationException(); }
	@Override
	public byte getAndMoveToLast(char key) { throw new UnsupportedOperationException(); }
	
	@Override
	public boolean containsKey(char key) {
		return findIndex(key) >= 0;
	}
	
	@Override
	@Deprecated
	public boolean containsKey(Object key) {
		return findIndex(key) >= 0;
	}
	
	@Override
	public boolean containsValue(byte value) {
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
			if((value == null && values[index] == getDefaultReturnValue()) || Objects.equals(value, Byte.valueOf(values[index]))) return true;
			index = (int)links[index];
		}
		return false;
	}
	
	@Override
	public byte remove(char key) { throw new UnsupportedOperationException(); }
	
	@Override
	public byte removeOrDefault(char key, byte defaultValue) { throw new UnsupportedOperationException(); }
	
	@Override
	@Deprecated
	public Byte remove(Object key) { throw new UnsupportedOperationException(); }
	
	@Override
	public boolean remove(char key, byte value) { throw new UnsupportedOperationException(); }
	
	@Override
	public boolean remove(Object key, Object value) { throw new UnsupportedOperationException(); }
	
	@Override
	public byte get(char key) {
		int slot = findIndex(key);
		return slot < 0 ? getDefaultReturnValue() : values[slot];
	}
	
	@Override
	public Byte get(Object key) {
		int slot = findIndex(key);
		return Byte.valueOf(slot < 0 ? getDefaultReturnValue() : values[slot]);
	}
	
	@Override
	public byte getOrDefault(char key, byte defaultValue) {
		int slot = findIndex(key);
		return slot < 0 ? defaultValue : values[slot];
	}
	
	@Override
	public char firstCharKey() {
		if(size == 0) throw new NoSuchElementException();
		return keys[firstIndex];
	}
	
	@Override
	public char pollFirstCharKey() { throw new UnsupportedOperationException(); }
	
	@Override
	public char lastCharKey() {
		if(size == 0) throw new NoSuchElementException();
		return keys[lastIndex];
	}
	
	@Override
	public char pollLastCharKey() { throw new UnsupportedOperationException(); }
	
	@Override
	public byte firstByteValue() {
		if(size == 0) throw new NoSuchElementException();
		return values[firstIndex];
	}
	
	@Override
	public byte lastByteValue() {
		if(size == 0) throw new NoSuchElementException();
		return values[lastIndex];
	}
	
	@Override
	public Char2ByteMap.Entry firstEntry() {
		if(size == 0) throw new NoSuchElementException();
		return new BasicEntry(keys[firstIndex], values[firstIndex]);
	}
	
	@Override
	public Char2ByteMap.Entry lastEntry() {
		if(size == 0) throw new NoSuchElementException();
		return new BasicEntry(keys[lastIndex], values[lastIndex]);
	}
	
	@Override
	public Char2ByteMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
	@Override
	public Char2ByteMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }

	@Override
	public ObjectOrderedSet<Char2ByteMap.Entry> char2ByteEntrySet() {
		if(entrySet == null) entrySet = new MapEntrySet();
		return entrySet;
	}
	
	@Override
	public CharOrderedSet keySet() {
		if(keySet == null) keySet = new KeySet();
		return keySet;
	}
	
	@Override
	public ByteOrderedCollection values() {
		if(valuesC == null) valuesC = new Values();
		return valuesC;
	}
	
	@Override
	public ImmutableChar2ByteOpenHashMap copy() {
		ImmutableChar2ByteOpenHashMap map = new ImmutableChar2ByteOpenHashMap();
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
	public void forEach(CharByteConsumer action) {
		int index = firstIndex;
		while(index != -1){
			action.accept(keys[index], values[index]);
			index = (int)links[index];
		}
	}
	
	@Override
	public boolean replace(char key, byte oldValue, byte newValue) { throw new UnsupportedOperationException(); }
	
	@Override
	public byte replace(char key, byte value) { throw new UnsupportedOperationException(); }
	
	@Override
	public byte computeByte(char key, CharByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
	@Override
	public byte computeByteIfAbsent(char key, Char2ByteFunction mappingFunction) { throw new UnsupportedOperationException(); }
	@Override
	public byte supplyByteIfAbsent(char key, ByteSupplier valueProvider) { throw new UnsupportedOperationException(); }
	@Override
	public byte computeByteIfPresent(char key, CharByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
	@Override
	public byte computeByteNonDefault(char key, CharByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
	@Override
	public byte computeByteIfAbsentNonDefault(char key, Char2ByteFunction mappingFunction) { throw new UnsupportedOperationException(); }
	@Override
	public byte supplyByteIfAbsentNonDefault(char key, ByteSupplier valueProvider) { throw new UnsupportedOperationException(); }
	@Override
	public byte computeByteIfPresentNonDefault(char key, CharByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
	@Override
	public byte mergeByte(char key, byte value, ByteByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
	
	@Override
	public void mergeAllByte(Char2ByteMap m, ByteByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
	
	@Override
	public int size() { return size; }
	
	@Override
	public void clear() { throw new UnsupportedOperationException(); }
	
	protected int findIndex(char key) {
		if(key == (char)0) return containsNull ? nullIndex : -(nullIndex + 1);
		int pos = HashUtil.mix(Character.hashCode(key)) & mask;
		char current = keys[pos];
		if(current != (char)0) {
			if(current == key) return pos;
			while((current = keys[pos = (++pos & mask)]) != (char)0)
				if(current == key) return pos;
		}
		return -(pos + 1);
	}
	
	protected int findIndex(Object key) {
		if(key == null) return containsNull ? nullIndex : -(nullIndex + 1);
		if(((Character)key).charValue() == (char)0) return containsNull ? nullIndex : -(nullIndex + 1);
		int pos = HashUtil.mix(key.hashCode()) & mask;
		char current = keys[pos];
		if(current != (char)0) {
			if(Objects.equals(key, Character.valueOf(current))) return pos;
			while((current = keys[pos = (++pos & mask)]) != (char)0)
				if(Objects.equals(key, Character.valueOf(current))) return pos;
		}
		return -(pos + 1);
	}
	
	protected class MapEntry implements Char2ByteMap.Entry, Map.Entry<Character, Byte> {
		public int index = -1;
		
		public MapEntry() {}
		public MapEntry(int index) {
			this.index = index;
		}

		@Override
		public char getCharKey() {
			return keys[index];
		}

		@Override
		public byte getByteValue() {
			return values[index];
		}

		@Override
		public byte setValue(byte value) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Map.Entry) {
				if(obj instanceof Char2ByteMap.Entry) {
					Char2ByteMap.Entry entry = (Char2ByteMap.Entry)obj;
					return keys[index] == entry.getCharKey() && values[index] == entry.getByteValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				if(!(key instanceof Character)) return false;
				Object value = entry.getValue();
				return key instanceof Character && value instanceof Byte && keys[index] == ((Character)key).charValue() && values[index] == ((Byte)value).byteValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Character.hashCode(keys[index]) ^ Byte.hashCode(values[index]);
		}
		
		@Override
		public String toString() {
			return Character.toString(keys[index]) + "=" + Byte.toString(values[index]);
		}
	}
	
	private class MapEntrySet extends AbstractObjectSet<Char2ByteMap.Entry> implements Char2ByteOrderedMap.FastOrderedSet {
		@Override
		public void addFirst(Char2ByteMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public void addLast(Char2ByteMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToFirst(Char2ByteMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Char2ByteMap.Entry o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean moveToFirst(Char2ByteMap.Entry o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean moveToLast(Char2ByteMap.Entry o) { throw new UnsupportedOperationException(); }
		
		@Override
		public Char2ByteMap.Entry getFirst() {
			return new BasicEntry(firstCharKey(), firstByteValue());
		}
		
		@Override
		public Char2ByteMap.Entry getLast() {
			return new BasicEntry(lastCharKey(), lastByteValue());
		}
		
		@Override
		public Char2ByteMap.Entry removeFirst() { throw new UnsupportedOperationException(); }
		
		@Override
		public Char2ByteMap.Entry removeLast() { throw new UnsupportedOperationException(); }
		
		@Override
		public ObjectBidirectionalIterator<Char2ByteMap.Entry> iterator() {
			return new EntryIterator(true);
		}
		
		@Override
		public ObjectBidirectionalIterator<Char2ByteMap.Entry> reverseIterator() {
			return new EntryIterator(false);
		}
		
		@Override
		public ObjectBidirectionalIterator<Char2ByteMap.Entry> iterator(Char2ByteMap.Entry fromElement) {
			return new EntryIterator(fromElement.getCharKey());
		}
		
		@Override
		public ObjectBidirectionalIterator<Char2ByteMap.Entry> fastIterator() {
			return new FastEntryIterator(true);
		}
		
		@Override
		public ObjectBidirectionalIterator<Char2ByteMap.Entry> fastIterator(char fromElement) {
			return new FastEntryIterator(fromElement);
		}
		
		@Override
		public MapEntrySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(Consumer<? super Char2ByteMap.Entry> action) {
			int index = firstIndex;
			while(index != -1){
				action.accept(new BasicEntry(keys[index], values[index]));
				index = (int)links[index];
			}
		}
		
		@Override
		public void fastForEach(Consumer<? super Char2ByteMap.Entry> action) {
			BasicEntry entry = new BasicEntry();
			int index = firstIndex;
			while(index != -1){
				entry.set(keys[index], values[index]);
				action.accept(entry);
				index = (int)links[index];
			}
		}
		
		@Override
		public void forEachIndexed(IntObjectConsumer<Char2ByteMap.Entry> action) {
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
		public <E> void forEach(E input, ObjectObjectConsumer<E, Char2ByteMap.Entry> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			int index = firstIndex;
			while(index != -1) {
				action.accept(input, new BasicEntry(keys[index], values[index]));
				index = (int)links[index];
			}
		}
		
		@Override
		public boolean matchesAny(Predicate<Char2ByteMap.Entry> filter) {
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
		public boolean matchesNone(Predicate<Char2ByteMap.Entry> filter) {
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
		public boolean matchesAll(Predicate<Char2ByteMap.Entry> filter) {
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
		public <E> E reduce(E identity, BiFunction<E, Char2ByteMap.Entry, E> operator) {
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
		public Optional<Char2ByteMap.Entry> reduce(ObjectObjectUnaryOperator<Char2ByteMap.Entry, Char2ByteMap.Entry> operator) {
			Objects.requireNonNull(operator);
			Char2ByteMap.Entry state = null;
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
			return empty ? Optional.empty() : Optional.ofNullable(state);
		}
		
		@Override
		public Optional<Char2ByteMap.Entry> findFirst(Predicate<Char2ByteMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return Optional.empty();
			BasicEntry entry = new BasicEntry();
			int index = firstIndex;
			while(index != -1) {
				entry.set(keys[index], values[index]);
				if(filter.test(entry)) return Optional.ofNullable(entry);
				index = (int)links[index];
			}
			return Optional.empty();
		}
		
		@Override
		public int count(Predicate<Char2ByteMap.Entry> filter) {
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
				if(o instanceof Char2ByteMap.Entry) {
					Char2ByteMap.Entry entry = (Char2ByteMap.Entry)o;
					int index = ImmutableChar2ByteOpenHashMap.this.findIndex(entry.getCharKey());
					if(index >= 0) return entry.getByteValue() == ImmutableChar2ByteOpenHashMap.this.values[index];
				}
				else {
					Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
					int index = ImmutableChar2ByteOpenHashMap.this.findIndex(entry.getKey());
					if(index >= 0) return Objects.equals(entry.getValue(), Byte.valueOf(ImmutableChar2ByteOpenHashMap.this.values[index]));
				}
			}
			return false;
		}
		
		@Override
		@Deprecated
		public boolean remove(Object o) { throw new UnsupportedOperationException(); }
		
		@Override
		public int size() {
			return ImmutableChar2ByteOpenHashMap.this.size();
		}
		
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
	}
	
	private final class KeySet extends AbstractCharSet implements CharOrderedSet {
		@Override
		public boolean contains(char e) {
			return containsKey(e);
		}
		
		@Override
		public boolean remove(char o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean add(char o) {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public void addFirst(char o) { throw new UnsupportedOperationException(); }
		
		@Override
		public void addLast(char o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAndMoveToFirst(char o) { throw new UnsupportedOperationException(); }

		@Override
		public boolean addAndMoveToLast(char o) { throw new UnsupportedOperationException(); }

		@Override
		public boolean moveToFirst(char o) { throw new UnsupportedOperationException(); }

		@Override
		public boolean moveToLast(char o) { throw new UnsupportedOperationException(); }
		
		@Override
		public CharListIterator iterator() {
			return new KeyIterator(true);
		}
		
		@Override
		public CharListIterator reverseIterator() {
			return new KeyIterator(false);
		}
		
		@Override
		public CharBidirectionalIterator iterator(char fromElement) {
			return new KeyIterator(fromElement);
		}
		
		@Override
		public KeySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public int size() {
			return ImmutableChar2ByteOpenHashMap.this.size();
		}
		
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		
		@Override
		public char getFirstChar() {
			return firstCharKey();
		}
		
		@Override
		public char removeFirstChar() { throw new UnsupportedOperationException(); }

		@Override
		public char getLastChar() {
			return lastCharKey();
		}

		@Override
		public char removeLastChar() { throw new UnsupportedOperationException(); }
		
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
			while(index != -1){
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
		public OptionalChar reduce(CharCharUnaryOperator operator) {
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
			return empty ? OptionalChar.empty() : OptionalChar.of(state);
		}
		
		@Override
		public OptionalChar findFirst(CharPredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return OptionalChar.empty();
			int index = firstIndex;
			while(index != -1){
				if(filter.test(keys[index])) return OptionalChar.of(keys[index]);
				index = (int)links[index];
			}
			return OptionalChar.empty();
		}
		
		@Override
		public int count(CharPredicate filter) {
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
	
	private class Values extends AbstractByteCollection implements ByteOrderedCollection {
		@Override
		public boolean contains(byte e) { return containsValue(e); }
		
		@Override
		public boolean add(byte o) { throw new UnsupportedOperationException(); }
		@Override
		public ByteIterator iterator() { return new ValueIterator(true); }
		@Override
		public int size() { return ImmutableChar2ByteOpenHashMap.this.size(); }
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		@Override
		public ByteOrderedCollection reversed() { return new AbstractByteCollection.ReverseByteOrderedCollection(this, this::reverseIterator); }
		private ByteIterator reverseIterator() {
			return new ValueIterator(false);
		}
		@Override
		public void addFirst(byte e) { throw new UnsupportedOperationException(); }
		@Override
		public void addLast(byte e) { throw new UnsupportedOperationException(); }
		@Override
		public byte getFirstByte() { return firstByteValue(); }
		@Override
		public byte removeFirstByte() { throw new UnsupportedOperationException(); }
		@Override
		public byte getLastByte() { return lastByteValue(); }
		@Override
		public byte removeLastByte() { throw new UnsupportedOperationException(); }
		@Override
		public void forEach(ByteConsumer action) {
			int index = firstIndex;
			while(index != -1){
				action.accept(values[index]);
				index = (int)links[index];
			}
		}
		
		@Override
		public void forEachIndexed(IntByteConsumer action) {
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
		public <E> void forEach(E input, ObjectByteConsumer<E> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			int index = firstIndex;
			while(index != -1){
				action.accept(input, values[index]);
				index = (int)links[index];
			}
		}
		
		@Override
		public boolean matchesAny(BytePredicate filter) {
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
		public boolean matchesNone(BytePredicate filter) {
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
		public boolean matchesAll(BytePredicate filter) {
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
		public byte reduce(byte identity, ByteByteUnaryOperator operator) {
			Objects.requireNonNull(operator);
			byte state = identity;
			int index = firstIndex;
			while(index != -1) {
				state = operator.applyAsByte(state, values[index]);
				index = (int)links[index];
			}
			return state;
		}
		
		@Override
		public OptionalByte reduce(ByteByteUnaryOperator operator) {
			Objects.requireNonNull(operator);
			byte state = (byte)0;
			boolean empty = true;
			int index = firstIndex;
			while(index != -1) {
				if(empty) {
					empty = false;
					state = values[index];
					index = (int)links[index];
					continue;
				}
				state = operator.applyAsByte(state, values[index]);
				index = (int)links[index];
			}
			return empty ? OptionalByte.empty() : OptionalByte.of(state);
		}
		
		@Override
		public OptionalByte findFirst(BytePredicate filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return OptionalByte.empty();
			int index = firstIndex;
			while(index != -1){
				if(filter.test(values[index])) return OptionalByte.of(values[index]);
				index = (int)links[index];
			}
			return OptionalByte.empty();
		}
		
		@Override
		public int count(BytePredicate filter) {
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
	
	private class FastEntryIterator extends MapIterator implements ObjectListIterator<Char2ByteMap.Entry> {
		MapEntry entry = new MapEntry();
		
		public FastEntryIterator(boolean start) { super(start); }
		public FastEntryIterator(char from) {
			super(from);
		}
		
		@Override
		public Char2ByteMap.Entry next() {
			entry.index = nextEntry();
			return entry;
		}
		
		@Override
		public Char2ByteMap.Entry previous() {
			entry.index = previousEntry();
			return entry;
		}
		
		@Override
		public void set(Char2ByteMap.Entry entry) { throw new UnsupportedOperationException(); }

		@Override
		public void add(Char2ByteMap.Entry entry) { throw new UnsupportedOperationException(); }
	}
	
	private class EntryIterator extends MapIterator implements ObjectListIterator<Char2ByteMap.Entry> {
		
		public EntryIterator(boolean start) { super(start); }
		public EntryIterator(char from) {
			super(from);
		}
		
		@Override
		public Char2ByteMap.Entry next() {
			return new MapEntry(nextEntry());
		}
		
		@Override
		public Char2ByteMap.Entry previous() {
			return new MapEntry(previousEntry());
		}
		
		@Override
		public void remove() { throw new UnsupportedOperationException(); }

		@Override
		public void set(Char2ByteMap.Entry entry) { throw new UnsupportedOperationException(); }

		@Override
		public void add(Char2ByteMap.Entry entry) { throw new UnsupportedOperationException(); }
	}
	
	private class KeyIterator extends MapIterator implements CharListIterator {
		
		public KeyIterator(boolean start) { super(start); }
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
	
	private class ValueIterator extends MapIterator implements ByteListIterator {
		public ValueIterator(boolean start) { super(start); }
		
		@Override
		public byte previousByte() {
			return values[previousEntry()];
		}

		@Override
		public byte nextByte() {
			return values[nextEntry()];
		}

		@Override
		public void set(byte e) { throw new UnsupportedOperationException(); }

		@Override
		public void add(byte e) { throw new UnsupportedOperationException(); }
		
	}
	
	private class MapIterator {
		boolean forward;
		int previous = -1;
		int next = -1;
		int current = -1;
		int index = 0;
		
		MapIterator(boolean start) {
			this.forward = start;
			if(start) next = firstIndex;
			else previous = lastIndex;
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
			return (forward ? next : previous) != -1;
		}

		public boolean hasPrevious() {
			return (forward ? previous : next) != -1;
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
			if(forward) moveBackwards();
			else moveForwards();
			if(index >= 0) index--;
			return current;
		}
		
		public int nextEntry() {
			if(!hasNext()) throw new NoSuchElementException();
			if(forward) moveForwards();
			else moveBackwards();
			if(index >= 0) index++;
			return current;
		}
		
		private void moveBackwards() {
			current = previous;
			previous = (int)(links[current] >> 32);
			next = current;
		}
		
		private void moveForwards() {
			current = next;
			next = (int)(links[current]);
			previous = current;
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