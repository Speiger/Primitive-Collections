package speiger.src.collections.doubles.lists;

import java.nio.DoubleBuffer;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterator.OfDouble;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.DoublePredicate;
import java.util.function.UnaryOperator;
import java.util.function.DoubleUnaryOperator;
import speiger.src.collections.ints.functions.consumer.IntDoubleConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectDoubleConsumer;
import speiger.src.collections.doubles.functions.function.DoubleDoubleUnaryOperator;
import speiger.src.collections.doubles.collections.DoubleCollection;
import speiger.src.collections.doubles.collections.DoubleStack;
import speiger.src.collections.doubles.collections.DoubleIterator;
import speiger.src.collections.doubles.queues.DoublePriorityDequeue;
import speiger.src.collections.doubles.functions.DoubleComparator;
import speiger.src.collections.doubles.functions.DoubleConsumer;
import speiger.src.collections.doubles.utils.DoubleArrays;
import speiger.src.collections.objects.utils.ObjectArrays;
import java.util.stream.DoubleStream;
import java.util.stream.StreamSupport;
import speiger.src.collections.doubles.collections.DoubleSplititerator;
import speiger.src.collections.doubles.utils.DoubleSplititerators;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Type-Specific LinkedList implementation of list that is written to reduce (un)boxing
 * 
 * <p>This implementation is optimized to improve how data is processed with interfaces like {@link DoubleStack} 
 * and with optimized functions that use type-specific implementations for primitives and optimized logic for bulk actions.
 * 
 */
public class DoubleLinkedList extends AbstractDoubleList implements DoublePriorityDequeue, DoubleStack
{
	Entry first;
	Entry last;
	int size = 0;
	
	/**
	 * Creates a new LinkedList.
	 */
	public DoubleLinkedList() {
	}
	
	/**
	 * Creates a new LinkedList a copy with the contents of the Collection.
	 * @param c the elements that should be added into the list
	 */
	@Deprecated
	public DoubleLinkedList(Collection<? extends Double> c) {
		addAll(c);
	}
	
	/**
	 * Creates a new LinkedList a copy with the contents of the Collection.
	 * @param c the elements that should be added into the list
	 */
	public DoubleLinkedList(DoubleCollection c) {
		addAll(c);
	}
	
	/**
	 * Creates a new LinkedList a copy with the contents of the List.
	 * @param l the elements that should be added into the list
	 */
	public DoubleLinkedList(DoubleList l) {
		addAll(l);
	}
	
	/**
	 * Creates a new LinkedList with a Copy of the array
	 * @param a the array that should be copied
	 */
	public DoubleLinkedList(double... a) {
		for(int i = 0,m=a.length;i<m;add(a[i++]));
	}
	
	/**
	 * Creates a new LinkedList with a Copy of the array with a custom length
	 * @param a the array that should be copied
	 * @param length the desired length that should be copied
	 */
	public DoubleLinkedList(double[] a, int length) {
		for(int i = 0,m=length;i<m;add(a[i++]));
	}
	
	/**
	 * Creates a new LinkedList with a Copy of the array with in the custom range.
	 * @param a the array that should be copied
	 * @param offset the starting offset of where the array should be copied from
	 * @param length the desired length that should be copied
	 * @throws IllegalStateException if offset is smaller then 0
	 * @throws IllegalStateException if the offset + length exceeds the array length
	 */
	public DoubleLinkedList(double[] a, int offset, int length) {
		SanityChecks.checkArrayCapacity(a.length, offset, length);
		for(int i = offset,m=offset+length;i<m;add(a[i++]));
	}
	
	@Override
	public boolean add(double e) {
		add(size(), e);
		return true;
	}
	
	@Override
	public void add(int index, double e) {
		checkAddRange(index);
		if(index == size) linkLast(e);
		else if(index == 0) linkFirst(e);
		else linkBefore(e, getNode(index));
	}
	
	@Override
	public boolean addAll(int index, DoubleCollection c) {
		int length = c.size();
		if(length == 0) return false;
		checkAddRange(index);
		Entry next;
		Entry prev;
		if(index == size) {
			prev = last;
			next = null;
		}
		else if(index == 0) {
			next = first;
			prev = null;
		}
		else {
			next = getNode(index);
			prev = next.prev;
		}
		for(DoubleIterator iter = c.iterator();iter.hasNext();) {
			Entry entry = new Entry(iter.nextDouble(), prev, null);
			if(prev == null) first = entry;
			else prev.next = entry;
			prev = entry;
		}
		if(next == null) last = prev;
		else {
			prev.next = next;
			next.prev = prev;
		}
		size += length;
		return true;
	}
	
