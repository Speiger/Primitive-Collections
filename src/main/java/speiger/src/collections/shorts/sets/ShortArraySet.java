package speiger.src.collections.shorts.sets;

import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.function.IntPredicate;
import speiger.src.collections.shorts.collections.ShortBidirectionalIterator;
import speiger.src.collections.shorts.collections.ShortCollection;
import speiger.src.collections.shorts.collections.ShortIterator;

import speiger.src.collections.objects.functions.consumer.ObjectShortConsumer;
import speiger.src.collections.shorts.functions.function.Short2BooleanFunction;
import speiger.src.collections.shorts.functions.function.ShortShortUnaryOperator;
import speiger.src.collections.shorts.functions.ShortConsumer;
import speiger.src.collections.objects.utils.ObjectArrays;
import speiger.src.collections.shorts.lists.ShortListIterator;
import speiger.src.collections.shorts.utils.ShortArrays;


/**
 * A Type Specific ArraySet implementation.
 * That is based around the idea of {@link java.util.List#indexOf(Object)} for no duplication.
 * Unless a array constructor is used the ArraySet does not allow for duplication.
 * This implementation does not shrink the backing array
 */
public class ShortArraySet extends AbstractShortSet implements ShortOrderedSet
{
	/** The Backing Array */
	protected transient short[] data;
	/** The amount of elements stored in the array*/
	protected int size = 0;
	
	/**
	 * Default Constructor
	 */
	public ShortArraySet() {
		data = ShortArrays.EMPTY_ARRAY;
	}
	
	/**
	 * Minimum Capacity Constructor
	 * @param capacity the minimum capacity of the internal array
	 * @throws NegativeArraySizeException if the capacity is negative
	 */
	public ShortArraySet(int capacity) {
		data = new short[capacity];
	}
	
	/**
	 * Constructur using initial Array
	 * @param array the array that should be used for set.
	 */
	public ShortArraySet(short[] array) {
		this(array, array.length);
	}
	
