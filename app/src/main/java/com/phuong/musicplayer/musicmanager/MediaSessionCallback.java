//package com.phuong.musicplayer.musicmanager;
//
//import android.media.session.MediaSession;
//
//public class MediaSessionCallback extends MediaSession.Callback {
//    @Override
//    public void onPlay() {
//        if (mPlayingQueue != null && !mPlayingQueue.isEmpty()) {
//            handlePlayRequest();
//        }
//    }
//
//    private void handlePlayRequest() {
//
//    }
//
//    @Override
//    public void onSkipToQueueItem(long queueId) {
//        if (mPlayingQueue != null && !mPlayingQueue.isEmpty()) {
//            // set the current index on queue from the music Id:
//            mCurrentIndexOnQueue = QueueHelper.getMusicIndexOnQueue(mPlayingQueue, queueId);
//            // play the music
//            handlePlayRequest();
//        }
//    }
//
//    @Override
//    public void onSeekTo(long position) {
//        seek(position);
//    }
//
//    private void seek(long position) {
//
//    }
//
//    @Override
//    public void onPause() {
//        handlePauseRequest();
//    }
//
//    private void handlePauseRequest() {
//
//    }
//
//    @Override
//    public void onStop() {
//        handleStopRequest(null);
//    }
//
//    private void handleStopRequest(Object o) {
//
//    }
//
//    @Override
//    public void onSkipToNext() {
//        next();
//    }
//
//    private void next() {
//
//    }
//
//    @Override
//    public void onSkipToPrevious() {
//        prev();
//    }
//
//    private void prev() {
//
//    }
//}