package speiger.src.collections.chars.utils;

import java.util.Collection;
import java.util.Objects;
import java.util.Random;
import java.util.RandomAccess;
import java.util.function.Consumer;
import java.nio.CharBuffer;

import speiger.src.collections.chars.collections.CharCollection;
import speiger.src.collections.chars.functions.CharConsumer;
import speiger.src.collections.chars.lists.AbstractCharList;
import speiger.src.collections.chars.lists.CharList;
import speiger.src.collections.chars.lists.CharListIterator;
import speiger.src.collections.chars.utils.CharArrays;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Helper class for Lists
 */
public class CharLists
{
	/**
	 * Empty List reference
	 */
	public static final EmptyList EMPTY = new EmptyList();
	
	/**
	 * Returns a Immutable EmptyList instance that is automatically casted.
	 * @return an empty list
	 */
	public static EmptyList empty() {
		return EMPTY;
	}
	
	/**
	 * Returns a Immutable List instance based on the instance given.
	 * @param l that should be made immutable/unmodifiable
	 * @return a unmodifiable list wrapper. If the list is implementing RandomAccess that is also transferred. If the List already a unmodifiable wrapper then it just returns itself.
	 */
	public static CharList unmodifiable(CharList l) {
		return l instanceof UnmodifiableList ? l : l instanceof RandomAccess ? new UnmodifiableRandomList(l) : new UnmodifiableList(l);
	}
	
	/**
	 * Returns a synchronized List instance based on the instance given.
	 * @param l that should be synchronized
	 * @return a synchronized list wrapper. If the list is implementing RandomAccess or ICharArray that is also transferred. If the List already a synchronized wrapper then it just returns itself.
	 */
	public static CharList synchronize(CharList l) {
		return l instanceof SynchronizedList ? l : (l instanceof ICharArray ? new SynchronizedArrayList(l) : (l instanceof RandomAccess ? new SynchronizedRandomAccessList(l) : new SynchronizedList(l)));
	}
	
	/**
	 * Returns a synchronized List instance based on the instance given.
	 * @param l that should be synchronized
	 * @param mutex is the controller of the synchronization block.
	 * @return a synchronized list wrapper. If the list is implementing RandomAccess or ICharArray that is also transferred. If the List already a synchronized wrapper then it just returns itself.
	 */
	public static CharList synchronize(CharList l, Object mutex) {
		return l instanceof SynchronizedList ? l : (l instanceof ICharArray ? new SynchronizedArrayList(l, mutex) : (l instanceof RandomAccess ? new SynchronizedRandomAccessList(l, mutex) : new SynchronizedList(l, mutex)));
	}
	
	/**
	 * Creates a Unmodifiable Singleton list
	 * @param element that should be used in the Singleton
	 * @return a singleton list that is unmodifiable
	 */
	public static CharList singleton(char element) {
		return new SingletonList(element);
	}
	
	/**
	 * Reverses the list
	 * @param list that should be reversed.
	 * @return the input list
	 */
	public static CharList reverse(CharList list) {
		int size = list.size();
		if(list instanceof ICharArray) {
			ICharArray array = (ICharArray)list;
			if(list instanceof SynchronizedArrayList) array.elements(T -> CharArrays.reverse(T, size));
			else CharArrays.reverse(array.elements(), size);
			return list;
		}
		if(list instanceof RandomAccess) {
			for (int i = 0, mid = size >> 1, j = size - 1; i < mid; i++, j--) {
				char t = list.getChar(i);
				list.set(i, list.getChar(j));
				list.set(j, t);
			}
			return list;
		}
		CharListIterator fwd = list.listIterator();
		CharListIterator rev = list.listIterator(size);
		for(int i = 0, mid = size >> 1; i < mid; i++) {
			char tmp = fwd.nextChar();
			fwd.set(rev.previousChar());
			rev.set(tmp);
		}
		return list;
	}
	
