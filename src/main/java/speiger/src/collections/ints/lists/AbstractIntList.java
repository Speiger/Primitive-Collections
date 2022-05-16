package speiger.src.collections.ints.lists;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;

import speiger.src.collections.ints.collections.AbstractIntCollection;
import speiger.src.collections.ints.collections.IntCollection;
import speiger.src.collections.ints.collections.IntIterator;
import speiger.src.collections.ints.collections.IntSplititerator;
import speiger.src.collections.ints.utils.IntSplititerators;
import speiger.src.collections.utils.SanityChecks;

/**
 * Abstract implementation of the {@link IntList} interface.
 */
public abstract class AbstractIntList extends AbstractIntCollection implements IntList 
{
	/**
	 * A Type-Specific implementation of add function that delegates to {@link List#add(int, Object)}
	 */
	@Override
	public boolean add(int e) {
		add(size(), e);
		return true;
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public void add(int index, Integer element) {
		add(index, element.intValue());
	}
	
	/**
	 * A Type-Specific implementation that iterates over the elements and adds them.
	 * @param c the elements that wants to be added
	 * @return true if the list was modified
	 */
	@Override
	public boolean addAll(IntCollection c) {
		return addAll(size(), c);
	}
	
	/**
	 * A Type-Specific implementation that iterates over the elements and adds them.
	 * @param c the elements that wants to be added
	 * @return true if the list was modified
	 */
	@Override
	public boolean addAll(IntList c) {
		return addAll(size(), c);
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public boolean addAll(Collection<? extends Integer> c)
	{
		return c instanceof IntCollection ? addAll((IntCollection)c) : addAll(size(), c);
	}
	
	/**
	 * The IndexOf implementation iterates over all elements and compares them to the search value.
	 * @param o the value that the index is searched for.
	 * @return index of the value that was searched for. -1 if not found
	 * @note it is highly suggested not to use this with Primitives because of boxing. But it is still supported because of ObjectComparason that are custom objects and allow to find the contents.
	 */
	@Override
	@Deprecated
	public int indexOf(Object o) {
		IntListIterator iter = listIterator();
		if(o == null) return -1;
		while(iter.hasNext()) {
			if(Objects.equals(o, Integer.valueOf(iter.nextInt())))
				return iter.previousIndex();
		}
		return -1;
	}
	
	/**
	 * The lastIndexOf implementation iterates over all elements and compares them to the search value.
	 * @param o the value that the index is searched for.
	 * @return the last index of the value that was searched for. -1 if not found
	 * @note it is highly suggested not to use this with Primitives because of boxing. But it is still supported because of ObjectComparason that are custom objects and allow to find the contents.
	 */
	@Override
	@Deprecated
	public int lastIndexOf(Object o) {
		IntListIterator iter = listIterator(size());
		if(o == null) return -1;
		while(iter.hasPrevious()) {
			if(Objects.equals(o, Integer.valueOf(iter.previousInt())))
				return iter.nextIndex();
		}
		return -1;
	}
	
	/**
	 * The indexOf implementation iterates over all elements and compares them to the search value.
	 * @param e the value that the index is searched for.
	 * @return index of the value that was searched for. -1 if not found
	 */
	@Override
	public int indexOf(int e) {
		IntListIterator iter = listIterator();
		while(iter.hasNext()) {
			if(iter.nextInt() == e)
				return iter.previousIndex();
		}
		return -1;
	}
	
	/**
	 * The lastIndexOf implementation iterates over all elements and compares them to the search value.
	 * @param e the value that the index is searched for.
	 * @return the last index of the value that was searched for. -1 if not found
	 */
	@Override
	public int lastIndexOf(int e) {
		IntListIterator iter = listIterator(size());
		while(iter.hasPrevious()) {
			if(iter.previousInt() == e)
				return iter.nextIndex();
		}
		return -1;
	}
	
	@Override
	public boolean swapRemoveInt(int e) {
		int index = indexOf(e);
		if(index == -1) return false;
		swapRemove(index);
		return true;
	}
	
	/**
	 * Compares if the list are the same.
	 */
	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof List))
			return false;
		List<?> l = (List<?>)o;
		if(l.size() != size()) return false;
		if(l instanceof IntList)
		{
			IntListIterator e1 = listIterator();
			IntListIterator e2 = ((IntList)l).listIterator();
			while (e1.hasNext() && e2.hasNext()) {
				if(!(e1.nextInt() == e2.nextInt()))
					return false;
			}
			return !(e1.hasNext() || e2.hasNext());
		}
		ListIterator<Integer> e1 = listIterator();
		ListIterator<?> e2 = l.listIterator();
		while (e1.hasNext() && e2.hasNext()) {
			if(!Objects.equals(e1.next(), e2.next()))
				return false;
		}
		return !(e1.hasNext() || e2.hasNext());
	}
	
	/**
	 * Generates the hashcode based on the values stored in the list.
	 */
	@Override
	public int hashCode() {
		int hashCode = 1;
		IntListIterator i = listIterator();
		while(i.hasNext())
			hashCode = 31 * hashCode + Integer.hashCode(i.nextInt());
		return hashCode;
	}
	
	@Override
	public IntList subList(int fromIndex, int toIndex) {
		SanityChecks.checkArrayCapacity(size(), fromIndex, toIndex-fromIndex);
		return new SubList(this, 0, fromIndex, toIndex);
	}
	
	@Override
	public IntIterator iterator() {
		return listIterator(0);
	}
	
	@Override
	public IntListIterator listIterator() {
		return listIterator(0);
	}

	@Override
	public IntListIterator listIterator(int index) {
		if(index < 0 || index > size()) throw new IndexOutOfBoundsException();
		return new IntListIter(index);
	}
	
	@Override
	public void size(int size) {
		while(size > size()) add(0);
		while(size < size()) removeInt(size() - 1);
	}
	
	public AbstractIntList copy() { throw new UnsupportedOperationException(); }
	
	private class SubList extends AbstractIntList
	{
		final AbstractIntList list;
		final int parentOffset;
		final int offset;
		int size;
		
		public SubList(AbstractIntList list, int offset, int from, int to) {
			this.list = list;
			this.parentOffset = from;
			this.offset = offset + from;
			this.size = to - from;
		}
		
		@Override
		public void add(int index, int element) {
			checkAddSubRange(index);
			list.add(parentOffset+index, element);
			size++;
		}
		
		@Override
		public boolean addAll(int index, Collection<? extends Integer> c) {
			checkAddSubRange(index);
			int add = c.size();
			if(add <= 0) return false;
			list.addAll(parentOffset+index, c);
			this.size += add;
			return true;
		}
		
		@Override
		public boolean addAll(int index, IntCollection c) {
			checkAddSubRange(index);
			int add = c.size();
			if(add <= 0) return false;
			list.addAll(parentOffset+index, c);
			this.size += add;
			return true;
		}

		@Override
		public boolean addAll(int index, IntList c) {
			checkAddSubRange(index);
			int add = c.size();
			if(add <= 0) return false;
			list.addAll(parentOffset+index, c);
			this.size += add;
			return true;
		}
		
		@Override
		public void addElements(int from, int[] a, int offset, int length) {
			checkAddSubRange(from);
			if(length <= 0) return;
			list.addElements(parentOffset+from, a, offset, length);
			this.size += length;
		}
		
		@Override
		public int[] getElements(int from, int[] a, int offset, int length) {
			SanityChecks.checkArrayCapacity(size, from, length);
			SanityChecks.checkArrayCapacity(a.length, offset, length);
			return list.getElements(from+this.offset, a, offset, length);
		}
		
		@Override
		public void removeElements(int from, int to) {
			if(to-from <= 0) return;
			checkSubRange(from);
			checkSubRange(to);
			list.removeElements(from+parentOffset, to+parentOffset);
			size -= to - from;
		}
		
		@Override
		public int[] extractElements(int from, int to) {
			checkSubRange(from);
			checkSubRange(to);
			int[] result = list.extractElements(from+parentOffset, to+parentOffset);
			size -= to - from;
			return result;
		}
		
		@Override
		public int getInt(int index) {
			checkSubRange(index);
			return list.getInt(offset+index);
		}

		@Override
		public int set(int index, int element) {
			checkSubRange(index);
			return list.set(offset+index, element);
		}
		
		@Override
		public int swapRemove(int index) {
			checkSubRange(index);
			int result = list.swapRemove(index+parentOffset);
			size--;
			return result;
		}
		
		@Override
		public int removeInt(int index) {
			checkSubRange(index);
			int result = list.removeInt(index+parentOffset);
			size--;
			return result;
		}
		
		@Override
		public int size() {
			return size;
		}
		
		@Override
		public IntSplititerator spliterator() { return IntSplititerators.createSplititerator(this, 16464); }
		
		@Override
		public IntListIterator listIterator(int index) {
			if(index < 0 || index > size()) throw new IndexOutOfBoundsException();
			return new SubListIterator(this, index);
		}
		
		@Override
		public IntList subList(int fromIndex, int toIndex) {
			SanityChecks.checkArrayCapacity(size, fromIndex, toIndex-fromIndex);
			return new SubList(list, offset, fromIndex, toIndex);
		}
		
		protected void checkSubRange(int index) {
			if (index < 0 || index >= size)
				throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
		}
		
		protected void checkAddSubRange(int index) {
			if (index < 0 || index > size)
				throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
		}
		
		private class SubListIterator implements IntListIterator
		{
			AbstractIntList list;
			int index;
			int lastReturned = -1;
			
			SubListIterator(AbstractIntList list, int index) {
				this.list = list;
				this.index = index;
			}
			
			@Override
			public boolean hasNext() {
				return index < list.size();
			}
			
			@Override
			public int nextInt() {
				if(!hasNext()) throw new NoSuchElementException();
				int i = index++;
				return list.getInt((lastReturned = i));
			}
			
			@Override
			public boolean hasPrevious() {
				return index > 0;
			}
			
			@Override
			public int previousInt() {
				if(!hasPrevious()) throw new NoSuchElementException();
				index--;
				return list.getInt((lastReturned = index));
			}
			
			@Override
			public int nextIndex() {
				return index;
			}
			
			@Override
			public int previousIndex() {
				return index-1;
			}
			
			@Override
			public void remove() {
				if(lastReturned == -1) throw new IllegalStateException();
				list.removeInt(lastReturned);
				index = lastReturned;
				lastReturned = -1;
			}
			
			@Override
			public void set(int e) {
				if(lastReturned == -1) throw new IllegalStateException();
				list.set(lastReturned, e);
			}
			
			@Override
			public void add(int e) {
				list.add(index++, e);
				lastReturned = -1;
			}
			
			@Override
			public int skip(int amount) {
				if(amount < 0) throw new IllegalStateException("Negative Numbers are not allowed");
				int steps = Math.min(amount, (list.size() - 1) - index);
				index += steps;
				return steps;
			}
			
			@Override
			public int back(int amount) {
				if(amount < 0) throw new IllegalStateException("Negative Numbers are not allowed");
				int steps = Math.min(amount, index);
				index -= steps;
				return steps;
			}
		}
	}
		
	private class IntListIter implements IntListIterator {
		int index;
		int lastReturned = -1;
		
		IntListIter(int index) {
			this.index = index;
		}
		
		@Override
		public boolean hasNext() {
			return index < size();
		}
		
		@Override
		public int nextInt() {
			if(!hasNext()) throw new NoSuchElementException();
			int i = index++;
			return getInt((lastReturned = i));
		}
		
		@Override
		public boolean hasPrevious() {
			return index > 0;
		}
		
		@Override
		public int previousInt() {
			if(!hasPrevious()) throw new NoSuchElementException();
			index--;
			return getInt((lastReturned = index));
		}
		
		@Override
		public int nextIndex() {
			return index;
		}
		
		@Override
		public int previousIndex() {
			return index-1;
		}
		
		@Override
		public void remove() {
			if(lastReturned == -1) throw new IllegalStateException();
			AbstractIntList.this.removeInt(lastReturned);
			index = lastReturned;
			lastReturned = -1;
		}
		
		@Override
		public void set(int e) {
			if(lastReturned == -1) throw new IllegalStateException();
			AbstractIntList.this.set(lastReturned, e);
		}
		
		@Override
		public void add(int e) {
			AbstractIntList.this.add(index++, e);
			lastReturned = -1;
		}
		
		@Override
		public int skip(int amount) {
			if(amount < 0) throw new IllegalStateException("Negative Numbers are not allowed");
			int steps = Math.min(amount, (size() - 1) - index);
			index += steps;
			return steps;
		}
		
		@Override
		public int back(int amount) {
			if(amount < 0) throw new IllegalStateException("Negative Numbers are not allowed");
			int steps = Math.min(amount, index);
			index -= steps;
			return steps;
		}
	}
}