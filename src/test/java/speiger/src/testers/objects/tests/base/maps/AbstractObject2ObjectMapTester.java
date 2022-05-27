package speiger.src.testers.objects.tests.base.maps;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.objects.maps.abstracts.AbstractObject2ObjectMap;
import speiger.src.collections.objects.maps.interfaces.Object2ObjectMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.objects.generators.maps.TestObject2ObjectMapGenerator;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
public class AbstractObject2ObjectMapTester<T, V> extends AbstractObjectContainerTester<Object2ObjectMap.Entry<T, V>, Object2ObjectMap<T, V>> {

	protected TestObject2ObjectMapGenerator<T, V> primitiveMapGenerator;

	protected Object2ObjectMap<T, V> getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<T, V>, ? extends Map.Entry<T, V>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestObject2ObjectMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestObject2ObjectMapGenerator<T, V>) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Object2ObjectMap<T, V> createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Object2ObjectMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Object2ObjectMap.Entry<T, V>> getOrderedElements() {
		ObjectList<Object2ObjectMap.Entry<T, V>> list = new ObjectArrayList<>();
		for (ObjectIterator<Object2ObjectMap.Entry<T, V>> iter = primitiveMapGenerator.order(new ObjectArrayList<Object2ObjectMap.Entry<T, V>>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Object2ObjectMap.Entry<T, V>> actualContents() {
		return getMap().object2ObjectEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Object2ObjectMap.Entry<T, V>[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Object2ObjectMap<T, V> resetContainer(Object2ObjectMap<T, V> newValue) {
		newValue.setDefaultReturnValue(null);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(T... elements) {
		for (T element : elements) {
			assertFalse("Should not contain key " + element, getMap().containsKey(element));
		}
	}

	protected void expectMissingValues(V... elements) {
		for (V element : elements) {
			assertFalse("Should not contain value " + element, getMap().containsValue(element));
		}
	}
	
	protected int getNumEntries() {
		return getNumElements();
	}
		
	protected ObjectCollection<Object2ObjectMap.Entry<T, V>> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Object2ObjectMap.Entry<T, V>> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Object2ObjectMap.Entry<T, V>... entries) {
		for (Object2ObjectMap.Entry<T, V> entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getKey() + " mapped to value " + entry.getValue(), valueEquals(getMap().get(entry.getKey()), entry.getValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Object2ObjectMap.Entry<T, V>> expected) {
		super.expectContents(expected);
		for (Object2ObjectMap.Entry<T, V> entry : expected) {
			assertEquals("Wrong value for key " + entry.getKey(), entry.getValue(), getMap().getObject(entry.getKey()));
		}
	}

	protected final void expectReplacement(Object2ObjectMap.Entry<T, V> newEntry) {
		ObjectList<Object2ObjectMap.Entry<T, V>> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Object2ObjectMap.Entry<T, V>> expected, Object2ObjectMap.Entry<T, V> newEntry) {
		for (ObjectListIterator<Object2ObjectMap.Entry<T, V>> i = expected.listIterator(); i.hasNext();) {
			if (ObjectHelpers.equals(i.next().getKey(), newEntry.getKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getKey(), expected));
	}

	private static <V> boolean valueEquals(V a, V b) {
		return Objects.equals(a, b);
	}
	
	protected Object2ObjectMap.Entry<T, V> entry(T key, V value) {
		return new AbstractObject2ObjectMap.BasicEntry<>(key, value);
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
	
	protected V[] emptyValueArray() {
		return (V[])new Object[0];
	}
	
	protected V[] createDisjointedValueArray() {
		V[] array = (V[])new Object[2];
		array[0] = v3();
		array[1] = v4();
		return array;
	}
	
	protected V[] emptyValueObjectArray() {
		return (V[])new Object[0];
	}
	
	protected V[] createDisjointedValueObjectArray() {
		V[] array = (V[])new Object[2];
		array[0] = v3();
		array[1] = v4();
		return array;
	}
	
	protected V get(T key) {
		return getMap().getObject(key);
	}

	protected final T k0() {
		return e0().getKey();
	}

	protected final V v0() {
		return e0().getValue();
	}

	protected final T k1() {
		return e1().getKey();
	}

	protected final V v1() {
		return e1().getValue();
	}

	protected final T k2() {
		return e2().getKey();
	}

	protected final V v2() {
		return e2().getValue();
	}

	protected final T k3() {
		return e3().getKey();
	}

	protected final V v3() {
		return e3().getValue();
	}

	protected final T k4() {
		return e4().getKey();
	}

	protected final V v4() {
		return e4().getValue();
	}
}