package speiger.src.collections.bytes.maps.impl.tree;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.BiFunction;

import speiger.src.collections.bytes.collections.ByteBidirectionalIterator;
import speiger.src.collections.bytes.functions.ByteComparator;
import speiger.src.collections.bytes.functions.ByteConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectByteConsumer;
import speiger.src.collections.bytes.functions.function.Byte2BooleanFunction;
import speiger.src.collections.bytes.functions.consumer.ByteIntConsumer;
import speiger.src.collections.bytes.functions.function.Byte2IntFunction;
import speiger.src.collections.bytes.functions.function.ByteIntUnaryOperator;
import speiger.src.collections.bytes.functions.function.ByteByteUnaryOperator;
import speiger.src.collections.bytes.maps.abstracts.AbstractByte2IntMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2IntMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2IntNavigableMap;
import speiger.src.collections.bytes.sets.AbstractByteSet;
import speiger.src.collections.bytes.sets.ByteNavigableSet;
import speiger.src.collections.bytes.sets.ByteSet;
import speiger.src.collections.bytes.sets.ByteSortedSet;
import speiger.src.collections.bytes.utils.maps.Byte2IntMaps;
import speiger.src.collections.ints.collections.AbstractIntCollection;
import speiger.src.collections.ints.collections.IntCollection;
import speiger.src.collections.ints.collections.IntIterator;
import speiger.src.collections.ints.functions.IntSupplier;
import speiger.src.collections.ints.collections.IntBidirectionalIterator;
import speiger.src.collections.ints.functions.function.IntIntUnaryOperator;
import speiger.src.collections.ints.functions.IntConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;
import speiger.src.collections.objects.functions.function.Object2BooleanFunction;
import speiger.src.collections.objects.functions.consumer.ObjectIntConsumer;
import speiger.src.collections.ints.functions.function.Int2BooleanFunction;
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
public class Byte2IntAVLTreeMap extends AbstractByte2IntMap implements Byte2IntNavigableMap
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
	protected transient ByteComparator comparator;
	
	/** the default return value for max searches */
	protected byte defaultMaxNotFound = Byte.MIN_VALUE;
	/** the default return value for min searches */
	protected byte defaultMinNotFound = Byte.MAX_VALUE;
	
	/** KeySet Cache */
	protected ByteNavigableSet keySet;
	/** Values Cache */
	protected IntCollection values;
	/** EntrySet Cache */
	protected ObjectSet<Byte2IntMap.Entry> entrySet;
	
	/**
	 * Default Constructor
	 */
	public Byte2IntAVLTreeMap() {
	}
	
	/**
	 * Constructor that allows to define the sorter
	 * @param comp the function that decides how the tree is sorted, can be null
	 */
	public Byte2IntAVLTreeMap(ByteComparator comp) {
		comparator = comp;
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Byte2IntAVLTreeMap(Byte[] keys, Integer[] values) {
		this(keys, values, null);
	}
	
	/**
	 * Helper constructor that has a custom sorter and allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param comp the function that decides how the tree is sorted, can be null
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Byte2IntAVLTreeMap(Byte[] keys, Integer[] values, ByteComparator comp) {
		comparator = comp;
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i].byteValue(), values[i].intValue());
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Byte2IntAVLTreeMap(byte[] keys, int[] values) {
		this(keys, values, null);
	}
	
	/**
	 * Helper constructor that has a custom sorter and allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param comp the function that decides how the tree is sorted, can be null
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Byte2IntAVLTreeMap(byte[] keys, int[] values, ByteComparator comp) {
		comparator = comp;
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i], values[i]);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 */
	public Byte2IntAVLTreeMap(Map<? extends Byte, ? extends Integer> map) {
		this(map, null);
	}
	
	/**
	 * A Helper constructor that has a custom sorter and allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param comp the function that decides how the tree is sorted, can be null
	 */
	public Byte2IntAVLTreeMap(Map<? extends Byte, ? extends Integer> map, ByteComparator comp) {
		comparator = comp;
		putAll(map);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 */
	public Byte2IntAVLTreeMap(Byte2IntMap map) {
		this(map, null);
	}
	
	/**
	 * A Type Specific Helper function that has a custom sorter and allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param comp the function that decides how the tree is sorted, can be null
 	 */
	public Byte2IntAVLTreeMap(Byte2IntMap map, ByteComparator comp) {
		comparator = comp;
		putAll(map);
	}

	@Override
	public void setDefaultMaxValue(byte value) { defaultMaxNotFound = value; }
	@Override
	public byte getDefaultMaxValue() { return defaultMaxNotFound; }
	@Override
	public void setDefaultMinValue(byte value) { defaultMinNotFound = value; }
	@Override
	public byte getDefaultMinValue() { return defaultMinNotFound; }
	
	
	@Override
	public int put(byte key, int value) {
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
	public int putIfAbsent(byte key, int value) {
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
	public int addTo(byte key, int value) {
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
	public int subFrom(byte key, int value) {
		if(tree == null) return getDefaultReturnValue();
		int compare = 0;
		Node parent = tree;
		while(true) {
			if((compare = compare(key, parent.key)) == 0)
			{
				int oldValue = parent.subFrom(value);
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
	public ByteComparator comparator() { return comparator; }

	@Override
	public boolean containsKey(byte key) {
		return findNode(key) != null;
	}
	
	@Override
	public int get(byte key) {
		Node node = findNode(key);
		return node == null ? getDefaultReturnValue() : node.value;
	}
	
	@Override
	public int getOrDefault(byte key, int defaultValue) {
		Node node = findNode(key);
		return node == null ? defaultValue : node.value;
	}
	
	@Override
	public byte firstByteKey() {
		if(tree == null) throw new NoSuchElementException();
		return first.key;
	}
	
	@Override
	public byte pollFirstByteKey() {
		if(tree == null) return (byte)0;
		byte result = first.key;
		removeNode(first);
		return result;
	}
	
	@Override
	public byte lastByteKey() {
		if(tree == null) throw new NoSuchElementException();
		return last.key;
	}
	
	@Override
	public byte pollLastByteKey() {
		if(tree == null) return (byte)0;
		byte result = last.key;
		removeNode(last);
		return result;
	}
	
	@Override
	public Byte2IntMap.Entry firstEntry() {
		if(tree == null) return null;
		return first.export();
	}
	
	@Override
	public Byte2IntMap.Entry lastEntry() {
		if(tree == null) return null;
		return last.export();
	}
	
	@Override
	public Byte2IntMap.Entry pollFirstEntry() {
		if(tree == null) return null;
		BasicEntry entry = first.export();
		removeNode(first);
		return entry;
	}
	
	@Override
	public Byte2IntMap.Entry pollLastEntry() {
		if(tree == null) return null;
		BasicEntry entry = last.export();
		removeNode(last);
		return entry;
	}
	
	@Override
	public int firstIntValue() {
		if(tree == null) throw new NoSuchElementException();
		return first.value;
	}
	
	@Override
	public int lastIntValue() {
		if(tree == null) throw new NoSuchElementException();
		return last.value;
	}
	
	@Override
	public int remove(byte key) {
		Node entry = findNode(key);
		if(entry == null) return getDefaultReturnValue();
		int value = entry.value;
		removeNode(entry);
		return value;
	}
	
	@Override
	public int removeOrDefault(byte key, int defaultValue) {
		Node entry = findNode(key);
		if(entry == null) return defaultValue;
		int value = entry.value;
		removeNode(entry);
		return value;
	}
	
	@Override
	public boolean remove(byte key, int value) {
		Node entry = findNode(key);
		if(entry == null || entry.value != value) return false;
		removeNode(entry);
		return true;
	}
	
	@Override
	public boolean replace(byte key, int oldValue, int newValue) {
		Node entry = findNode(key);
		if(entry == null || entry.value != oldValue) return false;
		entry.value = newValue;
		return true;
	}
	
	@Override
	public int replace(byte key, int value) {
		Node entry = findNode(key);
		if(entry == null) return getDefaultReturnValue();
		int oldValue = entry.value;
		entry.value = value;
		return oldValue;
	}
	
	@Override
	public int computeInt(byte key, ByteIntUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		Node entry = findNode(key);
		if(entry == null) {
			int newValue = mappingFunction.applyAsInt(key, getDefaultReturnValue());
			if(newValue == getDefaultReturnValue()) return newValue;
			put(key, newValue);
			return newValue;
		}
		int newValue = mappingFunction.applyAsInt(key, entry.value);
		if(newValue == getDefaultReturnValue()) {
			removeNode(entry);
			return newValue;
		}
		entry.value = newValue;
		return newValue;
	}
	
	@Override
	public int computeIntIfAbsent(byte key, Byte2IntFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		Node entry = findNode(key);
		if(entry == null) {
			int newValue = mappingFunction.get(key);
			if(newValue == getDefaultReturnValue()) return newValue;
			put(key, newValue);
			return newValue;
		}
		if(Objects.equals(entry.value, getDefaultReturnValue())) {
			int newValue = mappingFunction.get(key);
			if(newValue == getDefaultReturnValue()) return newValue;
			entry.value = newValue;
		}
		return entry.value;
	}
	
	@Override
	public int supplyIntIfAbsent(byte key, IntSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		Node entry = findNode(key);
		if(entry == null) {
			int newValue = valueProvider.getInt();
			if(newValue == getDefaultReturnValue()) return newValue;
			put(key, newValue);
			return newValue;
		}
		if(entry.value == getDefaultReturnValue()) {
			int newValue = valueProvider.getInt();
			if(newValue == getDefaultReturnValue()) return newValue;
			entry.value = newValue;
		}
		return entry.value;
	}
	
	@Override
	public int computeIntIfPresent(byte key, ByteIntUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		Node entry = findNode(key);
		if(entry == null || entry.value == getDefaultReturnValue()) return getDefaultReturnValue();
		int newValue = mappingFunction.applyAsInt(key, entry.value);
		if(newValue == getDefaultReturnValue()) {
			removeNode(entry);
			return newValue;
		}
		entry.value = newValue;
		return newValue;
	}
	
	@Override
	public int mergeInt(byte key, int value, IntIntUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		Node entry = findNode(key);
		int newValue = entry == null || entry.value == getDefaultReturnValue() ? value : mappingFunction.applyAsInt(entry.value, value);
		if(newValue == getDefaultReturnValue()) {
			if(entry != null)
				removeNode(entry);
		}
		else if(entry == null) put(key, newValue);
		else entry.value = newValue;
		return newValue;
	}
	
	@Override
	public void mergeAllInt(Byte2IntMap m, IntIntUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Byte2IntMap.Entry entry : Byte2IntMaps.fastIterable(m)) {
			byte key = entry.getByteKey();
			Node subEntry = findNode(key);
			int newValue = subEntry == null || subEntry.value == getDefaultReturnValue() ? entry.getIntValue() : mappingFunction.applyAsInt(subEntry.value, entry.getIntValue());
			if(newValue == getDefaultReturnValue()) {
				if(subEntry != null)
					removeNode(subEntry);
			}
			else if(subEntry == null) put(key, newValue);
			else subEntry.value = newValue;
		}
	}
	
	@Override
	public void forEach(ByteIntConsumer action) {
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
	
	protected ByteBidirectionalIterator keyIterator() {
		return new AscendingKeyIterator(first);
	}
	
	protected ByteBidirectionalIterator keyIterator(byte element) {
		return new AscendingKeyIterator(findNode(element));
	}
	
	protected ByteBidirectionalIterator descendingKeyIterator() {
		return new DescendingKeyIterator(last);
	}
		
	@Override
	public Byte2IntAVLTreeMap copy() {
		Byte2IntAVLTreeMap set = new Byte2IntAVLTreeMap();
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
	public ByteSortedSet keySet() {
		return navigableKeySet();
	}
	
	@Override
	public ObjectSet<Byte2IntMap.Entry> byte2IntEntrySet() {
		if(entrySet == null) entrySet = new EntrySet();
		return entrySet;
	}
	
	@Override
	public IntCollection values() {
		if(values == null) values = new Values();
		return values;
	}
	
	@Override
	public ByteNavigableSet navigableKeySet() {
		if(keySet == null) keySet = new KeySet(this);
		return keySet;
	}
	
	@Override
	public Byte2IntNavigableMap descendingMap() {
		return new DescendingNaivgableSubMap(this, true, (byte)0, true, true, (byte)0, true);
	}
	
	@Override
	public ByteNavigableSet descendingKeySet() {
		return descendingMap().navigableKeySet();
	}
	
	@Override
	public Byte2IntNavigableMap subMap(byte fromKey, boolean fromInclusive, byte toKey, boolean toInclusive) {
		return new AscendingNaivgableSubMap(this, false, fromKey, fromInclusive, false, toKey, toInclusive);
	}
	
	@Override
	public Byte2IntNavigableMap headMap(byte toKey, boolean inclusive) {
		return new AscendingNaivgableSubMap(this, true, (byte)0, true, false, toKey, inclusive);
	}
	
	@Override
	public Byte2IntNavigableMap tailMap(byte fromKey, boolean inclusive) {
		return new AscendingNaivgableSubMap(this, false, fromKey, inclusive, true, (byte)0, true);
	}
	
	@Override
	public byte lowerKey(byte e) {
		Node node = findLowerNode(e);
		return node != null ? node.key : getDefaultMinValue();
	}

	@Override
	public byte floorKey(byte e) {
		Node node = findFloorNode(e);
		return node != null ? node.key : getDefaultMinValue();
	}
	
	@Override
	public byte higherKey(byte e) {
		Node node = findHigherNode(e);
		return node != null ? node.key : getDefaultMaxValue();
	}

	@Override
	public byte ceilingKey(byte e) {
		Node node = findCeilingNode(e);
		return node != null ? node.key : getDefaultMaxValue();
	}
	
	@Override
	public Byte2IntMap.Entry lowerEntry(byte key) {
		Node node = findLowerNode(key);
		return node != null ? node.export() : null;
	}
	
	@Override
	public Byte2IntMap.Entry higherEntry(byte key) {
		Node node = findHigherNode(key);
		return node != null ? node.export() : null;
	}
	
	@Override
	public Byte2IntMap.Entry floorEntry(byte key) {
		Node node = findFloorNode(key);
		return node != null ? node.export() : null;
	}
	
	@Override
	public Byte2IntMap.Entry ceilingEntry(byte key) {
		Node node = findCeilingNode(key);
		return node != null ? node.export() : null;
	}
	
	protected Node findLowerNode(byte key) {
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
	
	protected Node findFloorNode(byte key) {
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
	
	protected Node findCeilingNode(byte key) {
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
	
	protected Node findHigherNode(byte key) {
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
	
	protected Node findNode(byte key) {
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
	
	protected void validate(byte k) { compare(k, k); }
	protected int compare(byte k, byte v) { return comparator != null ? comparator.compare(k, v) : Byte.compare(k, v);}
	
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
	
	static class KeySet extends AbstractByteSet implements ByteNavigableSet
	{
		Byte2IntNavigableMap map;

		public KeySet(Byte2IntNavigableMap map) {
			this.map = map;
		}
		
		@Override
		public void setDefaultMaxValue(byte e) { map.setDefaultMaxValue(e); }
		@Override
		public byte getDefaultMaxValue() { return map.getDefaultMaxValue(); }
		@Override
		public void setDefaultMinValue(byte e) { map.setDefaultMinValue(e); }
		@Override
		public byte getDefaultMinValue() { return map.getDefaultMinValue(); }
		@Override
		public byte lower(byte e) { return map.lowerKey(e); }
		@Override
		public byte floor(byte e) { return map.floorKey(e); }
		@Override
		public byte ceiling(byte e) { return map.ceilingKey(e); }
		@Override
		public byte higher(byte e) { return map.higherKey(e); }
		@Override
		public byte pollFirstByte() { return map.pollFirstByteKey(); }
		@Override
		public byte pollLastByte() { return map.pollLastByteKey(); }
		@Override
		public ByteComparator comparator() { return map.comparator(); }
		@Override
		public byte firstByte() { return map.firstByteKey(); } 
		@Override
		public byte lastByte() { return map.lastByteKey(); }
		@Override
		public void clear() { map.clear(); }
		
		@Override
		public boolean remove(byte o) { 
			int oldSize = map.size();
			map.remove(o); 
			return oldSize != map.size();
		}
		
		@Override
		public boolean add(byte e) { throw new UnsupportedOperationException(); }
		
		@Override
		public ByteBidirectionalIterator iterator(byte fromElement) {
			if(map instanceof Byte2IntAVLTreeMap) return ((Byte2IntAVLTreeMap)map).keyIterator(fromElement);
			return ((NavigableSubMap)map).keyIterator(fromElement);
		}
		
		@Override
		public ByteNavigableSet subSet(byte fromElement, boolean fromInclusive, byte toElement, boolean toInclusive) { return new KeySet(map.subMap(fromElement, fromInclusive, toElement, toInclusive)); }
		@Override
		public ByteNavigableSet headSet(byte toElement, boolean inclusive) { return new KeySet(map.headMap(toElement, inclusive)); }
		@Override
		public ByteNavigableSet tailSet(byte fromElement, boolean inclusive) { return new KeySet(map.tailMap(fromElement, inclusive)); }
		
		@Override
		public ByteBidirectionalIterator iterator() {
			if(map instanceof Byte2IntAVLTreeMap) return ((Byte2IntAVLTreeMap)map).keyIterator();
			return ((NavigableSubMap)map).keyIterator();
		}
		
		@Override
		public ByteBidirectionalIterator descendingIterator() {
			if(map instanceof Byte2IntAVLTreeMap) return ((Byte2IntAVLTreeMap)map).descendingKeyIterator();
			return ((NavigableSubMap)map).descendingKeyIterator();
		}
		
		protected Node start() {
			if(map instanceof Byte2IntAVLTreeMap) return ((Byte2IntAVLTreeMap)map).first;
			return ((NavigableSubMap)map).subLowest();
		}
		
		protected Node end() {
			if(map instanceof Byte2IntAVLTreeMap) return null;
			return ((NavigableSubMap)map).subHighest();
		}
		
		protected Node next(Node entry) {
			if(map instanceof Byte2IntAVLTreeMap) return entry.next();
			return ((NavigableSubMap)map).next(entry);
		}
		
		protected Node previous(Node entry) {
			if(map instanceof Byte2IntAVLTreeMap) return entry.previous();
			return ((NavigableSubMap)map).previous(entry);
		}
		
		@Override
		public ByteNavigableSet descendingSet() { return new KeySet(map.descendingMap()); }
		@Override
		public KeySet copy() { throw new UnsupportedOperationException(); }
		@Override
		public boolean isEmpty() { return map.isEmpty(); }
		@Override
		public int size() { return map.size(); }
		
		@Override
		public void forEach(ByteConsumer action) {
			Objects.requireNonNull(action);
			for(Node entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				action.accept(entry.key);
		}
		
		@Override
		public <E> void forEach(E input, ObjectByteConsumer<E> action) {
			Objects.requireNonNull(action);
			for(Node entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				action.accept(input, entry.key);
		}
		
		@Override
		public boolean matchesAny(Byte2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(Node entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				if(filter.get(entry.key)) return true;
			return false;
		}
		
		@Override
		public boolean matchesNone(Byte2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(Node entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				if(filter.get(entry.key)) return false;
			return true;
		}
		
		@Override
		public boolean matchesAll(Byte2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(Node entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				if(!filter.get(entry.key)) return false;
			return true;
		}
		
		@Override
		public byte reduce(byte identity, ByteByteUnaryOperator operator) {
			Objects.requireNonNull(operator);
			byte state = identity;
			for(Node entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				state = operator.applyAsByte(state, entry.key);
			return state;
		}
		
		@Override
		public byte reduce(ByteByteUnaryOperator operator) {
			Objects.requireNonNull(operator);
			byte state = (byte)0;
			boolean empty = true;
			for(Node entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry)) {
				if(empty) {
					empty = false;
					state = entry.key;
					continue;
				}
				state = operator.applyAsByte(state, entry.key);
			}
			return state;
		}
		
		@Override
		public byte findFirst(Byte2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(Node entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				if(filter.get(entry.key)) return entry.key;
			return (byte)0;
		}
		
		@Override
		public int count(Byte2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			int result = 0;
			for(Node entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				if(filter.get(entry.key)) result++;
			return result;
		}
	}
	
	static class AscendingNaivgableSubMap extends NavigableSubMap
	{
		AscendingNaivgableSubMap(Byte2IntAVLTreeMap map, boolean fromStart, byte lo, boolean loInclusive, boolean toEnd, byte hi, boolean hiInclusive) {
			super(map, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
		}
		
		@Override
		public Byte2IntNavigableMap descendingMap() {
			if(inverse == null) inverse = new DescendingNaivgableSubMap(map, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
			return inverse;
		}
		
		@Override
		public ObjectSet<Byte2IntMap.Entry> byte2IntEntrySet() {
			if(entrySet == null) entrySet = new AscendingSubEntrySet();
			return entrySet;
		}
		
		@Override
		public ByteNavigableSet navigableKeySet() {
			if(keySet == null) keySet = new KeySet(this);
			return keySet;
		}
		
		@Override
		public Byte2IntNavigableMap subMap(byte fromKey, boolean fromInclusive, byte toKey, boolean toInclusive) {
			if (!inRange(fromKey, fromInclusive)) throw new IllegalArgumentException("fromKey out of range");
			if (!inRange(toKey, toInclusive)) throw new IllegalArgumentException("toKey out of range");
			return new AscendingNaivgableSubMap(map, false, fromKey, fromInclusive, false, toKey, toInclusive);
		}
		
		@Override
		public Byte2IntNavigableMap headMap(byte toKey, boolean inclusive) {
			if (!inRange(toKey, inclusive)) throw new IllegalArgumentException("toKey out of range");
			return new AscendingNaivgableSubMap(map, fromStart, lo, loInclusive, false, toKey, inclusive);
		}
		
		@Override
		public Byte2IntNavigableMap tailMap(byte fromKey, boolean inclusive) {
			if (!inRange(fromKey, inclusive)) throw new IllegalArgumentException("fromKey out of range");
			return new AscendingNaivgableSubMap(map, false, fromKey, inclusive, toEnd, hi, hiInclusive);
		}
		
		@Override
		protected Node subLowest() { return absLowest(); }
		@Override
		protected Node subHighest() { return absHighest(); }
		@Override
		protected Node subCeiling(byte key) { return absCeiling(key); }
		@Override
		protected Node subHigher(byte key) { return absHigher(key); }
		@Override
		protected Node subFloor(byte key) { return absFloor(key); }
		@Override
		protected Node subLower(byte key) { return absLower(key); }
		
		@Override
		protected ByteBidirectionalIterator keyIterator() {
			return new AcsendingSubKeyIterator(absLowest(), absHighFence(), absLowFence()); 
		}
		@Override
		protected ByteBidirectionalIterator keyIterator(byte element) {
			return new AcsendingSubKeyIterator(absLower(element), absHighFence(), absLowFence()); 
		}
		
		@Override
		protected IntBidirectionalIterator valueIterator() {
			return new AcsendingSubValueIterator(absLowest(), absHighFence(), absLowFence()); 
		}
		
		@Override
		protected ByteBidirectionalIterator descendingKeyIterator() {
			return new DecsendingSubKeyIterator(absHighest(), absLowFence(), absHighFence()); 
		}
		
		class AscendingSubEntrySet extends SubEntrySet {
			@Override
			public ObjectIterator<Byte2IntMap.Entry> iterator() {
				return new AcsendingSubEntryIterator(absLowest(), absHighFence(), absLowFence());
			}
		}
	}
	
	static class DescendingNaivgableSubMap extends NavigableSubMap
	{
		ByteComparator comparator;
		DescendingNaivgableSubMap(Byte2IntAVLTreeMap map, boolean fromStart, byte lo, boolean loInclusive, boolean toEnd, byte hi, boolean hiInclusive) {
			super(map, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
			comparator = map.comparator() == null ? ByteComparator.of(Collections.reverseOrder()) : map.comparator().reversed();
		}
		
		@Override
		public ByteComparator comparator() { return comparator; }
		
		@Override
		public Byte2IntNavigableMap descendingMap() {
			if(inverse == null) inverse = new AscendingNaivgableSubMap(map, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
			return inverse;
		}

		@Override
		public ByteNavigableSet navigableKeySet() {
			if(keySet == null) keySet = new KeySet(this);
			return keySet;
		}
		
		@Override
		public Byte2IntNavigableMap subMap(byte fromKey, boolean fromInclusive, byte toKey, boolean toInclusive) {
			if (!inRange(fromKey, fromInclusive)) throw new IllegalArgumentException("fromKey out of range");
			if (!inRange(toKey, toInclusive)) throw new IllegalArgumentException("toKey out of range");
			return new DescendingNaivgableSubMap(map, false, toKey, toInclusive, false, fromKey, fromInclusive);
		}
		
		@Override
		public Byte2IntNavigableMap headMap(byte toKey, boolean inclusive) {
			if (!inRange(toKey, inclusive)) throw new IllegalArgumentException("toKey out of range");
			return new DescendingNaivgableSubMap(map, false, toKey, inclusive, toEnd, hi, hiInclusive);
		}
		
		@Override
		public Byte2IntNavigableMap tailMap(byte fromKey, boolean inclusive) {
			if (!inRange(fromKey, inclusive)) throw new IllegalArgumentException("fromKey out of range");
			return new DescendingNaivgableSubMap(map, fromStart, lo, loInclusive, false, fromKey, inclusive);
		}
		
		@Override
		public ObjectSet<Byte2IntMap.Entry> byte2IntEntrySet() {
			if(entrySet == null) entrySet = new DescendingSubEntrySet();
			return entrySet;
		}
		
		@Override
		protected Node subLowest() { return absHighest(); }
		@Override
		protected Node subHighest() { return absLowest(); }
		@Override
		protected Node subCeiling(byte key) { return absFloor(key); }
		@Override
		protected Node subHigher(byte key) { return absLower(key); }
		@Override
		protected Node subFloor(byte key) { return absCeiling(key); }
		@Override
		protected Node subLower(byte key) { return absHigher(key); }
		@Override
		protected Node next(Node entry) { return entry.previous(); }
		@Override
		protected Node previous(Node entry) { return entry.next(); }
		
		@Override
		protected ByteBidirectionalIterator keyIterator() {
			return new DecsendingSubKeyIterator(absHighest(), absLowFence(), absHighFence()); 
		}
		
		@Override
		protected ByteBidirectionalIterator keyIterator(byte element) {
			return new DecsendingSubKeyIterator(absHigher(element), absLowFence(), absHighFence()); 
		}
		
		@Override
		protected IntBidirectionalIterator valueIterator() {
			return new DecsendingSubValueIterator(absHighest(), absLowFence(), absHighFence()); 
		}
		
		@Override
		protected ByteBidirectionalIterator descendingKeyIterator() {
			return new AcsendingSubKeyIterator(absLowest(), absHighFence(), absLowFence()); 
		}
		
		class DescendingSubEntrySet extends SubEntrySet {
			@Override
			public ObjectIterator<Byte2IntMap.Entry> iterator() {
				return new DecsendingSubEntryIterator(absHighest(), absLowFence(), absHighFence());
			}
		}
	}
	
	static abstract class NavigableSubMap extends AbstractByte2IntMap implements Byte2IntNavigableMap
	{
		final Byte2IntAVLTreeMap map;
		final byte lo, hi;
		final boolean fromStart, toEnd;
		final boolean loInclusive, hiInclusive;
		
		Byte2IntNavigableMap inverse;
		ByteNavigableSet keySet;
		ObjectSet<Byte2IntMap.Entry> entrySet;
		IntCollection values;
		
		NavigableSubMap(Byte2IntAVLTreeMap map, boolean fromStart, byte lo, boolean loInclusive, boolean toEnd, byte hi, boolean hiInclusive) {
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
		public void setDefaultMaxValue(byte e) { map.setDefaultMaxValue(e); }
		@Override
		public byte getDefaultMaxValue() { return map.getDefaultMaxValue(); }
		@Override
		public void setDefaultMinValue(byte e) { map.setDefaultMinValue(e); }
		@Override
		public byte getDefaultMinValue() { return map.getDefaultMinValue(); }
		protected boolean isNullComparator() { return map.comparator() == null; }
		
		@Override
		public AbstractByte2IntMap setDefaultReturnValue(int v) { 
			map.setDefaultReturnValue(v);
			return this;
		}
		
		@Override
		public int getDefaultReturnValue() { return map.getDefaultReturnValue(); }
		
		@Override
		public IntCollection values() {
			if(values == null) values = new SubMapValues();
			return values;
		}
		
		@Override
		public ByteNavigableSet descendingKeySet() {
			return descendingMap().navigableKeySet();
		}
		
		@Override
		public ByteSet keySet() {
			return navigableKeySet();
		}
		
		protected abstract Node subLowest();
		protected abstract Node subHighest();
		protected abstract Node subCeiling(byte key);
		protected abstract Node subHigher(byte key);
		protected abstract Node subFloor(byte key);
		protected abstract Node subLower(byte key);
		protected abstract ByteBidirectionalIterator keyIterator();
		protected abstract ByteBidirectionalIterator keyIterator(byte element);
		protected abstract IntBidirectionalIterator valueIterator();
		protected abstract ByteBidirectionalIterator descendingKeyIterator();
		protected byte lowKeyOrNull(Node entry) { return entry == null ? (byte)0 : entry.key; }
		protected byte highKeyOrNull(Node entry) { return entry == null ? (byte)0 : entry.key; }
		protected Node next(Node entry) { return entry.next(); }
		protected Node previous(Node entry) { return entry.previous(); }
		
		protected boolean tooLow(byte key) {
			if (!fromStart) {
				int c = map.compare(key, lo);
				if (c < 0 || (c == 0 && !loInclusive)) return true;
			}
			return false;
		}
		
		protected boolean tooHigh(byte key) {
			if (!toEnd) {
				int c = map.compare(key, hi);
				if (c > 0 || (c == 0 && !hiInclusive)) return true;
			}
			return false;
		}
		protected boolean inRange(byte key) { return !tooLow(key) && !tooHigh(key); }
		protected boolean inClosedRange(byte key) { return (fromStart || map.compare(key, lo) >= 0) && (toEnd || map.compare(hi, key) >= 0); }
		protected boolean inRange(byte key, boolean inclusive) { return inclusive ? inRange(key) : inClosedRange(key); }
		
		protected Node absLowest() {
			Node e = (fromStart ?  map.first : (loInclusive ? map.findCeilingNode(lo) : map.findHigherNode(lo)));
			return (e == null || tooHigh(e.key)) ? null : e;
		}
		
		protected Node absHighest() {
			Node e = (toEnd ?  map.last : (hiInclusive ?  map.findFloorNode(hi) : map.findLowerNode(hi)));
			return (e == null || tooLow(e.key)) ? null : e;
		}
		
		protected Node absCeiling(byte key) {
			if (tooLow(key)) return absLowest();
			Node e = map.findCeilingNode(key);
			return (e == null || tooHigh(e.key)) ? null : e;
		}
		
		protected Node absHigher(byte key) {
			if (tooLow(key)) return absLowest();
			Node e = map.findHigherNode(key);
			return (e == null || tooHigh(e.key)) ? null : e;
		}
		
		protected Node absFloor(byte key) {
			if (tooHigh(key)) return absHighest();
			Node e = map.findFloorNode(key);
			return (e == null || tooLow(e.key)) ? null : e;
		}
		
		protected Node absLower(byte key) {
			if (tooHigh(key)) return absHighest();
			Node e = map.findLowerNode(key);
			return (e == null || tooLow(e.key)) ? null : e;
		}
		
		protected Node absHighFence() { return (toEnd ? null : (hiInclusive ? map.findHigherNode(hi) : map.findCeilingNode(hi))); }
		protected Node absLowFence() { return (fromStart ? null : (loInclusive ?  map.findLowerNode(lo) : map.findFloorNode(lo))); }
		
		@Override
		public ByteComparator comparator() { return map.comparator(); }
		
		@Override
		public byte pollFirstByteKey() {
			Node entry = subLowest();
			if(entry != null) {
				byte result = entry.key;
				map.removeNode(entry);
				return result;
			}
			return (byte)0;
		}
		
		@Override
		public byte pollLastByteKey() {
			Node entry = subHighest();
			if(entry != null) {
				byte result = entry.key;
				map.removeNode(entry);
				return result;
			}
			return (byte)0;
		}
		
		@Override
		public int firstIntValue() {
			Node entry = subLowest();
			return entry == null ? map.getDefaultReturnValue() : entry.value;
		}
		
		@Override
		public int lastIntValue() {
			Node entry = subHighest();
			return entry == null ? map.getDefaultReturnValue() : entry.value;
		}
		
		@Override
		public byte firstByteKey() {
			Node entry = subLowest();
			if(entry == null) throw new NoSuchElementException();
			return entry.key;
		}
		
		@Override
		public byte lastByteKey() {
			Node entry = subHighest();
			if(entry == null) throw new NoSuchElementException();
			return entry.key;
		}
		
		@Override
		public int put(byte key, int value) {
			if (!inRange(key)) throw new IllegalArgumentException("key out of range");
			return map.put(key, value);
		}
		
		@Override
		public int putIfAbsent(byte key, int value) {
			if (!inRange(key)) throw new IllegalArgumentException("key out of range");
			return map.putIfAbsent(key, value);
		}
		
		@Override
		public int addTo(byte key, int value) {
			if(!inRange(key)) throw new IllegalArgumentException("key out of range");
			return map.addTo(key, value);
		}
		
		@Override
		public int subFrom(byte key, int value) {
			if(!inRange(key)) throw new IllegalArgumentException("key out of range");
			return map.subFrom(key, value);
		}
		
		@Override
		public boolean containsKey(byte key) { return inRange(key) && map.containsKey(key); }
		
		@Override
		public int computeIntIfPresent(byte key, ByteIntUnaryOperator mappingFunction) {
			Objects.requireNonNull(mappingFunction);
			if(!inRange(key)) return getDefaultReturnValue();
			Node entry = map.findNode(key);
			if(entry == null || entry.value == getDefaultReturnValue()) return getDefaultReturnValue();
			int newValue = mappingFunction.apply(key, entry.value);
			if(newValue == getDefaultReturnValue()) {
				map.removeNode(entry);
				return newValue;
			}
			entry.value = newValue;
			return newValue;
		}
		
		@Override
		public int remove(byte key) {
			return inRange(key) ? map.remove(key) : getDefaultReturnValue();
		}
		
		@Override
		public int removeOrDefault(byte key, int defaultValue) {
			return inRange(key) ? map.remove(key) : defaultValue;
		}
		
		@Override
		public boolean remove(byte key, int value) {
			return inRange(key) && map.remove(key, value);
		}
		
		
		@Override
		public int get(byte key) {
			return inRange(key) ? map.get(key) : getDefaultReturnValue();
		}
		
		@Override
		public int getOrDefault(byte key, int defaultValue) {
			return inRange(key) ? map.getOrDefault(key, defaultValue) : getDefaultReturnValue();
		}
		
		
		@Override
		public byte lowerKey(byte key) { return lowKeyOrNull(subLower(key)); }
		@Override
		public byte floorKey(byte key) { return lowKeyOrNull(subFloor(key)); }
		@Override
		public byte ceilingKey(byte key) { return highKeyOrNull(subCeiling(key)); }
		@Override
		public byte higherKey(byte key) { return highKeyOrNull(subHigher(key)); }
		@Override
		public Byte2IntMap.Entry lowerEntry(byte key) { return subLower(key); }
		@Override
		public Byte2IntMap.Entry floorEntry(byte key) { return subFloor(key); }
		@Override
		public Byte2IntMap.Entry ceilingEntry(byte key) { return subCeiling(key); }
		@Override
		public Byte2IntMap.Entry higherEntry(byte key) { return subHigher(key); }
		
		@Override
		public boolean isEmpty() {
			if(fromStart && toEnd) return map.isEmpty();
			Node n = absLowest();
			return n == null || tooHigh(n.key);
		}
		
		@Override
		public int size() { return fromStart && toEnd ? map.size() : entrySet().size(); }
		
		@Override
		public Byte2IntNavigableMap copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public Byte2IntMap.Entry firstEntry() {
			Node entry = subLowest();
			return entry == null ? null : entry.export();
		}

		@Override
		public Byte2IntMap.Entry lastEntry() {
			Node entry = subHighest();
			return entry == null ? null : entry.export();
		}

		@Override
		public Byte2IntMap.Entry pollFirstEntry() {
			Node entry = subLowest();
			if(entry != null) {
				Byte2IntMap.Entry result = entry.export();
				map.removeNode(entry);
				return result;
			}
			return null;
		}

		@Override
		public Byte2IntMap.Entry pollLastEntry() {
			Node entry = subHighest();
			if(entry != null) {
				Byte2IntMap.Entry result = entry.export();
				map.removeNode(entry);
				return result;
			}
			return null;
		}
		
		abstract class SubEntrySet extends AbstractObjectSet<Byte2IntMap.Entry> {
			@Override
			public int size() {
				if (fromStart && toEnd) return map.size();
				int size = 0;
				for(ObjectIterator<Byte2IntMap.Entry> iter = iterator();iter.hasNext();iter.next(),size++);
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
				if(o instanceof Byte2IntMap.Entry)
				{
					Byte2IntMap.Entry entry = (Byte2IntMap.Entry) o;
					byte key = entry.getByteKey();
					if (!inRange(key)) return false;
					Node node = map.findNode(key);
					return node != null && entry.getIntValue() == node.getIntValue();
				}
				Map.Entry<?,?> entry = (Map.Entry<?,?>) o;
				if(entry.getKey() == null && isNullComparator()) return false;
				Byte key = (Byte)entry.getKey();
				if (!inRange(key)) return false;
				Node node = map.findNode(key);
				return node != null && Objects.equals(entry.getValue(), Integer.valueOf(node.getIntValue()));
			}
			
			@Override
			public boolean remove(Object o) {
				if (!(o instanceof Map.Entry)) return false;
				if(o instanceof Byte2IntMap.Entry)
				{
					Byte2IntMap.Entry entry = (Byte2IntMap.Entry) o;
					byte key = entry.getByteKey();
					if (!inRange(key)) return false;
					Node node = map.findNode(key);
					if (node != null && node.getValue() == entry.getValue()) {
						map.removeNode(node);
						return true;
					}
					return false;
				}
				Map.Entry<?,?> entry = (Map.Entry<?,?>) o;
				Byte key = (Byte)entry.getKey();
				if (!inRange(key)) return false;
				Node node = map.findNode(key);
				if (node != null && Objects.equals(node.getValue(), entry.getValue())) {
					map.removeNode(node);
					return true;
				}
				return false;
			}
			
			@Override
			public void forEach(Consumer<? super Byte2IntMap.Entry> action) {
				Objects.requireNonNull(action);
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					action.accept(new BasicEntry(entry.key, entry.value));
			}
			
			@Override
			public <E> void forEach(E input, ObjectObjectConsumer<E, Byte2IntMap.Entry> action) {
				Objects.requireNonNull(action);
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					action.accept(input, new BasicEntry(entry.key, entry.value));
			}
			
			@Override
			public boolean matchesAny(Object2BooleanFunction<Byte2IntMap.Entry> filter) {
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
			public boolean matchesNone(Object2BooleanFunction<Byte2IntMap.Entry> filter) {
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
			public boolean matchesAll(Object2BooleanFunction<Byte2IntMap.Entry> filter) {
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
			public <E> E reduce(E identity, BiFunction<E, Byte2IntMap.Entry, E> operator) {
				Objects.requireNonNull(operator);
				E state = identity;
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					state = operator.apply(state, new BasicEntry(entry.key, entry.value));
				}
				return state;
			}
			
			@Override
			public Byte2IntMap.Entry reduce(ObjectObjectUnaryOperator<Byte2IntMap.Entry, Byte2IntMap.Entry> operator) {
				Objects.requireNonNull(operator);
				Byte2IntMap.Entry state = null;
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
			public Byte2IntMap.Entry findFirst(Object2BooleanFunction<Byte2IntMap.Entry> filter) {
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
			public int count(Object2BooleanFunction<Byte2IntMap.Entry> filter) {
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
		
		final class SubMapValues extends AbstractIntCollection {
			@Override
			public boolean add(int o) { throw new UnsupportedOperationException(); }
			
			@Override
			public boolean contains(int e) {
				return containsValue(e);
			}
			
			@Override
			public IntIterator iterator() { return valueIterator(); }
			
			@Override
			public int size() {
				return NavigableSubMap.this.size();
			}
			
			@Override
			public void clear() {
				NavigableSubMap.this.clear();
			}
			
			@Override
			public void forEach(IntConsumer action) {
				Objects.requireNonNull(action);
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					action.accept(entry.value);
			}
			
			@Override
			public <E> void forEach(E input, ObjectIntConsumer<E> action) {
				Objects.requireNonNull(action);
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					action.accept(input, entry.value);
			}
			
			@Override
			public boolean matchesAny(Int2BooleanFunction filter) {
				Objects.requireNonNull(filter);
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					if(filter.get(entry.value)) return true;
				return false;
			}
			
			@Override
			public boolean matchesNone(Int2BooleanFunction filter) {
				Objects.requireNonNull(filter);
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					if(filter.get(entry.value)) return false;
				return true;
			}
			
			@Override
			public boolean matchesAll(Int2BooleanFunction filter) {
				Objects.requireNonNull(filter);
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					if(!filter.get(entry.value)) return false;
				return true;
			}
			
			@Override
			public int reduce(int identity, IntIntUnaryOperator operator) {
				Objects.requireNonNull(operator);
				int state = identity;
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					state = operator.applyAsInt(state, entry.value);
				return state;
			}
			
			@Override
			public int reduce(IntIntUnaryOperator operator) {
				Objects.requireNonNull(operator);
				int state = 0;
				boolean empty = true;
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					if(empty) {
						empty = false;
						state = entry.value;
						continue;
					}
					state = operator.applyAsInt(state, entry.value);
				}
				return state;
			}
			
			@Override
			public int findFirst(Int2BooleanFunction filter) {
				Objects.requireNonNull(filter);
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					if(filter.get(entry.value)) return entry.value;
				return 0;
			}
			
			@Override
			public int count(Int2BooleanFunction filter) {
				Objects.requireNonNull(filter);
				int result = 0;
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					if(filter.get(entry.value)) result++;
				return result;
			}
		}
		
		class DecsendingSubEntryIterator extends SubMapEntryIterator implements ObjectBidirectionalIterator<Byte2IntMap.Entry>
		{
			public DecsendingSubEntryIterator(Node first, Node forwardFence, Node backwardFence) {
				super(first, forwardFence, backwardFence);
			}
			
			@Override
			public Byte2IntMap.Entry previous() {
				if(!hasPrevious()) throw new NoSuchElementException();
				return nextEntry();
			}

			@Override
			public Byte2IntMap.Entry next() {
				if(!hasNext()) throw new NoSuchElementException();
				return previousEntry();
			}
		}
		
		class AcsendingSubEntryIterator extends SubMapEntryIterator implements ObjectBidirectionalIterator<Byte2IntMap.Entry>
		{
			public AcsendingSubEntryIterator(Node first, Node forwardFence, Node backwardFence) {
				super(first, forwardFence, backwardFence);
			}

			@Override
			public Byte2IntMap.Entry previous() {
				if(!hasPrevious()) throw new NoSuchElementException();
				return previousEntry();
			}

			@Override
			public Byte2IntMap.Entry next() {
				if(!hasNext()) throw new NoSuchElementException();
				return nextEntry();
			}
		}
		
		class DecsendingSubKeyIterator extends SubMapEntryIterator implements ByteBidirectionalIterator
		{
			public DecsendingSubKeyIterator(Node first, Node forwardFence, Node backwardFence) {
				super(first, forwardFence, backwardFence);
			}
			
			@Override
			public byte previousByte() {
				if(!hasPrevious()) throw new NoSuchElementException();
				return nextEntry().key;
			}

			@Override
			public byte nextByte() {
				if(!hasNext()) throw new NoSuchElementException();
				return previousEntry().key;
			}
		}
		
		class AcsendingSubKeyIterator extends SubMapEntryIterator implements ByteBidirectionalIterator
		{
			public AcsendingSubKeyIterator(Node first, Node forwardFence, Node backwardFence) {
				super(first, forwardFence, backwardFence);
			}

			@Override
			public byte previousByte() {
				if(!hasPrevious()) throw new NoSuchElementException();
				return previousEntry().key;
			}

			@Override
			public byte nextByte() {
				if(!hasNext()) throw new NoSuchElementException();
				return nextEntry().key;
			}
		}
		
		class AcsendingSubValueIterator extends SubMapEntryIterator implements IntBidirectionalIterator
		{
			public AcsendingSubValueIterator(Node first, Node forwardFence, Node backwardFence) {
				super(first, forwardFence, backwardFence);
			}

			@Override
			public int previousInt() {
				if(!hasPrevious()) throw new NoSuchElementException();
				return previousEntry().value;
			}

			@Override
			public int nextInt() {
				if(!hasNext()) throw new NoSuchElementException();
				return nextEntry().value;
			}
		}
		
		class DecsendingSubValueIterator extends SubMapEntryIterator implements IntBidirectionalIterator
		{
			public DecsendingSubValueIterator(Node first, Node forwardFence, Node backwardFence) {
				super(first, forwardFence, backwardFence);
			}
			
			@Override
			public int previousInt() {
				if(!hasPrevious()) throw new NoSuchElementException();
				return nextEntry().value;
			}

			@Override
			public int nextInt() {
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
			byte forwardFence;
			byte backwardFence;
			
			public SubMapEntryIterator(Node first, Node forwardFence, Node backwardFence)
			{
				next = first;
				this.forwardFence = forwardFence == null ? null : forwardFence.key;
				this.backwardFence = backwardFence == null ? null : backwardFence.key;
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
	
	class Values extends AbstractIntCollection
	{
		@Override
		public IntIterator iterator() {
			return new AscendingValueIterator(first);
		}
		
		@Override
		public boolean add(int e) { throw new UnsupportedOperationException(); }
		
		@Override
		public void clear() {
			Byte2IntAVLTreeMap.this.clear();
		}
		
		@Override
		public int size() {
			return Byte2IntAVLTreeMap.this.size;
		}
		
		@Override
		public boolean contains(int e) {
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
		public void forEach(IntConsumer action) {
			Objects.requireNonNull(action);
			for(Node entry = first;entry != null;entry = entry.next())
				action.accept(entry.value);
		}
		
		@Override
		public <E> void forEach(E input, ObjectIntConsumer<E> action) {
			Objects.requireNonNull(action);
			for(Node entry = first;entry != null;entry = entry.next())
				action.accept(input, entry.value);
		}
		
		@Override
		public boolean matchesAny(Int2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(Node entry = first;entry != null;entry = entry.next())
				if(filter.get(entry.value)) return true;
			return false;
		}
		
		@Override
		public boolean matchesNone(Int2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(Node entry = first;entry != null;entry = entry.next())
				if(filter.get(entry.value)) return false;
			return true;
		}
		
		@Override
		public boolean matchesAll(Int2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(Node entry = first;entry != null;entry = entry.next())
				if(!filter.get(entry.value)) return false;
			return true;
		}
		
		@Override
		public int reduce(int identity, IntIntUnaryOperator operator) {
			Objects.requireNonNull(operator);
			int state = identity;
			for(Node entry = first;entry != null;entry = entry.next())
				state = operator.applyAsInt(state, entry.value);
			return state;
		}
		
		@Override
		public int reduce(IntIntUnaryOperator operator) {
			Objects.requireNonNull(operator);
			int state = 0;
			boolean empty = true;
			for(Node entry = first;entry != null;entry = entry.next()) {
				if(empty) {
					empty = false;
					state = entry.value;
					continue;
				}
				state = operator.applyAsInt(state, entry.value);
			}
			return state;
		}
		
		@Override
		public int findFirst(Int2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(Node entry = first;entry != null;entry = entry.next())
				if(filter.get(entry.value)) return entry.value;
			return 0;
		}
		
		@Override
		public int count(Int2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			int result = 0;
			for(Node entry = first;entry != null;entry = entry.next())
				if(filter.get(entry.value)) result++;
			return result;
		}
	}
	
	class EntrySet extends AbstractObjectSet<Byte2IntMap.Entry> {
		
		@Override
		public ObjectIterator<Byte2IntMap.Entry> iterator() {
			return new AscendingMapEntryIterator(first);
		}
		
		@Override
		public void clear() {
			Byte2IntAVLTreeMap.this.clear();
		}
		
		@Override
		public int size() {
			return Byte2IntAVLTreeMap.this.size;
		}
		
		@Override
		public boolean contains(Object o) {
			if (!(o instanceof Map.Entry)) return false;
			if(o instanceof Byte2IntMap.Entry)
			{
				Byte2IntMap.Entry entry = (Byte2IntMap.Entry) o;
				byte key = entry.getByteKey();
				Node node = findNode(key);
				return node != null && entry.getIntValue() == node.getIntValue();
			}
			Map.Entry<?,?> entry = (Map.Entry<?,?>) o;
			if(entry.getKey() == null && comparator() == null) return false;
			if(!(entry.getKey() instanceof Byte)) return false;
			Byte key = (Byte)entry.getKey();
			Node node = findNode(key);
			return node != null && Objects.equals(entry.getValue(), Integer.valueOf(node.getIntValue()));
		}
		
		@Override
		public boolean remove(Object o) {
			if (!(o instanceof Map.Entry)) return false;
			if(o instanceof Byte2IntMap.Entry)
			{
				Byte2IntMap.Entry entry = (Byte2IntMap.Entry) o;
				byte key = entry.getByteKey();
				Node node = findNode(key);
				if (node != null && entry.getIntValue() == node.getIntValue()) {
					removeNode(node);
					return true;
				}
				return false;
			}
			Map.Entry<?,?> entry = (Map.Entry<?,?>) o;
			Byte key = (Byte)entry.getKey();
			Node node = findNode(key);
			if (node != null && Objects.equals(entry.getValue(), Integer.valueOf(node.getIntValue()))) {
				removeNode(node);
				return true;
			}
			return false;
		}
		
		@Override
		public void forEach(Consumer<? super Byte2IntMap.Entry> action) {
			Objects.requireNonNull(action);
			for(Node entry = first;entry != null;entry = entry.next())
				action.accept(new BasicEntry(entry.key, entry.value));
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, Byte2IntMap.Entry> action) {
			Objects.requireNonNull(action);
			for(Node entry = first;entry != null;entry = entry.next())
				action.accept(input, new BasicEntry(entry.key, entry.value));
		}
		
		@Override
		public boolean matchesAny(Object2BooleanFunction<Byte2IntMap.Entry> filter) {
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
		public boolean matchesNone(Object2BooleanFunction<Byte2IntMap.Entry> filter) {
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
		public boolean matchesAll(Object2BooleanFunction<Byte2IntMap.Entry> filter) {
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
		public <E> E reduce(E identity, BiFunction<E, Byte2IntMap.Entry, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			for(Node entry = first;entry != null;entry = entry.next()) {
				state = operator.apply(state, new BasicEntry(entry.key, entry.value));
			}
			return state;
		}
		
		@Override
		public Byte2IntMap.Entry reduce(ObjectObjectUnaryOperator<Byte2IntMap.Entry, Byte2IntMap.Entry> operator) {
			Objects.requireNonNull(operator);
			Byte2IntMap.Entry state = null;
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
		public Byte2IntMap.Entry findFirst(Object2BooleanFunction<Byte2IntMap.Entry> filter) {
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
		public int count(Object2BooleanFunction<Byte2IntMap.Entry> filter) {
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
	
	class DescendingKeyIterator extends MapEntryIterator implements ByteBidirectionalIterator
	{
		public DescendingKeyIterator(Node first) {
			super(first);
		}

		@Override
		public byte previousByte() {
			if(!hasPrevious()) throw new NoSuchElementException();
			return nextEntry().key;
		}
		
		@Override
		public byte nextByte() {
			if(!hasNext()) throw new NoSuchElementException();
			return previousEntry().key;
		}
	}
	
	class AscendingMapEntryIterator extends MapEntryIterator implements ObjectBidirectionalIterator<Byte2IntMap.Entry>
	{
		public AscendingMapEntryIterator(Node first)
		{
			super(first);
		}

		@Override
		public Byte2IntMap.Entry previous() {
			if(!hasPrevious()) throw new NoSuchElementException();
			return previousEntry();
		}

		@Override
		public Byte2IntMap.Entry next() {
			if(!hasNext()) throw new NoSuchElementException();
			return nextEntry();
		}
	}
	
	class AscendingValueIterator extends MapEntryIterator implements IntBidirectionalIterator
	{
		public AscendingValueIterator(Node first) {
			super(first);
		}

		@Override
		public int previousInt() {
			if(!hasPrevious()) throw new NoSuchElementException();
			return previousEntry().value;
		}

		@Override
		public int nextInt() {
			if(!hasNext()) throw new NoSuchElementException();
			return nextEntry().value;
		}
	}
	
	class AscendingKeyIterator extends MapEntryIterator implements ByteBidirectionalIterator
	{
		public AscendingKeyIterator(Node first) {
			super(first);
		}

		@Override
		public byte previousByte() {
			if(!hasPrevious()) throw new NoSuchElementException();
			return previousEntry().key;
		}

		@Override
		public byte nextByte() {
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
	
	private static final class Node implements Byte2IntMap.Entry
	{
		byte key;
		int value;
		int state;
		Node parent;
		Node left;
		Node right;
		
		Node(byte key, int value, Node parent) {
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
		public byte getByteKey() {
			return key;
		}
		
		@Override
		public int getIntValue() {
			return value;
		}
		
		@Override
		public int setValue(int value) {
			int oldValue = this.value;
			this.value = value;
			return oldValue;
		}
		
		int addTo(int value) {
			int oldValue = this.value;
			this.value += value;
			return oldValue;
		}
		
		int subFrom(int value) {
			int oldValue = this.value;
			this.value -= value;
			return oldValue;
		}
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Map.Entry) {
				if(obj instanceof Byte2IntMap.Entry) {
					Byte2IntMap.Entry entry = (Byte2IntMap.Entry)obj;
					return key == entry.getByteKey() && value == entry.getIntValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object otherKey = entry.getKey();
				if(otherKey == null) return false;
				Object otherValue = entry.getValue();
				return otherKey instanceof Byte && otherValue instanceof Integer && key == ((Byte)otherKey).byteValue() && value == ((Integer)otherValue).intValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Byte.hashCode(key) ^ Integer.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Byte.toString(key) + "=" + Integer.toString(value);
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