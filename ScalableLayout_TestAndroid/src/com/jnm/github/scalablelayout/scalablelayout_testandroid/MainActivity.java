package com.jnm.github.scalablelayout.scalablelayout_testandroid;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jnm.github.android.scalablelayout.scalablelayout_testandroid.R;
import com.jnm.github.scalablelayout.ScalableLayout;

public class MainActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	    // init base width and height of scalable layout.
		ScalableLayout sl = new ScalableLayout(this, 400, 200);
		sl.setBackgroundColor(Color.LTGRAY);
		
	    // insert TextView
		TextView tv = new TextView(this);
		tv.setText("test");
		tv.setBackgroundColor(Color.YELLOW);
		sl.addView(tv, 20f, 40f, 100f, 30f);
		sl.setTextSize(tv, 20f);
		
	    // insert ImageView
		ImageView iv = new ImageView(this);
		iv.setImageResource(R.drawable.ic_launcher);
		sl.addView(iv, 200f, 30f, 50f, 50f);
		
		RelativeLayout rl = (RelativeLayout)findViewById(R.id.main_relativelayout);
		RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		rllp.addRule(RelativeLayout.BELOW, R.id.main_textview);
		rl.addView(sl, rllp);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
	
		return true;
	}
	
}
