package speiger.src.testers.chars.tests.list;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.chars.lists.CharArrayList;
import speiger.src.testers.chars.tests.base.AbstractCharListTester;

@Ignore
@SuppressWarnings("javadoc")
public class CharListGetElementsTester extends AbstractCharListTester
{
	@CollectionSize.Require(SEVERAL)
	public void testGetElements_valid() {
		char[] samples = getSampleElements(2).toCharArray();
		assertArrayEquals("getElements(from, result) should match", samples, getList().getElements(0, new char[2]));
	}
	
	@CollectionSize.Require(absent = ZERO)
	public void testGetElements_validSmallerArray() {
		char[] samples = getSampleElements(2).toCharArray();
		samples[1] = (char)0;
		assertArrayEquals("getElements(from, result) should match", samples, getList().getElements(0, new char[2], 0, 1));
	}
	
	public void testGetElements_outputToLarge() {
		try {
			getList().getElements(0, new char[10]);
			fail("getElements(from, output) should have thrown a IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
		}
	}
	
	public void testGetElements_offsetToSmall() {
		try {
			getList().getElements(0, new char[2], -1, 2);
			fail("getElements(from, output, offset, length) should have thrown a IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
			//success
		}
	}

	public void testGetElements_offsetToLarge() {
		try {
			getList().getElements(0, new char[2], 10, 2);
			fail("getElements(from, output, offset, length) should have thrown a IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
			//success
		}
	}
	
	public void testGetElements_lengthExceedingArray() {
		try {
			getList().getElements(0, new char[2], 0, 10);
			fail("getElements(from, output, offset, length) should have thrown a IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
			//success
		}
	}
	
	public void testGet_negative() {
		try {
			getList().getElements(-1, new char[2]);
			fail("getElements(-1) should throw");
		} catch (IndexOutOfBoundsException expected) {
		}
	}
	
	public void testGet_tooLarge() {
		try {
			getList().getElements(getNumElements(), new char[2]);
			fail("get(size) should throw");
		} catch (IndexOutOfBoundsException expected) {
		}
	}
	
	private static void assertArrayEquals(String message, char[] expected, char[] actual) {
		assertEquals(message, CharArrayList.wrap(expected), CharArrayList.wrap(actual));
	}
}