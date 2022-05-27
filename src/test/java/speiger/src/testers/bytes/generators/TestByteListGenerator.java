package speiger.src.testers.bytes.generators;

import com.google.common.collect.testing.TestListGenerator;

import speiger.src.collections.bytes.lists.ByteList;

@SuppressWarnings("javadoc")
public interface TestByteListGenerator extends TestListGenerator<Byte>, TestByteCollectionGenerator
{
	@Override
	ByteList create(Object... elements);
	@Override
	ByteList create(byte... elements);
}