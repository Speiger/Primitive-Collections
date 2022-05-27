package speiger.src.testers.chars.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.chars.maps.abstracts.AbstractChar2CharMap;
import speiger.src.collections.chars.maps.interfaces.Char2CharMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.chars.generators.maps.TestChar2CharMapGenerator;
import speiger.src.testers.chars.utils.CharHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
public class AbstractChar2CharMapTester extends AbstractObjectContainerTester<Char2CharMap.Entry, Char2CharMap> {

	protected TestChar2CharMapGenerator primitiveMapGenerator;

	protected Char2CharMap getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Character, Character>, ? extends Map.Entry<Character, Character>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestChar2CharMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestChar2CharMapGenerator) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Char2CharMap createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Char2CharMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Char2CharMap.Entry> getOrderedElements() {
		ObjectList<Char2CharMap.Entry> list = new ObjectArrayList<>();
		for (ObjectIterator<Char2CharMap.Entry> iter = primitiveMapGenerator.order(new ObjectArrayList<Char2CharMap.Entry>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Char2CharMap.Entry> actualContents() {
		return getMap().char2CharEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Char2CharMap.Entry[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Char2CharMap resetContainer(Char2CharMap newValue) {
		newValue.setDefaultReturnValue((char)-1);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(char... elements) {
		for (char element : elements) {
			assertFalse("Should not contain key " + element, getMap().containsKey(element));
		}
	}

	protected void expectMissingValues(char... elements) {
		for (char element : elements) {
			assertFalse("Should not contain value " + element, getMap().containsValue(element));
		}
	}
	
	protected int getNumEntries() {
		return getNumElements();
	}
		
	protected ObjectCollection<Char2CharMap.Entry> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Char2CharMap.Entry> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Char2CharMap.Entry... entries) {
		for (Char2CharMap.Entry entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getCharKey() + " mapped to value " + entry.getCharValue(), valueEquals(getMap().get(entry.getCharKey()), entry.getCharValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Char2CharMap.Entry> expected) {
		super.expectContents(expected);
		for (Char2CharMap.Entry entry : expected) {
			assertEquals("Wrong value for key " + entry.getCharKey(), entry.getCharValue(), getMap().get(entry.getCharKey()));
		}
	}

	protected final void expectReplacement(Char2CharMap.Entry newEntry) {
		ObjectList<Char2CharMap.Entry> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Char2CharMap.Entry> expected, Char2CharMap.Entry newEntry) {
		for (ObjectListIterator<Char2CharMap.Entry> i = expected.listIterator(); i.hasNext();) {
			if (CharHelpers.equals(i.next().getCharKey(), newEntry.getCharKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getCharKey(), expected));
	}

	private static boolean valueEquals(char a, char b) {
		return a == b;
	}
	
	protected Char2CharMap.Entry entry(char key, char value) {
		return new AbstractChar2CharMap.BasicEntry(key, value);
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
	
	protected char[] emptyValueArray() {
		return new char[0];
	}
	
	protected char[] createDisjointedValueArray() {
		char[] array = new char[2];
		array[0] = v3();
		array[1] = v4();
		return array;
	}
	
	protected Character[] emptyValueObjectArray() {
		return new Character[0];
	}
	
	protected Character[] createDisjointedValueObjectArray() {
		Character[] array = new Character[2];
		array[0] = v3();
		array[1] = v4();
		return array;
	}
	
	protected char get(char key) {
		return getMap().get(key);
	}

	protected final char k0() {
		return e0().getCharKey();
	}

	protected final char v0() {
		return e0().getCharValue();
	}

	protected final char k1() {
		return e1().getCharKey();
	}

	protected final char v1() {
		return e1().getCharValue();
	}

	protected final char k2() {
		return e2().getCharKey();
	}

	protected final char v2() {
		return e2().getCharValue();
	}

	protected final char k3() {
		return e3().getCharKey();
	}

	protected final char v3() {
		return e3().getCharValue();
	}

	protected final char k4() {
		return e4().getCharKey();
	}

	protected final char v4() {
		return e4().getCharValue();
	}
}