package speiger.src.collections.ints.maps.impl.tree;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.BiFunction;

import speiger.src.collections.ints.collections.IntBidirectionalIterator;
import speiger.src.collections.ints.functions.IntComparator;
import speiger.src.collections.ints.functions.IntConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectIntConsumer;
import speiger.src.collections.ints.functions.function.Int2BooleanFunction;
import speiger.src.collections.ints.functions.consumer.IntObjectConsumer;
import speiger.src.collections.ints.functions.function.Int2ObjectFunction;
import speiger.src.collections.ints.functions.function.IntObjectUnaryOperator;
import speiger.src.collections.ints.functions.function.IntIntUnaryOperator;
import speiger.src.collections.ints.maps.abstracts.AbstractInt2ObjectMap;
import speiger.src.collections.ints.maps.interfaces.Int2ObjectMap;
import speiger.src.collections.ints.maps.interfaces.Int2ObjectNavigableMap;
import speiger.src.collections.ints.sets.AbstractIntSet;
import speiger.src.collections.ints.sets.IntNavigableSet;
import speiger.src.collections.ints.sets.IntSet;
import speiger.src.collections.ints.sets.IntSortedSet;
import speiger.src.collections.ints.utils.maps.Int2ObjectMaps;
import speiger.src.collections.objects.collections.AbstractObjectCollection;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.functions.ObjectSupplier;
import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;
import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;

import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;
import speiger.src.collections.objects.functions.function.Object2BooleanFunction;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectSet;

/**
 * A Simple Type Specific RB TreeMap implementation that reduces boxing/unboxing.
 * It is using a bit more memory then <a href="https://github.com/vigna/fastutil">FastUtil</a>,
 * but it saves a lot of Performance on the Optimized removal and iteration logic.
 * Which makes the implementation actually useable and does not get outperformed by Javas default implementation.
 * @param <V> the type of elements maintained by this Collection
 */
public class Int2ObjectRBTreeMap<V> extends AbstractInt2ObjectMap<V> implements Int2ObjectNavigableMap<V>
{
	/** The center of the Tree */
	protected transient Node<V> tree;
	/** The Lowest possible Node */
	protected transient Node<V> first;
	/** The Highest possible Node */
	protected transient Node<V> last;
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
	protected ObjectCollection<V> values;
	/** EntrySet Cache */
	protected ObjectSet<Int2ObjectMap.Entry<V>> entrySet;
	
	/**
	 * Default Constructor
	 */
	public Int2ObjectRBTreeMap() {
	}
	
