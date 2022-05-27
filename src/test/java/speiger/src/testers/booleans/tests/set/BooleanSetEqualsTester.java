package speiger.src.testers.booleans.tests.set;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.booleans.collections.BooleanCollection;
import speiger.src.testers.booleans.tests.base.AbstractBooleanSetTester;
import speiger.src.testers.booleans.utils.BooleanHelpers;
import speiger.src.testers.booleans.utils.MinimalBooleanSet;

@Ignore
public class BooleanSetEqualsTester extends AbstractBooleanSetTester
{
	public void testEquals_otherSetWithSameElements() {
		assertTrue("A Set should equal any other Set containing the same elements.", getSet().equals(MinimalBooleanSet.of(getSampleElements())));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_otherSetWithDifferentElements() {
		BooleanCollection elements = getSampleElements(getNumElements() - 1);
		elements.add(e3());
		assertFalse("A Set should not equal another Set containing different elements.", getSet().equals(MinimalBooleanSet.of(elements)));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_smallerSet() {
		BooleanCollection fewerElements = getSampleElements(getNumElements() - 1);
		assertFalse("Sets of different sizes should not be equal.", getSet().equals(MinimalBooleanSet.of(fewerElements)));
	}

	public void testEquals_largerSet() {
		BooleanCollection moreElements = getSampleElements(getNumElements() + 1);
		assertFalse("Sets of different sizes should not be equal.", getSet().equals(MinimalBooleanSet.of(moreElements)));
	}

	public void testEquals_list() {
		assertFalse("A List should never equal a Set.", getSet().equals(BooleanHelpers.copyToList(getSet())));
	}
}