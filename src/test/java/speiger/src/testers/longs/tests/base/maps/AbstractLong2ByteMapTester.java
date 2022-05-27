package speiger.src.testers.longs.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.longs.maps.abstracts.AbstractLong2ByteMap;
import speiger.src.collections.longs.maps.interfaces.Long2ByteMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.longs.generators.maps.TestLong2ByteMapGenerator;
import speiger.src.testers.longs.utils.LongHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class AbstractLong2ByteMapTester extends AbstractObjectContainerTester<Long2ByteMap.Entry, Long2ByteMap>
{
	protected TestLong2ByteMapGenerator primitiveMapGenerator;

	protected Long2ByteMap getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Long, Byte>, ? extends Map.Entry<Long, Byte>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestLong2ByteMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestLong2ByteMapGenerator) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Long2ByteMap createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Long2ByteMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Long2ByteMap.Entry> getOrderedElements() {
		ObjectList<Long2ByteMap.Entry> list = new ObjectArrayList<>();
		for (ObjectIterator<Long2ByteMap.Entry> iter = primitiveMapGenerator.order(new ObjectArrayList<Long2ByteMap.Entry>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Long2ByteMap.Entry> actualContents() {
		return getMap().long2ByteEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Long2ByteMap.Entry[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Long2ByteMap resetContainer(Long2ByteMap newValue) {
		newValue.setDefaultReturnValue((byte)-1);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(long... elements) {
		for (long element : elements) {
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
		
	protected ObjectCollection<Long2ByteMap.Entry> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Long2ByteMap.Entry> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Long2ByteMap.Entry... entries) {
		for (Long2ByteMap.Entry entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getLongKey() + " mapped to value " + entry.getByteValue(), valueEquals(getMap().get(entry.getLongKey()), entry.getByteValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Long2ByteMap.Entry> expected) {
		super.expectContents(expected);
		for (Long2ByteMap.Entry entry : expected) {
			assertEquals("Wrong value for key " + entry.getLongKey(), entry.getByteValue(), getMap().get(entry.getLongKey()));
		}
	}

	protected final void expectReplacement(Long2ByteMap.Entry newEntry) {
		ObjectList<Long2ByteMap.Entry> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Long2ByteMap.Entry> expected, Long2ByteMap.Entry newEntry) {
		for (ObjectListIterator<Long2ByteMap.Entry> i = expected.listIterator(); i.hasNext();) {
			if (LongHelpers.equals(i.next().getLongKey(), newEntry.getLongKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getLongKey(), expected));
	}

	private static boolean valueEquals(byte a, byte b) {
		return a == b;
	}
	
	protected Long2ByteMap.Entry entry(long key, byte value) {
		return new AbstractLong2ByteMap.BasicEntry(key, value);
	}
	
	protected long[] emptyKeyArray() {
		return new long[0];
	}
	
	protected long[] createDisjointedKeyArray() {
		long[] array = new long[2];
		array[0] = k3();
		array[1] = k4();
		return array;
	}
	
	protected Long[] emptyKeyObjectArray() {
		return new Long[0];
	}
	
	protected Long[] createDisjointedKeyObjectArray() {
		Long[] array = new Long[2];
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
	
	protected byte get(long key) {
		return getMap().get(key);
	}

	protected final long k0() {
		return e0().getLongKey();
	}

	protected final byte v0() {
		return e0().getByteValue();
	}

	protected final long k1() {
		return e1().getLongKey();
	}

	protected final byte v1() {
		return e1().getByteValue();
	}

	protected final long k2() {
		return e2().getLongKey();
	}

	protected final byte v2() {
		return e2().getByteValue();
	}

	protected final long k3() {
		return e3().getLongKey();
	}

	protected final byte v3() {
		return e3().getByteValue();
	}

	protected final long k4() {
		return e4().getLongKey();
	}

	protected final byte v4() {
		return e4().getByteValue();
	}
}