package speiger.src.testers.chars.tests.base;

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
@SuppressWarnings("javadoc")
public abstract class AbstractCharContainerTester<E> extends AbstractTester<OneSizeTestContainerGenerator<E, Character>>
{
	protected CharSamples samples;
	protected E container;
	protected TestCharCollectionGenerator primitiveGenerator;
	protected CollectionSize size;

	@Override
	@OverridingMethodsMustInvokeSuper
	public void setUp() throws Exception {
		super.setUp();
		setupGenerator();
	}
	
	protected void setupGenerator() {
		TestContainerGenerator<E, Character> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestCharCollectionGenerator)) throw new IllegalStateException("Test Generator Must extend TestCharCollectionGenerator");
		primitiveGenerator = (TestCharCollectionGenerator) generator;
		samples = primitiveGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();		
	}

	protected abstract CharCollection actualContents();

	protected E resetContainer() {
		return resetContainer(createTestSubject());
	}

	protected E resetContainer(E newValue) {
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
		return getSampleElements().toCharArray(new char[getNumElements()]);
	}

	protected char[] createOrderedArray() {
		return getOrderedElements().toCharArray(new char[getNumElements()]);
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
	
	public char[] createArray(char...array) {
		return array;
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

	protected E createTestSubject() {
		return (E)primitiveGenerator.create(getSampleElements(size.getNumElements()).toCharArray(new char[getNumElements()]));
	}

	protected CharCollection getSampleElements(int howMany) {
		return new CharArrayList(samples.asList().subList(0, howMany));
	}
}