	/**
	 * Constructur using initial Array
	 * @param array the array that should be used for set.
	 * @param length the amount of elements present within the array
	 * @throws NegativeArraySizeException if the length is negative
	 */
	public ShortArraySet(short[] array, int length) {
		this(length);
		addAll(array, length);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param c the elements that should be added to the set.
	 * @note this slowly checks every element to remove duplicates
	 */
	@Deprecated
	public ShortArraySet(Collection<? extends Short> c) {
		this(c.size());
		addAll(c);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param c the elements that should be added to the set.
	 * @note this slowly checks every element to remove duplicates
	 */
	public ShortArraySet(ShortCollection c) {
		this(c.size());
		addAll(c);
	}
	
	/**
	 * A Helper constructor that fast copies the element out of a set into the ArraySet.
	 * Since it is assumed that there is no duplication in the first place
	 * @param s the set the element should be taken from
	 */
	@Deprecated
	public ShortArraySet(Set<? extends Short> s) {
		this(s.size());
		for(Short e : s)
			data[size++] = e.shortValue();
	}
	
	/**
	 * A Helper constructor that fast copies the element out of a set into the ArraySet.
	 * Since it is assumed that there is no duplication in the first place
	 * @param s the set the element should be taken from
	 */
	public ShortArraySet(ShortSet s) {
		this(s.size());
		for(ShortIterator iter = s.iterator();iter.hasNext();data[size++] = iter.nextShort());
	}
	
	@Override
	public boolean add(short o) {
		int index = findIndex(o);
		if(index == -1) {
			if(data.length == size) data = Arrays.copyOf(data, size == 0 ? 2 : size * 2);
			data[size++] = o;
			return true;
		}
		return false;
	}
	
	@Override
	public boolean addAndMoveToFirst(short o) {
		int index = findIndex(o);
		if(index == -1) {
			if(data.length == size) data = Arrays.copyOf(data, size == 0 ? 2 : size * 2);
			System.arraycopy(data, 0, data, 1, size++);
			data[0] = o;
			return true;
		}
		else if(index != 0) {
			o = data[index];
			System.arraycopy(data, 0, data, 1, index);
			data[0] = o;
		}
		return false;
	}

	@Override
	public boolean addAndMoveToLast(short o) {
		int index = findIndex(o);
		if(index == -1) {
			if(data.length == size) data = Arrays.copyOf(data, size == 0 ? 2 : size * 2);
			data[size++] = o;
			return true;
		}
		else if(index != size - 1) {
			o = data[index];
			System.arraycopy(data, index+1, data, index, size - index);
			data[size-1] = o;
		}
		return false;
	}

	@Override
	public boolean moveToFirst(short o) {
		int index = findIndex(o);
		if(index > 0) {
			o = data[index];
			System.arraycopy(data, 0, data, 1, index);
			data[0] = o;
			return true;
		}
		return false;
	}

	@Override
	public boolean moveToLast(short o) {
		int index = findIndex(o);
		if(index != -1 && index != size - 1) {
			o = data[index];
			System.arraycopy(data, index+1, data, index, size - index - 1);
			data[size-1] = o;
			return true;
		}
		return false;
	}
	
	@Override
	public boolean contains(short e) {
		return findIndex(e) != -1;
	}
	
	@Override
	public short firstShort() {
		if(size == 0) throw new NoSuchElementException();
		return data[0];
	}
	
	@Override
	public short lastShort() {
		if(size == 0) throw new NoSuchElementException();
		return data[size - 1];
	}
	
	@Override
	public boolean removeAll(ShortCollection c) {
		int j = 0;
		for(int i = 0;i<size;i++) {
			if(!c.contains(data[i])) 
				data[j++] = data[i];
		}
		boolean result = j != size;
		size = j;
		return result;
	}
	
	@Override
	public boolean removeAll(ShortCollection c, ShortConsumer r) {
		int j = 0;
		for(int i = 0;i<size;i++) {
			if(!c.contains(data[i])) data[j++] = data[i];
			else r.accept(data[i]);
		}
		boolean result = j != size;
		size = j;
		return result;
	}
	
	@Override
	public boolean retainAll(ShortCollection c) {
		int j = 0;
		for(int i = 0;i<size;i++) {
			if(c.contains(data[i])) 
				data[j++] = data[i];
		}
		boolean result = j != size;
		size = j;
		return result;
	}
	
	@Override
	public boolean retainAll(ShortCollection c, ShortConsumer r) {
		int j = 0;
		for(int i = 0;i<size;i++) {
			if(c.contains(data[i])) data[j++] = data[i];
			else r.accept(data[i]);
		}
		boolean result = j != size;
		size = j;
		return result;
	}
	
	@Override
	@Deprecated
	public boolean removeAll(Collection<?> c) {
		int j = 0;
		for(int i = 0;i<size;i++) {
			if(!c.contains(Short.valueOf(data[i])))
				data[j++] = data[i];
		}
		boolean result = j != size;
		size = j;
		return result;
	}
	
	@Override
	@Deprecated
	public boolean retainAll(Collection<?> c) {
		int j = 0;
		for(int i = 0;i<size;i++) {
			if(c.contains(Short.valueOf(data[i])))
				data[j++] = data[i];
		}
		boolean result = j != size;
		size = j;
		return result;
	}
	
	@Override
	public boolean remove(short o) {
		int index = findIndex(o);
		if(index != -1) {
			size--;
			if(index != size) System.arraycopy(data, index+1, data, index, size - index);
			return true;
		}
		return false;
	}
	
	@Override
	public short pollFirstShort() {
		if(size == 0) throw new NoSuchElementException();
		short result = data[0];
		System.arraycopy(data, 1, data, 0, --size);
		return result;
	}
	
	@Override
	public short pollLastShort() {
		if(size == 0) throw new NoSuchElementException();
		size--;
		return data[size];
	}
	@Override
	public boolean remIf(IntPredicate filter) {
		Objects.requireNonNull(filter);
		boolean modified = false;
		int j = 0;
		for(int i = 0;i<size;i++) {
			if(!filter.test(data[i])) data[j++] = data[i];
			else modified = true;
		}
		size = j;
		return modified;
	}
	
	@Override
	public void forEach(ShortConsumer action) {
		Objects.requireNonNull(action);
		for(int i = 0;i<size;action.accept(data[i++]));
	}
	
	@Override
	public <E> void forEach(E input, ObjectShortConsumer<E> action) {
		Objects.requireNonNull(action);
		for(int i = 0;i<size;i++)
			action.accept(input, data[i]);		
	}
	
	@Override
	public boolean matchesAny(Short2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(filter.get(data[i])) return true;
		}
		return false;
	}
	
	@Override
	public boolean matchesNone(Short2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(filter.get(data[i])) return false;
		}
		return true;
	}
	
