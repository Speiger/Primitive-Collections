package speiger.src.collections.chars.sets;

import java.util.NavigableSet;

import speiger.src.collections.chars.collections.CharBidirectionalIterator;
import speiger.src.collections.chars.collections.CharSplititerator;
import speiger.src.collections.chars.utils.CharSets;
import speiger.src.collections.chars.utils.CharSplititerators;

/**
 * A Type Specific Navigable Set interface with a couple helper methods
 */
public interface CharNavigableSet extends NavigableSet<Character>, CharSortedSet
{
	/**
	 * A Type Specific lower method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the greatest lower key that can be found
	 */
	public char lower(char key);
	/**
	 * A Type Specific higher method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the lowest higher key that can be found
	 */
	public char higher(char key);
	/**
	 * A Type Specific floor method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the greatest lower or equal key that can be found
	 */
	public char floor(char key);
	/**
	 * A Type Specific ceiling method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the lowest higher or equal key that can be found
	 */
	public char ceiling(char key);
	
	/**
	 * A Helper method to set the max value for SubSets. (Default: char.MIN_VALUE)
	 * @param e the new max value
	 */
	public void setDefaultMaxValue(char e);
	/**
	 * A Helper method to get the max value for SubSets.
	 * @return the default max value.
	 */
	public char getDefaultMaxValue();
	
	/**
	 * A Helper method to set the min value for SubSets. (Default: char.MAX_VALUE)
	 * @param e the new min value
	 */
	public void setDefaultMinValue(char e);
	/**
	 * A Helper method to get the min value for SubSets.
	 * @return the default min value.
	 */
	public char getDefaultMinValue();
	
	@Override
	public default CharNavigableSet subSet(char fromElement, char toElement) { return subSet(fromElement, true, toElement, false); }
	@Override
	public default CharNavigableSet headSet(char toElement) { return headSet(toElement, false); }
	@Override
	public default CharNavigableSet tailSet(char fromElement) { return tailSet(fromElement, true); }
	
	/**
	 * A Type Specific SubSet method to reduce boxing/unboxing
	 * @param fromElement where the SubSet should start
	 * @param fromInclusive if the fromElement is inclusive or not
	 * @param toElement where the SubSet should end
	 * @param toInclusive if the toElement is inclusive or not
	 * @return a SubSet that is within the range of the desired range
	 */
	public CharNavigableSet subSet(char fromElement, boolean fromInclusive, char toElement, boolean toInclusive);
	/**
	 * A Type Specific HeadSet method to reduce boxing/unboxing
	 * @param toElement where the HeadSet should end
	 * @param inclusive if the toElement is inclusive or not
	 * @return a HeadSet that is within the range of the desired range
	 */
	public CharNavigableSet headSet(char toElement, boolean inclusive);
	/**
	 * A Type Specific TailSet method to reduce boxing/unboxing
	 * @param fromElement where the TailSet should start
	 * @param inclusive if the fromElement is inclusive or not
	 * @return a TailSet that is within the range of the desired range
	 */
	public CharNavigableSet tailSet(char fromElement, boolean inclusive);
	
	/** @return a Type Specific iterator */
	@Override
	public CharBidirectionalIterator iterator();
	/** @return a Type Specific desendingIterator */
	@Override
	public CharBidirectionalIterator descendingIterator();
	/** @return a Type Specific desendingSet */
	@Override
	public CharNavigableSet descendingSet();
	@Override
	public CharNavigableSet copy();
	
	/**
	 * Creates a Wrapped NavigableSet that is Synchronized
	 * @return a new NavigableSet that is synchronized
	 * @see CharSets#synchronize
	 */
	public default CharNavigableSet synchronize() { return CharSets.synchronize(this); }
	
	/**
	 * Creates a Wrapped NavigableSet that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new NavigableSet Wrapper that is synchronized
	 * @see CharSets#synchronize
	 */
	public default CharNavigableSet synchronize(Object mutex) { return CharSets.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped NavigableSet that is unmodifiable
	 * @return a new NavigableSet Wrapper that is unmodifiable
	 * @see CharSets#unmodifiable
	 */
	public default CharNavigableSet unmodifiable() { return CharSets.unmodifiable(this); }
	
	/**
	 * A Type Specific Type Splititerator to reduce boxing/unboxing
	 * @return type specific splititerator
	 */
	@Override
	default CharSplititerator spliterator() { return CharSplititerators.createSplititerator(this, 0); }
	
	@Override
	@Deprecated
	public default Character lower(Character e) { return Character.valueOf(lower(e.charValue())); }
	@Override
	@Deprecated
	public default Character floor(Character e) { return Character.valueOf(floor(e.charValue())); }
	@Override
	@Deprecated
	public default Character ceiling(Character e) { return Character.valueOf(ceiling(e.charValue())); }
	@Override
	@Deprecated
	public default Character higher(Character e) { return Character.valueOf(higher(e.charValue())); }
	@Override
	@Deprecated
	default Character first() { return CharSortedSet.super.first(); }
	@Override
	@Deprecated
	default Character last() { return CharSortedSet.super.last(); }
	@Override
	@Deprecated
	public default Character pollFirst() { return isEmpty() ? null : Character.valueOf(pollFirstChar()); }
	@Override
	@Deprecated
	public default Character pollLast() { return isEmpty() ? null : Character.valueOf(pollLastChar()); }
	
	@Override
	@Deprecated
	public default CharNavigableSet subSet(Character fromElement, boolean fromInclusive, Character toElement, boolean toInclusive) { return subSet(fromElement.charValue(), fromInclusive, toElement.charValue(), toInclusive); }
	@Override
	@Deprecated
	public default CharNavigableSet headSet(Character toElement, boolean inclusive) { return headSet(toElement.charValue(), inclusive); }
	@Override
	@Deprecated
	public default CharNavigableSet tailSet(Character fromElement, boolean inclusive) { return tailSet(fromElement.charValue(), inclusive); }
	@Override
	@Deprecated
	public default CharSortedSet subSet(Character fromElement, Character toElement) { return CharSortedSet.super.subSet(fromElement, toElement); }
	@Override
	@Deprecated
	public default CharSortedSet headSet(Character toElement) { return CharSortedSet.super.headSet(toElement); }
	@Override
	@Deprecated
	public default CharSortedSet tailSet(Character fromElement) { return CharSortedSet.super.tailSet(fromElement); }

}