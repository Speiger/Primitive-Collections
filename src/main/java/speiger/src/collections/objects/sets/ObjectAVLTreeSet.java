package speiger.src.collections.objects.sets;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.BiFunction;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;

import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;
import speiger.src.collections.ints.functions.consumer.IntObjectConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;
import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.utils.ObjectArrays;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Simple Type Specific AVL TreeSet implementation that reduces boxing/unboxing.
 * It is using a bit more memory then <a href="https://github.com/vigna/fastutil">FastUtil</a>,
 * but it saves a lot of Performance on the Optimized removal and iteration logic.
 * Which makes the implementation actually useable and does not get outperformed by Javas default implementation.
 * @param <T> the keyType of elements maintained by this Collection
 */
public class ObjectAVLTreeSet<T> extends AbstractObjectSet<T> implements ObjectNavigableSet<T>
{
	/** The center of the Tree */
	protected transient Entry<T> tree;
	/** The Lowest possible Node */
	protected transient Entry<T> first;
	/** The Highest possible Node */
	protected transient Entry<T> last;
	/** The amount of elements stored in the Set */
	protected int size = 0;
	/** The Sorter of the Tree */
	protected transient Comparator<T> comparator;
	
	/**
	 * Default Constructor
	 */
	public ObjectAVLTreeSet() {
	}
	
	/**
	 * Constructor that allows to define the sorter
	 * @param comp the function that decides how the tree is sorted, can be null
	 */
	public ObjectAVLTreeSet(Comparator<T> comp) {
		comparator = comp;
	}
	
	/**
	 * Helper constructor that allow to create a set from an array
	 * @param array the elements that should be used
	 */
	public ObjectAVLTreeSet(T[] array) {
		this(array, 0, array.length);
	}
	
	/**
	 * Helper constructor that allow to create a set from an array
	 * @param array the elements that should be used
	 * @param offset the starting index within the array
	 * @param length the amount of elements that are within the array
	 * @throws IllegalStateException if offset and length causes to step outside of the arrays range
	 */
	public ObjectAVLTreeSet(T[] array, int offset, int length) {
		SanityChecks.checkArrayCapacity(array.length, offset, length);
		for(int i = 0;i<length;i++) add(array[offset+i]);
	}
	
	/**
	 * Helper constructor that allow to create a set from an array
	 * @param array the elements that should be used
	 * @param comp the sorter of the tree, can be null
	 */
	public ObjectAVLTreeSet(T[] array, Comparator<T> comp) {
		this(array, 0, array.length, comp);
	}
	