	@Override
	public boolean matchesAll(Short2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(!filter.get(data[i])) return false;
		}
		return true;
	}
	
	@Override
	public short findFirst(Short2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(filter.get(data[i])) return data[i];
		}
		return (short)0;
	}
	
	@Override
	public short reduce(short identity, ShortShortUnaryOperator operator) {
		Objects.requireNonNull(operator);
		short state = identity;
		for(int i = 0;i<size;i++) {
			state = operator.applyAsShort(state, data[i]);
		}
		return state;
	}
	
	@Override
	public short reduce(ShortShortUnaryOperator operator) {
		Objects.requireNonNull(operator);
		short state = (short)0;
		boolean empty = true;
		for(int i = 0;i<size;i++) {
			if(empty) {
				empty = false;
				state = data[i];
				continue;
			}
			state = operator.applyAsShort(state, data[i]);
		}
		return state;
	}
	
	@Override
	public int count(Short2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		int result = 0;
		for(int i = 0;i<size;i++) {
			if(filter.get(data[i])) result++;
		}
		return result;
	}
	
	protected int findIndex(short o) {
		for(int i = size-1;i>=0;i--)
			if(data[i] == o) return i;
		return -1;
	}
	
	protected int findIndex(Object o) {
		for(int i = size-1;i>=0;i--)
			if(Objects.equals(o, Short.valueOf(data[i]))) return i;
		return -1;
	}
	
	@Override
	public ShortBidirectionalIterator iterator() {
		return new SetIterator(0);
	}
	
	@Override
	public ShortBidirectionalIterator iterator(short fromElement) {
		int index = findIndex(fromElement);
		if(index != -1) return new SetIterator(index);
		throw new NoSuchElementException();
	}
	
	public ShortArraySet copy() {
		ShortArraySet set = new ShortArraySet();
		set.data = Arrays.copyOf(data, data.length);
		set.size = size;
		return set;
	}
	
	@Override
	public void clear() {
		size = 0;
	}
	
	@Override
	public int size() {
		return size;
	}
	
	@Override
	public short[] toShortArray(short[] a) {
		if(a == null || a.length < size()) return Arrays.copyOf(data, size());
		System.arraycopy(data, 0, a, 0, size());
		if (a.length > size) a[size] = (short)0;
		return a;
	}
	
	@Override
	@Deprecated
	public Object[] toArray() {
		Object[] obj = new Object[size()];
		for(int i = 0;i<size();i++)
			obj[i] = Short.valueOf(data[i]);
		return obj;
	}
	
	@Override
	@Deprecated
	public <E> E[] toArray(E[] a) {
		if(a == null) a = (E[])new Object[size()];
		else if(a.length < size()) a = (E[])ObjectArrays.newArray(a.getClass().getComponentType(), size());
		for(int i = 0;i<size();i++)
			a[i] = (E)Short.valueOf(data[i]);
		if (a.length > size) a[size] = null;
		return a;
	}
	
