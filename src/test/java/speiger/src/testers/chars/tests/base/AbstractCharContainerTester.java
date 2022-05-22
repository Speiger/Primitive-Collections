package speiger.src.testers.chars.tests.base;

import java.util.Collection;

import com.google.common.collect.testing.AbstractTester;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;
import com.google.common.collect.testing.TestContainerGenerator;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.errorprone.annotations.OverridingMethodsMustInvokeSuper;

import org.junit.Ignore;

import speiger.src.collections.chars.collections.CharCollection;
import speiger.src.collections.chars.collections.CharIterator;
import speiger.src.collections.chars.lists.CharArrayList;
import speiger.src.collections.chars.lists.CharList;
import speiger.src.collections.chars.utils.CharLists;
import speiger.src.testers.chars.generators.TestCharCollectionGenerator;
import speiger.src.testers.chars.utils.CharHelpers;
import speiger.src.testers.chars.utils.CharSamples;
import speiger.src.testers.chars.utils.MinimalCharCollection;

@Ignore
public abstract class AbstractCharContainerTester extends AbstractTester<OneSizeTestContainerGenerator<Collection<Character>, Character>> {
	protected CharSamples samples;
	protected CharCollection container;
	protected TestCharCollectionGenerator primitiveGenerator;
	private CollectionSize size;

	@Override
	@OverridingMethodsMustInvokeSuper
	public void setUp() throws Exception {
		super.setUp();
		TestContainerGenerator<Collection<Character>, Character> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestCharCollectionGenerator)) throw new IllegalStateException("Test Generator Must extend TestCharCollectionGenerator");
		primitiveGenerator = (TestCharCollectionGenerator) generator;
		samples = primitiveGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}

	protected abstract CharCollection actualContents();

	protected CharCollection resetContainer() {
		return resetContainer(createTestSubject());
	}

	protected CharCollection resetContainer(CharCollection newValue) {
		container = newValue;
		return container;
	}

	protected void expectContents(char... elements) {
		expectContents(CharArrayList.wrap(elements));
	}

	protected void expectContents(CharCollection expected) {
		CharHelpers.assertEqualIgnoringOrder(expected, actualContents());
	}

	protected void expectUnchanged() {
		expectContents(getOrderedElements());
	}

	protected final void expectAdded(char... elements) {
		CharList expected = CharHelpers.copyToList(getSampleElements());
		expected.addAll(elements);
		expectContents(expected);
	}

	protected final void expectAddedIndex(int index, char... elements) {
		expectAdded(index, CharArrayList.wrap(elements));
	}

	protected final void expectAdded(int index, CharCollection elements) {
		CharList expected = CharHelpers.copyToList(getSampleElements());
		expected.addAll(index, elements);
		expectContents(expected);
	}

	protected void expectMissing(char... elements) {
		for (char element : elements) {
			assertFalse("Should not contain " + element, actualContents().contains(element));
		}
	}

	protected char[] createSamplesArray() {
		return getSampleElements().toCharArray();
	}

	protected char[] createOrderedArray() {
		return getOrderedElements().toCharArray();
	}

	public static class ArrayWithDuplicate {
		public final char[] elements;
		public final char duplicate;

		private ArrayWithDuplicate(char[] elements, char duplicate) {
			this.elements = elements;
			this.duplicate = duplicate;
		}
	}

	protected ArrayWithDuplicate createArrayWithDuplicateElement() {
		char[] elements = createSamplesArray();
		char duplicate = elements[(elements.length / 2) - 1];
		elements[(elements.length / 2) + 1] = duplicate;
		return new ArrayWithDuplicate(elements, duplicate);
	}

	protected int getNumElements() {
		return size.getNumElements();
	}

	protected CharCollection getSampleElements() {
		return getSampleElements(getNumElements());
	}

	protected CharList getOrderedElements() {
		CharList list = new CharArrayList();
		for (CharIterator iter = primitiveGenerator.order(new CharArrayList(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.nextChar());
		}
		return CharLists.unmodifiable(list);
	}
	
	protected char[] createDisjointArray() {
		return new char[]{e3(), e4()};
	}
	
	protected char[] emptyArray() {
		return new char[0];
	}
	
	protected MinimalCharCollection createDisjointCollection() {
		return MinimalCharCollection.of(e3(), e4());
	}
	
	protected MinimalCharCollection emptyCollection() {
		return MinimalCharCollection.of();
	}

	protected final char e0() {
		return samples.e0();
	}

	protected final char e1() {
		return samples.e1();
	}

	protected final char e2() {
		return samples.e2();
	}

	protected final char e3() {
		return samples.e3();
	}

	protected final char e4() {
		return samples.e4();
	}

	protected CharCollection createTestSubject() {
		return primitiveGenerator.create(getSampleElements(size.getNumElements()).toCharArray());
	}

	protected CharCollection getSampleElements(int howMany) {
		return new CharArrayList(samples.asList().subList(0, howMany));
	}
}