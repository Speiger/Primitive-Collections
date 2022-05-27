package speiger.src.testers.shorts.tests.base.maps;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.shorts.maps.abstracts.AbstractShort2ObjectMap;
import speiger.src.collections.shorts.maps.interfaces.Short2ObjectMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.shorts.generators.maps.TestShort2ObjectMapGenerator;
import speiger.src.testers.shorts.utils.ShortHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class AbstractShort2ObjectMapTester<V> extends AbstractObjectContainerTester<Short2ObjectMap.Entry<V>, Short2ObjectMap<V>>
{
	protected TestShort2ObjectMapGenerator<V> primitiveMapGenerator;

	protected Short2ObjectMap<V> getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Short, V>, ? extends Map.Entry<Short, V>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestShort2ObjectMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestShort2ObjectMapGenerator<V>) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Short2ObjectMap<V> createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Short2ObjectMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Short2ObjectMap.Entry<V>> getOrderedElements() {
		ObjectList<Short2ObjectMap.Entry<V>> list = new ObjectArrayList<>();
		for (ObjectIterator<Short2ObjectMap.Entry<V>> iter = primitiveMapGenerator.order(new ObjectArrayList<Short2ObjectMap.Entry<V>>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Short2ObjectMap.Entry<V>> actualContents() {
		return getMap().short2ObjectEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Short2ObjectMap.Entry<V>[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Short2ObjectMap<V> resetContainer(Short2ObjectMap<V> newValue) {
		newValue.setDefaultReturnValue(null);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(short... elements) {
		for (short element : elements) {
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
		
	protected ObjectCollection<Short2ObjectMap.Entry<V>> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Short2ObjectMap.Entry<V>> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Short2ObjectMap.Entry<V>... entries) {
		for (Short2ObjectMap.Entry<V> entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getShortKey() + " mapped to value " + entry.getValue(), valueEquals(getMap().get(entry.getShortKey()), entry.getValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Short2ObjectMap.Entry<V>> expected) {
		super.expectContents(expected);
		for (Short2ObjectMap.Entry<V> entry : expected) {
			assertEquals("Wrong value for key " + entry.getShortKey(), entry.getValue(), getMap().get(entry.getShortKey()));
		}
	}

	protected final void expectReplacement(Short2ObjectMap.Entry<V> newEntry) {
		ObjectList<Short2ObjectMap.Entry<V>> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Short2ObjectMap.Entry<V>> expected, Short2ObjectMap.Entry<V> newEntry) {
		for (ObjectListIterator<Short2ObjectMap.Entry<V>> i = expected.listIterator(); i.hasNext();) {
			if (ShortHelpers.equals(i.next().getShortKey(), newEntry.getShortKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getShortKey(), expected));
	}

	private static <V> boolean valueEquals(V a, V b) {
		return Objects.equals(a, b);
	}
	
	protected Short2ObjectMap.Entry<V> entry(short key, V value) {
		return new AbstractShort2ObjectMap.BasicEntry<>(key, value);
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
	
	protected V get(short key) {
		return getMap().get(key);
	}

	protected final short k0() {
		return e0().getShortKey();
	}

	protected final V v0() {
		return e0().getValue();
	}

	protected final short k1() {
		return e1().getShortKey();
	}

	protected final V v1() {
		return e1().getValue();
	}

	protected final short k2() {
		return e2().getShortKey();
	}

	protected final V v2() {
		return e2().getValue();
	}

	protected final short k3() {
		return e3().getShortKey();
	}

	protected final V v3() {
		return e3().getValue();
	}

	protected final short k4() {
		return e4().getShortKey();
	}

	protected final V v4() {
		return e4().getValue();
	}
}