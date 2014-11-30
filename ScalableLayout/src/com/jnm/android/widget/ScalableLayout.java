package com.jnm.android.widget;

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
		setTextView_WrapContent(pTextView, pDirection, true);
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
	
	
//	private float updateTextViewWidth(TextView pTV_Text, TextView_WrapContent_Direction pTextView_WrapContent_Direction, ScalableLayout.LayoutParams pTV_SLLP, float pBGWidth, float pBGHeight) {
//		float lTextView_OldScaleWidth = pTV_SLLP.getScale_Width();
//		float lTextView_OldWidth = pTV_Text.getWidth();
//		
////		log("updateTextViewWidth 1 lOldViewWidth:"+lTextView_OldWidth+" lOldViewScaleWidth:"+lTextView_OldScaleWidth+" pBGWidth:"+pBGWidth+" getScaleWidth:"+getScaleWidth() +"pTextView_WrapContent_Direction:"+pTextView_WrapContent_Direction);
//		if(lTextView_OldScaleWidth <= 0 || pBGWidth <= 0 || getScaleWidth() <= 0) {
//			postDelayedRequestLayout();
//			
//			return pBGWidth;
//		}
//		
//		int widthMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
//		int heightMeasureSpec = MeasureSpec.makeMeasureSpec((int)(pTV_Text.getHeight()/1.1f), MeasureSpec.EXACTLY);
//		pTV_Text.measure(widthMeasureSpec, heightMeasureSpec);
//		float lTextView_NewWidth = pTV_Text.getMeasuredWidth();
//		float lTextView_NewScaleWidth = (lTextView_NewWidth * getScaleWidth() / pBGWidth)*1.05f; // 정확하게 측정하지 못할때를 대비하기 위해서 공간을 좀 확보하기 위함이다. 그치만 안 좋은것같음
//		
//		float lTextView_DiffScaleWidth = lTextView_NewScaleWidth - lTextView_OldScaleWidth;
//		
////		log("updateTextViewWidth 2 lOldViewWidth:"+lTextView_OldWidth+" lOldViewScaleWidth:"+lTextView_OldScaleWidth+" pBGWidth:"+pBGWidth+" getScaleWidth:"+getScaleWidth() +"pTextView_WrapContent_Direction:"+pTextView_WrapContent_Direction);
//				
//		float lRootView_OldScaleWidth = getScaleWidth();
//		if(Math.abs(lTextView_NewWidth - lTextView_OldWidth) * 100 > pBGWidth && Math.abs(lTextView_DiffScaleWidth) > getScaleWidth() / 100f) {
////			log("updateTextViewWidth 3 lOldViewWidth:"+lTextView_OldWidth+" lOldViewScaleWidth:"+lTextView_OldScaleWidth+" pBGWidth:"+pBGWidth+" getScaleWidth:"+getScaleWidth() +"pTextView_WrapContent_Direction:"+pTextView_WrapContent_Direction);
//			for(int i=0;i<getChildCount();i++) {
//				View v = getChildAt(i);
//				if(v == pTV_Text) {
//					continue;
//				}
//				ScalableLayout.LayoutParams lV_SLLP = (ScalableLayout.LayoutParams) v.getLayoutParams();
//				switch (pTextView_WrapContent_Direction) {
//				case Left: {
//					// move views on left
//					// 왼쪽에 있는 뷰들 위치 이동
//					if(
//						lV_SLLP.getScale_Right() <= pTV_SLLP.getScale_Right() &&
//						(
//							( pTV_SLLP.getScale_Top() <= lV_SLLP.getScale_Top() && lV_SLLP.getScale_Top() <= pTV_SLLP.getScale_Bottom())
//							|| 
//							( pTV_SLLP.getScale_Top() <= lV_SLLP.getScale_Bottom() && lV_SLLP.getScale_Bottom() <= pTV_SLLP.getScale_Bottom()) 
//							||
//							( pTV_SLLP.getScale_Top() >= lV_SLLP.getScale_Top() &&  lV_SLLP.getScale_Bottom() >= pTV_SLLP.getScale_Bottom())
//							)) {
//						moveChildView(v, lV_SLLP.getScale_Left()-lTextView_DiffScaleWidth, lV_SLLP.getScale_Top());
//					}
//					else if( pTV_SLLP.mTextView_WrapContent_ResizeSurrounded &&
//						lV_SLLP.getScale_Top() <= pTV_SLLP.getScale_Top() &&
//						lV_SLLP.getScale_Left() <= pTV_SLLP.getScale_Left() &&
//						lV_SLLP.getScale_Right() >= pTV_SLLP.getScale_Right() &&
//						lV_SLLP.getScale_Bottom() >= pTV_SLLP.getScale_Bottom()) {
//						
//						moveChildView(v, lV_SLLP.getScale_Left()-lTextView_DiffScaleWidth, lV_SLLP.getScale_Top(), lV_SLLP.getScale_Width()+lTextView_DiffScaleWidth, lV_SLLP.getScale_Height());
//					}
//				} break;
//				case Right: {
//					// move views on right
//					// 오른쪽에 있는 뷰들 위치 이동					
//					if(
//						lV_SLLP.getScale_Left() >= pTV_SLLP.getScale_Left() &&
//						(
//							( pTV_SLLP.getScale_Top() <= lV_SLLP.getScale_Top() && lV_SLLP.getScale_Top() <= pTV_SLLP.getScale_Bottom())
//							|| 
//							( pTV_SLLP.getScale_Top() <= lV_SLLP.getScale_Bottom() && lV_SLLP.getScale_Bottom() <= pTV_SLLP.getScale_Bottom()) 
//							||
//							( pTV_SLLP.getScale_Top() >= lV_SLLP.getScale_Top() &&  lV_SLLP.getScale_Bottom() >= pTV_SLLP.getScale_Bottom())
//							)) {							
//						
//						moveChildView(v, lV_SLLP.getScale_Left()+lTextView_DiffScaleWidth, lV_SLLP.getScale_Top());
//					}
//					else if( pTV_SLLP.mTextView_WrapContent_ResizeSurrounded &&
//						lV_SLLP.getScale_Top() <= pTV_SLLP.getScale_Top() &&
//						lV_SLLP.getScale_Left() <= pTV_SLLP.getScale_Left() &&
//						lV_SLLP.getScale_Right() >= pTV_SLLP.getScale_Right() &&
//						lV_SLLP.getScale_Bottom() >= pTV_SLLP.getScale_Bottom()) {
//						
//						moveChildView(v, lV_SLLP.getScale_Left(), lV_SLLP.getScale_Top(), lV_SLLP.getScale_Width()+lTextView_DiffScaleWidth, lV_SLLP.getScale_Height());
//					}
//				} break;
//				case Center_Horizontal: {
//					// 왼쪽에 있는 뷰들 위치 이동
//					if(
//						lV_SLLP.getScale_Right() <= pTV_SLLP.getScale_Right() &&
//						(
//							( pTV_SLLP.getScale_Top() <= lV_SLLP.getScale_Top() && lV_SLLP.getScale_Top() <= pTV_SLLP.getScale_Bottom())
//							|| 
//							( pTV_SLLP.getScale_Top() <= lV_SLLP.getScale_Bottom() && lV_SLLP.getScale_Bottom() <= pTV_SLLP.getScale_Bottom()) 
//							||
//							( pTV_SLLP.getScale_Top() >= lV_SLLP.getScale_Top() &&  lV_SLLP.getScale_Bottom() >= pTV_SLLP.getScale_Bottom())
//							)) {
//						
//						moveChildView(v, lV_SLLP.getScale_Left()-lTextView_DiffScaleWidth/2, lV_SLLP.getScale_Top());
//					}
//					else if( pTV_SLLP.mTextView_WrapContent_ResizeSurrounded &&
//						lV_SLLP.getScale_Top() <= pTV_SLLP.getScale_Top() &&
//						lV_SLLP.getScale_Left() <= pTV_SLLP.getScale_Left() &&
//						lV_SLLP.getScale_Right() >= pTV_SLLP.getScale_Right() &&
//						lV_SLLP.getScale_Bottom() >= pTV_SLLP.getScale_Bottom()) {
//						
//						moveChildView(v, lV_SLLP.getScale_Left()-lTextView_DiffScaleWidth/2, lV_SLLP.getScale_Top(), lV_SLLP.getScale_Width()+lTextView_DiffScaleWidth/2, lV_SLLP.getScale_Height());
//					}
//					
//					// 오른쪽에 있는 뷰들 위치 이동					
//					if(
//						lV_SLLP.getScale_Left() >= pTV_SLLP.getScale_Left() &&
//						(
//							( pTV_SLLP.getScale_Top() <= lV_SLLP.getScale_Top() && lV_SLLP.getScale_Top() <= pTV_SLLP.getScale_Bottom())
//							|| 
//							( pTV_SLLP.getScale_Top() <= lV_SLLP.getScale_Bottom() && lV_SLLP.getScale_Bottom() <= pTV_SLLP.getScale_Bottom()) 
//							||
//							( pTV_SLLP.getScale_Top() >= lV_SLLP.getScale_Top() &&  lV_SLLP.getScale_Bottom() >= pTV_SLLP.getScale_Bottom())
//							)) {							
//						
//						moveChildView(v, lV_SLLP.getScale_Left()+lTextView_DiffScaleWidth/2, lV_SLLP.getScale_Top());
//					}
//					else if( pTV_SLLP.mTextView_WrapContent_ResizeSurrounded &&
//						lV_SLLP.getScale_Top() <= pTV_SLLP.getScale_Top() &&
//						lV_SLLP.getScale_Left() <= pTV_SLLP.getScale_Left() &&
//						lV_SLLP.getScale_Right() >= pTV_SLLP.getScale_Right() &&
//						lV_SLLP.getScale_Bottom() >= pTV_SLLP.getScale_Bottom()) {
//						
//						moveChildView(v, lV_SLLP.getScale_Left(), lV_SLLP.getScale_Top(), lV_SLLP.getScale_Width()+lTextView_DiffScaleWidth/2, lV_SLLP.getScale_Height());
//					}
//				} break;
//				}
//			}
//			
//			switch (pTextView_WrapContent_Direction) {
//			case Left: {
//				moveChildView(pTV_Text, pTV_SLLP.getScale_Left()-(lTextView_DiffScaleWidth), pTV_SLLP.getScale_Top(), lTextView_NewScaleWidth, pTV_SLLP.getScale_Height());
//			} break;
//			case Right: {
//				moveChildView(pTV_Text, pTV_SLLP.getScale_Left(), pTV_SLLP.getScale_Top(), lTextView_NewScaleWidth, pTV_SLLP.getScale_Height());
//			} break;
//			case Center_Horizontal: {
//				moveChildView(pTV_Text, pTV_SLLP.getScale_Left()-(lTextView_DiffScaleWidth)/2, pTV_SLLP.getScale_Top(), lTextView_NewScaleWidth, pTV_SLLP.getScale_Height());
//			} break;
//			}
//			
//			if(pTV_SLLP.mTextView_WrapContent_ResizeSurrounded) {
//				setScaleSize(getScaleWidth()+lTextView_NewScaleWidth-lTextView_OldScaleWidth, getScaleHeight(), true);
//			}
//		}
////		log("updateTextViewWidth 4 ret "+(pBGHeight*getScaleHeight()/lRootView_OldScaleWidth)+" = "+pBGHeight+"*"+getScaleHeight()+"/"+lRootView_OldScaleWidth);
//		return pBGWidth*getScaleWidth()/lRootView_OldScaleWidth;
//	}
//	private float updateTextViewHeight(TextView pTV_Text, TextView_WrapContent_Direction pTextView_WrapContent_Direction, ScalableLayout.LayoutParams pTV_SLLP, float pBGWidth, float pBGHeight) {
//		float lTextView_OldHeight = pTV_Text.getHeight();
//		float lTextView_OldScaleHeight = pTV_SLLP.getScale_Height();
//		
//		log("updateTextViewHeight 1 lOldViewHeight:"+lTextView_OldHeight+" lOldViewScaleHeight:"+lTextView_OldScaleHeight+" pBGHeight:"+pBGHeight+" getScaleHeight:"+getScaleHeight());
//		
////		return pBGHeight;
//		
//		
////		if(lTextView_OldHeight <= 0 || lTextView_OldScaleHeight <= 0 || pBGHeight <= 0 || getScaleHeight() <= 0) {
//		if(lTextView_OldScaleHeight <= 0 || pBGHeight <= 0 || getScaleHeight() <= 0) {
//			postDelayedRequestLayout();
//			return pBGHeight;
//		}
//		
////		int widthMeasureSpec = MeasureSpec.makeMeasureSpec(pTV_Text.getWidth(), MeasureSpec.AT_MOST);
//		int widthMeasureSpec = MeasureSpec.makeMeasureSpec((int)(pTV_Text.getWidth()/1.1f), MeasureSpec.EXACTLY);
//		int heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
////		int heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.AT_MOST);
//		pTV_Text.measure(widthMeasureSpec, heightMeasureSpec);
//		float lTextView_NewHeight = pTV_Text.getMeasuredHeight();
//		float lTextView_NewScaleHeight = (lTextView_NewHeight * getScaleHeight() / pBGHeight);
////		log(String.format("updateTextViewHeight %f = %f * %f / %f TextSize:%f", lTextView_NewScaleHeight, lTextView_NewHeight, getScaleHeight(), pBGHeight, pTV_Text.getTextSize()));
////		
//////		log("updateTextViewHeight "+lOldViewHeight+","+lNewViewHeight+" "+lOldScaleHeight+","+lNewScaleHeight);
//		log("updateTextViewHeight 2 lOldViewHeight:"+lTextView_OldHeight+" lOldViewScaleHeight:"+lTextView_OldScaleHeight+" pBGHeight:"+pBGHeight+" getScaleHeight:"+getScaleHeight());
////		
////		log(String.format("updateTextViewHeight1 View: %5.3f -> %5.3f Scale: %5.3f -> %5.3f", lTextView_OldHeight, lTextView_NewHeight, lTextView_OldScaleHeight, lTextView_NewScaleHeight));
////		log(String.format("updateTextViewHeight2 Scalable Scale: %5.3f,%5.3f ", getScaleWidth(), getScaleHeight()));
////		log(String.format("updateTextViewHeight3 Scalable View: %5.3f,%5.3f", pBGWidth, pBGHeight));
//
//		float lTextView_DiffScaleHeight = lTextView_NewScaleHeight - lTextView_OldScaleHeight;
//		float lRootView_OldScaleHeight = getScaleHeight();
//		
//		if(Math.abs(lTextView_NewHeight - lTextView_OldHeight) * 100 > pBGHeight && Math.abs(lTextView_DiffScaleHeight) > getScaleHeight() / 100f) {
//			log("updateTextViewHeight 3 lOldViewHeight:"+lTextView_OldHeight+" lOldViewScaleHeight:"+lTextView_OldScaleHeight+" pBGHeight:"+pBGHeight+" getScaleHeight:"+getScaleHeight());
//			for(int i=0;i<getChildCount();i++) {
//				View v = getChildAt(i);
//				if(v == pTV_Text) {
//					continue;
//				}
//				ScalableLayout.LayoutParams lSLLP = (ScalableLayout.LayoutParams) v.getLayoutParams();
//
////				log(String.format("lSLLP:"+lSLLP.toString()));
////				log(String.format("pTV_SLLP:"+pTV_SLLP.toString()));
////					getScaleHeight(), getScaleHeight()+lNewViewScaleHeight-lOldViewScaleHeight));
//				switch (pTextView_WrapContent_Direction) {
//				case Top: {
//					// 자신의 위에 있는 뷰들 위치 이동
//					if(
//						lSLLP.getScale_Bottom() <= pTV_SLLP.getScale_Bottom() &&
//						(
//							( pTV_SLLP.getScale_Left() <= lSLLP.getScale_Left() && lSLLP.getScale_Left() <= pTV_SLLP.getScale_Right())
//							|| 
//							( pTV_SLLP.getScale_Left() <= lSLLP.getScale_Right() && lSLLP.getScale_Right() <= pTV_SLLP.getScale_Right()) 
//							||
//							( lSLLP.getScale_Left() <= pTV_SLLP.getScale_Left() && pTV_SLLP.getScale_Right() <= lSLLP.getScale_Right())
//							)) {
//						
//						moveChildView(v, lSLLP.getScale_Left(), lSLLP.getScale_Top()-lTextView_DiffScaleHeight);
//					}
//					// 자신을 포함하는 뷰들 크기 변경
//					else if( pTV_SLLP.mTextView_WrapContent_ResizeSurrounded &&
//						lSLLP.getScale_Top() <= pTV_SLLP.getScale_Top() &&
//						lSLLP.getScale_Left() <= pTV_SLLP.getScale_Left() &&
//						lSLLP.getScale_Right() >= pTV_SLLP.getScale_Right() &&
//						lSLLP.getScale_Bottom() >= pTV_SLLP.getScale_Bottom()) {
//						
//						moveChildView(v, 
//							lSLLP.getScale_Left(), lSLLP.getScale_Top()-lTextView_DiffScaleHeight, 
//							lSLLP.getScale_Width(), lSLLP.getScale_Height()+lTextView_DiffScaleHeight);
//					}
//				} break;
//				case Bottom: {
//					// 자신의 아래에 있는 뷰들 위치 이동
//					if(
//						lSLLP.getScale_Top() >= pTV_SLLP.getScale_Top() &&
//						(
//							( pTV_SLLP.getScale_Left() <= lSLLP.getScale_Left() && lSLLP.getScale_Left() <= pTV_SLLP.getScale_Right())
//							|| 
//							( pTV_SLLP.getScale_Left() <= lSLLP.getScale_Right() && lSLLP.getScale_Right() <= pTV_SLLP.getScale_Right()) 
//							||
//							( lSLLP.getScale_Left() <= pTV_SLLP.getScale_Left() && pTV_SLLP.getScale_Right() <= lSLLP.getScale_Right())
////							( pTV_SLLP.getScale_Top() >= lSLLP.getScale_Top() &&  lSLLP.getScale_Bottom() >= pTV_SLLP.getScale_Bottom())
//							)) {
//						
//						moveChildView(v, lSLLP.getScale_Left(), lSLLP.getScale_Top()+lTextView_DiffScaleHeight);
//					}
//					// 자신을 포함하는 뷰들 크기 변경
//					else if( pTV_SLLP.mTextView_WrapContent_ResizeSurrounded &&
//						lSLLP.getScale_Top() <= pTV_SLLP.getScale_Top() &&
//						lSLLP.getScale_Left() <= pTV_SLLP.getScale_Left() &&
//						lSLLP.getScale_Right() >= pTV_SLLP.getScale_Right() &&
//						lSLLP.getScale_Bottom() >= pTV_SLLP.getScale_Bottom()) {
//						
//						moveChildView(v, 
//							lSLLP.getScale_Left(), lSLLP.getScale_Top(), 
//							lSLLP.getScale_Width(), lSLLP.getScale_Height()+lTextView_DiffScaleHeight);
//					}
//				} break;
//				}
//			}
//			
//			switch (pTextView_WrapContent_Direction) {
//			case Top: {
//			} break;
//			case Bottom: {
//				moveChildView(pTV_Text, pTV_SLLP.getScale_Left(), pTV_SLLP.getScale_Top(), pTV_SLLP.getScale_Width(), lTextView_NewScaleHeight);
//			} break;
//			}
//			
//			if(pTV_SLLP.mTextView_WrapContent_ResizeSurrounded) {
//				log(String.format("setScaleSize From:%5.3f To:%5.3f", getScaleHeight(), getScaleHeight()+lTextView_DiffScaleHeight));
//				setScaleSize(getScaleWidth(), getScaleHeight()+lTextView_DiffScaleHeight, true);
//			}
//		}
//		log("updateTextViewHeight 4 ret "+(pBGHeight*getScaleHeight()/lRootView_OldScaleHeight)+" = "+pBGHeight+"*"+getScaleHeight()+"/"+lRootView_OldScaleHeight);
//		return pBGHeight*getScaleHeight()/lRootView_OldScaleHeight;
//	}
	
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
			
