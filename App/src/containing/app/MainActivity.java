package containing.app;

import java.util.HashMap;

import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private Thread thread;
	private static boolean isConnected = false;
	private static TextView textView;
	private static MainActivity activity;
	private static Button button;
	private static GraphViewSeries stats;

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
    
    public static void showGraphView(HashMap<String, Double> data){
    	stats = new GraphViewSeries(new GraphViewData[] {
    	      new GraphViewData(1, data.get("train")),
    	      new GraphViewData(2, data.get("truck")),
    	      new GraphViewData(3, data.get("seaShip")),
    	      new GraphViewData(4, data.get("barge")),
    	      new GraphViewData(5, data.get("storage")),
    	      new GraphViewData(6, data.get("agv")),
    	      new GraphViewData(7, data.get("other"))
    	});
    	 
    	GraphView graphView = new BarGraphView(
    	      activity // context
    	      , "Amount of containers" // heading
    	);
    	graphView.addSeries(stats); // data
    	
    	graphView.getGraphViewStyle().setHorizontalLabelsColor(Color.BLACK);
    	graphView.getGraphViewStyle().setVerticalLabelsColor(Color.BLACK);
    	graphView.getGraphViewStyle().setNumHorizontalLabels(7);
    	graphView.setHorizontalLabels(new String[] {"Train" , "Truck", "Seaship", "Barge", "Storage", "AGV", "Other"});
    	graphView.getGraphViewStyle().setVerticalLabelsWidth(50);

    	final GraphView view = graphView;
    	
    	activity.runOnUiThread(new Runnable(){
        	@Override
        	public void run(){
        		LinearLayout layout = (LinearLayout) activity.findViewById(R.id.graphLayout);
            	layout.addView(view);            	
        	}
        });
    }
    
    public static void updateStats(final HashMap<String, Double> data){
    	activity.runOnUiThread(new Runnable(){
        	@Override
        	public void run(){
            	stats.resetData(new GraphViewData[] {
                	      new GraphViewData(1, data.get("train")),
                	      new GraphViewData(2, data.get("truck")),
                	      new GraphViewData(3, data.get("seaShip")),
                	      new GraphViewData(4, data.get("barge")),
                	      new GraphViewData(5, data.get("storage")),
                	      new GraphViewData(6, data.get("agv")),
                	      new GraphViewData(7, data.get("other"))
                	});
        	}
        });
    }
    
    public static GraphViewSeries getStats(){
    	return stats;
    }
}
