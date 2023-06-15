package speiger.src.collections.shorts.sets;

import java.util.SortedSet;

import speiger.src.collections.shorts.collections.ShortBidirectionalIterator;
import speiger.src.collections.shorts.collections.ShortSplititerator;
import speiger.src.collections.shorts.functions.ShortComparator;
import speiger.src.collections.shorts.utils.ShortSets;
import speiger.src.collections.shorts.utils.ShortSplititerators;

/**
 * A Type Specific SortedSet implementation to reduce boxing/unboxing
 * with a couple extra methods that allow greater control over sets.
 * @note ShortOrderedSet is only extended until 0.6.0 for Compat reasons.
 * The supported classes already implement ShortOrderedSet directly and will remove ShortSortedSet implementations in favor of ShortOrderedSet instead
 */
public interface ShortSortedSet extends ShortSet, SortedSet<Short>
{
	/**
	 * A Type Specific Comparator method
	 * @return the type specific comparator
	 */
	@Override
	public ShortComparator comparator();
	
	@Override
	public ShortSortedSet copy();
	
	@Override
	public ShortBidirectionalIterator iterator();
	/**
	 * A type Specific Iterator starting from a given key
	 * @param fromElement the element the iterator should start from
	 * @return a iterator starting from the given element
	 * @throws java.util.NoSuchElementException if fromElement isn't found
	 */
	public ShortBidirectionalIterator iterator(short fromElement);
	
	/**
	 * Creates a Wrapped SortedSet that is Synchronized
	 * @return a new SortedSet that is synchronized
	 * @see ShortSets#synchronize
	 */
	public default ShortSortedSet synchronize() { return ShortSets.synchronize(this); }
	
	/**
	 * Creates a Wrapped SortedSet that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new SortedSet Wrapper that is synchronized
	 * @see ShortSets#synchronize
	 */
	public default ShortSortedSet synchronize(Object mutex) { return ShortSets.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped SortedSet that is unmodifiable
	 * @return a new SortedSet Wrapper that is unmodifiable
	 * @see ShortSets#unmodifiable
	 */
	public default ShortSortedSet unmodifiable() { return ShortSets.unmodifiable(this); }
	
	/**
	 * A Type Specific Type Splititerator to reduce boxing/unboxing
	 * @return type specific splititerator
	 */
	@Override
	default ShortSplititerator spliterator() { return ShortSplititerators.createSplititerator(this, 0); }
	
	/**
	 * A Type Specific SubSet method to reduce boxing/unboxing
	 * @param fromElement where the SubSet should start
	 * @param toElement where the SubSet should end
	 * @return a SubSet that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public ShortSortedSet subSet(short fromElement, short toElement);
	/**
	 * A Type Specific HeadSet method to reduce boxing/unboxing
	 * @param toElement where the HeadSet should end
	 * @return a HeadSet that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public ShortSortedSet headSet(short toElement);
	/**
	 * A Type Specific TailSet method to reduce boxing/unboxing
	 * @param fromElement where the TailSet should start
	 * @return a TailSet that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public ShortSortedSet tailSet(short fromElement);
	
	/**
	 * A method to get the first element in the set
	 * @return first element in the set
	 */
	public short firstShort();
	/**
	 * A method to get and remove the first element in the set
	 * @return first element in the set
	 */
	public short pollFirstShort();
	/**
	 * A method to get the last element in the set
	 * @return last element in the set
	 */
	public short lastShort();
	/**
	 * A method to get and remove the last element in the set
	 * @return last element in the set
	 */
	public short pollLastShort();
	
	@Override
	@Deprecated
	public default ShortSortedSet subSet(Short fromElement, Short toElement) { return subSet(fromElement.shortValue(), toElement.shortValue()); }
	@Override
	@Deprecated
	public default ShortSortedSet headSet(Short toElement) { return headSet(toElement.shortValue()); }
	@Override
	@Deprecated
	public default ShortSortedSet tailSet(Short fromElement) { return tailSet(fromElement.shortValue()); }
	
	@Override
	@Deprecated
	public default Short first() { return Short.valueOf(firstShort()); }
	@Override
	@Deprecated
	default Short last() { return Short.valueOf(lastShort()); }
}