	/**
	 * Constructor that allows to define the sorter
	 * @param comp the function that decides how the tree is sorted, can be null
	 */
	public Int2ObjectRBTreeMap(IntComparator comp) {
		comparator = comp;
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Int2ObjectRBTreeMap(Integer[] keys, V[] values) {
		this(keys, values, null);
	}
	
	/**
	 * Helper constructor that has a custom sorter and allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param comp the function that decides how the tree is sorted, can be null
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Int2ObjectRBTreeMap(Integer[] keys, V[] values, IntComparator comp) {
		comparator = comp;
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i].intValue(), values[i]);
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Int2ObjectRBTreeMap(int[] keys, V[] values) {
		this(keys, values, null);
	}
	
	/**
	 * Helper constructor that has a custom sorter and allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param comp the function that decides how the tree is sorted, can be null
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Int2ObjectRBTreeMap(int[] keys, V[] values, IntComparator comp) {
		comparator = comp;
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i], values[i]);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 */
	public Int2ObjectRBTreeMap(Map<? extends Integer, ? extends V> map) {
		this(map, null);
	}
	
	/**
	 * A Helper constructor that has a custom sorter and allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param comp the function that decides how the tree is sorted, can be null
	 */
	public Int2ObjectRBTreeMap(Map<? extends Integer, ? extends V> map, IntComparator comp) {
		comparator = comp;
		putAll(map);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 */
	public Int2ObjectRBTreeMap(Int2ObjectMap<V> map) {
		this(map, null);
	}
	
	/**
	 * A Type Specific Helper function that has a custom sorter and allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param comp the function that decides how the tree is sorted, can be null
 	 */
	public Int2ObjectRBTreeMap(Int2ObjectMap<V> map, IntComparator comp) {
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
	public V put(int key, V value) {
		if(tree == null) {
			tree = first = last = new Node<>(key, value, null);
			size++;
			return getDefaultReturnValue();
		}
		int compare = 0;
		Node<V> parent = tree;
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
		Node<V> adding = new Node<>(key, value, parent);
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
	public V putIfAbsent(int key, V value) {
		if(tree == null) {
			tree = first = last = new Node<>(key, value, null);
			size++;
			return getDefaultReturnValue();
		}
		int compare = 0;
		Node<V> parent = tree;
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
		Node<V> adding = new Node<>(key, value, parent);
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
	public IntComparator comparator() { return comparator; }

	@Override
	public boolean containsKey(int key) {
		return findNode(key) != null;
	}
	
	@Override
	public V get(int key) {
		Node<V> node = findNode(key);
		return node == null ? getDefaultReturnValue() : node.value;
	}
	
	@Override
	public V getOrDefault(int key, V defaultValue) {
		Node<V> node = findNode(key);
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
	public Int2ObjectMap.Entry<V> firstEntry() {
		if(tree == null) return null;
		return first.export();
	}
	
	@Override
	public Int2ObjectMap.Entry<V> lastEntry() {
		if(tree == null) return null;
		return last.export();
	}
	
	@Override
	public Int2ObjectMap.Entry<V> pollFirstEntry() {
		if(tree == null) return null;
		BasicEntry<V> entry = first.export();
		removeNode(first);
		return entry;
	}
	
	@Override
	public Int2ObjectMap.Entry<V> pollLastEntry() {
		if(tree == null) return null;
		BasicEntry<V> entry = last.export();
		removeNode(last);
		return entry;
	}
	
	@Override
	public V firstValue() {
		if(tree == null) throw new NoSuchElementException();
		return first.value;
	}
	
	@Override
	public V lastValue() {
		if(tree == null) throw new NoSuchElementException();
		return last.value;
	}
	
	@Override
	public V remove(int key) {
		Node<V> entry = findNode(key);
		if(entry == null) return getDefaultReturnValue();
		V value = entry.value;
		removeNode(entry);
		return value;
	}
	
	@Override
	public V removeOrDefault(int key, V defaultValue) {
		Node<V> entry = findNode(key);
		if(entry == null) return defaultValue;
		V value = entry.value;
		removeNode(entry);
		return value;
	}
	
	@Override
	public boolean remove(int key, V value) {
		Node<V> entry = findNode(key);
		if(entry == null || entry.value != value) return false;
		removeNode(entry);
		return true;
	}
	
	@Override
	public boolean replace(int key, V oldValue, V newValue) {
		Node<V> entry = findNode(key);
		if(entry == null || entry.value != oldValue) return false;
		entry.value = newValue;
		return true;
	}
	
	@Override
	public V replace(int key, V value) {
		Node<V> entry = findNode(key);
		if(entry == null) return getDefaultReturnValue();
		V oldValue = entry.value;
		entry.value = value;
		return oldValue;
	}
	
	@Override
	public V compute(int key, IntObjectUnaryOperator<V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		Node<V> entry = findNode(key);
		if(entry == null) {
			V newValue = mappingFunction.apply(key, getDefaultReturnValue());
			if(Objects.equals(newValue, getDefaultReturnValue())) return newValue;
			put(key, newValue);
			return newValue;
		}
		V newValue = mappingFunction.apply(key, entry.value);
		if(Objects.equals(newValue, getDefaultReturnValue())) {
			removeNode(entry);
			return newValue;
		}
		entry.value = newValue;
		return newValue;
	}
	
	@Override
	public V computeIfAbsent(int key, Int2ObjectFunction<V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		Node<V> entry = findNode(key);
		if(entry == null) {
			V newValue = mappingFunction.get(key);
			if(Objects.equals(newValue, getDefaultReturnValue())) return newValue;
			put(key, newValue);
			return newValue;
		}
		if(Objects.equals(entry.value, getDefaultReturnValue())) {
			V newValue = mappingFunction.get(key);
			if(Objects.equals(newValue, getDefaultReturnValue())) return newValue;
			entry.value = newValue;
		}
		return entry.value;
	}
	
	@Override
	public V supplyIfAbsent(int key, ObjectSupplier<V> valueProvider) {
		Objects.requireNonNull(valueProvider);
		Node<V> entry = findNode(key);
		if(entry == null) {
			V newValue = valueProvider.get();
			if(Objects.equals(newValue, getDefaultReturnValue())) return newValue;
			put(key, newValue);
			return newValue;
		}
		if(Objects.equals(entry.value, getDefaultReturnValue())) {
			V newValue = valueProvider.get();
			if(Objects.equals(newValue, getDefaultReturnValue())) return newValue;
			entry.value = newValue;
		}
		return entry.value;
	}
	
	@Override
	public V computeIfPresent(int key, IntObjectUnaryOperator<V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		Node<V> entry = findNode(key);
		if(entry == null || Objects.equals(entry.value, getDefaultReturnValue())) return getDefaultReturnValue();
		V newValue = mappingFunction.apply(key, entry.value);
		if(Objects.equals(newValue, getDefaultReturnValue())) {
			removeNode(entry);
			return newValue;
		}
		entry.value = newValue;
		return newValue;
	}
	
	@Override
	public V merge(int key, V value, ObjectObjectUnaryOperator<V, V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		Node<V> entry = findNode(key);
		V newValue = entry == null || Objects.equals(entry.value, getDefaultReturnValue()) ? value : mappingFunction.apply(entry.value, value);
		if(Objects.equals(newValue, getDefaultReturnValue())) {
			if(entry != null)
				removeNode(entry);
		}
		else if(entry == null) put(key, newValue);
		else entry.value = newValue;
		return newValue;
	}
	
	@Override
	public void mergeAll(Int2ObjectMap<V> m, ObjectObjectUnaryOperator<V, V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Int2ObjectMap.Entry<V> entry : Int2ObjectMaps.fastIterable(m)) {
			int key = entry.getIntKey();
			Node<V> subEntry = findNode(key);
			V newValue = subEntry == null || Objects.equals(subEntry.value, getDefaultReturnValue()) ? entry.getValue() : mappingFunction.apply(subEntry.value, entry.getValue());
			if(Objects.equals(newValue, getDefaultReturnValue())) {
				if(subEntry != null)
					removeNode(subEntry);
			}
			else if(subEntry == null) put(key, newValue);
			else subEntry.value = newValue;
		}
	}
	
	@Override
	public void forEach(IntObjectConsumer<V> action) {
		for(Node<V> entry = first;entry != null;entry = entry.next())
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
	public Int2ObjectRBTreeMap<V> copy() {
		Int2ObjectRBTreeMap<V> set = new Int2ObjectRBTreeMap<>();
		set.size = size;
		if(tree != null) {
			set.tree = tree.copy();
			Node<V> lastFound = null;
			for(Node<V> entry = tree;entry != null;entry = entry.left) lastFound = entry;
			set.first = lastFound;
			lastFound = null;
			for(Node<V> entry = tree;entry != null;entry = entry.right) lastFound = entry;
			set.last = lastFound;
		}
		return set;
	}
	
	@Override
	public IntSortedSet keySet() {
		return navigableKeySet();
	}
	
	@Override
	public ObjectSet<Int2ObjectMap.Entry<V>> int2ObjectEntrySet() {
		if(entrySet == null) entrySet = new EntrySet();
		return entrySet;
	}
	
	@Override
	public ObjectCollection<V> values() {
		if(values == null) values = new Values();
		return values;
	}
	
	@Override
	public IntNavigableSet navigableKeySet() {
		if(keySet == null) keySet = new KeySet<>(this);
		return keySet;
	}
	
	@Override
	public Int2ObjectNavigableMap<V> descendingMap() {
		return new DescendingNaivgableSubMap<>(this, true, 0, true, true, 0, true);
	}
	
	@Override
	public IntNavigableSet descendingKeySet() {
		return descendingMap().navigableKeySet();
	}
	
	@Override
	public Int2ObjectNavigableMap<V> subMap(int fromKey, boolean fromInclusive, int toKey, boolean toInclusive) {
		return new AscendingNaivgableSubMap<>(this, false, fromKey, fromInclusive, false, toKey, toInclusive);
	}
	
	@Override
	public Int2ObjectNavigableMap<V> headMap(int toKey, boolean inclusive) {
		return new AscendingNaivgableSubMap<>(this, true, 0, true, false, toKey, inclusive);
	}
	
	@Override
	public Int2ObjectNavigableMap<V> tailMap(int fromKey, boolean inclusive) {
		return new AscendingNaivgableSubMap<>(this, false, fromKey, inclusive, true, 0, true);
	}
	
	@Override
	public int lowerKey(int e) {
		Node<V> node = findLowerNode(e);
		return node != null ? node.key : getDefaultMinValue();
	}

	@Override
	public int floorKey(int e) {
		Node<V> node = findFloorNode(e);
		return node != null ? node.key : getDefaultMinValue();
	}
	
	@Override
	public int higherKey(int e) {
		Node<V> node = findHigherNode(e);
		return node != null ? node.key : getDefaultMaxValue();
	}

	@Override
	public int ceilingKey(int e) {
		Node<V> node = findCeilingNode(e);
		return node != null ? node.key : getDefaultMaxValue();
	}
	
	@Override
	public Int2ObjectMap.Entry<V> lowerEntry(int key) {
		Node<V> node = findLowerNode(key);
		return node != null ? node.export() : null;
	}
	
	@Override
	public Int2ObjectMap.Entry<V> higherEntry(int key) {
		Node<V> node = findHigherNode(key);
		return node != null ? node.export() : null;
	}
	
	@Override
	public Int2ObjectMap.Entry<V> floorEntry(int key) {
		Node<V> node = findFloorNode(key);
		return node != null ? node.export() : null;
	}
	
	@Override
	public Int2ObjectMap.Entry<V> ceilingEntry(int key) {
		Node<V> node = findCeilingNode(key);
		return node != null ? node.export() : null;
	}
	
	protected Node<V> findLowerNode(int key) {
		Node<V> entry = tree;
		while(entry != null) {
			if(compare(key, entry.key) > 0) {
				if(entry.right != null) entry = entry.right;
				else return entry;
			}
			else {
				if(entry.left != null) entry = entry.left;
				else {
					Node<V> parent = entry.parent;
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
	
	protected Node<V> findFloorNode(int key) {
		Node<V> entry = tree;
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
					Node<V> parent = entry.parent;
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
	
	protected Node<V> findCeilingNode(int key) {
		Node<V> entry = tree;
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
					Node<V> parent = entry.parent;
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
	
	protected Node<V> findHigherNode(int key) {
		Node<V> entry = tree;
		while(entry != null) {
			if(compare(key, entry.key) < 0) {
				if(entry.left != null) entry = entry.left;
				else return entry;
			}
			else {
				if(entry.right != null) entry = entry.right;
				else {
					Node<V> parent = entry.parent;
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
	
	protected Node<V> findNode(int key) {
		Node<V> node = tree;
		int compare;
		while(node != null) {
			if((compare = compare(key, node.key)) == 0) return node;
			if(compare < 0) node = node.left;
			else node = node.right;
		}
		return null;
	}
	
	protected void removeNode(Node<V> entry) {
		size--;
		if(entry.needsSuccessor()) {
			Node<V> successor = entry.next();
			entry.key = successor.key;
			entry.value = successor.value;
			entry = successor;
		}
		Node<V> replacement = entry.left != null ? entry.left : entry.right;
		if(replacement != null) {
			if(entry.replace(replacement)) tree = replacement;
			if(entry == first) first = replacement;
			if(entry == last) last = entry.right != null ? entry.right : replacement;
			entry.left = entry.right = entry.parent = null;
			if(entry.isBlack()) fixAfterDeletion(replacement);
		}
		else if(entry.parent == null) tree = first = last = null;
		else {
			if(entry.isBlack())
				fixAfterDeletion(entry);
			entry.replace(null);
			if(entry.parent != null) {
				Node<V> parent = entry.parent;
				if(entry == first) first = parent.left != null ? parent.left : parent;
				if(entry == last) last = entry.right != null ? parent.right : parent;
			}
			entry.parent = null;
		}
	}
	
	protected void validate(int k) { compare(k, k); }
	protected int compare(int k, int v) { return comparator != null ? comparator.compare(k, v) : Integer.compare(k, v);}
	protected static <V> boolean isBlack(Node<V> p) { return p == null || p.isBlack(); }
	protected static <V> Node<V> parentOf(Node<V> p) { return (p == null ? null : p.parent); }
	protected static <V> void setBlack(Node<V> p, boolean c) { if(p != null) p.setBlack(c); }
	protected static <V> Node<V> leftOf(Node<V> p) { return p == null ? null : p.left; }
	protected static <V> Node<V> rightOf(Node<V> p) { return (p == null) ? null : p.right; }
	
	protected void rotateLeft(Node<V> entry) {
		if(entry != null) {
			Node<V> right = entry.right;
			entry.right = right.left;
			if(right.left != null) right.left.parent = entry;
			right.parent = entry.parent;
			if(entry.parent == null) tree = right;
			else if(entry.parent.left == entry) entry.parent.left = right;
			else entry.parent.right = right;
			right.left = entry;
			entry.parent = right;
		}
	}
	
	protected void rotateRight(Node<V> entry) {
		if(entry != null) {
			Node<V> left = entry.left;
			entry.left = left.right;
			if(left.right != null) left.right.parent = entry;
			left.parent = entry.parent;
			if(entry.parent == null) tree = left;
			else if(entry.parent.right == entry) entry.parent.right = left;
			else entry.parent.left = left;
			left.right = entry;
			entry.parent = left;
		}
	}
	
	protected void fixAfterInsertion(Node<V> entry) {
		entry.setBlack(false);
		while(entry != null && entry != tree && !entry.parent.isBlack()) {
			if(parentOf(entry) == leftOf(parentOf(parentOf(entry)))) {
				Node<V> y = rightOf(parentOf(parentOf(entry)));
				if(!isBlack(y)) {
					setBlack(parentOf(entry), true);
					setBlack(y, true);
					setBlack(parentOf(parentOf(entry)), false);
					entry = parentOf(parentOf(entry));
				}
				else {
					if(entry == rightOf(parentOf(entry))) {
						entry = parentOf(entry);
						rotateLeft(entry);
					}
					setBlack(parentOf(entry), true);
					setBlack(parentOf(parentOf(entry)), false);
					rotateRight(parentOf(parentOf(entry)));
				}
			}
			else {
				Node<V> y = leftOf(parentOf(parentOf(entry)));
				if(!isBlack(y)) {
					setBlack(parentOf(entry), true);
					setBlack(y, true);
					setBlack(parentOf(parentOf(entry)), false);
					entry = parentOf(parentOf(entry));
				}
				else {
					if(entry == leftOf(parentOf(entry))) {
						entry = parentOf(entry);
						rotateRight(entry);
					}
					setBlack(parentOf(entry), true);
					setBlack(parentOf(parentOf(entry)), false);
					rotateLeft(parentOf(parentOf(entry)));
				}
			}
		}
		tree.setBlack(true);
	}
	
	protected void fixAfterDeletion(Node<V> entry) {
		while(entry != tree && isBlack(entry)) {
			if(entry == leftOf(parentOf(entry))) {
				Node<V> sib = rightOf(parentOf(entry));
				if(!isBlack(sib)) {
					setBlack(sib, true);
					setBlack(parentOf(entry), false);
					rotateLeft(parentOf(entry));
					sib = rightOf(parentOf(entry));
				}
				if(isBlack(leftOf(sib)) && isBlack(rightOf(sib))) {
					setBlack(sib, false);
					entry = parentOf(entry);
				}
				else {
					if(isBlack(rightOf(sib))) {
						setBlack(leftOf(sib), true);
						setBlack(sib, false);
						rotateRight(sib);
						sib = rightOf(parentOf(entry));
					}
					setBlack(sib, isBlack(parentOf(entry)));
					setBlack(parentOf(entry), true);
					setBlack(rightOf(sib), true);
					rotateLeft(parentOf(entry));
					entry = tree;
				}
			}
			else {
				Node<V> sib = leftOf(parentOf(entry));
				if(!isBlack(sib)) {
					setBlack(sib, true);
					setBlack(parentOf(entry), false);
					rotateRight(parentOf(entry));
					sib = leftOf(parentOf(entry));
				}
				if(isBlack(rightOf(sib)) && isBlack(leftOf(sib))) {
					setBlack(sib, false);
					entry = parentOf(entry);
				}
				else {
					if(isBlack(leftOf(sib))) {
						setBlack(rightOf(sib), true);
						setBlack(sib, false);
						rotateLeft(sib);
						sib = leftOf(parentOf(entry));
					}
					setBlack(sib, isBlack(parentOf(entry)));
					setBlack(parentOf(entry), true);
					setBlack(leftOf(sib), true);
					rotateRight(parentOf(entry));
					entry = tree;
				}
			}
		}
		setBlack(entry, true);
	}
	
	static class KeySet<V> extends AbstractIntSet implements IntNavigableSet
	{
		Int2ObjectNavigableMap<V> map;

		public KeySet(Int2ObjectNavigableMap<V> map) {
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
			Int2ObjectMap.Entry<V> node = map.lowerEntry(e.intValue());
			return node != null ? node.getKey() : null;
		}
		
		@Override
		public Integer floor(Integer e) {
			Int2ObjectMap.Entry<V> node = map.floorEntry(e.intValue());
			return node != null ? node.getKey() : null;
		}
		
		@Override
		public Integer higher(Integer e) {
			Int2ObjectMap.Entry<V> node = map.higherEntry(e.intValue());
			return node != null ? node.getKey() : null;
		}
		
		@Override
		public Integer ceiling(Integer e) {
			Int2ObjectMap.Entry<V> node = map.ceilingEntry(e.intValue());
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
			if(map instanceof Int2ObjectRBTreeMap) return ((Int2ObjectRBTreeMap<V>)map).keyIterator(fromElement);
			return ((NavigableSubMap<V>)map).keyIterator(fromElement);
		}
		
		@Override
		public IntNavigableSet subSet(int fromElement, boolean fromInclusive, int toElement, boolean toInclusive) { return new KeySet<>(map.subMap(fromElement, fromInclusive, toElement, toInclusive)); }
		@Override
		public IntNavigableSet headSet(int toElement, boolean inclusive) { return new KeySet<>(map.headMap(toElement, inclusive)); }
		@Override
		public IntNavigableSet tailSet(int fromElement, boolean inclusive) { return new KeySet<>(map.tailMap(fromElement, inclusive)); }
		
		@Override
		public IntBidirectionalIterator iterator() {
			if(map instanceof Int2ObjectRBTreeMap) return ((Int2ObjectRBTreeMap<V>)map).keyIterator();
			return ((NavigableSubMap<V>)map).keyIterator();
		}
		
		@Override
		public IntBidirectionalIterator descendingIterator() {
			if(map instanceof Int2ObjectRBTreeMap) return ((Int2ObjectRBTreeMap<V>)map).descendingKeyIterator();
			return ((NavigableSubMap<V>)map).descendingKeyIterator();
		}
		
		protected Node<V> start() {
			if(map instanceof Int2ObjectRBTreeMap) return ((Int2ObjectRBTreeMap<V>)map).first;
			return ((NavigableSubMap<V>)map).subLowest();
		}
		
		protected Node<V> end() {
			if(map instanceof Int2ObjectRBTreeMap) return null;
			return ((NavigableSubMap<V>)map).subHighest();
		}
		
		protected Node<V> next(Node<V> entry) {
			if(map instanceof Int2ObjectRBTreeMap) return entry.next();
			return ((NavigableSubMap<V>)map).next(entry);
		}
		
		protected Node<V> previous(Node<V> entry) {
			if(map instanceof Int2ObjectRBTreeMap) return entry.previous();
			return ((NavigableSubMap<V>)map).previous(entry);
		}
		
		@Override
		public IntNavigableSet descendingSet() { return new KeySet<>(map.descendingMap()); }
		@Override
		public KeySet<V> copy() { throw new UnsupportedOperationException(); }
		@Override
		public boolean isEmpty() { return map.isEmpty(); }
		@Override
		public int size() { return map.size(); }
		
		@Override
		public void forEach(IntConsumer action) {
			Objects.requireNonNull(action);
			for(Node<V> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				action.accept(entry.key);
		}
		
		@Override
		public <E> void forEach(E input, ObjectIntConsumer<E> action) {
			Objects.requireNonNull(action);
			for(Node<V> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				action.accept(input, entry.key);
		}
		
		@Override
		public boolean matchesAny(Int2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(Node<V> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				if(filter.get(entry.key)) return true;
			return false;
		}
		
		@Override
		public boolean matchesNone(Int2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(Node<V> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				if(filter.get(entry.key)) return false;
			return true;
		}
		
		@Override
		public boolean matchesAll(Int2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(Node<V> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				if(!filter.get(entry.key)) return false;
			return true;
		}
		
		@Override
		public int reduce(int identity, IntIntUnaryOperator operator) {
			Objects.requireNonNull(operator);
			int state = identity;
			for(Node<V> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				state = operator.applyAsInt(state, entry.key);
			return state;
		}
		
		@Override
		public int reduce(IntIntUnaryOperator operator) {
			Objects.requireNonNull(operator);
			int state = 0;
			boolean empty = true;
			for(Node<V> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry)) {
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
		public int findFirst(Int2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(Node<V> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				if(filter.get(entry.key)) return entry.key;
			return 0;
		}
		
		@Override
		public int count(Int2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			int result = 0;
			for(Node<V> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				if(filter.get(entry.key)) result++;
			return result;
		}
	}
	
	static class AscendingNaivgableSubMap<V> extends NavigableSubMap<V>
	{
		AscendingNaivgableSubMap(Int2ObjectRBTreeMap<V> map, boolean fromStart, int lo, boolean loInclusive, boolean toEnd, int hi, boolean hiInclusive) {
			super(map, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
		}
		
		@Override
		public Int2ObjectNavigableMap<V> descendingMap() {
			if(inverse == null) inverse = new DescendingNaivgableSubMap<>(map, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
			return inverse;
		}
		
		@Override
		public ObjectSet<Int2ObjectMap.Entry<V>> int2ObjectEntrySet() {
			if(entrySet == null) entrySet = new AscendingSubEntrySet();
			return entrySet;
		}
		
		@Override
		public IntNavigableSet navigableKeySet() {
			if(keySet == null) keySet = new KeySet<>(this);
			return keySet;
		}
		
		@Override
		public Int2ObjectNavigableMap<V> subMap(int fromKey, boolean fromInclusive, int toKey, boolean toInclusive) {
			if (!inRange(fromKey, fromInclusive)) throw new IllegalArgumentException("fromKey out of range");
			if (!inRange(toKey, toInclusive)) throw new IllegalArgumentException("toKey out of range");
			return new AscendingNaivgableSubMap<>(map, false, fromKey, fromInclusive, false, toKey, toInclusive);
		}
		
		@Override
		public Int2ObjectNavigableMap<V> headMap(int toKey, boolean inclusive) {
			if (!inRange(toKey, inclusive)) throw new IllegalArgumentException("toKey out of range");
			return new AscendingNaivgableSubMap<>(map, fromStart, lo, loInclusive, false, toKey, inclusive);
		}
		
		@Override
		public Int2ObjectNavigableMap<V> tailMap(int fromKey, boolean inclusive) {
			if (!inRange(fromKey, inclusive)) throw new IllegalArgumentException("fromKey out of range");
			return new AscendingNaivgableSubMap<>(map, false, fromKey, inclusive, toEnd, hi, hiInclusive);
		}
		
		@Override
		protected Node<V> subLowest() { return absLowest(); }
		@Override
		protected Node<V> subHighest() { return absHighest(); }
		@Override
		protected Node<V> subCeiling(int key) { return absCeiling(key); }
		@Override
		protected Node<V> subHigher(int key) { return absHigher(key); }
		@Override
		protected Node<V> subFloor(int key) { return absFloor(key); }
		@Override
		protected Node<V> subLower(int key) { return absLower(key); }
		
		@Override
		protected IntBidirectionalIterator keyIterator() {
			return new AcsendingSubKeyIterator(absLowest(), absHighFence(), absLowFence()); 
		}
		@Override
		protected IntBidirectionalIterator keyIterator(int element) {
			return new AcsendingSubKeyIterator(absLower(element), absHighFence(), absLowFence()); 
		}
		
		@Override
		protected ObjectBidirectionalIterator<V> valueIterator() {
			return new AcsendingSubValueIterator(absLowest(), absHighFence(), absLowFence()); 
		}
		
		@Override
		protected IntBidirectionalIterator descendingKeyIterator() {
			return new DecsendingSubKeyIterator(absHighest(), absLowFence(), absHighFence()); 
		}
		
		class AscendingSubEntrySet extends SubEntrySet {
			@Override
			public ObjectIterator<Int2ObjectMap.Entry<V>> iterator() {
				return new AcsendingSubEntryIterator(absLowest(), absHighFence(), absLowFence());
			}
		}
	}
	
	static class DescendingNaivgableSubMap<V> extends NavigableSubMap<V>
	{
		IntComparator comparator;
		DescendingNaivgableSubMap(Int2ObjectRBTreeMap<V> map, boolean fromStart, int lo, boolean loInclusive, boolean toEnd, int hi, boolean hiInclusive) {
			super(map, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
			comparator = map.comparator() == null ? IntComparator.of(Collections.reverseOrder()) : map.comparator().reversed();
		}
		
		@Override
		public IntComparator comparator() { return comparator; }
		
		@Override
		public Int2ObjectNavigableMap<V> descendingMap() {
			if(inverse == null) inverse = new AscendingNaivgableSubMap<>(map, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
			return inverse;
		}

		@Override
		public IntNavigableSet navigableKeySet() {
			if(keySet == null) keySet = new KeySet<>(this);
			return keySet;
		}
		
		@Override
		public Int2ObjectNavigableMap<V> subMap(int fromKey, boolean fromInclusive, int toKey, boolean toInclusive) {
			if (!inRange(fromKey, fromInclusive)) throw new IllegalArgumentException("fromKey out of range");
			if (!inRange(toKey, toInclusive)) throw new IllegalArgumentException("toKey out of range");
			return new DescendingNaivgableSubMap<>(map, false, toKey, toInclusive, false, fromKey, fromInclusive);
		}
		
		@Override
		public Int2ObjectNavigableMap<V> headMap(int toKey, boolean inclusive) {
			if (!inRange(toKey, inclusive)) throw new IllegalArgumentException("toKey out of range");
			return new DescendingNaivgableSubMap<>(map, false, toKey, inclusive, toEnd, hi, hiInclusive);
		}
		
		@Override
		public Int2ObjectNavigableMap<V> tailMap(int fromKey, boolean inclusive) {
			if (!inRange(fromKey, inclusive)) throw new IllegalArgumentException("fromKey out of range");
			return new DescendingNaivgableSubMap<>(map, fromStart, lo, loInclusive, false, fromKey, inclusive);
		}
		
		@Override
		public ObjectSet<Int2ObjectMap.Entry<V>> int2ObjectEntrySet() {
			if(entrySet == null) entrySet = new DescendingSubEntrySet();
			return entrySet;
		}
		
		@Override
		protected Node<V> subLowest() { return absHighest(); }
		@Override
		protected Node<V> subHighest() { return absLowest(); }
		@Override
		protected Node<V> subCeiling(int key) { return absFloor(key); }
		@Override
		protected Node<V> subHigher(int key) { return absLower(key); }
		@Override
		protected Node<V> subFloor(int key) { return absCeiling(key); }
		@Override
		protected Node<V> subLower(int key) { return absHigher(key); }
		@Override
		protected Node<V> next(Node<V> entry) { return entry.previous(); }
		@Override
		protected Node<V> previous(Node<V> entry) { return entry.next(); }
		
		@Override
		protected IntBidirectionalIterator keyIterator() {
			return new DecsendingSubKeyIterator(absHighest(), absLowFence(), absHighFence()); 
		}
		
		@Override
		protected IntBidirectionalIterator keyIterator(int element) {
			return new DecsendingSubKeyIterator(absHigher(element), absLowFence(), absHighFence()); 
		}
		
		@Override
		protected ObjectBidirectionalIterator<V> valueIterator() {
			return new DecsendingSubValueIterator(absHighest(), absLowFence(), absHighFence()); 
		}
		
		@Override
		protected IntBidirectionalIterator descendingKeyIterator() {
			return new AcsendingSubKeyIterator(absLowest(), absHighFence(), absLowFence()); 
		}
		
		class DescendingSubEntrySet extends SubEntrySet {
			@Override
			public ObjectIterator<Int2ObjectMap.Entry<V>> iterator() {
				return new DecsendingSubEntryIterator(absHighest(), absLowFence(), absHighFence());
			}
		}
	}
	
	static abstract class NavigableSubMap<V> extends AbstractInt2ObjectMap<V> implements Int2ObjectNavigableMap<V>
	{
		final Int2ObjectRBTreeMap<V> map;
		final int lo, hi;
		final boolean fromStart, toEnd;
		final boolean loInclusive, hiInclusive;
		
		Int2ObjectNavigableMap<V> inverse;
		IntNavigableSet keySet;
		ObjectSet<Int2ObjectMap.Entry<V>> entrySet;
		ObjectCollection<V> values;
		
		NavigableSubMap(Int2ObjectRBTreeMap<V> map, boolean fromStart, int lo, boolean loInclusive, boolean toEnd, int hi, boolean hiInclusive) {
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
		public AbstractInt2ObjectMap<V> setDefaultReturnValue(V v) { 
			map.setDefaultReturnValue(v);
			return this;
		}
		
		@Override
		public V getDefaultReturnValue() { return map.getDefaultReturnValue(); }
		
		@Override
		public ObjectCollection<V> values() {
			if(values == null) values = new SubMapValues();
			return values;
		}
		
		@Override
		public IntNavigableSet descendingKeySet() {
			return descendingMap().navigableKeySet();
		}
		
		@Override
		public IntSet keySet() {
			return navigableKeySet();
		}
		
		protected abstract Node<V> subLowest();
		protected abstract Node<V> subHighest();
		protected abstract Node<V> subCeiling(int key);
		protected abstract Node<V> subHigher(int key);
		protected abstract Node<V> subFloor(int key);
		protected abstract Node<V> subLower(int key);
		protected abstract IntBidirectionalIterator keyIterator();
		protected abstract IntBidirectionalIterator keyIterator(int element);
		protected abstract ObjectBidirectionalIterator<V> valueIterator();
		protected abstract IntBidirectionalIterator descendingKeyIterator();
		protected int lowKeyOrNull(Node<V> entry) { return entry == null ? getDefaultMinValue() : entry.key; }
		protected int highKeyOrNull(Node<V> entry) { return entry == null ? getDefaultMaxValue() : entry.key; }
		protected Node<V> next(Node<V> entry) { return entry.next(); }
		protected Node<V> previous(Node<V> entry) { return entry.previous(); }
		
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
		
		protected Node<V> absLowest() {
			Node<V> e = (fromStart ?  map.first : (loInclusive ? map.findCeilingNode(lo) : map.findHigherNode(lo)));
			return (e == null || tooHigh(e.key)) ? null : e;
		}
		
		protected Node<V> absHighest() {
			Node<V> e = (toEnd ?  map.last : (hiInclusive ?  map.findFloorNode(hi) : map.findLowerNode(hi)));
			return (e == null || tooLow(e.key)) ? null : e;
		}
		
		protected Node<V> absCeiling(int key) {
			if (tooLow(key)) return absLowest();
			Node<V> e = map.findCeilingNode(key);
			return (e == null || tooHigh(e.key)) ? null : e;
		}
		
		protected Node<V> absHigher(int key) {
			if (tooLow(key)) return absLowest();
			Node<V> e = map.findHigherNode(key);
			return (e == null || tooHigh(e.key)) ? null : e;
		}
		
		protected Node<V> absFloor(int key) {
			if (tooHigh(key)) return absHighest();
			Node<V> e = map.findFloorNode(key);
			return (e == null || tooLow(e.key)) ? null : e;
		}
		
		protected Node<V> absLower(int key) {
			if (tooHigh(key)) return absHighest();
			Node<V> e = map.findLowerNode(key);
			return (e == null || tooLow(e.key)) ? null : e;
		}
		
		protected Node<V> absHighFence() { return (toEnd ? null : (hiInclusive ? map.findHigherNode(hi) : map.findCeilingNode(hi))); }
		protected Node<V> absLowFence() { return (fromStart ? null : (loInclusive ?  map.findLowerNode(lo) : map.findFloorNode(lo))); }
		
		@Override
		public IntComparator comparator() { return map.comparator(); }
		
		@Override
		public int pollFirstIntKey() {
			Node<V> entry = subLowest();
			if(entry != null) {
				int result = entry.key;
				map.removeNode(entry);
				return result;
			}
			return getDefaultMinValue();
		}
		
		@Override
		public int pollLastIntKey() {
			Node<V> entry = subHighest();
			if(entry != null) {
				int result = entry.key;
				map.removeNode(entry);
				return result;
			}
			return getDefaultMaxValue();
		}
		
		@Override
		public V firstValue() {
			Node<V> entry = subLowest();
			return entry == null ? map.getDefaultReturnValue() : entry.value;
		}
		
		@Override
		public V lastValue() {
			Node<V> entry = subHighest();
			return entry == null ? map.getDefaultReturnValue() : entry.value;
		}
		
		@Override
		public int firstIntKey() {
			Node<V> entry = subLowest();
			if(entry == null) throw new NoSuchElementException();
			return entry.key;
		}
		
		@Override
		public int lastIntKey() {
			Node<V> entry = subHighest();
			if(entry == null) throw new NoSuchElementException();
			return entry.key;
		}
		
		@Override
		public V put(int key, V value) {
			if (!inRange(key)) throw new IllegalArgumentException("key out of range");
			return map.put(key, value);
		}
		
		@Override
		public V putIfAbsent(int key, V value) {
			if (!inRange(key)) throw new IllegalArgumentException("key out of range");
			return map.putIfAbsent(key, value);
		}
		
		@Override
		public boolean containsKey(int key) { return inRange(key) && map.containsKey(key); }
		
		@Override
		public V computeIfPresent(int key, IntObjectUnaryOperator<V> mappingFunction) {
			Objects.requireNonNull(mappingFunction);
			if(!inRange(key)) return getDefaultReturnValue();
			Node<V> entry = map.findNode(key);
			if(entry == null || Objects.equals(entry.value, getDefaultReturnValue())) return getDefaultReturnValue();
			V newValue = mappingFunction.apply(key, entry.value);
			if(Objects.equals(newValue, getDefaultReturnValue())) {
				map.removeNode(entry);
				return newValue;
			}
			entry.value = newValue;
			return newValue;
		}
		
		@Override
		public V remove(int key) {
			return inRange(key) ? map.remove(key) : getDefaultReturnValue();
		}
		
		@Override
		public V removeOrDefault(int key, V defaultValue) {
			return inRange(key) ? map.removeOrDefault(key, defaultValue) : defaultValue;
		}
		
		@Override
		public boolean remove(int key, V value) {
			return inRange(key) && map.remove(key, value);
		}
		
		
		@Override
		public V get(int key) {
			return inRange(key) ? map.get(key) : getDefaultReturnValue();
		}
		
		@Override
		public V getOrDefault(int key, V defaultValue) {
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
		public Int2ObjectMap.Entry<V> lowerEntry(int key) { return subLower(key); }
		@Override
		public Int2ObjectMap.Entry<V> floorEntry(int key) { return subFloor(key); }
		@Override
		public Int2ObjectMap.Entry<V> ceilingEntry(int key) { return subCeiling(key); }
		@Override
		public Int2ObjectMap.Entry<V> higherEntry(int key) { return subHigher(key); }
		
		@Override
		public boolean isEmpty() {
			if(fromStart && toEnd) return map.isEmpty();
			Node<V> n = absLowest();
			return n == null || tooHigh(n.key);
		}
		
		@Override
		public int size() { return fromStart && toEnd ? map.size() : entrySet().size(); }
		
		@Override
		public Int2ObjectNavigableMap<V> copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public Int2ObjectMap.Entry<V> firstEntry() {
			Node<V> entry = subLowest();
			return entry == null ? null : entry.export();
		}

		@Override
		public Int2ObjectMap.Entry<V> lastEntry() {
			Node<V> entry = subHighest();
			return entry == null ? null : entry.export();
		}

		@Override
		public Int2ObjectMap.Entry<V> pollFirstEntry() {
			Node<V> entry = subLowest();
			if(entry != null) {
				Int2ObjectMap.Entry<V> result = entry.export();
				map.removeNode(entry);
				return result;
			}
			return null;
		}

		@Override
		public Int2ObjectMap.Entry<V> pollLastEntry() {
			Node<V> entry = subHighest();
			if(entry != null) {
				Int2ObjectMap.Entry<V> result = entry.export();
				map.removeNode(entry);
				return result;
			}
			return null;
		}
		
		abstract class SubEntrySet extends AbstractObjectSet<Int2ObjectMap.Entry<V>> {
			@Override
			public int size() {
				if (fromStart && toEnd) return map.size();
				int size = 0;
				for(ObjectIterator<Int2ObjectMap.Entry<V>> iter = iterator();iter.hasNext();iter.next(),size++);
				return size;
			}
			
			@Override
			public boolean isEmpty() {
				Node<V> n = absLowest();
				return n == null || tooHigh(n.key);
			}
			
			@Override
			public boolean contains(Object o) {
				if (!(o instanceof Map.Entry)) return false;
				if(o instanceof Int2ObjectMap.Entry)
				{
					Int2ObjectMap.Entry<V> entry = (Int2ObjectMap.Entry<V>) o;
					int key = entry.getIntKey();
					if (!inRange(key)) return false;
					Node<V> node = map.findNode(key);
					return node != null && Objects.equals(entry.getValue(), node.getValue());
				}
				Map.Entry<?,?> entry = (Map.Entry<?,?>) o;
				if(entry.getKey() == null && isNullComparator()) return false;
				Integer key = (Integer)entry.getKey();
				if (!inRange(key)) return false;
				Node<V> node = map.findNode(key);
				return node != null && Objects.equals(entry.getValue(), node.getValue());
			}
			
			@Override
			public boolean remove(Object o) {
				if (!(o instanceof Map.Entry)) return false;
				if(o instanceof Int2ObjectMap.Entry)
				{
					Int2ObjectMap.Entry<V> entry = (Int2ObjectMap.Entry<V>) o;
					int key = entry.getIntKey();
					if (!inRange(key)) return false;
					Node<V> node = map.findNode(key);
					if (node != null && Objects.equals(node.getValue(), entry.getValue())) {
						map.removeNode(node);
						return true;
					}
					return false;
				}
				Map.Entry<?,?> entry = (Map.Entry<?,?>) o;
				Integer key = (Integer)entry.getKey();
				if (!inRange(key)) return false;
				Node<V> node = map.findNode(key);
				if (node != null && Objects.equals(node.getValue(), entry.getValue())) {
					map.removeNode(node);
					return true;
				}
				return false;
			}
			
			@Override
			public void forEach(Consumer<? super Int2ObjectMap.Entry<V>> action) {
				Objects.requireNonNull(action);
				for(Node<V> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					action.accept(new BasicEntry<>(entry.key, entry.value));
			}
			
			@Override
			public <E> void forEach(E input, ObjectObjectConsumer<E, Int2ObjectMap.Entry<V>> action) {
				Objects.requireNonNull(action);
				for(Node<V> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					action.accept(input, new BasicEntry<>(entry.key, entry.value));
			}
			
			@Override
			public boolean matchesAny(Object2BooleanFunction<Int2ObjectMap.Entry<V>> filter) {
				Objects.requireNonNull(filter);
				if(size() <= 0) return false;
				BasicEntry<V> subEntry = new BasicEntry<>();
				for(Node<V> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					subEntry.set(entry.key, entry.value);
					if(filter.getBoolean(subEntry)) return true;
				}
				return false;
			}
			
			@Override
			public boolean matchesNone(Object2BooleanFunction<Int2ObjectMap.Entry<V>> filter) {
				Objects.requireNonNull(filter);
				if(size() <= 0) return true;
				BasicEntry<V> subEntry = new BasicEntry<>();
				for(Node<V> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					subEntry.set(entry.key, entry.value);
					if(filter.getBoolean(subEntry)) return false;
				}
				return true;
			}
			
			@Override
			public boolean matchesAll(Object2BooleanFunction<Int2ObjectMap.Entry<V>> filter) {
				Objects.requireNonNull(filter);
				if(size() <= 0) return true;
				BasicEntry<V> subEntry = new BasicEntry<>();
				for(Node<V> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					subEntry.set(entry.key, entry.value);
					if(!filter.getBoolean(subEntry)) return false;
				}
				return true;
			}
			
			@Override
			public <E> E reduce(E identity, BiFunction<E, Int2ObjectMap.Entry<V>, E> operator) {
				Objects.requireNonNull(operator);
				E state = identity;
				for(Node<V> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					state = operator.apply(state, new BasicEntry<>(entry.key, entry.value));
				}
				return state;
			}
			
			@Override
			public Int2ObjectMap.Entry<V> reduce(ObjectObjectUnaryOperator<Int2ObjectMap.Entry<V>, Int2ObjectMap.Entry<V>> operator) {
				Objects.requireNonNull(operator);
				Int2ObjectMap.Entry<V> state = null;
				boolean empty = true;
				for(Node<V> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					if(empty) {
						empty = false;
						state = new BasicEntry<>(entry.key, entry.value);
						continue;
					}
					state = operator.apply(state, new BasicEntry<>(entry.key, entry.value));
				}
				return state;
			}
			
			@Override
			public Int2ObjectMap.Entry<V> findFirst(Object2BooleanFunction<Int2ObjectMap.Entry<V>> filter) {
				Objects.requireNonNull(filter);
				if(size() <= 0) return null;
				BasicEntry<V> subEntry = new BasicEntry<>();
				for(Node<V> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					subEntry.set(entry.key, entry.value);
					if(filter.getBoolean(subEntry)) return subEntry;
				}
				return null;
			}
			
			@Override
			public int count(Object2BooleanFunction<Int2ObjectMap.Entry<V>> filter) {
				Objects.requireNonNull(filter);
				if(size() <= 0) return 0;
				int result = 0;
				BasicEntry<V> subEntry = new BasicEntry<>();
				for(Node<V> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					subEntry.set(entry.key, entry.value);
					if(filter.getBoolean(subEntry)) result++;
				}
				return result;
			}
		}
		
		final class SubMapValues extends AbstractObjectCollection<V> {
			@Override
			public boolean add(V o) { throw new UnsupportedOperationException(); }
			
			@Override
			public boolean contains(Object e) {
				return containsValue(e);
			}
			
			@Override
			public ObjectIterator<V> iterator() { return valueIterator(); }
			
			@Override
			public int size() {
				return NavigableSubMap.this.size();
			}
			
			@Override
			public void clear() {
				NavigableSubMap.this.clear();
			}
			
			@Override
			public void forEach(Consumer<? super V> action) {
				Objects.requireNonNull(action);
				for(Node<V> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					action.accept(entry.value);
			}
			
			@Override
			public <E> void forEach(E input, ObjectObjectConsumer<E, V> action) {
				Objects.requireNonNull(action);
				for(Node<V> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					action.accept(input, entry.value);
			}
			
			@Override
			public boolean matchesAny(Object2BooleanFunction<V> filter) {
				Objects.requireNonNull(filter);
				for(Node<V> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					if(filter.getBoolean(entry.value)) return true;
				return false;
			}
			
			@Override
			public boolean matchesNone(Object2BooleanFunction<V> filter) {
				Objects.requireNonNull(filter);
				for(Node<V> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					if(filter.getBoolean(entry.value)) return false;
				return true;
			}
			
			@Override
			public boolean matchesAll(Object2BooleanFunction<V> filter) {
				Objects.requireNonNull(filter);
				for(Node<V> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					if(!filter.getBoolean(entry.value)) return false;
				return true;
			}
			
			@Override
			public <E> E reduce(E identity, BiFunction<E, V, E> operator) {
				Objects.requireNonNull(operator);
				E state = identity;
				for(Node<V> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					state = operator.apply(state, entry.value);
				return state;
			}
			
			@Override
			public V reduce(ObjectObjectUnaryOperator<V, V> operator) {
				Objects.requireNonNull(operator);
				V state = null;
				boolean empty = true;
				for(Node<V> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					if(empty) {
						empty = false;
						state = entry.value;
						continue;
					}
					state = operator.apply(state, entry.value);
				}
				return state;
			}
			
			@Override
			public V findFirst(Object2BooleanFunction<V> filter) {
				Objects.requireNonNull(filter);
				for(Node<V> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					if(filter.getBoolean(entry.value)) return entry.value;
				return null;
			}
			
			@Override
			public int count(Object2BooleanFunction<V> filter) {
				Objects.requireNonNull(filter);
				int result = 0;
				for(Node<V> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					if(filter.getBoolean(entry.value)) result++;
				return result;
			}
		}
		
		class DecsendingSubEntryIterator extends SubMapEntryIterator implements ObjectBidirectionalIterator<Int2ObjectMap.Entry<V>>
		{
			public DecsendingSubEntryIterator(Node<V> first, Node<V> forwardFence, Node<V> backwardFence) {
				super(first, forwardFence, backwardFence);
			}
			
			@Override
			public Int2ObjectMap.Entry<V> previous() {
				if(!hasPrevious()) throw new NoSuchElementException();
				return nextEntry();
			}

			@Override
			public Int2ObjectMap.Entry<V> next() {
				if(!hasNext()) throw new NoSuchElementException();
				return previousEntry();
			}
		}
		
		class AcsendingSubEntryIterator extends SubMapEntryIterator implements ObjectBidirectionalIterator<Int2ObjectMap.Entry<V>>
		{
			public AcsendingSubEntryIterator(Node<V> first, Node<V> forwardFence, Node<V> backwardFence) {
				super(first, forwardFence, backwardFence);
			}

			@Override
			public Int2ObjectMap.Entry<V> previous() {
				if(!hasPrevious()) throw new NoSuchElementException();
				return previousEntry();
			}

			@Override
			public Int2ObjectMap.Entry<V> next() {
				if(!hasNext()) throw new NoSuchElementException();
				return nextEntry();
			}
		}
		
		class DecsendingSubKeyIterator extends SubMapEntryIterator implements IntBidirectionalIterator
		{
			public DecsendingSubKeyIterator(Node<V> first, Node<V> forwardFence, Node<V> backwardFence) {
				super(first, forwardFence, backwardFence);
			}
			
			@Override
			public int previousInt() {
				if(!hasPrevious()) throw new NoSuchElementException();
				return nextEntry().key;
			}

			@Override
			public int nextInt() {
				if(!hasNext()) throw new NoSuchElementException();
				return previousEntry().key;
			}
		}
		
		class AcsendingSubKeyIterator extends SubMapEntryIterator implements IntBidirectionalIterator
		{
			public AcsendingSubKeyIterator(Node<V> first, Node<V> forwardFence, Node<V> backwardFence) {
				super(first, forwardFence, backwardFence);
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
		
		class AcsendingSubValueIterator extends SubMapEntryIterator implements ObjectBidirectionalIterator<V>
		{
			public AcsendingSubValueIterator(Node<V> first, Node<V> forwardFence, Node<V> backwardFence) {
				super(first, forwardFence, backwardFence);
			}

			@Override
			public V previous() {
				if(!hasPrevious()) throw new NoSuchElementException();
				return previousEntry().value;
			}

			@Override
			public V next() {
				if(!hasNext()) throw new NoSuchElementException();
				return nextEntry().value;
			}
		}
		
		class DecsendingSubValueIterator extends SubMapEntryIterator implements ObjectBidirectionalIterator<V>
		{
			public DecsendingSubValueIterator(Node<V> first, Node<V> forwardFence, Node<V> backwardFence) {
				super(first, forwardFence, backwardFence);
			}
			
			@Override
			public V previous() {
				if(!hasPrevious()) throw new NoSuchElementException();
				return nextEntry().value;
			}

			@Override
			public V next() {
				if(!hasNext()) throw new NoSuchElementException();
				return previousEntry().value;
			}
		}
		
		abstract class SubMapEntryIterator
		{
			boolean wasForward;
			Node<V> lastReturned;
			Node<V> next;
			boolean unboundForwardFence;
			boolean unboundBackwardFence;
			int forwardFence;
			int backwardFence;
			
			public SubMapEntryIterator(Node<V> first, Node<V> forwardFence, Node<V> backwardFence)
			{
				next = first;
				this.forwardFence = forwardFence == null ? 0 : forwardFence.key;
				this.backwardFence = backwardFence == null ? 0 : backwardFence.key;
				unboundForwardFence = forwardFence == null;
				unboundBackwardFence = backwardFence == null;
			}
			
			public boolean hasNext() {
                return next != null && (unboundForwardFence || next.key != forwardFence);
			}
			
			protected Node<V> nextEntry() {
				lastReturned = next;
				Node<V> result = next;
				next = next.next();
				wasForward = true;
				return result;
			}
			
			public boolean hasPrevious() {
                return next != null && (unboundBackwardFence || next.key != backwardFence);
			}
			
			protected Node<V> previousEntry() {
				lastReturned = next;
				Node<V> result = next;
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
	
	class Values extends AbstractObjectCollection<V>
	{
		@Override
		public ObjectIterator<V> iterator() {
			return new AscendingValueIterator(first);
		}
		
		@Override
		public boolean add(V e) { throw new UnsupportedOperationException(); }
		
		@Override
		public void clear() {
			Int2ObjectRBTreeMap.this.clear();
		}
		
		@Override
		public int size() {
			return Int2ObjectRBTreeMap.this.size;
		}
		
		@Override
		public boolean contains(Object e) {
			return containsValue(e);
		}
		
		@Override
		public boolean remove(Object o) {
			for(Node<V> entry = first; entry != null; entry = entry.next()) {
				if(Objects.equals(entry.getValue(), o)) {
					removeNode(entry);
					return true;
				}
			}
			return false;
		}
		
		@Override
		public void forEach(Consumer<? super V> action) {
			Objects.requireNonNull(action);
			for(Node<V> entry = first;entry != null;entry = entry.next())
				action.accept(entry.value);
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, V> action) {
			Objects.requireNonNull(action);
			for(Node<V> entry = first;entry != null;entry = entry.next())
				action.accept(input, entry.value);
		}
		
		@Override
		public boolean matchesAny(Object2BooleanFunction<V> filter) {
			Objects.requireNonNull(filter);
			for(Node<V> entry = first;entry != null;entry = entry.next())
				if(filter.getBoolean(entry.value)) return true;
			return false;
		}
		
		@Override
		public boolean matchesNone(Object2BooleanFunction<V> filter) {
			Objects.requireNonNull(filter);
			for(Node<V> entry = first;entry != null;entry = entry.next())
				if(filter.getBoolean(entry.value)) return false;
			return true;
		}
		
		@Override
		public boolean matchesAll(Object2BooleanFunction<V> filter) {
			Objects.requireNonNull(filter);
			for(Node<V> entry = first;entry != null;entry = entry.next())
				if(!filter.getBoolean(entry.value)) return false;
			return true;
		}
		
		@Override
		public <E> E reduce(E identity, BiFunction<E, V, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			for(Node<V> entry = first;entry != null;entry = entry.next())
				state = operator.apply(state, entry.value);
			return state;
		}
		
		@Override
		public V reduce(ObjectObjectUnaryOperator<V, V> operator) {
			Objects.requireNonNull(operator);
			V state = null;
			boolean empty = true;
			for(Node<V> entry = first;entry != null;entry = entry.next()) {
				if(empty) {
					empty = false;
					state = entry.value;
					continue;
				}
				state = operator.apply(state, entry.value);
			}
			return state;
		}
		
		@Override
		public V findFirst(Object2BooleanFunction<V> filter) {
			Objects.requireNonNull(filter);
			for(Node<V> entry = first;entry != null;entry = entry.next())
				if(filter.getBoolean(entry.value)) return entry.value;
			return null;
		}
		
		@Override
		public int count(Object2BooleanFunction<V> filter) {
			Objects.requireNonNull(filter);
			int result = 0;
			for(Node<V> entry = first;entry != null;entry = entry.next())
				if(filter.getBoolean(entry.value)) result++;
			return result;
		}
	}
	
	class EntrySet extends AbstractObjectSet<Int2ObjectMap.Entry<V>> {
		
		@Override
		public ObjectIterator<Int2ObjectMap.Entry<V>> iterator() {
			return new AscendingMapEntryIterator(first);
		}
		
		@Override
		public void clear() {
			Int2ObjectRBTreeMap.this.clear();
		}
		
		@Override
		public int size() {
			return Int2ObjectRBTreeMap.this.size;
		}
		
		@Override
		public boolean contains(Object o) {
			if (!(o instanceof Map.Entry)) return false;
			if(o instanceof Int2ObjectMap.Entry)
			{
				Int2ObjectMap.Entry<V> entry = (Int2ObjectMap.Entry<V>) o;
				int key = entry.getIntKey();
				Node<V> node = findNode(key);
				return node != null && Objects.equals(entry.getValue(), node.getValue());
			}
			Map.Entry<?,?> entry = (Map.Entry<?,?>) o;
			if(entry.getKey() == null && comparator() == null) return false;
			if(!(entry.getKey() instanceof Integer)) return false;
			Integer key = (Integer)entry.getKey();
			Node<V> node = findNode(key);
			return node != null && Objects.equals(entry.getValue(), node.getValue());
		}
		
		@Override
		public boolean remove(Object o) {
			if (!(o instanceof Map.Entry)) return false;
			if(o instanceof Int2ObjectMap.Entry)
			{
				Int2ObjectMap.Entry<V> entry = (Int2ObjectMap.Entry<V>) o;
				int key = entry.getIntKey();
				Node<V> node = findNode(key);
				if (node != null && Objects.equals(entry.getValue(), node.getValue())) {
					removeNode(node);
					return true;
				}
				return false;
			}
			Map.Entry<?,?> entry = (Map.Entry<?,?>) o;
			Integer key = (Integer)entry.getKey();
			Node<V> node = findNode(key);
			if (node != null && Objects.equals(entry.getValue(), node.getValue())) {
				removeNode(node);
				return true;
			}
			return false;
		}
		
		@Override
		public void forEach(Consumer<? super Int2ObjectMap.Entry<V>> action) {
			Objects.requireNonNull(action);
			for(Node<V> entry = first;entry != null;entry = entry.next())
				action.accept(new BasicEntry<>(entry.key, entry.value));
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, Int2ObjectMap.Entry<V>> action) {
			Objects.requireNonNull(action);
			for(Node<V> entry = first;entry != null;entry = entry.next())
				action.accept(input, new BasicEntry<>(entry.key, entry.value));
		}
		
		@Override
		public boolean matchesAny(Object2BooleanFunction<Int2ObjectMap.Entry<V>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			BasicEntry<V> subEntry = new BasicEntry<>();
			for(Node<V> entry = first;entry != null;entry = entry.next()) {
				subEntry.set(entry.key, entry.value);
				if(filter.getBoolean(subEntry)) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Object2BooleanFunction<Int2ObjectMap.Entry<V>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			BasicEntry<V> subEntry = new BasicEntry<>();
			for(Node<V> entry = first;entry != null;entry = entry.next()) {
				subEntry.set(entry.key, entry.value);
				if(filter.getBoolean(subEntry)) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Object2BooleanFunction<Int2ObjectMap.Entry<V>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			BasicEntry<V> subEntry = new BasicEntry<>();
			for(Node<V> entry = first;entry != null;entry = entry.next()) {
				subEntry.set(entry.key, entry.value);
				if(!filter.getBoolean(subEntry)) return false;
			}
			return true;
		}
		
		@Override
		public <E> E reduce(E identity, BiFunction<E, Int2ObjectMap.Entry<V>, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			for(Node<V> entry = first;entry != null;entry = entry.next()) {
				state = operator.apply(state, new BasicEntry<>(entry.key, entry.value));
			}
			return state;
		}
		
		@Override
		public Int2ObjectMap.Entry<V> reduce(ObjectObjectUnaryOperator<Int2ObjectMap.Entry<V>, Int2ObjectMap.Entry<V>> operator) {
			Objects.requireNonNull(operator);
			Int2ObjectMap.Entry<V> state = null;
			boolean empty = true;
			for(Node<V> entry = first;entry != null;entry = entry.next()) {
				if(empty) {
					empty = false;
					state = new BasicEntry<>(entry.key, entry.value);
					continue;
				}
				state = operator.apply(state, new BasicEntry<>(entry.key, entry.value));
			}
			return state;
		}
		
		@Override
		public Int2ObjectMap.Entry<V> findFirst(Object2BooleanFunction<Int2ObjectMap.Entry<V>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return null;
			BasicEntry<V> subEntry = new BasicEntry<>();
			for(Node<V> entry = first;entry != null;entry = entry.next()) {
				subEntry.set(entry.key, entry.value);
				if(filter.getBoolean(subEntry)) return subEntry;
			}
			return null;
		}
		
		@Override
		public int count(Object2BooleanFunction<Int2ObjectMap.Entry<V>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			int result = 0;
			BasicEntry<V> subEntry = new BasicEntry<>();
			for(Node<V> entry = first;entry != null;entry = entry.next()) {
				subEntry.set(entry.key, entry.value);
				if(filter.getBoolean(subEntry)) result++;
			}
			return result;
		}
	}
	
	class DescendingKeyIterator extends MapEntryIterator implements IntBidirectionalIterator
	{
		public DescendingKeyIterator(Node<V> first) {
			super(first);
		}

		@Override
		public int previousInt() {
			if(!hasPrevious()) throw new NoSuchElementException();
			return nextEntry().key;
		}
		
		@Override
		public int nextInt() {
			if(!hasNext()) throw new NoSuchElementException();
			return previousEntry().key;
		}
	}
	
	class AscendingMapEntryIterator extends MapEntryIterator implements ObjectBidirectionalIterator<Int2ObjectMap.Entry<V>>
	{
		public AscendingMapEntryIterator(Node<V> first)
		{
			super(first);
		}

		@Override
		public Int2ObjectMap.Entry<V> previous() {
			if(!hasPrevious()) throw new NoSuchElementException();
			return previousEntry();
		}

		@Override
		public Int2ObjectMap.Entry<V> next() {
			if(!hasNext()) throw new NoSuchElementException();
			return nextEntry();
		}
	}
	
	class AscendingValueIterator extends MapEntryIterator implements ObjectBidirectionalIterator<V>
	{
		public AscendingValueIterator(Node<V> first) {
			super(first);
		}

		@Override
		public V previous() {
			if(!hasPrevious()) throw new NoSuchElementException();
			return previousEntry().value;
		}

		@Override
		public V next() {
			if(!hasNext()) throw new NoSuchElementException();
			return nextEntry().value;
		}
	}
	
	class AscendingKeyIterator extends MapEntryIterator implements IntBidirectionalIterator
	{
		public AscendingKeyIterator(Node<V> first) {
			super(first);
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
		boolean wasMoved = false;
		Node<V> lastReturned;
		Node<V> next;
		
		public MapEntryIterator(Node<V> first)
		{
			next = first;
		}
		
		public boolean hasNext() {
            return next != null;
		}
		
		protected Node<V> nextEntry() {
			lastReturned = next;
			Node<V> result = next;
			next = next.next();
			wasMoved = true;
			return result;
		}
		
		public boolean hasPrevious() {
            return next != null;
		}
		
		protected Node<V> previousEntry() {
			lastReturned = next;
			Node<V> result = next;
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
	
	private static final class Node<V> implements Int2ObjectMap.Entry<V>
	{
		static final int BLACK = 1;
		
		int key;
		V value;
		int state;
		Node<V> parent;
		Node<V> left;
		Node<V> right;
		
		Node(int key, V value, Node<V> parent) {
			this.key = key;
			this.value = value;
			this.parent = parent;
		}
		
		Node<V> copy() {
			Node<V> entry = new Node<>(key, value, null);
			entry.state = state;
			if(left != null) {
				Node<V> newLeft = left.copy();
				entry.left = newLeft;
				newLeft.parent = entry;
			}
			if(right != null) {
				Node<V> newRight = right.copy();
				entry.right = newRight;
				newRight.parent = entry;
			}
			return entry;
		}
		
		public BasicEntry<V> export() {
			return new BasicEntry<>(key, value);
		}
		
		@Override
		public int getIntKey() {
			return key;
		}
		
		@Override
		public V getValue() {
			return value;
		}
		
		@Override
		public V setValue(V value) {
			V oldValue = this.value;
			this.value = value;
			return oldValue;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Map.Entry) {
				if(obj instanceof Int2ObjectMap.Entry) {
					Int2ObjectMap.Entry<V> entry = (Int2ObjectMap.Entry<V>)obj;
					return key == entry.getIntKey() && Objects.equals(value, entry.getValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object otherKey = entry.getKey();
				if(otherKey == null) return false;
				Object otherValue = entry.getValue();
				return otherKey instanceof Integer && key == ((Integer)otherKey).intValue() && Objects.equals(value, otherValue);
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Integer.hashCode(key) ^ Objects.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Integer.toString(key) + "=" + Objects.toString(value);
		}
		
		boolean isBlack() {
			return (state & BLACK) != 0;
		}
		
		void setBlack(boolean value) {
			if(value) state |= BLACK;
			else state &= ~BLACK;
		}
		
		boolean needsSuccessor() { return left != null && right != null; }
		
		boolean replace(Node<V> entry) {
			if(entry != null) entry.parent = parent;
			if(parent != null) {
				if(parent.left == this) parent.left = entry;
				else parent.right = entry;
			}
			return parent == null;
		}
		
		Node<V> next() {
			if(right != null) {
				Node<V> parent = right;
				while(parent.left != null) parent = parent.left;
				return parent;
			}
			Node<V> parent = this.parent;
			Node<V> control = this;
			while(parent != null && control == parent.right) {
				control = parent;
				parent = parent.parent;
			}
			return parent;
		}
		
		Node<V> previous() {
			if(left != null) {
				Node<V> parent = left;
				while(parent.right != null) parent = parent.right;
				return parent;
			}
			Node<V> parent = this.parent;
			Node<V> control = this;
			while(parent != null && control == parent.left) {
				control = parent;
				parent = parent.parent;
			}
			return parent;
		}
	}
}