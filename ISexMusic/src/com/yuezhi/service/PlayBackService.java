package com.yuezhi.service;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import com.yuezhi.activity.Index_Activity;
import com.yuezhi.activity.R;
import com.yuezhi.domain.Mp3Info;
import com.yuezhi.utils.MediaUtil;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.text.style.UpdateAppearance;
import android.util.Log;

public class PlayBackService extends Service {
	private static final String TAG = "live";

	public static final String EXTRA_CMD = "music_cmd";
	public static final int CMD_PLAY_OR_PAUSE = 0;
	public static final int CMD_NEXT_PLAY = 1;

	public static final String EXTRA_IS_PLAYING = "music_isPlaying";
	public static final String EXTRA_ALL_TIME = "music_all_time";
	public static final String EXTRA_CURRENT_TIME = "music_current_time";
	public static final String EXTRA_TITLE = "music_title";

	private MediaPlayer mediaPlayer;
	private MusicBinder binder;

	private static int currentPosition = 0;
	// 顺序播放：1;
	// 单曲循环播放：0;
	// 随机播放：2.
	private static int playMode = 0;

	// 7.13>
	private List<Mp3Info> mp3Infos = null;
	SharedPreferences preferences;
	Editor editor;

	public class MusicBinder extends Binder {
		public void playOrPause() {
			PlayBackService.this.playOrPause();
		}

		public void playMusic() {
			PlayBackService.this.playMusic();
		}

		public void stop() {
			PlayBackService.this.stop();
		}

		public void seekTo(int where) {
			PlayBackService.this.seekTo(where);
		}

		public int getDuration() {
			return PlayBackService.this.getDuration();
		}

		public int getCurrentPosition() {
			return PlayBackService.this.getCurrentPosition();
		}

		public boolean isPlaying() {
			return PlayBackService.this.isPlaying();
		}

		public void playNextDependMode() {
			PlayBackService.this.playNextDependMode();
		}

		public String getTitle() {
			return PlayBackService.this.getTitle();
		}
	}

