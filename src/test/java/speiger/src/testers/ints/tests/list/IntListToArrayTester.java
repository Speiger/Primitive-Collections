package speiger.src.testers.ints.tests.list;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.ints.lists.IntArrayList;
import speiger.src.testers.ints.tests.base.AbstractIntListTester;

@Ignore
public class IntListToArrayTester extends AbstractIntListTester
{
	public void testToArray_noArg() {
		int[] actual = getList().toIntArray();
		assertArrayEquals("toArray() order should match list", createOrderedArray(), actual);
	}

	@CollectionSize.Require(absent = ZERO)
	public void testToArray_tooSmall() {
		int[] actual = getList().toIntArray(new int[0]);
		assertArrayEquals("toArray(tooSmall) order should match list", createOrderedArray(), actual);
	}

	public void testToArray_largeEnough() {
		int[] actual = getList().toIntArray(new int[getNumElements()]);
		assertArrayEquals("toArray(largeEnough) order should match list", createOrderedArray(), actual);
	}

	private static void assertArrayEquals(String message, int[] expected, int[] actual) {
		assertEquals(message, IntArrayList.wrap(expected), IntArrayList.wrap(actual));
	}
}