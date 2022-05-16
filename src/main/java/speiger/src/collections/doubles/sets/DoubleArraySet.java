package speiger.src.collections.doubles.sets;

import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.function.DoublePredicate;
import speiger.src.collections.doubles.collections.DoubleBidirectionalIterator;
import speiger.src.collections.doubles.collections.DoubleCollection;
import speiger.src.collections.doubles.collections.DoubleIterator;

import speiger.src.collections.objects.functions.consumer.ObjectDoubleConsumer;
import speiger.src.collections.doubles.functions.function.Double2BooleanFunction;
import speiger.src.collections.doubles.functions.function.DoubleDoubleUnaryOperator;
import speiger.src.collections.doubles.functions.DoubleConsumer;
import speiger.src.collections.objects.utils.ObjectArrays;
import speiger.src.collections.doubles.lists.DoubleListIterator;
import speiger.src.collections.doubles.utils.DoubleArrays;


/**
 * A Type Specific ArraySet implementation.
 * That is based around the idea of {@link java.util.List#indexOf(Object)} for no duplication.
 * Unless a array constructor is used the ArraySet does not allow for duplication.
 * This implementation does not shrink the backing array
 */
public class DoubleArraySet extends AbstractDoubleSet implements DoubleOrderedSet
{
	/** The Backing Array */
	protected transient double[] data;
	/** The amount of elements stored in the array*/
	protected int size = 0;
	
	/**
	 * Default Constructor
	 */
	public DoubleArraySet() {
		data = DoubleArrays.EMPTY_ARRAY;
	}
	
	/**
	 * Minimum Capacity Constructor
	 * @param capacity the minimum capacity of the internal array
	 * @throws NegativeArraySizeException if the capacity is negative
	 */
	public DoubleArraySet(int capacity) {
		data = new double[capacity];
	}
	
	/**
	 * Constructur using initial Array
	 * @param array the array that should be used for set.
	 */
	public DoubleArraySet(double[] array) {
		this(array, array.length);
	}
	
