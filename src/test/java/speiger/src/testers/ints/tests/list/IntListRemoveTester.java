package speiger.src.testers.ints.tests.list;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_REMOVE;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.ints.tests.base.AbstractIntListTester;

@Ignore
@SuppressWarnings("javadoc")
public class IntListRemoveTester extends AbstractIntListTester
{
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@CollectionSize.Require(absent = {ZERO, ONE})
	public void testRemove_duplicate() {
		ArrayWithDuplicate arrayAndDuplicate = createArrayWithDuplicateElement();
		collection = primitiveGenerator.create(arrayAndDuplicate.elements);
		int duplicate = arrayAndDuplicate.duplicate;

		int firstIndex = getList().indexOf(duplicate);
		int initialSize = getList().size();
		assertTrue("remove(present) should return true", getList().remInt(duplicate));
		assertTrue("After remove(duplicate), a list should still contain the duplicate element", getList().contains(duplicate));
		assertFalse("remove(duplicate) should remove the first instance of the " + "duplicate element in the list", firstIndex == getList().indexOf(duplicate));
		assertEquals("remove(present) should decrease the size of a list by one.", initialSize - 1, getList().size());
	}
}