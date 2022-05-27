package speiger.src.testers.longs.tests.base.maps;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.longs.maps.abstracts.AbstractLong2ObjectMap;
import speiger.src.collections.longs.maps.interfaces.Long2ObjectMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.longs.generators.maps.TestLong2ObjectMapGenerator;
import speiger.src.testers.longs.utils.LongHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class AbstractLong2ObjectMapTester<V> extends AbstractObjectContainerTester<Long2ObjectMap.Entry<V>, Long2ObjectMap<V>>
{
	protected TestLong2ObjectMapGenerator<V> primitiveMapGenerator;

	protected Long2ObjectMap<V> getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Long, V>, ? extends Map.Entry<Long, V>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestLong2ObjectMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestLong2ObjectMapGenerator<V>) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Long2ObjectMap<V> createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Long2ObjectMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Long2ObjectMap.Entry<V>> getOrderedElements() {
		ObjectList<Long2ObjectMap.Entry<V>> list = new ObjectArrayList<>();
		for (ObjectIterator<Long2ObjectMap.Entry<V>> iter = primitiveMapGenerator.order(new ObjectArrayList<Long2ObjectMap.Entry<V>>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Long2ObjectMap.Entry<V>> actualContents() {
		return getMap().long2ObjectEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Long2ObjectMap.Entry<V>[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Long2ObjectMap<V> resetContainer(Long2ObjectMap<V> newValue) {
		newValue.setDefaultReturnValue(null);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(long... elements) {
		for (long element : elements) {
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
		
	protected ObjectCollection<Long2ObjectMap.Entry<V>> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Long2ObjectMap.Entry<V>> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Long2ObjectMap.Entry<V>... entries) {
		for (Long2ObjectMap.Entry<V> entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getLongKey() + " mapped to value " + entry.getValue(), valueEquals(getMap().get(entry.getLongKey()), entry.getValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Long2ObjectMap.Entry<V>> expected) {
		super.expectContents(expected);
		for (Long2ObjectMap.Entry<V> entry : expected) {
			assertEquals("Wrong value for key " + entry.getLongKey(), entry.getValue(), getMap().get(entry.getLongKey()));
		}
	}

	protected final void expectReplacement(Long2ObjectMap.Entry<V> newEntry) {
		ObjectList<Long2ObjectMap.Entry<V>> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Long2ObjectMap.Entry<V>> expected, Long2ObjectMap.Entry<V> newEntry) {
		for (ObjectListIterator<Long2ObjectMap.Entry<V>> i = expected.listIterator(); i.hasNext();) {
			if (LongHelpers.equals(i.next().getLongKey(), newEntry.getLongKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getLongKey(), expected));
	}

	private static <V> boolean valueEquals(V a, V b) {
		return Objects.equals(a, b);
	}
	
	protected Long2ObjectMap.Entry<V> entry(long key, V value) {
		return new AbstractLong2ObjectMap.BasicEntry<>(key, value);
	}
	
	protected long[] emptyKeyArray() {
		return new long[0];
	}
	
	protected long[] createDisjointedKeyArray() {
		long[] array = new long[2];
		array[0] = k3();
		array[1] = k4();
		return array;
	}
	
	protected Long[] emptyKeyObjectArray() {
		return new Long[0];
	}
	
	protected Long[] createDisjointedKeyObjectArray() {
		Long[] array = new Long[2];
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
	
	protected V get(long key) {
		return getMap().get(key);
	}

	protected final long k0() {
		return e0().getLongKey();
	}

	protected final V v0() {
		return e0().getValue();
	}

	protected final long k1() {
		return e1().getLongKey();
	}

	protected final V v1() {
		return e1().getValue();
	}

	protected final long k2() {
		return e2().getLongKey();
	}

	protected final V v2() {
		return e2().getValue();
	}

	protected final long k3() {
		return e3().getLongKey();
	}

	protected final V v3() {
		return e3().getValue();
	}

	protected final long k4() {
		return e4().getLongKey();
	}

	protected final V v4() {
		return e4().getValue();
	}
}