//	Disabled until a Proper Implementation can be thought out or it is decided that the interface is thrown out.
//	private class SubSet extends AbstractShortSet implements ShortSortedSet {
//		int offset;
//		int length;
//		
//		SubSet(int offset, int length) {
//			this.offset = offset;
//			this.length = length;
//		}
//		
//		int end() { return offset+length; }
//
//		@Override
//		public boolean add(short o) {
//			int index = findIndex(o);
//			if(index == -1) {
//				if(data.length == size) data = Arrays.copyOf(data, size == 0 ? 2 : size * 2);
//				if(end() != size) System.arraycopy(data, end(), data, end()+1, size-(offset+length));
//				data[end()] = o;
//				size++;
//				length++;
//				return true;
//			}
//			return false;
//		}
//
//		@Override
//		public boolean addAndMoveToFirst(short o) {
//			int index = findIndex(o);
//			if(index == -1) {
//				if(data.length == size) data = Arrays.copyOf(data, size == 0 ? 2 : size * 2);
//				System.arraycopy(data, offset, data, offset+1, size-offset);
//				data[offset] = o;
//				size++;
//				length++;
//				return true;
//			}
//			else if(index != 0) {
//				o = data[index];
//				System.arraycopy(data, offset, data, offset+1, index-offset);
//				data[offset] = o;
//			}
//			return false;
//		}
//		
//		@Override
//		public boolean addAndMoveToLast(short o) {
//			int index = findIndex(o);
//			if(index == -1) {
//				if(data.length == size) data = Arrays.copyOf(data, size == 0 ? 2 : size * 2);
//				System.arraycopy(data, end()+1, data, end(), size-end());
//				data[end()] = o;
//				size++;
//				length++;
//				return true;
//			}
//			else if(index != 0) {
//				o = data[index];
//				System.arraycopy(data, offset+1, data, offset, index-offset);
//				data[offset+length] = o;
//			}
//			return false;
//		}
//		
//		@Override
//		public boolean moveToFirst(short o) {
//			int index = findIndex(o);
//			if(index > offset) {
//				o = data[index];
//				System.arraycopy(data, offset, data, offset+1, index-offset);
//				data[offset] = o;
//				return true;
//			}
//			return false;
//		}
//		
//		@Override
//		public boolean moveToLast(short o) {
//			int index = findIndex(o);
//			if(index != -1 && index < end()-1) {
//				o = data[index];
//				System.arraycopy(data, index+1, data, index, end()-index-1);
//				data[end()-1] = o;
//				return true;
//			}
//			return false;
//		}
//		
//#if !TYPE_OBJECT
//		@Override
//		public boolean contains(short e) {
//			return findIndex(e) != -1;
//		}
//		
//#endif
//		@Override
//		public boolean contains(Object e) {
//			return findIndex(e) != -1;
//		}
//		
//		@Override
//		public short firstShort() {
//			if(length == 0) throw new NoSuchElementException();
//			return data[offset];
//		}
//		
//		@Override
//		public short lastShort() {
//			if(length == 0) throw new NoSuchElementException();
//			return data[end()-1];
//		}
//		
//#if !TYPE_OBJECT
//		@Override
//		public boolean remove(short o) {
//			int index = findIndex(o);
//			if(index != -1) {
//				size--;
//				length--;
//				if(index != size) System.arraycopy(data, index+1, data, index, size - index);
//				return true;
//			}
//			return false;
//		}
//		
//#endif
//		@Override
//		public boolean remove(Object o) {
//			int index = findIndex(o);
//			if(index != -1) {
//				size--;
//				length--;
//				if(index != size) System.arraycopy(data, index+1, data, index, size - index);
//#if TYPE_OBJECT
//				data[size] = (short)0;
//#endif
//				return true;
//			}
//			return false;
//		}
//		
//		@Override
//		public short pollFirstShort() {
//			if(length == 0) throw new NoSuchElementException();
//			size--;
//			length--;
//			short result = data[offset];
//			System.arraycopy(data, offset+1, data, offset, size-offset);
//#if TYPE_OBJECT
//			data[size] = (short)0;
//#endif
//			return result;
//		}
//		
//		@Override
//		public short pollLastShort() {
//			if(length == 0) throw new NoSuchElementException();
//			short result = data[offset+length];
//			size--;
//			length--;
//			System.arraycopy(data, end()+1, data, end(), size-end());
//#if TYPE_OBJECT
//			data[size] = (short)0;
//#endif
//			return result;
//		}
//		
//		@Override
//		public ShortComparator comparator() {
//			return null;
//		}
//
//		@Override
//		public ShortBidirectionalIterator iterator() {
//			return new SetIterator(offset);
//		}
//
//		@Override
//		public ShortBidirectionalIterator iterator(short fromElement) {
//			int index = findIndex(fromElement);
//			if(index != -1) return new SetIterator(index);
//			throw new NoSuchElementException();
//		}
//		
//		@Override
//		public SubSet copy() { throw new UnsupportedOperationException(); }
//		
//		@Override
//		public ShortSortedSet subSet(short fromElement, short toElement) {
//			int fromIndex = findIndex(fromElement);
//			int toIndex = findIndex(toElement);
//			if(fromIndex == -1 || toIndex == -1) throw new NoSuchElementException();
//			return new SubSet(fromIndex, toIndex - fromIndex + 1);
//		}
//
//		@Override
//		public ShortSortedSet headSet(short toElement) {
//			int toIndex = findIndex(toElement);
//			if(toIndex == -1) throw new NoSuchElementException();
//			return new SubSet(0, toIndex+1);
//		}
//
//		@Override
//		public ShortSortedSet tailSet(short fromElement) {
//			int fromIndex = findIndex(fromElement);
//			if(fromIndex == -1) throw new NoSuchElementException();
//			return new SubSet(fromIndex, size - fromIndex);
//		}
//		
//		@Override
//		public int size() {
//			return length;
//		}
//		
//#if !TYPE_OBJECT
//		@Override
//		public short[] toShortArray(short[] a) {
//			if(a == null || a.length < size()) return Arrays.copyOfRange(data, offset, end());
//			System.arraycopy(data, offset, a, 0, size());
//			return a;
//		}
//		
//#endif
//		@Override
//		@Deprecated
//		public Object[] toArray() {
//			Object[] obj = new Object[size()];
//			for(int i = 0;i<size();i++)
//				obj[i] = Short.valueOf(data[offset+i]);
//			return obj;
//		}
//		
//		@Override
//		@Deprecated
//		public <E> E[] toArray(E[] a) {
//			if(a == null) a = (E[])new Object[size()];
//			else if(a.length < size()) a = (E[])ObjectArrays.newArray(a.getClass().getComponentType(), size());
//			for(int i = 0;i<size();i++)
//				a[i] = (E)Short.valueOf(data[offset+i]);
//			return a;
//		}
//		
//#if !TYPE_OBJECT
//		protected int findIndex(short o) {
//			for(int i = length-1;i>=0;i--)
//				if(data[offset+i] == o) return i + offset;
//			return -1;
//		}
//		
//#endif
//		protected int findIndex(Object o) {
//			for(int i = length-1;i>=0;i--)
//				if(Objects.equals(o, Short.valueOf(data[offset+i]))) return i + offset;
//			return -1;
//		}
//		
//		private class SetIterator implements ShortListIterator {
//			int index;
//			int lastReturned = -1;
//			
//			public SetIterator(int index) {
//				this.index = index;
//			}
//			
//			@Override
//			public boolean hasNext() {
//				return index < size();
//			}
//			
//			@Override
//			public short nextShort() {
//				if(!hasNext()) throw new NoSuchElementException();
//				lastReturned = index;
//				return data[index++];
//			}
//			
//			@Override
//			public boolean hasPrevious() {
//				return index > 0;
//			}
//			
//			@Override
//			public short previousShort() {
//				if(!hasPrevious()) throw new NoSuchElementException();
//				lastReturned = index;
//				return data[index--];
//			}
//			
//			@Override
//			public int nextIndex() {
//				return index;
//			}
//			
//			@Override
//			public int previousIndex() {
//				return index-1;
//			}
//			
//			@Override
//			public void remove() {
//				if(lastReturned == -1)
//					throw new IllegalStateException();
//				SubSet.this.remove(data[lastReturned]);
//				if(lastReturned < index)
//					index--;
//				lastReturned = -1;
//			}
//			
//		#if TYPE_OBJECT
//			@Override
//			public void set(Object e) { throw new UnsupportedOperationException(); }
//			
//			@Override
//			public void add(Object e) { throw new UnsupportedOperationException(); }
//			
//		#else
//			@Override
//			public void set(short e) { throw new UnsupportedOperationException(); }
//		
//			@Override
//			public void add(short e) { throw new UnsupportedOperationException(); }
//			
//		#endif
//			@Override
//			public int skip(int amount) {
//				if(amount < 0) throw new IllegalStateException("Negative Numbers are not allowed");
//				int steps = Math.min(amount, (size() - 1) - index);
//				index += steps;
//				return steps;
//			}
//			
//			@Override
//			public int back(int amount) {
//				if(amount < 0) throw new IllegalStateException("Negative Numbers are not allowed");
//				int steps = Math.min(amount, index);
//				index -= steps;
//				return steps;
//			}
//		}
//	}
	
	private class SetIterator implements ShortListIterator {
		int index;
		int lastReturned = -1;
		
		public SetIterator(int index) {
			this.index = index;
		}
		
		@Override
		public boolean hasNext() {
			return index < size();
		}
		
		@Override
		public short nextShort() {
			if(!hasNext()) throw new NoSuchElementException();
			lastReturned = index;
			return data[index++];
		}
		
		@Override
		public boolean hasPrevious() {
			return index > 0;
		}
		
		@Override
		public short previousShort() {
			if(!hasPrevious()) throw new NoSuchElementException();
			lastReturned = index;
			return data[index--];
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
			ShortArraySet.this.remove(data[lastReturned]);
			if(lastReturned < index) index--;
			lastReturned = -1;
		}
		
		@Override
		public void set(short e) { throw new UnsupportedOperationException(); }

		@Override
		public void add(short e) { throw new UnsupportedOperationException(); }
		
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