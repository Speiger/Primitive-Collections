package speiger.src.testers.chars.generators;

import speiger.src.collections.chars.sets.CharSortedSet;

public interface TestCharSortedSetGenerator extends TestCharSetGenerator {
	@Override
	CharSortedSet create(char... elements);
	@Override
	CharSortedSet create(Object... elements);
	

	  /**
	   * Returns an element less than the {@link #samples()} and less than {@link
	   * #belowSamplesGreater()}.
	   */
	  char belowSamplesLesser();

	  /**
	   * Returns an element less than the {@link #samples()} but greater than {@link
	   * #belowSamplesLesser()}.
	   */
	  char belowSamplesGreater();

	  /**
	   * Returns an element greater than the {@link #samples()} but less than {@link
	   * #aboveSamplesGreater()}.
	   */
	  char aboveSamplesLesser();

	  /**
	   * Returns an element greater than the {@link #samples()} and greater than {@link
	   * #aboveSamplesLesser()}.
	   */
	  char aboveSamplesGreater();
}