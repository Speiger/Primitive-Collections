package speiger.src.testers.longs.utils;


import speiger.src.collections.longs.collections.LongIterable;
import speiger.src.collections.longs.collections.LongIterator;
import speiger.src.collections.longs.lists.LongArrayList;
import speiger.src.collections.longs.lists.LongList;
import speiger.src.collections.longs.sets.LongSet;

public class MinimalLongSet extends MinimalLongCollection implements LongSet {
	public static MinimalLongSet of(long...array) {
		return MinimalLongSet.of(LongArrayList.wrap(array));
	}
	
	public static MinimalLongSet of(LongIterable iterable)
	{
		LongList list = new LongArrayList();
		for(LongIterator iter = iterable.iterator();iter.hasNext();)
		{
			long key = iter.nextLong();
			if(list.contains(key)) continue;
			list.add(key);
		}
		return new MinimalLongSet(list.toLongArray(new long[list.size()]));
	}
	
	protected MinimalLongSet(long[] contents) {
		super(contents);
	}
	
	@Override
	public LongSet copy() { throw new UnsupportedOperationException(); }
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof LongSet) {
			LongSet that = (LongSet) object;
			return (size() == that.size()) && this.containsAll(that);
		}
		return false;
	}

	@Override
	public int hashCode() {
		int hashCodeSum = 0;
		for (LongIterator iter = iterator();iter.hasNext();) {
			hashCodeSum += Long.hashCode(iter.nextLong());
		}
		return hashCodeSum;
	}

	@Override
	public boolean remove(long o) {
		for(LongIterator iter = iterator();iter.hasNext();) {
			if(iter.nextLong() == o) {
				iter.remove();
				return true;
			}
		}
		return false;
	}
}