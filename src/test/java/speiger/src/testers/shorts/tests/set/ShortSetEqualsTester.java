package speiger.src.testers.shorts.tests.set;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.shorts.collections.ShortCollection;
import speiger.src.testers.shorts.tests.base.AbstractShortSetTester;
import speiger.src.testers.shorts.utils.ShortHelpers;
import speiger.src.testers.shorts.utils.MinimalShortSet;

@Ignore
@SuppressWarnings("javadoc")
public class ShortSetEqualsTester extends AbstractShortSetTester
{
	public void testEquals_otherSetWithSameElements() {
		assertTrue("A Set should equal any other Set containing the same elements.", getSet().equals(MinimalShortSet.of(getSampleElements())));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_otherSetWithDifferentElements() {
		ShortCollection elements = getSampleElements(getNumElements() - 1);
		elements.add(e3());
		assertFalse("A Set should not equal another Set containing different elements.", getSet().equals(MinimalShortSet.of(elements)));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_smallerSet() {
		ShortCollection fewerElements = getSampleElements(getNumElements() - 1);
		assertFalse("Sets of different sizes should not be equal.", getSet().equals(MinimalShortSet.of(fewerElements)));
	}

	public void testEquals_largerSet() {
		ShortCollection moreElements = getSampleElements(getNumElements() + 1);
		assertFalse("Sets of different sizes should not be equal.", getSet().equals(MinimalShortSet.of(moreElements)));
	}

	public void testEquals_list() {
		assertFalse("A List should never equal a Set.", getSet().equals(ShortHelpers.copyToList(getSet())));
	}
}