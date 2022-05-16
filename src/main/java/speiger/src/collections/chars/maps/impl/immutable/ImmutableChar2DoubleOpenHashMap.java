package speiger.src.collections.chars.maps.impl.immutable;

import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.BiFunction;

import speiger.src.collections.chars.collections.CharBidirectionalIterator;
import speiger.src.collections.chars.functions.CharConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectCharConsumer;
import speiger.src.collections.chars.functions.function.Char2BooleanFunction;
import speiger.src.collections.chars.functions.consumer.CharDoubleConsumer;
import speiger.src.collections.chars.functions.function.Char2DoubleFunction;
import speiger.src.collections.chars.functions.function.CharDoubleUnaryOperator;
import speiger.src.collections.chars.functions.function.CharCharUnaryOperator;
import speiger.src.collections.chars.lists.CharListIterator;
import speiger.src.collections.chars.maps.interfaces.Char2DoubleOrderedMap;
import speiger.src.collections.chars.maps.abstracts.AbstractChar2DoubleMap;
import speiger.src.collections.chars.maps.interfaces.Char2DoubleMap;
import speiger.src.collections.chars.sets.CharOrderedSet;
import speiger.src.collections.chars.utils.maps.Char2DoubleMaps;
import speiger.src.collections.chars.utils.CharArrays;
import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;
import speiger.src.collections.objects.functions.function.Object2BooleanFunction;
import speiger.src.collections.objects.functions.consumer.ObjectDoubleConsumer;
import speiger.src.collections.doubles.functions.function.Double2BooleanFunction;
import speiger.src.collections.chars.sets.AbstractCharSet;
import speiger.src.collections.chars.sets.CharSet;
import speiger.src.collections.doubles.collections.AbstractDoubleCollection;
import speiger.src.collections.doubles.collections.DoubleCollection;
import speiger.src.collections.doubles.collections.DoubleIterator;
import speiger.src.collections.doubles.functions.DoubleSupplier;
import speiger.src.collections.doubles.functions.function.DoubleDoubleUnaryOperator;
import speiger.src.collections.doubles.functions.DoubleConsumer;
import speiger.src.collections.doubles.lists.DoubleListIterator;
import speiger.src.collections.doubles.utils.DoubleArrays;
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
public class ImmutableChar2DoubleOpenHashMap extends AbstractChar2DoubleMap implements Char2DoubleOrderedMap
{
	/** The Backing keys array */
	protected transient char[] keys;
	/** The Backing values array */
	protected transient double[] values;
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
	protected transient CharSet keySet;
	/** Values cache */
	protected transient DoubleCollection valuesC;
	
	/** Amount of Elements stored in the HashMap */
	protected int size;
	
