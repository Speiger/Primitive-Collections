package speiger.src.testers.ints.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.ints.maps.abstracts.AbstractInt2ShortMap;
import speiger.src.collections.ints.maps.interfaces.Int2ShortMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.ints.generators.maps.TestInt2ShortMapGenerator;
import speiger.src.testers.ints.utils.IntHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
public class AbstractInt2ShortMapTester extends AbstractObjectContainerTester<Int2ShortMap.Entry, Int2ShortMap> {

	protected TestInt2ShortMapGenerator primitiveMapGenerator;

	protected Int2ShortMap getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Integer, Short>, ? extends Map.Entry<Integer, Short>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestInt2ShortMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestInt2ShortMapGenerator) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Int2ShortMap createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Int2ShortMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Int2ShortMap.Entry> getOrderedElements() {
		ObjectList<Int2ShortMap.Entry> list = new ObjectArrayList<>();
		for (ObjectIterator<Int2ShortMap.Entry> iter = primitiveMapGenerator.order(new ObjectArrayList<Int2ShortMap.Entry>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Int2ShortMap.Entry> actualContents() {
		return getMap().int2ShortEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Int2ShortMap.Entry[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Int2ShortMap resetContainer(Int2ShortMap newValue) {
		newValue.setDefaultReturnValue((short)-1);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(int... elements) {
		for (int element : elements) {
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
		
	protected ObjectCollection<Int2ShortMap.Entry> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Int2ShortMap.Entry> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Int2ShortMap.Entry... entries) {
		for (Int2ShortMap.Entry entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getIntKey() + " mapped to value " + entry.getShortValue(), valueEquals(getMap().get(entry.getIntKey()), entry.getShortValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Int2ShortMap.Entry> expected) {
		super.expectContents(expected);
		for (Int2ShortMap.Entry entry : expected) {
			assertEquals("Wrong value for key " + entry.getIntKey(), entry.getShortValue(), getMap().get(entry.getIntKey()));
		}
	}

	protected final void expectReplacement(Int2ShortMap.Entry newEntry) {
		ObjectList<Int2ShortMap.Entry> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Int2ShortMap.Entry> expected, Int2ShortMap.Entry newEntry) {
		for (ObjectListIterator<Int2ShortMap.Entry> i = expected.listIterator(); i.hasNext();) {
			if (IntHelpers.equals(i.next().getIntKey(), newEntry.getIntKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getIntKey(), expected));
	}

	private static boolean valueEquals(short a, short b) {
		return a == b;
	}
	
	protected Int2ShortMap.Entry entry(int key, short value) {
		return new AbstractInt2ShortMap.BasicEntry(key, value);
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
	
	protected short get(int key) {
		return getMap().get(key);
	}

	protected final int k0() {
		return e0().getIntKey();
	}

	protected final short v0() {
		return e0().getShortValue();
	}

	protected final int k1() {
		return e1().getIntKey();
	}

	protected final short v1() {
		return e1().getShortValue();
	}

	protected final int k2() {
		return e2().getIntKey();
	}

	protected final short v2() {
		return e2().getShortValue();
	}

	protected final int k3() {
		return e3().getIntKey();
	}

	protected final short v3() {
		return e3().getShortValue();
	}

	protected final int k4() {
		return e4().getIntKey();
	}

	protected final short v4() {
		return e4().getShortValue();
	}
}