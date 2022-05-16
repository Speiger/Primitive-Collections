package speiger.src.collections.objects.maps.impl.misc;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.Objects;

import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;
import speiger.src.collections.objects.functions.consumer.ObjectFloatConsumer;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.maps.interfaces.Object2FloatMap;
import speiger.src.collections.objects.maps.interfaces.Object2FloatOrderedMap;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.floats.collections.AbstractFloatCollection;
import speiger.src.collections.floats.collections.FloatCollection;
import speiger.src.collections.floats.collections.FloatIterator;
import speiger.src.collections.floats.functions.FloatConsumer;
import speiger.src.collections.floats.lists.FloatListIterator;

/**
 * A Type Specific LinkedEnumMap implementation that allows for Primitive Values and faster iteration.
 * Unlike javas implementation this one does not jump around between a single long or long array implementation based around the enum size
 * This will cause a bit more memory usage but allows for a simpler implementation.
 * @param <T> the type of elements maintained by this Collection
 */
public class LinkedEnum2FloatMap<T extends Enum<T>> extends Enum2FloatMap<T> implements Object2FloatOrderedMap<T>
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
	public LinkedEnum2FloatMap(Class<T> keyType) {
		super(keyType);
		links = new long[keys.length];
	}
	
	/**
	 * Helper constructor that allow to create a EnumMap from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the EnumMap
	 * @param values the values that should be put into the EnumMap.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public LinkedEnum2FloatMap(T[] keys, Float[] values) {
		if(keys.length <= 0) throw new IllegalArgumentException("Empty Array are not allowed");
		if(keys.length != values.length) throw new IllegalArgumentException("Keys and Values have to be the same size");
		keyType = keys[0].getDeclaringClass();
		this.keys = getKeyUniverse(keyType);
		this.values = new float[keys.length];
		present = new long[((keys.length - 1) >> 6) + 1];
		links = new long[keys.length];
		putAll(keys, values);
	}
	
	/**
	 * Helper constructor that allow to create a EnumMap from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public LinkedEnum2FloatMap(T[] keys, float[] values) {
		if(keys.length <= 0) throw new IllegalArgumentException("Empty Array are not allowed");
		if(keys.length != values.length) throw new IllegalArgumentException("Keys and Values have to be the same size");
		keyType = keys[0].getDeclaringClass();
		this.keys = getKeyUniverse(keyType);
		this.values = new float[keys.length];
		present = new long[((keys.length - 1) >> 6) + 1];
		links = new long[keys.length];
		putAll(keys, values);		
	}
	
	/**
	 * A Helper constructor that allows to create a EnumMap with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 */
	public LinkedEnum2FloatMap(Map<? extends T, ? extends Float> map) {
		if(map instanceof LinkedEnum2FloatMap) {
			LinkedEnum2FloatMap<T> enumMap = (LinkedEnum2FloatMap<T>)map;
			keyType = enumMap.keyType;
			keys = enumMap.keys;
			values = enumMap.values.clone();
			present = enumMap.present.clone();
			links = enumMap.links.clone();
			size = enumMap.size;
		}
		else if(map instanceof Enum2FloatMap) {
			Enum2FloatMap<T> enumMap = (Enum2FloatMap<T>)map;
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
			this.values = new float[keys.length];
			present = new long[((keys.length - 1) >> 6) + 1];
			links = new long[keys.length];
			putAll(map);
		}
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new EnumMap with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 */
	public LinkedEnum2FloatMap(Object2FloatMap<T> map) {
		if(map instanceof LinkedEnum2FloatMap) {
			LinkedEnum2FloatMap<T> enumMap = (LinkedEnum2FloatMap<T>)map;
			keyType = enumMap.keyType;
			keys = enumMap.keys;
			values = enumMap.values.clone();
			present = enumMap.present.clone();
			links = enumMap.links.clone();
			size = enumMap.size;
		}
		else if(map instanceof Enum2FloatMap) {
			Enum2FloatMap<T> enumMap = (Enum2FloatMap<T>)map;
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
			this.values = new float[keys.length];
			present = new long[((keys.length - 1) >> 6) + 1];
			links = new long[keys.length];
			putAll(map);
		}
	}
	
	@Override
	public float putAndMoveToFirst(T key, float value) {
		int index = key.ordinal();
		if(isSet(index)) {
			float result = values[index];
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
	public float putAndMoveToLast(T key, float value) {
		int index = key.ordinal();
		if(isSet(index)) {
			float result = values[index];
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
	public float getAndMoveToFirst(T key) {
		int index = key.ordinal();
		if(index < 0) return getDefaultReturnValue();
		moveToFirstIndex(index);
		return values[index];
	}
	
	@Override
	public float getAndMoveToLast(T key) {
		int index = key.ordinal();
		if(index < 0) return getDefaultReturnValue();
		moveToLastIndex(index);
		return values[index];
	}
	
	@Override
	public LinkedEnum2FloatMap<T> copy() {
		LinkedEnum2FloatMap<T> map = new LinkedEnum2FloatMap<>(keyType);
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
		values[result.ordinal()] = 0F;
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
		values[result.ordinal()] = 0F;
		return result;
	}
	
	@Override
	public float firstFloatValue() {
		if(size == 0) throw new NoSuchElementException();
		return values[firstIndex];
	}
	
	@Override
	public float lastFloatValue() {
		if(size == 0) throw new NoSuchElementException();
		return values[lastIndex];
	}
	
	@Override
	public ObjectSet<Object2FloatMap.Entry<T>> object2FloatEntrySet() {
		if(entrySet == null) entrySet = new MapEntrySet();
		return entrySet;
	}
	
	@Override
	public ObjectSet<T> keySet() {
		if(keySet == null) keySet = new KeySet();
		return keySet;
	}
	
	@Override
	public FloatCollection values() {
		if(valuesC == null) valuesC = new Values();
		return valuesC;
	}
	
	@Override
	public void forEach(ObjectFloatConsumer<T> action) {
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
	
	private class MapEntrySet extends AbstractObjectSet<Object2FloatMap.Entry<T>> implements Object2FloatOrderedMap.FastOrderedSet<T> {
		@Override
		public boolean addAndMoveToFirst(Object2FloatMap.Entry<T> o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Object2FloatMap.Entry<T> o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean moveToFirst(Object2FloatMap.Entry<T> o) {
			return LinkedEnum2FloatMap.this.moveToFirst(o.getKey());
		}
		
		@Override
		public boolean moveToLast(Object2FloatMap.Entry<T> o) {
			return LinkedEnum2FloatMap.this.moveToLast(o.getKey());
		}
		
		@Override
		public Object2FloatMap.Entry<T> first() {
			return new BasicEntry<>(firstKey(), firstFloatValue());
		}
		
		@Override
		public Object2FloatMap.Entry<T> last() {
			return new BasicEntry<>(lastKey(), lastFloatValue());
		}
		
		@Override
		public Object2FloatMap.Entry<T> pollFirst() {
			BasicEntry<T> entry = new BasicEntry<>(firstKey(), firstFloatValue());
			pollFirstKey();
			return entry;
		}
		
		@Override
		public Object2FloatMap.Entry<T> pollLast() {
			BasicEntry<T> entry = new BasicEntry<>(lastKey(), lastFloatValue());
			pollLastKey();
			return entry;
		}
		
		@Override
		public ObjectBidirectionalIterator<Object2FloatMap.Entry<T>> iterator() {
			return new EntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Object2FloatMap.Entry<T>> iterator(Object2FloatMap.Entry<T> fromElement) {
			return new EntryIterator(fromElement.getKey());
		}
		
		@Override
		public ObjectBidirectionalIterator<Object2FloatMap.Entry<T>> fastIterator() {
			return new FastEntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Object2FloatMap.Entry<T>> fastIterator(T fromElement) {
			return new FastEntryIterator(fromElement);
		}
		
		public MapEntrySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(Consumer<? super Object2FloatMap.Entry<T>> action) {
			int index = firstIndex;
			while(index != -1){
				action.accept(new BasicEntry<>(keys[index], values[index]));
				index = (int)links[index];
			}
		}
		
		@Override
		public void fastForEach(Consumer<? super Object2FloatMap.Entry<T>> action) {
			BasicEntry<T> entry = new BasicEntry<>();
			int index = firstIndex;
			while(index != -1){
				entry.set(keys[index], values[index]);
				action.accept(entry);
				index = (int)links[index];
			}
		}
		
		@Override
		@Deprecated
		public boolean contains(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Object2FloatMap.Entry) {
					Object2FloatMap.Entry<T> entry = (Object2FloatMap.Entry<T>)o;
					if(!keyType.isInstance(entry.getKey())) return false;
					int index = ((T)entry.getKey()).ordinal();
					if(index >= 0 && LinkedEnum2FloatMap.this.isSet(index)) return Float.floatToIntBits(entry.getFloatValue()) == Float.floatToIntBits(LinkedEnum2FloatMap.this.values[index]);
				}
				else {
					Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
					if(!keyType.isInstance(entry.getKey())) return false;
					int index = ((T)entry.getKey()).ordinal();
					if(index >= 0 && LinkedEnum2FloatMap.this.isSet(index)) return Objects.equals(entry.getValue(), Float.valueOf(LinkedEnum2FloatMap.this.values[index]));
				}
			}
			return false;
		}
		
		@Override
		@Deprecated
		public boolean remove(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Object2FloatMap.Entry) {
					Object2FloatMap.Entry<T> entry = (Object2FloatMap.Entry<T>)o;
					return LinkedEnum2FloatMap.this.remove(entry.getKey(), entry.getFloatValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
				return LinkedEnum2FloatMap.this.remove(entry.getKey(), entry.getValue());
			}
			return false;
		}
		
		@Override
		public int size() {
			return LinkedEnum2FloatMap.this.size();
		}
		
		@Override
		public void clear() {
			LinkedEnum2FloatMap.this.clear();
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
			LinkedEnum2FloatMap.this.remove(o);
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
			return LinkedEnum2FloatMap.this.moveToFirst(o);
		}

		@Override
		public boolean moveToLast(T o) {
			return LinkedEnum2FloatMap.this.moveToLast(o);
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
			return LinkedEnum2FloatMap.this.size();
		}
		
		@Override
		public void clear() {
			LinkedEnum2FloatMap.this.clear();
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
	
	private class Values extends AbstractFloatCollection {
		@Override
		public boolean contains(float e) {
			return containsValue(e);
		}
		
		@Override
		public boolean add(float o) {
			throw new UnsupportedOperationException();
		}

		@Override
		public FloatIterator iterator() {
			return new ValueIterator();
		}
		
		@Override
		public int size() {
			return LinkedEnum2FloatMap.this.size();
		}
		
		@Override
		public void clear() {
			LinkedEnum2FloatMap.this.clear();
		}
		
		@Override
		public void forEach(FloatConsumer action) {
			int index = firstIndex;
			while(index != -1){
				action.accept(values[index]);
				index = (int)links[index];
			}
		}
	}
	
	private class FastEntryIterator extends MapIterator implements ObjectListIterator<Object2FloatMap.Entry<T>> {
		MapEntry entry = new MapEntry();
		
		public FastEntryIterator() {}
		public FastEntryIterator(T from) {
			super(from);
		}
		
		@Override
		public Object2FloatMap.Entry<T> next() {
			entry.index = nextEntry();
			return entry;
		}
		
		@Override
		public Object2FloatMap.Entry<T> previous() {
			entry.index = previousEntry();
			return entry;
		}
		
		@Override
		public void set(Object2FloatMap.Entry<T> entry) { throw new UnsupportedOperationException(); }

		@Override
		public void add(Object2FloatMap.Entry<T> entry) { throw new UnsupportedOperationException(); }
	}
	
	private class EntryIterator extends MapIterator implements ObjectListIterator<Object2FloatMap.Entry<T>> {
		MapEntry entry;
		
		public EntryIterator() {}
		public EntryIterator(T from) {
			super(from);
		}
		
		@Override
		public Object2FloatMap.Entry<T> next() {
			return entry = new MapEntry(nextEntry());
		}
		
		@Override
		public Object2FloatMap.Entry<T> previous() {
			return entry = new MapEntry(previousEntry());
		}
		
		@Override
		public void remove() {
			super.remove();
			entry.index = -1;
		}

		@Override
		public void set(Object2FloatMap.Entry<T> entry) { throw new UnsupportedOperationException(); }

		@Override
		public void add(Object2FloatMap.Entry<T> entry) { throw new UnsupportedOperationException(); }
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
	
	private class ValueIterator extends MapIterator implements FloatListIterator {
		public ValueIterator() {}
		
		@Override
		public float previousFloat() {
			return values[previousEntry()];
		}

		@Override
		public float nextFloat() {
			return values[nextEntry()];
		}

		@Override
		public void set(float e) { throw new UnsupportedOperationException(); }

		@Override
		public void add(float e) { throw new UnsupportedOperationException(); }
		
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
			values[current] = 0F;
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
	
	protected class MapEntry implements Object2FloatMap.Entry<T>, Map.Entry<T, Float> {
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
		public float getFloatValue() {
			return values[index];
		}

		@Override
		public float setValue(float value) {
			float oldValue = values[index];
			values[index] = value;
			return oldValue;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Map.Entry) {
				if(obj instanceof Object2FloatMap.Entry) {
					Object2FloatMap.Entry<T> entry = (Object2FloatMap.Entry<T>)obj;
					return Objects.equals(keys[index], entry.getKey()) && Float.floatToIntBits(values[index]) == Float.floatToIntBits(entry.getFloatValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return value instanceof Float && Objects.equals(keys[index], key) && Float.floatToIntBits(values[index]) == Float.floatToIntBits(((Float)value).floatValue());
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Objects.hashCode(keys[index]) ^ Float.hashCode(values[index]);
		}
		
		@Override
		public String toString() {
			return Objects.toString(keys[index]) + "=" + Float.toString(values[index]);
		}
	}
}