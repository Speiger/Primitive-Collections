package speiger.src.testers.shorts.generators;

import speiger.src.collections.shorts.sets.ShortSortedSet;

@SuppressWarnings("javadoc")
public interface TestShortSortedSetGenerator extends TestShortSetGenerator {
	@Override
	ShortSortedSet create(short... elements);
	@Override
	ShortSortedSet create(Object... elements);
	

	  /**
	   * Returns an element less than the {@link #samples()} and less than {@link
	   * #belowSamplesGreater()}.
	   */
	  short belowSamplesLesser();

	  /**
	   * Returns an element less than the {@link #samples()} but greater than {@link
	   * #belowSamplesLesser()}.
	   */
	  short belowSamplesGreater();

	  /**
	   * Returns an element greater than the {@link #samples()} but less than {@link
	   * #aboveSamplesGreater()}.
	   */
	  short aboveSamplesLesser();

	  /**
	   * Returns an element greater than the {@link #samples()} and greater than {@link
	   * #aboveSamplesLesser()}.
	   */
	  short aboveSamplesGreater();
}