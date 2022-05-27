package speiger.src.testers.objects.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.objects.maps.abstracts.AbstractObject2ShortMap;
import speiger.src.collections.objects.maps.interfaces.Object2ShortMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.objects.generators.maps.TestObject2ShortMapGenerator;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class AbstractObject2ShortMapTester<T> extends AbstractObjectContainerTester<Object2ShortMap.Entry<T>, Object2ShortMap<T>>
{
	protected TestObject2ShortMapGenerator<T> primitiveMapGenerator;

	protected Object2ShortMap<T> getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<T, Short>, ? extends Map.Entry<T, Short>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestObject2ShortMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestObject2ShortMapGenerator<T>) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Object2ShortMap<T> createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Object2ShortMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Object2ShortMap.Entry<T>> getOrderedElements() {
		ObjectList<Object2ShortMap.Entry<T>> list = new ObjectArrayList<>();
		for (ObjectIterator<Object2ShortMap.Entry<T>> iter = primitiveMapGenerator.order(new ObjectArrayList<Object2ShortMap.Entry<T>>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Object2ShortMap.Entry<T>> actualContents() {
		return getMap().object2ShortEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Object2ShortMap.Entry<T>[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Object2ShortMap<T> resetContainer(Object2ShortMap<T> newValue) {
		newValue.setDefaultReturnValue((short)-1);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(T... elements) {
		for (T element : elements) {
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
		
	protected ObjectCollection<Object2ShortMap.Entry<T>> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Object2ShortMap.Entry<T>> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Object2ShortMap.Entry<T>... entries) {
		for (Object2ShortMap.Entry<T> entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getKey() + " mapped to value " + entry.getShortValue(), valueEquals(getMap().get(entry.getKey()), entry.getShortValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Object2ShortMap.Entry<T>> expected) {
		super.expectContents(expected);
		for (Object2ShortMap.Entry<T> entry : expected) {
			assertEquals("Wrong value for key " + entry.getKey(), entry.getShortValue(), getMap().getShort(entry.getKey()));
		}
	}

	protected final void expectReplacement(Object2ShortMap.Entry<T> newEntry) {
		ObjectList<Object2ShortMap.Entry<T>> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Object2ShortMap.Entry<T>> expected, Object2ShortMap.Entry<T> newEntry) {
		for (ObjectListIterator<Object2ShortMap.Entry<T>> i = expected.listIterator(); i.hasNext();) {
			if (ObjectHelpers.equals(i.next().getKey(), newEntry.getKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getKey(), expected));
	}

	private static boolean valueEquals(short a, short b) {
		return a == b;
	}
	
	protected Object2ShortMap.Entry<T> entry(T key, short value) {
		return new AbstractObject2ShortMap.BasicEntry<>(key, value);
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
	
	protected short get(T key) {
		return getMap().getShort(key);
	}

	protected final T k0() {
		return e0().getKey();
	}

	protected final short v0() {
		return e0().getShortValue();
	}

	protected final T k1() {
		return e1().getKey();
	}

	protected final short v1() {
		return e1().getShortValue();
	}

	protected final T k2() {
		return e2().getKey();
	}

	protected final short v2() {
		return e2().getShortValue();
	}

	protected final T k3() {
		return e3().getKey();
	}

	protected final short v3() {
		return e3().getShortValue();
	}

	protected final T k4() {
		return e4().getKey();
	}

	protected final short v4() {
		return e4().getShortValue();
	}
}