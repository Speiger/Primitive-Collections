package speiger.src.collections.ints.sets;

import java.util.SortedSet;

import speiger.src.collections.ints.collections.IntBidirectionalIterator;
import speiger.src.collections.ints.collections.IntSplititerator;
import speiger.src.collections.ints.functions.IntComparator;
import speiger.src.collections.ints.utils.IntSets;
import speiger.src.collections.ints.utils.IntSplititerators;

/**
 * A Type Specific SortedSet implementation to reduce boxing/unboxing
 * with a couple extra methods that allow greater control over sets.
 * @note IntOrderedSet is only extended until 0.6.0 for Compat reasons.
 * The supported classes already implement IntOrderedSet directly and will remove IntSortedSet implementations in favor of IntOrderedSet instead
 */
public interface IntSortedSet extends IntSet, SortedSet<Integer>
{
	/**
	 * A Type Specific Comparator method
	 * @return the type specific comparator
	 */
	@Override
	public IntComparator comparator();
	
	@Override
	public IntSortedSet copy();
	
	@Override
	public IntBidirectionalIterator iterator();
	/**
	 * A type Specific Iterator starting from a given key
	 * @param fromElement the element the iterator should start from
	 * @return a iterator starting from the given element
	 * @throws java.util.NoSuchElementException if fromElement isn't found
	 */
	public IntBidirectionalIterator iterator(int fromElement);
	
	/**
	 * Creates a Wrapped SortedSet that is Synchronized
	 * @return a new SortedSet that is synchronized
	 * @see IntSets#synchronize
	 */
	public default IntSortedSet synchronize() { return IntSets.synchronize(this); }
	
	/**
	 * Creates a Wrapped SortedSet that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new SortedSet Wrapper that is synchronized
	 * @see IntSets#synchronize
	 */
	public default IntSortedSet synchronize(Object mutex) { return IntSets.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped SortedSet that is unmodifiable
	 * @return a new SortedSet Wrapper that is unmodifiable
	 * @see IntSets#unmodifiable
	 */
	public default IntSortedSet unmodifiable() { return IntSets.unmodifiable(this); }
	
	/**
	 * A Type Specific Type Splititerator to reduce boxing/unboxing
	 * @return type specific splititerator
	 */
	@Override
	default IntSplititerator spliterator() { return IntSplititerators.createSplititerator(this, 0); }
	
	/**
	 * A Type Specific SubSet method to reduce boxing/unboxing
	 * @param fromElement where the SubSet should start
	 * @param toElement where the SubSet should end
	 * @return a SubSet that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public IntSortedSet subSet(int fromElement, int toElement);
	/**
	 * A Type Specific HeadSet method to reduce boxing/unboxing
	 * @param toElement where the HeadSet should end
	 * @return a HeadSet that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public IntSortedSet headSet(int toElement);
	/**
	 * A Type Specific TailSet method to reduce boxing/unboxing
	 * @param fromElement where the TailSet should start
	 * @return a TailSet that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public IntSortedSet tailSet(int fromElement);
	
	/**
	 * A method to get the first element in the set
	 * @return first element in the set
	 */
	public int firstInt();
	/**
	 * A method to get and remove the first element in the set
	 * @return first element in the set
	 */
	public int pollFirstInt();
	/**
	 * A method to get the last element in the set
	 * @return last element in the set
	 */
	public int lastInt();
	/**
	 * A method to get and remove the last element in the set
	 * @return last element in the set
	 */
	public int pollLastInt();
	
	@Override
	@Deprecated
	public default IntSortedSet subSet(Integer fromElement, Integer toElement) { return subSet(fromElement.intValue(), toElement.intValue()); }
	@Override
	@Deprecated
	public default IntSortedSet headSet(Integer toElement) { return headSet(toElement.intValue()); }
	@Override
	@Deprecated
	public default IntSortedSet tailSet(Integer fromElement) { return tailSet(fromElement.intValue()); }
	
	@Override
	@Deprecated
	public default Integer first() { return Integer.valueOf(firstInt()); }
	@Override
	@Deprecated
	default Integer last() { return Integer.valueOf(lastInt()); }
}