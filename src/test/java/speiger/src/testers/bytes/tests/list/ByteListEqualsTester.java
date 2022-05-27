package speiger.src.testers.bytes.tests.list;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.bytes.collections.ByteCollection;
import speiger.src.collections.bytes.lists.ByteArrayList;
import speiger.src.testers.bytes.tests.base.AbstractByteListTester;
import speiger.src.testers.bytes.utils.MinimalByteSet;

@Ignore
@SuppressWarnings("javadoc")
public class ByteListEqualsTester extends AbstractByteListTester
{
	public void testEquals_otherListWithSameElements() {
		assertTrue("A List should equal any other List containing the same elements.", getList().equals(new ByteArrayList(getOrderedElements())));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_otherListWithDifferentElements() {
		ByteArrayList other = new ByteArrayList(getSampleElements());
		other.set(other.size() / 2, e3());
		assertFalse("A List should not equal another List containing different elements.", getList().equals(other));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_shorterList() {
		ByteCollection fewerElements = getSampleElements(getNumElements() - 1);
		assertFalse("Lists of different sizes should not be equal.", getList().equals(new ByteArrayList(fewerElements)));
	}

	public void testEquals_longerList() {
		ByteCollection moreElements = getSampleElements(getNumElements() + 1);
		assertFalse("Lists of different sizes should not be equal.", getList().equals(new ByteArrayList(moreElements)));
	}

	public void testEquals_set() {
		assertFalse("A List should never equal a Set.", getList().equals(MinimalByteSet.of(getList())));
	}
}