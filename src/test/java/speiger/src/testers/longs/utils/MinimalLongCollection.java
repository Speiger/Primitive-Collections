package speiger.src.testers.longs.utils;

import java.util.Collection;

import speiger.src.collections.longs.collections.AbstractLongCollection;
import speiger.src.collections.longs.collections.LongCollection;
import speiger.src.collections.longs.collections.LongIterator;
import speiger.src.collections.longs.functions.LongConsumer;
import speiger.src.collections.longs.lists.LongArrayList;

@SuppressWarnings("javadoc")
public class MinimalLongCollection extends AbstractLongCollection {

	private final long[] contents;

	protected MinimalLongCollection(long[] contents) {
		this.contents = contents;
	}

	public static MinimalLongCollection of(long... contents) {
		return new MinimalLongCollection(contents);
	}
	
	@Override
	public boolean add(long o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(LongCollection c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(LongCollection c, LongConsumer r) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(LongCollection c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(LongCollection c, LongConsumer r) {
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
	public LongIterator iterator() {
		return LongArrayList.wrap(contents).iterator();
	}
	
	@Override
	public long[] toLongArray(long[] a) {
		if(a.length < contents.length) a = new long[contents.length];
		System.arraycopy(contents, 0, a, 0, contents.length);
		if (a.length > contents.length) a[contents.length] = 0L;
		return a;
	}
	
	@Override
	public int size() {
		return contents.length;
	}
}