	@Override
	public boolean addAll(int index, DoubleList c) {
		return addAll(index, (DoubleCollection)c); //
	}
	
	@Override
	@Deprecated
	public boolean addAll(int index, Collection<? extends Double> c) {
		if(c instanceof DoubleCollection) return addAll(index, (DoubleCollection)c);
		int length = c.size();
		if(length == 0) return false;
		checkAddRange(index);
		Entry next;
		Entry prev;
		if(index == size) {
			prev = last;
			next = null;
		}
		else if(index == 0) {
			next = first;
			prev = null;
		}
		else {
			next = getNode(index);
			prev = next.prev;
		}
		for(Iterator<? extends Double> iter = c.iterator();iter.hasNext();) {
			Entry entry = new Entry(iter.next().doubleValue(), prev, null);
			if(prev == null) first = entry;
			else prev.next = entry;
			prev = entry;
		}
		if(next == null) last = prev;
		else {
			prev.next = next;
			next.prev = prev;
		}
		size += length;
		return true;
	}
	
	@Override
	public void enqueue(double e) {
		add(e);
	}
	
	@Override
	public void enqueueFirst(double e) {
		add(0, e);
	}
	
	@Override
	public void push(double e) {
		add(e);
	}
	
	@Override
	public boolean addAll(double[] e, int offset, int length) {
		if(length <= 0) return false;
		SanityChecks.checkArrayCapacity(e.length, offset, length);
		for(int i = 0;i<length;i++) linkLast(e[offset+i]);
		return true;
	}
	
	@Override
	public void addElements(int from, double[] a, int offset, int length) {
		if(length <= 0) return;
		SanityChecks.checkArrayCapacity(a.length, offset, length);
		checkAddRange(from);
		Entry next;
		Entry prev;
		if(from == size) {
			prev = last;
			next = null;
		}
		else if(from == 0) {
			next = first;
			prev = null;
		}
		else {
			next = getNode(from);
			prev = next.prev;
		}
		for(int i = offset,m=offset+length;i<m;i++) {
			Entry entry = new Entry(a[i], prev, null);
			if(prev == null) first = entry;
			else prev.next = entry;
			prev = entry;
		}
		if(next == null) last = prev;
		else {
			prev.next = next;
			next.prev = prev;
		}
		size += length;
	}
	
	@Override
	public double[] getElements(int from, double[] a, int offset, int length) {
		SanityChecks.checkArrayCapacity(size, from, length);
		SanityChecks.checkArrayCapacity(a.length, offset, length);
		Entry entry = getNode(from);
		while(length > 0) {
			a[offset++] = entry.value;
			length--;
			entry = entry.next;
		}
		return a;
	}
	
	@Override
	public double first() {
		return getFirstDouble();
	}
	
	@Override
	public double last() {
		return getLastDouble();
	}
	
	@Override
	public double getFirstDouble() {
		if(first == null) throw new NoSuchElementException();
		return first.value;
	}
	
	@Override
	public double getLastDouble() {
		if(last == null) throw new NoSuchElementException();
		return last.value;
	}
	
	@Override
	public double removeFirstDouble() {
		if(first == null) throw new NoSuchElementException();
		return unlinkFirst(first);
	}
	
	@Override
	public double removeLastDouble() {
		return pop();
	}
	
	@Override
	public double peek(int index) {
		return getDouble((size() - 1) - index);
	}
	
	@Override
	public double getDouble(int index) {
		checkRange(index);
		return getNode(index).value;
	}
	
	@Override
	@Deprecated
	public boolean contains(Object e) {
		return indexOf(e) != -1;
	}
	
	@Override
	@Deprecated
	public int indexOf(Object o) {
		Entry entry = first;
		for(int i = 0;entry != null;entry = entry.next,i++) {
			if(Objects.equals(Double.valueOf(entry.value), o)) return i;
		}
		return -1;
	}

	@Override
	@Deprecated
	public int lastIndexOf(Object o) {
		Entry entry = last;
		for(int i = size-1;entry != null;entry = entry.prev,i--) {
			if(Objects.equals(Double.valueOf(entry.value), o)) return i;
		}
		return -1;
	}
	
