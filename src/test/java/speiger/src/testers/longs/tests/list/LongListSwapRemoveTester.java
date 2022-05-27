package speiger.src.testers.longs.tests.list;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_REMOVE;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.longs.tests.base.AbstractLongListTester;

@Ignore
@SuppressWarnings("javadoc")
public class LongListSwapRemoveTester extends AbstractLongListTester
{
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = {ZERO, ONE})
	public void testRemove_duplicate() {
		ArrayWithDuplicate arrayAndDuplicate = createArrayWithDuplicateElement();
		collection = primitiveGenerator.create(arrayAndDuplicate.elements);
		long duplicate = arrayAndDuplicate.duplicate;
		long lastElement = arrayAndDuplicate.elements[arrayAndDuplicate.elements.length-1];
		
		int firstIndex = getList().indexOf(duplicate);
		int initialSize = getList().size();
		assertTrue("swapRemove(present) should return true", getList().swapRemoveLong(duplicate));
		assertTrue("removed element should have moved the last element in its place", getList().getLong(firstIndex) == lastElement);
		assertTrue("After remove(duplicate), a list should still contain the duplicate element", getList().contains(duplicate));
		assertEquals("remove(present) should decrease the size of a list by one.", initialSize - 1, getList().size());
	}
}