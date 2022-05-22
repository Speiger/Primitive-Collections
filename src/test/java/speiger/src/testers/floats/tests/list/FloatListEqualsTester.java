package speiger.src.testers.floats.tests.list;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.floats.collections.FloatCollection;
import speiger.src.collections.floats.lists.FloatArrayList;
import speiger.src.testers.floats.tests.base.AbstractFloatListTester;
import speiger.src.testers.floats.utils.MinimalFloatSet;

@Ignore
public class FloatListEqualsTester extends AbstractFloatListTester {
	public void testEquals_otherListWithSameElements() {
		assertTrue("A List should equal any other List containing the same elements.", getList().equals(new FloatArrayList(getOrderedElements())));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_otherListWithDifferentElements() {
		FloatArrayList other = new FloatArrayList(getSampleElements());
		other.set(other.size() / 2, e3());
		assertFalse("A List should not equal another List containing different elements.", getList().equals(other));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_shorterList() {
		FloatCollection fewerElements = getSampleElements(getNumElements() - 1);
		assertFalse("Lists of different sizes should not be equal.", getList().equals(new FloatArrayList(fewerElements)));
	}

	public void testEquals_longerList() {
		FloatCollection moreElements = getSampleElements(getNumElements() + 1);
		assertFalse("Lists of different sizes should not be equal.", getList().equals(new FloatArrayList(moreElements)));
	}

	public void testEquals_set() {
		assertFalse("A List should never equal a Set.", getList().equals(MinimalFloatSet.of(getList())));
	}
}