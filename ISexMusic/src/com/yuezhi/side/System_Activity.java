package com.yuezhi.side;

import com.yuezhi.activity.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class System_Activity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.system_version_layout);
		//定义返回按钮
		ImageButton systemButton= (ImageButton) findViewById(R.id.system_backBtn);
		systemButton.setOnClickListener(new OnClickListener() { 			 
			public void onClick(View v) { 
			              finish();
			            } 
			    });
	}
	
}
