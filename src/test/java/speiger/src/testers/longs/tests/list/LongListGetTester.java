package speiger.src.testers.longs.tests.list;

import org.junit.Ignore;

import speiger.src.testers.longs.tests.base.AbstractLongListTester;

@Ignore
@SuppressWarnings("javadoc")
public class LongListGetTester extends AbstractLongListTester
{
	public void testGet_valid() {
		expectContents(createOrderedArray());
	}
	
	public void testGet_validList() {
		expectContents(getOrderedElements());
	}

	public void testGet_negative() {
		try {
			getList().getLong(-1);
			fail("get(-1) should throw");
		} catch (IndexOutOfBoundsException expected) {
		}
	}

	public void testGet_tooLarge() {
		try {
			getList().getLong(getNumElements());
			fail("get(size) should throw");
		} catch (IndexOutOfBoundsException expected) {
		}
	}
	
}