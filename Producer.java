package com.twon.lab;

import java.util.function.Function;
import static java.lang.Math.sin;
import static java.lang.Math.tan;
import static java.lang.Math.expm1;
import static java.lang.Math.log1p;
import static java.lang.Math.pow;
import static java.lang.Math.PI;

sealed interface Producer {

	Function<Integer, Double> toFunction();
	
	default Function<Integer, Double> addFunction(Producer operand) {
		return i -> toFunction().apply(i) + operand.toFunction().apply(i);
	}
	
	default Function<Integer, Double> subtractFunction(Producer operand) {
		return i -> toFunction().apply(i) - operand.toFunction().apply(i);
	}
	
	default Function<Integer, Double> multiplyFunction(Producer operand) {
		return i -> toFunction().apply(i)*operand.toFunction().apply(i);
	}
	
	default Function<Integer, Double> divideFunction(Producer operand) {
		return i -> toFunction().apply(i)/(double) operand.toFunction().apply(i);
	}
}

record Pseudorandom() implements Producer {

	@Override
	public Function<Integer, Double> toFunction() {
		return i -> Math.random();
	}
	
}

record Constant(double value) implements Producer {
	static final double ZERO = 0.0,
						NEGATIVE = -1.0,
						POSITIVE = 1.0;

	@Override
	public Function<Integer, Double> toFunction() {
		return i -> value;
	}
	
}

record Linear() implements Producer {
	private static final double SCALAR = 1.0;

	@Override
	public Function<Integer, Double> toFunction() {
		return i -> SCALAR*i;
	}
	
}

record Sinusoidal(Producer theta) implements Producer {
	static final double WAVE = 2*PI;
	
	@Override
	public Function<Integer, Double> toFunction() {
		return i -> sin(theta.toFunction().apply(i));
	}

}

record Tangent(Producer theta) implements Producer {

	@Override
	public Function<Integer, Double> toFunction() {
		return i -> tan(theta.toFunction().apply(i));
	}
	
}

record Power(Producer base, Producer exponent) implements Producer {

	@Override
	public Function<Integer, Double> toFunction() {
		return i -> pow(base.toFunction().apply(i), exponent.toFunction().apply(i));
	}
	
}

record Exponential(Producer input) implements Producer {

	@Override
	public Function<Integer, Double> toFunction() {
		return i -> expm1(input.toFunction().apply(i));
	}
	
}

record Logarithmic(Producer input) implements Producer {

	@Override
	public Function<Integer, Double> toFunction() {
		return i -> log1p(input.toFunction().apply(i));
	}
}
