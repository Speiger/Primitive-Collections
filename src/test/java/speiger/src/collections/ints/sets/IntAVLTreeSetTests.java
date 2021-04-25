package speiger.src.collections.ints.sets;

import java.util.EnumSet;

import speiger.src.collections.ints.base.BaseIntNavigableSetTest;
import speiger.src.collections.tests.SortedSetTest;

@SuppressWarnings("javadoc")
public class IntAVLTreeSetTests extends BaseIntNavigableSetTest
{
	@Override
	protected IntNavigableSet create(int[] data) {
		return new IntAVLTreeSet(data);
	}
	
	@Override
	protected EnumSet<SortedSetTest> getValidSortedSetTests() { return EnumSet.of(SortedSetTest.PEEK, SortedSetTest.POLL, SortedSetTest.HEAD_SET, SortedSetTest.SUB_SET, SortedSetTest.TAIL_SET);}
}
