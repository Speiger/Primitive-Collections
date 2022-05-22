package speiger.src.testers.ints.utils;

import java.util.Collection;

import speiger.src.collections.ints.collections.AbstractIntCollection;
import speiger.src.collections.ints.collections.IntCollection;
import speiger.src.collections.ints.collections.IntIterator;
import speiger.src.collections.ints.functions.IntConsumer;
import speiger.src.collections.ints.lists.IntArrayList;

public class MinimalIntCollection extends AbstractIntCollection {

	private final int[] contents;

	protected MinimalIntCollection(int[] contents) {
		this.contents = contents;
	}

	public static MinimalIntCollection of(int... contents) {
		return new MinimalIntCollection(contents);
	}
	
	@Override
	public boolean add(int o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(IntCollection c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(IntCollection c, IntConsumer r) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(IntCollection c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(IntCollection c, IntConsumer r) {
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
	public IntIterator iterator() {
		return IntArrayList.wrap(contents).iterator();
	}
	
	@Override
	public int[] toIntArray(int[] a) {
		if(a.length < contents.length) a = new int[contents.length];
		System.arraycopy(contents, 0, a, 0, contents.length);
		if (a.length > contents.length) a[contents.length] = 0;
		return a;
	}
	
	@Override
	public int size() {
		return contents.length;
	}
}