package speiger.src.collections.doubles.sets;

import java.util.SortedSet;

import speiger.src.collections.doubles.collections.DoubleBidirectionalIterator;
import speiger.src.collections.doubles.collections.DoubleSplititerator;
import speiger.src.collections.doubles.functions.DoubleComparator;
import speiger.src.collections.doubles.utils.DoubleSets;
import speiger.src.collections.doubles.utils.DoubleSplititerators;

/**
 * A Type Specific SortedSet implementation to reduce boxing/unboxing
 * with a couple extra methods that allow greater control over sets.
 * @note DoubleOrderedSet is only extended until 0.6.0 for Compat reasons.
 * The supported classes already implement DoubleOrderedSet directly and will remove DoubleSortedSet implementations in favor of DoubleOrderedSet instead
 */
public interface DoubleSortedSet extends DoubleSet, SortedSet<Double>
{
	/**
	 * A Type Specific Comparator method
	 * @return the type specific comparator
	 */
	@Override
	public DoubleComparator comparator();
	
	@Override
	public DoubleSortedSet copy();
	
	@Override
	public DoubleBidirectionalIterator iterator();
	/**
	 * A type Specific Iterator starting from a given key
	 * @param fromElement the element the iterator should start from
	 * @return a iterator starting from the given element
	 * @throws java.util.NoSuchElementException if fromElement isn't found
	 */
	public DoubleBidirectionalIterator iterator(double fromElement);
	
	/**
	 * Creates a Wrapped SortedSet that is Synchronized
	 * @return a new SortedSet that is synchronized
	 * @see DoubleSets#synchronize
	 */
	public default DoubleSortedSet synchronize() { return DoubleSets.synchronize(this); }
	
	/**
	 * Creates a Wrapped SortedSet that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new SortedSet Wrapper that is synchronized
	 * @see DoubleSets#synchronize
	 */
	public default DoubleSortedSet synchronize(Object mutex) { return DoubleSets.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped SortedSet that is unmodifiable
	 * @return a new SortedSet Wrapper that is unmodifiable
	 * @see DoubleSets#unmodifiable
	 */
	public default DoubleSortedSet unmodifiable() { return DoubleSets.unmodifiable(this); }
	
	/**
	 * A Type Specific Type Splititerator to reduce boxing/unboxing
	 * @return type specific splititerator
	 */
	@Override
	default DoubleSplititerator spliterator() { return DoubleSplititerators.createSplititerator(this, 0); }
	
	/**
	 * A Type Specific SubSet method to reduce boxing/unboxing
	 * @param fromElement where the SubSet should start
	 * @param toElement where the SubSet should end
	 * @return a SubSet that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public DoubleSortedSet subSet(double fromElement, double toElement);
	/**
	 * A Type Specific HeadSet method to reduce boxing/unboxing
	 * @param toElement where the HeadSet should end
	 * @return a HeadSet that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public DoubleSortedSet headSet(double toElement);
	/**
	 * A Type Specific TailSet method to reduce boxing/unboxing
	 * @param fromElement where the TailSet should start
	 * @return a TailSet that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public DoubleSortedSet tailSet(double fromElement);
	
	/**
	 * A method to get the first element in the set
	 * @return first element in the set
	 */
	public double firstDouble();
	/**
	 * A method to get and remove the first element in the set
	 * @return first element in the set
	 */
	public double pollFirstDouble();
	/**
	 * A method to get the last element in the set
	 * @return last element in the set
	 */
	public double lastDouble();
	/**
	 * A method to get and remove the last element in the set
	 * @return last element in the set
	 */
	public double pollLastDouble();
	
	@Override
	@Deprecated
	public default DoubleSortedSet subSet(Double fromElement, Double toElement) { return subSet(fromElement.doubleValue(), toElement.doubleValue()); }
	@Override
	@Deprecated
	public default DoubleSortedSet headSet(Double toElement) { return headSet(toElement.doubleValue()); }
	@Override
	@Deprecated
	public default DoubleSortedSet tailSet(Double fromElement) { return tailSet(fromElement.doubleValue()); }
	
	@Override
	@Deprecated
	public default Double first() { return Double.valueOf(firstDouble()); }
	@Override
	@Deprecated
	default Double last() { return Double.valueOf(lastDouble()); }
}