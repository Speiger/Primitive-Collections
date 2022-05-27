package speiger.src.testers.chars.tests.base.maps;

import java.util.Locale;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.TestContainerGenerator;

import speiger.src.collections.chars.maps.abstracts.AbstractChar2DoubleMap;
import speiger.src.collections.chars.maps.interfaces.Char2DoubleMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.chars.generators.maps.TestChar2DoubleMapGenerator;
import speiger.src.testers.chars.utils.CharHelpers;
import speiger.src.testers.objects.tests.base.AbstractObjectContainerTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
public class AbstractChar2DoubleMapTester extends AbstractObjectContainerTester<Char2DoubleMap.Entry, Char2DoubleMap> {

	protected TestChar2DoubleMapGenerator primitiveMapGenerator;

	protected Char2DoubleMap getMap() {
		return container;
	}

	@Override
	protected void setupGenerator() {
		TestContainerGenerator<? extends Map<Character, Double>, ? extends Map.Entry<Character, Double>> generator = getSubjectGenerator().getInnerGenerator();
		if (!(generator instanceof TestChar2DoubleMapGenerator)) throw new IllegalStateException("Test Generator Must extend TestLong2ByteMapGenerator");
		primitiveMapGenerator = (TestChar2DoubleMapGenerator) generator;
		samples = primitiveMapGenerator.getSamples();
		size = getSubjectGenerator().getCollectionSize();
		resetContainer();
	}
	
	@Override
	protected Char2DoubleMap createTestSubject() {
		return primitiveMapGenerator.create(getSampleElements(size.getNumElements()).toArray(new Char2DoubleMap.Entry[getNumElements()]));
	}
	
	@Override
	protected ObjectList<Char2DoubleMap.Entry> getOrderedElements() {
		ObjectList<Char2DoubleMap.Entry> list = new ObjectArrayList<>();
		for (ObjectIterator<Char2DoubleMap.Entry> iter = primitiveMapGenerator.order(new ObjectArrayList<Char2DoubleMap.Entry>(getSampleElements())).iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return ObjectLists.unmodifiable(list);
	}
	
	@Override
	protected ObjectCollection<Char2DoubleMap.Entry> actualContents() {
		return getMap().char2DoubleEntrySet();
	}

	protected final void resetMap() {
		resetContainer();
	}

	protected void resetMap(Char2DoubleMap.Entry[] entries) {
		resetContainer(primitiveMapGenerator.create(entries));
	}
	
	@Override
	protected Char2DoubleMap resetContainer(Char2DoubleMap newValue) {
		newValue.setDefaultReturnValue(-1D);
		return super.resetContainer(newValue);
	}

	protected void expectMissingKeys(char... elements) {
		for (char element : elements) {
			assertFalse("Should not contain key " + element, getMap().containsKey(element));
		}
	}

	protected void expectMissingValues(double... elements) {
		for (double element : elements) {
			assertFalse("Should not contain value " + element, getMap().containsValue(element));
		}
	}
	
	protected int getNumEntries() {
		return getNumElements();
	}
		
	protected ObjectCollection<Char2DoubleMap.Entry> getSampleEntries(int howMany) {
		return getSampleElements(howMany);
	}

	protected ObjectCollection<Char2DoubleMap.Entry> getSampleEntries() {
		return getSampleElements();
	}

	@Override
	protected void expectMissing(Char2DoubleMap.Entry... entries) {
		for (Char2DoubleMap.Entry entry : entries) {
			assertFalse("Should not contain entry " + entry, actualContents().contains(entry));
			assertFalse("Should not contain key " + entry.getCharKey() + " mapped to value " + entry.getDoubleValue(), valueEquals(getMap().get(entry.getCharKey()), entry.getDoubleValue()));
		}
	}

	@Override
	protected void expectContents(ObjectCollection<Char2DoubleMap.Entry> expected) {
		super.expectContents(expected);
		for (Char2DoubleMap.Entry entry : expected) {
			assertEquals("Wrong value for key " + entry.getCharKey(), entry.getDoubleValue(), getMap().get(entry.getCharKey()));
		}
	}

	protected final void expectReplacement(Char2DoubleMap.Entry newEntry) {
		ObjectList<Char2DoubleMap.Entry> expected = ObjectHelpers.copyToList(getSampleElements());
		replaceValue(expected, newEntry);
		expectContents(expected);
	}

	private void replaceValue(ObjectList<Char2DoubleMap.Entry> expected, Char2DoubleMap.Entry newEntry) {
		for (ObjectListIterator<Char2DoubleMap.Entry> i = expected.listIterator(); i.hasNext();) {
			if (CharHelpers.equals(i.next().getCharKey(), newEntry.getCharKey())) {
				i.set(newEntry);
				return;
			}
		}
		throw new IllegalArgumentException(String.format(Locale.ROOT, "key %s not found in entries %s", newEntry.getCharKey(), expected));
	}

	private static boolean valueEquals(double a, double b) {
		return Double.doubleToLongBits(a) == Double.doubleToLongBits(b);
	}
	
	protected Char2DoubleMap.Entry entry(char key, double value) {
		return new AbstractChar2DoubleMap.BasicEntry(key, value);
	}
	
	protected char[] emptyKeyArray() {
		return new char[0];
	}
	
	protected char[] createDisjointedKeyArray() {
		char[] array = new char[2];
		array[0] = k3();
		array[1] = k4();
		return array;
	}
	
	protected Character[] emptyKeyObjectArray() {
		return new Character[0];
	}
	
	protected Character[] createDisjointedKeyObjectArray() {
		Character[] array = new Character[2];
		array[0] = k3();
		array[1] = k4();
		return array;
	}
	
	protected double[] emptyValueArray() {
		return new double[0];
	}
	
	protected double[] createDisjointedValueArray() {
		double[] array = new double[2];
		array[0] = v3();
		array[1] = v4();
		return array;
	}
	
	protected Double[] emptyValueObjectArray() {
		return new Double[0];
	}
	
	protected Double[] createDisjointedValueObjectArray() {
		Double[] array = new Double[2];
		array[0] = v3();
		array[1] = v4();
		return array;
	}
	
	protected double get(char key) {
		return getMap().get(key);
	}

	protected final char k0() {
		return e0().getCharKey();
	}

	protected final double v0() {
		return e0().getDoubleValue();
	}

	protected final char k1() {
		return e1().getCharKey();
	}

	protected final double v1() {
		return e1().getDoubleValue();
	}

	protected final char k2() {
		return e2().getCharKey();
	}

	protected final double v2() {
		return e2().getDoubleValue();
	}

	protected final char k3() {
		return e3().getCharKey();
	}

	protected final double v3() {
		return e3().getDoubleValue();
	}

	protected final char k4() {
		return e4().getCharKey();
	}

	protected final double v4() {
		return e4().getDoubleValue();
	}
}