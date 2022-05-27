package speiger.src.testers.ints.tests.base.maps;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.ints.maps.abstracts.AbstractInt2ObjectMap;
import speiger.src.collections.ints.maps.interfaces.Int2ObjectMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.ints.generators.maps.TestInt2ObjectMapGenerator;
import speiger.src.testers.ints.utils.IntHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class AbstractInt2ObjectMapTester<V> extends AbstractObjectContainerTester<Int2ObjectMap.Entry<V>, Int2ObjectMap<V>>
{
	protected TestInt2ObjectMapGenerator<V> primitiveMapGenerator;

	protected Int2ObjectMap<V> getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Integer, V>, ? extends Map.Entry<Integer, V>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestInt2ObjectMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestInt2ObjectMapGenerator<V>) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Int2ObjectMap<V> createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Int2ObjectMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Int2ObjectMap.Entry<V>> getOrderedElements() {
		ObjectList<Int2ObjectMap.Entry<V>> list = new ObjectArrayList<>();
		for (ObjectIterator<Int2ObjectMap.Entry<V>> iter = primitiveMapGenerator.order(new ObjectArrayList<Int2ObjectMap.Entry<V>>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Int2ObjectMap.Entry<V>> actualContents() {
		return getMap().int2ObjectEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Int2ObjectMap.Entry<V>[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Int2ObjectMap<V> resetContainer(Int2ObjectMap<V> newValue) {
		newValue.setDefaultReturnValue(null);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(int... elements) {
		for (int element : elements) {
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
		
	protected ObjectCollection<Int2ObjectMap.Entry<V>> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Int2ObjectMap.Entry<V>> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Int2ObjectMap.Entry<V>... entries) {
		for (Int2ObjectMap.Entry<V> entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getIntKey() + " mapped to value " + entry.getValue(), valueEquals(getMap().get(entry.getIntKey()), entry.getValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Int2ObjectMap.Entry<V>> expected) {
		super.expectContents(expected);
		for (Int2ObjectMap.Entry<V> entry : expected) {
			assertEquals("Wrong value for key " + entry.getIntKey(), entry.getValue(), getMap().get(entry.getIntKey()));
		}
	}

	protected final void expectReplacement(Int2ObjectMap.Entry<V> newEntry) {
		ObjectList<Int2ObjectMap.Entry<V>> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Int2ObjectMap.Entry<V>> expected, Int2ObjectMap.Entry<V> newEntry) {
		for (ObjectListIterator<Int2ObjectMap.Entry<V>> i = expected.listIterator(); i.hasNext();) {
			if (IntHelpers.equals(i.next().getIntKey(), newEntry.getIntKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getIntKey(), expected));
	}

	private static <V> boolean valueEquals(V a, V b) {
		return Objects.equals(a, b);
	}
	
	protected Int2ObjectMap.Entry<V> entry(int key, V value) {
		return new AbstractInt2ObjectMap.BasicEntry<>(key, value);
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
	
	protected V get(int key) {
		return getMap().get(key);
	}

	protected final int k0() {
		return e0().getIntKey();
	}

	protected final V v0() {
		return e0().getValue();
	}

	protected final int k1() {
		return e1().getIntKey();
	}

	protected final V v1() {
		return e1().getValue();
	}

	protected final int k2() {
		return e2().getIntKey();
	}

	protected final V v2() {
		return e2().getValue();
	}

	protected final int k3() {
		return e3().getIntKey();
	}

	protected final V v3() {
		return e3().getValue();
	}

	protected final int k4() {
		return e4().getIntKey();
	}

	protected final V v4() {
		return e4().getValue();
	}
}