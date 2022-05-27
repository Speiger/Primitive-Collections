package speiger.src.testers.objects.tests.list;

import org.junit.Ignore;

import speiger.src.testers.objects.tests.base.AbstractObjectListTester;

@Ignore
public class ObjectListGetTester<T> extends AbstractObjectListTester<T>
{
	public void testGet_valid() {
		expectContents(createOrderedArray());
	}
	
	public void testGet_validList() {
		expectContents(getOrderedElements());
	}

	public void testGet_negative() {
		try {
			getList().get(-1);
			fail("get(-1) should throw");
		} catch (IndexOutOfBoundsException expected) {
		}
	}

	public void testGet_tooLarge() {
		try {
			getList().get(getNumElements());
			fail("get(size) should throw");
		} catch (IndexOutOfBoundsException expected) {
		}
	}
	
}