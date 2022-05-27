package speiger.src.testers.bytes.generators;

import speiger.src.collections.bytes.sets.ByteSortedSet;

@SuppressWarnings("javadoc")
public interface TestByteSortedSetGenerator extends TestByteSetGenerator {
	@Override
	ByteSortedSet create(byte... elements);
	@Override
	ByteSortedSet create(Object... elements);
	

	  /**
	   * Returns an element less than the {@link #samples()} and less than {@link
	   * #belowSamplesGreater()}.
	   */
	  byte belowSamplesLesser();

	  /**
	   * Returns an element less than the {@link #samples()} but greater than {@link
	   * #belowSamplesLesser()}.
	   */
	  byte belowSamplesGreater();

	  /**
	   * Returns an element greater than the {@link #samples()} but less than {@link
	   * #aboveSamplesGreater()}.
	   */
	  byte aboveSamplesLesser();

	  /**
	   * Returns an element greater than the {@link #samples()} and greater than {@link
	   * #aboveSamplesLesser()}.
	   */
	  byte aboveSamplesGreater();
}