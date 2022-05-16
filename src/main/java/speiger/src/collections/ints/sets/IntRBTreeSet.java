package speiger.src.collections.ints.sets;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import speiger.src.collections.ints.collections.IntBidirectionalIterator;
import speiger.src.collections.ints.functions.IntComparator;
import speiger.src.collections.ints.functions.IntConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectIntConsumer;
import speiger.src.collections.ints.functions.function.Int2BooleanFunction;
import speiger.src.collections.ints.functions.function.IntIntUnaryOperator;
import speiger.src.collections.ints.collections.IntCollection;
import speiger.src.collections.ints.collections.IntIterator;
import speiger.src.collections.ints.utils.IntIterators;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Simple Type Specific RB TreeSet implementation that reduces boxing/unboxing.
 * It is using a bit more memory then <a href="https://github.com/vigna/fastutil">FastUtil</a>,
 * but it saves a lot of Performance on the Optimized removal and iteration logic.
 * Which makes the implementation actually useable and does not get outperformed by Javas default implementation.
 */
public class IntRBTreeSet extends AbstractIntSet implements IntNavigableSet
{
	/** The center of the Tree */
	protected transient Entry tree;
	/** The Lowest possible Node */
	protected transient Entry first;
	/** The Highest possible Node */
	protected transient Entry last;
	/** The amount of elements stored in the Set */
	protected int size = 0;
	/** The Sorter of the Tree */
	protected transient IntComparator comparator;
	/** the default return value for max searches */
	protected int defaultMaxNotFound = Integer.MIN_VALUE;
	/** the default return value for min searches */
	protected int defaultMinNotFound = Integer.MAX_VALUE;
	
	/**
	 * Default Constructor
	 */
	public IntRBTreeSet() {
	}
	
	/**
	 * Constructor that allows to define the sorter
	 * @param comp the function that decides how the tree is sorted, can be null
	 */
	public IntRBTreeSet(IntComparator comp) {
		comparator = comp;
	}
	
	/**
	 * Helper constructor that allow to create a set from an array
	 * @param array the elements that should be used
	 */
	public IntRBTreeSet(int[] array) {
		this(array, 0, array.length);
	}
	
	/**
	 * Helper constructor that allow to create a set from an array
	 * @param array the elements that should be used
	 * @param offset the starting index within the array
	 * @param length the amount of elements that are within the array
	 * @throws IllegalStateException if offset and length causes to step outside of the arrays range
	 */
	public IntRBTreeSet(int[] array, int offset, int length) {
		SanityChecks.checkArrayCapacity(array.length, offset, length);
		for(int i = 0;i<length;i++) add(array[offset+i]);
	}
	
