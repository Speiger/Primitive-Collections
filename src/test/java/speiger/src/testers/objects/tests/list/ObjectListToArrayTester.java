package speiger.src.testers.objects.tests.list;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.testers.objects.tests.base.AbstractObjectListTester;

@Ignore
public class ObjectListToArrayTester<T> extends AbstractObjectListTester<T>
{
	public void testToArray_noArg() {
		Object[] actual = getList().toArray();
		assertArrayEquals("toArray() order should match list", createOrderedArray(), actual);
	}

	@CollectionSize.Require(absent = ZERO)
	public void testToArray_tooSmall() {
		T[] actual = getList().toArray((T[])new Object[0]);
		assertArrayEquals("toArray(tooSmall) order should match list", createOrderedArray(), actual);
	}

	public void testToArray_largeEnough() {
		T[] actual = getList().toArray((T[])new Object[getNumElements()]);
		assertArrayEquals("toArray(largeEnough) order should match list", createOrderedArray(), actual);
	}

	private static void assertArrayEquals(String message, Object[] expected, Object[] actual) {
		assertEquals(message, ObjectArrayList.wrap(expected), ObjectArrayList.wrap(actual));
	}
}