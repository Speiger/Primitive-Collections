package speiger.src.collections.booleans.lists;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.RandomAccess;

import speiger.src.collections.booleans.collections.AbstractBooleanCollection;
import speiger.src.collections.booleans.collections.BooleanCollection;
import speiger.src.collections.booleans.collections.BooleanIterator;
import speiger.src.collections.booleans.collections.BooleanSplititerator;
import speiger.src.collections.ints.lists.IntList;
import speiger.src.collections.booleans.utils.BooleanSplititerators;
import speiger.src.collections.utils.SanityChecks;

/**
 * Abstract implementation of the {@link BooleanList} interface.
 */
public abstract class AbstractBooleanList extends AbstractBooleanCollection implements BooleanList 
{
	/**
	 * A Type-Specific implementation of add function that delegates to {@link List#add(int, Object)}
	 */
	@Override
	public boolean add(boolean e) {
		add(size(), e);
		return true;
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public void add(int index, Boolean element) {
		add(index, element.booleanValue());
	}
	
	/**
	 * A Type-Specific implementation that iterates over the elements and adds them.
	 * @param c the elements that wants to be added
	 * @return true if the list was modified
	 */
	@Override
	public boolean addAll(BooleanCollection c) {
		return addAll(size(), c);
	}
	
	/**
	 * A Type-Specific implementation that iterates over the elements and adds them.
	 * @param c the elements that wants to be added
	 * @return true if the list was modified
	 */
	@Override
	public boolean addAll(BooleanList c) {
		return addAll(size(), c);
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public boolean addAll(Collection<? extends Boolean> c) {
		return c instanceof BooleanCollection ? addAll((BooleanCollection)c) : addAll(size(), c);
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
		BooleanListIterator iter = listIterator();
		if(o == null) return -1;
		while(iter.hasNext()) {
			if(Objects.equals(o, Boolean.valueOf(iter.nextBoolean())))
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
		BooleanListIterator iter = listIterator(size());
		if(o == null) return -1;
		while(iter.hasPrevious()) {
			if(Objects.equals(o, Boolean.valueOf(iter.previousBoolean())))
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
	public int indexOf(boolean e) {
		BooleanListIterator iter = listIterator();
		while(iter.hasNext()) {
			if(iter.nextBoolean() == e)
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
	public int lastIndexOf(boolean e) {
		BooleanListIterator iter = listIterator(size());
		while(iter.hasPrevious()) {
			if(iter.previousBoolean() == e)
				return iter.nextIndex();
		}
		return -1;
	}
	
	@Override
	public boolean swapRemoveBoolean(boolean e) {
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
		if(l instanceof BooleanList)
		{
			BooleanListIterator e1 = listIterator();
			BooleanListIterator e2 = ((BooleanList)l).listIterator();
			while (e1.hasNext() && e2.hasNext()) {
				if(!(e1.nextBoolean() == e2.nextBoolean()))
					return false;
			}
			return !(e1.hasNext() || e2.hasNext());
		}
		ListIterator<Boolean> e1 = listIterator();
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
		BooleanListIterator i = listIterator();
		while(i.hasNext())
			hashCode = 31 * hashCode + Boolean.hashCode(i.nextBoolean());
		return hashCode;
	}
	
	@Override
	public BooleanList subList(int fromIndex, int toIndex) {
		SanityChecks.checkArrayCapacity(size(), fromIndex, toIndex-fromIndex);
		return new SubList(this, 0, fromIndex, toIndex);
	}
	
	@Override
	public BooleanList reversed() {
		return new ReversedList(this);
	}
	
	@Override
	public BooleanIterator iterator() {
		return listIterator(0);
	}
	
	@Override
	public BooleanListIterator listIterator() {
		return listIterator(0);
	}
		
	@Override
	public BooleanListIterator listIterator(int index) {
		if(index < 0 || index > size()) throw new IndexOutOfBoundsException();
		return new BooleanListIter(index);
	}
	
	@Override
	public BooleanListIterator indexedIterator(int...indecies) {
		return new IndexedIterator(indecies);
	}
	
	@Override
	public BooleanListIterator indexedIterator(IntList indecies) {
		return new ListIndexedIterator(indecies);
	}
	
	@Override
	public void size(int size) {
		while(size > size()) add(false);
		while(size < size()) removeBoolean(size() - 1);
	}
	
	public AbstractBooleanList copy() { throw new UnsupportedOperationException(); }
	
	private class ReversedList extends AbstractBooleanList
	{
		final AbstractBooleanList list;
		
		public ReversedList(AbstractBooleanList list) {
			this.list = list;
		}

		@Override
		public void add(int index, boolean e) {
			list.add(list.size() - index - 1, e);
		}
		
		@Override
		public boolean addAll(int index, BooleanCollection c) {
			return addCollection(index, c);
		}
		
		@Override
		public boolean addAll(int index, BooleanList c) {
			if(c instanceof RandomAccess) {
				for(int i = 0,m=c.size();i<m;i++) {
					list.add(list.size() - index - i - 1, c.getBoolean(i));
				}
				return true;
			}
			return addCollection(index, c);
		}
		
		private boolean addCollection(int index, BooleanCollection c) {
			int i = 0;
			for(BooleanIterator iter = c.iterator();iter.hasNext();i++) {
				list.add(list.size() - index - i - 1, iter.nextBoolean());
			}
			return true;
		}
		
		@Override
		public boolean addAll(int index, Collection<? extends Boolean> c) {
			int i = 0;
			for(Iterator<? extends Boolean> iter = c.iterator();iter.hasNext();i++) {
				list.add(list.size() - index - i - 1, iter.next());
			}
			return true;
		}
		
		@Override
		public boolean getBoolean(int index) {
			return list.getBoolean(list.size() - index - 1);
		}

		@Override
		public boolean set(int index, boolean e) {
			return list.set(list.size() - index - 1, e);
		}

		@Override
		public boolean removeBoolean(int index) {
			return list.removeBoolean(list.size() - index - 1);
		}
		
		@Override
		public void addElements(int from, boolean[] a, int offset, int length) {
			for(int i = 0,m=length;i<m;i++) {
				list.add(list.size() - from - i - 1, a[i+offset]);
			}
		}
		
		@Override
		public boolean[] getElements(int from, boolean[] a, int offset, int length) {
			return reverse(list.getElements(list.size() - from - 1, a, offset, length));
		}

		@Override
		public void removeElements(int from, int to) {
			list.removeElements(list.size() - to - 1, list.size() - from - 1);
		}

		@Override
		public boolean swapRemove(int index) {
			return list.swapRemove(list.size() - index - 1);
		}
		@Override
		public boolean[] extractElements(int from, int to) {
			return reverse(list.extractElements(list.size() - to - 1, list.size() - from - 1));
		}
		
		@Override
		public int size() {
			return list.size();
		}
		
		@Override
		public void clear() {
			list.clear();
		}
		
		@Override
		public BooleanList reversed() {
			return list;
		}
		
		private boolean[] reverse(boolean[] data) {
			for (int i = 0, mid = data.length >> 1, j = data.length - 1; i < mid; i++, j--) {
				boolean t = data[i];
				data[i] = data[j];
				data[j] = t;
			}
			return data;
		}
	
	}
	
	private class SubList extends AbstractBooleanList
	{
		final AbstractBooleanList list;
		final int parentOffset;
		final int offset;
		int size;
		
		public SubList(AbstractBooleanList list, int offset, int from, int to) {
			this.list = list;
			this.parentOffset = from;
			this.offset = offset + from;
			this.size = to - from;
		}
		
		@Override
		public void add(int index, boolean element) {
			checkAddSubRange(index);
			list.add(parentOffset+index, element);
			size++;
		}
		
		@Override
		public boolean addAll(int index, Collection<? extends Boolean> c) {
			checkAddSubRange(index);
			int add = c.size();
			if(add <= 0) return false;
			list.addAll(parentOffset+index, c);
			this.size += add;
			return true;
		}
		
		@Override
		public boolean addAll(int index, BooleanCollection c) {
			checkAddSubRange(index);
			int add = c.size();
			if(add <= 0) return false;
			list.addAll(parentOffset+index, c);
			this.size += add;
			return true;
		}

		@Override
		public boolean addAll(int index, BooleanList c) {
			checkAddSubRange(index);
			int add = c.size();
			if(add <= 0) return false;
			list.addAll(parentOffset+index, c);
			this.size += add;
			return true;
		}
		
		@Override
		public void addElements(int from, boolean[] a, int offset, int length) {
			checkAddSubRange(from);
			if(length <= 0) return;
			list.addElements(parentOffset+from, a, offset, length);
			this.size += length;
		}
		
		@Override
		public boolean[] getElements(int from, boolean[] a, int offset, int length) {
			SanityChecks.checkArrayCapacity(size, from, length);
			SanityChecks.checkArrayCapacity(a.length, offset, length);
			return list.getElements(from+parentOffset, a, offset, length);
		}
		
		@Override
		public void removeElements(int from, int to) {
			if(to-from <= 0) return;
			checkSubRange(from);
			checkAddSubRange(to);
			list.removeElements(from+parentOffset, to+parentOffset);
			size -= to - from;
		}
		
		@Override
		public boolean[] extractElements(int from, int to) {
			checkSubRange(from);
			checkAddSubRange(to);
			boolean[] result = list.extractElements(from+parentOffset, to+parentOffset);
			size -= result.length;
			return result;
		}
		
		@Override
		public boolean getBoolean(int index) {
			checkSubRange(index);
			return list.getBoolean(parentOffset+index);
		}

		@Override
		public boolean set(int index, boolean element) {
			checkSubRange(index);
			return list.set(parentOffset+index, element);
		}
		
		@Override
		public boolean swapRemove(int index) {
			checkSubRange(index);
			if(index == size-1) {
				boolean result = list.removeBoolean(parentOffset+size-1);
				size--;
				return result;
			}
			boolean result = list.set(index+parentOffset, list.getBoolean(parentOffset+size-1));
			list.removeBoolean(parentOffset+size-1);
			size--;
			return result;
		}
		
		@Override
		public boolean removeBoolean(int index) {
			checkSubRange(index);
			boolean result = list.removeBoolean(index+parentOffset);
			size--;
			return result;
		}
		
		@Override
		public int size() {
			return size;
		}
		
		@Override
		public BooleanSplititerator spliterator() { return BooleanSplititerators.createSplititerator(this, 16464); }
		
		@Override
		public BooleanListIterator listIterator(int index) {
			if(index < 0 || index > size()) throw new IndexOutOfBoundsException();
			return new SubListIterator(this, index);
		}
		
		@Override
		public BooleanList subList(int fromIndex, int toIndex) {
			SanityChecks.checkArrayCapacity(size, fromIndex, toIndex-fromIndex);
			return new SubList(this, offset, fromIndex, toIndex);
		}
		
		protected void checkSubRange(int index) {
			if (index < 0 || index >= size)
				throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
		}
		
		protected void checkAddSubRange(int index) {
			if (index < 0 || index > size)
				throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
		}
		
		private class SubListIterator implements BooleanListIterator
		{
			AbstractBooleanList list;
			int index;
			int lastReturned = -1;
			
			SubListIterator(AbstractBooleanList list, int index) {
				this.list = list;
				this.index = index;
			}
			
			@Override
			public boolean hasNext() {
				return index < list.size();
			}
			
			@Override
			public boolean nextBoolean() {
				if(!hasNext()) throw new NoSuchElementException();
				int i = index++;
				return list.getBoolean((lastReturned = i));
			}
			
			@Override
			public boolean hasPrevious() {
				return index > 0;
			}
			
			@Override
			public boolean previousBoolean() {
				if(!hasPrevious()) throw new NoSuchElementException();
				index--;
				return list.getBoolean((lastReturned = index));
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
				list.removeBoolean(lastReturned);
				index = lastReturned;
				lastReturned = -1;
			}
			
			@Override
			public void set(boolean e) {
				if(lastReturned == -1) throw new IllegalStateException();
				list.set(lastReturned, e);
			}
			
			@Override
			public void add(boolean e) {
				list.add(index, e);
				index++;
				lastReturned = -1;
			}
			
			@Override
			public int skip(int amount) {
				if(amount < 0) throw new IllegalStateException("Negative Numbers are not allowed");
				int steps = Math.min(amount, size() - index);
				index += steps;
				if(steps > 0) lastReturned = Math.min(index-1, size()-1);
				return steps;
			}
			
			@Override
			public int back(int amount) {
				if(amount < 0) throw new IllegalStateException("Negative Numbers are not allowed");
				int steps = Math.min(amount, index);
				index -= steps;
				if(steps > 0) lastReturned = Math.min(index, size()-1);
				return steps;
			}
		}
	}
	
	private class ListIndexedIterator implements BooleanListIterator {
		IntList indecies;
		int index;
		int lastReturned = -1;
		
		ListIndexedIterator(IntList indecies) {
			this.indecies = indecies;
		}
		
		@Override
		public boolean hasNext() {
			return index < indecies.size();
		}
		
		@Override
		public boolean nextBoolean() {
			if(!hasNext()) throw new NoSuchElementException();
			int i = index++;
			return getBoolean((lastReturned = indecies.getInt(i)));
		}
		
		@Override
		public boolean hasPrevious() {
			return index > 0;
		}
		
		@Override
		public boolean previousBoolean() {
			if(!hasPrevious()) throw new NoSuchElementException();
			index--;
			return getBoolean((lastReturned = indecies.getInt(index)));
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
		public void remove() { throw new UnsupportedOperationException(); }
		@Override
		public void add(boolean e) { throw new UnsupportedOperationException(); }
		
		@Override
		public void set(boolean e) {
			if(lastReturned == -1) throw new IllegalStateException();
			AbstractBooleanList.this.set(lastReturned, e);
		}
		
		@Override
		public int skip(int amount) {
			if(amount < 0) throw new IllegalStateException("Negative Numbers are not allowed");
			int steps = Math.min(amount, indecies.size() - index);
			index += steps;
			if(steps > 0) lastReturned = Math.min(index-1, indecies.size()-1);
			return steps;
		}
		
		@Override
		public int back(int amount) {
			if(amount < 0) throw new IllegalStateException("Negative Numbers are not allowed");
			int steps = Math.min(amount, index);
			index -= steps;
			if(steps > 0) lastReturned = Math.max(index, 0);
			return steps;
		}
	}
	
	private class IndexedIterator implements BooleanListIterator {
		int[] indecies;
		int index;
		int lastReturned = -1;
		
		IndexedIterator(int[] indecies) {
			this.indecies = indecies;
		}
		
		@Override
		public boolean hasNext() {
			return index < indecies.length;
		}
		
		@Override
		public boolean nextBoolean() {
			if(!hasNext()) throw new NoSuchElementException();
			int i = index++;
			return getBoolean((lastReturned = indecies[i]));
		}
		
		@Override
		public boolean hasPrevious() {
			return index > 0;
		}
		
		@Override
		public boolean previousBoolean() {
			if(!hasPrevious()) throw new NoSuchElementException();
			index--;
			return getBoolean((lastReturned = indecies[index]));
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
		public void remove() { throw new UnsupportedOperationException(); }
		@Override
		public void add(boolean e) { throw new UnsupportedOperationException(); }
		
		@Override
		public void set(boolean e) {
			if(lastReturned == -1) throw new IllegalStateException();
			AbstractBooleanList.this.set(lastReturned, e);
		}
		
		@Override
		public int skip(int amount) {
			if(amount < 0) throw new IllegalStateException("Negative Numbers are not allowed");
			int steps = Math.min(amount, indecies.length - index);
			index += steps;
			if(steps > 0) lastReturned = Math.min(index-1, indecies.length-1);
			return steps;
		}
		
		@Override
		public int back(int amount) {
			if(amount < 0) throw new IllegalStateException("Negative Numbers are not allowed");
			int steps = Math.min(amount, index);
			index -= steps;
			if(steps > 0) lastReturned = Math.max(index, 0);
			return steps;
		}
	}
	
	private class BooleanListIter implements BooleanListIterator {
		int index;
		int lastReturned = -1;
		
		BooleanListIter(int index) {
			this.index = index;
		}
		
		@Override
		public boolean hasNext() {
			return index < size();
		}
		
		@Override
		public boolean nextBoolean() {
			if(!hasNext()) throw new NoSuchElementException();
			int i = index++;
			return getBoolean((lastReturned = i));
		}
		
		@Override
		public boolean hasPrevious() {
			return index > 0;
		}
		
		@Override
		public boolean previousBoolean() {
			if(!hasPrevious()) throw new NoSuchElementException();
			index--;
			return getBoolean((lastReturned = index));
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
			AbstractBooleanList.this.removeBoolean(lastReturned);
			index = lastReturned;
			lastReturned = -1;
		}
		
		@Override
		public void set(boolean e) {
			if(lastReturned == -1) throw new IllegalStateException();
			AbstractBooleanList.this.set(lastReturned, e);
		}
		
		@Override
		public void add(boolean e) {
			AbstractBooleanList.this.add(index, e);
			index++;
			lastReturned = -1;
		}
		
		@Override
		public int skip(int amount) {
			if(amount < 0) throw new IllegalStateException("Negative Numbers are not allowed");
			int steps = Math.min(amount, size() - index);
			index += steps;
			if(steps > 0) lastReturned = Math.min(index-1, size()-1);
			return steps;
		}
		
		@Override
		public int back(int amount) {
			if(amount < 0) throw new IllegalStateException("Negative Numbers are not allowed");
			int steps = Math.min(amount, index);
			index -= steps;
			if(steps > 0) lastReturned = Math.max(index, 0);
			return steps;
		}
	}
}