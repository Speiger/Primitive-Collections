package speiger.src.testers.bytes.builder.maps;

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
import speiger.src.collections.bytes.maps.interfaces.Byte2CharMap;
import speiger.src.testers.chars.builder.CharCollectionTestSuiteBuilder;
import speiger.src.testers.chars.generators.TestCharCollectionGenerator;
import speiger.src.testers.bytes.builder.ByteSetTestSuiteBuilder;
import speiger.src.testers.bytes.generators.TestByteSetGenerator;
import speiger.src.testers.bytes.generators.maps.TestByte2CharMapGenerator;
import speiger.src.testers.bytes.impl.maps.DerivedByte2CharMapGenerators;
import speiger.src.testers.bytes.tests.maps.Byte2CharMapAddToTester;
import speiger.src.testers.bytes.tests.maps.Byte2CharMapClearTester;
import speiger.src.testers.bytes.tests.maps.Byte2CharMapComputeIfAbsentTester;
import speiger.src.testers.bytes.tests.maps.Byte2CharMapComputeIfPresentTester;
import speiger.src.testers.bytes.tests.maps.Byte2CharMapComputeTester;
import speiger.src.testers.bytes.tests.maps.Byte2CharMapContainsKeyTester;
import speiger.src.testers.bytes.tests.maps.Byte2CharMapContainsValueTester;
import speiger.src.testers.bytes.tests.maps.Byte2CharMapEntrySetTester;
import speiger.src.testers.bytes.tests.maps.Byte2CharMapEqualsTester;
import speiger.src.testers.bytes.tests.maps.Byte2CharMapForEachTester;
import speiger.src.testers.bytes.tests.maps.Byte2CharMapGetOrDefaultTester;
import speiger.src.testers.bytes.tests.maps.Byte2CharMapGetTester;
import speiger.src.testers.bytes.tests.maps.Byte2CharMapHashCodeTester;
import speiger.src.testers.bytes.tests.maps.Byte2CharMapIsEmptyTester;
import speiger.src.testers.bytes.tests.maps.Byte2CharMapMergeTester;
import speiger.src.testers.bytes.tests.maps.Byte2CharMapPutAllArrayTester;
import speiger.src.testers.bytes.tests.maps.Byte2CharMapPutAllTester;
import speiger.src.testers.bytes.tests.maps.Byte2CharMapPutIfAbsentTester;
import speiger.src.testers.bytes.tests.maps.Byte2CharMapPutTester;
import speiger.src.testers.bytes.tests.maps.Byte2CharMapRemoveEntryTester;
import speiger.src.testers.bytes.tests.maps.Byte2CharMapRemoveOrDefaultTester;
import speiger.src.testers.bytes.tests.maps.Byte2CharMapRemoveTester;
import speiger.src.testers.bytes.tests.maps.Byte2CharMapReplaceAllTester;
import speiger.src.testers.bytes.tests.maps.Byte2CharMapReplaceEntryTester;
import speiger.src.testers.bytes.tests.maps.Byte2CharMapReplaceTester;
import speiger.src.testers.bytes.tests.maps.Byte2CharMapSizeTester;
import speiger.src.testers.bytes.tests.maps.Byte2CharMapSupplyIfAbsentTester;
import speiger.src.testers.bytes.tests.maps.Byte2CharMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

@SuppressWarnings("javadoc")
public class Byte2CharMapTestSuiteBuilder extends MapTestSuiteBuilder<Byte, Character> {
	public static Byte2CharMapTestSuiteBuilder using(TestByte2CharMapGenerator generator) {
		return (Byte2CharMapTestSuiteBuilder) new Byte2CharMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Byte2CharMapClearTester.class);
		testers.add(Byte2CharMapComputeTester.class);
		testers.add(Byte2CharMapComputeIfAbsentTester.class);
		testers.add(Byte2CharMapComputeIfPresentTester.class);
		testers.add(Byte2CharMapSupplyIfAbsentTester.class);
		testers.add(Byte2CharMapContainsKeyTester.class);
		testers.add(Byte2CharMapContainsValueTester.class);
		testers.add(Byte2CharMapEntrySetTester.class);
		testers.add(Byte2CharMapEqualsTester.class);
		testers.add(Byte2CharMapForEachTester.class);
		testers.add(Byte2CharMapGetTester.class);
		testers.add(Byte2CharMapGetOrDefaultTester.class);
		testers.add(Byte2CharMapHashCodeTester.class);
		testers.add(Byte2CharMapIsEmptyTester.class);
		testers.add(Byte2CharMapMergeTester.class);
		testers.add(Byte2CharMapPutTester.class);
		testers.add(Byte2CharMapAddToTester.class);
		testers.add(Byte2CharMapPutAllTester.class);
		testers.add(Byte2CharMapPutAllArrayTester.class);
		testers.add(Byte2CharMapPutIfAbsentTester.class);
		testers.add(Byte2CharMapRemoveTester.class);
		testers.add(Byte2CharMapRemoveEntryTester.class);
		testers.add(Byte2CharMapRemoveOrDefaultTester.class);
		testers.add(Byte2CharMapReplaceTester.class);
		testers.add(Byte2CharMapReplaceAllTester.class);
		testers.add(Byte2CharMapReplaceEntryTester.class);
		testers.add(Byte2CharMapSizeTester.class);
		testers.add(Byte2CharMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Byte, Character>, Map.Entry<Byte, Character>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedByte2CharMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedByte2CharMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedByte2CharMapGenerators.MapValueCollectionGenerator(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Byte2CharMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Byte2CharMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected ByteSetTestSuiteBuilder createDerivedKeySetSuite(TestByteSetGenerator generator) {
		return ByteSetTestSuiteBuilder.using(generator);
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