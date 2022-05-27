package speiger.src.testers.objects.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.objects.maps.abstracts.AbstractObject2LongMap;
import speiger.src.collections.objects.maps.interfaces.Object2LongMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.objects.generators.maps.TestObject2LongMapGenerator;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class AbstractObject2LongMapTester<T> extends AbstractObjectContainerTester<Object2LongMap.Entry<T>, Object2LongMap<T>>
{
	protected TestObject2LongMapGenerator<T> primitiveMapGenerator;

	protected Object2LongMap<T> getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<T, Long>, ? extends Map.Entry<T, Long>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestObject2LongMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestObject2LongMapGenerator<T>) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Object2LongMap<T> createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Object2LongMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Object2LongMap.Entry<T>> getOrderedElements() {
		ObjectList<Object2LongMap.Entry<T>> list = new ObjectArrayList<>();
		for (ObjectIterator<Object2LongMap.Entry<T>> iter = primitiveMapGenerator.order(new ObjectArrayList<Object2LongMap.Entry<T>>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Object2LongMap.Entry<T>> actualContents() {
		return getMap().object2LongEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Object2LongMap.Entry<T>[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Object2LongMap<T> resetContainer(Object2LongMap<T> newValue) {
		newValue.setDefaultReturnValue(-1L);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(T... elements) {
		for (T element : elements) {
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
		
	protected ObjectCollection<Object2LongMap.Entry<T>> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Object2LongMap.Entry<T>> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Object2LongMap.Entry<T>... entries) {
		for (Object2LongMap.Entry<T> entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getKey() + " mapped to value " + entry.getLongValue(), valueEquals(getMap().get(entry.getKey()), entry.getLongValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Object2LongMap.Entry<T>> expected) {
		super.expectContents(expected);
		for (Object2LongMap.Entry<T> entry : expected) {
			assertEquals("Wrong value for key " + entry.getKey(), entry.getLongValue(), getMap().getLong(entry.getKey()));
		}
	}

	protected final void expectReplacement(Object2LongMap.Entry<T> newEntry) {
		ObjectList<Object2LongMap.Entry<T>> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Object2LongMap.Entry<T>> expected, Object2LongMap.Entry<T> newEntry) {
		for (ObjectListIterator<Object2LongMap.Entry<T>> i = expected.listIterator(); i.hasNext();) {
			if (ObjectHelpers.equals(i.next().getKey(), newEntry.getKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getKey(), expected));
	}

	private static boolean valueEquals(long a, long b) {
		return a == b;
	}
	
	protected Object2LongMap.Entry<T> entry(T key, long value) {
		return new AbstractObject2LongMap.BasicEntry<>(key, value);
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
	
	protected long get(T key) {
		return getMap().getLong(key);
	}

	protected final T k0() {
		return e0().getKey();
	}

	protected final long v0() {
		return e0().getLongValue();
	}

	protected final T k1() {
		return e1().getKey();
	}

	protected final long v1() {
		return e1().getLongValue();
	}

	protected final T k2() {
		return e2().getKey();
	}

	protected final long v2() {
		return e2().getLongValue();
	}

	protected final T k3() {
		return e3().getKey();
	}

	protected final long v3() {
		return e3().getLongValue();
	}

	protected final T k4() {
		return e4().getKey();
	}

	protected final long v4() {
		return e4().getLongValue();
	}
}