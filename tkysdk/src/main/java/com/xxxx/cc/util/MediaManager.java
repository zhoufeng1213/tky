package com.xxxx.cc.util;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

public class MediaManager {
    private static MediaPlayer mMediaPlayer;
    private static boolean isPause;
    private static String mLocalPath;

    public static void playSound(final Context context, final String localPath, final MediaPlayer.OnCompletionListener onCompletionListenter) {
        if (MediaManager.mMediaPlayer == null) {
            (MediaManager.mMediaPlayer = new MediaPlayer()).setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(final MediaPlayer mp, final int what, final int extra) {
                    MediaManager.mMediaPlayer.reset();
                    return false;
                }
            });
        } else {
            MediaManager.mMediaPlayer.reset();
        }
        try {
            MediaManager.mMediaPlayer.setAudioStreamType(3);
            MediaManager.mMediaPlayer.setOnCompletionListener(onCompletionListenter);
            MediaManager.mMediaPlayer.setDataSource(localPath);
            MediaManager.mMediaPlayer.prepare();
            MediaManager.mMediaPlayer.start();
            MediaManager.mLocalPath = localPath;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static MediaPlayer getMediaPlayer() {
        if (MediaManager.mMediaPlayer == null) {
            (MediaManager.mMediaPlayer = new MediaPlayer()).setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(final MediaPlayer mp, final int what, final int extra) {
                    MediaManager.mMediaPlayer.reset();
                    return false;
                }
            });
        }
        return MediaManager.mMediaPlayer;
    }

    public static String getDataSource() {
        if (MediaManager.mMediaPlayer != null) {
            return MediaManager.mLocalPath;
        }
        return "";
    }

    public static synchronized void pause() {
        if (MediaManager.mMediaPlayer != null && MediaManager.mMediaPlayer.isPlaying()) {
            MediaManager.mMediaPlayer.pause();
            MediaManager.isPause = true;
        }
    }

    public static synchronized void stop() {
        if (MediaManager.mMediaPlayer != null && MediaManager.mMediaPlayer.isPlaying()) {
            MediaManager.mMediaPlayer.stop();
            MediaManager.isPause = false;
        }
    }

    public static synchronized void resume() {
        if (MediaManager.mMediaPlayer != null && MediaManager.isPause) {
            MediaManager.mMediaPlayer.start();
            MediaManager.isPause = false;
        }
    }

    public static boolean isPause() {
        return MediaManager.isPause;
    }

    public static void setPause(final boolean isPause) {
        MediaManager.isPause = isPause;
    }

    public static void release() {
        if (MediaManager.mMediaPlayer != null) {
            MediaManager.isPause = false;
            MediaManager.mMediaPlayer.stop();
            MediaManager.mMediaPlayer.release();
            MediaManager.mMediaPlayer = null;
        }
    }

    public static synchronized void reset() {
        if (MediaManager.mMediaPlayer != null) {
            MediaManager.mMediaPlayer.reset();
            MediaManager.isPause = false;
        }
    }

    public static synchronized boolean isPlaying() {
        return MediaManager.mMediaPlayer != null && MediaManager.mMediaPlayer.isPlaying();
    }

    @SuppressLint({"NewApi"})
    public String getPathFromUri(final Context context, final Uri uri) {
        if (uri == null) {
            return null;
        }
        final boolean after44 = Build.VERSION.SDK_INT >= 19;
        if (!after44 || !DocumentsContract.isDocumentUri(context, uri)) {
            final String scheme = uri.getScheme();
            String path = null;
            if ("content".equals(scheme)) {
                path = queryAbsolutePath(context, uri);
            } else if ("file".equals(scheme)) {
                path = uri.getPath();
            }
            return path;
        }
        final String authority = uri.getAuthority();
        if ("com.android.externalstorage.documents".equals(authority)) {
            final String docId = DocumentsContract.getDocumentId(uri);
            final String[] divide = docId.split(":");
            final String type = divide[0];
            if ("primary".equals(type)) {
                final String path2 = Environment.getExternalStorageDirectory().getAbsolutePath().concat("/").concat(divide[1]);
                return path2;
            }
            final String path2 = "/storage/".concat(type).concat("/").concat(divide[1]);
            return path2;
        } else if ("com.android.providers.downloads.documents".equals(authority)) {
            final String docId = DocumentsContract.getDocumentId(uri);
            if (docId.startsWith("raw:")) {
                final String path3 = docId.replaceFirst("raw:", "");
                return path3;
            }
            final Uri downloadUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.parseLong(docId));
            final String path4 = queryAbsolutePath(context, downloadUri);
            return path4;
        } else {
            if ("com.android.providers.media.documents".equals(authority)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] divide = docId.split(":");
                final String type = divide[0];
                Uri mediaUri = null;
                if ("image".equals(type)) {
                    mediaUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    mediaUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else {
                    if (!"audio".equals(type)) {
                        return null;
                    }
                    mediaUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                mediaUri = ContentUris.withAppendedId(mediaUri, Long.parseLong(divide[1]));
                final String path5 = queryAbsolutePath(context, mediaUri);
                return path5;
            }
            return null;
        }
    }

    public static String queryAbsolutePath(final Context context, final Uri uri) {
        final String[] projection = {"_data"};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow("_data");
                return cursor.getString(index);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    static {
        MediaManager.isPause = false;
        if (MediaManager.mMediaPlayer == null) {
            (MediaManager.mMediaPlayer = new MediaPlayer()).setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(final MediaPlayer mp, final int what, final int extra) {
                    MediaManager.mMediaPlayer.reset();
                    return false;
                }
            });
        }
    }
}
