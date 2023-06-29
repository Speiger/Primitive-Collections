package speiger.src.collections.chars.maps.impl.tree;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;


import speiger.src.collections.chars.collections.CharBidirectionalIterator;
import speiger.src.collections.chars.functions.CharComparator;
import speiger.src.collections.chars.functions.CharConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectCharConsumer;
import speiger.src.collections.ints.functions.consumer.IntCharConsumer;
import speiger.src.collections.ints.functions.consumer.IntObjectConsumer;
import speiger.src.collections.chars.functions.function.CharFunction;
import speiger.src.collections.chars.functions.consumer.CharObjectConsumer;
import speiger.src.collections.chars.functions.function.CharPredicate;
import speiger.src.collections.chars.functions.function.CharObjectUnaryOperator;
import speiger.src.collections.chars.functions.function.CharCharUnaryOperator;
import speiger.src.collections.chars.maps.abstracts.AbstractChar2ObjectMap;
import speiger.src.collections.chars.maps.interfaces.Char2ObjectMap;
import speiger.src.collections.chars.maps.interfaces.Char2ObjectNavigableMap;
import speiger.src.collections.chars.sets.AbstractCharSet;
import speiger.src.collections.chars.sets.CharNavigableSet;
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
 * A Simple Type Specific AVL TreeMap implementation that reduces boxing/unboxing.
 * It is using a bit more memory then <a href="https://github.com/vigna/fastutil">FastUtil</a>,
 * but it saves a lot of Performance on the Optimized removal and iteration logic.
 * Which makes the implementation actually useable and does not get outperformed by Javas default implementation.
 * @param <V> the keyType of elements maintained by this Collection
 */
