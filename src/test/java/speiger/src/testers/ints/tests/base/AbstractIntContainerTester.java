package speiger.src.testers.ints.tests.base;

import java.util.Collection;

import com.google.common.collect.testing.AbstractTester;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;
import com.google.common.collect.testing.TestContainerGenerator;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.errorprone.annotations.OverridingMethodsMustInvokeSuper;

import org.junit.Ignore;

import speiger.src.collections.ints.collections.IntCollection;
import speiger.src.collections.ints.collections.IntIterator;
import speiger.src.collections.ints.lists.IntArrayList;
import speiger.src.collections.ints.lists.IntList;
import speiger.src.collections.ints.utils.IntLists;
import speiger.src.testers.ints.generators.TestIntCollectionGenerator;
import speiger.src.testers.ints.utils.IntHelpers;
import speiger.src.testers.ints.utils.IntSamples;
import speiger.src.testers.ints.utils.MinimalIntCollection;

@Ignore
public abstract class AbstractIntContainerTester extends AbstractTester<OneSizeTestContainerGenerator<Collection<Integer>, Integer>> {
	protected IntSamples samples;
	protected IntCollection container;
	protected TestIntCollectionGenerator primitiveGenerator;
	private CollectionSize size;

	@Override
	@OverridingMethodsMustInvokeSuper
	public void setUp() throws Exception {
		super.setUp();
		TestContainerGenerator<Collection<Integer>, Integer> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestIntCollectionGenerator)) throw new IllegalStateException("Test Generator Must extend TestIntCollectionGenerator");
		primitiveGenerator = (TestIntCollectionGenerator) generator;
		samples = primitiveGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}

	protected abstract IntCollection actualContents();

	protected IntCollection resetContainer() {
		return resetContainer(createTestSubject());
	}

	protected IntCollection resetContainer(IntCollection newValue) {
		container = newValue;
		return container;
	}

	protected void expectContents(int... elements) {
		expectContents(IntArrayList.wrap(elements));
	}

	protected void expectContents(IntCollection expected) {
		IntHelpers.assertEqualIgnoringOrder(expected, actualContents());
	}

	protected void expectUnchanged() {
		expectContents(getOrderedElements());
	}

	protected final void expectAdded(int... elements) {
		IntList expected = IntHelpers.copyToList(getSampleElements());
		expected.addAll(elements);
		expectContents(expected);
	}

	protected final void expectAddedIndex(int index, int... elements) {
		expectAdded(index, IntArrayList.wrap(elements));
	}

	protected final void expectAdded(int index, IntCollection elements) {
		IntList expected = IntHelpers.copyToList(getSampleElements());
		expected.addAll(index, elements);
		expectContents(expected);
	}

	protected void expectMissing(int... elements) {
		for (int element : elements) {
			assertFalse("Should not contain " + element, actualContents().contains(element));
		}
	}

	protected int[] createSamplesArray() {
		return getSampleElements().toIntArray();
	}

	protected int[] createOrderedArray() {
		return getOrderedElements().toIntArray();
	}

	public static class ArrayWithDuplicate {
		public final int[] elements;
		public final int duplicate;

		private ArrayWithDuplicate(int[] elements, int duplicate) {
			this.elements = elements;
			this.duplicate = duplicate;
		}
	}

	protected ArrayWithDuplicate createArrayWithDuplicateElement() {
		int[] elements = createSamplesArray();
		int duplicate = elements[(elements.length / 2) - 1];
		elements[(elements.length / 2) + 1] = duplicate;
		return new ArrayWithDuplicate(elements, duplicate);
	}

	protected int getNumElements() {
		return size.getNumElements();
	}

	protected IntCollection getSampleElements() {
		return getSampleElements(getNumElements());
	}

	protected IntList getOrderedElements() {
		IntList list = new IntArrayList();
		for (IntIterator iter = primitiveGenerator.order(new IntArrayList(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.nextInt());
		}
		return IntLists.unmodifiable(list);
	}
	
	protected int[] createDisjointArray() {
		return new int[]{e3(), e4()};
	}
	
	protected int[] emptyArray() {
		return new int[0];
	}
	
	protected MinimalIntCollection createDisjointCollection() {
		return MinimalIntCollection.of(e3(), e4());
	}
	
	protected MinimalIntCollection emptyCollection() {
		return MinimalIntCollection.of();
	}

	protected final int e0() {
		return samples.e0();
	}

	protected final int e1() {
		return samples.e1();
	}

	protected final int e2() {
		return samples.e2();
	}

	protected final int e3() {
		return samples.e3();
	}

	protected final int e4() {
		return samples.e4();
	}

	protected IntCollection createTestSubject() {
		return primitiveGenerator.create(getSampleElements(size.getNumElements()).toIntArray());
	}

	protected IntCollection getSampleElements(int howMany) {
		return new IntArrayList(samples.asList().subList(0, howMany));
	}
}