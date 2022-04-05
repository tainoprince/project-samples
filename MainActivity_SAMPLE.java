//...
    private final MediaControllerCompat.Callback controllerCallback =
            new MediaControllerCompat.Callback() {
        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat state) {
            super.onPlaybackStateChanged(state);

            if(state.getState() == PlaybackStateCompat.STATE_PLAYING) {
                play.setVisibility(View.INVISIBLE);
                restart.setVisibility(View.INVISIBLE);
                pause.setVisibility(View.VISIBLE);
            } else if (state.getState() == PlaybackStateCompat.STATE_PAUSED){
                pause.setVisibility(View.INVISIBLE);
                restart.setVisibility(View.INVISIBLE);
                play.setVisibility(View.VISIBLE);
            } else if (state.getPosition() != PLAYBACK_POSITION_INITIAL){
                play.setVisibility(View.INVISIBLE);
                restart.setVisibility(View.VISIBLE);
            }

            Log.d(TAG, "onPlaybackStateChanged(...)");
        }

        @Override
        public void onMetadataChanged(MediaMetadataCompat metadata) {
            super.onMetadataChanged(metadata);

            var position = metadata.getLong(PlaybackService.POSITION);
            var mediaController =
                    MediaControllerCompat.getMediaController(MainActivity.this);

            if (mediaController.getPlaybackState().getState() ==
                    PlaybackStateCompat.STATE_PLAYING) {
                if (!touching) seekBar.setProgress((int) position);
            } else {
                seekBar.setProgress((int) position);
            }

            Log.d(TAG, "onMetadataChanged(...)");
        }
        
      private final MediaBrowserCompat.ConnectionCallback connectionCallbacks =
            new MediaBrowserCompat.ConnectionCallback() {

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onConnected() {
            MediaSessionCompat.Token token = mediaBrowser.getSessionToken();
            var mediaController = new MediaControllerCompat(
                            MainActivity.this, token);
            MediaControllerCompat.setMediaController(
                    MainActivity.this, mediaController);
            mediaController.registerCallback(controllerCallback);

            Log.d(TAG, "connectionCallbacks.onConnected()");
        }
        //more callbacks
    };
 //...
