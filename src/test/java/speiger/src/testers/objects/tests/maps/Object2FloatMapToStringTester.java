package speiger.src.testers.objects.tests.maps;

import static com.google.common.collect.testing.features.CollectionFeature.NON_STANDARD_TOSTRING;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.Set;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.objects.maps.impl.hash.Object2FloatLinkedOpenHashMap;
import speiger.src.collections.objects.maps.interfaces.Object2FloatMap;
import speiger.src.testers.objects.tests.base.maps.AbstractObject2FloatMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Object2FloatMapToStringTester<T> extends AbstractObject2FloatMapTester<T>
{
	public void testToString_minimal() {
		assertNotNull("toString() should not return null", getMap().toString());
	}

	@CollectionSize.Require(ZERO)
	@CollectionFeature.Require(absent = NON_STANDARD_TOSTRING)
	public void testToString_size0() {
		assertEquals("emptyMap.toString should return {}", "{}", getMap().toString());
	}

	@CollectionSize.Require(ONE)
	@CollectionFeature.Require(absent = NON_STANDARD_TOSTRING)
	public void testToString_size1() {
		assertEquals("size1Map.toString should return {entry}", "{" + e0() + "}", getMap().toString());
	}

	@CollectionFeature.Require(absent = NON_STANDARD_TOSTRING)
	public void testToString_formatting() {
		assertEquals("map.toString() incorrect", expectedToString(getMap().object2FloatEntrySet()), getMap().toString());
	}

	private String expectedToString(Set<Object2FloatMap.Entry<T>> entries) {
		Object2FloatMap<T> reference = new Object2FloatLinkedOpenHashMap<>();
		for (Object2FloatMap.Entry<T> entry : entries) {
			reference.put(entry.getKey(), entry.getFloatValue());
		}
		return reference.toString();
	}
}