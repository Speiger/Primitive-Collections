package speiger.src.testers.objects.tests.collection;

import static com.google.common.collect.testing.features.CollectionFeature.KNOWN_ORDER;

import com.google.common.collect.testing.features.CollectionFeature;

import org.junit.Ignore;

import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.tests.base.AbstractObjectCollectionTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class ObjectCollectionToArrayTester<T> extends AbstractObjectCollectionTester<T>
{
	public void testToArray_noArgs() {
		Object[] array = collection.toArray();
		expectArrayContentsAnyOrder(createSamplesArray(), array);
	}

	public void testToArray_emptyArray() {
		T[] empty = emptyArray();
		T[] array = collection.toArray(empty);
		assertEquals("toLongArray(emptyT[]).length:", getNumElements(), array.length);
		expectArrayContentsAnyOrder(createSamplesArray(), array);
	}

	@CollectionFeature.Require(KNOWN_ORDER)
	public void testToArray_emptyArray_ordered() {
		T[] empty = emptyArray();
		T[] array = collection.toArray(empty);
		assertEquals("toLongArray(emptyT[]).length:", getNumElements(), array.length);
		expectArrayContentsInOrder(getOrderedElements(), array);
	}

	public void testToArray_emptyArrayOfObject() {
		T[] in = emptyArray();
		T[] array = collection.toArray(in);
		assertEquals("toLongArray(emptyObject[]).length", getNumElements(), array.length);
		expectArrayContentsAnyOrder(createSamplesArray(), array);
	}

	public void testToArray_rightSizedArray() {
		T[] array = (T[])new Object[getNumElements()];
		assertSame("toLongArray(sameSizeE[]) should return the given array", array, collection.toArray(array));
		expectArrayContentsAnyOrder(createSamplesArray(), array);
	}
	
	@CollectionFeature.Require(KNOWN_ORDER)
	public void testToArray_rightSizedArray_ordered() {
		T[] array = (T[])new Object[getNumElements()];
		assertSame("toLongArray(sameSizeE[]) should return the given array", array, collection.toArray(array));
		expectArrayContentsInOrder(getOrderedElements(), array);
	}

	public void testToArray_rightSizedArrayOfObject() {
		T[] array = (T[])new Object[getNumElements()];
		assertSame("toLongArray(sameSizeObject[]) should return the given array", array, collection.toArray(array));
		expectArrayContentsAnyOrder(createSamplesArray(), array);
	}

	@CollectionFeature.Require(KNOWN_ORDER)
	public void testToArray_rightSizedArrayOfObject_ordered() {
		T[] array = (T[])new Object[getNumElements()];
		assertSame("toLongArray(sameSizeObject[]) should return the given array", array, collection.toArray(array));
		expectArrayContentsInOrder(getOrderedElements(), array);
	}

	public void testToArray_oversizedArray() {
		T[] array = (T[])new Object[getNumElements() + 2];
		array[getNumElements()] = e3();
		array[getNumElements() + 1] = e3();
		assertSame("toLongArray(overSizedE[]) should return the given array", array, collection.toArray(array));

		ObjectList<T> subArray = ObjectArrayList.wrap(array).subList(0, getNumElements());
		T[] expectedSubArray = createSamplesArray();
		for (int i = 0; i < getNumElements(); i++) {
			assertTrue("toLongArray(overSizedE[]) should contain element " + expectedSubArray[i], subArray.contains(expectedSubArray[i]));
		}
		assertEquals("The array element immediately following the end of the collection should be nulled", null, array[getNumElements()]);
	}

	@CollectionFeature.Require(KNOWN_ORDER)
	public void testToArray_oversizedArray_ordered() {
		T[] array = (T[])new Object[getNumElements() + 2];
		array[getNumElements()] = e3();
		array[getNumElements() + 1] = e3();
		assertSame("toLongArray(overSizedE[]) should return the given array", array, collection.toArray(array));

		ObjectList<T> expected = getOrderedElements();
		for (int i = 0; i < getNumElements(); i++) {
			assertEquals(expected.get(i), array[i]);
		}
		assertEquals("The array element immediately following the end of the collection should be nulled", null, array[getNumElements()]);
	}

	private void expectArrayContentsAnyOrder(Object[] expected, Object[] actual) {
		ObjectHelpers.assertEqualIgnoringOrder(ObjectArrayList.wrap(expected), ObjectArrayList.wrap(actual));
	}

	private void expectArrayContentsInOrder(ObjectList<T> expected, T[] actual) {
		assertEquals("toArray() ordered contents: ", expected, ObjectArrayList.wrap(actual));
	}
}