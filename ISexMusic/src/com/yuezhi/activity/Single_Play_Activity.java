package com.yuezhi.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.yuezhi.adapter.MusicListAdapter;
import com.yuezhi.domain.Mp3Info;
import com.yuezhi.service.PlayBackService;
import com.yuezhi.service.PlayBackService.MusicBinder;
import com.yuezhi.utils.CustomDialog;
import com.yuezhi.utils.MediaUtil;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class Single_Play_Activity extends Activity implements OnClickListener,
		OnSeekBarChangeListener {
	// <7.8
	private static final String TAG = "live";
	// ���غ������İ�ť���� single_music_playing
	private Button goBackButton;
	private ImageButton voiceButton;
	// ����������岼��
	RelativeLayout playerVoiceRelativeLayout;
	// ���������ʾ�����ض���
	private Animation showVoicePanelAnimation;
	private Animation hiddenVoicePanelAnimation;
	// ��Ƶ�������ã��ṩ����Ƶ�Ŀ���
	private AudioManager am;
	// ��ǰ����
	int currentVolume;
	// �������
	int maxVolume;
	// ����������С
	SeekBar sb_player_voice;
	// ����ʱ��
	private TextView currentTimeProcess;
	private TextView totalTimeProcess;
	// 7.8>
	// <7.10
	// ��������
	SeekBar playingBar;
	// ��һ�װ�ť
	ImageButton previousSongImageButton;
	// ���Ż���ͣ�л���ť
	ImageButton playOrPauseImageButton;
	// ��һ�װ�ť
	ImageButton nextSongImageButton;

	// 7.10>
	// <7.11
	private static int PlayMode = 1;
	// ���ŷ�ʽ�л���ť
	ImageButton playModeImageButton;
	// ���������б�
	ImageButton songListImageButton;
	// MusicPlaybackLocalBinder
	// 7.11>
	// <7.12
	static int position = 0;

	private List<Mp3Info> mp3Infos = null;

	// ��������
	TextView music_title;
	// indexҳ�� ���� ��������
	TextView music_name_index;
	// index���桪������
	TextView music_singer_index;
	// �Զ����б�������
	MusicListAdapter listAdapter;

	MusicBinder musicService;
	// 7.12>
	// <7.13
	SharedPreferences preferences;
	Editor editor;
	// 7.13>
	//<7.14
	private ListView music_lrc_list;
	//7.14>
	// <7.10
	private ServiceConnection conn = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			if (service instanceof MusicBinder) {
				musicService = (MusicBinder) service;
				Log.d("live", "sigh:3");
				initService();
			}
		}
	};

	// 7.10>
	//<7.13
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			new CustomDialog.Builder(Single_Play_Activity.this)
					.setTitle(R.string.info)
					.setMessage(R.string.dialog_messenge)
					.setPositiveButton(R.string.index_confrim,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									exit();
								}
							}).setNeutralButton(R.string.index_cancel, null)
					.show();
			return false;
		}
		return false;
	};
	
	// �˳�����
		private void exit() {
			Intent eIntent = new Intent(Single_Play_Activity.this, PlayBackService.class);
			stopService(eIntent);
			finish();
		}
	//7.13>
	// <7.10
	private void initService() {
		int totalTime = musicService.getDuration();
		Log.d(TAG, "totalTime = "+totalTime);
		SimpleDateFormat format = new SimpleDateFormat("mm:ss");
		String allTime = format.format(new Date(musicService.getDuration()));
		String currTime = format.format(new Date(musicService
				.getCurrentPosition()));
		Log.d(TAG, "==initService== allTime = "+allTime);
		currentTimeProcess.setText(currTime);
		totalTimeProcess.setText(allTime);
		playingBar.setMax(totalTime);
		playingBar.setProgress(musicService.getCurrentPosition());
		music_title.setText(musicService.getTitle());
		Log.d("live", "==Single_Play_Activity   initService===");

		if (musicService.isPlaying()) {
			if (!handler.hasMessages(0)) {
				handler.sendEmptyMessage(0);
			}
		}
		updateButton();
	}

	// 7.10>
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.single_music_playing);
		// <7.8
		init();
		initSetOnClickListener();
		// �������������ʾ�����صĶ���
		showVoicePanelAnimation = AnimationUtils.loadAnimation(
				Single_Play_Activity.this, R.anim.push_up_in);
		hiddenVoicePanelAnimation = AnimationUtils.loadAnimation(
				Single_Play_Activity.this, R.anim.push_up_out);
		// ���ϵͳ�����ʾ�����صĶ���
		am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		currentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
		maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		sb_player_voice.setProgress(currentVolume);
		// ��ʼ������
		initView();
		am.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
		// 7.8>
		// <7.12
		mp3Infos = MediaUtil.getMp3Infos(Single_Play_Activity.this);
		listAdapter = new MusicListAdapter(this, mp3Infos);

		Intent intent_from_index = getIntent();
		position = intent_from_index.getIntExtra("position", position);
		// 7.12>
		// <7.10
		Intent serviceIntent = new Intent(this, PlayBackService.class);
		bindService(serviceIntent, conn, BIND_AUTO_CREATE);
		// startService(serviceIntent);
		Log.d("live", "sigh:2" + " position:" + position);
		// 7.10>
		//<7.14
		String[] strs=new String[]{
			"As Long As You Love Me",
			"Athough loneliness has always been a friend of mine",
			"I'm leaving my life in your hands",
			"People say I'm crazy and I am blind",
			"Risking it all in a glance",
			"How you got me blind is still a mystery",
			"I can't get you out of my head",
			"Don't care what is written in your history",
			"As long as you're here with me",
			"I don't care who you are",
			"Where you're from",
			"What you did",
			"As long as you love me",
			"Who you are",
			"Where you're from",
			"Don't care what you did",
			"As long as you love me",
			"Every little thing that you've said and done",
			"Feels like it's deep within me",
			"Doesn't really matter if you're on the run",
			"It seems like we're meant to be",
			"I don't care who you are",
			"Where you're from",
			"What you did",
			"As long as you love me",
			"Who you are",
			"Where you're from",
			"Don't care what you did",
			"As long as you love me",
			"As long as you love me",
			"As long as you love me",
			"I've tried to hide it so that no one knows",
			"But I guess it shows",
			"When you look in to my eyes",
			"What you did and where you're coming from",
			"I don't care As long as you love me,baby",
			"I don't care who you are",
			"Where you're from",
			"What you did",
			"As long as you love me",
			"Who you are",
			"Where you're from",
			"Don't care what you did",
			"As long as you love me",
			"Who you are",
			"Where you're from",
			"What you did",
			"As long as you love me",
			"Who you are",
			"Where you're from",
			"As long as you love me",
			"Who you are",
			"As long as you love me",
			"Who you are",
			"I don't care",
			"As long as you love me"
		};
		preferences = getSharedPreferences("PlayParameter", 0);
		position = preferences.getInt("position", 0);
		if(position==0){
			music_lrc_list.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,
					strs));
		}
		//7.14>
	}

	// <7.12
	public void playMusic() {
		position = preferences.getInt("position", position);
		musicService.playMusic();

		// �ı�������ơ�����
		String title = mp3Infos.get(position).getTitle();
		if (title.contains("?")) {
			title = mp3Infos.get(position).getDisplayName();
		}
		music_title.setText(title);
		Log.d("live", " toString" + mp3Infos.get(position).toString());
		/*
		 * music_name_index.setText(mp3Infos.get(listPosition).getDisplayName());
		 * music_singer_index.setText(mp3Infos.get(listPosition).getArtist());
		 */
	}

	// 7.12>
	// <7.8
	private void initView() {
		sb_player_voice.setMax(maxVolume);
	}

	// 7.8>

	private void initTitle() {
		position = preferences.getInt("position", position);
		String title = mp3Infos.get(position).getTitle();
		Log.d("live", "initTitle()...." + " title:" + title);
		Log.d("live", "position:" + position + " title:" + title);
		music_title.setText(title);
	}

	// ��ʼ��
	private void init() {
		// <7.8
		// ���غ����� single_music_playing
		goBackButton = (Button) findViewById(R.id.go_back_to_index_btn);
		voiceButton = (ImageButton) findViewById(R.id.ibtn_player_voice);

		// ����ʱ��
		currentTimeProcess = (TextView) findViewById(R.id.current_process);
		totalTimeProcess = (TextView) findViewById(R.id.total_process);

		// ����������岼��
		playerVoiceRelativeLayout = (RelativeLayout) findViewById(R.id.player_voice);
		// ����������
		sb_player_voice = (SeekBar) findViewById(R.id.sb_player_voice);
		// 7.8>
		// <7.10
		// ���Ž�����
		playingBar = (SeekBar) findViewById(R.id.audio_track);
		// ��һ��
		previousSongImageButton = (ImageButton) findViewById(R.id.previous_music);
		// ���Ż���ͣ
		playOrPauseImageButton = (ImageButton) findViewById(R.id.play_music);
		// ��һ��
		nextSongImageButton = (ImageButton) findViewById(R.id.next_music);
		// 7.10>
		// <7.11
		// �����б�
		songListImageButton = (ImageButton) findViewById(R.id.play_queue);
		// ���ŷ�ʽ�л�
		playModeImageButton = (ImageButton) findViewById(R.id.ic_play_mode);
		// 7.11>
		// <7.12
		// ��������
		music_title = (TextView) findViewById(R.id.music_title);
		// index��������
		music_name_index = (TextView) findViewById(R.id.music_name);
		// index�������
		music_singer_index = (TextView) findViewById(R.id.music_singer);
		// 7.12>

		// <7.13
		preferences = getSharedPreferences("PlayParameter", MODE_PRIVATE);
		editor = preferences.edit();
		//7.13>
		//<7.14
		music_lrc_list=(ListView) findViewById(R.id.lrc_list);
		//7.14>
	}

	// ������
	private void initSetOnClickListener() {
		// <7.8
		// ���غ����� single_music_playing
		goBackButton.setOnClickListener(Single_Play_Activity.this);
		voiceButton.setOnClickListener(Single_Play_Activity.this);
		// ����ʱ��
		currentTimeProcess.setOnClickListener(Single_Play_Activity.this);
		totalTimeProcess.setOnClickListener(Single_Play_Activity.this);
		// ����������
		sb_player_voice.setOnSeekBarChangeListener(new SeekBarChangeListener());
		// 7.8>
		// <7.10
		// ���Ž������ļ�����
		playingBar.setOnSeekBarChangeListener(Single_Play_Activity.this);
		// ������ͣ�ļ�����
		playOrPauseImageButton.setOnClickListener(Single_Play_Activity.this);
		// 7.10>
		// <7.11
		songListImageButton.setOnClickListener(Single_Play_Activity.this);
		playModeImageButton.setOnClickListener(Single_Play_Activity.this);
		// 7.11>
		// <7.12
		nextSongImageButton.setOnClickListener(this);
		previousSongImageButton.setOnClickListener(this);
		// 7.12>
	}

	// <7.8
	/*
	 * ʵ�ּ�������Seekbar����
	 */
	private class SeekBarChangeListener implements OnSeekBarChangeListener {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			switch (seekBar.getId()) {
			//����
			case R.id.sb_player_voice: {
				am.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
				Log.d(TAG, "am---->" + progress);
				break;
			}
			default:
				break;
			}
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
		}
	}

	// 7.8>
	// <7.8
	/*
	 * �ص��������ƺ���
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int action = event.getAction();
		int keyCode = event.getKeyCode();
		switch (keyCode) {
		// ��������
		case KeyEvent.KEYCODE_VOLUME_UP:
			if (currentVolume < maxVolume) {
				currentVolume = currentVolume + 1;
				sb_player_voice.setProgress(currentVolume);
				am.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);

			} else {
				am.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
			}
			return false;
			// �����Ӽ�
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			if (action == KeyEvent.ACTION_UP) {
				if (currentVolume > 0) {
					currentVolume = currentVolume - 1;
					sb_player_voice.setProgress(currentVolume);
					am.setStreamVolume(AudioManager.STREAM_MUSIC,
							currentVolume, 0);
				} else {
					am.setStreamVolume(AudioManager.STREAM_MUSIC,
							currentVolume, 0);
				}
			}
			return false;
		default:
			return super.dispatchKeyEvent(event);
		}
	}

	// 7.8>
	// ����
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// <7.8
		//������ҳ
		case R.id.go_back_to_index_btn: {
			Intent intent = new Intent(Single_Play_Activity.this,
					Index_Activity.class);
			editor.putInt("position", position);
			editor.commit();
			startActivity(intent);
			finish();
			break;
		}
		// 7.8>
		// <7.10
		//����
		case R.id.play_music: {
			if (musicService != null) {
				playOrPauseMusic();
			}
			break;
		}
		// 7.10>
		// <7.8
		case R.id.ibtn_player_voice: {
			// ��������
			voicePanelAnimation();
			break;
		}
		// 7.8>
		// <7.11
		// ��ǰ���Ŷ������
		case R.id.play_queue: {
			startActivity(new Intent(Single_Play_Activity.this,
					Single_Play_Queue_Activity.class));
			break;
		}
		// ����ģʽ
		case R.id.ic_play_mode: {
			switch ((PlayMode % 3)) {
			case 0:{
				// �б�ѭ������
				playModeImageButton
						.setImageResource(R.drawable.button_playmode_shuffle);
				PlayMode++;
				editor.putInt("playMode", PlayMode%3);
				editor.commit();
				Log.d("cbl", "���ѭ�����ţ�" + PlayMode % 3 );
				break;
			}
			case 1:{
				// 
				playModeImageButton
						.setImageResource(R.drawable.button_playmode_repeat_single);
				PlayMode++;
				editor.putInt("playMode", PlayMode%3);
				editor.commit();
				Log.d("cbl", "�������ţ�" + PlayMode % 3 );
				break;
			}
			case 2:{
				// ���ѭ������
				playModeImageButton
						.setImageResource(R.drawable.button_playmode_sequential);
				PlayMode++;
				editor.putInt("playMode", PlayMode%3);
				editor.commit();
				Log.d("cbl", "�б�ѭ�����ţ�" + PlayMode % 3 );
				break;
			}
			default:
				break;
			}
			break;
		}
		// 7.11>
		case R.id.current_process: {

			break;
		}
		case R.id.total_process: {

			break;
		}
		// <7.12
		case R.id.next_music: {
			nextSong();
			break;
		}
		case R.id.previous_music: {
			previousSong();
			break;
		}
		// 7.12>
		default:
			break;
		}
	}

	// <7.12
	public void previousSong() {
		position--;
		if (position < 0) {
			position = mp3Infos.size() - 1;
			editor.putInt("position", position);
			editor.commit();
			playMusic();
		} else {
			editor.putInt("position", position);
			editor.commit();
			playMusic();
		}
	}

	public void nextSong() {
		position++;
		if (position <= (mp3Infos.size() - 1)) {
			editor.putInt("position", position);
			editor.commit();
			playMusic();
		} else {
			position = 0;
			editor.putInt("position", position);
			editor.commit();
			playMusic();
		}
		music_title.setText(mp3Infos.get(position).getTitle());
	}

	// 7.12>
	// <7.8
	private void voicePanelAnimation() {
		if (playerVoiceRelativeLayout.getVisibility() == View.GONE) {
			playerVoiceRelativeLayout.startAnimation(showVoicePanelAnimation);
			playerVoiceRelativeLayout.setVisibility(View.VISIBLE);
		} else {
			playerVoiceRelativeLayout.startAnimation(hiddenVoicePanelAnimation);
			playerVoiceRelativeLayout.setVisibility(View.GONE);
		}
	}

	// 7.8>
	// <7.10
	// �����ֹͣ���ֵĽ��У�����û�������ť������ʵ������һ�����õĺ���
	private void stopMusic() {
		musicService.stop();
		if (handler.hasMessages(0)) {
			handler.removeMessages(0);
		}
		init();
	}

	private void updateButton() {
		if (musicService != null && musicService.isPlaying()) {
			playOrPauseImageButton.setImageResource(R.drawable.button_pause);
		} else {
			playOrPauseImageButton.setImageResource(R.drawable.button_play);
		}
	}

	private void playOrPauseMusic() {
		musicService.playOrPause();
		if (!handler.hasMessages(0)) {
			handler.sendEmptyMessage(0);
		}
		updateButton();
	}

	// 7.10>
	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// <7.10
		unbindService(conn);
		if (handler.hasMessages(0)) {
			handler.removeMessages(0);
		}
		// 7.10>
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	// <7.10
	/*
	 * 2016.7.10 handler
	 */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			playingBar.setProgress(musicService.getCurrentPosition());
			SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
			String allTime = dateFormat.format(new Date(musicService
					.getDuration()));
			String currTime = dateFormat.format(new Date(musicService
					.getCurrentPosition()));
			currentTimeProcess.setText(currTime);
			totalTimeProcess.setText(allTime);
			Log.d("live","alltime = "+allTime);
			if (!handler.hasMessages(0)) {
				handler.sendEmptyMessageDelayed(0, 200);
			}
		}
	};

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if (fromUser) {
			musicService.seekTo(progress);
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar arg0) {
	}
	// 7.10>
}
