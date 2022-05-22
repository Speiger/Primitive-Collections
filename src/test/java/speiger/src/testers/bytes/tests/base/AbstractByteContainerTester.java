package speiger.src.testers.bytes.tests.base;

import java.util.Collection;

import com.google.common.collect.testing.AbstractTester;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;
import com.google.common.collect.testing.TestContainerGenerator;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.errorprone.annotations.OverridingMethodsMustInvokeSuper;

import speiger.src.collections.bytes.collections.ByteCollection;
import speiger.src.collections.bytes.collections.ByteIterator;
import speiger.src.collections.bytes.lists.ByteArrayList;
import speiger.src.collections.bytes.lists.ByteList;
import speiger.src.collections.bytes.utils.ByteLists;
import speiger.src.testers.bytes.generators.TestByteCollectionGenerator;
import speiger.src.testers.bytes.utils.ByteHelpers;
import speiger.src.testers.bytes.utils.ByteSamples;
import speiger.src.testers.bytes.utils.MinimalByteCollection;

public abstract class AbstractByteContainerTester extends AbstractTester<OneSizeTestContainerGenerator<Collection<Byte>, Byte>> {
	protected ByteSamples samples;
	protected ByteCollection container;
	protected TestByteCollectionGenerator primitiveGenerator;
	private CollectionSize size;

	@Override
	@OverridingMethodsMustInvokeSuper
	public void setUp() throws Exception {
		super.setUp();
		TestContainerGenerator<Collection<Byte>, Byte> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestByteCollectionGenerator)) throw new IllegalStateException("Test Generator Must extend TestByteCollectionGenerator");
		primitiveGenerator = (TestByteCollectionGenerator) generator;
		samples = primitiveGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}

	protected abstract ByteCollection actualContents();

	protected ByteCollection resetContainer() {
		return resetContainer(createTestSubject());
	}

	protected ByteCollection resetContainer(ByteCollection newValue) {
		container = newValue;
		return container;
	}

	protected void expectContents(byte... elements) {
		expectContents(ByteArrayList.wrap(elements));
	}

	protected void expectContents(ByteCollection expected) {
		ByteHelpers.assertEqualIgnoringOrder(expected, actualContents());
	}

	protected void expectUnchanged() {
		expectContents(getOrderedElements());
	}

	protected final void expectAdded(byte... elements) {
		ByteList expected = ByteHelpers.copyToList(getSampleElements());
		expected.addAll(elements);
		expectContents(expected);
	}

	protected final void expectAddedIndex(int index, byte... elements) {
		expectAdded(index, ByteArrayList.wrap(elements));
	}

	protected final void expectAdded(int index, ByteCollection elements) {
		ByteList expected = ByteHelpers.copyToList(getSampleElements());
		expected.addAll(index, elements);
		expectContents(expected);
	}

	protected void expectMissing(byte... elements) {
		for (byte element : elements) {
			assertFalse("Should not contain " + element, actualContents().contains(element));
		}
	}

	protected byte[] createSamplesArray() {
		return getSampleElements().toByteArray();
	}

	protected byte[] createOrderedArray() {
		return getOrderedElements().toByteArray();
	}

	public static class ArrayWithDuplicate {
		public final byte[] elements;
		public final byte duplicate;

		private ArrayWithDuplicate(byte[] elements, byte duplicate) {
			this.elements = elements;
			this.duplicate = duplicate;
		}
	}

	protected ArrayWithDuplicate createArrayWithDuplicateElement() {
		byte[] elements = createSamplesArray();
		byte duplicate = elements[(elements.length / 2) - 1];
		elements[(elements.length / 2) + 1] = duplicate;
		return new ArrayWithDuplicate(elements, duplicate);
	}

	protected int getNumElements() {
		return size.getNumElements();
	}

	protected ByteCollection getSampleElements() {
		return getSampleElements(getNumElements());
	}

	protected ByteList getOrderedElements() {
		ByteList list = new ByteArrayList();
		for (ByteIterator iter = primitiveGenerator.order(new ByteArrayList(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.nextByte());
		}
		return ByteLists.unmodifiable(list);
	}
	
	protected byte[] createDisjointArray() {
		return new byte[]{e3(), e4()};
	}
	
	protected byte[] emptyArray() {
		return new byte[0];
	}
	
	protected MinimalByteCollection createDisjointCollection() {
		return MinimalByteCollection.of(e3(), e4());
	}
	
	protected MinimalByteCollection emptyCollection() {
		return MinimalByteCollection.of();
	}

	protected final byte e0() {
		return samples.e0();
	}

	protected final byte e1() {
		return samples.e1();
	}

	protected final byte e2() {
		return samples.e2();
	}

	protected final byte e3() {
		return samples.e3();
	}

	protected final byte e4() {
		return samples.e4();
	}

	protected ByteCollection createTestSubject() {
		return primitiveGenerator.create(getSampleElements(size.getNumElements()).toByteArray());
	}

	protected ByteCollection getSampleElements(int howMany) {
		return new ByteArrayList(samples.asList().subList(0, howMany));
	}
}