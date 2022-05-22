package speiger.src.testers.longs.tests.base;

import java.util.Collection;

import com.google.common.collect.testing.AbstractTester;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;
import com.google.common.collect.testing.TestContainerGenerator;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.errorprone.annotations.OverridingMethodsMustInvokeSuper;

import org.junit.Ignore;

import speiger.src.collections.longs.collections.LongCollection;
import speiger.src.collections.longs.collections.LongIterator;
import speiger.src.collections.longs.lists.LongArrayList;
import speiger.src.collections.longs.lists.LongList;
import speiger.src.collections.longs.utils.LongLists;
import speiger.src.testers.longs.generators.TestLongCollectionGenerator;
import speiger.src.testers.longs.utils.LongHelpers;
import speiger.src.testers.longs.utils.LongSamples;
import speiger.src.testers.longs.utils.MinimalLongCollection;

@Ignore
public abstract class AbstractLongContainerTester extends AbstractTester<OneSizeTestContainerGenerator<Collection<Long>, Long>> {
	protected LongSamples samples;
	protected LongCollection container;
	protected TestLongCollectionGenerator primitiveGenerator;
	private CollectionSize size;

	@Override
	@OverridingMethodsMustInvokeSuper
	public void setUp() throws Exception {
		super.setUp();
		TestContainerGenerator<Collection<Long>, Long> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestLongCollectionGenerator)) throw new IllegalStateException("Test Generator Must extend TestLongCollectionGenerator");
		primitiveGenerator = (TestLongCollectionGenerator) generator;
		samples = primitiveGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}

	protected abstract LongCollection actualContents();

	protected LongCollection resetContainer() {
		return resetContainer(createTestSubject());
	}

	protected LongCollection resetContainer(LongCollection newValue) {
		container = newValue;
		return container;
	}

	protected void expectContents(long... elements) {
		expectContents(LongArrayList.wrap(elements));
	}

	protected void expectContents(LongCollection expected) {
		LongHelpers.assertEqualIgnoringOrder(expected, actualContents());
	}

	protected void expectUnchanged() {
		expectContents(getOrderedElements());
	}

	protected final void expectAdded(long... elements) {
		LongList expected = LongHelpers.copyToList(getSampleElements());
		expected.addAll(elements);
		expectContents(expected);
	}

	protected final void expectAddedIndex(int index, long... elements) {
		expectAdded(index, LongArrayList.wrap(elements));
	}

	protected final void expectAdded(int index, LongCollection elements) {
		LongList expected = LongHelpers.copyToList(getSampleElements());
		expected.addAll(index, elements);
		expectContents(expected);
	}

	protected void expectMissing(long... elements) {
		for (long element : elements) {
			assertFalse("Should not contain " + element, actualContents().contains(element));
		}
	}

	protected long[] createSamplesArray() {
		return getSampleElements().toLongArray();
	}

	protected long[] createOrderedArray() {
		return getOrderedElements().toLongArray();
	}

	public static class ArrayWithDuplicate {
		public final long[] elements;
		public final long duplicate;

		private ArrayWithDuplicate(long[] elements, long duplicate) {
			this.elements = elements;
			this.duplicate = duplicate;
		}
	}

	protected ArrayWithDuplicate createArrayWithDuplicateElement() {
		long[] elements = createSamplesArray();
		long duplicate = elements[(elements.length / 2) - 1];
		elements[(elements.length / 2) + 1] = duplicate;
		return new ArrayWithDuplicate(elements, duplicate);
	}

	protected int getNumElements() {
		return size.getNumElements();
	}

	protected LongCollection getSampleElements() {
		return getSampleElements(getNumElements());
	}

	protected LongList getOrderedElements() {
		LongList list = new LongArrayList();
		for (LongIterator iter = primitiveGenerator.order(new LongArrayList(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.nextLong());
		}
		return LongLists.unmodifiable(list);
	}
	
	protected long[] createDisjointArray() {
		return new long[]{e3(), e4()};
	}
	
	protected long[] emptyArray() {
		return new long[0];
	}
	
	protected MinimalLongCollection createDisjointCollection() {
		return MinimalLongCollection.of(e3(), e4());
	}
	
	protected MinimalLongCollection emptyCollection() {
		return MinimalLongCollection.of();
	}

	protected final long e0() {
		return samples.e0();
	}

	protected final long e1() {
		return samples.e1();
	}

	protected final long e2() {
		return samples.e2();
	}

	protected final long e3() {
		return samples.e3();
	}

	protected final long e4() {
		return samples.e4();
	}

	protected LongCollection createTestSubject() {
		return primitiveGenerator.create(getSampleElements(size.getNumElements()).toLongArray());
	}

	protected LongCollection getSampleElements(int howMany) {
		return new LongArrayList(samples.asList().subList(0, howMany));
	}
}