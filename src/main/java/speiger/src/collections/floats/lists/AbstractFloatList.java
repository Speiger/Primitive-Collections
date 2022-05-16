package speiger.src.collections.floats.lists;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;

import speiger.src.collections.floats.collections.AbstractFloatCollection;
import speiger.src.collections.floats.collections.FloatCollection;
import speiger.src.collections.floats.collections.FloatIterator;
import speiger.src.collections.floats.collections.FloatSplititerator;
import speiger.src.collections.floats.utils.FloatSplititerators;
import speiger.src.collections.utils.SanityChecks;

/**
 * Abstract implementation of the {@link FloatList} interface.
 */
public abstract class AbstractFloatList extends AbstractFloatCollection implements FloatList 
{
	/**
	 * A Type-Specific implementation of add function that delegates to {@link List#add(int, Object)}
	 */
	@Override
	public boolean add(float e) {
		add(size(), e);
		return true;
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public void add(int index, Float element) {
		add(index, element.floatValue());
	}
	
	/**
	 * A Type-Specific implementation that iterates over the elements and adds them.
	 * @param c the elements that wants to be added
	 * @return true if the list was modified
	 */
	@Override
	public boolean addAll(FloatCollection c) {
		return addAll(size(), c);
	}
	
	/**
	 * A Type-Specific implementation that iterates over the elements and adds them.
	 * @param c the elements that wants to be added
	 * @return true if the list was modified
	 */
	@Override
	public boolean addAll(FloatList c) {
		return addAll(size(), c);
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public boolean addAll(Collection<? extends Float> c)
	{
		return c instanceof FloatCollection ? addAll((FloatCollection)c) : addAll(size(), c);
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
		FloatListIterator iter = listIterator();
		if(o == null) return -1;
		while(iter.hasNext()) {
			if(Objects.equals(o, Float.valueOf(iter.nextFloat())))
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
		FloatListIterator iter = listIterator(size());
		if(o == null) return -1;
		while(iter.hasPrevious()) {
			if(Objects.equals(o, Float.valueOf(iter.previousFloat())))
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
	public int indexOf(float e) {
		FloatListIterator iter = listIterator();
		while(iter.hasNext()) {
			if(Float.floatToIntBits(iter.nextFloat()) == Float.floatToIntBits(e))
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
	public int lastIndexOf(float e) {
		FloatListIterator iter = listIterator(size());
		while(iter.hasPrevious()) {
			if(Float.floatToIntBits(iter.previousFloat()) == Float.floatToIntBits(e))
				return iter.nextIndex();
		}
		return -1;
	}
	
	@Override
	public boolean swapRemoveFloat(float e) {
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
		if(l instanceof FloatList)
		{
			FloatListIterator e1 = listIterator();
			FloatListIterator e2 = ((FloatList)l).listIterator();
			while (e1.hasNext() && e2.hasNext()) {
				if(!(Float.floatToIntBits(e1.nextFloat()) == Float.floatToIntBits(e2.nextFloat())))
					return false;
			}
			return !(e1.hasNext() || e2.hasNext());
		}
		ListIterator<Float> e1 = listIterator();
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
		FloatListIterator i = listIterator();
		while(i.hasNext())
			hashCode = 31 * hashCode + Float.hashCode(i.nextFloat());
		return hashCode;
	}
	
	@Override
	public FloatList subList(int fromIndex, int toIndex) {
		SanityChecks.checkArrayCapacity(size(), fromIndex, toIndex-fromIndex);
		return new SubList(this, 0, fromIndex, toIndex);
	}
	
	@Override
	public FloatIterator iterator() {
		return listIterator(0);
	}
	
	@Override
	public FloatListIterator listIterator() {
		return listIterator(0);
	}

	@Override
	public FloatListIterator listIterator(int index) {
		if(index < 0 || index > size()) throw new IndexOutOfBoundsException();
		return new FloatListIter(index);
	}
	
	@Override
	public void size(int size) {
		while(size > size()) add(0F);
		while(size < size()) removeFloat(size() - 1);
	}
	
	public AbstractFloatList copy() { throw new UnsupportedOperationException(); }
	
	private class SubList extends AbstractFloatList
	{
		final AbstractFloatList list;
		final int parentOffset;
		final int offset;
		int size;
		
		public SubList(AbstractFloatList list, int offset, int from, int to) {
			this.list = list;
			this.parentOffset = from;
			this.offset = offset + from;
			this.size = to - from;
		}
		
		@Override
		public void add(int index, float element) {
			checkAddSubRange(index);
			list.add(parentOffset+index, element);
			size++;
		}
		
		@Override
		public boolean addAll(int index, Collection<? extends Float> c) {
			checkAddSubRange(index);
			int add = c.size();
			if(add <= 0) return false;
			list.addAll(parentOffset+index, c);
			this.size += add;
			return true;
		}
		
		@Override
		public boolean addAll(int index, FloatCollection c) {
			checkAddSubRange(index);
			int add = c.size();
			if(add <= 0) return false;
			list.addAll(parentOffset+index, c);
			this.size += add;
			return true;
		}

		@Override
		public boolean addAll(int index, FloatList c) {
			checkAddSubRange(index);
			int add = c.size();
			if(add <= 0) return false;
			list.addAll(parentOffset+index, c);
			this.size += add;
			return true;
		}
		
		@Override
		public void addElements(int from, float[] a, int offset, int length) {
			checkAddSubRange(from);
			if(length <= 0) return;
			list.addElements(parentOffset+from, a, offset, length);
			this.size += length;
		}
		
		@Override
		public float[] getElements(int from, float[] a, int offset, int length) {
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
		public float[] extractElements(int from, int to) {
			checkSubRange(from);
			checkSubRange(to);
			float[] result = list.extractElements(from+parentOffset, to+parentOffset);
			size -= to - from;
			return result;
		}
		
		@Override
		public float getFloat(int index) {
			checkSubRange(index);
			return list.getFloat(offset+index);
		}

		@Override
		public float set(int index, float element) {
			checkSubRange(index);
			return list.set(offset+index, element);
		}
		
		@Override
		public float swapRemove(int index) {
			checkSubRange(index);
			float result = list.swapRemove(index+parentOffset);
			size--;
			return result;
		}
		
		@Override
		public float removeFloat(int index) {
			checkSubRange(index);
			float result = list.removeFloat(index+parentOffset);
			size--;
			return result;
		}
		
		@Override
		public int size() {
			return size;
		}
		
		@Override
		public FloatSplititerator spliterator() { return FloatSplititerators.createSplititerator(this, 16464); }
		
		@Override
		public FloatListIterator listIterator(int index) {
			if(index < 0 || index > size()) throw new IndexOutOfBoundsException();
			return new SubListIterator(this, index);
		}
		
		@Override
		public FloatList subList(int fromIndex, int toIndex) {
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
		
		private class SubListIterator implements FloatListIterator
		{
			AbstractFloatList list;
			int index;
			int lastReturned = -1;
			
			SubListIterator(AbstractFloatList list, int index) {
				this.list = list;
				this.index = index;
			}
			
			@Override
			public boolean hasNext() {
				return index < list.size();
			}
			
			@Override
			public float nextFloat() {
				if(!hasNext()) throw new NoSuchElementException();
				int i = index++;
				return list.getFloat((lastReturned = i));
			}
			
			@Override
			public boolean hasPrevious() {
				return index > 0;
			}
			
			@Override
			public float previousFloat() {
				if(!hasPrevious()) throw new NoSuchElementException();
				index--;
				return list.getFloat((lastReturned = index));
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
				list.removeFloat(lastReturned);
				index = lastReturned;
				lastReturned = -1;
			}
			
			@Override
			public void set(float e) {
				if(lastReturned == -1) throw new IllegalStateException();
				list.set(lastReturned, e);
			}
			
			@Override
			public void add(float e) {
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
		
	private class FloatListIter implements FloatListIterator {
		int index;
		int lastReturned = -1;
		
		FloatListIter(int index) {
			this.index = index;
		}
		
		@Override
		public boolean hasNext() {
			return index < size();
		}
		
		@Override
		public float nextFloat() {
			if(!hasNext()) throw new NoSuchElementException();
			int i = index++;
			return getFloat((lastReturned = i));
		}
		
		@Override
		public boolean hasPrevious() {
			return index > 0;
		}
		
		@Override
		public float previousFloat() {
			if(!hasPrevious()) throw new NoSuchElementException();
			index--;
			return getFloat((lastReturned = index));
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
			AbstractFloatList.this.removeFloat(lastReturned);
			index = lastReturned;
			lastReturned = -1;
		}
		
		@Override
		public void set(float e) {
			if(lastReturned == -1) throw new IllegalStateException();
			AbstractFloatList.this.set(lastReturned, e);
		}
		
		@Override
		public void add(float e) {
			AbstractFloatList.this.add(index++, e);
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