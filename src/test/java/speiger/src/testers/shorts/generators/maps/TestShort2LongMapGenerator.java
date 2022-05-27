package speiger.src.testers.shorts.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.shorts.maps.interfaces.Short2LongMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public interface TestShort2LongMapGenerator extends TestMapGenerator<Short, Long> {
	public ObjectSamples<Short2LongMap.Entry> getSamples();
	public ObjectIterable<Short2LongMap.Entry> order(ObjectList<Short2LongMap.Entry> insertionOrder);
	public Short2LongMap create(Short2LongMap.Entry... elements);
	@Override
	default Short2LongMap create(Object... elements) {
		Short2LongMap.Entry[] result = new Short2LongMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Short2LongMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Short[] createKeyArray(int length) { return new Short[length]; }
	@Override
	default Long[] createValueArray(int length) { return new Long[length]; }
	@Override
	default Short2LongMap.Entry[] createArray(int length) { return new Short2LongMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Short, Long>> samples() {
		ObjectSamples<Short2LongMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getShortKey(), samples.e0().getLongValue()), 
			Helpers.mapEntry(samples.e1().getShortKey(), samples.e1().getLongValue()), 
			Helpers.mapEntry(samples.e2().getShortKey(), samples.e2().getLongValue()), 
			Helpers.mapEntry(samples.e3().getShortKey(), samples.e3().getLongValue()), 
			Helpers.mapEntry(samples.e4().getShortKey(), samples.e4().getLongValue())
		);
	}
}