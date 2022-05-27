package speiger.src.testers.doubles.tests.collection;

import static com.google.common.collect.testing.features.CollectionFeature.KNOWN_ORDER;

import com.google.common.collect.testing.features.CollectionFeature;

import org.junit.Ignore;

import speiger.src.collections.doubles.lists.DoubleArrayList;
import speiger.src.collections.doubles.lists.DoubleList;
import speiger.src.testers.doubles.tests.base.AbstractDoubleCollectionTester;
import speiger.src.testers.doubles.utils.DoubleHelpers;

@Ignore
public class DoubleCollectionForEachTester extends AbstractDoubleCollectionTester
{
	@CollectionFeature.Require(absent = KNOWN_ORDER)
	public void testForEachUnknownOrder() {
		DoubleList elements = new DoubleArrayList();
		collection.forEach(elements::add);
		DoubleHelpers.assertContentsAnyOrder(elements, createSamplesArray());
	}
	
@CollectionFeature.Require(absent = KNOWN_ORDER)
	public void testForEachExtraUnknownOrder() {
		DoubleList elements = new DoubleArrayList();
		collection.forEach(elements, DoubleList::add);
		DoubleHelpers.assertContentsAnyOrder(elements, createSamplesArray());
	}
	
	@CollectionFeature.Require(KNOWN_ORDER)
	public void testForEachKnownOrder() {
		DoubleList elements = new DoubleArrayList();
		collection.forEach(elements::add);
		assertEquals("Different ordered iteration", DoubleHelpers.copyToList(getOrderedElements()), elements);
	}
	
	@CollectionFeature.Require(KNOWN_ORDER)
	public void testForEachExtraKnownOrder() {
		DoubleList elements = new DoubleArrayList();
		collection.forEach(elements, DoubleList::add);
		assertEquals("Different ordered iteration", DoubleHelpers.copyToList(getOrderedElements()), elements);
	}
}