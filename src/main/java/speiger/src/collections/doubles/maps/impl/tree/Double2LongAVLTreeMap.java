package speiger.src.collections.doubles.maps.impl.tree;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.BiFunction;

import speiger.src.collections.doubles.collections.DoubleBidirectionalIterator;
import speiger.src.collections.doubles.functions.DoubleComparator;
import speiger.src.collections.doubles.functions.DoubleConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectDoubleConsumer;
import speiger.src.collections.doubles.functions.function.Double2BooleanFunction;
import speiger.src.collections.doubles.functions.consumer.DoubleLongConsumer;
import speiger.src.collections.doubles.functions.function.Double2LongFunction;
import speiger.src.collections.doubles.functions.function.DoubleLongUnaryOperator;
import speiger.src.collections.doubles.functions.function.DoubleDoubleUnaryOperator;
import speiger.src.collections.doubles.maps.abstracts.AbstractDouble2LongMap;
import speiger.src.collections.doubles.maps.interfaces.Double2LongMap;
import speiger.src.collections.doubles.maps.interfaces.Double2LongNavigableMap;
import speiger.src.collections.doubles.sets.AbstractDoubleSet;
import speiger.src.collections.doubles.sets.DoubleNavigableSet;
import speiger.src.collections.doubles.sets.DoubleSet;
import speiger.src.collections.doubles.sets.DoubleSortedSet;
import speiger.src.collections.doubles.utils.maps.Double2LongMaps;
import speiger.src.collections.longs.collections.AbstractLongCollection;
import speiger.src.collections.longs.collections.LongCollection;
import speiger.src.collections.longs.collections.LongIterator;
import speiger.src.collections.longs.functions.LongSupplier;
import speiger.src.collections.longs.collections.LongBidirectionalIterator;
import speiger.src.collections.longs.functions.function.LongLongUnaryOperator;
import speiger.src.collections.longs.functions.LongConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;
import speiger.src.collections.objects.functions.function.Object2BooleanFunction;
import speiger.src.collections.objects.functions.consumer.ObjectLongConsumer;
import speiger.src.collections.longs.functions.function.Long2BooleanFunction;
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
public class Double2LongAVLTreeMap extends AbstractDouble2LongMap implements Double2LongNavigableMap
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
	protected transient DoubleComparator comparator;
	
	/** the default return value for max searches */
	protected double defaultMaxNotFound = Double.MIN_VALUE;
	/** the default return value for min searches */
	protected double defaultMinNotFound = Double.MAX_VALUE;
	
	/** KeySet Cache */
	protected DoubleNavigableSet keySet;
	/** Values Cache */
	protected LongCollection values;
	/** EntrySet Cache */
	protected ObjectSet<Double2LongMap.Entry> entrySet;
	
	/**
	 * Default Constructor
	 */
	public Double2LongAVLTreeMap() {
	}
	
	/**
	 * Constructor that allows to define the sorter
	 * @param comp the function that decides how the tree is sorted, can be null
	 */
	public Double2LongAVLTreeMap(DoubleComparator comp) {
		comparator = comp;
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Double2LongAVLTreeMap(Double[] keys, Long[] values) {
		this(keys, values, null);
	}
	
	/**
	 * Helper constructor that has a custom sorter and allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param comp the function that decides how the tree is sorted, can be null
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Double2LongAVLTreeMap(Double[] keys, Long[] values, DoubleComparator comp) {
		comparator = comp;
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i].doubleValue(), values[i].longValue());
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Double2LongAVLTreeMap(double[] keys, long[] values) {
		this(keys, values, null);
	}
	
	/**
	 * Helper constructor that has a custom sorter and allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param comp the function that decides how the tree is sorted, can be null
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Double2LongAVLTreeMap(double[] keys, long[] values, DoubleComparator comp) {
		comparator = comp;
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i], values[i]);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 */
	public Double2LongAVLTreeMap(Map<? extends Double, ? extends Long> map) {
		this(map, null);
	}
	
	/**
	 * A Helper constructor that has a custom sorter and allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param comp the function that decides how the tree is sorted, can be null
	 */
	public Double2LongAVLTreeMap(Map<? extends Double, ? extends Long> map, DoubleComparator comp) {
		comparator = comp;
		putAll(map);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 */
	public Double2LongAVLTreeMap(Double2LongMap map) {
		this(map, null);
	}
	
	/**
	 * A Type Specific Helper function that has a custom sorter and allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param comp the function that decides how the tree is sorted, can be null
 	 */
	public Double2LongAVLTreeMap(Double2LongMap map, DoubleComparator comp) {
		comparator = comp;
		putAll(map);
	}

	@Override
	public void setDefaultMaxValue(double value) { defaultMaxNotFound = value; }
	@Override
	public double getDefaultMaxValue() { return defaultMaxNotFound; }
	@Override
	public void setDefaultMinValue(double value) { defaultMinNotFound = value; }
	@Override
	public double getDefaultMinValue() { return defaultMinNotFound; }
	
	
	@Override
	public long put(double key, long value) {
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
	public long putIfAbsent(double key, long value) {
		if(tree == null) {
			tree = first = last = new Node(key, value, null);
			size++;
			return getDefaultReturnValue();
		}
		int compare = 0;
		Node parent = tree;
		while(true) {
			if((compare = compare(key, parent.key)) == 0) return parent.value;
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
	public long addTo(double key, long value) {
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
	public long subFrom(double key, long value) {
		if(tree == null) return getDefaultReturnValue();
		int compare = 0;
		Node parent = tree;
		while(true) {
			if((compare = compare(key, parent.key)) == 0)
			{
				long oldValue = parent.subFrom(value);
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
	public DoubleComparator comparator() { return comparator; }

	@Override
	public boolean containsKey(double key) {
		return findNode(key) != null;
	}
	
	@Override
	public long get(double key) {
		Node node = findNode(key);
		return node == null ? getDefaultReturnValue() : node.value;
	}
	
	@Override
	public long getOrDefault(double key, long defaultValue) {
		Node node = findNode(key);
		return node == null ? defaultValue : node.value;
	}
	
	@Override
	public double firstDoubleKey() {
		if(tree == null) throw new NoSuchElementException();
		return first.key;
	}
	
	@Override
	public double pollFirstDoubleKey() {
		if(tree == null) return getDefaultMinValue();
		double result = first.key;
		removeNode(first);
		return result;
	}
	
	@Override
	public double lastDoubleKey() {
		if(tree == null) throw new NoSuchElementException();
		return last.key;
	}
	
	@Override
	public double pollLastDoubleKey() {
		if(tree == null) return getDefaultMaxValue();
		double result = last.key;
		removeNode(last);
		return result;
	}
	
	@Override
	public Double2LongMap.Entry firstEntry() {
		if(tree == null) return null;
		return first.export();
	}
	
	@Override
	public Double2LongMap.Entry lastEntry() {
		if(tree == null) return null;
		return last.export();
	}
	
	@Override
	public Double2LongMap.Entry pollFirstEntry() {
		if(tree == null) return null;
		BasicEntry entry = first.export();
		removeNode(first);
		return entry;
	}
	
	@Override
	public Double2LongMap.Entry pollLastEntry() {
		if(tree == null) return null;
		BasicEntry entry = last.export();
		removeNode(last);
		return entry;
	}
	
	@Override
	public long firstLongValue() {
		if(tree == null) throw new NoSuchElementException();
		return first.value;
	}
	
	@Override
	public long lastLongValue() {
		if(tree == null) throw new NoSuchElementException();
		return last.value;
	}
	
	@Override
	public long remove(double key) {
		Node entry = findNode(key);
		if(entry == null) return getDefaultReturnValue();
		long value = entry.value;
		removeNode(entry);
		return value;
	}
	
	@Override
	public long removeOrDefault(double key, long defaultValue) {
		Node entry = findNode(key);
		if(entry == null) return defaultValue;
		long value = entry.value;
		removeNode(entry);
		return value;
	}
	
	@Override
	public boolean remove(double key, long value) {
		Node entry = findNode(key);
		if(entry == null || entry.value != value) return false;
		removeNode(entry);
		return true;
	}
	
	@Override
	public boolean replace(double key, long oldValue, long newValue) {
		Node entry = findNode(key);
		if(entry == null || entry.value != oldValue) return false;
		entry.value = newValue;
		return true;
	}
	
	@Override
	public long replace(double key, long value) {
		Node entry = findNode(key);
		if(entry == null) return getDefaultReturnValue();
		long oldValue = entry.value;
		entry.value = value;
		return oldValue;
	}
	
	@Override
	public long computeLong(double key, DoubleLongUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		Node entry = findNode(key);
		if(entry == null) {
			long newValue = mappingFunction.applyAsLong(key, getDefaultReturnValue());
			if(newValue == getDefaultReturnValue()) return newValue;
			put(key, newValue);
			return newValue;
		}
		long newValue = mappingFunction.applyAsLong(key, entry.value);
		if(newValue == getDefaultReturnValue()) {
			removeNode(entry);
			return newValue;
		}
		entry.value = newValue;
		return newValue;
	}
	
	@Override
	public long computeLongIfAbsent(double key, Double2LongFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		Node entry = findNode(key);
		if(entry == null) {
			long newValue = mappingFunction.get(key);
			if(newValue == getDefaultReturnValue()) return newValue;
			put(key, newValue);
			return newValue;
		}
		if(Objects.equals(entry.value, getDefaultReturnValue())) {
			long newValue = mappingFunction.get(key);
			if(newValue == getDefaultReturnValue()) return newValue;
			entry.value = newValue;
		}
		return entry.value;
	}
	
	@Override
	public long supplyLongIfAbsent(double key, LongSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		Node entry = findNode(key);
		if(entry == null) {
			long newValue = valueProvider.getLong();
			if(newValue == getDefaultReturnValue()) return newValue;
			put(key, newValue);
			return newValue;
		}
		if(entry.value == getDefaultReturnValue()) {
			long newValue = valueProvider.getLong();
			if(newValue == getDefaultReturnValue()) return newValue;
			entry.value = newValue;
		}
		return entry.value;
	}
	
	@Override
	public long computeLongIfPresent(double key, DoubleLongUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		Node entry = findNode(key);
		if(entry == null || entry.value == getDefaultReturnValue()) return getDefaultReturnValue();
		long newValue = mappingFunction.applyAsLong(key, entry.value);
		if(newValue == getDefaultReturnValue()) {
			removeNode(entry);
			return newValue;
		}
		entry.value = newValue;
		return newValue;
	}
	
	@Override
	public long mergeLong(double key, long value, LongLongUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		Node entry = findNode(key);
		long newValue = entry == null || entry.value == getDefaultReturnValue() ? value : mappingFunction.applyAsLong(entry.value, value);
		if(newValue == getDefaultReturnValue()) {
			if(entry != null)
				removeNode(entry);
		}
		else if(entry == null) put(key, newValue);
		else entry.value = newValue;
		return newValue;
	}
	
	@Override
	public void mergeAllLong(Double2LongMap m, LongLongUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Double2LongMap.Entry entry : Double2LongMaps.fastIterable(m)) {
			double key = entry.getDoubleKey();
			Node subEntry = findNode(key);
			long newValue = subEntry == null || subEntry.value == getDefaultReturnValue() ? entry.getLongValue() : mappingFunction.applyAsLong(subEntry.value, entry.getLongValue());
			if(newValue == getDefaultReturnValue()) {
				if(subEntry != null)
					removeNode(subEntry);
			}
			else if(subEntry == null) put(key, newValue);
			else subEntry.value = newValue;
		}
	}
	
	@Override
	public void forEach(DoubleLongConsumer action) {
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
	
	protected DoubleBidirectionalIterator keyIterator() {
		return new AscendingKeyIterator(first);
	}
	
	protected DoubleBidirectionalIterator keyIterator(double element) {
		return new AscendingKeyIterator(findNode(element));
	}
	
	protected DoubleBidirectionalIterator descendingKeyIterator() {
		return new DescendingKeyIterator(last);
	}
		
	@Override
	public Double2LongAVLTreeMap copy() {
		Double2LongAVLTreeMap set = new Double2LongAVLTreeMap();
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
	public DoubleSortedSet keySet() {
		return navigableKeySet();
	}
	
	@Override
	public ObjectSet<Double2LongMap.Entry> double2LongEntrySet() {
		if(entrySet == null) entrySet = new EntrySet();
		return entrySet;
	}
	
	@Override
	public LongCollection values() {
		if(values == null) values = new Values();
		return values;
	}
	
	@Override
	public DoubleNavigableSet navigableKeySet() {
		if(keySet == null) keySet = new KeySet(this);
		return keySet;
	}
	
	@Override
	public Double2LongNavigableMap descendingMap() {
		return new DescendingNaivgableSubMap(this, true, 0D, true, true, 0D, true);
	}
	
	@Override
	public DoubleNavigableSet descendingKeySet() {
		return descendingMap().navigableKeySet();
	}
	
	@Override
	public Double2LongNavigableMap subMap(double fromKey, boolean fromInclusive, double toKey, boolean toInclusive) {
		return new AscendingNaivgableSubMap(this, false, fromKey, fromInclusive, false, toKey, toInclusive);
	}
	
	@Override
	public Double2LongNavigableMap headMap(double toKey, boolean inclusive) {
		return new AscendingNaivgableSubMap(this, true, 0D, true, false, toKey, inclusive);
	}
	
	@Override
	public Double2LongNavigableMap tailMap(double fromKey, boolean inclusive) {
		return new AscendingNaivgableSubMap(this, false, fromKey, inclusive, true, 0D, true);
	}
	
	@Override
	public double lowerKey(double e) {
		Node node = findLowerNode(e);
		return node != null ? node.key : getDefaultMinValue();
	}

	@Override
	public double floorKey(double e) {
		Node node = findFloorNode(e);
		return node != null ? node.key : getDefaultMinValue();
	}
	
	@Override
	public double higherKey(double e) {
		Node node = findHigherNode(e);
		return node != null ? node.key : getDefaultMaxValue();
	}

	@Override
	public double ceilingKey(double e) {
		Node node = findCeilingNode(e);
		return node != null ? node.key : getDefaultMaxValue();
	}
	
	@Override
	public Double2LongMap.Entry lowerEntry(double key) {
		Node node = findLowerNode(key);
		return node != null ? node.export() : null;
	}
	
	@Override
	public Double2LongMap.Entry higherEntry(double key) {
		Node node = findHigherNode(key);
		return node != null ? node.export() : null;
	}
	
	@Override
	public Double2LongMap.Entry floorEntry(double key) {
		Node node = findFloorNode(key);
		return node != null ? node.export() : null;
	}
	
	@Override
	public Double2LongMap.Entry ceilingEntry(double key) {
		Node node = findCeilingNode(key);
		return node != null ? node.export() : null;
	}
	
	protected Node findLowerNode(double key) {
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
	
	protected Node findFloorNode(double key) {
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
	
	protected Node findCeilingNode(double key) {
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
	
	protected Node findHigherNode(double key) {
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
	
	protected Node findNode(double key) {
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
	
	protected void validate(double k) { compare(k, k); }
	protected int compare(double k, double v) { return comparator != null ? comparator.compare(k, v) : Double.compare(k, v);}
	
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
	
	static class KeySet extends AbstractDoubleSet implements DoubleNavigableSet
	{
		Double2LongNavigableMap map;

		public KeySet(Double2LongNavigableMap map) {
			this.map = map;
		}
		
		@Override
		public void setDefaultMaxValue(double e) { map.setDefaultMaxValue(e); }
		@Override
		public double getDefaultMaxValue() { return map.getDefaultMaxValue(); }
		@Override
		public void setDefaultMinValue(double e) { map.setDefaultMinValue(e); }
		@Override
		public double getDefaultMinValue() { return map.getDefaultMinValue(); }
		@Override
		public double lower(double e) { return map.lowerKey(e); }
		@Override
		public double floor(double e) { return map.floorKey(e); }
		@Override
		public double ceiling(double e) { return map.ceilingKey(e); }
		@Override
		public double higher(double e) { return map.higherKey(e); }
		
		@Override
		public Double lower(Double e) {
			Double2LongMap.Entry node = map.lowerEntry(e.doubleValue());
			return node != null ? node.getKey() : null;
		}
		
		@Override
		public Double floor(Double e) {
			Double2LongMap.Entry node = map.floorEntry(e.doubleValue());
			return node != null ? node.getKey() : null;
		}
		
		@Override
		public Double higher(Double e) {
			Double2LongMap.Entry node = map.higherEntry(e.doubleValue());
			return node != null ? node.getKey() : null;
		}
		
		@Override
		public Double ceiling(Double e) {
			Double2LongMap.Entry node = map.ceilingEntry(e.doubleValue());
			return node != null ? node.getKey() : null;
		}
		
		@Override
		public double pollFirstDouble() { return map.pollFirstDoubleKey(); }
		@Override
		public double pollLastDouble() { return map.pollLastDoubleKey(); }
		@Override
		public DoubleComparator comparator() { return map.comparator(); }
		@Override
		public double firstDouble() { return map.firstDoubleKey(); } 
		@Override
		public double lastDouble() { return map.lastDoubleKey(); }
		@Override
		public void clear() { map.clear(); }
		
		@Override
		public boolean remove(double o) { 
			int oldSize = map.size();
			map.remove(o); 
			return oldSize != map.size();
		}
		
		@Override
		public boolean add(double e) { throw new UnsupportedOperationException(); }
		
		@Override
		public DoubleBidirectionalIterator iterator(double fromElement) {
			if(map instanceof Double2LongAVLTreeMap) return ((Double2LongAVLTreeMap)map).keyIterator(fromElement);
			return ((NavigableSubMap)map).keyIterator(fromElement);
		}
		
		@Override
		public DoubleNavigableSet subSet(double fromElement, boolean fromInclusive, double toElement, boolean toInclusive) { return new KeySet(map.subMap(fromElement, fromInclusive, toElement, toInclusive)); }
		@Override
		public DoubleNavigableSet headSet(double toElement, boolean inclusive) { return new KeySet(map.headMap(toElement, inclusive)); }
		@Override
		public DoubleNavigableSet tailSet(double fromElement, boolean inclusive) { return new KeySet(map.tailMap(fromElement, inclusive)); }
		
		@Override
		public DoubleBidirectionalIterator iterator() {
			if(map instanceof Double2LongAVLTreeMap) return ((Double2LongAVLTreeMap)map).keyIterator();
			return ((NavigableSubMap)map).keyIterator();
		}
		
		@Override
		public DoubleBidirectionalIterator descendingIterator() {
			if(map instanceof Double2LongAVLTreeMap) return ((Double2LongAVLTreeMap)map).descendingKeyIterator();
			return ((NavigableSubMap)map).descendingKeyIterator();
		}
		
		protected Node start() {
			if(map instanceof Double2LongAVLTreeMap) return ((Double2LongAVLTreeMap)map).first;
			return ((NavigableSubMap)map).subLowest();
		}
		
		protected Node end() {
			if(map instanceof Double2LongAVLTreeMap) return null;
			return ((NavigableSubMap)map).subHighest();
		}
		
		protected Node next(Node entry) {
			if(map instanceof Double2LongAVLTreeMap) return entry.next();
			return ((NavigableSubMap)map).next(entry);
		}
		
		protected Node previous(Node entry) {
			if(map instanceof Double2LongAVLTreeMap) return entry.previous();
			return ((NavigableSubMap)map).previous(entry);
		}
		
		@Override
		public DoubleNavigableSet descendingSet() { return new KeySet(map.descendingMap()); }
		@Override
		public KeySet copy() { throw new UnsupportedOperationException(); }
		@Override
		public boolean isEmpty() { return map.isEmpty(); }
		@Override
		public int size() { return map.size(); }
		
		@Override
		public void forEach(DoubleConsumer action) {
			Objects.requireNonNull(action);
			for(Node entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				action.accept(entry.key);
		}
		
		@Override
		public <E> void forEach(E input, ObjectDoubleConsumer<E> action) {
			Objects.requireNonNull(action);
			for(Node entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				action.accept(input, entry.key);
		}
		
		@Override
		public boolean matchesAny(Double2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(Node entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				if(filter.get(entry.key)) return true;
			return false;
		}
		
		@Override
		public boolean matchesNone(Double2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(Node entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				if(filter.get(entry.key)) return false;
			return true;
		}
		
		@Override
		public boolean matchesAll(Double2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(Node entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				if(!filter.get(entry.key)) return false;
			return true;
		}
		
		@Override
		public double reduce(double identity, DoubleDoubleUnaryOperator operator) {
			Objects.requireNonNull(operator);
			double state = identity;
			for(Node entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				state = operator.applyAsDouble(state, entry.key);
			return state;
		}
		
		@Override
		public double reduce(DoubleDoubleUnaryOperator operator) {
			Objects.requireNonNull(operator);
			double state = 0D;
			boolean empty = true;
			for(Node entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry)) {
				if(empty) {
					empty = false;
					state = entry.key;
					continue;
				}
				state = operator.applyAsDouble(state, entry.key);
			}
			return state;
		}
		
		@Override
		public double findFirst(Double2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(Node entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				if(filter.get(entry.key)) return entry.key;
			return 0D;
		}
		
		@Override
		public int count(Double2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			int result = 0;
			for(Node entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				if(filter.get(entry.key)) result++;
			return result;
		}
	}
	
	static class AscendingNaivgableSubMap extends NavigableSubMap
	{
		AscendingNaivgableSubMap(Double2LongAVLTreeMap map, boolean fromStart, double lo, boolean loInclusive, boolean toEnd, double hi, boolean hiInclusive) {
			super(map, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
		}
		
		@Override
		public Double2LongNavigableMap descendingMap() {
			if(inverse == null) inverse = new DescendingNaivgableSubMap(map, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
			return inverse;
		}
		
		@Override
		public ObjectSet<Double2LongMap.Entry> double2LongEntrySet() {
			if(entrySet == null) entrySet = new AscendingSubEntrySet();
			return entrySet;
		}
		
		@Override
		public DoubleNavigableSet navigableKeySet() {
			if(keySet == null) keySet = new KeySet(this);
			return keySet;
		}
		
		@Override
		public Double2LongNavigableMap subMap(double fromKey, boolean fromInclusive, double toKey, boolean toInclusive) {
			if (!inRange(fromKey, fromInclusive)) throw new IllegalArgumentException("fromKey out of range");
			if (!inRange(toKey, toInclusive)) throw new IllegalArgumentException("toKey out of range");
			return new AscendingNaivgableSubMap(map, false, fromKey, fromInclusive, false, toKey, toInclusive);
		}
		
		@Override
		public Double2LongNavigableMap headMap(double toKey, boolean inclusive) {
			if (!inRange(toKey, inclusive)) throw new IllegalArgumentException("toKey out of range");
			return new AscendingNaivgableSubMap(map, fromStart, lo, loInclusive, false, toKey, inclusive);
		}
		
		@Override
		public Double2LongNavigableMap tailMap(double fromKey, boolean inclusive) {
			if (!inRange(fromKey, inclusive)) throw new IllegalArgumentException("fromKey out of range");
			return new AscendingNaivgableSubMap(map, false, fromKey, inclusive, toEnd, hi, hiInclusive);
		}
		
		@Override
		protected Node subLowest() { return absLowest(); }
		@Override
		protected Node subHighest() { return absHighest(); }
		@Override
		protected Node subCeiling(double key) { return absCeiling(key); }
		@Override
		protected Node subHigher(double key) { return absHigher(key); }
		@Override
		protected Node subFloor(double key) { return absFloor(key); }
		@Override
		protected Node subLower(double key) { return absLower(key); }
		
		@Override
		protected DoubleBidirectionalIterator keyIterator() {
			return new AcsendingSubKeyIterator(absLowest(), absHighFence(), absLowFence()); 
		}
		@Override
		protected DoubleBidirectionalIterator keyIterator(double element) {
			return new AcsendingSubKeyIterator(absLower(element), absHighFence(), absLowFence()); 
		}
		
		@Override
		protected LongBidirectionalIterator valueIterator() {
			return new AcsendingSubValueIterator(absLowest(), absHighFence(), absLowFence()); 
		}
		
		@Override
		protected DoubleBidirectionalIterator descendingKeyIterator() {
			return new DecsendingSubKeyIterator(absHighest(), absLowFence(), absHighFence()); 
		}
		
		class AscendingSubEntrySet extends SubEntrySet {
			@Override
			public ObjectIterator<Double2LongMap.Entry> iterator() {
				return new AcsendingSubEntryIterator(absLowest(), absHighFence(), absLowFence());
			}
		}
	}
	
	static class DescendingNaivgableSubMap extends NavigableSubMap
	{
		DoubleComparator comparator;
		DescendingNaivgableSubMap(Double2LongAVLTreeMap map, boolean fromStart, double lo, boolean loInclusive, boolean toEnd, double hi, boolean hiInclusive) {
			super(map, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
			comparator = map.comparator() == null ? DoubleComparator.of(Collections.reverseOrder()) : map.comparator().reversed();
		}
		
		@Override
		public DoubleComparator comparator() { return comparator; }
		
		@Override
		public Double2LongNavigableMap descendingMap() {
			if(inverse == null) inverse = new AscendingNaivgableSubMap(map, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
			return inverse;
		}

		@Override
		public DoubleNavigableSet navigableKeySet() {
			if(keySet == null) keySet = new KeySet(this);
			return keySet;
		}
		
		@Override
		public Double2LongNavigableMap subMap(double fromKey, boolean fromInclusive, double toKey, boolean toInclusive) {
			if (!inRange(fromKey, fromInclusive)) throw new IllegalArgumentException("fromKey out of range");
			if (!inRange(toKey, toInclusive)) throw new IllegalArgumentException("toKey out of range");
			return new DescendingNaivgableSubMap(map, false, toKey, toInclusive, false, fromKey, fromInclusive);
		}
		
		@Override
		public Double2LongNavigableMap headMap(double toKey, boolean inclusive) {
			if (!inRange(toKey, inclusive)) throw new IllegalArgumentException("toKey out of range");
			return new DescendingNaivgableSubMap(map, false, toKey, inclusive, toEnd, hi, hiInclusive);
		}
		
		@Override
		public Double2LongNavigableMap tailMap(double fromKey, boolean inclusive) {
			if (!inRange(fromKey, inclusive)) throw new IllegalArgumentException("fromKey out of range");
			return new DescendingNaivgableSubMap(map, fromStart, lo, loInclusive, false, fromKey, inclusive);
		}
		
		@Override
		public ObjectSet<Double2LongMap.Entry> double2LongEntrySet() {
			if(entrySet == null) entrySet = new DescendingSubEntrySet();
			return entrySet;
		}
		
		@Override
		protected Node subLowest() { return absHighest(); }
		@Override
		protected Node subHighest() { return absLowest(); }
		@Override
		protected Node subCeiling(double key) { return absFloor(key); }
		@Override
		protected Node subHigher(double key) { return absLower(key); }
		@Override
		protected Node subFloor(double key) { return absCeiling(key); }
		@Override
		protected Node subLower(double key) { return absHigher(key); }
		@Override
		protected Node next(Node entry) { return entry.previous(); }
		@Override
		protected Node previous(Node entry) { return entry.next(); }
		
		@Override
		protected DoubleBidirectionalIterator keyIterator() {
			return new DecsendingSubKeyIterator(absHighest(), absLowFence(), absHighFence()); 
		}
		
		@Override
		protected DoubleBidirectionalIterator keyIterator(double element) {
			return new DecsendingSubKeyIterator(absHigher(element), absLowFence(), absHighFence()); 
		}
		
		@Override
		protected LongBidirectionalIterator valueIterator() {
			return new DecsendingSubValueIterator(absHighest(), absLowFence(), absHighFence()); 
		}
		
		@Override
		protected DoubleBidirectionalIterator descendingKeyIterator() {
			return new AcsendingSubKeyIterator(absLowest(), absHighFence(), absLowFence()); 
		}
		
		class DescendingSubEntrySet extends SubEntrySet {
			@Override
			public ObjectIterator<Double2LongMap.Entry> iterator() {
				return new DecsendingSubEntryIterator(absHighest(), absLowFence(), absHighFence());
			}
		}
	}
	
	static abstract class NavigableSubMap extends AbstractDouble2LongMap implements Double2LongNavigableMap
	{
		final Double2LongAVLTreeMap map;
		final double lo, hi;
		final boolean fromStart, toEnd;
		final boolean loInclusive, hiInclusive;
		
		Double2LongNavigableMap inverse;
		DoubleNavigableSet keySet;
		ObjectSet<Double2LongMap.Entry> entrySet;
		LongCollection values;
		
		NavigableSubMap(Double2LongAVLTreeMap map, boolean fromStart, double lo, boolean loInclusive, boolean toEnd, double hi, boolean hiInclusive) {
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
		public void setDefaultMaxValue(double e) { map.setDefaultMaxValue(e); }
		@Override
		public double getDefaultMaxValue() { return map.getDefaultMaxValue(); }
		@Override
		public void setDefaultMinValue(double e) { map.setDefaultMinValue(e); }
		@Override
		public double getDefaultMinValue() { return map.getDefaultMinValue(); }
		protected boolean isNullComparator() { return map.comparator() == null; }
		
		@Override
		public AbstractDouble2LongMap setDefaultReturnValue(long v) { 
			map.setDefaultReturnValue(v);
			return this;
		}
		
		@Override
		public long getDefaultReturnValue() { return map.getDefaultReturnValue(); }
		
		@Override
		public LongCollection values() {
			if(values == null) values = new SubMapValues();
			return values;
		}
		
		@Override
		public DoubleNavigableSet descendingKeySet() {
			return descendingMap().navigableKeySet();
		}
		
		@Override
		public DoubleSet keySet() {
			return navigableKeySet();
		}
		
		protected abstract Node subLowest();
		protected abstract Node subHighest();
		protected abstract Node subCeiling(double key);
		protected abstract Node subHigher(double key);
		protected abstract Node subFloor(double key);
		protected abstract Node subLower(double key);
		protected abstract DoubleBidirectionalIterator keyIterator();
		protected abstract DoubleBidirectionalIterator keyIterator(double element);
		protected abstract LongBidirectionalIterator valueIterator();
		protected abstract DoubleBidirectionalIterator descendingKeyIterator();
		protected double lowKeyOrNull(Node entry) { return entry == null ? getDefaultMinValue() : entry.key; }
		protected double highKeyOrNull(Node entry) { return entry == null ? getDefaultMaxValue() : entry.key; }
		protected Node next(Node entry) { return entry.next(); }
		protected Node previous(Node entry) { return entry.previous(); }
		
		protected boolean tooLow(double key) {
			if (!fromStart) {
				int c = map.compare(key, lo);
				if (c < 0 || (c == 0 && !loInclusive)) return true;
			}
			return false;
		}
		
		protected boolean tooHigh(double key) {
			if (!toEnd) {
				int c = map.compare(key, hi);
				if (c > 0 || (c == 0 && !hiInclusive)) return true;
			}
			return false;
		}
		protected boolean inRange(double key) { return !tooLow(key) && !tooHigh(key); }
		protected boolean inClosedRange(double key) { return (fromStart || map.compare(key, lo) >= 0) && (toEnd || map.compare(hi, key) >= 0); }
		protected boolean inRange(double key, boolean inclusive) { return inclusive ? inRange(key) : inClosedRange(key); }
		
		protected Node absLowest() {
			Node e = (fromStart ?  map.first : (loInclusive ? map.findCeilingNode(lo) : map.findHigherNode(lo)));
			return (e == null || tooHigh(e.key)) ? null : e;
		}
		
		protected Node absHighest() {
			Node e = (toEnd ?  map.last : (hiInclusive ?  map.findFloorNode(hi) : map.findLowerNode(hi)));
			return (e == null || tooLow(e.key)) ? null : e;
		}
		
		protected Node absCeiling(double key) {
			if (tooLow(key)) return absLowest();
			Node e = map.findCeilingNode(key);
			return (e == null || tooHigh(e.key)) ? null : e;
		}
		
		protected Node absHigher(double key) {
			if (tooLow(key)) return absLowest();
			Node e = map.findHigherNode(key);
			return (e == null || tooHigh(e.key)) ? null : e;
		}
		
		protected Node absFloor(double key) {
			if (tooHigh(key)) return absHighest();
			Node e = map.findFloorNode(key);
			return (e == null || tooLow(e.key)) ? null : e;
		}
		
		protected Node absLower(double key) {
			if (tooHigh(key)) return absHighest();
			Node e = map.findLowerNode(key);
			return (e == null || tooLow(e.key)) ? null : e;
		}
		
		protected Node absHighFence() { return (toEnd ? null : (hiInclusive ? map.findHigherNode(hi) : map.findCeilingNode(hi))); }
		protected Node absLowFence() { return (fromStart ? null : (loInclusive ?  map.findLowerNode(lo) : map.findFloorNode(lo))); }
		
		@Override
		public DoubleComparator comparator() { return map.comparator(); }
		
		@Override
		public double pollFirstDoubleKey() {
			Node entry = subLowest();
			if(entry != null) {
				double result = entry.key;
				map.removeNode(entry);
				return result;
			}
			return getDefaultMinValue();
		}
		
		@Override
		public double pollLastDoubleKey() {
			Node entry = subHighest();
			if(entry != null) {
				double result = entry.key;
				map.removeNode(entry);
				return result;
			}
			return getDefaultMaxValue();
		}
		
		@Override
		public long firstLongValue() {
			Node entry = subLowest();
			return entry == null ? map.getDefaultReturnValue() : entry.value;
		}
		
		@Override
		public long lastLongValue() {
			Node entry = subHighest();
			return entry == null ? map.getDefaultReturnValue() : entry.value;
		}
		
		@Override
		public double firstDoubleKey() {
			Node entry = subLowest();
			if(entry == null) throw new NoSuchElementException();
			return entry.key;
		}
		
		@Override
		public double lastDoubleKey() {
			Node entry = subHighest();
			if(entry == null) throw new NoSuchElementException();
			return entry.key;
		}
		
		@Override
		public long put(double key, long value) {
			if (!inRange(key)) throw new IllegalArgumentException("key out of range");
			return map.put(key, value);
		}
		
		@Override
		public long putIfAbsent(double key, long value) {
			if (!inRange(key)) throw new IllegalArgumentException("key out of range");
			return map.putIfAbsent(key, value);
		}
		
		@Override
		public long addTo(double key, long value) {
			if(!inRange(key)) throw new IllegalArgumentException("key out of range");
			return map.addTo(key, value);
		}
		
		@Override
		public long subFrom(double key, long value) {
			if(!inRange(key)) throw new IllegalArgumentException("key out of range");
			return map.subFrom(key, value);
		}
		
		@Override
		public boolean containsKey(double key) { return inRange(key) && map.containsKey(key); }
		
		@Override
		public long computeLongIfPresent(double key, DoubleLongUnaryOperator mappingFunction) {
			Objects.requireNonNull(mappingFunction);
			if(!inRange(key)) return getDefaultReturnValue();
			Node entry = map.findNode(key);
			if(entry == null || entry.value == getDefaultReturnValue()) return getDefaultReturnValue();
			long newValue = mappingFunction.apply(key, entry.value);
			if(newValue == getDefaultReturnValue()) {
				map.removeNode(entry);
				return newValue;
			}
			entry.value = newValue;
			return newValue;
		}
		
		@Override
		public long remove(double key) {
			return inRange(key) ? map.remove(key) : getDefaultReturnValue();
		}
		
		@Override
		public long removeOrDefault(double key, long defaultValue) {
			return inRange(key) ? map.removeOrDefault(key, defaultValue) : defaultValue;
		}
		
		@Override
		public boolean remove(double key, long value) {
			return inRange(key) && map.remove(key, value);
		}
		
		
		@Override
		public long get(double key) {
			return inRange(key) ? map.get(key) : getDefaultReturnValue();
		}
		
		@Override
		public long getOrDefault(double key, long defaultValue) {
			return inRange(key) ? map.getOrDefault(key, defaultValue) : getDefaultReturnValue();
		}
		
		
		@Override
		public double lowerKey(double key) { return lowKeyOrNull(subLower(key)); }
		@Override
		public double floorKey(double key) { return lowKeyOrNull(subFloor(key)); }
		@Override
		public double ceilingKey(double key) { return highKeyOrNull(subCeiling(key)); }
		@Override
		public double higherKey(double key) { return highKeyOrNull(subHigher(key)); }
		@Override
		public Double2LongMap.Entry lowerEntry(double key) { return subLower(key); }
		@Override
		public Double2LongMap.Entry floorEntry(double key) { return subFloor(key); }
		@Override
		public Double2LongMap.Entry ceilingEntry(double key) { return subCeiling(key); }
		@Override
		public Double2LongMap.Entry higherEntry(double key) { return subHigher(key); }
		
		@Override
		public boolean isEmpty() {
			if(fromStart && toEnd) return map.isEmpty();
			Node n = absLowest();
			return n == null || tooHigh(n.key);
		}
		
		@Override
		public int size() { return fromStart && toEnd ? map.size() : entrySet().size(); }
		
		@Override
		public Double2LongNavigableMap copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public Double2LongMap.Entry firstEntry() {
			Node entry = subLowest();
			return entry == null ? null : entry.export();
		}

		@Override
		public Double2LongMap.Entry lastEntry() {
			Node entry = subHighest();
			return entry == null ? null : entry.export();
		}

		@Override
		public Double2LongMap.Entry pollFirstEntry() {
			Node entry = subLowest();
			if(entry != null) {
				Double2LongMap.Entry result = entry.export();
				map.removeNode(entry);
				return result;
			}
			return null;
		}

		@Override
		public Double2LongMap.Entry pollLastEntry() {
			Node entry = subHighest();
			if(entry != null) {
				Double2LongMap.Entry result = entry.export();
				map.removeNode(entry);
				return result;
			}
			return null;
		}
		
		abstract class SubEntrySet extends AbstractObjectSet<Double2LongMap.Entry> {
			@Override
			public int size() {
				if (fromStart && toEnd) return map.size();
				int size = 0;
				for(ObjectIterator<Double2LongMap.Entry> iter = iterator();iter.hasNext();iter.next(),size++);
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
				if(o instanceof Double2LongMap.Entry)
				{
					Double2LongMap.Entry entry = (Double2LongMap.Entry) o;
					double key = entry.getDoubleKey();
					if (!inRange(key)) return false;
					Node node = map.findNode(key);
					return node != null && entry.getLongValue() == node.getLongValue();
				}
				Map.Entry<?,?> entry = (Map.Entry<?,?>) o;
				if(entry.getKey() == null && isNullComparator()) return false;
				Double key = (Double)entry.getKey();
				if (!inRange(key)) return false;
				Node node = map.findNode(key);
				return node != null && Objects.equals(entry.getValue(), Long.valueOf(node.getLongValue()));
			}
			
			@Override
			public boolean remove(Object o) {
				if (!(o instanceof Map.Entry)) return false;
				if(o instanceof Double2LongMap.Entry)
				{
					Double2LongMap.Entry entry = (Double2LongMap.Entry) o;
					double key = entry.getDoubleKey();
					if (!inRange(key)) return false;
					Node node = map.findNode(key);
					if (node != null && node.getLongValue() == entry.getLongValue()) {
						map.removeNode(node);
						return true;
					}
					return false;
				}
				Map.Entry<?,?> entry = (Map.Entry<?,?>) o;
				Double key = (Double)entry.getKey();
				if (!inRange(key)) return false;
				Node node = map.findNode(key);
				if (node != null && Objects.equals(node.getValue(), entry.getValue())) {
					map.removeNode(node);
					return true;
				}
				return false;
			}
			
			@Override
			public void forEach(Consumer<? super Double2LongMap.Entry> action) {
				Objects.requireNonNull(action);
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					action.accept(new BasicEntry(entry.key, entry.value));
			}
			
			@Override
			public <E> void forEach(E input, ObjectObjectConsumer<E, Double2LongMap.Entry> action) {
				Objects.requireNonNull(action);
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					action.accept(input, new BasicEntry(entry.key, entry.value));
			}
			
			@Override
			public boolean matchesAny(Object2BooleanFunction<Double2LongMap.Entry> filter) {
				Objects.requireNonNull(filter);
				if(size() <= 0) return false;
				BasicEntry subEntry = new BasicEntry();
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					subEntry.set(entry.key, entry.value);
					if(filter.getBoolean(subEntry)) return true;
				}
				return false;
			}
			
			@Override
			public boolean matchesNone(Object2BooleanFunction<Double2LongMap.Entry> filter) {
				Objects.requireNonNull(filter);
				if(size() <= 0) return true;
				BasicEntry subEntry = new BasicEntry();
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					subEntry.set(entry.key, entry.value);
					if(filter.getBoolean(subEntry)) return false;
				}
				return true;
			}
			
			@Override
			public boolean matchesAll(Object2BooleanFunction<Double2LongMap.Entry> filter) {
				Objects.requireNonNull(filter);
				if(size() <= 0) return true;
				BasicEntry subEntry = new BasicEntry();
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					subEntry.set(entry.key, entry.value);
					if(!filter.getBoolean(subEntry)) return false;
				}
				return true;
			}
			
			@Override
			public <E> E reduce(E identity, BiFunction<E, Double2LongMap.Entry, E> operator) {
				Objects.requireNonNull(operator);
				E state = identity;
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					state = operator.apply(state, new BasicEntry(entry.key, entry.value));
				}
				return state;
			}
			
			@Override
			public Double2LongMap.Entry reduce(ObjectObjectUnaryOperator<Double2LongMap.Entry, Double2LongMap.Entry> operator) {
				Objects.requireNonNull(operator);
				Double2LongMap.Entry state = null;
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
			public Double2LongMap.Entry findFirst(Object2BooleanFunction<Double2LongMap.Entry> filter) {
				Objects.requireNonNull(filter);
				if(size() <= 0) return null;
				BasicEntry subEntry = new BasicEntry();
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					subEntry.set(entry.key, entry.value);
					if(filter.getBoolean(subEntry)) return subEntry;
				}
				return null;
			}
			
			@Override
			public int count(Object2BooleanFunction<Double2LongMap.Entry> filter) {
				Objects.requireNonNull(filter);
				if(size() <= 0) return 0;
				int result = 0;
				BasicEntry subEntry = new BasicEntry();
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					subEntry.set(entry.key, entry.value);
					if(filter.getBoolean(subEntry)) result++;
				}
				return result;
			}
		}
		
		final class SubMapValues extends AbstractLongCollection {
			@Override
			public boolean add(long o) { throw new UnsupportedOperationException(); }
			
			@Override
			public boolean contains(long e) {
				return containsValue(e);
			}
			
			@Override
			public LongIterator iterator() { return valueIterator(); }
			
			@Override
			public int size() {
				return NavigableSubMap.this.size();
			}
			
			@Override
			public void clear() {
				NavigableSubMap.this.clear();
			}
			
			@Override
			public void forEach(LongConsumer action) {
				Objects.requireNonNull(action);
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					action.accept(entry.value);
			}
			
			@Override
			public <E> void forEach(E input, ObjectLongConsumer<E> action) {
				Objects.requireNonNull(action);
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					action.accept(input, entry.value);
			}
			
			@Override
			public boolean matchesAny(Long2BooleanFunction filter) {
				Objects.requireNonNull(filter);
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					if(filter.get(entry.value)) return true;
				return false;
			}
			
			@Override
			public boolean matchesNone(Long2BooleanFunction filter) {
				Objects.requireNonNull(filter);
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					if(filter.get(entry.value)) return false;
				return true;
			}
			
			@Override
			public boolean matchesAll(Long2BooleanFunction filter) {
				Objects.requireNonNull(filter);
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					if(!filter.get(entry.value)) return false;
				return true;
			}
			
			@Override
			public long reduce(long identity, LongLongUnaryOperator operator) {
				Objects.requireNonNull(operator);
				long state = identity;
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					state = operator.applyAsLong(state, entry.value);
				return state;
			}
			
			@Override
			public long reduce(LongLongUnaryOperator operator) {
				Objects.requireNonNull(operator);
				long state = 0L;
				boolean empty = true;
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					if(empty) {
						empty = false;
						state = entry.value;
						continue;
					}
					state = operator.applyAsLong(state, entry.value);
				}
				return state;
			}
			
			@Override
			public long findFirst(Long2BooleanFunction filter) {
				Objects.requireNonNull(filter);
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					if(filter.get(entry.value)) return entry.value;
				return 0L;
			}
			
			@Override
			public int count(Long2BooleanFunction filter) {
				Objects.requireNonNull(filter);
				int result = 0;
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					if(filter.get(entry.value)) result++;
				return result;
			}
		}
		
		class DecsendingSubEntryIterator extends SubMapEntryIterator implements ObjectBidirectionalIterator<Double2LongMap.Entry>
		{
			public DecsendingSubEntryIterator(Node first, Node forwardFence, Node backwardFence) {
				super(first, forwardFence, backwardFence);
			}
			
			@Override
			public Double2LongMap.Entry previous() {
				if(!hasPrevious()) throw new NoSuchElementException();
				return nextEntry();
			}

			@Override
			public Double2LongMap.Entry next() {
				if(!hasNext()) throw new NoSuchElementException();
				return previousEntry();
			}
		}
		
		class AcsendingSubEntryIterator extends SubMapEntryIterator implements ObjectBidirectionalIterator<Double2LongMap.Entry>
		{
			public AcsendingSubEntryIterator(Node first, Node forwardFence, Node backwardFence) {
				super(first, forwardFence, backwardFence);
			}

			@Override
			public Double2LongMap.Entry previous() {
				if(!hasPrevious()) throw new NoSuchElementException();
				return previousEntry();
			}

			@Override
			public Double2LongMap.Entry next() {
				if(!hasNext()) throw new NoSuchElementException();
				return nextEntry();
			}
		}
		
		class DecsendingSubKeyIterator extends SubMapEntryIterator implements DoubleBidirectionalIterator
		{
			public DecsendingSubKeyIterator(Node first, Node forwardFence, Node backwardFence) {
				super(first, forwardFence, backwardFence);
			}
			
			@Override
			public double previousDouble() {
				if(!hasPrevious()) throw new NoSuchElementException();
				return nextEntry().key;
			}

			@Override
			public double nextDouble() {
				if(!hasNext()) throw new NoSuchElementException();
				return previousEntry().key;
			}
		}
		
		class AcsendingSubKeyIterator extends SubMapEntryIterator implements DoubleBidirectionalIterator
		{
			public AcsendingSubKeyIterator(Node first, Node forwardFence, Node backwardFence) {
				super(first, forwardFence, backwardFence);
			}

			@Override
			public double previousDouble() {
				if(!hasPrevious()) throw new NoSuchElementException();
				return previousEntry().key;
			}

			@Override
			public double nextDouble() {
				if(!hasNext()) throw new NoSuchElementException();
				return nextEntry().key;
			}
		}
		
		class AcsendingSubValueIterator extends SubMapEntryIterator implements LongBidirectionalIterator
		{
			public AcsendingSubValueIterator(Node first, Node forwardFence, Node backwardFence) {
				super(first, forwardFence, backwardFence);
			}

			@Override
			public long previousLong() {
				if(!hasPrevious()) throw new NoSuchElementException();
				return previousEntry().value;
			}

			@Override
			public long nextLong() {
				if(!hasNext()) throw new NoSuchElementException();
				return nextEntry().value;
			}
		}
		
		class DecsendingSubValueIterator extends SubMapEntryIterator implements LongBidirectionalIterator
		{
			public DecsendingSubValueIterator(Node first, Node forwardFence, Node backwardFence) {
				super(first, forwardFence, backwardFence);
			}
			
			@Override
			public long previousLong() {
				if(!hasPrevious()) throw new NoSuchElementException();
				return nextEntry().value;
			}

			@Override
			public long nextLong() {
				if(!hasNext()) throw new NoSuchElementException();
				return previousEntry().value;
			}
		}
		
		abstract class SubMapEntryIterator
		{
			boolean wasForward;
			Node lastReturned;
			Node next;
			boolean unboundForwardFence;
			boolean unboundBackwardFence;
			double forwardFence;
			double backwardFence;
			
			public SubMapEntryIterator(Node first, Node forwardFence, Node backwardFence)
			{
				next = first;
				this.forwardFence = forwardFence == null ? 0D : forwardFence.key;
				this.backwardFence = backwardFence == null ? 0D : backwardFence.key;
				unboundForwardFence = forwardFence == null;
				unboundBackwardFence = backwardFence == null;
			}
			
			public boolean hasNext() {
                return next != null && (unboundForwardFence || next.key != forwardFence);
			}
			
			protected Node nextEntry() {
				lastReturned = next;
				Node result = next;
				next = next.next();
				wasForward = true;
				return result;
			}
			
			public boolean hasPrevious() {
                return next != null && (unboundBackwardFence || next.key != backwardFence);
			}
			
			protected Node previousEntry() {
				lastReturned = next;
				Node result = next;
				next = next.previous();
				wasForward = false;
				return result;
			}
			
			public void remove() {
				if(lastReturned == null) throw new IllegalStateException();
				if(wasForward && lastReturned.needsSuccessor()) next = lastReturned;
				map.removeNode(lastReturned);
				lastReturned = null;
			}
		}
	}
	
	class Values extends AbstractLongCollection
	{
		@Override
		public LongIterator iterator() {
			return new AscendingValueIterator(first);
		}
		
		@Override
		public boolean add(long e) { throw new UnsupportedOperationException(); }
		
		@Override
		public void clear() {
			Double2LongAVLTreeMap.this.clear();
		}
		
		@Override
		public int size() {
			return Double2LongAVLTreeMap.this.size;
		}
		
		@Override
		public boolean contains(long e) {
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
		public void forEach(LongConsumer action) {
			Objects.requireNonNull(action);
			for(Node entry = first;entry != null;entry = entry.next())
				action.accept(entry.value);
		}
		
		@Override
		public <E> void forEach(E input, ObjectLongConsumer<E> action) {
			Objects.requireNonNull(action);
			for(Node entry = first;entry != null;entry = entry.next())
				action.accept(input, entry.value);
		}
		
		@Override
		public boolean matchesAny(Long2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(Node entry = first;entry != null;entry = entry.next())
				if(filter.get(entry.value)) return true;
			return false;
		}
		
		@Override
		public boolean matchesNone(Long2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(Node entry = first;entry != null;entry = entry.next())
				if(filter.get(entry.value)) return false;
			return true;
		}
		
		@Override
		public boolean matchesAll(Long2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(Node entry = first;entry != null;entry = entry.next())
				if(!filter.get(entry.value)) return false;
			return true;
		}
		
		@Override
		public long reduce(long identity, LongLongUnaryOperator operator) {
			Objects.requireNonNull(operator);
			long state = identity;
			for(Node entry = first;entry != null;entry = entry.next())
				state = operator.applyAsLong(state, entry.value);
			return state;
		}
		
		@Override
		public long reduce(LongLongUnaryOperator operator) {
			Objects.requireNonNull(operator);
			long state = 0L;
			boolean empty = true;
			for(Node entry = first;entry != null;entry = entry.next()) {
				if(empty) {
					empty = false;
					state = entry.value;
					continue;
				}
				state = operator.applyAsLong(state, entry.value);
			}
			return state;
		}
		
		@Override
		public long findFirst(Long2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(Node entry = first;entry != null;entry = entry.next())
				if(filter.get(entry.value)) return entry.value;
			return 0L;
		}
		
		@Override
		public int count(Long2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			int result = 0;
			for(Node entry = first;entry != null;entry = entry.next())
				if(filter.get(entry.value)) result++;
			return result;
		}
	}
	
	class EntrySet extends AbstractObjectSet<Double2LongMap.Entry> {
		
		@Override
		public ObjectIterator<Double2LongMap.Entry> iterator() {
			return new AscendingMapEntryIterator(first);
		}
		
		@Override
		public void clear() {
			Double2LongAVLTreeMap.this.clear();
		}
		
		@Override
		public int size() {
			return Double2LongAVLTreeMap.this.size;
		}
		
		@Override
		public boolean contains(Object o) {
			if (!(o instanceof Map.Entry)) return false;
			if(o instanceof Double2LongMap.Entry)
			{
				Double2LongMap.Entry entry = (Double2LongMap.Entry) o;
				double key = entry.getDoubleKey();
				Node node = findNode(key);
				return node != null && entry.getLongValue() == node.getLongValue();
			}
			Map.Entry<?,?> entry = (Map.Entry<?,?>) o;
			if(entry.getKey() == null && comparator() == null) return false;
			if(!(entry.getKey() instanceof Double)) return false;
			Double key = (Double)entry.getKey();
			Node node = findNode(key);
			return node != null && Objects.equals(entry.getValue(), Long.valueOf(node.getLongValue()));
		}
		
		@Override
		public boolean remove(Object o) {
			if (!(o instanceof Map.Entry)) return false;
			if(o instanceof Double2LongMap.Entry)
			{
				Double2LongMap.Entry entry = (Double2LongMap.Entry) o;
				double key = entry.getDoubleKey();
				Node node = findNode(key);
				if (node != null && entry.getLongValue() == node.getLongValue()) {
					removeNode(node);
					return true;
				}
				return false;
			}
			Map.Entry<?,?> entry = (Map.Entry<?,?>) o;
			Double key = (Double)entry.getKey();
			Node node = findNode(key);
			if (node != null && Objects.equals(entry.getValue(), Long.valueOf(node.getLongValue()))) {
				removeNode(node);
				return true;
			}
			return false;
		}
		
		@Override
		public void forEach(Consumer<? super Double2LongMap.Entry> action) {
			Objects.requireNonNull(action);
			for(Node entry = first;entry != null;entry = entry.next())
				action.accept(new BasicEntry(entry.key, entry.value));
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, Double2LongMap.Entry> action) {
			Objects.requireNonNull(action);
			for(Node entry = first;entry != null;entry = entry.next())
				action.accept(input, new BasicEntry(entry.key, entry.value));
		}
		
		@Override
		public boolean matchesAny(Object2BooleanFunction<Double2LongMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			BasicEntry subEntry = new BasicEntry();
			for(Node entry = first;entry != null;entry = entry.next()) {
				subEntry.set(entry.key, entry.value);
				if(filter.getBoolean(subEntry)) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Object2BooleanFunction<Double2LongMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			BasicEntry subEntry = new BasicEntry();
			for(Node entry = first;entry != null;entry = entry.next()) {
				subEntry.set(entry.key, entry.value);
				if(filter.getBoolean(subEntry)) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Object2BooleanFunction<Double2LongMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			BasicEntry subEntry = new BasicEntry();
			for(Node entry = first;entry != null;entry = entry.next()) {
				subEntry.set(entry.key, entry.value);
				if(!filter.getBoolean(subEntry)) return false;
			}
			return true;
		}
		
		@Override
		public <E> E reduce(E identity, BiFunction<E, Double2LongMap.Entry, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			for(Node entry = first;entry != null;entry = entry.next()) {
				state = operator.apply(state, new BasicEntry(entry.key, entry.value));
			}
			return state;
		}
		
		@Override
		public Double2LongMap.Entry reduce(ObjectObjectUnaryOperator<Double2LongMap.Entry, Double2LongMap.Entry> operator) {
			Objects.requireNonNull(operator);
			Double2LongMap.Entry state = null;
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
		public Double2LongMap.Entry findFirst(Object2BooleanFunction<Double2LongMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return null;
			BasicEntry subEntry = new BasicEntry();
			for(Node entry = first;entry != null;entry = entry.next()) {
				subEntry.set(entry.key, entry.value);
				if(filter.getBoolean(subEntry)) return subEntry;
			}
			return null;
		}
		
		@Override
		public int count(Object2BooleanFunction<Double2LongMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			int result = 0;
			BasicEntry subEntry = new BasicEntry();
			for(Node entry = first;entry != null;entry = entry.next()) {
				subEntry.set(entry.key, entry.value);
				if(filter.getBoolean(subEntry)) result++;
			}
			return result;
		}
	}
	
	class DescendingKeyIterator extends MapEntryIterator implements DoubleBidirectionalIterator
	{
		public DescendingKeyIterator(Node first) {
			super(first);
		}

		@Override
		public double previousDouble() {
			if(!hasPrevious()) throw new NoSuchElementException();
			return nextEntry().key;
		}
		
		@Override
		public double nextDouble() {
			if(!hasNext()) throw new NoSuchElementException();
			return previousEntry().key;
		}
	}
	
	class AscendingMapEntryIterator extends MapEntryIterator implements ObjectBidirectionalIterator<Double2LongMap.Entry>
	{
		public AscendingMapEntryIterator(Node first)
		{
			super(first);
		}

		@Override
		public Double2LongMap.Entry previous() {
			if(!hasPrevious()) throw new NoSuchElementException();
			return previousEntry();
		}

		@Override
		public Double2LongMap.Entry next() {
			if(!hasNext()) throw new NoSuchElementException();
			return nextEntry();
		}
	}
	
	class AscendingValueIterator extends MapEntryIterator implements LongBidirectionalIterator
	{
		public AscendingValueIterator(Node first) {
			super(first);
		}

		@Override
		public long previousLong() {
			if(!hasPrevious()) throw new NoSuchElementException();
			return previousEntry().value;
		}

		@Override
		public long nextLong() {
			if(!hasNext()) throw new NoSuchElementException();
			return nextEntry().value;
		}
	}
	
	class AscendingKeyIterator extends MapEntryIterator implements DoubleBidirectionalIterator
	{
		public AscendingKeyIterator(Node first) {
			super(first);
		}

		@Override
		public double previousDouble() {
			if(!hasPrevious()) throw new NoSuchElementException();
			return previousEntry().key;
		}

		@Override
		public double nextDouble() {
			if(!hasNext()) throw new NoSuchElementException();
			return nextEntry().key;
		}
	}
	
	abstract class MapEntryIterator
	{
		boolean wasMoved = false;
		Node lastReturned;
		Node next;
		
		public MapEntryIterator(Node first)
		{
			next = first;
		}
		
		public boolean hasNext() {
            return next != null;
		}
		
		protected Node nextEntry() {
			lastReturned = next;
			Node result = next;
			next = next.next();
			wasMoved = true;
			return result;
		}
		
		public boolean hasPrevious() {
            return next != null;
		}
		
		protected Node previousEntry() {
			lastReturned = next;
			Node result = next;
			next = next.previous();
			wasMoved = false;
			return result;
		}
		
		public void remove() {
			if(lastReturned == null) throw new IllegalStateException();
			if(wasMoved && lastReturned.needsSuccessor()) next = lastReturned;
			removeNode(lastReturned);
			lastReturned = null;
		}
	}
	
	private static final class Node implements Double2LongMap.Entry
	{
		double key;
		long value;
		int state;
		Node parent;
		Node left;
		Node right;
		
		Node(double key, long value, Node parent) {
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
		public double getDoubleKey() {
			return key;
		}
		
		@Override
		public long getLongValue() {
			return value;
		}
		
		@Override
		public long setValue(long value) {
			long oldValue = this.value;
			this.value = value;
			return oldValue;
		}
		
		long addTo(long value) {
			long oldValue = this.value;
			this.value += value;
			return oldValue;
		}
		
		long subFrom(long value) {
			long oldValue = this.value;
			this.value -= value;
			return oldValue;
		}
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Map.Entry) {
				if(obj instanceof Double2LongMap.Entry) {
					Double2LongMap.Entry entry = (Double2LongMap.Entry)obj;
					return Double.doubleToLongBits(key) == Double.doubleToLongBits(entry.getDoubleKey()) && value == entry.getLongValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object otherKey = entry.getKey();
				if(otherKey == null) return false;
				Object otherValue = entry.getValue();
				return otherKey instanceof Double && otherValue instanceof Long && Double.doubleToLongBits(key) == Double.doubleToLongBits(((Double)otherKey).doubleValue()) && value == ((Long)otherValue).longValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Double.hashCode(key) ^ Long.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Double.toString(key) + "=" + Long.toString(value);
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