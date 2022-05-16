package speiger.src.collections.longs.lists;

import java.nio.LongBuffer;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterator.OfLong;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.function.LongPredicate;
import java.util.function.LongUnaryOperator;
import speiger.src.collections.objects.functions.consumer.ObjectLongConsumer;
import speiger.src.collections.longs.functions.function.Long2BooleanFunction;
import speiger.src.collections.longs.functions.function.LongLongUnaryOperator;
import speiger.src.collections.longs.collections.LongCollection;
import speiger.src.collections.longs.collections.LongStack;
import speiger.src.collections.longs.collections.LongIterator;
import speiger.src.collections.longs.queues.LongPriorityDequeue;
import speiger.src.collections.longs.functions.LongComparator;
import speiger.src.collections.longs.functions.LongConsumer;
import speiger.src.collections.longs.utils.LongArrays;
import speiger.src.collections.objects.utils.ObjectArrays;
import java.util.stream.LongStream;
import java.util.stream.StreamSupport;
import speiger.src.collections.longs.collections.LongSplititerator;
import speiger.src.collections.longs.utils.LongSplititerators;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Type-Specific LinkedList implementation of list that is written to reduce (un)boxing
 * 
 * <p>This implementation is optimized to improve how data is processed with interfaces like {@link LongStack} 
 * and with optimized functions that use type-specific implementations for primitives and optimized logic for bulk actions.
 * 
 */
public class LongLinkedList extends AbstractLongList implements LongPriorityDequeue, LongStack
{
	Entry first;
	Entry last;
	int size = 0;
	
	/**
	 * Creates a new LinkedList.
	 */
	public LongLinkedList() {
	}
	
	/**
	 * Creates a new LinkedList a copy with the contents of the Collection.
	 * @param c the elements that should be added into the list
	 */
	@Deprecated
	public LongLinkedList(Collection<? extends Long> c) {
		addAll(c);
	}
	
	/**
	 * Creates a new LinkedList a copy with the contents of the Collection.
	 * @param c the elements that should be added into the list
	 */
	public LongLinkedList(LongCollection c) {
		addAll(c);
	}
	
	/**
	 * Creates a new LinkedList a copy with the contents of the List.
	 * @param l the elements that should be added into the list
	 */
	public LongLinkedList(LongList l) {
		addAll(l);
	}
	
	/**
	 * Creates a new LinkedList with a Copy of the array
	 * @param a the array that should be copied
	 */
	public LongLinkedList(long... a) {
		for(int i = 0,m=a.length;i<m;add(a[i++]));
	}
	
