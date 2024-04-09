package com.ssomai.android.sample_scalablelayout;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ssomai.android.scalablelayout.ScalableLayout;


public class MainActivity extends Activity {

//    private static final String DebugTag = "ScalableLayout_TestAndroid";
    private static final String DebugTag = "TestAndroid";
    private TextView mTV_Log;
    private ScalableLayout mSL;

    private static void log(String pLog) {
        Log.e(DebugTag, "MainActivity] " + pLog);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ScalableLayout.setLoggable("MainActivity");
//        ScalableLayout.setLogger(new ScalableLayout.ILogger() {
//            @Override
//            public void log(String pLog) {
//                throw new NullPointerException();
////                mTV_Log.setText(pLog+"\n"+mTV_Log.getText()+"ssdfsdfsdfsdf");
//            }
//        });

		setContentView(R.layout.activity_main);
//        mTV_Log = (TextView) findViewById(R.id.tv_log);


////		ScalableLayout.setLoggable(DebugTag);
////
////		LinearLayout lLL = new LinearLayout(this);
////		lLL.setOrientation(LinearLayout.VERTICAL);
////		lLL.setBackgroundColor(Color.CYAN);
////		setContentView(lLL);
////
////		{
////			Button btn = new Button(this);
////			btn.setText("늘리기");
////			btn.setOnClickListener(new View.OnClickListener() {
////				@Override
////				public void onClick(View pV) {
////					mTV_Text.setText(mTV_Text.getText()+"ㅁㄷㄷㄷ디");
////				}
////			});
////			lLL.addView(btn);
////		}
////		{
////			Button btn = new Button(this);
////			btn.setText("줄이기");
////			btn.setOnClickListener(new View.OnClickListener() {
////				@Override
////				public void onClick(View pV) {
////					mTV_Text.setText(mTV_Text.getText().subSequence(0, mTV_Text.getText().length()/2));
////				}
////			});
////			lLL.addView(btn);
////		}
////
////
////		mSL = new ScalableLayout(this, 400, 400);
////		mSL.setBackgroundColor(Color.LTGRAY);
////		lLL.addView(mSL, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
////
////		ImageView iv = new ImageView(this);
////		iv.setImageResource(R.drawable.ic_launcher);
////		mSL.addView(iv, 200f, 30f, 50f, 50f);
////
////		ImageView iv_surrounded = new ImageView(this);
////		iv_surrounded.setBackgroundColor(Color.BLUE);
////		mSL.addView(iv_surrounded, 80f, 80f, 240f, 90f);
////
////		ImageView iv2 = new ImageView(this);
////		iv2.setImageResource(R.drawable.ic_launcher);
////		mSL.addView(iv2, 220f, 160f, 50f, 50f);
////
////		TextView tv3 = new TextView(this);
////		tv3.setText("test");
////		tv3.setBackgroundColor(Color.YELLOW);
////		mSL.addView(tv3, 0f, 210f, 60f, 30f);
////		mSL.setScale_TextSize(tv3, 20f);
////
////		TextView tv4 = new TextView(this);
////		tv4.setText("test");
////		tv4.setBackgroundColor(Color.YELLOW);
////		mSL.addView(tv4, 150f, 250f, 100f, 30f);
////		mSL.setScale_TextSize(tv4, 20f);
////
////		TextView tv5 = new TextView(this);
////		tv5.setText("test");
////		tv5.setBackgroundColor(Color.YELLOW);
////		mSL.addView(tv5, 350f, 300f, 100f, 30f);
////		mSL.setScale_TextSize(tv5, 20f);
////
////
////		mTV_Text = new TextView(this);
////		mTV_Text.setText("test");
////		mTV_Text.setBackgroundColor(Color.RED);
////		mSL.addView(mTV_Text, 40f, 170f, 100f, 0f);
////		mSL.setScale_TextSize(mTV_Text, 20f);
////		mSL.setTextView_WrapContent(mTV_Text, TextView_WrapContent_Direction.Bottom, true);
//
//
//
////		setContentView(R.layout.activity_main);
//
//        // init base width and height of scalable layout.
//        mSL = new ScalableLayout(this, 400, 500);
////		mSL = (ScalableLayout) findViewById(R.id.main_sl);
//        mSL.setBackgroundColor(Color.LTGRAY);
        ScalableLayout.setLoggable(DebugTag);
//
////		RelativeLayout rl = (RelativeLayout)findViewById(R.id.main_relativelayout);
////		RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
////		rllp.addRule(RelativeLayout.BELOW, R.id.main_textview);
////		rl.addView(mSL, rllp);
//
//        setContentView(mSL);
//
//
//        // insert TextView
////		TextView tv = new TextView(this);
////		tv.setText("test");
////		tv.setBackgroundColor(Color.YELLOW);
////		mSL.addView(tv, 20f, 40f, 100f, 30f);
////		mSL.setScale_TextSize(tv, 20f);
//
//        // insert ImageView
//        ImageView iv = new ImageView(this);
//        iv.setImageResource(com.ssomai.android.sample_scalablelayout.R.drawable.ic_launcher);
//        mSL.addView(iv, 200f, 30f, 50f, 50f);
//
//        ImageView iv_surrounded = new ImageView(this);
//        iv_surrounded.setBackgroundColor(Color.BLUE);
//        mSL.addView(iv_surrounded, 80f, 80f, 240f, 90f);
////		mSL.addView(mTV_Text, 100, 100, 200, 50);
//
//
////		mTV_Text = (TextView) findViewById(R.id.sl_textview);
////		mTV_Text = new EditText(this) {
//////			@Override
//////			protected void onTextChanged(CharSequence pText, int pStart, int pLengthBefore, int pLengthAfter) {
//////			    super.onTextChanged(pText, pStart, pLengthBefore, pLengthAfter);
//////				log("getHeight "+getHeight2(getContext(), (String)pText, (int)getTextSize(), getWidth()));
//////				log("aa "+pText);
//////			}
////		};
////		mTV_Text.setBackgroundColor(Color.RED);
////		mTV_Text.setText("test");
//////		mTV_Text.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
//////			 @SuppressLint("NewApi")
//////			 @SuppressWarnings("deprecation")
//////			 @Override
//////			  public void onGlobalLayout() {
//////				log("onGlobalLayout "+mTV_Text.getWidth());
//////			   //now we can retrieve the width and height
////////			   int width = view.getWidth();
////////			   int height = view.getHeight();
//////			   //...
//////			   //do whatever you want with them
//////			   //...
//////			   //this is an important step not to keep receiving callbacks:
//////			   //we should remove this listener
//////			   //I use the function to remove it based on the api level!
//////
////////			if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN)
////////			    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
////////			   else
////////			    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//////			  }
//////			 });
////		mSL.addView(mTV_Text, 100, 100, 200, 50);
////		mSL.setScale_TextSize(mTV_Text, 30);
//////		mTV_Text.setSingleLine();
////
////		mSL.setTextView_WrapContent(mTV_Text, TextView_WrapContent_Direction.Bottom, true);
//
////		mTV_Text.addTextChangedListener(new TextWatcher() {
////			@Override
////			public void onTextChanged(CharSequence pS, int pStart, int pBefore, int pCount) { }
////			@Override
////			public void beforeTextChanged(CharSequence pS, int pStart, int pCount, int pAfter) { }
////			@Override
////			public void afterTextChanged(Editable pS) {
////				ScalableLayout.LayoutParams lTV_SLLP = (ScalableLayout.LayoutParams) mTV_Text.getLayoutParams();
////
////				float lNewViewHeight = getHeight2(mTV_Text.getContext(), (String)pS.toString(), (int)mTV_Text.getTextSize(), mTV_Text.getWidth());
////				float lOldScaleHeight = lTV_SLLP.getScale_Height();
////				float lOldViewHeight = mTV_Text.getHeight();
////				float lNewScaleHeight = (lNewViewHeight * lOldScaleHeight / lOldViewHeight);
////
////				log("getHeight1 "+lNewViewHeight);
//////				log("getHeight2 "+mTV_Text.getHeight());
////				mSL.moveChildView(mTV_Text, lTV_SLLP.getScale_Left(), lTV_SLLP.getScale_Top(), lTV_SLLP.getScale_Width(), lNewScaleHeight);
////
////				for(int i=0;i<mSL.getChildCount();i++) {
////					View v = mSL.getChildAt(i);
////					if(v == mTV_Text) {
////						continue;
////					}
////					ScalableLayout.LayoutParams lSLLP = (ScalableLayout.LayoutParams) v.getLayoutParams();
////					if(
////						lSLLP.getScale_Top() >= lTV_SLLP.getScale_Top()+lOldScaleHeight &&
////						(
////						( lTV_SLLP.getScale_Left() <= lSLLP.getScale_Left() && lSLLP.getScale_Left() <= lTV_SLLP.getScale_Left()+lTV_SLLP.getScale_Width() )
////						||
////						( lTV_SLLP.getScale_Left() <= lSLLP.getScale_Left()+lSLLP.getScale_Width() && lSLLP.getScale_Left()+lSLLP.getScale_Width() <= lTV_SLLP.getScale_Left()+lTV_SLLP.getScale_Width())
////						)) {
////
////						mSL.moveChildView(v, lSLLP.getScale_Left(), lSLLP.getScale_Top()+lNewScaleHeight-lOldScaleHeight);
////					}
////				}
////				mSL.setScaleHeight(mSL.getScaleHeight()+lNewScaleHeight-lOldScaleHeight);
////
////				mSL.requestLayout();
////				mSL.forceLayout();
////			}
////		});
//
//        ImageView iv2 = new ImageView(this);
//        iv2.setImageResource(com.ssomai.android.sample_scalablelayout.R.drawable.ic_launcher);
//        mSL.addView(iv2, 220f, 160f, 50f, 50f);
//
//        TextView tv2 = new TextView(this);
//        tv2.setText("test2");
//        tv2.setBackgroundColor(Color.YELLOW);
//        mSL.addView(tv2, 40f, 170f, 100f, 30f);
//        mSL.setScale_TextSize(tv2, 20f);
//
//        TextView tv3 = new TextView(this);
//        tv3.setText("test3");
//        tv3.setBackgroundColor(Color.YELLOW);
//        mSL.addView(tv3, 0f, 210f, 60f, 30f);
//        mSL.setScale_TextSize(tv3, 20f);
//
//        TextView tv4 = new TextView(this);
//        tv4.setText("test4");
//        tv4.setBackgroundColor(Color.YELLOW);
//        mSL.addView(tv4, 150f, 250f, 100f, 30f);
//        mSL.setScale_TextSize(tv4, 20f);
//
//        TextView tv5 = new TextView(this);
//        tv5.setText("test5");
//        tv5.setBackgroundColor(Color.YELLOW);
//        mSL.addView(tv5, 350f, 300f, 100f, 30f);
//        mSL.setScale_TextSize(tv5, 20f);
//
//        TextView tv6 = new TextView(this);
//        tv6.setText("test6");
//        tv6.setBackgroundColor(Color.YELLOW);
//        mSL.addView(tv6, 50f, 300f, 120f, 40f);
//        mSL.setScale_TextSize(tv6, 20f);
//
////		mTV_Text = new TextView(this);
//////		mTV_Text.setText("test\n2233");
//////		mTV_Text.setText("test\n2233\n\n");
//////		mTV_Text.setText("test\n2233\n\n\n\nqwelkqjwkrjqw\n\naa");
//////		mTV_Text.setText("test\n2233\nqw\nel\nkqj\nwelkqwje\n\n\n\nwelkrjqw");
//////		mTV_Text.setText("test\n2233\nqw\nel\nkqj\nwelkqwje\n\n\n\nwelkrjqw"
//////					+"\n" +
//////					"ㅁㅈ디ㅏ거미더가\n" +
//////					"ㅁㅈ디ㅏ거밎더ㅏ\n" +
//////					"ㅁㅈ디ㅏ거미더가\n" +
//////					"ㅁㅈ디ㅏ거밎더ㅏ\n" +
//////					"ㅁㅈ디ㅏ거미더가\n" +
//////					"ㅁㅈ디ㅏ거밎더ㅏ\n" +
//////					"ㅁ기ㅏㅓㅎㅁ디가ㅓㅅㅎ미ㅓ\n"
//////);
////		mTV_Text.setText("test\n2233\nqw\nel\nkqj\nwelkqwje\n\n\n\nwelkrjqw"
////			+"\n" +
////			"1qwelkjlaske" +
////			"2aa409203kkl" +
//////			"3ㅁㅈ디ㅏ더가" +
//////			"4ㅁㅈ디ㅏ밎더ㅏ" +
//////			"5ㅁㅈ디ㅏ미더가" +
////			"6aaaaawww" +
////			"7zzzzzzz\n"
////			);
////		mTV_Text.setBackgroundColor(Color.RED);
////		mSL.addView(mTV_Text, 40f, 170f, 100f, 100f);
////		mSL.setScale_TextSize(mTV_Text, 30f);
////		mSL.setTextView_WrapContent(mTV_Text, TextView_WrapContent_Direction.Bottom, true);
//
//
//        mTV_Text = new TextView(this);
////		mTV_Text.setText("test\n2233\n\n\n\nqwelkqjwkrjqw\n\naa");
//        mTV_Text.setText("test");
//        mTV_Text.setBackgroundColor(Color.RED);
////		mSL.addView(mTV_Text, 40f, 170f, 100f, 100f);
//        mSL.addView(mTV_Text, 200f, 200f, 1f, 100f);
//        mSL.setScale_TextSize(mTV_Text, 30f);
//        mSL.setTextView_WrapContent(mTV_Text, ScalableLayout.TextView_WrapContent_Direction.Center_Horizontal, false);
//
////		Handler h = new Handler();
////		h.postDelayed(new Runnable() {
////			@Override
////			public void run() {
////				mTV_Text.setText(
////					mTV_Text.getText()
////					+"\n" +
////					"ㅁㅈ디ㅏ거미더가\n" +
////					"ㅁㅈ디ㅏ거밎더ㅏ\n" +
////					"ㅁㅈ디ㅏ거미더가\n" +
////					"ㅁㅈ디ㅏ거밎더ㅏ\n" +
////					"ㅁㅈ디ㅏ거미더가\n" +
////					"ㅁㅈ디ㅏ거밎더ㅏ\n" +
////					"ㅁㅈ디ㅏ거미더가\n" +
////					"ㅁㅈ디ㅏ거밎더ㅏ\n" +
////					"ㅁ기ㅏㅓㅎㅁ디가ㅓㅅㅎ미ㅓ\n"
////					);
////			}
////		}, 1000);
//
//        {
//            Button btn = new Button(this);
//            btn.setText("Refresh");
//            btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View pV) {
//                    mSL.requestLayout();
//                    mSL.forceLayout();
//                }
//            });
//            mSL.addView(btn, 205, 100, 200, 100);
//            mSL.setScale_TextSize(btn, 30);
//        }
//
////		{
////			Button btn = new Button(this);
////			btn.setText("늘리기");
////			btn.setOnClickListener(new View.OnClickListener() {
////				@Override
////				public void onClick(View pV) {
////					mTV_Text.setText(mTV_Text.getText()+"ㅁ디");
////				}
////			});
////			mSL.addView(btn, 305, 100, 100, 60);
////			mSL.setScale_TextSize(btn, 20);
////		}
////		{
////			Button btn = new Button(this);
////			btn.setText("줄이기");
////			btn.setOnClickListener(new View.OnClickListener() {
////				@Override
////				public void onClick(View pV) {
////					mTV_Text.setText(mTV_Text.getText().subSequence(0, mTV_Text.getText().length()/2));
////				}
////			});
////			mSL.addView(btn, 305, 170, 100, 60);
////			mSL.setScale_TextSize(btn, 20);
////		}


    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.ssomai.android.sample_scalablelayout.R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == com.ssomai.android.sample_scalablelayout.R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void onClick_SetMoreText(View view) {
        TextView tv = (TextView) findViewById(R.id.tv_moretext);
        tv.setText(tv.getText() + "커");
        tv.requestLayout();
    }

    public void onClick_SetLessText(View view) {
        TextView tv = (TextView) findViewById(R.id.tv_moretext);
        tv.setText(tv.getText().subSequence(1, tv.getText().length()));
        tv.requestLayout();
    }

    public void onClick_imgPatient(View view) {
        final TextView tv = (TextView) findViewById(R.id.txtUserName);
//        tv.setEllipsize(TextUtils.TruncateAt.END);
//        tv.setMaxLines(1);
//        tv.setText("text");
        tv.setText("테스트트트asdqwe답지다ㅣ");


        tv.setEllipsize(TextUtils.TruncateAt.MARQUEE);
//        tv.setEllipsize(TextUtils.TruncateAt.END);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tv.setEllipsize(TextUtils.TruncateAt.END);
                tv.forceLayout();
                tv.requestLayout();
                findViewById(R.id.patientResultScalable).requestLayout();
                findViewById(R.id.patientResultScalable).forceLayout();
                findViewById(R.id.patientResultScalable).invalidate();
                tv.invalidate();
            }
        }, 1000);
    }
}
