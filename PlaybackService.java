//...
private final MediaSessionCompat.Callback callback =
            new MediaSessionCompat.Callback() {
      
        @Override
        public void onPrepare() {
            super.onPrepare();

            prepareState();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                async = CompletableFuture
                        .runAsync(() -> player.prepare())
                        .thenRunAsync(() -> setTransportControlsCommand());
            } else {
                var thread = new Thread(() -> {
                    player.prepare();
                    setTransportControlsCommand();
                });
                thread.start();
            }

            Log.d(TAG, "onPrepare()");
        }
      
        @Override
        public void onPause() {
            super.onPause();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                async.thenRunAsync(() -> pauseAsync());
            } else {
                var thread = new Thread(() -> pauseAsync());
                thread.start();
            }

            Log.d(TAG, "onPause()");
        }
      //more callbacks
      
    };
//...
