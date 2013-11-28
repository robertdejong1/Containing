package containing.app;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private Thread thread;
	private static boolean isConnected = false;
	public static TextView textView;
	public static MainActivity activity;
	public static Button button;

	public static ProgressDialog dialog;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        
        button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
            	button.setEnabled(false);
                
            	dialog = ProgressDialog.show(MainActivity.this, "Connecting", "Please wait");
            	
            	EditText text = (EditText) findViewById(R.id.editText1);
            	String connectString = text.getText().toString();

            	String[] split = connectString.split(":", 2);
                String ip = split[0];
                int port = Integer.parseInt(split[1]);
                connect(ip, port);
            }
        });
        
        textView = (TextView) findViewById(R.id.textView1);
        activity = this;
    }
    
    private void connect(String ip, int port){
    	Runnable networkHandler = new NetworkHandler(ip, port);
        thread = new Thread(networkHandler);
        thread.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public static boolean getIsConnected(){
    	return isConnected;
    }
    public static void setIsConnected(final boolean value){
    	activity.runOnUiThread(new Runnable(){
        	@Override
        	public void run(){
        		if(dialog.isShowing()){
        			dialog.dismiss();
        		}
            	isConnected = value;
            	if(value){
            		textView.setText("");
            		button.setEnabled(false);
            		CommandHandler.addCommand("STATS");
            	}
            	else{
            		textView.setText("Not connected");
            		button.setEnabled(true);
            	}
            	           	
        	}
        });
    }
    
    public static void showDialog(final String text, final Exception e){
        showDialog(text +"\n\n" +e.getMessage());
    }
    public static void showDialog(final String text){
        activity.runOnUiThread(new Runnable(){
        	@Override
        	public void run(){
            	AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            	builder.setMessage(text).setTitle("Message");
            	AlertDialog dialog = builder.create();
            	dialog.show();
        	}
        });
    }
}
