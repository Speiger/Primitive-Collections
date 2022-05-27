package speiger.src.testers.objects.utils;

import java.util.Collection;
import java.util.function.Consumer;

import speiger.src.collections.objects.collections.AbstractObjectCollection;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.utils.ObjectArrays;
import speiger.src.collections.objects.lists.ObjectArrayList;

public class MinimalObjectCollection<T> extends AbstractObjectCollection<T> {

	private final T[] contents;

	protected MinimalObjectCollection(T[] contents) {
		this.contents = contents;
	}

	public static <T> MinimalObjectCollection<T> of(T... contents) {
		return new MinimalObjectCollection<>(contents);
	}
	
	@Override
	public boolean add(T o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(ObjectCollection<T> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(ObjectCollection<T> c, Consumer<T> r) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(ObjectCollection<T> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(ObjectCollection<T> c, Consumer<T> r) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ObjectIterator<T> iterator() {
		return ObjectArrayList.wrap(contents).iterator();
	}
	
	@Override
	public <E> E[] toArray(E[] a) {
		if(a == null) a = (E[])new Object[contents.length];
		else if(a.length < contents.length) a = (E[])ObjectArrays.newArray(a.getClass().getComponentType(), contents.length);
        System.arraycopy(contents, 0, a, 0, contents.length);
		if (a.length > contents.length) a[contents.length] = null;
		return a;
	}
	
	@Override
	public int size() {
		return contents.length;
	}
}