	private void init() {
		//获取歌曲
		mp3Infos = MediaUtil.getMp3Infos(PlayBackService.this);
		String url = mp3Infos.get(currentPosition).getUrl();
		Log.d("live", "init_CurrentPosition:" + currentPosition);
		Log.d("live", mp3Infos.get(0).toString());
		mediaPlayer = new MediaPlayer();
		try {
			mediaPlayer.setDataSource(url);
			//<7.14
			mediaPlayer.prepare();
			//mediaPlayer.start();
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
					mp.release();
				}
			});
			//7.14>
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		preferences = getSharedPreferences("PlayParameter", MODE_PRIVATE);
		editor = preferences.edit();
		editor.putInt("position", currentPosition);
		editor.commit();
		editor.putInt("playMode", playMode);
		editor.commit();
		updateAppWidget();
		Log.d(TAG, "=====PlayBackService  init mediaPlayer=====");
	}

	public void playNextDependMode() {
		currentPosition = preferences.getInt("position", currentPosition);
		playMode = preferences.getInt("playMode", playMode);

		switch (playMode) {
		case 0: {
			// //顺序循环播放
			currentPosition++;
			if (currentPosition == mp3Infos.size()) {
				currentPosition = 0;
				editor.putInt("position", currentPosition);
				editor.commit();
				playMusic();
			} else {
				editor.putInt("position", currentPosition);
				editor.commit();
				playMusic();
			}
			Log.d("cbl", "列表循环播放");
			break;
		}
		case 1: {
			//随机播放模式
			Random rd = new Random();
			currentPosition = rd.nextInt(mp3Infos.size());
			Log.d("cbl", "随机播放————>position："
					+ currentPosition);
			editor.putInt("position", currentPosition);
			editor.commit();
			playMusic();
			Log.d("cbl", "随机播放");
			break;
		}
		case 2: {
			//单曲循环播放
			playMusic();
			Log.d("live", "单曲循环播放");
			break;
		}
		default:
			break;
		}
	}

	public void playMusic() {
		// reset mediaPlayer
		currentPosition = preferences.getInt("position", currentPosition);
		String url = mp3Infos.get(currentPosition).getUrl();
		if (mediaPlayer != null) {
			mediaPlayer.reset();
		} else {
			mediaPlayer = new MediaPlayer();
		}

		Log.d("cbl", "playMusic_currentPosition:" + currentPosition
				+ " playMode:" + playMode);
		try {
			mediaPlayer.setDataSource(url);
			mediaPlayer.prepare();
			start();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void start() {
		// TODO Auto-generated method stub
		mediaPlayer.start();
		if (!handler.hasMessages(0)) {
			handler.sendEmptyMessage(0);
		}
		Log.d("cbl", "service.start");
		Log.d("cbl", "service.start_position:" + currentPosition);
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				playNextDependMode();
			}
		});
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "=====PlayBackService 创建service=====");
		init();
	}

	@Override
	public IBinder onBind(Intent intent) {
		if (binder == null) {
			binder = new MusicBinder();
		}
		return binder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		return true;
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "=====PlayBackService  销毁service=====");
		stop();
		super.onDestroy();
	}

	private void playOrPause() {
		if (mediaPlayer == null) {
			init();
		}
		if (!mediaPlayer.isPlaying()) {
			start();
			Log.d(TAG, "=====PlayBackService 播放音乐=====");
		} else {
			mediaPlayer.pause();
			Log.d(TAG, "=====PlayBackService 暂停音乐=====");
		}
		updateAppWidget();
		handler.sendEmptyMessage(0);
	}

	private void stop() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
			Log.d(TAG, "=====停止音乐=====");
			mediaPlayer = null;
			updateAppWidget();
			handler.removeMessages(0);
		}
	}

	private void updateAppWidget() {
		Log.d(TAG, "=====更新widget=====");
		Intent intent = new Intent("com.niit.action.UPDATE_WIDGET");
		intent.putExtra(EXTRA_ALL_TIME, getDuration());
		Log.d(TAG, "getDuration = "+getDuration());
		intent.putExtra(EXTRA_CURRENT_TIME, getCurrentPosition());
		String title = "乐知";
		if (mp3Infos != null) {
			title = mp3Infos.get(preferences.getInt("position", 0)).getTitle();
		}
		intent.putExtra(EXTRA_TITLE, title);
		intent.putExtra(EXTRA_IS_PLAYING, isPlaying());
		intent.putExtra(EXTRA_TITLE, getTitle());
		sendBroadcast(intent);
	}

	public String getTitle() {
		currentPosition = preferences.getInt("position", currentPosition);
		// Log.d("cbl", "getTitle().." + "title:" +
		// mp3Infos.get(currentPosition).getTitle());
		return mp3Infos.get(currentPosition).getTitle();
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			updateAppWidget();
			if (!handler.hasMessages(0)) {
				handler.sendEmptyMessageDelayed(0, 200);
			}
		};
	};

	private void seekTo(int where) {
		if (mediaPlayer != null) {
			mediaPlayer.seekTo(where);
			Log.d(TAG, "=====音乐跳转到：" + where + "=====");
			if (!mediaPlayer.isPlaying()) {
				start();
			}
		}
	}

	private int getDuration() {
		int result = 0;
		if (mediaPlayer != null) {
			result = mediaPlayer.getDuration();
		}
		return result;
	}

	private int getCurrentPosition() {
		int result = 0;
		if (mediaPlayer != null) {
			result = mediaPlayer.getCurrentPosition();
		}
		return result;
	}

	private boolean isPlaying() {
		boolean result = false;
		if (mediaPlayer != null) {
			result = mediaPlayer.isPlaying();
		}
		return result;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		int cmd = intent.getIntExtra(EXTRA_CMD, -1);
		switch (cmd) {
		case CMD_PLAY_OR_PAUSE:
			playOrPause();
			break;
		case CMD_NEXT_PLAY:
			Log.d("test", "desktop next");
			playNextDependMode();

		default:
			break;
		}
		return super.onStartCommand(intent, flags, startId);
	}
}
