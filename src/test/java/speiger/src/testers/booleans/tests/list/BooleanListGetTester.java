package speiger.src.testers.booleans.tests.list;

import org.junit.Ignore;

import speiger.src.testers.booleans.tests.base.AbstractBooleanListTester;

@Ignore
public class BooleanListGetTester extends AbstractBooleanListTester
{
	public void testGet_valid() {
		expectContents(createOrderedArray());
	}
	
	public void testGet_validList() {
		expectContents(getOrderedElements());
	}

	public void testGet_negative() {
		try {
			getList().getBoolean(-1);
			fail("get(-1) should throw");
		} catch (IndexOutOfBoundsException expected) {
		}
	}

	public void testGet_tooLarge() {
		try {
			getList().getBoolean(getNumElements());
			fail("get(size) should throw");
		} catch (IndexOutOfBoundsException expected) {
		}
	}
	
}