	/**
	 * Shuffles the list
	 * @param list that should be Shuffled.
	 * @return the input list
	 */
	public static CharList shuffle(CharList list) {
		return shuffle(list, SanityChecks.getRandom());
	}
	
	/**
	 * Shuffles the list
	 * @param list that should be Shuffled.
	 * @param random the random that should be used
	 * @return the input list
	 */
	public static CharList shuffle(CharList list, Random random) {
		int size = list.size();
		if(list instanceof ICharArray) {
			ICharArray array = (ICharArray)list;
			if(list instanceof SynchronizedArrayList)  array.elements(T -> CharArrays.shuffle(T, size, random));
			else CharArrays.shuffle(array.elements(), size, random);
			return list;
		}
		for(int i = list.size(); i-- != 0;) {
			int p = random.nextInt(i + 1);
			char t = list.getChar(i);
			list.set(i, list.getChar(p));
			list.set(p, t);
		}
		return list;
	}
	
	private static class SingletonList extends AbstractCharList
	{
		char element;
		
		SingletonList(char element)
		{
			this.element = element;
		}
		@Override
		public void add(int index, char e) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(int index, Collection<? extends Character> c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(int index, CharCollection c) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAll(int index, CharList c) { throw new UnsupportedOperationException(); }

		@Override
		public char getChar(int index) {
			if(index == 0) return element;
			throw new IndexOutOfBoundsException();
		}
		
		@Override
		public char set(int index, char e) { throw new UnsupportedOperationException(); }
		@Override
		public char removeChar(int index) { throw new UnsupportedOperationException(); }
		@Override
		public char swapRemove(int index) { throw new UnsupportedOperationException(); }
		@Override
		public void addElements(int from, char[] a, int offset, int length) { throw new UnsupportedOperationException(); }
		@Override
		public char[] getElements(int from, char[] a, int offset, int length) {
			if(from != 0 || length != 1) throw new IndexOutOfBoundsException();
			a[offset] = element;
			return a;
		}
		@Override
		public void fillBuffer(CharBuffer buffer) { buffer.put(element); }
		
		@Override
		public void removeElements(int from, int to) { throw new UnsupportedOperationException(); }
		@Override
		public char[] extractElements(int from, int to) { throw new UnsupportedOperationException(); }
		@Override
		public int size() { return 1; }
		
		@Override
		public SingletonList copy() { return new SingletonList(element); }
	}
	
	private static class SynchronizedArrayList extends SynchronizedList implements ICharArray
	{
		ICharArray l;
		
		SynchronizedArrayList(CharList l) {
			super(l);
			this.l = (ICharArray)l;
		}
		
		SynchronizedArrayList(CharList l, Object mutex) {
			super(l, mutex);
			this.l = (ICharArray)l;
		}
		
		@Override
		public void ensureCapacity(int size) { synchronized(mutex) { l.ensureCapacity(size); } }
		
		@Override
		public boolean trim(int size) { synchronized(mutex) { return l.trim(size); } }
		
		@Override
		public void clearAndTrim(int size) { synchronized(mutex) { l.clearAndTrim(size); } }
		
		@Override
		public char[] elements() { synchronized(mutex) { return l.elements(); } }
		
		@Override
		public void elements(Consumer<char[]> action) { 
			Objects.requireNonNull(action);
			synchronized(mutex) {
				l.elements(action);
			}
		}
	}
	
	private static class SynchronizedRandomAccessList extends SynchronizedList implements RandomAccess 
	{
		SynchronizedRandomAccessList(CharList l) {
			super(l);
		}
		
		SynchronizedRandomAccessList(CharList l, Object mutex) {
			super(l, mutex);
		}
	}
	
	private static class SynchronizedList extends CharCollections.SynchronizedCollection implements CharList
	{
		CharList l;
		
		SynchronizedList(CharList l) {
			super(l);
			this.l = l;
		}
		
		SynchronizedList(CharList l, Object mutex) {
			super(l, mutex);
			this.l = l;
		}
		
