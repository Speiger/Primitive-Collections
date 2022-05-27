package speiger.src.testers.longs.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.longs.maps.interfaces.Long2CharMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public interface TestLong2CharMapGenerator extends TestMapGenerator<Long, Character> {
	public ObjectSamples<Long2CharMap.Entry> getSamples();
	public ObjectIterable<Long2CharMap.Entry> order(ObjectList<Long2CharMap.Entry> insertionOrder);
	public Long2CharMap create(Long2CharMap.Entry... elements);
	@Override
	default Long2CharMap create(Object... elements) {
		Long2CharMap.Entry[] result = new Long2CharMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Long2CharMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Long[] createKeyArray(int length) { return new Long[length]; }
	@Override
	default Character[] createValueArray(int length) { return new Character[length]; }
	@Override
	default Long2CharMap.Entry[] createArray(int length) { return new Long2CharMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Long, Character>> samples() {
		ObjectSamples<Long2CharMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getLongKey(), samples.e0().getCharValue()), 
			Helpers.mapEntry(samples.e1().getLongKey(), samples.e1().getCharValue()), 
			Helpers.mapEntry(samples.e2().getLongKey(), samples.e2().getCharValue()), 
			Helpers.mapEntry(samples.e3().getLongKey(), samples.e3().getCharValue()), 
			Helpers.mapEntry(samples.e4().getLongKey(), samples.e4().getCharValue())
		);
	}
}