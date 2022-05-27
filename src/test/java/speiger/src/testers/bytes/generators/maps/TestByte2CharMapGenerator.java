package speiger.src.testers.bytes.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.bytes.maps.interfaces.Byte2CharMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public interface TestByte2CharMapGenerator extends TestMapGenerator<Byte, Character> {
	public ObjectSamples<Byte2CharMap.Entry> getSamples();
	public ObjectIterable<Byte2CharMap.Entry> order(ObjectList<Byte2CharMap.Entry> insertionOrder);
	public Byte2CharMap create(Byte2CharMap.Entry... elements);
	@Override
	default Byte2CharMap create(Object... elements) {
		Byte2CharMap.Entry[] result = new Byte2CharMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Byte2CharMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Byte[] createKeyArray(int length) { return new Byte[length]; }
	@Override
	default Character[] createValueArray(int length) { return new Character[length]; }
	@Override
	default Byte2CharMap.Entry[] createArray(int length) { return new Byte2CharMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Byte, Character>> samples() {
		ObjectSamples<Byte2CharMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getByteKey(), samples.e0().getCharValue()), 
			Helpers.mapEntry(samples.e1().getByteKey(), samples.e1().getCharValue()), 
			Helpers.mapEntry(samples.e2().getByteKey(), samples.e2().getCharValue()), 
			Helpers.mapEntry(samples.e3().getByteKey(), samples.e3().getCharValue()), 
			Helpers.mapEntry(samples.e4().getByteKey(), samples.e4().getCharValue())
		);
	}
}