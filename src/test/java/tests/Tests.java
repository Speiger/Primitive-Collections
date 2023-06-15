package tests;

import junit.framework.Test;
import junit.framework.TestSuite;

@SuppressWarnings("javadoc")
public class Tests {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("Tests");
		TestSuite lists = new TestSuite("Lists");
//		lists.addTest(BooleanListTests.suite());
//		lists.addTest(ByteListTests.suite());
//		lists.addTest(ShortListTests.suite());
//		lists.addTest(CharListTests.suite());
//		lists.addTest(IntListTests.suite());
//		lists.addTest(LongListTests.suite());
//		lists.addTest(ObjectListTests.suite());
//		lists.addTest(FloatListTests.suite());
//		lists.addTest(DoubleListTests.suite());
		suite.addTest(lists);
		TestSuite queues = new TestSuite("Queues");
//		queues.addTest(ByteQueueTests.suite());
//		queues.addTest(ShortQueueTests.suite());
//		queues.addTest(CharQueueTests.suite());
//		queues.addTest(IntQueueTests.suite());
//		queues.addTest(LongQueueTests.suite());
//		queues.addTest(ObjectQueueTests.suite());
//		queues.addTest(FloatQueueTests.suite());
//		queues.addTest(DoubleQueueTests.suite());
		suite.addTest(queues);
		TestSuite sets = new TestSuite("Sets");
//		sets.addTest(ByteSetTests.suite());
//		sets.addTest(ShortSetTests.suite());
//		sets.addTest(CharSetTests.suite());
//		sets.addTest(IntSetTests.suite());
//		sets.addTest(LongSetTests.suite());
//		sets.addTest(ObjectSetTests.suite());
//		sets.addTest(FloatSetTests.suite());
//		sets.addTest(DoubleSetTests.suite());
		suite.addTest(sets);
		TestSuite maps = new TestSuite("Maps");
		System.out.println("Start");
		byteSuite(maps);
		System.out.println("Bytes");
		shortSuite(maps);
		System.out.println("Shorts");
		charSuite(maps);
		System.out.println("Chars");
		intSuite(maps);
		System.out.println("Ints");
		longSuite(maps);
		System.out.println("Longs");
		floatSuite(maps);
		System.out.println("Floats");
		doubleSuite(maps);
		System.out.println("Doubles");
		objectSuite(maps);
		System.out.println("Objects");
		suite.addTest(maps);
		System.out.println("End");
		return suite;
	}
		
	private static void byteSuite(TestSuite suite) {
		TestSuite maps = new TestSuite("ByteMaps");
//		maps.addTest(Byte2BooleanMapTests.suite());
//		maps.addTest(Byte2ByteMapTests.suite());
//		maps.addTest(Byte2CharMapTests.suite());
//		maps.addTest(Byte2ShortMapTests.suite());
//		maps.addTest(Byte2IntMapTests.suite());
//		maps.addTest(Byte2LongMapTests.suite());
//		maps.addTest(Byte2FloatMapTests.suite());
//		maps.addTest(Byte2DoubleMapTests.suite());
//		maps.addTest(Byte2ObjectMapTests.suite());
		suite.addTest(maps);
	}
	
	private static void shortSuite(TestSuite suite) {
		TestSuite maps = new TestSuite("ShortMaps");
//		maps.addTest(Short2BooleanMapTests.suite());
//		maps.addTest(Short2ByteMapTests.suite());
//		maps.addTest(Short2CharMapTests.suite());
//		maps.addTest(Short2ShortMapTests.suite());
//		maps.addTest(Short2IntMapTests.suite());
//		maps.addTest(Short2LongMapTests.suite());
//		maps.addTest(Short2FloatMapTests.suite());
//		maps.addTest(Short2DoubleMapTests.suite());
//		maps.addTest(Short2ObjectMapTests.suite());
		suite.addTest(maps);
	}
	
	private static void charSuite(TestSuite suite) {
		TestSuite maps = new TestSuite("CharMaps");
//		maps.addTest(Char2BooleanMapTests.suite());
//		maps.addTest(Char2ByteMapTests.suite());
//		maps.addTest(Char2CharMapTests.suite());
//		maps.addTest(Char2ShortMapTests.suite());
//		maps.addTest(Char2IntMapTests.suite());
//		maps.addTest(Char2LongMapTests.suite());
//		maps.addTest(Char2FloatMapTests.suite());
//		maps.addTest(Char2DoubleMapTests.suite());
//		maps.addTest(Char2ObjectMapTests.suite());
		suite.addTest(maps);
	}
	
	private static void intSuite(TestSuite suite) {
		TestSuite maps = new TestSuite("IntMaps");
//		maps.addTest(Int2BooleanMapTests.suite());
//		maps.addTest(Int2ByteMapTests.suite());
//		maps.addTest(Int2CharMapTests.suite());
//		maps.addTest(Int2ShortMapTests.suite());
//		maps.addTest(Int2IntMapTests.suite());
//		maps.addTest(Int2LongMapTests.suite());
//		maps.addTest(Int2FloatMapTests.suite());
//		maps.addTest(Int2DoubleMapTests.suite());
//		maps.addTest(Int2ObjectMapTests.suite());
		suite.addTest(maps);
	}
	
	private static void longSuite(TestSuite suite) {
		TestSuite maps = new TestSuite("LongMaps");
//		maps.addTest(Long2BooleanMapTests.suite());
//		maps.addTest(Long2ByteMapTests.suite());
//		maps.addTest(Long2CharMapTests.suite());
//		maps.addTest(Long2ShortMapTests.suite());
//		maps.addTest(Long2IntMapTests.suite());
//		maps.addTest(Long2LongMapTests.suite());
//		maps.addTest(Long2FloatMapTests.suite());
//		maps.addTest(Long2DoubleMapTests.suite());
//		maps.addTest(Long2ObjectMapTests.suite());
		suite.addTest(maps);
	}
	
	private static void floatSuite(TestSuite suite) {
		TestSuite maps = new TestSuite("FloatMaps");
//		maps.addTest(Float2BooleanMapTests.suite());
//		maps.addTest(Float2ByteMapTests.suite());
//		maps.addTest(Float2CharMapTests.suite());
//		maps.addTest(Float2ShortMapTests.suite());
//		maps.addTest(Float2IntMapTests.suite());
//		maps.addTest(Float2LongMapTests.suite());
//		maps.addTest(Float2FloatMapTests.suite());
//		maps.addTest(Float2DoubleMapTests.suite());
//		maps.addTest(Float2ObjectMapTests.suite());
		suite.addTest(maps);
	}
	
	private static void doubleSuite(TestSuite suite) {
		TestSuite maps = new TestSuite("DoubleMaps");
//		maps.addTest(Double2BooleanMapTests.suite());
//		maps.addTest(Double2ByteMapTests.suite());
//		maps.addTest(Double2CharMapTests.suite());
//		maps.addTest(Double2ShortMapTests.suite());
//		maps.addTest(Double2IntMapTests.suite());
//		maps.addTest(Double2LongMapTests.suite());
//		maps.addTest(Double2FloatMapTests.suite());
//		maps.addTest(Double2DoubleMapTests.suite());
//		maps.addTest(Double2ObjectMapTests.suite());
		suite.addTest(maps);
	}
	
	private static void objectSuite(TestSuite suite) {
		TestSuite maps = new TestSuite("ObjectMaps");
//		maps.addTest(Object2BooleanMapTests.suite());
//		maps.addTest(Object2ByteMapTests.suite());
//		maps.addTest(Object2CharMapTests.suite());
//		maps.addTest(Object2ShortMapTests.suite());
//		maps.addTest(Object2IntMapTests.suite());
//		maps.addTest(Object2LongMapTests.suite());
//		maps.addTest(Object2FloatMapTests.suite());
//		maps.addTest(Object2DoubleMapTests.suite());
//		maps.addTest(Object2ObjectMapTests.suite());
		suite.addTest(maps);
	}
}