package speiger.src.testers.shorts.utils;


import speiger.src.collections.shorts.collections.ShortIterable;
import speiger.src.collections.shorts.collections.ShortIterator;
import speiger.src.collections.shorts.lists.ShortArrayList;
import speiger.src.collections.shorts.lists.ShortList;
import speiger.src.collections.shorts.sets.ShortSet;

public class MinimalShortSet extends MinimalShortCollection implements ShortSet {
	public static MinimalShortSet of(short...array) {
		return MinimalShortSet.of(ShortArrayList.wrap(array));
	}
	
	public static MinimalShortSet of(ShortIterable iterable)
	{
		ShortList list = new ShortArrayList();
		for(ShortIterator iter = iterable.iterator();iter.hasNext();)
		{
			short key = iter.nextShort();
			if(list.contains(key)) continue;
			list.add(key);
		}
		return new MinimalShortSet(list.toShortArray(new short[list.size()]));
	}
	
	protected MinimalShortSet(short[] contents) {
		super(contents);
	}
	
	@Override
	public ShortSet copy() { throw new UnsupportedOperationException(); }
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof ShortSet) {
			ShortSet that = (ShortSet) object;
			return (size() == that.size()) && this.containsAll(that);
		}
		return false;
	}

	@Override
	public int hashCode() {
		int hashCodeSum = 0;
		for (ShortIterator iter = iterator();iter.hasNext();) {
			hashCodeSum += Short.hashCode(iter.nextShort());
		}
		return hashCodeSum;
	}

	@Override
	public boolean remove(short o) {
		for(ShortIterator iter = iterator();iter.hasNext();) {
			if(iter.nextShort() == o) {
				iter.remove();
				return true;
			}
		}
		return false;
	}
}