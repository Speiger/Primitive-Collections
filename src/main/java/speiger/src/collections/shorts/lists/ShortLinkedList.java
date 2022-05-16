package speiger.src.collections.shorts.lists;

import java.nio.ShortBuffer;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterator.OfInt;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;
import speiger.src.collections.objects.functions.consumer.ObjectShortConsumer;
import speiger.src.collections.shorts.functions.function.Short2BooleanFunction;
import speiger.src.collections.shorts.functions.function.ShortShortUnaryOperator;
import speiger.src.collections.shorts.collections.ShortCollection;
import speiger.src.collections.shorts.collections.ShortStack;
import speiger.src.collections.shorts.collections.ShortIterator;
import speiger.src.collections.shorts.queues.ShortPriorityDequeue;
import speiger.src.collections.shorts.functions.ShortComparator;
import speiger.src.collections.shorts.functions.ShortConsumer;
import speiger.src.collections.shorts.utils.ShortArrays;
import speiger.src.collections.objects.utils.ObjectArrays;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;
import speiger.src.collections.shorts.collections.ShortSplititerator;
import speiger.src.collections.shorts.utils.ShortSplititerators;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Type-Specific LinkedList implementation of list that is written to reduce (un)boxing
 * 
 * <p>This implementation is optimized to improve how data is processed with interfaces like {@link ShortStack} 
 * and with optimized functions that use type-specific implementations for primitives and optimized logic for bulk actions.
 * 
 */
public class ShortLinkedList extends AbstractShortList implements ShortPriorityDequeue, ShortStack
{
	Entry first;
	Entry last;
	int size = 0;
	
	/**
	 * Creates a new LinkedList.
	 */
	public ShortLinkedList() {
	}
	
	/**
	 * Creates a new LinkedList a copy with the contents of the Collection.
	 * @param c the elements that should be added into the list
	 */
	@Deprecated
	public ShortLinkedList(Collection<? extends Short> c) {
		addAll(c);
	}
	
	/**
	 * Creates a new LinkedList a copy with the contents of the Collection.
	 * @param c the elements that should be added into the list
	 */
	public ShortLinkedList(ShortCollection c) {
		addAll(c);
	}
	
	/**
	 * Creates a new LinkedList a copy with the contents of the List.
	 * @param l the elements that should be added into the list
	 */
	public ShortLinkedList(ShortList l) {
		addAll(l);
	}
	
	/**
	 * Creates a new LinkedList with a Copy of the array
	 * @param a the array that should be copied
	 */
	public ShortLinkedList(short... a) {
		for(int i = 0,m=a.length;i<m;add(a[i++]));
	}
	
