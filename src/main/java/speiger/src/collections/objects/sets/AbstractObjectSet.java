package speiger.src.collections.objects.sets;

import java.util.Objects;
import java.util.Set;

import speiger.src.collections.objects.collections.AbstractObjectCollection;
import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;
import speiger.src.collections.objects.collections.ObjectIterator;

/**
 * Abstract Type Specific Set that reduces boxing/unboxing
 * @param <T> the keyType of elements maintained by this Collection
 */
public abstract class AbstractObjectSet<T> extends AbstractObjectCollection<T> implements ObjectSet<T>
{
	@Override
	public T addOrGet(T o) { throw new UnsupportedOperationException(); }
	
	@Override
	public abstract ObjectIterator<T> iterator();
	@Override
	public AbstractObjectSet<T> copy() { throw new UnsupportedOperationException(); }
	
	@Override
	public int hashCode() {
		int hashCode = 0;
		ObjectIterator<T> i = iterator();
		while(i.hasNext())
			hashCode += Objects.hashCode(i.next());
		return hashCode;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof Set))
			return false;
		Set<?> l = (Set<?>)o;
		if(l.size() != size()) return false;
		try {
			return containsAll(l);
		} catch (ClassCastException | NullPointerException unused) {
			return false;
		}
	}
	
	public static class ReversedObjectOrderedSet<T> extends AbstractObjectSet<T> implements ObjectOrderedSet<T> {
		protected ObjectOrderedSet<T> set;
		
		public ReversedObjectOrderedSet(ObjectOrderedSet<T> set) {
			this.set = set;
		}
		
		@Override
		public ReversedObjectOrderedSet<T> copy() { throw new UnsupportedOperationException(); }
		@Override
		public ObjectBidirectionalIterator<T> iterator(T fromElement) {
			return set.iterator(fromElement);
		}
		
		@Override
		public ObjectBidirectionalIterator<T> iterator() {
			return set.reverseIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<T> reverseIterator() {
			return set.iterator();
		}
		
		@Override
		public void addFirst(T o) {
			set.addLast(o);
		}
		
		@Override
		public void addLast(T o) {
			set.addFirst(o);
		}
		
		@Override
		public boolean remove(Object o) {
			return set.remove(o);
		}
		
		@Override
		public boolean add(T o) {
			return set.add(o);
		}
		
		@Override
		public boolean addAndMoveToFirst(T o) {
			return set.addAndMoveToLast(o);
		}
		
		@Override
		public boolean addAndMoveToLast(T o) {
			return set.addAndMoveToFirst(o);
		}
		
		@Override
		public boolean moveToFirst(T o) {
			return set.moveToLast(o);
		}
		
		@Override
		public boolean moveToLast(T o) {
			return set.moveToFirst(o);
		}
		
		@Override
		public T getFirst() {
			return set.getLast();
		}
		
		@Override
		public T removeFirst() {
			return set.removeLast();
		}
		
		@Override
		public T getLast() {
			return set.getFirst();
		}
		
		@Override
		public T removeLast() {
			return set.removeFirst();
		}
		
		@Override
		public ObjectOrderedSet<T> reversed() {
			return set;
		}
		
		@Override
		public int size() {
			return set.size();
		}
	}
}