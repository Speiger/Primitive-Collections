package speiger.src.testers.chars.utils;

import java.util.Collection;

import speiger.src.collections.chars.collections.AbstractCharCollection;
import speiger.src.collections.chars.collections.CharCollection;
import speiger.src.collections.chars.collections.CharIterator;
import speiger.src.collections.chars.functions.CharConsumer;
import speiger.src.collections.chars.lists.CharArrayList;

public class MinimalCharCollection extends AbstractCharCollection {

	private final char[] contents;

	protected MinimalCharCollection(char[] contents) {
		this.contents = contents;
	}

	public static MinimalCharCollection of(char... contents) {
		return new MinimalCharCollection(contents);
	}
	
	@Override
	public boolean add(char o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(CharCollection c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(CharCollection c, CharConsumer r) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(CharCollection c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(CharCollection c, CharConsumer r) {
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
	public CharIterator iterator() {
		return CharArrayList.wrap(contents).iterator();
	}
	
	@Override
	public char[] toCharArray(char[] a) {
		if(a.length < contents.length) a = new char[contents.length];
		System.arraycopy(contents, 0, a, 0, contents.length);
		if (a.length > contents.length) a[contents.length] = (char)0;
		return a;
	}
	
	@Override
	public int size() {
		return contents.length;
	}
}