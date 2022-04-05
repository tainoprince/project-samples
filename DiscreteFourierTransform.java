package com.twon.analysis;

import static com.twon.media.DynamicAudioFormat.SAMPLE_RATE;
import static com.twon.util.Mathematics.INDEX_FIRST;
import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.twon.media.DynamicAudioData;
import com.twon.media.DynamicAudioData.Frame;

public final class DiscreteFourierTransform {
	
	private final DynamicAudioData audioData;
	private final boolean scaled;
	private final Supplier<Frame> frames;
	private static final int ARGS_LENGTH = 2,
							ARGS_START = 0,
							ARGS_END = 1;
	
	public DiscreteFourierTransform(DynamicAudioData audioData) { this(audioData, true); }
	
	public DiscreteFourierTransform(DynamicAudioData audioData, boolean scaled) {
		this.audioData = audioData;
		frames = this.audioData.getFrameData();
		this.scaled = scaled;
	}
	
	public final DynamicAudioData getAudioData() { return audioData; }
	
	public final boolean isScaled() { return scaled; }
	
	public final Function<Integer, DFT> toFrameView(short frequencyStart, 
															short frequencyEnd) {
		return f -> toFrequencyView(f).apply(frequencyStart, frequencyEnd);
	}
	
	public final BiFunction<Short, Short, DFT> toFrequencyView(int frameCount) {

		List<Integer> sampleList = toSampleList(frameCount);		
		var sampleCount = toScaledSampleCount(sampleList.size());
		
		class DFTfrequency {
			
			private final int frequency;
			
			DFTfrequency(int frequency) {
				this.frequency = frequency;
			}
			
			public final short getFrequency() {return (short) frequency;}
			
			List<Unit> toFrequencyUnitList() {
				return IntStream
						.range(INDEX_FIRST, sampleCount)
						.boxed()
						.map(i -> new Unit(getFrequency(), sampleList.get(i), 
								i, sampleCount))
						.toList();			
			}
		}							
		
		return (a,z) -> {
			//provide validated BiFunction arguments
			var functionArgsArray = validatedArgs(a, z);
			short start = functionArgsArray[ARGS_START],
					end = functionArgsArray[ARGS_END];
			
			return IntStream
					.range(start, end)
					.boxed()
					.map(frequency -> new DFTfrequency(frequency))
					.map(DFTfrequency::toFrequencyUnitList)
					.flatMap(List::stream)
					.collect(Collectors.collectingAndThen(Collectors.toList(), DFT::new));
		};
						
	}
	
	private short[] validatedArgs(short first, short last) {
		var functionArgs = new short[ARGS_LENGTH];
		short firstPos = (short) abs(first),
				lastPos = (short) abs(last);
			
			if (firstPos < lastPos) {
				functionArgs[ARGS_START] = firstPos;
				functionArgs[ARGS_END] = lastPos;
			} else {
				functionArgs[ARGS_START] = firstPos;
				functionArgs[ARGS_END] = lastPos;
			}
			
		return functionArgs;	
	}
	
	private List<Integer> toSampleList(int frameCount) {
		return Stream
				.generate(frames)
				.limit(frameCount)
				.map(Frame::toIntegers)
				.flatMap(List::stream)
				.toList();
	}
	
	private int toScaledSampleCount(int sampleCount) {
		return scaled ? (int) (sampleCount/ (double) SAMPLE_RATE) : sampleCount;
	}
	
	/**
	 * Discrete Fourier Transform (DFT) for multiple frequencies or samples.
	 * @author Marién
	 *
	 */
	public record DFT(List<Unit> units) {
		
		public DoubleSummaryStatistics toStats() {
			return units
					.stream()
					.collect(Collectors.summarizingDouble(Unit::eval));
		}
	}
	
	/**
	 * Discrete Fourier Transform (DFT) for a single sample and frequency i.e. a DFT unit.
	 * @author Marién
	 *
	 */
	public record Unit(short frequency, int sample, int sampleIndex, int sampleCount) {
		private static final int SQUARED = 2;
			
		private double eval() {
			return sqrt(pow(cosineTerm(), SQUARED) + pow(sineTerm(), SQUARED));
		}
		
		private double cosineTerm() {
			return sample*cos(theta());
		}
		
		private double sineTerm() {		
			return sample*sin(theta());
		}
		
		private double theta() {
			return (2*PI*frequency*sampleIndex)/(double) sampleCount;
		}
		
	}
	
}
