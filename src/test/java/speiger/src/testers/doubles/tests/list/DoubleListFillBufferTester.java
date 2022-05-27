package speiger.src.testers.doubles.tests.list;

import org.junit.Ignore;

import java.nio.DoubleBuffer;

import speiger.src.collections.doubles.lists.DoubleArrayList;
import speiger.src.testers.doubles.tests.base.AbstractDoubleListTester;

@Ignore
@SuppressWarnings("javadoc")
public class DoubleListFillBufferTester extends AbstractDoubleListTester
{
	public void testFillBuffer() {
		DoubleBuffer buffer = DoubleBuffer.allocate(getNumElements());
		getList().fillBuffer(buffer);
		assertArrayEquals("Fill Buffer elements should equal", getSampleElements().toDoubleArray(), buffer.array());
	}

	private static void assertArrayEquals(String message, double[] expected, double[] actual) {
		assertEquals(message, DoubleArrayList.wrap(expected), DoubleArrayList.wrap(actual));
	}
}