	/**
	 * Creates a new LinkedList with a Copy of the array with a custom length
	 * @param a the array that should be copied
	 * @param length the desired length that should be copied
	 */
	public ShortLinkedList(short[] a, int length) {
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
	public ShortLinkedList(short[] a, int offset, int length) {
		SanityChecks.checkArrayCapacity(a.length, offset, length);
		for(int i = offset,m=offset+length;i<m;add(a[i++]));
	}
	
	@Override
	public boolean add(short e) {
		add(size(), e);
		return true;
	}
	
	@Override
	public void add(int index, short e) {
		checkAddRange(index);
		if(index == size) linkLast(e);
		else if(index == 0) linkFirst(e);
		else linkBefore(e, getNode(index));
	}
	
	@Override
	public boolean addAll(int index, ShortCollection c) {
		int length = c.size();
		if(length == 0) return false;
		checkAddRange(index);
		Entry next = null;
		Entry prev = null;
		if(index == size) prev = last;
		else if(index == 0) next = first;
		else {
			next = getNode(index);
			prev = next.prev;
		}
		for(ShortIterator iter = c.iterator();iter.hasNext();) {
			Entry entry = new Entry(iter.nextShort(), prev, null);
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
	public boolean addAll(int index, ShortList c) {
		int length = c.size();
		if(length == 0) return false;
		checkAddRange(index);
		Entry next = null;
		Entry prev = null;
		if(index == size) prev = last;
		else if(index == 0) next = first;
		else {
			next = getNode(index);
			prev = next.prev;
		}
		for(ShortIterator iter = c.iterator();iter.hasNext();) {
			Entry entry = new Entry(iter.nextShort(), prev, null);
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
	@Deprecated
	public boolean addAll(int index, Collection<? extends Short> c) {
		if(c instanceof ShortCollection) return addAll(index, (ShortCollection)c);
		int length = c.size();
		if(length == 0) return false;
		checkAddRange(index);
		Entry next = null;
		Entry prev = null;
		if(index == size) prev = last;
		else if(index == 0) next = first;
		else {
			next = getNode(index);
			prev = next.prev;
		}
		for(Iterator<? extends Short> iter = c.iterator();iter.hasNext();) {
			Entry entry = new Entry(iter.next().shortValue(), prev, null);
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
	public void enqueue(short e) {
		add(e);
	}
	
	@Override
	public void enqueueFirst(short e) {
		add(0, e);
	}
	
	@Override
	public void push(short e) {
		add(e);
	}
	
	@Override
	public boolean addAll(short[] e, int offset, int length) {
		if(length <= 0) return false;
		SanityChecks.checkArrayCapacity(e.length, offset, length);
		for(int i = 0;i<length;i++) linkLast(e[offset+i]);
		return true;
	}
	
	@Override
	public void addElements(int from, short[] a, int offset, int length) {
		SanityChecks.checkArrayCapacity(a.length, offset, length);
		checkAddRange(from);
		Entry next = null;
		Entry prev = null;
		if(from == size) prev = last;
		else if(from == 0) next = first;
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
	public short[] getElements(int from, short[] a, int offset, int length) {
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
	public short first() {
		if(first == null) throw new IllegalStateException();
		return first.value;
	}
	
	@Override
	public short last() {
		if(last == null) throw new IllegalStateException();
		return last.value;
	}
	
	@Override
	public short peek(int index) {
		return getShort((size() - 1) - index);
	}
	
	@Override
	public short getShort(int index) {
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
			if(Objects.equals(Short.valueOf(entry.value), o)) return i;
		}
		return -1;
	}

	@Override
	@Deprecated
	public int lastIndexOf(Object o) {
		Entry entry = last;
		for(int i = size-1;entry != null;entry = entry.prev,i--) {
			if(Objects.equals(Short.valueOf(entry.value), o)) return i;
		}
		return -1;
	}
	
	@Override
	public boolean contains(short e) {
		return indexOf(e) != -1;
	}
	
	@Override
	public int indexOf(short e) {
		Entry entry = first;
		for(int i = 0;entry != null;entry = entry.next,i++) {
			if(entry.value == e) return i;
		}
		return -1;
	}

	@Override
	public int lastIndexOf(short e) {
		Entry entry = last;
		for(int i = size-1;entry != null;entry = entry.prev,i--) {
			if(entry.value == e) return i;
		}
		return -1;
	}
	
	@Override
	public ShortListIterator listIterator(int index) {
		if(index < 0 || index > size()) throw new IndexOutOfBoundsException();
		if(index == size) return new ListIter(null, index);
		if(index == 0) return new ListIter(first, index);
		return new ListIter(getNode(index), index);
	}
	
	/**
	 * Returns a Java-Type-Specific Stream to reduce boxing/unboxing.
	 * @return a Stream of the closest java type
	 */
	public IntStream primitiveStream() { return StreamSupport.intStream(new SplitIterator(this, first, 0), false); }
	
	/**
	 * Returns a Java-Type-Specific Parallel Stream to reduce boxing/unboxing.
	 * @return a Stream of the closest java type
	 */
	public IntStream parallelPrimitiveStream() { return StreamSupport.intStream(new SplitIterator(this, first, 0), true); }
	/**
	 * A Type Specific Type Splititerator to reduce boxing/unboxing
	 * @return type specific splititerator
	 */
	@Override
	public ShortSplititerator spliterator() { return new TypeSplitIterator(this, first, 0); }
	
	@Override
	public void forEach(ShortConsumer action) {
		Objects.requireNonNull(action);
		for(Entry entry = first;entry != null;entry = entry.next) {
			action.accept(entry.value);
		}
	}
	
	@Override
	public <E> void forEach(E input, ObjectShortConsumer<E> action) {
		Objects.requireNonNull(action);
		for(Entry entry = first;entry != null;entry = entry.next)
			action.accept(input, entry.value);		
	}
	
	@Override
	public boolean matchesAny(Short2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(Entry entry = first;entry != null;entry = entry.next) {
			if(filter.get(entry.value)) return true;
		}
		return false;
	}
	
	@Override
	public boolean matchesNone(Short2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(Entry entry = first;entry != null;entry = entry.next) {
			if(filter.get(entry.value)) return false;
		}
		return true;
	}
	
	@Override
	public boolean matchesAll(Short2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(Entry entry = first;entry != null;entry = entry.next) {
			if(!filter.get(entry.value)) return false;
		}
		return true;
	}
	
	@Override
	public short findFirst(Short2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(Entry entry = first;entry != null;entry = entry.next) {
			if(filter.get(entry.value)) return entry.value;
		}
		return (short)0;
	}
	
	@Override
	public short reduce(short identity, ShortShortUnaryOperator operator) {
		Objects.requireNonNull(operator);
		short state = identity;
		for(Entry entry = first;entry != null;entry = entry.next) {
			state = operator.applyAsShort(state, entry.value);
		}
		return state;
	}
	
	@Override
	public short reduce(ShortShortUnaryOperator operator) {
		Objects.requireNonNull(operator);
		short state = (short)0;
		boolean empty = true;
		for(Entry entry = first;entry != null;entry = entry.next) {
			if(empty) {
				empty = false;
				state = entry.value;
				continue;
			}
			state = operator.applyAsShort(state, entry.value);
		}
		return state;
	}
	
	@Override
	public int count(Short2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		int result = 0;
		for(Entry entry = first;entry != null;entry = entry.next) {
			if(filter.get(entry.value)) result++;
		}
		return result;
	}
	
	@Override
	public short set(int index, short e) {
		checkRange(index);
		Entry node = getNode(index);
		short prev = node.value;
		node.value = e;
		return prev;
	}
	
	@Override
	@Deprecated
	public void replaceAll(UnaryOperator<Short> o) {
		Objects.requireNonNull(o);
		for(Entry entry = first;entry != null;entry = entry.next) {
			entry.value = o.apply(Short.valueOf(entry.value)).shortValue();
		}
	}
	
	@Override
	public void replaceShorts(IntUnaryOperator o) {
		Objects.requireNonNull(o);
		for(Entry entry = first;entry != null;entry = entry.next) {
			entry.value = SanityChecks.castToShort(o.applyAsInt(entry.value));
		}
	}
	
	@Override
	public void onChanged() {}
	@Override
	public ShortComparator comparator() { return null; }
	
	@Override
	public short dequeue() {
		if(first == null) throw new IllegalStateException();
		return unlinkFirst(first);
	}
	
	@Override
	public short dequeueLast() {
		if(last == null) throw new IllegalStateException();
		return unlinkLast(last);
	}
	
	@Override
	public short pop() {
		return dequeueLast();
	}
	
	@Override
	public boolean removeFirst(short e) {
		if(size == 0) return false;
		for(Entry entry = first;entry != null;entry = entry.next) {
			if(entry.value == e) {
				unlink(entry);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean removeLast(short e) {
		if(size == 0) return false;
		for(Entry entry = last;entry != null;entry = entry.prev) {
			if(entry.value == e) {
				unlink(entry);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public short swapRemove(int index) {
		checkRange(index);
		Entry entry = getNode(index);
		if(entry == null) return (short)0;
		if(entry.next == null) return unlinkLast(entry);
		Entry before = entry.prev;
		short result = unlink(entry);
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
		Entry temp = last;
		last = temp.prev;
		last.next = null;
		temp.next = before.next;
		temp.prev = before;
		before.next = temp;
		return result;
	}
	
	@Override
	public boolean swapRemoveShort(short e) {
		if(size == 0) return false;
		for(Entry entry = last;entry != null;entry = entry.prev) {
			if(entry.value == e) {
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
				Entry temp = last;
				last = temp.prev;
				last.next = null;
				temp.next = before.next;
				temp.prev = before;
				before.next = temp;
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean remShort(short e) {
		return removeFirst(e);
	}
	
	@Override
	public short removeShort(int index) {
		checkRange(index);
		Entry entry = getNode(index);
		return entry == null ? (short)0 : unlink(entry);
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
				entry = entry.next;
				unlink(entry.prev);
				length--;
			}
			return;
		}
		Entry entry = getNode(to);
		while(length > 0) {
			entry = entry.prev;
			unlink(entry.next);
			length--;
		}
	}
	
	@Override
	public short[] extractElements(int from, int to) {
		checkRange(from);
		checkAddRange(to);
		int length = to - from;
		if(length <= 0) return ShortArrays.EMPTY_ARRAY;
		short[] d = new short[length];
		if(from < size - to) {
			Entry entry = getNode(from);
			for(int i = 0;length > 0;i++, length--) {
				entry = entry.next;
				d[i] = unlink(entry.prev);
			}
			return d;
		}
		Entry entry = getNode(to);
		for(int i = length-1;length > 0;i--) {
			entry = entry.prev;
			d[i] = unlink(entry.next);
		}
		return d;
	}
	
	@Override
	public void fillBuffer(ShortBuffer buffer) {
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
			if(c.contains(Short.valueOf(entry.value))) {
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
			if(!c.contains(Short.valueOf(entry.value))) {
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
	public boolean removeAll(ShortCollection c) {
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
	public boolean removeAll(ShortCollection c, ShortConsumer r) {
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
	public boolean retainAll(ShortCollection c) {
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
	public boolean retainAll(ShortCollection c, ShortConsumer r) {
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
	public boolean removeIf(Predicate<? super Short> filter) {
		Objects.requireNonNull(filter);
		boolean modified = false;
		int j = 0;
		for(Entry entry = first;entry != null;) {
			if(filter.test(Short.valueOf(entry.value))) {
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
	public boolean remIf(IntPredicate filter) {
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
		Object[] obj = new Object[size];
		int i = 0;
		for(Entry entry = first;entry != null;entry = entry.next) {
			obj[i++] = Short.valueOf(entry.value);
		}
		return obj;
	}
	
	@Override
	public <E> E[] toArray(E[] a) {
		if(a == null) a = (E[])new Object[size];
		else if(a.length < size) a = (E[])ObjectArrays.newArray(a.getClass().getComponentType(), size);
		int i = 0;
		for(Entry entry = first;entry != null;entry = entry.next) {
			a[i++] = (E)Short.valueOf(entry.value);
		}
		if (a.length > size) a[size] = null;
		return a;
	}
	
	@Override
	public short[] toShortArray(short[] a) {
		if(a.length < size) a = new short[size];
		int i = 0;
		for(Entry entry = first;entry != null;entry = entry.next) {
			a[i++] = entry.value;
		}
		if (a.length > size) a[size] = (short)0;
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
	public ShortLinkedList copy() {
		ShortLinkedList list = new ShortLinkedList();
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
	
	protected void linkFirst(short e) {
		Entry f = first;
		Entry newNode = new Entry(e, null, f);
		first = newNode;
		if (f == null) last = newNode;
		else f.prev = newNode;
		size++;
	}
	
	protected void linkLast(short e) {
		Entry l = last;
		Entry newNode = new Entry(e, l, null);
		last = newNode;
		if (l == null) first = newNode;
		else l.next = newNode;
		size++;
	}
	
	protected void linkBefore(short e, Entry succ) {
		Entry prev = succ.prev;
		Entry newNode = new Entry(e, prev, succ);
		succ.prev = newNode;
		if (prev == null) first = newNode;
		else prev.next = newNode;
		size++;
	}
	
	protected short unlinkFirst(Entry f) {
		short element = f.value;
		Entry next = f.next;
		f.next = null;
		first = next;
		if (next == null) last = null;
		else next.prev = null;
		size--;
		return element;
	}
	
	protected short unlinkLast(Entry l) {
		short element = l.value;
		Entry prev = l.prev;
		l.prev = null;
		last = prev;
		if (prev == null) first = null;
		else prev.next = null;
		size--;
		return element;
	}
	
	protected short unlink(Entry x) {
		short element = x.value;
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
		short value;
		Entry prev;
		Entry next;
		
		public Entry(short value, Entry prev, Entry next)
		{
			this.value = value;
			this.prev = prev;
			this.next = next;
		}
	}
	
	private class ListIter implements ShortListIterator 
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
		public short previousShort() {
			if(!hasPrevious()) throw new NoSuchElementException();
            lastReturned = next = (next == null) ? last : next.prev;
			index--;
			return lastReturned.value;
		}
		
		@Override
		public short nextShort() {
			if(!hasNext()) throw new NoSuchElementException();
			lastReturned = next;
			next = next.next;
			index++;
			return lastReturned.value;
		}
		
		@Override
		public void set(short e) {
			if(lastReturned == null) throw new IllegalStateException();
			lastReturned.value = e;
		}
		
		@Override
		public void add(short e) {
            lastReturned = null;
            if (next == null) linkLast(e);
            else linkBefore(e, next);
            index++;
		}
	}
	
	private static class TypeSplitIterator implements ShortSplititerator 
	{
		static final int BATCH_UNIT = 1 << 10;
		static final int MAX_BATCH = 1 << 25;
		ShortLinkedList list;
		Entry entry;
		int index;
		
		TypeSplitIterator(ShortLinkedList list, Entry entry, int index)
		{
			this.list = list;
			this.entry = entry;
			this.index = index;
		}
		
		@Override
		public ShortSplititerator trySplit() {
			if(entry == null && estimateSize() > 0) {
				int size = Math.min(Math.min(index + BATCH_UNIT, MAX_BATCH), list.size) - index;
				if(size <= 0) return null;
				short[] data = new short[size];
				int subSize = 0;
				for(;subSize<size && entry != null;subSize++) {
					data[subSize] = entry.value;
					entry = entry.next;
					index++;
				}
				return ShortSplititerators.createArraySplititerator(data, subSize, characteristics());
			}
			return null;
		}
		
		@Override
		public boolean tryAdvance(ShortConsumer action) {
			if(hasNext()) {
				action.accept(nextShort());
				return true;
			}
			return false;
		}
		
		@Override
		public boolean tryAdvance(Consumer<? super Short> action) {
			if(hasNext()) {
				action.accept(Short.valueOf(nextShort()));
				return true;
			}
			return false;
		}
		
		@Override
		public long estimateSize() {
			return list.size - index;
		}
		
		@Override
		public int characteristics() {
			return Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED;
		}
		
		@Override
		public short nextShort() {
			short value = entry.value;
			entry = entry.next;
			index++;
			return value;
		}
		
		@Override
		public boolean hasNext() {
			return entry != null;
		}
	}
	
	private static class SplitIterator implements OfInt
	{
		static final int BATCH_UNIT = 1 << 10;
		static final int MAX_BATCH = 1 << 25;
		ShortLinkedList list;
		Entry entry;
		int index;
		
		SplitIterator(ShortLinkedList list, Entry entry, int index)
		{
			this.list = list;
			this.entry = entry;
			this.index = index;
		}
		
		@Override
		public OfInt trySplit() {
			if(entry == null && estimateSize() > 0) {
				int size = Math.min(Math.min(index + BATCH_UNIT, MAX_BATCH), list.size) - index;
				if(size <= 0) return null;
				short[] data = new short[size];
				int subSize = 0;
				for(;subSize<size && entry != null;subSize++) {
					data[subSize] = entry.value;
					entry = entry.next;
					index++;
				}
				return ShortSplititerators.createArrayJavaSplititerator(data, subSize, characteristics());
			}
			return null;
		}
		
		@Override
		public boolean tryAdvance(java.util.function.IntConsumer action) {
			if(hasNext()) {
				action.accept(next());
				return true;
			}
			return false;
		}
		
		@Override
		public long estimateSize() {
			return list.size - index;
		}
		
		@Override
		public int characteristics() {
			return Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED;
		}
		
		public short next() {
			short value = entry.value;
			entry = entry.next;
			index++;
			return value;
		}
		
		public boolean hasNext() {
			return entry != null;
		}
	}
}