package speiger.src.testers.bytes.tests.set;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.bytes.collections.ByteCollection;
import speiger.src.testers.bytes.tests.base.AbstractByteSetTester;
import speiger.src.testers.bytes.utils.ByteHelpers;
import speiger.src.testers.bytes.utils.MinimalByteSet;

public class ByteSetEqualsTester extends AbstractByteSetTester {
	public void testEquals_otherSetWithSameElements() {
		assertTrue("A Set should equal any other Set containing the same elements.", getSet().equals(MinimalByteSet.of(getSampleElements())));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_otherSetWithDifferentElements() {
		ByteCollection elements = getSampleElements(getNumElements() - 1);
		elements.add(e3());
		assertFalse("A Set should not equal another Set containing different elements.", getSet().equals(MinimalByteSet.of(elements)));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_smallerSet() {
		ByteCollection fewerElements = getSampleElements(getNumElements() - 1);
		assertFalse("Sets of different sizes should not be equal.", getSet().equals(MinimalByteSet.of(fewerElements)));
	}

	public void testEquals_largerSet() {
		ByteCollection moreElements = getSampleElements(getNumElements() + 1);
		assertFalse("Sets of different sizes should not be equal.", getSet().equals(MinimalByteSet.of(moreElements)));
	}

	public void testEquals_list() {
		assertFalse("A List should never equal a Set.", getSet().equals(ByteHelpers.copyToList(getSet())));
	}
}