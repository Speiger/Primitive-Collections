package speiger.src.testers.booleans.tests.list;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.booleans.lists.BooleanArrayList;
import speiger.src.testers.booleans.tests.base.AbstractBooleanListTester;

public class BooleanListToArrayTester extends AbstractBooleanListTester {
	public void testToArray_noArg() {
		boolean[] actual = getList().toBooleanArray();
		assertArrayEquals("toArray() order should match list", createOrderedArray(), actual);
	}

	@CollectionSize.Require(absent = ZERO)
	public void testToArray_tooSmall() {
		boolean[] actual = getList().toBooleanArray(new boolean[0]);
		assertArrayEquals("toArray(tooSmall) order should match list", createOrderedArray(), actual);
	}

	public void testToArray_largeEnough() {
		boolean[] actual = getList().toBooleanArray(new boolean[getNumElements()]);
		assertArrayEquals("toArray(largeEnough) order should match list", createOrderedArray(), actual);
	}

	private static void assertArrayEquals(String message, boolean[] expected, boolean[] actual) {
		assertEquals(message, BooleanArrayList.wrap(expected), BooleanArrayList.wrap(actual));
	}
}