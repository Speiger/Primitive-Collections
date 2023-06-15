package speiger.src.collections.chars.sets;

import java.util.SortedSet;

import speiger.src.collections.chars.collections.CharBidirectionalIterator;
import speiger.src.collections.chars.collections.CharSplititerator;
import speiger.src.collections.chars.functions.CharComparator;
import speiger.src.collections.chars.utils.CharSets;
import speiger.src.collections.chars.utils.CharSplititerators;

/**
 * A Type Specific SortedSet implementation to reduce boxing/unboxing
 * with a couple extra methods that allow greater control over sets.
 * @note CharOrderedSet is only extended until 0.6.0 for Compat reasons.
 * The supported classes already implement CharOrderedSet directly and will remove CharSortedSet implementations in favor of CharOrderedSet instead
 */
public interface CharSortedSet extends CharSet, SortedSet<Character>
{
	/**
	 * A Type Specific Comparator method
	 * @return the type specific comparator
	 */
	@Override
	public CharComparator comparator();
	
	@Override
	public CharSortedSet copy();
	
	@Override
	public CharBidirectionalIterator iterator();
	/**
	 * A type Specific Iterator starting from a given key
	 * @param fromElement the element the iterator should start from
	 * @return a iterator starting from the given element
	 * @throws java.util.NoSuchElementException if fromElement isn't found
	 */
	public CharBidirectionalIterator iterator(char fromElement);
	
	/**
	 * Creates a Wrapped SortedSet that is Synchronized
	 * @return a new SortedSet that is synchronized
	 * @see CharSets#synchronize
	 */
	public default CharSortedSet synchronize() { return CharSets.synchronize(this); }
	
	/**
	 * Creates a Wrapped SortedSet that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new SortedSet Wrapper that is synchronized
	 * @see CharSets#synchronize
	 */
	public default CharSortedSet synchronize(Object mutex) { return CharSets.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped SortedSet that is unmodifiable
	 * @return a new SortedSet Wrapper that is unmodifiable
	 * @see CharSets#unmodifiable
	 */
	public default CharSortedSet unmodifiable() { return CharSets.unmodifiable(this); }
	
	/**
	 * A Type Specific Type Splititerator to reduce boxing/unboxing
	 * @return type specific splititerator
	 */
	@Override
	default CharSplititerator spliterator() { return CharSplititerators.createSplititerator(this, 0); }
	
	/**
	 * A Type Specific SubSet method to reduce boxing/unboxing
	 * @param fromElement where the SubSet should start
	 * @param toElement where the SubSet should end
	 * @return a SubSet that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public CharSortedSet subSet(char fromElement, char toElement);
	/**
	 * A Type Specific HeadSet method to reduce boxing/unboxing
	 * @param toElement where the HeadSet should end
	 * @return a HeadSet that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public CharSortedSet headSet(char toElement);
	/**
	 * A Type Specific TailSet method to reduce boxing/unboxing
	 * @param fromElement where the TailSet should start
	 * @return a TailSet that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public CharSortedSet tailSet(char fromElement);
	
	/**
	 * A method to get the first element in the set
	 * @return first element in the set
	 */
	public char firstChar();
	/**
	 * A method to get and remove the first element in the set
	 * @return first element in the set
	 */
	public char pollFirstChar();
	/**
	 * A method to get the last element in the set
	 * @return last element in the set
	 */
	public char lastChar();
	/**
	 * A method to get and remove the last element in the set
	 * @return last element in the set
	 */
	public char pollLastChar();
	
	@Override
	@Deprecated
	public default CharSortedSet subSet(Character fromElement, Character toElement) { return subSet(fromElement.charValue(), toElement.charValue()); }
	@Override
	@Deprecated
	public default CharSortedSet headSet(Character toElement) { return headSet(toElement.charValue()); }
	@Override
	@Deprecated
	public default CharSortedSet tailSet(Character fromElement) { return tailSet(fromElement.charValue()); }
	
	@Override
	@Deprecated
	public default Character first() { return Character.valueOf(firstChar()); }
	@Override
	@Deprecated
	default Character last() { return Character.valueOf(lastChar()); }
}