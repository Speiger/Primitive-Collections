package speiger.src.collections.floats.sets;

import java.util.NavigableSet;

import speiger.src.collections.floats.collections.FloatBidirectionalIterator;
import speiger.src.collections.floats.collections.FloatSplititerator;
import speiger.src.collections.floats.utils.FloatSets;
import speiger.src.collections.floats.utils.FloatSplititerators;

/**
 * A Type Specific Navigable Set interface with a couple helper methods
 */
public interface FloatNavigableSet extends NavigableSet<Float>, FloatSortedSet
{
	/**
	 * A Type Specific lower method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the greatest lower key that can be found
	 */
	public float lower(float key);
	/**
	 * A Type Specific higher method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the lowest higher key that can be found
	 */
	public float higher(float key);
	/**
	 * A Type Specific floor method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the greatest lower or equal key that can be found
	 */
	public float floor(float key);
	/**
	 * A Type Specific ceiling method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the lowest higher or equal key that can be found
	 */
	public float ceiling(float key);
	
	/**
	 * A Helper method to set the max value for SubSets. (Default: float.MIN_VALUE)
	 * @param e the new max value
	 */
	public void setDefaultMaxValue(float e);
	/**
	 * A Helper method to get the max value for SubSets.
	 * @return the default max value.
	 */
	public float getDefaultMaxValue();
	
	/**
	 * A Helper method to set the min value for SubSets. (Default: float.MAX_VALUE)
	 * @param e the new min value
	 */
	public void setDefaultMinValue(float e);
	/**
	 * A Helper method to get the min value for SubSets.
	 * @return the default min value.
	 */
	public float getDefaultMinValue();
	
	@Override
	public default FloatNavigableSet subSet(float fromElement, float toElement) { return subSet(fromElement, true, toElement, false); }
	@Override
	public default FloatNavigableSet headSet(float toElement) { return headSet(toElement, false); }
	@Override
	public default FloatNavigableSet tailSet(float fromElement) { return tailSet(fromElement, true); }
	
	/**
	 * A Type Specific SubSet method to reduce boxing/unboxing
	 * @param fromElement where the SubSet should start
	 * @param fromInclusive if the fromElement is inclusive or not
	 * @param toElement where the SubSet should end
	 * @param toInclusive if the toElement is inclusive or not
	 * @return a SubSet that is within the range of the desired range
	 */
	public FloatNavigableSet subSet(float fromElement, boolean fromInclusive, float toElement, boolean toInclusive);
	/**
	 * A Type Specific HeadSet method to reduce boxing/unboxing
	 * @param toElement where the HeadSet should end
	 * @param inclusive if the toElement is inclusive or not
	 * @return a HeadSet that is within the range of the desired range
	 */
	public FloatNavigableSet headSet(float toElement, boolean inclusive);
	/**
	 * A Type Specific TailSet method to reduce boxing/unboxing
	 * @param fromElement where the TailSet should start
	 * @param inclusive if the fromElement is inclusive or not
	 * @return a TailSet that is within the range of the desired range
	 */
	public FloatNavigableSet tailSet(float fromElement, boolean inclusive);
	
	/** @return a Type Specific iterator */
	@Override
	public FloatBidirectionalIterator iterator();
	/** @return a Type Specific desendingIterator */
	@Override
	public FloatBidirectionalIterator descendingIterator();
	/** @return a Type Specific desendingSet */
	@Override
	public FloatNavigableSet descendingSet();
	@Override
	public FloatNavigableSet copy();
	
	/**
	 * Creates a Wrapped NavigableSet that is Synchronized
	 * @return a new NavigableSet that is synchronized
	 * @see FloatSets#synchronize
	 */
	public default FloatNavigableSet synchronize() { return FloatSets.synchronize(this); }
	
	/**
	 * Creates a Wrapped NavigableSet that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new NavigableSet Wrapper that is synchronized
	 * @see FloatSets#synchronize
	 */
	public default FloatNavigableSet synchronize(Object mutex) { return FloatSets.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped NavigableSet that is unmodifiable
	 * @return a new NavigableSet Wrapper that is unmodifiable
	 * @see FloatSets#unmodifiable
	 */
	public default FloatNavigableSet unmodifiable() { return FloatSets.unmodifiable(this); }
	
	/**
	 * A Type Specific Type Splititerator to reduce boxing/unboxing
	 * @return type specific splititerator
	 */
	@Override
	default FloatSplititerator spliterator() { return FloatSplititerators.createSplititerator(this, 0); }
	
	@Override
	@Deprecated
	public default Float lower(Float e) { return Float.valueOf(lower(e.floatValue())); }
	@Override
	@Deprecated
	public default Float floor(Float e) { return Float.valueOf(floor(e.floatValue())); }
	@Override
	@Deprecated
	public default Float ceiling(Float e) { return Float.valueOf(ceiling(e.floatValue())); }
	@Override
	@Deprecated
	public default Float higher(Float e) { return Float.valueOf(higher(e.floatValue())); }
	@Override
	@Deprecated
	default Float first() { return FloatSortedSet.super.first(); }
	@Override
	@Deprecated
	default Float last() { return FloatSortedSet.super.last(); }
	@Override
	@Deprecated
	public default Float pollFirst() { return isEmpty() ? null : Float.valueOf(pollFirstFloat()); }
	@Override
	@Deprecated
	public default Float pollLast() { return isEmpty() ? null : Float.valueOf(pollLastFloat()); }
	
	@Override
	@Deprecated
	public default FloatNavigableSet subSet(Float fromElement, boolean fromInclusive, Float toElement, boolean toInclusive) { return subSet(fromElement.floatValue(), fromInclusive, toElement.floatValue(), toInclusive); }
	@Override
	@Deprecated
	public default FloatNavigableSet headSet(Float toElement, boolean inclusive) { return headSet(toElement.floatValue(), inclusive); }
	@Override
	@Deprecated
	public default FloatNavigableSet tailSet(Float fromElement, boolean inclusive) { return tailSet(fromElement.floatValue(), inclusive); }
	@Override
	@Deprecated
	public default FloatSortedSet subSet(Float fromElement, Float toElement) { return FloatSortedSet.super.subSet(fromElement, toElement); }
	@Override
	@Deprecated
	public default FloatSortedSet headSet(Float toElement) { return FloatSortedSet.super.headSet(toElement); }
	@Override
	@Deprecated
	public default FloatSortedSet tailSet(Float fromElement) { return FloatSortedSet.super.tailSet(fromElement); }

}