package speiger.src.collections.doubles.sets;

import java.util.NavigableSet;

import speiger.src.collections.doubles.collections.DoubleBidirectionalIterator;
import speiger.src.collections.doubles.collections.DoubleSplititerator;
import speiger.src.collections.doubles.utils.DoubleSets;
import speiger.src.collections.doubles.utils.DoubleSplititerators;

/**
 * A Type Specific Navigable Set interface with a couple helper methods
 */
public interface DoubleNavigableSet extends NavigableSet<Double>, DoubleSortedSet
{
	/**
	 * A Type Specific lower method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the greatest lower key that can be found
	 */
	public double lower(double key);
	/**
	 * A Type Specific higher method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the lowest higher key that can be found
	 */
	public double higher(double key);
	/**
	 * A Type Specific floor method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the greatest lower or equal key that can be found
	 */
	public double floor(double key);
	/**
	 * A Type Specific ceiling method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the lowest higher or equal key that can be found
	 */
	public double ceiling(double key);
	
	/**
	 * A Helper method to set the max value for SubSets. (Default: double.MIN_VALUE)
	 * @param e the new max value
	 */
	public void setDefaultMaxValue(double e);
	/**
	 * A Helper method to get the max value for SubSets.
	 * @return the default max value.
	 */
	public double getDefaultMaxValue();
	
	/**
	 * A Helper method to set the min value for SubSets. (Default: double.MAX_VALUE)
	 * @param e the new min value
	 */
	public void setDefaultMinValue(double e);
	/**
	 * A Helper method to get the min value for SubSets.
	 * @return the default min value.
	 */
	public double getDefaultMinValue();
	
	@Override
	public default DoubleNavigableSet subSet(double fromElement, double toElement) { return subSet(fromElement, true, toElement, false); }
	@Override
	public default DoubleNavigableSet headSet(double toElement) { return headSet(toElement, false); }
	@Override
	public default DoubleNavigableSet tailSet(double fromElement) { return tailSet(fromElement, true); }
	
	/**
	 * A Type Specific SubSet method to reduce boxing/unboxing
	 * @param fromElement where the SubSet should start
	 * @param fromInclusive if the fromElement is inclusive or not
	 * @param toElement where the SubSet should end
	 * @param toInclusive if the toElement is inclusive or not
	 * @return a SubSet that is within the range of the desired range
	 */
	public DoubleNavigableSet subSet(double fromElement, boolean fromInclusive, double toElement, boolean toInclusive);
	/**
	 * A Type Specific HeadSet method to reduce boxing/unboxing
	 * @param toElement where the HeadSet should end
	 * @param inclusive if the toElement is inclusive or not
	 * @return a HeadSet that is within the range of the desired range
	 */
	public DoubleNavigableSet headSet(double toElement, boolean inclusive);
	/**
	 * A Type Specific TailSet method to reduce boxing/unboxing
	 * @param fromElement where the TailSet should start
	 * @param inclusive if the fromElement is inclusive or not
	 * @return a TailSet that is within the range of the desired range
	 */
	public DoubleNavigableSet tailSet(double fromElement, boolean inclusive);
	
	/** @return a Type Specific iterator */
	@Override
	public DoubleBidirectionalIterator iterator();
	/** @return a Type Specific desendingIterator */
	@Override
	public DoubleBidirectionalIterator descendingIterator();
	/** @return a Type Specific desendingSet */
	@Override
	public DoubleNavigableSet descendingSet();
	@Override
	public DoubleNavigableSet copy();
	
	/**
	 * Creates a Wrapped NavigableSet that is Synchronized
	 * @return a new NavigableSet that is synchronized
	 * @see DoubleSets#synchronize
	 */
	public default DoubleNavigableSet synchronize() { return DoubleSets.synchronize(this); }
	
	/**
	 * Creates a Wrapped NavigableSet that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new NavigableSet Wrapper that is synchronized
	 * @see DoubleSets#synchronize
	 */
	public default DoubleNavigableSet synchronize(Object mutex) { return DoubleSets.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped NavigableSet that is unmodifiable
	 * @return a new NavigableSet Wrapper that is unmodifiable
	 * @see DoubleSets#unmodifiable
	 */
	public default DoubleNavigableSet unmodifiable() { return DoubleSets.unmodifiable(this); }
	
	/**
	 * A Type Specific Type Splititerator to reduce boxing/unboxing
	 * @return type specific splititerator
	 */
	@Override
	default DoubleSplititerator spliterator() { return DoubleSplititerators.createSplititerator(this, 0); }
	
	@Override
	@Deprecated
	public default Double lower(Double e) { return Double.valueOf(lower(e.doubleValue())); }
	@Override
	@Deprecated
	public default Double floor(Double e) { return Double.valueOf(floor(e.doubleValue())); }
	@Override
	@Deprecated
	public default Double ceiling(Double e) { return Double.valueOf(ceiling(e.doubleValue())); }
	@Override
	@Deprecated
	public default Double higher(Double e) { return Double.valueOf(higher(e.doubleValue())); }
	@Override
	@Deprecated
	default Double first() { return DoubleSortedSet.super.first(); }
	@Override
	@Deprecated
	default Double last() { return DoubleSortedSet.super.last(); }
	@Override
	@Deprecated
	public default Double pollFirst() { return isEmpty() ? null : Double.valueOf(pollFirstDouble()); }
	@Override
	@Deprecated
	public default Double pollLast() { return isEmpty() ? null : Double.valueOf(pollLastDouble()); }
	
	@Override
	@Deprecated
	public default DoubleNavigableSet subSet(Double fromElement, boolean fromInclusive, Double toElement, boolean toInclusive) { return subSet(fromElement.doubleValue(), fromInclusive, toElement.doubleValue(), toInclusive); }
	@Override
	@Deprecated
	public default DoubleNavigableSet headSet(Double toElement, boolean inclusive) { return headSet(toElement.doubleValue(), inclusive); }
	@Override
	@Deprecated
	public default DoubleNavigableSet tailSet(Double fromElement, boolean inclusive) { return tailSet(fromElement.doubleValue(), inclusive); }
	@Override
	@Deprecated
	public default DoubleSortedSet subSet(Double fromElement, Double toElement) { return DoubleSortedSet.super.subSet(fromElement, toElement); }
	@Override
	@Deprecated
	public default DoubleSortedSet headSet(Double toElement) { return DoubleSortedSet.super.headSet(toElement); }
	@Override
	@Deprecated
	public default DoubleSortedSet tailSet(Double fromElement) { return DoubleSortedSet.super.tailSet(fromElement); }

}