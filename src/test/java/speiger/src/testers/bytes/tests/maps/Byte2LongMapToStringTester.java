package speiger.src.testers.bytes.tests.maps;

import static com.google.common.collect.testing.features.CollectionFeature.NON_STANDARD_TOSTRING;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.Set;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.bytes.maps.impl.hash.Byte2LongLinkedOpenHashMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2LongMap;
import speiger.src.testers.bytes.tests.base.maps.AbstractByte2LongMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Byte2LongMapToStringTester extends AbstractByte2LongMapTester
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
		assertEquals("map.toString() incorrect", expectedToString(getMap().byte2LongEntrySet()), getMap().toString());
	}

	private String expectedToString(Set<Byte2LongMap.Entry> entries) {
		Byte2LongMap reference = new Byte2LongLinkedOpenHashMap();
		for (Byte2LongMap.Entry entry : entries) {
			reference.put(entry.getByteKey(), entry.getLongValue());
		}
		return reference.toString();
	}
}