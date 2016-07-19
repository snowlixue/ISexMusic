package com.yuezhi.activity;

import java.util.HashMap;
import java.util.List;

import com.umeng.analytics.MobclickAgent;
import com.yuezhi.activity.Index_Activity.MusicListItemContextMenuListener;
import com.yuezhi.adapter.MusicListAdapter;
import com.yuezhi.domain.Mp3Info;
import com.yuezhi.utils.MediaUtil;
import com.google.analytics.tracking.android.EasyTracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/*
 * 2016.7.12  Single_Play_Activity�����Ŷ����ļ�
 * songListImageButton�������ʾ�����б�Ĵ����ļ�
 * @author  live
 */
public class Single_Play_Queue_Activity extends FragmentActivity implements
		OnItemClickListener {

	// ��հ�ť�Ͳ����б���ʾ
	private TextView clearTextView;
	private TextView playqueueTextView;

	private ListView musicListView;

	// �����б��������ִ��ڴ���ȡ����
	private List<Mp3Info> mp3Infos = null;

	// �Զ����б�������
	// MusicListAdapter listAdapter;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		Log.i("live", "Single_Play_Queue_Activity---onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.music_list_play_queue);
		init();

		musicListView = (ListView) findViewById(R.id.listview_music_play_queue);
		musicListView.setOnItemClickListener(new MusicListItemClickListener());
		musicListView
				.setOnCreateContextMenuListener(new MusicListItemContextMenuListener());
		// ��ȡ�������󼯺�
		mp3Infos = MediaUtil.getMp3Infos(Single_Play_Queue_Activity.this);
		// listAdapter = new MusicListAdapter(Single_Play_Queue_Activity.this,
		// mp3Infos);
		List<HashMap<String, String>> queues = MediaUtil.getMusicMaps(mp3Infos);
		SimpleAdapter listAdapter = new SimpleAdapter(this, queues,
				R.layout.music_list_play_queue_music_name, new String[] { "title","duration"},
				new int[] { R.id.queue_music_name,R.id.queue_music_time });
		Log.d("live", "adapter = "+listAdapter);
		musicListView.setAdapter(listAdapter);
	}

	private void init() {
		clearTextView = (TextView) findViewById(R.id.playqueue_clear);
		playqueueTextView = (TextView) findViewById(R.id.playqueue_title);
	}

	@Override
	public void onAttachedToWindow() {
		// ���ñ�Activity�ڸ����ڵ�λ��
		super.onAttachedToWindow();
		View view = getWindow().getDecorView();
		WindowManager.LayoutParams lp = (LayoutParams) view.getLayoutParams();
		lp.gravity = Gravity.RIGHT | Gravity.BOTTOM;
		lp.x = getResources().getDimensionPixelSize(
				R.dimen.playqueue_dialog_marginright);
		lp.y = getResources().getDimensionPixelSize(
				R.dimen.playqueue_dialog_marginbottom);
		lp.width = getResources().getDimensionPixelSize(
				R.dimen.playqueue_dialog_width);
		lp.height = getResources().getDimensionPixelSize(
				R.dimen.playqueue_dialog_height);
		getWindowManager().updateViewLayout(view, lp);
	}

	/*
	 * 2016.7.12 MusicListItemClickListener �б���������
	 */
	private class MusicListItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

		}
	}

	/*
	 * 2016.7.12 MusicListItemContextMenuListener �����Ĳ˵���ʾ������
	 */
	public class MusicListItemContextMenuListener implements
			OnCreateContextMenuListener {

		@Override
		public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenuInfo menuInfo) {

		}
	}

	@Override
	protected void onStart() {
		Log.i("live", "Single_Play_Queue_Activity---onStart");
		super.onStart();
		
		EasyTracker.getInstance(this).activityStart(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		
		EasyTracker.getInstance(this).activityStop(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

	}
}
