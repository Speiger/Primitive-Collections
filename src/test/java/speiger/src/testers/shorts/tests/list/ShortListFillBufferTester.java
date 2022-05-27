package speiger.src.testers.shorts.tests.list;

import org.junit.Ignore;

import java.nio.ShortBuffer;

import speiger.src.collections.shorts.lists.ShortArrayList;
import speiger.src.testers.shorts.tests.base.AbstractShortListTester;

@Ignore
public class ShortListFillBufferTester extends AbstractShortListTester
{
	public void testFillBuffer() {
		ShortBuffer buffer = ShortBuffer.allocate(getNumElements());
		getList().fillBuffer(buffer);
		assertArrayEquals("Fill Buffer elements should equal", getSampleElements().toShortArray(), buffer.array());
	}

	private static void assertArrayEquals(String message, short[] expected, short[] actual) {
		assertEquals(message, ShortArrayList.wrap(expected), ShortArrayList.wrap(actual));
	}
}