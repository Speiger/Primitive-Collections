package speiger.src.collections.longs.sets;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import speiger.src.collections.longs.collections.LongBidirectionalIterator;
import speiger.src.collections.longs.functions.LongComparator;
import speiger.src.collections.longs.functions.LongConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectLongConsumer;
import speiger.src.collections.longs.functions.function.Long2BooleanFunction;
import speiger.src.collections.longs.functions.function.LongLongUnaryOperator;
import speiger.src.collections.longs.collections.LongCollection;
import speiger.src.collections.longs.collections.LongIterator;
import speiger.src.collections.longs.utils.LongIterators;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Simple Type Specific RB TreeSet implementation that reduces boxing/unboxing.
 * It is using a bit more memory then <a href="https://github.com/vigna/fastutil">FastUtil</a>,
 * but it saves a lot of Performance on the Optimized removal and iteration logic.
 * Which makes the implementation actually useable and does not get outperformed by Javas default implementation.
 */
public class LongRBTreeSet extends AbstractLongSet implements LongNavigableSet
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
	protected transient LongComparator comparator;
	/** the default return value for max searches */
	protected long defaultMaxNotFound = Long.MIN_VALUE;
	/** the default return value for min searches */
	protected long defaultMinNotFound = Long.MAX_VALUE;
	
	/**
	 * Default Constructor
	 */
	public LongRBTreeSet() {
	}
	
	/**
	 * Constructor that allows to define the sorter
	 * @param comp the function that decides how the tree is sorted, can be null
	 */
	public LongRBTreeSet(LongComparator comp) {
		comparator = comp;
	}
	
	/**
	 * Helper constructor that allow to create a set from an array
	 * @param array the elements that should be used
	 */
	public LongRBTreeSet(long[] array) {
		this(array, 0, array.length);
	}
	
	/**
	 * Helper constructor that allow to create a set from an array
	 * @param array the elements that should be used
	 * @param offset the starting index within the array
	 * @param length the amount of elements that are within the array
	 * @throws IllegalStateException if offset and length causes to step outside of the arrays range
	 */
	public LongRBTreeSet(long[] array, int offset, int length) {
		SanityChecks.checkArrayCapacity(array.length, offset, length);
		for(int i = 0;i<length;i++) add(array[offset+i]);
	}
	
	/**
	 * Helper constructor that allow to create a set from an array
	 * @param array the elements that should be used
	 * @param comp the sorter of the tree, can be null
	 */
	public LongRBTreeSet(long[] array, LongComparator comp) {
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
	public LongRBTreeSet(long[] array, int offset, int length, LongComparator comp) {
		comparator = comp;
		SanityChecks.checkArrayCapacity(array.length, offset, length);
		for(int i = 0;i<length;i++) add(array[offset+i]);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided SortedSet.
	 * @param sortedSet the set the elements should be added to the TreeSet
	 * @note this also includes the Comparator if present
	 */
	public LongRBTreeSet(LongSortedSet sortedSet) {
		comparator = sortedSet.comparator();
		addAll(sortedSet);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param collection the set the elements should be added to the TreeSet
	 */
	@Deprecated
	public LongRBTreeSet(Collection<? extends Long> collection) {
		addAll(collection);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param collection the set the elements should be added to the TreeSet
	 * @param comp the sorter of the tree, can be null
	 */
	@Deprecated
	public LongRBTreeSet(Collection<? extends Long> collection, LongComparator comp) {
		comparator = comp;
		addAll(collection);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param collection the set the elements should be added to the TreeSet
	 */
	public LongRBTreeSet(LongCollection collection) {
		addAll(collection);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param collection the set the elements should be added to the TreeSet
	 * @param comp the sorter of the tree, can be null
	 */
	public LongRBTreeSet(LongCollection collection, LongComparator comp) {
		comparator = comp;
		addAll(collection);
	}
	
	/**
	 * A Helper constructor that allows to create a set from a iterator of an unknown size
	 * @param iterator the elements that should be added to the set
	 */
	public LongRBTreeSet(Iterator<Long> iterator) {
		this(LongIterators.wrap(iterator));
	}
	
	/**
	 * A Helper constructor that allows to create a set from a iterator of an unknown size
	 * @param iterator the elements that should be added to the set
	 * @param comp the sorter of the tree, can be null
	 */
	public LongRBTreeSet(Iterator<Long> iterator, LongComparator comp) {
		this(LongIterators.wrap(iterator), comp);
	}
	
	/**
	 * A Helper constructor that allows to create a set from a iterator of an unknown size
	 * @param iterator the elements that should be added to the set
	 */
	public LongRBTreeSet(LongIterator iterator) {
		while(iterator.hasNext()) add(iterator.nextLong());
	}
	
	/**
	 * A Helper constructor that allows to create a set from a iterator of an unknown size
	 * @param iterator the elements that should be added to the set
	 * @param comp the sorter of the tree, can be null
	 */
	public LongRBTreeSet(LongIterator iterator, LongComparator comp) {
		comparator = comp;
		while(iterator.hasNext()) add(iterator.nextLong());
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
	public boolean add(long o) {
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
	public long lower(long e) {
		Entry node = findLowerNode(e);
		return node != null ? node.key : getDefaultMinValue();
	}

	@Override
	public long floor(long e) {
		Entry node = findFloorNode(e);
		return node != null ? node.key : getDefaultMinValue();
	}
	
	@Override
	public long higher(long e) {
		Entry node = findHigherNode(e);
		return node != null ? node.key : getDefaultMaxValue();
	}

	@Override
	public long ceiling(long e) {
		Entry node = findCeilingNode(e);
		return node != null ? node.key : getDefaultMaxValue();
	}
	
	@Override
	public Long lower(Long e) {
		Entry node = findLowerNode(e);
		return node != null ? node.key : null;
	}
	
	@Override
	public Long floor(Long e) {
		Entry node = findFloorNode(e);
		return node != null ? node.key : null;
	}
	
	@Override
	public Long higher(Long e) {
		Entry node = findHigherNode(e);
		return node != null ? node.key : null;
	}
	
	@Override
	public Long ceiling(Long e) {
		Entry node = findCeilingNode(e);
		return node != null ? node.key : null;
	}
	
	@Override
	public void forEach(LongConsumer action) {
		Objects.requireNonNull(action);
		for(Entry entry = first;entry != null;entry = entry.next()) {
			action.accept(entry.key);
		}
	}
	
	@Override
	public <E> void forEach(E input, ObjectLongConsumer<E> action) {
		Objects.requireNonNull(action);
		for(Entry entry = first;entry != null;entry = entry.next())
			action.accept(input, entry.key);		
	}
	
	@Override
	public boolean matchesAny(Long2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(Entry entry = first;entry != null;entry = entry.next()) {
			if(filter.get(entry.key)) return true;
		}
		return false;
	}
	
	@Override
	public boolean matchesNone(Long2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(Entry entry = first;entry != null;entry = entry.next()) {
			if(filter.get(entry.key)) return false;
		}
		return true;
	}
	
	@Override
	public boolean matchesAll(Long2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(Entry entry = first;entry != null;entry = entry.next()) {
			if(!filter.get(entry.key)) return false;
		}
		return true;
	}
	
	@Override
	public long reduce(long identity, LongLongUnaryOperator operator) {
		Objects.requireNonNull(operator);
		long state = identity;
		for(Entry entry = first;entry != null;entry = entry.next()) {
			state = operator.applyAsLong(state, entry.key);
		}
		return state;
	}
	
	@Override
	public long reduce(LongLongUnaryOperator operator) {
		Objects.requireNonNull(operator);
		long state = 0L;
		boolean empty = true;
		for(Entry entry = first;entry != null;entry = entry.next()) {
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
		for(Entry entry = first;entry != null;entry = entry.next()) {
			if(filter.get(entry.key)) return entry.key;
		}
		return 0L;
	}
	
	@Override
	public int count(Long2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		int result = 0;
		for(Entry entry = first;entry != null;entry = entry.next()) {
			if(filter.get(entry.key)) result++;
		}
		return result;
	}
	
	protected Entry findNode(long o) {
		Entry node = tree;
		int compare;
		while(node != null) {
			if((compare = compare(o, node.key)) == 0) return node;
			if(compare < 0) node = node.left;
			else node = node.right;
		}
		return null;
	}
	
	protected Entry findLowerNode(long e) {
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
	
	protected Entry findFloorNode(long e) {
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
	
	protected Entry findCeilingNode(long e) {
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
	
	protected Entry findHigherNode(long e) {
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
	public boolean contains(long e) {
		return findNode(e) != null;
	}
	
	@Override
	public boolean contains(Object e) {
		return findNode(((Long)e).longValue()) != null;
	}
	
	@Override
	public long firstLong() {
		if(tree == null) throw new NoSuchElementException();
		return first.key;
	}
	
	@Override
	public long lastLong() {
		if(tree == null) throw new NoSuchElementException();
		return last.key;
	}
	
	@Override
	public boolean remove(long o) {
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
		Entry entry = findNode(((Long)o).longValue());
		if(entry != null) {
			removeNode(entry);
			return true;
		}
		return false;
	}
	
	@Override
	public long pollFirstLong() {
		if(tree == null) return getDefaultMinValue();
		long value = first.key;
		removeNode(first);
		return value;
	}
	
	@Override
	public long pollLastLong() {
		if(tree == null) return getDefaultMaxValue();
		long value = last.key;
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
	
	public LongRBTreeSet copy() {
		LongRBTreeSet set = new LongRBTreeSet();
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
	public LongComparator comparator() { return comparator; }
	
	@Override
	public LongBidirectionalIterator iterator() { return new AscendingSetIterator(first); }
	
	@Override
	public LongBidirectionalIterator iterator(long fromElement) {
		Entry entry = findNode(fromElement);
		return entry == null ? null : new AscendingSetIterator(entry);
	}
	
	@Override
	public LongBidirectionalIterator descendingIterator() { return new DescendingSetIterator(last); }
	
	@Override
	public LongNavigableSet subSet(long fromElement, boolean fromInclusive, long toElement, boolean toInclusive) {
		return new AscendingSubSet(this, false, fromElement, fromInclusive, false, toElement, toInclusive);
	}

	@Override
	public LongNavigableSet headSet(long toElement, boolean inclusive) {
		return new AscendingSubSet(this, true, 0L, true, false, toElement, inclusive);
	}

	@Override
	public LongNavigableSet tailSet(long fromElement, boolean inclusive) {
		return new AscendingSubSet(this, false, fromElement, inclusive, true, 0L, true);
	}
	
	@Override
	public LongNavigableSet descendingSet() {
		return new DescendingSubSet(this, true, 0L, true, true, 0L, true);
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
	
	protected void validate(long k) { compare(k, k); }
	protected int compare(long k, long v) { return comparator != null ? comparator.compare(k, v) : Long.compare(k, v);}
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
		public AscendingSubSet(LongRBTreeSet set, boolean fromStart, long lo, boolean loInclusive, boolean toEnd, long hi, boolean hiInclusive)
		{
			super(set, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
		}
		
		@Override
		public LongComparator comparator() { return set.comparator(); }
		
		@Override
		public LongNavigableSet subSet(long fromElement, boolean fromInclusive, long toElement, boolean toInclusive)
		{
			if(!inRange(fromElement, fromInclusive)) throw new IllegalArgumentException("fromElement out of range");
			if(!inRange(toElement, toInclusive)) throw new IllegalArgumentException("toElement out of range");
			return new AscendingSubSet(set, false, fromElement, fromInclusive, false, toElement, toInclusive);
		}
		
		@Override
		public LongNavigableSet headSet(long toElement, boolean inclusive)
		{
			if(!inRange(toElement, inclusive)) throw new IllegalArgumentException("toElement out of range");
			return new AscendingSubSet(set, fromStart, lo, loInclusive, false, toElement, inclusive);
		}

		@Override
		public LongNavigableSet tailSet(long fromElement, boolean inclusive)
		{
			if(!inRange(fromElement, inclusive)) throw new IllegalArgumentException("fromElement out of range");
			return new AscendingSubSet(set, false, fromElement, inclusive, toEnd, hi, hiInclusive);
		}
		
		@Override
		public LongBidirectionalIterator iterator()
		{
			return new AscendingSubSetIterator(absLowest(), absHighFence(), absLowFence());
		}
		
		@Override
		public LongBidirectionalIterator iterator(long fromElement)
		{
			return new AscendingSubSetIterator(absLower(fromElement), absHighFence(), absLowFence());
		}
		
		@Override
		public LongBidirectionalIterator descendingIterator()
		{
			return new DescendingSubSetIterator(absHighest(), absLowFence(), absHighFence());
		}

		@Override
		public LongNavigableSet descendingSet()
		{
			if(inverse == null) inverse = new DescendingSubSet(set, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
			return inverse;
		}
		
		@Override
		protected Entry subLowest() { return absLowest(); }
		@Override
		protected Entry subHighest() { return absHighest(); }
		@Override
		protected Entry subCeiling(long key) { return absCeiling(key); }
		@Override
		protected Entry subHigher(long key) { return absHigher(key); }
		@Override
		protected Entry subFloor(long key) { return absFloor(key); }
		@Override
		protected Entry subLower(long key) { return absLower(key); }
		@Override
		protected Entry start() { return absLowest(); }
		@Override
		protected Entry next(Entry entry) { return entry.next(); }
	}
	
	static class DescendingSubSet extends SubSet
	{
		LongComparator comparator;
		
		public DescendingSubSet(LongRBTreeSet set, boolean fromStart, long lo, boolean loInclusive, boolean toEnd, long hi, boolean hiInclusive)
		{
			super(set, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
			comparator = set.comparator() == null ? LongComparator.of(Collections.reverseOrder()) : set.comparator().reversed();
		}
		
		@Override
		public LongComparator comparator() { return comparator; }
		
		public long getDefaultMaxValue() { return super.getDefaultMinValue(); }
		
		public long getDefaultMinValue() { return super.getDefaultMaxValue(); }
		
		@Override
		public LongNavigableSet subSet(long fromElement, boolean fromInclusive, long toElement, boolean toInclusive) {
			if(!inRange(fromElement, fromInclusive)) throw new IllegalArgumentException("fromElement out of range");
			if(!inRange(toElement, toInclusive)) throw new IllegalArgumentException("toElement out of range");
			return new DescendingSubSet(set, false, fromElement, fromInclusive, false, toElement, toInclusive);
		}
		
		@Override
		public LongNavigableSet headSet(long toElement, boolean inclusive)
		{
			if(!inRange(toElement, inclusive)) throw new IllegalArgumentException("toElement out of range");
			return new DescendingSubSet(set, false, toElement, inclusive, toEnd, hi, hiInclusive);
		}

		@Override
		public LongNavigableSet tailSet(long fromElement, boolean inclusive) {
			if(!inRange(fromElement, inclusive)) throw new IllegalArgumentException("fromElement out of range");
			return new DescendingSubSet(set, fromStart, lo, loInclusive, false, fromElement, inclusive);
		}

		@Override
		public LongBidirectionalIterator iterator() {
			return new DescendingSubSetIterator(absHighest(), absLowFence(), absHighFence());
		}

		@Override
		public LongBidirectionalIterator descendingIterator() {
			return new AscendingSubSetIterator(absLowest(), absHighFence(), absLowFence());
		}

		@Override
		public LongNavigableSet descendingSet() {
			if(inverse == null) inverse = new AscendingSubSet(set, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
			return inverse;
		}
		
		@Override
		public LongBidirectionalIterator iterator(long fromElement) {
			return new DescendingSubSetIterator(absHigher(fromElement), absLowFence(), absHighFence());
		}
		
		@Override
		protected Entry subLowest() { return absHighest(); }
		@Override
		protected Entry subHighest() { return absLowest(); }
		@Override
		protected Entry subCeiling(long key) { return absFloor(key); }
		@Override
		protected Entry subHigher(long key) { return absLower(key); }
		@Override
		protected Entry subFloor(long key) { return absCeiling(key); }
		@Override
		protected Entry subLower(long key) { return absHigher(key); }
		@Override
		protected Entry start() { return absHighest(); }
		@Override
		protected Entry next(Entry entry) { return entry.previous(); }
	}
	
	static abstract class SubSet extends AbstractLongSet implements LongNavigableSet
	{
		final LongRBTreeSet set;
		final long lo, hi;
		final boolean fromStart, toEnd;
		final boolean loInclusive, hiInclusive;
		LongNavigableSet inverse;
		
		public SubSet(LongRBTreeSet set, boolean fromStart, long lo, boolean loInclusive, boolean toEnd, long hi, boolean hiInclusive)
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
		public void setDefaultMaxValue(long value) { set.setDefaultMaxValue(value); }
		
		@Override
		public long getDefaultMaxValue() { return set.getDefaultMaxValue(); }
		
		@Override
		public void setDefaultMinValue(long value) { set.setDefaultMinValue(value); }
		
		@Override
		public long getDefaultMinValue() { return set.getDefaultMinValue(); }
		
		@Override
		public abstract	LongBidirectionalIterator iterator();
		
		boolean tooLow(long key) {
			if (!fromStart) {
				int c = set.compare(key, lo);
				if (c < 0 || (c == 0 && !loInclusive)) return true;
			}
			return false;
		}
		
		boolean tooHigh(long key) {
			if (!toEnd) {
				int c = set.compare(key, hi);
				if (c > 0 || (c == 0 && !hiInclusive)) return true;
			}
			return false;
		}
		
		boolean inRange(long key) { return !tooLow(key) && !tooHigh(key); }
		boolean inClosedRange(long key) { return (fromStart || set.compare(key, lo) >= 0) && (toEnd || set.compare(hi, key) >= 0); }
		boolean inRange(long key, boolean inclusive) { return inclusive ? inRange(key) : inClosedRange(key); }
		
		protected abstract Entry subLowest();
		protected abstract Entry subHighest();
		protected abstract Entry subCeiling(long key);
		protected abstract Entry subHigher(long key);
		protected abstract Entry subFloor(long key);
		protected abstract Entry subLower(long key);
		protected long lowKeyOrNull(Entry entry) { return entry == null ? getDefaultMinValue() : entry.key; }
		protected long highKeyOrNull(Entry entry) { return entry == null ? getDefaultMaxValue() : entry.key; }
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
		
		final Entry absCeiling(long key) {
			if (tooLow(key)) return absLowest();
			Entry e = set.findCeilingNode(key);
			return (e == null || tooHigh(e.key)) ? null : e;
		}
		
		final Entry absHigher(long key) {
			if (tooLow(key)) return absLowest();
			Entry e = set.findHigherNode(key);
			return (e == null || tooHigh(e.key)) ? null : e;
		}
		
		final Entry absFloor(long key) {
			if (tooHigh(key)) return absHighest();
			Entry e = set.findFloorNode(key);
			return (e == null || tooLow(e.key)) ? null : e;
		}
		
		final Entry absLower(long key) {
			if (tooHigh(key)) return absHighest();
			Entry e = set.findLowerNode(key);
			return (e == null || tooLow(e.key)) ? null : e;
		}
		
		final Entry absHighFence() { return (toEnd ? null : (hiInclusive ? set.findHigherNode(hi) : set.findCeilingNode(hi))); }
		final Entry absLowFence() { return (fromStart ? null : (loInclusive ? set.findLowerNode(lo) : set.findFloorNode(lo))); }
		
		@Override
		public boolean add(long o) {
			if(!inRange(o)) throw new IllegalArgumentException("Key is out of range");
			return set.add(o);
		}
		
		@Override
		public boolean contains(long e) {
			return inRange(e) && set.contains(e);
		}
		
		@Override
		public boolean remove(long o) {
			return inRange(o) && set.remove(o);
		}
		
		@Override
		public boolean contains(Object e) {
			long o = ((Long)e).longValue();
			return inRange(o) && set.contains(o);
		}
	
		@Override
		public boolean remove(Object e) {
			long o = ((Long)e).longValue();
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
			for(LongIterator iter = iterator();iter.hasNext();iter.nextLong(),i++);
			return i;
		}
		
		@Override
		public long lower(long e) {
			return lowKeyOrNull(subLower(e));
		}
		
		@Override
		public long floor(long e) {
			return lowKeyOrNull(subFloor(e));
		}
		
		@Override
		public long ceiling(long e) {
			return highKeyOrNull(subCeiling(e));
		}
		
		@Override
		public long higher(long e) {
			return highKeyOrNull(subHigher(e));
		}
		
		@Override
		@Deprecated
		public Long lower(Long e) {
			Entry entry = subLower(e);
			return entry == null ? null : Long.valueOf(entry.key);
		}
		
		@Override
		@Deprecated
		public Long floor(Long e) {
			Entry entry = subFloor(e);
			return entry == null ? null : Long.valueOf(entry.key);
		}
		
		@Override
		@Deprecated
		public Long ceiling(Long e) {
			Entry entry = subCeiling(e);
			return entry == null ? null : Long.valueOf(entry.key);
		}
		
		@Override
		@Deprecated
		public Long higher(Long e) {
			Entry entry = subHigher(e);
			return entry == null ? null : Long.valueOf(entry.key);
		}
		
		@Override
		public long pollFirstLong() {
			Entry entry = subLowest();
			if(entry != null) {
				long result = entry.key;
				set.removeNode(entry);
				return result;
			}
			return getDefaultMinValue();
		}
		
		@Override
		public long pollLastLong() {
			Entry entry = subHighest();
			if(entry != null) {
				long result = entry.key;
				set.removeNode(entry);
				return result;
			}
			return getDefaultMaxValue();
		}
		
		@Override
		public long firstLong() {
			Entry entry = subLowest();
			if(entry == null) throw new NoSuchElementException();
			return entry.key;
		}
		
		@Override
		public long lastLong() {
			Entry entry = subHighest();
			if(entry == null) throw new NoSuchElementException();
			return entry.key;
		}
		
		@Override
		public SubSet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(LongConsumer action) {
			Objects.requireNonNull(action);
			for(Entry entry = start();entry != null && inRange(entry.key);entry = next(entry)) {
				action.accept(entry.key);
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectLongConsumer<E> action) {
			Objects.requireNonNull(action);
			for(Entry entry = start();entry != null && inRange(entry.key);entry = next(entry))
				action.accept(input, entry.key);		
		}
		
		@Override
		public boolean matchesAny(Long2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(Entry entry = start();entry != null && inRange(entry.key);entry = next(entry)) {
				if(filter.get(entry.key)) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Long2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(Entry entry = start();entry != null && inRange(entry.key);entry = next(entry)) {
				if(filter.get(entry.key)) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Long2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(Entry entry = start();entry != null && inRange(entry.key);entry = next(entry)) {
				if(!filter.get(entry.key)) return false;
			}
			return true;
		}
		
		@Override
		public long reduce(long identity, LongLongUnaryOperator operator) {
			Objects.requireNonNull(operator);
			long state = identity;
			for(Entry entry = start();entry != null && inRange(entry.key);entry = next(entry)) {
				state = operator.applyAsLong(state, entry.key);
			}
			return state;
		}
		
		@Override
		public long reduce(LongLongUnaryOperator operator) {
			Objects.requireNonNull(operator);
			long state = 0L;
			boolean empty = true;
			for(Entry entry = start();entry != null && inRange(entry.key);entry = next(entry)) {
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
			for(Entry entry = start();entry != null && inRange(entry.key);entry = next(entry)) {
				if(filter.get(entry.key)) return entry.key;
			}
			return 0L;
		}
		
		@Override
		public int count(Long2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			int result = 0;
			for(Entry entry = start();entry != null && inRange(entry.key);entry = next(entry)) {
				if(filter.get(entry.key)) result++;
			}
			return result;
		}
		
		class AscendingSubSetIterator implements LongBidirectionalIterator
		{
			Entry lastReturned;
			Entry next;
			boolean forwards = false;
			boolean unboundForwardFence;
			boolean unboundBackwardFence;
			long forwardFence;
			long backwardFence;
			
			public AscendingSubSetIterator(Entry first, Entry forwardFence, Entry backwardFence)
			{
				next = first;
				this.forwardFence = forwardFence == null ? 0L : forwardFence.key;
				this.backwardFence = backwardFence == null ? 0L : backwardFence.key;
				unboundForwardFence = forwardFence == null;
				unboundBackwardFence = backwardFence == null;
			}
			
			@Override
			public boolean hasNext() {
                return next != null && (unboundForwardFence || next.key != forwardFence);
			}
			
			@Override
			public long nextLong() {
				if(!hasNext()) throw new NoSuchElementException();
				lastReturned = next;
				long result = next.key;
				next = next.next();
				forwards = true;
				return result;
			}
			
			@Override
			public boolean hasPrevious() {
                return next != null && (unboundBackwardFence || next.key != backwardFence);
			}
			
			@Override
			public long previousLong() {
				if(!hasPrevious()) throw new NoSuchElementException();
				lastReturned = next;
				long result = next.key;
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
		
		class DescendingSubSetIterator implements LongBidirectionalIterator
		{
			Entry lastReturned;
			Entry next;
			boolean forwards = false;
			boolean unboundForwardFence;
			boolean unboundBackwardFence;
			long forwardFence;
			long backwardFence;
			
			public DescendingSubSetIterator(Entry first, Entry forwardFence, Entry backwardFence)
			{
				next = first;
				this.forwardFence = forwardFence == null ? 0L : forwardFence.key;
				this.backwardFence = backwardFence == null ? 0L : backwardFence.key;
				unboundForwardFence = forwardFence == null;
				unboundBackwardFence = backwardFence == null;
			}
			
			@Override
			public boolean hasNext() {
                return next != null && (unboundForwardFence || next.key != forwardFence);
			}
			
			@Override
			public long nextLong() {
				if(!hasNext()) throw new NoSuchElementException();
				lastReturned = next;
				long result = next.key;
				next = next.previous();
				forwards = false;
				return result;
			}
			
			@Override
			public boolean hasPrevious() {
                return next != null && (unboundBackwardFence || next.key != backwardFence);
			}
			
			@Override
			public long previousLong() {
				if(!hasPrevious()) throw new NoSuchElementException();
				lastReturned = next;
				long result = next.key;
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
	
	class AscendingSetIterator implements LongBidirectionalIterator
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
		public long nextLong() {
			if(!hasNext()) throw new NoSuchElementException();
			lastReturned = next;
			long result = next.key;
			next = next.next();
			forwards = true;
			return result;
		}
		
		@Override
		public boolean hasPrevious() {
            return next != null;
		}
		
		@Override
		public long previousLong() {
			if(!hasPrevious()) throw new NoSuchElementException();
			lastReturned = next;
			long result = next.key;
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
	
	class DescendingSetIterator implements LongBidirectionalIterator
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
		public long nextLong() {
			if(!hasNext()) throw new NoSuchElementException();
			lastReturned = next;
			long result = next.key;
			next = next.previous();
			forwards = false;
			return result;
		}
		
		@Override
		public boolean hasPrevious() {
            return next != null;
		}
		
		@Override
		public long previousLong() {
			if(!hasPrevious()) throw new NoSuchElementException();
			lastReturned = next;
			long result = next.key;
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
		
		long key;
		int state;
		Entry parent;
		Entry left;
		Entry right;
		
		Entry(long key, Entry parent) {
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