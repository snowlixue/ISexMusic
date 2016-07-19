package com.yuezhi.side;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class Alarm_Receiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent tempIntent=new Intent(context,Sleep_Activity.class);//创建Intent对象
		Bundle myBundle=new Bundle();
		myBundle.putString("msg", "msg");
		tempIntent.putExtras(myBundle);
		tempIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//设置新的task
		context.startActivity(tempIntent);//启动Actvity
	}

}
