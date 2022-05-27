package speiger.src.testers.ints.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.ints.maps.abstracts.AbstractInt2CharMap;
import speiger.src.collections.ints.maps.interfaces.Int2CharMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.ints.generators.maps.TestInt2CharMapGenerator;
import speiger.src.testers.ints.utils.IntHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
public class AbstractInt2CharMapTester extends AbstractObjectContainerTester<Int2CharMap.Entry, Int2CharMap> {

	protected TestInt2CharMapGenerator primitiveMapGenerator;

	protected Int2CharMap getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Integer, Character>, ? extends Map.Entry<Integer, Character>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestInt2CharMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestInt2CharMapGenerator) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Int2CharMap createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Int2CharMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Int2CharMap.Entry> getOrderedElements() {
		ObjectList<Int2CharMap.Entry> list = new ObjectArrayList<>();
		for (ObjectIterator<Int2CharMap.Entry> iter = primitiveMapGenerator.order(new ObjectArrayList<Int2CharMap.Entry>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Int2CharMap.Entry> actualContents() {
		return getMap().int2CharEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Int2CharMap.Entry[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Int2CharMap resetContainer(Int2CharMap newValue) {
		newValue.setDefaultReturnValue((char)-1);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(int... elements) {
		for (int element : elements) {
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
		
	protected ObjectCollection<Int2CharMap.Entry> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Int2CharMap.Entry> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Int2CharMap.Entry... entries) {
		for (Int2CharMap.Entry entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getIntKey() + " mapped to value " + entry.getCharValue(), valueEquals(getMap().get(entry.getIntKey()), entry.getCharValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Int2CharMap.Entry> expected) {
		super.expectContents(expected);
		for (Int2CharMap.Entry entry : expected) {
			assertEquals("Wrong value for key " + entry.getIntKey(), entry.getCharValue(), getMap().get(entry.getIntKey()));
		}
	}

	protected final void expectReplacement(Int2CharMap.Entry newEntry) {
		ObjectList<Int2CharMap.Entry> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Int2CharMap.Entry> expected, Int2CharMap.Entry newEntry) {
		for (ObjectListIterator<Int2CharMap.Entry> i = expected.listIterator(); i.hasNext();) {
			if (IntHelpers.equals(i.next().getIntKey(), newEntry.getIntKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getIntKey(), expected));
	}

	private static boolean valueEquals(char a, char b) {
		return a == b;
	}
	
	protected Int2CharMap.Entry entry(int key, char value) {
		return new AbstractInt2CharMap.BasicEntry(key, value);
	}
	
	protected int[] emptyKeyArray() {
		return new int[0];
	}
	
	protected int[] createDisjointedKeyArray() {
		int[] array = new int[2];
		array[0] = k3();
		array[1] = k4();
		return array;
	}
	
	protected Integer[] emptyKeyObjectArray() {
		return new Integer[0];
	}
	
	protected Integer[] createDisjointedKeyObjectArray() {
		Integer[] array = new Integer[2];
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
	
	protected char get(int key) {
		return getMap().get(key);
	}

	protected final int k0() {
		return e0().getIntKey();
	}

	protected final char v0() {
		return e0().getCharValue();
	}

	protected final int k1() {
		return e1().getIntKey();
	}

	protected final char v1() {
		return e1().getCharValue();
	}

	protected final int k2() {
		return e2().getIntKey();
	}

	protected final char v2() {
		return e2().getCharValue();
	}

	protected final int k3() {
		return e3().getIntKey();
	}

	protected final char v3() {
		return e3().getCharValue();
	}

	protected final int k4() {
		return e4().getIntKey();
	}

	protected final char v4() {
		return e4().getCharValue();
	}
}