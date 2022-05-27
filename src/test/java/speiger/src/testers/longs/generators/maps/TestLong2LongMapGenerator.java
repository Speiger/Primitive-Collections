package speiger.src.testers.longs.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.longs.maps.interfaces.Long2LongMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

public interface TestLong2LongMapGenerator extends TestMapGenerator<Long, Long> {
	public ObjectSamples<Long2LongMap.Entry> getSamples();
	public ObjectIterable<Long2LongMap.Entry> order(ObjectList<Long2LongMap.Entry> insertionOrder);
	public Long2LongMap create(Long2LongMap.Entry... elements);
	@Override
	default Long2LongMap create(Object... elements) {
		Long2LongMap.Entry[] result = new Long2LongMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Long2LongMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Long[] createKeyArray(int length) { return new Long[length]; }
	@Override
	default Long[] createValueArray(int length) { return new Long[length]; }
	@Override
	default Long2LongMap.Entry[] createArray(int length) { return new Long2LongMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Long, Long>> samples() {
		ObjectSamples<Long2LongMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getLongKey(), samples.e0().getLongValue()), 
			Helpers.mapEntry(samples.e1().getLongKey(), samples.e1().getLongValue()), 
			Helpers.mapEntry(samples.e2().getLongKey(), samples.e2().getLongValue()), 
			Helpers.mapEntry(samples.e3().getLongKey(), samples.e3().getLongValue()), 
			Helpers.mapEntry(samples.e4().getLongKey(), samples.e4().getLongValue())
		);
	}
}