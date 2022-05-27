package speiger.src.testers.shorts.tests.list;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_REMOVE;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.shorts.tests.base.AbstractShortListTester;

@Ignore
public class ShortListSwapRemoveTester extends AbstractShortListTester
{
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = {ZERO, ONE})
	public void testRemove_duplicate() {
		ArrayWithDuplicate arrayAndDuplicate = createArrayWithDuplicateElement();
		collection = primitiveGenerator.create(arrayAndDuplicate.elements);
		short duplicate = arrayAndDuplicate.duplicate;
		short lastElement = arrayAndDuplicate.elements[arrayAndDuplicate.elements.length-1];
		
		int firstIndex = getList().indexOf(duplicate);
		int initialSize = getList().size();
		assertTrue("swapRemove(present) should return true", getList().swapRemoveShort(duplicate));
		assertTrue("removed element should have moved the last element in its place", getList().getShort(firstIndex) == lastElement);
		assertTrue("After remove(duplicate), a list should still contain the duplicate element", getList().contains(duplicate));
		assertEquals("remove(present) should decrease the size of a list by one.", initialSize - 1, getList().size());
	}
}