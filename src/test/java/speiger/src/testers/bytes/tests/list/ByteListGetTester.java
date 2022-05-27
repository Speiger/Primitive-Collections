package speiger.src.testers.bytes.tests.list;

import org.junit.Ignore;

import speiger.src.testers.bytes.tests.base.AbstractByteListTester;

@Ignore
public class ByteListGetTester extends AbstractByteListTester
{
	public void testGet_valid() {
		expectContents(createOrderedArray());
	}
	
	public void testGet_validList() {
		expectContents(getOrderedElements());
	}

	public void testGet_negative() {
		try {
			getList().getByte(-1);
			fail("get(-1) should throw");
		} catch (IndexOutOfBoundsException expected) {
		}
	}

	public void testGet_tooLarge() {
		try {
			getList().getByte(getNumElements());
			fail("get(size) should throw");
		} catch (IndexOutOfBoundsException expected) {
		}
	}
	
}