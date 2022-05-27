package speiger.src.testers.bytes.generators;

import speiger.src.collections.bytes.sets.ByteNavigableSet;

@SuppressWarnings("javadoc")
public interface TestByteNavigableSetGenerator extends TestByteSortedSetGenerator {
	@Override
	ByteNavigableSet create(byte... elements);
	@Override
	ByteNavigableSet create(Object... elements);
}