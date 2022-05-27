package speiger.src.testers.ints.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.ints.maps.interfaces.Int2ShortMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public interface TestInt2ShortMapGenerator extends TestMapGenerator<Integer, Short> {
	public ObjectSamples<Int2ShortMap.Entry> getSamples();
	public ObjectIterable<Int2ShortMap.Entry> order(ObjectList<Int2ShortMap.Entry> insertionOrder);
	public Int2ShortMap create(Int2ShortMap.Entry... elements);
	@Override
	default Int2ShortMap create(Object... elements) {
		Int2ShortMap.Entry[] result = new Int2ShortMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Int2ShortMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Integer[] createKeyArray(int length) { return new Integer[length]; }
	@Override
	default Short[] createValueArray(int length) { return new Short[length]; }
	@Override
	default Int2ShortMap.Entry[] createArray(int length) { return new Int2ShortMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Integer, Short>> samples() {
		ObjectSamples<Int2ShortMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getIntKey(), samples.e0().getShortValue()), 
			Helpers.mapEntry(samples.e1().getIntKey(), samples.e1().getShortValue()), 
			Helpers.mapEntry(samples.e2().getIntKey(), samples.e2().getShortValue()), 
			Helpers.mapEntry(samples.e3().getIntKey(), samples.e3().getShortValue()), 
			Helpers.mapEntry(samples.e4().getIntKey(), samples.e4().getShortValue())
		);
	}
}