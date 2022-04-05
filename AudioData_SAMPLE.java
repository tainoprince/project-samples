//...	
	public final Supplier<Frame> getFrameData() { return frameData; }
	
	public record Frame(List<Sample> samples) {
		
		public final List<Byte> toBytes() {
			return samples
					.stream()
					.map(Sample::toBytes)
					.flatMap(Collection::stream)
					.toList();
		}
		
		public final List<Integer> toIntegers() {
			return samples
					.stream()
					.map(Sample::toInt)
					.toList();
		}		
	}
	
	public record Sample(int bytesPerSample, double rawSample, int sampleMax) {	
		private static final ByteConverter byteConverter;
		
		static { byteConverter = new ByteConverter(); }
		
		public final List<Byte> toBytes() {			
			return byteConverter.convert(bytesPerSample, toInt());
		}
		
		public final int toInt() { return (int) (rawSample*sampleMax); }
	}
	//...
