package speiger.src.testers.doubles.tests.maps;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_ITERATOR_REMOVE;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.SUPPORTS_PUT;

import java.util.Iterator;
import java.util.Set;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import speiger.src.collections.doubles.maps.interfaces.Double2CharMap;
import speiger.src.testers.doubles.tests.base.maps.AbstractDouble2CharMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Double2CharMapEntrySetTester extends AbstractDouble2CharMapTester
{
	@CollectionSize.Require(ONE)
	@CollectionFeature.Require(SUPPORTS_ITERATOR_REMOVE)
	public void testEntrySetIteratorRemove() {
		Set<Double2CharMap.Entry> entrySet = getMap().double2CharEntrySet();
		Iterator<Double2CharMap.Entry> entryItr = entrySet.iterator();
		assertEquals(e0(), entryItr.next());
		entryItr.remove();
		assertTrue(getMap().isEmpty());
		assertFalse(entrySet.contains(e0()));
	}
	
	@MapFeature.Require(SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testSetValue() {
		for (Double2CharMap.Entry entry : getMap().double2CharEntrySet()) {
			if (Double.doubleToLongBits(entry.getDoubleKey()) == Double.doubleToLongBits(k0())) {
				assertEquals("entry.setValue() should return the old value", v0(), entry.setValue(v3()));
				break;
			}
		}
		expectReplacement(entry(k0(), v3()));
	}

}