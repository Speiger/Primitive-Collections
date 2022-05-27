package speiger.src.testers.chars.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.chars.maps.abstracts.AbstractChar2IntMap;
import speiger.src.collections.chars.maps.interfaces.Char2IntMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.chars.generators.maps.TestChar2IntMapGenerator;
import speiger.src.testers.chars.utils.CharHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class AbstractChar2IntMapTester extends AbstractObjectContainerTester<Char2IntMap.Entry, Char2IntMap>
{
	protected TestChar2IntMapGenerator primitiveMapGenerator;

	protected Char2IntMap getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Character, Integer>, ? extends Map.Entry<Character, Integer>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestChar2IntMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestChar2IntMapGenerator) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Char2IntMap createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Char2IntMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Char2IntMap.Entry> getOrderedElements() {
		ObjectList<Char2IntMap.Entry> list = new ObjectArrayList<>();
		for (ObjectIterator<Char2IntMap.Entry> iter = primitiveMapGenerator.order(new ObjectArrayList<Char2IntMap.Entry>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Char2IntMap.Entry> actualContents() {
		return getMap().char2IntEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Char2IntMap.Entry[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Char2IntMap resetContainer(Char2IntMap newValue) {
		newValue.setDefaultReturnValue(-1);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(char... elements) {
		for (char element : elements) {
			assertFalse("Should not contain key " + element, getMap().containsKey(element));
		}
	}

	protected void expectMissingValues(int... elements) {
		for (int element : elements) {
			assertFalse("Should not contain value " + element, getMap().containsValue(element));
		}
	}
	
	protected int getNumEntries() {
		return getNumElements();
	}
		
	protected ObjectCollection<Char2IntMap.Entry> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Char2IntMap.Entry> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Char2IntMap.Entry... entries) {
		for (Char2IntMap.Entry entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getCharKey() + " mapped to value " + entry.getIntValue(), valueEquals(getMap().get(entry.getCharKey()), entry.getIntValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Char2IntMap.Entry> expected) {
		super.expectContents(expected);
		for (Char2IntMap.Entry entry : expected) {
			assertEquals("Wrong value for key " + entry.getCharKey(), entry.getIntValue(), getMap().get(entry.getCharKey()));
		}
	}

	protected final void expectReplacement(Char2IntMap.Entry newEntry) {
		ObjectList<Char2IntMap.Entry> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Char2IntMap.Entry> expected, Char2IntMap.Entry newEntry) {
		for (ObjectListIterator<Char2IntMap.Entry> i = expected.listIterator(); i.hasNext();) {
			if (CharHelpers.equals(i.next().getCharKey(), newEntry.getCharKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getCharKey(), expected));
	}

	private static boolean valueEquals(int a, int b) {
		return a == b;
	}
	
	protected Char2IntMap.Entry entry(char key, int value) {
		return new AbstractChar2IntMap.BasicEntry(key, value);
	}
	
	protected char[] emptyKeyArray() {
		return new char[0];
	}
	
	protected char[] createDisjointedKeyArray() {
		char[] array = new char[2];
		array[0] = k3();
		array[1] = k4();
		return array;
	}
	
	protected Character[] emptyKeyObjectArray() {
		return new Character[0];
	}
	
	protected Character[] createDisjointedKeyObjectArray() {
		Character[] array = new Character[2];
		array[0] = k3();
		array[1] = k4();
		return array;
	}
	
	protected int[] emptyValueArray() {
		return new int[0];
	}
	
	protected int[] createDisjointedValueArray() {
		int[] array = new int[2];
		array[0] = v3();
		array[1] = v4();
		return array;
	}
	
	protected Integer[] emptyValueObjectArray() {
		return new Integer[0];
	}
	
	protected Integer[] createDisjointedValueObjectArray() {
		Integer[] array = new Integer[2];
		array[0] = v3();
		array[1] = v4();
		return array;
	}
	
	protected int get(char key) {
		return getMap().get(key);
	}

	protected final char k0() {
		return e0().getCharKey();
	}

	protected final int v0() {
		return e0().getIntValue();
	}

	protected final char k1() {
		return e1().getCharKey();
	}

	protected final int v1() {
		return e1().getIntValue();
	}

	protected final char k2() {
		return e2().getCharKey();
	}

	protected final int v2() {
		return e2().getIntValue();
	}

	protected final char k3() {
		return e3().getCharKey();
	}

	protected final int v3() {
		return e3().getIntValue();
	}

	protected final char k4() {
		return e4().getCharKey();
	}

	protected final int v4() {
		return e4().getIntValue();
	}
}