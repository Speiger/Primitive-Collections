package speiger.src.testers.longs.tests.maps;

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

import speiger.src.collections.longs.maps.interfaces.Long2FloatMap;
import speiger.src.testers.longs.tests.base.maps.AbstractLong2FloatMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Long2FloatMapEntrySetTester extends AbstractLong2FloatMapTester
{
	@CollectionSize.Require(ONE)
	@CollectionFeature.Require(SUPPORTS_ITERATOR_REMOVE)
	public void testEntrySetIteratorRemove() {
		Set<Long2FloatMap.Entry> entrySet = getMap().long2FloatEntrySet();
		Iterator<Long2FloatMap.Entry> entryItr = entrySet.iterator();
		assertEquals(e0(), entryItr.next());
		entryItr.remove();
		assertTrue(getMap().isEmpty());
		assertFalse(entrySet.contains(e0()));
	}
	
	@MapFeature.Require(SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testSetValue() {
		for (Long2FloatMap.Entry entry : getMap().long2FloatEntrySet()) {
			if (entry.getLongKey() == k0()) {
				assertEquals("entry.setValue() should return the old value", v0(), entry.setValue(v3()));
				break;
			}
		}
		expectReplacement(entry(k0(), v3()));
	}

}