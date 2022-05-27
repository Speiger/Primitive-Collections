package speiger.src.testers.doubles.utils;


import speiger.src.collections.doubles.collections.DoubleIterable;
import speiger.src.collections.doubles.collections.DoubleIterator;
import speiger.src.collections.doubles.lists.DoubleArrayList;
import speiger.src.collections.doubles.lists.DoubleList;
import speiger.src.collections.doubles.sets.DoubleSet;

public class MinimalDoubleSet extends MinimalDoubleCollection implements DoubleSet {
	public static MinimalDoubleSet of(double...array) {
		return MinimalDoubleSet.of(DoubleArrayList.wrap(array));
	}
	
	public static MinimalDoubleSet of(DoubleIterable iterable)
	{
		DoubleList list = new DoubleArrayList();
		for(DoubleIterator iter = iterable.iterator();iter.hasNext();)
		{
			double key = iter.nextDouble();
			if(list.contains(key)) continue;
			list.add(key);
		}
		return new MinimalDoubleSet(list.toDoubleArray(new double[list.size()]));
	}
	
	protected MinimalDoubleSet(double[] contents) {
		super(contents);
	}
	
	@Override
	public DoubleSet copy() { throw new UnsupportedOperationException(); }
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof DoubleSet) {
			DoubleSet that = (DoubleSet) object;
			return (size() == that.size()) && this.containsAll(that);
		}
		return false;
	}

	@Override
	public int hashCode() {
		int hashCodeSum = 0;
		for (DoubleIterator iter = iterator();iter.hasNext();) {
			hashCodeSum += Double.hashCode(iter.nextDouble());
		}
		return hashCodeSum;
	}

	@Override
	public boolean remove(double o) {
		for(DoubleIterator iter = iterator();iter.hasNext();) {
			if(Double.doubleToLongBits(iter.nextDouble()) == Double.doubleToLongBits(o)) {
				iter.remove();
				return true;
			}
		}
		return false;
	}
}