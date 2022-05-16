package speiger.src.collections.doubles.sets;

import speiger.src.collections.doubles.collections.DoubleBidirectionalIterator;
import speiger.src.collections.doubles.collections.DoubleSplititerator;
import speiger.src.collections.doubles.utils.DoubleSets;
import speiger.src.collections.doubles.utils.DoubleSplititerators;

/**
 * A Special Set Interface giving Access to some really usefull functions
 * The Idea behind this interface is to allow access to functions that give control to the Order of elements.
 * Since Linked implementations as examples can be reordered outside of the Insertion Order.
 * This interface provides basic access to such functions while also providing some Sorted/NaivgableSet implementations that still fit into here.
 * 
 */
public interface DoubleOrderedSet extends DoubleSet
{
	/**
	 * A customized add method that allows you to insert into the first index.
	 * @param o the element that should be inserted
	 * @return true if it was added
	 * @see java.util.Set#add(Object)
	 */
	public boolean addAndMoveToFirst(double o);
	/**
	 * A customized add method that allows you to insert into the last index.
	 * @param o the element that should be inserted
	 * @return true if it was added
	 * @see java.util.Set#add(Object)
	 */
	public boolean addAndMoveToLast(double o);
	
	/**
	 * A specific move method to move a given key to the first index.
	 * @param o that should be moved to the first index
	 * @return true if the value was moved.
	 * @note returns false if the value was not present in the first place
	 */
	public boolean moveToFirst(double o);
	/**
	 * A specific move method to move a given key to the last index.
	 * @param o that should be moved to the first last
	 * @return true if the value was moved.
	 * @note returns false if the value was not present in the first place
	 */
	public boolean moveToLast(double o);
	
	@Override
	public DoubleOrderedSet copy();
	
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
	 * A Type Specific Type Splititerator to reduce boxing/unboxing
	 * @return type specific splititerator
	 */
	@Override
	default DoubleSplititerator spliterator() { return DoubleSplititerators.createSplititerator(this, 0); }
	
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
	
	/**
	 * Creates a Wrapped OrderedSet that is Synchronized
	 * @return a new OrderedSet that is synchronized
	 * @see DoubleSets#synchronize
	 */
	public default DoubleOrderedSet synchronize() { return DoubleSets.synchronize(this); }
	
	/**
	 * Creates a Wrapped OrderedSet that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new OrderedSet Wrapper that is synchronized
	 * @see DoubleSets#synchronize
	 */
	public default DoubleOrderedSet synchronize(Object mutex) { return DoubleSets.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped OrderedSet that is unmodifiable
	 * @return a new OrderedSet Wrapper that is unmodifiable
	 * @see DoubleSets#unmodifiable
	 */
	public default DoubleOrderedSet unmodifiable() { return DoubleSets.unmodifiable(this); }
	
}