		@Override
		public boolean addAll(int index, Collection<? extends Character> c) { synchronized(mutex) { return l.addAll(index, c); } }
		
		@Override
		public void add(int index, Character element) { synchronized(mutex) { l.add(index, element); } }
		
		@Override
		public void add(int index, char e) { synchronized(mutex) { l.add(index, e); } }
		
		@Override
		public boolean addAll(int index, CharCollection c) { synchronized(mutex) { return l.addAll(index, c); } }
		
		@Override
		public boolean addAll(CharList c) { synchronized(mutex) { return l.addAll(c); } }
		
		@Override
		public boolean addAll(int index, CharList c) { synchronized(mutex) { return l.addAll(index, c); } }
		
		@Override
		public char getChar(int index) { synchronized(mutex) { return l.getChar(index); } }
		
		@Override
		public void forEach(CharConsumer action) { synchronized(mutex) { l.forEach(action); } }
		
		@Override
		public char set(int index, char e) { synchronized(mutex) { return l.set(index, e); } }
		
		@Override
		public char removeChar(int index) { synchronized(mutex) { return l.removeChar(index); } }
		
		@Override
		public char swapRemove(int index) { synchronized(mutex) { return l.swapRemove(index); } }
		
		@Override
		public boolean swapRemoveChar(char e) { synchronized(mutex) { return l.swapRemoveChar(e); } }
		
		@Override
		@Deprecated
		public int indexOf(Object e) { synchronized(mutex) { return l.indexOf(e); } }
		
		@Override
		@Deprecated
		public int lastIndexOf(Object e) { synchronized(mutex) { return l.lastIndexOf(e); } }
				
		@Override
		public int indexOf(char e) { synchronized(mutex) { return l.indexOf(e); } }
		
		@Override
		public int lastIndexOf(char e) { synchronized(mutex) { return l.lastIndexOf(e); } }
		
		@Override
		public void addElements(int from, char[] a, int offset, int length) { synchronized(mutex) { addElements(from, a, offset, length); } }
		
		@Override
		public char[] getElements(int from, char[] a, int offset, int length) { synchronized(mutex) { return l.getElements(from, a, offset, length); } }
		
		@Override
		public void removeElements(int from, int to) { synchronized(mutex) { l.removeElements(from, to); } }
		
		@Override
		public char[] extractElements(int from, int to) { synchronized(mutex) { return l.extractElements(from, to); } }
		
		public void fillBuffer(CharBuffer buffer) { synchronized(mutex) { l.fillBuffer(buffer); } }
		@Override
		public CharListIterator listIterator() {
			return l.listIterator();
		}
		
		@Override
		public CharListIterator listIterator(int index) {
			return l.listIterator(index);
		}
		
		@Override
		public CharList subList(int from, int to) {
			return CharLists.synchronize(l.subList(from, to));
		}
		
		@Override
		public void size(int size) { synchronized(mutex) { l.size(size); } }
		
		@Override
		public CharList copy() { synchronized(mutex) { return l.copy(); } }
	}
	
	private static class UnmodifiableRandomList extends UnmodifiableList implements RandomAccess 
	{
		UnmodifiableRandomList(CharList l) {
			super(l);
		}
	}
	
	private static class UnmodifiableList extends CharCollections.UnmodifiableCollection implements CharList
	{
		final CharList l;
		
		UnmodifiableList(CharList l) {
			super(l);
			this.l = l;
		}
		
		@Override
		public boolean addAll(int index, Collection<? extends Character> c) { throw new UnsupportedOperationException(); }
		
		@Override
		public void add(int index, Character element) { throw new UnsupportedOperationException(); }
		
		@Override
		public void add(int index, char e) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(int index, CharCollection c) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(CharList c) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(int index, CharList c) { throw new UnsupportedOperationException(); }
		
		@Override
		public char getChar(int index) { return l.getChar(index); }
		
		@Override
		public void forEach(CharConsumer action) { l.forEach(action); }
		
