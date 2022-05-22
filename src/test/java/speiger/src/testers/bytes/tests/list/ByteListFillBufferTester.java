package speiger.src.testers.bytes.tests.list;

import org.junit.Ignore;

import java.nio.ByteBuffer;

import speiger.src.collections.bytes.lists.ByteArrayList;
import speiger.src.testers.bytes.tests.base.AbstractByteListTester;

@Ignore
public class ByteListFillBufferTester extends AbstractByteListTester {

	public void testFillBuffer() {
		ByteBuffer buffer = ByteBuffer.allocate(getNumElements());
		getList().fillBuffer(buffer);
		assertArrayEquals("Fill Buffer elements should equal", getSampleElements().toByteArray(), buffer.array());
	}

	private static void assertArrayEquals(String message, byte[] expected, byte[] actual) {
		assertEquals(message, ByteArrayList.wrap(expected), ByteArrayList.wrap(actual));
	}
}