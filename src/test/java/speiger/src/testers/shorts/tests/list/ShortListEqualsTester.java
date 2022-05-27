package speiger.src.testers.shorts.tests.list;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.shorts.collections.ShortCollection;
import speiger.src.collections.shorts.lists.ShortArrayList;
import speiger.src.testers.shorts.tests.base.AbstractShortListTester;
import speiger.src.testers.shorts.utils.MinimalShortSet;

@Ignore
public class ShortListEqualsTester extends AbstractShortListTester
{
	public void testEquals_otherListWithSameElements() {
		assertTrue("A List should equal any other List containing the same elements.", getList().equals(new ShortArrayList(getOrderedElements())));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_otherListWithDifferentElements() {
		ShortArrayList other = new ShortArrayList(getSampleElements());
		other.set(other.size() / 2, e3());
		assertFalse("A List should not equal another List containing different elements.", getList().equals(other));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_shorterList() {
		ShortCollection fewerElements = getSampleElements(getNumElements() - 1);
		assertFalse("Lists of different sizes should not be equal.", getList().equals(new ShortArrayList(fewerElements)));
	}

	public void testEquals_longerList() {
		ShortCollection moreElements = getSampleElements(getNumElements() + 1);
		assertFalse("Lists of different sizes should not be equal.", getList().equals(new ShortArrayList(moreElements)));
	}

	public void testEquals_set() {
		assertFalse("A List should never equal a Set.", getList().equals(MinimalShortSet.of(getList())));
	}
}