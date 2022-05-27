package speiger.src.testers.objects.tests.list;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_REMOVE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import org.junit.Ignore;

import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.testers.objects.tests.base.AbstractObjectListTester;

@Ignore
public class ObjectListExtractElementsTester<T> extends AbstractObjectListTester<T>
{
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = {ZERO})
	public void testRemoveElements() {
		assertArrayEquals("The Extracted Elements should be present", getSampleElements(1).toArray(), getList().extractElements(0, 1, Object.class));
		expectMissing(e0());
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = {ZERO})
	public void testRemove_FromToLow() {
		try {
			getList().extractElements(-1, 1, Object.class);
			fail("extractElements(toLow, high) should throw IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
		}
		expectUnchanged();
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = {ZERO})
	public void testRemove_toToHigh() {
		try {
			getList().extractElements(0, getNumElements()+1, Object.class);
			fail("extractElements(low, toHigh) should throw IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
		}
		expectUnchanged();
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = {ZERO})
	public void testRemove_mixedUp() {
		try {
			getList().extractElements(1, 0, Object.class);
		} catch (IndexOutOfBoundsException e) {
		}
		expectUnchanged();
	}
	
	private static void assertArrayEquals(String message, Object[] expected, Object[] actual) {
		assertEquals(message, ObjectArrayList.wrap(expected), ObjectArrayList.wrap(actual));
	}
}