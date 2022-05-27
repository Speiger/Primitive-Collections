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
import speiger.src.collections.ints.maps.abstracts.AbstractInt2ByteMap;
import speiger.src.collections.ints.maps.interfaces.Int2ByteMap;
import speiger.src.collections.ints.maps.interfaces.Int2ByteMap.Entry;
import speiger.src.collections.ints.maps.interfaces.Int2ByteNavigableMap;
import speiger.src.collections.ints.maps.interfaces.Int2ByteSortedMap;
import speiger.src.collections.ints.sets.IntSet;
import speiger.src.collections.ints.sets.IntSortedSet;
import speiger.src.collections.ints.sets.IntNavigableSet;
import speiger.src.testers.ints.generators.TestIntSetGenerator;
import speiger.src.testers.ints.generators.TestIntSortedSetGenerator;
import speiger.src.testers.ints.generators.TestIntNavigableSetGenerator;
import speiger.src.testers.ints.generators.maps.TestInt2ByteMapGenerator;
import speiger.src.testers.ints.generators.maps.TestInt2ByteSortedMapGenerator;
import speiger.src.testers.ints.utils.IntSamples;
import speiger.src.collections.bytes.collections.ByteCollection;
import speiger.src.collections.bytes.collections.ByteIterable;
import speiger.src.collections.bytes.lists.ByteList;
import speiger.src.collections.bytes.functions.ByteComparator;
import speiger.src.testers.bytes.generators.TestByteCollectionGenerator;
import speiger.src.testers.bytes.utils.ByteSamples;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.utils.ObjectIterators;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

public class DerivedInt2ByteMapGenerators {
	public static class NavigableMapGenerator extends SortedMapGenerator {
		public NavigableMapGenerator(TestInt2ByteSortedMapGenerator parent, Bound to, Bound from) {
			super(parent, to, from);
		}
		