	/**
	 * Constructur using initial Array
	 * @param array the array that should be used for set.
	 * @param length the amount of elements present within the array
	 * @throws NegativeArraySizeException if the length is negative
	 */
	public DoubleArraySet(double[] array, int length) {
		this(length);
		addAll(array, length);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param c the elements that should be added to the set.
	 * @note this slowly checks every element to remove duplicates
	 */
	@Deprecated
	public DoubleArraySet(Collection<? extends Double> c) {
		this(c.size());
		addAll(c);
	}
	
	/**
	 * A Helper constructor that allows to create a Set with exactly the same values as the provided collection.
	 * @param c the elements that should be added to the set.
	 * @note this slowly checks every element to remove duplicates
	 */
	public DoubleArraySet(DoubleCollection c) {
		this(c.size());
		addAll(c);
	}
	
	/**
	 * A Helper constructor that fast copies the element out of a set into the ArraySet.
	 * Since it is assumed that there is no duplication in the first place
	 * @param s the set the element should be taken from
	 */
	@Deprecated
	public DoubleArraySet(Set<? extends Double> s) {
		this(s.size());
		for(Double e : s)
			data[size++] = e.doubleValue();
	}
	
	/**
	 * A Helper constructor that fast copies the element out of a set into the ArraySet.
	 * Since it is assumed that there is no duplication in the first place
	 * @param s the set the element should be taken from
	 */
	public DoubleArraySet(DoubleSet s) {
		this(s.size());
		for(DoubleIterator iter = s.iterator();iter.hasNext();data[size++] = iter.nextDouble());
	}
	
	@Override
	public boolean add(double o) {
		int index = findIndex(o);
		if(index == -1) {
			if(data.length == size) data = Arrays.copyOf(data, size == 0 ? 2 : size * 2);
			data[size++] = o;
			return true;
		}
		return false;
	}
	
	@Override
	public boolean addAndMoveToFirst(double o) {
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
	public boolean addAndMoveToLast(double o) {
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
	public boolean moveToFirst(double o) {
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
	public boolean moveToLast(double o) {
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
	public boolean contains(double e) {
		return findIndex(e) != -1;
	}
	
	@Override
	public double firstDouble() {
		if(size == 0) throw new NoSuchElementException();
		return data[0];
	}
	
	@Override
	public double lastDouble() {
		if(size == 0) throw new NoSuchElementException();
		return data[size - 1];
	}
	
	@Override
	public boolean removeAll(DoubleCollection c) {
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
	public boolean removeAll(DoubleCollection c, DoubleConsumer r) {
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
	public boolean retainAll(DoubleCollection c) {
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
	public boolean retainAll(DoubleCollection c, DoubleConsumer r) {
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
			if(!c.contains(Double.valueOf(data[i])))
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
			if(c.contains(Double.valueOf(data[i])))
				data[j++] = data[i];
		}
		boolean result = j != size;
		size = j;
		return result;
	}
	
	@Override
	public boolean remove(double o) {
		int index = findIndex(o);
		if(index != -1) {
			size--;
			if(index != size) System.arraycopy(data, index+1, data, index, size - index);
			return true;
		}
		return false;
	}
	
	@Override
	public double pollFirstDouble() {
		if(size == 0) throw new NoSuchElementException();
		double result = data[0];
		System.arraycopy(data, 1, data, 0, --size);
		return result;
	}
	
	@Override
	public double pollLastDouble() {
		if(size == 0) throw new NoSuchElementException();
		size--;
		return data[size];
	}
	@Override
	public boolean remIf(DoublePredicate filter) {
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
	public void forEach(DoubleConsumer action) {
		Objects.requireNonNull(action);
		for(int i = 0;i<size;action.accept(data[i++]));
	}
	
	@Override
	public <E> void forEach(E input, ObjectDoubleConsumer<E> action) {
		Objects.requireNonNull(action);
		for(int i = 0;i<size;i++)
			action.accept(input, data[i]);		
	}
	
	@Override
	public boolean matchesAny(Double2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(filter.get(data[i])) return true;
		}
		return false;
	}
	
	@Override
	public boolean matchesNone(Double2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(filter.get(data[i])) return false;
		}
		return true;
	}
	
	@Override
	public boolean matchesAll(Double2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(!filter.get(data[i])) return false;
		}
		return true;
	}
	
	@Override
	public double findFirst(Double2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(filter.get(data[i])) return data[i];
		}
		return 0D;
	}
	
	@Override
	public double reduce(double identity, DoubleDoubleUnaryOperator operator) {
		Objects.requireNonNull(operator);
		double state = identity;
		for(int i = 0;i<size;i++) {
			state = operator.applyAsDouble(state, data[i]);
		}
		return state;
	}
	
	@Override
	public double reduce(DoubleDoubleUnaryOperator operator) {
		Objects.requireNonNull(operator);
		double state = 0D;
		boolean empty = true;
		for(int i = 0;i<size;i++) {
			if(empty) {
				empty = false;
				state = data[i];
				continue;
			}
			state = operator.applyAsDouble(state, data[i]);
		}
		return state;
	}
	
	@Override
	public int count(Double2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		int result = 0;
		for(int i = 0;i<size;i++) {
			if(filter.get(data[i])) result++;
		}
		return result;
	}
	
	protected int findIndex(double o) {
		for(int i = size-1;i>=0;i--)
			if(Double.doubleToLongBits(data[i]) == Double.doubleToLongBits(o)) return i;
		return -1;
	}
	
	protected int findIndex(Object o) {
		for(int i = size-1;i>=0;i--)
			if(Objects.equals(o, Double.valueOf(data[i]))) return i;
		return -1;
	}
	
	@Override
	public DoubleBidirectionalIterator iterator() {
		return new SetIterator(0);
	}
	
	@Override
	public DoubleBidirectionalIterator iterator(double fromElement) {
		int index = findIndex(fromElement);
		if(index != -1) return new SetIterator(index);
		throw new NoSuchElementException();
	}
	
	public DoubleArraySet copy() {
		DoubleArraySet set = new DoubleArraySet();
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
	public double[] toDoubleArray(double[] a) {
		if(a == null || a.length < size()) return Arrays.copyOf(data, size());
		System.arraycopy(data, 0, a, 0, size());
		if (a.length > size) a[size] = 0D;
		return a;
	}
	
	@Override
	@Deprecated
	public Object[] toArray() {
		Object[] obj = new Object[size()];
		for(int i = 0;i<size();i++)
			obj[i] = Double.valueOf(data[i]);
		return obj;
	}
	
	@Override
	@Deprecated
	public <E> E[] toArray(E[] a) {
		if(a == null) a = (E[])new Object[size()];
		else if(a.length < size()) a = (E[])ObjectArrays.newArray(a.getClass().getComponentType(), size());
		for(int i = 0;i<size();i++)
			a[i] = (E)Double.valueOf(data[i]);
		if (a.length > size) a[size] = null;
		return a;
	}
	
//	Disabled until a Proper Implementation can be thought out or it is decided that the interface is thrown out.
//	private class SubSet extends AbstractDoubleSet implements DoubleSortedSet {
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
//		public boolean add(double o) {
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
//		public boolean addAndMoveToFirst(double o) {
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
//		public boolean addAndMoveToLast(double o) {
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
//		public boolean moveToFirst(double o) {
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
//		public boolean moveToLast(double o) {
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
//		public boolean contains(double e) {
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
//		public double firstDouble() {
//			if(length == 0) throw new NoSuchElementException();
//			return data[offset];
//		}
//		
//		@Override
//		public double lastDouble() {
//			if(length == 0) throw new NoSuchElementException();
//			return data[end()-1];
//		}
//		
//#if !TYPE_OBJECT
//		@Override
//		public boolean remove(double o) {
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
//				data[size] = 0D;
//#endif
//				return true;
//			}
//			return false;
//		}
//		
//		@Override
//		public double pollFirstDouble() {
//			if(length == 0) throw new NoSuchElementException();
//			size--;
//			length--;
//			double result = data[offset];
//			System.arraycopy(data, offset+1, data, offset, size-offset);
//#if TYPE_OBJECT
//			data[size] = 0D;
//#endif
//			return result;
//		}
//		
//		@Override
//		public double pollLastDouble() {
//			if(length == 0) throw new NoSuchElementException();
//			double result = data[offset+length];
//			size--;
//			length--;
//			System.arraycopy(data, end()+1, data, end(), size-end());
//#if TYPE_OBJECT
//			data[size] = 0D;
//#endif
//			return result;
//		}
//		
//		@Override
//		public DoubleComparator comparator() {
//			return null;
//		}
//
//		@Override
//		public DoubleBidirectionalIterator iterator() {
//			return new SetIterator(offset);
//		}
//
//		@Override
//		public DoubleBidirectionalIterator iterator(double fromElement) {
//			int index = findIndex(fromElement);
//			if(index != -1) return new SetIterator(index);
//			throw new NoSuchElementException();
//		}
//		
//		@Override
//		public SubSet copy() { throw new UnsupportedOperationException(); }
//		
//		@Override
//		public DoubleSortedSet subSet(double fromElement, double toElement) {
//			int fromIndex = findIndex(fromElement);
//			int toIndex = findIndex(toElement);
//			if(fromIndex == -1 || toIndex == -1) throw new NoSuchElementException();
//			return new SubSet(fromIndex, toIndex - fromIndex + 1);
//		}
//
//		@Override
//		public DoubleSortedSet headSet(double toElement) {
//			int toIndex = findIndex(toElement);
//			if(toIndex == -1) throw new NoSuchElementException();
//			return new SubSet(0, toIndex+1);
//		}
//
//		@Override
//		public DoubleSortedSet tailSet(double fromElement) {
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
//		public double[] toDoubleArray(double[] a) {
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
//				obj[i] = Double.valueOf(data[offset+i]);
//			return obj;
//		}
//		
//		@Override
//		@Deprecated
//		public <E> E[] toArray(E[] a) {
//			if(a == null) a = (E[])new Object[size()];
//			else if(a.length < size()) a = (E[])ObjectArrays.newArray(a.getClass().getComponentType(), size());
//			for(int i = 0;i<size();i++)
//				a[i] = (E)Double.valueOf(data[offset+i]);
//			return a;
//		}
//		
//#if !TYPE_OBJECT
//		protected int findIndex(double o) {
//			for(int i = length-1;i>=0;i--)
//				if(Double.doubleToLongBits(data[offset+i]) == Double.doubleToLongBits(o)) return i + offset;
//			return -1;
//		}
//		
//#endif
//		protected int findIndex(Object o) {
//			for(int i = length-1;i>=0;i--)
//				if(Objects.equals(o, Double.valueOf(data[offset+i]))) return i + offset;
//			return -1;
//		}
//		
//		private class SetIterator implements DoubleListIterator {
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
//			public double nextDouble() {
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
//			public double previousDouble() {
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
//			public void set(double e) { throw new UnsupportedOperationException(); }
//		
//			@Override
//			public void add(double e) { throw new UnsupportedOperationException(); }
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
	
	private class SetIterator implements DoubleListIterator {
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
		public double nextDouble() {
			if(!hasNext()) throw new NoSuchElementException();
			lastReturned = index;
			return data[index++];
		}
		
		@Override
		public boolean hasPrevious() {
			return index > 0;
		}
		
		@Override
		public double previousDouble() {
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
			DoubleArraySet.this.remove(data[lastReturned]);
			if(lastReturned < index) index--;
			lastReturned = -1;
		}
		
		@Override
		public void set(double e) { throw new UnsupportedOperationException(); }

		@Override
		public void add(double e) { throw new UnsupportedOperationException(); }
		
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