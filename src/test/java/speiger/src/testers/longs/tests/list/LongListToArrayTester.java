package speiger.src.testers.longs.tests.list;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.longs.lists.LongArrayList;
import speiger.src.testers.longs.tests.base.AbstractLongListTester;

public class LongListToArrayTester extends AbstractLongListTester {
	public void testToArray_noArg() {
		long[] actual = getList().toLongArray();
		assertArrayEquals("toArray() order should match list", createOrderedArray(), actual);
	}

	@CollectionSize.Require(absent = ZERO)
	public void testToArray_tooSmall() {
		long[] actual = getList().toLongArray(new long[0]);
		assertArrayEquals("toArray(tooSmall) order should match list", createOrderedArray(), actual);
	}

	public void testToArray_largeEnough() {
		long[] actual = getList().toLongArray(new long[getNumElements()]);
		assertArrayEquals("toArray(largeEnough) order should match list", createOrderedArray(), actual);
	}

	private static void assertArrayEquals(String message, long[] expected, long[] actual) {
		assertEquals(message, LongArrayList.wrap(expected), LongArrayList.wrap(actual));
	}
}