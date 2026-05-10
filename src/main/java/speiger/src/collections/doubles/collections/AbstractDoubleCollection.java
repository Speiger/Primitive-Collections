package speiger.src.collections.doubles.collections;

import java.util.Collection;
import java.util.function.Supplier;
import java.util.Objects;
import java.util.AbstractCollection;

import speiger.src.collections.doubles.lists.DoubleListIterator;
import speiger.src.collections.doubles.functions.DoubleConsumer;
import speiger.src.collections.doubles.utils.DoubleIterators;
import speiger.src.collections.doubles.utils.DoubleArrays;

/**
 * Abstract Type Specific Collection that reduces boxing/unboxing
 */
public abstract class AbstractDoubleCollection extends AbstractCollection<Double> implements DoubleCollection
{
	@Override
	public abstract DoubleIterator iterator();
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public boolean add(Double e) { return DoubleCollection.super.add(e); }

	@Override
	public boolean addAll(DoubleCollection c) {
		boolean modified = false;
		for(DoubleIterator iter = c.iterator();iter.hasNext();modified |= add(iter.nextDouble()));
		return modified;
	}
	
	@Override
	public DoubleCollection copy() { throw new UnsupportedOperationException(); }
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public boolean contains(Object e) { return DoubleCollection.super.contains(e); }
	
