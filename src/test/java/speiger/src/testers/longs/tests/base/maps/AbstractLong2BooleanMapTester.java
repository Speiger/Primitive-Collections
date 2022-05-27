package speiger.src.testers.longs.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.longs.maps.abstracts.AbstractLong2BooleanMap;
import speiger.src.collections.longs.maps.interfaces.Long2BooleanMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.longs.generators.maps.TestLong2BooleanMapGenerator;
import speiger.src.testers.longs.utils.LongHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class AbstractLong2BooleanMapTester extends AbstractObjectContainerTester<Long2BooleanMap.Entry, Long2BooleanMap>
{
	protected TestLong2BooleanMapGenerator primitiveMapGenerator;

	protected Long2BooleanMap getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Long, Boolean>, ? extends Map.Entry<Long, Boolean>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestLong2BooleanMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestLong2BooleanMapGenerator) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Long2BooleanMap createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Long2BooleanMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Long2BooleanMap.Entry> getOrderedElements() {
		ObjectList<Long2BooleanMap.Entry> list = new ObjectArrayList<>();
		for (ObjectIterator<Long2BooleanMap.Entry> iter = primitiveMapGenerator.order(new ObjectArrayList<Long2BooleanMap.Entry>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Long2BooleanMap.Entry> actualContents() {
		return getMap().long2BooleanEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Long2BooleanMap.Entry[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Long2BooleanMap resetContainer(Long2BooleanMap newValue) {
		newValue.setDefaultReturnValue(false);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(long... elements) {
		for (long element : elements) {
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
		
	protected ObjectCollection<Long2BooleanMap.Entry> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Long2BooleanMap.Entry> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Long2BooleanMap.Entry... entries) {
		for (Long2BooleanMap.Entry entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getLongKey() + " mapped to value " + entry.getBooleanValue(), valueEquals(getMap().get(entry.getLongKey()), entry.getBooleanValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Long2BooleanMap.Entry> expected) {
		super.expectContents(expected);
		for (Long2BooleanMap.Entry entry : expected) {
			assertEquals("Wrong value for key " + entry.getLongKey(), entry.getBooleanValue(), getMap().get(entry.getLongKey()));
		}
	}

	protected final void expectReplacement(Long2BooleanMap.Entry newEntry) {
		ObjectList<Long2BooleanMap.Entry> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Long2BooleanMap.Entry> expected, Long2BooleanMap.Entry newEntry) {
		for (ObjectListIterator<Long2BooleanMap.Entry> i = expected.listIterator(); i.hasNext();) {
			if (LongHelpers.equals(i.next().getLongKey(), newEntry.getLongKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getLongKey(), expected));
	}

	private static boolean valueEquals(boolean a, boolean b) {
		return a == b;
	}
	
	protected Long2BooleanMap.Entry entry(long key, boolean value) {
		return new AbstractLong2BooleanMap.BasicEntry(key, value);
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
	
	protected boolean get(long key) {
		return getMap().get(key);
	}

	protected final long k0() {
		return e0().getLongKey();
	}

	protected final boolean v0() {
		return e0().getBooleanValue();
	}

	protected final long k1() {
		return e1().getLongKey();
	}

	protected final boolean v1() {
		return e1().getBooleanValue();
	}

	protected final long k2() {
		return e2().getLongKey();
	}

	protected final boolean v2() {
		return e2().getBooleanValue();
	}

	protected final long k3() {
		return e3().getLongKey();
	}

	protected final boolean v3() {
		return e3().getBooleanValue();
	}

	protected final long k4() {
		return e4().getLongKey();
	}

	protected final boolean v4() {
		return e4().getBooleanValue();
	}
}