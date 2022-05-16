package speiger.src.collections.bytes.sets;

import java.util.NavigableSet;

import speiger.src.collections.bytes.collections.ByteBidirectionalIterator;
import speiger.src.collections.bytes.collections.ByteSplititerator;
import speiger.src.collections.bytes.utils.ByteSets;
import speiger.src.collections.bytes.utils.ByteSplititerators;

/**
 * A Type Specific Navigable Set interface with a couple helper methods
 */
public interface ByteNavigableSet extends NavigableSet<Byte>, ByteSortedSet
{
	/**
	 * A Type Specific lower method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the greatest lower key that can be found
	 */
	public byte lower(byte key);
	/**
	 * A Type Specific higher method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the lowest higher key that can be found
	 */
	public byte higher(byte key);
	/**
	 * A Type Specific floor method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the greatest lower or equal key that can be found
	 */
	public byte floor(byte key);
	/**
	 * A Type Specific ceiling method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the lowest higher or equal key that can be found
	 */
	public byte ceiling(byte key);
	
	/**
	 * A Helper method to set the max value for SubSets. (Default: byte.MIN_VALUE)
	 * @param e the new max value
	 */
	public void setDefaultMaxValue(byte e);
	/**
	 * A Helper method to get the max value for SubSets.
	 * @return the default max value.
	 */
	public byte getDefaultMaxValue();
	
	/**
	 * A Helper method to set the min value for SubSets. (Default: byte.MAX_VALUE)
	 * @param e the new min value
	 */
	public void setDefaultMinValue(byte e);
	/**
	 * A Helper method to get the min value for SubSets.
	 * @return the default min value.
	 */
	public byte getDefaultMinValue();
	
	@Override
	public default ByteNavigableSet subSet(byte fromElement, byte toElement) { return subSet(fromElement, true, toElement, false); }
	@Override
	public default ByteNavigableSet headSet(byte toElement) { return headSet(toElement, false); }
	@Override
	public default ByteNavigableSet tailSet(byte fromElement) { return tailSet(fromElement, true); }
	
	/**
	 * A Type Specific SubSet method to reduce boxing/unboxing
	 * @param fromElement where the SubSet should start
	 * @param fromInclusive if the fromElement is inclusive or not
	 * @param toElement where the SubSet should end
	 * @param toInclusive if the toElement is inclusive or not
	 * @return a SubSet that is within the range of the desired range
	 */
	public ByteNavigableSet subSet(byte fromElement, boolean fromInclusive, byte toElement, boolean toInclusive);
	/**
	 * A Type Specific HeadSet method to reduce boxing/unboxing
	 * @param toElement where the HeadSet should end
	 * @param inclusive if the toElement is inclusive or not
	 * @return a HeadSet that is within the range of the desired range
	 */
	public ByteNavigableSet headSet(byte toElement, boolean inclusive);
	/**
	 * A Type Specific TailSet method to reduce boxing/unboxing
	 * @param fromElement where the TailSet should start
	 * @param inclusive if the fromElement is inclusive or not
	 * @return a TailSet that is within the range of the desired range
	 */
	public ByteNavigableSet tailSet(byte fromElement, boolean inclusive);
	
	/** @return a Type Specific iterator */
	@Override
	public ByteBidirectionalIterator iterator();
	/** @return a Type Specific desendingIterator */
	@Override
	public ByteBidirectionalIterator descendingIterator();
	/** @return a Type Specific desendingSet */
	@Override
	public ByteNavigableSet descendingSet();
	@Override
	public ByteNavigableSet copy();
	
	/**
	 * Creates a Wrapped NavigableSet that is Synchronized
	 * @return a new NavigableSet that is synchronized
	 * @see ByteSets#synchronize
	 */
	public default ByteNavigableSet synchronize() { return ByteSets.synchronize(this); }
	
	/**
	 * Creates a Wrapped NavigableSet that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new NavigableSet Wrapper that is synchronized
	 * @see ByteSets#synchronize
	 */
	public default ByteNavigableSet synchronize(Object mutex) { return ByteSets.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped NavigableSet that is unmodifiable
	 * @return a new NavigableSet Wrapper that is unmodifiable
	 * @see ByteSets#unmodifiable
	 */
	public default ByteNavigableSet unmodifiable() { return ByteSets.unmodifiable(this); }
	
	/**
	 * A Type Specific Type Splititerator to reduce boxing/unboxing
	 * @return type specific splititerator
	 */
	@Override
	default ByteSplititerator spliterator() { return ByteSplititerators.createSplititerator(this, 0); }
	
	@Override
	@Deprecated
	public default Byte lower(Byte e) { return Byte.valueOf(lower(e.byteValue())); }
	@Override
	@Deprecated
	public default Byte floor(Byte e) { return Byte.valueOf(floor(e.byteValue())); }
	@Override
	@Deprecated
	public default Byte ceiling(Byte e) { return Byte.valueOf(ceiling(e.byteValue())); }
	@Override
	@Deprecated
	public default Byte higher(Byte e) { return Byte.valueOf(higher(e.byteValue())); }
	@Override
	@Deprecated
	default Byte first() { return ByteSortedSet.super.first(); }
	@Override
	@Deprecated
	default Byte last() { return ByteSortedSet.super.last(); }
	@Override
	@Deprecated
	public default Byte pollFirst() { return isEmpty() ? null : Byte.valueOf(pollFirstByte()); }
	@Override
	@Deprecated
	public default Byte pollLast() { return isEmpty() ? null : Byte.valueOf(pollLastByte()); }
	
	@Override
	@Deprecated
	public default ByteNavigableSet subSet(Byte fromElement, boolean fromInclusive, Byte toElement, boolean toInclusive) { return subSet(fromElement.byteValue(), fromInclusive, toElement.byteValue(), toInclusive); }
	@Override
	@Deprecated
	public default ByteNavigableSet headSet(Byte toElement, boolean inclusive) { return headSet(toElement.byteValue(), inclusive); }
	@Override
	@Deprecated
	public default ByteNavigableSet tailSet(Byte fromElement, boolean inclusive) { return tailSet(fromElement.byteValue(), inclusive); }
	@Override
	@Deprecated
	public default ByteSortedSet subSet(Byte fromElement, Byte toElement) { return ByteSortedSet.super.subSet(fromElement, toElement); }
	@Override
	@Deprecated
	public default ByteSortedSet headSet(Byte toElement) { return ByteSortedSet.super.headSet(toElement); }
	@Override
	@Deprecated
	public default ByteSortedSet tailSet(Byte fromElement) { return ByteSortedSet.super.tailSet(fromElement); }

}