package speiger.src.testers.doubles.tests.list;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.doubles.lists.DoubleArrayList;
import speiger.src.testers.doubles.tests.base.AbstractDoubleListTester;

@Ignore
public class DoubleListGetElementsTester extends AbstractDoubleListTester {
	@CollectionSize.Require(SEVERAL)
	public void testGetElements_valid() {
		double[] samples = getSampleElements(2).toDoubleArray();
		assertArrayEquals("getElements(from, result) should match", samples, getList().getElements(0, new double[2]));
	}
	
	@CollectionSize.Require(absent = ZERO)
	public void testGetElements_validSmallerArray() {
		double[] samples = getSampleElements(2).toDoubleArray();
		samples[1] = 0D;
		assertArrayEquals("getElements(from, result) should match", samples, getList().getElements(0, new double[2], 0, 1));
	}
	
	public void testGetElements_outputToLarge() {
		try {
			getList().getElements(0, new double[10]);
			fail("getElements(from, output) should have thrown a IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
		}
	}
	
	public void testGetElements_offsetToSmall() {
		try {
			getList().getElements(0, new double[2], -1, 2);
			fail("getElements(from, output, offset, length) should have thrown a IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
			//success
		}
	}

	public void testGetElements_offsetToLarge() {
		try {
			getList().getElements(0, new double[2], 10, 2);
			fail("getElements(from, output, offset, length) should have thrown a IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
			//success
		}
	}
	
	public void testGetElements_lengthExceedingArray() {
		try {
			getList().getElements(0, new double[2], 0, 10);
			fail("getElements(from, output, offset, length) should have thrown a IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
			//success
		}
	}
	
	public void testGet_negative() {
		try {
			getList().getElements(-1, new double[2]);
			fail("getElements(-1) should throw");
		} catch (IndexOutOfBoundsException expected) {
		}
	}
	
	public void testGet_tooLarge() {
		try {
			getList().getElements(getNumElements(), new double[2]);
			fail("get(size) should throw");
		} catch (IndexOutOfBoundsException expected) {
		}
	}
	private static void assertArrayEquals(String message, double[] expected, double[] actual) {
		assertEquals(message, DoubleArrayList.wrap(expected), DoubleArrayList.wrap(actual));
	}
}