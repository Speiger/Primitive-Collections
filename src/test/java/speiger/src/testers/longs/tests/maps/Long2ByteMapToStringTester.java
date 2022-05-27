package speiger.src.testers.longs.tests.maps;

import static com.google.common.collect.testing.features.CollectionFeature.NON_STANDARD_TOSTRING;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.Set;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.longs.maps.impl.hash.Long2ByteLinkedOpenHashMap;
import speiger.src.collections.longs.maps.interfaces.Long2ByteMap;
import speiger.src.testers.longs.tests.base.maps.AbstractLong2ByteMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Long2ByteMapToStringTester extends AbstractLong2ByteMapTester
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
		assertEquals("map.toString() incorrect", expectedToString(getMap().long2ByteEntrySet()), getMap().toString());
	}

	private String expectedToString(Set<Long2ByteMap.Entry> entries) {
		Long2ByteMap reference = new Long2ByteLinkedOpenHashMap();
		for (Long2ByteMap.Entry entry : entries) {
			reference.put(entry.getLongKey(), entry.getByteValue());
		}
		return reference.toString();
	}
}