package speiger.src.testers.chars.builder.maps;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.testing.AbstractTester;
import com.google.common.collect.testing.FeatureSpecificTestSuiteBuilder;
import com.google.common.collect.testing.MapTestSuiteBuilder;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.Feature;
import com.google.common.collect.testing.features.MapFeature;
import com.google.common.collect.testing.testers.CollectionIteratorTester;

import junit.framework.TestSuite;
import speiger.src.collections.chars.maps.interfaces.Char2LongMap;
import speiger.src.testers.longs.builder.LongCollectionTestSuiteBuilder;
import speiger.src.testers.longs.generators.TestLongCollectionGenerator;
import speiger.src.testers.chars.builder.CharSetTestSuiteBuilder;
import speiger.src.testers.chars.generators.TestCharSetGenerator;
import speiger.src.testers.chars.generators.maps.TestChar2LongMapGenerator;
import speiger.src.testers.chars.impl.maps.DerivedChar2LongMapGenerators;
import speiger.src.testers.chars.tests.maps.Char2LongMapAddToTester;
import speiger.src.testers.chars.tests.maps.Char2LongMapClearTester;
import speiger.src.testers.chars.tests.maps.Char2LongMapComputeIfAbsentTester;
import speiger.src.testers.chars.tests.maps.Char2LongMapComputeIfPresentTester;
import speiger.src.testers.chars.tests.maps.Char2LongMapComputeTester;
import speiger.src.testers.chars.tests.maps.Char2LongMapContainsKeyTester;
import speiger.src.testers.chars.tests.maps.Char2LongMapContainsValueTester;
import speiger.src.testers.chars.tests.maps.Char2LongMapEntrySetTester;
import speiger.src.testers.chars.tests.maps.Char2LongMapEqualsTester;
import speiger.src.testers.chars.tests.maps.Char2LongMapForEachTester;
import speiger.src.testers.chars.tests.maps.Char2LongMapGetOrDefaultTester;
import speiger.src.testers.chars.tests.maps.Char2LongMapGetTester;
import speiger.src.testers.chars.tests.maps.Char2LongMapHashCodeTester;
import speiger.src.testers.chars.tests.maps.Char2LongMapIsEmptyTester;
import speiger.src.testers.chars.tests.maps.Char2LongMapMergeTester;
import speiger.src.testers.chars.tests.maps.Char2LongMapPutAllArrayTester;
import speiger.src.testers.chars.tests.maps.Char2LongMapPutAllTester;
import speiger.src.testers.chars.tests.maps.Char2LongMapPutIfAbsentTester;
import speiger.src.testers.chars.tests.maps.Char2LongMapPutTester;
import speiger.src.testers.chars.tests.maps.Char2LongMapRemoveEntryTester;
import speiger.src.testers.chars.tests.maps.Char2LongMapRemoveOrDefaultTester;
import speiger.src.testers.chars.tests.maps.Char2LongMapRemoveTester;
import speiger.src.testers.chars.tests.maps.Char2LongMapReplaceAllTester;
import speiger.src.testers.chars.tests.maps.Char2LongMapReplaceEntryTester;
import speiger.src.testers.chars.tests.maps.Char2LongMapReplaceTester;
import speiger.src.testers.chars.tests.maps.Char2LongMapSizeTester;
import speiger.src.testers.chars.tests.maps.Char2LongMapSupplyIfAbsentTester;
import speiger.src.testers.chars.tests.maps.Char2LongMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

