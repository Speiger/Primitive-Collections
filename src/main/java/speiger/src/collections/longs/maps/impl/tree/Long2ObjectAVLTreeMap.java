package speiger.src.collections.longs.maps.impl.tree;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.BiFunction;

import speiger.src.collections.longs.collections.LongBidirectionalIterator;
import speiger.src.collections.longs.functions.LongComparator;
import speiger.src.collections.longs.functions.LongConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectLongConsumer;
import speiger.src.collections.longs.functions.function.Long2BooleanFunction;
import speiger.src.collections.longs.functions.consumer.LongObjectConsumer;
import speiger.src.collections.longs.functions.function.Long2ObjectFunction;
import speiger.src.collections.longs.functions.function.LongObjectUnaryOperator;
import speiger.src.collections.longs.functions.function.LongLongUnaryOperator;
import speiger.src.collections.longs.maps.abstracts.AbstractLong2ObjectMap;
import speiger.src.collections.longs.maps.interfaces.Long2ObjectMap;
import speiger.src.collections.longs.maps.interfaces.Long2ObjectNavigableMap;
import speiger.src.collections.longs.sets.AbstractLongSet;
import speiger.src.collections.longs.sets.LongNavigableSet;
import speiger.src.collections.longs.sets.LongSet;
import speiger.src.collections.longs.sets.LongSortedSet;
import speiger.src.collections.longs.utils.maps.Long2ObjectMaps;
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
 * A Simple Type Specific AVL TreeMap implementation that reduces boxing/unboxing.
 * It is using a bit more memory then <a href="https://github.com/vigna/fastutil">FastUtil</a>,
 * but it saves a lot of Performance on the Optimized removal and iteration logic.
 * Which makes the implementation actually useable and does not get outperformed by Javas default implementation.
 * @param <V> the type of elements maintained by this Collection
 */
