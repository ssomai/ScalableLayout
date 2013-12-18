ScalableLayout for Android. 
====================

Class: com.jnm.android.widget.ScalableLayout 

Just one code for every resolution of Android devices!<br/>
====================

ScalableLayout is one of the way to show consistent UI<br/>
for every different resolution of Android devices including tablets.<br/>
<br/>
ScalableLayout can be usable in replace of Layouts for example FrameLayout or LinearLayout,<br/>
which are used to place widgets from Android platform.<br/>
<br/>
Widgets to make application UI like TextView or Imageview<br/>
would get relative (x,y) coordinates and relative (width, height) values in ScalableLayout.<br/>
And then the ScalableLayout would place and resize the widgets inside according to its size.<br/>
<br/>
You can use ScalableLayout by just one java file importing.<br/>
You can use both of Java and XML to place ScalableLayout in your project.<br/>
<br/>
ScalableLayout is used on the everysing application which was awarded as GooglePlay App Awards 2013.<br/>
<br/>



# Simple example in Java

    // Initialte ScalableLayout instance with 400 width and 200 height. 
    // It's relative unit. It's not pixel or dp unit.
    ScalableLayout sl = new ScalableLayout(this, 400, 200);


    // Gonna place a TextView instance inside ScalableLayout instance. 
    TextView tv = new TextView(this);
    
    // Placing a TextView with following parameters. left: 20, top: 40, width: 100, height: 30.  
    // You can place it very easily. 
    // It would place and scale automatically according to the size of its parent ScalableLayout.
    sl.addView(tv, 20f, 40f, 100f, 30f);
    
    // Set the text size of TextView as 20. It would scale automatically.
    sl.setScale_TextSize(tv, 20f);
    
    // All the original methods of TextView works properly. 
    tv.setText("test");
    tv.setBackgroundColor(Color.YELLOW);
    
    
    // Gonna place a ImageView instance inside ScalableLayout instance. 
    ImageView iv = new ImageView(this);
    
    // Placing a ImageView with following parameters. left: 200, top: 30, width: 50, height: 50.  
    // You can place it very easily. 
    // It would place and scale automatically according to the size of its parent ScalableLayout.
    sl.addView(iv, 200f, 30f, 50f, 50f);
    
    // All the original methods of ImageView works properly of course. 
    iv.setImageResource(R.drawable.ic_launcher);


# Simple example in XML

    <com.jnm.android.widget.ScalableLayout
      android:layout_width="match_parent"
      android:layout_height="wrap_content"
      android:background="@android:color/darker_gray"
      android:layout_above="@+id/main_textview"
      sl:scale_base_width="400"
      sl:scale_base_height="200"
      >
      <TextView 
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        sl:scale_left="20"
        sl:scale_top="40"
        sl:scale_width="100"
        sl:scale_height="30"
        sl:scale_textsize="20"
        android:text="@string/hello_world" 
        android:textColor="@android:color/white"
        android:background="@android:color/black"
        />
      <ImageView 
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        sl:scale_left="200"
        sl:scale_top="30"
        sl:scale_width="50"
        sl:scale_height="50"
        android:src="@drawable/ic_launcher"
        />
    </com.jnm.android.widget.ScalableLayout>        




Examples of ScalableLayout on different resolutions of Android devices.
====================
From left. Samsung Galaxy S4 (1920 x 1080. 16:9), LG Optimus View2 (1024 x 768. 4:3), Samsung Galaxy Note 10.1 (1280 x 800. 8:5)<br/><br/>
![alt tag](https://raw.github.com/ssomai/ScalableLayout/master/images/sl_01_main.jpg)
All the UIs are placed correctly on different resolutions.<br/><br/>

![alt tag](https://raw.github.com/ssomai/ScalableLayout/master/images/sl_02_singtop100.jpg)
All the UIs in ListView are placed correctly on different resolutions.<br/><br/>

![alt tag](https://raw.github.com/ssomai/ScalableLayout/master/images/sl_03_singoption.jpg)
UIs in Dialog are also placed correctly on different resolutions. You can notice there are more left and right margins on Optimus View 2 to layout correctly.<br/>




