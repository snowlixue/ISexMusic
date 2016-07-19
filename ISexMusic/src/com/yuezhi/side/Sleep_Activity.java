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
    EditText et;//�¼��༭��
    Button button;//���ð�ť
    String msg;//�¼���Ϣ
    Dialog dialog;//�Ի���
    private final int DIALOG=0; 
    TextView tv;//��¼�¼�
    StringBuilder sb;
    int count=1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sleep_layout);
        
		//���巵�ذ�ť
		ImageButton sleepButton= (ImageButton) findViewById(R.id.sleep_backBtn);
		sleepButton.setOnClickListener(new OnClickListener() { 			 
			public void onClick(View v) { 
			              finish();
			            } 
			    });
        
        et=(EditText)this.findViewById(R.id.EditText01);
        button=(Button)this.findViewById(R.id.Button01);
        tv=(TextView)this.findViewById(R.id.TextView01);
        Bundle bundle=this.getIntent().getExtras();//ȡ�ö��ŷ���bundle
        sb=new StringBuilder();
        if(bundle!=null)
        {
        	showDialog(DIALOG);//��ʾ�Ի���
        	
        }
    	final Calendar c=Calendar.getInstance();
        button.setOnClickListener//���ð�ť������
        (
        	new OnClickListener()
        	{
				public void onClick(View v) {
					msg=et.getText().toString().trim();//��ȡ�¼���Ϣ
					sb.append(count++);
					sb.append(".�����¼�Ϊ��");
					if(msg.length() == 0){sb.append("��");}
					else{sb.append(msg);}
					sb.append("\n");
					
					tv.setText(sb.toString().trim());
					c.setTimeInMillis(System.currentTimeMillis());//����ǰ�¼�����ΪĬ��ʱ��
					int hour=c.get(Calendar.HOUR_OF_DAY);//Сʱ
					int minute=c.get(Calendar.MINUTE);//����
					new TimePickerDialog(
							Sleep_Activity.this,
							new TimePickerDialog.OnTimeSetListener() {
								public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
									// TODO Auto-generated method stub
									c.setTimeInMillis(System.currentTimeMillis());//���õ�ǰʱ��
									c.set(Calendar.HOUR_OF_DAY, hourOfDay);//����Сʱ
									c.set(Calendar.MINUTE, minute);//���÷���
									c.set(Calendar.SECOND, 0);//������
									c.set(Calendar.MILLISECOND, 0);//���ú���
									Intent intent=new Intent(Sleep_Activity.this,Alarm_Receiver.class);//����AlarmReceiver��
									PendingIntent pi=PendingIntent.getBroadcast(//����PendingIntent����
											Sleep_Activity.this, 0, intent, 0);
									AlarmManager alarm=(AlarmManager)Sleep_Activity.this.getSystemService(ALARM_SERVICE);
									alarm.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);//������������һ��
//    									alarm.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 120000, pi);//ÿ����������һ��
									String tempHour=(hourOfDay+"").length()>1?hourOfDay+"":"0"+hourOfDay;
									String tempMinute=(minute+"").length()>1?minute+"":"0"+minute;
									Toast.makeText(
											Sleep_Activity.this, 
											"���õ�ʱ��Ϊ��"+tempHour+":"+tempMinute,
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
    	case DIALOG://��ʼ��
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
    	case DIALOG://�Ի���
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
	  	  dialog.setCancelable(true);//������dialog������Ե�����ؼ�
	  	 break;
    	}
    }
}