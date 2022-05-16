package speiger.src.collections.floats.sets;

import java.util.SortedSet;

import speiger.src.collections.floats.collections.FloatBidirectionalIterator;
import speiger.src.collections.floats.collections.FloatSplititerator;
import speiger.src.collections.floats.functions.FloatComparator;
import speiger.src.collections.floats.utils.FloatSets;
import speiger.src.collections.floats.utils.FloatSplititerators;

/**
 * A Type Specific SortedSet implementation to reduce boxing/unboxing
 * with a couple extra methods that allow greater control over sets.
 * @note FloatOrderedSet is only extended until 0.6.0 for Compat reasons.
 * The supported classes already implement FloatOrderedSet directly and will remove FloatSortedSet implementations in favor of FloatOrderedSet instead
 */
public interface FloatSortedSet extends FloatSet, SortedSet<Float>
{
	/**
	 * A Type Specific Comparator method
	 * @return the type specific comparator
	 */
	@Override
	public FloatComparator comparator();
	
	@Override
	public FloatSortedSet copy();
	
	@Override
	public FloatBidirectionalIterator iterator();
	/**
	 * A type Specific Iterator starting from a given key
	 * @param fromElement the element the iterator should start from
	 * @return a iterator starting from the given element
	 * @throws java.util.NoSuchElementException if fromElement isn't found
	 */
	public FloatBidirectionalIterator iterator(float fromElement);
	
	/**
	 * Creates a Wrapped SortedSet that is Synchronized
	 * @return a new SortedSet that is synchronized
	 * @see FloatSets#synchronize
	 */
	public default FloatSortedSet synchronize() { return FloatSets.synchronize(this); }
	
	/**
	 * Creates a Wrapped SortedSet that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new SortedSet Wrapper that is synchronized
	 * @see FloatSets#synchronize
	 */
	public default FloatSortedSet synchronize(Object mutex) { return FloatSets.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped SortedSet that is unmodifiable
	 * @return a new SortedSet Wrapper that is unmodifiable
	 * @see FloatSets#unmodifiable
	 */
	public default FloatSortedSet unmodifiable() { return FloatSets.unmodifiable(this); }
	
	/**
	 * A Type Specific Type Splititerator to reduce boxing/unboxing
	 * @return type specific splititerator
	 */
	@Override
	default FloatSplititerator spliterator() { return FloatSplititerators.createSplititerator(this, 0); }
	
	/**
	 * A Type Specific SubSet method to reduce boxing/unboxing
	 * @param fromElement where the SubSet should start
	 * @param toElement where the SubSet should end
	 * @return a SubSet that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public FloatSortedSet subSet(float fromElement, float toElement);
	/**
	 * A Type Specific HeadSet method to reduce boxing/unboxing
	 * @param toElement where the HeadSet should end
	 * @return a HeadSet that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public FloatSortedSet headSet(float toElement);
	/**
	 * A Type Specific TailSet method to reduce boxing/unboxing
	 * @param fromElement where the TailSet should start
	 * @return a TailSet that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public FloatSortedSet tailSet(float fromElement);
	
	/**
	 * A method to get the first element in the set
	 * @return first element in the set
	 */
	public float firstFloat();
	/**
	 * A method to get and remove the first element in the set
	 * @return first element in the set
	 */
	public float pollFirstFloat();
	/**
	 * A method to get the last element in the set
	 * @return last element in the set
	 */
	public float lastFloat();
	/**
	 * A method to get and remove the last element in the set
	 * @return last element in the set
	 */
	public float pollLastFloat();
	
	@Override
	@Deprecated
	public default FloatSortedSet subSet(Float fromElement, Float toElement) { return subSet(fromElement.floatValue(), toElement.floatValue()); }
	@Override
	@Deprecated
	public default FloatSortedSet headSet(Float toElement) { return headSet(toElement.floatValue()); }
	@Override
	@Deprecated
	public default FloatSortedSet tailSet(Float fromElement) { return tailSet(fromElement.floatValue()); }
	
	@Override
	@Deprecated
	public default Float first() { return Float.valueOf(firstFloat()); }
	@Override
	@Deprecated
	default Float last() { return Float.valueOf(lastFloat()); }
}