package speiger.src.collections.utils;

public class HashUtil
{
	public static final int DEFAULT_MIN_CAPACITY = 16;
	public static final float DEFAULT_LOAD_FACTOR = 0.75F;
	public static final float FAST_LOAD_FACTOR = 0.5F;
	public static final float FASTER_LOAD_FACTOR = 0.25F;
	
	private static final int INT_PHI = 0x9E3779B9;
	private static final int INV_INT_PHI = 0x144cbc89;
	
	public static int mix(final int x) {
		final int h = x * INT_PHI;
		return h ^ (h >>> 16);
	}
	
	public static int invMix(final int x) {
		return (x ^ x >>> 16) * INV_INT_PHI;
	}
	
	public static int nextPowerOfTwo(int x) {
		if(x == 0) return 1;
		x--;
		x |= x >> 1;
		x |= x >> 2;
		x |= x >> 4;
		x |= x >> 8;
		return (x | x >> 16) + 1;
	}
	
	public static long nextPowerOfTwo(long x) {
		if(x == 0) return 1L;
		x--;
		x |= x >> 1;
		x |= x >> 2;
		x |= x >> 4;
		x |= x >> 8;
		x |= x >> 16;
		return (x | x >> 32) + 1L;
	}
	
	public static int arraySize(int size, float loadFactor) {
		return (int)Math.min(1 << 30, Math.max(2, nextPowerOfTwo((long)Math.ceil(size / loadFactor))));
	}
}
