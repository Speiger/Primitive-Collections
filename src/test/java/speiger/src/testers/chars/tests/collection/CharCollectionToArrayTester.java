package speiger.src.testers.chars.tests.collection;

import static com.google.common.collect.testing.features.CollectionFeature.KNOWN_ORDER;

import com.google.common.collect.testing.features.CollectionFeature;

import org.junit.Ignore;

import speiger.src.collections.chars.lists.CharArrayList;
import speiger.src.collections.chars.lists.CharList;
import speiger.src.testers.chars.tests.base.AbstractCharCollectionTester;
import speiger.src.testers.chars.utils.CharHelpers;

@Ignore
public class CharCollectionToArrayTester extends AbstractCharCollectionTester{
	public void testToArray_noArgs() {
		char[] array = collection.toCharArray();
		expectArrayContentsAnyOrder(createSamplesArray(), array);
	}

	public void testToArray_emptyArray() {
		char[] empty = emptyArray();
		char[] array = collection.toCharArray(empty);
		assertEquals("toLongArray(emptyT[]).length:", getNumElements(), array.length);
		expectArrayContentsAnyOrder(createSamplesArray(), array);
	}

	@CollectionFeature.Require(KNOWN_ORDER)
	public void testToArray_emptyArray_ordered() {
		char[] empty = emptyArray();
		char[] array = collection.toCharArray(empty);
		assertEquals("toLongArray(emptyT[]).length:", getNumElements(), array.length);
		expectArrayContentsInOrder(getOrderedElements(), array);
	}

	public void testToArray_emptyArrayOfObject() {
		char[] in = emptyArray();
		char[] array = collection.toCharArray(in);
		assertEquals("toLongArray(emptyObject[]).length", getNumElements(), array.length);
		expectArrayContentsAnyOrder(createSamplesArray(), array);
	}

	public void testToArray_rightSizedArray() {
		char[] array = new char[getNumElements()];
		assertSame("toLongArray(sameSizeE[]) should return the given array", array, collection.toCharArray(array));
		expectArrayContentsAnyOrder(createSamplesArray(), array);
	}
	
	@CollectionFeature.Require(KNOWN_ORDER)
	public void testToArray_rightSizedArray_ordered() {
		char[] array = new char[getNumElements()];
		assertSame("toLongArray(sameSizeE[]) should return the given array", array, collection.toCharArray(array));
		expectArrayContentsInOrder(getOrderedElements(), array);
	}

	public void testToArray_rightSizedArrayOfObject() {
		char[] array = new char[getNumElements()];
		assertSame("toLongArray(sameSizeObject[]) should return the given array", array, collection.toCharArray(array));
		expectArrayContentsAnyOrder(createSamplesArray(), array);
	}

	@CollectionFeature.Require(KNOWN_ORDER)
	public void testToArray_rightSizedArrayOfObject_ordered() {
		char[] array = new char[getNumElements()];
		assertSame("toLongArray(sameSizeObject[]) should return the given array", array, collection.toCharArray(array));
		expectArrayContentsInOrder(getOrderedElements(), array);
	}

	public void testToArray_oversizedArray() {
		char[] array = new char[getNumElements() + 2];
		array[getNumElements()] = e3();
		array[getNumElements() + 1] = e3();
		assertSame("toLongArray(overSizedE[]) should return the given array", array, collection.toCharArray(array));

		CharList subArray = CharArrayList.wrap(array).subList(0, getNumElements());
		char[] expectedSubArray = createSamplesArray();
		for (int i = 0; i < getNumElements(); i++) {
			assertTrue("toLongArray(overSizedE[]) should contain element " + expectedSubArray[i], subArray.contains(expectedSubArray[i]));
		}
		assertEquals("The array element immediately following the end of the collection should be nulled", (char)0, array[getNumElements()]);
	}

	@CollectionFeature.Require(KNOWN_ORDER)
	public void testToArray_oversizedArray_ordered() {
		char[] array = new char[getNumElements() + 2];
		array[getNumElements()] = e3();
		array[getNumElements() + 1] = e3();
		assertSame("toLongArray(overSizedE[]) should return the given array", array, collection.toCharArray(array));

		CharList expected = getOrderedElements();
		for (int i = 0; i < getNumElements(); i++) {
			assertEquals(expected.getChar(i), array[i]);
		}
		assertEquals("The array element immediately following the end of the collection should be nulled", (char)0, array[getNumElements()]);
	}

	private void expectArrayContentsAnyOrder(char[] expected, char[] actual) {
		CharHelpers.assertEqualIgnoringOrder(CharArrayList.wrap(expected), CharArrayList.wrap(actual));
	}

	private void expectArrayContentsInOrder(CharList expected, char[] actual) {
		assertEquals("toCharArray() ordered contents: ", expected, CharArrayList.wrap(actual));
	}
}