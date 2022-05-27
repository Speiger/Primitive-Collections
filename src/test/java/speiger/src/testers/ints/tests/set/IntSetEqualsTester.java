package speiger.src.testers.ints.tests.set;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.ints.collections.IntCollection;
import speiger.src.testers.ints.tests.base.AbstractIntSetTester;
import speiger.src.testers.ints.utils.IntHelpers;
import speiger.src.testers.ints.utils.MinimalIntSet;

@Ignore
public class IntSetEqualsTester extends AbstractIntSetTester
{
	public void testEquals_otherSetWithSameElements() {
		assertTrue("A Set should equal any other Set containing the same elements.", getSet().equals(MinimalIntSet.of(getSampleElements())));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_otherSetWithDifferentElements() {
		IntCollection elements = getSampleElements(getNumElements() - 1);
		elements.add(e3());
		assertFalse("A Set should not equal another Set containing different elements.", getSet().equals(MinimalIntSet.of(elements)));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_smallerSet() {
		IntCollection fewerElements = getSampleElements(getNumElements() - 1);
		assertFalse("Sets of different sizes should not be equal.", getSet().equals(MinimalIntSet.of(fewerElements)));
	}

	public void testEquals_largerSet() {
		IntCollection moreElements = getSampleElements(getNumElements() + 1);
		assertFalse("Sets of different sizes should not be equal.", getSet().equals(MinimalIntSet.of(moreElements)));
	}

	public void testEquals_list() {
		assertFalse("A List should never equal a Set.", getSet().equals(IntHelpers.copyToList(getSet())));
	}
}