package speiger.src.collections.chars.sets;

import speiger.src.collections.chars.collections.CharBidirectionalIterator;
import speiger.src.collections.chars.collections.CharSplititerator;
import speiger.src.collections.chars.utils.CharSets;
import speiger.src.collections.chars.utils.CharSplititerators;

/**
 * A Special Set Interface giving Access to some really usefull functions
 * The Idea behind this interface is to allow access to functions that give control to the Order of elements.
 * Since Linked implementations as examples can be reordered outside of the Insertion Order.
 * This interface provides basic access to such functions while also providing some Sorted/NaivgableSet implementations that still fit into here.
 * 
 */
public interface CharOrderedSet extends CharSet
{
	/**
	 * A customized add method that allows you to insert into the first index.
	 * @param o the element that should be inserted
	 * @return true if it was added
	 * @see java.util.Set#add(Object)
	 */
	public boolean addAndMoveToFirst(char o);
	/**
	 * A customized add method that allows you to insert into the last index.
	 * @param o the element that should be inserted
	 * @return true if it was added
	 * @see java.util.Set#add(Object)
	 */
	public boolean addAndMoveToLast(char o);
	
	/**
	 * A specific move method to move a given key to the first index.
	 * @param o that should be moved to the first index
	 * @return true if the value was moved.
	 * @note returns false if the value was not present in the first place
	 */
	public boolean moveToFirst(char o);
	/**
	 * A specific move method to move a given key to the last index.
	 * @param o that should be moved to the first last
	 * @return true if the value was moved.
	 * @note returns false if the value was not present in the first place
	 */
	public boolean moveToLast(char o);
	
	@Override
	public CharOrderedSet copy();
	
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
	 * A Type Specific Type Splititerator to reduce boxing/unboxing
	 * @return type specific splititerator
	 */
	@Override
	default CharSplititerator spliterator() { return CharSplititerators.createSplititerator(this, 0); }
	
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
	
	/**
	 * Creates a Wrapped OrderedSet that is Synchronized
	 * @return a new OrderedSet that is synchronized
	 * @see CharSets#synchronize
	 */
	public default CharOrderedSet synchronize() { return CharSets.synchronize(this); }
	
	/**
	 * Creates a Wrapped OrderedSet that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new OrderedSet Wrapper that is synchronized
	 * @see CharSets#synchronize
	 */
	public default CharOrderedSet synchronize(Object mutex) { return CharSets.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped OrderedSet that is unmodifiable
	 * @return a new OrderedSet Wrapper that is unmodifiable
	 * @see CharSets#unmodifiable
	 */
	public default CharOrderedSet unmodifiable() { return CharSets.unmodifiable(this); }
	
}