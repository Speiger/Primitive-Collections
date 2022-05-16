package speiger.src.collections.chars.sets;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import speiger.src.collections.chars.collections.CharBidirectionalIterator;
import speiger.src.collections.chars.functions.CharComparator;
import speiger.src.collections.chars.functions.CharConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectCharConsumer;
import speiger.src.collections.chars.functions.function.Char2BooleanFunction;
import speiger.src.collections.chars.functions.function.CharCharUnaryOperator;
import speiger.src.collections.chars.collections.CharCollection;
import speiger.src.collections.chars.collections.CharIterator;
import speiger.src.collections.chars.utils.CharIterators;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Simple Type Specific AVL TreeSet implementation that reduces boxing/unboxing.
 * It is using a bit more memory then <a href="https://github.com/vigna/fastutil">FastUtil</a>,
 * but it saves a lot of Performance on the Optimized removal and iteration logic.
 * Which makes the implementation actually useable and does not get outperformed by Javas default implementation.
 */
public class CharAVLTreeSet extends AbstractCharSet implements CharNavigableSet
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
	protected transient CharComparator comparator;
	/** the default return value for max searches */
	protected char defaultMaxNotFound = Character.MIN_VALUE;
	/** the default return value for min searches */
	protected char defaultMinNotFound = Character.MAX_VALUE;
	
	/**
	 * Default Constructor
	 */
	public CharAVLTreeSet() {
	}
	
	/**
	 * Constructor that allows to define the sorter
	 * @param comp the function that decides how the tree is sorted, can be null
	 */
	public CharAVLTreeSet(CharComparator comp) {
		comparator = comp;
	}
	
	/**
	 * Helper constructor that allow to create a set from an array
	 * @param array the elements that should be used
	 */
	public CharAVLTreeSet(char[] array) {
		this(array, 0, array.length);
	}
	
	/**
	 * Helper constructor that allow to create a set from an array
	 * @param array the elements that should be used
	 * @param offset the starting index within the array
	 * @param length the amount of elements that are within the array
	 * @throws IllegalStateException if offset and length causes to step outside of the arrays range
	 */
	public CharAVLTreeSet(char[] array, int offset, int length) {
		SanityChecks.checkArrayCapacity(array.length, offset, length);
		for(int i = 0;i<length;i++) add(array[offset+i]);
	}
	
	/**
	 * Helper constructor that allow to create a set from an array
	 * @param array the elements that should be used
	 * @param comp the sorter of the tree, can be null
	 */
	public CharAVLTreeSet(char[] array, CharComparator comp) {
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
	public CharAVLTreeSet(char[] array, int offset, int length, CharComparator comp) {
		comparator = comp;
		SanityChecks.checkArrayCapacity(array.length, offset, length);
		for(int i = 0;i<length;i++) add(array[offset+i]);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided SortedSet.
	 * @param sortedSet the set the elements should be added to the TreeSet
	 * @note this also includes the Comparator if present
	 */
	public CharAVLTreeSet(CharSortedSet sortedSet) {
		comparator = sortedSet.comparator();
		addAll(sortedSet);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param collection the set the elements should be added to the TreeSet
	 */
	@Deprecated
	public CharAVLTreeSet(Collection<? extends Character> collection) {
		addAll(collection);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param collection the set the elements should be added to the TreeSet
	 * @param comp the sorter of the tree, can be null
	 */
	@Deprecated
	public CharAVLTreeSet(Collection<? extends Character> collection, CharComparator comp) {
		comparator = comp;
		addAll(collection);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param collection the set the elements should be added to the TreeSet
	 */
	public CharAVLTreeSet(CharCollection collection) {
		addAll(collection);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param collection the set the elements should be added to the TreeSet
	 * @param comp the sorter of the tree, can be null
	 */
	public CharAVLTreeSet(CharCollection collection, CharComparator comp) {
		comparator = comp;
		addAll(collection);
	}
	
	/**
	 * A Helper constructor that allows to create a set from a iterator of an unknown size
	 * @param iterator the elements that should be added to the set
	 */
	public CharAVLTreeSet(Iterator<Character> iterator) {
		this(CharIterators.wrap(iterator));
	}
	
	/**
	 * A Helper constructor that allows to create a set from a iterator of an unknown size
	 * @param iterator the elements that should be added to the set
	 * @param comp the sorter of the tree, can be null
	 */
	public CharAVLTreeSet(Iterator<Character> iterator, CharComparator comp) {
		this(CharIterators.wrap(iterator), comp);
	}
	
	/**
	 * A Helper constructor that allows to create a set from a iterator of an unknown size
	 * @param iterator the elements that should be added to the set
	 */
	public CharAVLTreeSet(CharIterator iterator) {
		while(iterator.hasNext()) add(iterator.nextChar());
	}
	
	/**
	 * A Helper constructor that allows to create a set from a iterator of an unknown size
	 * @param iterator the elements that should be added to the set
	 * @param comp the sorter of the tree, can be null
	 */
	public CharAVLTreeSet(CharIterator iterator, CharComparator comp) {
		comparator = comp;
		while(iterator.hasNext()) add(iterator.nextChar());
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
	public boolean add(char o) {
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
	public char lower(char e) {
		Entry node = findLowerNode(e);
		return node != null ? node.key : getDefaultMinValue();
	}

	@Override
	public char floor(char e) {
		Entry node = findFloorNode(e);
		return node != null ? node.key : getDefaultMinValue();
	}
	
	@Override
	public char higher(char e) {
		Entry node = findHigherNode(e);
		return node != null ? node.key : getDefaultMaxValue();
	}

	@Override
	public char ceiling(char e) {
		Entry node = findCeilingNode(e);
		return node != null ? node.key : getDefaultMaxValue();
	}
	
	@Override
	public Character lower(Character e) {
		Entry node = findLowerNode(e);
		return node != null ? node.key : null;
	}
	
	@Override
	public Character floor(Character e) {
		Entry node = findFloorNode(e);
		return node != null ? node.key : null;
	}
	
	@Override
	public Character higher(Character e) {
		Entry node = findHigherNode(e);
		return node != null ? node.key : null;
	}
	
	@Override
	public Character ceiling(Character e) {
		Entry node = findCeilingNode(e);
		return node != null ? node.key : null;
	}
	
	@Override
	public void forEach(CharConsumer action) {
		Objects.requireNonNull(action);
		for(Entry entry = first;entry != null;entry = entry.next()) {
			action.accept(entry.key);
		}
	}
	
	@Override
	public <E> void forEach(E input, ObjectCharConsumer<E> action) {
		Objects.requireNonNull(action);
		for(Entry entry = first;entry != null;entry = entry.next())
			action.accept(input, entry.key);		
	}
	
	@Override
	public boolean matchesAny(Char2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(Entry entry = first;entry != null;entry = entry.next()) {
			if(filter.get(entry.key)) return true;
		}
		return false;
	}
	
	@Override
	public boolean matchesNone(Char2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(Entry entry = first;entry != null;entry = entry.next()) {
			if(filter.get(entry.key)) return false;
		}
		return true;
	}
	
	@Override
	public boolean matchesAll(Char2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(Entry entry = first;entry != null;entry = entry.next()) {
			if(!filter.get(entry.key)) return false;
		}
		return true;
	}
	
	@Override
	public char findFirst(Char2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(Entry entry = first;entry != null;entry = entry.next()) {
			if(filter.get(entry.key)) return entry.key;
		}
		return (char)0;
	}
	
	@Override
	public char reduce(char identity, CharCharUnaryOperator operator) {
		Objects.requireNonNull(operator);
		char state = identity;
		for(Entry entry = first;entry != null;entry = entry.next()) {
			state = operator.applyAsChar(state, entry.key);
		}
		return state;
	}
	
	@Override
	public char reduce(CharCharUnaryOperator operator) {
		Objects.requireNonNull(operator);
		char state = (char)0;
		boolean empty = true;
		for(Entry entry = first;entry != null;entry = entry.next()) {
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
	public int count(Char2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		int result = 0;
		for(Entry entry = first;entry != null;entry = entry.next()) {
			if(filter.get(entry.key)) result++;
		}
		return result;
	}
	
	protected Entry findNode(char o) {
		Entry node = tree;
		int compare;
		while(node != null) {
			if((compare = compare(o, node.key)) == 0) return node;
			if(compare < 0) node = node.left;
			else node = node.right;
		}
		return null;
	}
	
	protected Entry findLowerNode(char e) {
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
	
	protected Entry findFloorNode(char e) {
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
	
	protected Entry findCeilingNode(char e) {
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
	
	protected Entry findHigherNode(char e) {
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
	public boolean contains(char e) {
		return findNode(e) != null;
	}
	
	@Override
	public boolean contains(Object e) {
		return findNode(((Character)e).charValue()) != null;
	}
	
	@Override
	public char firstChar() {
		if(tree == null) throw new NoSuchElementException();
		return first.key;
	}
	
	@Override
	public char lastChar() {
		if(tree == null) throw new NoSuchElementException();
		return last.key;
	}
	
	@Override
	public boolean remove(char o) {
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
		Entry entry = findNode(((Character)o).charValue());
		if(entry != null) {
			removeNode(entry);
			return true;
		}
		return false;
	}
	
	@Override
	public char pollFirstChar() {
		if(tree == null) return getDefaultMinValue();
		char value = first.key;
		removeNode(first);
		return value;
	}
	
	@Override
	public char pollLastChar() {
		if(tree == null) return getDefaultMaxValue();
		char value = last.key;
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
	
	public CharAVLTreeSet copy() {
		CharAVLTreeSet set = new CharAVLTreeSet();
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
	public CharComparator comparator() { return comparator; }
	
	@Override
	public CharBidirectionalIterator iterator() { return new AscendingSetIterator(first); }
	
	@Override
	public CharBidirectionalIterator iterator(char fromElement) {
		Entry entry = findNode(fromElement);
		return entry == null ? null : new AscendingSetIterator(entry);
	}
	
	@Override
	public CharBidirectionalIterator descendingIterator() { return new DescendingSetIterator(last); }
	
	@Override
	public CharNavigableSet subSet(char fromElement, boolean fromInclusive, char toElement, boolean toInclusive) {
		return new AscendingSubSet(this, false, fromElement, fromInclusive, false, toElement, toInclusive);
	}

	@Override
	public CharNavigableSet headSet(char toElement, boolean inclusive) {
		return new AscendingSubSet(this, true, (char)0, true, false, toElement, inclusive);
	}

	@Override
	public CharNavigableSet tailSet(char fromElement, boolean inclusive) {
		return new AscendingSubSet(this, false, fromElement, inclusive, true, (char)0, true);
	}
	
	@Override
	public CharNavigableSet descendingSet() {
		return new DescendingSubSet(this, true, (char)0, true, true, (char)0, true);
	}
	
	protected void removeNode(Entry entry) {
		size--;
		if(entry.needsSuccessor()) {
			Entry successor = entry.next();
			entry.key = successor.key;
			entry = successor;
		}
		if(entry.previous() == null) first = entry.next();
		if(entry.next() == null) last = entry.previous();
		Entry replacement = entry.left != null ? entry.left : entry.right;
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
			entry.updateHeight();
			right.updateHeight();
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
			entry.updateHeight();
			left.updateHeight();
		}
	}
	
	/** From CLR */
	protected void fixAfterInsertion(Entry entry) {
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
	protected void fixAfterDeletion(Entry entry) {
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
	
	static class AscendingSubSet extends SubSet
	{
		public AscendingSubSet(CharAVLTreeSet set, boolean fromStart, char lo, boolean loInclusive, boolean toEnd, char hi, boolean hiInclusive)
		{
			super(set, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
		}
		
		@Override
		public CharComparator comparator() { return set.comparator(); }
		
		@Override
		public CharNavigableSet subSet(char fromElement, boolean fromInclusive, char toElement, boolean toInclusive)
		{
			if(!inRange(fromElement, fromInclusive)) throw new IllegalArgumentException("fromElement out of range");
			if(!inRange(toElement, toInclusive)) throw new IllegalArgumentException("toElement out of range");
			return new AscendingSubSet(set, false, fromElement, fromInclusive, false, toElement, toInclusive);
		}
		
		@Override
		public CharNavigableSet headSet(char toElement, boolean inclusive)
		{
			if(!inRange(toElement, inclusive)) throw new IllegalArgumentException("toElement out of range");
			return new AscendingSubSet(set, fromStart, lo, loInclusive, false, toElement, inclusive);
		}

		@Override
		public CharNavigableSet tailSet(char fromElement, boolean inclusive)
		{
			if(!inRange(fromElement, inclusive)) throw new IllegalArgumentException("fromElement out of range");
			return new AscendingSubSet(set, false, fromElement, inclusive, toEnd, hi, hiInclusive);
		}
		
		@Override
		public CharBidirectionalIterator iterator()
		{
			return new AscendingSubSetIterator(absLowest(), absHighFence(), absLowFence());
		}
		
		@Override
		public CharBidirectionalIterator iterator(char fromElement)
		{
			return new AscendingSubSetIterator(absLower(fromElement), absHighFence(), absLowFence());
		}
		
		@Override
		public CharBidirectionalIterator descendingIterator()
		{
			return new DescendingSubSetIterator(absHighest(), absLowFence(), absHighFence());
		}

		@Override
		public CharNavigableSet descendingSet()
		{
			if(inverse == null) inverse = new DescendingSubSet(set, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
			return inverse;
		}
		
		@Override
		protected Entry subLowest() { return absLowest(); }
		@Override
		protected Entry subHighest() { return absHighest(); }
		@Override
		protected Entry subCeiling(char key) { return absCeiling(key); }
		@Override
		protected Entry subHigher(char key) { return absHigher(key); }
		@Override
		protected Entry subFloor(char key) { return absFloor(key); }
		@Override
		protected Entry subLower(char key) { return absLower(key); }
		@Override
		protected Entry start() { return absLowest(); }
		@Override
		protected Entry next(Entry entry) { return entry.next(); }
	}
	
	static class DescendingSubSet extends SubSet
	{
		CharComparator comparator;
		
		public DescendingSubSet(CharAVLTreeSet set, boolean fromStart, char lo, boolean loInclusive, boolean toEnd, char hi, boolean hiInclusive)
		{
			super(set, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
			comparator = set.comparator() == null ? CharComparator.of(Collections.reverseOrder()) : set.comparator().reversed();
		}
		
		@Override
		public CharComparator comparator() { return comparator; }
		
		public char getDefaultMaxValue() { return super.getDefaultMinValue(); }
		
		public char getDefaultMinValue() { return super.getDefaultMaxValue(); }
		
		@Override
		public CharNavigableSet subSet(char fromElement, boolean fromInclusive, char toElement, boolean toInclusive) {
			if(!inRange(fromElement, fromInclusive)) throw new IllegalArgumentException("fromElement out of range");
			if(!inRange(toElement, toInclusive)) throw new IllegalArgumentException("toElement out of range");
			return new DescendingSubSet(set, false, fromElement, fromInclusive, false, toElement, toInclusive);
		}
		
		@Override
		public CharNavigableSet headSet(char toElement, boolean inclusive)
		{
			if(!inRange(toElement, inclusive)) throw new IllegalArgumentException("toElement out of range");
			return new DescendingSubSet(set, false, toElement, inclusive, toEnd, hi, hiInclusive);
		}

		@Override
		public CharNavigableSet tailSet(char fromElement, boolean inclusive) {
			if(!inRange(fromElement, inclusive)) throw new IllegalArgumentException("fromElement out of range");
			return new DescendingSubSet(set, fromStart, lo, loInclusive, false, fromElement, inclusive);
		}

		@Override
		public CharBidirectionalIterator iterator() {
			return new DescendingSubSetIterator(absHighest(), absLowFence(), absHighFence());
		}

		@Override
		public CharBidirectionalIterator descendingIterator() {
			return new AscendingSubSetIterator(absLowest(), absHighFence(), absLowFence());
		}

		@Override
		public CharNavigableSet descendingSet() {
			if(inverse == null) inverse = new AscendingSubSet(set, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
			return inverse;
		}
		
		@Override
		public CharBidirectionalIterator iterator(char fromElement) {
			return new DescendingSubSetIterator(absHigher(fromElement), absLowFence(), absHighFence());
		}
		
		@Override
		protected Entry subLowest() { return absHighest(); }
		@Override
		protected Entry subHighest() { return absLowest(); }
		@Override
		protected Entry subCeiling(char key) { return absFloor(key); }
		@Override
		protected Entry subHigher(char key) { return absLower(key); }
		@Override
		protected Entry subFloor(char key) { return absCeiling(key); }
		@Override
		protected Entry subLower(char key) { return absHigher(key); }
		@Override
		protected Entry start() { return absHighest(); }
		@Override
		protected Entry next(Entry entry) { return entry.previous(); }
	}
	
	static abstract class SubSet extends AbstractCharSet implements CharNavigableSet
	{
		final CharAVLTreeSet set;
		final char lo, hi;
		final boolean fromStart, toEnd;
		final boolean loInclusive, hiInclusive;
		CharNavigableSet inverse;
		
		public SubSet(CharAVLTreeSet set, boolean fromStart, char lo, boolean loInclusive, boolean toEnd, char hi, boolean hiInclusive)
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
		public void setDefaultMaxValue(char value) { set.setDefaultMaxValue(value); }
		
		@Override
		public char getDefaultMaxValue() { return set.getDefaultMaxValue(); }
		
		@Override
		public void setDefaultMinValue(char value) { set.setDefaultMinValue(value); }
		
		@Override
		public char getDefaultMinValue() { return set.getDefaultMinValue(); }
		
		@Override
		public abstract	CharBidirectionalIterator iterator();
		
		boolean tooLow(char key) {
			if (!fromStart) {
				int c = set.compare(key, lo);
				if (c < 0 || (c == 0 && !loInclusive)) return true;
			}
			return false;
		}
		
		boolean tooHigh(char key) {
			if (!toEnd) {
				int c = set.compare(key, hi);
				if (c > 0 || (c == 0 && !hiInclusive)) return true;
			}
			return false;
		}
		
		boolean inRange(char key) { return !tooLow(key) && !tooHigh(key); }
		boolean inClosedRange(char key) { return (fromStart || set.compare(key, lo) >= 0) && (toEnd || set.compare(hi, key) >= 0); }
		boolean inRange(char key, boolean inclusive) { return inclusive ? inRange(key) : inClosedRange(key); }
		
		protected abstract Entry subLowest();
		protected abstract Entry subHighest();
		protected abstract Entry subCeiling(char key);
		protected abstract Entry subHigher(char key);
		protected abstract Entry subFloor(char key);
		protected abstract Entry subLower(char key);
		protected char lowKeyOrNull(Entry entry) { return entry == null ? getDefaultMinValue() : entry.key; }
		protected char highKeyOrNull(Entry entry) { return entry == null ? getDefaultMaxValue() : entry.key; }
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
		
		final Entry absCeiling(char key) {
			if (tooLow(key)) return absLowest();
			Entry e = set.findCeilingNode(key);
			return (e == null || tooHigh(e.key)) ? null : e;
		}
		
		final Entry absHigher(char key) {
			if (tooLow(key)) return absLowest();
			Entry e = set.findHigherNode(key);
			return (e == null || tooHigh(e.key)) ? null : e;
		}
		
		final Entry absFloor(char key) {
			if (tooHigh(key)) return absHighest();
			Entry e = set.findFloorNode(key);
			return (e == null || tooLow(e.key)) ? null : e;
		}
		
		final Entry absLower(char key) {
			if (tooHigh(key)) return absHighest();
			Entry e = set.findLowerNode(key);
			return (e == null || tooLow(e.key)) ? null : e;
		}
		
		final Entry absHighFence() { return (toEnd ? null : (hiInclusive ? set.findHigherNode(hi) : set.findCeilingNode(hi))); }
		final Entry absLowFence() { return (fromStart ? null : (loInclusive ? set.findLowerNode(lo) : set.findFloorNode(lo))); }
		
		@Override
		public boolean add(char o) {
			if(!inRange(o)) throw new IllegalArgumentException("Key is out of range");
			return set.add(o);
		}
		
		@Override
		public boolean contains(char e) {
			return inRange(e) && set.contains(e);
		}
		
		@Override
		public boolean remove(char o) {
			return inRange(o) && set.remove(o);
		}
		
		@Override
		public boolean contains(Object e) {
			char o = ((Character)e).charValue();
			return inRange(o) && set.contains(o);
		}
	
		@Override
		public boolean remove(Object e) {
			char o = ((Character)e).charValue();
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
			for(CharIterator iter = iterator();iter.hasNext();iter.nextChar(),i++);
			return i;
		}
		
		@Override
		public char lower(char e) {
			return lowKeyOrNull(subLower(e));
		}
		
		@Override
		public char floor(char e) {
			return lowKeyOrNull(subFloor(e));
		}
		
		@Override
		public char ceiling(char e) {
			return highKeyOrNull(subCeiling(e));
		}
		
		@Override
		public char higher(char e) {
			return highKeyOrNull(subHigher(e));
		}
		
		@Override
		@Deprecated
		public Character lower(Character e) {
			Entry entry = subLower(e);
			return entry == null ? null : Character.valueOf(entry.key);
		}
		
		@Override
		@Deprecated
		public Character floor(Character e) {
			Entry entry = subFloor(e);
			return entry == null ? null : Character.valueOf(entry.key);
		}
		
		@Override
		@Deprecated
		public Character ceiling(Character e) {
			Entry entry = subCeiling(e);
			return entry == null ? null : Character.valueOf(entry.key);
		}
		
		@Override
		@Deprecated
		public Character higher(Character e) {
			Entry entry = subHigher(e);
			return entry == null ? null : Character.valueOf(entry.key);
		}
		
		@Override
		public char pollFirstChar() {
			Entry entry = subLowest();
			if(entry != null) {
				char result = entry.key;
				set.removeNode(entry);
				return result;
			}
			return getDefaultMinValue();
		}
		
		@Override
		public char pollLastChar() {
			Entry entry = subHighest();
			if(entry != null) {
				char result = entry.key;
				set.removeNode(entry);
				return result;
			}
			return getDefaultMaxValue();
		}
		
		@Override
		public char firstChar() {
			Entry entry = subLowest();
			if(entry == null) throw new NoSuchElementException();
			return entry.key;
		}
		
		@Override
		public char lastChar() {
			Entry entry = subHighest();
			if(entry == null) throw new NoSuchElementException();
			return entry.key;
		}
		
		@Override
		public SubSet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(CharConsumer action) {
			Objects.requireNonNull(action);
			for(Entry entry = start();entry != null && inRange(entry.key);entry = next(entry)) {
				action.accept(entry.key);
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectCharConsumer<E> action) {
			Objects.requireNonNull(action);
			for(Entry entry = start();entry != null && inRange(entry.key);entry = next(entry))
				action.accept(input, entry.key);		
		}
		
		@Override
		public boolean matchesAny(Char2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(Entry entry = start();entry != null && inRange(entry.key);entry = next(entry)) {
				if(filter.get(entry.key)) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Char2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(Entry entry = start();entry != null && inRange(entry.key);entry = next(entry)) {
				if(filter.get(entry.key)) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Char2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(Entry entry = start();entry != null && inRange(entry.key);entry = next(entry)) {
				if(!filter.get(entry.key)) return false;
			}
			return true;
		}
		
		@Override
		public char reduce(char identity, CharCharUnaryOperator operator) {
			Objects.requireNonNull(operator);
			char state = identity;
			for(Entry entry = start();entry != null && inRange(entry.key);entry = next(entry)) {
				state = operator.applyAsChar(state, entry.key);
			}
			return state;
		}
		
		@Override
		public char reduce(CharCharUnaryOperator operator) {
			Objects.requireNonNull(operator);
			char state = (char)0;
			boolean empty = true;
			for(Entry entry = start();entry != null && inRange(entry.key);entry = next(entry)) {
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
		public char findFirst(Char2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(Entry entry = start();entry != null && inRange(entry.key);entry = next(entry)) {
				if(filter.get(entry.key)) return entry.key;
			}
			return (char)0;
		}
		
		@Override
		public int count(Char2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			int result = 0;
			for(Entry entry = start();entry != null && inRange(entry.key);entry = next(entry)) {
				if(filter.get(entry.key)) result++;
			}
			return result;
		}
		
		class AscendingSubSetIterator implements CharBidirectionalIterator
		{
			Entry lastReturned;
			Entry next;
			boolean forwards = false;
			boolean unboundForwardFence;
			boolean unboundBackwardFence;
			char forwardFence;
			char backwardFence;
			
			public AscendingSubSetIterator(Entry first, Entry forwardFence, Entry backwardFence)
			{
				next = first;
				this.forwardFence = forwardFence == null ? (char)0 : forwardFence.key;
				this.backwardFence = backwardFence == null ? (char)0 : backwardFence.key;
				unboundForwardFence = forwardFence == null;
				unboundBackwardFence = backwardFence == null;
			}
			
			@Override
			public boolean hasNext() {
                return next != null && (unboundForwardFence || next.key != forwardFence);
			}
			
			@Override
			public char nextChar() {
				if(!hasNext()) throw new NoSuchElementException();
				lastReturned = next;
				char result = next.key;
				next = next.next();
				forwards = true;
				return result;
			}
			
			@Override
			public boolean hasPrevious() {
                return next != null && (unboundBackwardFence || next.key != backwardFence);
			}
			
			@Override
			public char previousChar() {
				if(!hasPrevious()) throw new NoSuchElementException();
				lastReturned = next;
				char result = next.key;
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
		
		class DescendingSubSetIterator implements CharBidirectionalIterator
		{
			Entry lastReturned;
			Entry next;
			boolean forwards = false;
			boolean unboundForwardFence;
			boolean unboundBackwardFence;
			char forwardFence;
			char backwardFence;
			
			public DescendingSubSetIterator(Entry first, Entry forwardFence, Entry backwardFence)
			{
				next = first;
				this.forwardFence = forwardFence == null ? (char)0 : forwardFence.key;
				this.backwardFence = backwardFence == null ? (char)0 : backwardFence.key;
				unboundForwardFence = forwardFence == null;
				unboundBackwardFence = backwardFence == null;
			}
			
			@Override
			public boolean hasNext() {
                return next != null && (unboundForwardFence || next.key != forwardFence);
			}
			
			@Override
			public char nextChar() {
				if(!hasNext()) throw new NoSuchElementException();
				lastReturned = next;
				char result = next.key;
				next = next.previous();
				forwards = false;
				return result;
			}
			
			@Override
			public boolean hasPrevious() {
                return next != null && (unboundBackwardFence || next.key != backwardFence);
			}
			
			@Override
			public char previousChar() {
				if(!hasPrevious()) throw new NoSuchElementException();
				lastReturned = next;
				char result = next.key;
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
	
	class AscendingSetIterator implements CharBidirectionalIterator
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
		public char nextChar() {
			if(!hasNext()) throw new NoSuchElementException();
			lastReturned = next;
			char result = next.key;
			next = next.next();
			forwards = true;
			return result;
		}
		
		@Override
		public boolean hasPrevious() {
            return next != null;
		}
		
		@Override
		public char previousChar() {
			if(!hasPrevious()) throw new NoSuchElementException();
			lastReturned = next;
			char result = next.key;
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
	
	class DescendingSetIterator implements CharBidirectionalIterator
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
		public char nextChar() {
			if(!hasNext()) throw new NoSuchElementException();
			lastReturned = next;
			char result = next.key;
			next = next.previous();
			forwards = false;
			return result;
		}
		
		@Override
		public boolean hasPrevious() {
            return next != null;
		}
		
		@Override
		public char previousChar() {
			if(!hasPrevious()) throw new NoSuchElementException();
			lastReturned = next;
			char result = next.key;
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
		char key;
		int state;
		Entry parent;
		Entry left;
		Entry right;
		
		Entry(char key, Entry parent) {
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
		
		int getHeight() { return state; }
		
		void updateHeight() { state = (1 + Math.max(left == null ? -1 : left.getHeight(), right == null ? -1 : right.getHeight())); }
		
		int getBalance() { return (left == null ? -1 : left.getHeight()) - (right == null ? -1 : right.getHeight()); }
		
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