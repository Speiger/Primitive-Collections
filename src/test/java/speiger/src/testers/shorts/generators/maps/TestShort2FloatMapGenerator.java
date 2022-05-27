package speiger.src.testers.shorts.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.shorts.maps.interfaces.Short2FloatMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

public interface TestShort2FloatMapGenerator extends TestMapGenerator<Short, Float> {
	public ObjectSamples<Short2FloatMap.Entry> getSamples();
	public ObjectIterable<Short2FloatMap.Entry> order(ObjectList<Short2FloatMap.Entry> insertionOrder);
	public Short2FloatMap create(Short2FloatMap.Entry... elements);
	@Override
	default Short2FloatMap create(Object... elements) {
		Short2FloatMap.Entry[] result = new Short2FloatMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Short2FloatMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Short[] createKeyArray(int length) { return new Short[length]; }
	@Override
	default Float[] createValueArray(int length) { return new Float[length]; }
	@Override
	default Short2FloatMap.Entry[] createArray(int length) { return new Short2FloatMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Short, Float>> samples() {
		ObjectSamples<Short2FloatMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getShortKey(), samples.e0().getFloatValue()), 
			Helpers.mapEntry(samples.e1().getShortKey(), samples.e1().getFloatValue()), 
			Helpers.mapEntry(samples.e2().getShortKey(), samples.e2().getFloatValue()), 
			Helpers.mapEntry(samples.e3().getShortKey(), samples.e3().getFloatValue()), 
			Helpers.mapEntry(samples.e4().getShortKey(), samples.e4().getFloatValue())
		);
	}
}