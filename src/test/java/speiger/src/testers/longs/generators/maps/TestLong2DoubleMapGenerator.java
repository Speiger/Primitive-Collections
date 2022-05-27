package speiger.src.testers.longs.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.longs.maps.interfaces.Long2DoubleMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

public interface TestLong2DoubleMapGenerator extends TestMapGenerator<Long, Double> {
	public ObjectSamples<Long2DoubleMap.Entry> getSamples();
	public ObjectIterable<Long2DoubleMap.Entry> order(ObjectList<Long2DoubleMap.Entry> insertionOrder);
	public Long2DoubleMap create(Long2DoubleMap.Entry... elements);
	@Override
	default Long2DoubleMap create(Object... elements) {
		Long2DoubleMap.Entry[] result = new Long2DoubleMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Long2DoubleMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Long[] createKeyArray(int length) { return new Long[length]; }
	@Override
	default Double[] createValueArray(int length) { return new Double[length]; }
	@Override
	default Long2DoubleMap.Entry[] createArray(int length) { return new Long2DoubleMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Long, Double>> samples() {
		ObjectSamples<Long2DoubleMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getLongKey(), samples.e0().getDoubleValue()), 
			Helpers.mapEntry(samples.e1().getLongKey(), samples.e1().getDoubleValue()), 
			Helpers.mapEntry(samples.e2().getLongKey(), samples.e2().getDoubleValue()), 
			Helpers.mapEntry(samples.e3().getLongKey(), samples.e3().getDoubleValue()), 
			Helpers.mapEntry(samples.e4().getLongKey(), samples.e4().getDoubleValue())
		);
	}
}