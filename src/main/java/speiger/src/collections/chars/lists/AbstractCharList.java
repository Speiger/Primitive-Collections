package speiger.src.collections.chars.lists;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.RandomAccess;

import speiger.src.collections.chars.collections.AbstractCharCollection;
import speiger.src.collections.chars.collections.CharCollection;
import speiger.src.collections.chars.collections.CharIterator;
import speiger.src.collections.chars.collections.CharSplititerator;
import speiger.src.collections.chars.utils.CharSplititerators;
import speiger.src.collections.utils.SanityChecks;

/**
 * Abstract implementation of the {@link CharList} interface.
 */
public abstract class AbstractCharList extends AbstractCharCollection implements CharList 
{
	/**
	 * A Type-Specific implementation of add function that delegates to {@link List#add(int, Object)}
	 */
	@Override
	public boolean add(char e) {
		add(size(), e);
		return true;
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public void add(int index, Character element) {
		add(index, element.charValue());
	}
	
	/**
	 * A Type-Specific implementation that iterates over the elements and adds them.
	 * @param c the elements that wants to be added
	 * @return true if the list was modified
	 */
	@Override
	public boolean addAll(CharCollection c) {
		return addAll(size(), c);
	}
	
	/**
	 * A Type-Specific implementation that iterates over the elements and adds them.
	 * @param c the elements that wants to be added
	 * @return true if the list was modified
	 */
	@Override
	public boolean addAll(CharList c) {
		return addAll(size(), c);
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public boolean addAll(Collection<? extends Character> c) {
		return c instanceof CharCollection ? addAll((CharCollection)c) : addAll(size(), c);
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
		CharListIterator iter = listIterator();
		if(o == null) return -1;
		while(iter.hasNext()) {
			if(Objects.equals(o, Character.valueOf(iter.nextChar())))
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
		CharListIterator iter = listIterator(size());
		if(o == null) return -1;
		while(iter.hasPrevious()) {
			if(Objects.equals(o, Character.valueOf(iter.previousChar())))
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
	public int indexOf(char e) {
		CharListIterator iter = listIterator();
		while(iter.hasNext()) {
			if(iter.nextChar() == e)
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
	public int lastIndexOf(char e) {
		CharListIterator iter = listIterator(size());
		while(iter.hasPrevious()) {
			if(iter.previousChar() == e)
				return iter.nextIndex();
		}
		return -1;
	}
	
	@Override
	public boolean swapRemoveChar(char e) {
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
		if(l instanceof CharList)
		{
			CharListIterator e1 = listIterator();
			CharListIterator e2 = ((CharList)l).listIterator();
			while (e1.hasNext() && e2.hasNext()) {
				if(!(e1.nextChar() == e2.nextChar()))
					return false;
			}
			return !(e1.hasNext() || e2.hasNext());
		}
		ListIterator<Character> e1 = listIterator();
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
		CharListIterator i = listIterator();
		while(i.hasNext())
			hashCode = 31 * hashCode + Character.hashCode(i.nextChar());
		return hashCode;
	}
	
	@Override
	public CharList subList(int fromIndex, int toIndex) {
		SanityChecks.checkArrayCapacity(size(), fromIndex, toIndex-fromIndex);
		return new SubList(this, 0, fromIndex, toIndex);
	}
	
	@Override
	public CharList reversed() {
		return new ReversedList(this);
	}
	
	@Override
	public CharIterator iterator() {
		return listIterator(0);
	}
	
	@Override
	public CharListIterator listIterator() {
		return listIterator(0);
	}

	@Override
	public CharListIterator listIterator(int index) {
		if(index < 0 || index > size()) throw new IndexOutOfBoundsException();
		return new CharListIter(index);
	}
	
	@Override
	public void size(int size) {
		while(size > size()) add((char)0);
		while(size < size()) removeChar(size() - 1);
	}
	
	public AbstractCharList copy() { throw new UnsupportedOperationException(); }
	
	private class ReversedList extends AbstractCharList
	{
		final AbstractCharList list;
		
		public ReversedList(AbstractCharList list) {
			this.list = list;
		}

		@Override
		public void add(int index, char e) {
			list.add(list.size() - index - 1, e);
		}
		
		@Override
		public boolean addAll(int index, CharCollection c) {
			return addCollection(index, c);
		}
		
		@Override
		public boolean addAll(int index, CharList c) {
			if(c instanceof RandomAccess) {
				for(int i = 0,m=c.size();i<m;i++) {
					list.add(list.size() - index - i - 1, c.getChar(i));
				}
				return true;
			}
			return addCollection(index, c);
		}
		
		private boolean addCollection(int index, CharCollection c) {
			int i = 0;
			for(CharIterator iter = c.iterator();iter.hasNext();i++) {
				list.add(list.size() - index - i - 1, iter.nextChar());
			}
			return true;
		}
		
		@Override
		public boolean addAll(int index, Collection<? extends Character> c) {
			int i = 0;
			for(Iterator<? extends Character> iter = c.iterator();iter.hasNext();i++) {
				list.add(list.size() - index - i - 1, iter.next());
			}
			return true;
		}
		
		@Override
		public char getChar(int index) {
			return list.getChar(list.size() - index - 1);
		}

		@Override
		public char set(int index, char e) {
			return list.set(list.size() - index - 1, e);
		}

		@Override
		public char removeChar(int index) {
			return list.removeChar(list.size() - index - 1);
		}
		
		@Override
		public void addElements(int from, char[] a, int offset, int length) {
			for(int i = 0,m=length;i<m;i++) {
				list.add(list.size() - from - i - 1, a[i+offset]);
			}
		}
		
		@Override
		public char[] getElements(int from, char[] a, int offset, int length) {
			return reverse(list.getElements(list.size() - from - 1, a, offset, length));
		}

		@Override
		public void removeElements(int from, int to) {
			list.removeElements(list.size() - to - 1, list.size() - from - 1);
		}

		@Override
		public char swapRemove(int index) {
			return list.swapRemove(list.size() - index - 1);
		}
		@Override
		public char[] extractElements(int from, int to) {
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
		public CharList reversed() {
			return list;
		}
		
		private char[] reverse(char[] data) {
			for (int i = 0, mid = data.length >> 1, j = data.length - 1; i < mid; i++, j--) {
				char t = data[i];
				data[i] = data[j];
				data[j] = t;
			}
			return data;
		}
	
	}
	
	private class SubList extends AbstractCharList
	{
		final AbstractCharList list;
		final int parentOffset;
		final int offset;
		int size;
		
		public SubList(AbstractCharList list, int offset, int from, int to) {
			this.list = list;
			this.parentOffset = from;
			this.offset = offset + from;
			this.size = to - from;
		}
		
		@Override
		public void add(int index, char element) {
			checkAddSubRange(index);
			list.add(parentOffset+index, element);
			size++;
		}
		
		@Override
		public boolean addAll(int index, Collection<? extends Character> c) {
			checkAddSubRange(index);
			int add = c.size();
			if(add <= 0) return false;
			list.addAll(parentOffset+index, c);
			this.size += add;
			return true;
		}
		
		@Override
		public boolean addAll(int index, CharCollection c) {
			checkAddSubRange(index);
			int add = c.size();
			if(add <= 0) return false;
			list.addAll(parentOffset+index, c);
			this.size += add;
			return true;
		}

		@Override
		public boolean addAll(int index, CharList c) {
			checkAddSubRange(index);
			int add = c.size();
			if(add <= 0) return false;
			list.addAll(parentOffset+index, c);
			this.size += add;
			return true;
		}
		
		@Override
		public void addElements(int from, char[] a, int offset, int length) {
			checkAddSubRange(from);
			if(length <= 0) return;
			list.addElements(parentOffset+from, a, offset, length);
			this.size += length;
		}
		
		@Override
		public char[] getElements(int from, char[] a, int offset, int length) {
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
		public char[] extractElements(int from, int to) {
			checkSubRange(from);
			checkAddSubRange(to);
			char[] result = list.extractElements(from+parentOffset, to+parentOffset);
			size -= result.length;
			return result;
		}
		
		@Override
		public char getChar(int index) {
			checkSubRange(index);
			return list.getChar(parentOffset+index);
		}

		@Override
		public char set(int index, char element) {
			checkSubRange(index);
			return list.set(parentOffset+index, element);
		}
		
		@Override
		public char swapRemove(int index) {
			checkSubRange(index);
			if(index == size-1) {
				char result = list.removeChar(parentOffset+size-1);
				size--;
				return result;
			}
			char result = list.set(index+parentOffset, list.getChar(parentOffset+size-1));
			list.removeChar(parentOffset+size-1);
			size--;
			return result;
		}
		
		@Override
		public char removeChar(int index) {
			checkSubRange(index);
			char result = list.removeChar(index+parentOffset);
			size--;
			return result;
		}
		
		@Override
		public int size() {
			return size;
		}
		
		@Override
		public CharSplititerator spliterator() { return CharSplititerators.createSplititerator(this, 16464); }
		
		@Override
		public CharListIterator listIterator(int index) {
			if(index < 0 || index > size()) throw new IndexOutOfBoundsException();
			return new SubListIterator(this, index);
		}
		
		@Override
		public CharList subList(int fromIndex, int toIndex) {
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
		
		private class SubListIterator implements CharListIterator
		{
			AbstractCharList list;
			int index;
			int lastReturned = -1;
			
			SubListIterator(AbstractCharList list, int index) {
				this.list = list;
				this.index = index;
			}
			
			@Override
			public boolean hasNext() {
				return index < list.size();
			}
			
			@Override
			public char nextChar() {
				if(!hasNext()) throw new NoSuchElementException();
				int i = index++;
				return list.getChar((lastReturned = i));
			}
			
			@Override
			public boolean hasPrevious() {
				return index > 0;
			}
			
			@Override
			public char previousChar() {
				if(!hasPrevious()) throw new NoSuchElementException();
				index--;
				return list.getChar((lastReturned = index));
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
				list.removeChar(lastReturned);
				index = lastReturned;
				lastReturned = -1;
			}
			
			@Override
			public void set(char e) {
				if(lastReturned == -1) throw new IllegalStateException();
				list.set(lastReturned, e);
			}
			
			@Override
			public void add(char e) {
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
		
	private class CharListIter implements CharListIterator {
		int index;
		int lastReturned = -1;
		
		CharListIter(int index) {
			this.index = index;
		}
		
		@Override
		public boolean hasNext() {
			return index < size();
		}
		
		@Override
		public char nextChar() {
			if(!hasNext()) throw new NoSuchElementException();
			int i = index++;
			return getChar((lastReturned = i));
		}
		
		@Override
		public boolean hasPrevious() {
			return index > 0;
		}
		
		@Override
		public char previousChar() {
			if(!hasPrevious()) throw new NoSuchElementException();
			index--;
			return getChar((lastReturned = index));
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
			AbstractCharList.this.removeChar(lastReturned);
			index = lastReturned;
			lastReturned = -1;
		}
		
		@Override
		public void set(char e) {
			if(lastReturned == -1) throw new IllegalStateException();
			AbstractCharList.this.set(lastReturned, e);
		}
		
		@Override
		public void add(char e) {
			AbstractCharList.this.add(index, e);
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