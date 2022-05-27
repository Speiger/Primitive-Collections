package speiger.src.testers.longs.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.longs.maps.interfaces.Long2BooleanMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public interface TestLong2BooleanMapGenerator extends TestMapGenerator<Long, Boolean> {
	public ObjectSamples<Long2BooleanMap.Entry> getSamples();
	public ObjectIterable<Long2BooleanMap.Entry> order(ObjectList<Long2BooleanMap.Entry> insertionOrder);
	public Long2BooleanMap create(Long2BooleanMap.Entry... elements);
	@Override
	default Long2BooleanMap create(Object... elements) {
		Long2BooleanMap.Entry[] result = new Long2BooleanMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Long2BooleanMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Long[] createKeyArray(int length) { return new Long[length]; }
	@Override
	default Boolean[] createValueArray(int length) { return new Boolean[length]; }
	@Override
	default Long2BooleanMap.Entry[] createArray(int length) { return new Long2BooleanMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Long, Boolean>> samples() {
		ObjectSamples<Long2BooleanMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getLongKey(), samples.e0().getBooleanValue()), 
			Helpers.mapEntry(samples.e1().getLongKey(), samples.e1().getBooleanValue()), 
			Helpers.mapEntry(samples.e2().getLongKey(), samples.e2().getBooleanValue()), 
			Helpers.mapEntry(samples.e3().getLongKey(), samples.e3().getBooleanValue()), 
			Helpers.mapEntry(samples.e4().getLongKey(), samples.e4().getBooleanValue())
		);
	}
}