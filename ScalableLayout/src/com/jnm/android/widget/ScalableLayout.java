package com.jnm.android.widget;

import android.content.Context;
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
	
	private float mScale_Full_Width 		= Default_Scale_Base_Width;
	private float mScale_Full_Height 		= Default_Scale_Base_Height;
	private float mRatioOfWidthHeight 	= mScale_Full_Height / mScale_Full_Width;
	
	public float getScaleWidth() { return mScale_Full_Width; }
	public float getScaleHeight() { return mScale_Full_Height; }
	
	public void setScaleWidth(float pWidth) { setScaleSize(pWidth, mScale_Full_Height); }
	public void setScaleHeight(float pHeight) { setScaleSize(mScale_Full_Width, pHeight); }
	public void setScaleSize(float pWidth, float pHeight) {
		setScaleSize(pWidth, pHeight, true);
	}
	private void setScaleSize(float pWidth, float pHeight, boolean pWithInvalidate) {
		mScale_Full_Width = pWidth;
		mScale_Full_Height = pHeight;
		mRatioOfWidthHeight = mScale_Full_Height / mScale_Full_Width;
		if(pWithInvalidate) {
			postInvalidate();
		}
		
//		postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				requestLayout();
//				forceLayout();
//			}
//		}, 10);
	}
	
	public ScalableLayout(Context pContext) {
		this(pContext, Default_Scale_Base_Width, Default_Scale_Base_Height);
	}
	
	public ScalableLayout(Context pContext, AttributeSet pAttrs) {
		this(pContext, pAttrs, 
			pContext.obtainStyledAttributes(pAttrs, R.styleable.ScalableLayout).getFloat(R.styleable.ScalableLayout_scale_base_width, Default_Scale_Base_Width), 
			pContext.obtainStyledAttributes(pAttrs, R.styleable.ScalableLayout).getFloat(R.styleable.ScalableLayout_scale_base_height, Default_Scale_Base_Height) );
	}
	
	/**
	 * @param pContext
	 * @param pScale_Width Initial width of ScaleLayout.
	 * @param pScale_Height Initial height of ScaleLayout.
	 */
	public ScalableLayout(Context pContext, float pScale_Width, float pScale_Height) {
		this(pContext, null, pScale_Width, pScale_Height);
	}
	private ScalableLayout(Context pContext, AttributeSet pAttrs, float pScale_Width, float pScale_Height) {
		super(pContext, pAttrs);
		setScaleWidth(pScale_Width);
		setScaleHeight(pScale_Height);
	}
	
	public ScalableLayout.LayoutParams getChildLayoutParams(View pChild) {
		LayoutParams ret = (LayoutParams) pChild.getLayoutParams();
		if(ret == null) {
			ret = generateDefaultLayoutParams();
			pChild.setLayoutParams(ret);
		}
		return ret;
	}
	/**
	 * sets TextSize of TextView and EdiText to scale automatically
	 * @param pTextView
	 * @param pScale_TextSize
	 */
	public void setScale_TextSize(TextView pTextView, float pScale_TextSize) {
		getChildLayoutParams(pTextView).setScale_TextSize(pScale_TextSize);
	}
	public void setScale_TextViewWrapContentMode(TextView pTextView, TextViewWrapContentMode pMode, boolean pTotally) {
		pTextView.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence pS, int pStart, int pBefore, int pCount) {
				requestLayout();
				forceLayout();
			}
			@Override
			public void beforeTextChanged(CharSequence pS, int pStart, int pCount, int pAfter) { }
			@Override
			public void afterTextChanged(Editable pS) {
				requestLayout();
				forceLayout();
			}
		});
		getChildLayoutParams(pTextView).setScale_TextViewWrapContentMode(pMode, pTotally);
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
		log("generateLayoutParams AttributeSet: "+pAttrs);
		for(int i=0;i<pAttrs.getAttributeCount();i++) { 
			log("attr["+i+"] "+pAttrs.getAttributeName(i)+":"+pAttrs.getAttributeValue(i));
			
		}
		return new ScalableLayout.LayoutParams(
			 getContext().obtainStyledAttributes(pAttrs, R.styleable.ScalableLayout).getFloat(R.styleable.ScalableLayout_scale_left, Default_Scale_Left),
			 getContext().obtainStyledAttributes(pAttrs, R.styleable.ScalableLayout).getFloat(R.styleable.ScalableLayout_scale_top, Default_Scale_Top),
			 getContext().obtainStyledAttributes(pAttrs, R.styleable.ScalableLayout).getFloat(R.styleable.ScalableLayout_scale_width, Default_Scale_Width),
			 getContext().obtainStyledAttributes(pAttrs, R.styleable.ScalableLayout).getFloat(R.styleable.ScalableLayout_scale_height, Default_Scale_Height),
			 getContext().obtainStyledAttributes(pAttrs, R.styleable.ScalableLayout).getFloat(R.styleable.ScalableLayout_scale_textsize, Default_Scale_TextSize) );
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
		ScalableLayout.LayoutParams rect = getChildLayoutParams(pChildView);
		rect.mScale_Left = pScale_Left;
		rect.mScale_Top = pScale_Top;
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
		ScalableLayout.LayoutParams rect = getChildLayoutParams(pChildView);
		rect.mScale_Left = pScale_Left;
		rect.mScale_Top = pScale_Top;
		rect.mScale_Width = pScale_Width;
		rect.mScale_Height = pScale_Height;
		postInvalidate();
	}
	
	

