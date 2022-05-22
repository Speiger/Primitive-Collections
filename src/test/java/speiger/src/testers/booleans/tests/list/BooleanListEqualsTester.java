package speiger.src.testers.booleans.tests.list;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.booleans.collections.BooleanCollection;
import speiger.src.collections.booleans.lists.BooleanArrayList;
import speiger.src.testers.booleans.tests.base.AbstractBooleanListTester;
import speiger.src.testers.booleans.utils.MinimalBooleanSet;

public class BooleanListEqualsTester extends AbstractBooleanListTester {
	public void testEquals_otherListWithSameElements() {
		assertTrue("A List should equal any other List containing the same elements.", getList().equals(new BooleanArrayList(getOrderedElements())));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_otherListWithDifferentElements() {
		BooleanArrayList other = new BooleanArrayList(getSampleElements());
		other.set(other.size() / 2, e3());
		assertFalse("A List should not equal another List containing different elements.", getList().equals(other));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_shorterList() {
		BooleanCollection fewerElements = getSampleElements(getNumElements() - 1);
		assertFalse("Lists of different sizes should not be equal.", getList().equals(new BooleanArrayList(fewerElements)));
	}

	public void testEquals_longerList() {
		BooleanCollection moreElements = getSampleElements(getNumElements() + 1);
		assertFalse("Lists of different sizes should not be equal.", getList().equals(new BooleanArrayList(moreElements)));
	}

	public void testEquals_set() {
		assertFalse("A List should never equal a Set.", getList().equals(MinimalBooleanSet.of(getList())));
	}
}