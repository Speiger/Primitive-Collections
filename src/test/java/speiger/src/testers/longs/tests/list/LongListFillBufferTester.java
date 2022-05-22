package speiger.src.testers.longs.tests.list;

import org.junit.Ignore;

import java.nio.LongBuffer;

import speiger.src.collections.longs.lists.LongArrayList;
import speiger.src.testers.longs.tests.base.AbstractLongListTester;

@Ignore
public class LongListFillBufferTester extends AbstractLongListTester {

	public void testFillBuffer() {
		LongBuffer buffer = LongBuffer.allocate(getNumElements());
		getList().fillBuffer(buffer);
		assertArrayEquals("Fill Buffer elements should equal", getSampleElements().toLongArray(), buffer.array());
	}

	private static void assertArrayEquals(String message, long[] expected, long[] actual) {
		assertEquals(message, LongArrayList.wrap(expected), LongArrayList.wrap(actual));
	}
}