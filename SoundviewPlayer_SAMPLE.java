//...
    public final void play() throws IOException {
        var audioStream = new ByteArrayInputStream(samples);
        var chunks = new byte[buffer];
        long fixedPosition = position;

        audioStream.skip(position);
        audioTrack.play();
        bytesRemaining = audioStream.available();
        while (audioStream.read(chunks) != -1 &&
                audioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
            if (position != fixedPosition) {
                audioTrack.pause();
                audioTrack.flush();
                audioStream.close();
                play();
                break;
            }

            write = audioTrack.write(chunks, offset, length);
            if (offset != SAMPLES_OFFSET) {
                offset = SAMPLES_OFFSET;
                length = chunks.length;
            }

            bytesRemaining = audioStream.available();
            audioTrack.setNotificationMarkerPosition(audioTrack.getPlaybackHeadPosition());
            Log.d(TAG, "write = " + write + ", bytesRemaining = " + bytesRemaining);
        }
        if (isFinished()) audioTrack.stop();

        position = (totalBytes - bytesRemaining) % totalBytes;
        audioStream.close();

        Log.d(TAG, "play()");
    }
//...
