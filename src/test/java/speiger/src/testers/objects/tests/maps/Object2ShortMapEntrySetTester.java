package speiger.src.testers.objects.tests.maps;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_ITERATOR_REMOVE;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.SUPPORTS_PUT;

import java.util.Iterator;
import java.util.Set;
import java.util.Objects;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import speiger.src.collections.objects.maps.interfaces.Object2ShortMap;
import speiger.src.testers.objects.tests.base.maps.AbstractObject2ShortMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Object2ShortMapEntrySetTester<T> extends AbstractObject2ShortMapTester<T>
{
	@CollectionSize.Require(ONE)
	@CollectionFeature.Require(SUPPORTS_ITERATOR_REMOVE)
	public void testEntrySetIteratorRemove() {
		Set<Object2ShortMap.Entry<T>> entrySet = getMap().object2ShortEntrySet();
		Iterator<Object2ShortMap.Entry<T>> entryItr = entrySet.iterator();
		assertEquals(e0(), entryItr.next());
		entryItr.remove();
		assertTrue(getMap().isEmpty());
		assertFalse(entrySet.contains(e0()));
	}
	
	@MapFeature.Require(SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testSetValue() {
		for (Object2ShortMap.Entry<T> entry : getMap().object2ShortEntrySet()) {
			if (Objects.equals(entry.getKey(), k0())) {
				assertEquals("entry.setValue() should return the old value", v0(), entry.setValue(v3()));
				break;
			}
		}
		expectReplacement(entry(k0(), v3()));
	}

}