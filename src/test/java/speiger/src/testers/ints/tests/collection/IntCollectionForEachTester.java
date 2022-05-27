package speiger.src.testers.ints.tests.collection;

import static com.google.common.collect.testing.features.CollectionFeature.KNOWN_ORDER;

import com.google.common.collect.testing.features.CollectionFeature;

import org.junit.Ignore;

import speiger.src.collections.ints.lists.IntArrayList;
import speiger.src.collections.ints.lists.IntList;
import speiger.src.testers.ints.tests.base.AbstractIntCollectionTester;
import speiger.src.testers.ints.utils.IntHelpers;

@Ignore
public class IntCollectionForEachTester extends AbstractIntCollectionTester
{
	@CollectionFeature.Require(absent = KNOWN_ORDER)
	public void testForEachUnknownOrder() {
		IntList elements = new IntArrayList();
		collection.forEach(elements::add);
		IntHelpers.assertContentsAnyOrder(elements, createSamplesArray());
	}
	
@CollectionFeature.Require(absent = KNOWN_ORDER)
	public void testForEachExtraUnknownOrder() {
		IntList elements = new IntArrayList();
		collection.forEach(elements, IntList::add);
		IntHelpers.assertContentsAnyOrder(elements, createSamplesArray());
	}
	
	@CollectionFeature.Require(KNOWN_ORDER)
	public void testForEachKnownOrder() {
		IntList elements = new IntArrayList();
		collection.forEach(elements::add);
		assertEquals("Different ordered iteration", IntHelpers.copyToList(getOrderedElements()), elements);
	}
	
	@CollectionFeature.Require(KNOWN_ORDER)
	public void testForEachExtraKnownOrder() {
		IntList elements = new IntArrayList();
		collection.forEach(elements, IntList::add);
		assertEquals("Different ordered iteration", IntHelpers.copyToList(getOrderedElements()), elements);
	}
}