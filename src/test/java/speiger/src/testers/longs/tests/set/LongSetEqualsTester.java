package speiger.src.testers.longs.tests.set;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.longs.collections.LongCollection;
import speiger.src.testers.longs.tests.base.AbstractLongSetTester;
import speiger.src.testers.longs.utils.LongHelpers;
import speiger.src.testers.longs.utils.MinimalLongSet;

@Ignore
public class LongSetEqualsTester extends AbstractLongSetTester {
	public void testEquals_otherSetWithSameElements() {
		assertTrue("A Set should equal any other Set containing the same elements.", getSet().equals(MinimalLongSet.of(getSampleElements())));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_otherSetWithDifferentElements() {
		LongCollection elements = getSampleElements(getNumElements() - 1);
		elements.add(e3());
		assertFalse("A Set should not equal another Set containing different elements.", getSet().equals(MinimalLongSet.of(elements)));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_smallerSet() {
		LongCollection fewerElements = getSampleElements(getNumElements() - 1);
		assertFalse("Sets of different sizes should not be equal.", getSet().equals(MinimalLongSet.of(fewerElements)));
	}

	public void testEquals_largerSet() {
		LongCollection moreElements = getSampleElements(getNumElements() + 1);
		assertFalse("Sets of different sizes should not be equal.", getSet().equals(MinimalLongSet.of(moreElements)));
	}

	public void testEquals_list() {
		assertFalse("A List should never equal a Set.", getSet().equals(LongHelpers.copyToList(getSet())));
	}
}