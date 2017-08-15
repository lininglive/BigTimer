package org.LiN.bigtimer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.LiN.bigtimer.util.SystemUiHider;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class BigTimer_Activity extends Activity  {

	final private String TAG = "BigTimer_Activity";
	final private int TIME_UPDATE_SECONDE = 1000;
	private TextView tx_clock = null;
	private int x = 0;
	private SimpleDateFormat df = null;
	private TextClock tclock = null;
	private TextView show;
	
	ListView providers;
	LocationManager lm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.big_timer_mainlayout);
		
		tx_clock = (TextView)findViewById(R.id.Text_clock);
		df = new SimpleDateFormat("HH:mm");
		tx_clock.setText(df.format(new Date()));
		tclock = (TextClock)findViewById(R.id.textClock1);
		
		tclock.setFormat24Hour("yyyy年MM月dd号 H:mm EEEE");
		
		Message m = Message.obtain();
		m.what = 1;
		
		handler.sendMessageDelayed(m, TIME_UPDATE_SECONDE);
		//new TimeTask().execute();
		
		Timer mtimer = new Timer();
		mtimer.schedule(new java.util.TimerTask() {  
            
          @Override  
          public void run() {  
              // TODO Auto-generated method stub  
              //mHandler.sendEmptyMessage(TIME_TO_SCROLL_VIEW);  
          }  
      }, 11300);
		
		show = (TextView) findViewById(R.id.textView2);
		providers = (ListView)findViewById(R.id.providers);
		lm = (LocationManager)getSystemService(
			 Context.LOCATION_SERVICE);
		Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		updateView(location);
		lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 10, new LocationListener()
		{
			@Override
			public void onLocationChanged(Location location)
			{
				updateView(location);
			}
			
			@Override
			public void onProviderDisabled(String provoider)
			{
				updateView(null);
			}
			
			@Override
			public void onProviderEnabled(String provoider)
			{
				updateView(lm.getLastKnownLocation(provoider));
			}
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras)
			{
				
			}
		}
		);
		List<String> providerNames = lm.getAllProviders();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, providerNames);
		providers.setAdapter(adapter);
		
	}
	
	public void updateView(Location newlocation)
	{
		if(newlocation != null)
		{
			StringBuilder sb = new StringBuilder();
			sb.append("实时的位置信息：\n");
			sb.append("经度：");
			sb.append(newlocation.getLongitude());
			sb.append("\n 纬度：");
			sb.append(newlocation.getLatitude());
			//sb.append("\n 高度：");
			//sb.append(newlocation.getAltitude());
			//sb.append("\n 速度：");
			//sb.append(newlocation.getSpeed());
			//sb.append("\n 方向：");
			//sb.append(newlocation.getBearing());
			show.setText(sb.toString());
		}
		else
		{
			show.setText("");
		}
	}

	
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
        if(msg.what == 1)
        {
    		//SimpleDateFormat df = new SimpleDateFormat("HH:mm");
    		tx_clock.setText(df.format(new Date()));
    		x++;
    		Log.e(TAG,"handleMessage"+x+"秒");
    		Message mx = Message.obtain();
    		mx.what = 1;
    		handler.sendMessageDelayed(mx, TIME_UPDATE_SECONDE);
        }
        /*if(msg.what == 2)
        {
			Toast.makeText(BigTimer_Activity.this, "xxxoooxoo", Toast.LENGTH_SHORT).show();
        }*/
        super.handleMessage(msg);
        }
    };
    
    class TimeTask extends AsyncTask<Void, Void, Void>{
		
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			Message m = Message.obtain();
			m.what = 1;
			while(true){
				if(!tx_clock.getText().equals(df.format(new Date())))
				{
					handler.sendMessage(m);
					Log.e(TAG,"TimeTask sendMessage----");
				}
			}
		}
    }
	
}
