package com.jnm.android.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

@Deprecated
public class ScalableLayout_OnlyJava extends FrameLayout {
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
		mScale_Full_Width = pWidth;
		mScale_Full_Height = pHeight;
		mRatioOfWidthHeight = mScale_Full_Height / mScale_Full_Width;
		postInvalidate();
	}
	
	public ScalableLayout_OnlyJava(Context pContext) {
		this(pContext, Default_Scale_Base_Width, Default_Scale_Base_Height);
	}
	
	public ScalableLayout_OnlyJava(Context pContext, AttributeSet pAttrs) {
		this(pContext, pAttrs, Default_Scale_Base_Width, Default_Scale_Base_Height);
	}
	
	/**
	 * @param pContext
	 * @param pScale_Width Initial width of ScaleLayout.
	 * @param pScale_Height Initial height of ScaleLayout.
	 */
	public ScalableLayout_OnlyJava(Context pContext, float pScale_Width, float pScale_Height) {
		this(pContext, null, pScale_Width, pScale_Height);
	}
	private ScalableLayout_OnlyJava(Context pContext, AttributeSet pAttrs, float pScale_Width, float pScale_Height) {
		super(pContext, pAttrs);
		setScaleWidth(pScale_Width);
		setScaleHeight(pScale_Height);
	}
	
	public ScalableLayout_OnlyJava.LayoutParams getChildLayoutParams(View pChild) {
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
		if(pParams instanceof ScalableLayout_OnlyJava.LayoutParams) {
			addView_Final(pChild, pIndex, (ScalableLayout_OnlyJava.LayoutParams) pParams);
		} else {
			addView_Final(pChild, pIndex, generateLayoutParams(pParams));
		}
	}
	public void addView(View pChild, float pScale_Left, float pScale_Top, float pScale_Width, float pScale_Height) {
		addView_Final(pChild, getChildCount(), new ScalableLayout_OnlyJava.LayoutParams(pScale_Left, pScale_Top, pScale_Width, pScale_Height));
	}
	private final void addView_Final(View pChild, int pIndex, ScalableLayout_OnlyJava.LayoutParams pScaledLayoutParams) {
		super.addView(pChild, pIndex, pScaledLayoutParams);
	}
	
	@Override
	protected ScalableLayout_OnlyJava.LayoutParams generateDefaultLayoutParams() {
		return new ScalableLayout_OnlyJava.LayoutParams(0f, 0f, getScaleWidth(), getScaleHeight());
	}
	@Override
	public ScalableLayout_OnlyJava.LayoutParams generateLayoutParams(AttributeSet pAttrs) {
		log("generateLayoutParams AttributeSet: "+pAttrs);
		for(int i=0;i<pAttrs.getAttributeCount();i++) { 
			log("attr["+i+"] "+pAttrs.getAttributeName(i)+":"+pAttrs.getAttributeValue(i));
			
		}
		return new ScalableLayout_OnlyJava.LayoutParams(
			 Default_Scale_Left, Default_Scale_Top, Default_Scale_Width, Default_Scale_Height,
			 Default_Scale_TextSize );
	}
	@Override
	protected ScalableLayout_OnlyJava.LayoutParams generateLayoutParams(ViewGroup.LayoutParams pP) {
		if(pP instanceof ScalableLayout_OnlyJava.LayoutParams) {
			return (ScalableLayout_OnlyJava.LayoutParams) pP;
		}
		return new ScalableLayout_OnlyJava.LayoutParams(pP); 
	}
	
	
	/**
	 * move childView inside ScalableLayout
	 * @param pChildView view to move. should be child of ScaleLayout
	 * @param pScale_Left
	 * @param pScale_Top
	 */
	public void moveChildView(View pChildView, float pScale_Left, float pScale_Top) {
		ScalableLayout_OnlyJava.LayoutParams rect = getChildLayoutParams(pChildView);
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
		ScalableLayout_OnlyJava.LayoutParams rect = getChildLayoutParams(pChildView);
		rect.mScale_Left = pScale_Left;
		rect.mScale_Top = pScale_Top;
		rect.mScale_Width = pScale_Width;
		rect.mScale_Height = pScale_Height;
		postInvalidate();
	}
	
	
	
	@Override
	protected void onMeasure(int pWidthMeasureSpec, int pHeightMeasureSpec) {
		log("onMeasure ================ Start "+this.toString());
		float lBGWidth = 0;
		int lWidthMode = MeasureSpec.getMode(pWidthMeasureSpec);
		int lWidthSize = MeasureSpec.getSize(pWidthMeasureSpec);
		float lBGHeight = 0;
		int lHeightMode = MeasureSpec.getMode(pHeightMeasureSpec);
		int lHeightSize = MeasureSpec.getSize(pHeightMeasureSpec);
		
		switch (lWidthMode) {
		case MeasureSpec.EXACTLY:
			log("  onMeasure Width Exactly "+lBGWidth+" = min("+mScale_Full_Width+", "+lWidthSize+")");
			lBGWidth = lWidthSize;
			break;
		case MeasureSpec.AT_MOST:
			log("  onMeasure Width AtMost "+lBGWidth+" = min("+mScale_Full_Width+", "+lWidthSize+")");
			lBGWidth = lWidthSize;
			break;
		default:
			log("  onMeasure Width Unspecified "+lBGWidth+" = "+mScale_Full_Width);
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
			
			log("  onMeasure Height Exactly "+lBGHeight+", "+lHeightSize);
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
			
			log("  onMeasure Height AtMost "+lBGHeight+" = min("+(lBGWidth * mRatioOfWidthHeight)+", "+lHeightSize+")");
			break;
		default:
			lBGHeight = lBGWidth * mRatioOfWidthHeight;
			log("  onMeasure Height Unspecified "+lBGHeight+" = "+lBGWidth+"*"+mRatioOfWidthHeight);
			break;
		}
		
		if(lBGHeight/mRatioOfWidthHeight < lBGWidth) {
			log("  onMeasure Height Exactly Replace Width "+lBGWidth+" to "+(lBGHeight/mRatioOfWidthHeight));
			lBGWidth = lBGHeight/mRatioOfWidthHeight;
		}
		
		
		float lScale = lBGWidth / mScale_Full_Width;
		
		float lTopMarginFromWeight = (lBGHeight - (lBGWidth * mRatioOfWidthHeight))/4;
		log("  onMeasure ("+lBGWidth+","+lBGHeight+") Ratio:"+mRatioOfWidthHeight+" Scale:"+lScale+" lTopMarginFromWeight:"+lTopMarginFromWeight);
		
		for (int i=0;i<getChildCount();i++) {
			View lView = getChildAt(i);
				
			ScalableLayout_OnlyJava.LayoutParams lParams = getChildLayoutParams(lView);
			
			lParams.width = (int)(lScale * lParams.mScale_Width)+1;
			lParams.height = (int)(lScale * lParams.mScale_Height)+1;
			lParams.setMargins(Math.round(lScale * lParams.mScale_Left), Math.round(lScale * lParams.mScale_Top + lTopMarginFromWeight), 0, 0);
			log("  "+lView.toString()+" "+
				String.format("(%f, %f, %f, %f)", lParams.mScale_Left, lParams.mScale_Top, lParams.mScale_Width, lParams.mScale_Height)+
				"->"+
				String.format("(%d, %d, %d, %d)", lParams.leftMargin, lParams.topMargin, lParams.width, lParams.height));
			lView.setLayoutParams(lParams);
			
			if(lView instanceof TextView) {
				((TextView)lView).setTextSize(TypedValue.COMPLEX_UNIT_PX, lParams.mScale_TextSize * lScale);
			}
			else if(lView instanceof EditText) {
				((EditText)lView).setTextSize(TypedValue.COMPLEX_UNIT_PX, lParams.mScale_TextSize * lScale);
			}
		}
		
		log("onMeasure ================ End   "+this.toString());
		super.onMeasure(MeasureSpec.makeMeasureSpec(Math.round(lBGWidth), lWidthMode), MeasureSpec.makeMeasureSpec(Math.round(lBGHeight), lHeightMode));
		setMeasuredDimension(Math.round(lBGWidth), Math.round(lBGHeight));
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

		private float mScale_Left = Default_Scale_Left;
		public float getScale_Left() { return mScale_Left; }
		public void setScale_Left(float pScale_Left) { mScale_Left = pScale_Left; }

		private float mScale_Top = Default_Scale_Top;
		public float getScale_Top() { return mScale_Top; }
		public void setScale_Top(float pScale_Top) { mScale_Top = pScale_Top; }

		private float mScale_Width = Default_Scale_Width;
		public float getScale_Width() { return mScale_Width; }
		public void setScale_Width(float pScale_Width) { mScale_Width = pScale_Width; }

		private float mScale_Height = Default_Scale_Height;
		public float getScale_Height() { return mScale_Height; }
		public void setScale_Height(float pScale_Height) { mScale_Height = pScale_Height; }

		private float mScale_TextSize = Default_Scale_TextSize;
		public float getScale_TextSize() { return mScale_TextSize; }
		public void setScale_TextSize(float pScale_TextSize) { mScale_TextSize = pScale_TextSize; }
	}
	
	private void log(String pLog) {
		if(sLogTag_World != null) {
			Log.e(sLogTag_World, pLog);
		}
		if(mLogTag_This != null) {
			Log.e(mLogTag_This, pLog);
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
}

