package speiger.src.testers.bytes.generators;

import speiger.src.collections.bytes.sets.ByteOrderedSet;

public interface TestByteOrderedSetGenerator extends TestByteSetGenerator {
	@Override
	ByteOrderedSet create(byte... elements);
	@Override
	ByteOrderedSet create(Object... elements);
}