package speiger.src.collections.doubles.lists;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;

import speiger.src.collections.doubles.collections.AbstractDoubleCollection;
import speiger.src.collections.doubles.collections.DoubleCollection;
import speiger.src.collections.doubles.collections.DoubleIterator;
import speiger.src.collections.doubles.collections.DoubleSplititerator;
import speiger.src.collections.doubles.utils.DoubleSplititerators;
import speiger.src.collections.utils.SanityChecks;

/**
 * Abstract implementation of the {@link DoubleList} interface.
 */
public abstract class AbstractDoubleList extends AbstractDoubleCollection implements DoubleList 
{
	/**
	 * A Type-Specific implementation of add function that delegates to {@link List#add(int, Object)}
	 */
	@Override
	public boolean add(double e) {
		add(size(), e);
		return true;
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public void add(int index, Double element) {
		add(index, element.doubleValue());
	}
	
	/**
	 * A Type-Specific implementation that iterates over the elements and adds them.
	 * @param c the elements that wants to be added
	 * @return true if the list was modified
	 */
	@Override
	public boolean addAll(DoubleCollection c) {
		return addAll(size(), c);
	}
	
	/**
	 * A Type-Specific implementation that iterates over the elements and adds them.
	 * @param c the elements that wants to be added
	 * @return true if the list was modified
	 */
	@Override
	public boolean addAll(DoubleList c) {
		return addAll(size(), c);
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public boolean addAll(Collection<? extends Double> c)
	{
		return c instanceof DoubleCollection ? addAll((DoubleCollection)c) : addAll(size(), c);
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
		DoubleListIterator iter = listIterator();
		if(o == null) return -1;
		while(iter.hasNext()) {
			if(Objects.equals(o, Double.valueOf(iter.nextDouble())))
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
		DoubleListIterator iter = listIterator(size());
		if(o == null) return -1;
		while(iter.hasPrevious()) {
			if(Objects.equals(o, Double.valueOf(iter.previousDouble())))
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
	public int indexOf(double e) {
		DoubleListIterator iter = listIterator();
		while(iter.hasNext()) {
			if(Double.doubleToLongBits(iter.nextDouble()) == Double.doubleToLongBits(e))
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
	public int lastIndexOf(double e) {
		DoubleListIterator iter = listIterator(size());
		while(iter.hasPrevious()) {
			if(Double.doubleToLongBits(iter.previousDouble()) == Double.doubleToLongBits(e))
				return iter.nextIndex();
		}
		return -1;
	}
	
	@Override
	public boolean swapRemoveDouble(double e) {
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
		if(l instanceof DoubleList)
		{
			DoubleListIterator e1 = listIterator();
			DoubleListIterator e2 = ((DoubleList)l).listIterator();
			while (e1.hasNext() && e2.hasNext()) {
				if(!(Double.doubleToLongBits(e1.nextDouble()) == Double.doubleToLongBits(e2.nextDouble())))
					return false;
			}
			return !(e1.hasNext() || e2.hasNext());
		}
		ListIterator<Double> e1 = listIterator();
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
		DoubleListIterator i = listIterator();
		while(i.hasNext())
			hashCode = 31 * hashCode + Double.hashCode(i.nextDouble());
		return hashCode;
	}
	
	@Override
	public DoubleList subList(int fromIndex, int toIndex) {
		SanityChecks.checkArrayCapacity(size(), fromIndex, toIndex-fromIndex);
		return new SubList(this, 0, fromIndex, toIndex);
	}
	
	@Override
	public DoubleIterator iterator() {
		return listIterator(0);
	}
	
	@Override
	public DoubleListIterator listIterator() {
		return listIterator(0);
	}

	@Override
	public DoubleListIterator listIterator(int index) {
		if(index < 0 || index > size()) throw new IndexOutOfBoundsException();
		return new DoubleListIter(index);
	}
	
	@Override
	public void size(int size) {
		while(size > size()) add(0D);
		while(size < size()) removeDouble(size() - 1);
	}
	
	public AbstractDoubleList copy() { throw new UnsupportedOperationException(); }
	
	private class SubList extends AbstractDoubleList
	{
		final AbstractDoubleList list;
		final int parentOffset;
		final int offset;
		int size;
		
		public SubList(AbstractDoubleList list, int offset, int from, int to) {
			this.list = list;
			this.parentOffset = from;
			this.offset = offset + from;
			this.size = to - from;
		}
		
		@Override
		public void add(int index, double element) {
			checkAddSubRange(index);
			list.add(parentOffset+index, element);
			size++;
		}
		
		@Override
		public boolean addAll(int index, Collection<? extends Double> c) {
			checkAddSubRange(index);
			int add = c.size();
			if(add <= 0) return false;
			list.addAll(parentOffset+index, c);
			this.size += add;
			return true;
		}
		
		@Override
		public boolean addAll(int index, DoubleCollection c) {
			checkAddSubRange(index);
			int add = c.size();
			if(add <= 0) return false;
			list.addAll(parentOffset+index, c);
			this.size += add;
			return true;
		}

		@Override
		public boolean addAll(int index, DoubleList c) {
			checkAddSubRange(index);
			int add = c.size();
			if(add <= 0) return false;
			list.addAll(parentOffset+index, c);
			this.size += add;
			return true;
		}
		
		@Override
		public void addElements(int from, double[] a, int offset, int length) {
			checkAddSubRange(from);
			if(length <= 0) return;
			list.addElements(parentOffset+from, a, offset, length);
			this.size += length;
		}
		
		@Override
		public double[] getElements(int from, double[] a, int offset, int length) {
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
		public double[] extractElements(int from, int to) {
			checkSubRange(from);
			checkSubRange(to);
			double[] result = list.extractElements(from+parentOffset, to+parentOffset);
			size -= to - from;
			return result;
		}
		
		@Override
		public double getDouble(int index) {
			checkSubRange(index);
			return list.getDouble(offset+index);
		}

		@Override
		public double set(int index, double element) {
			checkSubRange(index);
			return list.set(offset+index, element);
		}
		
		@Override
		public double swapRemove(int index) {
			checkSubRange(index);
			double result = list.swapRemove(index+parentOffset);
			size--;
			return result;
		}
		
		@Override
		public double removeDouble(int index) {
			checkSubRange(index);
			double result = list.removeDouble(index+parentOffset);
			size--;
			return result;
		}
		
		@Override
		public int size() {
			return size;
		}
		
		@Override
		public DoubleSplititerator spliterator() { return DoubleSplititerators.createSplititerator(this, 16464); }
		
		@Override
		public DoubleListIterator listIterator(int index) {
			if(index < 0 || index > size()) throw new IndexOutOfBoundsException();
			return new SubListIterator(this, index);
		}
		
		@Override
		public DoubleList subList(int fromIndex, int toIndex) {
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
		
		private class SubListIterator implements DoubleListIterator
		{
			AbstractDoubleList list;
			int index;
			int lastReturned = -1;
			
			SubListIterator(AbstractDoubleList list, int index) {
				this.list = list;
				this.index = index;
			}
			
			@Override
			public boolean hasNext() {
				return index < list.size();
			}
			
			@Override
			public double nextDouble() {
				if(!hasNext()) throw new NoSuchElementException();
				int i = index++;
				return list.getDouble((lastReturned = i));
			}
			
			@Override
			public boolean hasPrevious() {
				return index > 0;
			}
			
			@Override
			public double previousDouble() {
				if(!hasPrevious()) throw new NoSuchElementException();
				index--;
				return list.getDouble((lastReturned = index));
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
				list.removeDouble(lastReturned);
				index = lastReturned;
				lastReturned = -1;
			}
			
			@Override
			public void set(double e) {
				if(lastReturned == -1) throw new IllegalStateException();
				list.set(lastReturned, e);
			}
			
			@Override
			public void add(double e) {
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
		
	private class DoubleListIter implements DoubleListIterator {
		int index;
		int lastReturned = -1;
		
		DoubleListIter(int index) {
			this.index = index;
		}
		
		@Override
		public boolean hasNext() {
			return index < size();
		}
		
		@Override
		public double nextDouble() {
			if(!hasNext()) throw new NoSuchElementException();
			int i = index++;
			return getDouble((lastReturned = i));
		}
		
		@Override
		public boolean hasPrevious() {
			return index > 0;
		}
		
		@Override
		public double previousDouble() {
			if(!hasPrevious()) throw new NoSuchElementException();
			index--;
			return getDouble((lastReturned = index));
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
			AbstractDoubleList.this.removeDouble(lastReturned);
			index = lastReturned;
			lastReturned = -1;
		}
		
		@Override
		public void set(double e) {
			if(lastReturned == -1) throw new IllegalStateException();
			AbstractDoubleList.this.set(lastReturned, e);
		}
		
		@Override
		public void add(double e) {
			AbstractDoubleList.this.add(index++, e);
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