package speiger.src.collections.objects.lists;

import java.util.Comparator;
import java.util.function.BiFunction;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;
import speiger.src.collections.objects.functions.function.Object2BooleanFunction;
import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.ints.functions.function.Int2ObjectFunction;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.queues.ObjectPriorityDequeue;
import speiger.src.collections.objects.utils.ObjectArrays;
import speiger.src.collections.utils.Stack;
import speiger.src.collections.objects.collections.ObjectSplititerator;
import speiger.src.collections.objects.utils.ObjectSplititerators;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Type-Specific LinkedList implementation of list that is written to reduce (un)boxing
 * 
 * <p>This implementation is optimized to improve how data is processed with interfaces like {@link Stack}
 * and with optimized functions that use type-specific implementations for primitives and optimized logic for bulk actions.
 * 
 * @param <T> the type of elements maintained by this Collection
 */
public class ObjectLinkedList<T> extends AbstractObjectList<T> implements ObjectPriorityDequeue<T>, Stack<T>
{
	Entry<T> first;
	Entry<T> last;
	int size = 0;
	
	/**
	 * Creates a new LinkedList.
	 */
	public ObjectLinkedList() {
	}
	
	/**
	 * Creates a new LinkedList a copy with the contents of the Collection.
	 * @param c the elements that should be added into the list
	 */
	@Deprecated
	public ObjectLinkedList(Collection<? extends T> c) {
		addAll(c);
	}
	
	/**
	 * Creates a new LinkedList a copy with the contents of the Collection.
	 * @param c the elements that should be added into the list
	 */
	public ObjectLinkedList(ObjectCollection<T> c) {
		addAll(c);
	}
	
	/**
	 * Creates a new LinkedList a copy with the contents of the List.
	 * @param l the elements that should be added into the list
	 */
	public ObjectLinkedList(ObjectList<T> l) {
		addAll(l);
	}
	
	/**
	 * Creates a new LinkedList with a Copy of the array
	 * @param a the array that should be copied
	 */
	public ObjectLinkedList(T... a) {
		for(int i = 0,m=a.length;i<m;add(a[i++]));
	}
	
