package speiger.src.testers.ints.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.ints.maps.abstracts.AbstractInt2ByteMap;
import speiger.src.collections.ints.maps.interfaces.Int2ByteMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.ints.generators.maps.TestInt2ByteMapGenerator;
import speiger.src.testers.ints.utils.IntHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
public class AbstractInt2ByteMapTester extends AbstractObjectContainerTester<Int2ByteMap.Entry, Int2ByteMap> {

	protected TestInt2ByteMapGenerator primitiveMapGenerator;

	protected Int2ByteMap getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Integer, Byte>, ? extends Map.Entry<Integer, Byte>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestInt2ByteMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestInt2ByteMapGenerator) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Int2ByteMap createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Int2ByteMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Int2ByteMap.Entry> getOrderedElements() {
		ObjectList<Int2ByteMap.Entry> list = new ObjectArrayList<>();
		for (ObjectIterator<Int2ByteMap.Entry> iter = primitiveMapGenerator.order(new ObjectArrayList<Int2ByteMap.Entry>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Int2ByteMap.Entry> actualContents() {
		return getMap().int2ByteEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Int2ByteMap.Entry[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Int2ByteMap resetContainer(Int2ByteMap newValue) {
		newValue.setDefaultReturnValue((byte)-1);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(int... elements) {
		for (int element : elements) {
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
		
	protected ObjectCollection<Int2ByteMap.Entry> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Int2ByteMap.Entry> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Int2ByteMap.Entry... entries) {
		for (Int2ByteMap.Entry entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getIntKey() + " mapped to value " + entry.getByteValue(), valueEquals(getMap().get(entry.getIntKey()), entry.getByteValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Int2ByteMap.Entry> expected) {
		super.expectContents(expected);
		for (Int2ByteMap.Entry entry : expected) {
			assertEquals("Wrong value for key " + entry.getIntKey(), entry.getByteValue(), getMap().get(entry.getIntKey()));
		}
	}

	protected final void expectReplacement(Int2ByteMap.Entry newEntry) {
		ObjectList<Int2ByteMap.Entry> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Int2ByteMap.Entry> expected, Int2ByteMap.Entry newEntry) {
		for (ObjectListIterator<Int2ByteMap.Entry> i = expected.listIterator(); i.hasNext();) {
			if (IntHelpers.equals(i.next().getIntKey(), newEntry.getIntKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getIntKey(), expected));
	}

	private static boolean valueEquals(byte a, byte b) {
		return a == b;
	}
	
	protected Int2ByteMap.Entry entry(int key, byte value) {
		return new AbstractInt2ByteMap.BasicEntry(key, value);
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
	
	protected byte get(int key) {
		return getMap().get(key);
	}

	protected final int k0() {
		return e0().getIntKey();
	}

	protected final byte v0() {
		return e0().getByteValue();
	}

	protected final int k1() {
		return e1().getIntKey();
	}

	protected final byte v1() {
		return e1().getByteValue();
	}

	protected final int k2() {
		return e2().getIntKey();
	}

	protected final byte v2() {
		return e2().getByteValue();
	}

	protected final int k3() {
		return e3().getIntKey();
	}

	protected final byte v3() {
		return e3().getByteValue();
	}

	protected final int k4() {
		return e4().getIntKey();
	}

	protected final byte v4() {
		return e4().getByteValue();
	}
}