	/**
	 * Helper constructor that allow to create a set from an array
	 * @param array the elements that should be used
	 * @param comp the sorter of the tree, can be null
	 */
	public IntRBTreeSet(int[] array, IntComparator comp) {
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
	public IntRBTreeSet(int[] array, int offset, int length, IntComparator comp) {
		comparator = comp;
		SanityChecks.checkArrayCapacity(array.length, offset, length);
		for(int i = 0;i<length;i++) add(array[offset+i]);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided SortedSet.
	 * @param sortedSet the set the elements should be added to the TreeSet
	 * @note this also includes the Comparator if present
	 */
	public IntRBTreeSet(IntSortedSet sortedSet) {
		comparator = sortedSet.comparator();
		addAll(sortedSet);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param collection the set the elements should be added to the TreeSet
	 */
	@Deprecated
	public IntRBTreeSet(Collection<? extends Integer> collection) {
		addAll(collection);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param collection the set the elements should be added to the TreeSet
	 * @param comp the sorter of the tree, can be null
	 */
	@Deprecated
	public IntRBTreeSet(Collection<? extends Integer> collection, IntComparator comp) {
		comparator = comp;
		addAll(collection);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param collection the set the elements should be added to the TreeSet
	 */
	public IntRBTreeSet(IntCollection collection) {
		addAll(collection);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param collection the set the elements should be added to the TreeSet
	 * @param comp the sorter of the tree, can be null
	 */
	public IntRBTreeSet(IntCollection collection, IntComparator comp) {
		comparator = comp;
		addAll(collection);
	}
	
	/**
	 * A Helper constructor that allows to create a set from a iterator of an unknown size
	 * @param iterator the elements that should be added to the set
	 */
	public IntRBTreeSet(Iterator<Integer> iterator) {
		this(IntIterators.wrap(iterator));
	}
	
	/**
	 * A Helper constructor that allows to create a set from a iterator of an unknown size
	 * @param iterator the elements that should be added to the set
	 * @param comp the sorter of the tree, can be null
	 */
	public IntRBTreeSet(Iterator<Integer> iterator, IntComparator comp) {
		this(IntIterators.wrap(iterator), comp);
	}
	
	/**
	 * A Helper constructor that allows to create a set from a iterator of an unknown size
	 * @param iterator the elements that should be added to the set
	 */
	public IntRBTreeSet(IntIterator iterator) {
		while(iterator.hasNext()) add(iterator.nextInt());
	}
	
	/**
	 * A Helper constructor that allows to create a set from a iterator of an unknown size
	 * @param iterator the elements that should be added to the set
	 * @param comp the sorter of the tree, can be null
	 */
	public IntRBTreeSet(IntIterator iterator, IntComparator comp) {
		comparator = comp;
		while(iterator.hasNext()) add(iterator.nextInt());
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
	public boolean add(int o) {
		if(tree == null) {
			tree = first = last = new Entry(o, null);
			size++;
			return true;
		}
		int compare = 0;
		Entry parent = tree;
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
		Entry adding = new Entry(o, parent);
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
	public int lower(int e) {
		Entry node = findLowerNode(e);
		return node != null ? node.key : getDefaultMinValue();
	}

	@Override
	public int floor(int e) {
		Entry node = findFloorNode(e);
		return node != null ? node.key : getDefaultMinValue();
	}
	
	@Override
	public int higher(int e) {
		Entry node = findHigherNode(e);
		return node != null ? node.key : getDefaultMaxValue();
	}

	@Override
	public int ceiling(int e) {
		Entry node = findCeilingNode(e);
		return node != null ? node.key : getDefaultMaxValue();
	}
	
	@Override
	public Integer lower(Integer e) {
		Entry node = findLowerNode(e);
		return node != null ? node.key : null;
	}
	
	@Override
	public Integer floor(Integer e) {
		Entry node = findFloorNode(e);
		return node != null ? node.key : null;
	}
	
	@Override
	public Integer higher(Integer e) {
		Entry node = findHigherNode(e);
		return node != null ? node.key : null;
	}
	
	@Override
	public Integer ceiling(Integer e) {
		Entry node = findCeilingNode(e);
		return node != null ? node.key : null;
	}
	
	@Override
	public void forEach(IntConsumer action) {
		Objects.requireNonNull(action);
		for(Entry entry = first;entry != null;entry = entry.next()) {
			action.accept(entry.key);
		}
	}
	
	@Override
	public <E> void forEach(E input, ObjectIntConsumer<E> action) {
		Objects.requireNonNull(action);
		for(Entry entry = first;entry != null;entry = entry.next())
			action.accept(input, entry.key);		
	}
	
	@Override
	public boolean matchesAny(Int2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(Entry entry = first;entry != null;entry = entry.next()) {
			if(filter.get(entry.key)) return true;
		}
		return false;
	}
	
	@Override
	public boolean matchesNone(Int2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(Entry entry = first;entry != null;entry = entry.next()) {
			if(filter.get(entry.key)) return false;
		}
		return true;
	}
	
	@Override
	public boolean matchesAll(Int2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(Entry entry = first;entry != null;entry = entry.next()) {
			if(!filter.get(entry.key)) return false;
		}
		return true;
	}
	
	@Override
	public int reduce(int identity, IntIntUnaryOperator operator) {
		Objects.requireNonNull(operator);
		int state = identity;
		for(Entry entry = first;entry != null;entry = entry.next()) {
			state = operator.applyAsInt(state, entry.key);
		}
		return state;
	}
	
	@Override
	public int reduce(IntIntUnaryOperator operator) {
		Objects.requireNonNull(operator);
		int state = 0;
		boolean empty = true;
		for(Entry entry = first;entry != null;entry = entry.next()) {
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
		for(Entry entry = first;entry != null;entry = entry.next()) {
			if(filter.get(entry.key)) return entry.key;
		}
		return 0;
	}
	
	@Override
	public int count(Int2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		int result = 0;
		for(Entry entry = first;entry != null;entry = entry.next()) {
			if(filter.get(entry.key)) result++;
		}
		return result;
	}
	
	protected Entry findNode(int o) {
		Entry node = tree;
		int compare;
		while(node != null) {
			if((compare = compare(o, node.key)) == 0) return node;
			if(compare < 0) node = node.left;
			else node = node.right;
		}
		return null;
	}
	
	protected Entry findLowerNode(int e) {
		Entry entry = tree;
		while(entry != null) {
			if(compare(e, entry.key) > 0) {
				if(entry.right != null) entry = entry.right;
				else return entry;
			}
			else {
				if(entry.left != null) entry = entry.left;
				else {
					Entry parent = entry.parent;
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
	
	protected Entry findFloorNode(int e) {
		Entry entry = tree;
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
					Entry parent = entry.parent;
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
	
	protected Entry findCeilingNode(int e) {
		Entry entry = tree;
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
					Entry parent = entry.parent;
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
	
	protected Entry findHigherNode(int e) {
		Entry entry = tree;
		while(entry != null) {
			if(compare(e, entry.key) < 0) {
				if(entry.left != null) entry = entry.left;
				else return entry;
			}
			else {
				if(entry.right != null) entry = entry.right;
				else {
					Entry parent = entry.parent;
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
	public boolean contains(int e) {
		return findNode(e) != null;
	}
	
	@Override
	public boolean contains(Object e) {
		return findNode(((Integer)e).intValue()) != null;
	}
	
	@Override
	public int firstInt() {
		if(tree == null) throw new NoSuchElementException();
		return first.key;
	}
	
	@Override
	public int lastInt() {
		if(tree == null) throw new NoSuchElementException();
		return last.key;
	}
	
	@Override
	public boolean remove(int o) {
		if(tree == null) return false;
		Entry entry = findNode(o);
		if(entry != null) {
			removeNode(entry);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean remove(Object o) {
		if(tree == null) return false;
		Entry entry = findNode(((Integer)o).intValue());
		if(entry != null) {
			removeNode(entry);
			return true;
		}
		return false;
	}
	
	@Override
	public int pollFirstInt() {
		if(tree == null) return getDefaultMinValue();
		int value = first.key;
		removeNode(first);
		return value;
	}
	
	@Override
	public int pollLastInt() {
		if(tree == null) return getDefaultMaxValue();
		int value = last.key;
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
	
	public IntRBTreeSet copy() {
		IntRBTreeSet set = new IntRBTreeSet();
		set.size = size;
		if(tree != null) {
			set.tree = tree.copy();
			Entry lastFound = null;
			for(Entry entry = tree;entry != null;entry = entry.left) lastFound = entry;
			set.first = lastFound;
			lastFound = null;
			for(Entry entry = tree;entry != null;entry = entry.right) lastFound = entry;
			set.last = lastFound;
		}
		return set;
	}
	
	@Override
	public IntComparator comparator() { return comparator; }
	
	@Override
	public IntBidirectionalIterator iterator() { return new AscendingSetIterator(first); }
	
	@Override
	public IntBidirectionalIterator iterator(int fromElement) {
		Entry entry = findNode(fromElement);
		return entry == null ? null : new AscendingSetIterator(entry);
	}
	
	@Override
	public IntBidirectionalIterator descendingIterator() { return new DescendingSetIterator(last); }
	
	@Override
	public IntNavigableSet subSet(int fromElement, boolean fromInclusive, int toElement, boolean toInclusive) {
		return new AscendingSubSet(this, false, fromElement, fromInclusive, false, toElement, toInclusive);
	}

	@Override
	public IntNavigableSet headSet(int toElement, boolean inclusive) {
		return new AscendingSubSet(this, true, 0, true, false, toElement, inclusive);
	}

	@Override
	public IntNavigableSet tailSet(int fromElement, boolean inclusive) {
		return new AscendingSubSet(this, false, fromElement, inclusive, true, 0, true);
	}
	
	@Override
	public IntNavigableSet descendingSet() {
		return new DescendingSubSet(this, true, 0, true, true, 0, true);
	}
	
	protected void removeNode(Entry entry) {
		size--;
		if(entry.needsSuccessor()) {
			Entry successor = entry.next();
			entry.key = successor.key;
			entry = successor;
		}
		Entry replacement = entry.left != null ? entry.left : entry.right;
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
				Entry parent = entry.parent;
				if(entry == first) first = parent.left != null ? parent.left : parent;
				if(entry == last) last = entry.right != null ? parent.right : parent;
			}
			entry.parent = null;
		}
	}
	
	protected void validate(int k) { compare(k, k); }
	protected int compare(int k, int v) { return comparator != null ? comparator.compare(k, v) : Integer.compare(k, v);}
	protected static boolean isBlack(Entry p) { return p == null || p.isBlack(); }
	protected static Entry parentOf(Entry p) { return (p == null ? null : p.parent); }
	protected static void setBlack(Entry p, boolean c) { if(p != null) p.setBlack(c); }
	protected static Entry leftOf(Entry p) { return p == null ? null : p.left; }
	protected static Entry rightOf(Entry p) { return (p == null) ? null : p.right; }
	
	/** From CLR */
	protected void rotateLeft(Entry entry) {
		if(entry != null) {
			Entry right = entry.right;
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
	
	/** From CLR */
	protected void rotateRight(Entry entry) {
		if(entry != null) {
			Entry left = entry.left;
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
	
	/** From CLR */
	protected void fixAfterInsertion(Entry entry) {
		entry.setBlack(false);
		while(entry != null && entry != tree && !entry.parent.isBlack()) {
			if(parentOf(entry) == leftOf(parentOf(parentOf(entry)))) {
				Entry y = rightOf(parentOf(parentOf(entry)));
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
				Entry y = leftOf(parentOf(parentOf(entry)));
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
	
	/** From CLR */
	protected void fixAfterDeletion(Entry entry) {
		while(entry != tree && isBlack(entry)) {
			if(entry == leftOf(parentOf(entry))) {
				Entry sib = rightOf(parentOf(entry));
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
				Entry sib = leftOf(parentOf(entry));
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
	
	static class AscendingSubSet extends SubSet
	{
		public AscendingSubSet(IntRBTreeSet set, boolean fromStart, int lo, boolean loInclusive, boolean toEnd, int hi, boolean hiInclusive)
		{
			super(set, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
		}
		
		@Override
		public IntComparator comparator() { return set.comparator(); }
		
		@Override
		public IntNavigableSet subSet(int fromElement, boolean fromInclusive, int toElement, boolean toInclusive)
		{
			if(!inRange(fromElement, fromInclusive)) throw new IllegalArgumentException("fromElement out of range");
			if(!inRange(toElement, toInclusive)) throw new IllegalArgumentException("toElement out of range");
			return new AscendingSubSet(set, false, fromElement, fromInclusive, false, toElement, toInclusive);
		}
		
		@Override
		public IntNavigableSet headSet(int toElement, boolean inclusive)
		{
			if(!inRange(toElement, inclusive)) throw new IllegalArgumentException("toElement out of range");
			return new AscendingSubSet(set, fromStart, lo, loInclusive, false, toElement, inclusive);
		}

		@Override
		public IntNavigableSet tailSet(int fromElement, boolean inclusive)
		{
			if(!inRange(fromElement, inclusive)) throw new IllegalArgumentException("fromElement out of range");
			return new AscendingSubSet(set, false, fromElement, inclusive, toEnd, hi, hiInclusive);
		}
		
		@Override
		public IntBidirectionalIterator iterator()
		{
			return new AscendingSubSetIterator(absLowest(), absHighFence(), absLowFence());
		}
		
		@Override
		public IntBidirectionalIterator iterator(int fromElement)
		{
			return new AscendingSubSetIterator(absLower(fromElement), absHighFence(), absLowFence());
		}
		
		@Override
		public IntBidirectionalIterator descendingIterator()
		{
			return new DescendingSubSetIterator(absHighest(), absLowFence(), absHighFence());
		}

		@Override
		public IntNavigableSet descendingSet()
		{
			if(inverse == null) inverse = new DescendingSubSet(set, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
			return inverse;
		}
		
		@Override
		protected Entry subLowest() { return absLowest(); }
		@Override
		protected Entry subHighest() { return absHighest(); }
		@Override
		protected Entry subCeiling(int key) { return absCeiling(key); }
		@Override
		protected Entry subHigher(int key) { return absHigher(key); }
		@Override
		protected Entry subFloor(int key) { return absFloor(key); }
		@Override
		protected Entry subLower(int key) { return absLower(key); }
		@Override
		protected Entry start() { return absLowest(); }
		@Override
		protected Entry next(Entry entry) { return entry.next(); }
	}
	
	static class DescendingSubSet extends SubSet
	{
		IntComparator comparator;
		
		public DescendingSubSet(IntRBTreeSet set, boolean fromStart, int lo, boolean loInclusive, boolean toEnd, int hi, boolean hiInclusive)
		{
			super(set, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
			comparator = set.comparator() == null ? IntComparator.of(Collections.reverseOrder()) : set.comparator().reversed();
		}
		
		@Override
		public IntComparator comparator() { return comparator; }
		
		public int getDefaultMaxValue() { return super.getDefaultMinValue(); }
		
		public int getDefaultMinValue() { return super.getDefaultMaxValue(); }
		
		@Override
		public IntNavigableSet subSet(int fromElement, boolean fromInclusive, int toElement, boolean toInclusive) {
			if(!inRange(fromElement, fromInclusive)) throw new IllegalArgumentException("fromElement out of range");
			if(!inRange(toElement, toInclusive)) throw new IllegalArgumentException("toElement out of range");
			return new DescendingSubSet(set, false, fromElement, fromInclusive, false, toElement, toInclusive);
		}
		
		@Override
		public IntNavigableSet headSet(int toElement, boolean inclusive)
		{
			if(!inRange(toElement, inclusive)) throw new IllegalArgumentException("toElement out of range");
			return new DescendingSubSet(set, false, toElement, inclusive, toEnd, hi, hiInclusive);
		}

		@Override
		public IntNavigableSet tailSet(int fromElement, boolean inclusive) {
			if(!inRange(fromElement, inclusive)) throw new IllegalArgumentException("fromElement out of range");
			return new DescendingSubSet(set, fromStart, lo, loInclusive, false, fromElement, inclusive);
		}

		@Override
		public IntBidirectionalIterator iterator() {
			return new DescendingSubSetIterator(absHighest(), absLowFence(), absHighFence());
		}

		@Override
		public IntBidirectionalIterator descendingIterator() {
			return new AscendingSubSetIterator(absLowest(), absHighFence(), absLowFence());
		}

		@Override
		public IntNavigableSet descendingSet() {
			if(inverse == null) inverse = new AscendingSubSet(set, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
			return inverse;
		}
		
		@Override
		public IntBidirectionalIterator iterator(int fromElement) {
			return new DescendingSubSetIterator(absHigher(fromElement), absLowFence(), absHighFence());
		}
		
		@Override
		protected Entry subLowest() { return absHighest(); }
		@Override
		protected Entry subHighest() { return absLowest(); }
		@Override
		protected Entry subCeiling(int key) { return absFloor(key); }
		@Override
		protected Entry subHigher(int key) { return absLower(key); }
		@Override
		protected Entry subFloor(int key) { return absCeiling(key); }
		@Override
		protected Entry subLower(int key) { return absHigher(key); }
		@Override
		protected Entry start() { return absHighest(); }
		@Override
		protected Entry next(Entry entry) { return entry.previous(); }
	}
	
	static abstract class SubSet extends AbstractIntSet implements IntNavigableSet
	{
		final IntRBTreeSet set;
		final int lo, hi;
		final boolean fromStart, toEnd;
		final boolean loInclusive, hiInclusive;
		IntNavigableSet inverse;
		
		public SubSet(IntRBTreeSet set, boolean fromStart, int lo, boolean loInclusive, boolean toEnd, int hi, boolean hiInclusive)
		{
			this.set = set;
			this.lo = lo;
			this.hi = hi;
			this.fromStart = fromStart;
			this.toEnd = toEnd;
			this.loInclusive = loInclusive;
			this.hiInclusive = hiInclusive;
		}
		
		@Override
		public void setDefaultMaxValue(int value) { set.setDefaultMaxValue(value); }
		
		@Override
		public int getDefaultMaxValue() { return set.getDefaultMaxValue(); }
		
		@Override
		public void setDefaultMinValue(int value) { set.setDefaultMinValue(value); }
		
		@Override
		public int getDefaultMinValue() { return set.getDefaultMinValue(); }
		
		@Override
		public abstract	IntBidirectionalIterator iterator();
		
		boolean tooLow(int key) {
			if (!fromStart) {
				int c = set.compare(key, lo);
				if (c < 0 || (c == 0 && !loInclusive)) return true;
			}
			return false;
		}
		
		boolean tooHigh(int key) {
			if (!toEnd) {
				int c = set.compare(key, hi);
				if (c > 0 || (c == 0 && !hiInclusive)) return true;
			}
			return false;
		}
		
		boolean inRange(int key) { return !tooLow(key) && !tooHigh(key); }
		boolean inClosedRange(int key) { return (fromStart || set.compare(key, lo) >= 0) && (toEnd || set.compare(hi, key) >= 0); }
		boolean inRange(int key, boolean inclusive) { return inclusive ? inRange(key) : inClosedRange(key); }
		
		protected abstract Entry subLowest();
		protected abstract Entry subHighest();
		protected abstract Entry subCeiling(int key);
		protected abstract Entry subHigher(int key);
		protected abstract Entry subFloor(int key);
		protected abstract Entry subLower(int key);
		protected int lowKeyOrNull(Entry entry) { return entry == null ? getDefaultMinValue() : entry.key; }
		protected int highKeyOrNull(Entry entry) { return entry == null ? getDefaultMaxValue() : entry.key; }
		protected abstract Entry start();
		protected abstract Entry next(Entry entry);
		
		final Entry absLowest() {
			Entry e = (fromStart ? set.first : (loInclusive ? set.findCeilingNode(lo) : set.findHigherNode(lo)));
			return (e == null || tooHigh(e.key)) ? null : e;
		}
		
		final Entry absHighest() {
			Entry e = (toEnd ? set.last : (hiInclusive ? set.findFloorNode(hi) : set.findLowerNode(hi)));
			return (e == null || tooLow(e.key)) ? null : e;
		}
		
		final Entry absCeiling(int key) {
			if (tooLow(key)) return absLowest();
			Entry e = set.findCeilingNode(key);
			return (e == null || tooHigh(e.key)) ? null : e;
		}
		
		final Entry absHigher(int key) {
			if (tooLow(key)) return absLowest();
			Entry e = set.findHigherNode(key);
			return (e == null || tooHigh(e.key)) ? null : e;
		}
		
		final Entry absFloor(int key) {
			if (tooHigh(key)) return absHighest();
			Entry e = set.findFloorNode(key);
			return (e == null || tooLow(e.key)) ? null : e;
		}
		
		final Entry absLower(int key) {
			if (tooHigh(key)) return absHighest();
			Entry e = set.findLowerNode(key);
			return (e == null || tooLow(e.key)) ? null : e;
		}
		
		final Entry absHighFence() { return (toEnd ? null : (hiInclusive ? set.findHigherNode(hi) : set.findCeilingNode(hi))); }
		final Entry absLowFence() { return (fromStart ? null : (loInclusive ? set.findLowerNode(lo) : set.findFloorNode(lo))); }
		
		@Override
		public boolean add(int o) {
			if(!inRange(o)) throw new IllegalArgumentException("Key is out of range");
			return set.add(o);
		}
		
		@Override
		public boolean contains(int e) {
			return inRange(e) && set.contains(e);
		}
		
		@Override
		public boolean remove(int o) {
			return inRange(o) && set.remove(o);
		}
		
		@Override
		public boolean contains(Object e) {
			int o = ((Integer)e).intValue();
			return inRange(o) && set.contains(o);
		}
	
		@Override
		public boolean remove(Object e) {
			int o = ((Integer)e).intValue();
			return inRange(o) && set.remove(o);
		}
		
		@Override
		public boolean isEmpty() {
			if(fromStart && toEnd) return set.isEmpty();
            Entry n = absLowest();
            return n == null || tooHigh(n.key);
		}
		
		@Override
		public int size() {
			if(fromStart && toEnd) return set.size();
			int i = 0;
			for(IntIterator iter = iterator();iter.hasNext();iter.nextInt(),i++);
			return i;
		}
		
		@Override
		public int lower(int e) {
			return lowKeyOrNull(subLower(e));
		}
		
		@Override
		public int floor(int e) {
			return lowKeyOrNull(subFloor(e));
		}
		
		@Override
		public int ceiling(int e) {
			return highKeyOrNull(subCeiling(e));
		}
		
		@Override
		public int higher(int e) {
			return highKeyOrNull(subHigher(e));
		}
		
		@Override
		@Deprecated
		public Integer lower(Integer e) {
			Entry entry = subLower(e);
			return entry == null ? null : Integer.valueOf(entry.key);
		}
		
		@Override
		@Deprecated
		public Integer floor(Integer e) {
			Entry entry = subFloor(e);
			return entry == null ? null : Integer.valueOf(entry.key);
		}
		
		@Override
		@Deprecated
		public Integer ceiling(Integer e) {
			Entry entry = subCeiling(e);
			return entry == null ? null : Integer.valueOf(entry.key);
		}
		
		@Override
		@Deprecated
		public Integer higher(Integer e) {
			Entry entry = subHigher(e);
			return entry == null ? null : Integer.valueOf(entry.key);
		}
		
		@Override
		public int pollFirstInt() {
			Entry entry = subLowest();
			if(entry != null) {
				int result = entry.key;
				set.removeNode(entry);
				return result;
			}
			return getDefaultMinValue();
		}
		
		@Override
		public int pollLastInt() {
			Entry entry = subHighest();
			if(entry != null) {
				int result = entry.key;
				set.removeNode(entry);
				return result;
			}
			return getDefaultMaxValue();
		}
		
		@Override
		public int firstInt() {
			Entry entry = subLowest();
			if(entry == null) throw new NoSuchElementException();
			return entry.key;
		}
		
		@Override
		public int lastInt() {
			Entry entry = subHighest();
			if(entry == null) throw new NoSuchElementException();
			return entry.key;
		}
		
		@Override
		public SubSet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(IntConsumer action) {
			Objects.requireNonNull(action);
			for(Entry entry = start();entry != null && inRange(entry.key);entry = next(entry)) {
				action.accept(entry.key);
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectIntConsumer<E> action) {
			Objects.requireNonNull(action);
			for(Entry entry = start();entry != null && inRange(entry.key);entry = next(entry))
				action.accept(input, entry.key);		
		}
		
		@Override
		public boolean matchesAny(Int2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(Entry entry = start();entry != null && inRange(entry.key);entry = next(entry)) {
				if(filter.get(entry.key)) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Int2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(Entry entry = start();entry != null && inRange(entry.key);entry = next(entry)) {
				if(filter.get(entry.key)) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Int2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(Entry entry = start();entry != null && inRange(entry.key);entry = next(entry)) {
				if(!filter.get(entry.key)) return false;
			}
			return true;
		}
		
		@Override
		public int reduce(int identity, IntIntUnaryOperator operator) {
			Objects.requireNonNull(operator);
			int state = identity;
			for(Entry entry = start();entry != null && inRange(entry.key);entry = next(entry)) {
				state = operator.applyAsInt(state, entry.key);
			}
			return state;
		}
		
		@Override
		public int reduce(IntIntUnaryOperator operator) {
			Objects.requireNonNull(operator);
			int state = 0;
			boolean empty = true;
			for(Entry entry = start();entry != null && inRange(entry.key);entry = next(entry)) {
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
			for(Entry entry = start();entry != null && inRange(entry.key);entry = next(entry)) {
				if(filter.get(entry.key)) return entry.key;
			}
			return 0;
		}
		
		@Override
		public int count(Int2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			int result = 0;
			for(Entry entry = start();entry != null && inRange(entry.key);entry = next(entry)) {
				if(filter.get(entry.key)) result++;
			}
			return result;
		}
		
		class AscendingSubSetIterator implements IntBidirectionalIterator
		{
			Entry lastReturned;
			Entry next;
			boolean forwards = false;
			boolean unboundForwardFence;
			boolean unboundBackwardFence;
			int forwardFence;
			int backwardFence;
			
			public AscendingSubSetIterator(Entry first, Entry forwardFence, Entry backwardFence)
			{
				next = first;
				this.forwardFence = forwardFence == null ? 0 : forwardFence.key;
				this.backwardFence = backwardFence == null ? 0 : backwardFence.key;
				unboundForwardFence = forwardFence == null;
				unboundBackwardFence = backwardFence == null;
			}
			
			@Override
			public boolean hasNext() {
                return next != null && (unboundForwardFence || next.key != forwardFence);
			}
			
			@Override
			public int nextInt() {
				if(!hasNext()) throw new NoSuchElementException();
				lastReturned = next;
				int result = next.key;
				next = next.next();
				forwards = true;
				return result;
			}
			
			@Override
			public boolean hasPrevious() {
                return next != null && (unboundBackwardFence || next.key != backwardFence);
			}
			
			@Override
			public int previousInt() {
				if(!hasPrevious()) throw new NoSuchElementException();
				lastReturned = next;
				int result = next.key;
				next = next.previous();
				forwards = false;
				return result;
			}
			
			@Override
			public void remove() {
				if(lastReturned == null) throw new IllegalStateException();
				if(forwards && lastReturned.needsSuccessor()) next = lastReturned;
				set.removeNode(lastReturned);
				lastReturned = null;
			}
		}
		
		class DescendingSubSetIterator implements IntBidirectionalIterator
		{
			Entry lastReturned;
			Entry next;
			boolean forwards = false;
			boolean unboundForwardFence;
			boolean unboundBackwardFence;
			int forwardFence;
			int backwardFence;
			
			public DescendingSubSetIterator(Entry first, Entry forwardFence, Entry backwardFence)
			{
				next = first;
				this.forwardFence = forwardFence == null ? 0 : forwardFence.key;
				this.backwardFence = backwardFence == null ? 0 : backwardFence.key;
				unboundForwardFence = forwardFence == null;
				unboundBackwardFence = backwardFence == null;
			}
			
			@Override
			public boolean hasNext() {
                return next != null && (unboundForwardFence || next.key != forwardFence);
			}
			
			@Override
			public int nextInt() {
				if(!hasNext()) throw new NoSuchElementException();
				lastReturned = next;
				int result = next.key;
				next = next.previous();
				forwards = false;
				return result;
			}
			
			@Override
			public boolean hasPrevious() {
                return next != null && (unboundBackwardFence || next.key != backwardFence);
			}
			
			@Override
			public int previousInt() {
				if(!hasPrevious()) throw new NoSuchElementException();
				lastReturned = next;
				int result = next.key;
				next = next.next();
				forwards = true;
				return result;
			}
			
			@Override
			public void remove() {
				if(lastReturned == null) throw new IllegalStateException();
				if(forwards && lastReturned.needsSuccessor()) next = lastReturned;
				set.removeNode(lastReturned);
				lastReturned = null;
			}
		}
	}
	
	class AscendingSetIterator implements IntBidirectionalIterator
	{
		Entry lastReturned;
		Entry next;
		boolean forwards = false;
		
		public AscendingSetIterator(Entry first)
		{
			next = first;
		}
		
		@Override
		public boolean hasNext() {
            return next != null;
		}
		
		@Override
		public int nextInt() {
			if(!hasNext()) throw new NoSuchElementException();
			lastReturned = next;
			int result = next.key;
			next = next.next();
			forwards = true;
			return result;
		}
		
		@Override
		public boolean hasPrevious() {
            return next != null;
		}
		
		@Override
		public int previousInt() {
			if(!hasPrevious()) throw new NoSuchElementException();
			lastReturned = next;
			int result = next.key;
			next = next.previous();
			forwards = false;
			return result;
		}
		
		@Override
		public void remove() {
			if(lastReturned == null) throw new IllegalStateException();
			if(forwards && lastReturned.needsSuccessor()) next = lastReturned;
			removeNode(lastReturned);
			lastReturned = null;
		}
	}
	
	class DescendingSetIterator implements IntBidirectionalIterator
	{
		Entry lastReturned;
		Entry next;
		boolean forwards = false;

		public DescendingSetIterator(Entry first)
		{
			next = first;
		}
		
		@Override
		public boolean hasNext() {
            return next != null;
		}
		
		@Override
		public int nextInt() {
			if(!hasNext()) throw new NoSuchElementException();
			lastReturned = next;
			int result = next.key;
			next = next.previous();
			forwards = false;
			return result;
		}
		
		@Override
		public boolean hasPrevious() {
            return next != null;
		}
		
		@Override
		public int previousInt() {
			if(!hasPrevious()) throw new NoSuchElementException();
			lastReturned = next;
			int result = next.key;
			next = next.next();
			forwards = true;
			return result;
		}
		
		@Override
		public void remove() {
			if(lastReturned == null) throw new IllegalStateException();
			if(forwards && lastReturned.needsSuccessor()) next = lastReturned;
			removeNode(lastReturned);
			lastReturned = null;
		}
	}
	
	private static final class Entry
	{
		static final int BLACK = 1;
		
		int key;
		int state;
		Entry parent;
		Entry left;
		Entry right;
		
		Entry(int key, Entry parent) {
			this.key = key;
			this.parent = parent;
		}
		
		Entry copy() {
			Entry entry = new Entry(key, null);
			entry.state = state;
			if(left != null) {
				Entry newLeft = left.copy();
				entry.left = newLeft;
				newLeft.parent = entry;
			}
			if(right != null) {
				Entry newRight = right.copy();
				entry.right = newRight;
				newRight.parent = entry;
			}
			return entry;
		}
		
		boolean isBlack() {
			return (state & BLACK) != 0;
		}
		
		void setBlack(boolean value) {
			if(value) state |= BLACK;
			else state &= ~BLACK;
		}
		
		boolean needsSuccessor() { return left != null && right != null; }
		
		boolean replace(Entry entry) {
			if(entry != null) entry.parent = parent;
			if(parent != null) {
				if(parent.left == this) parent.left = entry;
				else parent.right = entry;
			}
			return parent == null;
		}
		
		Entry next() {
			if(right != null) {
				Entry parent = right;
				while(parent.left != null) parent = parent.left;
				return parent;
			}
			Entry parent = this.parent;
			Entry control = this;
			while(parent != null && control == parent.right) {
				control = parent;
				parent = parent.parent;
			}
			return parent;
		}
		
		Entry previous() {
			if(left != null) {
				Entry parent = left;
				while(parent.right != null) parent = parent.right;
				return parent;
			}
			Entry parent = this.parent;
			Entry control = this;
			while(parent != null && control == parent.left) {
				control = parent;
				parent = parent.parent;
			}
			return parent;
		}
	}
}