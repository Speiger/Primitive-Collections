package speiger.src.collections.chars.maps.impl.immutable;

import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import speiger.src.collections.chars.collections.CharBidirectionalIterator;
import speiger.src.collections.chars.functions.CharConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectCharConsumer;
import speiger.src.collections.ints.functions.consumer.IntCharConsumer;
import speiger.src.collections.ints.functions.consumer.IntObjectConsumer;
import speiger.src.collections.chars.functions.function.CharPredicate;
import speiger.src.collections.chars.functions.consumer.CharObjectConsumer;
import speiger.src.collections.chars.functions.function.CharFunction;
import speiger.src.collections.chars.functions.function.CharObjectUnaryOperator;
import speiger.src.collections.chars.functions.function.CharCharUnaryOperator;
import speiger.src.collections.chars.lists.CharListIterator;
import speiger.src.collections.chars.maps.interfaces.Char2ObjectOrderedMap;
import speiger.src.collections.chars.maps.abstracts.AbstractChar2ObjectMap;
import speiger.src.collections.chars.maps.interfaces.Char2ObjectMap;
import speiger.src.collections.chars.sets.CharOrderedSet;
import speiger.src.collections.chars.utils.CharArrays;
import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;

import speiger.src.collections.chars.sets.AbstractCharSet;
import speiger.src.collections.objects.collections.AbstractObjectCollection;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.functions.ObjectSupplier;
import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;
import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
import speiger.src.collections.utils.HashUtil;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Type Specific Custom implementation of the HashMap
 * Instead of using Wrapper Object Arrays for storing keys and values there is dedicated arrays for storing keys and values.
 * Extra to that there is a couple quality of life functions provided
 * @param <V> the keyType of elements maintained by this Collection
 */
public class ImmutableChar2ObjectOpenHashMap<V> extends AbstractChar2ObjectMap<V> implements Char2ObjectOrderedMap<V>
{
	/** The Backing keys array */
	protected transient char[] keys;
	/** The Backing values array */
	protected transient V[] values;
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
	protected transient FastOrderedSet<V> entrySet;
	/** KeySet cache */
	protected transient CharOrderedSet keySet;
	/** Values cache */
	protected transient ObjectCollection<V> valuesC;
	
	/** Amount of Elements stored in the HashMap */
	protected int size;
	
