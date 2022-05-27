package speiger.src.testers.floats.tests.list;

import org.junit.Ignore;

import speiger.src.testers.floats.tests.base.AbstractFloatListTester;

@Ignore
@SuppressWarnings("javadoc")
public class FloatListGetTester extends AbstractFloatListTester
{
	public void testGet_valid() {
		expectContents(createOrderedArray());
	}
	
	public void testGet_validList() {
		expectContents(getOrderedElements());
	}

	public void testGet_negative() {
		try {
			getList().getFloat(-1);
			fail("get(-1) should throw");
		} catch (IndexOutOfBoundsException expected) {
		}
	}

	public void testGet_tooLarge() {
		try {
			getList().getFloat(getNumElements());
			fail("get(size) should throw");
		} catch (IndexOutOfBoundsException expected) {
		}
	}
	
}