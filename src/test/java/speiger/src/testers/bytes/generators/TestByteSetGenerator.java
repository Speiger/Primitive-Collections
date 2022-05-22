package speiger.src.testers.bytes.generators;

import com.google.common.collect.testing.TestSetGenerator;

import speiger.src.collections.bytes.sets.ByteSet;

public interface TestByteSetGenerator extends TestByteCollectionGenerator, TestSetGenerator<Byte> {
	@Override
	ByteSet create(byte...elements);
	@Override
	ByteSet create(Object...elements);
}