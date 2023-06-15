package speiger.src.collections.ints.sets;

import java.util.NavigableSet;

import speiger.src.collections.ints.collections.IntBidirectionalIterator;
import speiger.src.collections.ints.collections.IntSplititerator;
import speiger.src.collections.ints.utils.IntSets;
import speiger.src.collections.ints.utils.IntSplititerators;

/**
 * A Type Specific Navigable Set interface with a couple helper methods
 */
public interface IntNavigableSet extends NavigableSet<Integer>, IntSortedSet
{
	/**
	 * A Type Specific lower method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the greatest lower key that can be found
	 */
	public int lower(int key);
	/**
	 * A Type Specific higher method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the lowest higher key that can be found
	 */
	public int higher(int key);
	/**
	 * A Type Specific floor method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the greatest lower or equal key that can be found
	 */
	public int floor(int key);
	/**
	 * A Type Specific ceiling method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the lowest higher or equal key that can be found
	 */
	public int ceiling(int key);
	
	/**
	 * A Helper method to set the max value for SubSets. (Default: int.MIN_VALUE)
	 * @param e the new max value
	 */
	public void setDefaultMaxValue(int e);
	/**
	 * A Helper method to get the max value for SubSets.
	 * @return the default max value.
	 */
	public int getDefaultMaxValue();
	
	/**
	 * A Helper method to set the min value for SubSets. (Default: int.MAX_VALUE)
	 * @param e the new min value
	 */
	public void setDefaultMinValue(int e);
	/**
	 * A Helper method to get the min value for SubSets.
	 * @return the default min value.
	 */
	public int getDefaultMinValue();
	
	@Override
	public default IntNavigableSet subSet(int fromElement, int toElement) { return subSet(fromElement, true, toElement, false); }
	@Override
	public default IntNavigableSet headSet(int toElement) { return headSet(toElement, false); }
	@Override
	public default IntNavigableSet tailSet(int fromElement) { return tailSet(fromElement, true); }
	
	/**
	 * A Type Specific SubSet method to reduce boxing/unboxing
	 * @param fromElement where the SubSet should start
	 * @param fromInclusive if the fromElement is inclusive or not
	 * @param toElement where the SubSet should end
	 * @param toInclusive if the toElement is inclusive or not
	 * @return a SubSet that is within the range of the desired range
	 */
	public IntNavigableSet subSet(int fromElement, boolean fromInclusive, int toElement, boolean toInclusive);
	/**
	 * A Type Specific HeadSet method to reduce boxing/unboxing
	 * @param toElement where the HeadSet should end
	 * @param inclusive if the toElement is inclusive or not
	 * @return a HeadSet that is within the range of the desired range
	 */
	public IntNavigableSet headSet(int toElement, boolean inclusive);
	/**
	 * A Type Specific TailSet method to reduce boxing/unboxing
	 * @param fromElement where the TailSet should start
	 * @param inclusive if the fromElement is inclusive or not
	 * @return a TailSet that is within the range of the desired range
	 */
	public IntNavigableSet tailSet(int fromElement, boolean inclusive);
	
	/** @return a Type Specific iterator */
	@Override
	public IntBidirectionalIterator iterator();
	/** @return a Type Specific desendingIterator */
	@Override
	public IntBidirectionalIterator descendingIterator();
	/** @return a Type Specific desendingSet */
	@Override
	public IntNavigableSet descendingSet();
	@Override
	public IntNavigableSet copy();
	
	/**
	 * Creates a Wrapped NavigableSet that is Synchronized
	 * @return a new NavigableSet that is synchronized
	 * @see IntSets#synchronize
	 */
	public default IntNavigableSet synchronize() { return IntSets.synchronize(this); }
	
	/**
	 * Creates a Wrapped NavigableSet that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new NavigableSet Wrapper that is synchronized
	 * @see IntSets#synchronize
	 */
	public default IntNavigableSet synchronize(Object mutex) { return IntSets.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped NavigableSet that is unmodifiable
	 * @return a new NavigableSet Wrapper that is unmodifiable
	 * @see IntSets#unmodifiable
	 */
	public default IntNavigableSet unmodifiable() { return IntSets.unmodifiable(this); }
	
	/**
	 * A Type Specific Type Splititerator to reduce boxing/unboxing
	 * @return type specific splititerator
	 */
	@Override
	default IntSplititerator spliterator() { return IntSplititerators.createSplititerator(this, 0); }
	
	@Override
	@Deprecated
	public default Integer lower(Integer e) { return Integer.valueOf(lower(e.intValue())); }
	@Override
	@Deprecated
	public default Integer floor(Integer e) { return Integer.valueOf(floor(e.intValue())); }
	@Override
	@Deprecated
	public default Integer ceiling(Integer e) { return Integer.valueOf(ceiling(e.intValue())); }
	@Override
	@Deprecated
	public default Integer higher(Integer e) { return Integer.valueOf(higher(e.intValue())); }
	@Override
	@Deprecated
	default Integer first() { return IntSortedSet.super.first(); }
	@Override
	@Deprecated
	default Integer last() { return IntSortedSet.super.last(); }
	@Override
	@Deprecated
	public default Integer pollFirst() { return isEmpty() ? null : Integer.valueOf(pollFirstInt()); }
	@Override
	@Deprecated
	public default Integer pollLast() { return isEmpty() ? null : Integer.valueOf(pollLastInt()); }
	
	@Override
	@Deprecated
	public default IntNavigableSet subSet(Integer fromElement, boolean fromInclusive, Integer toElement, boolean toInclusive) { return subSet(fromElement.intValue(), fromInclusive, toElement.intValue(), toInclusive); }
	@Override
	@Deprecated
	public default IntNavigableSet headSet(Integer toElement, boolean inclusive) { return headSet(toElement.intValue(), inclusive); }
	@Override
	@Deprecated
	public default IntNavigableSet tailSet(Integer fromElement, boolean inclusive) { return tailSet(fromElement.intValue(), inclusive); }
	@Override
	@Deprecated
	public default IntSortedSet subSet(Integer fromElement, Integer toElement) { return IntSortedSet.super.subSet(fromElement, toElement); }
	@Override
	@Deprecated
	public default IntSortedSet headSet(Integer toElement) { return IntSortedSet.super.headSet(toElement); }
	@Override
	@Deprecated
	public default IntSortedSet tailSet(Integer fromElement) { return IntSortedSet.super.tailSet(fromElement); }

}