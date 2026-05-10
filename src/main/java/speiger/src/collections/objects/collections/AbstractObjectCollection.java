package speiger.src.collections.objects.collections;

import java.util.Collection;
import java.util.function.Supplier;
import java.util.Objects;
import java.util.AbstractCollection;
import java.util.function.Consumer;

import speiger.src.collections.objects.lists.ObjectListIterator;

/**
 * Abstract Type Specific Collection that reduces boxing/unboxing
 * @param <T> the keyType of elements maintained by this Collection
 */
public abstract class AbstractObjectCollection<T> extends AbstractCollection<T> implements ObjectCollection<T>
{
	@Override
	public abstract ObjectIterator<T> iterator();
	
	@Override
	public boolean addAll(ObjectCollection<T> c) {
		boolean modified = false;
		for(ObjectIterator<T> iter = c.iterator();iter.hasNext();modified |= add(iter.next()));
		return modified;
	}
	
	@Override
	public ObjectCollection<T> copy() { throw new UnsupportedOperationException(); }
	
	
	/**
	 * A Type-Specific implementation of containsAll. This implementation iterates over all elements and checks all elements are present in the other collection.
	 * @param c the collection that should be checked if it contains all elements.
	 * @return true if all elements were found in the collection
	 * @throws java.lang.NullPointerException if the collection is null
	 */
	@Override
	public boolean containsAll(ObjectCollection<T> c) {
		Objects.requireNonNull(c);
		if(c.isEmpty()) return true;
		for(ObjectIterator<T> iter = c.iterator();iter.hasNext();)
			if(!contains(iter.next()))
				return false;
		return true;
	}
	
	@Override
	public boolean containsAll(Collection<?> c) {
		Objects.requireNonNull(c);
		return c instanceof ObjectCollection ? containsAll((ObjectCollection<T>)c) : super.containsAll(c);
	}
	
	/**
	 * This implementation iterates over the elements of the collection and checks if they are stored in this collection
	 * @param c the elements that should be checked for
	 * @return true if any element is in this collection
	 * @throws java.lang.NullPointerException if the collection is null
	 */
	@Override
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
	public boolean containsAny(ObjectCollection<T> c) {
		Objects.requireNonNull(c);
		if(c.isEmpty()) return false;
		for(ObjectIterator<T> iter = c.iterator();iter.hasNext();) 
			if(contains(iter.next()))
				return true;
		return false;
	}
		
	/**
	 * A Type-Specific implementation of removeAll. This Implementation iterates over all elements and removes them as they were found in the other collection.
	 * @param c the elements that should be deleted
	 * @return true if the collection was modified.
	 * @throws java.lang.NullPointerException if the collection is null
	 */
	@Override
	public boolean removeAll(ObjectCollection<T> c) {
		Objects.requireNonNull(c);
		if(c.isEmpty()) return false;
		boolean modified = false;
		for(ObjectIterator<T> iter = iterator();iter.hasNext();) {
			if(c.contains(iter.next())) {
				iter.remove();
				modified = true;
			}
		}
		return modified;
	}

	@Override
	public boolean removeAll(ObjectCollection<T> c, Consumer<T> r) {
		Objects.requireNonNull(c);
		if(c.isEmpty()) return false;
		Objects.requireNonNull(r);
		boolean modified = false;
		for(ObjectIterator<T> iter = iterator();iter.hasNext();) {
			T e = iter.next();
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
	public boolean retainAll(ObjectCollection<T> c) {
		Objects.requireNonNull(c);
		if(c.isEmpty()) {
			boolean modified = !isEmpty();
			clear();
			return modified;
		}
		boolean modified = false;
		for(ObjectIterator<T> iter = iterator();iter.hasNext();) {
			if(!c.contains(iter.next())) {
				iter.remove();
				modified = true;
			}
		}
		return modified;
	}
	
	@Override
	public boolean retainAll(ObjectCollection<T> c, Consumer<T> r) {
		Objects.requireNonNull(c);
		Objects.requireNonNull(r);
		if(c.isEmpty()) {
			boolean modified = !isEmpty();
			forEach(r);
			clear();
			return modified;
		}
		boolean modified = false;
		for(ObjectIterator<T> iter = iterator();iter.hasNext();) {
			T e = iter.next();
			if(!c.contains(e)) {
				r.accept(e);
				iter.remove();
				modified = true;
			}
		}
		return modified;
	}
	
	public static class ReverseObjectOrderedCollection<T> extends AbstractObjectCollection<T> implements ObjectOrderedCollection<T> {
		ObjectOrderedCollection<T> collection;
		Supplier<ObjectIterator<T>> reverseIterator;
		
		public ReverseObjectOrderedCollection(ObjectOrderedCollection<T> collection, Supplier<ObjectIterator<T>> reverseIterator) {
			this.collection = collection;
			this.reverseIterator = reverseIterator;
		}
		
		@Override
		public boolean add(T o) { return collection.add(o); }
		@Override
		public ObjectOrderedCollection<T> reversed() { return collection; }
		@Override
		public void addFirst(T e) { collection.addLast(e); }
		@Override
		public void addLast(T e) { collection.addFirst(e); }
		@Override
		public boolean contains(Object e) { return collection.contains(e); }
		@Override
		public boolean remove(Object e) { return collection.remove(e); }
		@Override
		public void clear() { collection.clear(); }
		@Override
		public T getFirst() { return collection.getLast(); }
		@Override
		public T removeFirst() { return collection.removeLast(); }
		@Override
		public T getLast() { return collection.getFirst(); }
		@Override
		public T removeLast() { return collection.removeFirst(); }
		@Override
		public ObjectIterator<T> iterator() { return reverseIterator.get(); }
		@Override
		public int size() { return collection.size(); }
	}
	
	public static class ReverseBiIterator<T> implements ObjectListIterator<T> {
		ObjectListIterator<T> it;
		
		public ReverseBiIterator(ObjectListIterator<T> it) {
			this.it = it;
		}
		
		@Override
		public T next() { return it.previous(); }
		@Override
		public boolean hasNext() { return it.hasPrevious(); }
		@Override
		public boolean hasPrevious() { return it.hasNext(); }
		@Override
		public T previous() { return it.next(); }
		@Override
		public void remove() { it.remove(); }
		@Override
		public int nextIndex() { return it.previousIndex(); }
		@Override
		public int previousIndex() { return it.nextIndex(); }
		@Override
		public void set(T e) { it.set(e); }
		@Override
		public void add(T e) { it.add(e); }
	}
}