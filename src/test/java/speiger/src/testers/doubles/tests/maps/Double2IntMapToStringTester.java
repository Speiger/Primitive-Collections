package speiger.src.testers.doubles.tests.maps;

import static com.google.common.collect.testing.features.CollectionFeature.NON_STANDARD_TOSTRING;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.Set;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.doubles.maps.impl.hash.Double2IntLinkedOpenHashMap;
import speiger.src.collections.doubles.maps.interfaces.Double2IntMap;
import speiger.src.testers.doubles.tests.base.maps.AbstractDouble2IntMapTester;

@Ignore
public class Double2IntMapToStringTester extends AbstractDouble2IntMapTester
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
		assertEquals("map.toString() incorrect", expectedToString(getMap().double2IntEntrySet()), getMap().toString());
	}

	private String expectedToString(Set<Double2IntMap.Entry> entries) {
		Double2IntMap reference = new Double2IntLinkedOpenHashMap();
		for (Double2IntMap.Entry entry : entries) {
			reference.put(entry.getDoubleKey(), entry.getIntValue());
		}
		return reference.toString();
	}
}