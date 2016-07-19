package com.yuezhi.side;

import com.yuezhi.activity.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class Setting_Activity extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_layout);
		//定义返回按钮
		ImageButton settingButton= (ImageButton) findViewById(R.id.setting_backBtn);
		settingButton.setOnClickListener(new OnClickListener() { 			 
			public void onClick(View v) { 
			              finish();
			            } 
			    });
	}

}