	/**
	 * Creates a new LinkedList with a Copy of the array with a custom length
	 * @param a the array that should be copied
	 * @param length the desired length that should be copied
	 */
	public ObjectLinkedList(T[] a, int length) {
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
	public ObjectLinkedList(T[] a, int offset, int length) {
		SanityChecks.checkArrayCapacity(a.length, offset, length);
		for(int i = offset,m=offset+length;i<m;add(a[i++]));
	}
	
	@Override
	public boolean add(T e) {
		add(size(), e);
		return true;
	}
	
	@Override
	public void add(int index, T e) {
		checkAddRange(index);
		if(index == size) linkLast(e);
		else if(index == 0) linkFirst(e);
		else linkBefore(e, getNode(index));
	}
	
	@Override
	public boolean addAll(int index, ObjectCollection<T> c) {
		int length = c.size();
		if(length == 0) return false;
		checkAddRange(index);
		Entry<T> next;
		Entry<T> prev;
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
		for(ObjectIterator<T> iter = c.iterator();iter.hasNext();) {
			Entry<T> entry = new Entry<>(iter.next(), prev, null);
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
	public boolean addAll(int index, ObjectList<T> c) {
		int length = c.size();
		if(length == 0) return false;
		checkAddRange(index);
		Entry<T> next;
		Entry<T> prev;
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
		for(ObjectIterator<T> iter = c.iterator();iter.hasNext();) {
			Entry<T> entry = new Entry<>(iter.next(), prev, null);
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
	public boolean addAll(int index, Collection<? extends T> c) {
		if(c instanceof ObjectCollection) return addAll(index, (ObjectCollection<T>)c);
		int length = c.size();
		if(length == 0) return false;
		checkAddRange(index);
		Entry<T> next;
		Entry<T> prev;
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
		for(Iterator<? extends T> iter = c.iterator();iter.hasNext();) {
			Entry<T> entry = new Entry<>(iter.next(), prev, null);
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
	public void enqueue(T e) {
		add(e);
	}
	
	@Override
	public void enqueueFirst(T e) {
		add(0, e);
	}
	
	@Override
	public void push(T e) {
		add(e);
	}
	
	@Override
	public boolean addAll(T[] e, int offset, int length) {
		if(length <= 0) return false;
		SanityChecks.checkArrayCapacity(e.length, offset, length);
		for(int i = 0;i<length;i++) linkLast(e[offset+i]);
		return true;
	}
	
	@Override
	public void addElements(int from, T[] a, int offset, int length) {
		if(length <= 0) return;
		SanityChecks.checkArrayCapacity(a.length, offset, length);
		checkAddRange(from);
		Entry<T> next;
		Entry<T> prev;
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
			Entry<T> entry = new Entry<>(a[i], prev, null);
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
	public T[] getElements(int from, T[] a, int offset, int length) {
		SanityChecks.checkArrayCapacity(size, from, length);
		SanityChecks.checkArrayCapacity(a.length, offset, length);
		Entry<T> entry = getNode(from);
		while(length > 0) {
			a[offset++] = entry.value;
			length--;
			entry = entry.next;
		}
		return a;
	}
	
	@Override
	public T first() {
		if(first == null) throw new IllegalStateException();
		return first.value;
	}
	
	@Override
	public T last() {
		if(last == null) throw new IllegalStateException();
		return last.value;
	}
	
	@Override
	public T peek(int index) {
		return get((size() - 1) - index);
	}
	
	@Override
	public T get(int index) {
		checkRange(index);
		return getNode(index).value;
	}
	
	@Override
	public boolean contains(Object e) {
		return indexOf(e) != -1;
	}
	
	@Override
	public int indexOf(Object o) {
		Entry<T> entry = first;
		for(int i = 0;entry != null;entry = entry.next,i++) {
			if(Objects.equals(entry.value, o)) return i;
		}
		return -1;
	}

	@Override
	public int lastIndexOf(Object o) {
		Entry<T> entry = last;
		for(int i = size-1;entry != null;entry = entry.prev,i--) {
			if(Objects.equals(entry.value, o)) return i;
		}
		return -1;
	}
	
	@Override
	public ObjectListIterator<T> listIterator(int index) {
		if(index < 0 || index > size()) throw new IndexOutOfBoundsException();
		if(index == size) return new ListIter(null, index);
		if(index == 0) return new ListIter(first, index);
		return new ListIter(getNode(index), index);
	}
	
	/**
	 * A Type Specific Type Splititerator to reduce boxing/unboxing
	 * @return type specific splititerator
	 */
	@Override
	public ObjectSplititerator<T> spliterator() { return new TypeSplitIterator<>(this, first, 0); }
	
	@Override
	public void forEach(Consumer<? super T> action) {
		Objects.requireNonNull(action);
		for(Entry<T> entry = first;entry != null;entry = entry.next) {
			action.accept(entry.value);
		}
	}
	
	@Override
	public <E> void forEach(E input, ObjectObjectConsumer<E, T> action) {
		Objects.requireNonNull(action);
		for(Entry<T> entry = first;entry != null;entry = entry.next)
			action.accept(input, entry.value);		
	}
	
	@Override
	public boolean matchesAny(Object2BooleanFunction<T> filter) {
		Objects.requireNonNull(filter);
		for(Entry<T> entry = first;entry != null;entry = entry.next) {
			if(filter.getBoolean(entry.value)) return true;
		}
		return false;
	}
	
	@Override
	public boolean matchesNone(Object2BooleanFunction<T> filter) {
		Objects.requireNonNull(filter);
		for(Entry<T> entry = first;entry != null;entry = entry.next) {
			if(filter.getBoolean(entry.value)) return false;
		}
		return true;
	}
	
	@Override
	public boolean matchesAll(Object2BooleanFunction<T> filter) {
		Objects.requireNonNull(filter);
		for(Entry<T> entry = first;entry != null;entry = entry.next) {
			if(!filter.getBoolean(entry.value)) return false;
		}
		return true;
	}
	
	@Override
	public T findFirst(Object2BooleanFunction<T> filter) {
		Objects.requireNonNull(filter);
		for(Entry<T> entry = first;entry != null;entry = entry.next) {
			if(filter.getBoolean(entry.value)) return entry.value;
		}
		return null;
	}
	
	@Override
	public <E> E reduce(E identity, BiFunction<E, T, E> operator) {
		Objects.requireNonNull(operator);
		E state = identity;
		for(Entry<T> entry = first;entry != null;entry = entry.next) {
			state = operator.apply(state, entry.value);
		}
		return state;
	}
	
	@Override
	public T reduce(ObjectObjectUnaryOperator<T, T> operator) {
		Objects.requireNonNull(operator);
		T state = null;
		boolean empty = true;
		for(Entry<T> entry = first;entry != null;entry = entry.next) {
			if(empty) {
				empty = false;
				state = entry.value;
				continue;
			}
			state = operator.apply(state, entry.value);
		}
		return state;
	}
	
	@Override
	public int count(Object2BooleanFunction<T> filter) {
		Objects.requireNonNull(filter);
		int result = 0;
		for(Entry<T> entry = first;entry != null;entry = entry.next) {
			if(filter.getBoolean(entry.value)) result++;
		}
		return result;
	}
	
	@Override
	public T set(int index, T e) {
		checkRange(index);
		Entry<T> node = getNode(index);
		T prev = node.value;
		node.value = e;
		return prev;
	}
	
	@Override
	public void replaceAll(UnaryOperator<T> o) {
		Objects.requireNonNull(o);
		for(Entry<T> entry = first;entry != null;entry = entry.next) {
			entry.value = o.apply(entry.value);
		}
	}
	
	@Override
	public void onChanged() {}
	@Override
	public Comparator<T> comparator() { return null; }
	
	@Override
	public T dequeue() {
		if(first == null) throw new IllegalStateException();
		return unlinkFirst(first);
	}
	
	@Override
	public T dequeueLast() {
		if(last == null) throw new IllegalStateException();
		return unlinkLast(last);
	}
	
	@Override
	public T pop() {
		return dequeueLast();
	}
	
	@Override
	public boolean removeFirst(T e) {
		if(size == 0) return false;
		for(Entry<T> entry = first;entry != null;entry = entry.next) {
			if(Objects.equals(entry.value, e)) {
				unlink(entry);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean removeLast(T e) {
		if(size == 0) return false;
		for(Entry<T> entry = last;entry != null;entry = entry.prev) {
			if(Objects.equals(entry.value, e)) {
				unlink(entry);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public T swapRemove(int index) {
		checkRange(index);
		Entry<T> entry = getNode(index);
		if(entry == null) return null;
		if(entry.next == null) return unlinkLast(entry);
		Entry<T> before = entry.prev;
		T result = unlink(entry);
		if(before == null) {
			Entry<T> temp = last;
			last = temp.prev;
			last.next = null;
			temp.next = first;
			temp.prev = null;
			first.prev = temp;
			first = temp;
			return result;
		}
		else if(before.next != last) {
			Entry<T> temp = last;
			last = temp.prev;
			last.next = null;
			temp.next = before.next;
			temp.prev = before;
			before.next = temp;
		}
		return result;
	}
	
	@Override
	public boolean swapRemove(T e) {
		if(size == 0) return false;
		for(Entry<T> entry = last;entry != null;entry = entry.prev) {
			if(Objects.equals(entry.value, e)) {
				if(entry.next == null) {
					unlinkLast(entry);
					return true;
				}
				Entry<T> before = entry.prev;
				unlink(entry);
				if(before == null) {
					Entry<T> temp = last;
					last = temp.prev;
					last.next = null;
					temp.next = first;
					temp.prev = null;
					first.prev = temp;
					first = temp;
					return true;
				}
				else if(before.next != last) {
					Entry<T> temp = last;
					last = temp.prev;
					last.next = null;
					temp.next = before.next;
					temp.prev = before;
					before.next = temp;
				}
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean remove(Object e) {
		if(size <= 0) return false;
		for(Entry<T> entry = first;entry != null;entry = entry.next) {
			if(Objects.equals(entry.value, e)) {
				unlink(entry);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public T remove(int index) {
		checkRange(index);
		Entry<T> entry = getNode(index);
		return entry == null ? null : unlink(entry);
	}
	
	@Override
	public void removeElements(int from, int to) {
		checkRange(from);
		checkAddRange(to);
		int length = to - from;
		if(length <= 0) return;
		if(from < size - to) {
			Entry<T> entry = getNode(from);
			while(length > 0) {
				Entry<T> next = entry.next;
				unlink(entry);
				entry = next;
				length--;
			}
			return;
		}
		Entry<T> entry = getNode(to);
		while(length > 0) {
			Entry<T> prev = entry.prev;
			unlink(entry);
			entry = prev;
			length--;
		}
	}
	
	@Override
	public <K> K[] extractElements(int from, int to, Class<K> type) {
		checkRange(from);
		checkAddRange(to);
		int length = to - from;
		K[] a = ObjectArrays.newArray(type, length);
		if(length <= 0) return a;
		if(from < size - to) {
			Entry<T> entry = getNode(from);
			for(int i = 0;length > 0;i++, length--) {
				Entry<T> next = entry.next;
				a[i] = (K)unlink(entry);
				entry = next;
			}
			return a;
		}
		Entry<T> entry = getNode(to);
		for(int i = length-1;length > 0;i--, length--) {
			Entry<T> prev = entry.prev;
			a[i] = (K)unlink(entry);
			entry = prev;
		}
		return a;
	}
	
	@Override
	public boolean removeAll(Collection<?> c) {
		if(c.isEmpty()) return false;
		boolean modified = false;
		int j = 0;
		for(Entry<T> entry = first;entry != null;) {
			if(c.contains(entry.value)) {
				Entry<T> next = entry.next;
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
	public boolean retainAll(Collection<?> c) {
		if(c.isEmpty()) {
			boolean changed = size > 0;
			clear();
			return changed;
		}
		boolean modified = false;
		int j = 0;
		for(Entry<T> entry = first;entry != null;) {
			if(!c.contains(entry.value)) {
				Entry<T> next = entry.next;
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
	public boolean removeAll(ObjectCollection<T> c) {
		if(c.isEmpty()) return false;
		boolean modified = false;
		int j = 0;
		for(Entry<T> entry = first;entry != null;) {
			if(c.contains(entry.value)) {
				Entry<T> next = entry.next;
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
	public boolean removeAll(ObjectCollection<T> c, Consumer<T> r) {
		if(c.isEmpty()) return false;
		boolean modified = false;
		int j = 0;
		for(Entry<T> entry = first;entry != null;) {
			if(c.contains(entry.value)) {
				r.accept(entry.value);
				Entry<T> next = entry.next;
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
	public boolean retainAll(ObjectCollection<T> c) {
		if(c.isEmpty()) {
			boolean changed = size > 0;
			clear();
			return changed;
		}
		boolean modified = false;
		int j = 0;
		for(Entry<T> entry = first;entry != null;) {
			if(!c.contains(entry.value)) {
				Entry<T> next = entry.next;
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
	public boolean retainAll(ObjectCollection<T> c, Consumer<T> r) {
		if(c.isEmpty()) {
			boolean changed = size > 0;
			forEach(r);
			clear();
			return changed;
		}
		boolean modified = false;
		int j = 0;
		for(Entry<T> entry = first;entry != null;) {
			if(!c.contains(entry.value)) {
				r.accept(entry.value);
				Entry<T> next = entry.next;
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
	public boolean removeIf(Predicate<? super T> filter) {
		Objects.requireNonNull(filter);
		boolean modified = false;
		int j = 0;
		for(Entry<T> entry = first;entry != null;) {
			if(filter.test(entry.value)) {
				Entry<T> next = entry.next;
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
		for(Entry<T> entry = first;entry != null;entry = entry.next) {
			obj[i++] = entry.value;
		}
		return obj;
	}
	
	@Override
	public <E> E[] toArray(E[] a) {
		if(a == null) a = (E[])new Object[size];
		else if(a.length < size) a = (E[])ObjectArrays.newArray(a.getClass().getComponentType(), size);
		int i = 0;
		for(Entry<T> entry = first;entry != null;entry = entry.next) {
			a[i++] = (E)entry.value;
		}
		if (a.length > size) a[size] = null;
		return a;
	}
	
	@Override
	public T[] toArray(Int2ObjectFunction<T[]> action) {
		return super.toArray(action);
	}
	
	@Override
	public int size() {
		return size;
	}
	
	@Override
	public void clear() {
		for(Entry<T> entry = first;entry != null;) {
			Entry<T> next = entry.next;
			entry.next = entry.prev = null;
			entry = next;
		}
		first = null;
		last = null;
		size = 0;
	}
	
	@Override
	public ObjectLinkedList<T> copy() {
		ObjectLinkedList<T> list = new ObjectLinkedList<>();
		list.size = size;
		if(first != null) {
			list.first = new Entry<>(first.value, null, null);
			Entry<T> lastReturned = list.first;
			for(Entry<T> entry = first.next;entry != null;entry = entry.next) {
				Entry<T> next = new Entry<>(entry.value, lastReturned, null);
				lastReturned.next = next;
				lastReturned = next;
			}
			list.last = lastReturned;
		}
		return list;
	}
	
	protected Entry<T> getNode(int index) {
		if(index < size >> 2) {
			Entry<T> x = first;
			for (int i = 0; i < index; i++)
				x = x.next;
			return x;
		}
		Entry<T> x = last;
		for (int i = size - 1; i > index; i--)
			x = x.prev;
		return x;
	}
	
	protected void linkFirst(T e) {
		Entry<T> f = first;
		Entry<T> newNode = new Entry<>(e, null, f);
		first = newNode;
		if (f == null) last = newNode;
		else f.prev = newNode;
		size++;
	}
	
	protected void linkLast(T e) {
		Entry<T> l = last;
		Entry<T> newNode = new Entry<>(e, l, null);
		last = newNode;
		if (l == null) first = newNode;
		else l.next = newNode;
		size++;
	}
	
	protected void linkBefore(T e, Entry<T> succ) {
		Entry<T> prev = succ.prev;
		Entry<T> newNode = new Entry<>(e, prev, succ);
		succ.prev = newNode;
		if (prev == null) first = newNode;
		else prev.next = newNode;
		size++;
	}
	
	protected T unlinkFirst(Entry<T> f) {
		T element = f.value;
		Entry<T> next = f.next;
		f.next = null;
		f.value = null;
		first = next;
		if (next == null) last = null;
		else next.prev = null;
		size--;
		return element;
	}
	
	protected T unlinkLast(Entry<T> l) {
		T element = l.value;
		Entry<T> prev = l.prev;
		l.prev = null;
		l.value = null;
		last = prev;
		if (prev == null) first = null;
		else prev.next = null;
		size--;
		return element;
	}
	
	protected T unlink(Entry<T> x) {
		T element = x.value;
		Entry<T> next = x.next;
		Entry<T> prev = x.prev;
		x.value = null;
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
	
	protected static class Entry<T> {
		T value;
		Entry<T> prev;
		Entry<T> next;
		
		public Entry(T value, Entry<T> prev, Entry<T> next)
		{
			this.value = value;
			this.prev = prev;
			this.next = next;
		}
	}
	
	private class ListIter implements ObjectListIterator<T> 
	{
		Entry<T> next;
		Entry<T> lastReturned;
		int index;
		
		ListIter(Entry<T> next, int index) {
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
			Entry<T> lastNext = lastReturned.next;
			unlink(lastReturned);
			if (next == lastReturned) next = lastNext;
			else index--;
			lastReturned = null;
		}
		
		@Override
		public T previous() {
			if(!hasPrevious()) throw new NoSuchElementException();
            lastReturned = next = (next == null) ? last : next.prev;
			index--;
			return lastReturned.value;
		}
		
		@Override
		public T next() {
			if(!hasNext()) throw new NoSuchElementException();
			lastReturned = next;
			next = next.next;
			index++;
			return lastReturned.value;
		}
		
		@Override
		public void set(T e) {
			if(lastReturned == null) throw new IllegalStateException();
			lastReturned.value = e;
		}
		
		@Override
		public void add(T e) {
            lastReturned = null;
            if (next == null) linkLast(e);
            else linkBefore(e, next);
            index++;
		}
	}
	
	private static class TypeSplitIterator<T> implements ObjectSplititerator<T> 
	{
		static final int BATCH_UNIT = 1 << 10;
		static final int MAX_BATCH = 1 << 25;
		ObjectLinkedList<T> list;
		Entry<T> entry;
		int index;
		
		TypeSplitIterator(ObjectLinkedList<T> list, Entry<T> entry, int index)
		{
			this.list = list;
			this.entry = entry;
			this.index = index;
		}
		
		@Override
		public ObjectSplititerator<T> trySplit() {
			if(entry == null && estimateSize() > 0) {
				int size = Math.min(Math.min(index + BATCH_UNIT, MAX_BATCH), list.size) - index;
				if(size <= 0) return null;
				T[] data = (T[])new Object[size];
				int subSize = 0;
				for(;subSize<size && entry != null;subSize++) {
					data[subSize] = (T)entry.value;
					entry = entry.next;
					index++;
				}
				return ObjectSplititerators.createArraySplititerator(data, subSize, characteristics());
			}
			return null;
		}
		
		@Override
		public boolean tryAdvance(Consumer<? super T> action) {
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
		
		@Override
		public T next() {
			T value = entry.value;
			entry = entry.next;
			index++;
			return value;
		}
		
		@Override
		public boolean hasNext() {
			return entry != null;
		}
	}
	
}