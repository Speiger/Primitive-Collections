package speiger.src.collections.objects.maps.impl.misc;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.Objects;

import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;
import speiger.src.collections.objects.functions.consumer.ObjectDoubleConsumer;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.maps.interfaces.Object2DoubleMap;
import speiger.src.collections.objects.maps.interfaces.Object2DoubleOrderedMap;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
import speiger.src.collections.doubles.collections.AbstractDoubleCollection;
import speiger.src.collections.doubles.collections.DoubleCollection;
import speiger.src.collections.doubles.collections.DoubleIterator;
import speiger.src.collections.doubles.functions.DoubleConsumer;
import speiger.src.collections.doubles.lists.DoubleListIterator;

/**
 * A Type Specific LinkedEnumMap implementation that allows for Primitive Values and faster iteration.
 * Unlike javas implementation this one does not jump around between a single long or long array implementation based around the enum size
 * This will cause a bit more memory usage but allows for a simpler implementation.
 * @param <T> the keyType of elements maintained by this Collection
 */
public class LinkedEnum2DoubleMap<T extends Enum<T>> extends Enum2DoubleMap<T> implements Object2DoubleOrderedMap<T>
{
	/** The Backing array for links between nodes. Left 32 Bits => Previous Entry, Right 32 Bits => Next Entry */
	protected long[] links;
	/** The First Index in the Map */
	protected int firstIndex = -1;
	/** The Last Index in the Map */
	protected int lastIndex = -1;
	/**
	 * Default Constructor
	 * @param keyType the type of Enum that should be used
	 */
	public LinkedEnum2DoubleMap(Class<T> keyType) {
		super(keyType);
		links = new long[keys.length];
	}
	