	/**
	 * Creates a new LinkedList with a Copy of the array with a custom length
	 * @param a the array that should be copied
	 * @param length the desired length that should be copied
	 */
	public LongLinkedList(long[] a, int length) {
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
	public LongLinkedList(long[] a, int offset, int length) {
		SanityChecks.checkArrayCapacity(a.length, offset, length);
		for(int i = offset,m=offset+length;i<m;add(a[i++]));
	}
	
	@Override
	public boolean add(long e) {
		add(size(), e);
		return true;
	}
	
	@Override
	public void add(int index, long e) {
		checkAddRange(index);
		if(index == size) linkLast(e);
		else if(index == 0) linkFirst(e);
		else linkBefore(e, getNode(index));
	}
	
	@Override
	public boolean addAll(int index, LongCollection c) {
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
		for(LongIterator iter = c.iterator();iter.hasNext();) {
			Entry entry = new Entry(iter.nextLong(), prev, null);
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
	public boolean addAll(int index, LongList c) {
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
		for(LongIterator iter = c.iterator();iter.hasNext();) {
			Entry entry = new Entry(iter.nextLong(), prev, null);
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
	public boolean addAll(int index, Collection<? extends Long> c) {
		if(c instanceof LongCollection) return addAll(index, (LongCollection)c);
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
		for(Iterator<? extends Long> iter = c.iterator();iter.hasNext();) {
			Entry entry = new Entry(iter.next().longValue(), prev, null);
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
	public void enqueue(long e) {
		add(e);
	}
	
	@Override
	public void enqueueFirst(long e) {
		add(0, e);
	}
	
	@Override
	public void push(long e) {
		add(e);
	}
	
	@Override
	public boolean addAll(long[] e, int offset, int length) {
		if(length <= 0) return false;
		SanityChecks.checkArrayCapacity(e.length, offset, length);
		for(int i = 0;i<length;i++) linkLast(e[offset+i]);
		return true;
	}
	
	@Override
	public void addElements(int from, long[] a, int offset, int length) {
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
	public long[] getElements(int from, long[] a, int offset, int length) {
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
	public long first() {
		if(first == null) throw new IllegalStateException();
		return first.value;
	}
	
	@Override
	public long last() {
		if(last == null) throw new IllegalStateException();
		return last.value;
	}
	
	@Override
	public long peek(int index) {
		return getLong((size() - 1) - index);
	}
	
	@Override
	public long getLong(int index) {
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
			if(Objects.equals(Long.valueOf(entry.value), o)) return i;
		}
		return -1;
	}

	@Override
	@Deprecated
	public int lastIndexOf(Object o) {
		Entry entry = last;
		for(int i = size-1;entry != null;entry = entry.prev,i--) {
			if(Objects.equals(Long.valueOf(entry.value), o)) return i;
		}
		return -1;
	}
	
	@Override
	public boolean contains(long e) {
		return indexOf(e) != -1;
	}
	
	@Override
	public int indexOf(long e) {
		Entry entry = first;
		for(int i = 0;entry != null;entry = entry.next,i++) {
			if(entry.value == e) return i;
		}
		return -1;
	}

	@Override
	public int lastIndexOf(long e) {
		Entry entry = last;
		for(int i = size-1;entry != null;entry = entry.prev,i--) {
			if(entry.value == e) return i;
		}
		return -1;
	}
	
	@Override
	public LongListIterator listIterator(int index) {
		if(index < 0 || index > size()) throw new IndexOutOfBoundsException();
		if(index == size) return new ListIter(null, index);
		if(index == 0) return new ListIter(first, index);
		return new ListIter(getNode(index), index);
	}
	
	/**
	 * Returns a Java-Type-Specific Stream to reduce boxing/unboxing.
	 * @return a Stream of the closest java type
	 */
	public LongStream primitiveStream() { return StreamSupport.longStream(new SplitIterator(this, first, 0), false); }
	
	/**
	 * Returns a Java-Type-Specific Parallel Stream to reduce boxing/unboxing.
	 * @return a Stream of the closest java type
	 */
	public LongStream parallelPrimitiveStream() { return StreamSupport.longStream(new SplitIterator(this, first, 0), true); }
	/**
	 * A Type Specific Type Splititerator to reduce boxing/unboxing
	 * @return type specific splititerator
	 */
	@Override
	public LongSplititerator spliterator() { return new TypeSplitIterator(this, first, 0); }
	
	@Override
	public void forEach(LongConsumer action) {
		Objects.requireNonNull(action);
		for(Entry entry = first;entry != null;entry = entry.next) {
			action.accept(entry.value);
		}
	}
	
	@Override
	public <E> void forEach(E input, ObjectLongConsumer<E> action) {
		Objects.requireNonNull(action);
		for(Entry entry = first;entry != null;entry = entry.next)
			action.accept(input, entry.value);		
	}
	
	@Override
	public boolean matchesAny(Long2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(Entry entry = first;entry != null;entry = entry.next) {
			if(filter.get(entry.value)) return true;
		}
		return false;
	}
	
	@Override
	public boolean matchesNone(Long2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(Entry entry = first;entry != null;entry = entry.next) {
			if(filter.get(entry.value)) return false;
		}
		return true;
	}
	
	@Override
	public boolean matchesAll(Long2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(Entry entry = first;entry != null;entry = entry.next) {
			if(!filter.get(entry.value)) return false;
		}
		return true;
	}
	
	@Override
	public long findFirst(Long2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		for(Entry entry = first;entry != null;entry = entry.next) {
			if(filter.get(entry.value)) return entry.value;
		}
		return 0L;
	}
	
	@Override
	public long reduce(long identity, LongLongUnaryOperator operator) {
		Objects.requireNonNull(operator);
		long state = identity;
		for(Entry entry = first;entry != null;entry = entry.next) {
			state = operator.applyAsLong(state, entry.value);
		}
		return state;
	}
	
	@Override
	public long reduce(LongLongUnaryOperator operator) {
		Objects.requireNonNull(operator);
		long state = 0L;
		boolean empty = true;
		for(Entry entry = first;entry != null;entry = entry.next) {
			if(empty) {
				empty = false;
				state = entry.value;
				continue;
			}
			state = operator.applyAsLong(state, entry.value);
		}
		return state;
	}
	
	@Override
	public int count(Long2BooleanFunction filter) {
		Objects.requireNonNull(filter);
		int result = 0;
		for(Entry entry = first;entry != null;entry = entry.next) {
			if(filter.get(entry.value)) result++;
		}
		return result;
	}
	
	@Override
	public long set(int index, long e) {
		checkRange(index);
		Entry node = getNode(index);
		long prev = node.value;
		node.value = e;
		return prev;
	}
	
	@Override
	@Deprecated
	public void replaceAll(UnaryOperator<Long> o) {
		Objects.requireNonNull(o);
		for(Entry entry = first;entry != null;entry = entry.next) {
			entry.value = o.apply(Long.valueOf(entry.value)).longValue();
		}
	}
	
	@Override
	public void replaceLongs(LongUnaryOperator o) {
		Objects.requireNonNull(o);
		for(Entry entry = first;entry != null;entry = entry.next) {
			entry.value = o.applyAsLong(entry.value);
		}
	}
	
	@Override
	public void onChanged() {}
	@Override
	public LongComparator comparator() { return null; }
	
	@Override
	public long dequeue() {
		if(first == null) throw new IllegalStateException();
		return unlinkFirst(first);
	}
	
	@Override
	public long dequeueLast() {
		if(last == null) throw new IllegalStateException();
		return unlinkLast(last);
	}
	
	@Override
	public long pop() {
		return dequeueLast();
	}
	
	@Override
	public boolean removeFirst(long e) {
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
	public boolean removeLast(long e) {
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
	public long swapRemove(int index) {
		checkRange(index);
		Entry entry = getNode(index);
		if(entry == null) return 0L;
		if(entry.next == null) return unlinkLast(entry);
		Entry before = entry.prev;
		long result = unlink(entry);
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
	public boolean swapRemoveLong(long e) {
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
	public boolean remLong(long e) {
		return removeFirst(e);
	}
	
	@Override
	public long removeLong(int index) {
		checkRange(index);
		Entry entry = getNode(index);
		return entry == null ? 0L : unlink(entry);
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
	public long[] extractElements(int from, int to) {
		checkRange(from);
		checkAddRange(to);
		int length = to - from;
		if(length <= 0) return LongArrays.EMPTY_ARRAY;
		long[] d = new long[length];
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
	public void fillBuffer(LongBuffer buffer) {
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
			if(c.contains(Long.valueOf(entry.value))) {
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
			if(!c.contains(Long.valueOf(entry.value))) {
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
	public boolean removeAll(LongCollection c) {
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
	public boolean removeAll(LongCollection c, LongConsumer r) {
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
	public boolean retainAll(LongCollection c) {
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
	public boolean retainAll(LongCollection c, LongConsumer r) {
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
	public boolean removeIf(Predicate<? super Long> filter) {
		Objects.requireNonNull(filter);
		boolean modified = false;
		int j = 0;
		for(Entry entry = first;entry != null;) {
			if(filter.test(Long.valueOf(entry.value))) {
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
	public boolean remIf(LongPredicate filter) {
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
			obj[i++] = Long.valueOf(entry.value);
		}
		return obj;
	}
	
	@Override
	public <E> E[] toArray(E[] a) {
		if(a == null) a = (E[])new Object[size];
		else if(a.length < size) a = (E[])ObjectArrays.newArray(a.getClass().getComponentType(), size);
		int i = 0;
		for(Entry entry = first;entry != null;entry = entry.next) {
			a[i++] = (E)Long.valueOf(entry.value);
		}
		if (a.length > size) a[size] = null;
		return a;
	}
	
	@Override
	public long[] toLongArray(long[] a) {
		if(a.length < size) a = new long[size];
		int i = 0;
		for(Entry entry = first;entry != null;entry = entry.next) {
			a[i++] = entry.value;
		}
		if (a.length > size) a[size] = 0L;
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
	public LongLinkedList copy() {
		LongLinkedList list = new LongLinkedList();
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
	
	protected void linkFirst(long e) {
		Entry f = first;
		Entry newNode = new Entry(e, null, f);
		first = newNode;
		if (f == null) last = newNode;
		else f.prev = newNode;
		size++;
	}
	
	protected void linkLast(long e) {
		Entry l = last;
		Entry newNode = new Entry(e, l, null);
		last = newNode;
		if (l == null) first = newNode;
		else l.next = newNode;
		size++;
	}
	
	protected void linkBefore(long e, Entry succ) {
		Entry prev = succ.prev;
		Entry newNode = new Entry(e, prev, succ);
		succ.prev = newNode;
		if (prev == null) first = newNode;
		else prev.next = newNode;
		size++;
	}
	
	protected long unlinkFirst(Entry f) {
		long element = f.value;
		Entry next = f.next;
		f.next = null;
		first = next;
		if (next == null) last = null;
		else next.prev = null;
		size--;
		return element;
	}
	
	protected long unlinkLast(Entry l) {
		long element = l.value;
		Entry prev = l.prev;
		l.prev = null;
		last = prev;
		if (prev == null) first = null;
		else prev.next = null;
		size--;
		return element;
	}
	
	protected long unlink(Entry x) {
		long element = x.value;
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
		long value;
		Entry prev;
		Entry next;
		
		public Entry(long value, Entry prev, Entry next)
		{
			this.value = value;
			this.prev = prev;
			this.next = next;
		}
	}
	
	private class ListIter implements LongListIterator 
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
		public long previousLong() {
			if(!hasPrevious()) throw new NoSuchElementException();
            lastReturned = next = (next == null) ? last : next.prev;
			index--;
			return lastReturned.value;
		}
		
		@Override
		public long nextLong() {
			if(!hasNext()) throw new NoSuchElementException();
			lastReturned = next;
			next = next.next;
			index++;
			return lastReturned.value;
		}
		
		@Override
		public void set(long e) {
			if(lastReturned == null) throw new IllegalStateException();
			lastReturned.value = e;
		}
		
		@Override
		public void add(long e) {
            lastReturned = null;
            if (next == null) linkLast(e);
            else linkBefore(e, next);
            index++;
		}
	}
	
	private static class TypeSplitIterator implements LongSplititerator 
	{
		static final int BATCH_UNIT = 1 << 10;
		static final int MAX_BATCH = 1 << 25;
		LongLinkedList list;
		Entry entry;
		int index;
		
		TypeSplitIterator(LongLinkedList list, Entry entry, int index)
		{
			this.list = list;
			this.entry = entry;
			this.index = index;
		}
		
		@Override
		public LongSplititerator trySplit() {
			if(entry == null && estimateSize() > 0) {
				int size = Math.min(Math.min(index + BATCH_UNIT, MAX_BATCH), list.size) - index;
				if(size <= 0) return null;
				long[] data = new long[size];
				int subSize = 0;
				for(;subSize<size && entry != null;subSize++) {
					data[subSize] = entry.value;
					entry = entry.next;
					index++;
				}
				return LongSplititerators.createArraySplititerator(data, subSize, characteristics());
			}
			return null;
		}
		
		@Override
		public boolean tryAdvance(LongConsumer action) {
			if(hasNext()) {
				action.accept(nextLong());
				return true;
			}
			return false;
		}
		
		@Override
		public boolean tryAdvance(Consumer<? super Long> action) {
			if(hasNext()) {
				action.accept(Long.valueOf(nextLong()));
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
		public long nextLong() {
			long value = entry.value;
			entry = entry.next;
			index++;
			return value;
		}
		
		@Override
		public boolean hasNext() {
			return entry != null;
		}
	}
	
	private static class SplitIterator implements OfLong
	{
		static final int BATCH_UNIT = 1 << 10;
		static final int MAX_BATCH = 1 << 25;
		LongLinkedList list;
		Entry entry;
		int index;
		
		SplitIterator(LongLinkedList list, Entry entry, int index)
		{
			this.list = list;
			this.entry = entry;
			this.index = index;
		}
		
		@Override
		public OfLong trySplit() {
			if(entry == null && estimateSize() > 0) {
				int size = Math.min(Math.min(index + BATCH_UNIT, MAX_BATCH), list.size) - index;
				if(size <= 0) return null;
				long[] data = new long[size];
				int subSize = 0;
				for(;subSize<size && entry != null;subSize++) {
					data[subSize] = entry.value;
					entry = entry.next;
					index++;
				}
				return LongSplititerators.createArrayJavaSplititerator(data, subSize, characteristics());
			}
			return null;
		}
		
		@Override
		public boolean tryAdvance(java.util.function.LongConsumer action) {
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
		
		public long next() {
			long value = entry.value;
			entry = entry.next;
			index++;
			return value;
		}
		
		public boolean hasNext() {
			return entry != null;
		}
	}
}