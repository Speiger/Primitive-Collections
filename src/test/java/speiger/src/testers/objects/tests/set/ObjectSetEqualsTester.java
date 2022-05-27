package speiger.src.testers.objects.tests.set;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.testers.objects.tests.base.AbstractObjectSetTester;
import speiger.src.testers.objects.utils.ObjectHelpers;
import speiger.src.testers.objects.utils.MinimalObjectSet;

@Ignore
public class ObjectSetEqualsTester<T> extends AbstractObjectSetTester<T>
{
	public void testEquals_otherSetWithSameElements() {
		assertTrue("A Set should equal any other Set containing the same elements.", getSet().equals(MinimalObjectSet.of(getSampleElements())));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_otherSetWithDifferentElements() {
		ObjectCollection<T> elements = getSampleElements(getNumElements() - 1);
		elements.add(e3());
		assertFalse("A Set should not equal another Set containing different elements.", getSet().equals(MinimalObjectSet.of(elements)));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_smallerSet() {
		ObjectCollection<T> fewerElements = getSampleElements(getNumElements() - 1);
		assertFalse("Sets of different sizes should not be equal.", getSet().equals(MinimalObjectSet.of(fewerElements)));
	}

	public void testEquals_largerSet() {
		ObjectCollection<T> moreElements = getSampleElements(getNumElements() + 1);
		assertFalse("Sets of different sizes should not be equal.", getSet().equals(MinimalObjectSet.of(moreElements)));
	}

	public void testEquals_list() {
		assertFalse("A List should never equal a Set.", getSet().equals(ObjectHelpers.copyToList(getSet())));
	}
}