package com.yuezhi.activity;

import java.util.List;

import com.yuezhi.adapter.MusicListAdapter;
import com.yuezhi.domain.Mp3Info;
import com.yuezhi.service.PlayBackService;
import com.yuezhi.service.PlayBackService.MusicBinder;
import com.yuezhi.side.Login_Activity;
import com.yuezhi.side.Setting_Activity;
import com.yuezhi.side.Sleep_Activity;
import com.yuezhi.side.System_Activity;
import com.yuezhi.utils.CustomDialog;
import com.yuezhi.utils.MediaUtil;

import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Index_Activity extends Activity implements OnClickListener {

	private static int INDEX_COUNT = 1;
	// <7.8
	// ����
	Button playingButton;
	// ��һ��
	Button nextButton;
	// �����б�
	Button playlistButton;

	ImageView iconImageView;
	// ������������ת��ť
	ImageView userLoginImageView;
	TextView settingTextView;
	TextView sleepTextView;
	TextView systemversionTextView;
	TextView exitTextView;

	// ���ø��������Ĳ˵�
	PopupMenu popupMenu;
	Menu mmenu;
	// �����
	private SlideMenu slideMenu;

	private String[] mPlaneTitles;
	private DrawerLayout mDrawerLayout;
	// 7.8>
	// <7.9
	// ��ʾ�б�λ��
	private int listPosition = 0;
	// �����б��������ִ��ڴ���ȡ����
	private List<Mp3Info> mp3Infos = null;
	// ���ֲ����б�
	private ListView musicList;
	// �Զ����б�������
	MusicListAdapter listAdapter;
	// 7.9>
	// <7.11
	// �����б�
	private Button allmusiclist;
	// 7.11>
	// <7.12
	// ���½�menu����
	PopupMenu listPopupMenu;
	Menu listMenu;
	// �����͸���
	TextView music_name;
	TextView music_singer;
	// ����
	MusicBinder musicService;
	// ����һ���ַ�������ȡ����
	String str_music_title;
	// ��������������
	SeekBar seekBar;
	// 7.12>
	// <7.13
	SharedPreferences preferences;
	Editor editor;
	// 7.13>
	// <7.12
	// ������ʾservice�Ƿ��Ѿ�����
	// connection
	private ServiceConnection connection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			if (service instanceof MusicBinder) {
				musicService = (MusicBinder) service;
				init();
				if (str_music_title != null) {
					Log.d("live", "music_title:" + str_music_title);
					music_name.setText(str_music_title);
					// music_singer.setText(str_music_singer);
				}
			}
		}
	};

	// 7.12>
	
	//<7.13h
	Handler diHandler=null;
	//7.13h>
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// <7.8
		// ��ҳ
		setContentView(R.layout.index);
		getWindow().setBackgroundDrawableResource(R.drawable.music_background);

		// ÿ������app��ʼ����ֵ��Ȼ����ݴ�ֵ�����ж��Ƿ�finish
		Log.d("live", "onCreate=" + INDEX_COUNT);
		init();
		// 7.8>
		// <7.9
		musicList = (ListView) findViewById(R.id.music_list);
		musicList.setOnItemClickListener(new MusicListItemClickListener());
		musicList
				.setOnCreateContextMenuListener(new MusicListItemContextMenuListener());
		//<7.13h
		//�¿�һ���̣߳������첽�������ݣ����ͨ��handler����
		Runnable diRunnable=new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(200);
					//��ȡ�������󼯺�
					mp3Infos = MediaUtil.getMp3Infos(Index_Activity.this);
					//������Ϣ
					diHandler.sendMessage(handler.obtainMessage(0, mp3Infos));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		try {
			new Thread(diRunnable).start();
			diHandler=new Handler()
			{
				public void handleMessage(Message msg)
				{
					if(msg.what==0){
						@SuppressWarnings("unchecked")
						List<Mp3Info> mp3Infos=(List<Mp3Info>) msg.obj;
						BinderListDate(mp3Infos);
					}
				}
			};
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				Message msg=new Message();
				if(mp3Infos!=null){
					msg.what=0;
					msg.obj=mp3Infos;
				}
				diHandler.sendMessage(msg);
			}
		}).start();*/
		
		//7.13h>
		// 7.9>
		// <7.11
		listPopupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {
				case R.id.sort_by_song: {
					break;
				}
				case R.id.sort_by_singer: {
					break;
				}
				case R.id.sort_by_time: {
					break;
				}
				case R.id.sort_by_style: {
					break;
				}
				default:
					break;
				}
				return false;
			}
		});
		// 7.11>
		// <7.12
		Intent playservice = new Intent(this, PlayBackService.class);
		bindService(playservice, connection, BIND_AUTO_CREATE);
		startService(playservice);

		Intent intent_from_play = getIntent();
		str_music_title = intent_from_play.getStringExtra("music_title");
		listPosition = intent_from_play.getIntExtra("position", 2);
		// str_music_singer = intent_from_play.getStringExtra("music_singer");
		Log.d("live", "index_oncreate:" + "music_title:" + str_music_title);
		// 7.12>
		// <7.13
		// ���嵯����popup�˵�
		popupMenu = new PopupMenu(this, findViewById(R.id.more_functions));
		mmenu = popupMenu.getMenu();
		// ͨ��XML�ļ���Ӳ˵���
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.popupmenu, mmenu);
		// ���ñ�����popupmenu�����¼�
		popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {
				case R.id.spring:
					getWindow().setBackgroundDrawableResource(R.drawable.bga);
					Log.d("live", "=====beizhixing====");
					break;
				case R.id.summer:
					getWindow().setBackgroundDrawableResource(R.drawable.bgb);
					break;
				case R.id.autumn:
					getWindow().setBackgroundDrawableResource(R.drawable.bgc);
					break;
				case R.id.winter:
					getWindow().setBackgroundDrawableResource(R.drawable.bgd);
					break;
				default:
					break;
				}
				return false;
			}
		});
		// 7.13>
	}
	// handler�첽�������
	// <7.13h
	//����һ��handler�������첽��������
	/*Handler diHandler=new Handler(){
		public void handleMessage(android.os.Message msg){
			super.handleMessage(msg);
			if(msg.what==0){
				// ��ȡ�������󼯺�
				mp3Infos = MediaUtil.getMp3Infos(Index_Activity.this);
				mp3Infos = (List<Mp3Info>) msg.obj;
				listAdapter = new MusicListAdapter(Index_Activity.this, mp3Infos);
				musicList.setAdapter(listAdapter);
			}
		}
	};*/
	//������
	public void BinderListDate(List<Mp3Info> mp3Infos){
		listAdapter = new MusicListAdapter(Index_Activity.this, mp3Infos);
		musicList.setAdapter(listAdapter);
	}
	// 7.13h>

	private void init() {
		// <7.8
		// ���š���һ�ס������б�
		playingButton = (Button) findViewById(R.id.playing);
		nextButton = (Button) findViewById(R.id.next_song);
		playlistButton = (Button) findViewById(R.id.all_music_list);

		iconImageView = (ImageView) findViewById(R.id.music_album);

		slideMenu = (SlideMenu) findViewById(R.id.slide_menu);
		ImageView menuImg = (ImageView) findViewById(R.id.menu_navigation);
		// ������
		menuImg.setOnClickListener(this);
		iconImageView.setOnClickListener(this);

		// ���õ�½��ת�ļ�����
		userLoginImageView = (ImageView) findViewById(R.id.user_login_imageview);
		userLoginImageView.setOnClickListener(this);

		// ����ϵͳ������ת�ļ�����
		settingTextView = (TextView) findViewById(R.id.setting_textview);
		settingTextView.setOnClickListener(this);
		// <7.13
		// ����˯����ת�ļ�����
		sleepTextView = (TextView) findViewById(R.id.sleep_textview);
		sleepTextView.setOnClickListener(this);
		// ���ð汾��Ϣ��ת�ļ�����
		systemversionTextView = (TextView) findViewById(R.id.system_version_textview);
		systemversionTextView.setOnClickListener(this);
		// �����˳��ļ�����
		exitTextView = (TextView) findViewById(R.id.exit_textview);
		exitTextView.setOnClickListener(this);
		// 7.13>
		// 7.8>
		// <7.11
		// �����б�
		// ����PopupMenu��Menu��
		allmusiclist = (Button) findViewById(R.id.all_music_list);
		listPopupMenu = new PopupMenu(this, allmusiclist);
		listMenu = listPopupMenu.getMenu();
		// ͨ��xml�ļ���Ӳ˵���
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.sort_music_menu, listMenu);
		// 7.11>
		// <7.12
		seekBar = (SeekBar) findViewById(R.id.audio_track);

		music_name = (TextView) findViewById(R.id.music_name);
		music_singer = (TextView) findViewById(R.id.music_singer);
		// 7.12>
		// <7.13
		preferences = getSharedPreferences("PlayParameter", MODE_PRIVATE);
		editor = preferences.edit();
		// 7.13>
	}

	public void buttonClick(View v) {
		switch (v.getId()) {
		case R.id.playing: {
			if (musicService != null) {
				musicService.playOrPause();
				music_name.setText(mp3Infos.get(listPosition).getTitle());
				updateButton();
			}
			break;
		}
		case R.id.next_song: {
			nextSong();
			break;
		}
		default:
			break;
		}
	}

	// <7.12
	private void nextSong() {
		listPosition++;
		if (listPosition <= (mp3Infos.size() - 1)) {
			editor.putInt("position", listPosition);
			editor.commit();
			playMusic();
		} else {
			listPosition = 0;
			editor.putInt("position", listPosition);
			editor.commit();
			playMusic();
		}
		str_music_title = mp3Infos.get(listPosition).getTitle();
		music_name.setText(str_music_title);
	}

	/*
	 * �˷���ͨ�������б���λ������ȡmp3Info����
	 */
	public void playMusic() {
		musicService.playMusic();
	}

	// 7.12>
	// <7.8
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	// 7.8>
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// <7.8
		case R.id.menu_navigation: {
			if (slideMenu.isMainScreenShowing()) {
				slideMenu.openMenu();
			} else {
				slideMenu.closeMenu();
			}
			break;
		}
		case R.id.user_login_imageview: {
			Intent login_intent = new Intent(this, Login_Activity.class);
			startActivity(login_intent);
			break;
		}
		case R.id.setting_textview: {
			Intent setting_intent = new Intent(this, Setting_Activity.class);
			startActivity(setting_intent);
			break;
		}
		// <7.13
		case R.id.sleep_textview: {
			Intent sleep_intent = new Intent(this, Sleep_Activity.class);
			startActivity(sleep_intent);
			break;
		}
		case R.id.system_version_textview: {
			Intent systemversion_intent = new Intent(this,
					System_Activity.class);
			startActivity(systemversion_intent);
			break;
		}
		case R.id.exit_textview: {
			super.onDestroy();
			android.os.Process.killProcess(android.os.Process.myPid());
			Log.d("lixue", "onDestory��ִ��");
			break;
		}
		// 7.13>
		case R.id.music_album: {
			Intent intent = new Intent(Index_Activity.this,
					Single_Play_Activity.class);
			intent.putExtra("position", listPosition);
			startActivity(intent);
			finish();
			/*
			 * if (INDEX_COUNT > 0) { finish(); INDEX_COUNT--; } else {
			 * INDEX_COUNT++; }
			 */
			Log.d("live", "INDEX_COUNT = " + INDEX_COUNT);
			break;
		}
		// 7.8>
		}
	}

	/*
	 * 2016.7.9 MusicListItemClickListener �б���������
	 */
	private class MusicListItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// ��ȡ�б�����λ��
			listPosition = position;
			// ��������
			Log.d("live", "���λ�ã�" + position + ",���ƣ�"
					+ mp3Infos.get(position).getDisplayName() + ",URL:"
					+ mp3Infos.get(position).getUrl());

			music_name.setText(mp3Infos.get(position).getDisplayName());
			music_singer.setText(mp3Infos.get(position).getArtist());

			Intent intent_play = new Intent(Index_Activity.this,
					Single_Play_Activity.class);
			listPosition = position;
			editor.putInt("position", listPosition);
			editor.commit();
			playMusic();
			startActivity(intent_play);
			Log.d("live", "position:" + position);
			/*
			 * startActivityForResult(intent_play, 1); playMusic(position);
			 * 
			 * playOrPauseMusic();
			 */
		}
	}

	// <7.12
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void playOrPauseMusic() {
		musicService.playOrPause();
		if (!handler.hasMessages(0)) {
			handler.sendEmptyMessage(0);
		}
	}

	private void updateButton() {
		if (musicService != null && musicService.isPlaying()) {
			playingButton.setBackgroundResource(R.drawable.button_pause);
		} else {
			playingButton.setBackgroundResource(R.drawable.button_play);
		}
		/*
		 * ImageButton setImageResource Button setBackgroundResource�л�ͼƬ���Ƿǳ���
		 */
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// ����������ı����ֽ�����

			if (!handler.hasMessages(0)) {
				handler.sendEmptyMessageDelayed(0, 200);
			}
		};
	};

	// 7.12>
	/*
	 * 2016.7.9 MusicListItemContextMenuListener �����Ĳ˵���ʾ������
	 */
	public class MusicListItemContextMenuListener implements
			OnCreateContextMenuListener {

		@Override
		public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenuInfo menuInfo) {
		}
	}

	// <7.12
	// �˵�
	public void popupmenu_popupmenu(View v) {
		listPopupMenu.show();
	}

	// <7.13
	// ��ʾpopup�ķ���
	public void popupmenu(View v) {
		popupMenu.show();
	}

	// 7.13>

	// �����ؼ������Ի���ȷ���˳�
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			new CustomDialog.Builder(Index_Activity.this)
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
	}

	// �˳�����
	private void exit() {
		Intent eIntent = new Intent(Index_Activity.this, PlayBackService.class);
		stopService(eIntent);
		finish();
	}
	// 7.12>
}
