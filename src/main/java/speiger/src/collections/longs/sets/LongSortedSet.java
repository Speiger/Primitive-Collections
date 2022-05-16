package speiger.src.collections.longs.sets;

import java.util.SortedSet;

import speiger.src.collections.longs.collections.LongBidirectionalIterator;
import speiger.src.collections.longs.collections.LongSplititerator;
import speiger.src.collections.longs.functions.LongComparator;
import speiger.src.collections.longs.utils.LongSets;
import speiger.src.collections.longs.utils.LongSplititerators;

/**
 * A Type Specific SortedSet implementation to reduce boxing/unboxing
 * with a couple extra methods that allow greater control over sets.
 * @note LongOrderedSet is only extended until 0.6.0 for Compat reasons.
 * The supported classes already implement LongOrderedSet directly and will remove LongSortedSet implementations in favor of LongOrderedSet instead
 */
public interface LongSortedSet extends LongSet, SortedSet<Long>
{
	/**
	 * A Type Specific Comparator method
	 * @return the type specific comparator
	 */
	@Override
	public LongComparator comparator();
	
	@Override
	public LongSortedSet copy();
	
	@Override
	public LongBidirectionalIterator iterator();
	/**
	 * A type Specific Iterator starting from a given key
	 * @param fromElement the element the iterator should start from
	 * @return a iterator starting from the given element
	 * @throws java.util.NoSuchElementException if fromElement isn't found
	 */
	public LongBidirectionalIterator iterator(long fromElement);
	
	/**
	 * Creates a Wrapped SortedSet that is Synchronized
	 * @return a new SortedSet that is synchronized
	 * @see LongSets#synchronize
	 */
	public default LongSortedSet synchronize() { return LongSets.synchronize(this); }
	
	/**
	 * Creates a Wrapped SortedSet that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new SortedSet Wrapper that is synchronized
	 * @see LongSets#synchronize
	 */
	public default LongSortedSet synchronize(Object mutex) { return LongSets.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped SortedSet that is unmodifiable
	 * @return a new SortedSet Wrapper that is unmodifiable
	 * @see LongSets#unmodifiable
	 */
	public default LongSortedSet unmodifiable() { return LongSets.unmodifiable(this); }
	
	/**
	 * A Type Specific Type Splititerator to reduce boxing/unboxing
	 * @return type specific splititerator
	 */
	@Override
	default LongSplititerator spliterator() { return LongSplititerators.createSplititerator(this, 0); }
	
	/**
	 * A Type Specific SubSet method to reduce boxing/unboxing
	 * @param fromElement where the SubSet should start
	 * @param toElement where the SubSet should end
	 * @return a SubSet that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public LongSortedSet subSet(long fromElement, long toElement);
	/**
	 * A Type Specific HeadSet method to reduce boxing/unboxing
	 * @param toElement where the HeadSet should end
	 * @return a HeadSet that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public LongSortedSet headSet(long toElement);
	/**
	 * A Type Specific TailSet method to reduce boxing/unboxing
	 * @param fromElement where the TailSet should start
	 * @return a TailSet that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public LongSortedSet tailSet(long fromElement);
	
	/**
	 * A method to get the first element in the set
	 * @return first element in the set
	 */
	public long firstLong();
	/**
	 * A method to get and remove the first element in the set
	 * @return first element in the set
	 */
	public long pollFirstLong();
	/**
	 * A method to get the last element in the set
	 * @return last element in the set
	 */
	public long lastLong();
	/**
	 * A method to get and remove the last element in the set
	 * @return last element in the set
	 */
	public long pollLastLong();
	
	@Override
	@Deprecated
	public default LongSortedSet subSet(Long fromElement, Long toElement) { return subSet(fromElement.longValue(), toElement.longValue()); }
	@Override
	@Deprecated
	public default LongSortedSet headSet(Long toElement) { return headSet(toElement.longValue()); }
	@Override
	@Deprecated
	public default LongSortedSet tailSet(Long fromElement) { return tailSet(fromElement.longValue()); }
	
	@Override
	@Deprecated
	public default Long first() { return Long.valueOf(firstLong()); }
	@Override
	@Deprecated
	default Long last() { return Long.valueOf(lastLong()); }
}