package speiger.src.testers.doubles.utils;

import java.util.Collection;

import speiger.src.collections.doubles.collections.AbstractDoubleCollection;
import speiger.src.collections.doubles.collections.DoubleCollection;
import speiger.src.collections.doubles.collections.DoubleIterator;
import speiger.src.collections.doubles.functions.DoubleConsumer;
import speiger.src.collections.doubles.lists.DoubleArrayList;

@SuppressWarnings("javadoc")
public class MinimalDoubleCollection extends AbstractDoubleCollection {

	private final double[] contents;

	protected MinimalDoubleCollection(double[] contents) {
		this.contents = contents;
	}

	public static MinimalDoubleCollection of(double... contents) {
		return new MinimalDoubleCollection(contents);
	}
	
	@Override
	public boolean add(double o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(DoubleCollection c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(DoubleCollection c, DoubleConsumer r) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(DoubleCollection c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(DoubleCollection c, DoubleConsumer r) {
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
	public DoubleIterator iterator() {
		return DoubleArrayList.wrap(contents).iterator();
	}
	
	@Override
	public double[] toDoubleArray(double[] a) {
		if(a.length < contents.length) a = new double[contents.length];
		System.arraycopy(contents, 0, a, 0, contents.length);
		if (a.length > contents.length) a[contents.length] = 0D;
		return a;
	}
	
	@Override
	public int size() {
		return contents.length;
	}
}