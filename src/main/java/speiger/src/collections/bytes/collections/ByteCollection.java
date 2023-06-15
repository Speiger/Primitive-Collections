package speiger.src.collections.bytes.collections;

import java.util.Collection;
import java.util.Objects;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;
import speiger.src.collections.bytes.functions.ByteConsumer;
import speiger.src.collections.bytes.utils.ByteSplititerators;
import speiger.src.collections.bytes.utils.ByteCollections;
import speiger.src.collections.utils.ISizeProvider;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Type-Specific {@link Collection} that reduces (un)boxing
 */
public interface ByteCollection extends Collection<Byte>, ByteIterable, ISizeProvider
{
	/**
	 * A Type-Specific add function to reduce (un)boxing
	 * @param o the element that should be added
	 * @return true if the element was added to the collection
	 */
	public boolean add(byte o);
	
	/**
	 * A Type-Specific addAll function to reduce (un)boxing
	 * @param c the collection of elements that should be added
	 * @return true if elements were added into the collection
	 */
	public boolean addAll(ByteCollection c);
	
	/**
	 * A Type-Specific Array based addAll method to reduce the amount of Wrapping
	 * @param e the elements that should be added
	 * @return if the collection was modified
	 */
	public default boolean addAll(byte... e) { return addAll(e, 0, e.length); }
	
	/**
	 * A Type-Specific Array based addAll method to reduce the amount of Wrapping
	 * @param e the elements that should be added
	 * @param length how many elements of the array should be added
	 * @return if the collection was modified
	 */
	public default boolean addAll(byte[] e, int length) { return addAll(e, 0, length); }
	
