package speiger.src.testers.floats.utils;


import speiger.src.collections.floats.collections.FloatIterable;
import speiger.src.collections.floats.collections.FloatIterator;
import speiger.src.collections.floats.lists.FloatArrayList;
import speiger.src.collections.floats.lists.FloatList;
import speiger.src.collections.floats.sets.FloatSet;

public class MinimalFloatSet extends MinimalFloatCollection implements FloatSet {
	public static MinimalFloatSet of(float...array) {
		return MinimalFloatSet.of(FloatArrayList.wrap(array));
	}
	
	public static MinimalFloatSet of(FloatIterable iterable)
	{
		FloatList list = new FloatArrayList();
		for(FloatIterator iter = iterable.iterator();iter.hasNext();)
		{
			float key = iter.nextFloat();
			if(list.contains(key)) continue;
			list.add(key);
		}
		return new MinimalFloatSet(list.toFloatArray(new float[list.size()]));
	}
	
	protected MinimalFloatSet(float[] contents) {
		super(contents);
	}
	
	@Override
	public FloatSet copy() { throw new UnsupportedOperationException(); }
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof FloatSet) {
			FloatSet that = (FloatSet) object;
			return (size() == that.size()) && this.containsAll(that);
		}
		return false;
	}

	@Override
	public int hashCode() {
		int hashCodeSum = 0;
		for (FloatIterator iter = iterator();iter.hasNext();) {
			hashCodeSum += Float.hashCode(iter.nextFloat());
		}
		return hashCodeSum;
	}

	@Override
	public boolean remove(float o) {
		for(FloatIterator iter = iterator();iter.hasNext();) {
			if(Float.floatToIntBits(iter.nextFloat()) == Float.floatToIntBits(o)) {
				iter.remove();
				return true;
			}
		}
		return false;
	}
}