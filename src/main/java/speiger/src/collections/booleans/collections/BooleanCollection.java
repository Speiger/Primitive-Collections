package speiger.src.collections.booleans.collections;

import java.util.Collection;
import speiger.src.collections.booleans.functions.BooleanConsumer;
import speiger.src.collections.booleans.utils.BooleanSplititerators;
import speiger.src.collections.booleans.utils.BooleanCollections;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Type-Specific {@link Collection} that reduces (un)boxing
 */
public interface BooleanCollection extends Collection<Boolean>, BooleanIterable
{
	/**
	 * A Type-Specific add function to reduce (un)boxing
	 * @param o the element that should be added
	 * @return true if the element was added to the collection
	 */
	public boolean add(boolean o);
	
	/**
	 * A Type-Specific addAll function to reduce (un)boxing
	 * @param c the collection of elements that should be added
	 * @return true if elements were added into the collection
	 */
	public boolean addAll(BooleanCollection c);
	
	/**
	 * A Type-Specific Array based addAll method to reduce the amount of Wrapping
	 * @param e the elements that should be added
	 * @return if the collection was modified
	 */
	public default boolean addAll(boolean... e) { return addAll(e, 0, e.length); }
	
	/**
	 * A Type-Specific Array based addAll method to reduce the amount of Wrapping
	 * @param e the elements that should be added
	 * @param length how many elements of the array should be added
	 * @return if the collection was modified
	 */
	public default boolean addAll(boolean[] e, int length) { return addAll(e, 0, length); }
	
	/**
	 * A Type-Specific Array based addAll method to reduce the amount of Wrapping
	 * @param e the elements that should be added
	 * @param offset where to start within the array
	 * @param length how many elements of the array should be added
	 * @return if the collection was modified
	 */
	public default boolean addAll(boolean[] e, int offset, int length) {
		if(length <= 0) return false;
		SanityChecks.checkArrayCapacity(e.length, offset, length);
		boolean added = false;
		for(int i = 0;i<length;i++) {
			if(add(e[offset+i])) added = true;
		}
		return added;
	}
	
	/**
	 * A Type-Specific contains function to reduce (un)boxing
	 * @param o the element that is checked for
	 * @return true if the element is found in the collection
	 */
	public boolean contains(boolean o);
	
	/**
 	 * A Type-Specific containsAll function to reduce (un)boxing
 	 * @param c the collection of elements that should be tested for
 	 * @return true if all the element is found in the collection
 	 */
	public boolean containsAll(BooleanCollection c);
	
	/**
	 * A Type-Specific containsAny function to reduce (un)boxing
 	 * @param c the collection of elements that should be tested for
	 * @return true if any element was found
	 */
	public boolean containsAny(BooleanCollection c);
	
	/**
	 * Returns true if any element of the Collection is found in the provided collection.
	 * A Small Optimization function to find out of any element is present when comparing collections and not all of them.
 	 * @param c the collection of elements that should be tested for
	 * @return true if any element was found.
	 */
	@Deprecated
	public boolean containsAny(Collection<?> c);
	
	/**
	 * A Type-Specific remove function that reduces (un)boxing.
	 * @param o the element that should be removed
	 * @return true if the element was removed
	 * @see Collection#remove(Object)
	 */
	public boolean remBoolean(boolean o);
	
	/**
	 * A Type-Specific removeAll function that reduces (un)boxing.
	 * @param c the collection of elements that should be removed
	 * @return true if any element was removed
	 * @see Collection#removeAll(Collection)
	 */
	public boolean removeAll(BooleanCollection c);
	
	/**
	 * A Type-Specific removeAll function that reduces (un)boxing.
	 * It also notifies the remover of which exact element is going to be removed.
	 * @param c the collection of elements that should be removed
	 * @param r elements that got removed
	 * @return true if any element was removed
	 * @see Collection#removeAll(Collection)
	 */
	public boolean removeAll(BooleanCollection c, BooleanConsumer r);
	
	/**
 	 * A Type-Specific retainAll function that reduces (un)boxing.
	 * @param c the collection of elements that should be kept
 	 * @return true if any element was removed
 	 * @see Collection#retainAll(Collection)
 	 */
	public boolean retainAll(BooleanCollection c);
	
	/**
 	 * A Type-Specific retainAll function that reduces (un)boxing.
	 * It also notifies the remover of which exact element is going to be removed.
	 * @param c the collection of elements that should be kept
	 * @param r elements that got removed
 	 * @return true if any element was removed
 	 * @see Collection#retainAll(Collection)
 	 */
	public boolean retainAll(BooleanCollection c, BooleanConsumer r);
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to collect all elements
	 * @param collection that the elements should be inserted to
	 * @param <E> the collection type
	 * @return the input with the desired elements
	 */
	default <E extends BooleanCollection> E pour(E collection) {
		collection.addAll(this);
		return collection;
	}
	
	/**
	 * A Function that does a shallow clone of the Collection itself.
	 * This function is more optimized then a copy constructor since the Collection does not have to be unsorted/resorted.
	 * It can be compared to Cloneable but with less exception risk
	 * @return a Shallow Copy of the collection
	 * @note Wrappers and view collections will not support this feature
	 */
	public BooleanCollection copy();
	
	/**
	 * A Type-Specific toArray function that delegates to {@link #toBooleanArray(boolean[])} with a newly created array.
	 * @return an array containing all of the elements in this collection
	 * @see Collection#toArray()
	 */
	public boolean[] toBooleanArray();
	
	/**
	 * A Type-Specific toArray function that reduces (un)boxing.
	 * @param a array that the elements should be injected to. If null or to small a new array with the right size is created
	 * @return an array containing all of the elements in this collection
	 * @see Collection#toArray(Object[])
	 */
	public boolean[] toBooleanArray(boolean[] a);
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public default boolean add(Boolean o) { return add(o.booleanValue()); }
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public default boolean contains(Object o) { return o != null && contains(((Boolean)o).booleanValue()); }
	
	/** {@inheritDoc}
 	 * <p>This default implementation delegates to the corresponding type-specific function.
 	 * @deprecated Please use the corresponding type-specific function instead. 
 	 */
	@Override
	@Deprecated
	public default boolean remove(Object o) { return o != null && remBoolean(((Boolean)o).booleanValue()); }
	
	/**
	 * Returns a Type-Specific Iterator to reduce (un)boxing
	 * @return a iterator of the collection
	 * @see Collection#iterator()
	 */
	@Override
	public BooleanIterator iterator();
	
	/**
	 * Creates a Wrapped Collection that is Synchronized
	 * @return a new Collection that is synchronized
	 * @see BooleanCollections#synchronize
	 */
	public default BooleanCollection synchronize() { return BooleanCollections.synchronize(this); }
	
	/**
	 * Creates a Wrapped Collection that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new Collection Wrapper that is synchronized
	 * @see BooleanCollections#synchronize
	 */
	public default BooleanCollection synchronize(Object mutex) { return BooleanCollections.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped Collection that is unmodifiable
	 * @return a new Collection Wrapper that is unmodifiable
	 * @see BooleanCollections#unmodifiable
	 */
	public default BooleanCollection unmodifiable() { return BooleanCollections.unmodifiable(this); }
	
	/**
	 * A Type Specific Type Splititerator to reduce boxing/unboxing
	 * @return type specific splititerator
	 */
	@Override
	default BooleanSplititerator spliterator() { return BooleanSplititerators.createSplititerator(this, 0); }
}