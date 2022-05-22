package speiger.src.testers.floats.tests.list;

import org.junit.Ignore;

import java.nio.FloatBuffer;

import speiger.src.collections.floats.lists.FloatArrayList;
import speiger.src.testers.floats.tests.base.AbstractFloatListTester;

@Ignore
public class FloatListFillBufferTester extends AbstractFloatListTester {

	public void testFillBuffer() {
		FloatBuffer buffer = FloatBuffer.allocate(getNumElements());
		getList().fillBuffer(buffer);
		assertArrayEquals("Fill Buffer elements should equal", getSampleElements().toFloatArray(), buffer.array());
	}

	private static void assertArrayEquals(String message, float[] expected, float[] actual) {
		assertEquals(message, FloatArrayList.wrap(expected), FloatArrayList.wrap(actual));
	}
}