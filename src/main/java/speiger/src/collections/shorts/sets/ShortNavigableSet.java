package speiger.src.collections.shorts.sets;

import java.util.NavigableSet;

import speiger.src.collections.shorts.collections.ShortBidirectionalIterator;
import speiger.src.collections.shorts.collections.ShortSplititerator;
import speiger.src.collections.shorts.utils.ShortSets;
import speiger.src.collections.shorts.utils.ShortSplititerators;

/**
 * A Type Specific Navigable Set interface with a couple helper methods
 */
public interface ShortNavigableSet extends NavigableSet<Short>, ShortSortedSet
{
	/**
	 * A Type Specific lower method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the greatest lower key that can be found
	 */
	public short lower(short key);
	/**
	 * A Type Specific higher method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the lowest higher key that can be found
	 */
	public short higher(short key);
	/**
	 * A Type Specific floor method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the greatest lower or equal key that can be found
	 */
	public short floor(short key);
	/**
	 * A Type Specific ceiling method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the lowest higher or equal key that can be found
	 */
	public short ceiling(short key);
	
	/**
	 * A Helper method to set the max value for SubSets. (Default: short.MIN_VALUE)
	 * @param e the new max value
	 */
	public void setDefaultMaxValue(short e);
	/**
	 * A Helper method to get the max value for SubSets.
	 * @return the default max value.
	 */
	public short getDefaultMaxValue();
	
	/**
	 * A Helper method to set the min value for SubSets. (Default: short.MAX_VALUE)
	 * @param e the new min value
	 */
	public void setDefaultMinValue(short e);
	/**
	 * A Helper method to get the min value for SubSets.
	 * @return the default min value.
	 */
	public short getDefaultMinValue();
	
	@Override
	public default ShortNavigableSet subSet(short fromElement, short toElement) { return subSet(fromElement, true, toElement, false); }
	@Override
	public default ShortNavigableSet headSet(short toElement) { return headSet(toElement, false); }
	@Override
	public default ShortNavigableSet tailSet(short fromElement) { return tailSet(fromElement, true); }
	
	/**
	 * A Type Specific SubSet method to reduce boxing/unboxing
	 * @param fromElement where the SubSet should start
	 * @param fromInclusive if the fromElement is inclusive or not
	 * @param toElement where the SubSet should end
	 * @param toInclusive if the toElement is inclusive or not
	 * @return a SubSet that is within the range of the desired range
	 */
	public ShortNavigableSet subSet(short fromElement, boolean fromInclusive, short toElement, boolean toInclusive);
	/**
	 * A Type Specific HeadSet method to reduce boxing/unboxing
	 * @param toElement where the HeadSet should end
	 * @param inclusive if the toElement is inclusive or not
	 * @return a HeadSet that is within the range of the desired range
	 */
	public ShortNavigableSet headSet(short toElement, boolean inclusive);
	/**
	 * A Type Specific TailSet method to reduce boxing/unboxing
	 * @param fromElement where the TailSet should start
	 * @param inclusive if the fromElement is inclusive or not
	 * @return a TailSet that is within the range of the desired range
	 */
	public ShortNavigableSet tailSet(short fromElement, boolean inclusive);
	
	/** @return a Type Specific iterator */
	@Override
	public ShortBidirectionalIterator iterator();
	/** @return a Type Specific desendingIterator */
	@Override
	public ShortBidirectionalIterator descendingIterator();
	/** @return a Type Specific desendingSet */
	@Override
	public ShortNavigableSet descendingSet();
	@Override
	public ShortNavigableSet copy();
	
	/**
	 * Creates a Wrapped NavigableSet that is Synchronized
	 * @return a new NavigableSet that is synchronized
	 * @see ShortSets#synchronize
	 */
	public default ShortNavigableSet synchronize() { return ShortSets.synchronize(this); }
	
	/**
	 * Creates a Wrapped NavigableSet that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new NavigableSet Wrapper that is synchronized
	 * @see ShortSets#synchronize
	 */
	public default ShortNavigableSet synchronize(Object mutex) { return ShortSets.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped NavigableSet that is unmodifiable
	 * @return a new NavigableSet Wrapper that is unmodifiable
	 * @see ShortSets#unmodifiable
	 */
	public default ShortNavigableSet unmodifiable() { return ShortSets.unmodifiable(this); }
	
	/**
	 * A Type Specific Type Splititerator to reduce boxing/unboxing
	 * @return type specific splititerator
	 */
	@Override
	default ShortSplititerator spliterator() { return ShortSplititerators.createSplititerator(this, 0); }
	
	@Override
	@Deprecated
	public default Short lower(Short e) { return Short.valueOf(lower(e.shortValue())); }
	@Override
	@Deprecated
	public default Short floor(Short e) { return Short.valueOf(floor(e.shortValue())); }
	@Override
	@Deprecated
	public default Short ceiling(Short e) { return Short.valueOf(ceiling(e.shortValue())); }
	@Override
	@Deprecated
	public default Short higher(Short e) { return Short.valueOf(higher(e.shortValue())); }
	@Override
	@Deprecated
	default Short first() { return ShortSortedSet.super.first(); }
	@Override
	@Deprecated
	default Short last() { return ShortSortedSet.super.last(); }
	@Override
	@Deprecated
	public default Short pollFirst() { return isEmpty() ? null : Short.valueOf(pollFirstShort()); }
	@Override
	@Deprecated
	public default Short pollLast() { return isEmpty() ? null : Short.valueOf(pollLastShort()); }
	
	@Override
	@Deprecated
	public default ShortNavigableSet subSet(Short fromElement, boolean fromInclusive, Short toElement, boolean toInclusive) { return subSet(fromElement.shortValue(), fromInclusive, toElement.shortValue(), toInclusive); }
	@Override
	@Deprecated
	public default ShortNavigableSet headSet(Short toElement, boolean inclusive) { return headSet(toElement.shortValue(), inclusive); }
	@Override
	@Deprecated
	public default ShortNavigableSet tailSet(Short fromElement, boolean inclusive) { return tailSet(fromElement.shortValue(), inclusive); }
	@Override
	@Deprecated
	public default ShortSortedSet subSet(Short fromElement, Short toElement) { return ShortSortedSet.super.subSet(fromElement, toElement); }
	@Override
	@Deprecated
	public default ShortSortedSet headSet(Short toElement) { return ShortSortedSet.super.headSet(toElement); }
	@Override
	@Deprecated
	public default ShortSortedSet tailSet(Short fromElement) { return ShortSortedSet.super.tailSet(fromElement); }

}