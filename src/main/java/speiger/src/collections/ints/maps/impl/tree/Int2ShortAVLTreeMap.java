package speiger.src.collections.ints.maps.impl.tree;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.IntPredicate;


import speiger.src.collections.ints.collections.IntBidirectionalIterator;
import speiger.src.collections.ints.functions.IntComparator;
import speiger.src.collections.ints.functions.IntConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectIntConsumer;
import speiger.src.collections.ints.functions.consumer.IntIntConsumer;
import speiger.src.collections.ints.functions.consumer.IntObjectConsumer;
import speiger.src.collections.ints.functions.function.Int2ShortFunction;
import speiger.src.collections.ints.functions.consumer.IntShortConsumer;
import speiger.src.collections.ints.functions.function.IntShortUnaryOperator;
import speiger.src.collections.ints.functions.function.IntIntUnaryOperator;
import speiger.src.collections.ints.maps.abstracts.AbstractInt2ShortMap;
import speiger.src.collections.ints.maps.interfaces.Int2ShortMap;
import speiger.src.collections.ints.maps.interfaces.Int2ShortNavigableMap;
import speiger.src.collections.ints.sets.AbstractIntSet;
import speiger.src.collections.ints.sets.IntNavigableSet;
import speiger.src.collections.shorts.collections.AbstractShortCollection;
import speiger.src.collections.shorts.collections.ShortCollection;
import speiger.src.collections.shorts.collections.ShortIterator;
import speiger.src.collections.shorts.functions.ShortSupplier;
import speiger.src.collections.shorts.collections.ShortBidirectionalIterator;
import speiger.src.collections.shorts.functions.function.ShortShortUnaryOperator;
import speiger.src.collections.shorts.functions.ShortConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectShortConsumer;

import speiger.src.collections.shorts.functions.function.ShortPredicate;
import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectSet;

/**
 * A Simple Type Specific AVL TreeMap implementation that reduces boxing/unboxing.
 * It is using a bit more memory then <a href="https://github.com/vigna/fastutil">FastUtil</a>,
 * but it saves a lot of Performance on the Optimized removal and iteration logic.
 * Which makes the implementation actually useable and does not get outperformed by Javas default implementation.
 */
public class Int2ShortAVLTreeMap extends AbstractInt2ShortMap implements Int2ShortNavigableMap
{
	/** The center of the Tree */
	protected transient Node tree;
	/** The Lowest possible Node */
	protected transient Node first;
	/** The Highest possible Node */
	protected transient Node last;
	/** The amount of elements stored in the Map */
	protected int size = 0;
	/** The Sorter of the Tree */
	protected transient IntComparator comparator;
	
	/** the default return value for max searches */
	protected int defaultMaxNotFound = Integer.MIN_VALUE;
	/** the default return value for min searches */
	protected int defaultMinNotFound = Integer.MAX_VALUE;
	
	/** KeySet Cache */
	protected IntNavigableSet keySet;
	/** Values Cache */
	protected ShortCollection values;
	/** EntrySet Cache */
	protected ObjectSet<Int2ShortMap.Entry> entrySet;
	
	/**
	 * Default Constructor
	 */
	public Int2ShortAVLTreeMap() {
	}
	
