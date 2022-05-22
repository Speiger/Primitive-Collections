package speiger.src.testers.bytes.tests.list;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.bytes.lists.ByteArrayList;
import speiger.src.testers.bytes.tests.base.AbstractByteListTester;

@Ignore
public class ByteListGetElementsTester extends AbstractByteListTester {
	@CollectionSize.Require(SEVERAL)
	public void testGetElements_valid() {
		byte[] samples = getSampleElements(2).toByteArray();
		assertArrayEquals("getElements(from, result) should match", samples, getList().getElements(0, new byte[2]));
	}
	
	@CollectionSize.Require(absent = ZERO)
	public void testGetElements_validSmallerArray() {
		byte[] samples = getSampleElements(2).toByteArray();
		samples[1] = (byte)0;
		assertArrayEquals("getElements(from, result) should match", samples, getList().getElements(0, new byte[2], 0, 1));
	}
	
	public void testGetElements_outputToLarge() {
		try {
			getList().getElements(0, new byte[10]);
			fail("getElements(from, output) should have thrown a IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
		}
	}
	
	public void testGetElements_offsetToSmall() {
		try {
			getList().getElements(0, new byte[2], -1, 2);
			fail("getElements(from, output, offset, length) should have thrown a IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
			//success
		}
	}

	public void testGetElements_offsetToLarge() {
		try {
			getList().getElements(0, new byte[2], 10, 2);
			fail("getElements(from, output, offset, length) should have thrown a IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
			//success
		}
	}
	
	public void testGetElements_lengthExceedingArray() {
		try {
			getList().getElements(0, new byte[2], 0, 10);
			fail("getElements(from, output, offset, length) should have thrown a IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
			//success
		}
	}
	
	public void testGet_negative() {
		try {
			getList().getElements(-1, new byte[2]);
			fail("getElements(-1) should throw");
		} catch (IndexOutOfBoundsException expected) {
		}
	}
	
	public void testGet_tooLarge() {
		try {
			getList().getElements(getNumElements(), new byte[2]);
			fail("get(size) should throw");
		} catch (IndexOutOfBoundsException expected) {
		}
	}
	private static void assertArrayEquals(String message, byte[] expected, byte[] actual) {
		assertEquals(message, ByteArrayList.wrap(expected), ByteArrayList.wrap(actual));
	}
}