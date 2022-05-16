package speiger.src.collections.bytes.maps.impl.immutable;

import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.BiFunction;

import speiger.src.collections.bytes.collections.ByteBidirectionalIterator;
import speiger.src.collections.bytes.functions.ByteConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectByteConsumer;
import speiger.src.collections.bytes.functions.function.Byte2BooleanFunction;
import speiger.src.collections.bytes.functions.consumer.ByteObjectConsumer;
import speiger.src.collections.bytes.functions.function.Byte2ObjectFunction;
import speiger.src.collections.bytes.functions.function.ByteObjectUnaryOperator;
import speiger.src.collections.bytes.functions.function.ByteByteUnaryOperator;
import speiger.src.collections.bytes.lists.ByteListIterator;
import speiger.src.collections.bytes.maps.interfaces.Byte2ObjectOrderedMap;
import speiger.src.collections.bytes.maps.abstracts.AbstractByte2ObjectMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2ObjectMap;
import speiger.src.collections.bytes.sets.ByteOrderedSet;
import speiger.src.collections.bytes.utils.maps.Byte2ObjectMaps;
import speiger.src.collections.bytes.utils.ByteArrays;

import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;
import speiger.src.collections.objects.functions.function.Object2BooleanFunction;
import speiger.src.collections.bytes.sets.AbstractByteSet;
import speiger.src.collections.bytes.sets.ByteSet;
import speiger.src.collections.objects.collections.AbstractObjectCollection;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.functions.ObjectSupplier;
import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;
import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.HashUtil;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Type Specific Custom implementation of the HashMap
 * Instead of using Wrapper Object Arrays for storing keys and values there is dedicated arrays for storing keys and values.
 * Extra to that there is a couple quality of life functions provided
 * @param <V> the type of elements maintained by this Collection
 */
public class ImmutableByte2ObjectOpenHashMap<V> extends AbstractByte2ObjectMap<V> implements Byte2ObjectOrderedMap<V>
{
	/** The Backing keys array */
	protected transient byte[] keys;
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
	protected transient FastEntrySet<V> entrySet;
	/** KeySet cache */
	protected transient ByteSet keySet;
	/** Values cache */
	protected transient ObjectCollection<V> valuesC;
	
	/** Amount of Elements stored in the HashMap */
	protected int size;
	
