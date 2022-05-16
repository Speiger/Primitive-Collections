package speiger.src.collections.longs.sets;

import java.util.NavigableSet;

import speiger.src.collections.longs.collections.LongBidirectionalIterator;
import speiger.src.collections.longs.collections.LongSplititerator;
import speiger.src.collections.longs.utils.LongSets;
import speiger.src.collections.longs.utils.LongSplititerators;

/**
 * A Type Specific Navigable Set interface with a couple helper methods
 */
public interface LongNavigableSet extends NavigableSet<Long>, LongSortedSet
{
	/**
	 * A Type Specific lower method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the greatest lower key that can be found
	 */
	public long lower(long key);
	/**
	 * A Type Specific higher method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the lowest higher key that can be found
	 */
	public long higher(long key);
	/**
	 * A Type Specific floor method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the greatest lower or equal key that can be found
	 */
	public long floor(long key);
	/**
	 * A Type Specific ceiling method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the lowest higher or equal key that can be found
	 */
	public long ceiling(long key);
	
	/**
	 * A Helper method to set the max value for SubSets. (Default: long.MIN_VALUE)
	 * @param e the new max value
	 */
	public void setDefaultMaxValue(long e);
	/**
	 * A Helper method to get the max value for SubSets.
	 * @return the default max value.
	 */
	public long getDefaultMaxValue();
	
	/**
	 * A Helper method to set the min value for SubSets. (Default: long.MAX_VALUE)
	 * @param e the new min value
	 */
	public void setDefaultMinValue(long e);
	/**
	 * A Helper method to get the min value for SubSets.
	 * @return the default min value.
	 */
	public long getDefaultMinValue();
	
	@Override
	public default LongNavigableSet subSet(long fromElement, long toElement) { return subSet(fromElement, true, toElement, false); }
	@Override
	public default LongNavigableSet headSet(long toElement) { return headSet(toElement, false); }
	@Override
	public default LongNavigableSet tailSet(long fromElement) { return tailSet(fromElement, true); }
	
	/**
	 * A Type Specific SubSet method to reduce boxing/unboxing
	 * @param fromElement where the SubSet should start
	 * @param fromInclusive if the fromElement is inclusive or not
	 * @param toElement where the SubSet should end
	 * @param toInclusive if the toElement is inclusive or not
	 * @return a SubSet that is within the range of the desired range
	 */
	public LongNavigableSet subSet(long fromElement, boolean fromInclusive, long toElement, boolean toInclusive);
	/**
	 * A Type Specific HeadSet method to reduce boxing/unboxing
	 * @param toElement where the HeadSet should end
	 * @param inclusive if the toElement is inclusive or not
	 * @return a HeadSet that is within the range of the desired range
	 */
	public LongNavigableSet headSet(long toElement, boolean inclusive);
	/**
	 * A Type Specific TailSet method to reduce boxing/unboxing
	 * @param fromElement where the TailSet should start
	 * @param inclusive if the fromElement is inclusive or not
	 * @return a TailSet that is within the range of the desired range
	 */
	public LongNavigableSet tailSet(long fromElement, boolean inclusive);
	
	/** @return a Type Specific iterator */
	@Override
	public LongBidirectionalIterator iterator();
	/** @return a Type Specific desendingIterator */
	@Override
	public LongBidirectionalIterator descendingIterator();
	/** @return a Type Specific desendingSet */
	@Override
	public LongNavigableSet descendingSet();
	@Override
	public LongNavigableSet copy();
	
	/**
	 * Creates a Wrapped NavigableSet that is Synchronized
	 * @return a new NavigableSet that is synchronized
	 * @see LongSets#synchronize
	 */
	public default LongNavigableSet synchronize() { return LongSets.synchronize(this); }
	
	/**
	 * Creates a Wrapped NavigableSet that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new NavigableSet Wrapper that is synchronized
	 * @see LongSets#synchronize
	 */
	public default LongNavigableSet synchronize(Object mutex) { return LongSets.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped NavigableSet that is unmodifiable
	 * @return a new NavigableSet Wrapper that is unmodifiable
	 * @see LongSets#unmodifiable
	 */
	public default LongNavigableSet unmodifiable() { return LongSets.unmodifiable(this); }
	
	/**
	 * A Type Specific Type Splititerator to reduce boxing/unboxing
	 * @return type specific splititerator
	 */
	@Override
	default LongSplititerator spliterator() { return LongSplititerators.createSplititerator(this, 0); }
	
	@Override
	@Deprecated
	public default Long lower(Long e) { return Long.valueOf(lower(e.longValue())); }
	@Override
	@Deprecated
	public default Long floor(Long e) { return Long.valueOf(floor(e.longValue())); }
	@Override
	@Deprecated
	public default Long ceiling(Long e) { return Long.valueOf(ceiling(e.longValue())); }
	@Override
	@Deprecated
	public default Long higher(Long e) { return Long.valueOf(higher(e.longValue())); }
	@Override
	@Deprecated
	default Long first() { return LongSortedSet.super.first(); }
	@Override
	@Deprecated
	default Long last() { return LongSortedSet.super.last(); }
	@Override
	@Deprecated
	public default Long pollFirst() { return isEmpty() ? null : Long.valueOf(pollFirstLong()); }
	@Override
	@Deprecated
	public default Long pollLast() { return isEmpty() ? null : Long.valueOf(pollLastLong()); }
	
	@Override
	@Deprecated
	public default LongNavigableSet subSet(Long fromElement, boolean fromInclusive, Long toElement, boolean toInclusive) { return subSet(fromElement.longValue(), fromInclusive, toElement.longValue(), toInclusive); }
	@Override
	@Deprecated
	public default LongNavigableSet headSet(Long toElement, boolean inclusive) { return headSet(toElement.longValue(), inclusive); }
	@Override
	@Deprecated
	public default LongNavigableSet tailSet(Long fromElement, boolean inclusive) { return tailSet(fromElement.longValue(), inclusive); }
	@Override
	@Deprecated
	public default LongSortedSet subSet(Long fromElement, Long toElement) { return LongSortedSet.super.subSet(fromElement, toElement); }
	@Override
	@Deprecated
	public default LongSortedSet headSet(Long toElement) { return LongSortedSet.super.headSet(toElement); }
	@Override
	@Deprecated
	public default LongSortedSet tailSet(Long fromElement) { return LongSortedSet.super.tailSet(fromElement); }

}