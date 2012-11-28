package com.hackrgt.katanalocate;

import com.facebook.FacebookException;
import com.facebook.FacebookActivity;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.hackrgt.katanalocate.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends FacebookActivity {
    private Button sendRequestButton;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);
        this.openSession();
        
        //Testing send_message_view
        /*Intent sendViewActivity = new Intent(this, SendMessageActivity.class);
		startActivity(sendViewActivity);*/
    }
	
	public void createRequest(View v) {
		sendRequestDialog(this);
	}
    
    @Override
    protected void onSessionStateChange(SessionState state, Exception exception) {
    	
    }
    
    private void sendRequestDialog(final Activity activity) {
        Bundle params = new Bundle();
        params.putString("message", "Test request");
        WebDialog requestsDialog = (
            new WebDialog.RequestsDialogBuilder(activity,
                Session.getActiveSession(),
                params))
                .setOnCompleteListener(new OnCompleteListener() {
                    
                    public void onComplete(Bundle values,
                        FacebookException error) {
                        final String requestId = values.getString("request");
                        if (requestId != null) {
                            Toast.makeText(activity.getApplicationContext(), 
                                "Request sent",  
                                Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(activity.getApplicationContext(), 
                                "Request cancelled", 
                                Toast.LENGTH_SHORT).show();
                        }
                    }

                })
                .build();
        requestsDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    
}
