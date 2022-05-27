package speiger.src.testers.floats.generators;

import speiger.src.collections.floats.sets.FloatSortedSet;

@SuppressWarnings("javadoc")
public interface TestFloatSortedSetGenerator extends TestFloatSetGenerator {
	@Override
	FloatSortedSet create(float... elements);
	@Override
	FloatSortedSet create(Object... elements);
	

	  /**
	   * Returns an element less than the {@link #samples()} and less than {@link
	   * #belowSamplesGreater()}.
	   */
	  float belowSamplesLesser();

	  /**
	   * Returns an element less than the {@link #samples()} but greater than {@link
	   * #belowSamplesLesser()}.
	   */
	  float belowSamplesGreater();

	  /**
	   * Returns an element greater than the {@link #samples()} but less than {@link
	   * #aboveSamplesGreater()}.
	   */
	  float aboveSamplesLesser();

	  /**
	   * Returns an element greater than the {@link #samples()} and greater than {@link
	   * #aboveSamplesLesser()}.
	   */
	  float aboveSamplesGreater();
}