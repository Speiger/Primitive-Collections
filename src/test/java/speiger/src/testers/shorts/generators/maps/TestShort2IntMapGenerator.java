package speiger.src.testers.shorts.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.shorts.maps.interfaces.Short2IntMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public interface TestShort2IntMapGenerator extends TestMapGenerator<Short, Integer> {
	public ObjectSamples<Short2IntMap.Entry> getSamples();
	public ObjectIterable<Short2IntMap.Entry> order(ObjectList<Short2IntMap.Entry> insertionOrder);
	public Short2IntMap create(Short2IntMap.Entry... elements);
	@Override
	default Short2IntMap create(Object... elements) {
		Short2IntMap.Entry[] result = new Short2IntMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Short2IntMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Short[] createKeyArray(int length) { return new Short[length]; }
	@Override
	default Integer[] createValueArray(int length) { return new Integer[length]; }
	@Override
	default Short2IntMap.Entry[] createArray(int length) { return new Short2IntMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Short, Integer>> samples() {
		ObjectSamples<Short2IntMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getShortKey(), samples.e0().getIntValue()), 
			Helpers.mapEntry(samples.e1().getShortKey(), samples.e1().getIntValue()), 
			Helpers.mapEntry(samples.e2().getShortKey(), samples.e2().getIntValue()), 
			Helpers.mapEntry(samples.e3().getShortKey(), samples.e3().getIntValue()), 
			Helpers.mapEntry(samples.e4().getShortKey(), samples.e4().getIntValue())
		);
	}
}