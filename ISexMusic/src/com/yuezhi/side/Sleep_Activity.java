package com.yuezhi.side;

import java.util.Calendar;

import com.yuezhi.activity.R;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class Sleep_Activity extends Activity {
    EditText et;//事件编辑框
    Button button;//设置按钮
    String msg;//事件信息
    Dialog dialog;//对话框
    private final int DIALOG=0; 
    TextView tv;//记录事件
    StringBuilder sb;
    int count=1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sleep_layout);
        
		//定义返回按钮
		ImageButton sleepButton= (ImageButton) findViewById(R.id.sleep_backBtn);
		sleepButton.setOnClickListener(new OnClickListener() { 			 
			public void onClick(View v) { 
			              finish();
			            } 
			    });
        
        et=(EditText)this.findViewById(R.id.EditText01);
        button=(Button)this.findViewById(R.id.Button01);
        tv=(TextView)this.findViewById(R.id.TextView01);
        Bundle bundle=this.getIntent().getExtras();//取得短信发来bundle
        sb=new StringBuilder();
        if(bundle!=null)
        {
        	showDialog(DIALOG);//显示对话框
        	
        }
    	final Calendar c=Calendar.getInstance();
        button.setOnClickListener//设置按钮监听器
        (
        	new OnClickListener()
        	{
				public void onClick(View v) {
					msg=et.getText().toString().trim();//获取事件信息
					sb.append(count++);
					sb.append(".备忘事件为：");
					if(msg.length() == 0){sb.append("无");}
					else{sb.append(msg);}
					sb.append("\n");
					
					tv.setText(sb.toString().trim());
					c.setTimeInMillis(System.currentTimeMillis());//将当前事件设置为默认时间
					int hour=c.get(Calendar.HOUR_OF_DAY);//小时
					int minute=c.get(Calendar.MINUTE);//分钟
					new TimePickerDialog(
							Sleep_Activity.this,
							new TimePickerDialog.OnTimeSetListener() {
								public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
									// TODO Auto-generated method stub
									c.setTimeInMillis(System.currentTimeMillis());//设置当前时间
									c.set(Calendar.HOUR_OF_DAY, hourOfDay);//设置小时
									c.set(Calendar.MINUTE, minute);//设置分钟
									c.set(Calendar.SECOND, 0);//设置秒
									c.set(Calendar.MILLISECOND, 0);//设置毫秒
									Intent intent=new Intent(Sleep_Activity.this,Alarm_Receiver.class);//运行AlarmReceiver类
									PendingIntent pi=PendingIntent.getBroadcast(//创建PendingIntent对象
											Sleep_Activity.this, 0, intent, 0);
									AlarmManager alarm=(AlarmManager)Sleep_Activity.this.getSystemService(ALARM_SERVICE);
									alarm.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);//设置闹钟提醒一次
//    									alarm.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 120000, pi);//每两分钟提醒一次
									String tempHour=(hourOfDay+"").length()>1?hourOfDay+"":"0"+hourOfDay;
									String tempMinute=(minute+"").length()>1?minute+"":"0"+minute;
									Toast.makeText(
											Sleep_Activity.this, 
											"设置的时间为："+tempHour+":"+tempMinute,
											Toast.LENGTH_SHORT).show();
								}
							},hour,minute,true).show();
				}
        		
        	}
        );
    }
    @Override
    public Dialog onCreateDialog(int id)
    {    	
    	Dialog result=null;
    	switch(id)
    	{
    	case DIALOG://初始化
	      	  AlertDialog.Builder mywktzb=new AlertDialog.Builder(this); 
	      	mywktzb.setItems(
				null, 
			    null
			   );
			  dialog=mywktzb.create();
			  result=dialog;
	    	break; 
    	}
    	return result;
    }
    @Override
    public void onPrepareDialog(int id, final Dialog dialog)
    {
    	switch(id)
    	{
    	case DIALOG://对话框
			dialog.setContentView(R.layout.sleep_dialog_layout);
	  	    Button mywktzBOk=(Button)dialog.findViewById(R.id.mywktzOk);
	  	    mywktzBOk.setOnClickListener
	  	    (
	  	    		new OnClickListener()
	  	    		{
	  	    			public void onClick(View arg0) {
	  	    				dialog.cancel();
	  	    				Sleep_Activity.this.finish();
	  	    			}
	  	    		}
	  	    );
	  	  dialog.setCancelable(true);//设置在dialog界面可以点击返回键
	  	 break;
    	}
    }
}