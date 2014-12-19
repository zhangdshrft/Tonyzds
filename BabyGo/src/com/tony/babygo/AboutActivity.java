package com.tony.babygo;

import com.umeng.fb.FeedbackAgent;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AboutActivity extends BaseActivity {
	public static final String TAG = "AboutActivity";
	
	private Button feedBackBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		feedBackBtn = (Button) findViewById(R.id.feedback_btn);
		feedBackBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FeedbackAgent agent = new FeedbackAgent(AboutActivity.this);
				agent.startFeedbackActivity();
			}
		});
	}
}
