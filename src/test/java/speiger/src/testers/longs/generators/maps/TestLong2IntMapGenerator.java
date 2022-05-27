package speiger.src.testers.longs.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.longs.maps.interfaces.Long2IntMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public interface TestLong2IntMapGenerator extends TestMapGenerator<Long, Integer> {
	public ObjectSamples<Long2IntMap.Entry> getSamples();
	public ObjectIterable<Long2IntMap.Entry> order(ObjectList<Long2IntMap.Entry> insertionOrder);
	public Long2IntMap create(Long2IntMap.Entry... elements);
	@Override
	default Long2IntMap create(Object... elements) {
		Long2IntMap.Entry[] result = new Long2IntMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Long2IntMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Long[] createKeyArray(int length) { return new Long[length]; }
	@Override
	default Integer[] createValueArray(int length) { return new Integer[length]; }
	@Override
	default Long2IntMap.Entry[] createArray(int length) { return new Long2IntMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Long, Integer>> samples() {
		ObjectSamples<Long2IntMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getLongKey(), samples.e0().getIntValue()), 
			Helpers.mapEntry(samples.e1().getLongKey(), samples.e1().getIntValue()), 
			Helpers.mapEntry(samples.e2().getLongKey(), samples.e2().getIntValue()), 
			Helpers.mapEntry(samples.e3().getLongKey(), samples.e3().getIntValue()), 
			Helpers.mapEntry(samples.e4().getLongKey(), samples.e4().getIntValue())
		);
	}
}