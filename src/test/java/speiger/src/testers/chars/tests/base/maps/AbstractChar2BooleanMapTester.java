package speiger.src.testers.chars.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.chars.maps.abstracts.AbstractChar2BooleanMap;
import speiger.src.collections.chars.maps.interfaces.Char2BooleanMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.chars.generators.maps.TestChar2BooleanMapGenerator;
import speiger.src.testers.chars.utils.CharHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class AbstractChar2BooleanMapTester extends AbstractObjectContainerTester<Char2BooleanMap.Entry, Char2BooleanMap>
{
	protected TestChar2BooleanMapGenerator primitiveMapGenerator;

	protected Char2BooleanMap getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Character, Boolean>, ? extends Map.Entry<Character, Boolean>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestChar2BooleanMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestChar2BooleanMapGenerator) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Char2BooleanMap createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Char2BooleanMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Char2BooleanMap.Entry> getOrderedElements() {
		ObjectList<Char2BooleanMap.Entry> list = new ObjectArrayList<>();
		for (ObjectIterator<Char2BooleanMap.Entry> iter = primitiveMapGenerator.order(new ObjectArrayList<Char2BooleanMap.Entry>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Char2BooleanMap.Entry> actualContents() {
		return getMap().char2BooleanEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Char2BooleanMap.Entry[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Char2BooleanMap resetContainer(Char2BooleanMap newValue) {
		newValue.setDefaultReturnValue(false);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(char... elements) {
		for (char element : elements) {
			assertFalse("Should not contain key " + element, getMap().containsKey(element));
		}
	}

	protected void expectMissingValues(boolean... elements) {
		for (boolean element : elements) {
			assertFalse("Should not contain value " + element, getMap().containsValue(element));
		}
	}
	
	protected int getNumEntries() {
		return getNumElements();
	}
		
	protected ObjectCollection<Char2BooleanMap.Entry> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Char2BooleanMap.Entry> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Char2BooleanMap.Entry... entries) {
		for (Char2BooleanMap.Entry entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getCharKey() + " mapped to value " + entry.getBooleanValue(), valueEquals(getMap().get(entry.getCharKey()), entry.getBooleanValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Char2BooleanMap.Entry> expected) {
		super.expectContents(expected);
		for (Char2BooleanMap.Entry entry : expected) {
			assertEquals("Wrong value for key " + entry.getCharKey(), entry.getBooleanValue(), getMap().get(entry.getCharKey()));
		}
	}

	protected final void expectReplacement(Char2BooleanMap.Entry newEntry) {
		ObjectList<Char2BooleanMap.Entry> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Char2BooleanMap.Entry> expected, Char2BooleanMap.Entry newEntry) {
		for (ObjectListIterator<Char2BooleanMap.Entry> i = expected.listIterator(); i.hasNext();) {
			if (CharHelpers.equals(i.next().getCharKey(), newEntry.getCharKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getCharKey(), expected));
	}

	private static boolean valueEquals(boolean a, boolean b) {
		return a == b;
	}
	
	protected Char2BooleanMap.Entry entry(char key, boolean value) {
		return new AbstractChar2BooleanMap.BasicEntry(key, value);
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
	
	protected boolean[] emptyValueArray() {
		return new boolean[0];
	}
	
	protected boolean[] createDisjointedValueArray() {
		boolean[] array = new boolean[2];
		array[0] = v3();
		array[1] = v4();
		return array;
	}
	
	protected Boolean[] emptyValueObjectArray() {
		return new Boolean[0];
	}
	
	protected Boolean[] createDisjointedValueObjectArray() {
		Boolean[] array = new Boolean[2];
		array[0] = v3();
		array[1] = v4();
		return array;
	}
	
	protected boolean get(char key) {
		return getMap().get(key);
	}

	protected final char k0() {
		return e0().getCharKey();
	}

	protected final boolean v0() {
		return e0().getBooleanValue();
	}

	protected final char k1() {
		return e1().getCharKey();
	}

	protected final boolean v1() {
		return e1().getBooleanValue();
	}

	protected final char k2() {
		return e2().getCharKey();
	}

	protected final boolean v2() {
		return e2().getBooleanValue();
	}

	protected final char k3() {
		return e3().getCharKey();
	}

	protected final boolean v3() {
		return e3().getBooleanValue();
	}

	protected final char k4() {
		return e4().getCharKey();
	}

	protected final boolean v4() {
		return e4().getBooleanValue();
	}
}