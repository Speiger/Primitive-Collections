package speiger.src.testers.bytes.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.bytes.maps.abstracts.AbstractByte2BooleanMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2BooleanMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.bytes.generators.maps.TestByte2BooleanMapGenerator;
import speiger.src.testers.bytes.utils.ByteHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class AbstractByte2BooleanMapTester extends AbstractObjectContainerTester<Byte2BooleanMap.Entry, Byte2BooleanMap>
{
	protected TestByte2BooleanMapGenerator primitiveMapGenerator;

	protected Byte2BooleanMap getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Byte, Boolean>, ? extends Map.Entry<Byte, Boolean>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestByte2BooleanMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestByte2BooleanMapGenerator) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Byte2BooleanMap createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Byte2BooleanMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Byte2BooleanMap.Entry> getOrderedElements() {
		ObjectList<Byte2BooleanMap.Entry> list = new ObjectArrayList<>();
		for (ObjectIterator<Byte2BooleanMap.Entry> iter = primitiveMapGenerator.order(new ObjectArrayList<Byte2BooleanMap.Entry>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Byte2BooleanMap.Entry> actualContents() {
		return getMap().byte2BooleanEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Byte2BooleanMap.Entry[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Byte2BooleanMap resetContainer(Byte2BooleanMap newValue) {
		newValue.setDefaultReturnValue(false);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(byte... elements) {
		for (byte element : elements) {
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
		
	protected ObjectCollection<Byte2BooleanMap.Entry> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Byte2BooleanMap.Entry> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Byte2BooleanMap.Entry... entries) {
		for (Byte2BooleanMap.Entry entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getByteKey() + " mapped to value " + entry.getBooleanValue(), valueEquals(getMap().get(entry.getByteKey()), entry.getBooleanValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Byte2BooleanMap.Entry> expected) {
		super.expectContents(expected);
		for (Byte2BooleanMap.Entry entry : expected) {
			assertEquals("Wrong value for key " + entry.getByteKey(), entry.getBooleanValue(), getMap().get(entry.getByteKey()));
		}
	}

	protected final void expectReplacement(Byte2BooleanMap.Entry newEntry) {
		ObjectList<Byte2BooleanMap.Entry> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Byte2BooleanMap.Entry> expected, Byte2BooleanMap.Entry newEntry) {
		for (ObjectListIterator<Byte2BooleanMap.Entry> i = expected.listIterator(); i.hasNext();) {
			if (ByteHelpers.equals(i.next().getByteKey(), newEntry.getByteKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getByteKey(), expected));
	}

	private static boolean valueEquals(boolean a, boolean b) {
		return a == b;
	}
	
	protected Byte2BooleanMap.Entry entry(byte key, boolean value) {
		return new AbstractByte2BooleanMap.BasicEntry(key, value);
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
	
	protected boolean get(byte key) {
		return getMap().get(key);
	}

	protected final byte k0() {
		return e0().getByteKey();
	}

	protected final boolean v0() {
		return e0().getBooleanValue();
	}

	protected final byte k1() {
		return e1().getByteKey();
	}

	protected final boolean v1() {
		return e1().getBooleanValue();
	}

	protected final byte k2() {
		return e2().getByteKey();
	}

	protected final boolean v2() {
		return e2().getBooleanValue();
	}

	protected final byte k3() {
		return e3().getByteKey();
	}

	protected final boolean v3() {
		return e3().getBooleanValue();
	}

	protected final byte k4() {
		return e4().getByteKey();
	}

	protected final boolean v4() {
		return e4().getBooleanValue();
	}
}