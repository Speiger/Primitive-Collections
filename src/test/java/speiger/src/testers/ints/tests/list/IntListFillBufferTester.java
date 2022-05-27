package speiger.src.testers.ints.tests.list;

import org.junit.Ignore;

import java.nio.IntBuffer;

import speiger.src.collections.ints.lists.IntArrayList;
import speiger.src.testers.ints.tests.base.AbstractIntListTester;

@Ignore
public class IntListFillBufferTester extends AbstractIntListTester
{
	public void testFillBuffer() {
		IntBuffer buffer = IntBuffer.allocate(getNumElements());
		getList().fillBuffer(buffer);
		assertArrayEquals("Fill Buffer elements should equal", getSampleElements().toIntArray(), buffer.array());
	}

	private static void assertArrayEquals(String message, int[] expected, int[] actual) {
		assertEquals(message, IntArrayList.wrap(expected), IntArrayList.wrap(actual));
	}
}