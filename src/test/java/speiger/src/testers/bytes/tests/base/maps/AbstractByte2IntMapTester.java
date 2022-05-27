package speiger.src.testers.bytes.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.bytes.maps.abstracts.AbstractByte2IntMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2IntMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.bytes.generators.maps.TestByte2IntMapGenerator;
import speiger.src.testers.bytes.utils.ByteHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class AbstractByte2IntMapTester extends AbstractObjectContainerTester<Byte2IntMap.Entry, Byte2IntMap>
{
	protected TestByte2IntMapGenerator primitiveMapGenerator;

	protected Byte2IntMap getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Byte, Integer>, ? extends Map.Entry<Byte, Integer>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestByte2IntMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestByte2IntMapGenerator) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Byte2IntMap createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Byte2IntMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Byte2IntMap.Entry> getOrderedElements() {
		ObjectList<Byte2IntMap.Entry> list = new ObjectArrayList<>();
		for (ObjectIterator<Byte2IntMap.Entry> iter = primitiveMapGenerator.order(new ObjectArrayList<Byte2IntMap.Entry>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Byte2IntMap.Entry> actualContents() {
		return getMap().byte2IntEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Byte2IntMap.Entry[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Byte2IntMap resetContainer(Byte2IntMap newValue) {
		newValue.setDefaultReturnValue(-1);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(byte... elements) {
		for (byte element : elements) {
			assertFalse("Should not contain key " + element, getMap().containsKey(element));
		}
	}

	protected void expectMissingValues(int... elements) {
		for (int element : elements) {
			assertFalse("Should not contain value " + element, getMap().containsValue(element));
		}
	}
	
	protected int getNumEntries() {
		return getNumElements();
	}
		
	protected ObjectCollection<Byte2IntMap.Entry> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Byte2IntMap.Entry> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Byte2IntMap.Entry... entries) {
		for (Byte2IntMap.Entry entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getByteKey() + " mapped to value " + entry.getIntValue(), valueEquals(getMap().get(entry.getByteKey()), entry.getIntValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Byte2IntMap.Entry> expected) {
		super.expectContents(expected);
		for (Byte2IntMap.Entry entry : expected) {
			assertEquals("Wrong value for key " + entry.getByteKey(), entry.getIntValue(), getMap().get(entry.getByteKey()));
		}
	}

	protected final void expectReplacement(Byte2IntMap.Entry newEntry) {
		ObjectList<Byte2IntMap.Entry> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Byte2IntMap.Entry> expected, Byte2IntMap.Entry newEntry) {
		for (ObjectListIterator<Byte2IntMap.Entry> i = expected.listIterator(); i.hasNext();) {
			if (ByteHelpers.equals(i.next().getByteKey(), newEntry.getByteKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getByteKey(), expected));
	}

	private static boolean valueEquals(int a, int b) {
		return a == b;
	}
	
	protected Byte2IntMap.Entry entry(byte key, int value) {
		return new AbstractByte2IntMap.BasicEntry(key, value);
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
	
	protected int[] emptyValueArray() {
		return new int[0];
	}
	
	protected int[] createDisjointedValueArray() {
		int[] array = new int[2];
		array[0] = v3();
		array[1] = v4();
		return array;
	}
	
	protected Integer[] emptyValueObjectArray() {
		return new Integer[0];
	}
	
	protected Integer[] createDisjointedValueObjectArray() {
		Integer[] array = new Integer[2];
		array[0] = v3();
		array[1] = v4();
		return array;
	}
	
	protected int get(byte key) {
		return getMap().get(key);
	}

	protected final byte k0() {
		return e0().getByteKey();
	}

	protected final int v0() {
		return e0().getIntValue();
	}

	protected final byte k1() {
		return e1().getByteKey();
	}

	protected final int v1() {
		return e1().getIntValue();
	}

	protected final byte k2() {
		return e2().getByteKey();
	}

	protected final int v2() {
		return e2().getIntValue();
	}

	protected final byte k3() {
		return e3().getByteKey();
	}

	protected final int v3() {
		return e3().getIntValue();
	}

	protected final byte k4() {
		return e4().getByteKey();
	}

	protected final int v4() {
		return e4().getIntValue();
	}
}