//	private static int measureTextHeight(Context context, String text, int textSize, int deviceWidth) {
//		TextView textView = new TextView(context);
//		textView.setText(text);
//		textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
//		int widthMeasureSpec = MeasureSpec.makeMeasureSpec(deviceWidth, MeasureSpec.AT_MOST);
//		int heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
//		textView.measure(widthMeasureSpec, heightMeasureSpec);
//		return textView.getMeasuredHeight();
//	}
	private float updateTextViewWidth(TextView pTV_Text, ScalableLayout.LayoutParams pTV_SLLP, float pBGWidth, float pBGHeight) {
		float lOldViewScaleWidth = pTV_SLLP.getScale_Width();
		float lOldViewWidth = pTV_Text.getWidth();
		
		log("updateTextViewWidth lOldViewWidth:"+lOldViewWidth+" lOldViewScaleWidth:"+lOldViewScaleWidth+" pBGWidth:"+pBGWidth+" getScaleWidth:"+getScaleWidth());
		if(lOldViewWidth <= 0 || lOldViewScaleWidth <= 0 || pBGWidth <= 0 || getScaleWidth() <= 0) {
			getHandler().postDelayed(new Runnable() {
				@Override
				public void run() {
					requestLayout();
					forceLayout();
				}
			}, 10);
			return pBGWidth;
		}
		
		int widthMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		int heightMeasureSpec = MeasureSpec.makeMeasureSpec(pTV_Text.getHeight(), MeasureSpec.AT_MOST);
		pTV_Text.measure(widthMeasureSpec, heightMeasureSpec);
		float lNewViewWidth = pTV_Text.getMeasuredWidth();
		float lNewViewScaleWidth = (lNewViewWidth * getScaleWidth() / pBGWidth);
		
//		if(lOldScaleWidth != lNewScaleWidth) {
		float lOldScaleWidth = getScaleWidth();
		if(Math.abs(lNewViewWidth - lOldViewWidth) * 100 > pBGWidth && Math.abs(lNewViewScaleWidth - lOldViewScaleWidth) > getScaleWidth() / 100f) {
			for(int i=0;i<getChildCount();i++) {
				View v = getChildAt(i);
				if(v == pTV_Text) {
					continue;
				}
				ScalableLayout.LayoutParams lSLLP = (ScalableLayout.LayoutParams) v.getLayoutParams();
				// 오른쪽에 있는 뷰들 위치 이동
				if(
					lSLLP.getScale_Left() >= pTV_SLLP.getScale_Left()+lOldViewScaleWidth &&
					(
						( pTV_SLLP.getScale_Top() <= lSLLP.getScale_Top() && lSLLP.getScale_Top() <= pTV_SLLP.getScale_Top()+pTV_SLLP.getScale_Height() )
						|| 
						( pTV_SLLP.getScale_Top() <= lSLLP.getScale_Top()+lSLLP.getScale_Height() && lSLLP.getScale_Top()+lSLLP.getScale_Height() <= pTV_SLLP.getScale_Top()+pTV_SLLP.getScale_Height()) 
						)) {
					
					moveChildView(v, lSLLP.getScale_Left()+lNewViewScaleWidth-lOldViewScaleWidth, lSLLP.getScale_Top());
				}
				// TODO 자신을 포함하고 있는 뷰들 크기 변경
//				else if(
//					lSLLP.getScale_Left() >= pTV_SLLP.getScale_Left()+lOldViewScaleWidth &&
//					(
//						( pTV_SLLP.getScale_Top() <= lSLLP.getScale_Top() && lSLLP.getScale_Top() <= pTV_SLLP.getScale_Top()+pTV_SLLP.getScale_Height() )
//						|| 
//						( pTV_SLLP.getScale_Top() <= lSLLP.getScale_Top()+lSLLP.getScale_Height() && lSLLP.getScale_Top()+lSLLP.getScale_Height() <= pTV_SLLP.getScale_Top()+pTV_SLLP.getScale_Height()) 
//						)) {
//					
//					moveChildView(v, lSLLP.getScale_Left()+lNewViewScaleWidth-lOldViewScaleWidth, lSLLP.getScale_Top());
//				}
				else if( 
					lSLLP.getScale_Top() <= pTV_SLLP.getScale_Top() &&
					lSLLP.getScale_Left() <= pTV_SLLP.getScale_Left() &&
					lSLLP.getScale_Right() >= pTV_SLLP.getScale_Right() &&
					lSLLP.getScale_Bottom() >= pTV_SLLP.getScale_Bottom()) {
					
					moveChildView(v, lSLLP.getScale_Left(), lSLLP.getScale_Top(), lSLLP.getScale_Width()+lNewViewScaleWidth-lOldViewScaleWidth, lSLLP.getScale_Height());
				}
			}
			moveChildView(pTV_Text, pTV_SLLP.getScale_Left(), pTV_SLLP.getScale_Top(), lNewViewScaleWidth, pTV_SLLP.getScale_Height());
			
			if(pTV_SLLP.mScale_TextViewWrapContentTotally) {
				setScaleSize(getScaleWidth()+lNewViewScaleWidth-lOldViewScaleWidth, getScaleHeight(), true);
			}
		}
		return pBGWidth*getScaleWidth()/lOldScaleWidth;
	}
	private float updateTextViewHeight(TextView pTV_Text, ScalableLayout.LayoutParams pTV_SLLP, float pBGWidth, float pBGHeight) {
//		float lNewViewHeight = measureTextHeight(pTV_Text.getContext(), pTV_Text.getText().toString(), (int)pTV_Text.getTextSize(), pTV_Text.getWidth());
		
		float lOldViewScaleHeight = pTV_SLLP.getScale_Height();
		float lOldViewHeight = pTV_Text.getHeight();
		
		log("updateTextViewHeight lOldViewHeight:"+lOldViewHeight+" lOldViewScaleHeight:"+lOldViewScaleHeight+" pBGHeight:"+pBGHeight+" getScaleHeight:"+getScaleHeight());
		if(lOldViewHeight <= 0 || lOldViewScaleHeight <= 0 || pBGHeight <= 0 || getScaleHeight() <= 0) {
//		if(lOldScaleHeight <= 0 || getHeight() <= 0 || getScaleHeight() <= 0) {
			getHandler().postDelayed(new Runnable() {
				@Override
				public void run() {
					requestLayout();
					forceLayout();
				}
			}, 10);
			return pBGHeight;
		}
		
		int widthMeasureSpec = MeasureSpec.makeMeasureSpec(pTV_Text.getWidth(), MeasureSpec.AT_MOST);
		int heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		pTV_Text.measure(widthMeasureSpec, heightMeasureSpec);
		float lNewViewHeight = pTV_Text.getMeasuredHeight();
		float lNewViewScaleHeight = (lNewViewHeight * getScaleHeight() / pBGHeight);
		
//		log("updateTextViewHeight "+lOldViewHeight+","+lNewViewHeight+" "+lOldScaleHeight+","+lNewScaleHeight);
		
		log(String.format("updateTextViewHeight1 View: %5.3f -> %5.3f Scale: %5.3f -> %5.3f",lOldViewHeight,lNewViewHeight,lOldViewScaleHeight,lNewViewScaleHeight));
		log(String.format("updateTextViewHeight2 Scalable Scale: %5.3f,%5.3f ", getScaleWidth(), getScaleHeight()));
		log(String.format("updateTextViewHeight3 Scalable View: %5.3f,%5.3f", pBGWidth, pBGHeight));
		
		float lOldScaleHeight = getScaleHeight();
		
		if(Math.abs(lNewViewHeight - lOldViewHeight) * 100 > pBGHeight && Math.abs(lNewViewScaleHeight - lOldViewScaleHeight) > getScaleHeight() / 100f) {
			for(int i=0;i<getChildCount();i++) {
				View v = getChildAt(i);
				if(v == pTV_Text) {
					continue;
				}
				ScalableLayout.LayoutParams lSLLP = (ScalableLayout.LayoutParams) v.getLayoutParams();

				log(String.format("lSLLP:"+lSLLP.toString()));
				log(String.format("pTV_SLLP:"+pTV_SLLP.toString()));
//					getScaleHeight(), getScaleHeight()+lNewViewScaleHeight-lOldViewScaleHeight));
				// 자신의 아래에 있는 뷰들 위치 이동
				if(
					lSLLP.getScale_Top() >= pTV_SLLP.getScale_Top() &&
					(
//						( pTV_SLLP.getScale_Left() <= lSLLP.getScale_Left() && lSLLP.getScale_Left() <= pTV_SLLP.getScale_Left()+pTV_SLLP.getScale_Width() )
//						|| 
//						( pTV_SLLP.getScale_Left() <= lSLLP.getScale_Left()+lSLLP.getScale_Width() && lSLLP.getScale_Left()+lSLLP.getScale_Width() <= pTV_SLLP.getScale_Left()+pTV_SLLP.getScale_Width()) 
//						)) {
						( pTV_SLLP.getScale_Left() <= lSLLP.getScale_Left() && lSLLP.getScale_Left() <= pTV_SLLP.getScale_Right() )
						|| 
						( pTV_SLLP.getScale_Left() <= lSLLP.getScale_Right() && lSLLP.getScale_Right() <= pTV_SLLP.getScale_Right()) 
						)) {
					
//					log(String.format("moveChildView From:%5.3f,%5.3f To:%5.3f,%5.3f",
//						lSLLP.getScale_Left(),lSLLP.getScale_Top(),
//						lSLLP.getScale_Left(),lSLLP.getScale_Top()+lNewViewScaleHeight-lOldViewScaleHeight));
					
					moveChildView(v, lSLLP.getScale_Left(), lSLLP.getScale_Top()+lNewViewScaleHeight-lOldViewScaleHeight);
				}
				// 자신을 포함하는 뷰들 크기 변경
				else if( 
					lSLLP.getScale_Top() <= pTV_SLLP.getScale_Top() &&
					lSLLP.getScale_Left() <= pTV_SLLP.getScale_Left() &&
					lSLLP.getScale_Right() >= pTV_SLLP.getScale_Right() &&
					lSLLP.getScale_Bottom() >= pTV_SLLP.getScale_Bottom()) {
					
					moveChildView(v, lSLLP.getScale_Left(), lSLLP.getScale_Top(), lSLLP.getScale_Width(), lSLLP.getScale_Height()+lNewViewScaleHeight-lOldViewScaleHeight);
				}
			}
			moveChildView(pTV_Text, pTV_SLLP.getScale_Left(), pTV_SLLP.getScale_Top(), pTV_SLLP.getScale_Width(), lNewViewScaleHeight);
			
			if(pTV_SLLP.mScale_TextViewWrapContentTotally) {
				log(String.format("setScaleSize From:%5.3f To:%5.3f",
					getScaleHeight(), getScaleHeight()+lNewViewScaleHeight-lOldViewScaleHeight));
				setScaleSize(getScaleWidth(), getScaleHeight()+lNewViewScaleHeight-lOldViewScaleHeight, true);
			}
		}
		return pBGHeight*getScaleHeight()/lOldScaleHeight;
	}
	@Override
	protected void onMeasure(int pWidthMeasureSpec, int pHeightMeasureSpec) {
//		log("onMeasure ================ Start "+this.toString());
		float lBGWidth = 0;
		int lWidthMode = MeasureSpec.getMode(pWidthMeasureSpec);
		int lWidthSize = MeasureSpec.getSize(pWidthMeasureSpec);
		float lBGHeight = 0;
		int lHeightMode = MeasureSpec.getMode(pHeightMeasureSpec);
		int lHeightSize = MeasureSpec.getSize(pHeightMeasureSpec);
		
		switch (lWidthMode) {
		case MeasureSpec.EXACTLY:
//			log("  onMeasure Width Exactly "+lBGWidth+" = min("+mScale_Full_Width+", "+lWidthSize+")");
			lBGWidth = lWidthSize;
			break;
		case MeasureSpec.AT_MOST:
//			log("  onMeasure Width AtMost "+lBGWidth+" = min("+mScale_Full_Width+", "+lWidthSize+")");
			lBGWidth = lWidthSize;
			break;
		default:
//			log("  onMeasure Width Unspecified "+lBGWidth+" = "+mScale_Full_Width);
			lBGWidth = mScale_Full_Width;
			break;
		}
		
		switch (lHeightMode) {
		case MeasureSpec.EXACTLY:
			switch (lWidthMode) {
			case MeasureSpec.EXACTLY:
				lBGHeight = Math.min(lBGWidth * mRatioOfWidthHeight, lHeightSize);
				break;
			case MeasureSpec.AT_MOST:
				lBGHeight = Math.min(lBGWidth * mRatioOfWidthHeight, lHeightSize);
				break;
			default:
				lBGHeight = lHeightSize;
				lBGWidth = lBGHeight / mRatioOfWidthHeight;
				break;
			}
			
//			log("  onMeasure Height Exactly "+lBGHeight+", "+lHeightSize);
			break;
		case MeasureSpec.AT_MOST:
			
			switch (lWidthMode) {
			case MeasureSpec.EXACTLY:
				lBGHeight = lBGWidth * mRatioOfWidthHeight;
				break;
			case MeasureSpec.AT_MOST:
				lBGHeight = Math.min(lBGWidth * mRatioOfWidthHeight, lHeightSize);
				break;
			default:
				lBGHeight = Math.min(lBGWidth * mRatioOfWidthHeight, lHeightSize);
				break;
			}
			
//			log("  onMeasure Height AtMost "+lBGHeight+" = min("+(lBGWidth * mRatioOfWidthHeight)+", "+lHeightSize+")");
			break;
		default:
			lBGHeight = lBGWidth * mRatioOfWidthHeight;
//			log("  onMeasure Height Unspecified "+lBGHeight+" = "+lBGWidth+"*"+mRatioOfWidthHeight);
			break;
		}
		
		if(lBGHeight/mRatioOfWidthHeight < lBGWidth) {
//			log("  onMeasure Height Exactly Replace Width "+lBGWidth+" to "+(lBGHeight/mRatioOfWidthHeight));
			lBGWidth = lBGHeight/mRatioOfWidthHeight;
		}

		log(String.format("onMeasure 1 lScale:%5.3f ChildCount:%d", (lBGWidth/mScale_Full_Width), getChildCount()));
		for (int i=0;i<getChildCount();i++) {
			View lView = getChildAt(i);
				
			if(lView instanceof TextView) {
				TextView v = (TextView) lView;
				
				ScalableLayout.LayoutParams lParams = getChildLayoutParams(lView);
				
				log(String.format("onMeasure 1.1 lParams:"+lParams.mScale_TextViewWrapContentMode));
				switch (lParams.mScale_TextViewWrapContentMode) {
				case Horizontal: {
					lBGWidth = updateTextViewWidth(v, lParams, lBGWidth, lBGHeight);
				} break;
				case Vertical: {
					lBGHeight = updateTextViewHeight(v, lParams, lBGWidth, lBGHeight);
				} break;
				}
			}
		}
		log(String.format("onMeasure 2 lScale:%5.3f", (lBGWidth/mScale_Full_Width)));
		
		float lScale = lBGWidth / mScale_Full_Width;
		
		float lTopMarginFromWeight = (lBGHeight - (lBGWidth * mRatioOfWidthHeight))/4;
//		log("  onMeasure ("+lBGWidth+","+lBGHeight+") Ratio:"+mRatioOfWidthHeight+" Scale:"+lScale+" lTopMarginFromWeight:"+lTopMarginFromWeight);
		
		for (int i=0;i<getChildCount();i++) {
			View lView = getChildAt(i);
				
			ScalableLayout.LayoutParams lParams = getChildLayoutParams(lView);
			
			boolean dif = false;
			
			if(lParams.width != (int)(lScale * lParams.mScale_Width)+1)
				dif = true;
			lParams.width = (int)(lScale * lParams.mScale_Width)+1;
			if(lParams.height != (int)(lScale * lParams.mScale_Height)+1)
				dif = true;
			lParams.height = (int)(lScale * lParams.mScale_Height)+1;
			
			if(lParams.leftMargin != Math.round(lScale * lParams.mScale_Left))
				dif = true;
			lParams.leftMargin = Math.round(lScale * lParams.mScale_Left);
			
			if(lParams.topMargin != Math.round(lScale * lParams.mScale_Top + lTopMarginFromWeight))
				dif = true;
			lParams.topMargin = Math.round(lScale * lParams.mScale_Top + lTopMarginFromWeight);
			
			
			lParams.rightMargin = 0;
			lParams.bottomMargin = 0;
			
//			lParams.setMargins(Math.round(lScale * lParams.mScale_Left), Math.round(lScale * lParams.mScale_Top + lTopMarginFromWeight), 0, 0);
//			log("  "+lView.toString()+" "+
//				String.format("(%f, %f, %f, %f)", lParams.mScale_Left, lParams.mScale_Top, lParams.mScale_Width, lParams.mScale_Height)+
//				"->"+
//				String.format("(%d, %d, %d, %d)", lParams.leftMargin, lParams.topMargin, lParams.width, lParams.height));

			
			
			if(dif) {
				lView.setLayoutParams(lParams);
			}
			
			if(lView instanceof TextView) {
				TextView v = (TextView) lView;
				if(v.getTextSize() != lParams.mScale_TextSize * lScale) {
					v.setTextSize(TypedValue.COMPLEX_UNIT_PX, lParams.mScale_TextSize * lScale);
				}
				
//				switch (lParams.mScale_TextViewWrapContentMode) {
//				case Horizontal: {
//				} break;
//				case Vertical: {
//					dif = updateTextViewHeight(v, lParams);
//				} break;
//				}
			}
//			else if(lView instanceof EditText) {
//				EditText v = (EditText) lView;
//				if(v.getTextSize() != lParams.mScale_TextSize * lScale) {
//					v.setTextSize(TypedValue.COMPLEX_UNIT_PX, lParams.mScale_TextSize * lScale);
//				}
//			}
		}
		
//		log("onMeasure ================ End   "+this.toString());
		super.onMeasure(MeasureSpec.makeMeasureSpec(Math.round(lBGWidth), lWidthMode), MeasureSpec.makeMeasureSpec(Math.round(lBGHeight), lHeightMode));
		setMeasuredDimension(Math.round(lBGWidth), Math.round(lBGHeight));
	}
	
	

	
	public static enum TextViewWrapContentMode {
		None, Horizontal, Vertical,
	}
	/**
	 * ScalableLayout.LayoutParams
	 */
	public static class LayoutParams extends FrameLayout.LayoutParams {
		public LayoutParams(Context pContext, AttributeSet pAttrs) { super(pContext, pAttrs); }
		public LayoutParams(
				float pScale_Left, float pScale_Top, 
				float pScale_Width, float pScale_Height) {
			this(pScale_Left, pScale_Top, pScale_Width, pScale_Height, Default_Scale_TextSize);
		}
		private LayoutParams(
				float pScale_Left, float pScale_Top, 
				float pScale_Width, float pScale_Height, float pScale_TextSize) {
			super(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.LEFT | Gravity.TOP);
			
			setScale_Left(pScale_Left);
			setScale_Top(pScale_Top);
			setScale_Width(pScale_Width);
			setScale_Height(pScale_Height);
			
			setScale_TextSize(pScale_TextSize);
		}
		
		private LayoutParams(android.view.ViewGroup.LayoutParams pP) {
			this(
				Default_Scale_Left, Default_Scale_Top, 
				Default_Scale_Width, Default_Scale_Height, 
				Default_Scale_TextSize);
			
			width = pP.width;
			height = pP.height;
			layoutAnimationParameters = pP.layoutAnimationParameters;
			gravity = Gravity.LEFT | Gravity.TOP;
		}

		@Override
		public String toString() {
		    return "("+getScale_Left()+","+getScale_Top()+") ("+getScale_Right()+","+getScale_Bottom()+")";
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

		private float mScale_TextSize = Default_Scale_TextSize;
		public float getScale_TextSize() { return mScale_TextSize; }
		public void setScale_TextSize(float pScale_TextSize) { mScale_TextSize = pScale_TextSize; }
		
		private TextViewWrapContentMode mScale_TextViewWrapContentMode = TextViewWrapContentMode.None;
		private boolean 				mScale_TextViewWrapContentTotally = false;
		public void setScale_TextViewWrapContentMode(TextViewWrapContentMode scale_TextViewWrapContentMode, boolean pTotally) { 
			mScale_TextViewWrapContentMode = scale_TextViewWrapContentMode; 
			mScale_TextViewWrapContentTotally = pTotally;
		}
	}
	
	private void log(String pLog) {
		if(sLogTag_World != null) {
			Log.e(sLogTag_World, this+"] "+pLog);
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
	
	
	
	private static String sLogTag_World = null;
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
		sLogTag_World = pLogTag;
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
}

