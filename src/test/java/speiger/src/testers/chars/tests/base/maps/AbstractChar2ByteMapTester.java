package speiger.src.testers.chars.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.chars.maps.abstracts.AbstractChar2ByteMap;
import speiger.src.collections.chars.maps.interfaces.Char2ByteMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.chars.generators.maps.TestChar2ByteMapGenerator;
import speiger.src.testers.chars.utils.CharHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class AbstractChar2ByteMapTester extends AbstractObjectContainerTester<Char2ByteMap.Entry, Char2ByteMap>
{
	protected TestChar2ByteMapGenerator primitiveMapGenerator;

	protected Char2ByteMap getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Character, Byte>, ? extends Map.Entry<Character, Byte>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestChar2ByteMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestChar2ByteMapGenerator) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Char2ByteMap createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Char2ByteMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Char2ByteMap.Entry> getOrderedElements() {
		ObjectList<Char2ByteMap.Entry> list = new ObjectArrayList<>();
		for (ObjectIterator<Char2ByteMap.Entry> iter = primitiveMapGenerator.order(new ObjectArrayList<Char2ByteMap.Entry>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Char2ByteMap.Entry> actualContents() {
		return getMap().char2ByteEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Char2ByteMap.Entry[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Char2ByteMap resetContainer(Char2ByteMap newValue) {
		newValue.setDefaultReturnValue((byte)-1);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(char... elements) {
		for (char element : elements) {
			assertFalse("Should not contain key " + element, getMap().containsKey(element));
		}
	}

	protected void expectMissingValues(byte... elements) {
		for (byte element : elements) {
			assertFalse("Should not contain value " + element, getMap().containsValue(element));
		}
	}
	
	protected int getNumEntries() {
		return getNumElements();
	}
		
	protected ObjectCollection<Char2ByteMap.Entry> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Char2ByteMap.Entry> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Char2ByteMap.Entry... entries) {
		for (Char2ByteMap.Entry entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getCharKey() + " mapped to value " + entry.getByteValue(), valueEquals(getMap().get(entry.getCharKey()), entry.getByteValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Char2ByteMap.Entry> expected) {
		super.expectContents(expected);
		for (Char2ByteMap.Entry entry : expected) {
			assertEquals("Wrong value for key " + entry.getCharKey(), entry.getByteValue(), getMap().get(entry.getCharKey()));
		}
	}

	protected final void expectReplacement(Char2ByteMap.Entry newEntry) {
		ObjectList<Char2ByteMap.Entry> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Char2ByteMap.Entry> expected, Char2ByteMap.Entry newEntry) {
		for (ObjectListIterator<Char2ByteMap.Entry> i = expected.listIterator(); i.hasNext();) {
			if (CharHelpers.equals(i.next().getCharKey(), newEntry.getCharKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getCharKey(), expected));
	}

	private static boolean valueEquals(byte a, byte b) {
		return a == b;
	}
	
	protected Char2ByteMap.Entry entry(char key, byte value) {
		return new AbstractChar2ByteMap.BasicEntry(key, value);
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
	
	protected byte[] emptyValueArray() {
		return new byte[0];
	}
	
	protected byte[] createDisjointedValueArray() {
		byte[] array = new byte[2];
		array[0] = v3();
		array[1] = v4();
		return array;
	}
	
	protected Byte[] emptyValueObjectArray() {
		return new Byte[0];
	}
	
	protected Byte[] createDisjointedValueObjectArray() {
		Byte[] array = new Byte[2];
		array[0] = v3();
		array[1] = v4();
		return array;
	}
	
	protected byte get(char key) {
		return getMap().get(key);
	}

	protected final char k0() {
		return e0().getCharKey();
	}

	protected final byte v0() {
		return e0().getByteValue();
	}

	protected final char k1() {
		return e1().getCharKey();
	}

	protected final byte v1() {
		return e1().getByteValue();
	}

	protected final char k2() {
		return e2().getCharKey();
	}

	protected final byte v2() {
		return e2().getByteValue();
	}

	protected final char k3() {
		return e3().getCharKey();
	}

	protected final byte v3() {
		return e3().getByteValue();
	}

	protected final char k4() {
		return e4().getCharKey();
	}

	protected final byte v4() {
		return e4().getByteValue();
	}
}