	/**
	 * A Type-Specific implementation of contains. This implementation iterates over the elements and returns true if the value match.
	 * @param e the element that should be searched for.
	 * @return true if the value was found.
	 */
	@Override
	public boolean contains(double e) {
		for(DoubleIterator iter = iterator();iter.hasNext();) { if(Double.doubleToLongBits(iter.nextDouble()) == Double.doubleToLongBits(e)) return true; }
		return false;
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public boolean addAll(Collection<? extends Double> c)
	{
		return c instanceof DoubleCollection ? addAll((DoubleCollection)c) : super.addAll(c);
	}
	
	/**
	 * A Type-Specific implementation of containsAll. This implementation iterates over all elements and checks all elements are present in the other collection.
	 * @param c the collection that should be checked if it contains all elements.
	 * @return true if all elements were found in the collection
	 * @throws java.lang.NullPointerException if the collection is null
	 */
	@Override
	public boolean containsAll(DoubleCollection c) {
		Objects.requireNonNull(c);
		if(c.isEmpty()) return true;
		for(DoubleIterator iter = c.iterator();iter.hasNext();)
			if(!contains(iter.nextDouble()))
				return false;
		return true;
	}
	
	@Override
	public boolean containsAll(Collection<?> c) {
		Objects.requireNonNull(c);
		return c instanceof DoubleCollection ? containsAll((DoubleCollection)c) : super.containsAll(c);
	}
	
	/**
	 * This implementation iterates over the elements of the collection and checks if they are stored in this collection
	 * @param c the elements that should be checked for
	 * @return true if any element is in this collection
	 * @throws java.lang.NullPointerException if the collection is null
	 */
	@Override
	@Deprecated
	public boolean containsAny(Collection<?> c) {
		Objects.requireNonNull(c);
		if(c.isEmpty()) return false;
		for(Object e : c) 
			if(contains(e))
				return true;
		return false;
	}
	
	/**
	 * This implementation iterates over the elements of the collection and checks if they are stored in this collection.
	 * @param c the elements that should be checked for
	 * @return true if any element is in this collection
 	 * @throws java.lang.NullPointerException if the collection is null
	 */
	@Override
	public boolean containsAny(DoubleCollection c) {
		Objects.requireNonNull(c);
		if(c.isEmpty()) return false;
		for(DoubleIterator iter = c.iterator();iter.hasNext();) 
			if(contains(iter.nextDouble()))
				return true;
		return false;
	}
		
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public boolean remove(Object e) { return DoubleCollection.super.remove(e); }
	
	/**
	 * A Type-Specific implementation of remove. This implementation iterates over the elements until it finds the element that is searched for or it runs out of elements.
	 * It stops after finding the first element
	 * @param e the element that is searched for
	 * @return true if the element was found and removed.
	 */
	@Override
	public boolean remDouble(double e) {
		for(DoubleIterator iter = iterator();iter.hasNext();) {
			if(Double.doubleToLongBits(iter.nextDouble()) == Double.doubleToLongBits(e)) {
				iter.remove();
				return true;
			}
		}
		return false;
	}
	
	/**
	 * A Type-Specific implementation of removeAll. This Implementation iterates over all elements and removes them as they were found in the other collection.
	 * @param c the elements that should be deleted
	 * @return true if the collection was modified.
	 * @throws java.lang.NullPointerException if the collection is null
	 */
	@Override
	public boolean removeAll(DoubleCollection c) {
		Objects.requireNonNull(c);
		if(c.isEmpty()) return false;
		boolean modified = false;
		for(DoubleIterator iter = iterator();iter.hasNext();) {
			if(c.contains(iter.nextDouble())) {
				iter.remove();
				modified = true;
			}
		}
		return modified;
	}

	@Override
	public boolean removeAll(DoubleCollection c, DoubleConsumer r) {
		Objects.requireNonNull(c);
		if(c.isEmpty()) return false;
		Objects.requireNonNull(r);
		boolean modified = false;
		for(DoubleIterator iter = iterator();iter.hasNext();) {
			double e = iter.nextDouble();
			if(c.contains(e)) {
				r.accept(e);
				iter.remove();
				modified = true;
			}
		}
		return modified;
	}
	
	/**
	 * A Type-Specific implementation of retainAll. This Implementation iterates over all elements and removes them as they were not found in the other collection.
	 * @param c the elements that should be kept
	 * @return true if the collection was modified.
	 * @throws java.lang.NullPointerException if the collection is null
	 */
	@Override
	public boolean retainAll(DoubleCollection c) {
		Objects.requireNonNull(c);
		if(c.isEmpty()) {
			boolean modified = !isEmpty();
			clear();
			return modified;
		}
		boolean modified = false;
		for(DoubleIterator iter = iterator();iter.hasNext();) {
			if(!c.contains(iter.nextDouble())) {
				iter.remove();
				modified = true;
			}
		}
		return modified;
	}
	
	@Override
	public boolean retainAll(DoubleCollection c, DoubleConsumer r) {
		Objects.requireNonNull(c);
		Objects.requireNonNull(r);
		if(c.isEmpty()) {
			boolean modified = !isEmpty();
			forEach(r);
			clear();
			return modified;
		}
		boolean modified = false;
		for(DoubleIterator iter = iterator();iter.hasNext();) {
			double e = iter.nextDouble();
			if(!c.contains(e)) {
				r.accept(e);
				iter.remove();
				modified = true;
			}
		}
		return modified;
	}
	
	/**
	 * A Type-Specific implementation of toArray that links to {@link #toDoubleArray(double[])} with a newly created array.
	 * @return an array containing all of the elements in this collection
	 */
	@Override
	public double[] toDoubleArray() {
		if(isEmpty()) return DoubleArrays.EMPTY_ARRAY;
		return toDoubleArray(new double[size()]);
	}
	
	/**
	 * A Type-Specific implementation of toArray. This implementation iterates over all elements and unwraps them into primitive type.
	 * @param a array that the elements should be injected to. If null or to small a new array with the right size is created
	 * @return an array containing all of the elements in this collection
	 */
	@Override
	public double[] toDoubleArray(double[] a) {
		if(a == null || a.length < size()) a = new double[size()];
		DoubleIterators.unwrap(a, iterator());
		if (a.length > size()) a[size()] = 0D;
		return a;
	}

	public static class ReverseDoubleOrderedCollection extends AbstractDoubleCollection implements DoubleOrderedCollection {
		DoubleOrderedCollection collection;
		Supplier<DoubleIterator> reverseIterator;
		
		public ReverseDoubleOrderedCollection(DoubleOrderedCollection collection, Supplier<DoubleIterator> reverseIterator) {
			this.collection = collection;
			this.reverseIterator = reverseIterator;
		}
		
		@Override
		public boolean add(double o) { return collection.add(o); }
		@Override
		public DoubleOrderedCollection reversed() { return collection; }
		@Override
		public void addFirst(double e) { collection.addLast(e); }
		@Override
		public void addLast(double e) { collection.addFirst(e); }
		@Override
		public boolean contains(double e) { return collection.contains(e); }
		@Override
		public boolean remDouble(double e) { return collection.remDouble(e); }
		@Override
		public void clear() { collection.clear(); }
		@Override
		public double getFirstDouble() { return collection.getLastDouble(); }
		@Override
		public double removeFirstDouble() { return collection.removeLastDouble(); }
		@Override
		public double getLastDouble() { return collection.getFirstDouble(); }
		@Override
		public double removeLastDouble() { return collection.removeFirstDouble(); }
		@Override
		public DoubleIterator iterator() { return reverseIterator.get(); }
		@Override
		public int size() { return collection.size(); }
	}
	
	public static class ReverseBiIterator implements DoubleListIterator {
		DoubleListIterator it;
		
		public ReverseBiIterator(DoubleListIterator it) {
			this.it = it;
		}
		
		@Override
		public double nextDouble() { return it.previousDouble(); }
		@Override
		public boolean hasNext() { return it.hasPrevious(); }
		@Override
		public boolean hasPrevious() { return it.hasNext(); }
		@Override
		public double previousDouble() { return it.nextDouble(); }
		@Override
		public void remove() { it.remove(); }
		@Override
		public int nextIndex() { return it.previousIndex(); }
		@Override
		public int previousIndex() { return it.nextIndex(); }
		@Override
		public void set(double e) { it.set(e); }
		@Override
		public void add(double e) { it.add(e); }
	}
}