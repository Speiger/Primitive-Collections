package speiger.src.testers.shorts.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.shorts.maps.abstracts.AbstractShort2CharMap;
import speiger.src.collections.shorts.maps.interfaces.Short2CharMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.shorts.generators.maps.TestShort2CharMapGenerator;
import speiger.src.testers.shorts.utils.ShortHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
public class AbstractShort2CharMapTester extends AbstractObjectContainerTester<Short2CharMap.Entry, Short2CharMap> {

	protected TestShort2CharMapGenerator primitiveMapGenerator;

	protected Short2CharMap getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Short, Character>, ? extends Map.Entry<Short, Character>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestShort2CharMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestShort2CharMapGenerator) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Short2CharMap createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Short2CharMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Short2CharMap.Entry> getOrderedElements() {
		ObjectList<Short2CharMap.Entry> list = new ObjectArrayList<>();
		for (ObjectIterator<Short2CharMap.Entry> iter = primitiveMapGenerator.order(new ObjectArrayList<Short2CharMap.Entry>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Short2CharMap.Entry> actualContents() {
		return getMap().short2CharEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Short2CharMap.Entry[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Short2CharMap resetContainer(Short2CharMap newValue) {
		newValue.setDefaultReturnValue((char)-1);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(short... elements) {
		for (short element : elements) {
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
		
	protected ObjectCollection<Short2CharMap.Entry> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Short2CharMap.Entry> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Short2CharMap.Entry... entries) {
		for (Short2CharMap.Entry entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getShortKey() + " mapped to value " + entry.getCharValue(), valueEquals(getMap().get(entry.getShortKey()), entry.getCharValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Short2CharMap.Entry> expected) {
		super.expectContents(expected);
		for (Short2CharMap.Entry entry : expected) {
			assertEquals("Wrong value for key " + entry.getShortKey(), entry.getCharValue(), getMap().get(entry.getShortKey()));
		}
	}

	protected final void expectReplacement(Short2CharMap.Entry newEntry) {
		ObjectList<Short2CharMap.Entry> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Short2CharMap.Entry> expected, Short2CharMap.Entry newEntry) {
		for (ObjectListIterator<Short2CharMap.Entry> i = expected.listIterator(); i.hasNext();) {
			if (ShortHelpers.equals(i.next().getShortKey(), newEntry.getShortKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getShortKey(), expected));
	}

	private static boolean valueEquals(char a, char b) {
		return a == b;
	}
	
	protected Short2CharMap.Entry entry(short key, char value) {
		return new AbstractShort2CharMap.BasicEntry(key, value);
	}
	
	protected short[] emptyKeyArray() {
		return new short[0];
	}
	
	protected short[] createDisjointedKeyArray() {
		short[] array = new short[2];
		array[0] = k3();
		array[1] = k4();
		return array;
	}
	
	protected Short[] emptyKeyObjectArray() {
		return new Short[0];
	}
	
	protected Short[] createDisjointedKeyObjectArray() {
		Short[] array = new Short[2];
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
	
	protected char get(short key) {
		return getMap().get(key);
	}

	protected final short k0() {
		return e0().getShortKey();
	}

	protected final char v0() {
		return e0().getCharValue();
	}

	protected final short k1() {
		return e1().getShortKey();
	}

	protected final char v1() {
		return e1().getCharValue();
	}

	protected final short k2() {
		return e2().getShortKey();
	}

	protected final char v2() {
		return e2().getCharValue();
	}

	protected final short k3() {
		return e3().getShortKey();
	}

	protected final char v3() {
		return e3().getCharValue();
	}

	protected final short k4() {
		return e4().getShortKey();
	}

	protected final char v4() {
		return e4().getCharValue();
	}
}