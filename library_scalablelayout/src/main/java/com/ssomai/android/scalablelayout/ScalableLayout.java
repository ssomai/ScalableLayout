package com.ssomai.android.scalablelayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

public class ScalableLayout extends FrameLayout {
    private static final float Default_Scale_Base_Width = 100f;
    private static final float Default_Scale_Base_Height = 100f;

    private static final float Default_Scale_Left = 0f;
    private static final float Default_Scale_Top = 0f;
    private static final float Default_Scale_Width = 100f;
    private static final float Default_Scale_Height = 100f;

    private static final float Default_Scale_TextSize = 100f;

    private float mScale_Root_Width 	= Default_Scale_Base_Width;
    private float mScale_Root_Height 	= Default_Scale_Base_Height;
    private float mRatioOfWidthHeight 	= mScale_Root_Height / mScale_Root_Width;

    public float getScaleWidth() { return mScale_Root_Width; }
    public float getScaleHeight() { return mScale_Root_Height; }

    public void setScaleWidth(float pWidth) { setScaleSize(pWidth, mScale_Root_Height); }
    public void setScaleHeight(float pHeight) { setScaleSize(mScale_Root_Width, pHeight); }
    public void setScaleSize(float pWidth, float pHeight) {
        setScaleSize(pWidth, pHeight, true);
    }
    private void setScaleSize(float pWidth, float pHeight, boolean pWithInvalidate) {
        mScale_Root_Width = pWidth;
        mScale_Root_Height = pHeight;
        mRatioOfWidthHeight = mScale_Root_Height / mScale_Root_Width;
        if(pWithInvalidate) {
            postDelayedRequestLayout();
        }
    }
    private long mLastRequestPostTime = 0;
    private void postDelayedRequestLayout() {
        long now = System.currentTimeMillis();
        if(mLastRequestPostTime < now - 50 || now < mLastRequestPostTime) {
            mLastRequestPostTime = now;
//			log("==== postDelayedRequestLayout Start");
//			log(getStackTrace(Thread.currentThread().getStackTrace()));
//			log("==== postDelayedRequestLayout End");
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    requestLayout();
                    forceLayout();
                }
            }, 10);
        }
    }

    /**
     * Simple constructor to use when creating a view from code.
     * Scale_Width, Scale_Height uses a default value of 100.
     * <p>
     * code에서 ScalableLayout을 생성할 때 사용하는 constructor.
     * Scale_Width, Scale_Height를 기본값인 100으로 세팅함.
     *
     * @param pContext
     */
    public ScalableLayout(Context pContext) {
        this(pContext, Default_Scale_Base_Width, Default_Scale_Base_Height);
    }

    /**
     * Constructor that is called when inflating a view from XML.
     * <p>
     * XML에서 ScalableLayout을 생성할 때 사용하는 constructor.
     *
     * @param pContext
     * @param pAttrs
     */
    public ScalableLayout(Context pContext, AttributeSet pAttrs) {
        this(pContext, pAttrs,
                pContext.obtainStyledAttributes(pAttrs, R.styleable.ScalableLayout).getFloat(R.styleable.ScalableLayout_scale_base_width, Default_Scale_Base_Width),
                pContext.obtainStyledAttributes(pAttrs, R.styleable.ScalableLayout).getFloat(R.styleable.ScalableLayout_scale_base_height, Default_Scale_Base_Height)
        );
    }

    /**
     * Simple constructor to use when creating a view from code.
     * <p>
     * code에서 ScalableLayout을 생성할 때 사용하는 constructor.
     *
     * @param pContext
     * @param pScale_Width Scale width of ScalableLayout.
     * @param pScale_Height Scale height of ScalableLayout.
     */
    public ScalableLayout(Context pContext, float pScale_Width, float pScale_Height) {
        this(pContext, null, pScale_Width, pScale_Height);
    }

    private ScalableLayout(Context pContext, AttributeSet pAttrs, float pScale_Width, float pScale_Height) {
        super(pContext, pAttrs);
        setScaleSize(pScale_Width, pScale_Height, true);
    }

    /**
     * @param pChild
     * @return Scalable.LayoutParams of pChild
     */
    public ScalableLayout.LayoutParams getChildLayoutParams(View pChild) {
        ViewGroup.LayoutParams lp = pChild.getLayoutParams();
        if(lp == null) {
            lp = generateDefaultLayoutParams();
            pChild.setLayoutParams(lp);
        }
        if(lp instanceof ScalableLayout.LayoutParams == false) {
            throw new IllegalArgumentException("pChild has not ScalableLayout.LayoutParams "+pChild.getLayoutParams());
        }
        return (ScalableLayout.LayoutParams) pChild.getLayoutParams();
    }
    /**
     * sets TextSize of TextView and EdiText to scale automatically
     * @param pTextView
     * @param pScale_TextSize
     */
    public void setScale_TextSize(TextView pTextView, float pScale_TextSize) {
        getChildLayoutParams(pTextView).setScale_TextSize(pScale_TextSize);
    }
    /**
     * @param pTextView target textview to wrap content
     * @param pDirection
     * @param pRescaleSurrounded
     */
    public void setTextView_WrapContent(TextView pTextView, TextView_WrapContent_Direction pDirection, boolean pRescaleSurrounded) {
        setTextView_WrapContent(pTextView, pDirection, pRescaleSurrounded, true);
    }
    public void setTextView_WrapContent(TextView pTextView, TextView_WrapContent_Direction pDirection, boolean pRescaleSurrounded, boolean pMoveSibligs) {
        getChildLayoutParams(pTextView).setTextView_WrapContent(pDirection, pRescaleSurrounded, pMoveSibligs);
        refreshTextChangedListener(pTextView);
    }
    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence pS, int pStart, int pBefore, int pCount) {
            postDelayedRequestLayout();
        }
        @Override
        public void beforeTextChanged(CharSequence pS, int pStart, int pCount, int pAfter) { }
        @Override
        public void afterTextChanged(Editable pS) {
            postDelayedRequestLayout();
        }
    };
    private void refreshTextChangedListener(TextView pTextView) {
        LayoutParams lSLLP = getChildLayoutParams(pTextView);

        try {
            pTextView.removeTextChangedListener(mTextWatcher);
        } catch (Throwable e) {
        }
        if(lSLLP.mTextView_WrapContent_Direction != TextView_WrapContent_Direction.None) {
            pTextView.addTextChangedListener(mTextWatcher);
        }
    }

    @Override
    public void addView(View pChild) { addView(pChild, getChildCount()); }
    @Override
    public void addView(View pChild, int pIndex) { addView(pChild, pIndex, generateDefaultLayoutParams()); }
    @Override
    public void addView(View pChild, int pWidth, int pHeight) { addView(pChild, new ViewGroup.LayoutParams(pWidth, pHeight)); }
    @Override
    public void addView(View pChild, ViewGroup.LayoutParams pParams) { addView(pChild, getChildCount(), pParams); }
    @Override
    public void addView(View pChild, int pIndex, ViewGroup.LayoutParams pParams) {
        if(pParams instanceof ScalableLayout.LayoutParams) {
            addView_Final(pChild, pIndex, (ScalableLayout.LayoutParams) pParams);
        } else {
            addView_Final(pChild, pIndex, generateLayoutParams(pParams));
        }
    }
    public void addView(View pChild, float pScale_Left, float pScale_Top, float pScale_Width, float pScale_Height) {
        addView_Final(pChild, getChildCount(), new ScalableLayout.LayoutParams(pScale_Left, pScale_Top, pScale_Width, pScale_Height));
    }
    private final void addView_Final(View pChild, int pIndex, ScalableLayout.LayoutParams pScaledLayoutParams) {
        super.addView(pChild, pIndex, pScaledLayoutParams);
    }

    @Override
    protected ScalableLayout.LayoutParams generateDefaultLayoutParams() {
        return new ScalableLayout.LayoutParams(0f, 0f, getScaleWidth(), getScaleHeight());
    }
    @Override
    public ScalableLayout.LayoutParams generateLayoutParams(AttributeSet pAttrs) {
        TypedArray attrs = getContext().obtainStyledAttributes(pAttrs, R.styleable.ScalableLayout);

        TextView_WrapContent_Direction dir = TextView_WrapContent_Direction.None;
        String dirstr = attrs.getString(R.styleable.ScalableLayout_textview_wrapcontent_direction);
        if(dirstr != null) {
            for(TextView_WrapContent_Direction d : TextView_WrapContent_Direction.values()) {
                if(dirstr.toLowerCase().compareTo(d.name().toLowerCase()) == 0) {
                    dir = d;
                    break;
                }
            }
        }

        return new ScalableLayout.LayoutParams(
                attrs.getFloat(R.styleable.ScalableLayout_scale_left, Default_Scale_Left),
                attrs.getFloat(R.styleable.ScalableLayout_scale_top, Default_Scale_Top),
                attrs.getFloat(R.styleable.ScalableLayout_scale_width, Default_Scale_Width),
                attrs.getFloat(R.styleable.ScalableLayout_scale_height, Default_Scale_Height),
                attrs.getFloat(R.styleable.ScalableLayout_scale_textsize, Default_Scale_TextSize),
                dir,
                attrs.getBoolean(R.styleable.ScalableLayout_textview_wrapcontent_resizesurrounded, false),
                attrs.getBoolean(R.styleable.ScalableLayout_textview_wrapcontent_movesiblings, true)
        );
    }
    @Override
    protected ScalableLayout.LayoutParams generateLayoutParams(ViewGroup.LayoutParams pP) {
        if(pP instanceof ScalableLayout.LayoutParams) {
            return (ScalableLayout.LayoutParams) pP;
        }
        return new ScalableLayout.LayoutParams(pP);
    }


    /**
     * move childView inside ScalableLayout
     * @param pChildView view to move. should be child of ScaleLayout
     * @param pScale_Left
     * @param pScale_Top
     */
    public void moveChildView(View pChildView, float pScale_Left, float pScale_Top) {
        ScalableLayout.LayoutParams lSLLP = getChildLayoutParams(pChildView);
//		log("moveChildView 1 lSLLP:"+lSLLP+" pChildView:"+pChildView);
        lSLLP.mScale_Left = pScale_Left;
        lSLLP.mScale_Top = pScale_Top;
//		log("moveChildView 2 lSLLP:"+lSLLP+" pChildView:"+pChildView);
//		log("moveChildView "+getStackTrace(Thread.currentThread().getStackTrace()));
        postInvalidate();
    }

    /**
     * move and resize childView inside ScalableLayout
     * @param pChildView view to move. should be child of ScaleLayout
     * @param pScale_Left
     * @param pScale_Top
     * @param pScale_Width
     * @param pScale_Height
     */
    public void moveChildView(View pChildView, float pScale_Left, float pScale_Top, float pScale_Width, float pScale_Height) {
        ScalableLayout.LayoutParams lSLLP = getChildLayoutParams(pChildView);
        lSLLP.mScale_Left = pScale_Left;
        lSLLP.mScale_Top = pScale_Top;
        lSLLP.mScale_Width = pScale_Width;
        lSLLP.mScale_Height = pScale_Height;
        postInvalidate();
    }


    @Override
    protected void onMeasure(int pWidthMeasureSpec, int pHeightMeasureSpec) {
//		log("onMeasure ================ Start "+this.toString());

//		float 	lRoot_Width 					= 0;
        float 	lRoot_Width_Max 				= 0;
        int 	lRoot_MeasureSpec_WidthMode 	= MeasureSpec.getMode(pWidthMeasureSpec);

//		float 	lRoot_Height 					= 0;
        float 	lRoot_Height_Max 				= 0;
        int 	lRoot_MeasureSpec_HeightMode 	= MeasureSpec.getMode(pHeightMeasureSpec);

        // get condition of ScalableLayout
        // ScalableLayout이 지켜야 되는 조건들을 확인함
        {
            int lRoot_MeasureSpec_Width 	= MeasureSpec.getSize(pWidthMeasureSpec);
            int lRoot_MeasureSpec_Height 	= MeasureSpec.getSize(pHeightMeasureSpec);
            switch (lRoot_MeasureSpec_WidthMode) {
                case MeasureSpec.EXACTLY:
//				log("  onMeasure Width  Exactly "+lRoot_Width+" = min("+mScale_Root_Width+", "+lRoot_MeasureSpec_Width+")");
                    lRoot_Width_Max 	= lRoot_MeasureSpec_Width;
//				log("  onMeasure Width  Exactly = "+lRoot_Width_Max);
//				lRoot_Width 		= lRoot_MeasureSpec_Width;
                    break;
                case MeasureSpec.AT_MOST:
                    if(lRoot_MeasureSpec_HeightMode == MeasureSpec.EXACTLY) {
//					lRoot_Width_Max 	= lRoot_MeasureSpec_Width;
                        lRoot_Width_Max 	= Float.MAX_VALUE;
                    } else {
                        lRoot_Width_Max 	= lRoot_MeasureSpec_Width;
                    }
//				lRoot_Width 		= lRoot_MeasureSpec_Width;
//				log("  onMeasure Width  AtMost  = "+lRoot_Width_Max);
                    break;
                default:
//				log("  onMeasure Width  Unspecified "+lRoot_Width_Max+" = "+mScale_Root_Width);
//				lRoot_Width 		= mScale_Root_Width;
//				lRoot_Width_Max 	= Float.MAX_VALUE;
//				lRoot_Width 		= lRoot_WidthSize;
//				lRoot_Width_Max 	= lRoot_WidthSize;
//				break;
//				throw new IllegalArgumentException(String.format("  onMeasure Width  Unspecified %08X", lRoot_MeasureSpec_Width));
                    lRoot_Width_Max 	= Float.MAX_VALUE;
//				throw new IllegalArgumentException(String.format("  onMeasure Width Unspecified %08X lRoot_MeasureSpec_WidthMode:%d lRoot_MeasureSpec_Width:%d",
//					lRoot_MeasureSpec_Width, lRoot_MeasureSpec_WidthMode, lRoot_MeasureSpec_Width));
            }

            switch (lRoot_MeasureSpec_HeightMode) {
                case MeasureSpec.EXACTLY:
                    lRoot_Height_Max 	= lRoot_MeasureSpec_Height;
//				log("  onMeasure Height Exactly = "+lRoot_Height_Max);
                    break;
                case MeasureSpec.AT_MOST:
                    if(lRoot_MeasureSpec_WidthMode == MeasureSpec.EXACTLY) {
//					lRoot_Height_Max 	= lRoot_MeasureSpec_Height;
                        lRoot_Height_Max = Float.MAX_VALUE;
                    } else {
                        lRoot_Height_Max 	= lRoot_MeasureSpec_Height;
                    }
//				log("  onMeasure Height AtMost  = "+lRoot_Height_Max);
                    break;
                default:
//				lRoot_Height = lRoot_Width * mRatioOfWidthHeight;
////			log("  onMeasure Height Unspecified "+lRootHeight+" = "+lRootWidth+"*"+mRatioOfWidthHeight);
//				break;
                    lRoot_Height_Max = Float.MAX_VALUE;
//				throw new IllegalArgumentException(String.format("  onMeasure Height Unspecified %08X lRoot_MeasureSpec_HeightMode:%d lRoot_MeasureSpec_Height:%d",
//					lRoot_MeasureSpec_Height, lRoot_MeasureSpec_HeightMode, lRoot_MeasureSpec_Height));
            }
        }

//		log(String.format("onMeasure 1 lScale:%5.3f ChildCount:%d Root:(%5.3f, %5.3f)", (lRootWidth/mScale_Full_Width), getChildCount(), lRootWidth, lRootHeight));
//		log(String.format("onMeasure 1 lScale:%5.3f ChildCount:%d", (lRootWidth/mScale_Full_Width), getChildCount()));

        float 	lRoot_Width 					= 0;
        float 	lRoot_Height 					= 0;
        for(int tryi = 0 ; tryi < 3 ; tryi++) {
            float lScale_Ratio_Pre = Math.min(lRoot_Width_Max / mScale_Root_Width, lRoot_Height_Max / mScale_Root_Height);
//			log(String.format("onMeasure 1 lScale_Ratio:%5.3f Scale_Ratio:(%5.3f, %5.3f) Scale:(%5.3f, %5.3f)",
//				lScale_Ratio_Pre, (lRoot_Width_Max/mScale_Root_Width), (lRoot_Height_Max / mScale_Root_Height), mScale_Root_Width, mScale_Root_Height));

            //////////////////////// Text Measure
            // TODO english
            // ScalableLayout내의 TextView중에 TextView_WrapContent_Direction 세팅되어 있을 경우 ScalableLayout의 Scale Size와 이웃한 View들의 Scalable LayoutParams를 재계산
            for (int i=0;i<getChildCount();i++) {
                View lView = getChildAt(i);

                if(lView instanceof TextView) {
                    updateTextViewSize((TextView)lView, lScale_Ratio_Pre);
                }
            }



            // calculate final size of ScalableLayout
            // ScalableLayout의 최종적인 크기를 결정지음
            float lScale_Ratio_Post = Math.min(lRoot_Width_Max / mScale_Root_Width, lRoot_Height_Max / mScale_Root_Height);

            {
                lRoot_Width 	= mScale_Root_Width * lScale_Ratio_Post;
                lRoot_Height 	= mScale_Root_Height * lScale_Ratio_Post;
            }

            {


                // 최종적으로 배치 위치를 세팅하기 위해 각 자식 뷰들의 layoutparams를 업데이트함
                // update layoutparams of each child view to arrange finally
                float lTopMarginFromWeight = (lRoot_Height - (lRoot_Width * mRatioOfWidthHeight))/4;
//				log("  onMeasure ("+lRoot_Width+","+lRoot_Height+") Ratio:"+mRatioOfWidthHeight+" lScale_TextViewMeasure_Post:"+lScale_Ratio_Post+" lTopMarginFromWeight:"+lTopMarginFromWeight);
                for (int i=0;i<getChildCount();i++) {
                    View lView = getChildAt(i);

                    ScalableLayout.LayoutParams lParams = getChildLayoutParams(lView);

//					log("  onMeasure lSLLP:"+lParams+"\n");
                    boolean dif = false;
//					log("  onMeasure bef v:"+lView+" pos:("+lParams.leftMargin+","+lParams.topMargin+") size:("+lParams.width+","+lParams.height+") dif:"+dif);

                    int lParams_Left = Math.round(lScale_Ratio_Post * lParams.mScale_Left);
//					log("  onMeasure Left: "+lParams.leftMargin+" "+lParams_Left+" "+lScale_Ratio_Post+" "+lParams.mScale_Left);
                    if(lParams.leftMargin != lParams_Left)
                        dif = true;
                    lParams.leftMargin = lParams_Left;

                    int lParams_Top = Math.round(lScale_Ratio_Post * lParams.mScale_Top + lTopMarginFromWeight);
                    if(lParams.topMargin != lParams_Top)
                        dif = true;
                    lParams.topMargin = lParams_Top;

                    int lParams_Width = Math.round(lScale_Ratio_Post * (lParams.mScale_Left + lParams.mScale_Width)) - lParams.leftMargin;
                    if(lParams.width != lParams_Width)
                        dif = true;
                    lParams.width = lParams_Width;

                    int lParams_Height = Math.round(lScale_Ratio_Post * (lParams.mScale_Top + lParams.mScale_Height)) - lParams.topMargin;
                    if(lParams.height != lParams_Height)
                        dif = true;
                    lParams.height = lParams_Height;

//					log("  onMeasure aft v:"+lView+" pos:("+lParams.leftMargin+","+lParams.topMargin+") size:("+lParams.width+","+lParams.height+") dif:"+dif);

//				if(lParams.mScale_TextSize != -1 && lParams.mTextView_WrapContent_Direction == TextView_WrapContent_Direction.None) {
                    if(lParams.mScale_TextSize != -1) {
                        if(lView instanceof TextView) {
                            TextView v = (TextView) lView;
//						if(v.getTextSize() != lParams.mScale_TextSize * lScale_Ratio_Post) {
                            if(isDifferentSufficiently(lParams.mScale_TextSize * lScale_Ratio_Post, v.getTextSize())) {
                                v.setTextSize(TypedValue.COMPLEX_UNIT_PX, lParams.mScale_TextSize * lScale_Ratio_Post);
                            }
                        }
                    }

                    if(dif) {
                        lView.setLayoutParams(lParams);
                    }
                }
            }
//			log(String.format("onMeasure end lScale_Ratio:%5.3f->%5.3f Root:(%5.3f,%5.3f) Scale_Root:(%5.3f,%5.3f) ",
//				lScale_Ratio_Pre, lScale_Ratio_Post,
//				lRoot_Width, lRoot_Height,
//				mScale_Root_Width, mScale_Root_Height));
            if(isDifferentSufficiently(lScale_Ratio_Post, lScale_Ratio_Pre, 1.01f) == false) {
                break;
            }
        }

//		log("onMeasure ================ End   "+this.toString());
        super.onMeasure(MeasureSpec.makeMeasureSpec(Math.round(lRoot_Width), lRoot_MeasureSpec_WidthMode), MeasureSpec.makeMeasureSpec(Math.round(lRoot_Height), lRoot_MeasureSpec_HeightMode));
        setMeasuredDimension(Math.round(lRoot_Width), Math.round(lRoot_Height));
    }


    private boolean isDifferentSufficiently(float pNew, float pOld) {
        return isDifferentSufficiently(pNew, pOld, 1.1f);
    }
    private boolean isDifferentSufficiently(float pNew, float pOld, float pDiffDelta) {
        if( pNew < pOld / pDiffDelta || pOld * pDiffDelta < pNew ) {
            return true;
        }
        return false;
    }

    private enum ViewPosition {
        Top,
        Bottom,
        Left,
        Right,
        Surrounded,
        Nothing
    }
    private ViewPosition getViewPosition(LayoutParams pSrc, LayoutParams pDst) {
        // 위에 있는 경우
        if(pSrc.getScale_Top() >= pDst.getScale_Bottom() &&
                (
                        ( pSrc.getScale_Left() <= pDst.getScale_Left() && pDst.getScale_Left() <= pSrc.getScale_Right())
                                ||
                                ( pSrc.getScale_Left() <= pDst.getScale_Right() && pDst.getScale_Right() <= pSrc.getScale_Right())
                                ||
                                ( pDst.getScale_Left() <= pSrc.getScale_Left() && pSrc.getScale_Right() <= pDst.getScale_Right())
                )) {
            return ViewPosition.Top;
        }
        // 아래에 있는 경우
        else if(pSrc.getScale_Bottom() <= pDst.getScale_Top() &&
                (
                        ( pSrc.getScale_Left() <= pDst.getScale_Left() && pDst.getScale_Left() <= pSrc.getScale_Right())
                                ||
                                ( pSrc.getScale_Left() <= pDst.getScale_Right() && pDst.getScale_Right() <= pSrc.getScale_Right())
                                ||
                                ( pDst.getScale_Left() <= pSrc.getScale_Left() && pSrc.getScale_Right() <= pDst.getScale_Right())
                )) {
            return ViewPosition.Bottom;
        }
        // 왼쪽에 있는 경우
        else if(pSrc.getScale_Left() >= pDst.getScale_Right() &&
                (
                        ( pSrc.getScale_Top() <= pDst.getScale_Top() && pDst.getScale_Top() <= pSrc.getScale_Bottom())
                                ||
                                ( pSrc.getScale_Top() <= pDst.getScale_Bottom() && pDst.getScale_Bottom() <= pSrc.getScale_Bottom())
                                ||
                                ( pSrc.getScale_Top() >= pDst.getScale_Top() &&  pDst.getScale_Bottom() >= pSrc.getScale_Bottom())
                )) {
            return ViewPosition.Left;
        }
        // 오른쪽에 있는 경우
        else if(pSrc.getScale_Right() <= pDst.getScale_Left() &&
                (
                        ( pSrc.getScale_Top() <= pDst.getScale_Top() && pDst.getScale_Top() <= pSrc.getScale_Bottom())
                                ||
                                ( pSrc.getScale_Top() <= pDst.getScale_Bottom() && pDst.getScale_Bottom() <= pSrc.getScale_Bottom())
                                ||
                                ( pSrc.getScale_Top() >= pDst.getScale_Top() &&  pDst.getScale_Bottom() >= pSrc.getScale_Bottom())
                )) {
            return ViewPosition.Right;
        }
        // 감싸고 있는 경우
        else if(pDst.getScale_Top() <= pSrc.getScale_Top() &&
                pDst.getScale_Left() <= pSrc.getScale_Left() &&
                pDst.getScale_Right() >= pSrc.getScale_Right() &&
                pDst.getScale_Bottom() >= pSrc.getScale_Bottom()) {
            return ViewPosition.Surrounded;
        }

        return ViewPosition.Nothing;
    }

    private void updateTextViewSize(TextView pTV_Text, float pScale_TextViewMeasure_Pre) {
        refreshTextChangedListener(pTV_Text);

        ScalableLayout.LayoutParams lTV_SLLP = getChildLayoutParams(pTV_Text);
        TextView_WrapContent_Direction lTextView_WrapContent_Direction = lTV_SLLP.mTextView_WrapContent_Direction;
        if(lTextView_WrapContent_Direction == TextView_WrapContent_Direction.None) {
            return;
        }

        float lTextView_ScaleWidth_Old 		= lTV_SLLP.getScale_Width();
        float lTextView_ScaleHeight_Old 	= lTV_SLLP.getScale_Height();

        float lTextView_ScaleWidth_New 		= lTextView_ScaleWidth_Old;
        float lTextView_ScaleHeight_New 	= lTextView_ScaleHeight_Old;
        {
            // get new scale size
            // 새로운 Scale 크기를 가져옴
            float lTextSize_New = lTV_SLLP.mScale_TextSize * pScale_TextViewMeasure_Pre;
//			log("updateTextViewHeight 1.1 lTextView_ScaleHeight_Old:"+lTextView_ScaleHeight_Old+" getScaleHeight():"+getScaleHeight()+" pScale_TextViewMeasure_Pre:"+pScale_TextViewMeasure_Pre);
//			log("updateTextViewHeight 1.2 lTextSize_New:"+lTextSize_New+" pTV_Text.getTextSize():"+pTV_Text.getTextSize());
            if(isDifferentSufficiently(lTextSize_New, pTV_Text.getTextSize())) {
//				log("updateTextViewHeight 1.2.1 TextSize:"+pTV_Text.getTextSize()+"->"+lTextSize_New);
                pTV_Text.setTextSize(TypedValue.COMPLEX_UNIT_PX, lTextSize_New);
            }

            switch (lTextView_WrapContent_Direction) {
                case Top:
                case Bottom:
                case Center_Vertical:
                {
                    int widthMeasureSpec 		= MeasureSpec.makeMeasureSpec((int) (lTextView_ScaleWidth_Old * pScale_TextViewMeasure_Pre), MeasureSpec.EXACTLY);
                    int heightMeasureSpec 		= MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
                    pTV_Text.measure(widthMeasureSpec, heightMeasureSpec);

                    float lTextView_Height_New 	= pTV_Text.getMeasuredHeight() * 1.01f;
                    lTextView_ScaleHeight_New 	= lTextView_Height_New / pScale_TextViewMeasure_Pre;
                } break;
                case Left:
                case Right:
                case Center_Horizontal:
                {
                    int widthMeasureSpec 		= MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
                    int heightMeasureSpec 		= MeasureSpec.makeMeasureSpec((int) (lTextView_ScaleHeight_Old * pScale_TextViewMeasure_Pre), MeasureSpec.EXACTLY);
                    pTV_Text.measure(widthMeasureSpec, heightMeasureSpec);

                    float lTextView_Width_New 	= pTV_Text.getMeasuredWidth() * 1.01f;
                    lTextView_ScaleWidth_New 	= lTextView_Width_New / pScale_TextViewMeasure_Pre;
//				log("updateTextViewWidth 1.3 lTextView_NewWidth:"+lTextView_Width_New+" lTextView_ScaleWidth_New:"+lTextView_ScaleWidth_New);
                } break;
                default:
                    break;
            }
        }
//		log("updateTextViewWidth 1.4 " +
//			" lTextView_ScaleWidth_New:"+lTextView_ScaleWidth_New+" lTextView_ScaleWidth_Old:"+lTextView_ScaleWidth_Old+
//			" lTextView_ScaleHeight_New:"+lTextView_ScaleHeight_New+" lTextView_ScaleHeight_Old:"+lTextView_ScaleHeight_Old);

        if(
                isDifferentSufficiently(lTextView_ScaleWidth_New, lTextView_ScaleWidth_Old) == false &&
                isDifferentSufficiently(lTextView_ScaleHeight_New, lTextView_ScaleHeight_Old) == false) {
            return;
        }

        float lTextView_ScaleWidth_Diff 	= lTextView_ScaleWidth_New - lTextView_ScaleWidth_Old;
        float lTextView_ScaleHeight_Diff 	= lTextView_ScaleHeight_New - lTextView_ScaleHeight_Old;
        if(lTV_SLLP.mTextView_WrapContent_MoveSiblings == true) {
            for(int i = 0; i < getChildCount(); i++) {
                View v = getChildAt(i);
                if(v == pTV_Text) {
                    continue;
                }
                ScalableLayout.LayoutParams lSLLP = getChildLayoutParams(v);
                ViewPosition position = getViewPosition(lTV_SLLP, lSLLP);

                switch (lTextView_WrapContent_Direction) {
                    case Top: {
                        if(lTV_SLLP.mTextView_WrapContent_ResizeSurrounded) {
                            switch (position) {
                                case Top: {
                                } break;
                                case Surrounded: {
                                    moveChildView(v,
                                            lSLLP.getScale_Left(), lSLLP.getScale_Top(),
                                            lSLLP.getScale_Width(), lSLLP.getScale_Height()+lTextView_ScaleHeight_Diff);
                                } break;
                                default: {
                                    moveChildView(v, lSLLP.getScale_Left(), lSLLP.getScale_Top()+lTextView_ScaleHeight_Diff);
                                } break;
                            }
                        } else {
                            switch (position) {
                                case Top: {
                                    moveChildView(v, lSLLP.getScale_Left(), lSLLP.getScale_Top()-lTextView_ScaleHeight_Diff);
                                } break;
                            }
                        }
                    } break;
                    case Bottom: {
                        if(lTV_SLLP.mTextView_WrapContent_ResizeSurrounded) {
                           switch (position) {
                               case Bottom: {
                                   moveChildView(v, lSLLP.getScale_Left(), lSLLP.getScale_Top() + lTextView_ScaleHeight_Diff);
                               } break;
                               case Surrounded: {
                                   moveChildView(v,
                                            lSLLP.getScale_Left(), lSLLP.getScale_Top(),
                                            lSLLP.getScale_Width(), lSLLP.getScale_Height() + lTextView_ScaleHeight_Diff);
                               } break;
                           }
                        } else {
                            switch (position) {
                                case Bottom: {
                                    moveChildView(v, lSLLP.getScale_Left(), lSLLP.getScale_Top() + lTextView_ScaleHeight_Diff);
                                } break;
                            } break;
                        }
                    } break;
                    case Center_Vertical: {
                        if(lTV_SLLP.mTextView_WrapContent_ResizeSurrounded) {
                            switch (position) {
                                case Top: {
                                } break;
                                case Bottom: {
                                    moveChildView(v, lSLLP.getScale_Left(), lSLLP.getScale_Top() + lTextView_ScaleHeight_Diff);
                                } break;
                                case Surrounded: {
                                    moveChildView(v, lSLLP.getScale_Left(), lSLLP.getScale_Top(), lSLLP.getScale_Width(), lSLLP.getScale_Height()+lTextView_ScaleHeight_Diff);
                                } break;
                                default: {
                                    moveChildView(v, lSLLP.getScale_Left(), lSLLP.getScale_Top() + lTextView_ScaleHeight_Diff / 2, lSLLP.getScale_Width(), lSLLP.getScale_Height());
                                } break;
                            }
                        } else {
                            switch (position) {
                                case Top: {
                                    moveChildView(v, lSLLP.getScale_Left(), lSLLP.getScale_Top() - lTextView_ScaleHeight_Diff / 2);
                                } break;
                                case Bottom: {
                                    moveChildView(v, lSLLP.getScale_Left(), lSLLP.getScale_Top() + lTextView_ScaleHeight_Diff / 2);
                                } break;
                            }
                        }
                    } break;
                    case Left: {
                        if(lTV_SLLP.mTextView_WrapContent_ResizeSurrounded) {
                            switch (position) {
                                case Left: {
                                } break;
                                case Surrounded: {
                                    moveChildView(v, lSLLP.getScale_Left(), lSLLP.getScale_Top(), lSLLP.getScale_Width()+lTextView_ScaleWidth_Diff, lSLLP.getScale_Height());
                                } break;
                                default: {
                                    moveChildView(v, lSLLP.getScale_Left()+lTextView_ScaleWidth_Diff, lSLLP.getScale_Top());
                                } break;
                            }
                        } else {
                            switch (position) {
                                case Left: {
                                    moveChildView(v, lSLLP.getScale_Left()-lTextView_ScaleWidth_Diff, lSLLP.getScale_Top());
                                } break;
                            }
                        }
                    } break;
                    case Right: {
                        if(lTV_SLLP.mTextView_WrapContent_ResizeSurrounded) {
                            switch (position) {
                                case Right: {
                                    moveChildView(v, lSLLP.getScale_Left()+lTextView_ScaleWidth_Diff, lSLLP.getScale_Top());
                                } break;
                                case Surrounded: {
                                    moveChildView(v, lSLLP.getScale_Left(), lSLLP.getScale_Top(), lSLLP.getScale_Width()+lTextView_ScaleWidth_Diff, lSLLP.getScale_Height());
                                } break;
                            }
                        } else {
                            switch (position) {
                                case Right: {
                                    moveChildView(v, lSLLP.getScale_Left()+lTextView_ScaleWidth_Diff, lSLLP.getScale_Top());
                                } break;
                            }
                        }
                    } break;
                    case Center_Horizontal: {
                        if(lTV_SLLP.mTextView_WrapContent_ResizeSurrounded) {
                            switch (position) {
                                case Left: {
                                } break;
                                case Right: {
                                    moveChildView(v, lSLLP.getScale_Left() + lTextView_ScaleWidth_Diff, lSLLP.getScale_Top());
                                } break;
                                case Surrounded: {
                                    moveChildView(v, lSLLP.getScale_Left(), lSLLP.getScale_Top(), lSLLP.getScale_Width() + lTextView_ScaleWidth_Diff, lSLLP.getScale_Height());
                                } break;
                                default: {
                                    moveChildView(v, lSLLP.getScale_Left() + lTextView_ScaleWidth_Diff / 2, lSLLP.getScale_Top(), lSLLP.getScale_Width(), lSLLP.getScale_Height());
                                } break;
                            }
                        } else {
                            switch (position) {
                                case Left: {
                                    moveChildView(v, lSLLP.getScale_Left() - lTextView_ScaleWidth_Diff / 2, lSLLP.getScale_Top());
                                } break;
                                case Right: {
                                    moveChildView(v, lSLLP.getScale_Left() + lTextView_ScaleWidth_Diff / 2, lSLLP.getScale_Top());
                                } break;
                            }
                        }
                    } break;
                    default: {
                    } break;
                }
            }
        } else {
            for(int i = 0; i < getChildCount(); i++) {
                View v = getChildAt(i);
                if(v == pTV_Text) {
                    continue;
                }
                ScalableLayout.LayoutParams lSLLP = getChildLayoutParams(v);
                ViewPosition position = getViewPosition(lTV_SLLP, lSLLP);

                switch (lTextView_WrapContent_Direction) {
                    case Top: {
                        if(lTV_SLLP.mTextView_WrapContent_ResizeSurrounded) {
                            switch (position) {
                                case Surrounded: {
                                    moveChildView(v,
                                            lSLLP.getScale_Left(), lSLLP.getScale_Top(),
                                            lSLLP.getScale_Width(), lSLLP.getScale_Height()+lTextView_ScaleHeight_Diff);
                                } break;
                                default: {
                                    moveChildView(v, lSLLP.getScale_Left(), lSLLP.getScale_Top()+lTextView_ScaleHeight_Diff);
                                } break;
                            }
                        } else {
                        }
                    } break;
                    case Bottom: {
                        if(lTV_SLLP.mTextView_WrapContent_ResizeSurrounded) {
                            switch (position) {
                                case Surrounded: {
                                    moveChildView(v,
                                            lSLLP.getScale_Left(), lSLLP.getScale_Top(),
                                            lSLLP.getScale_Width(), lSLLP.getScale_Height() + lTextView_ScaleHeight_Diff);
                                } break;
                            }
                        } else {
                        }
                    } break;
                    case Center_Vertical: {
                        if(lTV_SLLP.mTextView_WrapContent_ResizeSurrounded) {
                            switch (position) {
                                case Surrounded: {
                                    moveChildView(v, lSLLP.getScale_Left(), lSLLP.getScale_Top(), lSLLP.getScale_Width(), lSLLP.getScale_Height()+lTextView_ScaleHeight_Diff);
                                } break;
                                default: {
                                    moveChildView(v, lSLLP.getScale_Left(), lSLLP.getScale_Top() + lTextView_ScaleHeight_Diff / 2, lSLLP.getScale_Width(), lSLLP.getScale_Height());
                                } break;
                            }
                        } else {
                        }
                    } break;
                    case Left: {
                        if(lTV_SLLP.mTextView_WrapContent_ResizeSurrounded) {
                            switch (position) {
                                case Surrounded: {
                                    moveChildView(v, lSLLP.getScale_Left(), lSLLP.getScale_Top(), lSLLP.getScale_Width()+lTextView_ScaleWidth_Diff, lSLLP.getScale_Height());
                                } break;
                                default: {
                                    moveChildView(v, lSLLP.getScale_Left()+lTextView_ScaleWidth_Diff, lSLLP.getScale_Top());
                                } break;
                            }
                        } else {
                        }
                    } break;
                    case Right: {
                        if(lTV_SLLP.mTextView_WrapContent_ResizeSurrounded) {
                            switch (position) {
                                case Surrounded: {
                                    moveChildView(v, lSLLP.getScale_Left(), lSLLP.getScale_Top(), lSLLP.getScale_Width()+lTextView_ScaleWidth_Diff, lSLLP.getScale_Height());
                                } break;
                            }
                        } else {
                        }
                    } break;
                    case Center_Horizontal: {
                        if(lTV_SLLP.mTextView_WrapContent_ResizeSurrounded) {
                            switch (position) {
                                case Surrounded: {
                                    moveChildView(v, lSLLP.getScale_Left(), lSLLP.getScale_Top(), lSLLP.getScale_Width() + lTextView_ScaleWidth_Diff, lSLLP.getScale_Height());
                                } break;
                                default: {
                                    moveChildView(v, lSLLP.getScale_Left() + lTextView_ScaleWidth_Diff / 2, lSLLP.getScale_Top(), lSLLP.getScale_Width(), lSLLP.getScale_Height());
                                } break;
                            }
                        } else {
                        }
                    } break;
                    default: {
                    } break;
                }
            }
        }

        switch (lTextView_WrapContent_Direction) {
            case Top: {
                if(lTV_SLLP.mTextView_WrapContent_ResizeSurrounded) {
                    moveChildView(pTV_Text, lTV_SLLP.getScale_Left(), lTV_SLLP.getScale_Top(), lTV_SLLP.getScale_Width(), lTextView_ScaleHeight_New);
                } else {
                    moveChildView(pTV_Text, lTV_SLLP.getScale_Left(), lTV_SLLP.getScale_Top() - lTextView_ScaleHeight_Diff, lTV_SLLP.getScale_Width(), lTextView_ScaleHeight_New);
                }
            } break;
            case Bottom: {
                moveChildView(pTV_Text, lTV_SLLP.getScale_Left(), lTV_SLLP.getScale_Top(), lTV_SLLP.getScale_Width(), lTextView_ScaleHeight_New);
            } break;
            case Center_Vertical: {
                if(lTV_SLLP.mTextView_WrapContent_ResizeSurrounded) {
                    moveChildView(pTV_Text, lTV_SLLP.getScale_Left(), lTV_SLLP.getScale_Top(), lTV_SLLP.getScale_Width(), lTextView_ScaleHeight_New);
                } else {
                    moveChildView(pTV_Text, lTV_SLLP.getScale_Left(), lTV_SLLP.getScale_Top() - lTextView_ScaleHeight_Diff / 2, lTV_SLLP.getScale_Width(), lTextView_ScaleHeight_New);
                }
            } break;
            case Left: {
                if(lTV_SLLP.mTextView_WrapContent_ResizeSurrounded) {
                    moveChildView(pTV_Text, lTV_SLLP.getScale_Left(), lTV_SLLP.getScale_Top(), lTextView_ScaleWidth_New, lTV_SLLP.getScale_Height());
                } else {
                    moveChildView(pTV_Text, lTV_SLLP.getScale_Left() - lTextView_ScaleWidth_Diff, lTV_SLLP.getScale_Top(), lTextView_ScaleWidth_New, lTV_SLLP.getScale_Height());
                }
            } break;
            case Right: {
                moveChildView(pTV_Text, lTV_SLLP.getScale_Left(), lTV_SLLP.getScale_Top(), lTextView_ScaleWidth_New, lTV_SLLP.getScale_Height());
            } break;
            case Center_Horizontal: {
                if(lTV_SLLP.mTextView_WrapContent_ResizeSurrounded) {
                    moveChildView(pTV_Text, lTV_SLLP.getScale_Left(), lTV_SLLP.getScale_Top(), lTextView_ScaleWidth_New, lTV_SLLP.getScale_Height());
                } else {
                    moveChildView(pTV_Text, lTV_SLLP.getScale_Left() - lTextView_ScaleWidth_Diff / 2, lTV_SLLP.getScale_Top(), lTextView_ScaleWidth_New, lTV_SLLP.getScale_Height());
                }
            } break;
            default:
                break;
        }

        if(lTV_SLLP.mTextView_WrapContent_ResizeSurrounded) {
//			log("setScaleSize From:"+getScaleHeight()+" To:"+(getScaleHeight()+lTextView_ScaleHeight_Diff)+" pScale_TextViewMeasure_Pre:"+pScale_TextViewMeasure_Pre);
            setScaleSize(getScaleWidth()+lTextView_ScaleWidth_Diff, getScaleHeight()+lTextView_ScaleHeight_Diff, false);
        }

//		log("updateTextViewHeight 4 lTextView_ScaleHeight_Old:"+lTextView_ScaleHeight_Old+" getScaleHeight():"+getScaleHeight()+" pScale_TextViewMeasure_Pre:"+pScale_TextViewMeasure_Pre);
//		log("updateTextViewHeight ret "+(pRootHeight+"*"+getScaleHeight()+"/"+lRootView_OldScaleHeight));
//		log("updateTextViewHeight ret "+(pRootHeight*getScaleHeight()/lRootView_OldScaleHeight));
//		return pRootHeight*getScaleHeight()/lRootView_OldScaleHeight;
    }




    public enum TextView_WrapContent_Direction {
        None, Left, Right, Center_Horizontal, Top, Bottom, Center_Vertical,
    }
    /**
     * ScalableLayout.LayoutParams
     */
    public static class LayoutParams extends FrameLayout.LayoutParams {
        public LayoutParams(Context pContext, AttributeSet pAttrs) { super(pContext, pAttrs); }
        public LayoutParams(
                float pScale_Left, float pScale_Top,
                float pScale_Width, float pScale_Height) {
            this(pScale_Left, pScale_Top, pScale_Width, pScale_Height, Default_Scale_TextSize, TextView_WrapContent_Direction.None, false, true);
        }
        private LayoutParams(
                float pScale_Left, float pScale_Top,
                float pScale_Width, float pScale_Height, float pScale_TextSize,
                TextView_WrapContent_Direction pTextView_WrapContent_Direction, boolean pTextView_WrapContent_ResizeSurrounded, boolean pTextView_WrapContent_MoveSiblings ) {
            super(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.LEFT | Gravity.TOP);

            setScale_Left(pScale_Left);
            setScale_Top(pScale_Top);
            setScale_Width(pScale_Width);
            setScale_Height(pScale_Height);

            setScale_TextSize(pScale_TextSize);

            setTextView_WrapContent(pTextView_WrapContent_Direction, pTextView_WrapContent_ResizeSurrounded, pTextView_WrapContent_MoveSiblings);
        }

        private LayoutParams(android.view.ViewGroup.LayoutParams pLayoutParams) {
            this(
                    Default_Scale_Left, Default_Scale_Top,
                    Default_Scale_Width, Default_Scale_Height,
                    Default_Scale_TextSize, TextView_WrapContent_Direction.None, false, true);

            width = pLayoutParams.width;
            height = pLayoutParams.height;
            layoutAnimationParameters = pLayoutParams.layoutAnimationParameters;
            gravity = Gravity.LEFT | Gravity.TOP;
        }

        @Override
        public String toString() {
            return String.format("%08x (%6.3f, %6.3f) (%6.3f, %6.3f)", hashCode(), getScale_Left(), getScale_Top(), getScale_Right(), getScale_Bottom());
        }
        private float mScale_Left = Default_Scale_Left;
        public float getScale_Left() { return mScale_Left; }
        public void setScale_Left(float pScale_Left) { mScale_Left = pScale_Left; }
        public float getScale_Right() { return getScale_Left()+getScale_Width(); }

        private float mScale_Top = Default_Scale_Top;
        public float getScale_Top() { return mScale_Top; }
        public void setScale_Top(float pScale_Top) { mScale_Top = pScale_Top; }
        public float getScale_Bottom() { return getScale_Top()+getScale_Height(); }

        private float mScale_Width = Default_Scale_Width;
        public float getScale_Width() { return mScale_Width; }
        public void setScale_Width(float pScale_Width) { mScale_Width = pScale_Width; }

        private float mScale_Height = Default_Scale_Height;
        public float getScale_Height() { return mScale_Height; }
        public void setScale_Height(float pScale_Height) { mScale_Height = pScale_Height; }

        private float mScale_TextSize = -1f;
        public float getScale_TextSize() { return mScale_TextSize; }
        public void setScale_TextSize(float pScale_TextSize) { mScale_TextSize = pScale_TextSize; }

        private TextView_WrapContent_Direction 		mTextView_WrapContent_Direction 		= TextView_WrapContent_Direction.None;
        private boolean 							mTextView_WrapContent_ResizeSurrounded 	= false;
        private boolean 							mTextView_WrapContent_MoveSiblings 		= true;
        public void setTextView_WrapContent(TextView_WrapContent_Direction pTextView_WrapContent_Direction, boolean pTextView_WrapContent_ResizeSurrounded, boolean pTextView_WrapContent_MoveSiblings) {
            mTextView_WrapContent_Direction 		= pTextView_WrapContent_Direction;
            mTextView_WrapContent_ResizeSurrounded 	= pTextView_WrapContent_ResizeSurrounded;
            mTextView_WrapContent_MoveSiblings 		= pTextView_WrapContent_MoveSiblings;
        }
    }










    //////////////// 편리하게 View를 추가하기 위한 함수들 //////////////
    /**
     * adds new TextView with (TextSize, Left, Top, Width, Height) parameters
     * DuplicateState is false by default.
     * @param pText
     * @param pScale_TextSize
     * @param pScale_Left
     * @param pScale_Top
     * @param pScale_Width
     * @param pScale_Height
     * @return created TextView instance
     */
    public TextView addNewTextView(String pText,
                                   float pScale_TextSize,
                                   float pScale_Left, float pScale_Top,
                                   float pScale_Width, float pScale_Height) {
        return addNewTextView(pText, pScale_TextSize, pScale_Left, pScale_Top, pScale_Width, pScale_Height, false);
    }

    /**
     * adds new TextView with (TextSize, Left, Top, Width, Height, duplicateState) parameters
     * @param pText
     * @param pScale_TextSize
     * @param pScale_Left
     * @param pScale_Top
     * @param pScale_Width
     * @param pScale_Height
     * @param pDuplicateState
     * @return created TextView instance
     */
    public TextView addNewTextView(String pText,
                                   float pScale_TextSize,
                                   float pScale_Left, float pScale_Top,
                                   float pScale_Width, float pScale_Height,
                                   boolean pDuplicateState) {
        TextView ret = new TextView(getContext());
        addView(ret, pScale_Left, pScale_Top, pScale_Width, pScale_Height);

        setScale_TextSize(ret, pScale_TextSize);
        ret.setText(pText);
        ret.setGravity(Gravity.CENTER);
        ret.setTextColor(Color.BLACK);
        ret.setDuplicateParentStateEnabled(pDuplicateState);

        return ret;
    }

    /**
     * adds new EditText with (TextSize, Left, Top, Width, Height) parameters
     * @param pScale_TextSize
     * @param pScale_Left
     * @param pScale_Top
     * @param pScale_Width
     * @param pScale_Height
     * @return created EditText instance
     */
    public EditText addNewEditText(float pScale_TextSize, float pScale_Left, float pScale_Top, float pScale_Width, float pScale_Height) {
        EditText ret = new EditText(getContext());
        addView(ret, pScale_Left, pScale_Top, pScale_Width, pScale_Height);

        setScale_TextSize(ret, pScale_TextSize);
        ret.setGravity(Gravity.CENTER);
        ret.setTextColor(Color.BLACK);
        return ret;
    }

    /**
     * adds new ImageView with (Left, Top, Width, Height) parameters
     * Drawable is null by default.
     * DuplicateState is false by default.
     * @param pScale_Left
     * @param pScale_Top
     * @param pScale_Width
     * @param pScale_Height
     * @return created ImageView instance
     */
    public ImageView addNewImageView(float pScale_Left, float pScale_Top, float pScale_Width, float pScale_Height) {
        return addNewImageView((Drawable)null, pScale_Left, pScale_Top, pScale_Width, pScale_Height, false);
    }

    /**
     * adds new ImageView with (Bitmap, Left, Top, Width, Height) parameters
     * sets Image of ImageView with Bitmap parameter
     * DuplicateState is false by default.
     * @param pBitmap
     * @param pScale_Left
     * @param pScale_Top
     * @param pScale_Width
     * @param pScale_Height
     * @return created ImageView instance
     */
    public ImageView addNewImageView(Bitmap pBitmap, float pScale_Left, float pScale_Top, float pScale_Width, float pScale_Height) {
        BitmapDrawable bm = new BitmapDrawable(getResources(), pBitmap);
        return addNewImageView(bm, pScale_Left, pScale_Top, pScale_Width, pScale_Height, false);
    }
    /**
     * adds new ImageView with (ResourceID, Left, Top, Width, Height) parameters
     * sets Image of ImageView with ResourceID parameter
     * DuplicateState is false by default.
     * @param pResID
     * @param pScale_Left
     * @param pScale_Top
     * @param pScale_Width
     * @param pScale_Height
     * @return created ImageView instance
     */
    public ImageView addNewImageView(int pResID, float pScale_Left, float pScale_Top, float pScale_Width, float pScale_Height) {
        return addNewImageView(pResID, pScale_Left, pScale_Top, pScale_Width, pScale_Height, false);
    }
    /**
     * adds new ImageView with (ResourceID, Left, Top, Width, Height, DuplicateState) parameters
     * sets Image of ImageView with ResourceID parameter
     * @param pResID
     * @param pScale_Left
     * @param pScale_Top
     * @param pScale_Width
     * @param pScale_Height
     * @param pDuplicateState
     * @return created ImageView instance
     */
    public ImageView addNewImageView(int pResID, float pScale_Left, float pScale_Top, float pScale_Width, float pScale_Height, boolean pDuplicateState) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return addNewImageView(
                new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), pResID, options)),
                pScale_Left, pScale_Top, pScale_Width, pScale_Height, pDuplicateState);
    }
    /**
     * adds new ImageView with (Drawable, Left, Top, Width, Height) parameters
     * sets Image of ImageView with Drawable parameter
     * DuplicateState is false by default.
     * @param drawable
     * @param pScale_Left
     * @param pScale_Top
     * @param pScale_Width
     * @param pScale_Height
     * @return created ImageView instance
     */
    public ImageView addNewImageView(Drawable drawable, float pScale_Left, float pScale_Top, float pScale_Width, float pScale_Height) {
        return addNewImageView(drawable, pScale_Left, pScale_Top, pScale_Width, pScale_Height, false);
    }
    /**
     * adds new ImageView with (Drawable, Left, Top, Width, Height, DuplicateState) parameters
     * sets Image of ImageView with Drawable parameter
     * @param drawable
     * @param pScale_Left
     * @param pScale_Top
     * @param pScale_Width
     * @param pScale_Height
     * @param pDuplicateState
     * @return
     */
    public ImageView addNewImageView(Drawable drawable, float pScale_Left, float pScale_Top, float pScale_Width, float pScale_Height, boolean pDuplicateState) {
        ImageView lNewImageView = new ImageView(getContext());
        lNewImageView.setImageDrawable(drawable);
        lNewImageView.setScaleType(ScaleType.FIT_XY);
        lNewImageView.setDuplicateParentStateEnabled(pDuplicateState);

        addView(lNewImageView, pScale_Left, pScale_Top, pScale_Width, pScale_Height);
        return lNewImageView;
    }




















    ////////////////////////// Log
    @Override
    public String toString() {
        return String.format("{ScalableLayout:%08x}", this.hashCode());
    }
    void log(String pLog) {
        if(sLogTag_Global != null) {
            Log.e(sLogTag_Global, this+"] "+pLog);
        }
        if(mLogTag_This != null) {
            Log.e(mLogTag_This, this+"] "+pLog);
        }
    }


    private String mLogTag_This = null;
    public String getLogTag_This() {
        return mLogTag_This;
    }
    /**
     * setLoggable("ScalableLayout");
     */
    public void setThisLoggable() {
        setThisLoggable("ScalableLayout");
    }
    /**
     * Log를 출력할수 있게함
     * @param pLogTag DDMS Log Tag를 지정
     */
    public void setThisLoggable(String pLogTag) {
        mLogTag_This = pLogTag;
    }

    private static String getStackTrace(StackTraceElement[] ste) {
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<ste.length;i++){
            sb.append("\t"+ste[i].toString()+"\n");
        }
        return sb.toString();
    }
//	private static String getStackTrace(Throwable e) {
//		StringBuilder sb = new StringBuilder();
//		printStackTrace(sb, e);
//		while(e.getCause() != null){
//			e = e.getCause();
//			printStackTrace(sb, e);
//		}
//		return sb.toString();
//	}
//	private static void printStackTrace(StringBuilder sb, Throwable e) {
//		sb.append("Caused by: "+e.getClass().getName()+", "+e.getMessage()+"\n");
//		StackTraceElement[] ste = e.getStackTrace();
//		for(int i=0;i<ste.length;i++){
//			sb.append("\t"+ste[i].toString()+"\n");
//		}
//	}



    private static String sLogTag_Global = null;
    /**
     * setLoggable("ScalableLayout");
     */
    public static void setLoggable() {
        setLoggable("ScalableLayout");
    }
    /**
     * Log를 출력할수 있게함
     * @param pLogTag DDMS Log Tag를 지정
     */
    public static void setLoggable(String pLogTag) {
        sLogTag_Global = pLogTag;
    }

}
