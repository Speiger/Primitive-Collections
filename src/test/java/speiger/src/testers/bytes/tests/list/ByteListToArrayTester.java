package speiger.src.testers.bytes.tests.list;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.bytes.lists.ByteArrayList;
import speiger.src.testers.bytes.tests.base.AbstractByteListTester;

@Ignore
public class ByteListToArrayTester extends AbstractByteListTester {
	public void testToArray_noArg() {
		byte[] actual = getList().toByteArray();
		assertArrayEquals("toArray() order should match list", createOrderedArray(), actual);
	}

	@CollectionSize.Require(absent = ZERO)
	public void testToArray_tooSmall() {
		byte[] actual = getList().toByteArray(new byte[0]);
		assertArrayEquals("toArray(tooSmall) order should match list", createOrderedArray(), actual);
	}

	public void testToArray_largeEnough() {
		byte[] actual = getList().toByteArray(new byte[getNumElements()]);
		assertArrayEquals("toArray(largeEnough) order should match list", createOrderedArray(), actual);
	}

	private static void assertArrayEquals(String message, byte[] expected, byte[] actual) {
		assertEquals(message, ByteArrayList.wrap(expected), ByteArrayList.wrap(actual));
	}
}