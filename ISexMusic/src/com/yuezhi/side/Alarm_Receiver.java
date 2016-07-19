package com.yuezhi.side;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class Alarm_Receiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent tempIntent=new Intent(context,Sleep_Activity.class);//����Intent����
		Bundle myBundle=new Bundle();
		myBundle.putString("msg", "msg");
		tempIntent.putExtras(myBundle);
		tempIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//�����µ�task
		context.startActivity(tempIntent);//����Actvity
	}

}
