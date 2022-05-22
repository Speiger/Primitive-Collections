package speiger.src.testers.shorts.tests.list;

import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.shorts.lists.ShortArrayList;
import speiger.src.testers.shorts.tests.base.AbstractShortListTester;

public class ShortListGetElementsTester extends AbstractShortListTester {
	@CollectionSize.Require(SEVERAL)
	public void testGetElements_valid() {
		short[] samples = getSampleElements(2).toShortArray();
		assertArrayEquals("getElements(from, result) should match", samples, getList().getElements(0, new short[2]));
	}
	
	@CollectionSize.Require(absent = ZERO)
	public void testGetElements_validSmallerArray() {
		short[] samples = getSampleElements(2).toShortArray();
		samples[1] = (short)0;
		assertArrayEquals("getElements(from, result) should match", samples, getList().getElements(0, new short[2], 0, 1));
	}
	
	public void testGetElements_outputToLarge() {
		try {
			getList().getElements(0, new short[10]);
			fail("getElements(from, output) should have thrown a IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
		}
	}
	
	public void testGetElements_offsetToSmall() {
		try {
			getList().getElements(0, new short[2], -1, 2);
			fail("getElements(from, output, offset, length) should have thrown a IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
			//success
		}
	}

	public void testGetElements_offsetToLarge() {
		try {
			getList().getElements(0, new short[2], 10, 2);
			fail("getElements(from, output, offset, length) should have thrown a IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
			//success
		}
	}
	
	public void testGetElements_lengthExceedingArray() {
		try {
			getList().getElements(0, new short[2], 0, 10);
			fail("getElements(from, output, offset, length) should have thrown a IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
			//success
		}
	}
	
	public void testGet_negative() {
		try {
			getList().getElements(-1, new short[2]);
			fail("getElements(-1) should throw");
		} catch (IndexOutOfBoundsException expected) {
		}
	}
	
	public void testGet_tooLarge() {
		try {
			getList().getElements(getNumElements(), new short[2]);
			fail("get(size) should throw");
		} catch (IndexOutOfBoundsException expected) {
		}
	}
	private static void assertArrayEquals(String message, short[] expected, short[] actual) {
		assertEquals(message, ShortArrayList.wrap(expected), ShortArrayList.wrap(actual));
	}
}