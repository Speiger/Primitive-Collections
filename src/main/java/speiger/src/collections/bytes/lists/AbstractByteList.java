package speiger.src.collections.bytes.lists;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.RandomAccess;

import speiger.src.collections.bytes.collections.AbstractByteCollection;
import speiger.src.collections.bytes.collections.ByteCollection;
import speiger.src.collections.bytes.collections.ByteIterator;
import speiger.src.collections.bytes.collections.ByteSplititerator;
import speiger.src.collections.bytes.utils.ByteSplititerators;
import speiger.src.collections.utils.SanityChecks;

/**
 * Abstract implementation of the {@link ByteList} interface.
 */
public abstract class AbstractByteList extends AbstractByteCollection implements ByteList 
{
	/**
	 * A Type-Specific implementation of add function that delegates to {@link List#add(int, Object)}
	 */
	@Override
	public boolean add(byte e) {
		add(size(), e);
		return true;
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public void add(int index, Byte element) {
		add(index, element.byteValue());
	}
	
	/**
	 * A Type-Specific implementation that iterates over the elements and adds them.
	 * @param c the elements that wants to be added
	 * @return true if the list was modified
	 */
	@Override
	public boolean addAll(ByteCollection c) {
		return addAll(size(), c);
	}
	
	/**
	 * A Type-Specific implementation that iterates over the elements and adds them.
	 * @param c the elements that wants to be added
	 * @return true if the list was modified
	 */
	@Override
	public boolean addAll(ByteList c) {
		return addAll(size(), c);
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public boolean addAll(Collection<? extends Byte> c) {
		return c instanceof ByteCollection ? addAll((ByteCollection)c) : addAll(size(), c);
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
		ByteListIterator iter = listIterator();
		if(o == null) return -1;
		while(iter.hasNext()) {
			if(Objects.equals(o, Byte.valueOf(iter.nextByte())))
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
		ByteListIterator iter = listIterator(size());
		if(o == null) return -1;
		while(iter.hasPrevious()) {
			if(Objects.equals(o, Byte.valueOf(iter.previousByte())))
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
	public int indexOf(byte e) {
		ByteListIterator iter = listIterator();
		while(iter.hasNext()) {
			if(iter.nextByte() == e)
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
	public int lastIndexOf(byte e) {
		ByteListIterator iter = listIterator(size());
		while(iter.hasPrevious()) {
			if(iter.previousByte() == e)
				return iter.nextIndex();
		}
		return -1;
	}
	
	@Override
	public boolean swapRemoveByte(byte e) {
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
		if(l instanceof ByteList)
		{
			ByteListIterator e1 = listIterator();
			ByteListIterator e2 = ((ByteList)l).listIterator();
			while (e1.hasNext() && e2.hasNext()) {
				if(!(e1.nextByte() == e2.nextByte()))
					return false;
			}
			return !(e1.hasNext() || e2.hasNext());
		}
		ListIterator<Byte> e1 = listIterator();
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
		ByteListIterator i = listIterator();
		while(i.hasNext())
			hashCode = 31 * hashCode + Byte.hashCode(i.nextByte());
		return hashCode;
	}
	
	@Override
	public ByteList subList(int fromIndex, int toIndex) {
		SanityChecks.checkArrayCapacity(size(), fromIndex, toIndex-fromIndex);
		return new SubList(this, 0, fromIndex, toIndex);
	}
	
	@Override
	public ByteList reversed() {
		return new ReversedList(this);
	}
	
	@Override
	public ByteIterator iterator() {
		return listIterator(0);
	}
	
	@Override
	public ByteListIterator listIterator() {
		return listIterator(0);
	}

	@Override
	public ByteListIterator listIterator(int index) {
		if(index < 0 || index > size()) throw new IndexOutOfBoundsException();
		return new ByteListIter(index);
	}
	
	@Override
	public void size(int size) {
		while(size > size()) add((byte)0);
		while(size < size()) removeByte(size() - 1);
	}
	
	public AbstractByteList copy() { throw new UnsupportedOperationException(); }
	
	private class ReversedList extends AbstractByteList
	{
		final AbstractByteList list;
		
		public ReversedList(AbstractByteList list) {
			this.list = list;
		}

		@Override
		public void add(int index, byte e) {
			list.add(list.size() - index - 1, e);
		}
		
		@Override
		public boolean addAll(int index, ByteCollection c) {
			return addCollection(index, c);
		}
		
		@Override
		public boolean addAll(int index, ByteList c) {
			if(c instanceof RandomAccess) {
				for(int i = 0,m=c.size();i<m;i++) {
					list.add(list.size() - index - i - 1, c.getByte(i));
				}
				return true;
			}
			return addCollection(index, c);
		}
		
		private boolean addCollection(int index, ByteCollection c) {
			int i = 0;
			for(ByteIterator iter = c.iterator();iter.hasNext();i++) {
				list.add(list.size() - index - i - 1, iter.nextByte());
			}
			return true;
		}
		
		@Override
		public boolean addAll(int index, Collection<? extends Byte> c) {
			int i = 0;
			for(Iterator<? extends Byte> iter = c.iterator();iter.hasNext();i++) {
				list.add(list.size() - index - i - 1, iter.next());
			}
			return true;
		}
		
		@Override
		public byte getByte(int index) {
			return list.getByte(list.size() - index - 1);
		}

		@Override
		public byte set(int index, byte e) {
			return list.set(list.size() - index - 1, e);
		}

		@Override
		public byte removeByte(int index) {
			return list.removeByte(list.size() - index - 1);
		}
		
		@Override
		public void addElements(int from, byte[] a, int offset, int length) {
			for(int i = 0,m=length;i<m;i++) {
				list.add(list.size() - from - i - 1, a[i+offset]);
			}
		}
		
		@Override
		public byte[] getElements(int from, byte[] a, int offset, int length) {
			return reverse(list.getElements(list.size() - from - 1, a, offset, length));
		}

		@Override
		public void removeElements(int from, int to) {
			list.removeElements(list.size() - to - 1, list.size() - from - 1);
		}

		@Override
		public byte swapRemove(int index) {
			return list.swapRemove(list.size() - index - 1);
		}
		@Override
		public byte[] extractElements(int from, int to) {
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
		public ByteList reversed() {
			return list;
		}
		
		private byte[] reverse(byte[] data) {
			for (int i = 0, mid = data.length >> 1, j = data.length - 1; i < mid; i++, j--) {
				byte t = data[i];
				data[i] = data[j];
				data[j] = t;
			}
			return data;
		}
	
	}
	
	private class SubList extends AbstractByteList
	{
		final AbstractByteList list;
		final int parentOffset;
		final int offset;
		int size;
		
		public SubList(AbstractByteList list, int offset, int from, int to) {
			this.list = list;
			this.parentOffset = from;
			this.offset = offset + from;
			this.size = to - from;
		}
		
		@Override
		public void add(int index, byte element) {
			checkAddSubRange(index);
			list.add(parentOffset+index, element);
			size++;
		}
		
		@Override
		public boolean addAll(int index, Collection<? extends Byte> c) {
			checkAddSubRange(index);
			int add = c.size();
			if(add <= 0) return false;
			list.addAll(parentOffset+index, c);
			this.size += add;
			return true;
		}
		
		@Override
		public boolean addAll(int index, ByteCollection c) {
			checkAddSubRange(index);
			int add = c.size();
			if(add <= 0) return false;
			list.addAll(parentOffset+index, c);
			this.size += add;
			return true;
		}

		@Override
		public boolean addAll(int index, ByteList c) {
			checkAddSubRange(index);
			int add = c.size();
			if(add <= 0) return false;
			list.addAll(parentOffset+index, c);
			this.size += add;
			return true;
		}
		
		@Override
		public void addElements(int from, byte[] a, int offset, int length) {
			checkAddSubRange(from);
			if(length <= 0) return;
			list.addElements(parentOffset+from, a, offset, length);
			this.size += length;
		}
		
		@Override
		public byte[] getElements(int from, byte[] a, int offset, int length) {
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
		public byte[] extractElements(int from, int to) {
			checkSubRange(from);
			checkAddSubRange(to);
			byte[] result = list.extractElements(from+parentOffset, to+parentOffset);
			size -= result.length;
			return result;
		}
		
		@Override
		public byte getByte(int index) {
			checkSubRange(index);
			return list.getByte(parentOffset+index);
		}

		@Override
		public byte set(int index, byte element) {
			checkSubRange(index);
			return list.set(parentOffset+index, element);
		}
		
		@Override
		public byte swapRemove(int index) {
			checkSubRange(index);
			if(index == size-1) {
				byte result = list.removeByte(parentOffset+size-1);
				size--;
				return result;
			}
			byte result = list.set(index+parentOffset, list.getByte(parentOffset+size-1));
			list.removeByte(parentOffset+size-1);
			size--;
			return result;
		}
		
		@Override
		public byte removeByte(int index) {
			checkSubRange(index);
			byte result = list.removeByte(index+parentOffset);
			size--;
			return result;
		}
		
		@Override
		public int size() {
			return size;
		}
		
		@Override
		public ByteSplititerator spliterator() { return ByteSplititerators.createSplititerator(this, 16464); }
		
		@Override
		public ByteListIterator listIterator(int index) {
			if(index < 0 || index > size()) throw new IndexOutOfBoundsException();
			return new SubListIterator(this, index);
		}
		
		@Override
		public ByteList subList(int fromIndex, int toIndex) {
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
		
		private class SubListIterator implements ByteListIterator
		{
			AbstractByteList list;
			int index;
			int lastReturned = -1;
			
			SubListIterator(AbstractByteList list, int index) {
				this.list = list;
				this.index = index;
			}
			
			@Override
			public boolean hasNext() {
				return index < list.size();
			}
			
			@Override
			public byte nextByte() {
				if(!hasNext()) throw new NoSuchElementException();
				int i = index++;
				return list.getByte((lastReturned = i));
			}
			
			@Override
			public boolean hasPrevious() {
				return index > 0;
			}
			
			@Override
			public byte previousByte() {
				if(!hasPrevious()) throw new NoSuchElementException();
				index--;
				return list.getByte((lastReturned = index));
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
				list.removeByte(lastReturned);
				index = lastReturned;
				lastReturned = -1;
			}
			
			@Override
			public void set(byte e) {
				if(lastReturned == -1) throw new IllegalStateException();
				list.set(lastReturned, e);
			}
			
			@Override
			public void add(byte e) {
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
		
	private class ByteListIter implements ByteListIterator {
		int index;
		int lastReturned = -1;
		
		ByteListIter(int index) {
			this.index = index;
		}
		
		@Override
		public boolean hasNext() {
			return index < size();
		}
		
		@Override
		public byte nextByte() {
			if(!hasNext()) throw new NoSuchElementException();
			int i = index++;
			return getByte((lastReturned = i));
		}
		
		@Override
		public boolean hasPrevious() {
			return index > 0;
		}
		
		@Override
		public byte previousByte() {
			if(!hasPrevious()) throw new NoSuchElementException();
			index--;
			return getByte((lastReturned = index));
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
			AbstractByteList.this.removeByte(lastReturned);
			index = lastReturned;
			lastReturned = -1;
		}
		
		@Override
		public void set(byte e) {
			if(lastReturned == -1) throw new IllegalStateException();
			AbstractByteList.this.set(lastReturned, e);
		}
		
		@Override
		public void add(byte e) {
			AbstractByteList.this.add(index, e);
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