package speiger.src.collections.objects.sets;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.BiFunction;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;

import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;
import speiger.src.collections.objects.functions.function.Object2BooleanFunction;
import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectArrays;


/**
 * A Type Specific ArraySet implementation.
 * That is based around the idea of {@link java.util.List#indexOf(Object)} for no duplication.
 * Unless a array constructor is used the ArraySet does not allow for duplication.
 * This implementation does not shrink the backing array
 * @param <T> the type of elements maintained by this Collection
 */
public class ObjectArraySet<T> extends AbstractObjectSet<T> implements ObjectOrderedSet<T>
{
	/** The Backing Array */
	protected transient T[] data;
	/** The amount of elements stored in the array*/
	protected int size = 0;
	
	/**
	 * Default Constructor
	 */
	public ObjectArraySet() {
		data = (T[])ObjectArrays.EMPTY_ARRAY;
	}
	
	/**
	 * Minimum Capacity Constructor
	 * @param capacity the minimum capacity of the internal array
	 * @throws NegativeArraySizeException if the capacity is negative
	 */
	public ObjectArraySet(int capacity) {
		data = (T[])new Object[capacity];
	}
	
	/**
	 * Constructur using initial Array
	 * @param array the array that should be used for set.
	 */
	public ObjectArraySet(T[] array) {
		this(array, array.length);
	}
	
