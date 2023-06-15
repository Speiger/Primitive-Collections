package speiger.src.collections.longs.lists;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.RandomAccess;

import speiger.src.collections.longs.collections.AbstractLongCollection;
import speiger.src.collections.longs.collections.LongCollection;
import speiger.src.collections.longs.collections.LongIterator;
import speiger.src.collections.longs.collections.LongSplititerator;
import speiger.src.collections.longs.utils.LongSplititerators;
import speiger.src.collections.utils.SanityChecks;

/**
 * Abstract implementation of the {@link LongList} interface.
 */
public abstract class AbstractLongList extends AbstractLongCollection implements LongList 
{
	/**
	 * A Type-Specific implementation of add function that delegates to {@link List#add(int, Object)}
	 */
	@Override
	public boolean add(long e) {
		add(size(), e);
		return true;
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public void add(int index, Long element) {
		add(index, element.longValue());
	}
	
	/**
	 * A Type-Specific implementation that iterates over the elements and adds them.
	 * @param c the elements that wants to be added
	 * @return true if the list was modified
	 */
	@Override
	public boolean addAll(LongCollection c) {
		return addAll(size(), c);
	}
	
	/**
	 * A Type-Specific implementation that iterates over the elements and adds them.
	 * @param c the elements that wants to be added
	 * @return true if the list was modified
	 */
	@Override
	public boolean addAll(LongList c) {
		return addAll(size(), c);
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public boolean addAll(Collection<? extends Long> c) {
		return c instanceof LongCollection ? addAll((LongCollection)c) : addAll(size(), c);
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
		LongListIterator iter = listIterator();
		if(o == null) return -1;
		while(iter.hasNext()) {
			if(Objects.equals(o, Long.valueOf(iter.nextLong())))
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
		LongListIterator iter = listIterator(size());
		if(o == null) return -1;
		while(iter.hasPrevious()) {
			if(Objects.equals(o, Long.valueOf(iter.previousLong())))
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
	public int indexOf(long e) {
		LongListIterator iter = listIterator();
		while(iter.hasNext()) {
			if(iter.nextLong() == e)
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
	public int lastIndexOf(long e) {
		LongListIterator iter = listIterator(size());
		while(iter.hasPrevious()) {
			if(iter.previousLong() == e)
				return iter.nextIndex();
		}
		return -1;
	}
	
	@Override
	public boolean swapRemoveLong(long e) {
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
		if(l instanceof LongList)
		{
			LongListIterator e1 = listIterator();
			LongListIterator e2 = ((LongList)l).listIterator();
			while (e1.hasNext() && e2.hasNext()) {
				if(!(e1.nextLong() == e2.nextLong()))
					return false;
			}
			return !(e1.hasNext() || e2.hasNext());
		}
		ListIterator<Long> e1 = listIterator();
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
		LongListIterator i = listIterator();
		while(i.hasNext())
			hashCode = 31 * hashCode + Long.hashCode(i.nextLong());
		return hashCode;
	}
	
	@Override
	public LongList subList(int fromIndex, int toIndex) {
		SanityChecks.checkArrayCapacity(size(), fromIndex, toIndex-fromIndex);
		return new SubList(this, 0, fromIndex, toIndex);
	}
	
	@Override
	public LongList reversed() {
		return new ReversedList(this);
	}
	
	@Override
	public LongIterator iterator() {
		return listIterator(0);
	}
	
	@Override
	public LongListIterator listIterator() {
		return listIterator(0);
	}

	@Override
	public LongListIterator listIterator(int index) {
		if(index < 0 || index > size()) throw new IndexOutOfBoundsException();
		return new LongListIter(index);
	}
	
	@Override
	public void size(int size) {
		while(size > size()) add(0L);
		while(size < size()) removeLong(size() - 1);
	}
	
	public AbstractLongList copy() { throw new UnsupportedOperationException(); }
	
	private class ReversedList extends AbstractLongList
	{
		final AbstractLongList list;
		
		public ReversedList(AbstractLongList list) {
			this.list = list;
		}

		@Override
		public void add(int index, long e) {
			list.add(list.size() - index - 1, e);
		}
		
		@Override
		public boolean addAll(int index, LongCollection c) {
			return addCollection(index, c);
		}
		
		@Override
		public boolean addAll(int index, LongList c) {
			if(c instanceof RandomAccess) {
				for(int i = 0,m=c.size();i<m;i++) {
					list.add(list.size() - index - i - 1, c.getLong(i));
				}
				return true;
			}
			return addCollection(index, c);
		}
		
		private boolean addCollection(int index, LongCollection c) {
			int i = 0;
			for(LongIterator iter = c.iterator();iter.hasNext();i++) {
				list.add(list.size() - index - i - 1, iter.nextLong());
			}
			return true;
		}
		
		@Override
		public boolean addAll(int index, Collection<? extends Long> c) {
			int i = 0;
			for(Iterator<? extends Long> iter = c.iterator();iter.hasNext();i++) {
				list.add(list.size() - index - i - 1, iter.next());
			}
			return true;
		}
		
		@Override
		public long getLong(int index) {
			return list.getLong(list.size() - index - 1);
		}

		@Override
		public long set(int index, long e) {
			return list.set(list.size() - index - 1, e);
		}

		@Override
		public long removeLong(int index) {
			return list.removeLong(list.size() - index - 1);
		}
		
		@Override
		public void addElements(int from, long[] a, int offset, int length) {
			for(int i = 0,m=length;i<m;i++) {
				list.add(list.size() - from - i - 1, a[i+offset]);
			}
		}
		
		@Override
		public long[] getElements(int from, long[] a, int offset, int length) {
			return reverse(list.getElements(list.size() - from - 1, a, offset, length));
		}

		@Override
		public void removeElements(int from, int to) {
			list.removeElements(list.size() - to - 1, list.size() - from - 1);
		}

		@Override
		public long swapRemove(int index) {
			return list.swapRemove(list.size() - index - 1);
		}
		@Override
		public long[] extractElements(int from, int to) {
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
		public LongList reversed() {
			return list;
		}
		
		private long[] reverse(long[] data) {
			for (int i = 0, mid = data.length >> 1, j = data.length - 1; i < mid; i++, j--) {
				long t = data[i];
				data[i] = data[j];
				data[j] = t;
			}
			return data;
		}
	
	}
	
	private class SubList extends AbstractLongList
	{
		final AbstractLongList list;
		final int parentOffset;
		final int offset;
		int size;
		
		public SubList(AbstractLongList list, int offset, int from, int to) {
			this.list = list;
			this.parentOffset = from;
			this.offset = offset + from;
			this.size = to - from;
		}
		
		@Override
		public void add(int index, long element) {
			checkAddSubRange(index);
			list.add(parentOffset+index, element);
			size++;
		}
		
		@Override
		public boolean addAll(int index, Collection<? extends Long> c) {
			checkAddSubRange(index);
			int add = c.size();
			if(add <= 0) return false;
			list.addAll(parentOffset+index, c);
			this.size += add;
			return true;
		}
		
		@Override
		public boolean addAll(int index, LongCollection c) {
			checkAddSubRange(index);
			int add = c.size();
			if(add <= 0) return false;
			list.addAll(parentOffset+index, c);
			this.size += add;
			return true;
		}

		@Override
		public boolean addAll(int index, LongList c) {
			checkAddSubRange(index);
			int add = c.size();
			if(add <= 0) return false;
			list.addAll(parentOffset+index, c);
			this.size += add;
			return true;
		}
		
		@Override
		public void addElements(int from, long[] a, int offset, int length) {
			checkAddSubRange(from);
			if(length <= 0) return;
			list.addElements(parentOffset+from, a, offset, length);
			this.size += length;
		}
		
		@Override
		public long[] getElements(int from, long[] a, int offset, int length) {
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
		public long[] extractElements(int from, int to) {
			checkSubRange(from);
			checkAddSubRange(to);
			long[] result = list.extractElements(from+parentOffset, to+parentOffset);
			size -= result.length;
			return result;
		}
		
		@Override
		public long getLong(int index) {
			checkSubRange(index);
			return list.getLong(parentOffset+index);
		}

		@Override
		public long set(int index, long element) {
			checkSubRange(index);
			return list.set(parentOffset+index, element);
		}
		
		@Override
		public long swapRemove(int index) {
			checkSubRange(index);
			if(index == size-1) {
				long result = list.removeLong(parentOffset+size-1);
				size--;
				return result;
			}
			long result = list.set(index+parentOffset, list.getLong(parentOffset+size-1));
			list.removeLong(parentOffset+size-1);
			size--;
			return result;
		}
		
		@Override
		public long removeLong(int index) {
			checkSubRange(index);
			long result = list.removeLong(index+parentOffset);
			size--;
			return result;
		}
		
		@Override
		public int size() {
			return size;
		}
		
		@Override
		public LongSplititerator spliterator() { return LongSplititerators.createSplititerator(this, 16464); }
		
		@Override
		public LongListIterator listIterator(int index) {
			if(index < 0 || index > size()) throw new IndexOutOfBoundsException();
			return new SubListIterator(this, index);
		}
		
		@Override
		public LongList subList(int fromIndex, int toIndex) {
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
		
		private class SubListIterator implements LongListIterator
		{
			AbstractLongList list;
			int index;
			int lastReturned = -1;
			
			SubListIterator(AbstractLongList list, int index) {
				this.list = list;
				this.index = index;
			}
			
			@Override
			public boolean hasNext() {
				return index < list.size();
			}
			
			@Override
			public long nextLong() {
				if(!hasNext()) throw new NoSuchElementException();
				int i = index++;
				return list.getLong((lastReturned = i));
			}
			
			@Override
			public boolean hasPrevious() {
				return index > 0;
			}
			
			@Override
			public long previousLong() {
				if(!hasPrevious()) throw new NoSuchElementException();
				index--;
				return list.getLong((lastReturned = index));
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
				list.removeLong(lastReturned);
				index = lastReturned;
				lastReturned = -1;
			}
			
			@Override
			public void set(long e) {
				if(lastReturned == -1) throw new IllegalStateException();
				list.set(lastReturned, e);
			}
			
			@Override
			public void add(long e) {
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
		
	private class LongListIter implements LongListIterator {
		int index;
		int lastReturned = -1;
		
		LongListIter(int index) {
			this.index = index;
		}
		
		@Override
		public boolean hasNext() {
			return index < size();
		}
		
		@Override
		public long nextLong() {
			if(!hasNext()) throw new NoSuchElementException();
			int i = index++;
			return getLong((lastReturned = i));
		}
		
		@Override
		public boolean hasPrevious() {
			return index > 0;
		}
		
		@Override
		public long previousLong() {
			if(!hasPrevious()) throw new NoSuchElementException();
			index--;
			return getLong((lastReturned = index));
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
			AbstractLongList.this.removeLong(lastReturned);
			index = lastReturned;
			lastReturned = -1;
		}
		
		@Override
		public void set(long e) {
			if(lastReturned == -1) throw new IllegalStateException();
			AbstractLongList.this.set(lastReturned, e);
		}
		
		@Override
		public void add(long e) {
			AbstractLongList.this.add(index, e);
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