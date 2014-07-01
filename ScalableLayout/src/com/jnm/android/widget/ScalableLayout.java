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
//			post(new Runnable() {
//				@Override
//				public void run() {
//					log("setScaleSize Invalidate ");
//					requestLayout();
//					forceLayout();
//				}
//			});
		}
	}
	
	public ScalableLayout(Context pContext) {
		this(pContext, Default_Scale_Base_Width, Default_Scale_Base_Height);
	}
	
	public ScalableLayout(Context pContext, AttributeSet pAttrs) {
		this(pContext, pAttrs, 
			pContext.obtainStyledAttributes(pAttrs, R.styleable.ScalableLayout).getFloat(R.styleable.ScalableLayout_scale_base_width, Default_Scale_Base_Width), 
			pContext.obtainStyledAttributes(pAttrs, R.styleable.ScalableLayout).getFloat(R.styleable.ScalableLayout_scale_base_height, Default_Scale_Base_Height)
			);
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
	/**
	 * @param pTextView target textview to wrap content
	 * @param pDirection 
	 * @param pRescaleSurrounded 
	 */
	public void setTextView_WrapContent(TextView pTextView, TextView_WrapContent_Direction pDirection, boolean pRescaleSurrounded) {
		getChildLayoutParams(pTextView).setTextView_WrapContent(pDirection, pRescaleSurrounded);
		refreshTextChangedListener(pTextView);
	}
	private TextWatcher mTextWatcher = new TextWatcher() {
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
	};
//	private void refreshTextChangedListener(TextView pTextView, TextView_WrapContent_Direction pDirection) {
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
//		log("generateLayoutParams AttributeSet: "+pAttrs);
//		for(int i=0;i<pAttrs.getAttributeCount();i++) { 
//			log("attr["+i+"] "+pAttrs.getAttributeName(i)+":"+pAttrs.getAttributeValue(i));
//			
//		}
		
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
//		try {
//			dir = TextView_WrapContent_Direction.valueOf(attrs.getString(R.styleable.ScalableLayout_textview_wrapcontent_direction));
//		} catch (Throwable e) {
//			dir = TextView_WrapContent_Direction.None;
//		}
		
		return new ScalableLayout.LayoutParams(
			attrs.getFloat(R.styleable.ScalableLayout_scale_left, Default_Scale_Left),
			attrs.getFloat(R.styleable.ScalableLayout_scale_top, Default_Scale_Top),
			attrs.getFloat(R.styleable.ScalableLayout_scale_width, Default_Scale_Width),
			attrs.getFloat(R.styleable.ScalableLayout_scale_height, Default_Scale_Height),
			attrs.getFloat(R.styleable.ScalableLayout_scale_textsize, Default_Scale_TextSize),
			dir,
			attrs.getBoolean(R.styleable.ScalableLayout_textview_wrapcontent_resizesurrounded, false)
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
	
	
	private float updateTextViewWidth(TextView pTV_Text, TextView_WrapContent_Direction pTextView_WrapContent_Direction, ScalableLayout.LayoutParams pTV_SLLP, float pBGWidth, float pBGHeight) {
		float lTextView_OldScaleWidth = pTV_SLLP.getScale_Width();
		float lTextView_OldWidth = pTV_Text.getWidth();
		
		log("updateTextViewWidth lOldViewWidth:"+lTextView_OldWidth+" lOldViewScaleWidth:"+lTextView_OldScaleWidth+" pBGWidth:"+pBGWidth+" getScaleWidth:"+getScaleWidth() +"pTextView_WrapContent_Direction:"+pTextView_WrapContent_Direction);
		if(lTextView_OldWidth <= 0 || lTextView_OldScaleWidth <= 0 || pBGWidth <= 0 || getScaleWidth() <= 0) {
			postDelayed(new Runnable() {
				@Override
				public void run() {
//					log("updateTextViewWidth 2 ");
					requestLayout();
					forceLayout();
				}
			}, 10);
			return pBGWidth;
		}
		
		int widthMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
//		int heightMeasureSpec = MeasureSpec.makeMeasureSpec(pTV_Text.getHeight(), MeasureSpec.AT_MOST);
		int heightMeasureSpec = MeasureSpec.makeMeasureSpec((int)(pTV_Text.getHeight()/1.1f), MeasureSpec.EXACTLY);
		pTV_Text.measure(widthMeasureSpec, heightMeasureSpec);
		float lTextView_NewWidth = pTV_Text.getMeasuredWidth();
		float lTextView_NewScaleWidth = (lTextView_NewWidth * getScaleWidth() / pBGWidth)*1.05f; // 정확하게 측정하지 못할때를 대비하기 위해서 공간을 좀 확보하기 위함이다. 그치만 안 좋은것같음
		
		float lTextView_DiffScaleWidth = lTextView_NewScaleWidth - lTextView_OldScaleWidth;
		
				
		float lRootView_OldScaleWidth = getScaleWidth();
		if(Math.abs(lTextView_NewWidth - lTextView_OldWidth) * 100 > pBGWidth && Math.abs(lTextView_DiffScaleWidth) > getScaleWidth() / 100f) {
			for(int i=0;i<getChildCount();i++) {
				View v = getChildAt(i);
				if(v == pTV_Text) {
					continue;
				}
				ScalableLayout.LayoutParams lV_SLLP = (ScalableLayout.LayoutParams) v.getLayoutParams();
				switch (pTextView_WrapContent_Direction) {
				case Left: {
					// 왼쪽에 있는 뷰들 위치 이동
					if(
						lV_SLLP.getScale_Right() <= pTV_SLLP.getScale_Right() &&
						(
							( pTV_SLLP.getScale_Top() <= lV_SLLP.getScale_Top() && lV_SLLP.getScale_Top() <= pTV_SLLP.getScale_Bottom())
							|| 
							( pTV_SLLP.getScale_Top() <= lV_SLLP.getScale_Bottom() && lV_SLLP.getScale_Bottom() <= pTV_SLLP.getScale_Bottom()) 
							||
							( pTV_SLLP.getScale_Top() >= lV_SLLP.getScale_Top() &&  lV_SLLP.getScale_Bottom() >= pTV_SLLP.getScale_Bottom())
							)) {
						
						moveChildView(v, lV_SLLP.getScale_Left()-lTextView_DiffScaleWidth, lV_SLLP.getScale_Top());
					}
					else if( pTV_SLLP.mTextView_WrapContent_ResizeSurrounded &&
						lV_SLLP.getScale_Top() <= pTV_SLLP.getScale_Top() &&
						lV_SLLP.getScale_Left() <= pTV_SLLP.getScale_Left() &&
						lV_SLLP.getScale_Right() >= pTV_SLLP.getScale_Right() &&
						lV_SLLP.getScale_Bottom() >= pTV_SLLP.getScale_Bottom()) {
						
						moveChildView(v, lV_SLLP.getScale_Left()-lTextView_DiffScaleWidth, lV_SLLP.getScale_Top(), lV_SLLP.getScale_Width()+lTextView_DiffScaleWidth, lV_SLLP.getScale_Height());
					}
				} break;
				case Right: {
					// 오른쪽에 있는 뷰들 위치 이동					
					if(
						lV_SLLP.getScale_Left() >= pTV_SLLP.getScale_Left() &&
						(
							( pTV_SLLP.getScale_Top() <= lV_SLLP.getScale_Top() && lV_SLLP.getScale_Top() <= pTV_SLLP.getScale_Bottom())
							|| 
							( pTV_SLLP.getScale_Top() <= lV_SLLP.getScale_Bottom() && lV_SLLP.getScale_Bottom() <= pTV_SLLP.getScale_Bottom()) 
							||
							( pTV_SLLP.getScale_Top() >= lV_SLLP.getScale_Top() &&  lV_SLLP.getScale_Bottom() >= pTV_SLLP.getScale_Bottom())
							)) {							
						
						moveChildView(v, lV_SLLP.getScale_Left()+lTextView_DiffScaleWidth, lV_SLLP.getScale_Top());
					}
					else if( pTV_SLLP.mTextView_WrapContent_ResizeSurrounded &&
						lV_SLLP.getScale_Top() <= pTV_SLLP.getScale_Top() &&
						lV_SLLP.getScale_Left() <= pTV_SLLP.getScale_Left() &&
						lV_SLLP.getScale_Right() >= pTV_SLLP.getScale_Right() &&
						lV_SLLP.getScale_Bottom() >= pTV_SLLP.getScale_Bottom()) {
						
						moveChildView(v, lV_SLLP.getScale_Left(), lV_SLLP.getScale_Top(), lV_SLLP.getScale_Width()+lTextView_DiffScaleWidth, lV_SLLP.getScale_Height());
					}
				} break;
				case Center_Horizontal: {
					// 왼쪽에 있는 뷰들 위치 이동
					if(
						lV_SLLP.getScale_Right() <= pTV_SLLP.getScale_Right() &&
						(
							( pTV_SLLP.getScale_Top() <= lV_SLLP.getScale_Top() && lV_SLLP.getScale_Top() <= pTV_SLLP.getScale_Bottom())
							|| 
							( pTV_SLLP.getScale_Top() <= lV_SLLP.getScale_Bottom() && lV_SLLP.getScale_Bottom() <= pTV_SLLP.getScale_Bottom()) 
							||
							( pTV_SLLP.getScale_Top() >= lV_SLLP.getScale_Top() &&  lV_SLLP.getScale_Bottom() >= pTV_SLLP.getScale_Bottom())
							)) {
						
						moveChildView(v, lV_SLLP.getScale_Left()-lTextView_DiffScaleWidth/2, lV_SLLP.getScale_Top());
					}
					else if( pTV_SLLP.mTextView_WrapContent_ResizeSurrounded &&
						lV_SLLP.getScale_Top() <= pTV_SLLP.getScale_Top() &&
						lV_SLLP.getScale_Left() <= pTV_SLLP.getScale_Left() &&
						lV_SLLP.getScale_Right() >= pTV_SLLP.getScale_Right() &&
						lV_SLLP.getScale_Bottom() >= pTV_SLLP.getScale_Bottom()) {
						
						moveChildView(v, lV_SLLP.getScale_Left()-lTextView_DiffScaleWidth/2, lV_SLLP.getScale_Top(), lV_SLLP.getScale_Width()+lTextView_DiffScaleWidth/2, lV_SLLP.getScale_Height());
					}
					
					// 오른쪽에 있는 뷰들 위치 이동					
					if(
						lV_SLLP.getScale_Left() >= pTV_SLLP.getScale_Left() &&
						(
							( pTV_SLLP.getScale_Top() <= lV_SLLP.getScale_Top() && lV_SLLP.getScale_Top() <= pTV_SLLP.getScale_Bottom())
							|| 
							( pTV_SLLP.getScale_Top() <= lV_SLLP.getScale_Bottom() && lV_SLLP.getScale_Bottom() <= pTV_SLLP.getScale_Bottom()) 
							||
							( pTV_SLLP.getScale_Top() >= lV_SLLP.getScale_Top() &&  lV_SLLP.getScale_Bottom() >= pTV_SLLP.getScale_Bottom())
							)) {							
						
						moveChildView(v, lV_SLLP.getScale_Left()+lTextView_DiffScaleWidth/2, lV_SLLP.getScale_Top());
					}
					else if( pTV_SLLP.mTextView_WrapContent_ResizeSurrounded &&
						lV_SLLP.getScale_Top() <= pTV_SLLP.getScale_Top() &&
						lV_SLLP.getScale_Left() <= pTV_SLLP.getScale_Left() &&
						lV_SLLP.getScale_Right() >= pTV_SLLP.getScale_Right() &&
						lV_SLLP.getScale_Bottom() >= pTV_SLLP.getScale_Bottom()) {
						
						moveChildView(v, lV_SLLP.getScale_Left(), lV_SLLP.getScale_Top(), lV_SLLP.getScale_Width()+lTextView_DiffScaleWidth/2, lV_SLLP.getScale_Height());
					}
				} break;
				}
			}
			
			switch (pTextView_WrapContent_Direction) {
			case Left: {
				moveChildView(pTV_Text, pTV_SLLP.getScale_Left()-(lTextView_DiffScaleWidth), pTV_SLLP.getScale_Top(), lTextView_NewScaleWidth, pTV_SLLP.getScale_Height());
			} break;
			case Right: {
				moveChildView(pTV_Text, pTV_SLLP.getScale_Left(), pTV_SLLP.getScale_Top(), lTextView_NewScaleWidth, pTV_SLLP.getScale_Height());
			} break;
			case Center_Horizontal: {
				moveChildView(pTV_Text, pTV_SLLP.getScale_Left()-(lTextView_DiffScaleWidth)/2, pTV_SLLP.getScale_Top(), lTextView_NewScaleWidth, pTV_SLLP.getScale_Height());
			} break;
			}
			
			if(pTV_SLLP.mTextView_WrapContent_ResizeSurrounded) {
				setScaleSize(getScaleWidth()+lTextView_NewScaleWidth-lTextView_OldScaleWidth, getScaleHeight(), true);
			}
		}
		return pBGWidth*getScaleWidth()/lRootView_OldScaleWidth;
	}
	private float updateTextViewHeight(TextView pTV_Text, TextView_WrapContent_Direction pTextView_WrapContent_Direction, ScalableLayout.LayoutParams pTV_SLLP, float pBGWidth, float pBGHeight) {
		float lTextView_OldHeight = pTV_Text.getHeight();
		float lTextView_OldScaleHeight = pTV_SLLP.getScale_Height();
		
		log("updateTextViewHeight 1 lOldViewHeight:"+lTextView_OldHeight+" lOldViewScaleHeight:"+lTextView_OldScaleHeight+" pBGHeight:"+pBGHeight+" getScaleHeight:"+getScaleHeight());
		if(lTextView_OldHeight <= 0 || lTextView_OldScaleHeight <= 0 || pBGHeight <= 0 || getScaleHeight() <= 0) {
//		if(lTextView_OldScaleHeight <= 0 || pBGHeight <= 0 || getScaleHeight() <= 0) {
			postDelayed(new Runnable() {
				@Override
				public void run() {
//					log("updateTextViewHeight 2 ");
					requestLayout();
					forceLayout();
				}
			}, 10);
//			log("updateTextViewHeight 1 ret "+(pBGHeight));
			return pBGHeight;
		}
		
//		int widthMeasureSpec = MeasureSpec.makeMeasureSpec(pTV_Text.getWidth(), MeasureSpec.AT_MOST);
		int widthMeasureSpec = MeasureSpec.makeMeasureSpec((int)(pTV_Text.getWidth()/1.1f), MeasureSpec.EXACTLY);
		int heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
//		int heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.AT_MOST);
		pTV_Text.measure(widthMeasureSpec, heightMeasureSpec);
		float lTextView_NewHeight = pTV_Text.getMeasuredHeight();
		float lTextView_NewScaleHeight = (lTextView_NewHeight * getScaleHeight() / pBGHeight);
		log(String.format("updateTextViewHeight %f = %f * %f / %f TextSize:%f", lTextView_NewScaleHeight, lTextView_NewHeight, getScaleHeight(), pBGHeight, pTV_Text.getTextSize()));
		
//		log("updateTextViewHeight "+lOldViewHeight+","+lNewViewHeight+" "+lOldScaleHeight+","+lNewScaleHeight);
		log("updateTextViewHeight 2 lOldViewHeight:"+lTextView_OldHeight+" lOldViewScaleHeight:"+lTextView_OldScaleHeight+" pBGHeight:"+pBGHeight+" getScaleHeight:"+getScaleHeight());
		
		log(String.format("updateTextViewHeight1 View: %5.3f -> %5.3f Scale: %5.3f -> %5.3f", lTextView_OldHeight, lTextView_NewHeight, lTextView_OldScaleHeight, lTextView_NewScaleHeight));
		log(String.format("updateTextViewHeight2 Scalable Scale: %5.3f,%5.3f ", getScaleWidth(), getScaleHeight()));
		log(String.format("updateTextViewHeight3 Scalable View: %5.3f,%5.3f", pBGWidth, pBGHeight));

		float lTextView_DiffScaleHeight = lTextView_NewScaleHeight - lTextView_OldScaleHeight;
		float lRootView_OldScaleHeight = getScaleHeight();
		
		if(Math.abs(lTextView_NewHeight - lTextView_OldHeight) * 100 > pBGHeight && Math.abs(lTextView_DiffScaleHeight) > getScaleHeight() / 100f) {
			log("updateTextViewHeight 3 lOldViewHeight:"+lTextView_OldHeight+" lOldViewScaleHeight:"+lTextView_OldScaleHeight+" pBGHeight:"+pBGHeight+" getScaleHeight:"+getScaleHeight());
			for(int i=0;i<getChildCount();i++) {
				View v = getChildAt(i);
				if(v == pTV_Text) {
					continue;
				}
				ScalableLayout.LayoutParams lSLLP = (ScalableLayout.LayoutParams) v.getLayoutParams();

//				log(String.format("lSLLP:"+lSLLP.toString()));
//				log(String.format("pTV_SLLP:"+pTV_SLLP.toString()));
//					getScaleHeight(), getScaleHeight()+lNewViewScaleHeight-lOldViewScaleHeight));
				switch (pTextView_WrapContent_Direction) {
				case Top: {
					// 자신의 위에 있는 뷰들 위치 이동
					if(
						lSLLP.getScale_Bottom() <= pTV_SLLP.getScale_Bottom() &&
						(
							( pTV_SLLP.getScale_Left() <= lSLLP.getScale_Left() && lSLLP.getScale_Left() <= pTV_SLLP.getScale_Right())
							|| 
							( pTV_SLLP.getScale_Left() <= lSLLP.getScale_Right() && lSLLP.getScale_Right() <= pTV_SLLP.getScale_Right()) 
							||
							( lSLLP.getScale_Left() <= pTV_SLLP.getScale_Left() && pTV_SLLP.getScale_Right() <= lSLLP.getScale_Right())
							)) {
						
						moveChildView(v, lSLLP.getScale_Left(), lSLLP.getScale_Top()-lTextView_DiffScaleHeight);
					}
					// 자신을 포함하는 뷰들 크기 변경
					else if( pTV_SLLP.mTextView_WrapContent_ResizeSurrounded &&
						lSLLP.getScale_Top() <= pTV_SLLP.getScale_Top() &&
						lSLLP.getScale_Left() <= pTV_SLLP.getScale_Left() &&
						lSLLP.getScale_Right() >= pTV_SLLP.getScale_Right() &&
						lSLLP.getScale_Bottom() >= pTV_SLLP.getScale_Bottom()) {
						
						moveChildView(v, 
							lSLLP.getScale_Left(), lSLLP.getScale_Top()-lTextView_DiffScaleHeight, 
							lSLLP.getScale_Width(), lSLLP.getScale_Height()+lTextView_DiffScaleHeight);
					}
				} break;
				case Bottom: {
					// 자신의 아래에 있는 뷰들 위치 이동
					if(
						lSLLP.getScale_Top() >= pTV_SLLP.getScale_Top() &&
						(
							( pTV_SLLP.getScale_Left() <= lSLLP.getScale_Left() && lSLLP.getScale_Left() <= pTV_SLLP.getScale_Right())
							|| 
							( pTV_SLLP.getScale_Left() <= lSLLP.getScale_Right() && lSLLP.getScale_Right() <= pTV_SLLP.getScale_Right()) 
							||
							( lSLLP.getScale_Left() <= pTV_SLLP.getScale_Left() && pTV_SLLP.getScale_Right() <= lSLLP.getScale_Right())
//							( pTV_SLLP.getScale_Top() >= lSLLP.getScale_Top() &&  lSLLP.getScale_Bottom() >= pTV_SLLP.getScale_Bottom())
							)) {
						
						moveChildView(v, lSLLP.getScale_Left(), lSLLP.getScale_Top()+lTextView_DiffScaleHeight);
					}
					// 자신을 포함하는 뷰들 크기 변경
					else if( pTV_SLLP.mTextView_WrapContent_ResizeSurrounded &&
						lSLLP.getScale_Top() <= pTV_SLLP.getScale_Top() &&
						lSLLP.getScale_Left() <= pTV_SLLP.getScale_Left() &&
						lSLLP.getScale_Right() >= pTV_SLLP.getScale_Right() &&
						lSLLP.getScale_Bottom() >= pTV_SLLP.getScale_Bottom()) {
						
						moveChildView(v, 
							lSLLP.getScale_Left(), lSLLP.getScale_Top(), 
							lSLLP.getScale_Width(), lSLLP.getScale_Height()+lTextView_DiffScaleHeight);
					}
				} break;
				}
			}
			
			switch (pTextView_WrapContent_Direction) {
			case Top: {
			} break;
			case Bottom: {
				moveChildView(pTV_Text, pTV_SLLP.getScale_Left(), pTV_SLLP.getScale_Top(), pTV_SLLP.getScale_Width(), lTextView_NewScaleHeight);
			} break;
			}
			
			if(pTV_SLLP.mTextView_WrapContent_ResizeSurrounded) {
//				log(String.format("setScaleSize From:%5.3f To:%5.3f", getScaleHeight(), getScaleHeight()+lTextView_DiffScaleHeight));
				setScaleSize(getScaleWidth(), getScaleHeight()+lTextView_DiffScaleHeight, true);
			}
		}
//		log("updateTextViewHeight 4 ret "+(pBGHeight*getScaleHeight()/lRootView_OldScaleHeight)+" = "+pBGHeight+"*"+getScaleHeight()+"/"+lRootView_OldScaleHeight);
		return pBGHeight*getScaleHeight()/lRootView_OldScaleHeight;
	}
	@Override
	protected void onMeasure(int pWidthMeasureSpec, int pHeightMeasureSpec) {
//		log("onMeasure ================ Start "+this.toString());
		float lRootWidth = 0;
		int lWidthMode = MeasureSpec.getMode(pWidthMeasureSpec);
		int lWidthSize = MeasureSpec.getSize(pWidthMeasureSpec);
		float lRootHeight = 0;
		int lHeightMode = MeasureSpec.getMode(pHeightMeasureSpec);
		int lHeightSize = MeasureSpec.getSize(pHeightMeasureSpec);
		
		switch (lWidthMode) {
		case MeasureSpec.EXACTLY:
			log("  onMeasure Width  Exactly "+lRootWidth+" = min("+mScale_Full_Width+", "+lWidthSize+")");
			lRootWidth = lWidthSize;
			break;
		case MeasureSpec.AT_MOST:
			log("  onMeasure Width  AtMost "+lRootWidth+" = min("+mScale_Full_Width+", "+lWidthSize+")");
			lRootWidth = lWidthSize;
			break;
		default:
			log("  onMeasure Width  Unspecified "+lRootWidth+" = "+mScale_Full_Width);
			lRootWidth = mScale_Full_Width;
			break;
		}
		
		switch (lHeightMode) {
		case MeasureSpec.EXACTLY:
			switch (lWidthMode) {
			case MeasureSpec.EXACTLY:
				log("  onMeasure Height Exactly Width Exactly "+lRootHeight+" = min("+lRootWidth+"*"+mRatioOfWidthHeight+", "+lHeightSize+")");
				lRootHeight = Math.min(lRootWidth * mRatioOfWidthHeight, lHeightSize);
				break;
			case MeasureSpec.AT_MOST:
				lRootHeight = Math.min(lRootWidth * mRatioOfWidthHeight, lHeightSize);
				break;
			default:
				lRootHeight = lHeightSize;
				lRootWidth = lRootHeight / mRatioOfWidthHeight;
				break;
			}
			
			log("  onMeasure Height Exactly "+lRootHeight+", "+lHeightSize);
			break;
		case MeasureSpec.AT_MOST:
			switch (lWidthMode) {
			case MeasureSpec.EXACTLY:
				lRootHeight = lRootWidth * mRatioOfWidthHeight;
				break;
			case MeasureSpec.AT_MOST:
				lRootHeight = Math.min(lRootWidth * mRatioOfWidthHeight, lHeightSize);
				break;
			default:
				lRootHeight = Math.min(lRootWidth * mRatioOfWidthHeight, lHeightSize);
				break;
			}
			
			log("  onMeasure Height AtMost "+lRootHeight+" = min("+(lRootWidth * mRatioOfWidthHeight)+", "+lHeightSize+")");
			break;
		default:
			lRootHeight = lRootWidth * mRatioOfWidthHeight;
			log("  onMeasure Height Unspecified "+lRootHeight+" = "+lRootWidth+"*"+mRatioOfWidthHeight);
			break;
		}
		
		if(lRootHeight/mRatioOfWidthHeight < lRootWidth) {
//			log("  onMeasure Height Exactly Replace Width "+lBGWidth+" to "+(lBGHeight/mRatioOfWidthHeight));
			lRootWidth = lRootHeight/mRatioOfWidthHeight;
		}

		log(String.format("onMeasure 1 lScale:%5.3f ChildCount:%d Root:(%5.3f, %5.3f)", (lRootWidth/mScale_Full_Width), getChildCount(), lRootWidth, lRootHeight));
//		log(String.format("onMeasure 1 lScale:%5.3f ChildCount:%d", (lRootWidth/mScale_Full_Width), getChildCount()));

		float lScale = lRootWidth / mScale_Full_Width;
		
		float lForTextViewRootWidth = lRootWidth;
		float lForTextViewRootHeight = lRootHeight;
		
		for (int i=0;i<getChildCount();i++) {
			View lView = getChildAt(i);
				
			if(lView instanceof TextView) {
				TextView v = (TextView) lView;

				ScalableLayout.LayoutParams lParams = getChildLayoutParams(lView);
				if(v.getTextSize() != lParams.mScale_TextSize * lScale) {
					v.setTextSize(TypedValue.COMPLEX_UNIT_PX, lParams.mScale_TextSize * lScale);
				}
				refreshTextChangedListener(v);
				
				log(String.format("onMeasure 1 lParams:%s TVIndex:%d lForTextViewRootHeight:%f", lParams.mTextView_WrapContent_Direction.name(), i, lForTextViewRootHeight));
				switch (lParams.mTextView_WrapContent_Direction) {
				case Left: {
					lForTextViewRootWidth  = updateTextViewWidth( v, lParams.mTextView_WrapContent_Direction, lParams, lForTextViewRootWidth, lForTextViewRootHeight);
				} break;
				case Right: {
					lForTextViewRootWidth  = updateTextViewWidth( v, lParams.mTextView_WrapContent_Direction, lParams, lForTextViewRootWidth, lForTextViewRootHeight);
				} break;
				case Center_Horizontal: {
					lForTextViewRootWidth  = updateTextViewWidth( v, lParams.mTextView_WrapContent_Direction, lParams, lForTextViewRootWidth, lForTextViewRootHeight);
				} break;
				case Top: {
					lForTextViewRootHeight = updateTextViewHeight(v, lParams.mTextView_WrapContent_Direction, lParams, lForTextViewRootWidth, lForTextViewRootHeight);
				} break;
				case Bottom: {
					lForTextViewRootHeight = updateTextViewHeight(v, lParams.mTextView_WrapContent_Direction, lParams, lForTextViewRootWidth, lForTextViewRootHeight);
				} break;
				}
				log(String.format("onMeasure 2 lParams:%s TVIndex:%d lForTextViewRootHeight:%f", lParams.mTextView_WrapContent_Direction.name(), i, lForTextViewRootHeight));
			}
		}

		log(String.format("onMeasure 2 lScale:%5.3f ChildCount:%d Root:(%5.3f, %5.3f) ForTextView:(%5.3f, %5.3f)", 
			(lRootWidth/mScale_Full_Width), getChildCount(), lRootWidth, lRootHeight, lForTextViewRootWidth, lForTextViewRootHeight));
		if(lForTextViewRootWidth != lRootWidth || lForTextViewRootHeight != lRootHeight) {
			if(lWidthMode == MeasureSpec.EXACTLY && lHeightMode == MeasureSpec.EXACTLY) {
				// 둘중에 커진 놈 기준으로
				if(lForTextViewRootWidth / lRootWidth > lForTextViewRootHeight / lRootHeight) {
					lRootHeight /= (lForTextViewRootWidth / lRootWidth);
					lRootWidth = lForTextViewRootWidth;
				} else {
					lRootWidth /= (lForTextViewRootHeight / lRootHeight);
					lRootHeight = lForTextViewRootHeight;
				}
//			} else if(lWidthMode == MeasureSpec.EXACTLY && lHeightMode != MeasureSpec.EXACTLY) {
//				lRootHeight /= (lForTextViewRootWidth / lRootWidth);
//				lRootWidth = lForTextViewRootWidth;
//			} else if(lWidthMode != MeasureSpec.EXACTLY && lHeightMode == MeasureSpec.EXACTLY) {
//				lRootWidth /= (lForTextViewRootHeight / lRootHeight);
//				lRootHeight = lForTextViewRootHeight;
			} else {
				lRootWidth = lForTextViewRootWidth;
				lRootHeight = lForTextViewRootHeight;
			}
			
				postDelayed(new Runnable() {
					@Override
					public void run() {
						requestLayout();
						forceLayout();
					}
				}, 10);
				
		}
		
//		log(String.format("onMeasure 2 lScale:%5.3f", (lRootWidth/mScale_Full_Width)));
		log(String.format("onMeasure 3 lScale:%5.3f ChildCount:%d Root:(%5.3f, %5.3f) ForTextView:(%5.3f, %5.3f)", 
			(lRootWidth/mScale_Full_Width), getChildCount(), lRootWidth, lRootHeight, lForTextViewRootWidth, lForTextViewRootHeight));
		
		lScale = lRootWidth / mScale_Full_Width;
		
		float lTopMarginFromWeight = (lRootHeight - (lRootWidth * mRatioOfWidthHeight))/4;
//		log("  onMeasure ("+lBGWidth+","+lBGHeight+") Ratio:"+mRatioOfWidthHeight+" Scale:"+lScale+" lTopMarginFromWeight:"+lTopMarginFromWeight);
		
		for (int i=0;i<getChildCount();i++) {
			View lView = getChildAt(i);
				
			ScalableLayout.LayoutParams lParams = getChildLayoutParams(lView);
			
			boolean dif = false;
			
			if(lParams.width != (int)Math.round(lScale * lParams.mScale_Width))
				dif = true;
			lParams.width = (int)Math.round(lScale * lParams.mScale_Width);
			if(lParams.height != (int)Math.round(lScale * lParams.mScale_Height))
				dif = true;
			lParams.height = (int)Math.round(lScale * lParams.mScale_Height);
			
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
//				TextView v = (TextView) lView;
//				if(v.getTextSize() != lParams.mScale_TextSize * lScale) {
//					v.setTextSize(TypedValue.COMPLEX_UNIT_PX, lParams.mScale_TextSize * lScale);
//				}
				
				
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
		super.onMeasure(MeasureSpec.makeMeasureSpec(Math.round(lRootWidth), lWidthMode), MeasureSpec.makeMeasureSpec(Math.round(lRootHeight), lHeightMode));
		setMeasuredDimension(Math.round(lRootWidth), Math.round(lRootHeight));
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
			this(pScale_Left, pScale_Top, pScale_Width, pScale_Height, Default_Scale_TextSize, TextView_WrapContent_Direction.None, false);
		}
		private LayoutParams(
				float pScale_Left, float pScale_Top, 
				float pScale_Width, float pScale_Height, float pScale_TextSize,
				TextView_WrapContent_Direction pTextView_WrapContent_Direction, boolean pTextView_WrapContent_ResizeSurrounded ) {
			super(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.LEFT | Gravity.TOP);
			
			setScale_Left(pScale_Left);
			setScale_Top(pScale_Top);
			setScale_Width(pScale_Width);
			setScale_Height(pScale_Height);
			
			setScale_TextSize(pScale_TextSize);
			
			setTextView_WrapContent(pTextView_WrapContent_Direction, pTextView_WrapContent_ResizeSurrounded);
		}
		
		private LayoutParams(android.view.ViewGroup.LayoutParams pLayoutParams) {
			this(
				Default_Scale_Left, Default_Scale_Top, 
				Default_Scale_Width, Default_Scale_Height, 
				Default_Scale_TextSize, TextView_WrapContent_Direction.None, false);
			
			width = pLayoutParams.width;
			height = pLayoutParams.height;
			layoutAnimationParameters = pLayoutParams.layoutAnimationParameters;
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
		
		private TextView_WrapContent_Direction 		mTextView_WrapContent_Direction 		= TextView_WrapContent_Direction.None;
		private boolean 							mTextView_WrapContent_ResizeSurrounded 	= false;
		public void setTextView_WrapContent(TextView_WrapContent_Direction pTextView_WrapContent_Direction, boolean pTextView_WrapContent_ResizeSurrounded) { 
			mTextView_WrapContent_Direction 		= pTextView_WrapContent_Direction; 
			mTextView_WrapContent_ResizeSurrounded 	= pTextView_WrapContent_ResizeSurrounded;
		}
	}
	
	private void log(String pLog) {
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

