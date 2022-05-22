package speiger.src.testers.chars.tests.list;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.chars.collections.CharCollection;
import speiger.src.collections.chars.lists.CharArrayList;
import speiger.src.testers.chars.tests.base.AbstractCharListTester;
import speiger.src.testers.chars.utils.MinimalCharSet;

public class CharListEqualsTester extends AbstractCharListTester {
	public void testEquals_otherListWithSameElements() {
		assertTrue("A List should equal any other List containing the same elements.", getList().equals(new CharArrayList(getOrderedElements())));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_otherListWithDifferentElements() {
		CharArrayList other = new CharArrayList(getSampleElements());
		other.set(other.size() / 2, e3());
		assertFalse("A List should not equal another List containing different elements.", getList().equals(other));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_shorterList() {
		CharCollection fewerElements = getSampleElements(getNumElements() - 1);
		assertFalse("Lists of different sizes should not be equal.", getList().equals(new CharArrayList(fewerElements)));
	}

	public void testEquals_longerList() {
		CharCollection moreElements = getSampleElements(getNumElements() + 1);
		assertFalse("Lists of different sizes should not be equal.", getList().equals(new CharArrayList(moreElements)));
	}

	public void testEquals_set() {
		assertFalse("A List should never equal a Set.", getList().equals(MinimalCharSet.of(getList())));
	}
}