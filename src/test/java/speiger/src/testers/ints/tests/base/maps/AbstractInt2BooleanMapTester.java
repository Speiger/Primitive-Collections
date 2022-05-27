package speiger.src.testers.ints.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.ints.maps.abstracts.AbstractInt2BooleanMap;
import speiger.src.collections.ints.maps.interfaces.Int2BooleanMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.ints.generators.maps.TestInt2BooleanMapGenerator;
import speiger.src.testers.ints.utils.IntHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class AbstractInt2BooleanMapTester extends AbstractObjectContainerTester<Int2BooleanMap.Entry, Int2BooleanMap>
{
	protected TestInt2BooleanMapGenerator primitiveMapGenerator;

	protected Int2BooleanMap getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Integer, Boolean>, ? extends Map.Entry<Integer, Boolean>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestInt2BooleanMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestInt2BooleanMapGenerator) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Int2BooleanMap createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Int2BooleanMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Int2BooleanMap.Entry> getOrderedElements() {
		ObjectList<Int2BooleanMap.Entry> list = new ObjectArrayList<>();
		for (ObjectIterator<Int2BooleanMap.Entry> iter = primitiveMapGenerator.order(new ObjectArrayList<Int2BooleanMap.Entry>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Int2BooleanMap.Entry> actualContents() {
		return getMap().int2BooleanEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Int2BooleanMap.Entry[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Int2BooleanMap resetContainer(Int2BooleanMap newValue) {
		newValue.setDefaultReturnValue(false);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(int... elements) {
		for (int element : elements) {
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
		
	protected ObjectCollection<Int2BooleanMap.Entry> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Int2BooleanMap.Entry> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Int2BooleanMap.Entry... entries) {
		for (Int2BooleanMap.Entry entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getIntKey() + " mapped to value " + entry.getBooleanValue(), valueEquals(getMap().get(entry.getIntKey()), entry.getBooleanValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Int2BooleanMap.Entry> expected) {
		super.expectContents(expected);
		for (Int2BooleanMap.Entry entry : expected) {
			assertEquals("Wrong value for key " + entry.getIntKey(), entry.getBooleanValue(), getMap().get(entry.getIntKey()));
		}
	}

	protected final void expectReplacement(Int2BooleanMap.Entry newEntry) {
		ObjectList<Int2BooleanMap.Entry> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Int2BooleanMap.Entry> expected, Int2BooleanMap.Entry newEntry) {
		for (ObjectListIterator<Int2BooleanMap.Entry> i = expected.listIterator(); i.hasNext();) {
			if (IntHelpers.equals(i.next().getIntKey(), newEntry.getIntKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getIntKey(), expected));
	}

	private static boolean valueEquals(boolean a, boolean b) {
		return a == b;
	}
	
	protected Int2BooleanMap.Entry entry(int key, boolean value) {
		return new AbstractInt2BooleanMap.BasicEntry(key, value);
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
	
	protected boolean get(int key) {
		return getMap().get(key);
	}

	protected final int k0() {
		return e0().getIntKey();
	}

	protected final boolean v0() {
		return e0().getBooleanValue();
	}

	protected final int k1() {
		return e1().getIntKey();
	}

	protected final boolean v1() {
		return e1().getBooleanValue();
	}

	protected final int k2() {
		return e2().getIntKey();
	}

	protected final boolean v2() {
		return e2().getBooleanValue();
	}

	protected final int k3() {
		return e3().getIntKey();
	}

	protected final boolean v3() {
		return e3().getBooleanValue();
	}

	protected final int k4() {
		return e4().getIntKey();
	}

	protected final boolean v4() {
		return e4().getBooleanValue();
	}
}