	@Override
	public boolean contains(double e) {
		return indexOf(e) != -1;
	}
	
	@Override
	public int indexOf(double e) {
		Entry entry = first;
		for(int i = 0;entry != null;entry = entry.next,i++) {
			if(Double.doubleToLongBits(entry.value) == Double.doubleToLongBits(e)) return i;
		}
		return -1;
	}

	@Override
	public int lastIndexOf(double e) {
		Entry entry = last;
		for(int i = size-1;entry != null;entry = entry.prev,i--) {
			if(Double.doubleToLongBits(entry.value) == Double.doubleToLongBits(e)) return i;
		}
		return -1;
	}
	
	@Override
	public DoubleListIterator listIterator(int index) {
		if(index < 0 || index > size()) throw new IndexOutOfBoundsException();
		if(index == size) return new ListIter(null, index);
		if(index == 0) return new ListIter(first, index);
		return new ListIter(getNode(index), index);
	}
	
	/**
	 * Returns a Java-Type-Specific Stream to reduce boxing/unboxing.
	 * @return a Stream of the closest java type
	 */
	public DoubleStream primitiveStream() { return StreamSupport.doubleStream(new SplitIterator(this, first, 0), false); }
	
	/**
	 * Returns a Java-Type-Specific Parallel Stream to reduce boxing/unboxing.
	 * @return a Stream of the closest java type
	 */
	public DoubleStream parallelPrimitiveStream() { return StreamSupport.doubleStream(new SplitIterator(this, first, 0), true); }
	/**
	 * A Type Specific Type Splititerator to reduce boxing/unboxing
	 * @return type specific splititerator
	 */
	@Override
	public DoubleSplititerator spliterator() { return new TypeSplitIterator(this, first, 0); }
	
	@Override
	public void forEach(DoubleConsumer action) {
		Objects.requireNonNull(action);
		for(Entry entry = first;entry != null;entry = entry.next) {
			action.accept(entry.value);
		}
	}
	
	@Override
	public void forEachIndexed(IntDoubleConsumer action) {
		Objects.requireNonNull(action);
		int index = 0;
		for(Entry entry = first;entry != null;entry = entry.next)
			action.accept(index++, entry.value);
	}
	
	@Override
	public <E> void forEach(E input, ObjectDoubleConsumer<E> action) {
		Objects.requireNonNull(action);
		for(Entry entry = first;entry != null;entry = entry.next)
			action.accept(input, entry.value);		
	}
	
	@Override
	public boolean matchesAny(DoublePredicate filter) {
		Objects.requireNonNull(filter);
		for(Entry entry = first;entry != null;entry = entry.next) {
			if(filter.test(entry.value)) return true;
		}
		return false;
	}
	
	@Override
	public boolean matchesNone(DoublePredicate filter) {
		Objects.requireNonNull(filter);
		for(Entry entry = first;entry != null;entry = entry.next) {
			if(filter.test(entry.value)) return false;
		}
		return true;
	}
	
	@Override
	public boolean matchesAll(DoublePredicate filter) {
		Objects.requireNonNull(filter);
		for(Entry entry = first;entry != null;entry = entry.next) {
			if(!filter.test(entry.value)) return false;
		}
		return true;
	}
	
	@Override
	public double findFirst(DoublePredicate filter) {
		Objects.requireNonNull(filter);
		for(Entry entry = first;entry != null;entry = entry.next) {
			if(filter.test(entry.value)) return entry.value;
		}
		return 0D;
	}
	
	@Override
	public double reduce(double identity, DoubleDoubleUnaryOperator operator) {
		Objects.requireNonNull(operator);
		double state = identity;
		for(Entry entry = first;entry != null;entry = entry.next) {
			state = operator.applyAsDouble(state, entry.value);
		}
		return state;
	}
	
	@Override
	public double reduce(DoubleDoubleUnaryOperator operator) {
		Objects.requireNonNull(operator);
		double state = 0D;
		boolean empty = true;
		for(Entry entry = first;entry != null;entry = entry.next) {
			if(empty) {
				empty = false;
				state = entry.value;
				continue;
			}
			state = operator.applyAsDouble(state, entry.value);
		}
		return state;
	}
	
	@Override
	public int count(DoublePredicate filter) {
		Objects.requireNonNull(filter);
		int result = 0;
		for(Entry entry = first;entry != null;entry = entry.next) {
			if(filter.test(entry.value)) result++;
		}
		return result;
	}
	
