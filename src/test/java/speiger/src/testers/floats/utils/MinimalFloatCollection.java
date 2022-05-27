package speiger.src.testers.floats.utils;

import java.util.Collection;

import speiger.src.collections.floats.collections.AbstractFloatCollection;
import speiger.src.collections.floats.collections.FloatCollection;
import speiger.src.collections.floats.collections.FloatIterator;
import speiger.src.collections.floats.functions.FloatConsumer;
import speiger.src.collections.floats.lists.FloatArrayList;

@SuppressWarnings("javadoc")
public class MinimalFloatCollection extends AbstractFloatCollection {

	private final float[] contents;

	protected MinimalFloatCollection(float[] contents) {
		this.contents = contents;
	}

	public static MinimalFloatCollection of(float... contents) {
		return new MinimalFloatCollection(contents);
	}
	
	@Override
	public boolean add(float o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(FloatCollection c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(FloatCollection c, FloatConsumer r) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(FloatCollection c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(FloatCollection c, FloatConsumer r) {
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
	public FloatIterator iterator() {
		return FloatArrayList.wrap(contents).iterator();
	}
	
	@Override
	public float[] toFloatArray(float[] a) {
		if(a.length < contents.length) a = new float[contents.length];
		System.arraycopy(contents, 0, a, 0, contents.length);
		if (a.length > contents.length) a[contents.length] = 0F;
		return a;
	}
	
	@Override
	public int size() {
		return contents.length;
	}
}