	/**
	 * Helper constructor for copying the Map
	 */
	protected ImmutableByte2ObjectOpenHashMap() {}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public ImmutableByte2ObjectOpenHashMap(Byte[] keys, V[] values) {
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
	public ImmutableByte2ObjectOpenHashMap(Byte[] keys, V[] values, float loadFactor) {
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		init(ByteArrays.unwrap(keys), values, 0, keys.length, loadFactor);
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public ImmutableByte2ObjectOpenHashMap(byte[] keys, V[] values) {
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
	public ImmutableByte2ObjectOpenHashMap(byte[] keys, V[] values, float loadFactor) {
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		init(keys, values, 0, keys.length, loadFactor);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 */
	public ImmutableByte2ObjectOpenHashMap(Map<? extends Byte, ? extends V> map) {
		this(map, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public ImmutableByte2ObjectOpenHashMap(Map<? extends Byte, ? extends V> map, float loadFactor) {
		byte[] keys = new byte[map.size()];
		V[] values = (V[])new Object[keys.length];
		int index = 0;
		for(Map.Entry<? extends Byte, ? extends V> entry : map.entrySet()) {
			keys[index] = entry.getKey().byteValue();
			values[index] = entry.getValue();
			index++;
		}
		init(keys, values, 0, index, loadFactor);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 */
	public ImmutableByte2ObjectOpenHashMap(Byte2ObjectMap<V> map) {
		this(map, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
 	 */
	public ImmutableByte2ObjectOpenHashMap(Byte2ObjectMap<V> map, float loadFactor) {
		byte[] keys = new byte[map.size()];
		V[] values = (V[])new Object[keys.length];
		int index = 0;
		for(Byte2ObjectMap.Entry<V> entry : Byte2ObjectMaps.fastIterable(map)) {
			keys[index] = entry.getByteKey();
			values[index] = entry.getValue();
			index++;
		}
		init(keys, values, 0, index, loadFactor);
	}
	
	protected void init(byte[] a, V[] b, int offset, int length, float loadFactor) {
		SanityChecks.checkArrayCapacity(a.length, offset, length);
		int newSize = HashUtil.arraySize(length+1, loadFactor);
		int newMask = newSize - 1;
		byte[] newKeys = new byte[newSize + 1];
		V[] newValues = (V[])new Object[newSize + 1];
		long[] newLinks = new long[newSize + 1];
		int prev = -1;
		for(int i = offset,m=offset+length;i<m;i++)
		{
			byte o = a[i];
			if(o == (byte)0) {
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
			int pos = HashUtil.mix(Byte.hashCode(o)) & newMask;
			byte current = newKeys[pos];
			if(current != (byte)0) {
				if(current == o) {
					newValues[pos] = b[i];
					continue;
				}
				while((current = newKeys[pos = (++pos & newMask)]) != (byte)0) {
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
	public V put(byte key, V value) { throw new UnsupportedOperationException(); }
	@Override
	public V putIfAbsent(byte key, V value) { throw new UnsupportedOperationException(); }	
	@Override
	public V putAndMoveToFirst(byte key, V value) { throw new UnsupportedOperationException(); }
	@Override
	public V putAndMoveToLast(byte key, V value) { throw new UnsupportedOperationException(); }
	@Override
	public boolean moveToFirst(byte key) { throw new UnsupportedOperationException(); }
	@Override
	public boolean moveToLast(byte key) { throw new UnsupportedOperationException(); }
	@Override
	public V getAndMoveToFirst(byte key) { throw new UnsupportedOperationException(); }
	@Override
	public V getAndMoveToLast(byte key) { throw new UnsupportedOperationException(); }
	
	@Override
	public boolean containsKey(byte key) {
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
	public V remove(byte key) { throw new UnsupportedOperationException(); }
	
	@Override
	public V removeOrDefault(byte key, V defaultValue) { throw new UnsupportedOperationException(); }
	
	@Override
	@Deprecated
	public V remove(Object key) { throw new UnsupportedOperationException(); }
	
	@Override
	public boolean remove(byte key, V value) { throw new UnsupportedOperationException(); }
	
	@Override
	public boolean remove(Object key, Object value) { throw new UnsupportedOperationException(); }
	
	@Override
	public V get(byte key) {
		int slot = findIndex(key);
		return slot < 0 ? getDefaultReturnValue() : values[slot];
	}
	
	@Override
	public V get(Object key) {
		int slot = findIndex(key);
		return slot < 0 ? getDefaultReturnValue() : values[slot];
	}
	
	@Override
	public V getOrDefault(byte key, V defaultValue) {
		int slot = findIndex(key);
		return slot < 0 ? defaultValue : values[slot];
	}
	
	@Override
	public byte firstByteKey() {
		if(size == 0) throw new NoSuchElementException();
		return keys[firstIndex];
	}
	
	@Override
	public byte pollFirstByteKey() { throw new UnsupportedOperationException(); }
	
	@Override
	public byte lastByteKey() {
		if(size == 0) throw new NoSuchElementException();
		return keys[lastIndex];
	}
	
	@Override
	public byte pollLastByteKey() { throw new UnsupportedOperationException(); }
	
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
	public ObjectSet<Byte2ObjectMap.Entry<V>> byte2ObjectEntrySet() {
		if(entrySet == null) entrySet = new MapEntrySet();
		return entrySet;
	}
	
	@Override
	public ByteSet keySet() {
		if(keySet == null) keySet = new KeySet();
		return keySet;
	}
	
	@Override
	public ObjectCollection<V> values() {
		if(valuesC == null) valuesC = new Values();
		return valuesC;
	}
	
	@Override
	public ImmutableByte2ObjectOpenHashMap<V> copy() {
		ImmutableByte2ObjectOpenHashMap<V> map = new ImmutableByte2ObjectOpenHashMap<>();
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
	public void forEach(ByteObjectConsumer<V> action) {
		int index = firstIndex;
		while(index != -1){
			action.accept(keys[index], values[index]);
			index = (int)links[index];
		}
	}
	
	@Override
	public boolean replace(byte key, V oldValue, V newValue) { throw new UnsupportedOperationException(); }
	
	@Override
	public V replace(byte key, V value) { throw new UnsupportedOperationException(); }
	
	@Override
	public V compute(byte key, ByteObjectUnaryOperator<V> mappingFunction) { throw new UnsupportedOperationException(); }
	
	@Override
	public V computeIfAbsent(byte key, Byte2ObjectFunction<V> mappingFunction) { throw new UnsupportedOperationException(); }
	
	@Override
	public V supplyIfAbsent(byte key, ObjectSupplier<V> valueProvider) { throw new UnsupportedOperationException(); }
	
	@Override
	public V computeIfPresent(byte key, ByteObjectUnaryOperator<V> mappingFunction) { throw new UnsupportedOperationException(); }
	
	@Override
	public V merge(byte key, V value, ObjectObjectUnaryOperator<V, V> mappingFunction) { throw new UnsupportedOperationException(); }
	
	@Override
	public void mergeAll(Byte2ObjectMap<V> m, ObjectObjectUnaryOperator<V, V> mappingFunction) { throw new UnsupportedOperationException(); }
	
	@Override
	public int size() { return size; }
	
	@Override
	public void clear() { throw new UnsupportedOperationException(); }
	
	protected int findIndex(byte key) {
		if(key == (byte)0) return containsNull ? nullIndex : -(nullIndex + 1);
		int pos = HashUtil.mix(Byte.hashCode(key)) & mask;
		byte current = keys[pos];
		if(current != (byte)0) {
			if(current == key) return pos;
			while((current = keys[pos = (++pos & mask)]) != (byte)0)
				if(current == key) return pos;
		}
		return -(pos + 1);
	}
	
	protected int findIndex(Object key) {
		if(key == null) return containsNull ? nullIndex : -(nullIndex + 1);
		int pos = HashUtil.mix(key.hashCode()) & mask;
		byte current = keys[pos];
		if(current != (byte)0) {
			if(Objects.equals(key, Byte.valueOf(current))) return pos;
			while((current = keys[pos = (++pos & mask)]) != (byte)0)
				if(Objects.equals(key, Byte.valueOf(current))) return pos;
		}
		return -(pos + 1);
	}
	
	protected class MapEntry implements Byte2ObjectMap.Entry<V>, Map.Entry<Byte, V> {
		public int index = -1;
		
		public MapEntry() {}
		public MapEntry(int index) {
			this.index = index;
		}

		@Override
		public byte getByteKey() {
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
				if(obj instanceof Byte2ObjectMap.Entry) {
					Byte2ObjectMap.Entry<V> entry = (Byte2ObjectMap.Entry<V>)obj;
					return keys[index] == entry.getByteKey() && Objects.equals(values[index], entry.getValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				if(!(key instanceof Byte)) return false;
				Object value = entry.getValue();
				return key instanceof Byte && keys[index] == ((Byte)key).byteValue() && Objects.equals(values[index], value);
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Byte.hashCode(keys[index]) ^ Objects.hashCode(values[index]);
		}
		
		@Override
		public String toString() {
			return Byte.toString(keys[index]) + "=" + Objects.toString(values[index]);
		}
	}
	
	private class MapEntrySet extends AbstractObjectSet<Byte2ObjectMap.Entry<V>> implements Byte2ObjectOrderedMap.FastOrderedSet<V> {
		@Override
		public boolean addAndMoveToFirst(Byte2ObjectMap.Entry<V> o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Byte2ObjectMap.Entry<V> o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean moveToFirst(Byte2ObjectMap.Entry<V> o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean moveToLast(Byte2ObjectMap.Entry<V> o) { throw new UnsupportedOperationException(); }
		
		@Override
		public Byte2ObjectMap.Entry<V> first() {
			return new BasicEntry<>(firstByteKey(), firstValue());
		}
		
		@Override
		public Byte2ObjectMap.Entry<V> last() {
			return new BasicEntry<>(lastByteKey(), lastValue());
		}
		
		@Override
		public Byte2ObjectMap.Entry<V> pollFirst() { throw new UnsupportedOperationException(); }
		
		@Override
		public Byte2ObjectMap.Entry<V> pollLast() { throw new UnsupportedOperationException(); }
		
		@Override
		public ObjectBidirectionalIterator<Byte2ObjectMap.Entry<V>> iterator() {
			return new EntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Byte2ObjectMap.Entry<V>> iterator(Byte2ObjectMap.Entry<V> fromElement) {
			return new EntryIterator(fromElement.getByteKey());
		}
		
		@Override
		public ObjectBidirectionalIterator<Byte2ObjectMap.Entry<V>> fastIterator() {
			return new FastEntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Byte2ObjectMap.Entry<V>> fastIterator(byte fromElement) {
			return new FastEntryIterator(fromElement);
		}
		
		@Override
		public MapEntrySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(Consumer<? super Byte2ObjectMap.Entry<V>> action) {
			int index = firstIndex;
			while(index != -1){
				action.accept(new BasicEntry<>(keys[index], values[index]));
				index = (int)links[index];
			}
		}
		
		@Override
		public void fastForEach(Consumer<? super Byte2ObjectMap.Entry<V>> action) {
			BasicEntry<V> entry = new BasicEntry<>();
			int index = firstIndex;
			while(index != -1){
				entry.set(keys[index], values[index]);
				action.accept(entry);
				index = (int)links[index];
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, Byte2ObjectMap.Entry<V>> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			int index = firstIndex;
			while(index != -1) {
				action.accept(input, new BasicEntry<>(keys[index], values[index]));
				index = (int)links[index];
			}
		}
		
		@Override
		public boolean matchesAny(Object2BooleanFunction<Byte2ObjectMap.Entry<V>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			BasicEntry<V> entry = new BasicEntry<>();
			int index = firstIndex;
			while(index != -1) {
				entry.set(keys[index], values[index]);
				if(filter.getBoolean(entry)) return true;
				index = (int)links[index];
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Object2BooleanFunction<Byte2ObjectMap.Entry<V>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			BasicEntry<V> entry = new BasicEntry<>();
			int index = firstIndex;
			while(index != -1) {
				entry.set(keys[index], values[index]);
				if(filter.getBoolean(entry)) return false;
				index = (int)links[index];
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Object2BooleanFunction<Byte2ObjectMap.Entry<V>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			BasicEntry<V> entry = new BasicEntry<>();
			int index = firstIndex;
			while(index != -1) {
				entry.set(keys[index], values[index]);
				if(!filter.getBoolean(entry)) return false;
				index = (int)links[index];
			}
			return true;
		}
		
		@Override
		public <E> E reduce(E identity, BiFunction<E, Byte2ObjectMap.Entry<V>, E> operator) {
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
		public Byte2ObjectMap.Entry<V> reduce(ObjectObjectUnaryOperator<Byte2ObjectMap.Entry<V>, Byte2ObjectMap.Entry<V>> operator) {
			Objects.requireNonNull(operator);
			Byte2ObjectMap.Entry<V> state = null;
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
		public Byte2ObjectMap.Entry<V> findFirst(Object2BooleanFunction<Byte2ObjectMap.Entry<V>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return null;
			BasicEntry<V> entry = new BasicEntry<>();
			int index = firstIndex;
			while(index != -1) {
				entry.set(keys[index], values[index]);
				if(filter.getBoolean(entry)) return entry;
				index = (int)links[index];
			}
			return null;
		}
		
		@Override
		public int count(Object2BooleanFunction<Byte2ObjectMap.Entry<V>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			BasicEntry<V> entry = new BasicEntry<>();
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
				if(o instanceof Byte2ObjectMap.Entry) {
					Byte2ObjectMap.Entry<V> entry = (Byte2ObjectMap.Entry<V>)o;
					int index = ImmutableByte2ObjectOpenHashMap.this.findIndex(entry.getByteKey());
					if(index >= 0) return Objects.equals(entry.getValue(), ImmutableByte2ObjectOpenHashMap.this.values[index]);
				}
				else {
					Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
					int index = ImmutableByte2ObjectOpenHashMap.this.findIndex(entry.getKey());
					if(index >= 0) return Objects.equals(entry.getValue(), ImmutableByte2ObjectOpenHashMap.this.values[index]);
				}
			}
			return false;
		}
		
		@Override
		@Deprecated
		public boolean remove(Object o) { throw new UnsupportedOperationException(); }
		
		@Override
		public int size() {
			return ImmutableByte2ObjectOpenHashMap.this.size();
		}
		
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
	}
	
	private final class KeySet extends AbstractByteSet implements ByteOrderedSet {
		@Override
		public boolean contains(byte e) {
			return containsKey(e);
		}
		
		@Override
		public boolean remove(byte o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean add(byte o) {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean addAndMoveToFirst(byte o) { throw new UnsupportedOperationException(); }

		@Override
		public boolean addAndMoveToLast(byte o) { throw new UnsupportedOperationException(); }

		@Override
		public boolean moveToFirst(byte o) { throw new UnsupportedOperationException(); }

		@Override
		public boolean moveToLast(byte o) { throw new UnsupportedOperationException(); }
		
		@Override
		public ByteListIterator iterator() {
			return new KeyIterator();
		}
		
		@Override
		public ByteBidirectionalIterator iterator(byte fromElement) {
			return new KeyIterator(fromElement);
		}
		
		@Override
		public KeySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public int size() {
			return ImmutableByte2ObjectOpenHashMap.this.size();
		}
		
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		
		@Override
		public byte firstByte() {
			return firstByteKey();
		}
		
		@Override
		public byte pollFirstByte() { throw new UnsupportedOperationException(); }

		@Override
		public byte lastByte() {
			return lastByteKey();
		}

		@Override
		public byte pollLastByte() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(ByteConsumer action) {
			int index = firstIndex;
			while(index != -1){
				action.accept(keys[index]);
				index = (int)links[index];
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectByteConsumer<E> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			int index = firstIndex;
			while(index != -1){
				action.accept(input, keys[index]);
				index = (int)links[index];
			}
		}
		
		@Override
		public boolean matchesAny(Byte2BooleanFunction filter) {
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
		public boolean matchesNone(Byte2BooleanFunction filter) {
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
		public boolean matchesAll(Byte2BooleanFunction filter) {
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
		public byte reduce(byte identity, ByteByteUnaryOperator operator) {
			Objects.requireNonNull(operator);
			byte state = identity;
			int index = firstIndex;
			while(index != -1) {
				state = operator.applyAsByte(state, keys[index]);
				index = (int)links[index];
			}
			return state;
		}
		
		@Override
		public byte reduce(ByteByteUnaryOperator operator) {
			Objects.requireNonNull(operator);
			byte state = (byte)0;
			boolean empty = true;
			int index = firstIndex;
			while(index != -1) {
				if(empty) {
					empty = false;
					state = keys[index];
					index = (int)links[index];
					continue;
				}
				state = operator.applyAsByte(state, keys[index]);
				index = (int)links[index];
			}
			return state;
		}
		
		@Override
		public byte findFirst(Byte2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return (byte)0;
			int index = firstIndex;
			while(index != -1){
				if(filter.get(keys[index])) return keys[index];
				index = (int)links[index];
			}
			return (byte)0;
		}
		
		@Override
		public int count(Byte2BooleanFunction filter) {
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
			return ImmutableByte2ObjectOpenHashMap.this.size();
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
		public boolean matchesAny(Object2BooleanFunction<V> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			int index = firstIndex;
			while(index != -1){
				if(filter.getBoolean(values[index])) return true;
				index = (int)links[index];
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Object2BooleanFunction<V> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			int index = firstIndex;
			while(index != -1){
				if(filter.getBoolean(values[index])) return false;
				index = (int)links[index];
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Object2BooleanFunction<V> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			int index = firstIndex;
			while(index != -1){
				if(!filter.getBoolean(values[index])) return false;
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
		public V findFirst(Object2BooleanFunction<V> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return null;
			int index = firstIndex;
			while(index != -1){
				if(filter.getBoolean(values[index])) return values[index];
				index = (int)links[index];
			}
			return null;
		}
		
		@Override
		public int count(Object2BooleanFunction<V> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			int result = 0;
			int index = firstIndex;
			while(index != -1){
				if(filter.getBoolean(values[index])) result++;
				index = (int)links[index];
			}
			return result;
		}
	}
	
	private class FastEntryIterator extends MapIterator implements ObjectListIterator<Byte2ObjectMap.Entry<V>> {
		MapEntry entry = new MapEntry();
		
		public FastEntryIterator() {}
		public FastEntryIterator(byte from) {
			super(from);
		}
		
		@Override
		public Byte2ObjectMap.Entry<V> next() {
			entry.index = nextEntry();
			return entry;
		}
		
		@Override
		public Byte2ObjectMap.Entry<V> previous() {
			entry.index = previousEntry();
			return entry;
		}
		
		@Override
		public void set(Byte2ObjectMap.Entry<V> entry) { throw new UnsupportedOperationException(); }

		@Override
		public void add(Byte2ObjectMap.Entry<V> entry) { throw new UnsupportedOperationException(); }
	}
	
	private class EntryIterator extends MapIterator implements ObjectListIterator<Byte2ObjectMap.Entry<V>> {
		
		public EntryIterator() {}
		public EntryIterator(byte from) {
			super(from);
		}
		
		@Override
		public Byte2ObjectMap.Entry<V> next() {
			return new MapEntry(nextEntry());
		}
		
		@Override
		public Byte2ObjectMap.Entry<V> previous() {
			return new MapEntry(previousEntry());
		}
		
		@Override
		public void remove() { throw new UnsupportedOperationException(); }

		@Override
		public void set(Byte2ObjectMap.Entry<V> entry) { throw new UnsupportedOperationException(); }

		@Override
		public void add(Byte2ObjectMap.Entry<V> entry) { throw new UnsupportedOperationException(); }
	}
	
	private class KeyIterator extends MapIterator implements ByteListIterator {
		
		public KeyIterator() {}
		public KeyIterator(byte from) {
			super(from);
		}
		
		@Override
		public byte previousByte() {
			return keys[previousEntry()];
		}

		@Override
		public byte nextByte() {
			return keys[nextEntry()];
		}
		
		@Override
		public void set(byte e) { throw new UnsupportedOperationException(); }
		@Override
		public void add(byte e) { throw new UnsupportedOperationException(); }
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
		
		MapIterator(byte from) {
			if(from == (byte)0) {
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
				int pos = HashUtil.mix(Byte.hashCode(from)) & mask;
				while(keys[pos] != (byte)0) {
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