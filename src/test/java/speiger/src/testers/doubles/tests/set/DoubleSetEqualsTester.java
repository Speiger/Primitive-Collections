package speiger.src.testers.doubles.tests.set;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.doubles.collections.DoubleCollection;
import speiger.src.testers.doubles.tests.base.AbstractDoubleSetTester;
import speiger.src.testers.doubles.utils.DoubleHelpers;
import speiger.src.testers.doubles.utils.MinimalDoubleSet;

public class DoubleSetEqualsTester extends AbstractDoubleSetTester {
	public void testEquals_otherSetWithSameElements() {
		assertTrue("A Set should equal any other Set containing the same elements.", getSet().equals(MinimalDoubleSet.of(getSampleElements())));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_otherSetWithDifferentElements() {
		DoubleCollection elements = getSampleElements(getNumElements() - 1);
		elements.add(e3());
		assertFalse("A Set should not equal another Set containing different elements.", getSet().equals(MinimalDoubleSet.of(elements)));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_smallerSet() {
		DoubleCollection fewerElements = getSampleElements(getNumElements() - 1);
		assertFalse("Sets of different sizes should not be equal.", getSet().equals(MinimalDoubleSet.of(fewerElements)));
	}

	public void testEquals_largerSet() {
		DoubleCollection moreElements = getSampleElements(getNumElements() + 1);
		assertFalse("Sets of different sizes should not be equal.", getSet().equals(MinimalDoubleSet.of(moreElements)));
	}

	public void testEquals_list() {
		assertFalse("A List should never equal a Set.", getSet().equals(DoubleHelpers.copyToList(getSet())));
	}
}