package speiger.src.testers.booleans.tests.list;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.booleans.lists.BooleanArrayList;
import speiger.src.testers.booleans.tests.base.AbstractBooleanListTester;

@Ignore
public class BooleanListGetElementsTester extends AbstractBooleanListTester {
	@CollectionSize.Require(SEVERAL)
	public void testGetElements_valid() {
		boolean[] samples = getSampleElements(2).toBooleanArray();
		assertArrayEquals("getElements(from, result) should match", samples, getList().getElements(0, new boolean[2]));
	}
	
	@CollectionSize.Require(absent = ZERO)
	public void testGetElements_validSmallerArray() {
		boolean[] samples = getSampleElements(2).toBooleanArray();
		samples[1] = false;
		assertArrayEquals("getElements(from, result) should match", samples, getList().getElements(0, new boolean[2], 0, 1));
	}
	
	public void testGetElements_outputToLarge() {
		try {
			getList().getElements(0, new boolean[10]);
			fail("getElements(from, output) should have thrown a IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
		}
	}
	
	public void testGetElements_offsetToSmall() {
		try {
			getList().getElements(0, new boolean[2], -1, 2);
			fail("getElements(from, output, offset, length) should have thrown a IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
			//success
		}
	}

	public void testGetElements_offsetToLarge() {
		try {
			getList().getElements(0, new boolean[2], 10, 2);
			fail("getElements(from, output, offset, length) should have thrown a IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
			//success
		}
	}
	
	public void testGetElements_lengthExceedingArray() {
		try {
			getList().getElements(0, new boolean[2], 0, 10);
			fail("getElements(from, output, offset, length) should have thrown a IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
			//success
		}
	}
	
	public void testGet_negative() {
		try {
			getList().getElements(-1, new boolean[2]);
			fail("getElements(-1) should throw");
		} catch (IndexOutOfBoundsException expected) {
		}
	}
	
	public void testGet_tooLarge() {
		try {
			getList().getElements(getNumElements(), new boolean[2]);
			fail("get(size) should throw");
		} catch (IndexOutOfBoundsException expected) {
		}
	}
	private static void assertArrayEquals(String message, boolean[] expected, boolean[] actual) {
		assertEquals(message, BooleanArrayList.wrap(expected), BooleanArrayList.wrap(actual));
	}
}