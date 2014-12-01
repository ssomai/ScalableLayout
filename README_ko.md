ScalableLayout for Android. 
====================

Class: com.jnm.android.widget.ScalableLayout 

단 한번의 코딩으로 태블릿을 포함한 모든 화면에서 정확한 UI를 표현하고 싶다면!<br/>
====================
ScalableLayout은 화면의 크기가 매우 다양한 안드로이드 폰과 태블릿 환경에서<br/>
일관성 있는 UI를 표현하기 위한 방법 중의 하나입니다.<br/>
<br/>
ScalableLayout은 android에서 widget을 감싸는 용도로 쓰이는<br/>
FrameLayout 이나 LinearLayout 대신 이용될 수 있는 Layout 입니다.<br/>
<br/>
UI를 구성하는 TextView나 ImageView 같은 view 들은<br/>
ScalableLayout 안에서 상대적인 (x,y) 좌표와 (width, height) 값을 부여받은 뒤,<br/>
ScalableLayout 의 크기가 변함에 따라 비율에 맞춰 위치와 크기가 변화됩니다.<br/>
TextView나 EditText같이 텍스트가 들어가는 view들은 (text size) 값을 부여하면<br/>
텍스트 크기가 ScalableLayout의 크기에 맞춰서 변화됩니다.<br/>
<br/>
ScalableLayout은 단 하나의 java file을 import하는 것만으로 모든 기능을 제공합니다.<br/>
Java 또는 XML 의 Android 에서 UI를 구성하는 두가지 방법 모두에서 ScalableLayout을 이용할 수 있습니다.<br/>
<br/>
ScalableLayout은 2013년에 Google Play에서 빛난 올해의 앱 모음에 선정된<br/>
S.M.ENTERTAINMENT의 everysing 앱에서도 이용되어 그 우수성을 증명한 바 있습니다.<br/>
<br/>

# 자바로 뷰를 배치하는 예시 코드

    // ScalableLayout의 사이즈를 400 x 200 으로 설정합니다. 
    // 상대적인 단위로 pixel 이나 dp 단위가 아닙니다.
    ScalableLayout sl = new ScalableLayout(this, 400, 200);


    // ScalableLayout에 TextView를 넣어보겠습니다. 
    TextView tv = new TextView(this);
    
    // 왼쪽 x 좌표는 20, y좌표는 40, width는 100, height는 30으로 ScalableLayout에 TextView를 넣습니다. 
    // 단 한줄의 코딩으로 view를 넣은 후 이후에는 자동으로 리사이즈 됩니다.
    sl.addView(tv, 20f, 40f, 100f, 30f);
    
    // TextView 안의 text의 사이즈를 20으로 설정합니다. text의 사이즈도 자동으로 조절됩니다.
    sl.setScale_TextSize(tv, 20f);
    
    // 기존의 TextView 함수들이 물론 정상적으로 작동합니다.
    tv.setText("test");
    tv.setBackgroundColor(Color.YELLOW);
    
    
    // ScalableLayout에 ImageView를 넣어보겠습니다. 
    ImageView iv = new ImageView(this);
    
    // 왼쪽 x 좌표는 200, y좌표는 30, width는 50, height도 50으로 ScalableLayout에 ImageView를 넣습니다. 
    // 단 한줄의 코딩으로 view를 넣은 후 이후에는 자동으로 리사이즈 됩니다.
    sl.addView(iv, 200f, 30f, 50f, 50f);
    
    // 마찬가지로 기존의 ImageView 함수들이 물론 정상적으로 작동합니다.
    iv.setImageResource(R.drawable.ic_launcher);


# xml로 뷰를 배치하는 예시 코드

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


# 다양한 비율의 화면에서 ScalableLayout이 적용된 예.
왼쪽부터 삼성 갤럭시 S4 (1920 x 1080. 16:9), LG 옵티머스 뷰2 (1024 x 768. 4:3), 갤럭시 노트 10.1 (1280 x 800. 8:5)<br/><br/>
![alt tag](https://raw.github.com/ssomai/ScalableLayout/master/images/sl_01_main.jpg)
모든 사이즈의 화면에서 각각의 UI가 정확하게 표현되어 있습니다.<br/><br/>

![alt tag](https://raw.github.com/ssomai/ScalableLayout/master/images/sl_02_singtop100.jpg)
ListView에서 각각의 UI가 정확하게 표현되어 있습니다.<br/><br/>

![alt tag](https://raw.github.com/ssomai/ScalableLayout/master/images/sl_03_singoption.jpg)
다이얼로그에서도 각각의 UI가 정확하게 표현되어 있습니다. 비율대로 표현되기 때문에 옵티머스뷰2에서는 좌우의 여백이 큽니다.

# ScalableLayout으로 Layout하실 때 참고하셔야 할 점
ScalableLayout을 Root Layout으로 지정하시는 것은 추천하지 않습니다.
그 이유는 폰마다 화면 비율이 다르기 때문입니다.
ScalableLayout은 지정받은 비율을 그대로 유지합니다.
즉, 600 x 1000 로 지정하셨다면 그 비율을 그대로 유지합니다.

추천드리고 싶은 방법은 LinearLayout이나 FrameLayout, RelatvieLayout등을 최상위로 두시고
그안에 각 부분들을 ScalableLayout을 만드시는 것입니다.

그리고 기획이나 디자인 단계에 종사하시는 분께 안드로이드에서는 화면비율이 달라지는것을 항상 고려해달라고 부탁드려주세요. ㅎㅎ



