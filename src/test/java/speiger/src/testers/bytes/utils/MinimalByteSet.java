package speiger.src.testers.bytes.utils;

import speiger.src.collections.bytes.collections.ByteIterable;
import speiger.src.collections.bytes.collections.ByteIterator;
import speiger.src.collections.bytes.lists.ByteArrayList;
import speiger.src.collections.bytes.lists.ByteList;
import speiger.src.collections.bytes.sets.ByteSet;

public class MinimalByteSet extends MinimalByteCollection implements ByteSet {
	public static MinimalByteSet of(byte...array) {
		return MinimalByteSet.of(ByteArrayList.wrap(array));
	}
	
	public static MinimalByteSet of(ByteIterable iterable)
	{
		ByteList list = new ByteArrayList();
		for(ByteIterator iter = iterable.iterator();iter.hasNext();)
		{
			byte key = iter.nextByte();
			if(list.contains(key)) continue;
			list.add(key);
		}
		return new MinimalByteSet(list.toByteArray());
	}
	
	protected MinimalByteSet(byte[] contents) {
		super(contents);
	}
	
	@Override
	public ByteSet copy() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof ByteSet) {
			ByteSet that = (ByteSet) object;
			return (size() == that.size()) && this.containsAll(that);
		}
		return false;
	}

	@Override
	public int hashCode() {
		int hashCodeSum = 0;
		for (ByteIterator iter = iterator();iter.hasNext();) {
			hashCodeSum += Byte.hashCode(iter.nextByte());
		}
		return hashCodeSum;
	}

	@Override
	public boolean remove(byte o) {
		for(ByteIterator iter = iterator();iter.hasNext();) {
			if(iter.nextByte() == o) {
				iter.remove();
				return true;
			}
		}
		return false;
	}

}