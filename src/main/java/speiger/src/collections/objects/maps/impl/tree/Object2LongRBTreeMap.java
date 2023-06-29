package speiger.src.collections.objects.maps.impl.tree;

import java.util.Collections;
import java.util.Map;
import java.util.Comparator;
import java.util.Objects;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.LongPredicate;

import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;
import speiger.src.collections.ints.functions.consumer.IntObjectConsumer;
import speiger.src.collections.ints.functions.consumer.IntLongConsumer;
import speiger.src.collections.objects.functions.function.ToLongFunction;
import speiger.src.collections.objects.functions.consumer.ObjectLongConsumer;
import speiger.src.collections.objects.functions.function.ObjectLongUnaryOperator;
import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;
import speiger.src.collections.objects.maps.abstracts.AbstractObject2LongMap;
import speiger.src.collections.objects.maps.interfaces.Object2LongMap;
import speiger.src.collections.objects.maps.interfaces.Object2LongNavigableMap;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectNavigableSet;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.longs.collections.AbstractLongCollection;
import speiger.src.collections.longs.collections.LongCollection;
import speiger.src.collections.longs.collections.LongIterator;
import speiger.src.collections.longs.functions.LongSupplier;
import speiger.src.collections.longs.collections.LongBidirectionalIterator;
import speiger.src.collections.longs.functions.function.LongLongUnaryOperator;
import speiger.src.collections.longs.functions.LongConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;

import speiger.src.collections.objects.collections.ObjectIterator;

/**
 * A Simple Type Specific RB TreeMap implementation that reduces boxing/unboxing.
 * It is using a bit more memory then <a href="https://github.com/vigna/fastutil">FastUtil</a>,
 * but it saves a lot of Performance on the Optimized removal and iteration logic.
 * Which makes the implementation actually useable and does not get outperformed by Javas default implementation.
 * @param <T> the keyType of elements maintained by this Collection
 */
public class Object2LongRBTreeMap<T> extends AbstractObject2LongMap<T> implements Object2LongNavigableMap<T>
{
	/** The center of the Tree */
	protected transient Node<T> tree;
	/** The Lowest possible Node */
	protected transient Node<T> first;
	/** The Highest possible Node */
	protected transient Node<T> last;
	/** The amount of elements stored in the Map */
	protected int size = 0;
	/** The Sorter of the Tree */
	protected transient Comparator<T> comparator;
	
	
	/** KeySet Cache */
	protected ObjectNavigableSet<T> keySet;
	/** Values Cache */
	protected LongCollection values;
	/** EntrySet Cache */
	protected ObjectSet<Object2LongMap.Entry<T>> entrySet;
	
	/**
	 * Default Constructor
	 */
	public Object2LongRBTreeMap() {
	}
	