	/**
	 * Helper constructor for copying the Map
	 */
	protected ImmutableChar2DoubleOpenHashMap() {}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public ImmutableChar2DoubleOpenHashMap(Character[] keys, Double[] values) {
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
	public ImmutableChar2DoubleOpenHashMap(Character[] keys, Double[] values, float loadFactor) {
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		init(CharArrays.unwrap(keys), DoubleArrays.unwrap(values), 0, keys.length, loadFactor);
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public ImmutableChar2DoubleOpenHashMap(char[] keys, double[] values) {
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
	public ImmutableChar2DoubleOpenHashMap(char[] keys, double[] values, float loadFactor) {
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		init(keys, values, 0, keys.length, loadFactor);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 */
	public ImmutableChar2DoubleOpenHashMap(Map<? extends Character, ? extends Double> map) {
		this(map, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public ImmutableChar2DoubleOpenHashMap(Map<? extends Character, ? extends Double> map, float loadFactor) {
		char[] keys = new char[map.size()];
		double[] values = new double[keys.length];
		int index = 0;
		for(Map.Entry<? extends Character, ? extends Double> entry : map.entrySet()) {
			keys[index] = entry.getKey().charValue();
			values[index] = entry.getValue().doubleValue();
			index++;
		}
		init(keys, values, 0, index, loadFactor);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 */
	public ImmutableChar2DoubleOpenHashMap(Char2DoubleMap map) {
		this(map, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
 	 */
	public ImmutableChar2DoubleOpenHashMap(Char2DoubleMap map, float loadFactor) {
		char[] keys = new char[map.size()];
		double[] values = new double[keys.length];
		int index = 0;
		for(Char2DoubleMap.Entry entry : Char2DoubleMaps.fastIterable(map)) {
			keys[index] = entry.getCharKey();
			values[index] = entry.getDoubleValue();
			index++;
		}
		init(keys, values, 0, index, loadFactor);
	}
	
	protected void init(char[] a, double[] b, int offset, int length, float loadFactor) {
		SanityChecks.checkArrayCapacity(a.length, offset, length);
		int newSize = HashUtil.arraySize(length+1, loadFactor);
		int newMask = newSize - 1;
		char[] newKeys = new char[newSize + 1];
		double[] newValues = new double[newSize + 1];
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
	public double put(char key, double value) { throw new UnsupportedOperationException(); }
	@Override
	public double putIfAbsent(char key, double value) { throw new UnsupportedOperationException(); }	
	@Override
	public double addTo(char key, double value) { throw new UnsupportedOperationException(); }
	@Override
	public double subFrom(char key, double value) { throw new UnsupportedOperationException(); }	
	@Override
	public double putAndMoveToFirst(char key, double value) { throw new UnsupportedOperationException(); }
	@Override
	public double putAndMoveToLast(char key, double value) { throw new UnsupportedOperationException(); }
	@Override
	public boolean moveToFirst(char key) { throw new UnsupportedOperationException(); }
	@Override
	public boolean moveToLast(char key) { throw new UnsupportedOperationException(); }
	@Override
	public double getAndMoveToFirst(char key) { throw new UnsupportedOperationException(); }
	@Override
	public double getAndMoveToLast(char key) { throw new UnsupportedOperationException(); }
	
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
	public boolean containsValue(double value) {
		int index = firstIndex;
		while(index != -1) {
			if(Double.doubleToLongBits(values[index]) == Double.doubleToLongBits(value)) return true;
			index = (int)links[index];
		}
		return false;
	}
	
	@Override
	@Deprecated
	public boolean containsValue(Object value) {
		int index = firstIndex;
		while(index != -1) {
			if((value == null && values[index] == getDefaultReturnValue()) || Objects.equals(value, Double.valueOf(values[index]))) return true;
			index = (int)links[index];
		}
		return false;
	}
	
	@Override
	public double remove(char key) { throw new UnsupportedOperationException(); }
	
	@Override
	public double removeOrDefault(char key, double defaultValue) { throw new UnsupportedOperationException(); }
	
	@Override
	@Deprecated
	public Double remove(Object key) { throw new UnsupportedOperationException(); }
	
	@Override
	public boolean remove(char key, double value) { throw new UnsupportedOperationException(); }
	
	@Override
	public boolean remove(Object key, Object value) { throw new UnsupportedOperationException(); }
	
	@Override
	public double get(char key) {
		int slot = findIndex(key);
		return slot < 0 ? getDefaultReturnValue() : values[slot];
	}
	
	@Override
	public Double get(Object key) {
		int slot = findIndex(key);
		return Double.valueOf(slot < 0 ? getDefaultReturnValue() : values[slot]);
	}
	
	@Override
	public double getOrDefault(char key, double defaultValue) {
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
	public double firstDoubleValue() {
		if(size == 0) throw new NoSuchElementException();
		return values[firstIndex];
	}
	
	@Override
	public double lastDoubleValue() {
		if(size == 0) throw new NoSuchElementException();
		return values[lastIndex];
	}	

	@Override
	public ObjectSet<Char2DoubleMap.Entry> char2DoubleEntrySet() {
		if(entrySet == null) entrySet = new MapEntrySet();
		return entrySet;
	}
	
	@Override
	public CharSet keySet() {
		if(keySet == null) keySet = new KeySet();
		return keySet;
	}
	
	@Override
	public DoubleCollection values() {
		if(valuesC == null) valuesC = new Values();
		return valuesC;
	}
	
	@Override
	public ImmutableChar2DoubleOpenHashMap copy() {
		ImmutableChar2DoubleOpenHashMap map = new ImmutableChar2DoubleOpenHashMap();
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
	public void forEach(CharDoubleConsumer action) {
		int index = firstIndex;
		while(index != -1){
			action.accept(keys[index], values[index]);
			index = (int)links[index];
		}
	}
	
	@Override
	public boolean replace(char key, double oldValue, double newValue) { throw new UnsupportedOperationException(); }
	
	@Override
	public double replace(char key, double value) { throw new UnsupportedOperationException(); }
	
	@Override
	public double computeDouble(char key, CharDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
	
	@Override
	public double computeDoubleIfAbsent(char key, Char2DoubleFunction mappingFunction) { throw new UnsupportedOperationException(); }
	
	@Override
	public double supplyDoubleIfAbsent(char key, DoubleSupplier valueProvider) { throw new UnsupportedOperationException(); }
	
	@Override
	public double computeDoubleIfPresent(char key, CharDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
	
	@Override
	public double mergeDouble(char key, double value, DoubleDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
	
	@Override
	public void mergeAllDouble(Char2DoubleMap m, DoubleDoubleUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
	
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
		int pos = HashUtil.mix(key.hashCode()) & mask;
		char current = keys[pos];
		if(current != (char)0) {
			if(Objects.equals(key, Character.valueOf(current))) return pos;
			while((current = keys[pos = (++pos & mask)]) != (char)0)
				if(Objects.equals(key, Character.valueOf(current))) return pos;
		}
		return -(pos + 1);
	}
	
	protected class MapEntry implements Char2DoubleMap.Entry, Map.Entry<Character, Double> {
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
		public double getDoubleValue() {
			return values[index];
		}

		@Override
		public double setValue(double value) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Map.Entry) {
				if(obj instanceof Char2DoubleMap.Entry) {
					Char2DoubleMap.Entry entry = (Char2DoubleMap.Entry)obj;
					return keys[index] == entry.getCharKey() && Double.doubleToLongBits(values[index]) == Double.doubleToLongBits(entry.getDoubleValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				if(!(key instanceof Character)) return false;
				Object value = entry.getValue();
				return key instanceof Character && value instanceof Double && keys[index] == ((Character)key).charValue() && Double.doubleToLongBits(values[index]) == Double.doubleToLongBits(((Double)value).doubleValue());
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Character.hashCode(keys[index]) ^ Double.hashCode(values[index]);
		}
		
		@Override
		public String toString() {
			return Character.toString(keys[index]) + "=" + Double.toString(values[index]);
		}
	}
	
	private class MapEntrySet extends AbstractObjectSet<Char2DoubleMap.Entry> implements Char2DoubleOrderedMap.FastOrderedSet {
		@Override
		public boolean addAndMoveToFirst(Char2DoubleMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Char2DoubleMap.Entry o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean moveToFirst(Char2DoubleMap.Entry o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean moveToLast(Char2DoubleMap.Entry o) { throw new UnsupportedOperationException(); }
		
		@Override
		public Char2DoubleMap.Entry first() {
			return new BasicEntry(firstCharKey(), firstDoubleValue());
		}
		
		@Override
		public Char2DoubleMap.Entry last() {
			return new BasicEntry(lastCharKey(), lastDoubleValue());
		}
		
		@Override
		public Char2DoubleMap.Entry pollFirst() { throw new UnsupportedOperationException(); }
		
		@Override
		public Char2DoubleMap.Entry pollLast() { throw new UnsupportedOperationException(); }
		
		@Override
		public ObjectBidirectionalIterator<Char2DoubleMap.Entry> iterator() {
			return new EntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Char2DoubleMap.Entry> iterator(Char2DoubleMap.Entry fromElement) {
			return new EntryIterator(fromElement.getCharKey());
		}
		
		@Override
		public ObjectBidirectionalIterator<Char2DoubleMap.Entry> fastIterator() {
			return new FastEntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Char2DoubleMap.Entry> fastIterator(char fromElement) {
			return new FastEntryIterator(fromElement);
		}
		
		@Override
		public MapEntrySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(Consumer<? super Char2DoubleMap.Entry> action) {
			int index = firstIndex;
			while(index != -1){
				action.accept(new BasicEntry(keys[index], values[index]));
				index = (int)links[index];
			}
		}
		
		@Override
		public void fastForEach(Consumer<? super Char2DoubleMap.Entry> action) {
			BasicEntry entry = new BasicEntry();
			int index = firstIndex;
			while(index != -1){
				entry.set(keys[index], values[index]);
				action.accept(entry);
				index = (int)links[index];
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, Char2DoubleMap.Entry> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			int index = firstIndex;
			while(index != -1) {
				action.accept(input, new BasicEntry(keys[index], values[index]));
				index = (int)links[index];
			}
		}
		
		@Override
		public boolean matchesAny(Object2BooleanFunction<Char2DoubleMap.Entry> filter) {
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
		public boolean matchesNone(Object2BooleanFunction<Char2DoubleMap.Entry> filter) {
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
		public boolean matchesAll(Object2BooleanFunction<Char2DoubleMap.Entry> filter) {
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
		public <E> E reduce(E identity, BiFunction<E, Char2DoubleMap.Entry, E> operator) {
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
		public Char2DoubleMap.Entry reduce(ObjectObjectUnaryOperator<Char2DoubleMap.Entry, Char2DoubleMap.Entry> operator) {
			Objects.requireNonNull(operator);
			Char2DoubleMap.Entry state = null;
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
		public Char2DoubleMap.Entry findFirst(Object2BooleanFunction<Char2DoubleMap.Entry> filter) {
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
		public int count(Object2BooleanFunction<Char2DoubleMap.Entry> filter) {
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
				if(o instanceof Char2DoubleMap.Entry) {
					Char2DoubleMap.Entry entry = (Char2DoubleMap.Entry)o;
					int index = ImmutableChar2DoubleOpenHashMap.this.findIndex(entry.getCharKey());
					if(index >= 0) return Double.doubleToLongBits(entry.getDoubleValue()) == Double.doubleToLongBits(ImmutableChar2DoubleOpenHashMap.this.values[index]);
				}
				else {
					Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
					int index = ImmutableChar2DoubleOpenHashMap.this.findIndex(entry.getKey());
					if(index >= 0) return Objects.equals(entry.getValue(), Double.valueOf(ImmutableChar2DoubleOpenHashMap.this.values[index]));
				}
			}
			return false;
		}
		
		@Override
		@Deprecated
		public boolean remove(Object o) { throw new UnsupportedOperationException(); }
		
		@Override
		public int size() {
			return ImmutableChar2DoubleOpenHashMap.this.size();
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
			return ImmutableChar2DoubleOpenHashMap.this.size();
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
		public boolean matchesAny(Char2BooleanFunction filter) {
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
		public boolean matchesNone(Char2BooleanFunction filter) {
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
		public boolean matchesAll(Char2BooleanFunction filter) {
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
		public char findFirst(Char2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return (char)0;
			int index = firstIndex;
			while(index != -1){
				if(filter.get(keys[index])) return keys[index];
				index = (int)links[index];
			}
			return (char)0;
		}
		
		@Override
		public int count(Char2BooleanFunction filter) {
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
	
	private class Values extends AbstractDoubleCollection {
		@Override
		public boolean contains(double e) {
			return containsValue(e);
		}
		
		@Override
		public boolean add(double o) {
			throw new UnsupportedOperationException();
		}

		@Override
		public DoubleIterator iterator() {
			return new ValueIterator();
		}
		
		@Override
		public int size() {
			return ImmutableChar2DoubleOpenHashMap.this.size();
		}
		
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(DoubleConsumer action) {
			int index = firstIndex;
			while(index != -1){
				action.accept(values[index]);
				index = (int)links[index];
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectDoubleConsumer<E> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			int index = firstIndex;
			while(index != -1){
				action.accept(input, values[index]);
				index = (int)links[index];
			}
		}
		
		@Override
		public boolean matchesAny(Double2BooleanFunction filter) {
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
		public boolean matchesNone(Double2BooleanFunction filter) {
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
		public boolean matchesAll(Double2BooleanFunction filter) {
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
		public double reduce(double identity, DoubleDoubleUnaryOperator operator) {
			Objects.requireNonNull(operator);
			double state = identity;
			int index = firstIndex;
			while(index != -1) {
				state = operator.applyAsDouble(state, values[index]);
				index = (int)links[index];
			}
			return state;
		}
		
		@Override
		public double reduce(DoubleDoubleUnaryOperator operator) {
			Objects.requireNonNull(operator);
			double state = 0D;
			boolean empty = true;
			int index = firstIndex;
			while(index != -1) {
				if(empty) {
					empty = false;
					state = values[index];
					index = (int)links[index];
					continue;
				}
				state = operator.applyAsDouble(state, values[index]);
				index = (int)links[index];
			}
			return state;
		}
		
		@Override
		public double findFirst(Double2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0D;
			int index = firstIndex;
			while(index != -1){
				if(filter.get(values[index])) return values[index];
				index = (int)links[index];
			}
			return 0D;
		}
		
		@Override
		public int count(Double2BooleanFunction filter) {
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
	
	private class FastEntryIterator extends MapIterator implements ObjectListIterator<Char2DoubleMap.Entry> {
		MapEntry entry = new MapEntry();
		
		public FastEntryIterator() {}
		public FastEntryIterator(char from) {
			super(from);
		}
		
		@Override
		public Char2DoubleMap.Entry next() {
			entry.index = nextEntry();
			return entry;
		}
		
		@Override
		public Char2DoubleMap.Entry previous() {
			entry.index = previousEntry();
			return entry;
		}
		
		@Override
		public void set(Char2DoubleMap.Entry entry) { throw new UnsupportedOperationException(); }

		@Override
		public void add(Char2DoubleMap.Entry entry) { throw new UnsupportedOperationException(); }
	}
	
	private class EntryIterator extends MapIterator implements ObjectListIterator<Char2DoubleMap.Entry> {
		
		public EntryIterator() {}
		public EntryIterator(char from) {
			super(from);
		}
		
		@Override
		public Char2DoubleMap.Entry next() {
			return new MapEntry(nextEntry());
		}
		
		@Override
		public Char2DoubleMap.Entry previous() {
			return new MapEntry(previousEntry());
		}
		
		@Override
		public void remove() { throw new UnsupportedOperationException(); }

		@Override
		public void set(Char2DoubleMap.Entry entry) { throw new UnsupportedOperationException(); }

		@Override
		public void add(Char2DoubleMap.Entry entry) { throw new UnsupportedOperationException(); }
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
	
	private class ValueIterator extends MapIterator implements DoubleListIterator {
		public ValueIterator() {}
		
		@Override
		public double previousDouble() {
			return values[previousEntry()];
		}

		@Override
		public double nextDouble() {
			return values[nextEntry()];
		}

		@Override
		public void set(double e) { throw new UnsupportedOperationException(); }

		@Override
		public void add(double e) { throw new UnsupportedOperationException(); }
		
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