public class Long2ObjectAVLTreeMap<V> extends AbstractLong2ObjectMap<V> implements Long2ObjectNavigableMap<V>
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
	protected transient LongComparator comparator;
	
	/** the default return value for max searches */
	protected long defaultMaxNotFound = Long.MIN_VALUE;
	/** the default return value for min searches */
	protected long defaultMinNotFound = Long.MAX_VALUE;
	
	/** KeySet Cache */
	protected LongNavigableSet keySet;
	/** Values Cache */
	protected ObjectCollection<V> values;
	/** EntrySet Cache */
	protected ObjectSet<Long2ObjectMap.Entry<V>> entrySet;
	
	/**
	 * Default Constructor
	 */
	public Long2ObjectAVLTreeMap() {
	}
	
	/**
	 * Constructor that allows to define the sorter
	 * @param comp the function that decides how the tree is sorted, can be null
	 */
	public Long2ObjectAVLTreeMap(LongComparator comp) {
		comparator = comp;
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Long2ObjectAVLTreeMap(Long[] keys, V[] values) {
		this(keys, values, null);
	}
	
	/**
	 * Helper constructor that has a custom sorter and allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param comp the function that decides how the tree is sorted, can be null
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Long2ObjectAVLTreeMap(Long[] keys, V[] values, LongComparator comp) {
		comparator = comp;
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i].longValue(), values[i]);
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Long2ObjectAVLTreeMap(long[] keys, V[] values) {
		this(keys, values, null);
	}
	
	/**
	 * Helper constructor that has a custom sorter and allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param comp the function that decides how the tree is sorted, can be null
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Long2ObjectAVLTreeMap(long[] keys, V[] values, LongComparator comp) {
		comparator = comp;
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i], values[i]);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 */
	public Long2ObjectAVLTreeMap(Map<? extends Long, ? extends V> map) {
		this(map, null);
	}
	
	/**
	 * A Helper constructor that has a custom sorter and allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param comp the function that decides how the tree is sorted, can be null
	 */
	public Long2ObjectAVLTreeMap(Map<? extends Long, ? extends V> map, LongComparator comp) {
		comparator = comp;
		putAll(map);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 */
	public Long2ObjectAVLTreeMap(Long2ObjectMap<V> map) {
		this(map, null);
	}
	
	/**
	 * A Type Specific Helper function that has a custom sorter and allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param comp the function that decides how the tree is sorted, can be null
 	 */
	public Long2ObjectAVLTreeMap(Long2ObjectMap<V> map, LongComparator comp) {
		comparator = comp;
		putAll(map);
	}

	@Override
	public void setDefaultMaxValue(long value) { defaultMaxNotFound = value; }
	@Override
	public long getDefaultMaxValue() { return defaultMaxNotFound; }
	@Override
	public void setDefaultMinValue(long value) { defaultMinNotFound = value; }
	@Override
	public long getDefaultMinValue() { return defaultMinNotFound; }
	
	
	@Override
	public V put(long key, V value) {
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
	public V putIfAbsent(long key, V value) {
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
	public LongComparator comparator() { return comparator; }

	@Override
	public boolean containsKey(long key) {
		return findNode(key) != null;
	}
	
	@Override
	public V get(long key) {
		Node<V> node = findNode(key);
		return node == null ? getDefaultReturnValue() : node.value;
	}
	
	@Override
	public V getOrDefault(long key, V defaultValue) {
		Node<V> node = findNode(key);
		return node == null ? defaultValue : node.value;
	}
	
	@Override
	public long firstLongKey() {
		if(tree == null) throw new NoSuchElementException();
		return first.key;
	}
	
	@Override
	public long pollFirstLongKey() {
		if(tree == null) return getDefaultMinValue();
		long result = first.key;
		removeNode(first);
		return result;
	}
	
	@Override
	public long lastLongKey() {
		if(tree == null) throw new NoSuchElementException();
		return last.key;
	}
	
	@Override
	public long pollLastLongKey() {
		if(tree == null) return getDefaultMaxValue();
		long result = last.key;
		removeNode(last);
		return result;
	}
	
	@Override
	public Long2ObjectMap.Entry<V> firstEntry() {
		if(tree == null) return null;
		return first.export();
	}
	
	@Override
	public Long2ObjectMap.Entry<V> lastEntry() {
		if(tree == null) return null;
		return last.export();
	}
	
	@Override
	public Long2ObjectMap.Entry<V> pollFirstEntry() {
		if(tree == null) return null;
		BasicEntry<V> entry = first.export();
		removeNode(first);
		return entry;
	}
	
	@Override
	public Long2ObjectMap.Entry<V> pollLastEntry() {
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
	public V remove(long key) {
		Node<V> entry = findNode(key);
		if(entry == null) return getDefaultReturnValue();
		V value = entry.value;
		removeNode(entry);
		return value;
	}
	
	@Override
	public V removeOrDefault(long key, V defaultValue) {
		Node<V> entry = findNode(key);
		if(entry == null) return defaultValue;
		V value = entry.value;
		removeNode(entry);
		return value;
	}
	
	@Override
	public boolean remove(long key, V value) {
		Node<V> entry = findNode(key);
		if(entry == null || entry.value != value) return false;
		removeNode(entry);
		return true;
	}
	
	@Override
	public boolean replace(long key, V oldValue, V newValue) {
		Node<V> entry = findNode(key);
		if(entry == null || entry.value != oldValue) return false;
		entry.value = newValue;
		return true;
	}
	
	@Override
	public V replace(long key, V value) {
		Node<V> entry = findNode(key);
		if(entry == null) return getDefaultReturnValue();
		V oldValue = entry.value;
		entry.value = value;
		return oldValue;
	}
	
	@Override
	public V compute(long key, LongObjectUnaryOperator<V> mappingFunction) {
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
	public V computeIfAbsent(long key, Long2ObjectFunction<V> mappingFunction) {
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
	public V supplyIfAbsent(long key, ObjectSupplier<V> valueProvider) {
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
	public V computeIfPresent(long key, LongObjectUnaryOperator<V> mappingFunction) {
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
	public V merge(long key, V value, ObjectObjectUnaryOperator<V, V> mappingFunction) {
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
	public void mergeAll(Long2ObjectMap<V> m, ObjectObjectUnaryOperator<V, V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Long2ObjectMap.Entry<V> entry : Long2ObjectMaps.fastIterable(m)) {
			long key = entry.getLongKey();
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
	public void forEach(LongObjectConsumer<V> action) {
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
	
	protected LongBidirectionalIterator keyIterator() {
		return new AscendingKeyIterator(first);
	}
	
	protected LongBidirectionalIterator keyIterator(long element) {
		return new AscendingKeyIterator(findNode(element));
	}
	
	protected LongBidirectionalIterator descendingKeyIterator() {
		return new DescendingKeyIterator(last);
	}
		
	@Override
	public Long2ObjectAVLTreeMap<V> copy() {
		Long2ObjectAVLTreeMap<V> set = new Long2ObjectAVLTreeMap<>();
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
	public LongSortedSet keySet() {
		return navigableKeySet();
	}
	
	@Override
	public ObjectSet<Long2ObjectMap.Entry<V>> long2ObjectEntrySet() {
		if(entrySet == null) entrySet = new EntrySet();
		return entrySet;
	}
	
	@Override
	public ObjectCollection<V> values() {
		if(values == null) values = new Values();
		return values;
	}
	
	@Override
	public LongNavigableSet navigableKeySet() {
		if(keySet == null) keySet = new KeySet<>(this);
		return keySet;
	}
	
	@Override
	public Long2ObjectNavigableMap<V> descendingMap() {
		return new DescendingNaivgableSubMap<>(this, true, 0L, true, true, 0L, true);
	}
	
	@Override
	public LongNavigableSet descendingKeySet() {
		return descendingMap().navigableKeySet();
	}
	
	@Override
	public Long2ObjectNavigableMap<V> subMap(long fromKey, boolean fromInclusive, long toKey, boolean toInclusive) {
		return new AscendingNaivgableSubMap<>(this, false, fromKey, fromInclusive, false, toKey, toInclusive);
	}
	
	@Override
	public Long2ObjectNavigableMap<V> headMap(long toKey, boolean inclusive) {
		return new AscendingNaivgableSubMap<>(this, true, 0L, true, false, toKey, inclusive);
	}
	
	@Override
	public Long2ObjectNavigableMap<V> tailMap(long fromKey, boolean inclusive) {
		return new AscendingNaivgableSubMap<>(this, false, fromKey, inclusive, true, 0L, true);
	}
	
	@Override
	public long lowerKey(long e) {
		Node<V> node = findLowerNode(e);
		return node != null ? node.key : getDefaultMinValue();
	}

	@Override
	public long floorKey(long e) {
		Node<V> node = findFloorNode(e);
		return node != null ? node.key : getDefaultMinValue();
	}
	
	@Override
	public long higherKey(long e) {
		Node<V> node = findHigherNode(e);
		return node != null ? node.key : getDefaultMaxValue();
	}

	@Override
	public long ceilingKey(long e) {
		Node<V> node = findCeilingNode(e);
		return node != null ? node.key : getDefaultMaxValue();
	}
	
	@Override
	public Long2ObjectMap.Entry<V> lowerEntry(long key) {
		Node<V> node = findLowerNode(key);
		return node != null ? node.export() : null;
	}
	
	@Override
	public Long2ObjectMap.Entry<V> higherEntry(long key) {
		Node<V> node = findHigherNode(key);
		return node != null ? node.export() : null;
	}
	
	@Override
	public Long2ObjectMap.Entry<V> floorEntry(long key) {
		Node<V> node = findFloorNode(key);
		return node != null ? node.export() : null;
	}
	
	@Override
	public Long2ObjectMap.Entry<V> ceilingEntry(long key) {
		Node<V> node = findCeilingNode(key);
		return node != null ? node.export() : null;
	}
	
	protected Node<V> findLowerNode(long key) {
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
	
	protected Node<V> findFloorNode(long key) {
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
	
	protected Node<V> findCeilingNode(long key) {
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
	
	protected Node<V> findHigherNode(long key) {
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
	
	protected Node<V> findNode(long key) {
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
		if(entry.previous() == null) first = entry.next();
		if(entry.next() == null) last = entry.previous();
		Node<V> replacement = entry.left != null ? entry.left : entry.right;
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
	
	protected void validate(long k) { compare(k, k); }
	protected int compare(long k, long v) { return comparator != null ? comparator.compare(k, v) : Long.compare(k, v);}
	
	/** From CLR */
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
			entry.updateHeight();
			right.updateHeight();
		}
	}
	
	/** From CLR */
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
			entry.updateHeight();
			left.updateHeight();
		}
	}
	
	/** From CLR */
	protected void fixAfterInsertion(Node<V> entry) {
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
	protected void fixAfterDeletion(Node<V> entry) {
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
	
	static class KeySet<V> extends AbstractLongSet implements LongNavigableSet
	{
		Long2ObjectNavigableMap<V> map;

		public KeySet(Long2ObjectNavigableMap<V> map) {
			this.map = map;
		}
		
		@Override
		public void setDefaultMaxValue(long e) { map.setDefaultMaxValue(e); }
		@Override
		public long getDefaultMaxValue() { return map.getDefaultMaxValue(); }
		@Override
		public void setDefaultMinValue(long e) { map.setDefaultMinValue(e); }
		@Override
		public long getDefaultMinValue() { return map.getDefaultMinValue(); }
		@Override
		public long lower(long e) { return map.lowerKey(e); }
		@Override
		public long floor(long e) { return map.floorKey(e); }
		@Override
		public long ceiling(long e) { return map.ceilingKey(e); }
		@Override
		public long higher(long e) { return map.higherKey(e); }
		
		@Override
		public Long lower(Long e) {
			Long2ObjectMap.Entry<V> node = map.lowerEntry(e.longValue());
			return node != null ? node.getKey() : null;
		}
		
		@Override
		public Long floor(Long e) {
			Long2ObjectMap.Entry<V> node = map.floorEntry(e.longValue());
			return node != null ? node.getKey() : null;
		}
		
		@Override
		public Long higher(Long e) {
			Long2ObjectMap.Entry<V> node = map.higherEntry(e.longValue());
			return node != null ? node.getKey() : null;
		}
		
		@Override
		public Long ceiling(Long e) {
			Long2ObjectMap.Entry<V> node = map.ceilingEntry(e.longValue());
			return node != null ? node.getKey() : null;
		}
		
		@Override
		public long pollFirstLong() { return map.pollFirstLongKey(); }
		@Override
		public long pollLastLong() { return map.pollLastLongKey(); }
		@Override
		public LongComparator comparator() { return map.comparator(); }
		@Override
		public long firstLong() { return map.firstLongKey(); } 
		@Override
		public long lastLong() { return map.lastLongKey(); }
		@Override
		public void clear() { map.clear(); }
		
		@Override
		public boolean remove(long o) { 
			int oldSize = map.size();
			map.remove(o); 
			return oldSize != map.size();
		}
		
		@Override
		public boolean add(long e) { throw new UnsupportedOperationException(); }
		
		@Override
		public LongBidirectionalIterator iterator(long fromElement) {
			if(map instanceof Long2ObjectAVLTreeMap) return ((Long2ObjectAVLTreeMap<V>)map).keyIterator(fromElement);
			return ((NavigableSubMap<V>)map).keyIterator(fromElement);
		}
		
		@Override
		public LongNavigableSet subSet(long fromElement, boolean fromInclusive, long toElement, boolean toInclusive) { return new KeySet<>(map.subMap(fromElement, fromInclusive, toElement, toInclusive)); }
		@Override
		public LongNavigableSet headSet(long toElement, boolean inclusive) { return new KeySet<>(map.headMap(toElement, inclusive)); }
		@Override
		public LongNavigableSet tailSet(long fromElement, boolean inclusive) { return new KeySet<>(map.tailMap(fromElement, inclusive)); }
		
		@Override
		public LongBidirectionalIterator iterator() {
			if(map instanceof Long2ObjectAVLTreeMap) return ((Long2ObjectAVLTreeMap<V>)map).keyIterator();
			return ((NavigableSubMap<V>)map).keyIterator();
		}
		
		@Override
		public LongBidirectionalIterator descendingIterator() {
			if(map instanceof Long2ObjectAVLTreeMap) return ((Long2ObjectAVLTreeMap<V>)map).descendingKeyIterator();
			return ((NavigableSubMap<V>)map).descendingKeyIterator();
		}
		
		protected Node<V> start() {
			if(map instanceof Long2ObjectAVLTreeMap) return ((Long2ObjectAVLTreeMap<V>)map).first;
			return ((NavigableSubMap<V>)map).subLowest();
		}
		
		protected Node<V> end() {
			if(map instanceof Long2ObjectAVLTreeMap) return null;
			return ((NavigableSubMap<V>)map).subHighest();
		}
		
		protected Node<V> next(Node<V> entry) {
			if(map instanceof Long2ObjectAVLTreeMap) return entry.next();
			return ((NavigableSubMap<V>)map).next(entry);
		}
		
		protected Node<V> previous(Node<V> entry) {
			if(map instanceof Long2ObjectAVLTreeMap) return entry.previous();
			return ((NavigableSubMap<V>)map).previous(entry);
		}
		
		@Override
		public LongNavigableSet descendingSet() { return new KeySet<>(map.descendingMap()); }
		@Override
		public KeySet<V> copy() { throw new UnsupportedOperationException(); }
		@Override
		public boolean isEmpty() { return map.isEmpty(); }
		@Override
		public int size() { return map.size(); }
		
		@Override
		public void forEach(LongConsumer action) {
			Objects.requireNonNull(action);
			for(Node<V> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				action.accept(entry.key);
		}
		
		@Override
		public <E> void forEach(E input, ObjectLongConsumer<E> action) {
			Objects.requireNonNull(action);
			for(Node<V> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				action.accept(input, entry.key);
		}
		
		@Override
		public boolean matchesAny(Long2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(Node<V> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				if(filter.get(entry.key)) return true;
			return false;
		}
		
		@Override
		public boolean matchesNone(Long2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(Node<V> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				if(filter.get(entry.key)) return false;
			return true;
		}
		
		@Override
		public boolean matchesAll(Long2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(Node<V> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				if(!filter.get(entry.key)) return false;
			return true;
		}
		
		@Override
		public long reduce(long identity, LongLongUnaryOperator operator) {
			Objects.requireNonNull(operator);
			long state = identity;
			for(Node<V> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				state = operator.applyAsLong(state, entry.key);
			return state;
		}
		
		@Override
		public long reduce(LongLongUnaryOperator operator) {
			Objects.requireNonNull(operator);
			long state = 0L;
			boolean empty = true;
			for(Node<V> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry)) {
				if(empty) {
					empty = false;
					state = entry.key;
					continue;
				}
				state = operator.applyAsLong(state, entry.key);
			}
			return state;
		}
		
		@Override
		public long findFirst(Long2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(Node<V> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				if(filter.get(entry.key)) return entry.key;
			return 0L;
		}
		
		@Override
		public int count(Long2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			int result = 0;
			for(Node<V> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				if(filter.get(entry.key)) result++;
			return result;
		}
	}
	
	static class AscendingNaivgableSubMap<V> extends NavigableSubMap<V>
	{
		AscendingNaivgableSubMap(Long2ObjectAVLTreeMap<V> map, boolean fromStart, long lo, boolean loInclusive, boolean toEnd, long hi, boolean hiInclusive) {
			super(map, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
		}
		
		@Override
		public Long2ObjectNavigableMap<V> descendingMap() {
			if(inverse == null) inverse = new DescendingNaivgableSubMap<>(map, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
			return inverse;
		}
		
		@Override
		public ObjectSet<Long2ObjectMap.Entry<V>> long2ObjectEntrySet() {
			if(entrySet == null) entrySet = new AscendingSubEntrySet();
			return entrySet;
		}
		
		@Override
		public LongNavigableSet navigableKeySet() {
			if(keySet == null) keySet = new KeySet<>(this);
			return keySet;
		}
		
		@Override
		public Long2ObjectNavigableMap<V> subMap(long fromKey, boolean fromInclusive, long toKey, boolean toInclusive) {
			if (!inRange(fromKey, fromInclusive)) throw new IllegalArgumentException("fromKey out of range");
			if (!inRange(toKey, toInclusive)) throw new IllegalArgumentException("toKey out of range");
			return new AscendingNaivgableSubMap<>(map, false, fromKey, fromInclusive, false, toKey, toInclusive);
		}
		
		@Override
		public Long2ObjectNavigableMap<V> headMap(long toKey, boolean inclusive) {
			if (!inRange(toKey, inclusive)) throw new IllegalArgumentException("toKey out of range");
			return new AscendingNaivgableSubMap<>(map, fromStart, lo, loInclusive, false, toKey, inclusive);
		}
		
		@Override
		public Long2ObjectNavigableMap<V> tailMap(long fromKey, boolean inclusive) {
			if (!inRange(fromKey, inclusive)) throw new IllegalArgumentException("fromKey out of range");
			return new AscendingNaivgableSubMap<>(map, false, fromKey, inclusive, toEnd, hi, hiInclusive);
		}
		
		@Override
		protected Node<V> subLowest() { return absLowest(); }
		@Override
		protected Node<V> subHighest() { return absHighest(); }
		@Override
		protected Node<V> subCeiling(long key) { return absCeiling(key); }
		@Override
		protected Node<V> subHigher(long key) { return absHigher(key); }
		@Override
		protected Node<V> subFloor(long key) { return absFloor(key); }
		@Override
		protected Node<V> subLower(long key) { return absLower(key); }
		
		@Override
		protected LongBidirectionalIterator keyIterator() {
			return new AcsendingSubKeyIterator(absLowest(), absHighFence(), absLowFence()); 
		}
		@Override
		protected LongBidirectionalIterator keyIterator(long element) {
			return new AcsendingSubKeyIterator(absLower(element), absHighFence(), absLowFence()); 
		}
		
		@Override
		protected ObjectBidirectionalIterator<V> valueIterator() {
			return new AcsendingSubValueIterator(absLowest(), absHighFence(), absLowFence()); 
		}
		
		@Override
		protected LongBidirectionalIterator descendingKeyIterator() {
			return new DecsendingSubKeyIterator(absHighest(), absLowFence(), absHighFence()); 
		}
		
		class AscendingSubEntrySet extends SubEntrySet {
			@Override
			public ObjectIterator<Long2ObjectMap.Entry<V>> iterator() {
				return new AcsendingSubEntryIterator(absLowest(), absHighFence(), absLowFence());
			}
		}
	}
	
	static class DescendingNaivgableSubMap<V> extends NavigableSubMap<V>
	{
		LongComparator comparator;
		DescendingNaivgableSubMap(Long2ObjectAVLTreeMap<V> map, boolean fromStart, long lo, boolean loInclusive, boolean toEnd, long hi, boolean hiInclusive) {
			super(map, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
			comparator = map.comparator() == null ? LongComparator.of(Collections.reverseOrder()) : map.comparator().reversed();
		}
		
		@Override
		public LongComparator comparator() { return comparator; }
		
		@Override
		public Long2ObjectNavigableMap<V> descendingMap() {
			if(inverse == null) inverse = new AscendingNaivgableSubMap<>(map, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
			return inverse;
		}

		@Override
		public LongNavigableSet navigableKeySet() {
			if(keySet == null) keySet = new KeySet<>(this);
			return keySet;
		}
		
		@Override
		public Long2ObjectNavigableMap<V> subMap(long fromKey, boolean fromInclusive, long toKey, boolean toInclusive) {
			if (!inRange(fromKey, fromInclusive)) throw new IllegalArgumentException("fromKey out of range");
			if (!inRange(toKey, toInclusive)) throw new IllegalArgumentException("toKey out of range");
			return new DescendingNaivgableSubMap<>(map, false, toKey, toInclusive, false, fromKey, fromInclusive);
		}
		
		@Override
		public Long2ObjectNavigableMap<V> headMap(long toKey, boolean inclusive) {
			if (!inRange(toKey, inclusive)) throw new IllegalArgumentException("toKey out of range");
			return new DescendingNaivgableSubMap<>(map, false, toKey, inclusive, toEnd, hi, hiInclusive);
		}
		
		@Override
		public Long2ObjectNavigableMap<V> tailMap(long fromKey, boolean inclusive) {
			if (!inRange(fromKey, inclusive)) throw new IllegalArgumentException("fromKey out of range");
			return new DescendingNaivgableSubMap<>(map, fromStart, lo, loInclusive, false, fromKey, inclusive);
		}
		
		@Override
		public ObjectSet<Long2ObjectMap.Entry<V>> long2ObjectEntrySet() {
			if(entrySet == null) entrySet = new DescendingSubEntrySet();
			return entrySet;
		}
		
		@Override
		protected Node<V> subLowest() { return absHighest(); }
		@Override
		protected Node<V> subHighest() { return absLowest(); }
		@Override
		protected Node<V> subCeiling(long key) { return absFloor(key); }
		@Override
		protected Node<V> subHigher(long key) { return absLower(key); }
		@Override
		protected Node<V> subFloor(long key) { return absCeiling(key); }
		@Override
		protected Node<V> subLower(long key) { return absHigher(key); }
		@Override
		protected Node<V> next(Node<V> entry) { return entry.previous(); }
		@Override
		protected Node<V> previous(Node<V> entry) { return entry.next(); }
		
		@Override
		protected LongBidirectionalIterator keyIterator() {
			return new DecsendingSubKeyIterator(absHighest(), absLowFence(), absHighFence()); 
		}
		
		@Override
		protected LongBidirectionalIterator keyIterator(long element) {
			return new DecsendingSubKeyIterator(absHigher(element), absLowFence(), absHighFence()); 
		}
		
		@Override
		protected ObjectBidirectionalIterator<V> valueIterator() {
			return new DecsendingSubValueIterator(absHighest(), absLowFence(), absHighFence()); 
		}
		
		@Override
		protected LongBidirectionalIterator descendingKeyIterator() {
			return new AcsendingSubKeyIterator(absLowest(), absHighFence(), absLowFence()); 
		}
		
		class DescendingSubEntrySet extends SubEntrySet {
			@Override
			public ObjectIterator<Long2ObjectMap.Entry<V>> iterator() {
				return new DecsendingSubEntryIterator(absHighest(), absLowFence(), absHighFence());
			}
		}
	}
	
	static abstract class NavigableSubMap<V> extends AbstractLong2ObjectMap<V> implements Long2ObjectNavigableMap<V>
	{
		final Long2ObjectAVLTreeMap<V> map;
		final long lo, hi;
		final boolean fromStart, toEnd;
		final boolean loInclusive, hiInclusive;
		
		Long2ObjectNavigableMap<V> inverse;
		LongNavigableSet keySet;
		ObjectSet<Long2ObjectMap.Entry<V>> entrySet;
		ObjectCollection<V> values;
		
		NavigableSubMap(Long2ObjectAVLTreeMap<V> map, boolean fromStart, long lo, boolean loInclusive, boolean toEnd, long hi, boolean hiInclusive) {
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
		public void setDefaultMaxValue(long e) { map.setDefaultMaxValue(e); }
		@Override
		public long getDefaultMaxValue() { return map.getDefaultMaxValue(); }
		@Override
		public void setDefaultMinValue(long e) { map.setDefaultMinValue(e); }
		@Override
		public long getDefaultMinValue() { return map.getDefaultMinValue(); }
		protected boolean isNullComparator() { return map.comparator() == null; }
		
		@Override
		public AbstractLong2ObjectMap<V> setDefaultReturnValue(V v) { 
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
		public LongNavigableSet descendingKeySet() {
			return descendingMap().navigableKeySet();
		}
		
		@Override
		public LongSet keySet() {
			return navigableKeySet();
		}
		
		protected abstract Node<V> subLowest();
		protected abstract Node<V> subHighest();
		protected abstract Node<V> subCeiling(long key);
		protected abstract Node<V> subHigher(long key);
		protected abstract Node<V> subFloor(long key);
		protected abstract Node<V> subLower(long key);
		protected abstract LongBidirectionalIterator keyIterator();
		protected abstract LongBidirectionalIterator keyIterator(long element);
		protected abstract ObjectBidirectionalIterator<V> valueIterator();
		protected abstract LongBidirectionalIterator descendingKeyIterator();
		protected long lowKeyOrNull(Node<V> entry) { return entry == null ? getDefaultMinValue() : entry.key; }
		protected long highKeyOrNull(Node<V> entry) { return entry == null ? getDefaultMaxValue() : entry.key; }
		protected Node<V> next(Node<V> entry) { return entry.next(); }
		protected Node<V> previous(Node<V> entry) { return entry.previous(); }
		
		protected boolean tooLow(long key) {
			if (!fromStart) {
				int c = map.compare(key, lo);
				if (c < 0 || (c == 0 && !loInclusive)) return true;
			}
			return false;
		}
		
		protected boolean tooHigh(long key) {
			if (!toEnd) {
				int c = map.compare(key, hi);
				if (c > 0 || (c == 0 && !hiInclusive)) return true;
			}
			return false;
		}
		protected boolean inRange(long key) { return !tooLow(key) && !tooHigh(key); }
		protected boolean inClosedRange(long key) { return (fromStart || map.compare(key, lo) >= 0) && (toEnd || map.compare(hi, key) >= 0); }
		protected boolean inRange(long key, boolean inclusive) { return inclusive ? inRange(key) : inClosedRange(key); }
		
		protected Node<V> absLowest() {
			Node<V> e = (fromStart ?  map.first : (loInclusive ? map.findCeilingNode(lo) : map.findHigherNode(lo)));
			return (e == null || tooHigh(e.key)) ? null : e;
		}
		
		protected Node<V> absHighest() {
			Node<V> e = (toEnd ?  map.last : (hiInclusive ?  map.findFloorNode(hi) : map.findLowerNode(hi)));
			return (e == null || tooLow(e.key)) ? null : e;
		}
		
		protected Node<V> absCeiling(long key) {
			if (tooLow(key)) return absLowest();
			Node<V> e = map.findCeilingNode(key);
			return (e == null || tooHigh(e.key)) ? null : e;
		}
		
		protected Node<V> absHigher(long key) {
			if (tooLow(key)) return absLowest();
			Node<V> e = map.findHigherNode(key);
			return (e == null || tooHigh(e.key)) ? null : e;
		}
		
		protected Node<V> absFloor(long key) {
			if (tooHigh(key)) return absHighest();
			Node<V> e = map.findFloorNode(key);
			return (e == null || tooLow(e.key)) ? null : e;
		}
		
		protected Node<V> absLower(long key) {
			if (tooHigh(key)) return absHighest();
			Node<V> e = map.findLowerNode(key);
			return (e == null || tooLow(e.key)) ? null : e;
		}
		
		protected Node<V> absHighFence() { return (toEnd ? null : (hiInclusive ? map.findHigherNode(hi) : map.findCeilingNode(hi))); }
		protected Node<V> absLowFence() { return (fromStart ? null : (loInclusive ?  map.findLowerNode(lo) : map.findFloorNode(lo))); }
		
		@Override
		public LongComparator comparator() { return map.comparator(); }
		
		@Override
		public long pollFirstLongKey() {
			Node<V> entry = subLowest();
			if(entry != null) {
				long result = entry.key;
				map.removeNode(entry);
				return result;
			}
			return getDefaultMinValue();
		}
		
		@Override
		public long pollLastLongKey() {
			Node<V> entry = subHighest();
			if(entry != null) {
				long result = entry.key;
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
		public long firstLongKey() {
			Node<V> entry = subLowest();
			if(entry == null) throw new NoSuchElementException();
			return entry.key;
		}
		
		@Override
		public long lastLongKey() {
			Node<V> entry = subHighest();
			if(entry == null) throw new NoSuchElementException();
			return entry.key;
		}
		
		@Override
		public V put(long key, V value) {
			if (!inRange(key)) throw new IllegalArgumentException("key out of range");
			return map.put(key, value);
		}
		
		@Override
		public V putIfAbsent(long key, V value) {
			if (!inRange(key)) throw new IllegalArgumentException("key out of range");
			return map.putIfAbsent(key, value);
		}
		
		@Override
		public boolean containsKey(long key) { return inRange(key) && map.containsKey(key); }
		
		@Override
		public V computeIfPresent(long key, LongObjectUnaryOperator<V> mappingFunction) {
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
		public V remove(long key) {
			return inRange(key) ? map.remove(key) : getDefaultReturnValue();
		}
		
		@Override
		public V removeOrDefault(long key, V defaultValue) {
			return inRange(key) ? map.removeOrDefault(key, defaultValue) : defaultValue;
		}
		
		@Override
		public boolean remove(long key, V value) {
			return inRange(key) && map.remove(key, value);
		}
		
		
		@Override
		public V get(long key) {
			return inRange(key) ? map.get(key) : getDefaultReturnValue();
		}
		
		@Override
		public V getOrDefault(long key, V defaultValue) {
			return inRange(key) ? map.getOrDefault(key, defaultValue) : getDefaultReturnValue();
		}
		
		
		@Override
		public long lowerKey(long key) { return lowKeyOrNull(subLower(key)); }
		@Override
		public long floorKey(long key) { return lowKeyOrNull(subFloor(key)); }
		@Override
		public long ceilingKey(long key) { return highKeyOrNull(subCeiling(key)); }
		@Override
		public long higherKey(long key) { return highKeyOrNull(subHigher(key)); }
		@Override
		public Long2ObjectMap.Entry<V> lowerEntry(long key) { return subLower(key); }
		@Override
		public Long2ObjectMap.Entry<V> floorEntry(long key) { return subFloor(key); }
		@Override
		public Long2ObjectMap.Entry<V> ceilingEntry(long key) { return subCeiling(key); }
		@Override
		public Long2ObjectMap.Entry<V> higherEntry(long key) { return subHigher(key); }
		
		@Override
		public boolean isEmpty() {
			if(fromStart && toEnd) return map.isEmpty();
			Node<V> n = absLowest();
			return n == null || tooHigh(n.key);
		}
		
		@Override
		public int size() { return fromStart && toEnd ? map.size() : entrySet().size(); }
		
		@Override
		public Long2ObjectNavigableMap<V> copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public Long2ObjectMap.Entry<V> firstEntry() {
			Node<V> entry = subLowest();
			return entry == null ? null : entry.export();
		}

		@Override
		public Long2ObjectMap.Entry<V> lastEntry() {
			Node<V> entry = subHighest();
			return entry == null ? null : entry.export();
		}

		@Override
		public Long2ObjectMap.Entry<V> pollFirstEntry() {
			Node<V> entry = subLowest();
			if(entry != null) {
				Long2ObjectMap.Entry<V> result = entry.export();
				map.removeNode(entry);
				return result;
			}
			return null;
		}

		@Override
		public Long2ObjectMap.Entry<V> pollLastEntry() {
			Node<V> entry = subHighest();
			if(entry != null) {
				Long2ObjectMap.Entry<V> result = entry.export();
				map.removeNode(entry);
				return result;
			}
			return null;
		}
		
		abstract class SubEntrySet extends AbstractObjectSet<Long2ObjectMap.Entry<V>> {
			@Override
			public int size() {
				if (fromStart && toEnd) return map.size();
				int size = 0;
				for(ObjectIterator<Long2ObjectMap.Entry<V>> iter = iterator();iter.hasNext();iter.next(),size++);
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
				if(o instanceof Long2ObjectMap.Entry)
				{
					Long2ObjectMap.Entry<V> entry = (Long2ObjectMap.Entry<V>) o;
					long key = entry.getLongKey();
					if (!inRange(key)) return false;
					Node<V> node = map.findNode(key);
					return node != null && Objects.equals(entry.getValue(), node.getValue());
				}
				Map.Entry<?,?> entry = (Map.Entry<?,?>) o;
				if(entry.getKey() == null && isNullComparator()) return false;
				Long key = (Long)entry.getKey();
				if (!inRange(key)) return false;
				Node<V> node = map.findNode(key);
				return node != null && Objects.equals(entry.getValue(), node.getValue());
			}
			
			@Override
			public boolean remove(Object o) {
				if (!(o instanceof Map.Entry)) return false;
				if(o instanceof Long2ObjectMap.Entry)
				{
					Long2ObjectMap.Entry<V> entry = (Long2ObjectMap.Entry<V>) o;
					long key = entry.getLongKey();
					if (!inRange(key)) return false;
					Node<V> node = map.findNode(key);
					if (node != null && Objects.equals(node.getValue(), entry.getValue())) {
						map.removeNode(node);
						return true;
					}
					return false;
				}
				Map.Entry<?,?> entry = (Map.Entry<?,?>) o;
				Long key = (Long)entry.getKey();
				if (!inRange(key)) return false;
				Node<V> node = map.findNode(key);
				if (node != null && Objects.equals(node.getValue(), entry.getValue())) {
					map.removeNode(node);
					return true;
				}
				return false;
			}
			
			@Override
			public void forEach(Consumer<? super Long2ObjectMap.Entry<V>> action) {
				Objects.requireNonNull(action);
				for(Node<V> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					action.accept(new BasicEntry<>(entry.key, entry.value));
			}
			
			@Override
			public <E> void forEach(E input, ObjectObjectConsumer<E, Long2ObjectMap.Entry<V>> action) {
				Objects.requireNonNull(action);
				for(Node<V> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					action.accept(input, new BasicEntry<>(entry.key, entry.value));
			}
			
			@Override
			public boolean matchesAny(Object2BooleanFunction<Long2ObjectMap.Entry<V>> filter) {
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
			public boolean matchesNone(Object2BooleanFunction<Long2ObjectMap.Entry<V>> filter) {
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
			public boolean matchesAll(Object2BooleanFunction<Long2ObjectMap.Entry<V>> filter) {
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
			public <E> E reduce(E identity, BiFunction<E, Long2ObjectMap.Entry<V>, E> operator) {
				Objects.requireNonNull(operator);
				E state = identity;
				for(Node<V> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					state = operator.apply(state, new BasicEntry<>(entry.key, entry.value));
				}
				return state;
			}
			
			@Override
			public Long2ObjectMap.Entry<V> reduce(ObjectObjectUnaryOperator<Long2ObjectMap.Entry<V>, Long2ObjectMap.Entry<V>> operator) {
				Objects.requireNonNull(operator);
				Long2ObjectMap.Entry<V> state = null;
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
			public Long2ObjectMap.Entry<V> findFirst(Object2BooleanFunction<Long2ObjectMap.Entry<V>> filter) {
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
			public int count(Object2BooleanFunction<Long2ObjectMap.Entry<V>> filter) {
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
		
		class DecsendingSubEntryIterator extends SubMapEntryIterator implements ObjectBidirectionalIterator<Long2ObjectMap.Entry<V>>
		{
			public DecsendingSubEntryIterator(Node<V> first, Node<V> forwardFence, Node<V> backwardFence) {
				super(first, forwardFence, backwardFence);
			}
			
			@Override
			public Long2ObjectMap.Entry<V> previous() {
				if(!hasPrevious()) throw new NoSuchElementException();
				return nextEntry();
			}

			@Override
			public Long2ObjectMap.Entry<V> next() {
				if(!hasNext()) throw new NoSuchElementException();
				return previousEntry();
			}
		}
		
		class AcsendingSubEntryIterator extends SubMapEntryIterator implements ObjectBidirectionalIterator<Long2ObjectMap.Entry<V>>
		{
			public AcsendingSubEntryIterator(Node<V> first, Node<V> forwardFence, Node<V> backwardFence) {
				super(first, forwardFence, backwardFence);
			}

			@Override
			public Long2ObjectMap.Entry<V> previous() {
				if(!hasPrevious()) throw new NoSuchElementException();
				return previousEntry();
			}

			@Override
			public Long2ObjectMap.Entry<V> next() {
				if(!hasNext()) throw new NoSuchElementException();
				return nextEntry();
			}
		}
		
		class DecsendingSubKeyIterator extends SubMapEntryIterator implements LongBidirectionalIterator
		{
			public DecsendingSubKeyIterator(Node<V> first, Node<V> forwardFence, Node<V> backwardFence) {
				super(first, forwardFence, backwardFence);
			}
			
			@Override
			public long previousLong() {
				if(!hasPrevious()) throw new NoSuchElementException();
				return nextEntry().key;
			}

			@Override
			public long nextLong() {
				if(!hasNext()) throw new NoSuchElementException();
				return previousEntry().key;
			}
		}
		
		class AcsendingSubKeyIterator extends SubMapEntryIterator implements LongBidirectionalIterator
		{
			public AcsendingSubKeyIterator(Node<V> first, Node<V> forwardFence, Node<V> backwardFence) {
				super(first, forwardFence, backwardFence);
			}

			@Override
			public long previousLong() {
				if(!hasPrevious()) throw new NoSuchElementException();
				return previousEntry().key;
			}

			@Override
			public long nextLong() {
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
			long forwardFence;
			long backwardFence;
			
			public SubMapEntryIterator(Node<V> first, Node<V> forwardFence, Node<V> backwardFence)
			{
				next = first;
				this.forwardFence = forwardFence == null ? 0L : forwardFence.key;
				this.backwardFence = backwardFence == null ? 0L : backwardFence.key;
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
			Long2ObjectAVLTreeMap.this.clear();
		}
		
		@Override
		public int size() {
			return Long2ObjectAVLTreeMap.this.size;
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
	
	class EntrySet extends AbstractObjectSet<Long2ObjectMap.Entry<V>> {
		
		@Override
		public ObjectIterator<Long2ObjectMap.Entry<V>> iterator() {
			return new AscendingMapEntryIterator(first);
		}
		
		@Override
		public void clear() {
			Long2ObjectAVLTreeMap.this.clear();
		}
		
		@Override
		public int size() {
			return Long2ObjectAVLTreeMap.this.size;
		}
		
		@Override
		public boolean contains(Object o) {
			if (!(o instanceof Map.Entry)) return false;
			if(o instanceof Long2ObjectMap.Entry)
			{
				Long2ObjectMap.Entry<V> entry = (Long2ObjectMap.Entry<V>) o;
				long key = entry.getLongKey();
				Node<V> node = findNode(key);
				return node != null && Objects.equals(entry.getValue(), node.getValue());
			}
			Map.Entry<?,?> entry = (Map.Entry<?,?>) o;
			if(entry.getKey() == null && comparator() == null) return false;
			if(!(entry.getKey() instanceof Long)) return false;
			Long key = (Long)entry.getKey();
			Node<V> node = findNode(key);
			return node != null && Objects.equals(entry.getValue(), node.getValue());
		}
		
		@Override
		public boolean remove(Object o) {
			if (!(o instanceof Map.Entry)) return false;
			if(o instanceof Long2ObjectMap.Entry)
			{
				Long2ObjectMap.Entry<V> entry = (Long2ObjectMap.Entry<V>) o;
				long key = entry.getLongKey();
				Node<V> node = findNode(key);
				if (node != null && Objects.equals(entry.getValue(), node.getValue())) {
					removeNode(node);
					return true;
				}
				return false;
			}
			Map.Entry<?,?> entry = (Map.Entry<?,?>) o;
			Long key = (Long)entry.getKey();
			Node<V> node = findNode(key);
			if (node != null && Objects.equals(entry.getValue(), node.getValue())) {
				removeNode(node);
				return true;
			}
			return false;
		}
		
		@Override
		public void forEach(Consumer<? super Long2ObjectMap.Entry<V>> action) {
			Objects.requireNonNull(action);
			for(Node<V> entry = first;entry != null;entry = entry.next())
				action.accept(new BasicEntry<>(entry.key, entry.value));
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, Long2ObjectMap.Entry<V>> action) {
			Objects.requireNonNull(action);
			for(Node<V> entry = first;entry != null;entry = entry.next())
				action.accept(input, new BasicEntry<>(entry.key, entry.value));
		}
		
		@Override
		public boolean matchesAny(Object2BooleanFunction<Long2ObjectMap.Entry<V>> filter) {
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
		public boolean matchesNone(Object2BooleanFunction<Long2ObjectMap.Entry<V>> filter) {
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
		public boolean matchesAll(Object2BooleanFunction<Long2ObjectMap.Entry<V>> filter) {
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
		public <E> E reduce(E identity, BiFunction<E, Long2ObjectMap.Entry<V>, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			for(Node<V> entry = first;entry != null;entry = entry.next()) {
				state = operator.apply(state, new BasicEntry<>(entry.key, entry.value));
			}
			return state;
		}
		
		@Override
		public Long2ObjectMap.Entry<V> reduce(ObjectObjectUnaryOperator<Long2ObjectMap.Entry<V>, Long2ObjectMap.Entry<V>> operator) {
			Objects.requireNonNull(operator);
			Long2ObjectMap.Entry<V> state = null;
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
		public Long2ObjectMap.Entry<V> findFirst(Object2BooleanFunction<Long2ObjectMap.Entry<V>> filter) {
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
		public int count(Object2BooleanFunction<Long2ObjectMap.Entry<V>> filter) {
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
	
	class DescendingKeyIterator extends MapEntryIterator implements LongBidirectionalIterator
	{
		public DescendingKeyIterator(Node<V> first) {
			super(first);
		}

		@Override
		public long previousLong() {
			if(!hasPrevious()) throw new NoSuchElementException();
			return nextEntry().key;
		}
		
		@Override
		public long nextLong() {
			if(!hasNext()) throw new NoSuchElementException();
			return previousEntry().key;
		}
	}
	
	class AscendingMapEntryIterator extends MapEntryIterator implements ObjectBidirectionalIterator<Long2ObjectMap.Entry<V>>
	{
		public AscendingMapEntryIterator(Node<V> first)
		{
			super(first);
		}

		@Override
		public Long2ObjectMap.Entry<V> previous() {
			if(!hasPrevious()) throw new NoSuchElementException();
			return previousEntry();
		}

		@Override
		public Long2ObjectMap.Entry<V> next() {
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
	
	class AscendingKeyIterator extends MapEntryIterator implements LongBidirectionalIterator
	{
		public AscendingKeyIterator(Node<V> first) {
			super(first);
		}

		@Override
		public long previousLong() {
			if(!hasPrevious()) throw new NoSuchElementException();
			return previousEntry().key;
		}

		@Override
		public long nextLong() {
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
	
	private static final class Node<V> implements Long2ObjectMap.Entry<V>
	{
		long key;
		V value;
		int state;
		Node<V> parent;
		Node<V> left;
		Node<V> right;
		
		Node(long key, V value, Node<V> parent) {
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
		public long getLongKey() {
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
				if(obj instanceof Long2ObjectMap.Entry) {
					Long2ObjectMap.Entry<V> entry = (Long2ObjectMap.Entry<V>)obj;
					return key == entry.getLongKey() && Objects.equals(value, entry.getValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object otherKey = entry.getKey();
				if(otherKey == null) return false;
				Object otherValue = entry.getValue();
				return otherKey instanceof Long && key == ((Long)otherKey).longValue() && Objects.equals(value, otherValue);
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Long.hashCode(key) ^ Objects.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Long.toString(key) + "=" + Objects.toString(value);
		}
		
		int getHeight() { return state; }
		
		void updateHeight() { state = (1 + Math.max(left == null ? -1 : left.getHeight(), right == null ? -1 : right.getHeight())); }
		
		int getBalance() { return (left == null ? -1 : left.getHeight()) - (right == null ? -1 : right.getHeight()); }
		
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