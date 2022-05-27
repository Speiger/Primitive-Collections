package speiger.src.testers.longs.tests.list;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.longs.collections.LongCollection;
import speiger.src.collections.longs.lists.LongArrayList;
import speiger.src.testers.longs.tests.base.AbstractLongListTester;
import speiger.src.testers.longs.utils.MinimalLongSet;

@Ignore
@SuppressWarnings("javadoc")
public class LongListEqualsTester extends AbstractLongListTester
{
	public void testEquals_otherListWithSameElements() {
		assertTrue("A List should equal any other List containing the same elements.", getList().equals(new LongArrayList(getOrderedElements())));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_otherListWithDifferentElements() {
		LongArrayList other = new LongArrayList(getSampleElements());
		other.set(other.size() / 2, e3());
		assertFalse("A List should not equal another List containing different elements.", getList().equals(other));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_shorterList() {
		LongCollection fewerElements = getSampleElements(getNumElements() - 1);
		assertFalse("Lists of different sizes should not be equal.", getList().equals(new LongArrayList(fewerElements)));
	}

	public void testEquals_longerList() {
		LongCollection moreElements = getSampleElements(getNumElements() + 1);
		assertFalse("Lists of different sizes should not be equal.", getList().equals(new LongArrayList(moreElements)));
	}

	public void testEquals_set() {
		assertFalse("A List should never equal a Set.", getList().equals(MinimalLongSet.of(getList())));
	}
}