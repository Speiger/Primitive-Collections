package speiger.src.testers.booleans.tests.base;

import java.util.Collection;

import com.google.common.collect.testing.AbstractTester;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;
import com.google.common.collect.testing.TestContainerGenerator;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.errorprone.annotations.OverridingMethodsMustInvokeSuper;

import org.junit.Ignore;

import speiger.src.collections.booleans.collections.BooleanCollection;
import speiger.src.collections.booleans.collections.BooleanIterator;
import speiger.src.collections.booleans.lists.BooleanArrayList;
import speiger.src.collections.booleans.lists.BooleanList;
import speiger.src.collections.booleans.utils.BooleanLists;
import speiger.src.testers.booleans.generators.TestBooleanCollectionGenerator;
import speiger.src.testers.booleans.utils.BooleanHelpers;
import speiger.src.testers.booleans.utils.BooleanSamples;
import speiger.src.testers.booleans.utils.MinimalBooleanCollection;

@Ignore
public abstract class AbstractBooleanContainerTester extends AbstractTester<OneSizeTestContainerGenerator<Collection<Boolean>, Boolean>> {
	protected BooleanSamples samples;
	protected BooleanCollection container;
	protected TestBooleanCollectionGenerator primitiveGenerator;
	private CollectionSize size;

	@Override
	@OverridingMethodsMustInvokeSuper
	public void setUp() throws Exception {
		super.setUp();
		TestContainerGenerator<Collection<Boolean>, Boolean> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestBooleanCollectionGenerator)) throw new IllegalStateException("Test Generator Must extend TestBooleanCollectionGenerator");
		primitiveGenerator = (TestBooleanCollectionGenerator) generator;
		samples = primitiveGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}

	protected abstract BooleanCollection actualContents();

	protected BooleanCollection resetContainer() {
		return resetContainer(createTestSubject());
	}

	protected BooleanCollection resetContainer(BooleanCollection newValue) {
		container = newValue;
		return container;
	}

	protected void expectContents(boolean... elements) {
		expectContents(BooleanArrayList.wrap(elements));
	}

	protected void expectContents(BooleanCollection expected) {
		BooleanHelpers.assertEqualIgnoringOrder(expected, actualContents());
	}

	protected void expectUnchanged() {
		expectContents(getOrderedElements());
	}

	protected final void expectAdded(boolean... elements) {
		BooleanList expected = BooleanHelpers.copyToList(getSampleElements());
		expected.addAll(elements);
		expectContents(expected);
	}

	protected final void expectAddedIndex(int index, boolean... elements) {
		expectAdded(index, BooleanArrayList.wrap(elements));
	}

	protected final void expectAdded(int index, BooleanCollection elements) {
		BooleanList expected = BooleanHelpers.copyToList(getSampleElements());
		expected.addAll(index, elements);
		expectContents(expected);
	}

	protected void expectMissing(boolean... elements) {
		for (boolean element : elements) {
			assertFalse("Should not contain " + element, actualContents().contains(element));
		}
	}

	protected boolean[] createSamplesArray() {
		return getSampleElements().toBooleanArray();
	}

	protected boolean[] createOrderedArray() {
		return getOrderedElements().toBooleanArray();
	}

	public static class ArrayWithDuplicate {
		public final boolean[] elements;
		public final boolean duplicate;

		private ArrayWithDuplicate(boolean[] elements, boolean duplicate) {
			this.elements = elements;
			this.duplicate = duplicate;
		}
	}

	protected ArrayWithDuplicate createArrayWithDuplicateElement() {
		boolean[] elements = createSamplesArray();
		boolean duplicate = elements[(elements.length / 2) - 1];
		elements[(elements.length / 2) + 1] = duplicate;
		return new ArrayWithDuplicate(elements, duplicate);
	}

	protected int getNumElements() {
		return size.getNumElements();
	}

	protected BooleanCollection getSampleElements() {
		return getSampleElements(getNumElements());
	}

	protected BooleanList getOrderedElements() {
		BooleanList list = new BooleanArrayList();
		for (BooleanIterator iter = primitiveGenerator.order(new BooleanArrayList(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.nextBoolean());
		}
		return BooleanLists.unmodifiable(list);
	}
	
	protected boolean[] createDisjointArray() {
		return new boolean[]{e3(), e4()};
	}
	
	protected boolean[] emptyArray() {
		return new boolean[0];
	}
	
	protected MinimalBooleanCollection createDisjointCollection() {
		return MinimalBooleanCollection.of(e3(), e4());
	}
	
	protected MinimalBooleanCollection emptyCollection() {
		return MinimalBooleanCollection.of();
	}

	protected final boolean e0() {
		return samples.e0();
	}

	protected final boolean e1() {
		return samples.e1();
	}

	protected final boolean e2() {
		return samples.e2();
	}

	protected final boolean e3() {
		return samples.e3();
	}

	protected final boolean e4() {
		return samples.e4();
	}

	protected BooleanCollection createTestSubject() {
		return primitiveGenerator.create(getSampleElements(size.getNumElements()).toBooleanArray());
	}

	protected BooleanCollection getSampleElements(int howMany) {
		return new BooleanArrayList(samples.asList().subList(0, howMany));
	}
}