	/**
	 * Helper constructor that allow to create a set from an array
	 * @param array the elements that should be used
	 * @param offset the starting index within the array
	 * @param length the amount of elements that are within the array
	 * @param comp the sorter of the tree, can be null
	 * @throws IllegalStateException if offset and length causes to step outside of the arrays range
	 */
	public ObjectAVLTreeSet(T[] array, int offset, int length, Comparator<T> comp) {
		comparator = comp;
		SanityChecks.checkArrayCapacity(array.length, offset, length);
		for(int i = 0;i<length;i++) add(array[offset+i]);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided SortedSet.
	 * @param sortedSet the set the elements should be added to the TreeSet
	 * @note this also includes the Comparator if present
	 */
	public ObjectAVLTreeSet(ObjectSortedSet<T> sortedSet) {
		comparator = sortedSet.comparator();
		addAll(sortedSet);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param collection the set the elements should be added to the TreeSet
	 */
	public ObjectAVLTreeSet(Collection<? extends T> collection) {
		addAll(collection);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param collection the set the elements should be added to the TreeSet
	 * @param comp the sorter of the tree, can be null
	 */
	public ObjectAVLTreeSet(Collection<? extends T> collection, Comparator<T> comp) {
		comparator = comp;
		addAll(collection);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param collection the set the elements should be added to the TreeSet
	 */
	public ObjectAVLTreeSet(ObjectCollection<T> collection) {
		addAll(collection);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param collection the set the elements should be added to the TreeSet
	 * @param comp the sorter of the tree, can be null
	 */
	public ObjectAVLTreeSet(ObjectCollection<T> collection, Comparator<T> comp) {
		comparator = comp;
		addAll(collection);
	}
	
	/**
	 * A Helper constructor that allows to create a set from a iterator of an unknown size
	 * @param iterator the elements that should be added to the set
	 */
	public ObjectAVLTreeSet(Iterator<T> iterator) {
		while(iterator.hasNext()) add(iterator.next());
	}
	
	/**
	 * A Helper constructor that allows to create a set from a iterator of an unknown size
	 * @param iterator the elements that should be added to the set
	 * @param comp the sorter of the tree, can be null
	 */
	public ObjectAVLTreeSet(Iterator<T> iterator, Comparator<T> comp) {
		comparator = comp;
		while(iterator.hasNext()) add(iterator.next());
	}
	
	/**
	 * A Helper constructor that allows to create a set from a iterator of an unknown size
	 * @param iterator the elements that should be added to the set
	 */
	public ObjectAVLTreeSet(ObjectIterator<T> iterator) {
		while(iterator.hasNext()) add(iterator.next());
	}
	
	/**
	 * A Helper constructor that allows to create a set from a iterator of an unknown size
	 * @param iterator the elements that should be added to the set
	 * @param comp the sorter of the tree, can be null
	 */
	public ObjectAVLTreeSet(ObjectIterator<T> iterator, Comparator<T> comp) {
		comparator = comp;
		while(iterator.hasNext()) add(iterator.next());
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
	public boolean add(T o) {
		validate(o);
		if(tree == null) {
			tree = first = last = new Entry<>(o, null);
			size++;
			return true;
		}
		int compare = 0;
		Entry<T> parent = tree;
		while(true) {
			if((compare = compare(o, parent.key)) == 0) return false;
			if(compare < 0) {
				if(parent.left == null) break;
				parent = parent.left;
			}
			else if(compare > 0) {
				if(parent.right == null) break;
				parent = parent.right;
			}
		}
		Entry<T> adding = new Entry<>(o, parent);
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
		return true;
	}
	
	@Override
	public T addOrGet(T o) {
		validate(o);
		if(tree == null) {
			tree = first = last = new Entry<>(o, null);
			size++;
			return o;
		}
		int compare = 0;
		Entry<T> parent = tree;
		while(true) {
			if((compare = compare(o, parent.key)) == 0) return parent.key;
			if(compare < 0) {
				if(parent.left == null) break;
				parent = parent.left;
			}
			else if(compare > 0) {
				if(parent.right == null) break;
				parent = parent.right;
			}
		}
		Entry<T> adding = new Entry<>(o, parent);
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
		return o;
	}
	
	@Override
	public T lower(T e) {
		Entry<T> node = findLowerNode(e);
		return node != null ? node.key : getDefaultMinValue();
	}

	@Override
	public T floor(T e) {
		Entry<T> node = findFloorNode(e);
		return node != null ? node.key : getDefaultMinValue();
	}
	
	@Override
	public T higher(T e) {
		Entry<T> node = findHigherNode(e);
		return node != null ? node.key : getDefaultMaxValue();
	}

	@Override
	public T ceiling(T e) {
		Entry<T> node = findCeilingNode(e);
		return node != null ? node.key : getDefaultMaxValue();
	}
	
	@Override
	public void forEach(Consumer<? super T> action) {
		Objects.requireNonNull(action);
		for(Entry<T> entry = first;entry != null;entry = entry.next()) {
			action.accept(entry.key);
		}
	}
	
	@Override
	public void forEachIndexed(IntObjectConsumer<T> action) {
		Objects.requireNonNull(action);
		int index = 0;
		for(Entry<T> entry = first;entry != null;entry = entry.next()) {
			action.accept(index++, entry.key);
		}
	}
	
	@Override
	public <E> void forEach(E input, ObjectObjectConsumer<E, T> action) {
		Objects.requireNonNull(action);
		for(Entry<T> entry = first;entry != null;entry = entry.next())
			action.accept(input, entry.key);		
	}
	
	@Override
	public boolean matchesAny(Predicate<T> filter) {
		Objects.requireNonNull(filter);
		for(Entry<T> entry = first;entry != null;entry = entry.next()) {
			if(filter.test(entry.key)) return true;
		}
		return false;
	}
	
	@Override
	public boolean matchesNone(Predicate<T> filter) {
		Objects.requireNonNull(filter);
		for(Entry<T> entry = first;entry != null;entry = entry.next()) {
			if(filter.test(entry.key)) return false;
		}
		return true;
	}
	
	@Override
	public boolean matchesAll(Predicate<T> filter) {
		Objects.requireNonNull(filter);
		for(Entry<T> entry = first;entry != null;entry = entry.next()) {
			if(!filter.test(entry.key)) return false;
		}
		return true;
	}
	
	@Override
	public T findFirst(Predicate<T> filter) {
		Objects.requireNonNull(filter);
		for(Entry<T> entry = first;entry != null;entry = entry.next()) {
			if(filter.test(entry.key)) return entry.key;
		}
		return null;
	}
	
	@Override
	public <E> E reduce(E identity, BiFunction<E, T, E> operator) {
		Objects.requireNonNull(operator);
		E state = identity;
		for(Entry<T> entry = first;entry != null;entry = entry.next()) {
			state = operator.apply(state, entry.key);
		}
		return state;
	}
	
	@Override
	public T reduce(ObjectObjectUnaryOperator<T, T> operator) {
		Objects.requireNonNull(operator);
		T state = null;
		boolean empty = true;
		for(Entry<T> entry = first;entry != null;entry = entry.next()) {
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
	public int count(Predicate<T> filter) {
		Objects.requireNonNull(filter);
		int result = 0;
		for(Entry<T> entry = first;entry != null;entry = entry.next()) {
			if(filter.test(entry.key)) result++;
		}
		return result;
	}
	
	protected Entry<T> findNode(T o) {
		Entry<T> node = tree;
		int compare;
		while(node != null) {
			if((compare = compare(o, node.key)) == 0) return node;
			if(compare < 0) node = node.left;
			else node = node.right;
		}
		return null;
	}
	
	protected Entry<T> findLowerNode(T e) {
		Entry<T> entry = tree;
		while(entry != null) {
			if(compare(e, entry.key) > 0) {
				if(entry.right != null) entry = entry.right;
				else return entry;
			}
			else {
				if(entry.left != null) entry = entry.left;
				else {
					Entry<T> parent = entry.parent;
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
	
	protected Entry<T> findFloorNode(T e) {
		Entry<T> entry = tree;
		int compare;
		while(entry != null) {
			if((compare = compare(e, entry.key)) > 0) {
				if(entry.right == null) break;
				entry = entry.right;
				continue;
			}
			else if(compare < 0) {
				if(entry.left != null) entry = entry.left;
				else {
					Entry<T> parent = entry.parent;
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
	
	protected Entry<T> findCeilingNode(T e) {
		Entry<T> entry = tree;
		int compare;
		while(entry != null) {
			if((compare = compare(e, entry.key)) < 0) {
				if(entry.left == null) break;
				entry = entry.left;
				continue;
			}
			else if(compare > 0) {
				if(entry.right != null) entry = entry.right;
				else {
					Entry<T> parent = entry.parent;
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
	
	protected Entry<T> findHigherNode(T e) {
		Entry<T> entry = tree;
		while(entry != null) {
			if(compare(e, entry.key) < 0) {
				if(entry.left != null) entry = entry.left;
				else return entry;
			}
			else {
				if(entry.right != null) entry = entry.right;
				else {
					Entry<T> parent = entry.parent;
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
	
	@Override
	public boolean contains(Object e) {
		return findNode(((T)e)) != null;
	}
	
	@Override
	public T first() {
		if(tree == null) throw new NoSuchElementException();
		return first.key;
	}
	
	@Override
	public T last() {
		if(tree == null) throw new NoSuchElementException();
		return last.key;
	}
	
	@Override
	public boolean remove(Object o) {
		if(tree == null) return false;
		Entry<T> entry = findNode(((T)o));
		if(entry != null) {
			removeNode(entry);
			return true;
		}
		return false;
	}
	
	@Override
	public T pollFirst() {
		if(tree == null) return null;
		T value = first.key;
		removeNode(first);
		return value;
	}
	
	@Override
	public T pollLast() {
		if(tree == null) return null;
		T value = last.key;
		removeNode(last);
		return value;
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
	
	@Override
	@Deprecated
	public Object[] toArray() {
		if(isEmpty()) return ObjectArrays.EMPTY_ARRAY;
		Object[] obj = new Object[size()];
		int index = 0;
		for(Entry<T> entry = first;entry != null;entry = entry.next()) {
			obj[index++] = entry.key;
		}
		return obj;
	}
	
	@Override
	public <E> E[] toArray(E[] a) {
		if(a == null) a = (E[])new Object[size()];
		else if(a.length < size()) a = (E[])ObjectArrays.newArray(a.getClass().getComponentType(), size());
		int index = 0;
		for(Entry<T> entry = first;entry != null;entry = entry.next()) {
			a[index++] = (E)entry.key;
		}
		if (a.length > size) a[size] = null;
		return a;
	}
	
	public ObjectAVLTreeSet<T> copy() {
		ObjectAVLTreeSet<T> set = new ObjectAVLTreeSet<>();
		set.size = size;
		if(tree != null) {
			set.tree = tree.copy();
			Entry<T> lastFound = null;
			for(Entry<T> entry = tree;entry != null;entry = entry.left) lastFound = entry;
			set.first = lastFound;
			lastFound = null;
			for(Entry<T> entry = tree;entry != null;entry = entry.right) lastFound = entry;
			set.last = lastFound;
		}
		return set;
	}
	
	@Override
	public Comparator<T> comparator() { return comparator; }
	
	@Override
	public ObjectBidirectionalIterator<T> iterator() { return new AscendingSetIterator(first); }
	
	@Override
	public ObjectBidirectionalIterator<T> iterator(T fromElement) {
		Entry<T> entry = findNode(fromElement);
		return entry == null ? null : new AscendingSetIterator(entry);
	}
	
	@Override
	public ObjectBidirectionalIterator<T> descendingIterator() { return new DescendingSetIterator(last); }
	
	@Override
	public ObjectNavigableSet<T> subSet(T fromElement, boolean fromInclusive, T toElement, boolean toInclusive) {
		return new AscendingSubSet<>(this, false, fromElement, fromInclusive, false, toElement, toInclusive);
	}

	@Override
	public ObjectNavigableSet<T> headSet(T toElement, boolean inclusive) {
		return new AscendingSubSet<>(this, true, null, true, false, toElement, inclusive);
	}

	@Override
	public ObjectNavigableSet<T> tailSet(T fromElement, boolean inclusive) {
		return new AscendingSubSet<>(this, false, fromElement, inclusive, true, null, true);
	}
	
	@Override
	public ObjectNavigableSet<T> descendingSet() {
		return new DescendingSubSet<>(this, true, null, true, true, null, true);
	}
	
	protected void removeNode(Entry<T> entry) {
		size--;
		if(entry.needsSuccessor()) {
			Entry<T> successor = entry.next();
			entry.key = successor.key;
			entry = successor;
		}
		if(entry.previous() == null) first = entry.next();
		if(entry.next() == null) last = entry.previous();
		Entry<T> replacement = entry.left != null ? entry.left : entry.right;
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
	
	protected void validate(T k) { compare(k, k); }
	protected int compare(T k, T v) { return comparator != null ? comparator.compare(k, v) : ((Comparable<T>)k).compareTo((T)v);}
	
	/** From CLR */
	protected void rotateLeft(Entry<T> entry) {
		if(entry != null) {
			Entry<T> right = entry.right;
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
	protected void rotateRight(Entry<T> entry) {
		if(entry != null) {
			Entry<T> left = entry.left;
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
	protected void fixAfterInsertion(Entry<T> entry) {
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
	protected void fixAfterDeletion(Entry<T> entry) {
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
	
	static class AscendingSubSet<T> extends SubSet<T>
	{
		public AscendingSubSet(ObjectAVLTreeSet<T> set, boolean fromStart, T lo, boolean loInclusive, boolean toEnd, T hi, boolean hiInclusive)
		{
			super(set, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
		}
		
		@Override
		public Comparator<T> comparator() { return set.comparator(); }
		
		@Override
		public ObjectNavigableSet<T> subSet(T fromElement, boolean fromInclusive, T toElement, boolean toInclusive)
		{
			if(!inRange(fromElement, fromInclusive)) throw new IllegalArgumentException("fromElement out of range");
			if(!inRange(toElement, toInclusive)) throw new IllegalArgumentException("toElement out of range");
			return new AscendingSubSet<>(set, false, fromElement, fromInclusive, false, toElement, toInclusive);
		}
		
		@Override
		public ObjectNavigableSet<T> headSet(T toElement, boolean inclusive)
		{
			if(!inRange(toElement, inclusive)) throw new IllegalArgumentException("toElement out of range");
			return new AscendingSubSet<>(set, fromStart, lo, loInclusive, false, toElement, inclusive);
		}

		@Override
		public ObjectNavigableSet<T> tailSet(T fromElement, boolean inclusive)
		{
			if(!inRange(fromElement, inclusive)) throw new IllegalArgumentException("fromElement out of range");
			return new AscendingSubSet<>(set, false, fromElement, inclusive, toEnd, hi, hiInclusive);
		}
		
		@Override
		public ObjectBidirectionalIterator<T> iterator()
		{
			return new AscendingSubSetIterator(absLowest(), absHighFence(), absLowFence());
		}
		
		@Override
		public ObjectBidirectionalIterator<T> iterator(T fromElement)
		{
			return new AscendingSubSetIterator(absLower(fromElement), absHighFence(), absLowFence());
		}
		
		@Override
		public ObjectBidirectionalIterator<T> descendingIterator()
		{
			return new DescendingSubSetIterator(absHighest(), absLowFence(), absHighFence());
		}

		@Override
		public ObjectNavigableSet<T> descendingSet()
		{
			if(inverse == null) inverse = new DescendingSubSet<>(set, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
			return inverse;
		}
		
		@Override
		protected Entry<T> subLowest() { return absLowest(); }
		@Override
		protected Entry<T> subHighest() { return absHighest(); }
		@Override
		protected Entry<T> subCeiling(T key) { return absCeiling(key); }
		@Override
		protected Entry<T> subHigher(T key) { return absHigher(key); }
		@Override
		protected Entry<T> subFloor(T key) { return absFloor(key); }
		@Override
		protected Entry<T> subLower(T key) { return absLower(key); }
		@Override
		protected Entry<T> start() { return absLowest(); }
		@Override
		protected Entry<T> next(Entry<T> entry) { return entry.next(); }
	}
	
	static class DescendingSubSet<T> extends SubSet<T>
	{
		Comparator<T> comparator;
		
		public DescendingSubSet(ObjectAVLTreeSet<T> set, boolean fromStart, T lo, boolean loInclusive, boolean toEnd, T hi, boolean hiInclusive)
		{
			super(set, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
			comparator = Collections.reverseOrder(set.comparator());
		}
		
		@Override
		public Comparator<T> comparator() { return comparator; }
		
		@Override
		public ObjectNavigableSet<T> subSet(T fromElement, boolean fromInclusive, T toElement, boolean toInclusive) {
			if(!inRange(fromElement, fromInclusive)) throw new IllegalArgumentException("fromElement out of range");
			if(!inRange(toElement, toInclusive)) throw new IllegalArgumentException("toElement out of range");
			return new DescendingSubSet<>(set, false, toElement, toInclusive, false, fromElement, fromInclusive);
		}
		
		@Override
		public ObjectNavigableSet<T> headSet(T toElement, boolean inclusive)
		{
			if(!inRange(toElement, inclusive)) throw new IllegalArgumentException("toElement out of range");
			return new DescendingSubSet<>(set, false, toElement, inclusive, toEnd, hi, hiInclusive);
		}

		@Override
		public ObjectNavigableSet<T> tailSet(T fromElement, boolean inclusive) {
			if(!inRange(fromElement, inclusive)) throw new IllegalArgumentException("fromElement out of range");
			return new DescendingSubSet<>(set, fromStart, lo, loInclusive, false, fromElement, inclusive);
		}

		@Override
		public ObjectBidirectionalIterator<T> iterator() {
			return new DescendingSubSetIterator(absHighest(), absLowFence(), absHighFence());
		}

		@Override
		public ObjectBidirectionalIterator<T> descendingIterator() {
			return new AscendingSubSetIterator(absLowest(), absHighFence(), absLowFence());
		}

		@Override
		public ObjectNavigableSet<T> descendingSet() {
			if(inverse == null) inverse = new AscendingSubSet<>(set, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
			return inverse;
		}
		
		@Override
		public ObjectBidirectionalIterator<T> iterator(T fromElement) {
			return new DescendingSubSetIterator(absHigher(fromElement), absLowFence(), absHighFence());
		}
		
		@Override
		protected Entry<T> subLowest() { return absHighest(); }
		@Override
		protected Entry<T> subHighest() { return absLowest(); }
		@Override
		protected Entry<T> subCeiling(T key) { return absFloor(key); }
		@Override
		protected Entry<T> subHigher(T key) { return absLower(key); }
		@Override
		protected Entry<T> subFloor(T key) { return absCeiling(key); }
		@Override
		protected Entry<T> subLower(T key) { return absHigher(key); }
		@Override
		protected Entry<T> start() { return absHighest(); }
		@Override
		protected Entry<T> next(Entry<T> entry) { return entry.previous(); }
	}
	
	static abstract class SubSet<T> extends AbstractObjectSet<T> implements ObjectNavigableSet<T>
	{
		final ObjectAVLTreeSet<T> set;
		final T lo, hi;
		final boolean fromStart, toEnd;
		final boolean loInclusive, hiInclusive;
		ObjectNavigableSet<T> inverse;
		
		public SubSet(ObjectAVLTreeSet<T> set, boolean fromStart, T lo, boolean loInclusive, boolean toEnd, T hi, boolean hiInclusive)
		{
			this.set = set;
			this.lo = lo;
			this.hi = hi;
			this.fromStart = fromStart;
			this.toEnd = toEnd;
			this.loInclusive = loInclusive;
			this.hiInclusive = hiInclusive;
		}
		
		public T getDefaultMaxValue() { return null; }
		public T getDefaultMinValue() { return null; }
		@Override
		public abstract	ObjectBidirectionalIterator<T> iterator();
		
		boolean tooLow(T key) {
			if (!fromStart) {
				int c = set.compare(key, lo);
				if (c < 0 || (c == 0 && !loInclusive)) return true;
			}
			return false;
		}
		
		boolean tooHigh(T key) {
			if (!toEnd) {
				int c = set.compare(key, hi);
				if (c > 0 || (c == 0 && !hiInclusive)) return true;
			}
			return false;
		}
		
		boolean inRange(T key) { return !tooLow(key) && !tooHigh(key); }
		boolean inClosedRange(T key) { return (fromStart || set.compare(key, lo) >= 0) && (toEnd || set.compare(hi, key) >= 0); }
		boolean inRange(T key, boolean inclusive) { return inclusive ? inRange(key) : inClosedRange(key); }
		
		protected abstract Entry<T> subLowest();
		protected abstract Entry<T> subHighest();
		protected abstract Entry<T> subCeiling(T key);
		protected abstract Entry<T> subHigher(T key);
		protected abstract Entry<T> subFloor(T key);
		protected abstract Entry<T> subLower(T key);
		protected T lowKeyOrNull(Entry<T> entry) { return entry == null ? getDefaultMinValue() : entry.key; }
		protected T highKeyOrNull(Entry<T> entry) { return entry == null ? getDefaultMaxValue() : entry.key; }
		protected abstract Entry<T> start();
		protected abstract Entry<T> next(Entry<T> entry);
		
		final Entry<T> absLowest() {
			Entry<T> e = (fromStart ? set.first : (loInclusive ? set.findCeilingNode(lo) : set.findHigherNode(lo)));
			return (e == null || tooHigh(e.key)) ? null : e;
		}
		
		final Entry<T> absHighest() {
			Entry<T> e = (toEnd ? set.last : (hiInclusive ? set.findFloorNode(hi) : set.findLowerNode(hi)));
			return (e == null || tooLow(e.key)) ? null : e;
		}
		
		final Entry<T> absCeiling(T key) {
			if (tooLow(key)) return absLowest();
			Entry<T> e = set.findCeilingNode(key);
			return (e == null || tooHigh(e.key)) ? null : e;
		}
		
		final Entry<T> absHigher(T key) {
			if (tooLow(key)) return absLowest();
			Entry<T> e = set.findHigherNode(key);
			return (e == null || tooHigh(e.key)) ? null : e;
		}
		
		final Entry<T> absFloor(T key) {
			if (tooHigh(key)) return absHighest();
			Entry<T> e = set.findFloorNode(key);
			return (e == null || tooLow(e.key)) ? null : e;
		}
		
		final Entry<T> absLower(T key) {
			if (tooHigh(key)) return absHighest();
			Entry<T> e = set.findLowerNode(key);
			return (e == null || tooLow(e.key)) ? null : e;
		}
		
		final Entry<T> absHighFence() { return (toEnd ? null : (hiInclusive ? set.findHigherNode(hi) : set.findCeilingNode(hi))); }
		final Entry<T> absLowFence() { return (fromStart ? null : (loInclusive ? set.findLowerNode(lo) : set.findFloorNode(lo))); }
		
		@Override
		public boolean add(T o) {
			if(!inRange(o)) throw new IllegalArgumentException("Key is out of range");
			return set.add(o);
		}
		
		@Override
		public boolean contains(Object e) {
			T o = ((T)e);
			return inRange(o) && set.contains(o);
		}
	
		@Override
		public boolean remove(Object e) {
			T o = ((T)e);
			return inRange(o) && set.remove(o);
		}
		
		@Override
		public boolean isEmpty() {
			if(fromStart && toEnd) return set.isEmpty();
            Entry<T> n = absLowest();
            return n == null || tooHigh(n.key);
		}
		
		@Override
		public int size() {
			if(fromStart && toEnd) return set.size();
			int i = 0;
			for(ObjectIterator<T> iter = iterator();iter.hasNext();iter.next(),i++);
			return i;
		}
		
		@Override
		public T lower(T e) {
			return lowKeyOrNull(subLower(e));
		}
		
		@Override
		public T floor(T e) {
			return lowKeyOrNull(subFloor(e));
		}
		
		@Override
		public T ceiling(T e) {
			return highKeyOrNull(subCeiling(e));
		}
		
		@Override
		public T higher(T e) {
			return highKeyOrNull(subHigher(e));
		}
		
		@Override
		public T pollFirst() {
			Entry<T> entry = subLowest();
			if(entry != null) {
				T result = entry.key;
				set.removeNode(entry);
				return result;
			}
			return getDefaultMinValue();
		}
		
		@Override
		public T pollLast() {
			Entry<T> entry = subHighest();
			if(entry != null) {
				T result = entry.key;
				set.removeNode(entry);
				return result;
			}
			return getDefaultMaxValue();
		}
		
		@Override
		public T first() {
			Entry<T> entry = subLowest();
			if(entry == null) throw new NoSuchElementException();
			return entry.key;
		}
		
		@Override
		public T last() {
			Entry<T> entry = subHighest();
			if(entry == null) throw new NoSuchElementException();
			return entry.key;
		}
		
		@Override
		public SubSet<T> copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(Consumer<? super T> action) {
			Objects.requireNonNull(action);
			for(Entry<T> entry = start();entry != null && inRange(entry.key);entry = next(entry)) {
				action.accept(entry.key);
			}
		}
		
		@Override
		public void forEachIndexed(IntObjectConsumer<T> action) {
			Objects.requireNonNull(action);
			int index = 0;
			for(Entry<T> entry = start();entry != null && inRange(entry.key);entry = next(entry)) {
				action.accept(index++, entry.key);
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, T> action) {
			Objects.requireNonNull(action);
			for(Entry<T> entry = start();entry != null && inRange(entry.key);entry = next(entry))
				action.accept(input, entry.key);		
		}
		
		@Override
		public boolean matchesAny(Predicate<T> filter) {
			Objects.requireNonNull(filter);
			for(Entry<T> entry = start();entry != null && inRange(entry.key);entry = next(entry)) {
				if(filter.test(entry.key)) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Predicate<T> filter) {
			Objects.requireNonNull(filter);
			for(Entry<T> entry = start();entry != null && inRange(entry.key);entry = next(entry)) {
				if(filter.test(entry.key)) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Predicate<T> filter) {
			Objects.requireNonNull(filter);
			for(Entry<T> entry = start();entry != null && inRange(entry.key);entry = next(entry)) {
				if(!filter.test(entry.key)) return false;
			}
			return true;
		}
		
		@Override
		public <E> E reduce(E identity, BiFunction<E, T, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			for(Entry<T> entry = start();entry != null && inRange(entry.key);entry = next(entry)) {
				state = operator.apply(state, entry.key);
			}
			return state;
		}
		
		@Override
		public T reduce(ObjectObjectUnaryOperator<T, T> operator) {
			Objects.requireNonNull(operator);
			T state = null;
			boolean empty = true;
			for(Entry<T> entry = start();entry != null && inRange(entry.key);entry = next(entry)) {
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
			for(Entry<T> entry = start();entry != null && inRange(entry.key);entry = next(entry)) {
				if(filter.test(entry.key)) return entry.key;
			}
			return null;
		}
		
		@Override
		public int count(Predicate<T> filter) {
			Objects.requireNonNull(filter);
			int result = 0;
			for(Entry<T> entry = start();entry != null && inRange(entry.key);entry = next(entry)) {
				if(filter.test(entry.key)) result++;
			}
			return result;
		}
		
		class AscendingSubSetIterator implements ObjectBidirectionalIterator<T>
		{
			Entry<T> lastReturned;
			Entry<T> next;
			Entry<T> previous;
			boolean forwards = false;
			boolean unboundForwardFence;
			boolean unboundBackwardFence;
			T forwardFence;
			T backwardFence;
			
			public AscendingSubSetIterator(Entry<T> first, Entry<T> forwardFence, Entry<T> backwardFence)
			{
				next = first;
				previous = first == null ? null : first.previous();
				this.forwardFence = forwardFence == null ? null : forwardFence.key;
				this.backwardFence = backwardFence == null ? null : backwardFence.key;
				unboundForwardFence = forwardFence == null;
				unboundBackwardFence = backwardFence == null;
			}
			
			@Override
			public boolean hasNext() {
                return next != null && (unboundForwardFence || next.key != forwardFence);
			}
			
			@Override
			public T next() {
				if(!hasNext()) throw new NoSuchElementException();
				lastReturned = next;
				previous = next;
				T result = next.key;
				next = next.next();
				forwards = true;
				return result;
			}
			
			@Override
			public boolean hasPrevious() {
                return previous != null && (unboundBackwardFence || previous.key != backwardFence);
			}
			
			@Override
			public T previous() {
				if(!hasPrevious()) throw new NoSuchElementException();
				lastReturned = previous;
				next = previous;
				T result = previous.key;
				previous = previous.previous();
				forwards = false;
				return result;
			}
			
			@Override
			public void remove() {
				if(lastReturned == null) throw new IllegalStateException();
				if(previous == lastReturned) previous = previous.previous();
				if(next == lastReturned) next = next.next();
				if(forwards && lastReturned.needsSuccessor()) next = lastReturned;
				set.removeNode(lastReturned);
				lastReturned = null;
			}
		}
		
		class DescendingSubSetIterator implements ObjectBidirectionalIterator<T>
		{
			Entry<T> lastReturned;
			Entry<T> next;
			Entry<T> previous;
			boolean forwards = false;
			boolean unboundForwardFence;
			boolean unboundBackwardFence;
			T forwardFence;
			T backwardFence;
			
			public DescendingSubSetIterator(Entry<T> first, Entry<T> forwardFence, Entry<T> backwardFence)
			{
				next = first;
				previous = first == null ? null : first.next();
				this.forwardFence = forwardFence == null ? null : forwardFence.key;
				this.backwardFence = backwardFence == null ? null : backwardFence.key;
				unboundForwardFence = forwardFence == null;
				unboundBackwardFence = backwardFence == null;
			}
			
			@Override
			public boolean hasNext() {
                return next != null && (unboundForwardFence || next.key != forwardFence);
			}
			
			@Override
			public T next() {
				if(!hasNext()) throw new NoSuchElementException();
				lastReturned = next;
				previous = next;
				T result = next.key;
				next = next.previous();
				forwards = false;
				return result;
			}
			
			@Override
			public boolean hasPrevious() {
                return previous != null && (unboundBackwardFence || previous.key != backwardFence);
			}
			
			@Override
			public T previous() {
				if(!hasPrevious()) throw new NoSuchElementException();
				lastReturned = previous;
				next = previous;
				T result = previous.key;
				previous = previous.next();
				forwards = true;
				return result;
			}
			
			@Override
			public void remove() {
				if(lastReturned == null) throw new IllegalStateException();
				if(previous == lastReturned) previous = previous.next();
				if(next == lastReturned) next = next.previous();
				if(forwards && lastReturned.needsSuccessor()) next = lastReturned;
				set.removeNode(lastReturned);
				lastReturned = null;
			}
		}
	}
	
	class AscendingSetIterator implements ObjectBidirectionalIterator<T>
	{
		Entry<T> lastReturned;
		Entry<T> next;
		Entry<T> previous;
		boolean forwards = false;
		
		public AscendingSetIterator(Entry<T> first)
		{
			next = first;
			previous = first == null ? null : first.previous();
		}
		
		@Override
		public boolean hasNext() {
            return next != null;
		}
		
		@Override
		public T next() {
			if(!hasNext()) throw new NoSuchElementException();
			lastReturned = next;
			previous = next;
			T result = next.key;
			next = next.next();
			forwards = true;
			return result;
		}
		
		@Override
		public boolean hasPrevious() {
            return previous != null;
		}
		
		@Override
		public T previous() {
			if(!hasPrevious()) throw new NoSuchElementException();
			lastReturned = previous;
			next = previous;
			T result = previous.key;
			previous = previous.previous();
			forwards = false;
			return result;
		}
		
		@Override
		public void remove() {
			if(lastReturned == null) throw new IllegalStateException();
			if(lastReturned == previous) previous = previous.previous();
			if(lastReturned == next) next = next.next();
			if(forwards && lastReturned.needsSuccessor()) next = lastReturned;
			removeNode(lastReturned);
			lastReturned = null;
		}
	}
	
	class DescendingSetIterator implements ObjectBidirectionalIterator<T>
	{
		Entry<T> lastReturned;
		Entry<T> next;
		Entry<T> previous;
		boolean forwards = false;

		public DescendingSetIterator(Entry<T> first)
		{
			next = first;
			previous = first == null ? null : first.next();
		}
		
		@Override
		public boolean hasNext() {
            return next != null;
		}
		
		@Override
		public T next() {
			if(!hasNext()) throw new NoSuchElementException();
			lastReturned = next;
			previous = next;
			T result = next.key;
			next = next.previous();
			forwards = false;
			return result;
		}
		
		@Override
		public boolean hasPrevious() {
            return previous != null;
		}
		
		@Override
		public T previous() {
			if(!hasPrevious()) throw new NoSuchElementException();
			lastReturned = previous;
			next = previous;
			T result = previous.key;
			previous = previous.next();
			forwards = true;
			return result;
		}
		
		@Override
		public void remove() {
			if(lastReturned == null) throw new IllegalStateException();
			if(lastReturned == previous) previous = previous.next();
			if(lastReturned == next) next = next.previous();
			if(forwards && lastReturned.needsSuccessor()) next = lastReturned;
			removeNode(lastReturned);
			lastReturned = null;
		}
	}
	
	private static final class Entry<T>
	{
		T key;
		int state;
		Entry<T> parent;
		Entry<T> left;
		Entry<T> right;
		
		Entry(T key, Entry<T> parent) {
			this.key = key;
			this.parent = parent;
		}
		
		Entry<T> copy() {
			Entry<T> entry = new Entry<>(key, null);
			entry.state = state;
			if(left != null) {
				Entry<T> newLeft = left.copy();
				entry.left = newLeft;
				newLeft.parent = entry;
			}
			if(right != null) {
				Entry<T> newRight = right.copy();
				entry.right = newRight;
				newRight.parent = entry;
			}
			return entry;
		}
		
		int getHeight() { return state; }
		
		void updateHeight() { state = (1 + Math.max(left == null ? -1 : left.getHeight(), right == null ? -1 : right.getHeight())); }
		
		int getBalance() { return (left == null ? -1 : left.getHeight()) - (right == null ? -1 : right.getHeight()); }
		
		boolean needsSuccessor() { return left != null && right != null; }
		
		boolean replace(Entry<T> entry) {
			if(entry != null) entry.parent = parent;
			if(parent != null) {
				if(parent.left == this) parent.left = entry;
				else parent.right = entry;
			}
			return parent == null;
		}
		
		Entry<T> next() {
			if(right != null) {
				Entry<T> parent = right;
				while(parent.left != null) parent = parent.left;
				return parent;
			}
			Entry<T> parent = this.parent;
			Entry<T> control = this;
			while(parent != null && control == parent.right) {
				control = parent;
				parent = parent.parent;
			}
			return parent;
		}
		
		Entry<T> previous() {
			if(left != null) {
				Entry<T> parent = left;
				while(parent.right != null) parent = parent.right;
				return parent;
			}
			Entry<T> parent = this.parent;
			Entry<T> control = this;
			while(parent != null && control == parent.left) {
				control = parent;
				parent = parent.parent;
			}
			return parent;
		}
	}
}