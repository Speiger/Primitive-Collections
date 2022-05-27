package speiger.src.testers.shorts.builder.maps;

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
import speiger.src.collections.shorts.maps.interfaces.Short2CharMap;
import speiger.src.testers.chars.builder.CharCollectionTestSuiteBuilder;
import speiger.src.testers.chars.generators.TestCharCollectionGenerator;
import speiger.src.testers.shorts.builder.ShortSetTestSuiteBuilder;
import speiger.src.testers.shorts.generators.TestShortSetGenerator;
import speiger.src.testers.shorts.generators.maps.TestShort2CharMapGenerator;
import speiger.src.testers.shorts.impl.maps.DerivedShort2CharMapGenerators;
import speiger.src.testers.shorts.tests.maps.Short2CharMapAddToTester;
import speiger.src.testers.shorts.tests.maps.Short2CharMapClearTester;
import speiger.src.testers.shorts.tests.maps.Short2CharMapComputeIfAbsentTester;
import speiger.src.testers.shorts.tests.maps.Short2CharMapComputeIfPresentTester;
import speiger.src.testers.shorts.tests.maps.Short2CharMapComputeTester;
import speiger.src.testers.shorts.tests.maps.Short2CharMapContainsKeyTester;
import speiger.src.testers.shorts.tests.maps.Short2CharMapContainsValueTester;
import speiger.src.testers.shorts.tests.maps.Short2CharMapEntrySetTester;
import speiger.src.testers.shorts.tests.maps.Short2CharMapEqualsTester;
import speiger.src.testers.shorts.tests.maps.Short2CharMapForEachTester;
import speiger.src.testers.shorts.tests.maps.Short2CharMapGetOrDefaultTester;
import speiger.src.testers.shorts.tests.maps.Short2CharMapGetTester;
import speiger.src.testers.shorts.tests.maps.Short2CharMapHashCodeTester;
import speiger.src.testers.shorts.tests.maps.Short2CharMapIsEmptyTester;
import speiger.src.testers.shorts.tests.maps.Short2CharMapMergeTester;
import speiger.src.testers.shorts.tests.maps.Short2CharMapPutAllArrayTester;
import speiger.src.testers.shorts.tests.maps.Short2CharMapPutAllTester;
import speiger.src.testers.shorts.tests.maps.Short2CharMapPutIfAbsentTester;
import speiger.src.testers.shorts.tests.maps.Short2CharMapPutTester;
import speiger.src.testers.shorts.tests.maps.Short2CharMapRemoveEntryTester;
import speiger.src.testers.shorts.tests.maps.Short2CharMapRemoveOrDefaultTester;
import speiger.src.testers.shorts.tests.maps.Short2CharMapRemoveTester;
import speiger.src.testers.shorts.tests.maps.Short2CharMapReplaceAllTester;
import speiger.src.testers.shorts.tests.maps.Short2CharMapReplaceEntryTester;
import speiger.src.testers.shorts.tests.maps.Short2CharMapReplaceTester;
import speiger.src.testers.shorts.tests.maps.Short2CharMapSizeTester;
import speiger.src.testers.shorts.tests.maps.Short2CharMapSupplyIfAbsentTester;
import speiger.src.testers.shorts.tests.maps.Short2CharMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

@SuppressWarnings("javadoc")
public class Short2CharMapTestSuiteBuilder extends MapTestSuiteBuilder<Short, Character> {
	public static Short2CharMapTestSuiteBuilder using(TestShort2CharMapGenerator generator) {
		return (Short2CharMapTestSuiteBuilder) new Short2CharMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Short2CharMapClearTester.class);
		testers.add(Short2CharMapComputeTester.class);
		testers.add(Short2CharMapComputeIfAbsentTester.class);
		testers.add(Short2CharMapComputeIfPresentTester.class);
		testers.add(Short2CharMapSupplyIfAbsentTester.class);
		testers.add(Short2CharMapContainsKeyTester.class);
		testers.add(Short2CharMapContainsValueTester.class);
		testers.add(Short2CharMapEntrySetTester.class);
		testers.add(Short2CharMapEqualsTester.class);
		testers.add(Short2CharMapForEachTester.class);
		testers.add(Short2CharMapGetTester.class);
		testers.add(Short2CharMapGetOrDefaultTester.class);
		testers.add(Short2CharMapHashCodeTester.class);
		testers.add(Short2CharMapIsEmptyTester.class);
		testers.add(Short2CharMapMergeTester.class);
		testers.add(Short2CharMapPutTester.class);
		testers.add(Short2CharMapAddToTester.class);
		testers.add(Short2CharMapPutAllTester.class);
		testers.add(Short2CharMapPutAllArrayTester.class);
		testers.add(Short2CharMapPutIfAbsentTester.class);
		testers.add(Short2CharMapRemoveTester.class);
		testers.add(Short2CharMapRemoveEntryTester.class);
		testers.add(Short2CharMapRemoveOrDefaultTester.class);
		testers.add(Short2CharMapReplaceTester.class);
		testers.add(Short2CharMapReplaceAllTester.class);
		testers.add(Short2CharMapReplaceEntryTester.class);
		testers.add(Short2CharMapSizeTester.class);
		testers.add(Short2CharMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Short, Character>, Map.Entry<Short, Character>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedShort2CharMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedShort2CharMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedShort2CharMapGenerators.MapValueCollectionGenerator(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Short2CharMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Short2CharMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected ShortSetTestSuiteBuilder createDerivedKeySetSuite(TestShortSetGenerator generator) {
		return ShortSetTestSuiteBuilder.using(generator);
	}
	
	protected CharCollectionTestSuiteBuilder createDerivedValueCollectionSuite(TestCharCollectionGenerator generator) {
		return CharCollectionTestSuiteBuilder.using(generator);
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