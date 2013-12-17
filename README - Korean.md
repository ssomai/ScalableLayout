ScalableLayout
====================

ScalableLayout for Android.

Class: com.jnm.android.widget.ScalableLayout



ScalableLayout은 화면의 크기가 매우 다양한 안드로이드 폰과 태블릿 환경에서<br/> 
일관성 있는 UI를 표현하기 위한 방법 중의 하나입니다.<br/>
<br/>
ScalableLayout은 android에서 widget을 감싸는 용도로 쓰이는<br/>
FrameLayout 이나 LinearLayout 대신 이용될 수 있는 Layout 입니다.<br/>
<br/>
UI를 구성하는 TextView나 ImageView 같은 view 들은<br/>
ScalableLayout 안에서 상대적인 (x,y) 좌표와 (width, height) 값을 부여받은 뒤,<br/>
ScalableLayout 의 크기가 변함에 따라 비율에 맞춰 위치와 크기가 변화됩니다.<br/>
<br/>
ScalableLayout은 단 하나의 java file을 import하는 것으로 모든 기능을 제공합니다.<br/>
Java 또는 XML 의 Android 에서 UI를 구성하는 두가지 방법 모두에서 ScalableLayout을 이용할 수 있습니다.<br/>
<br/>
ScalableLayout은 2013년에 Google Play에서 빛난 올해의 앱 모음에 선정된<br/>
S.M.ENTERTAINMENT의 everysing 앱에서도 이용되어 그 우수성을 증명한 바 있습니다.<br/>
<br/>

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

원리
====================
작성중...


예시 화면
====================
![alt tag](https://raw.github.com/ssomai/ScalableLayout/master/images/totalshot.jpg)
![alt tag](https://raw.github.com/ssomai/ScalableLayout/master/images/tablet.jpg)
![alt tag](https://raw.github.com/ssomai/ScalableLayout/master/images/phone.jpg)