	/**
	 * Constructur using initial Array
	 * @param array the array that should be used for set.
	 * @param length the amount of elements present within the array
	 * @throws NegativeArraySizeException if the length is negative
	 */
	public ObjectArraySet(T[] array, int length) {
		this(length);
		addAll(array, length);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param c the elements that should be added to the set.
	 * @note this slowly checks every element to remove duplicates
	 */
	public ObjectArraySet(Collection<? extends T> c) {
		this(c.size());
		addAll(c);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param c the elements that should be added to the set.
	 * @note this slowly checks every element to remove duplicates
	 */
	public ObjectArraySet(ObjectCollection<T> c) {
		this(c.size());
		addAll(c);
	}
	
	/**
	 * A Helper constructor that fast copies the element out of a set into the ArraySet.
	 * Since it is assumed that there is no duplication in the first place
	 * @param s the set the element should be taken from
	 */
	public ObjectArraySet(Set<? extends T> s) {
		this(s.size());
		for(T e : s)
			data[size++] = e;
	}
	
	/**
	 * A Helper constructor that fast copies the element out of a set into the ArraySet.
	 * Since it is assumed that there is no duplication in the first place
	 * @param s the set the element should be taken from
	 */
	public ObjectArraySet(ObjectSet<T> s) {
		this(s.size());
		for(ObjectIterator<T> iter = s.iterator();iter.hasNext();data[size++] = iter.next());
	}
	
	@Override
	public boolean add(T o) {
		int index = findIndex(o);
		if(index == -1) {
			if(data.length == size) data = Arrays.copyOf(data, size == 0 ? 2 : size * 2);
			data[size++] = o;
			return true;
		}
		return false;
	}
	
	@Override
	public T addOrGet(T o) {
		int index = findIndex(o);
		if(index != -1) return data[index];
		if(data.length == size) data = Arrays.copyOf(data, size == 0 ? 2 : size * 2);
		data[size++] = o;
		return o;
	}
	
	@Override
	public boolean addAndMoveToFirst(T o) {
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
	public boolean addAndMoveToLast(T o) {
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
	public boolean moveToFirst(T o) {
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
	public boolean moveToLast(T o) {
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
	public boolean contains(Object e) {
		return findIndex(e) != -1;
	}
	
	@Override
	public T first() {
		if(size == 0) throw new NoSuchElementException();
		return data[0];
	}
	
	@Override
	public T last() {
		if(size == 0) throw new NoSuchElementException();
		return data[size - 1];
	}
	
	@Override
	public boolean removeAll(ObjectCollection<T> c) {
		int j = 0;
		for(int i = 0;i<size;i++) {
			if(!c.contains(data[i])) 
				data[j++] = data[i];
		}
		boolean result = j != size;
		Arrays.fill(data, j, size, null);
		size = j;
		return result;
	}
	
	@Override
	public boolean removeAll(ObjectCollection<T> c, Consumer<T> r) {
		int j = 0;
		for(int i = 0;i<size;i++) {
			if(!c.contains(data[i])) data[j++] = data[i];
			else r.accept(data[i]);
		}
		boolean result = j != size;
		Arrays.fill(data, j, size, null);
		size = j;
		return result;
	}
	
	@Override
	public boolean retainAll(ObjectCollection<T> c) {
		int j = 0;
		for(int i = 0;i<size;i++) {
			if(c.contains(data[i])) 
				data[j++] = data[i];
		}
		boolean result = j != size;
		Arrays.fill(data, j, size, null);
		size = j;
		return result;
	}
	
	@Override
	public boolean retainAll(ObjectCollection<T> c, Consumer<T> r) {
		int j = 0;
		for(int i = 0;i<size;i++) {
			if(c.contains(data[i])) data[j++] = data[i];
			else r.accept(data[i]);
		}
		boolean result = j != size;
		Arrays.fill(data, j, size, null);
		size = j;
		return result;
	}
	
	@Override
	public boolean removeAll(Collection<?> c) {
		int j = 0;
		for(int i = 0;i<size;i++) {
			if(!c.contains(data[i]))
				data[j++] = data[i];
		}
		boolean result = j != size;
		Arrays.fill(data, j, size, null);
		size = j;
		return result;
	}
	
	@Override
	public boolean retainAll(Collection<?> c) {
		int j = 0;
		for(int i = 0;i<size;i++) {
			if(c.contains(data[i]))
				data[j++] = data[i];
		}
		boolean result = j != size;
		Arrays.fill(data, j, size, null);
		size = j;
		return result;
	}
	
	@Override
	public boolean remove(Object o) {
		int index = findIndex(o);
		if(index != -1) {
			size--;
			if(index != size) System.arraycopy(data, index+1, data, index, size - index);
			data[size] = null;
			return true;
		}
		return false;
	}
	
	@Override
	public T pollFirst() {
		if(size == 0) throw new NoSuchElementException();
		T result = data[0];
		System.arraycopy(data, 1, data, 0, --size);
		data[size] = null;
		return result;
	}
	
	@Override
	public T pollLast() {
		if(size == 0) throw new NoSuchElementException();
		size--;
		T result = data[size];
		data[size] = null;
		return result;
	}
	@Override
	public void forEach(Consumer<? super T> action) {
		Objects.requireNonNull(action);
		for(int i = 0;i<size;action.accept(data[i++]));
	}
	
	@Override
	public <E> void forEach(E input, ObjectObjectConsumer<E, T> action) {
		Objects.requireNonNull(action);
		for(int i = 0;i<size;i++)
			action.accept(input, data[i]);		
	}
	
	@Override
	public boolean matchesAny(Object2BooleanFunction<T> filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(filter.getBoolean(data[i])) return true;
		}
		return false;
	}
	
	@Override
	public boolean matchesNone(Object2BooleanFunction<T> filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(filter.getBoolean(data[i])) return false;
		}
		return true;
	}
	
	@Override
	public boolean matchesAll(Object2BooleanFunction<T> filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(!filter.getBoolean(data[i])) return false;
		}
		return true;
	}
	
	@Override
	public T findFirst(Object2BooleanFunction<T> filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(filter.getBoolean(data[i])) return data[i];
		}
		return null;
	}
	
	@Override
	public <E> E reduce(E identity, BiFunction<E, T, E> operator) {
		Objects.requireNonNull(operator);
		E state = identity;
		for(int i = 0;i<size;i++) {
			state = operator.apply(state, data[i]);
		}
		return state;
	}
	
	@Override
	public T reduce(ObjectObjectUnaryOperator<T, T> operator) {
		Objects.requireNonNull(operator);
		T state = null;
		boolean empty = true;
		for(int i = 0;i<size;i++) {
			if(empty) {
				empty = false;
				state = data[i];
				continue;
			}
			state = operator.apply(state, data[i]);
		}
		return state;
	}
	
	@Override
	public int count(Object2BooleanFunction<T> filter) {
		Objects.requireNonNull(filter);
		int result = 0;
		for(int i = 0;i<size;i++) {
			if(filter.getBoolean(data[i])) result++;
		}
		return result;
	}
	
	protected int findIndex(Object o) {
		for(int i = size-1;i>=0;i--)
			if(Objects.equals(o, data[i])) return i;
		return -1;
	}
	
	@Override
	public ObjectBidirectionalIterator<T> iterator() {
		return new SetIterator(0);
	}
	
	@Override
	public ObjectBidirectionalIterator<T> iterator(T fromElement) {
		int index = findIndex(fromElement);
		if(index != -1) return new SetIterator(index);
		throw new NoSuchElementException();
	}
	
	public ObjectArraySet<T> copy() {
		ObjectArraySet<T> set = new ObjectArraySet<>();
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
	@Deprecated
	public Object[] toArray() {
		Object[] obj = new Object[size()];
		for(int i = 0;i<size();i++)
			obj[i] = data[i];
		return obj;
	}
	
	@Override
	public <E> E[] toArray(E[] a) {
		if(a == null) a = (E[])new Object[size()];
		else if(a.length < size()) a = (E[])ObjectArrays.newArray(a.getClass().getComponentType(), size());
		for(int i = 0;i<size();i++)
			a[i] = (E)data[i];
		if (a.length > size) a[size] = null;
		return a;
	}
	
//	Disabled until a Proper Implementation can be thought out or it is decided that the interface is thrown out.
//	private class SubSet extends AbstractObjectSet<T> implements ObjectSortedSet<T> {
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
//		public boolean add(T o) {
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
//		public boolean addAndMoveToFirst(T o) {
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
//		public boolean addAndMoveToLast(T o) {
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
//		public boolean moveToFirst(T o) {
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
//		public boolean moveToLast(T o) {
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
//		public boolean contains(T e) {
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
//		public T first() {
//			if(length == 0) throw new NoSuchElementException();
//			return data[offset];
//		}
//		
//		@Override
//		public T last() {
//			if(length == 0) throw new NoSuchElementException();
//			return data[end()-1];
//		}
//		
//#if !TYPE_OBJECT
//		@Override
//		public boolean remove(T o) {
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
//				data[size] = null;
//#endif
//				return true;
//			}
//			return false;
//		}
//		
//		@Override
//		public T pollFirst() {
//			if(length == 0) throw new NoSuchElementException();
//			size--;
//			length--;
//			T result = data[offset];
//			System.arraycopy(data, offset+1, data, offset, size-offset);
//#if TYPE_OBJECT
//			data[size] = null;
//#endif
//			return result;
//		}
//		
//		@Override
//		public T pollLast() {
//			if(length == 0) throw new NoSuchElementException();
//			T result = data[offset+length];
//			size--;
//			length--;
//			System.arraycopy(data, end()+1, data, end(), size-end());
//#if TYPE_OBJECT
//			data[size] = null;
//#endif
//			return result;
//		}
//		
//		@Override
//		public Comparator<T> comparator() {
//			return null;
//		}
//
//		@Override
//		public ObjectBidirectionalIterator<T> iterator() {
//			return new SetIterator(offset);
//		}
//
//		@Override
//		public ObjectBidirectionalIterator<T> iterator(T fromElement) {
//			int index = findIndex(fromElement);
//			if(index != -1) return new SetIterator(index);
//			throw new NoSuchElementException();
//		}
//		
//		@Override
//		public SubSet copy() { throw new UnsupportedOperationException(); }
//		
//		@Override
//		public ObjectSortedSet<T> subSet(T fromElement, T toElement) {
//			int fromIndex = findIndex(fromElement);
//			int toIndex = findIndex(toElement);
//			if(fromIndex == -1 || toIndex == -1) throw new NoSuchElementException();
//			return new SubSet(fromIndex, toIndex - fromIndex + 1);
//		}
//
//		@Override
//		public ObjectSortedSet<T> headSet(T toElement) {
//			int toIndex = findIndex(toElement);
//			if(toIndex == -1) throw new NoSuchElementException();
//			return new SubSet(0, toIndex+1);
//		}
//
//		@Override
//		public ObjectSortedSet<T> tailSet(T fromElement) {
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
//		public T[] toArray(T[] a) {
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
//				obj[i] = data[offset+i];
//			return obj;
//		}
//		
//		@Override
//		public <E> E[] toArray(E[] a) {
//			if(a == null) a = (E[])new Object[size()];
//			else if(a.length < size()) a = (E[])ObjectArrays.newArray(a.getClass().getComponentType(), size());
//			for(int i = 0;i<size();i++)
//				a[i] = (E)data[offset+i];
//			return a;
//		}
//		
//#if !TYPE_OBJECT
//		protected int findIndex(T o) {
//			for(int i = length-1;i>=0;i--)
//				if(Objects.equals(data[offset+i], o)) return i + offset;
//			return -1;
//		}
//		
//#endif
//		protected int findIndex(Object o) {
//			for(int i = length-1;i>=0;i--)
//				if(Objects.equals(o, data[offset+i])) return i + offset;
//			return -1;
//		}
//		
//		private class SetIterator implements ObjectListIterator<T> {
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
//			public T next() {
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
//			public T previous() {
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
//			public void set(T e) { throw new UnsupportedOperationException(); }
//		
//			@Override
//			public void add(T e) { throw new UnsupportedOperationException(); }
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
	
	private class SetIterator implements ObjectListIterator<T> {
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
		public T next() {
			if(!hasNext()) throw new NoSuchElementException();
			lastReturned = index;
			return data[index++];
		}
		
		@Override
		public boolean hasPrevious() {
			return index > 0;
		}
		
		@Override
		public T previous() {
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
			ObjectArraySet.this.remove(data[lastReturned]);
			if(lastReturned < index) index--;
			lastReturned = -1;
		}
		
		@Override
		public void set(Object e) { throw new UnsupportedOperationException(); }
		
		@Override
		public void add(Object e) { throw new UnsupportedOperationException(); }
		
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