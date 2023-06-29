package speiger.src.collections.floats.maps.impl.tree;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import speiger.src.collections.floats.collections.FloatBidirectionalIterator;
import speiger.src.collections.floats.functions.FloatComparator;
import speiger.src.collections.floats.functions.FloatConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectFloatConsumer;
import speiger.src.collections.ints.functions.consumer.IntFloatConsumer;
import speiger.src.collections.ints.functions.consumer.IntObjectConsumer;
import speiger.src.collections.floats.functions.function.FloatFunction;
import speiger.src.collections.floats.functions.consumer.FloatObjectConsumer;
import speiger.src.collections.floats.functions.function.FloatPredicate;
import speiger.src.collections.floats.functions.function.FloatObjectUnaryOperator;
import speiger.src.collections.floats.functions.function.FloatFloatUnaryOperator;
import speiger.src.collections.floats.maps.abstracts.AbstractFloat2ObjectMap;
import speiger.src.collections.floats.maps.interfaces.Float2ObjectMap;
import speiger.src.collections.floats.maps.interfaces.Float2ObjectNavigableMap;
import speiger.src.collections.floats.sets.AbstractFloatSet;
import speiger.src.collections.floats.sets.FloatNavigableSet;
import speiger.src.collections.objects.collections.AbstractObjectCollection;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.functions.ObjectSupplier;
import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;
import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;
import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;

import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectSet;

/**
 * A Simple Type Specific RB TreeMap implementation that reduces boxing/unboxing.
 * It is using a bit more memory then <a href="https://github.com/vigna/fastutil">FastUtil</a>,
 * but it saves a lot of Performance on the Optimized removal and iteration logic.
 * Which makes the implementation actually useable and does not get outperformed by Javas default implementation.
 * @param <V> the keyType of elements maintained by this Collection
 */
