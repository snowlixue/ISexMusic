package com.yuezhi.activity;

import java.lang.ref.WeakReference;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;

import com.umeng.analytics.MobclickAgent;

public class BeforeIndexActivity extends FragmentActivity {

	private MyHandler myHandler = new MyHandler(this);
	private final int DELAYMILLIS = 2000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		myHandler.sendEmptyMessageDelayed(0, DELAYMILLIS);

		init();
	}

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
	public void onBackPressed() {
	}

	private void init() {
		MobclickAgent.openActivityDurationTrack(true);
		MobclickAgent.updateOnlineConfig(this);
		MobclickAgent.onError(this);
	}

	private static class MyHandler extends Handler {
		// 使用弱引用，避免Handler造成的内存泄露(Message持有Handler的引用，内部定义的Handler类持有外部类的引用)
		WeakReference<BeforeIndexActivity> mFragmentWeakReference = null;
		BeforeIndexActivity mActivity = null;

		public MyHandler(BeforeIndexActivity a) {
			mFragmentWeakReference = new WeakReference<BeforeIndexActivity>(a);
			mActivity = mFragmentWeakReference.get();
		}

		@Override
		public void handleMessage(Message msg) {
			mActivity
					.startActivity(new Intent(mActivity, Index_Activity.class));
			mActivity.finish();
		}
	}
}