	/**
	 * Helper constructor that allow to create a EnumMap from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the EnumMap
	 * @param values the values that should be put into the EnumMap.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public LinkedEnum2DoubleMap(T[] keys, Double[] values) {
		if(keys.length <= 0) throw new IllegalArgumentException("Empty Array are not allowed");
		if(keys.length != values.length) throw new IllegalArgumentException("Keys and Values have to be the same size");
		keyType = keys[0].getDeclaringClass();
		this.keys = getKeyUniverse(keyType);
		this.values = new double[this.keys.length];
		present = new long[((this.keys.length - 1) >> 6) + 1];
		links = new long[this.keys.length];
		putAll(keys, values);
	}
	
	/**
	 * Helper constructor that allow to create a EnumMap from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public LinkedEnum2DoubleMap(T[] keys, double[] values) {
		if(keys.length <= 0) throw new IllegalArgumentException("Empty Array are not allowed");
		if(keys.length != values.length) throw new IllegalArgumentException("Keys and Values have to be the same size");
		keyType = keys[0].getDeclaringClass();
		this.keys = getKeyUniverse(keyType);
		this.values = new double[this.keys.length];
		present = new long[((this.keys.length - 1) >> 6) + 1];
		links = new long[this.keys.length];
		putAll(keys, values);		
	}
	
	/**
	 * A Helper constructor that allows to create a EnumMap with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 */
	public LinkedEnum2DoubleMap(Map<? extends T, ? extends Double> map) {
		if(map instanceof LinkedEnum2DoubleMap) {
			LinkedEnum2DoubleMap<T> enumMap = (LinkedEnum2DoubleMap<T>)map;
			keyType = enumMap.keyType;
			keys = enumMap.keys;
			values = enumMap.values.clone();
			present = enumMap.present.clone();
			links = enumMap.links.clone();
			size = enumMap.size;
		}
		else if(map instanceof Enum2DoubleMap) {
			Enum2DoubleMap<T> enumMap = (Enum2DoubleMap<T>)map;
			keyType = enumMap.keyType;
			keys = enumMap.keys;
			values = enumMap.values.clone();
			present = enumMap.present.clone();
			links = new long[keys.length];
			for(int i = 0,m=keys.length;i<m;i++) {
				if(isSet(i)) {
					if(size == 0) {
						firstIndex = lastIndex = i;
						links[i] = -1L;
					}
					else {
						links[lastIndex] ^= ((links[lastIndex] ^ (i & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
						links[i] = ((lastIndex & 0xFFFFFFFFL) << 32) | 0xFFFFFFFFL;
						lastIndex = i;
					}
					size++;
				}
			}
		}
		else if(map.isEmpty()) throw new IllegalArgumentException("Empty Maps are not allowed");
		else {
			keyType = map.keySet().iterator().next().getDeclaringClass();
			this.keys = getKeyUniverse(keyType);
			this.values = new double[keys.length];
			present = new long[((keys.length - 1) >> 6) + 1];
			links = new long[keys.length];
			putAll(map);
		}
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new EnumMap with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 */
	public LinkedEnum2DoubleMap(Object2DoubleMap<T> map) {
		if(map instanceof LinkedEnum2DoubleMap) {
			LinkedEnum2DoubleMap<T> enumMap = (LinkedEnum2DoubleMap<T>)map;
			keyType = enumMap.keyType;
			keys = enumMap.keys;
			values = enumMap.values.clone();
			present = enumMap.present.clone();
			links = enumMap.links.clone();
			size = enumMap.size;
		}
		else if(map instanceof Enum2DoubleMap) {
			Enum2DoubleMap<T> enumMap = (Enum2DoubleMap<T>)map;
			keyType = enumMap.keyType;
			keys = enumMap.keys;
			values = enumMap.values.clone();
			present = enumMap.present.clone();
			links = new long[keys.length];
			for(int i = 0,m=keys.length;i<m;i++) {
				if(isSet(i)) {
					if(size == 0) {
						firstIndex = lastIndex = i;
						links[i] = -1L;
					}
					else {
						links[lastIndex] ^= ((links[lastIndex] ^ (i & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
						links[i] = ((lastIndex & 0xFFFFFFFFL) << 32) | 0xFFFFFFFFL;
						lastIndex = i;
					}
					size++;
				}
			}
		}
		else if(map.isEmpty()) throw new IllegalArgumentException("Empty Maps are not allowed");
		else {
			keyType = map.keySet().iterator().next().getDeclaringClass();
			this.keys = getKeyUniverse(keyType);
			this.values = new double[keys.length];
			present = new long[((keys.length - 1) >> 6) + 1];
			links = new long[keys.length];
			putAll(map);
		}
	}
	
	@Override
	public double putAndMoveToFirst(T key, double value) {
		int index = key.ordinal();
		if(isSet(index)) {
			double result = values[index];
			values[index] = value;
			moveToFirstIndex(index);
			return result;
		}
		set(index);
		values[index] = value;
		onNodeAdded(index);
		moveToFirstIndex(index);
		return getDefaultReturnValue();
	}
	
	@Override
	public double putAndMoveToLast(T key, double value) {
		int index = key.ordinal();
		if(isSet(index)) {
			double result = values[index];
			values[index] = value;
			moveToLastIndex(index);
			return result;
		}
		set(index);
		values[index] = value;
		onNodeAdded(index);
		moveToLastIndex(index);
		return getDefaultReturnValue();
	}
	
	@Override
	public boolean moveToFirst(T key) {
		int index = key.ordinal();
		if(isSet(index)) {
			moveToFirstIndex(index);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean moveToLast(T key) {
		int index = key.ordinal();
		if(isSet(index)) {
			moveToLastIndex(index);
			return true;
		}
		return false;
	}
	
	@Override
	public double getAndMoveToFirst(T key) {
		int index = key.ordinal();
		if(!isSet(index)) return getDefaultReturnValue();
		moveToFirstIndex(index);
		return values[index];
	}
	
	@Override
	public double getAndMoveToLast(T key) {
		int index = key.ordinal();
		if(!isSet(index)) return getDefaultReturnValue();
		moveToLastIndex(index);
		return values[index];
	}
	
	@Override
	public LinkedEnum2DoubleMap<T> copy() {
		LinkedEnum2DoubleMap<T> map = new LinkedEnum2DoubleMap<>(keyType);
		map.size = size;
		System.arraycopy(present, 0, map.present, 0, Math.min(present.length, map.present.length));
		System.arraycopy(values, 0, map.values, 0, Math.min(values.length, map.values.length));
		System.arraycopy(links, 0, map.links, 0, Math.min(links.length, map.links.length));
		map.firstIndex = firstIndex;
		map.lastIndex = lastIndex;
		return map;
	}
	
	@Override
	public T firstKey() {
		if(size == 0) throw new NoSuchElementException();
		return keys[firstIndex];
	}
	
	@Override
	public T pollFirstKey() {
		if(size == 0) throw new NoSuchElementException();
		int pos = firstIndex;
		firstIndex = (int)links[pos];
		if(0 <= firstIndex) links[firstIndex] |= 0xFFFFFFFF00000000L;
		T result = keys[pos];
		size--;
		values[result.ordinal()] = 0D;
		return result;
	}
	
	@Override
	public T lastKey() {
		if(size == 0) throw new NoSuchElementException();
		return keys[lastIndex];
	}
	
	@Override
	public T pollLastKey() {
		if(size == 0) throw new NoSuchElementException();
		int pos = lastIndex;
		lastIndex = (int)(links[pos] >>> 32);
		if(0 <= lastIndex) links[lastIndex] |= 0xFFFFFFFFL;
		T result = keys[pos];
		size--;
		values[result.ordinal()] = 0D;
		return result;
	}
	
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
	public ObjectOrderedSet<Object2DoubleMap.Entry<T>> object2DoubleEntrySet() {
		if(entrySet == null) entrySet = new MapEntrySet();
		return (ObjectOrderedSet<Object2DoubleMap.Entry<T>>)entrySet;
	}
	
	@Override
	public ObjectOrderedSet<T> keySet() {
		if(keySet == null) keySet = new KeySet();
		return (ObjectOrderedSet<T>)keySet;
	}
	
	@Override
	public DoubleCollection values() {
		if(valuesC == null) valuesC = new Values();
		return valuesC;
	}
	
	@Override
	public void forEach(ObjectDoubleConsumer<T> action) {
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
	
	private class MapEntrySet extends AbstractObjectSet<Object2DoubleMap.Entry<T>> implements Object2DoubleOrderedMap.FastOrderedSet<T> {
		@Override
		public boolean addAndMoveToFirst(Object2DoubleMap.Entry<T> o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Object2DoubleMap.Entry<T> o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean moveToFirst(Object2DoubleMap.Entry<T> o) {
			return LinkedEnum2DoubleMap.this.moveToFirst(o.getKey());
		}
		
		@Override
		public boolean moveToLast(Object2DoubleMap.Entry<T> o) {
			return LinkedEnum2DoubleMap.this.moveToLast(o.getKey());
		}
		
		@Override
		public Object2DoubleMap.Entry<T> first() {
			return new BasicEntry<>(firstKey(), firstDoubleValue());
		}
		
		@Override
		public Object2DoubleMap.Entry<T> last() {
			return new BasicEntry<>(lastKey(), lastDoubleValue());
		}
		
		@Override
		public Object2DoubleMap.Entry<T> pollFirst() {
			BasicEntry<T> entry = new BasicEntry<>(firstKey(), firstDoubleValue());
			pollFirstKey();
			return entry;
		}
		
		@Override
		public Object2DoubleMap.Entry<T> pollLast() {
			BasicEntry<T> entry = new BasicEntry<>(lastKey(), lastDoubleValue());
			pollLastKey();
			return entry;
		}
		
		@Override
		public ObjectBidirectionalIterator<Object2DoubleMap.Entry<T>> iterator() {
			return new EntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Object2DoubleMap.Entry<T>> iterator(Object2DoubleMap.Entry<T> fromElement) {
			return new EntryIterator(fromElement.getKey());
		}
		
		@Override
		public ObjectBidirectionalIterator<Object2DoubleMap.Entry<T>> fastIterator() {
			return new FastEntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Object2DoubleMap.Entry<T>> fastIterator(T fromElement) {
			return new FastEntryIterator(fromElement);
		}
		
		public MapEntrySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(Consumer<? super Object2DoubleMap.Entry<T>> action) {
			int index = firstIndex;
			while(index != -1){
				action.accept(new ValueMapEntry(index));
				index = (int)links[index];
			}
		}
		
		@Override
		public void fastForEach(Consumer<? super Object2DoubleMap.Entry<T>> action) {
			MapEntry entry = new MapEntry();
			int index = firstIndex;
			while(index != -1){
				entry.set(index);
				action.accept(entry);
				index = (int)links[index];
			}
		}
		
		@Override
		@Deprecated
		public boolean contains(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Object2DoubleMap.Entry) {
					Object2DoubleMap.Entry<T> entry = (Object2DoubleMap.Entry<T>)o;
					if(!keyType.isInstance(entry.getKey())) return false;
					int index = ((T)entry.getKey()).ordinal();
					if(index >= 0 && LinkedEnum2DoubleMap.this.isSet(index)) return Double.doubleToLongBits(entry.getDoubleValue()) == Double.doubleToLongBits(LinkedEnum2DoubleMap.this.values[index]);
				}
				else {
					Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
					if(!keyType.isInstance(entry.getKey())) return false;
					int index = ((T)entry.getKey()).ordinal();
					if(index >= 0 && LinkedEnum2DoubleMap.this.isSet(index)) return Objects.equals(entry.getValue(), Double.valueOf(LinkedEnum2DoubleMap.this.values[index]));
				}
			}
			return false;
		}
		
		@Override
		@Deprecated
		public boolean remove(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Object2DoubleMap.Entry) {
					Object2DoubleMap.Entry<T> entry = (Object2DoubleMap.Entry<T>)o;
					return LinkedEnum2DoubleMap.this.remove(entry.getKey(), entry.getDoubleValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
				return LinkedEnum2DoubleMap.this.remove(entry.getKey(), entry.getValue());
			}
			return false;
		}
		
		@Override
		public int size() {
			return LinkedEnum2DoubleMap.this.size();
		}
		
		@Override
		public void clear() {
			LinkedEnum2DoubleMap.this.clear();
		}
	}
	
	private final class KeySet extends AbstractObjectSet<T> implements ObjectOrderedSet<T> {
		@Override
		@Deprecated
		public boolean contains(Object e) {
			return containsKey(e);
		}
		
		@Override
		public boolean remove(Object o) {
			int oldSize = size;
			LinkedEnum2DoubleMap.this.remove(o);
			return size != oldSize;
		}
		
		@Override
		public boolean add(T o) {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean addAndMoveToFirst(T o) { throw new UnsupportedOperationException(); }

		@Override
		public boolean addAndMoveToLast(T o) { throw new UnsupportedOperationException(); }

		@Override
		public boolean moveToFirst(T o) {
			return LinkedEnum2DoubleMap.this.moveToFirst(o);
		}

		@Override
		public boolean moveToLast(T o) {
			return LinkedEnum2DoubleMap.this.moveToLast(o);
		}
		
		@Override
		public ObjectListIterator<T> iterator() {
			return new KeyIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<T> iterator(T fromElement) {
			return new KeyIterator(fromElement);
		}
		
		public KeySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public int size() {
			return LinkedEnum2DoubleMap.this.size();
		}
		
		@Override
		public void clear() {
			LinkedEnum2DoubleMap.this.clear();
		}
		
		@Override
		public T first() {
			return firstKey();
		}
		
		@Override
		public T pollFirst() {
			return pollFirstKey();
		}

		@Override
		public T last() {
			return lastKey();
		}

		@Override
		public T pollLast() {
			return pollLastKey();
		}
		
		@Override
		public void forEach(Consumer<? super T> action) {
			int index = firstIndex;
			while(index != -1){
				action.accept(keys[index]);
				index = (int)links[index];
			}
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
			return LinkedEnum2DoubleMap.this.size();
		}
		
		@Override
		public void clear() {
			LinkedEnum2DoubleMap.this.clear();
		}
		
		@Override
		public void forEach(DoubleConsumer action) {
			int index = firstIndex;
			while(index != -1){
				action.accept(values[index]);
				index = (int)links[index];
			}
		}
	}
	
	private class FastEntryIterator extends MapIterator implements ObjectListIterator<Object2DoubleMap.Entry<T>> {
		MapEntry entry = new MapEntry();
		
		public FastEntryIterator() {}
		public FastEntryIterator(T from) {
			super(from);
		}
		
		@Override
		public Object2DoubleMap.Entry<T> next() {
			entry.index = nextEntry();
			return entry;
		}
		
		@Override
		public Object2DoubleMap.Entry<T> previous() {
			entry.index = previousEntry();
			return entry;
		}
		
		@Override
		public void set(Object2DoubleMap.Entry<T> entry) { throw new UnsupportedOperationException(); }

		@Override
		public void add(Object2DoubleMap.Entry<T> entry) { throw new UnsupportedOperationException(); }
	}
	
	private class EntryIterator extends MapIterator implements ObjectListIterator<Object2DoubleMap.Entry<T>> {
		MapEntry entry;
		
		public EntryIterator() {}
		public EntryIterator(T from) {
			super(from);
		}
		
		@Override
		public Object2DoubleMap.Entry<T> next() {
			return entry = new ValueMapEntry(nextEntry());
		}
		
		@Override
		public Object2DoubleMap.Entry<T> previous() {
			return entry = new ValueMapEntry(previousEntry());
		}
		
		@Override
		public void remove() {
			super.remove();
			entry.index = -1;
		}

		@Override
		public void set(Object2DoubleMap.Entry<T> entry) { throw new UnsupportedOperationException(); }

		@Override
		public void add(Object2DoubleMap.Entry<T> entry) { throw new UnsupportedOperationException(); }
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
		
		MapIterator(T from) {
			previous = from.ordinal() - 1;
			index = from.ordinal();
			next = from.ordinal();
			if(!isSet(index)) throw new NoSuchElementException();
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
			
			if (next == -1) lastIndex = previous;
			else links[next] ^= ((links[next] ^ ((previous & 0xFFFFFFFFL) << 32)) & 0xFFFFFFFF00000000L);
			values[current] = 0D;
			present[current >> 6] &= ~(1L << current);
			current = -1;
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
	
	protected class ValueMapEntry extends MapEntry {
		protected T key;
		protected double value;
		
		public ValueMapEntry(int index) {
			super(index);
			key = keys[index];
			value = values[index];
		}
		
		@Override
		public T getKey() {
			return key;
		}

		@Override
		public double getDoubleValue() {
			return value;
		}
		
		@Override
		public double setValue(double value) {
			this.value = value;
			return super.setValue(value);
		}
	}
	
	protected class MapEntry implements Object2DoubleMap.Entry<T>, Map.Entry<T, Double> {
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
			return keys[index];
		}

		@Override
		public double getDoubleValue() {
			return values[index];
		}

		@Override
		public double setValue(double value) {
			double oldValue = values[index];
			values[index] = value;
			return oldValue;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Map.Entry) {
				if(obj instanceof Object2DoubleMap.Entry) {
					Object2DoubleMap.Entry<T> entry = (Object2DoubleMap.Entry<T>)obj;
					return Objects.equals(getKey(), entry.getKey()) && Double.doubleToLongBits(getDoubleValue()) == Double.doubleToLongBits(entry.getDoubleValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return value instanceof Double && Objects.equals(getKey(), key) && Double.doubleToLongBits(getDoubleValue()) == Double.doubleToLongBits(((Double)value).doubleValue());
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Objects.hashCode(getKey()) ^ Double.hashCode(getDoubleValue());
		}
		
		@Override
		public String toString() {
			return Objects.toString(getKey()) + "=" + Double.toString(getDoubleValue());
		}
	}
}