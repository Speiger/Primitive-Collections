package speiger.src.testers.ints.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.ints.maps.abstracts.AbstractInt2IntMap;
import speiger.src.collections.ints.maps.interfaces.Int2IntMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.ints.generators.maps.TestInt2IntMapGenerator;
import speiger.src.testers.ints.utils.IntHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
public class AbstractInt2IntMapTester extends AbstractObjectContainerTester<Int2IntMap.Entry, Int2IntMap> {

	protected TestInt2IntMapGenerator primitiveMapGenerator;

	protected Int2IntMap getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Integer, Integer>, ? extends Map.Entry<Integer, Integer>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestInt2IntMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestInt2IntMapGenerator) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Int2IntMap createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Int2IntMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Int2IntMap.Entry> getOrderedElements() {
		ObjectList<Int2IntMap.Entry> list = new ObjectArrayList<>();
		for (ObjectIterator<Int2IntMap.Entry> iter = primitiveMapGenerator.order(new ObjectArrayList<Int2IntMap.Entry>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Int2IntMap.Entry> actualContents() {
		return getMap().int2IntEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Int2IntMap.Entry[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Int2IntMap resetContainer(Int2IntMap newValue) {
		newValue.setDefaultReturnValue(-1);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(int... elements) {
		for (int element : elements) {
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
		
	protected ObjectCollection<Int2IntMap.Entry> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Int2IntMap.Entry> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Int2IntMap.Entry... entries) {
		for (Int2IntMap.Entry entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getIntKey() + " mapped to value " + entry.getIntValue(), valueEquals(getMap().get(entry.getIntKey()), entry.getIntValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Int2IntMap.Entry> expected) {
		super.expectContents(expected);
		for (Int2IntMap.Entry entry : expected) {
			assertEquals("Wrong value for key " + entry.getIntKey(), entry.getIntValue(), getMap().get(entry.getIntKey()));
		}
	}

	protected final void expectReplacement(Int2IntMap.Entry newEntry) {
		ObjectList<Int2IntMap.Entry> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Int2IntMap.Entry> expected, Int2IntMap.Entry newEntry) {
		for (ObjectListIterator<Int2IntMap.Entry> i = expected.listIterator(); i.hasNext();) {
			if (IntHelpers.equals(i.next().getIntKey(), newEntry.getIntKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getIntKey(), expected));
	}

	private static boolean valueEquals(int a, int b) {
		return a == b;
	}
	
	protected Int2IntMap.Entry entry(int key, int value) {
		return new AbstractInt2IntMap.BasicEntry(key, value);
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
	
	protected int get(int key) {
		return getMap().get(key);
	}

	protected final int k0() {
		return e0().getIntKey();
	}

	protected final int v0() {
		return e0().getIntValue();
	}

	protected final int k1() {
		return e1().getIntKey();
	}

	protected final int v1() {
		return e1().getIntValue();
	}

	protected final int k2() {
		return e2().getIntKey();
	}

	protected final int v2() {
		return e2().getIntValue();
	}

	protected final int k3() {
		return e3().getIntKey();
	}

	protected final int v3() {
		return e3().getIntValue();
	}

	protected final int k4() {
		return e4().getIntKey();
	}

	protected final int v4() {
		return e4().getIntValue();
	}
}