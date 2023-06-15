package speiger.src.collections.bytes.sets;

import java.util.SortedSet;

import speiger.src.collections.bytes.collections.ByteBidirectionalIterator;
import speiger.src.collections.bytes.collections.ByteSplititerator;
import speiger.src.collections.bytes.functions.ByteComparator;
import speiger.src.collections.bytes.utils.ByteSets;
import speiger.src.collections.bytes.utils.ByteSplititerators;

/**
 * A Type Specific SortedSet implementation to reduce boxing/unboxing
 * with a couple extra methods that allow greater control over sets.
 * @note ByteOrderedSet is only extended until 0.6.0 for Compat reasons.
 * The supported classes already implement ByteOrderedSet directly and will remove ByteSortedSet implementations in favor of ByteOrderedSet instead
 */
public interface ByteSortedSet extends ByteSet, SortedSet<Byte>
{
	/**
	 * A Type Specific Comparator method
	 * @return the type specific comparator
	 */
	@Override
	public ByteComparator comparator();
	
	@Override
	public ByteSortedSet copy();
	
	@Override
	public ByteBidirectionalIterator iterator();
	/**
	 * A type Specific Iterator starting from a given key
	 * @param fromElement the element the iterator should start from
	 * @return a iterator starting from the given element
	 * @throws java.util.NoSuchElementException if fromElement isn't found
	 */
	public ByteBidirectionalIterator iterator(byte fromElement);
	
	/**
	 * Creates a Wrapped SortedSet that is Synchronized
	 * @return a new SortedSet that is synchronized
	 * @see ByteSets#synchronize
	 */
	public default ByteSortedSet synchronize() { return ByteSets.synchronize(this); }
	
	/**
	 * Creates a Wrapped SortedSet that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new SortedSet Wrapper that is synchronized
	 * @see ByteSets#synchronize
	 */
	public default ByteSortedSet synchronize(Object mutex) { return ByteSets.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped SortedSet that is unmodifiable
	 * @return a new SortedSet Wrapper that is unmodifiable
	 * @see ByteSets#unmodifiable
	 */
	public default ByteSortedSet unmodifiable() { return ByteSets.unmodifiable(this); }
	
	/**
	 * A Type Specific Type Splititerator to reduce boxing/unboxing
	 * @return type specific splititerator
	 */
	@Override
	default ByteSplititerator spliterator() { return ByteSplititerators.createSplititerator(this, 0); }
	
	/**
	 * A Type Specific SubSet method to reduce boxing/unboxing
	 * @param fromElement where the SubSet should start
	 * @param toElement where the SubSet should end
	 * @return a SubSet that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public ByteSortedSet subSet(byte fromElement, byte toElement);
	/**
	 * A Type Specific HeadSet method to reduce boxing/unboxing
	 * @param toElement where the HeadSet should end
	 * @return a HeadSet that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public ByteSortedSet headSet(byte toElement);
	/**
	 * A Type Specific TailSet method to reduce boxing/unboxing
	 * @param fromElement where the TailSet should start
	 * @return a TailSet that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public ByteSortedSet tailSet(byte fromElement);
	
	/**
	 * A method to get the first element in the set
	 * @return first element in the set
	 */
	public byte firstByte();
	/**
	 * A method to get and remove the first element in the set
	 * @return first element in the set
	 */
	public byte pollFirstByte();
	/**
	 * A method to get the last element in the set
	 * @return last element in the set
	 */
	public byte lastByte();
	/**
	 * A method to get and remove the last element in the set
	 * @return last element in the set
	 */
	public byte pollLastByte();
	
	@Override
	@Deprecated
	public default ByteSortedSet subSet(Byte fromElement, Byte toElement) { return subSet(fromElement.byteValue(), toElement.byteValue()); }
	@Override
	@Deprecated
	public default ByteSortedSet headSet(Byte toElement) { return headSet(toElement.byteValue()); }
	@Override
	@Deprecated
	public default ByteSortedSet tailSet(Byte fromElement) { return tailSet(fromElement.byteValue()); }
	
	@Override
	@Deprecated
	public default Byte first() { return Byte.valueOf(firstByte()); }
	@Override
	@Deprecated
	default Byte last() { return Byte.valueOf(lastByte()); }
}