ScalableLayout
====================

ScalableLayout for Android.

com.jnm.github.android.scalablelayout.ScalableLayout

It supports any display size.


Principle
====================
Writing...

Example XML

    <RelativeLayout 
        xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:tools="http://schemas.android.com/tools"
        xmlns:sl="http://schemas.android.com/apk/res/com.jnm.github.android.scalablelayout.scalablelayout_testandroid"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:id="@+id/main_relativelayout"
        tools:context=".MainActivity" >
      <TextView
          android:layout_width="wrap_content"
          android:layout_height="wrap_content"
          android:layout_centerHorizontal="true"
          android:layout_centerVertical="true"
          android:id="@+id/main_textview"
          android:text="@string/hello_world" 
          />
      <com.jnm.github.scalablelayout.ScalableLayout
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
          sl:scale_top="30"
          sl:scale_left="40"
          sl:scale_width="100"
          sl:scale_height="100"
          sl:scale_textsize="20"
          android:text="@string/hello_world" 
          android:textColor="@android:color/white"
          android:background="@android:color/black"
          />
      </com.jnm.github.scalablelayout.ScalableLayout>
    </RelativeLayout>

Preview
====================
![alt tag](https://raw.github.com/ssomai/ScalableLayout/master/images/totalshot.jpg)
![alt tag](https://raw.github.com/ssomai/ScalableLayout/master/images/tablet.jpg)
![alt tag](https://raw.github.com/ssomai/ScalableLayout/master/images/phone.jpg)
