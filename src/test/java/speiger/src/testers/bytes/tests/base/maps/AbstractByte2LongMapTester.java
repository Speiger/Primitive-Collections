package speiger.src.testers.bytes.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.bytes.maps.abstracts.AbstractByte2LongMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2LongMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.bytes.generators.maps.TestByte2LongMapGenerator;
import speiger.src.testers.bytes.utils.ByteHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
public class AbstractByte2LongMapTester extends AbstractObjectContainerTester<Byte2LongMap.Entry, Byte2LongMap> {

	protected TestByte2LongMapGenerator primitiveMapGenerator;

	protected Byte2LongMap getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Byte, Long>, ? extends Map.Entry<Byte, Long>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestByte2LongMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestByte2LongMapGenerator) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Byte2LongMap createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Byte2LongMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Byte2LongMap.Entry> getOrderedElements() {
		ObjectList<Byte2LongMap.Entry> list = new ObjectArrayList<>();
		for (ObjectIterator<Byte2LongMap.Entry> iter = primitiveMapGenerator.order(new ObjectArrayList<Byte2LongMap.Entry>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Byte2LongMap.Entry> actualContents() {
		return getMap().byte2LongEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Byte2LongMap.Entry[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Byte2LongMap resetContainer(Byte2LongMap newValue) {
		newValue.setDefaultReturnValue(-1L);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(byte... elements) {
		for (byte element : elements) {
			assertFalse("Should not contain key " + element, getMap().containsKey(element));
		}
	}

	protected void expectMissingValues(long... elements) {
		for (long element : elements) {
			assertFalse("Should not contain value " + element, getMap().containsValue(element));
		}
	}
	
	protected int getNumEntries() {
		return getNumElements();
	}
		
	protected ObjectCollection<Byte2LongMap.Entry> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Byte2LongMap.Entry> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Byte2LongMap.Entry... entries) {
		for (Byte2LongMap.Entry entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getByteKey() + " mapped to value " + entry.getLongValue(), valueEquals(getMap().get(entry.getByteKey()), entry.getLongValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Byte2LongMap.Entry> expected) {
		super.expectContents(expected);
		for (Byte2LongMap.Entry entry : expected) {
			assertEquals("Wrong value for key " + entry.getByteKey(), entry.getLongValue(), getMap().get(entry.getByteKey()));
		}
	}

	protected final void expectReplacement(Byte2LongMap.Entry newEntry) {
		ObjectList<Byte2LongMap.Entry> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Byte2LongMap.Entry> expected, Byte2LongMap.Entry newEntry) {
		for (ObjectListIterator<Byte2LongMap.Entry> i = expected.listIterator(); i.hasNext();) {
			if (ByteHelpers.equals(i.next().getByteKey(), newEntry.getByteKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getByteKey(), expected));
	}

	private static boolean valueEquals(long a, long b) {
		return a == b;
	}
	
	protected Byte2LongMap.Entry entry(byte key, long value) {
		return new AbstractByte2LongMap.BasicEntry(key, value);
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
	
	protected long[] emptyValueArray() {
		return new long[0];
	}
	
	protected long[] createDisjointedValueArray() {
		long[] array = new long[2];
		array[0] = v3();
		array[1] = v4();
		return array;
	}
	
	protected Long[] emptyValueObjectArray() {
		return new Long[0];
	}
	
	protected Long[] createDisjointedValueObjectArray() {
		Long[] array = new Long[2];
		array[0] = v3();
		array[1] = v4();
		return array;
	}
	
	protected long get(byte key) {
		return getMap().get(key);
	}

	protected final byte k0() {
		return e0().getByteKey();
	}

	protected final long v0() {
		return e0().getLongValue();
	}

	protected final byte k1() {
		return e1().getByteKey();
	}

	protected final long v1() {
		return e1().getLongValue();
	}

	protected final byte k2() {
		return e2().getByteKey();
	}

	protected final long v2() {
		return e2().getLongValue();
	}

	protected final byte k3() {
		return e3().getByteKey();
	}

	protected final long v3() {
		return e3().getLongValue();
	}

	protected final byte k4() {
		return e4().getByteKey();
	}

	protected final long v4() {
		return e4().getLongValue();
	}
}