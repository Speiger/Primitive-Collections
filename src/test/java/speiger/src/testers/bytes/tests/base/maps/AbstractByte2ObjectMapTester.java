package speiger.src.testers.bytes.tests.base.maps;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.bytes.maps.abstracts.AbstractByte2ObjectMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2ObjectMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.bytes.generators.maps.TestByte2ObjectMapGenerator;
import speiger.src.testers.bytes.utils.ByteHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
public class AbstractByte2ObjectMapTester<V> extends AbstractObjectContainerTester<Byte2ObjectMap.Entry<V>, Byte2ObjectMap<V>> {

	protected TestByte2ObjectMapGenerator<V> primitiveMapGenerator;

	protected Byte2ObjectMap<V> getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Byte, V>, ? extends Map.Entry<Byte, V>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestByte2ObjectMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestByte2ObjectMapGenerator<V>) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Byte2ObjectMap<V> createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Byte2ObjectMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Byte2ObjectMap.Entry<V>> getOrderedElements() {
		ObjectList<Byte2ObjectMap.Entry<V>> list = new ObjectArrayList<>();
		for (ObjectIterator<Byte2ObjectMap.Entry<V>> iter = primitiveMapGenerator.order(new ObjectArrayList<Byte2ObjectMap.Entry<V>>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Byte2ObjectMap.Entry<V>> actualContents() {
		return getMap().byte2ObjectEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Byte2ObjectMap.Entry<V>[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Byte2ObjectMap<V> resetContainer(Byte2ObjectMap<V> newValue) {
		newValue.setDefaultReturnValue(null);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(byte... elements) {
		for (byte element : elements) {
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
		
	protected ObjectCollection<Byte2ObjectMap.Entry<V>> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Byte2ObjectMap.Entry<V>> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Byte2ObjectMap.Entry<V>... entries) {
		for (Byte2ObjectMap.Entry<V> entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getByteKey() + " mapped to value " + entry.getValue(), valueEquals(getMap().get(entry.getByteKey()), entry.getValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Byte2ObjectMap.Entry<V>> expected) {
		super.expectContents(expected);
		for (Byte2ObjectMap.Entry<V> entry : expected) {
			assertEquals("Wrong value for key " + entry.getByteKey(), entry.getValue(), getMap().get(entry.getByteKey()));
		}
	}

	protected final void expectReplacement(Byte2ObjectMap.Entry<V> newEntry) {
		ObjectList<Byte2ObjectMap.Entry<V>> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Byte2ObjectMap.Entry<V>> expected, Byte2ObjectMap.Entry<V> newEntry) {
		for (ObjectListIterator<Byte2ObjectMap.Entry<V>> i = expected.listIterator(); i.hasNext();) {
			if (ByteHelpers.equals(i.next().getByteKey(), newEntry.getByteKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getByteKey(), expected));
	}

	private static <V> boolean valueEquals(V a, V b) {
		return Objects.equals(a, b);
	}
	
	protected Byte2ObjectMap.Entry<V> entry(byte key, V value) {
		return new AbstractByte2ObjectMap.BasicEntry<>(key, value);
	}
	
	protected byte[] emptyKeyArray() {
		return new byte[0];
	}
	
	protected byte[] createDisjointedKeyArray() {
		byte[] array = new byte[2];
		array[0] = k3();
		array[1] = k4();
		return array;
	}
	
	protected Byte[] emptyKeyObjectArray() {
		return new Byte[0];
	}
	
	protected Byte[] createDisjointedKeyObjectArray() {
		Byte[] array = new Byte[2];
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
	
	protected V get(byte key) {
		return getMap().get(key);
	}

	protected final byte k0() {
		return e0().getByteKey();
	}

	protected final V v0() {
		return e0().getValue();
	}

	protected final byte k1() {
		return e1().getByteKey();
	}

	protected final V v1() {
		return e1().getValue();
	}

	protected final byte k2() {
		return e2().getByteKey();
	}

	protected final V v2() {
		return e2().getValue();
	}

	protected final byte k3() {
		return e3().getByteKey();
	}

	protected final V v3() {
		return e3().getValue();
	}

	protected final byte k4() {
		return e4().getByteKey();
	}

	protected final V v4() {
		return e4().getValue();
	}
}