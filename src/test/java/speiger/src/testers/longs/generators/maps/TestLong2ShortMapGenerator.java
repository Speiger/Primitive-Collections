package speiger.src.testers.longs.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.longs.maps.interfaces.Long2ShortMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public interface TestLong2ShortMapGenerator extends TestMapGenerator<Long, Short> {
	public ObjectSamples<Long2ShortMap.Entry> getSamples();
	public ObjectIterable<Long2ShortMap.Entry> order(ObjectList<Long2ShortMap.Entry> insertionOrder);
	public Long2ShortMap create(Long2ShortMap.Entry... elements);
	@Override
	default Long2ShortMap create(Object... elements) {
		Long2ShortMap.Entry[] result = new Long2ShortMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Long2ShortMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Long[] createKeyArray(int length) { return new Long[length]; }
	@Override
	default Short[] createValueArray(int length) { return new Short[length]; }
	@Override
	default Long2ShortMap.Entry[] createArray(int length) { return new Long2ShortMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Long, Short>> samples() {
		ObjectSamples<Long2ShortMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getLongKey(), samples.e0().getShortValue()), 
			Helpers.mapEntry(samples.e1().getLongKey(), samples.e1().getShortValue()), 
			Helpers.mapEntry(samples.e2().getLongKey(), samples.e2().getShortValue()), 
			Helpers.mapEntry(samples.e3().getLongKey(), samples.e3().getShortValue()), 
			Helpers.mapEntry(samples.e4().getLongKey(), samples.e4().getShortValue())
		);
	}
}