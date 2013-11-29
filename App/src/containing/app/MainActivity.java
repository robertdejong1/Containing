package containing.app;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

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
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private Thread thread;
	private static boolean isConnected = false;
	private static TextView textView;
	private static MainActivity activity;
	private static Button button;

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
        
        showChart();
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
    
    public XYMultipleSeriesDataset getDataSet(Object json) {
    	XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

        XYSeries train = new XYSeries("Train");
        XYSeries truck = new XYSeries("Truck");
        XYSeries seaShip = new XYSeries("Seaship");
        XYSeries barge = new XYSeries("Barge");
        XYSeries opslag = new XYSeries("Storage");
        XYSeries agv = new XYSeries("AGV");
        XYSeries other = new XYSeries("Other");
        
        train.add(1, 1);
        truck.add(2,1);
        seaShip.add(3, 1);
        barge.add(4,1);
        opslag.add(5, 1);
        agv.add(6,5);
        other.add(7,6);
        
        dataset.addSeries(train);
        dataset.addSeries(truck);
        dataset.addSeries(seaShip);
        dataset.addSeries(barge);
        dataset.addSeries(opslag);
        dataset.addSeries(agv);
        dataset.addSeries(other);
        
        return dataset;
    }
    
    public XYMultipleSeriesRenderer getRender(){
    	XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
        //mRenderer.setChartTitle("Submission Statistics");
        mRenderer.setXTitle("Type");
        mRenderer.setYTitle("Number of containers");
        mRenderer.setAxesColor(Color.BLACK);
        mRenderer.setLabelsColor(Color.BLACK);
        mRenderer.setApplyBackgroundColor(true);
        mRenderer.setBackgroundColor(Color.WHITE);
        mRenderer.setMarginsColor(Color.WHITE);
        mRenderer.setZoomEnabled(false);
        mRenderer.setZoomButtonsVisible(false);

    //  mRenderer.setMargins(new int[] {20, 30, 15, 0});
        mRenderer.setAxisTitleTextSize(16);
        mRenderer.setChartTitleTextSize(20);
        mRenderer.setLabelsTextSize(15);
        mRenderer.setLegendTextSize(15);
        
        mRenderer.addXTextLabel(1, "Train");
        mRenderer.addXTextLabel(2, "Truck");
        mRenderer.addXTextLabel(3, "Seaship");
        mRenderer.addXTextLabel(4, "Barge");
        mRenderer.addXTextLabel(5, "Storage");
        mRenderer.addXTextLabel(6, "AGV");
        mRenderer.addXTextLabel(7, "Other");
        
        mRenderer.setBarWidth(50);
        mRenderer.setXAxisMin(0);
        mRenderer.setYAxisMin(0);
        
        mRenderer.setShowGridX(false);

        XYSeriesRenderer renderer = new XYSeriesRenderer();
        renderer.setColor(Color.parseColor("#00AA00"));
        renderer.setDisplayChartValues(true);

        XYSeriesRenderer renderer2 = new XYSeriesRenderer();
        renderer2.setColor(Color.parseColor("#666600"));
        renderer2.setDisplayChartValues(true);

        XYSeriesRenderer renderer3 = new XYSeriesRenderer();
        renderer3.setColor(Color.parseColor("#FF0000"));
        renderer3.setDisplayChartValues(true);

        XYSeriesRenderer renderer4 = new XYSeriesRenderer();
        renderer4.setColor(Color.parseColor("#0000FF"));
        renderer4.setDisplayChartValues(true);

        XYSeriesRenderer renderer5 = new XYSeriesRenderer();
        renderer5.setColor(Color.parseColor("#6767D0"));
        renderer5.setDisplayChartValues(true);

        XYSeriesRenderer renderer6 = new XYSeriesRenderer();
        renderer6.setColor(Color.parseColor("#AAAA00"));
        renderer6.setDisplayChartValues(true);

        XYSeriesRenderer renderer7 = new XYSeriesRenderer();
        renderer7.setColor(Color.parseColor("#00AAAA"));
        renderer7.setDisplayChartValues(true);

        mRenderer.addSeriesRenderer(renderer);
        mRenderer.addSeriesRenderer(renderer2);
        mRenderer.addSeriesRenderer(renderer3);
        mRenderer.addSeriesRenderer(renderer4);
        mRenderer.addSeriesRenderer(renderer5);
        mRenderer.addSeriesRenderer(renderer6);
        mRenderer.addSeriesRenderer(renderer7);
        
        return mRenderer;
    }
    
    public void showChart(){
    	startActivity(ChartFactory.getBarChartIntent(this, getDataSet(new Object()), getRender(), Type.DEFAULT));
    }
    
    
}
