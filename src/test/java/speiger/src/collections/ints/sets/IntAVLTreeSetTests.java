package speiger.src.collections.ints.sets;

import speiger.src.collections.ints.base.BaseIntNavigableSetTest;

@SuppressWarnings("javadoc")
public class IntAVLTreeSetTests extends BaseIntNavigableSetTest
{
	@Override
	protected IntNavigableSet create(int[] data) {
		return new IntAVLTreeSet(data);
	}
}
