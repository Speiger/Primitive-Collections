package speiger.src.testers.ints.impl.maps;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.testing.DerivedCollectionGenerators.Bound;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;

import speiger.src.collections.ints.functions.IntComparator;
import speiger.src.collections.ints.collections.IntIterable;
import speiger.src.collections.ints.lists.IntArrayList;
import speiger.src.collections.ints.lists.IntList;
import speiger.src.collections.ints.maps.abstracts.AbstractInt2FloatMap;
import speiger.src.collections.ints.maps.interfaces.Int2FloatMap;
import speiger.src.collections.ints.maps.interfaces.Int2FloatMap.Entry;
import speiger.src.collections.ints.maps.interfaces.Int2FloatNavigableMap;
import speiger.src.collections.ints.maps.interfaces.Int2FloatSortedMap;
import speiger.src.collections.ints.sets.IntSet;
import speiger.src.collections.ints.sets.IntSortedSet;
import speiger.src.collections.ints.sets.IntNavigableSet;
import speiger.src.testers.ints.generators.TestIntSetGenerator;
import speiger.src.testers.ints.generators.TestIntSortedSetGenerator;
import speiger.src.testers.ints.generators.TestIntNavigableSetGenerator;
import speiger.src.testers.ints.generators.maps.TestInt2FloatMapGenerator;
import speiger.src.testers.ints.generators.maps.TestInt2FloatSortedMapGenerator;
import speiger.src.testers.ints.utils.IntSamples;
import speiger.src.collections.floats.collections.FloatCollection;
import speiger.src.collections.floats.collections.FloatIterable;
import speiger.src.collections.floats.lists.FloatList;
import speiger.src.collections.floats.functions.FloatComparator;
import speiger.src.testers.floats.generators.TestFloatCollectionGenerator;
import speiger.src.testers.floats.utils.FloatSamples;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.utils.ObjectIterators;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class DerivedInt2FloatMapGenerators {
	public static class NavigableMapGenerator extends SortedMapGenerator {
		public NavigableMapGenerator(TestInt2FloatSortedMapGenerator parent, Bound to, Bound from) {
			super(parent, to, from);
		}
		
	    @Override
	    Int2FloatNavigableMap createSubMap(Int2FloatSortedMap sortedMap, int firstExclusive, int lastExclusive) {
	    	Int2FloatNavigableMap map = (Int2FloatNavigableMap) sortedMap;
	      if (from == Bound.NO_BOUND && to == Bound.INCLUSIVE) {
	        return map.headMap(lastInclusive, true);
	      } else if (from == Bound.EXCLUSIVE && to == Bound.NO_BOUND) {
	        return map.tailMap(firstExclusive, false);
	      } else if (from == Bound.EXCLUSIVE && to == Bound.EXCLUSIVE) {
	        return map.subMap(firstExclusive, false, lastExclusive, false);
	      } else if (from == Bound.EXCLUSIVE && to == Bound.INCLUSIVE) {
	        return map.subMap(firstExclusive, false, lastInclusive, true);
	      } else if (from == Bound.INCLUSIVE && to == Bound.INCLUSIVE) {
	        return map.subMap(firstInclusive, true, lastInclusive, true);
	      } else {
	        return (Int2FloatNavigableMap) super.createSubMap(map, firstExclusive, lastExclusive);
	      }
	    }
	}
	
	public static class SortedMapGenerator extends MapGenerator implements TestInt2FloatSortedMapGenerator {
		TestInt2FloatSortedMapGenerator parent;
		Bound to;
		Bound from;
		int firstInclusive;
		int lastInclusive;
		Comparator<Entry> entryComparator;

		public SortedMapGenerator(TestInt2FloatSortedMapGenerator parent, Bound to, Bound from) {
			super(parent);
			this.parent = parent;
			this.to = to;
			this.from = from;
			Int2FloatSortedMap map = parent.create();
			entryComparator = DerivedInt2FloatMapGenerators.entryComparator(map.comparator());
			ObjectList<Entry> samples = parent.getSamples().asList();
			samples.sort(entryComparator);
			firstInclusive = samples.get(0).getIntKey();
			lastInclusive = samples.get(samples.size() - 1).getIntKey();
		}
		
		@Override
		public Int2FloatSortedMap create(Entry... elements) {
			ObjectList<Entry> entries = new ObjectArrayList<>();
			if (from != Bound.NO_BOUND) {
				entries.add(parent.belowSamplesLesser());
				entries.add(parent.belowSamplesGreater());
			}
			if (to != Bound.NO_BOUND) {
				entries.add(parent.aboveSamplesLesser());
				entries.add(parent.aboveSamplesGreater());
			}
			entries.addAll(elements);
			return createSubMap(parent.create(entries.toArray(Entry[]::new)), parent.belowSamplesGreater().getIntKey(), parent.aboveSamplesLesser().getIntKey());
		}
		
		Int2FloatSortedMap createSubMap(Int2FloatSortedMap map, int firstExclusive, int lastExclusive) {
			if (from == Bound.NO_BOUND && to == Bound.EXCLUSIVE) {
				return map.headMap(lastExclusive);
			} else if (from == Bound.INCLUSIVE && to == Bound.NO_BOUND) {
				return map.tailMap(firstInclusive);
			} else if (from == Bound.INCLUSIVE && to == Bound.EXCLUSIVE) {
				return map.subMap(firstInclusive, lastExclusive);
			} else {
				throw new IllegalArgumentException();
			}
		}

		@Override
		public Entry belowSamplesLesser() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Entry belowSamplesGreater() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Entry aboveSamplesLesser() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Entry aboveSamplesGreater() {
			throw new UnsupportedOperationException();
		}
	}
	
	public static class DescendingTestMapGenerator extends MapGenerator implements TestInt2FloatSortedMapGenerator
	{
		TestInt2FloatSortedMapGenerator parent;
		
		public DescendingTestMapGenerator(TestInt2FloatSortedMapGenerator parent) {
			super(parent);
			this.parent = parent;
		}

		@Override
		public Int2FloatNavigableMap create(Entry... elements) {
			return ((Int2FloatNavigableMap)parent.create(elements)).descendingMap();
		}
		
		@Override
		public Iterable<Map.Entry<Integer, Float>> order(List<Map.Entry<Integer, Float>> insertionOrder) {
			ObjectList<Map.Entry<Integer, Float>> values = ObjectIterators.pour(ObjectIterators.wrap(parent.order(insertionOrder).iterator()));
			ObjectLists.reverse(values);
			return values;
		}
		
		@Override
		public ObjectIterable<Entry> order(ObjectList<Entry> insertionOrder) {
			ObjectList<Entry> values = parent.order(insertionOrder).pourAsList();
			ObjectLists.reverse(values);
			return values;
		}
		
		@Override
		public Entry belowSamplesLesser() {
			return parent.aboveSamplesGreater();
		}

		@Override
		public Entry belowSamplesGreater() {
			return parent.aboveSamplesLesser();
		}

		@Override
		public Entry aboveSamplesLesser() {
			return parent.belowSamplesGreater();
		}

		@Override
		public Entry aboveSamplesGreater() {
			return parent.belowSamplesLesser();
		}
	}
	
	public static class MapGenerator implements TestInt2FloatMapGenerator
	{
		TestInt2FloatMapGenerator parent;
		
		public MapGenerator(TestInt2FloatMapGenerator parent) {
			this.parent = parent;
		}

		@Override
		public Iterable<Map.Entry<Integer, Float>> order(List<Map.Entry<Integer, Float>> insertionOrder) {
			return parent.order(insertionOrder);
		}
		
		@Override
		public ObjectSamples<Entry> getSamples() {
			return parent.getSamples();
		}
		
		@Override
		public ObjectIterable<Entry> order(ObjectList<Entry> insertionOrder) {
			return parent.order(insertionOrder);
		}
		
		@Override
		public Int2FloatMap create(Entry... elements) {
			return parent.create(elements);
		}
	}

	public static class MapEntrySetGenerator implements TestObjectSetGenerator<Int2FloatMap.Entry> {
		TestInt2FloatMapGenerator generator;

		public MapEntrySetGenerator(OneSizeTestContainerGenerator<Map<Integer, Float>, Map.Entry<Integer, Float>> inner) {
			generator = (TestInt2FloatMapGenerator) inner.getInnerGenerator();
		}

		@Override
		public ObjectSamples<Entry> getSamples() {
			return generator.getSamples();
		}

		@Override
		public ObjectIterable<Entry> order(ObjectList<Entry> insertionOrder) {
			return generator.order(insertionOrder);
		}

		@Override
		public Iterable<Entry> order(List<Entry> insertionOrder) {
			return generator.order(new ObjectArrayList<Entry>(insertionOrder));
		}

		@Override
		public Entry[] createArray(int length) {
			return new Entry[length];
		}

		@Override
		public ObjectSet<Entry> create(Object... elements) {
			return generator.create(elements).int2FloatEntrySet();
		}
	}

	public static TestIntSetGenerator keySetGenerator(OneSizeTestContainerGenerator<Map<Integer, Float>, Map.Entry<Integer, Float>> inner) {
		if (inner.getInnerGenerator() instanceof TestInt2FloatSortedMapGenerator) {
			IntSet set = ((TestInt2FloatSortedMapGenerator) inner.getInnerGenerator()).create().keySet();
			if(set instanceof IntNavigableSet) return new MapNavigableKeySetGenerator(inner);
			if(set instanceof IntSortedSet) return new MapSortedKeySetGenerator(inner);
		}
		return new MapKeySetGenerator(inner);
	}
	
	public static class MapNavigableKeySetGenerator extends MapSortedKeySetGenerator implements TestIntNavigableSetGenerator {
		public MapNavigableKeySetGenerator(OneSizeTestContainerGenerator<Map<Integer, Float>, Map.Entry<Integer, Float>> inner) {
			super(inner);
		}
		
		@Override
		public IntNavigableSet create(int... elements) {
			return (IntNavigableSet) super.create(elements);
		}

		@Override
		public IntNavigableSet create(Object... elements) {
			return (IntNavigableSet) super.create(elements);
		}
	}
	
	public static class MapSortedKeySetGenerator extends MapKeySetGenerator implements TestIntSortedSetGenerator {
		TestInt2FloatSortedMapGenerator generator;

		public MapSortedKeySetGenerator(OneSizeTestContainerGenerator<Map<Integer, Float>, Map.Entry<Integer, Float>> inner) {
			super(inner);
			generator = (TestInt2FloatSortedMapGenerator) inner.getInnerGenerator();
		}

		@Override
		public IntSortedSet create(int... elements) {
			return (IntSortedSet) super.create(elements);
		}

		@Override
		public IntSortedSet create(Object... elements) {
			return (IntSortedSet) super.create(elements);
		}

		@Override
		public int belowSamplesLesser() {
			return generator.belowSamplesLesser().getIntKey();
		}

		@Override
		public int belowSamplesGreater() {
			return generator.belowSamplesGreater().getIntKey();
		}

		@Override
		public int aboveSamplesLesser() {
			return generator.aboveSamplesLesser().getIntKey();
		}

		@Override
		public int aboveSamplesGreater() {
			return generator.aboveSamplesGreater().getIntKey();
		}
	}

	public static class MapKeySetGenerator implements TestIntSetGenerator {
		TestInt2FloatMapGenerator generator;
		IntSamples samples;

		public MapKeySetGenerator(OneSizeTestContainerGenerator<Map<Integer, Float>, Map.Entry<Integer, Float>> inner) {
			generator = (TestInt2FloatMapGenerator) inner.getInnerGenerator();
			ObjectSamples<Int2FloatMap.Entry> samples = generator.getSamples();
			this.samples = new IntSamples(samples.e0().getIntKey(), samples.e1().getIntKey(), samples.e2().getIntKey(), samples.e3().getIntKey(), samples.e4().getIntKey());
		}

		@Override
		public IntSamples getSamples() {
			return samples;
		}

		@Override
		public IntIterable order(IntList insertionOrder) {
			float value = generator.getSamples().e0().getFloatValue();
			ObjectList<Entry> entries = new ObjectArrayList<>();
			for (int key : insertionOrder) {
				entries.add(new AbstractInt2FloatMap.BasicEntry(key, value));
			}
			IntList list = new IntArrayList();
			for (Entry entry : generator.order(entries)) {
				list.add(entry.getIntKey());
			}
			return list;
		}

		@Override
		public Iterable<Integer> order(List<Integer> insertionOrder) {
			float value = generator.getSamples().e0().getFloatValue();
			ObjectList<Entry> entries = new ObjectArrayList<>();
			for (int key : insertionOrder) {
				entries.add(new AbstractInt2FloatMap.BasicEntry(key, value));
			}
			IntList list = new IntArrayList();
			for (Entry entry : generator.order(entries)) {
				list.add(entry.getIntKey());
			}
			return list;
		}
		
		@Override
		public IntSet create(int... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractInt2FloatMap.BasicEntry(elements[index++], entry.getFloatValue());
			}
			return generator.create(result).keySet();
		}

		@Override
		public IntSet create(Object... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractInt2FloatMap.BasicEntry((Integer) elements[index++], entry.getValue());
			}
			return generator.create(result).keySet();
		}
	}

	public static class MapValueCollectionGenerator implements TestFloatCollectionGenerator {
		TestInt2FloatMapGenerator generator;
		FloatSamples samples;

		public MapValueCollectionGenerator(OneSizeTestContainerGenerator<Map<Integer, Float>, Map.Entry<Integer, Float>> inner) {
			generator = (TestInt2FloatMapGenerator) inner.getInnerGenerator();
			ObjectSamples<Int2FloatMap.Entry> samples = generator.getSamples();
			this.samples = new FloatSamples(samples.e0().getFloatValue(), samples.e1().getFloatValue(), samples.e2().getFloatValue(), samples.e3().getFloatValue(), samples.e4().getFloatValue());
		}
		
		@Override
		public FloatSamples getSamples() {
			return samples;
		}

		@Override
		public FloatIterable order(FloatList insertionOrder) {
			ObjectList<Entry> list = generator.order(generator.getSamples().asList()).pourAsList();
			insertionOrder.sort(new FloatComparator() {
				@Override
				public int compare(float key, float value) {
					return Integer.signum(indexOf(key) - indexOf(value));
				}
				
				protected int indexOf(float entry) {
					for(int i = 0,m=list.size();i<m;i++) {
						if(Float.floatToIntBits(list.get(i).getFloatValue()) == Float.floatToIntBits(entry)) return i;
					}
					throw new IllegalArgumentException("Int2FloatMap.values generator can order only sample values");
				}
			});
			return insertionOrder;
		}

		@Override
		public Iterable<Float> order(List<Float> insertionOrder) {
			ObjectList<Entry> list = generator.order(generator.getSamples().asList()).pourAsList();
			insertionOrder.sort(new Comparator<Float>() {
				@Override
				public int compare(Float key, Float value) {
					return Integer.signum(indexOf(key) - indexOf(value));
				}
				
				protected int indexOf(Float entry) {
					for(int i = 0,m=list.size();i<m;i++) {
						if(Float.floatToIntBits(list.get(i).getFloatValue()) == Float.floatToIntBits(entry.floatValue())) return i;
					}
					throw new IllegalArgumentException("Int2FloatMap.values generator can order only sample values");
				}
			});
			return insertionOrder;
		}
		
		@Override
		public FloatCollection create(float... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractInt2FloatMap.BasicEntry(entry.getIntKey(), elements[index++]);
			}
			return generator.create(result).values();
		}
		
		@Override
		public FloatCollection create(Object... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractInt2FloatMap.BasicEntry(entry.getKey(), (Float)elements[index++]);
			}
			return generator.create(result).values();
		}
	}
	
	public static Comparator<Map.Entry<Integer, Float>> entryObjectComparator(Comparator<Integer> keyComparator) {
		return new Comparator<Map.Entry<Integer, Float>>() {
			@Override
			public int compare(Map.Entry<Integer, Float> a, Map.Entry<Integer, Float> b) {
				if(keyComparator == null) {
					return Integer.compare(a.getKey().intValue(), b.getKey().intValue());
				}
				return keyComparator.compare(a.getKey().intValue(), b.getKey().intValue());
			}
		};
	}
	
	public static Comparator<Entry> entryComparator(IntComparator keyComparator) {
		return new Comparator<Entry>() {
			@Override
			public int compare(Entry a, Entry b) {
				if(keyComparator == null) {
					return Integer.compare(a.getIntKey(), b.getIntKey());
				}
				return keyComparator.compare(a.getIntKey(), b.getIntKey());
			}
		};
	}
}