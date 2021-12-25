package speiger.src.collections.ints.sets;

import speiger.src.collections.ints.base.BaseIntOrderedSetTest;

@SuppressWarnings("javadoc")
public class IntArraySetTests extends BaseIntOrderedSetTest
{
	@Override
	protected IntOrderedSet create(int[] data) { return new IntArraySet(data.clone()); }
}
