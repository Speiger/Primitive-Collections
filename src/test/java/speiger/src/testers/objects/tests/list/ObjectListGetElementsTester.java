package speiger.src.testers.objects.tests.list;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.testers.objects.tests.base.AbstractObjectListTester;

@Ignore
@SuppressWarnings("javadoc")
public class ObjectListGetElementsTester<T> extends AbstractObjectListTester<T>
{
	@CollectionSize.Require(SEVERAL)
	public void testGetElements_valid() {
		Object[] samples = getSampleElements(2).toArray();
		assertArrayEquals("getElements(from, result) should match", samples, getList().getElements(0, (T[])new Object[2]));
	}
	
	@CollectionSize.Require(absent = ZERO)
	public void testGetElements_validSmallerArray() {
		Object[] samples = getSampleElements(2).toArray();
		samples[1] = null;
		assertArrayEquals("getElements(from, result) should match", samples, getList().getElements(0, (T[])new Object[2], 0, 1));
	}
	
	public void testGetElements_outputToLarge() {
		try {
			getList().getElements(0, (T[])new Object[10]);
			fail("getElements(from, output) should have thrown a IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
		}
	}
	
	public void testGetElements_offsetToSmall() {
		try {
			getList().getElements(0, (T[])new Object[2], -1, 2);
			fail("getElements(from, output, offset, length) should have thrown a IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
			//success
		}
	}

	public void testGetElements_offsetToLarge() {
		try {
			getList().getElements(0, (T[])new Object[2], 10, 2);
			fail("getElements(from, output, offset, length) should have thrown a IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
			//success
		}
	}
	
	public void testGetElements_lengthExceedingArray() {
		try {
			getList().getElements(0, (T[])new Object[2], 0, 10);
			fail("getElements(from, output, offset, length) should have thrown a IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
			//success
		}
	}
	
	public void testGet_negative() {
		try {
			getList().getElements(-1, (T[])new Object[2]);
			fail("getElements(-1) should throw");
		} catch (IndexOutOfBoundsException expected) {
		}
	}
	
	public void testGet_tooLarge() {
		try {
			getList().getElements(getNumElements(), (T[])new Object[2]);
			fail("get(size) should throw");
		} catch (IndexOutOfBoundsException expected) {
		}
	}
	
	private static void assertArrayEquals(String message, Object[] expected, Object[] actual) {
		assertEquals(message, ObjectArrayList.wrap(expected), ObjectArrayList.wrap(actual));
	}
}