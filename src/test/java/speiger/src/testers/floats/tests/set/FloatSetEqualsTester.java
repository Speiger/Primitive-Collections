package speiger.src.testers.floats.tests.set;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.floats.collections.FloatCollection;
import speiger.src.testers.floats.tests.base.AbstractFloatSetTester;
import speiger.src.testers.floats.utils.FloatHelpers;
import speiger.src.testers.floats.utils.MinimalFloatSet;

@Ignore
public class FloatSetEqualsTester extends AbstractFloatSetTester
{
	public void testEquals_otherSetWithSameElements() {
		assertTrue("A Set should equal any other Set containing the same elements.", getSet().equals(MinimalFloatSet.of(getSampleElements())));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_otherSetWithDifferentElements() {
		FloatCollection elements = getSampleElements(getNumElements() - 1);
		elements.add(e3());
		assertFalse("A Set should not equal another Set containing different elements.", getSet().equals(MinimalFloatSet.of(elements)));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_smallerSet() {
		FloatCollection fewerElements = getSampleElements(getNumElements() - 1);
		assertFalse("Sets of different sizes should not be equal.", getSet().equals(MinimalFloatSet.of(fewerElements)));
	}

	public void testEquals_largerSet() {
		FloatCollection moreElements = getSampleElements(getNumElements() + 1);
		assertFalse("Sets of different sizes should not be equal.", getSet().equals(MinimalFloatSet.of(moreElements)));
	}

	public void testEquals_list() {
		assertFalse("A List should never equal a Set.", getSet().equals(FloatHelpers.copyToList(getSet())));
	}
}