	/**
	 * Constructor that allows to define the sorter
	 * @param comp the function that decides how the tree is sorted, can be null
	 */
	public Object2LongRBTreeMap(Comparator<T> comp) {
		comparator = comp;
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Object2LongRBTreeMap(T[] keys, Long[] values) {
		this(keys, values, null);
	}
	
	/**
	 * Helper constructor that has a custom sorter and allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param comp the function that decides how the tree is sorted, can be null
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Object2LongRBTreeMap(T[] keys, Long[] values, Comparator<T> comp) {
		comparator = comp;
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i], values[i].longValue());
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Object2LongRBTreeMap(T[] keys, long[] values) {
		this(keys, values, null);
	}
	
	/**
	 * Helper constructor that has a custom sorter and allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param comp the function that decides how the tree is sorted, can be null
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Object2LongRBTreeMap(T[] keys, long[] values, Comparator<T> comp) {
		comparator = comp;
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i], values[i]);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 */
	public Object2LongRBTreeMap(Map<? extends T, ? extends Long> map) {
		this(map, null);
	}
	
	/**
	 * A Helper constructor that has a custom sorter and allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param comp the function that decides how the tree is sorted, can be null
	 */
	public Object2LongRBTreeMap(Map<? extends T, ? extends Long> map, Comparator<T> comp) {
		comparator = comp;
		putAll(map);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 */
	public Object2LongRBTreeMap(Object2LongMap<T> map) {
		this(map, null);
	}
	
	/**
	 * A Type Specific Helper function that has a custom sorter and allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param comp the function that decides how the tree is sorted, can be null
 	 */
	public Object2LongRBTreeMap(Object2LongMap<T> map, Comparator<T> comp) {
		comparator = comp;
		putAll(map);
	}

	/** only used for primitives 
	 * @return null 
	 */
	public T getDefaultMaxValue() { return null; }
	/** only used for primitives 
	 * @return null 
	 */
	public T getDefaultMinValue() { return null; }
	
	
	@Override
	public long put(T key, long value) {
		validate(key);
		if(tree == null) {
			tree = first = last = new Node<>(key, value, null);
			size++;
			return getDefaultReturnValue();
		}
		int compare = 0;
		Node<T> parent = tree;
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
		Node<T> adding = new Node<>(key, value, parent);
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
	public long putIfAbsent(T key, long value) {
		validate(key);
		if(tree == null) {
			tree = first = last = new Node<>(key, value, null);
			size++;
			return getDefaultReturnValue();
		}
		int compare = 0;
		Node<T> parent = tree;
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
		Node<T> adding = new Node<>(key, value, parent);
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
	public long addTo(T key, long value) {
		validate(key);
		if(tree == null) {
			tree = first = last = new Node<>(key, value, null);
			size++;
			return getDefaultReturnValue();
		}
		int compare = 0;
		Node<T> parent = tree;
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
		Node<T> adding = new Node<>(key, value, parent);
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
	public long subFrom(T key, long value) {
		if(tree == null) return getDefaultReturnValue();
		int compare = 0;
		Node<T> parent = tree;
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
	public Comparator<T> comparator() { return comparator; }

	@Override
	public boolean containsKey(Object key) {
		return findNode((T)key) != null;
	}
	
	@Override
	public long getLong(T key) {
		Node<T> node = findNode(key);
		return node == null ? getDefaultReturnValue() : node.value;
	}
	
	@Override
	public long getOrDefault(T key, long defaultValue) {
		Node<T> node = findNode(key);
		return node == null ? defaultValue : node.value;
	}
	
	@Override
	public T firstKey() {
		if(tree == null) throw new NoSuchElementException();
		return first.key;
	}
	
	@Override
	public T pollFirstKey() {
		if(tree == null) return getDefaultMinValue();
		T result = first.key;
		removeNode(first);
		return result;
	}
	
	@Override
	public T lastKey() {
		if(tree == null) throw new NoSuchElementException();
		return last.key;
	}
	
	@Override
	public T pollLastKey() {
		if(tree == null) return getDefaultMaxValue();
		T result = last.key;
		removeNode(last);
		return result;
	}
	
	@Override
	public Object2LongMap.Entry<T> firstEntry() {
		if(tree == null) return null;
		return first.export();
	}
	
	@Override
	public Object2LongMap.Entry<T> lastEntry() {
		if(tree == null) return null;
		return last.export();
	}
	
	@Override
	public Object2LongMap.Entry<T> pollFirstEntry() {
		if(tree == null) return null;
		BasicEntry<T> entry = first.export();
		removeNode(first);
		return entry;
	}
	
	@Override
	public Object2LongMap.Entry<T> pollLastEntry() {
		if(tree == null) return null;
		BasicEntry<T> entry = last.export();
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
	public long rem(T key) {
		Node<T> entry = findNode(key);
		if(entry == null) return getDefaultReturnValue();
		long value = entry.value;
		removeNode(entry);
		return value;
	}
	
	@Override
	public long remOrDefault(T key, long defaultValue) {
		Node<T> entry = findNode(key);
		if(entry == null) return defaultValue;
		long value = entry.value;
		removeNode(entry);
		return value;
	}
	
	@Override
	public boolean remove(T key, long value) {
		Node<T> entry = findNode(key);
		if(entry == null || entry.value != value) return false;
		removeNode(entry);
		return true;
	}
	
	@Override
	public boolean replace(T key, long oldValue, long newValue) {
		Node<T> entry = findNode(key);
		if(entry == null || entry.value != oldValue) return false;
		entry.value = newValue;
		return true;
	}
	
	@Override
	public long replace(T key, long value) {
		Node<T> entry = findNode(key);
		if(entry == null) return getDefaultReturnValue();
		long oldValue = entry.value;
		entry.value = value;
		return oldValue;
	}
	
	@Override
	public long computeLong(T key, ObjectLongUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		validate(key);
		Node<T> entry = findNode(key);
		if(entry == null) {
			long newValue = mappingFunction.applyAsLong(key, getDefaultReturnValue());
			put(key, newValue);
			return newValue;
		}
		long newValue = mappingFunction.applyAsLong(key, entry.value);
		entry.value = newValue;
		return newValue;
	}
	
	@Override
	public long computeLongIfAbsent(T key, ToLongFunction<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		validate(key);
		Node<T> entry = findNode(key);
		if(entry == null) {
			long newValue = mappingFunction.applyAsLong(key);
			put(key, newValue);
			return newValue;
		}
		return entry.value;
	}
	
	@Override
	public long supplyLongIfAbsent(T key, LongSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		validate(key);
		Node<T> entry = findNode(key);
		if(entry == null) {
			long newValue = valueProvider.getAsLong();
			put(key, newValue);
			return newValue;
		}
		return entry.value;
	}
	
	@Override
	public long computeLongIfPresent(T key, ObjectLongUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		validate(key);
		Node<T> entry = findNode(key);
		if(entry == null) return getDefaultReturnValue();
		long newValue = mappingFunction.applyAsLong(key, entry.value);
		entry.value = newValue;
		return newValue;
	}
	
	@Override
	public long computeLongNonDefault(T key, ObjectLongUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		validate(key);
		Node<T> entry = findNode(key);
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
	public long computeLongIfAbsentNonDefault(T key, ToLongFunction<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		validate(key);
		Node<T> entry = findNode(key);
		if(entry == null) {
			long newValue = mappingFunction.applyAsLong(key);
			if(newValue == getDefaultReturnValue()) return newValue;
			put(key, newValue);
			return newValue;
		}
		if(Objects.equals(entry.value, getDefaultReturnValue())) {
			long newValue = mappingFunction.applyAsLong(key);
			if(newValue == getDefaultReturnValue()) return newValue;
			entry.value = newValue;
		}
		return entry.value;
	}
	
	@Override
	public long supplyLongIfAbsentNonDefault(T key, LongSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		validate(key);
		Node<T> entry = findNode(key);
		if(entry == null) {
			long newValue = valueProvider.getAsLong();
			if(newValue == getDefaultReturnValue()) return newValue;
			put(key, newValue);
			return newValue;
		}
		if(entry.value == getDefaultReturnValue()) {
			long newValue = valueProvider.getAsLong();
			if(newValue == getDefaultReturnValue()) return newValue;
			entry.value = newValue;
		}
		return entry.value;
	}
	
	@Override
	public long computeLongIfPresentNonDefault(T key, ObjectLongUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		validate(key);
		Node<T> entry = findNode(key);
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
	public long mergeLong(T key, long value, LongLongUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		validate(key);
		Node<T> entry = findNode(key);
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
	public void mergeAllLong(Object2LongMap<T> m, LongLongUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Object2LongMap.Entry<T> entry : getFastIterable(m)) {
			T key = entry.getKey();
			Node<T> subEntry = findNode(key);
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
	public void forEach(ObjectLongConsumer<T> action) {
		for(Node<T> entry = first;entry != null;entry = entry.next())
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
	
	protected ObjectBidirectionalIterator<T> keyIterator() {
		return new AscendingKeyIterator(first);
	}
	
	protected ObjectBidirectionalIterator<T> keyIterator(T element) {
		return new AscendingKeyIterator(findNode(element));
	}
	
	protected ObjectBidirectionalIterator<T> descendingKeyIterator() {
		return new DescendingKeyIterator(last);
	}
		
	@Override
	public Object2LongRBTreeMap<T> copy() {
		Object2LongRBTreeMap<T> set = new Object2LongRBTreeMap<>();
		set.size = size;
		if(tree != null) {
			set.tree = tree.copy();
			Node<T> lastFound = null;
			for(Node<T> entry = tree;entry != null;entry = entry.left) lastFound = entry;
			set.first = lastFound;
			lastFound = null;
			for(Node<T> entry = tree;entry != null;entry = entry.right) lastFound = entry;
			set.last = lastFound;
		}
		return set;
	}
	
	@Override
	public ObjectNavigableSet<T> keySet() {
		return navigableKeySet();
	}
	
	@Override
	public ObjectSet<Object2LongMap.Entry<T>> object2LongEntrySet() {
		if(entrySet == null) entrySet = new EntrySet();
		return entrySet;
	}
	
	@Override
	public LongCollection values() {
		if(values == null) values = new Values();
		return values;
	}
	
	@Override
	public ObjectNavigableSet<T> navigableKeySet() {
		if(keySet == null) keySet = new KeySet<>(this);
		return keySet;
	}
	
	@Override
	public Object2LongNavigableMap<T> descendingMap() {
		return new DescendingNaivgableSubMap<>(this, true, null, true, true, null, true);
	}
	
	@Override
	public ObjectNavigableSet<T> descendingKeySet() {
		return descendingMap().navigableKeySet();
	}
	
	@Override
	public Object2LongNavigableMap<T> subMap(T fromKey, boolean fromInclusive, T toKey, boolean toInclusive) {
		return new AscendingNaivgableSubMap<>(this, false, fromKey, fromInclusive, false, toKey, toInclusive);
	}
	
	@Override
	public Object2LongNavigableMap<T> headMap(T toKey, boolean inclusive) {
		return new AscendingNaivgableSubMap<>(this, true, null, true, false, toKey, inclusive);
	}
	
	@Override
	public Object2LongNavigableMap<T> tailMap(T fromKey, boolean inclusive) {
		return new AscendingNaivgableSubMap<>(this, false, fromKey, inclusive, true, null, true);
	}
	
	@Override
	public T lowerKey(T e) {
		Node<T> node = findLowerNode(e);
		return node != null ? node.key : getDefaultMinValue();
	}

	@Override
	public T floorKey(T e) {
		Node<T> node = findFloorNode(e);
		return node != null ? node.key : getDefaultMinValue();
	}
	
	@Override
	public T higherKey(T e) {
		Node<T> node = findHigherNode(e);
		return node != null ? node.key : getDefaultMaxValue();
	}

	@Override
	public T ceilingKey(T e) {
		Node<T> node = findCeilingNode(e);
		return node != null ? node.key : getDefaultMaxValue();
	}
	
	@Override
	public Object2LongMap.Entry<T> lowerEntry(T key) {
		Node<T> node = findLowerNode(key);
		return node != null ? node.export() : null;
	}
	
	@Override
	public Object2LongMap.Entry<T> higherEntry(T key) {
		Node<T> node = findHigherNode(key);
		return node != null ? node.export() : null;
	}
	
	@Override
	public Object2LongMap.Entry<T> floorEntry(T key) {
		Node<T> node = findFloorNode(key);
		return node != null ? node.export() : null;
	}
	
	@Override
	public Object2LongMap.Entry<T> ceilingEntry(T key) {
		Node<T> node = findCeilingNode(key);
		return node != null ? node.export() : null;
	}
	
	protected Node<T> findLowerNode(T key) {
		Node<T> entry = tree;
		while(entry != null) {
			if(compare(key, entry.key) > 0) {
				if(entry.right != null) entry = entry.right;
				else return entry;
			}
			else {
				if(entry.left != null) entry = entry.left;
				else {
					Node<T> parent = entry.parent;
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
	
	protected Node<T> findFloorNode(T key) {
		Node<T> entry = tree;
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
					Node<T> parent = entry.parent;
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
	
	protected Node<T> findCeilingNode(T key) {
		Node<T> entry = tree;
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
					Node<T> parent = entry.parent;
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
	
	protected Node<T> findHigherNode(T key) {
		Node<T> entry = tree;
		while(entry != null) {
			if(compare(key, entry.key) < 0) {
				if(entry.left != null) entry = entry.left;
				else return entry;
			}
			else {
				if(entry.right != null) entry = entry.right;
				else {
					Node<T> parent = entry.parent;
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
	
	protected Node<T> findNode(T key) {
		Node<T> node = tree;
		int compare;
		while(node != null) {
			if((compare = compare(key, node.key)) == 0) return node;
			if(compare < 0) node = node.left;
			else node = node.right;
		}
		return null;
	}
	
	protected void removeNode(Node<T> entry) {
		size--;
		if(entry.needsSuccessor()) {
			Node<T> successor = entry.next();
			entry.key = successor.key;
			entry.value = successor.value;
			entry = successor;
		}
		Node<T> replacement = entry.left != null ? entry.left : entry.right;
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
				Node<T> parent = entry.parent;
				if(entry == first) first = parent.left != null ? parent.left : parent;
				if(entry == last) last = entry.right != null ? parent.right : parent;
			}
			entry.parent = null;
		}
	}
	
	protected void validate(T k) { compare(k, k); }
	protected int compare(T k, T v) { return comparator != null ? comparator.compare(k, v) : ((Comparable<T>)k).compareTo((T)v);}
	protected static <T> boolean isBlack(Node<T> p) { return p == null || p.isBlack(); }
	protected static <T> Node<T> parentOf(Node<T> p) { return (p == null ? null : p.parent); }
	protected static <T> void setBlack(Node<T> p, boolean c) { if(p != null) p.setBlack(c); }
	protected static <T> Node<T> leftOf(Node<T> p) { return p == null ? null : p.left; }
	protected static <T> Node<T> rightOf(Node<T> p) { return (p == null) ? null : p.right; }
	
	protected void rotateLeft(Node<T> entry) {
		if(entry != null) {
			Node<T> right = entry.right;
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
	
	protected void rotateRight(Node<T> entry) {
		if(entry != null) {
			Node<T> left = entry.left;
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
	
	protected void fixAfterInsertion(Node<T> entry) {
		entry.setBlack(false);
		while(entry != null && entry != tree && !entry.parent.isBlack()) {
			if(parentOf(entry) == leftOf(parentOf(parentOf(entry)))) {
				Node<T> y = rightOf(parentOf(parentOf(entry)));
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
				Node<T> y = leftOf(parentOf(parentOf(entry)));
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
	
	protected void fixAfterDeletion(Node<T> entry) {
		while(entry != tree && isBlack(entry)) {
			if(entry == leftOf(parentOf(entry))) {
				Node<T> sib = rightOf(parentOf(entry));
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
				Node<T> sib = leftOf(parentOf(entry));
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
	
	static class KeySet<T> extends AbstractObjectSet<T> implements ObjectNavigableSet<T>
	{
		Object2LongNavigableMap<T> map;

		public KeySet(Object2LongNavigableMap<T> map) {
			this.map = map;
		}
		
		@Override
		public T lower(T e) { return map.lowerKey(e); }
		@Override
		public T floor(T e) { return map.floorKey(e); }
		@Override
		public T ceiling(T e) { return map.ceilingKey(e); }
		@Override
		public T higher(T e) { return map.higherKey(e); }
		
		@Override
		public T pollFirst() { return map.pollFirstKey(); }
		@Override
		public T pollLast() { return map.pollLastKey(); }
		@Override
		public Comparator<T> comparator() { return map.comparator(); }
		@Override
		public T first() { return map.firstKey(); } 
		@Override
		public T last() { return map.lastKey(); }
		@Override
		public void clear() { map.clear(); }
		
		@Override
		public boolean remove(Object o) { 
			int oldSize = map.size();
			map.remove(o); 
			return oldSize != map.size();
		}
		
		@Override
		public boolean add(T e) { throw new UnsupportedOperationException(); }
		@Override
		public ObjectBidirectionalIterator<T> iterator(T fromElement) {
			if(map instanceof Object2LongRBTreeMap) return ((Object2LongRBTreeMap<T>)map).keyIterator(fromElement);
			return ((NavigableSubMap<T>)map).keyIterator(fromElement);
		}
		
		@Override
		public ObjectNavigableSet<T> subSet(T fromElement, boolean fromInclusive, T toElement, boolean toInclusive) { return new KeySet<>(map.subMap(fromElement, fromInclusive, toElement, toInclusive)); }
		@Override
		public ObjectNavigableSet<T> headSet(T toElement, boolean inclusive) { return new KeySet<>(map.headMap(toElement, inclusive)); }
		@Override
		public ObjectNavigableSet<T> tailSet(T fromElement, boolean inclusive) { return new KeySet<>(map.tailMap(fromElement, inclusive)); }
		
		@Override
		public ObjectBidirectionalIterator<T> iterator() {
			if(map instanceof Object2LongRBTreeMap) return ((Object2LongRBTreeMap<T>)map).keyIterator();
			return ((NavigableSubMap<T>)map).keyIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<T> descendingIterator() {
			if(map instanceof Object2LongRBTreeMap) return ((Object2LongRBTreeMap<T>)map).descendingKeyIterator();
			return ((NavigableSubMap<T>)map).descendingKeyIterator();
		}
		
		protected Node<T> start() {
			if(map instanceof Object2LongRBTreeMap) return ((Object2LongRBTreeMap<T>)map).first;
			return ((NavigableSubMap<T>)map).subLowest();
		}
		
		protected Node<T> end() {
			if(map instanceof Object2LongRBTreeMap) return null;
			return ((NavigableSubMap<T>)map).subHighest();
		}
		
		protected Node<T> next(Node<T> entry) {
			if(map instanceof Object2LongRBTreeMap) return entry.next();
			return ((NavigableSubMap<T>)map).next(entry);
		}
		
		protected Node<T> previous(Node<T> entry) {
			if(map instanceof Object2LongRBTreeMap) return entry.previous();
			return ((NavigableSubMap<T>)map).previous(entry);
		}
		
		@Override
		public ObjectNavigableSet<T> descendingSet() { return new KeySet<>(map.descendingMap()); }
		@Override
		public KeySet<T> copy() { throw new UnsupportedOperationException(); }
		@Override
		public boolean isEmpty() { return map.isEmpty(); }
		@Override
		public int size() { return map.size(); }
		
		@Override
		public void forEach(Consumer<? super T> action) {
			Objects.requireNonNull(action);
			for(Node<T> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				action.accept(entry.key);
		}
		
		@Override
		public void forEachIndexed(IntObjectConsumer<T> action) {
			Objects.requireNonNull(action);
			int index = 0;
			for(Node<T> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				action.accept(index++, entry.key);
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, T> action) {
			Objects.requireNonNull(action);
			for(Node<T> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				action.accept(input, entry.key);
		}
		
		@Override
		public boolean matchesAny(Predicate<T> filter) {
			Objects.requireNonNull(filter);
			for(Node<T> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				if(filter.test(entry.key)) return true;
			return false;
		}
		
		@Override
		public boolean matchesNone(Predicate<T> filter) {
			Objects.requireNonNull(filter);
			for(Node<T> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				if(filter.test(entry.key)) return false;
			return true;
		}
		
		@Override
		public boolean matchesAll(Predicate<T> filter) {
			Objects.requireNonNull(filter);
			for(Node<T> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				if(!filter.test(entry.key)) return false;
			return true;
		}
		
		@Override
		public <E> E reduce(E identity, BiFunction<E, T, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			for(Node<T> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				state = operator.apply(state, entry.key);
			return state;
		}
		
		@Override
		public T reduce(ObjectObjectUnaryOperator<T, T> operator) {
			Objects.requireNonNull(operator);
			T state = null;
			boolean empty = true;
			for(Node<T> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry)) {
				if(empty) {
					empty = false;
					state = entry.key;
					continue;
				}
				state = operator.apply(state, entry.key);
			}
			return state;
		}
		
		@Override
		public T findFirst(Predicate<T> filter) {
			Objects.requireNonNull(filter);
			for(Node<T> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				if(filter.test(entry.key)) return entry.key;
			return null;
		}
		
		@Override
		public int count(Predicate<T> filter) {
			Objects.requireNonNull(filter);
			int result = 0;
			for(Node<T> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				if(filter.test(entry.key)) result++;
			return result;
		}
	}
	
	static class AscendingNaivgableSubMap<T> extends NavigableSubMap<T>
	{
		AscendingNaivgableSubMap(Object2LongRBTreeMap<T> map, boolean fromStart, T lo, boolean loInclusive, boolean toEnd, T hi, boolean hiInclusive) {
			super(map, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
		}
		
		@Override
		public Object2LongNavigableMap<T> descendingMap() {
			if(inverse == null) inverse = new DescendingNaivgableSubMap<>(map, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
			return inverse;
		}
		
		@Override
		public ObjectSet<Object2LongMap.Entry<T>> object2LongEntrySet() {
			if(entrySet == null) entrySet = new AscendingSubEntrySet();
			return entrySet;
		}
		
		@Override
		public ObjectNavigableSet<T> navigableKeySet() {
			if(keySet == null) keySet = new KeySet<>(this);
			return keySet;
		}
		
		@Override
		public ObjectNavigableSet<T> keySet() {
			return navigableKeySet();
		}
		
		@Override
		public Object2LongNavigableMap<T> subMap(T fromKey, boolean fromInclusive, T toKey, boolean toInclusive) {
			if (!inRange(fromKey, fromInclusive)) throw new IllegalArgumentException("fromKey out of range");
			if (!inRange(toKey, toInclusive)) throw new IllegalArgumentException("toKey out of range");
			return new AscendingNaivgableSubMap<>(map, false, fromKey, fromInclusive, false, toKey, toInclusive);
		}
		
		@Override
		public Object2LongNavigableMap<T> headMap(T toKey, boolean inclusive) {
			if (!inRange(toKey, inclusive)) throw new IllegalArgumentException("toKey out of range");
			return new AscendingNaivgableSubMap<>(map, fromStart, lo, loInclusive, false, toKey, inclusive);
		}
		
		@Override
		public Object2LongNavigableMap<T> tailMap(T fromKey, boolean inclusive) {
			if (!inRange(fromKey, inclusive)) throw new IllegalArgumentException("fromKey out of range");
			return new AscendingNaivgableSubMap<>(map, false, fromKey, inclusive, toEnd, hi, hiInclusive);
		}
		
		@Override
		protected Node<T> subLowest() { return absLowest(); }
		@Override
		protected Node<T> subHighest() { return absHighest(); }
		@Override
		protected Node<T> subCeiling(T key) { return absCeiling(key); }
		@Override
		protected Node<T> subHigher(T key) { return absHigher(key); }
		@Override
		protected Node<T> subFloor(T key) { return absFloor(key); }
		@Override
		protected Node<T> subLower(T key) { return absLower(key); }
		
		@Override
		protected ObjectBidirectionalIterator<T> keyIterator() {
			return new AcsendingSubKeyIterator(absLowest(), absHighFence(), absLowFence()); 
		}
		@Override
		protected ObjectBidirectionalIterator<T> keyIterator(T element) {
			return new AcsendingSubKeyIterator(absLower(element), absHighFence(), absLowFence()); 
		}
		
		@Override
		protected LongBidirectionalIterator valueIterator() {
			return new AcsendingSubValueIterator(absLowest(), absHighFence(), absLowFence()); 
		}
		
		@Override
		protected ObjectBidirectionalIterator<T> descendingKeyIterator() {
			return new DecsendingSubKeyIterator(absHighest(), absLowFence(), absHighFence()); 
		}
		
		class AscendingSubEntrySet extends SubEntrySet {
			@Override
			public ObjectIterator<Object2LongMap.Entry<T>> iterator() {
				return new AcsendingSubEntryIterator(absLowest(), absHighFence(), absLowFence());
			}
		}
	}
	
	static class DescendingNaivgableSubMap<T> extends NavigableSubMap<T>
	{
		Comparator<T> comparator;
		DescendingNaivgableSubMap(Object2LongRBTreeMap<T> map, boolean fromStart, T lo, boolean loInclusive, boolean toEnd, T hi, boolean hiInclusive) {
			super(map, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
			comparator = Collections.reverseOrder(map.comparator());
		}
		
		@Override
		public Comparator<T> comparator() { return comparator; }
		
		@Override
		public Object2LongNavigableMap<T> descendingMap() {
			if(inverse == null) inverse = new AscendingNaivgableSubMap<>(map, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
			return inverse;
		}

		@Override
		public ObjectNavigableSet<T> navigableKeySet() {
			if(keySet == null) keySet = new KeySet<>(this);
			return keySet;
		}
		
		@Override
		public ObjectNavigableSet<T> keySet() {
			return navigableKeySet();
		}
		
		@Override
		public Object2LongNavigableMap<T> subMap(T fromKey, boolean fromInclusive, T toKey, boolean toInclusive) {
			if (!inRange(fromKey, fromInclusive)) throw new IllegalArgumentException("fromKey out of range");
			if (!inRange(toKey, toInclusive)) throw new IllegalArgumentException("toKey out of range");
			return new DescendingNaivgableSubMap<>(map, false, toKey, toInclusive, false, fromKey, fromInclusive);
		}
		
		@Override
		public Object2LongNavigableMap<T> headMap(T toKey, boolean inclusive) {
			if (!inRange(toKey, inclusive)) throw new IllegalArgumentException("toKey out of range");
			return new DescendingNaivgableSubMap<>(map, false, toKey, inclusive, toEnd, hi, hiInclusive);
		}
		
		@Override
		public Object2LongNavigableMap<T> tailMap(T fromKey, boolean inclusive) {
			if (!inRange(fromKey, inclusive)) throw new IllegalArgumentException("fromKey out of range");
			return new DescendingNaivgableSubMap<>(map, fromStart, lo, loInclusive, false, fromKey, inclusive);
		}
		
		@Override
		public ObjectSet<Object2LongMap.Entry<T>> object2LongEntrySet() {
			if(entrySet == null) entrySet = new DescendingSubEntrySet();
			return entrySet;
		}
		
		@Override
		protected Node<T> subLowest() { return absHighest(); }
		@Override
		protected Node<T> subHighest() { return absLowest(); }
		@Override
		protected Node<T> subCeiling(T key) { return absFloor(key); }
		@Override
		protected Node<T> subHigher(T key) { return absLower(key); }
		@Override
		protected Node<T> subFloor(T key) { return absCeiling(key); }
		@Override
		protected Node<T> subLower(T key) { return absHigher(key); }
		@Override
		protected Node<T> next(Node<T> entry) { return entry.previous(); }
		@Override
		protected Node<T> previous(Node<T> entry) { return entry.next(); }
		
		@Override
		protected ObjectBidirectionalIterator<T> keyIterator() {
			return new DecsendingSubKeyIterator(absHighest(), absLowFence(), absHighFence()); 
		}
		
		@Override
		protected ObjectBidirectionalIterator<T> keyIterator(T element) {
			return new DecsendingSubKeyIterator(absHigher(element), absLowFence(), absHighFence()); 
		}
		
		@Override
		protected LongBidirectionalIterator valueIterator() {
			return new DecsendingSubValueIterator(absHighest(), absLowFence(), absHighFence()); 
		}
		
		@Override
		protected ObjectBidirectionalIterator<T> descendingKeyIterator() {
			return new AcsendingSubKeyIterator(absLowest(), absHighFence(), absLowFence()); 
		}
		
		class DescendingSubEntrySet extends SubEntrySet {
			@Override
			public ObjectIterator<Object2LongMap.Entry<T>> iterator() {
				return new DecsendingSubEntryIterator(absHighest(), absLowFence(), absHighFence());
			}
		}
	}
	
	static abstract class NavigableSubMap<T> extends AbstractObject2LongMap<T> implements Object2LongNavigableMap<T>
	{
		final Object2LongRBTreeMap<T> map;
		final T lo, hi;
		final boolean fromStart, toEnd;
		final boolean loInclusive, hiInclusive;
		
		Object2LongNavigableMap<T> inverse;
		ObjectNavigableSet<T> keySet;
		ObjectSet<Object2LongMap.Entry<T>> entrySet;
		LongCollection values;
		
		NavigableSubMap(Object2LongRBTreeMap<T> map, boolean fromStart, T lo, boolean loInclusive, boolean toEnd, T hi, boolean hiInclusive) {
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
		
		public T getDefaultMaxValue() { return map.getDefaultMaxValue(); }
		public T getDefaultMinValue() { return map.getDefaultMinValue(); }
		protected boolean isNullComparator() { return map.comparator() == null; }
		
		@Override
		public AbstractObject2LongMap<T> setDefaultReturnValue(long v) { 
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
		public ObjectNavigableSet<T> descendingKeySet() {
			return descendingMap().navigableKeySet();
		}
		
		@Override
		public ObjectNavigableSet<T> keySet() {
			return navigableKeySet();
		}
		
		protected abstract Node<T> subLowest();
		protected abstract Node<T> subHighest();
		protected abstract Node<T> subCeiling(T key);
		protected abstract Node<T> subHigher(T key);
		protected abstract Node<T> subFloor(T key);
		protected abstract Node<T> subLower(T key);
		protected abstract ObjectBidirectionalIterator<T> keyIterator();
		protected abstract ObjectBidirectionalIterator<T> keyIterator(T element);
		protected abstract LongBidirectionalIterator valueIterator();
		protected abstract ObjectBidirectionalIterator<T> descendingKeyIterator();
		protected T lowKeyOrNull(Node<T> entry) { return entry == null ? getDefaultMinValue() : entry.key; }
		protected T highKeyOrNull(Node<T> entry) { return entry == null ? getDefaultMaxValue() : entry.key; }
		protected Node<T> next(Node<T> entry) { return entry.next(); }
		protected Node<T> previous(Node<T> entry) { return entry.previous(); }
		
		protected boolean tooLow(T key) {
			if (!fromStart) {
				int c = map.compare(key, lo);
				if (c < 0 || (c == 0 && !loInclusive)) return true;
			}
			return false;
		}
		
		protected boolean tooHigh(T key) {
			if (!toEnd) {
				int c = map.compare(key, hi);
				if (c > 0 || (c == 0 && !hiInclusive)) return true;
			}
			return false;
		}
		protected boolean inRange(T key) { return !tooLow(key) && !tooHigh(key); }
		protected boolean inClosedRange(T key) { return (fromStart || map.compare(key, lo) >= 0) && (toEnd || map.compare(hi, key) >= 0); }
		protected boolean inRange(T key, boolean inclusive) { return inclusive ? inRange(key) : inClosedRange(key); }
		
		protected Node<T> absLowest() {
			Node<T> e = (fromStart ?  map.first : (loInclusive ? map.findCeilingNode(lo) : map.findHigherNode(lo)));
			return (e == null || tooHigh(e.key)) ? null : e;
		}
		
		protected Node<T> absHighest() {
			Node<T> e = (toEnd ?  map.last : (hiInclusive ?  map.findFloorNode(hi) : map.findLowerNode(hi)));
			return (e == null || tooLow(e.key)) ? null : e;
		}
		
		protected Node<T> absCeiling(T key) {
			if (tooLow(key)) return absLowest();
			Node<T> e = map.findCeilingNode(key);
			return (e == null || tooHigh(e.key)) ? null : e;
		}
		
		protected Node<T> absHigher(T key) {
			if (tooLow(key)) return absLowest();
			Node<T> e = map.findHigherNode(key);
			return (e == null || tooHigh(e.key)) ? null : e;
		}
		
		protected Node<T> absFloor(T key) {
			if (tooHigh(key)) return absHighest();
			Node<T> e = map.findFloorNode(key);
			return (e == null || tooLow(e.key)) ? null : e;
		}
		
		protected Node<T> absLower(T key) {
			if (tooHigh(key)) return absHighest();
			Node<T> e = map.findLowerNode(key);
			return (e == null || tooLow(e.key)) ? null : e;
		}
		
		protected Node<T> absHighFence() { return (toEnd ? null : (hiInclusive ? map.findHigherNode(hi) : map.findCeilingNode(hi))); }
		protected Node<T> absLowFence() { return (fromStart ? null : (loInclusive ?  map.findLowerNode(lo) : map.findFloorNode(lo))); }
		
		@Override
		public Comparator<T> comparator() { return map.comparator(); }
		
		@Override
		public T pollFirstKey() {
			Node<T> entry = subLowest();
			if(entry != null) {
				T result = entry.key;
				map.removeNode(entry);
				return result;
			}
			return getDefaultMinValue();
		}
		
		@Override
		public T pollLastKey() {
			Node<T> entry = subHighest();
			if(entry != null) {
				T result = entry.key;
				map.removeNode(entry);
				return result;
			}
			return getDefaultMaxValue();
		}
		
		@Override
		public long firstLongValue() {
			Node<T> entry = subLowest();
			if(entry == null) throw new NoSuchElementException();
			return entry.value;
		}
		
		@Override
		public long lastLongValue() {
			Node<T> entry = subHighest();
			if(entry == null) throw new NoSuchElementException();
			return entry.value;
		}
		
		@Override
		public T firstKey() {
			Node<T> entry = subLowest();
			if(entry == null) throw new NoSuchElementException();
			return entry.key;
		}
		
		@Override
		public T lastKey() {
			Node<T> entry = subHighest();
			if(entry == null) throw new NoSuchElementException();
			return entry.key;
		}
		
		@Override
		public long put(T key, long value) {
			if (!inRange(key)) throw new IllegalArgumentException("key out of range");
			return map.put(key, value);
		}
		
		@Override
		public long putIfAbsent(T key, long value) {
			if (!inRange(key)) throw new IllegalArgumentException("key out of range");
			return map.putIfAbsent(key, value);
		}
		
		@Override
		public long addTo(T key, long value) {
			if(!inRange(key)) throw new IllegalArgumentException("key out of range");
			return map.addTo(key, value);
		}
		
		@Override
		public long subFrom(T key, long value) {
			if(!inRange(key)) throw new IllegalArgumentException("key out of range");
			return map.subFrom(key, value);
		}
		
		@Override
		public boolean containsKey(Object key) { return inRange((T)key) && map.containsKey(key); }
		@Override
		public long computeLongIfPresent(T key, ObjectLongUnaryOperator<T> mappingFunction) {
			Objects.requireNonNull(mappingFunction);
			map.validate(key);
			if(!inRange(key)) return getDefaultReturnValue();
			Node<T> entry = map.findNode(key);
			if(entry == null) return getDefaultReturnValue();
			entry.value = mappingFunction.apply(key, entry.value);
			return entry.value;
		}
		
		@Override
		public long rem(T key) {
			return inRange(key) ? map.rem(key) : getDefaultReturnValue();
		}
		
		@Override
		public long remOrDefault(T key, long defaultValue) {
			return inRange(key) ? map.remOrDefault(key, defaultValue) : defaultValue;
		}
		
		@Override
		public boolean remove(T key, long value) {
			return inRange(key) && map.remove(key, value);
		}
		
		
		@Override
		public long getLong(T key) {
			return inRange(key) ? map.getLong(key) : getDefaultReturnValue();
		}
		
		@Override
		public long getOrDefault(T key, long defaultValue) {
			return inRange(key) ? map.getOrDefault(key, defaultValue) : getDefaultReturnValue();
		}
		
		
		@Override
		public T lowerKey(T key) { return lowKeyOrNull(subLower(key)); }
		@Override
		public T floorKey(T key) { return lowKeyOrNull(subFloor(key)); }
		@Override
		public T ceilingKey(T key) { return highKeyOrNull(subCeiling(key)); }
		@Override
		public T higherKey(T key) { return highKeyOrNull(subHigher(key)); }
		@Override
		public Object2LongMap.Entry<T> lowerEntry(T key) { return subLower(key); }
		@Override
		public Object2LongMap.Entry<T> floorEntry(T key) { return subFloor(key); }
		@Override
		public Object2LongMap.Entry<T> ceilingEntry(T key) { return subCeiling(key); }
		@Override
		public Object2LongMap.Entry<T> higherEntry(T key) { return subHigher(key); }
		
		@Override
		public boolean isEmpty() {
			if(fromStart && toEnd) return map.isEmpty();
			Node<T> n = absLowest();
			return n == null || tooHigh(n.key);
		}
		
		@Override
		public int size() { return fromStart && toEnd ? map.size() : entrySet().size(); }
		
		@Override
		public Object2LongNavigableMap<T> copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public Object2LongMap.Entry<T> firstEntry() {
			Node<T> entry = subLowest();
			return entry == null ? null : entry.export();
		}

		@Override
		public Object2LongMap.Entry<T> lastEntry() {
			Node<T> entry = subHighest();
			return entry == null ? null : entry.export();
		}

		@Override
		public Object2LongMap.Entry<T> pollFirstEntry() {
			Node<T> entry = subLowest();
			if(entry != null) {
				Object2LongMap.Entry<T> result = entry.export();
				map.removeNode(entry);
				return result;
			}
			return null;
		}

		@Override
		public Object2LongMap.Entry<T> pollLastEntry() {
			Node<T> entry = subHighest();
			if(entry != null) {
				Object2LongMap.Entry<T> result = entry.export();
				map.removeNode(entry);
				return result;
			}
			return null;
		}
		
		abstract class SubEntrySet extends AbstractObjectSet<Object2LongMap.Entry<T>> {
			@Override
			public int size() {
				if (fromStart && toEnd) return map.size();
				int size = 0;
				for(ObjectIterator<Object2LongMap.Entry<T>> iter = iterator();iter.hasNext();iter.next(),size++);
				return size;
			}
			
			@Override
			public boolean isEmpty() {
				Node<T> n = absLowest();
				return n == null || tooHigh(n.key);
			}
			
			@Override
			public boolean contains(Object o) {
				if (!(o instanceof Map.Entry)) return false;
				if(o instanceof Object2LongMap.Entry)
				{
					Object2LongMap.Entry<T> entry = (Object2LongMap.Entry<T>) o;
					if(entry.getKey() == null && isNullComparator()) return false;
					T key = entry.getKey();
					if (!inRange(key)) return false;
					Node<T> node = map.findNode(key);
					return node != null && entry.getLongValue() == node.getLongValue();
				}
				Map.Entry<?,?> entry = (Map.Entry<?,?>) o;
				if(entry.getKey() == null && isNullComparator()) return false;
				T key = (T)entry.getKey();
				if (!inRange(key)) return false;
				Node<T> node = map.findNode(key);
				return node != null && Objects.equals(entry.getValue(), Long.valueOf(node.getLongValue()));
			}
			
			@Override
			public boolean remove(Object o) {
				if (!(o instanceof Map.Entry)) return false;
				if(o instanceof Object2LongMap.Entry)
				{
					Object2LongMap.Entry<T> entry = (Object2LongMap.Entry<T>) o;
					T key = entry.getKey();
					if (!inRange(key)) return false;
					Node<T> node = map.findNode(key);
					if (node != null && node.getLongValue() == entry.getLongValue()) {
						map.removeNode(node);
						return true;
					}
					return false;
				}
				Map.Entry<?,?> entry = (Map.Entry<?,?>) o;
				T key = (T)entry.getKey();
				if (!inRange(key)) return false;
				Node<T> node = map.findNode(key);
				if (node != null && Objects.equals(node.getValue(), entry.getValue())) {
					map.removeNode(node);
					return true;
				}
				return false;
			}
			
			@Override
			public void forEach(Consumer<? super Object2LongMap.Entry<T>> action) {
				Objects.requireNonNull(action);
				for(Node<T> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					action.accept(new BasicEntry<>(entry.key, entry.value));
			}
			
			@Override
			public void forEachIndexed(IntObjectConsumer<Object2LongMap.Entry<T>> action) {
				Objects.requireNonNull(action);
				int index = 0;
				for(Node<T> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					action.accept(index++, new BasicEntry<>(entry.key, entry.value));
			}
			
			@Override
			public <E> void forEach(E input, ObjectObjectConsumer<E, Object2LongMap.Entry<T>> action) {
				Objects.requireNonNull(action);
				for(Node<T> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					action.accept(input, new BasicEntry<>(entry.key, entry.value));
			}
			
			@Override
			public boolean matchesAny(Predicate<Object2LongMap.Entry<T>> filter) {
				Objects.requireNonNull(filter);
				if(size() <= 0) return false;
				BasicEntry<T> subEntry = new BasicEntry<>();
				for(Node<T> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					subEntry.set(entry.key, entry.value);
					if(filter.test(subEntry)) return true;
				}
				return false;
			}
			
			@Override
			public boolean matchesNone(Predicate<Object2LongMap.Entry<T>> filter) {
				Objects.requireNonNull(filter);
				if(size() <= 0) return true;
				BasicEntry<T> subEntry = new BasicEntry<>();
				for(Node<T> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					subEntry.set(entry.key, entry.value);
					if(filter.test(subEntry)) return false;
				}
				return true;
			}
			
			@Override
			public boolean matchesAll(Predicate<Object2LongMap.Entry<T>> filter) {
				Objects.requireNonNull(filter);
				if(size() <= 0) return true;
				BasicEntry<T> subEntry = new BasicEntry<>();
				for(Node<T> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					subEntry.set(entry.key, entry.value);
					if(!filter.test(subEntry)) return false;
				}
				return true;
			}
			
			@Override
			public <E> E reduce(E identity, BiFunction<E, Object2LongMap.Entry<T>, E> operator) {
				Objects.requireNonNull(operator);
				E state = identity;
				for(Node<T> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					state = operator.apply(state, new BasicEntry<>(entry.key, entry.value));
				}
				return state;
			}
			
			@Override
			public Object2LongMap.Entry<T> reduce(ObjectObjectUnaryOperator<Object2LongMap.Entry<T>, Object2LongMap.Entry<T>> operator) {
				Objects.requireNonNull(operator);
				Object2LongMap.Entry<T> state = null;
				boolean empty = true;
				for(Node<T> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
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
			public Object2LongMap.Entry<T> findFirst(Predicate<Object2LongMap.Entry<T>> filter) {
				Objects.requireNonNull(filter);
				if(size() <= 0) return null;
				BasicEntry<T> subEntry = new BasicEntry<>();
				for(Node<T> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					subEntry.set(entry.key, entry.value);
					if(filter.test(subEntry)) return subEntry;
				}
				return null;
			}
			
			@Override
			public int count(Predicate<Object2LongMap.Entry<T>> filter) {
				Objects.requireNonNull(filter);
				if(size() <= 0) return 0;
				int result = 0;
				BasicEntry<T> subEntry = new BasicEntry<>();
				for(Node<T> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					subEntry.set(entry.key, entry.value);
					if(filter.test(subEntry)) result++;
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
				for(Node<T> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					action.accept(entry.value);
			}
			
			@Override
			public void forEachIndexed(IntLongConsumer action) {
				Objects.requireNonNull(action);
				int index = 0;
				for(Node<T> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					action.accept(index++, entry.value);
			}
			
			@Override
			public <E> void forEach(E input, ObjectLongConsumer<E> action) {
				Objects.requireNonNull(action);
				for(Node<T> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					action.accept(input, entry.value);
			}
			
			@Override
			public boolean matchesAny(LongPredicate filter) {
				Objects.requireNonNull(filter);
				for(Node<T> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					if(filter.test(entry.value)) return true;
				return false;
			}
			
			@Override
			public boolean matchesNone(LongPredicate filter) {
				Objects.requireNonNull(filter);
				for(Node<T> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					if(filter.test(entry.value)) return false;
				return true;
			}
			
			@Override
			public boolean matchesAll(LongPredicate filter) {
				Objects.requireNonNull(filter);
				for(Node<T> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					if(!filter.test(entry.value)) return false;
				return true;
			}
			
			@Override
			public long reduce(long identity, LongLongUnaryOperator operator) {
				Objects.requireNonNull(operator);
				long state = identity;
				for(Node<T> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					state = operator.applyAsLong(state, entry.value);
				return state;
			}
			
			@Override
			public long reduce(LongLongUnaryOperator operator) {
				Objects.requireNonNull(operator);
				long state = 0L;
				boolean empty = true;
				for(Node<T> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
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
			public long findFirst(LongPredicate filter) {
				Objects.requireNonNull(filter);
				for(Node<T> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					if(filter.test(entry.value)) return entry.value;
				return 0L;
			}
			
			@Override
			public int count(LongPredicate filter) {
				Objects.requireNonNull(filter);
				int result = 0;
				for(Node<T> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					if(filter.test(entry.value)) result++;
				return result;
			}
		}
		
		class DecsendingSubEntryIterator extends SubMapEntryIterator implements ObjectBidirectionalIterator<Object2LongMap.Entry<T>>
		{
			public DecsendingSubEntryIterator(Node<T> first, Node<T> forwardFence, Node<T> backwardFence) {
				super(first, forwardFence, backwardFence, false);
			}
			
			@Override
			protected Node<T> moveNext(Node<T> node) {
				return node.previous();
			}
			
			@Override
			protected Node<T> movePrevious(Node<T> node) {
				return node.next();
			}
			
			@Override
			public Object2LongMap.Entry<T> previous() {
				if(!hasPrevious()) throw new NoSuchElementException();
				return previousEntry();
			}

			@Override
			public Object2LongMap.Entry<T> next() {
				if(!hasNext()) throw new NoSuchElementException();
				return nextEntry();
			}
		}
		
		class AcsendingSubEntryIterator extends SubMapEntryIterator implements ObjectBidirectionalIterator<Object2LongMap.Entry<T>>
		{
			public AcsendingSubEntryIterator(Node<T> first, Node<T> forwardFence, Node<T> backwardFence) {
				super(first, forwardFence, backwardFence, true);
			}

			@Override
			public Object2LongMap.Entry<T> previous() {
				if(!hasPrevious()) throw new NoSuchElementException();
				return previousEntry();
			}

			@Override
			public Object2LongMap.Entry<T> next() {
				if(!hasNext()) throw new NoSuchElementException();
				return nextEntry();
			}
		}
		
		class DecsendingSubKeyIterator extends SubMapEntryIterator implements ObjectBidirectionalIterator<T>
		{
			public DecsendingSubKeyIterator(Node<T> first, Node<T> forwardFence, Node<T> backwardFence) {
				super(first, forwardFence, backwardFence, false);
			}
			
			@Override
			protected Node<T> moveNext(Node<T> node) {
				return node.previous();
			}
			
			@Override
			protected Node<T> movePrevious(Node<T> node) {
				return node.next();
			}
			
			@Override
			public T previous() {
				if(!hasPrevious()) throw new NoSuchElementException();
				return previousEntry().key;
			}

			@Override
			public T next() {
				if(!hasNext()) throw new NoSuchElementException();
				return nextEntry().key;
			}
		}
		
		class AcsendingSubKeyIterator extends SubMapEntryIterator implements ObjectBidirectionalIterator<T>
		{
			public AcsendingSubKeyIterator(Node<T> first, Node<T> forwardFence, Node<T> backwardFence) {
				super(first, forwardFence, backwardFence, true);
			}

			@Override
			public T previous() {
				if(!hasPrevious()) throw new NoSuchElementException();
				return previousEntry().key;
			}

			@Override
			public T next() {
				if(!hasNext()) throw new NoSuchElementException();
				return nextEntry().key;
			}
		}
		
		class AcsendingSubValueIterator extends SubMapEntryIterator implements LongBidirectionalIterator
		{
			public AcsendingSubValueIterator(Node<T> first, Node<T> forwardFence, Node<T> backwardFence) {
				super(first, forwardFence, backwardFence, true);
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
			public DecsendingSubValueIterator(Node<T> first, Node<T> forwardFence, Node<T> backwardFence) {
				super(first, forwardFence, backwardFence, false);
			}
			
			@Override
			protected Node<T> moveNext(Node<T> node) {
				return node.previous();
			}
			
			@Override
			protected Node<T> movePrevious(Node<T> node) {
				return node.next();
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
		
		abstract class SubMapEntryIterator
		{
			final boolean isForward;
			boolean wasForward;
			Node<T> lastReturned;
			Node<T> next;
			Node<T> previous;
			boolean unboundForwardFence;
			boolean unboundBackwardFence;
			T forwardFence;
			T backwardFence;
			
			public SubMapEntryIterator(Node<T> first, Node<T> forwardFence, Node<T> backwardFence, boolean isForward) {
				next = first;
				previous = first == null ? null : movePrevious(first);
				this.forwardFence = forwardFence == null ? null : forwardFence.key;
				this.backwardFence = backwardFence == null ? null : backwardFence.key;
				unboundForwardFence = forwardFence == null;
				unboundBackwardFence = backwardFence == null;
				this.isForward = isForward;
			}
			
			protected Node<T> moveNext(Node<T> node) {
				return node.next();
			}
			
			protected Node<T> movePrevious(Node<T> node) {
				return node.previous();
			}
			
			public boolean hasNext() {
                return next != null && (unboundForwardFence || next.key != forwardFence);
			}
			
			protected Node<T> nextEntry() {
				lastReturned = next;
				previous = next;
				Node<T> result = next;
				next = moveNext(next);
				wasForward = isForward;
				return result;
			}
			
			public boolean hasPrevious() {
                return previous != null && (unboundBackwardFence || previous.key != backwardFence);
			}
			
			protected Node<T> previousEntry() {
				lastReturned = previous;
				next = previous;
				Node<T> result = previous;
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
			Object2LongRBTreeMap.this.clear();
		}
		
		@Override
		public int size() {
			return Object2LongRBTreeMap.this.size;
		}
		
		@Override
		public boolean contains(long e) {
			return containsValue(e);
		}
		
		@Override
		public boolean remove(Object o) {
			for(Node<T> entry = first; entry != null; entry = entry.next()) {
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
			for(Node<T> entry = first;entry != null;entry = entry.next())
				action.accept(entry.value);
		}
		
		@Override
		public void forEachIndexed(IntLongConsumer action) {
			Objects.requireNonNull(action);
			int index = 0;
			for(Node<T> entry = first;entry != null;entry = entry.next())
				action.accept(index++, entry.value);
		}
		
		@Override
		public <E> void forEach(E input, ObjectLongConsumer<E> action) {
			Objects.requireNonNull(action);
			for(Node<T> entry = first;entry != null;entry = entry.next())
				action.accept(input, entry.value);
		}
		
		@Override
		public boolean matchesAny(LongPredicate filter) {
			Objects.requireNonNull(filter);
			for(Node<T> entry = first;entry != null;entry = entry.next())
				if(filter.test(entry.value)) return true;
			return false;
		}
		
		@Override
		public boolean matchesNone(LongPredicate filter) {
			Objects.requireNonNull(filter);
			for(Node<T> entry = first;entry != null;entry = entry.next())
				if(filter.test(entry.value)) return false;
			return true;
		}
		
		@Override
		public boolean matchesAll(LongPredicate filter) {
			Objects.requireNonNull(filter);
			for(Node<T> entry = first;entry != null;entry = entry.next())
				if(!filter.test(entry.value)) return false;
			return true;
		}
		
		@Override
		public long reduce(long identity, LongLongUnaryOperator operator) {
			Objects.requireNonNull(operator);
			long state = identity;
			for(Node<T> entry = first;entry != null;entry = entry.next())
				state = operator.applyAsLong(state, entry.value);
			return state;
		}
		
		@Override
		public long reduce(LongLongUnaryOperator operator) {
			Objects.requireNonNull(operator);
			long state = 0L;
			boolean empty = true;
			for(Node<T> entry = first;entry != null;entry = entry.next()) {
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
		public long findFirst(LongPredicate filter) {
			Objects.requireNonNull(filter);
			for(Node<T> entry = first;entry != null;entry = entry.next())
				if(filter.test(entry.value)) return entry.value;
			return 0L;
		}
		
		@Override
		public int count(LongPredicate filter) {
			Objects.requireNonNull(filter);
			int result = 0;
			for(Node<T> entry = first;entry != null;entry = entry.next())
				if(filter.test(entry.value)) result++;
			return result;
		}
	}
	
	class EntrySet extends AbstractObjectSet<Object2LongMap.Entry<T>> {
		
		@Override
		public ObjectIterator<Object2LongMap.Entry<T>> iterator() {
			return new AscendingMapEntryIterator(first);
		}
		
		@Override
		public void clear() {
			Object2LongRBTreeMap.this.clear();
		}
		
		@Override
		public int size() {
			return Object2LongRBTreeMap.this.size;
		}
		
		@Override
		public boolean contains(Object o) {
			if (!(o instanceof Map.Entry)) return false;
			if(o instanceof Object2LongMap.Entry)
			{
				Object2LongMap.Entry<T> entry = (Object2LongMap.Entry<T>) o;
				if(entry.getKey() == null && comparator() == null) return false;
				T key = entry.getKey();
				Node<T> node = findNode(key);
				return node != null && entry.getLongValue() == node.getLongValue();
			}
			Map.Entry<?,?> entry = (Map.Entry<?,?>) o;
			if(entry.getKey() == null && comparator() == null) return false;
			T key = (T)entry.getKey();
			Node<T> node = findNode(key);
			return node != null && Objects.equals(entry.getValue(), Long.valueOf(node.getLongValue()));
		}
		
		@Override
		public boolean remove(Object o) {
			if (!(o instanceof Map.Entry)) return false;
			if(o instanceof Object2LongMap.Entry)
			{
				Object2LongMap.Entry<T> entry = (Object2LongMap.Entry<T>) o;
				T key = entry.getKey();
				Node<T> node = findNode(key);
				if (node != null && entry.getLongValue() == node.getLongValue()) {
					removeNode(node);
					return true;
				}
				return false;
			}
			Map.Entry<?,?> entry = (Map.Entry<?,?>) o;
			T key = (T)entry.getKey();
			Node<T> node = findNode(key);
			if (node != null && Objects.equals(entry.getValue(), Long.valueOf(node.getLongValue()))) {
				removeNode(node);
				return true;
			}
			return false;
		}
		
		@Override
		public void forEach(Consumer<? super Object2LongMap.Entry<T>> action) {
			Objects.requireNonNull(action);
			for(Node<T> entry = first;entry != null;entry = entry.next())
				action.accept(new BasicEntry<>(entry.key, entry.value));
		}
		
		@Override
		public void forEachIndexed(IntObjectConsumer<Object2LongMap.Entry<T>> action) {
			Objects.requireNonNull(action);
			int index = 0;
			for(Node<T> entry = first;entry != null;entry = entry.next())
				action.accept(index++, new BasicEntry<>(entry.key, entry.value));
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, Object2LongMap.Entry<T>> action) {
			Objects.requireNonNull(action);
			for(Node<T> entry = first;entry != null;entry = entry.next())
				action.accept(input, new BasicEntry<>(entry.key, entry.value));
		}
		
		@Override
		public boolean matchesAny(Predicate<Object2LongMap.Entry<T>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			BasicEntry<T> subEntry = new BasicEntry<>();
			for(Node<T> entry = first;entry != null;entry = entry.next()) {
				subEntry.set(entry.key, entry.value);
				if(filter.test(subEntry)) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Predicate<Object2LongMap.Entry<T>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			BasicEntry<T> subEntry = new BasicEntry<>();
			for(Node<T> entry = first;entry != null;entry = entry.next()) {
				subEntry.set(entry.key, entry.value);
				if(filter.test(subEntry)) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Predicate<Object2LongMap.Entry<T>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			BasicEntry<T> subEntry = new BasicEntry<>();
			for(Node<T> entry = first;entry != null;entry = entry.next()) {
				subEntry.set(entry.key, entry.value);
				if(!filter.test(subEntry)) return false;
			}
			return true;
		}
		
		@Override
		public <E> E reduce(E identity, BiFunction<E, Object2LongMap.Entry<T>, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			for(Node<T> entry = first;entry != null;entry = entry.next()) {
				state = operator.apply(state, new BasicEntry<>(entry.key, entry.value));
			}
			return state;
		}
		
		@Override
		public Object2LongMap.Entry<T> reduce(ObjectObjectUnaryOperator<Object2LongMap.Entry<T>, Object2LongMap.Entry<T>> operator) {
			Objects.requireNonNull(operator);
			Object2LongMap.Entry<T> state = null;
			boolean empty = true;
			for(Node<T> entry = first;entry != null;entry = entry.next()) {
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
		public Object2LongMap.Entry<T> findFirst(Predicate<Object2LongMap.Entry<T>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return null;
			BasicEntry<T> subEntry = new BasicEntry<>();
			for(Node<T> entry = first;entry != null;entry = entry.next()) {
				subEntry.set(entry.key, entry.value);
				if(filter.test(subEntry)) return subEntry;
			}
			return null;
		}
		
		@Override
		public int count(Predicate<Object2LongMap.Entry<T>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			int result = 0;
			BasicEntry<T> subEntry = new BasicEntry<>();
			for(Node<T> entry = first;entry != null;entry = entry.next()) {
				subEntry.set(entry.key, entry.value);
				if(filter.test(subEntry)) result++;
			}
			return result;
		}
	}
	
	class DescendingKeyIterator extends MapEntryIterator implements ObjectBidirectionalIterator<T>
	{
		public DescendingKeyIterator(Node<T> first) {
			super(first, false);
		}
		
		@Override
		protected Node<T> moveNext(Node<T> node) {
			return node.previous();
		}
		
		@Override
		protected Node<T> movePrevious(Node<T> node) {
			return node.next();
		}
		
		@Override
		public T previous() {
			if(!hasPrevious()) throw new NoSuchElementException();
			return previousEntry().key;
		}

		@Override
		public T next() {
			if(!hasNext()) throw new NoSuchElementException();
			return nextEntry().key;
		}
	}
	
	class AscendingMapEntryIterator extends MapEntryIterator implements ObjectBidirectionalIterator<Object2LongMap.Entry<T>>
	{
		public AscendingMapEntryIterator(Node<T> first)
		{
			super(first, true);
		}

		@Override
		public Object2LongMap.Entry<T> previous() {
			if(!hasPrevious()) throw new NoSuchElementException();
			return previousEntry();
		}

		@Override
		public Object2LongMap.Entry<T> next() {
			if(!hasNext()) throw new NoSuchElementException();
			return nextEntry();
		}
	}
	
	class AscendingValueIterator extends MapEntryIterator implements LongBidirectionalIterator
	{
		public AscendingValueIterator(Node<T> first) {
			super(first, true);
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
	
	class AscendingKeyIterator extends MapEntryIterator implements ObjectBidirectionalIterator<T>
	{
		public AscendingKeyIterator(Node<T> first) {
			super(first, true);
		}

		@Override
		public T previous() {
			if(!hasPrevious()) throw new NoSuchElementException();
			return previousEntry().key;
		}

		@Override
		public T next() {
			if(!hasNext()) throw new NoSuchElementException();
			return nextEntry().key;
		}
	}
	
	abstract class MapEntryIterator
	{
		final boolean isForward;
		boolean wasMoved = false;
		Node<T> lastReturned;
		Node<T> next;
		Node<T> previous;
		
		public MapEntryIterator(Node<T> first, boolean isForward) {
			next = first;
			previous = first == null ? null : movePrevious(first);
			this.isForward = isForward;
		}
		
		protected Node<T> moveNext(Node<T> node) {
			return node.next();
		}
		
		protected Node<T> movePrevious(Node<T> node) {
			return node.previous();
		}
		
		public boolean hasNext() {
            return next != null;
		}
		
		protected Node<T> nextEntry() {
			lastReturned = next;
			previous = next;
			Node<T> result = next;
			next = moveNext(next);
			wasMoved = isForward;
			return result;
		}
		
		public boolean hasPrevious() {
            return previous != null;
		}
		
		protected Node<T> previousEntry() {
			lastReturned = previous;
			next = previous;
			Node<T> result = previous;
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
	
	private static final class Node<T> implements Object2LongMap.Entry<T>
	{
		static final int BLACK = 1;
		
		T key;
		long value;
		int state;
		Node<T> parent;
		Node<T> left;
		Node<T> right;
		
		Node(T key, long value, Node<T> parent) {
			this.key = key;
			this.value = value;
			this.parent = parent;
		}
		
		Node<T> copy() {
			Node<T> entry = new Node<>(key, value, null);
			entry.state = state;
			if(left != null) {
				Node<T> newLeft = left.copy();
				entry.left = newLeft;
				newLeft.parent = entry;
			}
			if(right != null) {
				Node<T> newRight = right.copy();
				entry.right = newRight;
				newRight.parent = entry;
			}
			return entry;
		}
		
		public BasicEntry<T> export() {
			return new BasicEntry<>(key, value);
		}
		
		@Override
		public T getKey() {
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
				if(obj instanceof Object2LongMap.Entry) {
					Object2LongMap.Entry<T> entry = (Object2LongMap.Entry<T>)obj;
					if(entry.getKey() == null) return false;
					return Objects.equals(key, entry.getKey()) && value == entry.getLongValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object otherKey = entry.getKey();
				if(otherKey == null) return false;
				Object otherValue = entry.getValue();
				return otherValue instanceof Long && Objects.equals(key, otherKey) && value == ((Long)otherValue).longValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Objects.hashCode(key) ^ Long.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Objects.toString(key) + "=" + Long.toString(value);
		}
		
		boolean isBlack() {
			return (state & BLACK) != 0;
		}
		
		void setBlack(boolean value) {
			if(value) state |= BLACK;
			else state &= ~BLACK;
		}
		
		boolean needsSuccessor() { return left != null && right != null; }
		
		boolean replace(Node<T> entry) {
			if(entry != null) entry.parent = parent;
			if(parent != null) {
				if(parent.left == this) parent.left = entry;
				else parent.right = entry;
			}
			return parent == null;
		}
		
		Node<T> next() {
			if(right != null) {
				Node<T> parent = right;
				while(parent.left != null) parent = parent.left;
				return parent;
			}
			Node<T> parent = this.parent;
			Node<T> control = this;
			while(parent != null && control == parent.right) {
				control = parent;
				parent = parent.parent;
			}
			return parent;
		}
		
		Node<T> previous() {
			if(left != null) {
				Node<T> parent = left;
				while(parent.right != null) parent = parent.right;
				return parent;
			}
			Node<T> parent = this.parent;
			Node<T> control = this;
			while(parent != null && control == parent.left) {
				control = parent;
				parent = parent.parent;
			}
			return parent;
		}
	}
}