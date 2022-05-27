package speiger.src.testers.objects.tests.list;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.testers.objects.tests.base.AbstractObjectListTester;
import speiger.src.testers.objects.utils.MinimalObjectSet;

@Ignore
@SuppressWarnings("javadoc")
public class ObjectListEqualsTester<T> extends AbstractObjectListTester<T>
{
	public void testEquals_otherListWithSameElements() {
		assertTrue("A List should equal any other List containing the same elements.", getList().equals(new ObjectArrayList<>(getOrderedElements())));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_otherListWithDifferentElements() {
		ObjectArrayList<T> other = new ObjectArrayList<>(getSampleElements());
		other.set(other.size() / 2, e3());
		assertFalse("A List should not equal another List containing different elements.", getList().equals(other));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_shorterList() {
		ObjectCollection<T> fewerElements = getSampleElements(getNumElements() - 1);
		assertFalse("Lists of different sizes should not be equal.", getList().equals(new ObjectArrayList<>(fewerElements)));
	}

	public void testEquals_longerList() {
		ObjectCollection<T> moreElements = getSampleElements(getNumElements() + 1);
		assertFalse("Lists of different sizes should not be equal.", getList().equals(new ObjectArrayList<>(moreElements)));
	}

	public void testEquals_set() {
		assertFalse("A List should never equal a Set.", getList().equals(MinimalObjectSet.of(getList())));
	}
}