	@Override
	public double set(int index, double e) {
		checkRange(index);
		Entry node = getNode(index);
		double prev = node.value;
		node.value = e;
		return prev;
	}
	
	@Override
	@Deprecated
	public void replaceAll(UnaryOperator<Double> o) {
		Objects.requireNonNull(o);
		for(Entry entry = first;entry != null;entry = entry.next) {
			entry.value = o.apply(Double.valueOf(entry.value)).doubleValue();
		}
	}
	
	@Override
	public void replaceDoubles(DoubleUnaryOperator o) {
		Objects.requireNonNull(o);
		for(Entry entry = first;entry != null;entry = entry.next) {
			entry.value = o.applyAsDouble(entry.value);
		}
	}
	
	@Override
	public void onChanged() {}
	@Override
	public DoubleComparator comparator() { return null; }
	
	@Override
	public double dequeue() {
		if(first == null) throw new NoSuchElementException();
		return unlinkFirst(first);
	}
	
	@Override
	public double dequeueLast() {
		return pop();
	}
	
	@Override
	public double pop() {
		if(last == null) throw new NoSuchElementException();
		return unlinkLast(last);
	}
	
	@Override
	public boolean removeFirst(double e) {
		return remDouble(e);
	}
	
	@Override
	public boolean removeLast(double e) {
		if(size == 0) return false;
		for(Entry entry = last;entry != null;entry = entry.prev) {
			if(Double.doubleToLongBits(entry.value) == Double.doubleToLongBits(e)) {
				unlink(entry);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public double swapRemove(int index) {
		checkRange(index);
		Entry entry = getNode(index);
		if(entry == null) return 0D;
		if(entry.next == null) return unlinkLast(entry);
		Entry before = entry.prev;
		double result = unlink(entry);
		if(before == null) {
			Entry temp = last;
			last = temp.prev;
			last.next = null;
			temp.next = first;
			temp.prev = null;
			first.prev = temp;
			first = temp;
			return result;
		}
		else if(before.next != last) {
			Entry temp = last;
			last = temp.prev;
			last.next = null;
			temp.next = before.next;
			temp.prev = before;
			before.next = temp;
			temp.next.prev = temp;
		}
		return result;
	}
	
	@Override
	public boolean swapRemoveDouble(double e) {
		if(size == 0) return false;
		for(Entry entry = last;entry != null;entry = entry.prev) {
			if(Double.doubleToLongBits(entry.value) == Double.doubleToLongBits(e)) {
				if(entry.next == null) {
					unlinkLast(entry);
					return true;
				}
				Entry before = entry.prev;
				unlink(entry);
				if(before == null) {
					Entry temp = last;
					last = temp.prev;
					last.next = null;
					temp.next = first;
					temp.prev = null;
					first.prev = temp;
					first = temp;
					return true;
				}
				else if(before.next != last) {
					Entry temp = last;
					last = temp.prev;
					last.next = null;
					temp.next = before.next;
					temp.prev = before;
					before.next = temp;
					temp.next.prev = temp;
				}
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean remDouble(double e) {
		if(size == 0) return false;
		for(Entry entry = first;entry != null;entry = entry.next) {
			if(Double.doubleToLongBits(entry.value) == Double.doubleToLongBits(e)) {
				unlink(entry);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public double removeDouble(int index) {
		checkRange(index);
		Entry entry = getNode(index);
		return entry == null ? 0D : unlink(entry);
	}
	
	@Override
	public void removeElements(int from, int to) {
		checkRange(from);
		checkAddRange(to);
		int length = to - from;
		if(length <= 0) return;
		if(from < size - to) {
			Entry entry = getNode(from);
			while(length > 0) {
				Entry next = entry.next;
				unlink(entry);
				entry = next;
				length--;
			}
			return;
		}
		Entry entry = getNode(to-1);
		while(length > 0) {
			Entry prev = entry.prev;
			unlink(entry);
			entry = prev;
			length--;
		}
	}
	
	@Override
	public double[] extractElements(int from, int to) {
		checkRange(from);
		checkAddRange(to);
		int length = to - from;
		if(length <= 0) return DoubleArrays.EMPTY_ARRAY;
		double[] d = new double[length];
		if(from < size - to) {
			Entry entry = getNode(from);
			for(int i = 0;length > 0;i++, length--) {
				Entry next = entry.next;
				d[i] = unlink(entry);
				entry = next;
			}
			return d;
		}
		Entry entry = getNode(to-1);
		for(int i = length-1;length > 0;i--, length--) {
			Entry prev = entry.prev;
			d[i] = unlink(entry);
			entry = prev;
		}
		return d;
	}
	
	@Override
	public void fillBuffer(DoubleBuffer buffer) {
		for(Entry entry = first;entry != null;entry = entry.next)
			buffer.put(entry.value);
	}
	
	@Override
	@Deprecated
	public boolean removeAll(Collection<?> c) {
		if(c.isEmpty()) return false;
		boolean modified = false;
		int j = 0;
		for(Entry entry = first;entry != null;) {
			if(c.contains(Double.valueOf(entry.value))) {
				Entry next = entry.next;
				unlink(entry);
				entry = next;
				modified = true;
				continue;
			}
			else j++;
			entry = entry.next;
		}
		size = j;
		return modified;
	}
	
	@Override
	@Deprecated
	public boolean retainAll(Collection<?> c) {
		if(c.isEmpty()) {
			boolean changed = size > 0;
			clear();
			return changed;
		}
		boolean modified = false;
		int j = 0;
		for(Entry entry = first;entry != null;) {
			if(!c.contains(Double.valueOf(entry.value))) {
				Entry next = entry.next;
				unlink(entry);
				entry = next;
				modified = true;
				continue;
			}
			else j++;
			entry = entry.next;
		}
		size = j;
		return modified;
	}
	
	@Override
	public boolean removeAll(DoubleCollection c) {
		if(c.isEmpty()) return false;
		boolean modified = false;
		int j = 0;
		for(Entry entry = first;entry != null;) {
			if(c.contains(entry.value)) {
				Entry next = entry.next;
				unlink(entry);
				entry = next;
				modified = true;
				continue;
			}
			else j++;
			entry = entry.next;
		}
		size = j;
		return modified;
	}
	
	@Override
	public boolean removeAll(DoubleCollection c, DoubleConsumer r) {
		if(c.isEmpty()) return false;
		boolean modified = false;
		int j = 0;
		for(Entry entry = first;entry != null;) {
			if(c.contains(entry.value)) {
				r.accept(entry.value);
				Entry next = entry.next;
				unlink(entry);
				entry = next;
				modified = true;
				continue;
			}
			else j++;
			entry = entry.next;
		}
		size = j;
		return modified;
	}
	
	@Override
	public boolean retainAll(DoubleCollection c) {
		if(c.isEmpty()) {
			boolean changed = size > 0;
			clear();
			return changed;
		}
		boolean modified = false;
		int j = 0;
		for(Entry entry = first;entry != null;) {
			if(!c.contains(entry.value)) {
				Entry next = entry.next;
				unlink(entry);
				entry = next;
				modified = true;
				continue;
			}
			else j++;
			entry = entry.next;
		}
		size = j;
		return modified;
	}
	
	@Override
	public boolean retainAll(DoubleCollection c, DoubleConsumer r) {
		if(c.isEmpty()) {
			boolean changed = size > 0;
			forEach(r);
			clear();
			return changed;
		}
		boolean modified = false;
		int j = 0;
		for(Entry entry = first;entry != null;) {
			if(!c.contains(entry.value)) {
				r.accept(entry.value);
				Entry next = entry.next;
				unlink(entry);
				entry = next;
				modified = true;
				continue;
			}
			else j++;
			entry = entry.next;
		}
		size = j;
		return modified;
	}
	
	@Override
	@Deprecated
	public boolean removeIf(Predicate<? super Double> filter) {
		Objects.requireNonNull(filter);
		boolean modified = false;
		int j = 0;
		for(Entry entry = first;entry != null;) {
			if(filter.test(Double.valueOf(entry.value))) {
				Entry next = entry.next;
				unlink(entry);
				entry = next;
				modified = true;
				continue;
			}
			else j++;
			entry = entry.next;
		}
		size = j;
		return modified;
	}
	
	@Override
	public boolean remIf(DoublePredicate filter) {
		boolean modified = false;
		int j = 0;
		for(Entry entry = first;entry != null;) {
			if(filter.test(entry.value)) {
				Entry next = entry.next;
				unlink(entry);
				entry = next;
				modified = true;
				continue;
			}
			else j++;
			entry = entry.next;
		}
		size = j;
		return modified;
	}
	
	@Override
	public Object[] toArray() {
		if(size == 0) return ObjectArrays.EMPTY_ARRAY;
		Object[] obj = new Object[size];
		int i = 0;
		for(Entry entry = first;entry != null;entry = entry.next) {
			obj[i++] = Double.valueOf(entry.value);
		}
		return obj;
	}
	
	@Override
	public <E> E[] toArray(E[] a) {
		if(a == null) a = (E[])new Object[size];
		else if(a.length < size) a = (E[])ObjectArrays.newArray(a.getClass().getComponentType(), size);
		int i = 0;
		for(Entry entry = first;entry != null;entry = entry.next) {
			a[i++] = (E)Double.valueOf(entry.value);
		}
		if (a.length > size) a[size] = null;
		return a;
	}
	
	@Override
	public double[] toDoubleArray(double[] a) {
		if(a.length < size) a = new double[size];
		int i = 0;
		for(Entry entry = first;entry != null;entry = entry.next) {
			a[i++] = entry.value;
		}
		if (a.length > size) a[size] = 0D;
		return a;
	}
	
	@Override
	public int size() {
		return size;
	}
	
	@Override
	public void clear() {
		for(Entry entry = first;entry != null;) {
			Entry next = entry.next;
			entry.next = entry.prev = null;
			entry = next;
		}
		first = null;
		last = null;
		size = 0;
	}
	
	@Override
	public DoubleLinkedList copy() {
		DoubleLinkedList list = new DoubleLinkedList();
		list.size = size;
		if(first != null) {
			list.first = new Entry(first.value, null, null);
			Entry lastReturned = list.first;
			for(Entry entry = first.next;entry != null;entry = entry.next) {
				Entry next = new Entry(entry.value, lastReturned, null);
				lastReturned.next = next;
				lastReturned = next;
			}
			list.last = lastReturned;
		}
		return list;
	}
	
	protected Entry getNode(int index) {
		if(index < size >> 2) {
			Entry x = first;
			for (int i = 0; i < index; i++)
				x = x.next;
			return x;
		}
		Entry x = last;
		for (int i = size - 1; i > index; i--)
			x = x.prev;
		return x;
	}
	
	protected void linkFirst(double e) {
		Entry f = first;
		Entry newNode = new Entry(e, null, f);
		first = newNode;
		if (f == null) last = newNode;
		else f.prev = newNode;
		size++;
	}
	
	protected void linkLast(double e) {
		Entry l = last;
		Entry newNode = new Entry(e, l, null);
		last = newNode;
		if (l == null) first = newNode;
		else l.next = newNode;
		size++;
	}
	
	protected void linkBefore(double e, Entry succ) {
		Entry prev = succ.prev;
		Entry newNode = new Entry(e, prev, succ);
		succ.prev = newNode;
		if (prev == null) first = newNode;
		else prev.next = newNode;
		size++;
	}
	
	protected double unlinkFirst(Entry f) {
		double element = f.value;
		Entry next = f.next;
		f.next = null;
		first = next;
		if (next == null) last = null;
		else next.prev = null;
		size--;
		return element;
	}
	
	protected double unlinkLast(Entry l) {
		double element = l.value;
		Entry prev = l.prev;
		l.prev = null;
		last = prev;
		if (prev == null) first = null;
		else prev.next = null;
		size--;
		return element;
	}
	
	protected double unlink(Entry x) {
		double element = x.value;
		Entry next = x.next;
		Entry prev = x.prev;
		if (prev == null) first = next;
		else {
			prev.next = next;
			x.prev = null;
		}
		if (next == null) last = prev;
		else {
			next.prev = prev;
			x.next = null;
		}
		size--;
		return element;
	}
	
	protected void checkRange(int index) {
		if (index < 0 || index >= size)
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
	}
	
	protected void checkAddRange(int index) {
		if (index < 0 || index > size)
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
	}
	
	protected static class Entry {
		double value;
		Entry prev;
		Entry next;
		
		public Entry(double value, Entry prev, Entry next) {
			this.value = value;
			this.prev = prev;
			this.next = next;
		}
		
		@Override
		public String toString() {
			return Double.toString(value);
		}
	}
	
	private class ListIter implements DoubleListIterator 
	{
		Entry next;
		Entry lastReturned;
		int index;
		
		ListIter(Entry next, int index) {
			this.next = next;
			this.index = index;
		}
		
		@Override
		public boolean hasNext() {
			return index < size;
		}
		
		@Override
		public boolean hasPrevious() {
			return index > 0;
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
			if(lastReturned == null) throw new IllegalStateException();
			Entry lastNext = lastReturned.next;
			unlink(lastReturned);
			if (next == lastReturned) next = lastNext;
			else index--;
			lastReturned = null;
		}
		
		@Override
		public double previousDouble() {
			if(!hasPrevious()) throw new NoSuchElementException();
            lastReturned = next = (next == null) ? last : next.prev;
			index--;
			return lastReturned.value;
		}
		
		@Override
		public double nextDouble() {
			if(!hasNext()) throw new NoSuchElementException();
			lastReturned = next;
			next = next.next;
			index++;
			return lastReturned.value;
		}
		
		@Override
		public void set(double e) {
			if(lastReturned == null) throw new IllegalStateException();
			lastReturned.value = e;
		}
		
		@Override
		public void add(double e) {
            lastReturned = null;
            if (next == null) linkLast(e);
            else linkBefore(e, next);
            index++;
		}
	}
	
	private static class TypeSplitIterator implements DoubleSplititerator 
	{
		static final int BATCH_UNIT = 1 << 10;
		static final int MAX_BATCH = 1 << 25;
		DoubleLinkedList list;
		Entry entry;
		int index;
		
		TypeSplitIterator(DoubleLinkedList list, Entry entry, int index)
		{
			this.list = list;
			this.entry = entry;
			this.index = index;
		}
		
		@Override
		public DoubleSplititerator trySplit() {
			if(entry == null && estimateSize() > 0) {
				int size = Math.min(Math.min(index + BATCH_UNIT, MAX_BATCH), list.size) - index;
				if(size <= 0) return null;
				double[] data = new double[size];
				int subSize = 0;
				for(;subSize<size && entry != null;subSize++) {
					data[subSize] = entry.value;
					entry = entry.next;
					index++;
				}
				return DoubleSplititerators.createArraySplititerator(data, subSize, characteristics());
			}
			return null;
		}
		
		@Override
		public boolean tryAdvance(DoubleConsumer action) {
			if(hasNext()) {
				action.accept(nextDouble());
				return true;
			}
			return false;
		}
		
		@Override
		public boolean tryAdvance(Consumer<? super Double> action) {
			if(hasNext()) {
				action.accept(Double.valueOf(nextDouble()));
				return true;
			}
			return false;
		}
		
		@Override
		public long estimateSize() {
			return (long)list.size - (long)index;
		}
		
		@Override
		public int characteristics() {
			return Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED;
		}
		
		@Override
		public double nextDouble() {
			double value = entry.value;
			entry = entry.next;
			index++;
			return value;
		}
		
		@Override
		public boolean hasNext() {
			return entry != null;
		}
	}
	
	private static class SplitIterator implements OfDouble
	{
		static final int BATCH_UNIT = 1 << 10;
		static final int MAX_BATCH = 1 << 25;
		DoubleLinkedList list;
		Entry entry;
		int index;
		
		SplitIterator(DoubleLinkedList list, Entry entry, int index)
		{
			this.list = list;
			this.entry = entry;
			this.index = index;
		}
		
		@Override
		public OfDouble trySplit() {
			if(entry == null && estimateSize() > 0) {
				int size = Math.min(Math.min(index + BATCH_UNIT, MAX_BATCH), list.size) - index;
				if(size <= 0) return null;
				double[] data = new double[size];
				int subSize = 0;
				for(;subSize<size && entry != null;subSize++) {
					data[subSize] = entry.value;
					entry = entry.next;
					index++;
				}
				return DoubleSplititerators.createArrayJavaSplititerator(data, subSize, characteristics());
			}
			return null;
		}
		
		@Override
		public boolean tryAdvance(java.util.function.DoubleConsumer action) {
			if(hasNext()) {
				action.accept(next());
				return true;
			}
			return false;
		}
		
		@Override
		public long estimateSize() {
			return (long)list.size - (long)index;
		}
		
		@Override
		public int characteristics() {
			return Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED;
		}
		
		public double next() {
			double value = entry.value;
			entry = entry.next;
			index++;
			return value;
		}
		
		public boolean hasNext() {
			return entry != null;
		}
	}
}