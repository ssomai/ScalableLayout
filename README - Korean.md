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
<br/>
ScalableLayout은 단 하나의 java file을 import하는 것만으로 모든 기능을 제공합니다.<br/>
Java 또는 XML 의 Android 에서 UI를 구성하는 두가지 방법 모두에서 ScalableLayout을 이용할 수 있습니다.<br/>
<br/>
ScalableLayout은 2013년에 Google Play에서 빛난 올해의 앱 모음에 선정된<br/>
S.M.ENTERTAINMENT의 everysing 앱에서도 이용되어 그 우수성을 증명한 바 있습니다.<br/>
<br/>

다양한 비율의 화면에서 ScalableLayout이 적용된 예.
====================
왼쪽부터 삼성 갤럭시 S4 (1920 x 1080. 16:9), LG 옵티머스 뷰2 (1024 x 768. 4:3), 갤럭시 노트 10.1 (1280 x 800. 8:5)<br/><br/>
![alt tag](https://raw.github.com/ssomai/ScalableLayout/master/images/sl_01_main.jpg)
모든 사이즈의 화면에서 각각의 UI가 정확하게 표현되어 있습니다.<br/><br/>

![alt tag](https://raw.github.com/ssomai/ScalableLayout/master/images/sl_02_singtop100.jpg)
ListView에서 각각의 UI가 정확하게 표현되어 있습니다.<br/><br/>

![alt tag](https://raw.github.com/ssomai/ScalableLayout/master/images/sl_03_singoption.jpg)
다이얼로그에서도 각각의 UI가 정확하게 표현되어 있습니다. 비율대로 표현되기 때문에 옵티머스뷰2에서는 좌우의 여백이 큽니다.



