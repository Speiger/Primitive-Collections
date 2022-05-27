package speiger.src.testers.ints.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.ints.maps.interfaces.Int2FloatMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public interface TestInt2FloatMapGenerator extends TestMapGenerator<Integer, Float> {
	public ObjectSamples<Int2FloatMap.Entry> getSamples();
	public ObjectIterable<Int2FloatMap.Entry> order(ObjectList<Int2FloatMap.Entry> insertionOrder);
	public Int2FloatMap create(Int2FloatMap.Entry... elements);
	@Override
	default Int2FloatMap create(Object... elements) {
		Int2FloatMap.Entry[] result = new Int2FloatMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Int2FloatMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Integer[] createKeyArray(int length) { return new Integer[length]; }
	@Override
	default Float[] createValueArray(int length) { return new Float[length]; }
	@Override
	default Int2FloatMap.Entry[] createArray(int length) { return new Int2FloatMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Integer, Float>> samples() {
		ObjectSamples<Int2FloatMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getIntKey(), samples.e0().getFloatValue()), 
			Helpers.mapEntry(samples.e1().getIntKey(), samples.e1().getFloatValue()), 
			Helpers.mapEntry(samples.e2().getIntKey(), samples.e2().getFloatValue()), 
			Helpers.mapEntry(samples.e3().getIntKey(), samples.e3().getFloatValue()), 
			Helpers.mapEntry(samples.e4().getIntKey(), samples.e4().getFloatValue())
		);
	}
}