	/**
	 * Helper constructor for copying the Map
	 */
	protected ImmutableChar2ObjectOpenHashMap() {}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public ImmutableChar2ObjectOpenHashMap(Character[] keys, V[] values) {
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
	public ImmutableChar2ObjectOpenHashMap(Character[] keys, V[] values, float loadFactor) {
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		init(CharArrays.unwrap(keys), values, 0, keys.length, loadFactor);
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public ImmutableChar2ObjectOpenHashMap(char[] keys, V[] values) {
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
	public ImmutableChar2ObjectOpenHashMap(char[] keys, V[] values, float loadFactor) {
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		init(keys, values, 0, keys.length, loadFactor);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 */
	public ImmutableChar2ObjectOpenHashMap(Map<? extends Character, ? extends V> map) {
		this(map, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public ImmutableChar2ObjectOpenHashMap(Map<? extends Character, ? extends V> map, float loadFactor) {
		char[] keys = new char[map.size()];
		V[] values = (V[])new Object[keys.length];
		int index = 0;
		for(Map.Entry<? extends Character, ? extends V> entry : map.entrySet()) {
			keys[index] = entry.getKey().charValue();
			values[index] = entry.getValue();
			index++;
		}
		init(keys, values, 0, index, loadFactor);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 */
	public ImmutableChar2ObjectOpenHashMap(Char2ObjectMap<V> map) {
		this(map, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
 	 */
	public ImmutableChar2ObjectOpenHashMap(Char2ObjectMap<V> map, float loadFactor) {
		char[] keys = new char[map.size()];
		V[] values = (V[])new Object[keys.length];
		int index = 0;
		for(Char2ObjectMap.Entry<V> entry : getFastIterable(map)) {
			keys[index] = entry.getCharKey();
			values[index] = entry.getValue();
			index++;
		}
		init(keys, values, 0, index, loadFactor);
	}
	
	protected void init(char[] a, V[] b, int offset, int length, float loadFactor) {
		SanityChecks.checkArrayCapacity(a.length, offset, length);
		int newSize = HashUtil.arraySize(length+1, loadFactor);
		int newMask = newSize - 1;
		char[] newKeys = new char[newSize + 1];
		V[] newValues = (V[])new Object[newSize + 1];
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
	public V put(char key, V value) { throw new UnsupportedOperationException(); }
	@Override
	public V putIfAbsent(char key, V value) { throw new UnsupportedOperationException(); }	
	@Override
	public V putAndMoveToFirst(char key, V value) { throw new UnsupportedOperationException(); }
	@Override
	public V putAndMoveToLast(char key, V value) { throw new UnsupportedOperationException(); }
	@Override
	public boolean moveToFirst(char key) { throw new UnsupportedOperationException(); }
	@Override
	public boolean moveToLast(char key) { throw new UnsupportedOperationException(); }
	@Override
	public V getAndMoveToFirst(char key) { throw new UnsupportedOperationException(); }
	@Override
	public V getAndMoveToLast(char key) { throw new UnsupportedOperationException(); }
	
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
	public boolean containsValue(Object value) {
		int index = firstIndex;
		while(index != -1) {
			if(Objects.equals(values[index], value)) return true;
			index = (int)links[index];
		}
		return false;
	}
	
	@Override
	public V remove(char key) { throw new UnsupportedOperationException(); }
	
	@Override
	public V removeOrDefault(char key, V defaultValue) { throw new UnsupportedOperationException(); }
	
	@Override
	@Deprecated
	public V remove(Object key) { throw new UnsupportedOperationException(); }
	
	@Override
	public boolean remove(char key, V value) { throw new UnsupportedOperationException(); }
	
	@Override
	public boolean remove(Object key, Object value) { throw new UnsupportedOperationException(); }
	
	@Override
	public V get(char key) {
		int slot = findIndex(key);
		return slot < 0 ? getDefaultReturnValue() : values[slot];
	}
	
	@Override
	public V get(Object key) {
		int slot = findIndex(key);
		return slot < 0 ? getDefaultReturnValue() : values[slot];
	}
	
	@Override
	public V getOrDefault(char key, V defaultValue) {
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
	public V firstValue() {
		if(size == 0) throw new NoSuchElementException();
		return values[firstIndex];
	}
	
	@Override
	public V lastValue() {
		if(size == 0) throw new NoSuchElementException();
		return values[lastIndex];
	}	

	@Override
	public ObjectOrderedSet<Char2ObjectMap.Entry<V>> char2ObjectEntrySet() {
		if(entrySet == null) entrySet = new MapEntrySet();
		return entrySet;
	}
	
	@Override
	public CharOrderedSet keySet() {
		if(keySet == null) keySet = new KeySet();
		return keySet;
	}
	
	@Override
	public ObjectCollection<V> values() {
		if(valuesC == null) valuesC = new Values();
		return valuesC;
	}
	
	@Override
	public ImmutableChar2ObjectOpenHashMap<V> copy() {
		ImmutableChar2ObjectOpenHashMap<V> map = new ImmutableChar2ObjectOpenHashMap<>();
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
	public void forEach(CharObjectConsumer<V> action) {
		int index = firstIndex;
		while(index != -1){
			action.accept(keys[index], values[index]);
			index = (int)links[index];
		}
	}
	
	@Override
	public boolean replace(char key, V oldValue, V newValue) { throw new UnsupportedOperationException(); }
	
	@Override
	public V replace(char key, V value) { throw new UnsupportedOperationException(); }
	
	@Override
	public V compute(char key, CharObjectUnaryOperator<V> mappingFunction) { throw new UnsupportedOperationException(); }
	@Override
	public V computeIfAbsent(char key, CharFunction<V> mappingFunction) { throw new UnsupportedOperationException(); }
	@Override
	public V supplyIfAbsent(char key, ObjectSupplier<V> valueProvider) { throw new UnsupportedOperationException(); }
	@Override
	public V computeIfPresent(char key, CharObjectUnaryOperator<V> mappingFunction) { throw new UnsupportedOperationException(); }
	@Override
	public V merge(char key, V value, ObjectObjectUnaryOperator<V, V> mappingFunction) { throw new UnsupportedOperationException(); }
	
	@Override
	public void mergeAll(Char2ObjectMap<V> m, ObjectObjectUnaryOperator<V, V> mappingFunction) { throw new UnsupportedOperationException(); }
	
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
	
	protected class MapEntry implements Char2ObjectMap.Entry<V>, Map.Entry<Character, V> {
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
		public V getValue() {
			return values[index];
		}

		@Override
		public V setValue(V value) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Map.Entry) {
				if(obj instanceof Char2ObjectMap.Entry) {
					Char2ObjectMap.Entry<V> entry = (Char2ObjectMap.Entry<V>)obj;
					return keys[index] == entry.getCharKey() && Objects.equals(values[index], entry.getValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				if(!(key instanceof Character)) return false;
				Object value = entry.getValue();
				return key instanceof Character && keys[index] == ((Character)key).charValue() && Objects.equals(values[index], value);
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Character.hashCode(keys[index]) ^ Objects.hashCode(values[index]);
		}
		
		@Override
		public String toString() {
			return Character.toString(keys[index]) + "=" + Objects.toString(values[index]);
		}
	}
	
	private class MapEntrySet extends AbstractObjectSet<Char2ObjectMap.Entry<V>> implements Char2ObjectOrderedMap.FastOrderedSet<V> {
		@Override
		public boolean addAndMoveToFirst(Char2ObjectMap.Entry<V> o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Char2ObjectMap.Entry<V> o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean moveToFirst(Char2ObjectMap.Entry<V> o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean moveToLast(Char2ObjectMap.Entry<V> o) { throw new UnsupportedOperationException(); }
		
		@Override
		public Char2ObjectMap.Entry<V> first() {
			return new BasicEntry<>(firstCharKey(), firstValue());
		}
		
		@Override
		public Char2ObjectMap.Entry<V> last() {
			return new BasicEntry<>(lastCharKey(), lastValue());
		}
		
		@Override
		public Char2ObjectMap.Entry<V> pollFirst() { throw new UnsupportedOperationException(); }
		
		@Override
		public Char2ObjectMap.Entry<V> pollLast() { throw new UnsupportedOperationException(); }
		
		@Override
		public ObjectBidirectionalIterator<Char2ObjectMap.Entry<V>> iterator() {
			return new EntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Char2ObjectMap.Entry<V>> iterator(Char2ObjectMap.Entry<V> fromElement) {
			return new EntryIterator(fromElement.getCharKey());
		}
		
		@Override
		public ObjectBidirectionalIterator<Char2ObjectMap.Entry<V>> fastIterator() {
			return new FastEntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Char2ObjectMap.Entry<V>> fastIterator(char fromElement) {
			return new FastEntryIterator(fromElement);
		}
		
		@Override
		public MapEntrySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(Consumer<? super Char2ObjectMap.Entry<V>> action) {
			int index = firstIndex;
			while(index != -1){
				action.accept(new BasicEntry<>(keys[index], values[index]));
				index = (int)links[index];
			}
		}
		
		@Override
		public void fastForEach(Consumer<? super Char2ObjectMap.Entry<V>> action) {
			BasicEntry<V> entry = new BasicEntry<>();
			int index = firstIndex;
			while(index != -1){
				entry.set(keys[index], values[index]);
				action.accept(entry);
				index = (int)links[index];
			}
		}
		
		@Override
		public void forEachIndexed(IntObjectConsumer<Char2ObjectMap.Entry<V>> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			int count = 0;
			int index = firstIndex;
			while(index != -1) {
				action.accept(count++, new BasicEntry<>(keys[index], values[index]));
				index = (int)links[index];
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, Char2ObjectMap.Entry<V>> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			int index = firstIndex;
			while(index != -1) {
				action.accept(input, new BasicEntry<>(keys[index], values[index]));
				index = (int)links[index];
			}
		}
		
		@Override
		public boolean matchesAny(Predicate<Char2ObjectMap.Entry<V>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			BasicEntry<V> entry = new BasicEntry<>();
			int index = firstIndex;
			while(index != -1) {
				entry.set(keys[index], values[index]);
				if(filter.test(entry)) return true;
				index = (int)links[index];
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Predicate<Char2ObjectMap.Entry<V>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			BasicEntry<V> entry = new BasicEntry<>();
			int index = firstIndex;
			while(index != -1) {
				entry.set(keys[index], values[index]);
				if(filter.test(entry)) return false;
				index = (int)links[index];
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Predicate<Char2ObjectMap.Entry<V>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			BasicEntry<V> entry = new BasicEntry<>();
			int index = firstIndex;
			while(index != -1) {
				entry.set(keys[index], values[index]);
				if(!filter.test(entry)) return false;
				index = (int)links[index];
			}
			return true;
		}
		
		@Override
		public <E> E reduce(E identity, BiFunction<E, Char2ObjectMap.Entry<V>, E> operator) {
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
		public Char2ObjectMap.Entry<V> reduce(ObjectObjectUnaryOperator<Char2ObjectMap.Entry<V>, Char2ObjectMap.Entry<V>> operator) {
			Objects.requireNonNull(operator);
			Char2ObjectMap.Entry<V> state = null;
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
		public Char2ObjectMap.Entry<V> findFirst(Predicate<Char2ObjectMap.Entry<V>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return null;
			BasicEntry<V> entry = new BasicEntry<>();
			int index = firstIndex;
			while(index != -1) {
				entry.set(keys[index], values[index]);
				if(filter.test(entry)) return entry;
				index = (int)links[index];
			}
			return null;
		}
		
		@Override
		public int count(Predicate<Char2ObjectMap.Entry<V>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			BasicEntry<V> entry = new BasicEntry<>();
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
				if(o instanceof Char2ObjectMap.Entry) {
					Char2ObjectMap.Entry<V> entry = (Char2ObjectMap.Entry<V>)o;
					int index = ImmutableChar2ObjectOpenHashMap.this.findIndex(entry.getCharKey());
					if(index >= 0) return Objects.equals(entry.getValue(), ImmutableChar2ObjectOpenHashMap.this.values[index]);
				}
				else {
					Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
					int index = ImmutableChar2ObjectOpenHashMap.this.findIndex(entry.getKey());
					if(index >= 0) return Objects.equals(entry.getValue(), ImmutableChar2ObjectOpenHashMap.this.values[index]);
				}
			}
			return false;
		}
		
		@Override
		@Deprecated
		public boolean remove(Object o) { throw new UnsupportedOperationException(); }
		
		@Override
		public int size() {
			return ImmutableChar2ObjectOpenHashMap.this.size();
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
		public boolean addAndMoveToFirst(char o) { throw new UnsupportedOperationException(); }

		@Override
		public boolean addAndMoveToLast(char o) { throw new UnsupportedOperationException(); }

		@Override
		public boolean moveToFirst(char o) { throw new UnsupportedOperationException(); }

		@Override
		public boolean moveToLast(char o) { throw new UnsupportedOperationException(); }
		
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
			return ImmutableChar2ObjectOpenHashMap.this.size();
		}
		
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		
		@Override
		public char firstChar() {
			return firstCharKey();
		}
		
		@Override
		public char pollFirstChar() { throw new UnsupportedOperationException(); }

		@Override
		public char lastChar() {
			return lastCharKey();
		}

		@Override
		public char pollLastChar() { throw new UnsupportedOperationException(); }
		
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
			int index = firstIndex;
			int result = 0;
			while(index != -1){
				if(filter.test(keys[index])) result++;
				index = (int)links[index];
			}
			return result;
		}
	}
	
	private class Values extends AbstractObjectCollection<V> {
		@Override
		@Deprecated
		public boolean contains(Object e) {
			return containsValue(e);
		}
		
		@Override
		public boolean add(V o) {
			throw new UnsupportedOperationException();
		}

		@Override
		public ObjectIterator<V> iterator() {
			return new ValueIterator();
		}
		
		@Override
		public int size() {
			return ImmutableChar2ObjectOpenHashMap.this.size();
		}
		
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(Consumer<? super V> action) {
			int index = firstIndex;
			while(index != -1){
				action.accept(values[index]);
				index = (int)links[index];
			}
		}
		
		@Override
		public void forEachIndexed(IntObjectConsumer<V> action) {
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
		public <E> void forEach(E input, ObjectObjectConsumer<E, V> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			int index = firstIndex;
			while(index != -1){
				action.accept(input, values[index]);
				index = (int)links[index];
			}
		}
		
		@Override
		public boolean matchesAny(Predicate<V> filter) {
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
		public boolean matchesNone(Predicate<V> filter) {
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
		public boolean matchesAll(Predicate<V> filter) {
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
		public <E> E reduce(E identity, BiFunction<E, V, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			int index = firstIndex;
			while(index != -1) {
				state = operator.apply(state, values[index]);
				index = (int)links[index];
			}
			return state;
		}
		
		@Override
		public V reduce(ObjectObjectUnaryOperator<V, V> operator) {
			Objects.requireNonNull(operator);
			V state = null;
			boolean empty = true;
			int index = firstIndex;
			while(index != -1) {
				if(empty) {
					empty = false;
					state = values[index];
					index = (int)links[index];
					continue;
				}
				state = operator.apply(state, values[index]);
				index = (int)links[index];
			}
			return state;
		}
		
		@Override
		public V findFirst(Predicate<V> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return null;
			int index = firstIndex;
			while(index != -1){
				if(filter.test(values[index])) return values[index];
				index = (int)links[index];
			}
			return null;
		}
		
		@Override
		public int count(Predicate<V> filter) {
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
	
	private class FastEntryIterator extends MapIterator implements ObjectListIterator<Char2ObjectMap.Entry<V>> {
		MapEntry entry = new MapEntry();
		
		public FastEntryIterator() {}
		public FastEntryIterator(char from) {
			super(from);
		}
		
		@Override
		public Char2ObjectMap.Entry<V> next() {
			entry.index = nextEntry();
			return entry;
		}
		
		@Override
		public Char2ObjectMap.Entry<V> previous() {
			entry.index = previousEntry();
			return entry;
		}
		
		@Override
		public void set(Char2ObjectMap.Entry<V> entry) { throw new UnsupportedOperationException(); }

		@Override
		public void add(Char2ObjectMap.Entry<V> entry) { throw new UnsupportedOperationException(); }
	}
	
	private class EntryIterator extends MapIterator implements ObjectListIterator<Char2ObjectMap.Entry<V>> {
		
		public EntryIterator() {}
		public EntryIterator(char from) {
			super(from);
		}
		
		@Override
		public Char2ObjectMap.Entry<V> next() {
			return new MapEntry(nextEntry());
		}
		
		@Override
		public Char2ObjectMap.Entry<V> previous() {
			return new MapEntry(previousEntry());
		}
		
		@Override
		public void remove() { throw new UnsupportedOperationException(); }

		@Override
		public void set(Char2ObjectMap.Entry<V> entry) { throw new UnsupportedOperationException(); }

		@Override
		public void add(Char2ObjectMap.Entry<V> entry) { throw new UnsupportedOperationException(); }
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
	
	private class ValueIterator extends MapIterator implements ObjectListIterator<V> {
		public ValueIterator() {}
		
		@Override
		public V previous() {
			return values[previousEntry()];
		}

		@Override
		public V next() {
			return values[nextEntry()];
		}

		@Override
		public void set(V e) { throw new UnsupportedOperationException(); }

		@Override
		public void add(V e) { throw new UnsupportedOperationException(); }
		
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