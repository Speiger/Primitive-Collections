package speiger.src.testers.chars.tests.list;

import org.junit.Ignore;

import speiger.src.testers.chars.tests.base.AbstractCharListTester;

@Ignore
public class CharListGetTester extends AbstractCharListTester
{
	public void testGet_valid() {
		expectContents(createOrderedArray());
	}
	
	public void testGet_validList() {
		expectContents(getOrderedElements());
	}

	public void testGet_negative() {
		try {
			getList().getChar(-1);
			fail("get(-1) should throw");
		} catch (IndexOutOfBoundsException expected) {
		}
	}

	public void testGet_tooLarge() {
		try {
			getList().getChar(getNumElements());
			fail("get(size) should throw");
		} catch (IndexOutOfBoundsException expected) {
		}
	}
	
}