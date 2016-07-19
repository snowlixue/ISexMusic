package com.yuezhi.side;

import com.yuezhi.activity.R;

import android.R.layout;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Login_Activity extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
		//定义返回按钮
		Button loginButton= (Button) findViewById(R.id.login_exit_btn);
		loginButton.setOnClickListener(new OnClickListener() { 			 
			public void onClick(View v) { 
			              finish();
			            } 
			    });
	}

}