	/**
	 * A Type-Specific Array based addAll method to reduce the amount of Wrapping
	 * @param e the elements that should be added
	 * @param offset where to start within the array
	 * @param length how many elements of the array should be added
	 * @return if the collection was modified
	 */
	public default boolean addAll(byte[] e, int offset, int length) {
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
	public boolean contains(byte o);
	
	/**
 	 * A Type-Specific containsAll function to reduce (un)boxing
 	 * @param c the collection of elements that should be tested for
 	 * @return true if all the element is found in the collection
 	 */
	public boolean containsAll(ByteCollection c);
	
	/**
	 * A Type-Specific containsAny function to reduce (un)boxing
 	 * @param c the collection of elements that should be tested for
	 * @return true if any element was found
	 */
	public boolean containsAny(ByteCollection c);
	
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
	public boolean remByte(byte o);
	
	/**
	 * A Type-Specific removeAll function that reduces (un)boxing.
	 * @param c the collection of elements that should be removed
	 * @return true if any element was removed
	 * @see Collection#removeAll(Collection)
	 */
	public boolean removeAll(ByteCollection c);
	
	/**
	 * A Type-Specific removeAll function that reduces (un)boxing.
	 * It also notifies the remover of which exact element is going to be removed.
	 * @param c the collection of elements that should be removed
	 * @param r elements that got removed
	 * @return true if any element was removed
	 * @see Collection#removeAll(Collection)
	 */
	public boolean removeAll(ByteCollection c, ByteConsumer r);
	
	/**
 	 * A Type-Specific retainAll function that reduces (un)boxing.
	 * @param c the collection of elements that should be kept
 	 * @return true if any element was removed
 	 * @see Collection#retainAll(Collection)
 	 */
	public boolean retainAll(ByteCollection c);
	
	/**
 	 * A Type-Specific retainAll function that reduces (un)boxing.
	 * It also notifies the remover of which exact element is going to be removed.
	 * @param c the collection of elements that should be kept
	 * @param r elements that got removed
 	 * @return true if any element was removed
 	 * @see Collection#retainAll(Collection)
 	 */
	public boolean retainAll(ByteCollection c, ByteConsumer r);
	
	/**
	 * A Helper function to reduce the usage of Streams and allows to collect all elements
	 * @param collection that the elements should be inserted to
	 * @param <E> the collection type
	 * @return the input with the desired elements
	 */
	default <E extends ByteCollection> E pour(E collection) {
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
	public ByteCollection copy();
	
	/**
	 * A Type-Specific toArray function that delegates to {@link #toByteArray(byte[])} with a newly created array.
	 * @return an array containing all of the elements in this collection
	 * @see Collection#toArray()
	 */
	public byte[] toByteArray();
	
	/**
	 * A Type-Specific toArray function that reduces (un)boxing.
	 * @param a array that the elements should be injected to. If null or to small a new array with the right size is created
	 * @return an array containing all of the elements in this collection
	 * @see Collection#toArray(Object[])
	 */
	public byte[] toByteArray(byte[] a);
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public default boolean removeIf(Predicate<? super Byte> filter) {
		Objects.requireNonNull(filter);
		return remIf(v -> filter.test(Byte.valueOf(SanityChecks.castToByte(v))));
	}
	
	/**
	 * A Type-Specific removeIf function to reduce (un)boxing.
	 * <p>Removes elements that were selected by the filter
	 * @see Collection#removeIf(Predicate)
	 * @param filter Filters the elements that should be removed
	 * @return true if the collection was modified
	 * @throws java.lang.NullPointerException if filter is null
	 */
	public default boolean remIf(IntPredicate filter) {
		Objects.requireNonNull(filter);
		boolean removed = false;
		final ByteIterator each = iterator();
		while (each.hasNext()) {
			if (filter.test(each.nextByte())) {
				each.remove();
				removed = true;
			}
		}
		return removed;
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public default boolean add(Byte o) { return add(o.byteValue()); }
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public default boolean contains(Object o) { return o != null && contains(((Byte)o).byteValue()); }
	
	/** {@inheritDoc}
 	 * <p>This default implementation delegates to the corresponding type-specific function.
 	 * @deprecated Please use the corresponding type-specific function instead. 
 	 */
	@Override
	@Deprecated
	public default boolean remove(Object o) { return o != null && remByte(((Byte)o).byteValue()); }
	
	/**
	 * Returns a Type-Specific Iterator to reduce (un)boxing
	 * @return a iterator of the collection
	 * @see Collection#iterator()
	 */
	@Override
	public ByteIterator iterator();
	
	/**
	 * Creates a Wrapped Collection that is Synchronized
	 * @return a new Collection that is synchronized
	 * @see ByteCollections#synchronize
	 */
	public default ByteCollection synchronize() { return ByteCollections.synchronize(this); }
	
	/**
	 * Creates a Wrapped Collection that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new Collection Wrapper that is synchronized
	 * @see ByteCollections#synchronize
	 */
	public default ByteCollection synchronize(Object mutex) { return ByteCollections.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped Collection that is unmodifiable
	 * @return a new Collection Wrapper that is unmodifiable
	 * @see ByteCollections#unmodifiable
	 */
	public default ByteCollection unmodifiable() { return ByteCollections.unmodifiable(this); }
	
	/**
	 * Returns a Java-Type-Specific Stream to reduce boxing/unboxing.
	 * @return a Stream of the closest java type
	 */
	default IntStream primitiveStream() { return StreamSupport.intStream(ByteSplititerators.createJavaSplititerator(this, 0), false); }
	
	/**
	 * Returns a Java-Type-Specific Parallel Stream to reduce boxing/unboxing.
	 * @return a Stream of the closest java type
	 */
	default IntStream parallelPrimitiveStream() { return StreamSupport.intStream(ByteSplititerators.createJavaSplititerator(this, 0), true); }
	
	/**
	 * A Type Specific Type Splititerator to reduce boxing/unboxing
	 * @return type specific splititerator
	 */
	@Override
	default ByteSplititerator spliterator() { return ByteSplititerators.createSplititerator(this, 0); }
}