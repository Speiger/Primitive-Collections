package speiger.src.testers.ints.tests.list;

import org.junit.Ignore;

import speiger.src.testers.ints.tests.base.AbstractIntListTester;

@Ignore
public class IntListGetTester extends AbstractIntListTester {
	public void testGet_valid() {
		expectContents(createOrderedArray());
	}
	
	public void testGet_validList() {
		expectContents(getOrderedElements());
	}

	public void testGet_negative() {
		try {
			getList().getInt(-1);
			fail("get(-1) should throw");
		} catch (IndexOutOfBoundsException expected) {
		}
	}

	public void testGet_tooLarge() {
		try {
			getList().getInt(getNumElements());
			fail("get(size) should throw");
		} catch (IndexOutOfBoundsException expected) {
		}
	}
	
}