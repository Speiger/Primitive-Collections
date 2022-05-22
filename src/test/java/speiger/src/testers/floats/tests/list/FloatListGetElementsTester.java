package speiger.src.testers.floats.tests.list;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.floats.lists.FloatArrayList;
import speiger.src.testers.floats.tests.base.AbstractFloatListTester;

@Ignore
public class FloatListGetElementsTester extends AbstractFloatListTester {
	@CollectionSize.Require(SEVERAL)
	public void testGetElements_valid() {
		float[] samples = getSampleElements(2).toFloatArray();
		assertArrayEquals("getElements(from, result) should match", samples, getList().getElements(0, new float[2]));
	}
	
	@CollectionSize.Require(absent = ZERO)
	public void testGetElements_validSmallerArray() {
		float[] samples = getSampleElements(2).toFloatArray();
		samples[1] = 0F;
		assertArrayEquals("getElements(from, result) should match", samples, getList().getElements(0, new float[2], 0, 1));
	}
	
	public void testGetElements_outputToLarge() {
		try {
			getList().getElements(0, new float[10]);
			fail("getElements(from, output) should have thrown a IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
		}
	}
	
	public void testGetElements_offsetToSmall() {
		try {
			getList().getElements(0, new float[2], -1, 2);
			fail("getElements(from, output, offset, length) should have thrown a IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
			//success
		}
	}

	public void testGetElements_offsetToLarge() {
		try {
			getList().getElements(0, new float[2], 10, 2);
			fail("getElements(from, output, offset, length) should have thrown a IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
			//success
		}
	}
	
	public void testGetElements_lengthExceedingArray() {
		try {
			getList().getElements(0, new float[2], 0, 10);
			fail("getElements(from, output, offset, length) should have thrown a IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
			//success
		}
	}
	
	public void testGet_negative() {
		try {
			getList().getElements(-1, new float[2]);
			fail("getElements(-1) should throw");
		} catch (IndexOutOfBoundsException expected) {
		}
	}
	
	public void testGet_tooLarge() {
		try {
			getList().getElements(getNumElements(), new float[2]);
			fail("get(size) should throw");
		} catch (IndexOutOfBoundsException expected) {
		}
	}
	private static void assertArrayEquals(String message, float[] expected, float[] actual) {
		assertEquals(message, FloatArrayList.wrap(expected), FloatArrayList.wrap(actual));
	}
}