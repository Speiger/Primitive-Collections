package speiger.src.testers.shorts.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.shorts.maps.interfaces.Short2CharMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

public interface TestShort2CharMapGenerator extends TestMapGenerator<Short, Character> {
	public ObjectSamples<Short2CharMap.Entry> getSamples();
	public ObjectIterable<Short2CharMap.Entry> order(ObjectList<Short2CharMap.Entry> insertionOrder);
	public Short2CharMap create(Short2CharMap.Entry... elements);
	@Override
	default Short2CharMap create(Object... elements) {
		Short2CharMap.Entry[] result = new Short2CharMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Short2CharMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Short[] createKeyArray(int length) { return new Short[length]; }
	@Override
	default Character[] createValueArray(int length) { return new Character[length]; }
	@Override
	default Short2CharMap.Entry[] createArray(int length) { return new Short2CharMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Short, Character>> samples() {
		ObjectSamples<Short2CharMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getShortKey(), samples.e0().getCharValue()), 
			Helpers.mapEntry(samples.e1().getShortKey(), samples.e1().getCharValue()), 
			Helpers.mapEntry(samples.e2().getShortKey(), samples.e2().getCharValue()), 
			Helpers.mapEntry(samples.e3().getShortKey(), samples.e3().getCharValue()), 
			Helpers.mapEntry(samples.e4().getShortKey(), samples.e4().getCharValue())
		);
	}
}