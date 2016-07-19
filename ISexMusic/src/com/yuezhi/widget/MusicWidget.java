package com.yuezhi.widget;

import com.yuezhi.activity.Index_Activity;
import com.yuezhi.activity.R;
import com.yuezhi.service.PlayBackService;

import android.R.plurals;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.RemoteViews;
/**
 * 
 * @author Nao
 *Widegt部分，上部分是向service发出信息请求响应
 */
public class MusicWidget extends AppWidgetProvider {
	public static final String UPDATE_ACTION = "com.niit.action.UPDATE_WIDGET";
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub
		//super.onUpdate(context, appWidgetManager, appWidgetIds);
		RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.widget_layout);
		PendingIntent textIntent = PendingIntent.getActivity(context, 0, new Intent(context,Index_Activity.class), 0);
		views.setOnClickPendingIntent(R.id.widget_image_layout, textIntent);
		//点击启动activity
		Intent playIntent = new Intent(context,PlayBackService.class);
		playIntent.putExtra(PlayBackService.EXTRA_CMD, PlayBackService.CMD_PLAY_OR_PAUSE);
		PendingIntent playOrPauseIntent = PendingIntent.getService(context, 1, playIntent, 0);
		views.setOnClickPendingIntent(R.id.widget_play_btn, playOrPauseIntent);
		Intent nextIntent = new Intent(context,PlayBackService.class);
		nextIntent.putExtra(PlayBackService.EXTRA_CMD, PlayBackService.CMD_NEXT_PLAY);
		PendingIntent nextPlayIntent = PendingIntent.getService(context, 2, nextIntent, 0);
		views.setOnClickPendingIntent(R.id.widget_next_btn, nextPlayIntent);
		if (appWidgetIds != null) {
			appWidgetManager.updateAppWidget(appWidgetIds, views);
		} else {
			appWidgetManager.updateAppWidget(
					new ComponentName(context, this.getClass()), views);
		}
	}
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onReceive(context, intent);
		if (UPDATE_ACTION.equals(intent.getAction())) {
			RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.widget_layout);
			//为等会的改歌曲名留白
			String  title = intent.getStringExtra(PlayBackService.EXTRA_TITLE);
			views.setTextViewText(R.id.widget_text, title);
			boolean isPlaying = intent.getBooleanExtra(PlayBackService.EXTRA_IS_PLAYING, false);
			if (isPlaying) {
				views.setImageViewResource(R.id.widget_play_btn, R.drawable.kg_ic_playing_bar_pause_default);
			}else{
				views.setImageViewResource(R.id.widget_play_btn, R.drawable.kg_ic_playing_bar_play_default);
			}
			AppWidgetManager appWidgetManager = AppWidgetManager
					.getInstance(context);
			appWidgetManager.updateAppWidget(
					new ComponentName(context, this.getClass()), views);
		}
		
	}
	
}
