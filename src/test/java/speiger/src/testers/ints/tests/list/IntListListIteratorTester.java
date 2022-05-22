package speiger.src.testers.ints.tests.list;

import org.junit.Ignore;

import static com.google.common.collect.testing.IteratorFeature.MODIFIABLE;
import static com.google.common.collect.testing.IteratorFeature.UNMODIFIABLE;
import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_REMOVE;
import static com.google.common.collect.testing.features.ListFeature.SUPPORTS_ADD_WITH_INDEX;
import static com.google.common.collect.testing.features.ListFeature.SUPPORTS_SET;

import java.util.Set;

import com.google.common.collect.testing.IteratorFeature;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.ListFeature;

import speiger.src.collections.ints.lists.IntList;
import speiger.src.collections.ints.lists.IntListIterator;
import speiger.src.collections.ints.utils.IntLists;
import speiger.src.testers.ints.tests.base.AbstractIntListTester;
import speiger.src.testers.ints.utils.IntHelpers;
import speiger.src.testers.ints.utils.IntListIteratorTester;

@Ignore
public class IntListListIteratorTester extends AbstractIntListTester {
	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	@ListFeature.Require(absent = { SUPPORTS_SET, SUPPORTS_ADD_WITH_INDEX })
	public void testListIterator_unmodifiable() {
		runListIteratorTest(UNMODIFIABLE);
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@ListFeature.Require({ SUPPORTS_SET, SUPPORTS_ADD_WITH_INDEX })
	public void testListIterator_fullyModifiable() {
		runListIteratorTest(MODIFIABLE);
	}

	private void runListIteratorTest(Set<IteratorFeature> features) {
		new IntListIteratorTester(4, IntLists.singleton(e4()), features, IntHelpers.copyToList(getOrderedElements()), 0) {
			@Override
			protected IntListIterator newTargetIterator() {
				resetCollection();
				return getList().listIterator();
			}

			@Override
			protected void verify(IntList elements) {
				expectContents(elements);
			}
		}.test();
	}

	public void testListIterator_tooLow() {
		try {
			getList().listIterator(-1);
			fail();
		} catch (IndexOutOfBoundsException expected) {
		}
	}

	public void testListIterator_tooHigh() {
		try {
			getList().listIterator(getNumElements() + 1);
			fail();
		} catch (IndexOutOfBoundsException expected) {
		}
	}

	public void testListIterator_atSize() {
		getList().listIterator(getNumElements());
	}
}