package speiger.src.collections.ints.sets;

import speiger.src.collections.ints.base.BaseIntSortedSetTest;

@SuppressWarnings("javadoc")
public class IntArraySetTests extends BaseIntSortedSetTest
{
	@Override
	protected IntSortedSet create(int[] data) { return new IntArraySet(data.clone()); }
}
