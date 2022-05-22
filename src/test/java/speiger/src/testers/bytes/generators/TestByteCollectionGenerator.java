package speiger.src.testers.bytes.generators;

import java.util.List;

import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestCollectionGenerator;

import speiger.src.collections.bytes.collections.ByteCollection;
import speiger.src.collections.bytes.collections.ByteIterable;
import speiger.src.collections.bytes.lists.ByteList;
import speiger.src.testers.bytes.utils.ByteSamples;

public interface TestByteCollectionGenerator extends TestCollectionGenerator<Byte>
{
	public ByteSamples getSamples();
	public ByteCollection create(byte...elements);
	public ByteIterable order(ByteList insertionOrder);
	
	@Override
	public default SampleElements<Byte> samples() {return getSamples().toSamples();}
	@Override
	public ByteCollection create(Object... elements);
	@Override
	public default Byte[] createArray(int length) { return new Byte[length]; }
	@Override
	public Iterable<Byte> order(List<Byte> insertionOrder);
}