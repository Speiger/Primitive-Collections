package speiger.src.testers.chars.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.chars.maps.abstracts.AbstractChar2ShortMap;
import speiger.src.collections.chars.maps.interfaces.Char2ShortMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.chars.generators.maps.TestChar2ShortMapGenerator;
import speiger.src.testers.chars.utils.CharHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class AbstractChar2ShortMapTester extends AbstractObjectContainerTester<Char2ShortMap.Entry, Char2ShortMap>
{
	protected TestChar2ShortMapGenerator primitiveMapGenerator;

	protected Char2ShortMap getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Character, Short>, ? extends Map.Entry<Character, Short>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestChar2ShortMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestChar2ShortMapGenerator) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Char2ShortMap createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Char2ShortMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Char2ShortMap.Entry> getOrderedElements() {
		ObjectList<Char2ShortMap.Entry> list = new ObjectArrayList<>();
		for (ObjectIterator<Char2ShortMap.Entry> iter = primitiveMapGenerator.order(new ObjectArrayList<Char2ShortMap.Entry>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Char2ShortMap.Entry> actualContents() {
		return getMap().char2ShortEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Char2ShortMap.Entry[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Char2ShortMap resetContainer(Char2ShortMap newValue) {
		newValue.setDefaultReturnValue((short)-1);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(char... elements) {
		for (char element : elements) {
			assertFalse("Should not contain key " + element, getMap().containsKey(element));
		}
	}

	protected void expectMissingValues(short... elements) {
		for (short element : elements) {
			assertFalse("Should not contain value " + element, getMap().containsValue(element));
		}
	}
	
	protected int getNumEntries() {
		return getNumElements();
	}
		
	protected ObjectCollection<Char2ShortMap.Entry> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Char2ShortMap.Entry> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Char2ShortMap.Entry... entries) {
		for (Char2ShortMap.Entry entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getCharKey() + " mapped to value " + entry.getShortValue(), valueEquals(getMap().get(entry.getCharKey()), entry.getShortValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Char2ShortMap.Entry> expected) {
		super.expectContents(expected);
		for (Char2ShortMap.Entry entry : expected) {
			assertEquals("Wrong value for key " + entry.getCharKey(), entry.getShortValue(), getMap().get(entry.getCharKey()));
		}
	}

	protected final void expectReplacement(Char2ShortMap.Entry newEntry) {
		ObjectList<Char2ShortMap.Entry> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Char2ShortMap.Entry> expected, Char2ShortMap.Entry newEntry) {
		for (ObjectListIterator<Char2ShortMap.Entry> i = expected.listIterator(); i.hasNext();) {
			if (CharHelpers.equals(i.next().getCharKey(), newEntry.getCharKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getCharKey(), expected));
	}

	private static boolean valueEquals(short a, short b) {
		return a == b;
	}
	
	protected Char2ShortMap.Entry entry(char key, short value) {
		return new AbstractChar2ShortMap.BasicEntry(key, value);
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
	
	protected short[] emptyValueArray() {
		return new short[0];
	}
	
	protected short[] createDisjointedValueArray() {
		short[] array = new short[2];
		array[0] = v3();
		array[1] = v4();
		return array;
	}
	
	protected Short[] emptyValueObjectArray() {
		return new Short[0];
	}
	
	protected Short[] createDisjointedValueObjectArray() {
		Short[] array = new Short[2];
		array[0] = v3();
		array[1] = v4();
		return array;
	}
	
	protected short get(char key) {
		return getMap().get(key);
	}

	protected final char k0() {
		return e0().getCharKey();
	}

	protected final short v0() {
		return e0().getShortValue();
	}

	protected final char k1() {
		return e1().getCharKey();
	}

	protected final short v1() {
		return e1().getShortValue();
	}

	protected final char k2() {
		return e2().getCharKey();
	}

	protected final short v2() {
		return e2().getShortValue();
	}

	protected final char k3() {
		return e3().getCharKey();
	}

	protected final short v3() {
		return e3().getShortValue();
	}

	protected final char k4() {
		return e4().getCharKey();
	}

	protected final short v4() {
		return e4().getShortValue();
	}
}