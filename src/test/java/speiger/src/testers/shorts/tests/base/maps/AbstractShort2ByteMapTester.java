package speiger.src.testers.shorts.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.shorts.maps.abstracts.AbstractShort2ByteMap;
import speiger.src.collections.shorts.maps.interfaces.Short2ByteMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.shorts.generators.maps.TestShort2ByteMapGenerator;
import speiger.src.testers.shorts.utils.ShortHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class AbstractShort2ByteMapTester extends AbstractObjectContainerTester<Short2ByteMap.Entry, Short2ByteMap>
{
	protected TestShort2ByteMapGenerator primitiveMapGenerator;

	protected Short2ByteMap getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Short, Byte>, ? extends Map.Entry<Short, Byte>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestShort2ByteMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestShort2ByteMapGenerator) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Short2ByteMap createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Short2ByteMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Short2ByteMap.Entry> getOrderedElements() {
		ObjectList<Short2ByteMap.Entry> list = new ObjectArrayList<>();
		for (ObjectIterator<Short2ByteMap.Entry> iter = primitiveMapGenerator.order(new ObjectArrayList<Short2ByteMap.Entry>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Short2ByteMap.Entry> actualContents() {
		return getMap().short2ByteEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Short2ByteMap.Entry[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Short2ByteMap resetContainer(Short2ByteMap newValue) {
		newValue.setDefaultReturnValue((byte)-1);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(short... elements) {
		for (short element : elements) {
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
		
	protected ObjectCollection<Short2ByteMap.Entry> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Short2ByteMap.Entry> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Short2ByteMap.Entry... entries) {
		for (Short2ByteMap.Entry entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getShortKey() + " mapped to value " + entry.getByteValue(), valueEquals(getMap().get(entry.getShortKey()), entry.getByteValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Short2ByteMap.Entry> expected) {
		super.expectContents(expected);
		for (Short2ByteMap.Entry entry : expected) {
			assertEquals("Wrong value for key " + entry.getShortKey(), entry.getByteValue(), getMap().get(entry.getShortKey()));
		}
	}

	protected final void expectReplacement(Short2ByteMap.Entry newEntry) {
		ObjectList<Short2ByteMap.Entry> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Short2ByteMap.Entry> expected, Short2ByteMap.Entry newEntry) {
		for (ObjectListIterator<Short2ByteMap.Entry> i = expected.listIterator(); i.hasNext();) {
			if (ShortHelpers.equals(i.next().getShortKey(), newEntry.getShortKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getShortKey(), expected));
	}

	private static boolean valueEquals(byte a, byte b) {
		return a == b;
	}
	
	protected Short2ByteMap.Entry entry(short key, byte value) {
		return new AbstractShort2ByteMap.BasicEntry(key, value);
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
	
	protected byte get(short key) {
		return getMap().get(key);
	}

	protected final short k0() {
		return e0().getShortKey();
	}

	protected final byte v0() {
		return e0().getByteValue();
	}

	protected final short k1() {
		return e1().getShortKey();
	}

	protected final byte v1() {
		return e1().getByteValue();
	}

	protected final short k2() {
		return e2().getShortKey();
	}

	protected final byte v2() {
		return e2().getByteValue();
	}

	protected final short k3() {
		return e3().getShortKey();
	}

	protected final byte v3() {
		return e3().getByteValue();
	}

	protected final short k4() {
		return e4().getShortKey();
	}

	protected final byte v4() {
		return e4().getByteValue();
	}
}