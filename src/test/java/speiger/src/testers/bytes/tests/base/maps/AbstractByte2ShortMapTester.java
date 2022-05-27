package speiger.src.testers.bytes.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.bytes.maps.abstracts.AbstractByte2ShortMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2ShortMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.bytes.generators.maps.TestByte2ShortMapGenerator;
import speiger.src.testers.bytes.utils.ByteHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class AbstractByte2ShortMapTester extends AbstractObjectContainerTester<Byte2ShortMap.Entry, Byte2ShortMap>
{
	protected TestByte2ShortMapGenerator primitiveMapGenerator;

	protected Byte2ShortMap getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Byte, Short>, ? extends Map.Entry<Byte, Short>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestByte2ShortMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestByte2ShortMapGenerator) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Byte2ShortMap createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Byte2ShortMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Byte2ShortMap.Entry> getOrderedElements() {
		ObjectList<Byte2ShortMap.Entry> list = new ObjectArrayList<>();
		for (ObjectIterator<Byte2ShortMap.Entry> iter = primitiveMapGenerator.order(new ObjectArrayList<Byte2ShortMap.Entry>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Byte2ShortMap.Entry> actualContents() {
		return getMap().byte2ShortEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Byte2ShortMap.Entry[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Byte2ShortMap resetContainer(Byte2ShortMap newValue) {
		newValue.setDefaultReturnValue((short)-1);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(byte... elements) {
		for (byte element : elements) {
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
		
	protected ObjectCollection<Byte2ShortMap.Entry> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Byte2ShortMap.Entry> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Byte2ShortMap.Entry... entries) {
		for (Byte2ShortMap.Entry entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getByteKey() + " mapped to value " + entry.getShortValue(), valueEquals(getMap().get(entry.getByteKey()), entry.getShortValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Byte2ShortMap.Entry> expected) {
		super.expectContents(expected);
		for (Byte2ShortMap.Entry entry : expected) {
			assertEquals("Wrong value for key " + entry.getByteKey(), entry.getShortValue(), getMap().get(entry.getByteKey()));
		}
	}

	protected final void expectReplacement(Byte2ShortMap.Entry newEntry) {
		ObjectList<Byte2ShortMap.Entry> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Byte2ShortMap.Entry> expected, Byte2ShortMap.Entry newEntry) {
		for (ObjectListIterator<Byte2ShortMap.Entry> i = expected.listIterator(); i.hasNext();) {
			if (ByteHelpers.equals(i.next().getByteKey(), newEntry.getByteKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getByteKey(), expected));
	}

	private static boolean valueEquals(short a, short b) {
		return a == b;
	}
	
	protected Byte2ShortMap.Entry entry(byte key, short value) {
		return new AbstractByte2ShortMap.BasicEntry(key, value);
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
	
	protected short get(byte key) {
		return getMap().get(key);
	}

	protected final byte k0() {
		return e0().getByteKey();
	}

	protected final short v0() {
		return e0().getShortValue();
	}

	protected final byte k1() {
		return e1().getByteKey();
	}

	protected final short v1() {
		return e1().getShortValue();
	}

	protected final byte k2() {
		return e2().getByteKey();
	}

	protected final short v2() {
		return e2().getShortValue();
	}

	protected final byte k3() {
		return e3().getByteKey();
	}

	protected final short v3() {
		return e3().getShortValue();
	}

	protected final byte k4() {
		return e4().getByteKey();
	}

	protected final short v4() {
		return e4().getShortValue();
	}
}