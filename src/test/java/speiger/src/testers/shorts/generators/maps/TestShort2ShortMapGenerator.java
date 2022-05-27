package speiger.src.testers.shorts.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.shorts.maps.interfaces.Short2ShortMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public interface TestShort2ShortMapGenerator extends TestMapGenerator<Short, Short> {
	public ObjectSamples<Short2ShortMap.Entry> getSamples();
	public ObjectIterable<Short2ShortMap.Entry> order(ObjectList<Short2ShortMap.Entry> insertionOrder);
	public Short2ShortMap create(Short2ShortMap.Entry... elements);
	@Override
	default Short2ShortMap create(Object... elements) {
		Short2ShortMap.Entry[] result = new Short2ShortMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Short2ShortMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Short[] createKeyArray(int length) { return new Short[length]; }
	@Override
	default Short[] createValueArray(int length) { return new Short[length]; }
	@Override
	default Short2ShortMap.Entry[] createArray(int length) { return new Short2ShortMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Short, Short>> samples() {
		ObjectSamples<Short2ShortMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getShortKey(), samples.e0().getShortValue()), 
			Helpers.mapEntry(samples.e1().getShortKey(), samples.e1().getShortValue()), 
			Helpers.mapEntry(samples.e2().getShortKey(), samples.e2().getShortValue()), 
			Helpers.mapEntry(samples.e3().getShortKey(), samples.e3().getShortValue()), 
			Helpers.mapEntry(samples.e4().getShortKey(), samples.e4().getShortValue())
		);
	}
}