package speiger.src.testers.chars.tests.set;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.chars.collections.CharCollection;
import speiger.src.testers.chars.tests.base.AbstractCharSetTester;
import speiger.src.testers.chars.utils.CharHelpers;
import speiger.src.testers.chars.utils.MinimalCharSet;

@Ignore
@SuppressWarnings("javadoc")
public class CharSetEqualsTester extends AbstractCharSetTester
{
	public void testEquals_otherSetWithSameElements() {
		assertTrue("A Set should equal any other Set containing the same elements.", getSet().equals(MinimalCharSet.of(getSampleElements())));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_otherSetWithDifferentElements() {
		CharCollection elements = getSampleElements(getNumElements() - 1);
		elements.add(e3());
		assertFalse("A Set should not equal another Set containing different elements.", getSet().equals(MinimalCharSet.of(elements)));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_smallerSet() {
		CharCollection fewerElements = getSampleElements(getNumElements() - 1);
		assertFalse("Sets of different sizes should not be equal.", getSet().equals(MinimalCharSet.of(fewerElements)));
	}

	public void testEquals_largerSet() {
		CharCollection moreElements = getSampleElements(getNumElements() + 1);
		assertFalse("Sets of different sizes should not be equal.", getSet().equals(MinimalCharSet.of(moreElements)));
	}

	public void testEquals_list() {
		assertFalse("A List should never equal a Set.", getSet().equals(CharHelpers.copyToList(getSet())));
	}
}