@SuppressWarnings("javadoc")
public class Char2LongMapTestSuiteBuilder extends MapTestSuiteBuilder<Character, Long> {
	public static Char2LongMapTestSuiteBuilder using(TestChar2LongMapGenerator generator) {
		return (Char2LongMapTestSuiteBuilder) new Char2LongMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Char2LongMapClearTester.class);
		testers.add(Char2LongMapComputeTester.class);
		testers.add(Char2LongMapComputeIfAbsentTester.class);
		testers.add(Char2LongMapComputeIfPresentTester.class);
		testers.add(Char2LongMapSupplyIfAbsentTester.class);
		testers.add(Char2LongMapContainsKeyTester.class);
		testers.add(Char2LongMapContainsValueTester.class);
		testers.add(Char2LongMapEntrySetTester.class);
		testers.add(Char2LongMapEqualsTester.class);
		testers.add(Char2LongMapForEachTester.class);
		testers.add(Char2LongMapGetTester.class);
		testers.add(Char2LongMapGetOrDefaultTester.class);
		testers.add(Char2LongMapHashCodeTester.class);
		testers.add(Char2LongMapIsEmptyTester.class);
		testers.add(Char2LongMapMergeTester.class);
		testers.add(Char2LongMapPutTester.class);
		testers.add(Char2LongMapAddToTester.class);
		testers.add(Char2LongMapPutAllTester.class);
		testers.add(Char2LongMapPutAllArrayTester.class);
		testers.add(Char2LongMapPutIfAbsentTester.class);
		testers.add(Char2LongMapRemoveTester.class);
		testers.add(Char2LongMapRemoveEntryTester.class);
		testers.add(Char2LongMapRemoveOrDefaultTester.class);
		testers.add(Char2LongMapReplaceTester.class);
		testers.add(Char2LongMapReplaceAllTester.class);
		testers.add(Char2LongMapReplaceEntryTester.class);
		testers.add(Char2LongMapSizeTester.class);
		testers.add(Char2LongMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Character, Long>, Map.Entry<Character, Long>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedChar2LongMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedChar2LongMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedChar2LongMapGenerators.MapValueCollectionGenerator(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Char2LongMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Char2LongMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected CharSetTestSuiteBuilder createDerivedKeySetSuite(TestCharSetGenerator generator) {
		return CharSetTestSuiteBuilder.using(generator);
	}
	
	protected LongCollectionTestSuiteBuilder createDerivedValueCollectionSuite(TestLongCollectionGenerator generator) {
		return LongCollectionTestSuiteBuilder.using(generator);
	}
	
	private static Set<Feature<?>> computeEntrySetFeatures(Set<Feature<?>> mapFeatures) {
		Set<Feature<?>> entrySetFeatures = MapTestSuiteBuilder.computeCommonDerivedCollectionFeatures(mapFeatures);
		if (mapFeatures.contains(MapFeature.ALLOWS_NULL_ENTRY_QUERIES)) {
			entrySetFeatures.add(CollectionFeature.ALLOWS_NULL_QUERIES);
		}
		entrySetFeatures.remove(SpecialFeature.COPYING);
		return entrySetFeatures;
	}

	private static Set<Feature<?>> computeKeySetFeatures(Set<Feature<?>> mapFeatures) {
		Set<Feature<?>> keySetFeatures = MapTestSuiteBuilder.computeCommonDerivedCollectionFeatures(mapFeatures);
		keySetFeatures.add(CollectionFeature.SUBSET_VIEW);
		if (mapFeatures.contains(MapFeature.ALLOWS_NULL_KEYS)) {
			keySetFeatures.add(CollectionFeature.ALLOWS_NULL_VALUES);
		} else if (mapFeatures.contains(MapFeature.ALLOWS_NULL_KEY_QUERIES)) {
			keySetFeatures.add(CollectionFeature.ALLOWS_NULL_QUERIES);
		}
		keySetFeatures.remove(SpecialFeature.COPYING);
		return keySetFeatures;
	}

	private static Set<Feature<?>> computeValuesCollectionFeatures(Set<Feature<?>> mapFeatures) {
		Set<Feature<?>> valuesCollectionFeatures = MapTestSuiteBuilder.computeCommonDerivedCollectionFeatures(mapFeatures);
		if (mapFeatures.contains(MapFeature.ALLOWS_NULL_VALUE_QUERIES)) {
			valuesCollectionFeatures.add(CollectionFeature.ALLOWS_NULL_QUERIES);
		}
		if (mapFeatures.contains(MapFeature.ALLOWS_NULL_VALUES)) {
			valuesCollectionFeatures.add(CollectionFeature.ALLOWS_NULL_VALUES);
		}
		valuesCollectionFeatures.remove(SpecialFeature.COPYING);
		return valuesCollectionFeatures;
	}
	
	private static Set<Method> getEntrySetSuppressing(Set<Method> suppressing) {
		TestUtils.getSurpession(suppressing, CollectionIteratorTester.class, "testIterator_removeAffectsBackingCollection");
		TestUtils.getSurpession(suppressing, ObjectCollectionIteratorTester.class, "testIterator_removeAffectsBackingCollection");
		TestUtils.getSurpession(suppressing, ObjectCollectionRemoveAllTester.class, "testRemoveAll_someFetchRemovedElements");
		TestUtils.getSurpession(suppressing, ObjectCollectionRetainAllTester.class, "testRetainAllExtra_disjointPreviouslyNonEmpty", "testRetainAllExtra_containsDuplicatesSizeSeveral", "testRetainAllExtra_subset", "testRetainAllExtra_partialOverlap");
		return suppressing;
	}
}