	/**
	 * Constructor that allows to define the sorter
	 * @param comp the function that decides how the tree is sorted, can be null
	 */
	public Int2ShortAVLTreeMap(IntComparator comp) {
		comparator = comp;
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Int2ShortAVLTreeMap(Integer[] keys, Short[] values) {
		this(keys, values, null);
	}
	
	/**
	 * Helper constructor that has a custom sorter and allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param comp the function that decides how the tree is sorted, can be null
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Int2ShortAVLTreeMap(Integer[] keys, Short[] values, IntComparator comp) {
		comparator = comp;
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i].intValue(), values[i].shortValue());
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Int2ShortAVLTreeMap(int[] keys, short[] values) {
		this(keys, values, null);
	}
	
	/**
	 * Helper constructor that has a custom sorter and allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param comp the function that decides how the tree is sorted, can be null
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Int2ShortAVLTreeMap(int[] keys, short[] values, IntComparator comp) {
		comparator = comp;
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i], values[i]);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 */
	public Int2ShortAVLTreeMap(Map<? extends Integer, ? extends Short> map) {
		this(map, null);
	}
	
	/**
	 * A Helper constructor that has a custom sorter and allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param comp the function that decides how the tree is sorted, can be null
	 */
	public Int2ShortAVLTreeMap(Map<? extends Integer, ? extends Short> map, IntComparator comp) {
		comparator = comp;
		putAll(map);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 */
	public Int2ShortAVLTreeMap(Int2ShortMap map) {
		this(map, null);
	}
	
	/**
	 * A Type Specific Helper function that has a custom sorter and allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param comp the function that decides how the tree is sorted, can be null
 	 */
	public Int2ShortAVLTreeMap(Int2ShortMap map, IntComparator comp) {
		comparator = comp;
		putAll(map);
	}

	@Override
	public void setDefaultMaxValue(int value) { defaultMaxNotFound = value; }
	@Override
	public int getDefaultMaxValue() { return defaultMaxNotFound; }
	@Override
	public void setDefaultMinValue(int value) { defaultMinNotFound = value; }
	@Override
	public int getDefaultMinValue() { return defaultMinNotFound; }
	
	
	@Override
	public short put(int key, short value) {
		if(tree == null) {
			tree = first = last = new Node(key, value, null);
			size++;
			return getDefaultReturnValue();
		}
		int compare = 0;
		Node parent = tree;
		while(true) {
			if((compare = compare(key, parent.key)) == 0) return parent.setValue(value);
			if(compare < 0) {
				if(parent.left == null) break;
				parent = parent.left;
			}
			else if(compare > 0) {
				if(parent.right == null) break;
				parent = parent.right;
			}
		}
		Node adding = new Node(key, value, parent);
		if(compare < 0)  {
			parent.left = adding;
			if(parent == first)	first = adding;
		}
		else {
			parent.right = adding;
			if(parent == last) last = adding;
		}
		fixAfterInsertion(adding);
		size++;
		return getDefaultReturnValue();
	}
	
	@Override
	public short putIfAbsent(int key, short value) {
		if(tree == null) {
			tree = first = last = new Node(key, value, null);
			size++;
			return getDefaultReturnValue();
		}
		int compare = 0;
		Node parent = tree;
		while(true) {
			if((compare = compare(key, parent.key)) == 0) {
				if(parent.value == getDefaultReturnValue()) return parent.setValue(value);
				return parent.value;
			}
			if(compare < 0) {
				if(parent.left == null) break;
				parent = parent.left;
			}
			else if(compare > 0) {
				if(parent.right == null) break;
				parent = parent.right;
			}
		}
		Node adding = new Node(key, value, parent);
		if(compare < 0)  {
			parent.left = adding;
			if(parent == first)	first = adding;
		}
		else  {
			parent.right = adding;
			if(parent == last) last = adding;
		}
		fixAfterInsertion(adding);
		size++;
		return getDefaultReturnValue();
	}
	
	@Override
	public short addTo(int key, short value) {
		if(tree == null) {
			tree = first = last = new Node(key, value, null);
			size++;
			return getDefaultReturnValue();
		}
		int compare = 0;
		Node parent = tree;
		while(true) {
			if((compare = compare(key, parent.key)) == 0) return parent.addTo(value);
			if(compare < 0) {
				if(parent.left == null) break;
				parent = parent.left;
			}
			else if(compare > 0) {
				if(parent.right == null) break;
				parent = parent.right;
			}
		}
		Node adding = new Node(key, value, parent);
		if(compare < 0)  {
			parent.left = adding;
			if(parent == first)	first = adding;
		}
		else  {
			parent.right = adding;
			if(parent == last) last = adding;
		}
		fixAfterInsertion(adding);
		size++;
		return getDefaultReturnValue();
	}
	
	@Override
	public short subFrom(int key, short value) {
		if(tree == null) return getDefaultReturnValue();
		int compare = 0;
		Node parent = tree;
		while(true) {
			if((compare = compare(key, parent.key)) == 0)
			{
				short oldValue = parent.subFrom(value);
				if(value < 0 ? (parent.value >= getDefaultReturnValue()) : (parent.value <= getDefaultReturnValue())) removeNode(parent);
				return oldValue;
			}
			if(compare < 0) {
				if(parent.left == null) break;
				parent = parent.left;
			}
			else if(compare > 0) {
				if(parent.right == null) break;
				parent = parent.right;
			}
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public IntComparator comparator() { return comparator; }

	@Override
	public boolean containsKey(int key) {
		return findNode(key) != null;
	}
	
	@Override
	public short get(int key) {
		Node node = findNode(key);
		return node == null ? getDefaultReturnValue() : node.value;
	}
	
	@Override
	public short getOrDefault(int key, short defaultValue) {
		Node node = findNode(key);
		return node == null ? defaultValue : node.value;
	}
	
	@Override
	public int firstIntKey() {
		if(tree == null) throw new NoSuchElementException();
		return first.key;
	}
	
	@Override
	public int pollFirstIntKey() {
		if(tree == null) return getDefaultMinValue();
		int result = first.key;
		removeNode(first);
		return result;
	}
	
	@Override
	public int lastIntKey() {
		if(tree == null) throw new NoSuchElementException();
		return last.key;
	}
	
	@Override
	public int pollLastIntKey() {
		if(tree == null) return getDefaultMaxValue();
		int result = last.key;
		removeNode(last);
		return result;
	}
	
	@Override
	public Int2ShortMap.Entry firstEntry() {
		if(tree == null) return null;
		return first.export();
	}
	
	@Override
	public Int2ShortMap.Entry lastEntry() {
		if(tree == null) return null;
		return last.export();
	}
	
	@Override
	public Int2ShortMap.Entry pollFirstEntry() {
		if(tree == null) return null;
		BasicEntry entry = first.export();
		removeNode(first);
		return entry;
	}
	
	@Override
	public Int2ShortMap.Entry pollLastEntry() {
		if(tree == null) return null;
		BasicEntry entry = last.export();
		removeNode(last);
		return entry;
	}
	
	@Override
	public short firstShortValue() {
		if(tree == null) throw new NoSuchElementException();
		return first.value;
	}
	
	@Override
	public short lastShortValue() {
		if(tree == null) throw new NoSuchElementException();
		return last.value;
	}
	
	@Override
	public short remove(int key) {
		Node entry = findNode(key);
		if(entry == null) return getDefaultReturnValue();
		short value = entry.value;
		removeNode(entry);
		return value;
	}
	
	@Override
	public short removeOrDefault(int key, short defaultValue) {
		Node entry = findNode(key);
		if(entry == null) return defaultValue;
		short value = entry.value;
		removeNode(entry);
		return value;
	}
	
	@Override
	public boolean remove(int key, short value) {
		Node entry = findNode(key);
		if(entry == null || entry.value != value) return false;
		removeNode(entry);
		return true;
	}
	
	@Override
	public boolean replace(int key, short oldValue, short newValue) {
		Node entry = findNode(key);
		if(entry == null || entry.value != oldValue) return false;
		entry.value = newValue;
		return true;
	}
	
	@Override
	public short replace(int key, short value) {
		Node entry = findNode(key);
		if(entry == null) return getDefaultReturnValue();
		short oldValue = entry.value;
		entry.value = value;
		return oldValue;
	}
	
	@Override
	public short computeShort(int key, IntShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		Node entry = findNode(key);
		if(entry == null) {
			short newValue = mappingFunction.applyAsShort(key, getDefaultReturnValue());
			put(key, newValue);
			return newValue;
		}
		short newValue = mappingFunction.applyAsShort(key, entry.value);
		entry.value = newValue;
		return newValue;
	}
	
	@Override
	public short computeShortNonDefault(int key, IntShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		Node entry = findNode(key);
		if(entry == null) {
			short newValue = mappingFunction.applyAsShort(key, getDefaultReturnValue());
			if(newValue == getDefaultReturnValue()) return newValue;
			put(key, newValue);
			return newValue;
		}
		short newValue = mappingFunction.applyAsShort(key, entry.value);
		if(newValue == getDefaultReturnValue()) {
			removeNode(entry);
			return newValue;
		}
		entry.value = newValue;
		return newValue;
	}
	
	@Override
	public short computeShortIfAbsent(int key, Int2ShortFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		Node entry = findNode(key);
		if(entry == null) {
			short newValue = mappingFunction.applyAsShort(key);
			put(key, newValue);
			return newValue;
		}
		return entry.value;
	}
	
	@Override
	public short computeShortIfAbsentNonDefault(int key, Int2ShortFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		Node entry = findNode(key);
		if(entry == null) {
			short newValue = mappingFunction.applyAsShort(key);
			if(newValue == getDefaultReturnValue()) return newValue;
			put(key, newValue);
			return newValue;
		}
		if(Objects.equals(entry.value, getDefaultReturnValue())) {
			short newValue = mappingFunction.applyAsShort(key);
			if(newValue == getDefaultReturnValue()) return newValue;
			entry.value = newValue;
		}
		return entry.value;
	}
	
	@Override
	public short supplyShortIfAbsent(int key, ShortSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		Node entry = findNode(key);
		if(entry == null) {
			short newValue = valueProvider.getAsInt();
			put(key, newValue);
			return newValue;
		}
		return entry.value;
	}
	
	@Override
	public short supplyShortIfAbsentNonDefault(int key, ShortSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		Node entry = findNode(key);
		if(entry == null) {
			short newValue = valueProvider.getAsInt();
			if(newValue == getDefaultReturnValue()) return newValue;
			put(key, newValue);
			return newValue;
		}
		if(entry.value == getDefaultReturnValue()) {
			short newValue = valueProvider.getAsInt();
			if(newValue == getDefaultReturnValue()) return newValue;
			entry.value = newValue;
		}
		return entry.value;
	}
	
	@Override
	public short computeShortIfPresent(int key, IntShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		Node entry = findNode(key);
		if(entry == null) return getDefaultReturnValue();
		short newValue = mappingFunction.applyAsShort(key, entry.value);
		entry.value = newValue;
		return newValue;
	}
	
	@Override
	public short computeShortIfPresentNonDefault(int key, IntShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		Node entry = findNode(key);
		if(entry == null || entry.value == getDefaultReturnValue()) return getDefaultReturnValue();
		short newValue = mappingFunction.applyAsShort(key, entry.value);
		if(newValue == getDefaultReturnValue()) {
			removeNode(entry);
			return newValue;
		}
		entry.value = newValue;
		return newValue;
	}
	
	@Override
	public short mergeShort(int key, short value, ShortShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		Node entry = findNode(key);
		short newValue = entry == null || entry.value == getDefaultReturnValue() ? value : mappingFunction.applyAsShort(entry.value, value);
		if(newValue == getDefaultReturnValue()) {
			if(entry != null)
				removeNode(entry);
		}
		else if(entry == null) put(key, newValue);
		else entry.value = newValue;
		return newValue;
	}
	
	@Override
	public void mergeAllShort(Int2ShortMap m, ShortShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Int2ShortMap.Entry entry : getFastIterable(m)) {
			int key = entry.getIntKey();
			Node subEntry = findNode(key);
			short newValue = subEntry == null || subEntry.value == getDefaultReturnValue() ? entry.getShortValue() : mappingFunction.applyAsShort(subEntry.value, entry.getShortValue());
			if(newValue == getDefaultReturnValue()) {
				if(subEntry != null)
					removeNode(subEntry);
			}
			else if(subEntry == null) put(key, newValue);
			else subEntry.value = newValue;
		}
	}
	
	@Override
	public void forEach(IntShortConsumer action) {
		for(Node entry = first;entry != null;entry = entry.next())
			action.accept(entry.key, entry.value);
	}
	
	@Override
	public int size() { return size; }
	
	@Override
	public void clear() {
		size = 0;
		first = null;
		last = null;
		tree = null;
	}
	
	protected IntBidirectionalIterator keyIterator() {
		return new AscendingKeyIterator(first);
	}
	
	protected IntBidirectionalIterator keyIterator(int element) {
		return new AscendingKeyIterator(findNode(element));
	}
	
	protected IntBidirectionalIterator descendingKeyIterator() {
		return new DescendingKeyIterator(last);
	}
		
	@Override
	public Int2ShortAVLTreeMap copy() {
		Int2ShortAVLTreeMap set = new Int2ShortAVLTreeMap();
		set.size = size;
		if(tree != null) {
			set.tree = tree.copy();
			Node lastFound = null;
			for(Node entry = tree;entry != null;entry = entry.left) lastFound = entry;
			set.first = lastFound;
			lastFound = null;
			for(Node entry = tree;entry != null;entry = entry.right) lastFound = entry;
			set.last = lastFound;
		}
		return set;
	}
	
	@Override
	public IntNavigableSet keySet() {
		return navigableKeySet();
	}
	
	@Override
	public ObjectSet<Int2ShortMap.Entry> int2ShortEntrySet() {
		if(entrySet == null) entrySet = new EntrySet();
		return entrySet;
	}
	
	@Override
	public ShortCollection values() {
		if(values == null) values = new Values();
		return values;
	}
	
	@Override
	public IntNavigableSet navigableKeySet() {
		if(keySet == null) keySet = new KeySet(this);
		return keySet;
	}
	
	@Override
	public Int2ShortNavigableMap descendingMap() {
		return new DescendingNaivgableSubMap(this, true, 0, true, true, 0, true);
	}
	
	@Override
	public IntNavigableSet descendingKeySet() {
		return descendingMap().navigableKeySet();
	}
	
	@Override
	public Int2ShortNavigableMap subMap(int fromKey, boolean fromInclusive, int toKey, boolean toInclusive) {
		return new AscendingNaivgableSubMap(this, false, fromKey, fromInclusive, false, toKey, toInclusive);
	}
	
	@Override
	public Int2ShortNavigableMap headMap(int toKey, boolean inclusive) {
		return new AscendingNaivgableSubMap(this, true, 0, true, false, toKey, inclusive);
	}
	
	@Override
	public Int2ShortNavigableMap tailMap(int fromKey, boolean inclusive) {
		return new AscendingNaivgableSubMap(this, false, fromKey, inclusive, true, 0, true);
	}
	
	@Override
	public int lowerKey(int e) {
		Node node = findLowerNode(e);
		return node != null ? node.key : getDefaultMinValue();
	}

	@Override
	public int floorKey(int e) {
		Node node = findFloorNode(e);
		return node != null ? node.key : getDefaultMinValue();
	}
	
	@Override
	public int higherKey(int e) {
		Node node = findHigherNode(e);
		return node != null ? node.key : getDefaultMaxValue();
	}

	@Override
	public int ceilingKey(int e) {
		Node node = findCeilingNode(e);
		return node != null ? node.key : getDefaultMaxValue();
	}
	
	@Override
	public Int2ShortMap.Entry lowerEntry(int key) {
		Node node = findLowerNode(key);
		return node != null ? node.export() : null;
	}
	
	@Override
	public Int2ShortMap.Entry higherEntry(int key) {
		Node node = findHigherNode(key);
		return node != null ? node.export() : null;
	}
	
	@Override
	public Int2ShortMap.Entry floorEntry(int key) {
		Node node = findFloorNode(key);
		return node != null ? node.export() : null;
	}
	
	@Override
	public Int2ShortMap.Entry ceilingEntry(int key) {
		Node node = findCeilingNode(key);
		return node != null ? node.export() : null;
	}
	
	protected Node findLowerNode(int key) {
		Node entry = tree;
		while(entry != null) {
			if(compare(key, entry.key) > 0) {
				if(entry.right != null) entry = entry.right;
				else return entry;
			}
			else {
				if(entry.left != null) entry = entry.left;
				else {
					Node parent = entry.parent;
					while(parent != null && parent.left == entry) {
						entry = parent;
						parent = parent.parent;
					}
					return parent;
				}
			}
		}
		return null;
	}
	
	protected Node findFloorNode(int key) {
		Node entry = tree;
		int compare;
		while(entry != null) {
			if((compare = compare(key, entry.key)) > 0) {
				if(entry.right == null) break;
				entry = entry.right;
				continue;
			}
			else if(compare < 0) {
				if(entry.left != null) entry = entry.left;
				else {
					Node parent = entry.parent;
					while(parent != null && parent.left == entry) {
						entry = parent;
						parent = parent.parent;
					}
					return parent;
				}
				continue;
			}
			break;
		}
		return entry;
	}
	
	protected Node findCeilingNode(int key) {
		Node entry = tree;
		int compare;
		while(entry != null) {
			if((compare = compare(key, entry.key)) < 0) {
				if(entry.left == null) break;
				entry = entry.left;
				continue;
			}
			else if(compare > 0) {
				if(entry.right != null) entry = entry.right;
				else {
					Node parent = entry.parent;
					while(parent != null && parent.right == entry) {
						entry = parent;
						parent = parent.parent;
					}
					return parent;
				}
				continue;
			}
			break;
		}
		return entry;
	}
	
	protected Node findHigherNode(int key) {
		Node entry = tree;
		while(entry != null) {
			if(compare(key, entry.key) < 0) {
				if(entry.left != null) entry = entry.left;
				else return entry;
			}
			else {
				if(entry.right != null) entry = entry.right;
				else {
					Node parent = entry.parent;
					while(parent != null && parent.right == entry) {
						entry = parent;
						parent = parent.parent;
					}
					return parent;
				}
			}
		}
		return null;
	}
	
	protected Node findNode(int key) {
		Node node = tree;
		int compare;
		while(node != null) {
			if((compare = compare(key, node.key)) == 0) return node;
			if(compare < 0) node = node.left;
			else node = node.right;
		}
		return null;
	}
	
	protected void removeNode(Node entry) {
		size--;
		if(entry.needsSuccessor()) {
			Node successor = entry.next();
			entry.key = successor.key;
			entry.value = successor.value;
			entry = successor;
		}
		if(entry.previous() == null) first = entry.next();
		if(entry.next() == null) last = entry.previous();
		Node replacement = entry.left != null ? entry.left : entry.right;
		if(replacement != null) {
			if(entry.replace(replacement)) tree = replacement;
			entry.left = entry.right = entry.parent = null;
			fixAfterDeletion(replacement);
		}
		else if(entry.parent == null) tree = first = last = null;
		else {
			fixAfterDeletion(entry);
			entry.replace(null);
			entry.parent = null;
		}
	}
	
	protected void validate(int k) { compare(k, k); }
	protected int compare(int k, int v) { return comparator != null ? comparator.compare(k, v) : Integer.compare(k, v);}
	
	/** From CLR */
	protected void rotateLeft(Node entry) {
		if(entry != null) {
			Node right = entry.right;
			entry.right = right.left;
			if(right.left != null) right.left.parent = entry;
			right.parent = entry.parent;
			if(entry.parent == null) tree = right;
			else if(entry.parent.left == entry) entry.parent.left = right;
			else entry.parent.right = right;
			right.left = entry;
			entry.parent = right;
			entry.updateHeight();
			right.updateHeight();
		}
	}
	
	/** From CLR */
	protected void rotateRight(Node entry) {
		if(entry != null) {
			Node left = entry.left;
			entry.left = left.right;
			if(left.right != null) left.right.parent = entry;
			left.parent = entry.parent;
			if(entry.parent == null) tree = left;
			else if(entry.parent.right == entry) entry.parent.right = left;
			else entry.parent.left = left;
			left.right = entry;
			entry.parent = left;
			entry.updateHeight();
			left.updateHeight();
		}
	}
	
	/** From CLR */
	protected void fixAfterInsertion(Node entry) {
		while(entry != null) {
			entry.updateHeight();
			int balance = entry.getBalance();
			if(balance > 1) {
				int compare = entry.left.getBalance();
				if(compare > 0) rotateRight(entry);
				else if(compare < 0) {
					rotateLeft(entry.left);
					rotateRight(entry);
				}
			}
			else if(balance < -1) {
				int compare = entry.right.getBalance();
				if(compare < 0) rotateLeft(entry);
				else if(compare > 0) {
					rotateRight(entry.right);
					rotateLeft(entry);
				}
			}
			entry = entry.parent;
		}
	}
	
	/** From CLR */
	protected void fixAfterDeletion(Node entry) {
		if(entry != null) {
			entry.updateHeight();
			int balance = entry.getBalance();
			if(balance > 1) {
				int subBalance = entry.left.getBalance();
				if(subBalance >= 0) rotateRight(entry);
				else {
					rotateLeft(entry.left);
					rotateRight(entry);
				}
			}
			else if(balance < -1)
			{
				int subBalance = entry.right.getBalance();
				if(subBalance <= 0) rotateLeft(entry);
				else {
					rotateRight(entry.right);
					rotateLeft(entry);
				}
			}
			entry = entry.parent;
		}
	}
	
	static class KeySet extends AbstractIntSet implements IntNavigableSet
	{
		Int2ShortNavigableMap map;

		public KeySet(Int2ShortNavigableMap map) {
			this.map = map;
		}
		
		@Override
		public void setDefaultMaxValue(int e) { map.setDefaultMaxValue(e); }
		@Override
		public int getDefaultMaxValue() { return map.getDefaultMaxValue(); }
		@Override
		public void setDefaultMinValue(int e) { map.setDefaultMinValue(e); }
		@Override
		public int getDefaultMinValue() { return map.getDefaultMinValue(); }
		@Override
		public int lower(int e) { return map.lowerKey(e); }
		@Override
		public int floor(int e) { return map.floorKey(e); }
		@Override
		public int ceiling(int e) { return map.ceilingKey(e); }
		@Override
		public int higher(int e) { return map.higherKey(e); }
		
		@Override
		public Integer lower(Integer e) {
			Int2ShortMap.Entry node = map.lowerEntry(e.intValue());
			return node != null ? node.getKey() : null;
		}
		
		@Override
		public Integer floor(Integer e) {
			Int2ShortMap.Entry node = map.floorEntry(e.intValue());
			return node != null ? node.getKey() : null;
		}
		
		@Override
		public Integer higher(Integer e) {
			Int2ShortMap.Entry node = map.higherEntry(e.intValue());
			return node != null ? node.getKey() : null;
		}
		
		@Override
		public Integer ceiling(Integer e) {
			Int2ShortMap.Entry node = map.ceilingEntry(e.intValue());
			return node != null ? node.getKey() : null;
		}
		
		@Override
		public int pollFirstInt() { return map.pollFirstIntKey(); }
		@Override
		public int pollLastInt() { return map.pollLastIntKey(); }
		@Override
		public IntComparator comparator() { return map.comparator(); }
		@Override
		public int firstInt() { return map.firstIntKey(); } 
		@Override
		public int lastInt() { return map.lastIntKey(); }
		@Override
		public void clear() { map.clear(); }
		
		@Override
		public boolean remove(int o) { 
			int oldSize = map.size();
			map.remove(o); 
			return oldSize != map.size();
		}
		
		@Override
		public boolean add(int e) { throw new UnsupportedOperationException(); }
		
		@Override
		public IntBidirectionalIterator iterator(int fromElement) {
			if(map instanceof Int2ShortAVLTreeMap) return ((Int2ShortAVLTreeMap)map).keyIterator(fromElement);
			return ((NavigableSubMap)map).keyIterator(fromElement);
		}
		
		@Override
		public IntNavigableSet subSet(int fromElement, boolean fromInclusive, int toElement, boolean toInclusive) { return new KeySet(map.subMap(fromElement, fromInclusive, toElement, toInclusive)); }
		@Override
		public IntNavigableSet headSet(int toElement, boolean inclusive) { return new KeySet(map.headMap(toElement, inclusive)); }
		@Override
		public IntNavigableSet tailSet(int fromElement, boolean inclusive) { return new KeySet(map.tailMap(fromElement, inclusive)); }
		
		@Override
		public IntBidirectionalIterator iterator() {
			if(map instanceof Int2ShortAVLTreeMap) return ((Int2ShortAVLTreeMap)map).keyIterator();
			return ((NavigableSubMap)map).keyIterator();
		}
		
		@Override
		public IntBidirectionalIterator descendingIterator() {
			if(map instanceof Int2ShortAVLTreeMap) return ((Int2ShortAVLTreeMap)map).descendingKeyIterator();
			return ((NavigableSubMap)map).descendingKeyIterator();
		}
		
		protected Node start() {
			if(map instanceof Int2ShortAVLTreeMap) return ((Int2ShortAVLTreeMap)map).first;
			return ((NavigableSubMap)map).subLowest();
		}
		
		protected Node end() {
			if(map instanceof Int2ShortAVLTreeMap) return null;
			return ((NavigableSubMap)map).subHighest();
		}
		
		protected Node next(Node entry) {
			if(map instanceof Int2ShortAVLTreeMap) return entry.next();
			return ((NavigableSubMap)map).next(entry);
		}
		
		protected Node previous(Node entry) {
			if(map instanceof Int2ShortAVLTreeMap) return entry.previous();
			return ((NavigableSubMap)map).previous(entry);
		}
		
		@Override
		public IntNavigableSet descendingSet() { return new KeySet(map.descendingMap()); }
		@Override
		public KeySet copy() { throw new UnsupportedOperationException(); }
		@Override
		public boolean isEmpty() { return map.isEmpty(); }
		@Override
		public int size() { return map.size(); }
		
		@Override
		public void forEach(IntConsumer action) {
			Objects.requireNonNull(action);
			for(Node entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				action.accept(entry.key);
		}
		
		@Override
		public void forEachIndexed(IntIntConsumer action) {
			Objects.requireNonNull(action);
			int index = 0;
			for(Node entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				action.accept(index++, entry.key);
		}
		
		@Override
		public <E> void forEach(E input, ObjectIntConsumer<E> action) {
			Objects.requireNonNull(action);
			for(Node entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				action.accept(input, entry.key);
		}
		
		@Override
		public boolean matchesAny(IntPredicate filter) {
			Objects.requireNonNull(filter);
			for(Node entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				if(filter.test(entry.key)) return true;
			return false;
		}
		
		@Override
		public boolean matchesNone(IntPredicate filter) {
			Objects.requireNonNull(filter);
			for(Node entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				if(filter.test(entry.key)) return false;
			return true;
		}
		
		@Override
		public boolean matchesAll(IntPredicate filter) {
			Objects.requireNonNull(filter);
			for(Node entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				if(!filter.test(entry.key)) return false;
			return true;
		}
		
		@Override
		public int reduce(int identity, IntIntUnaryOperator operator) {
			Objects.requireNonNull(operator);
			int state = identity;
			for(Node entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				state = operator.applyAsInt(state, entry.key);
			return state;
		}
		
		@Override
		public int reduce(IntIntUnaryOperator operator) {
			Objects.requireNonNull(operator);
			int state = 0;
			boolean empty = true;
			for(Node entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry)) {
				if(empty) {
					empty = false;
					state = entry.key;
					continue;
				}
				state = operator.applyAsInt(state, entry.key);
			}
			return state;
		}
		
		@Override
		public int findFirst(IntPredicate filter) {
			Objects.requireNonNull(filter);
			for(Node entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				if(filter.test(entry.key)) return entry.key;
			return 0;
		}
		
		@Override
		public int count(IntPredicate filter) {
			Objects.requireNonNull(filter);
			int result = 0;
			for(Node entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				if(filter.test(entry.key)) result++;
			return result;
		}
	}
	
	static class AscendingNaivgableSubMap extends NavigableSubMap
	{
		AscendingNaivgableSubMap(Int2ShortAVLTreeMap map, boolean fromStart, int lo, boolean loInclusive, boolean toEnd, int hi, boolean hiInclusive) {
			super(map, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
		}
		
		@Override
		public Int2ShortNavigableMap descendingMap() {
			if(inverse == null) inverse = new DescendingNaivgableSubMap(map, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
			return inverse;
		}
		
		@Override
		public ObjectSet<Int2ShortMap.Entry> int2ShortEntrySet() {
			if(entrySet == null) entrySet = new AscendingSubEntrySet();
			return entrySet;
		}
		
		@Override
		public IntNavigableSet navigableKeySet() {
			if(keySet == null) keySet = new KeySet(this);
			return keySet;
		}
		
		@Override
		public Int2ShortNavigableMap subMap(int fromKey, boolean fromInclusive, int toKey, boolean toInclusive) {
			if (!inRange(fromKey, fromInclusive)) throw new IllegalArgumentException("fromKey out of range");
			if (!inRange(toKey, toInclusive)) throw new IllegalArgumentException("toKey out of range");
			return new AscendingNaivgableSubMap(map, false, fromKey, fromInclusive, false, toKey, toInclusive);
		}
		
		@Override
		public Int2ShortNavigableMap headMap(int toKey, boolean inclusive) {
			if (!inRange(toKey, inclusive)) throw new IllegalArgumentException("toKey out of range");
			return new AscendingNaivgableSubMap(map, fromStart, lo, loInclusive, false, toKey, inclusive);
		}
		
		@Override
		public Int2ShortNavigableMap tailMap(int fromKey, boolean inclusive) {
			if (!inRange(fromKey, inclusive)) throw new IllegalArgumentException("fromKey out of range");
			return new AscendingNaivgableSubMap(map, false, fromKey, inclusive, toEnd, hi, hiInclusive);
		}
		
		@Override
		protected Node subLowest() { return absLowest(); }
		@Override
		protected Node subHighest() { return absHighest(); }
		@Override
		protected Node subCeiling(int key) { return absCeiling(key); }
		@Override
		protected Node subHigher(int key) { return absHigher(key); }
		@Override
		protected Node subFloor(int key) { return absFloor(key); }
		@Override
		protected Node subLower(int key) { return absLower(key); }
		
		@Override
		protected IntBidirectionalIterator keyIterator() {
			return new AcsendingSubKeyIterator(absLowest(), absHighFence(), absLowFence()); 
		}
		@Override
		protected IntBidirectionalIterator keyIterator(int element) {
			return new AcsendingSubKeyIterator(absLower(element), absHighFence(), absLowFence()); 
		}
		
		@Override
		protected ShortBidirectionalIterator valueIterator() {
			return new AcsendingSubValueIterator(absLowest(), absHighFence(), absLowFence()); 
		}
		
		@Override
		protected IntBidirectionalIterator descendingKeyIterator() {
			return new DecsendingSubKeyIterator(absHighest(), absLowFence(), absHighFence()); 
		}
		
		class AscendingSubEntrySet extends SubEntrySet {
			@Override
			public ObjectIterator<Int2ShortMap.Entry> iterator() {
				return new AcsendingSubEntryIterator(absLowest(), absHighFence(), absLowFence());
			}
		}
	}
	
	static class DescendingNaivgableSubMap extends NavigableSubMap
	{
		IntComparator comparator;
		DescendingNaivgableSubMap(Int2ShortAVLTreeMap map, boolean fromStart, int lo, boolean loInclusive, boolean toEnd, int hi, boolean hiInclusive) {
			super(map, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
			comparator = map.comparator() == null ? IntComparator.of(Collections.reverseOrder()) : map.comparator().reversed();
		}
		
		@Override
		public IntComparator comparator() { return comparator; }
		
		@Override
		public Int2ShortNavigableMap descendingMap() {
			if(inverse == null) inverse = new AscendingNaivgableSubMap(map, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
			return inverse;
		}

		@Override
		public IntNavigableSet navigableKeySet() {
			if(keySet == null) keySet = new KeySet(this);
			return keySet;
		}
		
		@Override
		public Int2ShortNavigableMap subMap(int fromKey, boolean fromInclusive, int toKey, boolean toInclusive) {
			if (!inRange(fromKey, fromInclusive)) throw new IllegalArgumentException("fromKey out of range");
			if (!inRange(toKey, toInclusive)) throw new IllegalArgumentException("toKey out of range");
			return new DescendingNaivgableSubMap(map, false, toKey, toInclusive, false, fromKey, fromInclusive);
		}
		
		@Override
		public Int2ShortNavigableMap headMap(int toKey, boolean inclusive) {
			if (!inRange(toKey, inclusive)) throw new IllegalArgumentException("toKey out of range");
			return new DescendingNaivgableSubMap(map, false, toKey, inclusive, toEnd, hi, hiInclusive);
		}
		
		@Override
		public Int2ShortNavigableMap tailMap(int fromKey, boolean inclusive) {
			if (!inRange(fromKey, inclusive)) throw new IllegalArgumentException("fromKey out of range");
			return new DescendingNaivgableSubMap(map, fromStart, lo, loInclusive, false, fromKey, inclusive);
		}
		
		@Override
		public ObjectSet<Int2ShortMap.Entry> int2ShortEntrySet() {
			if(entrySet == null) entrySet = new DescendingSubEntrySet();
			return entrySet;
		}
		
		@Override
		protected Node subLowest() { return absHighest(); }
		@Override
		protected Node subHighest() { return absLowest(); }
		@Override
		protected Node subCeiling(int key) { return absFloor(key); }
		@Override
		protected Node subHigher(int key) { return absLower(key); }
		@Override
		protected Node subFloor(int key) { return absCeiling(key); }
		@Override
		protected Node subLower(int key) { return absHigher(key); }
		@Override
		protected Node next(Node entry) { return entry.previous(); }
		@Override
		protected Node previous(Node entry) { return entry.next(); }
		
		@Override
		protected IntBidirectionalIterator keyIterator() {
			return new DecsendingSubKeyIterator(absHighest(), absLowFence(), absHighFence()); 
		}
		
		@Override
		protected IntBidirectionalIterator keyIterator(int element) {
			return new DecsendingSubKeyIterator(absHigher(element), absLowFence(), absHighFence()); 
		}
		
		@Override
		protected ShortBidirectionalIterator valueIterator() {
			return new DecsendingSubValueIterator(absHighest(), absLowFence(), absHighFence()); 
		}
		
		@Override
		protected IntBidirectionalIterator descendingKeyIterator() {
			return new AcsendingSubKeyIterator(absLowest(), absHighFence(), absLowFence()); 
		}
		
		class DescendingSubEntrySet extends SubEntrySet {
			@Override
			public ObjectIterator<Int2ShortMap.Entry> iterator() {
				return new DecsendingSubEntryIterator(absHighest(), absLowFence(), absHighFence());
			}
		}
	}
	
	static abstract class NavigableSubMap extends AbstractInt2ShortMap implements Int2ShortNavigableMap
	{
		final Int2ShortAVLTreeMap map;
		final int lo, hi;
		final boolean fromStart, toEnd;
		final boolean loInclusive, hiInclusive;
		
		Int2ShortNavigableMap inverse;
		IntNavigableSet keySet;
		ObjectSet<Int2ShortMap.Entry> entrySet;
		ShortCollection values;
		
		NavigableSubMap(Int2ShortAVLTreeMap map, boolean fromStart, int lo, boolean loInclusive, boolean toEnd, int hi, boolean hiInclusive) {
			if (!fromStart && !toEnd) {
				if (map.compare(lo, hi) > 0) throw new IllegalArgumentException("fromKey > toKey");
			} 
			else {
				if (!fromStart) map.validate(lo);
				if (!toEnd) map.validate(hi);
			}
			this.map = map;
			this.fromStart = fromStart;
			this.lo = lo;
			this.loInclusive = loInclusive;
			this.toEnd = toEnd;
			this.hi = hi;
			this.hiInclusive = hiInclusive;
		}
		
		@Override
		public void setDefaultMaxValue(int e) { map.setDefaultMaxValue(e); }
		@Override
		public int getDefaultMaxValue() { return map.getDefaultMaxValue(); }
		@Override
		public void setDefaultMinValue(int e) { map.setDefaultMinValue(e); }
		@Override
		public int getDefaultMinValue() { return map.getDefaultMinValue(); }
		protected boolean isNullComparator() { return map.comparator() == null; }
		
		@Override
		public AbstractInt2ShortMap setDefaultReturnValue(short v) { 
			map.setDefaultReturnValue(v);
			return this;
		}
		
		@Override
		public short getDefaultReturnValue() { return map.getDefaultReturnValue(); }
		
		@Override
		public ShortCollection values() {
			if(values == null) values = new SubMapValues();
			return values;
		}
		
		@Override
		public IntNavigableSet descendingKeySet() {
			return descendingMap().navigableKeySet();
		}
		
		@Override
		public IntNavigableSet keySet() {
			return navigableKeySet();
		}
		
		protected abstract Node subLowest();
		protected abstract Node subHighest();
		protected abstract Node subCeiling(int key);
		protected abstract Node subHigher(int key);
		protected abstract Node subFloor(int key);
		protected abstract Node subLower(int key);
		protected abstract IntBidirectionalIterator keyIterator();
		protected abstract IntBidirectionalIterator keyIterator(int element);
		protected abstract ShortBidirectionalIterator valueIterator();
		protected abstract IntBidirectionalIterator descendingKeyIterator();
		protected int lowKeyOrNull(Node entry) { return entry == null ? getDefaultMinValue() : entry.key; }
		protected int highKeyOrNull(Node entry) { return entry == null ? getDefaultMaxValue() : entry.key; }
		protected Node next(Node entry) { return entry.next(); }
		protected Node previous(Node entry) { return entry.previous(); }
		
		protected boolean tooLow(int key) {
			if (!fromStart) {
				int c = map.compare(key, lo);
				if (c < 0 || (c == 0 && !loInclusive)) return true;
			}
			return false;
		}
		
		protected boolean tooHigh(int key) {
			if (!toEnd) {
				int c = map.compare(key, hi);
				if (c > 0 || (c == 0 && !hiInclusive)) return true;
			}
			return false;
		}
		protected boolean inRange(int key) { return !tooLow(key) && !tooHigh(key); }
		protected boolean inClosedRange(int key) { return (fromStart || map.compare(key, lo) >= 0) && (toEnd || map.compare(hi, key) >= 0); }
		protected boolean inRange(int key, boolean inclusive) { return inclusive ? inRange(key) : inClosedRange(key); }
		
		protected Node absLowest() {
			Node e = (fromStart ?  map.first : (loInclusive ? map.findCeilingNode(lo) : map.findHigherNode(lo)));
			return (e == null || tooHigh(e.key)) ? null : e;
		}
		
		protected Node absHighest() {
			Node e = (toEnd ?  map.last : (hiInclusive ?  map.findFloorNode(hi) : map.findLowerNode(hi)));
			return (e == null || tooLow(e.key)) ? null : e;
		}
		
		protected Node absCeiling(int key) {
			if (tooLow(key)) return absLowest();
			Node e = map.findCeilingNode(key);
			return (e == null || tooHigh(e.key)) ? null : e;
		}
		
		protected Node absHigher(int key) {
			if (tooLow(key)) return absLowest();
			Node e = map.findHigherNode(key);
			return (e == null || tooHigh(e.key)) ? null : e;
		}
		
		protected Node absFloor(int key) {
			if (tooHigh(key)) return absHighest();
			Node e = map.findFloorNode(key);
			return (e == null || tooLow(e.key)) ? null : e;
		}
		
		protected Node absLower(int key) {
			if (tooHigh(key)) return absHighest();
			Node e = map.findLowerNode(key);
			return (e == null || tooLow(e.key)) ? null : e;
		}
		
		protected Node absHighFence() { return (toEnd ? null : (hiInclusive ? map.findHigherNode(hi) : map.findCeilingNode(hi))); }
		protected Node absLowFence() { return (fromStart ? null : (loInclusive ?  map.findLowerNode(lo) : map.findFloorNode(lo))); }
		
		@Override
		public IntComparator comparator() { return map.comparator(); }
		
		@Override
		public int pollFirstIntKey() {
			Node entry = subLowest();
			if(entry != null) {
				int result = entry.key;
				map.removeNode(entry);
				return result;
			}
			return getDefaultMinValue();
		}
		
		@Override
		public int pollLastIntKey() {
			Node entry = subHighest();
			if(entry != null) {
				int result = entry.key;
				map.removeNode(entry);
				return result;
			}
			return getDefaultMaxValue();
		}
		
		@Override
		public short firstShortValue() {
			Node entry = subLowest();
			if(entry == null) throw new NoSuchElementException();
			return entry.value;
		}
		
		@Override
		public short lastShortValue() {
			Node entry = subHighest();
			if(entry == null) throw new NoSuchElementException();
			return entry.value;
		}
		
		@Override
		public int firstIntKey() {
			Node entry = subLowest();
			if(entry == null) throw new NoSuchElementException();
			return entry.key;
		}
		
		@Override
		public int lastIntKey() {
			Node entry = subHighest();
			if(entry == null) throw new NoSuchElementException();
			return entry.key;
		}
		
		@Override
		public short put(int key, short value) {
			if (!inRange(key)) throw new IllegalArgumentException("key out of range");
			return map.put(key, value);
		}
		
		@Override
		public short putIfAbsent(int key, short value) {
			if (!inRange(key)) throw new IllegalArgumentException("key out of range");
			return map.putIfAbsent(key, value);
		}
		
		@Override
		public short addTo(int key, short value) {
			if(!inRange(key)) throw new IllegalArgumentException("key out of range");
			return map.addTo(key, value);
		}
		
		@Override
		public short subFrom(int key, short value) {
			if(!inRange(key)) throw new IllegalArgumentException("key out of range");
			return map.subFrom(key, value);
		}
		
		@Override
		public boolean containsKey(int key) { return inRange(key) && map.containsKey(key); }
		
		@Override
		public short computeShortIfPresent(int key, IntShortUnaryOperator mappingFunction) {
			Objects.requireNonNull(mappingFunction);
			if(!inRange(key)) return getDefaultReturnValue();
			Node entry = map.findNode(key);
			if(entry == null || entry.value == getDefaultReturnValue()) return getDefaultReturnValue();
			short newValue = mappingFunction.apply(key, entry.value);
			if(newValue == getDefaultReturnValue()) {
				map.removeNode(entry);
				return newValue;
			}
			entry.value = newValue;
			return newValue;
		}
		
		@Override
		public short remove(int key) {
			return inRange(key) ? map.remove(key) : getDefaultReturnValue();
		}
		
		@Override
		public short removeOrDefault(int key, short defaultValue) {
			return inRange(key) ? map.removeOrDefault(key, defaultValue) : defaultValue;
		}
		
		@Override
		public boolean remove(int key, short value) {
			return inRange(key) && map.remove(key, value);
		}
		
		
		@Override
		public short get(int key) {
			return inRange(key) ? map.get(key) : getDefaultReturnValue();
		}
		
		@Override
		public short getOrDefault(int key, short defaultValue) {
			return inRange(key) ? map.getOrDefault(key, defaultValue) : getDefaultReturnValue();
		}
		
		
		@Override
		public int lowerKey(int key) { return lowKeyOrNull(subLower(key)); }
		@Override
		public int floorKey(int key) { return lowKeyOrNull(subFloor(key)); }
		@Override
		public int ceilingKey(int key) { return highKeyOrNull(subCeiling(key)); }
		@Override
		public int higherKey(int key) { return highKeyOrNull(subHigher(key)); }
		@Override
		public Int2ShortMap.Entry lowerEntry(int key) { return subLower(key); }
		@Override
		public Int2ShortMap.Entry floorEntry(int key) { return subFloor(key); }
		@Override
		public Int2ShortMap.Entry ceilingEntry(int key) { return subCeiling(key); }
		@Override
		public Int2ShortMap.Entry higherEntry(int key) { return subHigher(key); }
		
		@Override
		public boolean isEmpty() {
			if(fromStart && toEnd) return map.isEmpty();
			Node n = absLowest();
			return n == null || tooHigh(n.key);
		}
		
		@Override
		public int size() { return fromStart && toEnd ? map.size() : entrySet().size(); }
		
		@Override
		public Int2ShortNavigableMap copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public Int2ShortMap.Entry firstEntry() {
			Node entry = subLowest();
			return entry == null ? null : entry.export();
		}

		@Override
		public Int2ShortMap.Entry lastEntry() {
			Node entry = subHighest();
			return entry == null ? null : entry.export();
		}

		@Override
		public Int2ShortMap.Entry pollFirstEntry() {
			Node entry = subLowest();
			if(entry != null) {
				Int2ShortMap.Entry result = entry.export();
				map.removeNode(entry);
				return result;
			}
			return null;
		}

		@Override
		public Int2ShortMap.Entry pollLastEntry() {
			Node entry = subHighest();
			if(entry != null) {
				Int2ShortMap.Entry result = entry.export();
				map.removeNode(entry);
				return result;
			}
			return null;
		}
		
		abstract class SubEntrySet extends AbstractObjectSet<Int2ShortMap.Entry> {
			@Override
			public int size() {
				if (fromStart && toEnd) return map.size();
				int size = 0;
				for(ObjectIterator<Int2ShortMap.Entry> iter = iterator();iter.hasNext();iter.next(),size++);
				return size;
			}
			
			@Override
			public boolean isEmpty() {
				Node n = absLowest();
				return n == null || tooHigh(n.key);
			}
			
			@Override
			public boolean contains(Object o) {
				if (!(o instanceof Map.Entry)) return false;
				if(o instanceof Int2ShortMap.Entry)
				{
					Int2ShortMap.Entry entry = (Int2ShortMap.Entry) o;
					int key = entry.getIntKey();
					if (!inRange(key)) return false;
					Node node = map.findNode(key);
					return node != null && entry.getShortValue() == node.getShortValue();
				}
				Map.Entry<?,?> entry = (Map.Entry<?,?>) o;
				if(entry.getKey() == null && isNullComparator()) return false;
				Integer key = (Integer)entry.getKey();
				if (!inRange(key)) return false;
				Node node = map.findNode(key);
				return node != null && Objects.equals(entry.getValue(), Short.valueOf(node.getShortValue()));
			}
			
			@Override
			public boolean remove(Object o) {
				if (!(o instanceof Map.Entry)) return false;
				if(o instanceof Int2ShortMap.Entry)
				{
					Int2ShortMap.Entry entry = (Int2ShortMap.Entry) o;
					int key = entry.getIntKey();
					if (!inRange(key)) return false;
					Node node = map.findNode(key);
					if (node != null && node.getShortValue() == entry.getShortValue()) {
						map.removeNode(node);
						return true;
					}
					return false;
				}
				Map.Entry<?,?> entry = (Map.Entry<?,?>) o;
				Integer key = (Integer)entry.getKey();
				if (!inRange(key)) return false;
				Node node = map.findNode(key);
				if (node != null && Objects.equals(node.getValue(), entry.getValue())) {
					map.removeNode(node);
					return true;
				}
				return false;
			}
			
			@Override
			public void forEach(Consumer<? super Int2ShortMap.Entry> action) {
				Objects.requireNonNull(action);
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					action.accept(new BasicEntry(entry.key, entry.value));
			}
			
			@Override
			public void forEachIndexed(IntObjectConsumer<Int2ShortMap.Entry> action) {
				Objects.requireNonNull(action);
				int index = 0;
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					action.accept(index++, new BasicEntry(entry.key, entry.value));
			}
			
			@Override
			public <E> void forEach(E input, ObjectObjectConsumer<E, Int2ShortMap.Entry> action) {
				Objects.requireNonNull(action);
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					action.accept(input, new BasicEntry(entry.key, entry.value));
			}
			
			@Override
			public boolean matchesAny(Predicate<Int2ShortMap.Entry> filter) {
				Objects.requireNonNull(filter);
				if(size() <= 0) return false;
				BasicEntry subEntry = new BasicEntry();
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					subEntry.set(entry.key, entry.value);
					if(filter.test(subEntry)) return true;
				}
				return false;
			}
			
			@Override
			public boolean matchesNone(Predicate<Int2ShortMap.Entry> filter) {
				Objects.requireNonNull(filter);
				if(size() <= 0) return true;
				BasicEntry subEntry = new BasicEntry();
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					subEntry.set(entry.key, entry.value);
					if(filter.test(subEntry)) return false;
				}
				return true;
			}
			
			@Override
			public boolean matchesAll(Predicate<Int2ShortMap.Entry> filter) {
				Objects.requireNonNull(filter);
				if(size() <= 0) return true;
				BasicEntry subEntry = new BasicEntry();
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					subEntry.set(entry.key, entry.value);
					if(!filter.test(subEntry)) return false;
				}
				return true;
			}
			
			@Override
			public <E> E reduce(E identity, BiFunction<E, Int2ShortMap.Entry, E> operator) {
				Objects.requireNonNull(operator);
				E state = identity;
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					state = operator.apply(state, new BasicEntry(entry.key, entry.value));
				}
				return state;
			}
			
			@Override
			public Int2ShortMap.Entry reduce(ObjectObjectUnaryOperator<Int2ShortMap.Entry, Int2ShortMap.Entry> operator) {
				Objects.requireNonNull(operator);
				Int2ShortMap.Entry state = null;
				boolean empty = true;
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					if(empty) {
						empty = false;
						state = new BasicEntry(entry.key, entry.value);
						continue;
					}
					state = operator.apply(state, new BasicEntry(entry.key, entry.value));
				}
				return state;
			}
			
			@Override
			public Int2ShortMap.Entry findFirst(Predicate<Int2ShortMap.Entry> filter) {
				Objects.requireNonNull(filter);
				if(size() <= 0) return null;
				BasicEntry subEntry = new BasicEntry();
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					subEntry.set(entry.key, entry.value);
					if(filter.test(subEntry)) return subEntry;
				}
				return null;
			}
			
			@Override
			public int count(Predicate<Int2ShortMap.Entry> filter) {
				Objects.requireNonNull(filter);
				if(size() <= 0) return 0;
				int result = 0;
				BasicEntry subEntry = new BasicEntry();
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					subEntry.set(entry.key, entry.value);
					if(filter.test(subEntry)) result++;
				}
				return result;
			}
		}
		
		final class SubMapValues extends AbstractShortCollection {
			@Override
			public boolean add(short o) { throw new UnsupportedOperationException(); }
			
			@Override
			public boolean contains(short e) {
				return containsValue(e);
			}
			
			@Override
			public ShortIterator iterator() { return valueIterator(); }
			
			@Override
			public int size() {
				return NavigableSubMap.this.size();
			}
			
			@Override
			public void clear() {
				NavigableSubMap.this.clear();
			}
			
			@Override
			public void forEach(ShortConsumer action) {
				Objects.requireNonNull(action);
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					action.accept(entry.value);
			}
			
			@Override
			public void forEachIndexed(IntShortConsumer action) {
				Objects.requireNonNull(action);
				int index = 0;
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					action.accept(index++, entry.value);
			}
			
			@Override
			public <E> void forEach(E input, ObjectShortConsumer<E> action) {
				Objects.requireNonNull(action);
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					action.accept(input, entry.value);
			}
			
			@Override
			public boolean matchesAny(ShortPredicate filter) {
				Objects.requireNonNull(filter);
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					if(filter.test(entry.value)) return true;
				return false;
			}
			
			@Override
			public boolean matchesNone(ShortPredicate filter) {
				Objects.requireNonNull(filter);
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					if(filter.test(entry.value)) return false;
				return true;
			}
			
			@Override
			public boolean matchesAll(ShortPredicate filter) {
				Objects.requireNonNull(filter);
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					if(!filter.test(entry.value)) return false;
				return true;
			}
			
			@Override
			public short reduce(short identity, ShortShortUnaryOperator operator) {
				Objects.requireNonNull(operator);
				short state = identity;
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					state = operator.applyAsShort(state, entry.value);
				return state;
			}
			
			@Override
			public short reduce(ShortShortUnaryOperator operator) {
				Objects.requireNonNull(operator);
				short state = (short)0;
				boolean empty = true;
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					if(empty) {
						empty = false;
						state = entry.value;
						continue;
					}
					state = operator.applyAsShort(state, entry.value);
				}
				return state;
			}
			
			@Override
			public short findFirst(ShortPredicate filter) {
				Objects.requireNonNull(filter);
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					if(filter.test(entry.value)) return entry.value;
				return (short)0;
			}
			
			@Override
			public int count(ShortPredicate filter) {
				Objects.requireNonNull(filter);
				int result = 0;
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					if(filter.test(entry.value)) result++;
				return result;
			}
		}
		
		class DecsendingSubEntryIterator extends SubMapEntryIterator implements ObjectBidirectionalIterator<Int2ShortMap.Entry>
		{
			public DecsendingSubEntryIterator(Node first, Node forwardFence, Node backwardFence) {
				super(first, forwardFence, backwardFence, false);
			}
			
			@Override
			protected Node moveNext(Node node) {
				return node.previous();
			}
			
			@Override
			protected Node movePrevious(Node node) {
				return node.next();
			}
			
			@Override
			public Int2ShortMap.Entry previous() {
				if(!hasPrevious()) throw new NoSuchElementException();
				return previousEntry();
			}

			@Override
			public Int2ShortMap.Entry next() {
				if(!hasNext()) throw new NoSuchElementException();
				return nextEntry();
			}
		}
		
		class AcsendingSubEntryIterator extends SubMapEntryIterator implements ObjectBidirectionalIterator<Int2ShortMap.Entry>
		{
			public AcsendingSubEntryIterator(Node first, Node forwardFence, Node backwardFence) {
				super(first, forwardFence, backwardFence, true);
			}

			@Override
			public Int2ShortMap.Entry previous() {
				if(!hasPrevious()) throw new NoSuchElementException();
				return previousEntry();
			}

			@Override
			public Int2ShortMap.Entry next() {
				if(!hasNext()) throw new NoSuchElementException();
				return nextEntry();
			}
		}
		
		class DecsendingSubKeyIterator extends SubMapEntryIterator implements IntBidirectionalIterator
		{
			public DecsendingSubKeyIterator(Node first, Node forwardFence, Node backwardFence) {
				super(first, forwardFence, backwardFence, false);
			}
			
			@Override
			protected Node moveNext(Node node) {
				return node.previous();
			}
			
			@Override
			protected Node movePrevious(Node node) {
				return node.next();
			}
			
			@Override
			public int previousInt() {
				if(!hasPrevious()) throw new NoSuchElementException();
				return previousEntry().key;
			}

			@Override
			public int nextInt() {
				if(!hasNext()) throw new NoSuchElementException();
				return nextEntry().key;
			}
		}
		
		class AcsendingSubKeyIterator extends SubMapEntryIterator implements IntBidirectionalIterator
		{
			public AcsendingSubKeyIterator(Node first, Node forwardFence, Node backwardFence) {
				super(first, forwardFence, backwardFence, true);
			}

			@Override
			public int previousInt() {
				if(!hasPrevious()) throw new NoSuchElementException();
				return previousEntry().key;
			}

			@Override
			public int nextInt() {
				if(!hasNext()) throw new NoSuchElementException();
				return nextEntry().key;
			}
		}
		
		class AcsendingSubValueIterator extends SubMapEntryIterator implements ShortBidirectionalIterator
		{
			public AcsendingSubValueIterator(Node first, Node forwardFence, Node backwardFence) {
				super(first, forwardFence, backwardFence, true);
			}

			@Override
			public short previousShort() {
				if(!hasPrevious()) throw new NoSuchElementException();
				return previousEntry().value;
			}

			@Override
			public short nextShort() {
				if(!hasNext()) throw new NoSuchElementException();
				return nextEntry().value;
			}
		}
		
		class DecsendingSubValueIterator extends SubMapEntryIterator implements ShortBidirectionalIterator
		{
			public DecsendingSubValueIterator(Node first, Node forwardFence, Node backwardFence) {
				super(first, forwardFence, backwardFence, false);
			}
			
			@Override
			protected Node moveNext(Node node) {
				return node.previous();
			}
			
			@Override
			protected Node movePrevious(Node node) {
				return node.next();
			}
			
			@Override
			public short previousShort() {
				if(!hasPrevious()) throw new NoSuchElementException();
				return previousEntry().value;
			}

			@Override
			public short nextShort() {
				if(!hasNext()) throw new NoSuchElementException();
				return nextEntry().value;
			}
		}
		
		abstract class SubMapEntryIterator
		{
			final boolean isForward;
			boolean wasForward;
			Node lastReturned;
			Node next;
			Node previous;
			boolean unboundForwardFence;
			boolean unboundBackwardFence;
			int forwardFence;
			int backwardFence;
			
			public SubMapEntryIterator(Node first, Node forwardFence, Node backwardFence, boolean isForward) {
				next = first;
				previous = first == null ? null : movePrevious(first);
				this.forwardFence = forwardFence == null ? 0 : forwardFence.key;
				this.backwardFence = backwardFence == null ? 0 : backwardFence.key;
				unboundForwardFence = forwardFence == null;
				unboundBackwardFence = backwardFence == null;
				this.isForward = isForward;
			}
			
			protected Node moveNext(Node node) {
				return node.next();
			}
			
			protected Node movePrevious(Node node) {
				return node.previous();
			}
			
			public boolean hasNext() {
                return next != null && (unboundForwardFence || next.key != forwardFence);
			}
			
			protected Node nextEntry() {
				lastReturned = next;
				previous = next;
				Node result = next;
				next = moveNext(next);
				wasForward = isForward;
				return result;
			}
			
			public boolean hasPrevious() {
                return previous != null && (unboundBackwardFence || previous.key != backwardFence);
			}
			
			protected Node previousEntry() {
				lastReturned = previous;
				next = previous;
				Node result = previous;
				previous = movePrevious(previous);
				wasForward = !isForward;
				return result;
			}
			
			public void remove() {
				if(lastReturned == null) throw new IllegalStateException();
				if(next == lastReturned) next = moveNext(next);
				if(previous == lastReturned) previous = movePrevious(previous);
				if(wasForward && lastReturned.needsSuccessor()) next = lastReturned;
				map.removeNode(lastReturned);
				lastReturned = null;
			}
		}
	}
	
	class Values extends AbstractShortCollection
	{
		@Override
		public ShortIterator iterator() {
			return new AscendingValueIterator(first);
		}
		
		@Override
		public boolean add(short e) { throw new UnsupportedOperationException(); }
		
		@Override
		public void clear() {
			Int2ShortAVLTreeMap.this.clear();
		}
		
		@Override
		public int size() {
			return Int2ShortAVLTreeMap.this.size;
		}
		
		@Override
		public boolean contains(short e) {
			return containsValue(e);
		}
		
		@Override
		public boolean remove(Object o) {
			for(Node entry = first; entry != null; entry = entry.next()) {
				if(Objects.equals(entry.getValue(), o)) {
					removeNode(entry);
					return true;
				}
			}
			return false;
		}
		
		@Override
		public void forEach(ShortConsumer action) {
			Objects.requireNonNull(action);
			for(Node entry = first;entry != null;entry = entry.next())
				action.accept(entry.value);
		}
		
		@Override
		public void forEachIndexed(IntShortConsumer action) {
			Objects.requireNonNull(action);
			int index = 0;
			for(Node entry = first;entry != null;entry = entry.next())
				action.accept(index++, entry.value);
		}
		
		@Override
		public <E> void forEach(E input, ObjectShortConsumer<E> action) {
			Objects.requireNonNull(action);
			for(Node entry = first;entry != null;entry = entry.next())
				action.accept(input, entry.value);
		}
		
		@Override
		public boolean matchesAny(ShortPredicate filter) {
			Objects.requireNonNull(filter);
			for(Node entry = first;entry != null;entry = entry.next())
				if(filter.test(entry.value)) return true;
			return false;
		}
		
		@Override
		public boolean matchesNone(ShortPredicate filter) {
			Objects.requireNonNull(filter);
			for(Node entry = first;entry != null;entry = entry.next())
				if(filter.test(entry.value)) return false;
			return true;
		}
		
		@Override
		public boolean matchesAll(ShortPredicate filter) {
			Objects.requireNonNull(filter);
			for(Node entry = first;entry != null;entry = entry.next())
				if(!filter.test(entry.value)) return false;
			return true;
		}
		
		@Override
		public short reduce(short identity, ShortShortUnaryOperator operator) {
			Objects.requireNonNull(operator);
			short state = identity;
			for(Node entry = first;entry != null;entry = entry.next())
				state = operator.applyAsShort(state, entry.value);
			return state;
		}
		
		@Override
		public short reduce(ShortShortUnaryOperator operator) {
			Objects.requireNonNull(operator);
			short state = (short)0;
			boolean empty = true;
			for(Node entry = first;entry != null;entry = entry.next()) {
				if(empty) {
					empty = false;
					state = entry.value;
					continue;
				}
				state = operator.applyAsShort(state, entry.value);
			}
			return state;
		}
		
		@Override
		public short findFirst(ShortPredicate filter) {
			Objects.requireNonNull(filter);
			for(Node entry = first;entry != null;entry = entry.next())
				if(filter.test(entry.value)) return entry.value;
			return (short)0;
		}
		
		@Override
		public int count(ShortPredicate filter) {
			Objects.requireNonNull(filter);
			int result = 0;
			for(Node entry = first;entry != null;entry = entry.next())
				if(filter.test(entry.value)) result++;
			return result;
		}
	}
	
	class EntrySet extends AbstractObjectSet<Int2ShortMap.Entry> {
		
		@Override
		public ObjectIterator<Int2ShortMap.Entry> iterator() {
			return new AscendingMapEntryIterator(first);
		}
		
		@Override
		public void clear() {
			Int2ShortAVLTreeMap.this.clear();
		}
		
		@Override
		public int size() {
			return Int2ShortAVLTreeMap.this.size;
		}
		
		@Override
		public boolean contains(Object o) {
			if (!(o instanceof Map.Entry)) return false;
			if(o instanceof Int2ShortMap.Entry)
			{
				Int2ShortMap.Entry entry = (Int2ShortMap.Entry) o;
				int key = entry.getIntKey();
				Node node = findNode(key);
				return node != null && entry.getShortValue() == node.getShortValue();
			}
			Map.Entry<?,?> entry = (Map.Entry<?,?>) o;
			if(entry.getKey() == null && comparator() == null) return false;
			if(!(entry.getKey() instanceof Integer)) return false;
			Integer key = (Integer)entry.getKey();
			Node node = findNode(key);
			return node != null && Objects.equals(entry.getValue(), Short.valueOf(node.getShortValue()));
		}
		
		@Override
		public boolean remove(Object o) {
			if (!(o instanceof Map.Entry)) return false;
			if(o instanceof Int2ShortMap.Entry)
			{
				Int2ShortMap.Entry entry = (Int2ShortMap.Entry) o;
				int key = entry.getIntKey();
				Node node = findNode(key);
				if (node != null && entry.getShortValue() == node.getShortValue()) {
					removeNode(node);
					return true;
				}
				return false;
			}
			Map.Entry<?,?> entry = (Map.Entry<?,?>) o;
			Integer key = (Integer)entry.getKey();
			Node node = findNode(key);
			if (node != null && Objects.equals(entry.getValue(), Short.valueOf(node.getShortValue()))) {
				removeNode(node);
				return true;
			}
			return false;
		}
		
		@Override
		public void forEach(Consumer<? super Int2ShortMap.Entry> action) {
			Objects.requireNonNull(action);
			for(Node entry = first;entry != null;entry = entry.next())
				action.accept(new BasicEntry(entry.key, entry.value));
		}
		
		@Override
		public void forEachIndexed(IntObjectConsumer<Int2ShortMap.Entry> action) {
			Objects.requireNonNull(action);
			int index = 0;
			for(Node entry = first;entry != null;entry = entry.next())
				action.accept(index++, new BasicEntry(entry.key, entry.value));
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, Int2ShortMap.Entry> action) {
			Objects.requireNonNull(action);
			for(Node entry = first;entry != null;entry = entry.next())
				action.accept(input, new BasicEntry(entry.key, entry.value));
		}
		
		@Override
		public boolean matchesAny(Predicate<Int2ShortMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			BasicEntry subEntry = new BasicEntry();
			for(Node entry = first;entry != null;entry = entry.next()) {
				subEntry.set(entry.key, entry.value);
				if(filter.test(subEntry)) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Predicate<Int2ShortMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			BasicEntry subEntry = new BasicEntry();
			for(Node entry = first;entry != null;entry = entry.next()) {
				subEntry.set(entry.key, entry.value);
				if(filter.test(subEntry)) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Predicate<Int2ShortMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			BasicEntry subEntry = new BasicEntry();
			for(Node entry = first;entry != null;entry = entry.next()) {
				subEntry.set(entry.key, entry.value);
				if(!filter.test(subEntry)) return false;
			}
			return true;
		}
		
		@Override
		public <E> E reduce(E identity, BiFunction<E, Int2ShortMap.Entry, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			for(Node entry = first;entry != null;entry = entry.next()) {
				state = operator.apply(state, new BasicEntry(entry.key, entry.value));
			}
			return state;
		}
		
		@Override
		public Int2ShortMap.Entry reduce(ObjectObjectUnaryOperator<Int2ShortMap.Entry, Int2ShortMap.Entry> operator) {
			Objects.requireNonNull(operator);
			Int2ShortMap.Entry state = null;
			boolean empty = true;
			for(Node entry = first;entry != null;entry = entry.next()) {
				if(empty) {
					empty = false;
					state = new BasicEntry(entry.key, entry.value);
					continue;
				}
				state = operator.apply(state, new BasicEntry(entry.key, entry.value));
			}
			return state;
		}
		
		@Override
		public Int2ShortMap.Entry findFirst(Predicate<Int2ShortMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return null;
			BasicEntry subEntry = new BasicEntry();
			for(Node entry = first;entry != null;entry = entry.next()) {
				subEntry.set(entry.key, entry.value);
				if(filter.test(subEntry)) return subEntry;
			}
			return null;
		}
		
		@Override
		public int count(Predicate<Int2ShortMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			int result = 0;
			BasicEntry subEntry = new BasicEntry();
			for(Node entry = first;entry != null;entry = entry.next()) {
				subEntry.set(entry.key, entry.value);
				if(filter.test(subEntry)) result++;
			}
			return result;
		}
	}
	
	class DescendingKeyIterator extends MapEntryIterator implements IntBidirectionalIterator
	{
		public DescendingKeyIterator(Node first) {
			super(first, false);
		}
		
		@Override
		protected Node moveNext(Node node) {
			return node.previous();
		}
		
		@Override
		protected Node movePrevious(Node node) {
			return node.next();
		}
		
		@Override
		public int previousInt() {
			if(!hasPrevious()) throw new NoSuchElementException();
			return previousEntry().key;
		}

		@Override
		public int nextInt() {
			if(!hasNext()) throw new NoSuchElementException();
			return nextEntry().key;
		}
	}
	
	class AscendingMapEntryIterator extends MapEntryIterator implements ObjectBidirectionalIterator<Int2ShortMap.Entry>
	{
		public AscendingMapEntryIterator(Node first)
		{
			super(first, true);
		}

		@Override
		public Int2ShortMap.Entry previous() {
			if(!hasPrevious()) throw new NoSuchElementException();
			return previousEntry();
		}

		@Override
		public Int2ShortMap.Entry next() {
			if(!hasNext()) throw new NoSuchElementException();
			return nextEntry();
		}
	}
	
	class AscendingValueIterator extends MapEntryIterator implements ShortBidirectionalIterator
	{
		public AscendingValueIterator(Node first) {
			super(first, true);
		}

		@Override
		public short previousShort() {
			if(!hasPrevious()) throw new NoSuchElementException();
			return previousEntry().value;
		}

		@Override
		public short nextShort() {
			if(!hasNext()) throw new NoSuchElementException();
			return nextEntry().value;
		}
	}
	
	class AscendingKeyIterator extends MapEntryIterator implements IntBidirectionalIterator
	{
		public AscendingKeyIterator(Node first) {
			super(first, true);
		}

		@Override
		public int previousInt() {
			if(!hasPrevious()) throw new NoSuchElementException();
			return previousEntry().key;
		}

		@Override
		public int nextInt() {
			if(!hasNext()) throw new NoSuchElementException();
			return nextEntry().key;
		}
	}
	
	abstract class MapEntryIterator
	{
		final boolean isForward;
		boolean wasMoved = false;
		Node lastReturned;
		Node next;
		Node previous;
		
		public MapEntryIterator(Node first, boolean isForward) {
			next = first;
			previous = first == null ? null : movePrevious(first);
			this.isForward = isForward;
		}
		
		protected Node moveNext(Node node) {
			return node.next();
		}
		
		protected Node movePrevious(Node node) {
			return node.previous();
		}
		
		public boolean hasNext() {
            return next != null;
		}
		
		protected Node nextEntry() {
			lastReturned = next;
			previous = next;
			Node result = next;
			next = moveNext(next);
			wasMoved = isForward;
			return result;
		}
		
		public boolean hasPrevious() {
            return previous != null;
		}
		
		protected Node previousEntry() {
			lastReturned = previous;
			next = previous;
			Node result = previous;
			previous = movePrevious(previous);
			wasMoved = !isForward;
			return result;
		}
		
		public void remove() {
			if(lastReturned == null) throw new IllegalStateException();
			if(next == lastReturned) next = moveNext(next);
			if(previous == lastReturned) previous = movePrevious(previous);
			if(wasMoved && lastReturned.needsSuccessor()) next = lastReturned;
			removeNode(lastReturned);
			lastReturned = null;
		}
	}
	
	private static final class Node implements Int2ShortMap.Entry
	{
		int key;
		short value;
		int state;
		Node parent;
		Node left;
		Node right;
		
		Node(int key, short value, Node parent) {
			this.key = key;
			this.value = value;
			this.parent = parent;
		}
		
		Node copy() {
			Node entry = new Node(key, value, null);
			entry.state = state;
			if(left != null) {
				Node newLeft = left.copy();
				entry.left = newLeft;
				newLeft.parent = entry;
			}
			if(right != null) {
				Node newRight = right.copy();
				entry.right = newRight;
				newRight.parent = entry;
			}
			return entry;
		}
		
		public BasicEntry export() {
			return new BasicEntry(key, value);
		}
		
		@Override
		public int getIntKey() {
			return key;
		}
		
		@Override
		public short getShortValue() {
			return value;
		}
		
		@Override
		public short setValue(short value) {
			short oldValue = this.value;
			this.value = value;
			return oldValue;
		}
		
		short addTo(short value) {
			short oldValue = this.value;
			this.value += value;
			return oldValue;
		}
		
		short subFrom(short value) {
			short oldValue = this.value;
			this.value -= value;
			return oldValue;
		}
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Map.Entry) {
				if(obj instanceof Int2ShortMap.Entry) {
					Int2ShortMap.Entry entry = (Int2ShortMap.Entry)obj;
					return key == entry.getIntKey() && value == entry.getShortValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object otherKey = entry.getKey();
				if(otherKey == null) return false;
				Object otherValue = entry.getValue();
				return otherKey instanceof Integer && otherValue instanceof Short && key == ((Integer)otherKey).intValue() && value == ((Short)otherValue).shortValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Integer.hashCode(key) ^ Short.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Integer.toString(key) + "=" + Short.toString(value);
		}
		
		int getHeight() { return state; }
		
		void updateHeight() { state = (1 + Math.max(left == null ? -1 : left.getHeight(), right == null ? -1 : right.getHeight())); }
		
		int getBalance() { return (left == null ? -1 : left.getHeight()) - (right == null ? -1 : right.getHeight()); }
		
		boolean needsSuccessor() { return left != null && right != null; }
		
		boolean replace(Node entry) {
			if(entry != null) entry.parent = parent;
			if(parent != null) {
				if(parent.left == this) parent.left = entry;
				else parent.right = entry;
			}
			return parent == null;
		}
		
		Node next() {
			if(right != null) {
				Node parent = right;
				while(parent.left != null) parent = parent.left;
				return parent;
			}
			Node parent = this.parent;
			Node control = this;
			while(parent != null && control == parent.right) {
				control = parent;
				parent = parent.parent;
			}
			return parent;
		}
		
		Node previous() {
			if(left != null) {
				Node parent = left;
				while(parent.right != null) parent = parent.right;
				return parent;
			}
			Node parent = this.parent;
			Node control = this;
			while(parent != null && control == parent.left) {
				control = parent;
				parent = parent.parent;
			}
			return parent;
		}
	}
}