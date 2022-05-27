package speiger.src.testers.booleans.utils;


import speiger.src.collections.booleans.collections.BooleanIterable;
import speiger.src.collections.booleans.collections.BooleanIterator;
import speiger.src.collections.booleans.lists.BooleanArrayList;
import speiger.src.collections.booleans.lists.BooleanList;
import speiger.src.collections.booleans.sets.BooleanSet;

public class MinimalBooleanSet extends MinimalBooleanCollection implements BooleanSet {
	public static MinimalBooleanSet of(boolean...array) {
		return MinimalBooleanSet.of(BooleanArrayList.wrap(array));
	}
	
	public static MinimalBooleanSet of(BooleanIterable iterable)
	{
		BooleanList list = new BooleanArrayList();
		for(BooleanIterator iter = iterable.iterator();iter.hasNext();)
		{
			boolean key = iter.nextBoolean();
			if(list.contains(key)) continue;
			list.add(key);
		}
		return new MinimalBooleanSet(list.toBooleanArray(new boolean[list.size()]));
	}
	
	protected MinimalBooleanSet(boolean[] contents) {
		super(contents);
	}
	
	@Override
	public BooleanSet copy() { throw new UnsupportedOperationException(); }
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof BooleanSet) {
			BooleanSet that = (BooleanSet) object;
			return (size() == that.size()) && this.containsAll(that);
		}
		return false;
	}

	@Override
	public int hashCode() {
		int hashCodeSum = 0;
		for (BooleanIterator iter = iterator();iter.hasNext();) {
			hashCodeSum += Boolean.hashCode(iter.nextBoolean());
		}
		return hashCodeSum;
	}

	@Override
	public boolean remove(boolean o) {
		for(BooleanIterator iter = iterator();iter.hasNext();) {
			if(iter.nextBoolean() == o) {
				iter.remove();
				return true;
			}
		}
		return false;
	}
}