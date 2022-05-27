package speiger.src.testers.ints.utils;


import speiger.src.collections.ints.collections.IntIterable;
import speiger.src.collections.ints.collections.IntIterator;
import speiger.src.collections.ints.lists.IntArrayList;
import speiger.src.collections.ints.lists.IntList;
import speiger.src.collections.ints.sets.IntSet;

@SuppressWarnings("javadoc")
public class MinimalIntSet extends MinimalIntCollection implements IntSet {
	public static MinimalIntSet of(int...array) {
		return MinimalIntSet.of(IntArrayList.wrap(array));
	}
	
	public static MinimalIntSet of(IntIterable iterable)
	{
		IntList list = new IntArrayList();
		for(IntIterator iter = iterable.iterator();iter.hasNext();)
		{
			int key = iter.nextInt();
			if(list.contains(key)) continue;
			list.add(key);
		}
		return new MinimalIntSet(list.toIntArray(new int[list.size()]));
	}
	
	protected MinimalIntSet(int[] contents) {
		super(contents);
	}
	
	@Override
	public IntSet copy() { throw new UnsupportedOperationException(); }
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof IntSet) {
			IntSet that = (IntSet) object;
			return (size() == that.size()) && this.containsAll(that);
		}
		return false;
	}

	@Override
	public int hashCode() {
		int hashCodeSum = 0;
		for (IntIterator iter = iterator();iter.hasNext();) {
			hashCodeSum += Integer.hashCode(iter.nextInt());
		}
		return hashCodeSum;
	}

	@Override
	public boolean remove(int o) {
		for(IntIterator iter = iterator();iter.hasNext();) {
			if(iter.nextInt() == o) {
				iter.remove();
				return true;
			}
		}
		return false;
	}
}