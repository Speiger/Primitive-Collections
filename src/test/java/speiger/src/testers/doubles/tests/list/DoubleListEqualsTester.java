package speiger.src.testers.doubles.tests.list;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.doubles.collections.DoubleCollection;
import speiger.src.collections.doubles.lists.DoubleArrayList;
import speiger.src.testers.doubles.tests.base.AbstractDoubleListTester;
import speiger.src.testers.doubles.utils.MinimalDoubleSet;

@Ignore
@SuppressWarnings("javadoc")
public class DoubleListEqualsTester extends AbstractDoubleListTester
{
	public void testEquals_otherListWithSameElements() {
		assertTrue("A List should equal any other List containing the same elements.", getList().equals(new DoubleArrayList(getOrderedElements())));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_otherListWithDifferentElements() {
		DoubleArrayList other = new DoubleArrayList(getSampleElements());
		other.set(other.size() / 2, e3());
		assertFalse("A List should not equal another List containing different elements.", getList().equals(other));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_shorterList() {
		DoubleCollection fewerElements = getSampleElements(getNumElements() - 1);
		assertFalse("Lists of different sizes should not be equal.", getList().equals(new DoubleArrayList(fewerElements)));
	}

	public void testEquals_longerList() {
		DoubleCollection moreElements = getSampleElements(getNumElements() + 1);
		assertFalse("Lists of different sizes should not be equal.", getList().equals(new DoubleArrayList(moreElements)));
	}

	public void testEquals_set() {
		assertFalse("A List should never equal a Set.", getList().equals(MinimalDoubleSet.of(getList())));
	}
}