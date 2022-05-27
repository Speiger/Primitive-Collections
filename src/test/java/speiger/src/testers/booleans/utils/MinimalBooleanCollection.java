package speiger.src.testers.booleans.utils;

import java.util.Collection;

import speiger.src.collections.booleans.collections.AbstractBooleanCollection;
import speiger.src.collections.booleans.collections.BooleanCollection;
import speiger.src.collections.booleans.collections.BooleanIterator;
import speiger.src.collections.booleans.functions.BooleanConsumer;
import speiger.src.collections.booleans.lists.BooleanArrayList;

@SuppressWarnings("javadoc")
public class MinimalBooleanCollection extends AbstractBooleanCollection {

	private final boolean[] contents;

	protected MinimalBooleanCollection(boolean[] contents) {
		this.contents = contents;
	}

	public static MinimalBooleanCollection of(boolean... contents) {
		return new MinimalBooleanCollection(contents);
	}
	
	@Override
	public boolean add(boolean o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(BooleanCollection c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(BooleanCollection c, BooleanConsumer r) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(BooleanCollection c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(BooleanCollection c, BooleanConsumer r) {
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
	public BooleanIterator iterator() {
		return BooleanArrayList.wrap(contents).iterator();
	}
	
	@Override
	public boolean[] toBooleanArray(boolean[] a) {
		if(a.length < contents.length) a = new boolean[contents.length];
		System.arraycopy(contents, 0, a, 0, contents.length);
		if (a.length > contents.length) a[contents.length] = false;
		return a;
	}
	
	@Override
	public int size() {
		return contents.length;
	}
}