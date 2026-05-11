package speiger.src.collections.floats.functions;

import java.util.NoSuchElementException;
import java.util.function.Supplier;

public final class OptionalFloat {
	private static final OptionalFloat EMPTY = new OptionalFloat();
	
	private final boolean isPresent;
	private final float value;
	
	private OptionalFloat() {
		this.isPresent = false;
		this.value = 0F;
	}
	
	private OptionalFloat(float value) {
		this.isPresent = true;
		this.value = value;
	}
	
	public static OptionalFloat empty() {
		return EMPTY;
	}
	
	public static OptionalFloat of(float value) {
		return new OptionalFloat(value);
	}
	
	public float getAsFloat() {
		if(!isPresent) throw new NoSuchElementException("No value present");
		return value;
	}
	
	public boolean isPresent() {
		return isPresent;
	}
	
	public boolean isEmpty() {
		return !isPresent;
	}
	
	public void ifPresent(FloatConsumer consumer) {
		if(isPresent) consumer.accept(value);
	}
	
    public void ifPresentOrElse(FloatConsumer action, Runnable emptyAction) {
        if (isPresent) action.accept(value);
        else emptyAction.run();
    }
	
	public float orElse(float other) {
		return isPresent ? value : other;
	}
	
	public float orElseGet(FloatSupplier other) {
		return isPresent ? value : other.getAsFloat();
	}
	
	public float orElseThrow() {
        if (!isPresent) throw new NoSuchElementException("No value present");
		return value;
	}
	
    public <X extends Throwable> float orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
    	if(isPresent) return value;
    	else throw exceptionSupplier.get();
    }
    
    @Override
    public boolean equals(Object obj) {
    	if(obj == this) return true;
    	if(obj instanceof OptionalFloat) {
    		OptionalFloat other = (OptionalFloat)obj;
    		return (isPresent && other.isPresent ? Float.floatToIntBits(value) == Float.floatToIntBits(other.value) : isPresent == other.isPresent);
    	}
    	return false;
    }
    
    @Override
    public int hashCode() {
    	return isPresent ? Float.hashCode(value) : 0;
    }
    
    @Override
    public String toString() {
    	return isPresent ? "OptionalFloat["+value+"]" : "OptionalFloat.empty";
    }
}