//			if(mHaveBeenLayouted == true && isDifferentSufficiently(lScale_Ratio_Post, lScale_Ratio_Pre) == false) {
//				break;
//			}
			
//			log(String.format("onMeasure 2 lScale_Ratio:%5.3f->%5.3f Scale_Ratio:(%5.3f, %5.3f) Scale:(%5.3f, %5.3f)", 
//				lScale_Ratio_Pre, lScale_Ratio_Post, (lRoot_Width_Max/mScale_Root_Width), (lRoot_Height_Max / mScale_Root_Height), mScale_Root_Width, mScale_Root_Height));
			{
				lRoot_Width 	= mScale_Root_Width * lScale_Ratio_Post;
				lRoot_Height 	= mScale_Root_Height * lScale_Ratio_Post;
//				log(String.format("onMeasure 2 Root_Max:(%10.7f, %10.7f) Root:(%10.7f, %10.7f)", lRoot_Width_Max, lRoot_Height_Max, lRoot_Width, lRoot_Height));
				
//			if(Math.abs(lForTextViewRootWidth - lRoot_Width) >= 1 || Math.abs(lForTextViewRootHeight - lRoot_Height) >= 1) {
//				log(String.format("onMeasure 2.1 %b %b", Math.abs(lForTextViewRootWidth - lRoot_Width) >= 1,  Math.abs(lForTextViewRootHeight - lRoot_Height) >= 1));
//				if(lRoot_MeasureSpec_WidthMode == MeasureSpec.EXACTLY && lRoot_MeasureSpec_HeightMode == MeasureSpec.EXACTLY) {
//					// 둘중에 커진 놈 기준으로
//					if(lForTextViewRootWidth / lRoot_Width > lForTextViewRootHeight / lRoot_Height) {
//						lRoot_Height /= (lForTextViewRootWidth / lRoot_Width);
//						lRoot_Width = lForTextViewRootWidth;
//						log(String.format("onMeasure 2.1.1 %b %b", Math.abs(lForTextViewRootWidth - lRoot_Width) >= 1,  Math.abs(lForTextViewRootHeight - lRoot_Height) >= 1));
//					} else {
//						lRoot_Width /= (lForTextViewRootHeight / lRoot_Height);
//						lRoot_Height = lForTextViewRootHeight;
//						log(String.format("onMeasure 2.1.2 %b %b", Math.abs(lForTextViewRootWidth - lRoot_Width) >= 1,  Math.abs(lForTextViewRootHeight - lRoot_Height) >= 1));
//					}
////			} else if(lWidthMode == MeasureSpec.EXACTLY && lHeightMode != MeasureSpec.EXACTLY) {
////				lRootHeight /= (lForTextViewRootWidth / lRootWidth);
////				lRootWidth = lForTextViewRootWidth;
////			} else if(lWidthMode != MeasureSpec.EXACTLY && lHeightMode == MeasureSpec.EXACTLY) {
////				lRootWidth /= (lForTextViewRootHeight / lRootHeight);
////				lRootHeight = lForTextViewRootHeight;
//				} else {
//					lRoot_Width = lForTextViewRootWidth;
//					lRoot_Height = lForTextViewRootHeight;
//					log(String.format("onMeasure 2.1.3 %b %b", Math.abs(lForTextViewRootWidth - lRoot_Width) >= 1,  Math.abs(lForTextViewRootHeight - lRoot_Height) >= 1));
//				}
//				
////			postDelayedRequestLayout();
//			}
			}
			
			{
//			log(String.format("onMeasure 2 lScale:%5.3f", (lRoot_Width/mScale_Root_Width)));
//			log(String.format("onMeasure 3 lScale:%5.3f ChildCount:%d Root:(%5.3f, %5.3f) ForTextView:(%5.3f, %5.3f)", 
//				(lRoot_Width/mScale_Root_Width), getChildCount(), lRoot_Width, lRoot_Height, lForTextViewRootWidth, lForTextViewRootHeight));
				
//			float lScale_TextViewMeasure_Post = Math.min(lRoot_Width_Max / mScale_Root_Width, lRoot_Height_Max / mScale_Root_Height);
//			float lScale_TextViewMeasure_Post = Math.min(lRoot_Width / mScale_Root_Width, lRoot_Height / mScale_Root_Height);
//			float lScale_TextViewMeasure_Post = lRoot_Width / mScale_Root_Width;
				
				
				
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
	
	
//	@Override
//	protected void onMeasure(int pWidthMeasureSpec, int pHeightMeasureSpec) {
//		log("onMeasure ================ Start "+this.toString());
//
//		float 	lRoot_Width 					= 0;
//		float 	lRoot_Width_Max 				= 0;
//		int 	lRoot_MeasureSpec_WidthMode 	= MeasureSpec.getMode(pWidthMeasureSpec);
//		
//		float 	lRoot_Height 					= 0;
//		float 	lRoot_Height_Max 				= 0;
//		int 	lRoot_MeasureSpec_HeightMode 	= MeasureSpec.getMode(pHeightMeasureSpec);
//		
//		// get condition of ScalableLayout
//		// ScalableLayout이 지켜야 되는 조건들을 확인함
//		{
//			int lRoot_MeasureSpec_Width 	= MeasureSpec.getSize(pWidthMeasureSpec);
//			int lRoot_MeasureSpec_Height 	= MeasureSpec.getSize(pHeightMeasureSpec);
//			switch (lRoot_MeasureSpec_WidthMode) {
//			case MeasureSpec.EXACTLY:
//				log("  onMeasure Width  Exactly "+lRoot_Width+" = min("+mScale_Root_Width+", "+lRoot_MeasureSpec_Width+")");
//				lRoot_Width 		= lRoot_MeasureSpec_Width;
//				lRoot_Width_Max 	= lRoot_MeasureSpec_Width;
//				break;
//			case MeasureSpec.AT_MOST:
//				log("  onMeasure Width  AtMost  "+lRoot_Width+" = min("+mScale_Root_Width+", "+lRoot_MeasureSpec_Width+")");
//				lRoot_Width 		= lRoot_MeasureSpec_Width;
//				lRoot_Width_Max 	= lRoot_MeasureSpec_Width;
//				break;
//			default:
//				log("  onMeasure Width  Unspecified "+lRoot_Width+" = "+mScale_Root_Width);
//				// TODO 이건 제대로 체크해야 함
////				lRoot_Width 		= mScale_Root_Width;
////				lRoot_Width_Max 	= Float.MAX_VALUE;
////				lRoot_Width 		= lRoot_WidthSize;
////				lRoot_Width_Max 	= lRoot_WidthSize;
////				break;
//				throw new IllegalArgumentException("  onMeasure Width  Unspecified "+lRoot_MeasureSpec_Width);
//			}
//			
//			switch (lRoot_MeasureSpec_HeightMode) {
//			case MeasureSpec.EXACTLY:
//				switch (lRoot_MeasureSpec_WidthMode) {
//				case MeasureSpec.EXACTLY:
//					log("  onMeasure Height Exactly Width Exactly "+lRoot_Height+" = min("+lRoot_Width+"*"+mRatioOfWidthHeight+", "+lRoot_MeasureSpec_Height+")");
//					lRoot_Height = Math.min(lRoot_Width * mRatioOfWidthHeight, lRoot_MeasureSpec_Height);
//					lRoot_Height_Max = lRoot_MeasureSpec_Height;
//					break;
//				case MeasureSpec.AT_MOST:
//					log("  onMeasure Height AtMost  Width Exactly "+lRoot_Height+" = min("+lRoot_Width+"*"+mRatioOfWidthHeight+", "+lRoot_MeasureSpec_Height+")");
//					lRoot_Height = Math.min(lRoot_Width * mRatioOfWidthHeight, lRoot_MeasureSpec_Height);
//					lRoot_Height_Max = lRoot_MeasureSpec_Height;
//					break;
//				default:
//					log("  onMeasure Height Unspecified "+lRoot_Height+" = min("+lRoot_Width+"*"+mRatioOfWidthHeight+", "+lRoot_MeasureSpec_Height+")");
//					lRoot_Height = lRoot_MeasureSpec_Height;
//					lRoot_Width = lRoot_Height / mRatioOfWidthHeight;
//					lRoot_Height_Max = Float.MAX_VALUE;
//					break;
//				}
//				
//				break;
//			case MeasureSpec.AT_MOST:
//				switch (lRoot_MeasureSpec_WidthMode) {
//				case MeasureSpec.EXACTLY:
//					lRoot_Height = lRoot_Width * mRatioOfWidthHeight;
//					break;
//				case MeasureSpec.AT_MOST:
//					lRoot_Height = Math.min(lRoot_Width * mRatioOfWidthHeight, lRoot_MeasureSpec_Height);
//					break;
//				default:
//					lRoot_Height = Math.min(lRoot_Width * mRatioOfWidthHeight, lRoot_MeasureSpec_Height);
//					break;
//				}
//				
////			log("  onMeasure Height AtMost "+lRootHeight+" = min("+(lRootWidth * mRatioOfWidthHeight)+", "+lHeightSize+")");
//				break;
//			default:
//				lRoot_Height = lRoot_Width * mRatioOfWidthHeight;
////			log("  onMeasure Height Unspecified "+lRootHeight+" = "+lRootWidth+"*"+mRatioOfWidthHeight);
//				break;
//			}
//			
//			if(lRoot_Height/mRatioOfWidthHeight < lRoot_Width) {
////			log("  onMeasure Height Exactly Replace Width "+lRootWidth+" to "+(lRootHeight/mRatioOfWidthHeight));
//				lRoot_Width = lRoot_Height/mRatioOfWidthHeight;
//			}
//		}
//		
////		log(String.format("onMeasure 1 lScale:%5.3f ChildCount:%d Root:(%5.3f, %5.3f)", (lRootWidth/mScale_Full_Width), getChildCount(), lRootWidth, lRootHeight));
////		log(String.format("onMeasure 1 lScale:%5.3f ChildCount:%d", (lRootWidth/mScale_Full_Width), getChildCount()));
//		
////		float lScale = lRootWidth / mScale_Full_Width;
//		float lScale_TextViewMeasure_Pre = lRoot_Width / mScale_Root_Width;
//		
//		float lForTextViewRootWidth = lRoot_Width;
//		float lForTextViewRootHeight = lRoot_Height;
//		
//		//////////////////////// Text Measure
//		for (int i=0;i<getChildCount();i++) {
//			View lView = getChildAt(i);
//			
//			if(lView instanceof TextView) {
//				lForTextViewRootHeight = updateTextViewSize((TextView)lView, lForTextViewRootWidth, lForTextViewRootHeight, lScale_TextViewMeasure_Pre);
//				
////				TextView v = (TextView) lView;
////
////				ScalableLayout.LayoutParams lParams = getChildLayoutParams(v);
//////				float lNewTextSize = lParams.mScale_TextSize * lScale_TextViewMeasure_Pre;
//////				if( lNewTextSize < v.getTextSize() / 1.1f || v.getTextSize() * 1.1f < lNewTextSize ) {
//////					v.setTextSize(TypedValue.COMPLEX_UNIT_PX, lParams.mScale_TextSize * lScale_TextViewMeasure_Pre);
//////				}
//////				refreshTextChangedListener(v);
////				
//////				log(String.format("onMeasure 1 lParams:%s TVIndex:%d lForTextViewRootHeight:%f", lParams.mTextView_WrapContent_Direction.name(), i, lForTextViewRootHeight));
////				switch (lParams.mTextView_WrapContent_Direction) {
////				case Left: {
////					lForTextViewRootWidth  = updateTextViewWidth( v, lParams.mTextView_WrapContent_Direction, lParams, lForTextViewRootWidth, lForTextViewRootHeight);
////				} break;
////				case Right: {
////					lForTextViewRootWidth  = updateTextViewWidth( v, lParams.mTextView_WrapContent_Direction, lParams, lForTextViewRootWidth, lForTextViewRootHeight);
////				} break;
////				case Center_Horizontal: {
////					lForTextViewRootWidth  = updateTextViewWidth( v, lParams.mTextView_WrapContent_Direction, lParams, lForTextViewRootWidth, lForTextViewRootHeight);
////				} break;
////				case Top: {
////					lForTextViewRootHeight = updateTextViewSize(v, lForTextViewRootWidth, lForTextViewRootHeight, lScale_TextViewMeasure_Pre);
////				} break;
////				case Bottom: {
////					lForTextViewRootHeight = updateTextViewSize(v, lForTextViewRootWidth, lForTextViewRootHeight, lScale_TextViewMeasure_Pre);
////				} break;
////				}
//////				log(String.format("onMeasure 2 lParams:%s TVIndex:%d lForTextViewRootHeight:%f", lParams.mTextView_WrapContent_Direction.name(), i, lForTextViewRootHeight));
//			}
//		}
//		
//		
//		
//		// calculate final size of ScalableLayout
//		// ScalableLayout의 최종적인 크기를 결정지음
//		{
//			log(String.format("onMeasure 2 lScale:%5.3f ChildCount:%d Root:(%10.7f, %10.7f) ForTextView:(%10.7f, %10.7f)", 
//				(lRoot_Width/mScale_Root_Width), getChildCount(), lRoot_Width, lRoot_Height, lForTextViewRootWidth, lForTextViewRootHeight));
//			if(Math.abs(lForTextViewRootWidth - lRoot_Width) >= 1 || Math.abs(lForTextViewRootHeight - lRoot_Height) >= 1) {
//				log(String.format("onMeasure 2.1 %b %b", Math.abs(lForTextViewRootWidth - lRoot_Width) >= 1,  Math.abs(lForTextViewRootHeight - lRoot_Height) >= 1));
//				if(lRoot_MeasureSpec_WidthMode == MeasureSpec.EXACTLY && lRoot_MeasureSpec_HeightMode == MeasureSpec.EXACTLY) {
//					// 둘중에 커진 놈 기준으로
//					if(lForTextViewRootWidth / lRoot_Width > lForTextViewRootHeight / lRoot_Height) {
//						lRoot_Height /= (lForTextViewRootWidth / lRoot_Width);
//						lRoot_Width = lForTextViewRootWidth;
//						log(String.format("onMeasure 2.1.1 %b %b", Math.abs(lForTextViewRootWidth - lRoot_Width) >= 1,  Math.abs(lForTextViewRootHeight - lRoot_Height) >= 1));
//					} else {
//						lRoot_Width /= (lForTextViewRootHeight / lRoot_Height);
//						lRoot_Height = lForTextViewRootHeight;
//						log(String.format("onMeasure 2.1.2 %b %b", Math.abs(lForTextViewRootWidth - lRoot_Width) >= 1,  Math.abs(lForTextViewRootHeight - lRoot_Height) >= 1));
//					}
////			} else if(lWidthMode == MeasureSpec.EXACTLY && lHeightMode != MeasureSpec.EXACTLY) {
////				lRootHeight /= (lForTextViewRootWidth / lRootWidth);
////				lRootWidth = lForTextViewRootWidth;
////			} else if(lWidthMode != MeasureSpec.EXACTLY && lHeightMode == MeasureSpec.EXACTLY) {
////				lRootWidth /= (lForTextViewRootHeight / lRootHeight);
////				lRootHeight = lForTextViewRootHeight;
//				} else {
//					lRoot_Width = lForTextViewRootWidth;
//					lRoot_Height = lForTextViewRootHeight;
//					log(String.format("onMeasure 2.1.3 %b %b", Math.abs(lForTextViewRootWidth - lRoot_Width) >= 1,  Math.abs(lForTextViewRootHeight - lRoot_Height) >= 1));
//				}
//				
////			postDelayedRequestLayout();
//			}
//		}
//		
//		{
//			log(String.format("onMeasure 2 lScale:%5.3f", (lRoot_Width/mScale_Root_Width)));
//			log(String.format("onMeasure 3 lScale:%5.3f ChildCount:%d Root:(%5.3f, %5.3f) ForTextView:(%5.3f, %5.3f)", 
//				(lRoot_Width/mScale_Root_Width), getChildCount(), lRoot_Width, lRoot_Height, lForTextViewRootWidth, lForTextViewRootHeight));
//			
//			float lScale_TextViewMeasure_Post = lRoot_Width / mScale_Root_Width;
//			
//			
//			
//			// 최종적으로 배치 위치를 세팅하기 위해 각 자식 뷰들의 layoutparams를 업데이트함
//			// update layoutparams of each child view to arrange finally 
//			float lTopMarginFromWeight = (lRoot_Height - (lRoot_Width * mRatioOfWidthHeight))/4;
//			log("  onMeasure ("+lRoot_Width+","+lRoot_Height+") Ratio:"+mRatioOfWidthHeight+" lScale_TextViewMeasure_Post:"+lScale_TextViewMeasure_Post+" lTopMarginFromWeight:"+lTopMarginFromWeight);
//			for (int i=0;i<getChildCount();i++) {
//				View lView = getChildAt(i);
//				
//				ScalableLayout.LayoutParams lParams = getChildLayoutParams(lView);
//				
//				boolean dif = false;
//				
//				if(lParams.leftMargin != Math.round(lScale_TextViewMeasure_Post * lParams.mScale_Left))
//					dif = true;
//				lParams.leftMargin = Math.round(lScale_TextViewMeasure_Post * lParams.mScale_Left);
//				
//				if(lParams.topMargin != Math.round(lScale_TextViewMeasure_Post * lParams.mScale_Top + lTopMarginFromWeight))
//					dif = true;
//				lParams.topMargin = Math.round(lScale_TextViewMeasure_Post * lParams.mScale_Top + lTopMarginFromWeight);
//				
//				int nParamsWidth = Math.round(lScale_TextViewMeasure_Post * (lParams.mScale_Left + lParams.mScale_Width)) - lParams.leftMargin;
//				if(lParams.width != nParamsWidth)
//					dif = true;
//				lParams.width = nParamsWidth;
//				
//				int nParamsHeight = Math.round(lScale_TextViewMeasure_Post * (lParams.mScale_Top + lParams.mScale_Height)) - lParams.topMargin;
//				if(lParams.height != nParamsHeight)
//					dif = true;
//				lParams.height = nParamsHeight;
//				
////			if(lView instanceof ScalableLayout) {
////				ScalableLayout lSL = (ScalableLayout) lView;
////				float lDif = ((float)lParams.width/(float)lParams.height) / lSL.mScale_Full_Width/lSL.mScale_Full_Height;
////				if(0.98f < lDif && lDif < 1.02f) {
////					
////				}
////			}
//				
////			lParams.rightMargin = lWidthSize - lParams.width - lParams.leftMargin;
////			lParams.bottomMargin = lHeightSize - lParams.height - lParams.topMargin;
////			
//////			lParams.setMargins(Math.round(lScale * lParams.mScale_Left), Math.round(lScale * lParams.mScale_Top + lTopMarginFromWeight), 0, 0);
////			log("  "+lView.toString()+" "+
////				String.format("(%f, %f, %f, %f)", lParams.mScale_Left, lParams.mScale_Top, lParams.mScale_Width, lParams.mScale_Height)+
////				"->"+
////				String.format("(%f, %f, %f, %f)", 
////					lScale * lParams.mScale_Left, lScale * lParams.mScale_Top + lTopMarginFromWeight, 
////					lScale * lParams.mScale_Width, lScale * lParams.mScale_Height));
////			log("  "+lView.toString()+" "+
////				String.format("(%f, %f, %f, %f)", lParams.mScale_Left, lParams.mScale_Top, lParams.mScale_Width, lParams.mScale_Height)+
////				"->"+
////				String.format("(%d, %d, %d, %d)", lParams.leftMargin, lParams.topMargin, lParams.width, lParams.height));
//				
//				
//				
//				if(dif) {
//					lView.setLayoutParams(lParams);
//				}
//				
////			if(lView instanceof TextView) {
//////				TextView v = (TextView) lView;
//////				if(v.getTextSize() != lParams.mScale_TextSize * lScale) {
//////					v.setTextSize(TypedValue.COMPLEX_UNIT_PX, lParams.mScale_TextSize * lScale);
//////				}
////				
////				
//////				switch (lParams.mScale_TextViewWrapContentMode) {
//////				case Horizontal: {
//////				} break;
//////				case Vertical: {
//////					dif = updateTextViewHeight(v, lParams);
//////				} break;
//////				}
////			}
////			
//////			else if(lView instanceof EditText) {
//////				EditText v = (EditText) lView;
//////				if(v.getTextSize() != lParams.mScale_TextSize * lScale) {
//////					v.setTextSize(TypedValue.COMPLEX_UNIT_PX, lParams.mScale_TextSize * lScale);
//////				}
//////			}
//			}
//		}
//		
//		log("onMeasure ================ End   "+this.toString());
//		super.onMeasure(MeasureSpec.makeMeasureSpec(Math.round(lRoot_Width), lRoot_MeasureSpec_WidthMode), MeasureSpec.makeMeasureSpec(Math.round(lRoot_Height), lRoot_MeasureSpec_HeightMode));
//		setMeasuredDimension(Math.round(lRoot_Width), Math.round(lRoot_Height));
//	}
	
	
	private boolean isDifferentSufficiently(float pNew, float pOld) {
		return isDifferentSufficiently(pNew, pOld, 1.1f);
	}
	private boolean isDifferentSufficiently(float pNew, float pOld, float pDiffDelta) {
		if( pNew < pOld / pDiffDelta || pOld * pDiffDelta < pNew ) {
			return true;
		}
		return false;
//		float val = (Math.abs(pNew - pOld)*100f/Math.max(pNew + pOld, 200f));
//		boolean ret = val >= 5f;
//		log("isDifferentSufficiently pNew:"+pNew+" pOld:"+pOld+" val:"+val+" ret:"+ret);
//		return ret;
	}
	
	private void updateTextViewSize(TextView pTV_Text, float pScale_TextViewMeasure_Pre) {
		refreshTextChangedListener(pTV_Text);
		
		ScalableLayout.LayoutParams lTV_SLLP = getChildLayoutParams(pTV_Text);
		TextView_WrapContent_Direction lTextView_WrapContent_Direction = lTV_SLLP.mTextView_WrapContent_Direction;
		if(lTextView_WrapContent_Direction == TextView_WrapContent_Direction.None) {
			return;
		}
		
//		switch (lTV_SLLP.mTextView_WrapContent_Direction) {
//		case Left: {
//			lForTextViewRootWidth  = updateTextViewWidth( v, lTV_SLLP.mTextView_WrapContent_Direction, lTV_SLLP, lForTextViewRootWidth, lForTextViewRootHeight);
//		} break;
//		case Right: {
//			lForTextViewRootWidth  = updateTextViewWidth( v, lTV_SLLP.mTextView_WrapContent_Direction, lTV_SLLP, lForTextViewRootWidth, lForTextViewRootHeight);
//		} break;
//		case Center_Horizontal: {
//			lForTextViewRootWidth  = updateTextViewWidth( v, lTV_SLLP.mTextView_WrapContent_Direction, lTV_SLLP, lForTextViewRootWidth, lForTextViewRootHeight);
//		} break;
//		case Top: {
//			lForTextViewRootHeight = updateTextViewSize(v, lForTextViewRootWidth, lForTextViewRootHeight, lScale_TextViewMeasure_Pre);
//		} break;
//		case Bottom: {
//			lForTextViewRootHeight = updateTextViewSize(v, lForTextViewRootWidth, lForTextViewRootHeight, lScale_TextViewMeasure_Pre);
//		} break;
//		}

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
			case Bottom: {
				int widthMeasureSpec 		= MeasureSpec.makeMeasureSpec((int) (lTextView_ScaleWidth_Old * pScale_TextViewMeasure_Pre), MeasureSpec.EXACTLY);
				int heightMeasureSpec 		= MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
				pTV_Text.measure(widthMeasureSpec, heightMeasureSpec);
				
				float lTextView_Height_New 	= pTV_Text.getMeasuredHeight() * 1.01f;
				lTextView_ScaleHeight_New 	= lTextView_Height_New / pScale_TextViewMeasure_Pre;
//				log("updateTextViewHeight 1.3 lTextSize:"+pTV_Text.getTextSize()+"->"+lTextSize_New);
//				log("updateTextViewHeight 1.4 lTextView_ScaleHeight:"+lTextView_ScaleHeight_Old+"->"+lTextView_ScaleHeight_New);
//				log("updateTextViewHeight 1.5 lTextView_Height:"+pTV_Text.getHeight()+"->"+lTextView_Height_New);
//				" getScaleHeight:"+getScaleHeight()+" pScale_TextViewMeasure_Pre:"+pScale_TextViewMeasure_Pre);
				// +" lTextView_ScaleHeight_New:"+lTextView_ScaleHeight_New+" getScaleHeight:"+getScaleHeight()+" pScale_TextViewMeasure_Pre:"+pScale_TextViewMeasure_Pre);
			} break;
			case Center_Horizontal:
			case Left:
			case Right: {
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
		
		if(	isDifferentSufficiently(lTextView_ScaleWidth_New, lTextView_ScaleWidth_Old) == false && 
			isDifferentSufficiently(lTextView_ScaleHeight_New, lTextView_ScaleHeight_Old) == false) {
			return;
		}
		
		float lTextView_ScaleWidth_Diff 	= lTextView_ScaleWidth_New - lTextView_ScaleWidth_Old;
		float lTextView_ScaleHeight_Diff 	= lTextView_ScaleHeight_New - lTextView_ScaleHeight_Old;
		if(lTV_SLLP.mTextView_WrapContent_MoveSiblings == true) {
//			log("updateTextViewHeight 1.3 lTextSize_New:"+lTextSize_New+" lTextView_OldScaleHeight:"+lTextView_ScaleHeight_Old+" lTextView_ScaleHeight_New:"+lTextView_ScaleHeight_New+" getScaleHeight:"+getScaleHeight()+" pScale_TextViewMeasure_Pre:"+pScale_TextViewMeasure_Pre);
//			log("updateTextViewHeight 3 lTextSize_New:"+lTextSize_New+" lTextView_Width:"+lTextView_Width+" lTextView_OldScaleHeight:"+lTextView_ScaleHeight_Old+" pRootHeight:"+pRootHeight+" getScaleHeight:"+getScaleHeight()+" pScale_TextViewMeasure_Pre:"+pScale_TextViewMeasure_Pre);
//			log("updateTextViewHeight 3 (lTextView_NewHeight - lTextView_OldHeight):"+(lTextView_NewHeight+"-"+lTextView_Height_Old));
//			log("updateTextViewHeight 3 (lTextView_NewHeight - lTextView_OldHeight):"+(lTextView_NewHeight - lTextView_Height_Old));
//			log("updateTextViewHeight 3 (lTextView_NewHeight - lTextView_OldHeight):"+(Math.abs(lTextView_NewHeight - lTextView_Height_Old)*100f/Math.max(lTextView_NewHeight + lTextView_Height_Old, 200f)));
			
			for(int i = 0; i < getChildCount(); i++) {
				View v = getChildAt(i);
				if(v == pTV_Text) {
					continue;
				}
				ScalableLayout.LayoutParams lSLLP = getChildLayoutParams(v);
				
				switch (lTextView_WrapContent_Direction) {
				case Top: {
					// 자신의 위에 있는 뷰들 위치 이동
					if(
						lSLLP.getScale_Bottom() <= lTV_SLLP.getScale_Bottom() &&
						(
							( lTV_SLLP.getScale_Left() <= lSLLP.getScale_Left() && lSLLP.getScale_Left() <= lTV_SLLP.getScale_Right())
							|| 
							( lTV_SLLP.getScale_Left() <= lSLLP.getScale_Right() && lSLLP.getScale_Right() <= lTV_SLLP.getScale_Right()) 
							||
							( lSLLP.getScale_Left() <= lTV_SLLP.getScale_Left() && lTV_SLLP.getScale_Right() <= lSLLP.getScale_Right())
							)) {
						moveChildView(v, lSLLP.getScale_Left(), lSLLP.getScale_Top()-lTextView_ScaleHeight_Diff);
					}
					// 자신을 포함하는 뷰들 크기 변경
					else if( lTV_SLLP.mTextView_WrapContent_ResizeSurrounded &&
						lSLLP.getScale_Top() <= lTV_SLLP.getScale_Top() &&
						lSLLP.getScale_Left() <= lTV_SLLP.getScale_Left() &&
						lSLLP.getScale_Right() >= lTV_SLLP.getScale_Right() &&
						lSLLP.getScale_Bottom() >= lTV_SLLP.getScale_Bottom()) {
						
						moveChildView(v, 
							lSLLP.getScale_Left(), lSLLP.getScale_Top()-lTextView_ScaleHeight_Diff, 
							lSLLP.getScale_Width(), lSLLP.getScale_Height()+lTextView_ScaleHeight_Diff);
					}
				} break;
				case Bottom: {
					// 자신의 아래에 있는 뷰들 위치 이동
					if(
						lSLLP.getScale_Top() >= lTV_SLLP.getScale_Top() &&
						(
							( lTV_SLLP.getScale_Left() <= lSLLP.getScale_Left() && lSLLP.getScale_Left() <= lTV_SLLP.getScale_Right())
							|| 
							( lTV_SLLP.getScale_Left() <= lSLLP.getScale_Right() && lSLLP.getScale_Right() <= lTV_SLLP.getScale_Right()) 
							||
							( lSLLP.getScale_Left() <= lTV_SLLP.getScale_Left() && lTV_SLLP.getScale_Right() <= lSLLP.getScale_Right())
							)) {
						moveChildView(v, lSLLP.getScale_Left(), lSLLP.getScale_Top()+lTextView_ScaleHeight_Diff);
					}
					// 자신을 포함하는 뷰들 크기 변경
					else if( lTV_SLLP.mTextView_WrapContent_ResizeSurrounded &&
						lSLLP.getScale_Top() <= lTV_SLLP.getScale_Top() &&
						lSLLP.getScale_Left() <= lTV_SLLP.getScale_Left() &&
						lSLLP.getScale_Right() >= lTV_SLLP.getScale_Right() &&
						lSLLP.getScale_Bottom() >= lTV_SLLP.getScale_Bottom()) {
						
						moveChildView(v, 
							lSLLP.getScale_Left(), lSLLP.getScale_Top(), 
							lSLLP.getScale_Width(), lSLLP.getScale_Height()+lTextView_ScaleHeight_Diff);
					}
				} break;
				case Left: {
					// move views on left
					// 왼쪽에 있는 뷰들 위치 이동
					if(
						lSLLP.getScale_Right() <= lTV_SLLP.getScale_Right() &&
						(
							( lTV_SLLP.getScale_Top() <= lSLLP.getScale_Top() && lSLLP.getScale_Top() <= lTV_SLLP.getScale_Bottom())
							|| 
							( lTV_SLLP.getScale_Top() <= lSLLP.getScale_Bottom() && lSLLP.getScale_Bottom() <= lTV_SLLP.getScale_Bottom()) 
							||
							( lTV_SLLP.getScale_Top() >= lSLLP.getScale_Top() &&  lSLLP.getScale_Bottom() >= lTV_SLLP.getScale_Bottom())
							)) {
						moveChildView(v, lSLLP.getScale_Left()-lTextView_ScaleWidth_Diff, lSLLP.getScale_Top());
					}
					else if( lTV_SLLP.mTextView_WrapContent_ResizeSurrounded &&
						lSLLP.getScale_Top() <= lTV_SLLP.getScale_Top() &&
						lSLLP.getScale_Left() <= lTV_SLLP.getScale_Left() &&
						lSLLP.getScale_Right() >= lTV_SLLP.getScale_Right() &&
						lSLLP.getScale_Bottom() >= lTV_SLLP.getScale_Bottom()) {
						
						moveChildView(v, lSLLP.getScale_Left()-lTextView_ScaleWidth_Diff, lSLLP.getScale_Top(), lSLLP.getScale_Width()+lTextView_ScaleWidth_Diff, lSLLP.getScale_Height());
					}
				} break;
				case Right: {
					// move views on right
					// 오른쪽에 있는 뷰들 위치 이동					
					if(
						lSLLP.getScale_Left() >= lTV_SLLP.getScale_Left() &&
						(
							( lTV_SLLP.getScale_Top() <= lSLLP.getScale_Top() && lSLLP.getScale_Top() <= lTV_SLLP.getScale_Bottom())
							|| 
							( lTV_SLLP.getScale_Top() <= lSLLP.getScale_Bottom() && lSLLP.getScale_Bottom() <= lTV_SLLP.getScale_Bottom()) 
							||
							( lTV_SLLP.getScale_Top() >= lSLLP.getScale_Top() &&  lSLLP.getScale_Bottom() >= lTV_SLLP.getScale_Bottom())
							)) {							
						
						moveChildView(v, lSLLP.getScale_Left()+lTextView_ScaleWidth_Diff, lSLLP.getScale_Top());
					}
					else if( lTV_SLLP.mTextView_WrapContent_ResizeSurrounded &&
						lSLLP.getScale_Top() <= lTV_SLLP.getScale_Top() &&
						lSLLP.getScale_Left() <= lTV_SLLP.getScale_Left() &&
						lSLLP.getScale_Right() >= lTV_SLLP.getScale_Right() &&
						lSLLP.getScale_Bottom() >= lTV_SLLP.getScale_Bottom()) {
						
						moveChildView(v, lSLLP.getScale_Left(), lSLLP.getScale_Top(), lSLLP.getScale_Width()+lTextView_ScaleWidth_Diff, lSLLP.getScale_Height());
					}
				} break;
				case Center_Horizontal: {
					// 왼쪽에 있는 뷰들 위치 이동
//					log("Center_Horizontal "
//							+ "v:"+v+"\n"
//							+ "lSLLP:"+lSLLP+" \n"
//							+ "lTV_SLLP:"+lTV_SLLP+" \n"
//							+ "1 "+ (lSLLP.getScale_Right() <= lTV_SLLP.getScale_Right()) + "\n"
//							+ "2 " + ( lTV_SLLP.getScale_Top() <= lSLLP.getScale_Top() && lSLLP.getScale_Top() <= lTV_SLLP.getScale_Bottom()) + "\n"
//							+ "3 " + ( lTV_SLLP.getScale_Top() <= lSLLP.getScale_Top() && lSLLP.getScale_Top() <= lTV_SLLP.getScale_Bottom()) + "\n" 
//							+ "4 " + ( lTV_SLLP.getScale_Top() <= lSLLP.getScale_Top() && lSLLP.getScale_Top() <= lTV_SLLP.getScale_Bottom())
//							);
					if(
						lSLLP.getScale_Right() <= lTV_SLLP.getScale_Right() &&
						(
							( lTV_SLLP.getScale_Top() <= lSLLP.getScale_Top() && lSLLP.getScale_Top() <= lTV_SLLP.getScale_Bottom())
							|| 
							( lTV_SLLP.getScale_Top() <= lSLLP.getScale_Bottom() && lSLLP.getScale_Bottom() <= lTV_SLLP.getScale_Bottom()) 
							||
							( lTV_SLLP.getScale_Top() >= lSLLP.getScale_Top() &&  lSLLP.getScale_Bottom() >= lTV_SLLP.getScale_Bottom())
							)) {
						
//						log("moveChildView "
//								+ "v:"+v+"\n"
//								+ "1 " + ( lSLLP.getScale_Left()-lTextView_ScaleWidth_Diff/2)+"\n"
//								+ "2 " + ( lSLLP.getScale_Top() ) + "\n"
//								);
						moveChildView(v, lSLLP.getScale_Left()-lTextView_ScaleWidth_Diff/2, lSLLP.getScale_Top());
					}
					else if( lTV_SLLP.mTextView_WrapContent_ResizeSurrounded &&
						lSLLP.getScale_Top() <= lTV_SLLP.getScale_Top() &&
						lSLLP.getScale_Left() <= lTV_SLLP.getScale_Left() &&
						lSLLP.getScale_Right() >= lTV_SLLP.getScale_Right() &&
						lSLLP.getScale_Bottom() >= lTV_SLLP.getScale_Bottom()) {
						
						moveChildView(v, lSLLP.getScale_Left()-lTextView_ScaleWidth_Diff/2, lSLLP.getScale_Top(), lSLLP.getScale_Width()+lTextView_ScaleWidth_Diff/2, lSLLP.getScale_Height());
					}
					
//					// 오른쪽에 있는 뷰들 위치 이동
//					if(
//						lSLLP.getScale_Left() >= lTV_SLLP.getScale_Left() &&
//						(
//							( lTV_SLLP.getScale_Top() <= lTV_SLLP.getScale_Top() && lSLLP.getScale_Top() <= lTV_SLLP.getScale_Bottom())
//							|| 
//							( lTV_SLLP.getScale_Top() <= lTV_SLLP.getScale_Bottom() && lTV_SLLP.getScale_Bottom() <= lTV_SLLP.getScale_Bottom()) 
//							||
//							( lTV_SLLP.getScale_Top() >= lTV_SLLP.getScale_Top() &&  lTV_SLLP.getScale_Bottom() >= lTV_SLLP.getScale_Bottom())
//							)) {
//						
//						moveChildView(v, lTV_SLLP.getScale_Left()+lTextView_ScaleWidth_Diff/2, lTV_SLLP.getScale_Top());
//					}
//					else if( lTV_SLLP.mTextView_WrapContent_ResizeSurrounded &&
//						lTV_SLLP.getScale_Top() <= lTV_SLLP.getScale_Top() &&
//						lTV_SLLP.getScale_Left() <= lTV_SLLP.getScale_Left() &&
//						lTV_SLLP.getScale_Right() >= lTV_SLLP.getScale_Right() &&
//						lTV_SLLP.getScale_Bottom() >= lTV_SLLP.getScale_Bottom()) {
//						
////						moveChildView(v, lTV_SLLP.getScale_Left(), lTV_SLLP.getScale_Top(), lTV_SLLP.getScale_Width()+lTextView_ScaleWidth_Diff/2, lTV_SLLP.getScale_Height());
//						moveChildView(v, lSLLP.getScale_Left()-lTextView_ScaleWidth_Diff/2, lSLLP.getScale_Top(), lSLLP.getScale_Width()+lTextView_ScaleWidth_Diff/2, lSLLP.getScale_Height());
//					}
					

					// 오른쪽에 있는 뷰들 위치 이동					
					else if(
						lSLLP.getScale_Left() >= lTV_SLLP.getScale_Left() &&
						(
							( lTV_SLLP.getScale_Top() <= lSLLP.getScale_Top() && lSLLP.getScale_Top() <= lTV_SLLP.getScale_Bottom())
							|| 
							( lTV_SLLP.getScale_Top() <= lSLLP.getScale_Bottom() && lSLLP.getScale_Bottom() <= lTV_SLLP.getScale_Bottom()) 
							||
							( lTV_SLLP.getScale_Top() >= lSLLP.getScale_Top() &&  lSLLP.getScale_Bottom() >= lTV_SLLP.getScale_Bottom())
							)) {
						moveChildView(v, lSLLP.getScale_Left()+lTextView_ScaleWidth_Diff/2, lSLLP.getScale_Top());
					}
					else if( lTV_SLLP.mTextView_WrapContent_ResizeSurrounded &&
						lSLLP.getScale_Top() <= lTV_SLLP.getScale_Top() &&
						lSLLP.getScale_Left() <= lTV_SLLP.getScale_Left() &&
						lSLLP.getScale_Right() >= lTV_SLLP.getScale_Right() &&
						lSLLP.getScale_Bottom() >= lTV_SLLP.getScale_Bottom()) {
						
						moveChildView(v, lSLLP.getScale_Left(), lSLLP.getScale_Top(), lSLLP.getScale_Width()+lTextView_ScaleWidth_Diff/2, lSLLP.getScale_Height());
					}
				} break;
				default:
					break;
				}
			}
		}
		
		switch (lTextView_WrapContent_Direction) {
		case Top: {
			moveChildView(pTV_Text, lTV_SLLP.getScale_Left(), lTV_SLLP.getScale_Top()-lTextView_ScaleHeight_Diff, lTV_SLLP.getScale_Width(), lTextView_ScaleHeight_New);
		} break;
		case Bottom: {
			moveChildView(pTV_Text, lTV_SLLP.getScale_Left(), lTV_SLLP.getScale_Top(), lTV_SLLP.getScale_Width(), lTextView_ScaleHeight_New);
		} break;
		case Left: {
			moveChildView(pTV_Text, lTV_SLLP.getScale_Left()-lTextView_ScaleWidth_Diff, lTV_SLLP.getScale_Top(), lTextView_ScaleWidth_New, lTV_SLLP.getScale_Height());
		} break;
		case Right: {
			moveChildView(pTV_Text, lTV_SLLP.getScale_Left(), lTV_SLLP.getScale_Top(), lTextView_ScaleWidth_New, lTV_SLLP.getScale_Height());
		} break;
		case Center_Horizontal: {
			moveChildView(pTV_Text, lTV_SLLP.getScale_Left()-lTextView_ScaleWidth_Diff/2, lTV_SLLP.getScale_Top(), lTextView_ScaleWidth_New, lTV_SLLP.getScale_Height());
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




	public static enum TextView_WrapContent_Direction {
		None, Left, Right, Center_Horizontal, Top, Bottom,
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

