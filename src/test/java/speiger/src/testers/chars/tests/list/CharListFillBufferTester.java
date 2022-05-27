package speiger.src.testers.chars.tests.list;

import org.junit.Ignore;

import java.nio.CharBuffer;

import speiger.src.collections.chars.lists.CharArrayList;
import speiger.src.testers.chars.tests.base.AbstractCharListTester;

@Ignore
public class CharListFillBufferTester extends AbstractCharListTester
{
	public void testFillBuffer() {
		CharBuffer buffer = CharBuffer.allocate(getNumElements());
		getList().fillBuffer(buffer);
		assertArrayEquals("Fill Buffer elements should equal", getSampleElements().toCharArray(), buffer.array());
	}

	private static void assertArrayEquals(String message, char[] expected, char[] actual) {
		assertEquals(message, CharArrayList.wrap(expected), CharArrayList.wrap(actual));
	}
}