package speiger.src.testers.objects.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.objects.maps.abstracts.AbstractObject2ByteMap;
import speiger.src.collections.objects.maps.interfaces.Object2ByteMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.objects.generators.maps.TestObject2ByteMapGenerator;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class AbstractObject2ByteMapTester<T> extends AbstractObjectContainerTester<Object2ByteMap.Entry<T>, Object2ByteMap<T>>
{
	protected TestObject2ByteMapGenerator<T> primitiveMapGenerator;

	protected Object2ByteMap<T> getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<T, Byte>, ? extends Map.Entry<T, Byte>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestObject2ByteMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestObject2ByteMapGenerator<T>) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Object2ByteMap<T> createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Object2ByteMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Object2ByteMap.Entry<T>> getOrderedElements() {
		ObjectList<Object2ByteMap.Entry<T>> list = new ObjectArrayList<>();
		for (ObjectIterator<Object2ByteMap.Entry<T>> iter = primitiveMapGenerator.order(new ObjectArrayList<Object2ByteMap.Entry<T>>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Object2ByteMap.Entry<T>> actualContents() {
		return getMap().object2ByteEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Object2ByteMap.Entry<T>[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Object2ByteMap<T> resetContainer(Object2ByteMap<T> newValue) {
		newValue.setDefaultReturnValue((byte)-1);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(T... elements) {
		for (T element : elements) {
			assertFalse("Should not contain key " + element, getMap().containsKey(element));
		}
	}

	protected void expectMissingValues(byte... elements) {
		for (byte element : elements) {
			assertFalse("Should not contain value " + element, getMap().containsValue(element));
		}
	}
	
	protected int getNumEntries() {
		return getNumElements();
	}
		
	protected ObjectCollection<Object2ByteMap.Entry<T>> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Object2ByteMap.Entry<T>> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Object2ByteMap.Entry<T>... entries) {
		for (Object2ByteMap.Entry<T> entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getKey() + " mapped to value " + entry.getByteValue(), valueEquals(getMap().get(entry.getKey()), entry.getByteValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Object2ByteMap.Entry<T>> expected) {
		super.expectContents(expected);
		for (Object2ByteMap.Entry<T> entry : expected) {
			assertEquals("Wrong value for key " + entry.getKey(), entry.getByteValue(), getMap().getByte(entry.getKey()));
		}
	}

	protected final void expectReplacement(Object2ByteMap.Entry<T> newEntry) {
		ObjectList<Object2ByteMap.Entry<T>> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Object2ByteMap.Entry<T>> expected, Object2ByteMap.Entry<T> newEntry) {
		for (ObjectListIterator<Object2ByteMap.Entry<T>> i = expected.listIterator(); i.hasNext();) {
			if (ObjectHelpers.equals(i.next().getKey(), newEntry.getKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getKey(), expected));
	}

	private static boolean valueEquals(byte a, byte b) {
		return a == b;
	}
	
	protected Object2ByteMap.Entry<T> entry(T key, byte value) {
		return new AbstractObject2ByteMap.BasicEntry<>(key, value);
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
	
	protected byte[] emptyValueArray() {
		return new byte[0];
	}
	
	protected byte[] createDisjointedValueArray() {
		byte[] array = new byte[2];
		array[0] = v3();
		array[1] = v4();
		return array;
	}
	
	protected Byte[] emptyValueObjectArray() {
		return new Byte[0];
	}
	
	protected Byte[] createDisjointedValueObjectArray() {
		Byte[] array = new Byte[2];
		array[0] = v3();
		array[1] = v4();
		return array;
	}
	
	protected byte get(T key) {
		return getMap().getByte(key);
	}

	protected final T k0() {
		return e0().getKey();
	}

	protected final byte v0() {
		return e0().getByteValue();
	}

	protected final T k1() {
		return e1().getKey();
	}

	protected final byte v1() {
		return e1().getByteValue();
	}

	protected final T k2() {
		return e2().getKey();
	}

	protected final byte v2() {
		return e2().getByteValue();
	}

	protected final T k3() {
		return e3().getKey();
	}

	protected final byte v3() {
		return e3().getByteValue();
	}

	protected final T k4() {
		return e4().getKey();
	}

	protected final byte v4() {
		return e4().getByteValue();
	}
}