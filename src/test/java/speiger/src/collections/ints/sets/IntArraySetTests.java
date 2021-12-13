package speiger.src.collections.ints.sets;

import java.util.EnumSet;

import speiger.src.collections.ints.base.BaseIntSortedSetTest;
import speiger.src.collections.tests.SortedSetTest;

@SuppressWarnings("javadoc")
public class IntArraySetTests extends BaseIntSortedSetTest
{
	@Override
	protected EnumSet<SortedSetTest> getValidSortedSetTests() { return EnumSet.complementOf(EnumSet.of(SortedSetTest.SUB_SET, SortedSetTest.HEAD_SET, SortedSetTest.TAIL_SET)); }
	@Override
	protected IntSortedSet create(int[] data) { return new IntArraySet(data.clone()); }
}