public class Char2ObjectAVLTreeMap<V> extends AbstractChar2ObjectMap<V> implements Char2ObjectNavigableMap<V>
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
	protected transient CharComparator comparator;
	
	/** the default return value for max searches */
	protected char defaultMaxNotFound = Character.MIN_VALUE;
	/** the default return value for min searches */
	protected char defaultMinNotFound = Character.MAX_VALUE;
	
	/** KeySet Cache */
	protected CharNavigableSet keySet;
	/** Values Cache */
	protected ObjectCollection<V> values;
	/** EntrySet Cache */
	protected ObjectSet<Char2ObjectMap.Entry<V>> entrySet;
	
	/**
	 * Default Constructor
	 */
	public Char2ObjectAVLTreeMap() {
	}
	
	/**
	 * Constructor that allows to define the sorter
	 * @param comp the function that decides how the tree is sorted, can be null
	 */
	public Char2ObjectAVLTreeMap(CharComparator comp) {
		comparator = comp;
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Char2ObjectAVLTreeMap(Character[] keys, V[] values) {
		this(keys, values, null);
	}
	
	/**
	 * Helper constructor that has a custom sorter and allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param comp the function that decides how the tree is sorted, can be null
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Char2ObjectAVLTreeMap(Character[] keys, V[] values, CharComparator comp) {
		comparator = comp;
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i].charValue(), values[i]);
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Char2ObjectAVLTreeMap(char[] keys, V[] values) {
		this(keys, values, null);
	}
	
	/**
	 * Helper constructor that has a custom sorter and allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param comp the function that decides how the tree is sorted, can be null
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Char2ObjectAVLTreeMap(char[] keys, V[] values, CharComparator comp) {
		comparator = comp;
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i], values[i]);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 */
	public Char2ObjectAVLTreeMap(Map<? extends Character, ? extends V> map) {
		this(map, null);
	}
	
	/**
	 * A Helper constructor that has a custom sorter and allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param comp the function that decides how the tree is sorted, can be null
	 */
	public Char2ObjectAVLTreeMap(Map<? extends Character, ? extends V> map, CharComparator comp) {
		comparator = comp;
		putAll(map);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 */
	public Char2ObjectAVLTreeMap(Char2ObjectMap<V> map) {
		this(map, null);
	}
	
	/**
	 * A Type Specific Helper function that has a custom sorter and allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param comp the function that decides how the tree is sorted, can be null
 	 */
	public Char2ObjectAVLTreeMap(Char2ObjectMap<V> map, CharComparator comp) {
		comparator = comp;
		putAll(map);
	}

	@Override
	public void setDefaultMaxValue(char value) { defaultMaxNotFound = value; }
	@Override
	public char getDefaultMaxValue() { return defaultMaxNotFound; }
	@Override
	public void setDefaultMinValue(char value) { defaultMinNotFound = value; }
	@Override
	public char getDefaultMinValue() { return defaultMinNotFound; }
	
	
	@Override
	public V put(char key, V value) {
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
	public V putIfAbsent(char key, V value) {
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
	public CharComparator comparator() { return comparator; }

	@Override
	public boolean containsKey(char key) {
		return findNode(key) != null;
	}
	
	@Override
	public V get(char key) {
		Node<V> node = findNode(key);
		return node == null ? getDefaultReturnValue() : node.value;
	}
	
	@Override
	public V getOrDefault(char key, V defaultValue) {
		Node<V> node = findNode(key);
		return node == null ? defaultValue : node.value;
	}
	
	@Override
	public char firstCharKey() {
		if(tree == null) throw new NoSuchElementException();
		return first.key;
	}
	
	@Override
	public char pollFirstCharKey() {
		if(tree == null) return getDefaultMinValue();
		char result = first.key;
		removeNode(first);
		return result;
	}
	
	@Override
	public char lastCharKey() {
		if(tree == null) throw new NoSuchElementException();
		return last.key;
	}
	
	@Override
	public char pollLastCharKey() {
		if(tree == null) return getDefaultMaxValue();
		char result = last.key;
		removeNode(last);
		return result;
	}
	
	@Override
	public Char2ObjectMap.Entry<V> firstEntry() {
		if(tree == null) return null;
		return first.export();
	}
	
	@Override
	public Char2ObjectMap.Entry<V> lastEntry() {
		if(tree == null) return null;
		return last.export();
	}
	
	@Override
	public Char2ObjectMap.Entry<V> pollFirstEntry() {
		if(tree == null) return null;
		BasicEntry<V> entry = first.export();
		removeNode(first);
		return entry;
	}
	
	@Override
	public Char2ObjectMap.Entry<V> pollLastEntry() {
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
	public V remove(char key) {
		Node<V> entry = findNode(key);
		if(entry == null) return getDefaultReturnValue();
		V value = entry.value;
		removeNode(entry);
		return value;
	}
	
	@Override
	public V removeOrDefault(char key, V defaultValue) {
		Node<V> entry = findNode(key);
		if(entry == null) return defaultValue;
		V value = entry.value;
		removeNode(entry);
		return value;
	}
	
	@Override
	public boolean remove(char key, V value) {
		Node<V> entry = findNode(key);
		if(entry == null || entry.value != value) return false;
		removeNode(entry);
		return true;
	}
	
	@Override
	public boolean replace(char key, V oldValue, V newValue) {
		Node<V> entry = findNode(key);
		if(entry == null || entry.value != oldValue) return false;
		entry.value = newValue;
		return true;
	}
	
	@Override
	public V replace(char key, V value) {
		Node<V> entry = findNode(key);
		if(entry == null) return getDefaultReturnValue();
		V oldValue = entry.value;
		entry.value = value;
		return oldValue;
	}
	
	@Override
	public V compute(char key, CharObjectUnaryOperator<V> mappingFunction) {
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
	public V computeIfAbsent(char key, CharFunction<V> mappingFunction) {
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
	public V supplyIfAbsent(char key, ObjectSupplier<V> valueProvider) {
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
	public V computeIfPresent(char key, CharObjectUnaryOperator<V> mappingFunction) {
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
	public V merge(char key, V value, ObjectObjectUnaryOperator<V, V> mappingFunction) {
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
	public void mergeAll(Char2ObjectMap<V> m, ObjectObjectUnaryOperator<V, V> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Char2ObjectMap.Entry<V> entry : getFastIterable(m)) {
			char key = entry.getCharKey();
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
	public void forEach(CharObjectConsumer<V> action) {
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
	
	protected CharBidirectionalIterator keyIterator() {
		return new AscendingKeyIterator(first);
	}
	
	protected CharBidirectionalIterator keyIterator(char element) {
		return new AscendingKeyIterator(findNode(element));
	}
	
	protected CharBidirectionalIterator descendingKeyIterator() {
		return new DescendingKeyIterator(last);
	}
		
	@Override
	public Char2ObjectAVLTreeMap<V> copy() {
		Char2ObjectAVLTreeMap<V> set = new Char2ObjectAVLTreeMap<>();
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
	public CharNavigableSet keySet() {
		return navigableKeySet();
	}
	
	@Override
	public ObjectSet<Char2ObjectMap.Entry<V>> char2ObjectEntrySet() {
		if(entrySet == null) entrySet = new EntrySet();
		return entrySet;
	}
	
	@Override
	public ObjectCollection<V> values() {
		if(values == null) values = new Values();
		return values;
	}
	
	@Override
	public CharNavigableSet navigableKeySet() {
		if(keySet == null) keySet = new KeySet<>(this);
		return keySet;
	}
	
	@Override
	public Char2ObjectNavigableMap<V> descendingMap() {
		return new DescendingNaivgableSubMap<>(this, true, (char)0, true, true, (char)0, true);
	}
	
	@Override
	public CharNavigableSet descendingKeySet() {
		return descendingMap().navigableKeySet();
	}
	
	@Override
	public Char2ObjectNavigableMap<V> subMap(char fromKey, boolean fromInclusive, char toKey, boolean toInclusive) {
		return new AscendingNaivgableSubMap<>(this, false, fromKey, fromInclusive, false, toKey, toInclusive);
	}
	
	@Override
	public Char2ObjectNavigableMap<V> headMap(char toKey, boolean inclusive) {
		return new AscendingNaivgableSubMap<>(this, true, (char)0, true, false, toKey, inclusive);
	}
	
	@Override
	public Char2ObjectNavigableMap<V> tailMap(char fromKey, boolean inclusive) {
		return new AscendingNaivgableSubMap<>(this, false, fromKey, inclusive, true, (char)0, true);
	}
	
	@Override
	public char lowerKey(char e) {
		Node<V> node = findLowerNode(e);
		return node != null ? node.key : getDefaultMinValue();
	}

	@Override
	public char floorKey(char e) {
		Node<V> node = findFloorNode(e);
		return node != null ? node.key : getDefaultMinValue();
	}
	
	@Override
	public char higherKey(char e) {
		Node<V> node = findHigherNode(e);
		return node != null ? node.key : getDefaultMaxValue();
	}

	@Override
	public char ceilingKey(char e) {
		Node<V> node = findCeilingNode(e);
		return node != null ? node.key : getDefaultMaxValue();
	}
	
	@Override
	public Char2ObjectMap.Entry<V> lowerEntry(char key) {
		Node<V> node = findLowerNode(key);
		return node != null ? node.export() : null;
	}
	
	@Override
	public Char2ObjectMap.Entry<V> higherEntry(char key) {
		Node<V> node = findHigherNode(key);
		return node != null ? node.export() : null;
	}
	
	@Override
	public Char2ObjectMap.Entry<V> floorEntry(char key) {
		Node<V> node = findFloorNode(key);
		return node != null ? node.export() : null;
	}
	
	@Override
	public Char2ObjectMap.Entry<V> ceilingEntry(char key) {
		Node<V> node = findCeilingNode(key);
		return node != null ? node.export() : null;
	}
	
	protected Node<V> findLowerNode(char key) {
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
	
	protected Node<V> findFloorNode(char key) {
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
	
	protected Node<V> findCeilingNode(char key) {
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
	
	protected Node<V> findHigherNode(char key) {
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
	
	protected Node<V> findNode(char key) {
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
	
	protected void validate(char k) { compare(k, k); }
	protected int compare(char k, char v) { return comparator != null ? comparator.compare(k, v) : Character.compare(k, v);}
	
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
	
	static class KeySet<V> extends AbstractCharSet implements CharNavigableSet
	{
		Char2ObjectNavigableMap<V> map;

		public KeySet(Char2ObjectNavigableMap<V> map) {
			this.map = map;
		}
		
		@Override
		public void setDefaultMaxValue(char e) { map.setDefaultMaxValue(e); }
		@Override
		public char getDefaultMaxValue() { return map.getDefaultMaxValue(); }
		@Override
		public void setDefaultMinValue(char e) { map.setDefaultMinValue(e); }
		@Override
		public char getDefaultMinValue() { return map.getDefaultMinValue(); }
		@Override
		public char lower(char e) { return map.lowerKey(e); }
		@Override
		public char floor(char e) { return map.floorKey(e); }
		@Override
		public char ceiling(char e) { return map.ceilingKey(e); }
		@Override
		public char higher(char e) { return map.higherKey(e); }
		
		@Override
		public Character lower(Character e) {
			Char2ObjectMap.Entry<V> node = map.lowerEntry(e.charValue());
			return node != null ? node.getKey() : null;
		}
		
		@Override
		public Character floor(Character e) {
			Char2ObjectMap.Entry<V> node = map.floorEntry(e.charValue());
			return node != null ? node.getKey() : null;
		}
		
		@Override
		public Character higher(Character e) {
			Char2ObjectMap.Entry<V> node = map.higherEntry(e.charValue());
			return node != null ? node.getKey() : null;
		}
		
		@Override
		public Character ceiling(Character e) {
			Char2ObjectMap.Entry<V> node = map.ceilingEntry(e.charValue());
			return node != null ? node.getKey() : null;
		}
		
		@Override
		public char pollFirstChar() { return map.pollFirstCharKey(); }
		@Override
		public char pollLastChar() { return map.pollLastCharKey(); }
		@Override
		public CharComparator comparator() { return map.comparator(); }
		@Override
		public char firstChar() { return map.firstCharKey(); } 
		@Override
		public char lastChar() { return map.lastCharKey(); }
		@Override
		public void clear() { map.clear(); }
		
		@Override
		public boolean remove(char o) { 
			int oldSize = map.size();
			map.remove(o); 
			return oldSize != map.size();
		}
		
		@Override
		public boolean add(char e) { throw new UnsupportedOperationException(); }
		
		@Override
		public CharBidirectionalIterator iterator(char fromElement) {
			if(map instanceof Char2ObjectAVLTreeMap) return ((Char2ObjectAVLTreeMap<V>)map).keyIterator(fromElement);
			return ((NavigableSubMap<V>)map).keyIterator(fromElement);
		}
		
		@Override
		public CharNavigableSet subSet(char fromElement, boolean fromInclusive, char toElement, boolean toInclusive) { return new KeySet<>(map.subMap(fromElement, fromInclusive, toElement, toInclusive)); }
		@Override
		public CharNavigableSet headSet(char toElement, boolean inclusive) { return new KeySet<>(map.headMap(toElement, inclusive)); }
		@Override
		public CharNavigableSet tailSet(char fromElement, boolean inclusive) { return new KeySet<>(map.tailMap(fromElement, inclusive)); }
		
		@Override
		public CharBidirectionalIterator iterator() {
			if(map instanceof Char2ObjectAVLTreeMap) return ((Char2ObjectAVLTreeMap<V>)map).keyIterator();
			return ((NavigableSubMap<V>)map).keyIterator();
		}
		
		@Override
		public CharBidirectionalIterator descendingIterator() {
			if(map instanceof Char2ObjectAVLTreeMap) return ((Char2ObjectAVLTreeMap<V>)map).descendingKeyIterator();
			return ((NavigableSubMap<V>)map).descendingKeyIterator();
		}
		
		protected Node<V> start() {
			if(map instanceof Char2ObjectAVLTreeMap) return ((Char2ObjectAVLTreeMap<V>)map).first;
			return ((NavigableSubMap<V>)map).subLowest();
		}
		
		protected Node<V> end() {
			if(map instanceof Char2ObjectAVLTreeMap) return null;
			return ((NavigableSubMap<V>)map).subHighest();
		}
		
		protected Node<V> next(Node<V> entry) {
			if(map instanceof Char2ObjectAVLTreeMap) return entry.next();
			return ((NavigableSubMap<V>)map).next(entry);
		}
		
		protected Node<V> previous(Node<V> entry) {
			if(map instanceof Char2ObjectAVLTreeMap) return entry.previous();
			return ((NavigableSubMap<V>)map).previous(entry);
		}
		
		@Override
		public CharNavigableSet descendingSet() { return new KeySet<>(map.descendingMap()); }
		@Override
		public KeySet<V> copy() { throw new UnsupportedOperationException(); }
		@Override
		public boolean isEmpty() { return map.isEmpty(); }
		@Override
		public int size() { return map.size(); }
		
		@Override
		public void forEach(CharConsumer action) {
			Objects.requireNonNull(action);
			for(Node<V> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				action.accept(entry.key);
		}
		
		@Override
		public void forEachIndexed(IntCharConsumer action) {
			Objects.requireNonNull(action);
			int index = 0;
			for(Node<V> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				action.accept(index++, entry.key);
		}
		
		@Override
		public <E> void forEach(E input, ObjectCharConsumer<E> action) {
			Objects.requireNonNull(action);
			for(Node<V> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				action.accept(input, entry.key);
		}
		
		@Override
		public boolean matchesAny(CharPredicate filter) {
			Objects.requireNonNull(filter);
			for(Node<V> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				if(filter.test(entry.key)) return true;
			return false;
		}
		
		@Override
		public boolean matchesNone(CharPredicate filter) {
			Objects.requireNonNull(filter);
			for(Node<V> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				if(filter.test(entry.key)) return false;
			return true;
		}
		
		@Override
		public boolean matchesAll(CharPredicate filter) {
			Objects.requireNonNull(filter);
			for(Node<V> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				if(!filter.test(entry.key)) return false;
			return true;
		}
		
		@Override
		public char reduce(char identity, CharCharUnaryOperator operator) {
			Objects.requireNonNull(operator);
			char state = identity;
			for(Node<V> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				state = operator.applyAsChar(state, entry.key);
			return state;
		}
		
		@Override
		public char reduce(CharCharUnaryOperator operator) {
			Objects.requireNonNull(operator);
			char state = (char)0;
			boolean empty = true;
			for(Node<V> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry)) {
				if(empty) {
					empty = false;
					state = entry.key;
					continue;
				}
				state = operator.applyAsChar(state, entry.key);
			}
			return state;
		}
		
		@Override
		public char findFirst(CharPredicate filter) {
			Objects.requireNonNull(filter);
			for(Node<V> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				if(filter.test(entry.key)) return entry.key;
			return (char)0;
		}
		
		@Override
		public int count(CharPredicate filter) {
			Objects.requireNonNull(filter);
			int result = 0;
			for(Node<V> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				if(filter.test(entry.key)) result++;
			return result;
		}
	}
	
	static class AscendingNaivgableSubMap<V> extends NavigableSubMap<V>
	{
		AscendingNaivgableSubMap(Char2ObjectAVLTreeMap<V> map, boolean fromStart, char lo, boolean loInclusive, boolean toEnd, char hi, boolean hiInclusive) {
			super(map, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
		}
		
		@Override
		public Char2ObjectNavigableMap<V> descendingMap() {
			if(inverse == null) inverse = new DescendingNaivgableSubMap<>(map, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
			return inverse;
		}
		
		@Override
		public ObjectSet<Char2ObjectMap.Entry<V>> char2ObjectEntrySet() {
			if(entrySet == null) entrySet = new AscendingSubEntrySet();
			return entrySet;
		}
		
		@Override
		public CharNavigableSet navigableKeySet() {
			if(keySet == null) keySet = new KeySet<>(this);
			return keySet;
		}
		
		@Override
		public Char2ObjectNavigableMap<V> subMap(char fromKey, boolean fromInclusive, char toKey, boolean toInclusive) {
			if (!inRange(fromKey, fromInclusive)) throw new IllegalArgumentException("fromKey out of range");
			if (!inRange(toKey, toInclusive)) throw new IllegalArgumentException("toKey out of range");
			return new AscendingNaivgableSubMap<>(map, false, fromKey, fromInclusive, false, toKey, toInclusive);
		}
		
		@Override
		public Char2ObjectNavigableMap<V> headMap(char toKey, boolean inclusive) {
			if (!inRange(toKey, inclusive)) throw new IllegalArgumentException("toKey out of range");
			return new AscendingNaivgableSubMap<>(map, fromStart, lo, loInclusive, false, toKey, inclusive);
		}
		
		@Override
		public Char2ObjectNavigableMap<V> tailMap(char fromKey, boolean inclusive) {
			if (!inRange(fromKey, inclusive)) throw new IllegalArgumentException("fromKey out of range");
			return new AscendingNaivgableSubMap<>(map, false, fromKey, inclusive, toEnd, hi, hiInclusive);
		}
		
		@Override
		protected Node<V> subLowest() { return absLowest(); }
		@Override
		protected Node<V> subHighest() { return absHighest(); }
		@Override
		protected Node<V> subCeiling(char key) { return absCeiling(key); }
		@Override
		protected Node<V> subHigher(char key) { return absHigher(key); }
		@Override
		protected Node<V> subFloor(char key) { return absFloor(key); }
		@Override
		protected Node<V> subLower(char key) { return absLower(key); }
		
		@Override
		protected CharBidirectionalIterator keyIterator() {
			return new AcsendingSubKeyIterator(absLowest(), absHighFence(), absLowFence()); 
		}
		@Override
		protected CharBidirectionalIterator keyIterator(char element) {
			return new AcsendingSubKeyIterator(absLower(element), absHighFence(), absLowFence()); 
		}
		
		@Override
		protected ObjectBidirectionalIterator<V> valueIterator() {
			return new AcsendingSubValueIterator(absLowest(), absHighFence(), absLowFence()); 
		}
		
		@Override
		protected CharBidirectionalIterator descendingKeyIterator() {
			return new DecsendingSubKeyIterator(absHighest(), absLowFence(), absHighFence()); 
		}
		
		class AscendingSubEntrySet extends SubEntrySet {
			@Override
			public ObjectIterator<Char2ObjectMap.Entry<V>> iterator() {
				return new AcsendingSubEntryIterator(absLowest(), absHighFence(), absLowFence());
			}
		}
	}
	
	static class DescendingNaivgableSubMap<V> extends NavigableSubMap<V>
	{
		CharComparator comparator;
		DescendingNaivgableSubMap(Char2ObjectAVLTreeMap<V> map, boolean fromStart, char lo, boolean loInclusive, boolean toEnd, char hi, boolean hiInclusive) {
			super(map, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
			comparator = map.comparator() == null ? CharComparator.of(Collections.reverseOrder()) : map.comparator().reversed();
		}
		
		@Override
		public CharComparator comparator() { return comparator; }
		
		@Override
		public Char2ObjectNavigableMap<V> descendingMap() {
			if(inverse == null) inverse = new AscendingNaivgableSubMap<>(map, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
			return inverse;
		}

		@Override
		public CharNavigableSet navigableKeySet() {
			if(keySet == null) keySet = new KeySet<>(this);
			return keySet;
		}
		
		@Override
		public Char2ObjectNavigableMap<V> subMap(char fromKey, boolean fromInclusive, char toKey, boolean toInclusive) {
			if (!inRange(fromKey, fromInclusive)) throw new IllegalArgumentException("fromKey out of range");
			if (!inRange(toKey, toInclusive)) throw new IllegalArgumentException("toKey out of range");
			return new DescendingNaivgableSubMap<>(map, false, toKey, toInclusive, false, fromKey, fromInclusive);
		}
		
		@Override
		public Char2ObjectNavigableMap<V> headMap(char toKey, boolean inclusive) {
			if (!inRange(toKey, inclusive)) throw new IllegalArgumentException("toKey out of range");
			return new DescendingNaivgableSubMap<>(map, false, toKey, inclusive, toEnd, hi, hiInclusive);
		}
		
		@Override
		public Char2ObjectNavigableMap<V> tailMap(char fromKey, boolean inclusive) {
			if (!inRange(fromKey, inclusive)) throw new IllegalArgumentException("fromKey out of range");
			return new DescendingNaivgableSubMap<>(map, fromStart, lo, loInclusive, false, fromKey, inclusive);
		}
		
		@Override
		public ObjectSet<Char2ObjectMap.Entry<V>> char2ObjectEntrySet() {
			if(entrySet == null) entrySet = new DescendingSubEntrySet();
			return entrySet;
		}
		
		@Override
		protected Node<V> subLowest() { return absHighest(); }
		@Override
		protected Node<V> subHighest() { return absLowest(); }
		@Override
		protected Node<V> subCeiling(char key) { return absFloor(key); }
		@Override
		protected Node<V> subHigher(char key) { return absLower(key); }
		@Override
		protected Node<V> subFloor(char key) { return absCeiling(key); }
		@Override
		protected Node<V> subLower(char key) { return absHigher(key); }
		@Override
		protected Node<V> next(Node<V> entry) { return entry.previous(); }
		@Override
		protected Node<V> previous(Node<V> entry) { return entry.next(); }
		
		@Override
		protected CharBidirectionalIterator keyIterator() {
			return new DecsendingSubKeyIterator(absHighest(), absLowFence(), absHighFence()); 
		}
		
		@Override
		protected CharBidirectionalIterator keyIterator(char element) {
			return new DecsendingSubKeyIterator(absHigher(element), absLowFence(), absHighFence()); 
		}
		
		@Override
		protected ObjectBidirectionalIterator<V> valueIterator() {
			return new DecsendingSubValueIterator(absHighest(), absLowFence(), absHighFence()); 
		}
		
		@Override
		protected CharBidirectionalIterator descendingKeyIterator() {
			return new AcsendingSubKeyIterator(absLowest(), absHighFence(), absLowFence()); 
		}
		
		class DescendingSubEntrySet extends SubEntrySet {
			@Override
			public ObjectIterator<Char2ObjectMap.Entry<V>> iterator() {
				return new DecsendingSubEntryIterator(absHighest(), absLowFence(), absHighFence());
			}
		}
	}
	
	static abstract class NavigableSubMap<V> extends AbstractChar2ObjectMap<V> implements Char2ObjectNavigableMap<V>
	{
		final Char2ObjectAVLTreeMap<V> map;
		final char lo, hi;
		final boolean fromStart, toEnd;
		final boolean loInclusive, hiInclusive;
		
		Char2ObjectNavigableMap<V> inverse;
		CharNavigableSet keySet;
		ObjectSet<Char2ObjectMap.Entry<V>> entrySet;
		ObjectCollection<V> values;
		
		NavigableSubMap(Char2ObjectAVLTreeMap<V> map, boolean fromStart, char lo, boolean loInclusive, boolean toEnd, char hi, boolean hiInclusive) {
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
		public void setDefaultMaxValue(char e) { map.setDefaultMaxValue(e); }
		@Override
		public char getDefaultMaxValue() { return map.getDefaultMaxValue(); }
		@Override
		public void setDefaultMinValue(char e) { map.setDefaultMinValue(e); }
		@Override
		public char getDefaultMinValue() { return map.getDefaultMinValue(); }
		protected boolean isNullComparator() { return map.comparator() == null; }
		
		@Override
		public AbstractChar2ObjectMap<V> setDefaultReturnValue(V v) { 
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
		public CharNavigableSet descendingKeySet() {
			return descendingMap().navigableKeySet();
		}
		
		@Override
		public CharNavigableSet keySet() {
			return navigableKeySet();
		}
		
		protected abstract Node<V> subLowest();
		protected abstract Node<V> subHighest();
		protected abstract Node<V> subCeiling(char key);
		protected abstract Node<V> subHigher(char key);
		protected abstract Node<V> subFloor(char key);
		protected abstract Node<V> subLower(char key);
		protected abstract CharBidirectionalIterator keyIterator();
		protected abstract CharBidirectionalIterator keyIterator(char element);
		protected abstract ObjectBidirectionalIterator<V> valueIterator();
		protected abstract CharBidirectionalIterator descendingKeyIterator();
		protected char lowKeyOrNull(Node<V> entry) { return entry == null ? getDefaultMinValue() : entry.key; }
		protected char highKeyOrNull(Node<V> entry) { return entry == null ? getDefaultMaxValue() : entry.key; }
		protected Node<V> next(Node<V> entry) { return entry.next(); }
		protected Node<V> previous(Node<V> entry) { return entry.previous(); }
		
		protected boolean tooLow(char key) {
			if (!fromStart) {
				int c = map.compare(key, lo);
				if (c < 0 || (c == 0 && !loInclusive)) return true;
			}
			return false;
		}
		
		protected boolean tooHigh(char key) {
			if (!toEnd) {
				int c = map.compare(key, hi);
				if (c > 0 || (c == 0 && !hiInclusive)) return true;
			}
			return false;
		}
		protected boolean inRange(char key) { return !tooLow(key) && !tooHigh(key); }
		protected boolean inClosedRange(char key) { return (fromStart || map.compare(key, lo) >= 0) && (toEnd || map.compare(hi, key) >= 0); }
		protected boolean inRange(char key, boolean inclusive) { return inclusive ? inRange(key) : inClosedRange(key); }
		
		protected Node<V> absLowest() {
			Node<V> e = (fromStart ?  map.first : (loInclusive ? map.findCeilingNode(lo) : map.findHigherNode(lo)));
			return (e == null || tooHigh(e.key)) ? null : e;
		}
		
		protected Node<V> absHighest() {
			Node<V> e = (toEnd ?  map.last : (hiInclusive ?  map.findFloorNode(hi) : map.findLowerNode(hi)));
			return (e == null || tooLow(e.key)) ? null : e;
		}
		
		protected Node<V> absCeiling(char key) {
			if (tooLow(key)) return absLowest();
			Node<V> e = map.findCeilingNode(key);
			return (e == null || tooHigh(e.key)) ? null : e;
		}
		
		protected Node<V> absHigher(char key) {
			if (tooLow(key)) return absLowest();
			Node<V> e = map.findHigherNode(key);
			return (e == null || tooHigh(e.key)) ? null : e;
		}
		
		protected Node<V> absFloor(char key) {
			if (tooHigh(key)) return absHighest();
			Node<V> e = map.findFloorNode(key);
			return (e == null || tooLow(e.key)) ? null : e;
		}
		
		protected Node<V> absLower(char key) {
			if (tooHigh(key)) return absHighest();
			Node<V> e = map.findLowerNode(key);
			return (e == null || tooLow(e.key)) ? null : e;
		}
		
		protected Node<V> absHighFence() { return (toEnd ? null : (hiInclusive ? map.findHigherNode(hi) : map.findCeilingNode(hi))); }
		protected Node<V> absLowFence() { return (fromStart ? null : (loInclusive ?  map.findLowerNode(lo) : map.findFloorNode(lo))); }
		
		@Override
		public CharComparator comparator() { return map.comparator(); }
		
		@Override
		public char pollFirstCharKey() {
			Node<V> entry = subLowest();
			if(entry != null) {
				char result = entry.key;
				map.removeNode(entry);
				return result;
			}
			return getDefaultMinValue();
		}
		
		@Override
		public char pollLastCharKey() {
			Node<V> entry = subHighest();
			if(entry != null) {
				char result = entry.key;
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
		public char firstCharKey() {
			Node<V> entry = subLowest();
			if(entry == null) throw new NoSuchElementException();
			return entry.key;
		}
		
		@Override
		public char lastCharKey() {
			Node<V> entry = subHighest();
			if(entry == null) throw new NoSuchElementException();
			return entry.key;
		}
		
		@Override
		public V put(char key, V value) {
			if (!inRange(key)) throw new IllegalArgumentException("key out of range");
			return map.put(key, value);
		}
		
		@Override
		public V putIfAbsent(char key, V value) {
			if (!inRange(key)) throw new IllegalArgumentException("key out of range");
			return map.putIfAbsent(key, value);
		}
		
		@Override
		public boolean containsKey(char key) { return inRange(key) && map.containsKey(key); }
		
		@Override
		public V computeIfPresent(char key, CharObjectUnaryOperator<V> mappingFunction) {
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
		public V remove(char key) {
			return inRange(key) ? map.remove(key) : getDefaultReturnValue();
		}
		
		@Override
		public V removeOrDefault(char key, V defaultValue) {
			return inRange(key) ? map.removeOrDefault(key, defaultValue) : defaultValue;
		}
		
		@Override
		public boolean remove(char key, V value) {
			return inRange(key) && map.remove(key, value);
		}
		
		
		@Override
		public V get(char key) {
			return inRange(key) ? map.get(key) : getDefaultReturnValue();
		}
		
		@Override
		public V getOrDefault(char key, V defaultValue) {
			return inRange(key) ? map.getOrDefault(key, defaultValue) : getDefaultReturnValue();
		}
		
		
		@Override
		public char lowerKey(char key) { return lowKeyOrNull(subLower(key)); }
		@Override
		public char floorKey(char key) { return lowKeyOrNull(subFloor(key)); }
		@Override
		public char ceilingKey(char key) { return highKeyOrNull(subCeiling(key)); }
		@Override
		public char higherKey(char key) { return highKeyOrNull(subHigher(key)); }
		@Override
		public Char2ObjectMap.Entry<V> lowerEntry(char key) { return subLower(key); }
		@Override
		public Char2ObjectMap.Entry<V> floorEntry(char key) { return subFloor(key); }
		@Override
		public Char2ObjectMap.Entry<V> ceilingEntry(char key) { return subCeiling(key); }
		@Override
		public Char2ObjectMap.Entry<V> higherEntry(char key) { return subHigher(key); }
		
		@Override
		public boolean isEmpty() {
			if(fromStart && toEnd) return map.isEmpty();
			Node<V> n = absLowest();
			return n == null || tooHigh(n.key);
		}
		
		@Override
		public int size() { return fromStart && toEnd ? map.size() : entrySet().size(); }
		
		@Override
		public Char2ObjectNavigableMap<V> copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public Char2ObjectMap.Entry<V> firstEntry() {
			Node<V> entry = subLowest();
			return entry == null ? null : entry.export();
		}

		@Override
		public Char2ObjectMap.Entry<V> lastEntry() {
			Node<V> entry = subHighest();
			return entry == null ? null : entry.export();
		}

		@Override
		public Char2ObjectMap.Entry<V> pollFirstEntry() {
			Node<V> entry = subLowest();
			if(entry != null) {
				Char2ObjectMap.Entry<V> result = entry.export();
				map.removeNode(entry);
				return result;
			}
			return null;
		}

		@Override
		public Char2ObjectMap.Entry<V> pollLastEntry() {
			Node<V> entry = subHighest();
			if(entry != null) {
				Char2ObjectMap.Entry<V> result = entry.export();
				map.removeNode(entry);
				return result;
			}
			return null;
		}
		
		abstract class SubEntrySet extends AbstractObjectSet<Char2ObjectMap.Entry<V>> {
			@Override
			public int size() {
				if (fromStart && toEnd) return map.size();
				int size = 0;
				for(ObjectIterator<Char2ObjectMap.Entry<V>> iter = iterator();iter.hasNext();iter.next(),size++);
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
				if(o instanceof Char2ObjectMap.Entry)
				{
					Char2ObjectMap.Entry<V> entry = (Char2ObjectMap.Entry<V>) o;
					char key = entry.getCharKey();
					if (!inRange(key)) return false;
					Node<V> node = map.findNode(key);
					return node != null && Objects.equals(entry.getValue(), node.getValue());
				}
				Map.Entry<?,?> entry = (Map.Entry<?,?>) o;
				if(entry.getKey() == null && isNullComparator()) return false;
				Character key = (Character)entry.getKey();
				if (!inRange(key)) return false;
				Node<V> node = map.findNode(key);
				return node != null && Objects.equals(entry.getValue(), node.getValue());
			}
			
			@Override
			public boolean remove(Object o) {
				if (!(o instanceof Map.Entry)) return false;
				if(o instanceof Char2ObjectMap.Entry)
				{
					Char2ObjectMap.Entry<V> entry = (Char2ObjectMap.Entry<V>) o;
					char key = entry.getCharKey();
					if (!inRange(key)) return false;
					Node<V> node = map.findNode(key);
					if (node != null && Objects.equals(node.getValue(), entry.getValue())) {
						map.removeNode(node);
						return true;
					}
					return false;
				}
				Map.Entry<?,?> entry = (Map.Entry<?,?>) o;
				Character key = (Character)entry.getKey();
				if (!inRange(key)) return false;
				Node<V> node = map.findNode(key);
				if (node != null && Objects.equals(node.getValue(), entry.getValue())) {
					map.removeNode(node);
					return true;
				}
				return false;
			}
			
			@Override
			public void forEach(Consumer<? super Char2ObjectMap.Entry<V>> action) {
				Objects.requireNonNull(action);
				for(Node<V> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					action.accept(new BasicEntry<>(entry.key, entry.value));
			}
			
			@Override
			public void forEachIndexed(IntObjectConsumer<Char2ObjectMap.Entry<V>> action) {
				Objects.requireNonNull(action);
				int index = 0;
				for(Node<V> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					action.accept(index++, new BasicEntry<>(entry.key, entry.value));
			}
			
			@Override
			public <E> void forEach(E input, ObjectObjectConsumer<E, Char2ObjectMap.Entry<V>> action) {
				Objects.requireNonNull(action);
				for(Node<V> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					action.accept(input, new BasicEntry<>(entry.key, entry.value));
			}
			
			@Override
			public boolean matchesAny(Predicate<Char2ObjectMap.Entry<V>> filter) {
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
			public boolean matchesNone(Predicate<Char2ObjectMap.Entry<V>> filter) {
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
			public boolean matchesAll(Predicate<Char2ObjectMap.Entry<V>> filter) {
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
			public <E> E reduce(E identity, BiFunction<E, Char2ObjectMap.Entry<V>, E> operator) {
				Objects.requireNonNull(operator);
				E state = identity;
				for(Node<V> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					state = operator.apply(state, new BasicEntry<>(entry.key, entry.value));
				}
				return state;
			}
			
			@Override
			public Char2ObjectMap.Entry<V> reduce(ObjectObjectUnaryOperator<Char2ObjectMap.Entry<V>, Char2ObjectMap.Entry<V>> operator) {
				Objects.requireNonNull(operator);
				Char2ObjectMap.Entry<V> state = null;
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
			public Char2ObjectMap.Entry<V> findFirst(Predicate<Char2ObjectMap.Entry<V>> filter) {
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
			public int count(Predicate<Char2ObjectMap.Entry<V>> filter) {
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
		
		class DecsendingSubEntryIterator extends SubMapEntryIterator implements ObjectBidirectionalIterator<Char2ObjectMap.Entry<V>>
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
			public Char2ObjectMap.Entry<V> previous() {
				if(!hasPrevious()) throw new NoSuchElementException();
				return previousEntry();
			}

			@Override
			public Char2ObjectMap.Entry<V> next() {
				if(!hasNext()) throw new NoSuchElementException();
				return nextEntry();
			}
		}
		
		class AcsendingSubEntryIterator extends SubMapEntryIterator implements ObjectBidirectionalIterator<Char2ObjectMap.Entry<V>>
		{
			public AcsendingSubEntryIterator(Node<V> first, Node<V> forwardFence, Node<V> backwardFence) {
				super(first, forwardFence, backwardFence, true);
			}

			@Override
			public Char2ObjectMap.Entry<V> previous() {
				if(!hasPrevious()) throw new NoSuchElementException();
				return previousEntry();
			}

			@Override
			public Char2ObjectMap.Entry<V> next() {
				if(!hasNext()) throw new NoSuchElementException();
				return nextEntry();
			}
		}
		
		class DecsendingSubKeyIterator extends SubMapEntryIterator implements CharBidirectionalIterator
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
			public char previousChar() {
				if(!hasPrevious()) throw new NoSuchElementException();
				return previousEntry().key;
			}

			@Override
			public char nextChar() {
				if(!hasNext()) throw new NoSuchElementException();
				return nextEntry().key;
			}
		}
		
		class AcsendingSubKeyIterator extends SubMapEntryIterator implements CharBidirectionalIterator
		{
			public AcsendingSubKeyIterator(Node<V> first, Node<V> forwardFence, Node<V> backwardFence) {
				super(first, forwardFence, backwardFence, true);
			}

			@Override
			public char previousChar() {
				if(!hasPrevious()) throw new NoSuchElementException();
				return previousEntry().key;
			}

			@Override
			public char nextChar() {
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
			char forwardFence;
			char backwardFence;
			
			public SubMapEntryIterator(Node<V> first, Node<V> forwardFence, Node<V> backwardFence, boolean isForward) {
				next = first;
				previous = first == null ? null : movePrevious(first);
				this.forwardFence = forwardFence == null ? (char)0 : forwardFence.key;
				this.backwardFence = backwardFence == null ? (char)0 : backwardFence.key;
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
			Char2ObjectAVLTreeMap.this.clear();
		}
		
		@Override
		public int size() {
			return Char2ObjectAVLTreeMap.this.size;
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
	
	class EntrySet extends AbstractObjectSet<Char2ObjectMap.Entry<V>> {
		
		@Override
		public ObjectIterator<Char2ObjectMap.Entry<V>> iterator() {
			return new AscendingMapEntryIterator(first);
		}
		
		@Override
		public void clear() {
			Char2ObjectAVLTreeMap.this.clear();
		}
		
		@Override
		public int size() {
			return Char2ObjectAVLTreeMap.this.size;
		}
		
		@Override
		public boolean contains(Object o) {
			if (!(o instanceof Map.Entry)) return false;
			if(o instanceof Char2ObjectMap.Entry)
			{
				Char2ObjectMap.Entry<V> entry = (Char2ObjectMap.Entry<V>) o;
				char key = entry.getCharKey();
				Node<V> node = findNode(key);
				return node != null && Objects.equals(entry.getValue(), node.getValue());
			}
			Map.Entry<?,?> entry = (Map.Entry<?,?>) o;
			if(entry.getKey() == null && comparator() == null) return false;
			if(!(entry.getKey() instanceof Character)) return false;
			Character key = (Character)entry.getKey();
			Node<V> node = findNode(key);
			return node != null && Objects.equals(entry.getValue(), node.getValue());
		}
		
		@Override
		public boolean remove(Object o) {
			if (!(o instanceof Map.Entry)) return false;
			if(o instanceof Char2ObjectMap.Entry)
			{
				Char2ObjectMap.Entry<V> entry = (Char2ObjectMap.Entry<V>) o;
				char key = entry.getCharKey();
				Node<V> node = findNode(key);
				if (node != null && Objects.equals(entry.getValue(), node.getValue())) {
					removeNode(node);
					return true;
				}
				return false;
			}
			Map.Entry<?,?> entry = (Map.Entry<?,?>) o;
			Character key = (Character)entry.getKey();
			Node<V> node = findNode(key);
			if (node != null && Objects.equals(entry.getValue(), node.getValue())) {
				removeNode(node);
				return true;
			}
			return false;
		}
		
		@Override
		public void forEach(Consumer<? super Char2ObjectMap.Entry<V>> action) {
			Objects.requireNonNull(action);
			for(Node<V> entry = first;entry != null;entry = entry.next())
				action.accept(new BasicEntry<>(entry.key, entry.value));
		}
		
		@Override
		public void forEachIndexed(IntObjectConsumer<Char2ObjectMap.Entry<V>> action) {
			Objects.requireNonNull(action);
			int index = 0;
			for(Node<V> entry = first;entry != null;entry = entry.next())
				action.accept(index++, new BasicEntry<>(entry.key, entry.value));
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, Char2ObjectMap.Entry<V>> action) {
			Objects.requireNonNull(action);
			for(Node<V> entry = first;entry != null;entry = entry.next())
				action.accept(input, new BasicEntry<>(entry.key, entry.value));
		}
		
		@Override
		public boolean matchesAny(Predicate<Char2ObjectMap.Entry<V>> filter) {
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
		public boolean matchesNone(Predicate<Char2ObjectMap.Entry<V>> filter) {
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
		public boolean matchesAll(Predicate<Char2ObjectMap.Entry<V>> filter) {
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
		public <E> E reduce(E identity, BiFunction<E, Char2ObjectMap.Entry<V>, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			for(Node<V> entry = first;entry != null;entry = entry.next()) {
				state = operator.apply(state, new BasicEntry<>(entry.key, entry.value));
			}
			return state;
		}
		
		@Override
		public Char2ObjectMap.Entry<V> reduce(ObjectObjectUnaryOperator<Char2ObjectMap.Entry<V>, Char2ObjectMap.Entry<V>> operator) {
			Objects.requireNonNull(operator);
			Char2ObjectMap.Entry<V> state = null;
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
		public Char2ObjectMap.Entry<V> findFirst(Predicate<Char2ObjectMap.Entry<V>> filter) {
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
		public int count(Predicate<Char2ObjectMap.Entry<V>> filter) {
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
	
	class DescendingKeyIterator extends MapEntryIterator implements CharBidirectionalIterator
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
		public char previousChar() {
			if(!hasPrevious()) throw new NoSuchElementException();
			return previousEntry().key;
		}

		@Override
		public char nextChar() {
			if(!hasNext()) throw new NoSuchElementException();
			return nextEntry().key;
		}
	}
	
	class AscendingMapEntryIterator extends MapEntryIterator implements ObjectBidirectionalIterator<Char2ObjectMap.Entry<V>>
	{
		public AscendingMapEntryIterator(Node<V> first)
		{
			super(first, true);
		}

		@Override
		public Char2ObjectMap.Entry<V> previous() {
			if(!hasPrevious()) throw new NoSuchElementException();
			return previousEntry();
		}

		@Override
		public Char2ObjectMap.Entry<V> next() {
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
	
	class AscendingKeyIterator extends MapEntryIterator implements CharBidirectionalIterator
	{
		public AscendingKeyIterator(Node<V> first) {
			super(first, true);
		}

		@Override
		public char previousChar() {
			if(!hasPrevious()) throw new NoSuchElementException();
			return previousEntry().key;
		}

		@Override
		public char nextChar() {
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
	
	private static final class Node<V> implements Char2ObjectMap.Entry<V>
	{
		char key;
		V value;
		int state;
		Node<V> parent;
		Node<V> left;
		Node<V> right;
		
		Node(char key, V value, Node<V> parent) {
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
		public char getCharKey() {
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
				if(obj instanceof Char2ObjectMap.Entry) {
					Char2ObjectMap.Entry<V> entry = (Char2ObjectMap.Entry<V>)obj;
					return key == entry.getCharKey() && Objects.equals(value, entry.getValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object otherKey = entry.getKey();
				if(otherKey == null) return false;
				Object otherValue = entry.getValue();
				return otherKey instanceof Character && key == ((Character)otherKey).charValue() && Objects.equals(value, otherValue);
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Character.hashCode(key) ^ Objects.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Character.toString(key) + "=" + Objects.toString(value);
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