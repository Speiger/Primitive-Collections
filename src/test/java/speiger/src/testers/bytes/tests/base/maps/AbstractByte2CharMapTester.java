package speiger.src.testers.bytes.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.bytes.maps.abstracts.AbstractByte2CharMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2CharMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.bytes.generators.maps.TestByte2CharMapGenerator;
import speiger.src.testers.bytes.utils.ByteHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class AbstractByte2CharMapTester extends AbstractObjectContainerTester<Byte2CharMap.Entry, Byte2CharMap>
{
	protected TestByte2CharMapGenerator primitiveMapGenerator;

	protected Byte2CharMap getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Byte, Character>, ? extends Map.Entry<Byte, Character>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestByte2CharMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestByte2CharMapGenerator) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Byte2CharMap createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Byte2CharMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Byte2CharMap.Entry> getOrderedElements() {
		ObjectList<Byte2CharMap.Entry> list = new ObjectArrayList<>();
		for (ObjectIterator<Byte2CharMap.Entry> iter = primitiveMapGenerator.order(new ObjectArrayList<Byte2CharMap.Entry>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Byte2CharMap.Entry> actualContents() {
		return getMap().byte2CharEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Byte2CharMap.Entry[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Byte2CharMap resetContainer(Byte2CharMap newValue) {
		newValue.setDefaultReturnValue((char)-1);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(byte... elements) {
		for (byte element : elements) {
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
		
	protected ObjectCollection<Byte2CharMap.Entry> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Byte2CharMap.Entry> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Byte2CharMap.Entry... entries) {
		for (Byte2CharMap.Entry entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getByteKey() + " mapped to value " + entry.getCharValue(), valueEquals(getMap().get(entry.getByteKey()), entry.getCharValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Byte2CharMap.Entry> expected) {
		super.expectContents(expected);
		for (Byte2CharMap.Entry entry : expected) {
			assertEquals("Wrong value for key " + entry.getByteKey(), entry.getCharValue(), getMap().get(entry.getByteKey()));
		}
	}

	protected final void expectReplacement(Byte2CharMap.Entry newEntry) {
		ObjectList<Byte2CharMap.Entry> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Byte2CharMap.Entry> expected, Byte2CharMap.Entry newEntry) {
		for (ObjectListIterator<Byte2CharMap.Entry> i = expected.listIterator(); i.hasNext();) {
			if (ByteHelpers.equals(i.next().getByteKey(), newEntry.getByteKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getByteKey(), expected));
	}

	private static boolean valueEquals(char a, char b) {
		return a == b;
	}
	
	protected Byte2CharMap.Entry entry(byte key, char value) {
		return new AbstractByte2CharMap.BasicEntry(key, value);
	}
	
	protected byte[] emptyKeyArray() {
		return new byte[0];
	}
	
	protected byte[] createDisjointedKeyArray() {
		byte[] array = new byte[2];
		array[0] = k3();
		array[1] = k4();
		return array;
	}
	
	protected Byte[] emptyKeyObjectArray() {
		return new Byte[0];
	}
	
	protected Byte[] createDisjointedKeyObjectArray() {
		Byte[] array = new Byte[2];
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
	
	protected char get(byte key) {
		return getMap().get(key);
	}

	protected final byte k0() {
		return e0().getByteKey();
	}

	protected final char v0() {
		return e0().getCharValue();
	}

	protected final byte k1() {
		return e1().getByteKey();
	}

	protected final char v1() {
		return e1().getCharValue();
	}

	protected final byte k2() {
		return e2().getByteKey();
	}

	protected final char v2() {
		return e2().getCharValue();
	}

	protected final byte k3() {
		return e3().getByteKey();
	}

	protected final char v3() {
		return e3().getCharValue();
	}

	protected final byte k4() {
		return e4().getByteKey();
	}

	protected final char v4() {
		return e4().getCharValue();
	}
}