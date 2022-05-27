package speiger.src.testers.shorts.tests.list;

import org.junit.Ignore;

import speiger.src.testers.shorts.tests.base.AbstractShortListTester;

@Ignore
@SuppressWarnings("javadoc")
public class ShortListGetTester extends AbstractShortListTester
{
	public void testGet_valid() {
		expectContents(createOrderedArray());
	}
	
	public void testGet_validList() {
		expectContents(getOrderedElements());
	}

	public void testGet_negative() {
		try {
			getList().getShort(-1);
			fail("get(-1) should throw");
		} catch (IndexOutOfBoundsException expected) {
		}
	}

	public void testGet_tooLarge() {
		try {
			getList().getShort(getNumElements());
			fail("get(size) should throw");
		} catch (IndexOutOfBoundsException expected) {
		}
	}
	
}