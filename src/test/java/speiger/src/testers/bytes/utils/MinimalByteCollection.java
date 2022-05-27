package speiger.src.testers.bytes.utils;

import java.util.Collection;

import speiger.src.collections.bytes.collections.AbstractByteCollection;
import speiger.src.collections.bytes.collections.ByteCollection;
import speiger.src.collections.bytes.collections.ByteIterator;
import speiger.src.collections.bytes.functions.ByteConsumer;
import speiger.src.collections.bytes.lists.ByteArrayList;

@SuppressWarnings("javadoc")
public class MinimalByteCollection extends AbstractByteCollection {

	private final byte[] contents;

	protected MinimalByteCollection(byte[] contents) {
		this.contents = contents;
	}

	public static MinimalByteCollection of(byte... contents) {
		return new MinimalByteCollection(contents);
	}
	
	@Override
	public boolean add(byte o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(ByteCollection c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(ByteCollection c, ByteConsumer r) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(ByteCollection c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(ByteCollection c, ByteConsumer r) {
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
	public ByteIterator iterator() {
		return ByteArrayList.wrap(contents).iterator();
	}
	
	@Override
	public byte[] toByteArray(byte[] a) {
		if(a.length < contents.length) a = new byte[contents.length];
		System.arraycopy(contents, 0, a, 0, contents.length);
		if (a.length > contents.length) a[contents.length] = (byte)0;
		return a;
	}
	
	@Override
	public int size() {
		return contents.length;
	}
}