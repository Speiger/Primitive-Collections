package speiger.src.testers.objects.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.objects.maps.abstracts.AbstractObject2IntMap;
import speiger.src.collections.objects.maps.interfaces.Object2IntMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.objects.generators.maps.TestObject2IntMapGenerator;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class AbstractObject2IntMapTester<T> extends AbstractObjectContainerTester<Object2IntMap.Entry<T>, Object2IntMap<T>>
{
	protected TestObject2IntMapGenerator<T> primitiveMapGenerator;

	protected Object2IntMap<T> getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<T, Integer>, ? extends Map.Entry<T, Integer>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestObject2IntMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestObject2IntMapGenerator<T>) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Object2IntMap<T> createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Object2IntMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Object2IntMap.Entry<T>> getOrderedElements() {
		ObjectList<Object2IntMap.Entry<T>> list = new ObjectArrayList<>();
		for (ObjectIterator<Object2IntMap.Entry<T>> iter = primitiveMapGenerator.order(new ObjectArrayList<Object2IntMap.Entry<T>>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Object2IntMap.Entry<T>> actualContents() {
		return getMap().object2IntEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Object2IntMap.Entry<T>[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Object2IntMap<T> resetContainer(Object2IntMap<T> newValue) {
		newValue.setDefaultReturnValue(-1);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(T... elements) {
		for (T element : elements) {
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
		
	protected ObjectCollection<Object2IntMap.Entry<T>> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Object2IntMap.Entry<T>> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Object2IntMap.Entry<T>... entries) {
		for (Object2IntMap.Entry<T> entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getKey() + " mapped to value " + entry.getIntValue(), valueEquals(getMap().get(entry.getKey()), entry.getIntValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Object2IntMap.Entry<T>> expected) {
		super.expectContents(expected);
		for (Object2IntMap.Entry<T> entry : expected) {
			assertEquals("Wrong value for key " + entry.getKey(), entry.getIntValue(), getMap().getInt(entry.getKey()));
		}
	}

	protected final void expectReplacement(Object2IntMap.Entry<T> newEntry) {
		ObjectList<Object2IntMap.Entry<T>> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Object2IntMap.Entry<T>> expected, Object2IntMap.Entry<T> newEntry) {
		for (ObjectListIterator<Object2IntMap.Entry<T>> i = expected.listIterator(); i.hasNext();) {
			if (ObjectHelpers.equals(i.next().getKey(), newEntry.getKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getKey(), expected));
	}

	private static boolean valueEquals(int a, int b) {
		return a == b;
	}
	
	protected Object2IntMap.Entry<T> entry(T key, int value) {
		return new AbstractObject2IntMap.BasicEntry<>(key, value);
	}
	
	protected T[] emptyKeyArray() {
		return (T[])new Object[0];
	}
	
	protected T[] createDisjointedKeyArray() {
		T[] array = (T[])new Object[2];
		array[0] = k3();
		array[1] = k4();
		return array;
	}
	
	protected T[] emptyKeyObjectArray() {
		return (T[])new Object[0];
	}
	
	protected T[] createDisjointedKeyObjectArray() {
		T[] array = (T[])new Object[2];
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
	
	protected int get(T key) {
		return getMap().getInt(key);
	}

	protected final T k0() {
		return e0().getKey();
	}

	protected final int v0() {
		return e0().getIntValue();
	}

	protected final T k1() {
		return e1().getKey();
	}

	protected final int v1() {
		return e1().getIntValue();
	}

	protected final T k2() {
		return e2().getKey();
	}

	protected final int v2() {
		return e2().getIntValue();
	}

	protected final T k3() {
		return e3().getKey();
	}

	protected final int v3() {
		return e3().getIntValue();
	}

	protected final T k4() {
		return e4().getKey();
	}

	protected final int v4() {
		return e4().getIntValue();
	}
}