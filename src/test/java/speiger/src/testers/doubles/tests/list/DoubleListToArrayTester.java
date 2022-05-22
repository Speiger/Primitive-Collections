package speiger.src.testers.doubles.tests.list;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.doubles.lists.DoubleArrayList;
import speiger.src.testers.doubles.tests.base.AbstractDoubleListTester;

@Ignore
public class DoubleListToArrayTester extends AbstractDoubleListTester {
	public void testToArray_noArg() {
		double[] actual = getList().toDoubleArray();
		assertArrayEquals("toArray() order should match list", createOrderedArray(), actual);
	}

	@CollectionSize.Require(absent = ZERO)
	public void testToArray_tooSmall() {
		double[] actual = getList().toDoubleArray(new double[0]);
		assertArrayEquals("toArray(tooSmall) order should match list", createOrderedArray(), actual);
	}

	public void testToArray_largeEnough() {
		double[] actual = getList().toDoubleArray(new double[getNumElements()]);
		assertArrayEquals("toArray(largeEnough) order should match list", createOrderedArray(), actual);
	}

	private static void assertArrayEquals(String message, double[] expected, double[] actual) {
		assertEquals(message, DoubleArrayList.wrap(expected), DoubleArrayList.wrap(actual));
	}
}