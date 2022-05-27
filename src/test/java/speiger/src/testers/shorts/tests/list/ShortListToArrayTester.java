package speiger.src.testers.shorts.tests.list;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.shorts.lists.ShortArrayList;
import speiger.src.testers.shorts.tests.base.AbstractShortListTester;

@Ignore
@SuppressWarnings("javadoc")
public class ShortListToArrayTester extends AbstractShortListTester
{
	public void testToArray_noArg() {
		short[] actual = getList().toShortArray();
		assertArrayEquals("toArray() order should match list", createOrderedArray(), actual);
	}

	@CollectionSize.Require(absent = ZERO)
	public void testToArray_tooSmall() {
		short[] actual = getList().toShortArray(new short[0]);
		assertArrayEquals("toArray(tooSmall) order should match list", createOrderedArray(), actual);
	}

	public void testToArray_largeEnough() {
		short[] actual = getList().toShortArray(new short[getNumElements()]);
		assertArrayEquals("toArray(largeEnough) order should match list", createOrderedArray(), actual);
	}

	private static void assertArrayEquals(String message, short[] expected, short[] actual) {
		assertEquals(message, ShortArrayList.wrap(expected), ShortArrayList.wrap(actual));
	}
}