		@Override
		public char set(int index, char e) { throw new UnsupportedOperationException(); }
		
		@Override
		public char removeChar(int index) { throw new UnsupportedOperationException(); }
		
		@Override
		public char swapRemove(int index) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean swapRemoveChar(char e) { throw new UnsupportedOperationException(); }
		
		@Override
		@Deprecated
		public int indexOf(Object e) { return l.indexOf(e); }
		
		@Override
		@Deprecated
		public int lastIndexOf(Object e) { return l.lastIndexOf(e); }
				
		@Override
		public int indexOf(char e) { return l.indexOf(e); }
		
		@Override
		public int lastIndexOf(char e) { return l.lastIndexOf(e); }
		
		@Override
		public void addElements(int from, char[] a, int offset, int length) { throw new UnsupportedOperationException(); }
		
		@Override
		public char[] getElements(int from, char[] a, int offset, int length) {
			return l.getElements(from, a, offset, length);
		}
		
		@Override
		public void removeElements(int from, int to) { throw new UnsupportedOperationException(); }
		
		@Override
		public char[] extractElements(int from, int to) { throw new UnsupportedOperationException(); }
		
		public void fillBuffer(CharBuffer buffer) { l.fillBuffer(buffer); }
		
		@Override
		public CharListIterator listIterator() {
			return CharIterators.unmodifiable(l.listIterator());
		}
		
		@Override
		public CharListIterator listIterator(int index) {
			return CharIterators.unmodifiable(l.listIterator(index));
		}
		
		@Override
		public CharList subList(int from, int to) {
			return CharLists.unmodifiable(l.subList(from, to));
		}
		
		@Override
		public void size(int size) { throw new UnsupportedOperationException(); }
		
		@Override
		public CharList copy() { return l.copy(); }
	}
	
	private static class EmptyList extends CharCollections.EmptyCollection implements CharList
	{
		@Override
		public boolean addAll(int index, Collection<? extends Character> c) { throw new UnsupportedOperationException(); }
		
		@Override
		public void add(int index, Character element) { throw new UnsupportedOperationException(); }
		
		@Override
		public void add(int index, char e) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(int index, CharCollection c) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(CharList c) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean addAll(int index, CharList c) { throw new UnsupportedOperationException(); }
		
		@Override
		public char getChar(int index) { throw new IndexOutOfBoundsException(); }
		
		@Override
		public char set(int index, char e) { throw new UnsupportedOperationException(); }
		
		@Override
		public char removeChar(int index) { throw new UnsupportedOperationException(); }
		
		@Override
		public char swapRemove(int index) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean swapRemoveChar(char e) { throw new UnsupportedOperationException(); }
		
		@Override
		public int indexOf(Object e) { return -1; }
		
		@Override
		public int lastIndexOf(Object e) { return -1; }
				
		@Override
		public int indexOf(char e) { return -1; }
		
		@Override
		public int lastIndexOf(char e) { return -1; }
		
		@Override
		public void addElements(int from, char[] a, int offset, int length){ throw new UnsupportedOperationException(); }
		
		@Override
		public char[] getElements(int from, char[] a, int offset, int length) { throw new IndexOutOfBoundsException(); }
		
		@Override
		public void removeElements(int from, int to) { throw new UnsupportedOperationException(); }
		
		@Override
		public char[] extractElements(int from, int to) { throw new UnsupportedOperationException(); }
		
		@Override
		public CharListIterator listIterator() {
			return CharIterators.empty();
		}
		
		@Override
		public CharListIterator listIterator(int index) {
			if(index != 0)
				throw new IndexOutOfBoundsException();
			return CharIterators.empty();
		}
		
		@Override
		public CharList subList(int from, int to) { throw new UnsupportedOperationException(); }
		
		@Override
		public void size(int size) { throw new UnsupportedOperationException(); }
		
		@Override
		public EmptyList copy() { return this; }
	}
}