package speiger.src.testers.objects.tests.list;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.FAILS_FAST_ON_CONCURRENT_MODIFICATION;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.ListFeature.SUPPORTS_REMOVE_WITH_INDEX;

import java.util.ConcurrentModificationException;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.ListFeature;

import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.tests.base.AbstractObjectListTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
public class ObjectListRemoveAtIndexTester<T> extends AbstractObjectListTester<T>
{
	@ListFeature.Require(absent = SUPPORTS_REMOVE_WITH_INDEX)
	@CollectionSize.Require(absent = ZERO)
	public void testRemoveAtIndex_unsupported() {
		try {
			getList().remove(0);
			fail("remove(i) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}

	@ListFeature.Require(SUPPORTS_REMOVE_WITH_INDEX)
	public void testRemoveAtIndex_negative() {
		try {
			getList().remove(-1);
			fail("remove(-1) should throw");
		} catch (IndexOutOfBoundsException expected) {
		}
		expectUnchanged();
	}

	@ListFeature.Require(SUPPORTS_REMOVE_WITH_INDEX)
	public void testRemoveAtIndex_tooLarge() {
		try {
			getList().remove(getNumElements());
			fail("remove(size) should throw");
		} catch (IndexOutOfBoundsException expected) {
		}
		expectUnchanged();
	}

	@ListFeature.Require(SUPPORTS_REMOVE_WITH_INDEX)
	@CollectionSize.Require(absent = ZERO)
	public void testRemoveAtIndex_first() {
		runRemoveTest(0);
	}

	@ListFeature.Require(SUPPORTS_REMOVE_WITH_INDEX)
	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testRemoveAtIndex_middle() {
		runRemoveTest(getNumElements() / 2);
	}

	@CollectionFeature.Require(FAILS_FAST_ON_CONCURRENT_MODIFICATION)
	@ListFeature.Require(SUPPORTS_REMOVE_WITH_INDEX)
	@CollectionSize.Require(absent = ZERO)
	public void testRemoveAtIndexConcurrentWithIteration() {
		try {
			ObjectIterator<T> iterator = collection.iterator();
			getList().remove(getNumElements() / 2);
			iterator.next();
			fail("Expected ConcurrentModificationException");
		} catch (ConcurrentModificationException expected) {
			// success
		}
	}

	@ListFeature.Require(SUPPORTS_REMOVE_WITH_INDEX)
	@CollectionSize.Require(absent = ZERO)
	public void testRemoveAtIndex_last() {
		runRemoveTest(getNumElements() - 1);
	}

	private void runRemoveTest(int index) {
		assertEquals(String.format("remove(%d) should return the element at index %d", index, index), getList().get(index), getList().remove(index));
		ObjectList<T> expected = ObjectHelpers.copyToList(createSamplesArray());
		expected.remove(index);
		expectContents(expected);
	}
}