public class Float2ObjectRBTreeMap<V> extends AbstractFloat2ObjectMap<V> implements Float2ObjectNavigableMap<V>
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
	protected transient FloatComparator comparator;
	
	/** the default return value for max searches */
	protected float defaultMaxNotFound = Float.MIN_VALUE;
	/** the default return value for min searches */
	protected float defaultMinNotFound = Float.MAX_VALUE;
	
	/** KeySet Cache */
	protected FloatNavigableSet keySet;
	/** Values Cache */
	protected ObjectCollection<V> values;
	/** EntrySet Cache */
	protected ObjectSet<Float2ObjectMap.Entry<V>> entrySet;
	
	/**
	 * Default Constructor
	 */
	public Float2ObjectRBTreeMap() {
	}
	
	/**
	 * Constructor that allows to define the sorter
	 * @param comp the function that decides how the tree is sorted, can be null
	 */
	public Float2ObjectRBTreeMap(FloatComparator comp) {
		comparator = comp;
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Float2ObjectRBTreeMap(Float[] keys, V[] values) {
		this(keys, values, null);
	}
	
	/**
	 * Helper constructor that has a custom sorter and allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param comp the function that decides how the tree is sorted, can be null
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Float2ObjectRBTreeMap(Float[] keys, V[] values, FloatComparator comp) {
		comparator = comp;
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i].floatValue(), values[i]);
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Float2ObjectRBTreeMap(float[] keys, V[] values) {
		this(keys, values, null);
	}
	
	/**
	 * Helper constructor that has a custom sorter and allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param comp the function that decides how the tree is sorted, can be null
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Float2ObjectRBTreeMap(float[] keys, V[] values, FloatComparator comp) {
		comparator = comp;
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i], values[i]);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 */
	public Float2ObjectRBTreeMap(Map<? extends Float, ? extends V> map) {
		this(map, null);
	}
	
	/**
	 * A Helper constructor that has a custom sorter and allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param comp the function that decides how the tree is sorted, can be null
	 */
	public Float2ObjectRBTreeMap(Map<? extends Float, ? extends V> map, FloatComparator comp) {
		comparator = comp;
		putAll(map);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 */
	public Float2ObjectRBTreeMap(Float2ObjectMap<V> map) {
		this(map, null);
	}
	
	/**
	 * A Type Specific Helper function that has a custom sorter and allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param comp the function that decides how the tree is sorted, can be null
 	 */
	public Float2ObjectRBTreeMap(Float2ObjectMap<V> map, FloatComparator comp) {
		comparator = comp;
		putAll(map);
	}

	@Override
	public void setDefaultMaxValue(float value) { defaultMaxNotFound = value; }
	@Override
	public float getDefaultMaxValue() { return defaultMaxNotFound; }
	@Override
	public void setDefaultMinValue(float value) { defaultMinNotFound = value; }
	@Override
	public float getDefaultMinValue() { return defaultMinNotFound; }
	
	
	@Override
	public V put(float key, V value) {
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
	public V putIfAbsent(float key, V value) {
		if(tree == null) {
			tree = first = last = new Node<>(key, value, null);
			size++;
			return getDefaultReturnValue();
		}
		int compare = 0;
		Node<V> parent = tree;
		while(true) {
			if((compare = compare(key, parent.key)) == 0) {
				if(Objects.equals(parent.value, getDefaultReturnValue())) return parent.setValue(value);
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
	public FloatComparator comparator() { return comparator; }

	@Override
	public boolean containsKey(float key) {
		return findNode(key) != null;
	}
	
	@Override
	public V get(float key) {
		Node<V> node = findNode(key);
		return node == null ? getDefaultReturnValue() : node.value;
	}
	
	@Override
	public V getOrDefault(float key, V defaultValue) {
		Node<V> node = findNode(key);
		return node == null ? defaultValue : node.value;
	}
	
	@Override
	public float firstFloatKey() {
		if(tree == null) throw new NoSuchElementException();
		return first.key;
	}
	
	@Override
	public float pollFirstFloatKey() {
		if(tree == null) return getDefaultMinValue();
		float result = first.key;
		removeNode(first);
		return result;
	}
	
	@Override
	public float lastFloatKey() {
		if(tree == null) throw new NoSuchElementException();
		return last.key;
	}
	
	@Override
	public float pollLastFloatKey() {
		if(tree == null) return getDefaultMaxValue();
		float result = last.key;
		removeNode(last);
		return result;
	}
	
	@Override
	public Float2ObjectMap.Entry<V> firstEntry() {
		if(tree == null) return null;
		return first.export();
	}
	
	@Override
	public Float2ObjectMap.Entry<V> lastEntry() {
		if(tree == null) return null;
		return last.export();
	}
	
	@Override
	public Float2ObjectMap.Entry<V> pollFirstEntry() {
		if(tree == null) return null;
		BasicEntry<V> entry = first.export();
		removeNode(first);
		return entry;
	}
	
	@Override
	public Float2ObjectMap.Entry<V> pollLastEntry() {
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
	public V remove(float key) {
		Node<V> entry = findNode(key);
		if(entry == null) return getDefaultReturnValue();
		V value = entry.value;
		removeNode(entry);
		return value;
	}
	
	@Override
	public V removeOrDefault(float key, V defaultValue) {
		Node<V> entry = findNode(key);
		if(entry == null) return defaultValue;
		V value = entry.value;
		removeNode(entry);
		return value;
	}
	
	@Override
	public boolean remove(float key, V value) {
		Node<V> entry = findNode(key);
		if(entry == null || entry.value != value) return false;
		removeNode(entry);
		return true;
	}
	
	@Override
	public boolean replace(float key, V oldValue, V newValue) {
		Node<V> entry = findNode(key);
		if(entry == null || entry.value != oldValue) return false;
		entry.value = newValue;
		return true;
	}
	
	@Override
	public V replace(float key, V value) {
		Node<V> entry = findNode(key);
		if(entry == null) return getDefaultReturnValue();
		V oldValue = entry.value;
		entry.value = value;
		return oldValue;
	}
	
	@Override
	public V compute(float key, FloatObjectUnaryOperator<V> mappingFunction) {
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
	public V computeIfAbsent(float key, FloatFunction<V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		Node<V> entry = findNode(key);
		if(entry == null) {
			V newValue = mappingFunction.apply(key);
			if(Objects.equals(newValue, getDefaultReturnValue())) return newValue;
			put(key, newValue);
			return newValue;
		}
		if(Objects.equals(entry.value, getDefaultReturnValue())) {
			V newValue = mappingFunction.apply(key);
			if(Objects.equals(newValue, getDefaultReturnValue())) return newValue;
			entry.value = newValue;
		}
		return entry.value;
	}
	
	@Override
	public V supplyIfAbsent(float key, ObjectSupplier<V> valueProvider) {
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
	public V computeIfPresent(float key, FloatObjectUnaryOperator<V> mappingFunction) {
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
	public V merge(float key, V value, ObjectObjectUnaryOperator<V, V> mappingFunction) {
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
	public void mergeAll(Float2ObjectMap<V> m, ObjectObjectUnaryOperator<V, V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Float2ObjectMap.Entry<V> entry : getFastIterable(m)) {
			float key = entry.getFloatKey();
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
	public void forEach(FloatObjectConsumer<V> action) {
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
	
	protected FloatBidirectionalIterator keyIterator() {
		return new AscendingKeyIterator(first);
	}
	
	protected FloatBidirectionalIterator keyIterator(float element) {
		return new AscendingKeyIterator(findNode(element));
	}
	
	protected FloatBidirectionalIterator descendingKeyIterator() {
		return new DescendingKeyIterator(last);
	}
		
	@Override
	public Float2ObjectRBTreeMap<V> copy() {
		Float2ObjectRBTreeMap<V> set = new Float2ObjectRBTreeMap<>();
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
	public FloatNavigableSet keySet() {
		return navigableKeySet();
	}
	
	@Override
	public ObjectSet<Float2ObjectMap.Entry<V>> float2ObjectEntrySet() {
		if(entrySet == null) entrySet = new EntrySet();
		return entrySet;
	}
	
	@Override
	public ObjectCollection<V> values() {
		if(values == null) values = new Values();
		return values;
	}
	
	@Override
	public FloatNavigableSet navigableKeySet() {
		if(keySet == null) keySet = new KeySet<>(this);
		return keySet;
	}
	
	@Override
	public Float2ObjectNavigableMap<V> descendingMap() {
		return new DescendingNaivgableSubMap<>(this, true, 0F, true, true, 0F, true);
	}
	
	@Override
	public FloatNavigableSet descendingKeySet() {
		return descendingMap().navigableKeySet();
	}
	
	@Override
	public Float2ObjectNavigableMap<V> subMap(float fromKey, boolean fromInclusive, float toKey, boolean toInclusive) {
		return new AscendingNaivgableSubMap<>(this, false, fromKey, fromInclusive, false, toKey, toInclusive);
	}
	
	@Override
	public Float2ObjectNavigableMap<V> headMap(float toKey, boolean inclusive) {
		return new AscendingNaivgableSubMap<>(this, true, 0F, true, false, toKey, inclusive);
	}
	
	@Override
	public Float2ObjectNavigableMap<V> tailMap(float fromKey, boolean inclusive) {
		return new AscendingNaivgableSubMap<>(this, false, fromKey, inclusive, true, 0F, true);
	}
	
	@Override
	public float lowerKey(float e) {
		Node<V> node = findLowerNode(e);
		return node != null ? node.key : getDefaultMinValue();
	}

	@Override
	public float floorKey(float e) {
		Node<V> node = findFloorNode(e);
		return node != null ? node.key : getDefaultMinValue();
	}
	
	@Override
	public float higherKey(float e) {
		Node<V> node = findHigherNode(e);
		return node != null ? node.key : getDefaultMaxValue();
	}

	@Override
	public float ceilingKey(float e) {
		Node<V> node = findCeilingNode(e);
		return node != null ? node.key : getDefaultMaxValue();
	}
	
	@Override
	public Float2ObjectMap.Entry<V> lowerEntry(float key) {
		Node<V> node = findLowerNode(key);
		return node != null ? node.export() : null;
	}
	
	@Override
	public Float2ObjectMap.Entry<V> higherEntry(float key) {
		Node<V> node = findHigherNode(key);
		return node != null ? node.export() : null;
	}
	
	@Override
	public Float2ObjectMap.Entry<V> floorEntry(float key) {
		Node<V> node = findFloorNode(key);
		return node != null ? node.export() : null;
	}
	
	@Override
	public Float2ObjectMap.Entry<V> ceilingEntry(float key) {
		Node<V> node = findCeilingNode(key);
		return node != null ? node.export() : null;
	}
	
	protected Node<V> findLowerNode(float key) {
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
	
	protected Node<V> findFloorNode(float key) {
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
	
	protected Node<V> findCeilingNode(float key) {
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
	
	protected Node<V> findHigherNode(float key) {
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
	
	protected Node<V> findNode(float key) {
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
	
	protected void validate(float k) { compare(k, k); }
	protected int compare(float k, float v) { return comparator != null ? comparator.compare(k, v) : Float.compare(k, v);}
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
	
	static class KeySet<V> extends AbstractFloatSet implements FloatNavigableSet
	{
		Float2ObjectNavigableMap<V> map;

		public KeySet(Float2ObjectNavigableMap<V> map) {
			this.map = map;
		}
		
		@Override
		public void setDefaultMaxValue(float e) { map.setDefaultMaxValue(e); }
		@Override
		public float getDefaultMaxValue() { return map.getDefaultMaxValue(); }
		@Override
		public void setDefaultMinValue(float e) { map.setDefaultMinValue(e); }
		@Override
		public float getDefaultMinValue() { return map.getDefaultMinValue(); }
		@Override
		public float lower(float e) { return map.lowerKey(e); }
		@Override
		public float floor(float e) { return map.floorKey(e); }
		@Override
		public float ceiling(float e) { return map.ceilingKey(e); }
		@Override
		public float higher(float e) { return map.higherKey(e); }
		
		@Override
		public Float lower(Float e) {
			Float2ObjectMap.Entry<V> node = map.lowerEntry(e.floatValue());
			return node != null ? node.getKey() : null;
		}
		
		@Override
		public Float floor(Float e) {
			Float2ObjectMap.Entry<V> node = map.floorEntry(e.floatValue());
			return node != null ? node.getKey() : null;
		}
		
		@Override
		public Float higher(Float e) {
			Float2ObjectMap.Entry<V> node = map.higherEntry(e.floatValue());
			return node != null ? node.getKey() : null;
		}
		
		@Override
		public Float ceiling(Float e) {
			Float2ObjectMap.Entry<V> node = map.ceilingEntry(e.floatValue());
			return node != null ? node.getKey() : null;
		}
		
		@Override
		public float pollFirstFloat() { return map.pollFirstFloatKey(); }
		@Override
		public float pollLastFloat() { return map.pollLastFloatKey(); }
		@Override
		public FloatComparator comparator() { return map.comparator(); }
		@Override
		public float firstFloat() { return map.firstFloatKey(); } 
		@Override
		public float lastFloat() { return map.lastFloatKey(); }
		@Override
		public void clear() { map.clear(); }
		
		@Override
		public boolean remove(float o) { 
			int oldSize = map.size();
			map.remove(o); 
			return oldSize != map.size();
		}
		
		@Override
		public boolean add(float e) { throw new UnsupportedOperationException(); }
		@Override
		public FloatBidirectionalIterator iterator(float fromElement) {
			if(map instanceof Float2ObjectRBTreeMap) return ((Float2ObjectRBTreeMap<V>)map).keyIterator(fromElement);
			return ((NavigableSubMap<V>)map).keyIterator(fromElement);
		}
		
		@Override
		public FloatNavigableSet subSet(float fromElement, boolean fromInclusive, float toElement, boolean toInclusive) { return new KeySet<>(map.subMap(fromElement, fromInclusive, toElement, toInclusive)); }
		@Override
		public FloatNavigableSet headSet(float toElement, boolean inclusive) { return new KeySet<>(map.headMap(toElement, inclusive)); }
		@Override
		public FloatNavigableSet tailSet(float fromElement, boolean inclusive) { return new KeySet<>(map.tailMap(fromElement, inclusive)); }
		
		@Override
		public FloatBidirectionalIterator iterator() {
			if(map instanceof Float2ObjectRBTreeMap) return ((Float2ObjectRBTreeMap<V>)map).keyIterator();
			return ((NavigableSubMap<V>)map).keyIterator();
		}
		
		@Override
		public FloatBidirectionalIterator descendingIterator() {
			if(map instanceof Float2ObjectRBTreeMap) return ((Float2ObjectRBTreeMap<V>)map).descendingKeyIterator();
			return ((NavigableSubMap<V>)map).descendingKeyIterator();
		}
		
		protected Node<V> start() {
			if(map instanceof Float2ObjectRBTreeMap) return ((Float2ObjectRBTreeMap<V>)map).first;
			return ((NavigableSubMap<V>)map).subLowest();
		}
		
		protected Node<V> end() {
			if(map instanceof Float2ObjectRBTreeMap) return null;
			return ((NavigableSubMap<V>)map).subHighest();
		}
		
		protected Node<V> next(Node<V> entry) {
			if(map instanceof Float2ObjectRBTreeMap) return entry.next();
			return ((NavigableSubMap<V>)map).next(entry);
		}
		
		protected Node<V> previous(Node<V> entry) {
			if(map instanceof Float2ObjectRBTreeMap) return entry.previous();
			return ((NavigableSubMap<V>)map).previous(entry);
		}
		
		@Override
		public FloatNavigableSet descendingSet() { return new KeySet<>(map.descendingMap()); }
		@Override
		public KeySet<V> copy() { throw new UnsupportedOperationException(); }
		@Override
		public boolean isEmpty() { return map.isEmpty(); }
		@Override
		public int size() { return map.size(); }
		
		@Override
		public void forEach(FloatConsumer action) {
			Objects.requireNonNull(action);
			for(Node<V> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				action.accept(entry.key);
		}
		
		@Override
		public void forEachIndexed(IntFloatConsumer action) {
			Objects.requireNonNull(action);
			int index = 0;
			for(Node<V> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				action.accept(index++, entry.key);
		}
		
		@Override
		public <E> void forEach(E input, ObjectFloatConsumer<E> action) {
			Objects.requireNonNull(action);
			for(Node<V> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				action.accept(input, entry.key);
		}
		
		@Override
		public boolean matchesAny(FloatPredicate filter) {
			Objects.requireNonNull(filter);
			for(Node<V> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				if(filter.test(entry.key)) return true;
			return false;
		}
		
		@Override
		public boolean matchesNone(FloatPredicate filter) {
			Objects.requireNonNull(filter);
			for(Node<V> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				if(filter.test(entry.key)) return false;
			return true;
		}
		
		@Override
		public boolean matchesAll(FloatPredicate filter) {
			Objects.requireNonNull(filter);
			for(Node<V> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				if(!filter.test(entry.key)) return false;
			return true;
		}
		
		@Override
		public float reduce(float identity, FloatFloatUnaryOperator operator) {
			Objects.requireNonNull(operator);
			float state = identity;
			for(Node<V> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				state = operator.applyAsFloat(state, entry.key);
			return state;
		}
		
		@Override
		public float reduce(FloatFloatUnaryOperator operator) {
			Objects.requireNonNull(operator);
			float state = 0F;
			boolean empty = true;
			for(Node<V> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry)) {
				if(empty) {
					empty = false;
					state = entry.key;
					continue;
				}
				state = operator.applyAsFloat(state, entry.key);
			}
			return state;
		}
		
		@Override
		public float findFirst(FloatPredicate filter) {
			Objects.requireNonNull(filter);
			for(Node<V> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				if(filter.test(entry.key)) return entry.key;
			return 0F;
		}
		
		@Override
		public int count(FloatPredicate filter) {
			Objects.requireNonNull(filter);
			int result = 0;
			for(Node<V> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				if(filter.test(entry.key)) result++;
			return result;
		}
	}
	
	static class AscendingNaivgableSubMap<V> extends NavigableSubMap<V>
	{
		AscendingNaivgableSubMap(Float2ObjectRBTreeMap<V> map, boolean fromStart, float lo, boolean loInclusive, boolean toEnd, float hi, boolean hiInclusive) {
			super(map, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
		}
		
		@Override
		public Float2ObjectNavigableMap<V> descendingMap() {
			if(inverse == null) inverse = new DescendingNaivgableSubMap<>(map, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
			return inverse;
		}
		
		@Override
		public ObjectSet<Float2ObjectMap.Entry<V>> float2ObjectEntrySet() {
			if(entrySet == null) entrySet = new AscendingSubEntrySet();
			return entrySet;
		}
		
		@Override
		public FloatNavigableSet navigableKeySet() {
			if(keySet == null) keySet = new KeySet<>(this);
			return keySet;
		}
		
		@Override
		public FloatNavigableSet keySet() {
			return navigableKeySet();
		}
		
		@Override
		public Float2ObjectNavigableMap<V> subMap(float fromKey, boolean fromInclusive, float toKey, boolean toInclusive) {
			if (!inRange(fromKey, fromInclusive)) throw new IllegalArgumentException("fromKey out of range");
			if (!inRange(toKey, toInclusive)) throw new IllegalArgumentException("toKey out of range");
			return new AscendingNaivgableSubMap<>(map, false, fromKey, fromInclusive, false, toKey, toInclusive);
		}
		
		@Override
		public Float2ObjectNavigableMap<V> headMap(float toKey, boolean inclusive) {
			if (!inRange(toKey, inclusive)) throw new IllegalArgumentException("toKey out of range");
			return new AscendingNaivgableSubMap<>(map, fromStart, lo, loInclusive, false, toKey, inclusive);
		}
		
		@Override
		public Float2ObjectNavigableMap<V> tailMap(float fromKey, boolean inclusive) {
			if (!inRange(fromKey, inclusive)) throw new IllegalArgumentException("fromKey out of range");
			return new AscendingNaivgableSubMap<>(map, false, fromKey, inclusive, toEnd, hi, hiInclusive);
		}
		
		@Override
		protected Node<V> subLowest() { return absLowest(); }
		@Override
		protected Node<V> subHighest() { return absHighest(); }
		@Override
		protected Node<V> subCeiling(float key) { return absCeiling(key); }
		@Override
		protected Node<V> subHigher(float key) { return absHigher(key); }
		@Override
		protected Node<V> subFloor(float key) { return absFloor(key); }
		@Override
		protected Node<V> subLower(float key) { return absLower(key); }
		
		@Override
		protected FloatBidirectionalIterator keyIterator() {
			return new AcsendingSubKeyIterator(absLowest(), absHighFence(), absLowFence()); 
		}
		@Override
		protected FloatBidirectionalIterator keyIterator(float element) {
			return new AcsendingSubKeyIterator(absLower(element), absHighFence(), absLowFence()); 
		}
		
		@Override
		protected ObjectBidirectionalIterator<V> valueIterator() {
			return new AcsendingSubValueIterator(absLowest(), absHighFence(), absLowFence()); 
		}
		
		@Override
		protected FloatBidirectionalIterator descendingKeyIterator() {
			return new DecsendingSubKeyIterator(absHighest(), absLowFence(), absHighFence()); 
		}
		
		class AscendingSubEntrySet extends SubEntrySet {
			@Override
			public ObjectIterator<Float2ObjectMap.Entry<V>> iterator() {
				return new AcsendingSubEntryIterator(absLowest(), absHighFence(), absLowFence());
			}
		}
	}
	
	static class DescendingNaivgableSubMap<V> extends NavigableSubMap<V>
	{
		FloatComparator comparator;
		DescendingNaivgableSubMap(Float2ObjectRBTreeMap<V> map, boolean fromStart, float lo, boolean loInclusive, boolean toEnd, float hi, boolean hiInclusive) {
			super(map, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
			comparator = map.comparator() == null ? FloatComparator.of(Collections.reverseOrder()) : map.comparator().reversed();
		}
		
		@Override
		public FloatComparator comparator() { return comparator; }
		
		@Override
		public Float2ObjectNavigableMap<V> descendingMap() {
			if(inverse == null) inverse = new AscendingNaivgableSubMap<>(map, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
			return inverse;
		}

		@Override
		public FloatNavigableSet navigableKeySet() {
			if(keySet == null) keySet = new KeySet<>(this);
			return keySet;
		}
		
		@Override
		public FloatNavigableSet keySet() {
			return navigableKeySet();
		}
		
		@Override
		public Float2ObjectNavigableMap<V> subMap(float fromKey, boolean fromInclusive, float toKey, boolean toInclusive) {
			if (!inRange(fromKey, fromInclusive)) throw new IllegalArgumentException("fromKey out of range");
			if (!inRange(toKey, toInclusive)) throw new IllegalArgumentException("toKey out of range");
			return new DescendingNaivgableSubMap<>(map, false, toKey, toInclusive, false, fromKey, fromInclusive);
		}
		
		@Override
		public Float2ObjectNavigableMap<V> headMap(float toKey, boolean inclusive) {
			if (!inRange(toKey, inclusive)) throw new IllegalArgumentException("toKey out of range");
			return new DescendingNaivgableSubMap<>(map, false, toKey, inclusive, toEnd, hi, hiInclusive);
		}
		
		@Override
		public Float2ObjectNavigableMap<V> tailMap(float fromKey, boolean inclusive) {
			if (!inRange(fromKey, inclusive)) throw new IllegalArgumentException("fromKey out of range");
			return new DescendingNaivgableSubMap<>(map, fromStart, lo, loInclusive, false, fromKey, inclusive);
		}
		
		@Override
		public ObjectSet<Float2ObjectMap.Entry<V>> float2ObjectEntrySet() {
			if(entrySet == null) entrySet = new DescendingSubEntrySet();
			return entrySet;
		}
		
		@Override
		protected Node<V> subLowest() { return absHighest(); }
		@Override
		protected Node<V> subHighest() { return absLowest(); }
		@Override
		protected Node<V> subCeiling(float key) { return absFloor(key); }
		@Override
		protected Node<V> subHigher(float key) { return absLower(key); }
		@Override
		protected Node<V> subFloor(float key) { return absCeiling(key); }
		@Override
		protected Node<V> subLower(float key) { return absHigher(key); }
		@Override
		protected Node<V> next(Node<V> entry) { return entry.previous(); }
		@Override
		protected Node<V> previous(Node<V> entry) { return entry.next(); }
		
		@Override
		protected FloatBidirectionalIterator keyIterator() {
			return new DecsendingSubKeyIterator(absHighest(), absLowFence(), absHighFence()); 
		}
		
		@Override
		protected FloatBidirectionalIterator keyIterator(float element) {
			return new DecsendingSubKeyIterator(absHigher(element), absLowFence(), absHighFence()); 
		}
		
		@Override
		protected ObjectBidirectionalIterator<V> valueIterator() {
			return new DecsendingSubValueIterator(absHighest(), absLowFence(), absHighFence()); 
		}
		
		@Override
		protected FloatBidirectionalIterator descendingKeyIterator() {
			return new AcsendingSubKeyIterator(absLowest(), absHighFence(), absLowFence()); 
		}
		
		class DescendingSubEntrySet extends SubEntrySet {
			@Override
			public ObjectIterator<Float2ObjectMap.Entry<V>> iterator() {
				return new DecsendingSubEntryIterator(absHighest(), absLowFence(), absHighFence());
			}
		}
	}
	
	static abstract class NavigableSubMap<V> extends AbstractFloat2ObjectMap<V> implements Float2ObjectNavigableMap<V>
	{
		final Float2ObjectRBTreeMap<V> map;
		final float lo, hi;
		final boolean fromStart, toEnd;
		final boolean loInclusive, hiInclusive;
		
		Float2ObjectNavigableMap<V> inverse;
		FloatNavigableSet keySet;
		ObjectSet<Float2ObjectMap.Entry<V>> entrySet;
		ObjectCollection<V> values;
		
		NavigableSubMap(Float2ObjectRBTreeMap<V> map, boolean fromStart, float lo, boolean loInclusive, boolean toEnd, float hi, boolean hiInclusive) {
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
		public void setDefaultMaxValue(float e) { map.setDefaultMaxValue(e); }
		@Override
		public float getDefaultMaxValue() { return map.getDefaultMaxValue(); }
		@Override
		public void setDefaultMinValue(float e) { map.setDefaultMinValue(e); }
		@Override
		public float getDefaultMinValue() { return map.getDefaultMinValue(); }
		protected boolean isNullComparator() { return map.comparator() == null; }
		
		@Override
		public AbstractFloat2ObjectMap<V> setDefaultReturnValue(V v) { 
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
		public FloatNavigableSet descendingKeySet() {
			return descendingMap().navigableKeySet();
		}
		
		@Override
		public FloatNavigableSet keySet() {
			return navigableKeySet();
		}
		
		protected abstract Node<V> subLowest();
		protected abstract Node<V> subHighest();
		protected abstract Node<V> subCeiling(float key);
		protected abstract Node<V> subHigher(float key);
		protected abstract Node<V> subFloor(float key);
		protected abstract Node<V> subLower(float key);
		protected abstract FloatBidirectionalIterator keyIterator();
		protected abstract FloatBidirectionalIterator keyIterator(float element);
		protected abstract ObjectBidirectionalIterator<V> valueIterator();
		protected abstract FloatBidirectionalIterator descendingKeyIterator();
		protected float lowKeyOrNull(Node<V> entry) { return entry == null ? getDefaultMinValue() : entry.key; }
		protected float highKeyOrNull(Node<V> entry) { return entry == null ? getDefaultMaxValue() : entry.key; }
		protected Node<V> next(Node<V> entry) { return entry.next(); }
		protected Node<V> previous(Node<V> entry) { return entry.previous(); }
		
		protected boolean tooLow(float key) {
			if (!fromStart) {
				int c = map.compare(key, lo);
				if (c < 0 || (c == 0 && !loInclusive)) return true;
			}
			return false;
		}
		
		protected boolean tooHigh(float key) {
			if (!toEnd) {
				int c = map.compare(key, hi);
				if (c > 0 || (c == 0 && !hiInclusive)) return true;
			}
			return false;
		}
		protected boolean inRange(float key) { return !tooLow(key) && !tooHigh(key); }
		protected boolean inClosedRange(float key) { return (fromStart || map.compare(key, lo) >= 0) && (toEnd || map.compare(hi, key) >= 0); }
		protected boolean inRange(float key, boolean inclusive) { return inclusive ? inRange(key) : inClosedRange(key); }
		
		protected Node<V> absLowest() {
			Node<V> e = (fromStart ?  map.first : (loInclusive ? map.findCeilingNode(lo) : map.findHigherNode(lo)));
			return (e == null || tooHigh(e.key)) ? null : e;
		}
		
		protected Node<V> absHighest() {
			Node<V> e = (toEnd ?  map.last : (hiInclusive ?  map.findFloorNode(hi) : map.findLowerNode(hi)));
			return (e == null || tooLow(e.key)) ? null : e;
		}
		
		protected Node<V> absCeiling(float key) {
			if (tooLow(key)) return absLowest();
			Node<V> e = map.findCeilingNode(key);
			return (e == null || tooHigh(e.key)) ? null : e;
		}
		
		protected Node<V> absHigher(float key) {
			if (tooLow(key)) return absLowest();
			Node<V> e = map.findHigherNode(key);
			return (e == null || tooHigh(e.key)) ? null : e;
		}
		
		protected Node<V> absFloor(float key) {
			if (tooHigh(key)) return absHighest();
			Node<V> e = map.findFloorNode(key);
			return (e == null || tooLow(e.key)) ? null : e;
		}
		
		protected Node<V> absLower(float key) {
			if (tooHigh(key)) return absHighest();
			Node<V> e = map.findLowerNode(key);
			return (e == null || tooLow(e.key)) ? null : e;
		}
		
		protected Node<V> absHighFence() { return (toEnd ? null : (hiInclusive ? map.findHigherNode(hi) : map.findCeilingNode(hi))); }
		protected Node<V> absLowFence() { return (fromStart ? null : (loInclusive ?  map.findLowerNode(lo) : map.findFloorNode(lo))); }
		
		@Override
		public FloatComparator comparator() { return map.comparator(); }
		
		@Override
		public float pollFirstFloatKey() {
			Node<V> entry = subLowest();
			if(entry != null) {
				float result = entry.key;
				map.removeNode(entry);
				return result;
			}
			return getDefaultMinValue();
		}
		
		@Override
		public float pollLastFloatKey() {
			Node<V> entry = subHighest();
			if(entry != null) {
				float result = entry.key;
				map.removeNode(entry);
				return result;
			}
			return getDefaultMaxValue();
		}
		
		@Override
		public V firstValue() {
			Node<V> entry = subLowest();
			if(entry == null) throw new NoSuchElementException();
			return entry.value;
		}
		
		@Override
		public V lastValue() {
			Node<V> entry = subHighest();
			if(entry == null) throw new NoSuchElementException();
			return entry.value;
		}
		
		@Override
		public float firstFloatKey() {
			Node<V> entry = subLowest();
			if(entry == null) throw new NoSuchElementException();
			return entry.key;
		}
		
		@Override
		public float lastFloatKey() {
			Node<V> entry = subHighest();
			if(entry == null) throw new NoSuchElementException();
			return entry.key;
		}
		
		@Override
		public V put(float key, V value) {
			if (!inRange(key)) throw new IllegalArgumentException("key out of range");
			return map.put(key, value);
		}
		
		@Override
		public V putIfAbsent(float key, V value) {
			if (!inRange(key)) throw new IllegalArgumentException("key out of range");
			return map.putIfAbsent(key, value);
		}
		
		@Override
		public boolean containsKey(float key) { return inRange(key) && map.containsKey(key); }
		
		@Override
		public V computeIfPresent(float key, FloatObjectUnaryOperator<V> mappingFunction) {
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
		public V remove(float key) {
			return inRange(key) ? map.remove(key) : getDefaultReturnValue();
		}
		
		@Override
		public V removeOrDefault(float key, V defaultValue) {
			return inRange(key) ? map.removeOrDefault(key, defaultValue) : defaultValue;
		}
		
		@Override
		public boolean remove(float key, V value) {
			return inRange(key) && map.remove(key, value);
		}
		
		
		@Override
		public V get(float key) {
			return inRange(key) ? map.get(key) : getDefaultReturnValue();
		}
		
		@Override
		public V getOrDefault(float key, V defaultValue) {
			return inRange(key) ? map.getOrDefault(key, defaultValue) : getDefaultReturnValue();
		}
		
		
		@Override
		public float lowerKey(float key) { return lowKeyOrNull(subLower(key)); }
		@Override
		public float floorKey(float key) { return lowKeyOrNull(subFloor(key)); }
		@Override
		public float ceilingKey(float key) { return highKeyOrNull(subCeiling(key)); }
		@Override
		public float higherKey(float key) { return highKeyOrNull(subHigher(key)); }
		@Override
		public Float2ObjectMap.Entry<V> lowerEntry(float key) { return subLower(key); }
		@Override
		public Float2ObjectMap.Entry<V> floorEntry(float key) { return subFloor(key); }
		@Override
		public Float2ObjectMap.Entry<V> ceilingEntry(float key) { return subCeiling(key); }
		@Override
		public Float2ObjectMap.Entry<V> higherEntry(float key) { return subHigher(key); }
		
		@Override
		public boolean isEmpty() {
			if(fromStart && toEnd) return map.isEmpty();
			Node<V> n = absLowest();
			return n == null || tooHigh(n.key);
		}
		
		@Override
		public int size() { return fromStart && toEnd ? map.size() : entrySet().size(); }
		
		@Override
		public Float2ObjectNavigableMap<V> copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public Float2ObjectMap.Entry<V> firstEntry() {
			Node<V> entry = subLowest();
			return entry == null ? null : entry.export();
		}

		@Override
		public Float2ObjectMap.Entry<V> lastEntry() {
			Node<V> entry = subHighest();
			return entry == null ? null : entry.export();
		}

		@Override
		public Float2ObjectMap.Entry<V> pollFirstEntry() {
			Node<V> entry = subLowest();
			if(entry != null) {
				Float2ObjectMap.Entry<V> result = entry.export();
				map.removeNode(entry);
				return result;
			}
			return null;
		}

		@Override
		public Float2ObjectMap.Entry<V> pollLastEntry() {
			Node<V> entry = subHighest();
			if(entry != null) {
				Float2ObjectMap.Entry<V> result = entry.export();
				map.removeNode(entry);
				return result;
			}
			return null;
		}
		
		abstract class SubEntrySet extends AbstractObjectSet<Float2ObjectMap.Entry<V>> {
			@Override
			public int size() {
				if (fromStart && toEnd) return map.size();
				int size = 0;
				for(ObjectIterator<Float2ObjectMap.Entry<V>> iter = iterator();iter.hasNext();iter.next(),size++);
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
				if(o instanceof Float2ObjectMap.Entry)
				{
					Float2ObjectMap.Entry<V> entry = (Float2ObjectMap.Entry<V>) o;
					float key = entry.getFloatKey();
					if (!inRange(key)) return false;
					Node<V> node = map.findNode(key);
					return node != null && Objects.equals(entry.getValue(), node.getValue());
				}
				Map.Entry<?,?> entry = (Map.Entry<?,?>) o;
				if(entry.getKey() == null && isNullComparator()) return false;
				Float key = (Float)entry.getKey();
				if (!inRange(key)) return false;
				Node<V> node = map.findNode(key);
				return node != null && Objects.equals(entry.getValue(), node.getValue());
			}
			
			@Override
			public boolean remove(Object o) {
				if (!(o instanceof Map.Entry)) return false;
				if(o instanceof Float2ObjectMap.Entry)
				{
					Float2ObjectMap.Entry<V> entry = (Float2ObjectMap.Entry<V>) o;
					float key = entry.getFloatKey();
					if (!inRange(key)) return false;
					Node<V> node = map.findNode(key);
					if (node != null && Objects.equals(node.getValue(), entry.getValue())) {
						map.removeNode(node);
						return true;
					}
					return false;
				}
				Map.Entry<?,?> entry = (Map.Entry<?,?>) o;
				Float key = (Float)entry.getKey();
				if (!inRange(key)) return false;
				Node<V> node = map.findNode(key);
				if (node != null && Objects.equals(node.getValue(), entry.getValue())) {
					map.removeNode(node);
					return true;
				}
				return false;
			}
			
			@Override
			public void forEach(Consumer<? super Float2ObjectMap.Entry<V>> action) {
				Objects.requireNonNull(action);
				for(Node<V> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					action.accept(new BasicEntry<>(entry.key, entry.value));
			}
			
			@Override
			public void forEachIndexed(IntObjectConsumer<Float2ObjectMap.Entry<V>> action) {
				Objects.requireNonNull(action);
				int index = 0;
				for(Node<V> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					action.accept(index++, new BasicEntry<>(entry.key, entry.value));
			}
			
			@Override
			public <E> void forEach(E input, ObjectObjectConsumer<E, Float2ObjectMap.Entry<V>> action) {
				Objects.requireNonNull(action);
				for(Node<V> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					action.accept(input, new BasicEntry<>(entry.key, entry.value));
			}
			
			@Override
			public boolean matchesAny(Predicate<Float2ObjectMap.Entry<V>> filter) {
				Objects.requireNonNull(filter);
				if(size() <= 0) return false;
				BasicEntry<V> subEntry = new BasicEntry<>();
				for(Node<V> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					subEntry.set(entry.key, entry.value);
					if(filter.test(subEntry)) return true;
				}
				return false;
			}
			
			@Override
			public boolean matchesNone(Predicate<Float2ObjectMap.Entry<V>> filter) {
				Objects.requireNonNull(filter);
				if(size() <= 0) return true;
				BasicEntry<V> subEntry = new BasicEntry<>();
				for(Node<V> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					subEntry.set(entry.key, entry.value);
					if(filter.test(subEntry)) return false;
				}
				return true;
			}
			
			@Override
			public boolean matchesAll(Predicate<Float2ObjectMap.Entry<V>> filter) {
				Objects.requireNonNull(filter);
				if(size() <= 0) return true;
				BasicEntry<V> subEntry = new BasicEntry<>();
				for(Node<V> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					subEntry.set(entry.key, entry.value);
					if(!filter.test(subEntry)) return false;
				}
				return true;
			}
			
			@Override
			public <E> E reduce(E identity, BiFunction<E, Float2ObjectMap.Entry<V>, E> operator) {
				Objects.requireNonNull(operator);
				E state = identity;
				for(Node<V> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					state = operator.apply(state, new BasicEntry<>(entry.key, entry.value));
				}
				return state;
			}
			
			@Override
			public Float2ObjectMap.Entry<V> reduce(ObjectObjectUnaryOperator<Float2ObjectMap.Entry<V>, Float2ObjectMap.Entry<V>> operator) {
				Objects.requireNonNull(operator);
				Float2ObjectMap.Entry<V> state = null;
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
			public Float2ObjectMap.Entry<V> findFirst(Predicate<Float2ObjectMap.Entry<V>> filter) {
				Objects.requireNonNull(filter);
				if(size() <= 0) return null;
				BasicEntry<V> subEntry = new BasicEntry<>();
				for(Node<V> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					subEntry.set(entry.key, entry.value);
					if(filter.test(subEntry)) return subEntry;
				}
				return null;
			}
			
			@Override
			public int count(Predicate<Float2ObjectMap.Entry<V>> filter) {
				Objects.requireNonNull(filter);
				if(size() <= 0) return 0;
				int result = 0;
				BasicEntry<V> subEntry = new BasicEntry<>();
				for(Node<V> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					subEntry.set(entry.key, entry.value);
					if(filter.test(subEntry)) result++;
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
			public void forEachIndexed(IntObjectConsumer<V> action) {
				Objects.requireNonNull(action);
				int index = 0;
				for(Node<V> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					action.accept(index++, entry.value);
			}
			
			@Override
			public <E> void forEach(E input, ObjectObjectConsumer<E, V> action) {
				Objects.requireNonNull(action);
				for(Node<V> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					action.accept(input, entry.value);
			}
			
			@Override
			public boolean matchesAny(Predicate<V> filter) {
				Objects.requireNonNull(filter);
				for(Node<V> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					if(filter.test(entry.value)) return true;
				return false;
			}
			
			@Override
			public boolean matchesNone(Predicate<V> filter) {
				Objects.requireNonNull(filter);
				for(Node<V> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					if(filter.test(entry.value)) return false;
				return true;
			}
			
			@Override
			public boolean matchesAll(Predicate<V> filter) {
				Objects.requireNonNull(filter);
				for(Node<V> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					if(!filter.test(entry.value)) return false;
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
			public V findFirst(Predicate<V> filter) {
				Objects.requireNonNull(filter);
				for(Node<V> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					if(filter.test(entry.value)) return entry.value;
				return null;
			}
			
			@Override
			public int count(Predicate<V> filter) {
				Objects.requireNonNull(filter);
				int result = 0;
				for(Node<V> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					if(filter.test(entry.value)) result++;
				return result;
			}
		}
		
		class DecsendingSubEntryIterator extends SubMapEntryIterator implements ObjectBidirectionalIterator<Float2ObjectMap.Entry<V>>
		{
			public DecsendingSubEntryIterator(Node<V> first, Node<V> forwardFence, Node<V> backwardFence) {
				super(first, forwardFence, backwardFence, false);
			}
			
			@Override
			protected Node<V> moveNext(Node<V> node) {
				return node.previous();
			}
			
			@Override
			protected Node<V> movePrevious(Node<V> node) {
				return node.next();
			}
			
			@Override
			public Float2ObjectMap.Entry<V> previous() {
				if(!hasPrevious()) throw new NoSuchElementException();
				return previousEntry();
			}

			@Override
			public Float2ObjectMap.Entry<V> next() {
				if(!hasNext()) throw new NoSuchElementException();
				return nextEntry();
			}
		}
		
		class AcsendingSubEntryIterator extends SubMapEntryIterator implements ObjectBidirectionalIterator<Float2ObjectMap.Entry<V>>
		{
			public AcsendingSubEntryIterator(Node<V> first, Node<V> forwardFence, Node<V> backwardFence) {
				super(first, forwardFence, backwardFence, true);
			}

			@Override
			public Float2ObjectMap.Entry<V> previous() {
				if(!hasPrevious()) throw new NoSuchElementException();
				return previousEntry();
			}

			@Override
			public Float2ObjectMap.Entry<V> next() {
				if(!hasNext()) throw new NoSuchElementException();
				return nextEntry();
			}
		}
		
		class DecsendingSubKeyIterator extends SubMapEntryIterator implements FloatBidirectionalIterator
		{
			public DecsendingSubKeyIterator(Node<V> first, Node<V> forwardFence, Node<V> backwardFence) {
				super(first, forwardFence, backwardFence, false);
			}
			
			@Override
			protected Node<V> moveNext(Node<V> node) {
				return node.previous();
			}
			
			@Override
			protected Node<V> movePrevious(Node<V> node) {
				return node.next();
			}
			
			@Override
			public float previousFloat() {
				if(!hasPrevious()) throw new NoSuchElementException();
				return previousEntry().key;
			}

			@Override
			public float nextFloat() {
				if(!hasNext()) throw new NoSuchElementException();
				return nextEntry().key;
			}
		}
		
		class AcsendingSubKeyIterator extends SubMapEntryIterator implements FloatBidirectionalIterator
		{
			public AcsendingSubKeyIterator(Node<V> first, Node<V> forwardFence, Node<V> backwardFence) {
				super(first, forwardFence, backwardFence, true);
			}

			@Override
			public float previousFloat() {
				if(!hasPrevious()) throw new NoSuchElementException();
				return previousEntry().key;
			}

			@Override
			public float nextFloat() {
				if(!hasNext()) throw new NoSuchElementException();
				return nextEntry().key;
			}
		}
		
		class AcsendingSubValueIterator extends SubMapEntryIterator implements ObjectBidirectionalIterator<V>
		{
			public AcsendingSubValueIterator(Node<V> first, Node<V> forwardFence, Node<V> backwardFence) {
				super(first, forwardFence, backwardFence, true);
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
				super(first, forwardFence, backwardFence, false);
			}
			
			@Override
			protected Node<V> moveNext(Node<V> node) {
				return node.previous();
			}
			
			@Override
			protected Node<V> movePrevious(Node<V> node) {
				return node.next();
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
		
		abstract class SubMapEntryIterator
		{
			final boolean isForward;
			boolean wasForward;
			Node<V> lastReturned;
			Node<V> next;
			Node<V> previous;
			boolean unboundForwardFence;
			boolean unboundBackwardFence;
			float forwardFence;
			float backwardFence;
			
			public SubMapEntryIterator(Node<V> first, Node<V> forwardFence, Node<V> backwardFence, boolean isForward) {
				next = first;
				previous = first == null ? null : movePrevious(first);
				this.forwardFence = forwardFence == null ? 0F : forwardFence.key;
				this.backwardFence = backwardFence == null ? 0F : backwardFence.key;
				unboundForwardFence = forwardFence == null;
				unboundBackwardFence = backwardFence == null;
				this.isForward = isForward;
			}
			
			protected Node<V> moveNext(Node<V> node) {
				return node.next();
			}
			
			protected Node<V> movePrevious(Node<V> node) {
				return node.previous();
			}
			
			public boolean hasNext() {
                return next != null && (unboundForwardFence || next.key != forwardFence);
			}
			
			protected Node<V> nextEntry() {
				lastReturned = next;
				previous = next;
				Node<V> result = next;
				next = moveNext(next);
				wasForward = isForward;
				return result;
			}
			
			public boolean hasPrevious() {
                return previous != null && (unboundBackwardFence || previous.key != backwardFence);
			}
			
			protected Node<V> previousEntry() {
				lastReturned = previous;
				next = previous;
				Node<V> result = previous;
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
			Float2ObjectRBTreeMap.this.clear();
		}
		
		@Override
		public int size() {
			return Float2ObjectRBTreeMap.this.size;
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
		public void forEachIndexed(IntObjectConsumer<V> action) {
			Objects.requireNonNull(action);
			int index = 0;
			for(Node<V> entry = first;entry != null;entry = entry.next())
				action.accept(index++, entry.value);
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, V> action) {
			Objects.requireNonNull(action);
			for(Node<V> entry = first;entry != null;entry = entry.next())
				action.accept(input, entry.value);
		}
		
		@Override
		public boolean matchesAny(Predicate<V> filter) {
			Objects.requireNonNull(filter);
			for(Node<V> entry = first;entry != null;entry = entry.next())
				if(filter.test(entry.value)) return true;
			return false;
		}
		
		@Override
		public boolean matchesNone(Predicate<V> filter) {
			Objects.requireNonNull(filter);
			for(Node<V> entry = first;entry != null;entry = entry.next())
				if(filter.test(entry.value)) return false;
			return true;
		}
		
		@Override
		public boolean matchesAll(Predicate<V> filter) {
			Objects.requireNonNull(filter);
			for(Node<V> entry = first;entry != null;entry = entry.next())
				if(!filter.test(entry.value)) return false;
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
		public V findFirst(Predicate<V> filter) {
			Objects.requireNonNull(filter);
			for(Node<V> entry = first;entry != null;entry = entry.next())
				if(filter.test(entry.value)) return entry.value;
			return null;
		}
		
		@Override
		public int count(Predicate<V> filter) {
			Objects.requireNonNull(filter);
			int result = 0;
			for(Node<V> entry = first;entry != null;entry = entry.next())
				if(filter.test(entry.value)) result++;
			return result;
		}
	}
	
	class EntrySet extends AbstractObjectSet<Float2ObjectMap.Entry<V>> {
		
		@Override
		public ObjectIterator<Float2ObjectMap.Entry<V>> iterator() {
			return new AscendingMapEntryIterator(first);
		}
		
		@Override
		public void clear() {
			Float2ObjectRBTreeMap.this.clear();
		}
		
		@Override
		public int size() {
			return Float2ObjectRBTreeMap.this.size;
		}
		
		@Override
		public boolean contains(Object o) {
			if (!(o instanceof Map.Entry)) return false;
			if(o instanceof Float2ObjectMap.Entry)
			{
				Float2ObjectMap.Entry<V> entry = (Float2ObjectMap.Entry<V>) o;
				float key = entry.getFloatKey();
				Node<V> node = findNode(key);
				return node != null && Objects.equals(entry.getValue(), node.getValue());
			}
			Map.Entry<?,?> entry = (Map.Entry<?,?>) o;
			if(entry.getKey() == null && comparator() == null) return false;
			if(!(entry.getKey() instanceof Float)) return false;
			Float key = (Float)entry.getKey();
			Node<V> node = findNode(key);
			return node != null && Objects.equals(entry.getValue(), node.getValue());
		}
		
		@Override
		public boolean remove(Object o) {
			if (!(o instanceof Map.Entry)) return false;
			if(o instanceof Float2ObjectMap.Entry)
			{
				Float2ObjectMap.Entry<V> entry = (Float2ObjectMap.Entry<V>) o;
				float key = entry.getFloatKey();
				Node<V> node = findNode(key);
				if (node != null && Objects.equals(entry.getValue(), node.getValue())) {
					removeNode(node);
					return true;
				}
				return false;
			}
			Map.Entry<?,?> entry = (Map.Entry<?,?>) o;
			Float key = (Float)entry.getKey();
			Node<V> node = findNode(key);
			if (node != null && Objects.equals(entry.getValue(), node.getValue())) {
				removeNode(node);
				return true;
			}
			return false;
		}
		
		@Override
		public void forEach(Consumer<? super Float2ObjectMap.Entry<V>> action) {
			Objects.requireNonNull(action);
			for(Node<V> entry = first;entry != null;entry = entry.next())
				action.accept(new BasicEntry<>(entry.key, entry.value));
		}
		
		@Override
		public void forEachIndexed(IntObjectConsumer<Float2ObjectMap.Entry<V>> action) {
			Objects.requireNonNull(action);
			int index = 0;
			for(Node<V> entry = first;entry != null;entry = entry.next())
				action.accept(index++, new BasicEntry<>(entry.key, entry.value));
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, Float2ObjectMap.Entry<V>> action) {
			Objects.requireNonNull(action);
			for(Node<V> entry = first;entry != null;entry = entry.next())
				action.accept(input, new BasicEntry<>(entry.key, entry.value));
		}
		
		@Override
		public boolean matchesAny(Predicate<Float2ObjectMap.Entry<V>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			BasicEntry<V> subEntry = new BasicEntry<>();
			for(Node<V> entry = first;entry != null;entry = entry.next()) {
				subEntry.set(entry.key, entry.value);
				if(filter.test(subEntry)) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Predicate<Float2ObjectMap.Entry<V>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			BasicEntry<V> subEntry = new BasicEntry<>();
			for(Node<V> entry = first;entry != null;entry = entry.next()) {
				subEntry.set(entry.key, entry.value);
				if(filter.test(subEntry)) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Predicate<Float2ObjectMap.Entry<V>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			BasicEntry<V> subEntry = new BasicEntry<>();
			for(Node<V> entry = first;entry != null;entry = entry.next()) {
				subEntry.set(entry.key, entry.value);
				if(!filter.test(subEntry)) return false;
			}
			return true;
		}
		
		@Override
		public <E> E reduce(E identity, BiFunction<E, Float2ObjectMap.Entry<V>, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			for(Node<V> entry = first;entry != null;entry = entry.next()) {
				state = operator.apply(state, new BasicEntry<>(entry.key, entry.value));
			}
			return state;
		}
		
		@Override
		public Float2ObjectMap.Entry<V> reduce(ObjectObjectUnaryOperator<Float2ObjectMap.Entry<V>, Float2ObjectMap.Entry<V>> operator) {
			Objects.requireNonNull(operator);
			Float2ObjectMap.Entry<V> state = null;
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
		public Float2ObjectMap.Entry<V> findFirst(Predicate<Float2ObjectMap.Entry<V>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return null;
			BasicEntry<V> subEntry = new BasicEntry<>();
			for(Node<V> entry = first;entry != null;entry = entry.next()) {
				subEntry.set(entry.key, entry.value);
				if(filter.test(subEntry)) return subEntry;
			}
			return null;
		}
		
		@Override
		public int count(Predicate<Float2ObjectMap.Entry<V>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			int result = 0;
			BasicEntry<V> subEntry = new BasicEntry<>();
			for(Node<V> entry = first;entry != null;entry = entry.next()) {
				subEntry.set(entry.key, entry.value);
				if(filter.test(subEntry)) result++;
			}
			return result;
		}
	}
	
	class DescendingKeyIterator extends MapEntryIterator implements FloatBidirectionalIterator
	{
		public DescendingKeyIterator(Node<V> first) {
			super(first, false);
		}
		
		@Override
		protected Node<V> moveNext(Node<V> node) {
			return node.previous();
		}
		
		@Override
		protected Node<V> movePrevious(Node<V> node) {
			return node.next();
		}
		
		@Override
		public float previousFloat() {
			if(!hasPrevious()) throw new NoSuchElementException();
			return previousEntry().key;
		}

		@Override
		public float nextFloat() {
			if(!hasNext()) throw new NoSuchElementException();
			return nextEntry().key;
		}
	}
	
	class AscendingMapEntryIterator extends MapEntryIterator implements ObjectBidirectionalIterator<Float2ObjectMap.Entry<V>>
	{
		public AscendingMapEntryIterator(Node<V> first)
		{
			super(first, true);
		}

		@Override
		public Float2ObjectMap.Entry<V> previous() {
			if(!hasPrevious()) throw new NoSuchElementException();
			return previousEntry();
		}

		@Override
		public Float2ObjectMap.Entry<V> next() {
			if(!hasNext()) throw new NoSuchElementException();
			return nextEntry();
		}
	}
	
	class AscendingValueIterator extends MapEntryIterator implements ObjectBidirectionalIterator<V>
	{
		public AscendingValueIterator(Node<V> first) {
			super(first, true);
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
	
	class AscendingKeyIterator extends MapEntryIterator implements FloatBidirectionalIterator
	{
		public AscendingKeyIterator(Node<V> first) {
			super(first, true);
		}

		@Override
		public float previousFloat() {
			if(!hasPrevious()) throw new NoSuchElementException();
			return previousEntry().key;
		}

		@Override
		public float nextFloat() {
			if(!hasNext()) throw new NoSuchElementException();
			return nextEntry().key;
		}
	}
	
	abstract class MapEntryIterator
	{
		final boolean isForward;
		boolean wasMoved = false;
		Node<V> lastReturned;
		Node<V> next;
		Node<V> previous;
		
		public MapEntryIterator(Node<V> first, boolean isForward) {
			next = first;
			previous = first == null ? null : movePrevious(first);
			this.isForward = isForward;
		}
		
		protected Node<V> moveNext(Node<V> node) {
			return node.next();
		}
		
		protected Node<V> movePrevious(Node<V> node) {
			return node.previous();
		}
		
		public boolean hasNext() {
            return next != null;
		}
		
		protected Node<V> nextEntry() {
			lastReturned = next;
			previous = next;
			Node<V> result = next;
			next = moveNext(next);
			wasMoved = isForward;
			return result;
		}
		
		public boolean hasPrevious() {
            return previous != null;
		}
		
		protected Node<V> previousEntry() {
			lastReturned = previous;
			next = previous;
			Node<V> result = previous;
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
	
	private static final class Node<V> implements Float2ObjectMap.Entry<V>
	{
		static final int BLACK = 1;
		
		float key;
		V value;
		int state;
		Node<V> parent;
		Node<V> left;
		Node<V> right;
		
		Node(float key, V value, Node<V> parent) {
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
		public float getFloatKey() {
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
				if(obj instanceof Float2ObjectMap.Entry) {
					Float2ObjectMap.Entry<V> entry = (Float2ObjectMap.Entry<V>)obj;
					return Float.floatToIntBits(key) == Float.floatToIntBits(entry.getFloatKey()) && Objects.equals(value, entry.getValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object otherKey = entry.getKey();
				if(otherKey == null) return false;
				Object otherValue = entry.getValue();
				return otherKey instanceof Float && Float.floatToIntBits(key) == Float.floatToIntBits(((Float)otherKey).floatValue()) && Objects.equals(value, otherValue);
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Float.hashCode(key) ^ Objects.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Float.toString(key) + "=" + Objects.toString(value);
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