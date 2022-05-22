package speiger.src.testers.shorts.utils;

import java.util.Collection;

import speiger.src.collections.shorts.collections.AbstractShortCollection;
import speiger.src.collections.shorts.collections.ShortCollection;
import speiger.src.collections.shorts.collections.ShortIterator;
import speiger.src.collections.shorts.functions.ShortConsumer;
import speiger.src.collections.shorts.lists.ShortArrayList;

public class MinimalShortCollection extends AbstractShortCollection {

	private final short[] contents;

	protected MinimalShortCollection(short[] contents) {
		this.contents = contents;
	}

	public static MinimalShortCollection of(short... contents) {
		return new MinimalShortCollection(contents);
	}
	
	@Override
	public boolean add(short o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(ShortCollection c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(ShortCollection c, ShortConsumer r) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(ShortCollection c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(ShortCollection c, ShortConsumer r) {
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
	public ShortIterator iterator() {
		return ShortArrayList.wrap(contents).iterator();
	}
	
	@Override
	public short[] toShortArray(short[] a) {
		if(a.length < contents.length) a = new short[contents.length];
		System.arraycopy(contents, 0, a, 0, contents.length);
		if (a.length > contents.length) a[contents.length] = (short)0;
		return a;
	}
	
	@Override
	public int size() {
		return contents.length;
	}
}