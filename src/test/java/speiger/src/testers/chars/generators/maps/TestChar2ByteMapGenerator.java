package speiger.src.testers.chars.generators.maps;

import java.util.Map;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;

import speiger.src.collections.chars.maps.interfaces.Char2ByteMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.utils.ObjectSamples;

public interface TestChar2ByteMapGenerator extends TestMapGenerator<Character, Byte> {
	public ObjectSamples<Char2ByteMap.Entry> getSamples();
	public ObjectIterable<Char2ByteMap.Entry> order(ObjectList<Char2ByteMap.Entry> insertionOrder);
	public Char2ByteMap create(Char2ByteMap.Entry... elements);
	@Override
	default Char2ByteMap create(Object... elements) {
		Char2ByteMap.Entry[] result = new Char2ByteMap.Entry[elements.length];
		for(int i = 0;i<elements.length;i++) {
			result[i] = (Char2ByteMap.Entry) elements[i];
		}
		return create(result);
	}
	
	@Override
	default Character[] createKeyArray(int length) { return new Character[length]; }
	@Override
	default Byte[] createValueArray(int length) { return new Byte[length]; }
	@Override
	default Char2ByteMap.Entry[] createArray(int length) { return new Char2ByteMap.Entry[length]; }
	
	@Override
	default SampleElements<Map.Entry<Character, Byte>> samples() {
		ObjectSamples<Char2ByteMap.Entry> samples = getSamples();
		return new SampleElements<>(
			Helpers.mapEntry(samples.e0().getCharKey(), samples.e0().getByteValue()), 
			Helpers.mapEntry(samples.e1().getCharKey(), samples.e1().getByteValue()), 
			Helpers.mapEntry(samples.e2().getCharKey(), samples.e2().getByteValue()), 
			Helpers.mapEntry(samples.e3().getCharKey(), samples.e3().getByteValue()), 
			Helpers.mapEntry(samples.e4().getCharKey(), samples.e4().getByteValue())
		);
	}
}