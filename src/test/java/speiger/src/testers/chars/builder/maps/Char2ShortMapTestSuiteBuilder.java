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
import speiger.src.collections.chars.maps.interfaces.Char2ShortMap;
import speiger.src.testers.shorts.builder.ShortCollectionTestSuiteBuilder;
import speiger.src.testers.shorts.generators.TestShortCollectionGenerator;
import speiger.src.testers.chars.builder.CharSetTestSuiteBuilder;
import speiger.src.testers.chars.generators.TestCharSetGenerator;
import speiger.src.testers.chars.generators.maps.TestChar2ShortMapGenerator;
import speiger.src.testers.chars.impl.maps.DerivedChar2ShortMapGenerators;
import speiger.src.testers.chars.tests.maps.Char2ShortMapAddToTester;
import speiger.src.testers.chars.tests.maps.Char2ShortMapClearTester;
import speiger.src.testers.chars.tests.maps.Char2ShortMapComputeIfAbsentTester;
import speiger.src.testers.chars.tests.maps.Char2ShortMapComputeIfPresentTester;
import speiger.src.testers.chars.tests.maps.Char2ShortMapComputeTester;
import speiger.src.testers.chars.tests.maps.Char2ShortMapContainsKeyTester;
import speiger.src.testers.chars.tests.maps.Char2ShortMapContainsValueTester;
import speiger.src.testers.chars.tests.maps.Char2ShortMapEntrySetTester;
import speiger.src.testers.chars.tests.maps.Char2ShortMapEqualsTester;
import speiger.src.testers.chars.tests.maps.Char2ShortMapForEachTester;
import speiger.src.testers.chars.tests.maps.Char2ShortMapGetOrDefaultTester;
import speiger.src.testers.chars.tests.maps.Char2ShortMapGetTester;
import speiger.src.testers.chars.tests.maps.Char2ShortMapHashCodeTester;
import speiger.src.testers.chars.tests.maps.Char2ShortMapIsEmptyTester;
import speiger.src.testers.chars.tests.maps.Char2ShortMapMergeTester;
import speiger.src.testers.chars.tests.maps.Char2ShortMapPutAllArrayTester;
import speiger.src.testers.chars.tests.maps.Char2ShortMapPutAllTester;
import speiger.src.testers.chars.tests.maps.Char2ShortMapPutIfAbsentTester;
import speiger.src.testers.chars.tests.maps.Char2ShortMapPutTester;
import speiger.src.testers.chars.tests.maps.Char2ShortMapRemoveEntryTester;
import speiger.src.testers.chars.tests.maps.Char2ShortMapRemoveOrDefaultTester;
import speiger.src.testers.chars.tests.maps.Char2ShortMapRemoveTester;
import speiger.src.testers.chars.tests.maps.Char2ShortMapReplaceAllTester;
import speiger.src.testers.chars.tests.maps.Char2ShortMapReplaceEntryTester;
import speiger.src.testers.chars.tests.maps.Char2ShortMapReplaceTester;
import speiger.src.testers.chars.tests.maps.Char2ShortMapSizeTester;
import speiger.src.testers.chars.tests.maps.Char2ShortMapSupplyIfAbsentTester;
import speiger.src.testers.chars.tests.maps.Char2ShortMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

public class Char2ShortMapTestSuiteBuilder extends MapTestSuiteBuilder<Character, Short> {
	public static Char2ShortMapTestSuiteBuilder using(TestChar2ShortMapGenerator generator) {
		return (Char2ShortMapTestSuiteBuilder) new Char2ShortMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Char2ShortMapClearTester.class);
		testers.add(Char2ShortMapComputeTester.class);
		testers.add(Char2ShortMapComputeIfAbsentTester.class);
		testers.add(Char2ShortMapComputeIfPresentTester.class);
		testers.add(Char2ShortMapSupplyIfAbsentTester.class);
		testers.add(Char2ShortMapContainsKeyTester.class);
		testers.add(Char2ShortMapContainsValueTester.class);
		testers.add(Char2ShortMapEntrySetTester.class);
		testers.add(Char2ShortMapEqualsTester.class);
		testers.add(Char2ShortMapForEachTester.class);
		testers.add(Char2ShortMapGetTester.class);
		testers.add(Char2ShortMapGetOrDefaultTester.class);
		testers.add(Char2ShortMapHashCodeTester.class);
		testers.add(Char2ShortMapIsEmptyTester.class);
		testers.add(Char2ShortMapMergeTester.class);
		testers.add(Char2ShortMapPutTester.class);
		testers.add(Char2ShortMapAddToTester.class);
		testers.add(Char2ShortMapPutAllTester.class);
		testers.add(Char2ShortMapPutAllArrayTester.class);
		testers.add(Char2ShortMapPutIfAbsentTester.class);
		testers.add(Char2ShortMapRemoveTester.class);
		testers.add(Char2ShortMapRemoveEntryTester.class);
		testers.add(Char2ShortMapRemoveOrDefaultTester.class);
		testers.add(Char2ShortMapReplaceTester.class);
		testers.add(Char2ShortMapReplaceAllTester.class);
		testers.add(Char2ShortMapReplaceEntryTester.class);
		testers.add(Char2ShortMapSizeTester.class);
		testers.add(Char2ShortMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Character, Short>, Map.Entry<Character, Short>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedChar2ShortMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedChar2ShortMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedChar2ShortMapGenerators.MapValueCollectionGenerator(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Char2ShortMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Char2ShortMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected CharSetTestSuiteBuilder createDerivedKeySetSuite(TestCharSetGenerator generator) {
		return CharSetTestSuiteBuilder.using(generator);
	}
	
	protected ShortCollectionTestSuiteBuilder createDerivedValueCollectionSuite(TestShortCollectionGenerator generator) {
		return ShortCollectionTestSuiteBuilder.using(generator);
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