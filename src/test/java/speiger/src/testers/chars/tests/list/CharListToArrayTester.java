package speiger.src.testers.chars.tests.list;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.chars.lists.CharArrayList;
import speiger.src.testers.chars.tests.base.AbstractCharListTester;

public class CharListToArrayTester extends AbstractCharListTester {
	public void testToArray_noArg() {
		char[] actual = getList().toCharArray();
		assertArrayEquals("toArray() order should match list", createOrderedArray(), actual);
	}

	@CollectionSize.Require(absent = ZERO)
	public void testToArray_tooSmall() {
		char[] actual = getList().toCharArray(new char[0]);
		assertArrayEquals("toArray(tooSmall) order should match list", createOrderedArray(), actual);
	}

	public void testToArray_largeEnough() {
		char[] actual = getList().toCharArray(new char[getNumElements()]);
		assertArrayEquals("toArray(largeEnough) order should match list", createOrderedArray(), actual);
	}

	private static void assertArrayEquals(String message, char[] expected, char[] actual) {
		assertEquals(message, CharArrayList.wrap(expected), CharArrayList.wrap(actual));
	}
}