package speiger.src.testers.objects.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.objects.maps.abstracts.AbstractObject2BooleanMap;
import speiger.src.collections.objects.maps.interfaces.Object2BooleanMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.objects.generators.maps.TestObject2BooleanMapGenerator;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class AbstractObject2BooleanMapTester<T> extends AbstractObjectContainerTester<Object2BooleanMap.Entry<T>, Object2BooleanMap<T>>
{
	protected TestObject2BooleanMapGenerator<T> primitiveMapGenerator;

	protected Object2BooleanMap<T> getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<T, Boolean>, ? extends Map.Entry<T, Boolean>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestObject2BooleanMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestObject2BooleanMapGenerator<T>) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Object2BooleanMap<T> createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Object2BooleanMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Object2BooleanMap.Entry<T>> getOrderedElements() {
		ObjectList<Object2BooleanMap.Entry<T>> list = new ObjectArrayList<>();
		for (ObjectIterator<Object2BooleanMap.Entry<T>> iter = primitiveMapGenerator.order(new ObjectArrayList<Object2BooleanMap.Entry<T>>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Object2BooleanMap.Entry<T>> actualContents() {
		return getMap().object2BooleanEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Object2BooleanMap.Entry<T>[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Object2BooleanMap<T> resetContainer(Object2BooleanMap<T> newValue) {
		newValue.setDefaultReturnValue(false);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(T... elements) {
		for (T element : elements) {
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
		
	protected ObjectCollection<Object2BooleanMap.Entry<T>> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Object2BooleanMap.Entry<T>> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Object2BooleanMap.Entry<T>... entries) {
		for (Object2BooleanMap.Entry<T> entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getKey() + " mapped to value " + entry.getBooleanValue(), valueEquals(getMap().get(entry.getKey()), entry.getBooleanValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Object2BooleanMap.Entry<T>> expected) {
		super.expectContents(expected);
		for (Object2BooleanMap.Entry<T> entry : expected) {
			assertEquals("Wrong value for key " + entry.getKey(), entry.getBooleanValue(), getMap().getBoolean(entry.getKey()));
		}
	}

	protected final void expectReplacement(Object2BooleanMap.Entry<T> newEntry) {
		ObjectList<Object2BooleanMap.Entry<T>> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Object2BooleanMap.Entry<T>> expected, Object2BooleanMap.Entry<T> newEntry) {
		for (ObjectListIterator<Object2BooleanMap.Entry<T>> i = expected.listIterator(); i.hasNext();) {
			if (ObjectHelpers.equals(i.next().getKey(), newEntry.getKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getKey(), expected));
	}

	private static boolean valueEquals(boolean a, boolean b) {
		return a == b;
	}
	
	protected Object2BooleanMap.Entry<T> entry(T key, boolean value) {
		return new AbstractObject2BooleanMap.BasicEntry<>(key, value);
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
	
	protected boolean get(T key) {
		return getMap().getBoolean(key);
	}

	protected final T k0() {
		return e0().getKey();
	}

	protected final boolean v0() {
		return e0().getBooleanValue();
	}

	protected final T k1() {
		return e1().getKey();
	}

	protected final boolean v1() {
		return e1().getBooleanValue();
	}

	protected final T k2() {
		return e2().getKey();
	}

	protected final boolean v2() {
		return e2().getBooleanValue();
	}

	protected final T k3() {
		return e3().getKey();
	}

	protected final boolean v3() {
		return e3().getBooleanValue();
	}

	protected final T k4() {
		return e4().getKey();
	}

	protected final boolean v4() {
		return e4().getBooleanValue();
	}
}