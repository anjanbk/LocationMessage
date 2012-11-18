package com.hackrgt.katanalocate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SendMessageActivity extends Activity implements OnClickListener {

	private Button submitButton;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_message_view);
        
        
        submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(this);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	public void onClick(View view) {
		if (view.getId() == submitButton.getId()) {
			Intent mainActivity = new Intent(this, MainActivity.class);
			startActivity(mainActivity);
		}
	}
}
