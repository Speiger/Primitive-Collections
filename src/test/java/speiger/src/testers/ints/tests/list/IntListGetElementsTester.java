package speiger.src.testers.ints.tests.list;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.ints.lists.IntArrayList;
import speiger.src.testers.ints.tests.base.AbstractIntListTester;

@Ignore
@SuppressWarnings("javadoc")
public class IntListGetElementsTester extends AbstractIntListTester
{
	@CollectionSize.Require(SEVERAL)
	public void testGetElements_valid() {
		int[] samples = getSampleElements(2).toIntArray();
		assertArrayEquals("getElements(from, result) should match", samples, getList().getElements(0, new int[2]));
	}
	
	@CollectionSize.Require(absent = ZERO)
	public void testGetElements_validSmallerArray() {
		int[] samples = getSampleElements(2).toIntArray();
		samples[1] = 0;
		assertArrayEquals("getElements(from, result) should match", samples, getList().getElements(0, new int[2], 0, 1));
	}
	
	public void testGetElements_outputToLarge() {
		try {
			getList().getElements(0, new int[10]);
			fail("getElements(from, output) should have thrown a IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
		}
	}
	
	public void testGetElements_offsetToSmall() {
		try {
			getList().getElements(0, new int[2], -1, 2);
			fail("getElements(from, output, offset, length) should have thrown a IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
			//success
		}
	}

	public void testGetElements_offsetToLarge() {
		try {
			getList().getElements(0, new int[2], 10, 2);
			fail("getElements(from, output, offset, length) should have thrown a IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
			//success
		}
	}
	
	public void testGetElements_lengthExceedingArray() {
		try {
			getList().getElements(0, new int[2], 0, 10);
			fail("getElements(from, output, offset, length) should have thrown a IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
			//success
		}
	}
	
	public void testGet_negative() {
		try {
			getList().getElements(-1, new int[2]);
			fail("getElements(-1) should throw");
		} catch (IndexOutOfBoundsException expected) {
		}
	}
	
	public void testGet_tooLarge() {
		try {
			getList().getElements(getNumElements(), new int[2]);
			fail("get(size) should throw");
		} catch (IndexOutOfBoundsException expected) {
		}
	}
	
	private static void assertArrayEquals(String message, int[] expected, int[] actual) {
		assertEquals(message, IntArrayList.wrap(expected), IntArrayList.wrap(actual));
	}
}