	    @Override
	    Int2ByteNavigableMap createSubMap(Int2ByteSortedMap sortedMap, int firstExclusive, int lastExclusive) {
	    	Int2ByteNavigableMap map = (Int2ByteNavigableMap) sortedMap;
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
	        return (Int2ByteNavigableMap) super.createSubMap(map, firstExclusive, lastExclusive);
	      }
	    }
	}
	
	public static class SortedMapGenerator extends MapGenerator implements TestInt2ByteSortedMapGenerator {
		TestInt2ByteSortedMapGenerator parent;
		Bound to;
		Bound from;
		int firstInclusive;
		int lastInclusive;
		Comparator<Entry> entryComparator;

		public SortedMapGenerator(TestInt2ByteSortedMapGenerator parent, Bound to, Bound from) {
			super(parent);
			this.parent = parent;
			this.to = to;
			this.from = from;
			Int2ByteSortedMap map = parent.create();
			entryComparator = DerivedInt2ByteMapGenerators.entryComparator(map.comparator());
			ObjectList<Entry> samples = parent.getSamples().asList();
			samples.sort(entryComparator);
			firstInclusive = samples.get(0).getIntKey();
			lastInclusive = samples.get(samples.size() - 1).getIntKey();
		}
		
		@Override
		public Int2ByteSortedMap create(Entry... elements) {
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
		
		Int2ByteSortedMap createSubMap(Int2ByteSortedMap map, int firstExclusive, int lastExclusive) {
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
	
	public static class DescendingTestMapGenerator extends MapGenerator implements TestInt2ByteSortedMapGenerator
	{
		TestInt2ByteSortedMapGenerator parent;
		
		public DescendingTestMapGenerator(TestInt2ByteSortedMapGenerator parent) {
			super(parent);
			this.parent = parent;
		}

		@Override
		public Int2ByteNavigableMap create(Entry... elements) {
			return ((Int2ByteNavigableMap)parent.create(elements)).descendingMap();
		}
		
		@Override
		public Iterable<Map.Entry<Integer, Byte>> order(List<Map.Entry<Integer, Byte>> insertionOrder) {
			ObjectList<Map.Entry<Integer, Byte>> values = ObjectIterators.pour(ObjectIterators.wrap(parent.order(insertionOrder).iterator()));
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
	
	public static class MapGenerator implements TestInt2ByteMapGenerator
	{
		TestInt2ByteMapGenerator parent;
		
		public MapGenerator(TestInt2ByteMapGenerator parent) {
			this.parent = parent;
		}

		@Override
		public Iterable<Map.Entry<Integer, Byte>> order(List<Map.Entry<Integer, Byte>> insertionOrder) {
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
		public Int2ByteMap create(Entry... elements) {
			return parent.create(elements);
		}
	}

	public static class MapEntrySetGenerator implements TestObjectSetGenerator<Int2ByteMap.Entry> {
		TestInt2ByteMapGenerator generator;

		public MapEntrySetGenerator(OneSizeTestContainerGenerator<Map<Integer, Byte>, Map.Entry<Integer, Byte>> inner) {
			generator = (TestInt2ByteMapGenerator) inner.getInnerGenerator();
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
			return generator.create(elements).int2ByteEntrySet();
		}
	}

	public static TestIntSetGenerator keySetGenerator(OneSizeTestContainerGenerator<Map<Integer, Byte>, Map.Entry<Integer, Byte>> inner) {
		if (inner.getInnerGenerator() instanceof TestInt2ByteSortedMapGenerator) {
			IntSet set = ((TestInt2ByteSortedMapGenerator) inner.getInnerGenerator()).create().keySet();
			if(set instanceof IntNavigableSet) return new MapNavigableKeySetGenerator(inner);
			if(set instanceof IntSortedSet) return new MapSortedKeySetGenerator(inner);
		}
		return new MapKeySetGenerator(inner);
	}
	
	public static class MapNavigableKeySetGenerator extends MapSortedKeySetGenerator implements TestIntNavigableSetGenerator {
		public MapNavigableKeySetGenerator(OneSizeTestContainerGenerator<Map<Integer, Byte>, Map.Entry<Integer, Byte>> inner) {
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
		TestInt2ByteSortedMapGenerator generator;

		public MapSortedKeySetGenerator(OneSizeTestContainerGenerator<Map<Integer, Byte>, Map.Entry<Integer, Byte>> inner) {
			super(inner);
			generator = (TestInt2ByteSortedMapGenerator) inner.getInnerGenerator();
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
		TestInt2ByteMapGenerator generator;
		IntSamples samples;

		public MapKeySetGenerator(OneSizeTestContainerGenerator<Map<Integer, Byte>, Map.Entry<Integer, Byte>> inner) {
			generator = (TestInt2ByteMapGenerator) inner.getInnerGenerator();
			ObjectSamples<Int2ByteMap.Entry> samples = generator.getSamples();
			this.samples = new IntSamples(samples.e0().getIntKey(), samples.e1().getIntKey(), samples.e2().getIntKey(), samples.e3().getIntKey(), samples.e4().getIntKey());
		}

		@Override
		public IntSamples getSamples() {
			return samples;
		}

		@Override
		public IntIterable order(IntList insertionOrder) {
			byte value = generator.getSamples().e0().getByteValue();
			ObjectList<Entry> entries = new ObjectArrayList<>();
			for (int key : insertionOrder) {
				entries.add(new AbstractInt2ByteMap.BasicEntry(key, value));
			}
			IntList list = new IntArrayList();
			for (Entry entry : generator.order(entries)) {
				list.add(entry.getIntKey());
			}
			return list;
		}

		@Override
		public Iterable<Integer> order(List<Integer> insertionOrder) {
			byte value = generator.getSamples().e0().getByteValue();
			ObjectList<Entry> entries = new ObjectArrayList<>();
			for (int key : insertionOrder) {
				entries.add(new AbstractInt2ByteMap.BasicEntry(key, value));
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
				result[index] = new AbstractInt2ByteMap.BasicEntry(elements[index++], entry.getByteValue());
			}
			return generator.create(result).keySet();
		}

		@Override
		public IntSet create(Object... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractInt2ByteMap.BasicEntry((Integer) elements[index++], entry.getValue());
			}
			return generator.create(result).keySet();
		}
	}

	public static class MapValueCollectionGenerator implements TestByteCollectionGenerator {
		TestInt2ByteMapGenerator generator;
		ByteSamples samples;

		public MapValueCollectionGenerator(OneSizeTestContainerGenerator<Map<Integer, Byte>, Map.Entry<Integer, Byte>> inner) {
			generator = (TestInt2ByteMapGenerator) inner.getInnerGenerator();
			ObjectSamples<Int2ByteMap.Entry> samples = generator.getSamples();
			this.samples = new ByteSamples(samples.e0().getByteValue(), samples.e1().getByteValue(), samples.e2().getByteValue(), samples.e3().getByteValue(), samples.e4().getByteValue());
		}
		
		@Override
		public ByteSamples getSamples() {
			return samples;
		}

		@Override
		public ByteIterable order(ByteList insertionOrder) {
			ObjectList<Entry> list = generator.order(generator.getSamples().asList()).pourAsList();
			insertionOrder.sort(new ByteComparator() {
				@Override
				public int compare(byte key, byte value) {
					return Integer.signum(indexOf(key) - indexOf(value));
				}
				
				protected int indexOf(byte entry) {
					for(int i = 0,m=list.size();i<m;i++) {
						if(list.get(i).getByteValue() == entry) return i;
					}
					throw new IllegalArgumentException("Int2ByteMap.values generator can order only sample values");
				}
			});
			return insertionOrder;
		}

		@Override
		public Iterable<Byte> order(List<Byte> insertionOrder) {
			ObjectList<Entry> list = generator.order(generator.getSamples().asList()).pourAsList();
			insertionOrder.sort(new Comparator<Byte>() {
				@Override
				public int compare(Byte key, Byte value) {
					return Integer.signum(indexOf(key) - indexOf(value));
				}
				
				protected int indexOf(Byte entry) {
					for(int i = 0,m=list.size();i<m;i++) {
						if(list.get(i).getByteValue() == entry.byteValue()) return i;
					}
					throw new IllegalArgumentException("Int2ByteMap.values generator can order only sample values");
				}
			});
			return insertionOrder;
		}
		
		@Override
		public ByteCollection create(byte... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractInt2ByteMap.BasicEntry(entry.getIntKey(), elements[index++]);
			}
			return generator.create(result).values();
		}
		
		@Override
		public ByteCollection create(Object... elements) {
			Entry[] result = new Entry[elements.length];
			int index = 0;
			for (Entry entry : generator.getSamples().asList().subList(0, elements.length)) {
				result[index] = new AbstractInt2ByteMap.BasicEntry(entry.getKey(), (Byte)elements[index++]);
			}
			return generator.create(result).values();
		}
	}
	
	public static Comparator<Map.Entry<Integer, Byte>> entryObjectComparator(Comparator<Integer> keyComparator) {
		return new Comparator<Map.Entry<Integer, Byte>>() {
			@Override
			public int compare(Map.Entry<Integer, Byte> a, Map.Entry<Integer, Byte> b) {
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