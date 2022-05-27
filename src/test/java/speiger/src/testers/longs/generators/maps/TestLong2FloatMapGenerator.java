package speiger.src.testers.longs.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.longs.maps.interfaces.Long2FloatMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

public interface TestLong2FloatMapGenerator extends TestMapGenerator<Long, Float> {
	public ObjectSamples<Long2FloatMap.Entry> getSamples();
	public ObjectIterable<Long2FloatMap.Entry> order(ObjectList<Long2FloatMap.Entry> insertionOrder);
	public Long2FloatMap create(Long2FloatMap.Entry... elements);
	@Override
	default Long2FloatMap create(Object... elements) {
		Long2FloatMap.Entry[] result = new Long2FloatMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Long2FloatMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Long[] createKeyArray(int length) { return new Long[length]; }
	@Override
	default Float[] createValueArray(int length) { return new Float[length]; }
	@Override
	default Long2FloatMap.Entry[] createArray(int length) { return new Long2FloatMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Long, Float>> samples() {
		ObjectSamples<Long2FloatMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getLongKey(), samples.e0().getFloatValue()), 
			Helpers.mapEntry(samples.e1().getLongKey(), samples.e1().getFloatValue()), 
			Helpers.mapEntry(samples.e2().getLongKey(), samples.e2().getFloatValue()), 
			Helpers.mapEntry(samples.e3().getLongKey(), samples.e3().getFloatValue()), 
			Helpers.mapEntry(samples.e4().getLongKey(), samples.e4().getFloatValue())
		);
	}
}