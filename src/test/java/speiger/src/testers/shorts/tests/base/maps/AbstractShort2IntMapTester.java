package speiger.src.testers.shorts.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.shorts.maps.abstracts.AbstractShort2IntMap;
import speiger.src.collections.shorts.maps.interfaces.Short2IntMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.shorts.generators.maps.TestShort2IntMapGenerator;
import speiger.src.testers.shorts.utils.ShortHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class AbstractShort2IntMapTester extends AbstractObjectContainerTester<Short2IntMap.Entry, Short2IntMap>
{
	protected TestShort2IntMapGenerator primitiveMapGenerator;

	protected Short2IntMap getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Short, Integer>, ? extends Map.Entry<Short, Integer>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestShort2IntMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestShort2IntMapGenerator) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Short2IntMap createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Short2IntMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Short2IntMap.Entry> getOrderedElements() {
		ObjectList<Short2IntMap.Entry> list = new ObjectArrayList<>();
		for (ObjectIterator<Short2IntMap.Entry> iter = primitiveMapGenerator.order(new ObjectArrayList<Short2IntMap.Entry>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Short2IntMap.Entry> actualContents() {
		return getMap().short2IntEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Short2IntMap.Entry[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Short2IntMap resetContainer(Short2IntMap newValue) {
		newValue.setDefaultReturnValue(-1);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(short... elements) {
		for (short element : elements) {
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
		
	protected ObjectCollection<Short2IntMap.Entry> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Short2IntMap.Entry> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Short2IntMap.Entry... entries) {
		for (Short2IntMap.Entry entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getShortKey() + " mapped to value " + entry.getIntValue(), valueEquals(getMap().get(entry.getShortKey()), entry.getIntValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Short2IntMap.Entry> expected) {
		super.expectContents(expected);
		for (Short2IntMap.Entry entry : expected) {
			assertEquals("Wrong value for key " + entry.getShortKey(), entry.getIntValue(), getMap().get(entry.getShortKey()));
		}
	}

	protected final void expectReplacement(Short2IntMap.Entry newEntry) {
		ObjectList<Short2IntMap.Entry> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Short2IntMap.Entry> expected, Short2IntMap.Entry newEntry) {
		for (ObjectListIterator<Short2IntMap.Entry> i = expected.listIterator(); i.hasNext();) {
			if (ShortHelpers.equals(i.next().getShortKey(), newEntry.getShortKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getShortKey(), expected));
	}

	private static boolean valueEquals(int a, int b) {
		return a == b;
	}
	
	protected Short2IntMap.Entry entry(short key, int value) {
		return new AbstractShort2IntMap.BasicEntry(key, value);
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
	
	protected int get(short key) {
		return getMap().get(key);
	}

	protected final short k0() {
		return e0().getShortKey();
	}

	protected final int v0() {
		return e0().getIntValue();
	}

	protected final short k1() {
		return e1().getShortKey();
	}

	protected final int v1() {
		return e1().getIntValue();
	}

	protected final short k2() {
		return e2().getShortKey();
	}

	protected final int v2() {
		return e2().getIntValue();
	}

	protected final short k3() {
		return e3().getShortKey();
	}

	protected final int v3() {
		return e3().getIntValue();
	}

	protected final short k4() {
		return e4().getShortKey();
	}

	protected final int v4() {
		return e4().getIntValue();
	}
}