package com.hackrgt.katanalocate;

import com.hackrgt.katanalocate.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);
        
        //Testing send_message_view
        /*Intent sendViewActivity = new Intent(this, SendMessageActivity.class);
		startActivity(sendViewActivity);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    
}
