package com.jnm.github.scalablelayout;

import java.util.Vector;

import com.jnm.github.scalablelayout.ScalableLayout.LayoutParams;

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

public class ScalableLayout extends FrameLayout {
	private static final float Default_Scale_Base_Width = 100f;
	private static final float Default_Scale_Base_Height = 100f;
	
	private static final float Default_Scale_Left = 0f;
	private static final float Default_Scale_Top = 0f;
	private static final float Default_Scale_Width = 100f;
	private static final float Default_Scale_Height = 100f;
	
	private static final float Default_Scale_TextSize = 100f;
	public static class LayoutParams extends FrameLayout.LayoutParams {
		public LayoutParams(Context pContext, AttributeSet pAttrs) {
			super(pContext, pAttrs);
		}

		public LayoutParams(float pScale_Left, float pScale_Top, float pScale_Width, float pScale_Height) {
			this(pScale_Left, pScale_Top, pScale_Width, pScale_Height, 10f);
		}
		private LayoutParams(float pScale_Left, float pScale_Top, float pScale_Width, float pScale_Height, float pScale_TextSize) {
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
	
	
	private static String sLogTag = "ScaledLayout";
	public static void setLoggable() {
		setLoggable("ScaledLayout");
	}
	public static void setLoggable(String pLogTag) {
		sLogTag = pLogTag;
	}
	private static void log(String pLog) {
		if(sLogTag != null) {
			Log.e(sLogTag, pLog);
		}
	}
	
	// private Vector<View> mViews = new Vector<View>();
	
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
	
	public ScalableLayout(Context pContext) {
		this(pContext, Default_Scale_Base_Width, Default_Scale_Base_Height);
	}
	public ScalableLayout(Context pContext, AttributeSet pAttrs) {
		this(pContext, pAttrs, 
			pContext.obtainStyledAttributes(pAttrs, R.styleable.ScalableLayout).getFloat(R.styleable.ScalableLayout_scale_base_width, Default_Scale_Base_Width), 
			pContext.obtainStyledAttributes(pAttrs, R.styleable.ScalableLayout).getFloat(R.styleable.ScalableLayout_scale_base_height, Default_Scale_Base_Height) );
	}
	public ScalableLayout(Context pContext, float pScale_Width, float pScale_Height) {
		this(pContext, null, pScale_Width, pScale_Height);
	}
	private ScalableLayout(Context pContext, AttributeSet pAttrs, float pScale_Width, float pScale_Height) {
		super(pContext, pAttrs);
		setScaleWidth(pScale_Width);
		setScaleHeight(pScale_Height);
	}
	
	private ScalableLayout.LayoutParams getChildLayoutParams(View pChild) {
		LayoutParams ret = (LayoutParams) pChild.getLayoutParams();
		if(ret == null) {
			ret = generateDefaultLayoutParams();
			pChild.setLayoutParams(ret);
		}
		return ret;
	}
	public void setTextSize(TextView pTextView, float pScale_TextSize) {
		getChildLayoutParams(pTextView).setScale_TextSize(pScale_TextSize);
	}
	public TextView addNewTextView(String pText, float pScale_TextSize, float pScale_Left, float pScale_Top, float pScale_Width, float pScale_Height) {
		return addNewTextView(pText, pScale_TextSize, pScale_Left, pScale_Top, pScale_Width, pScale_Height, false);
	}
	public TextView addNewTextView(String pText, float pScale_TextSize, float pScale_Left, float pScale_Top, float pScale_Width, float pScale_Height, boolean pDuplicateState) {
		TextView ret = new TextView(getContext());
		addView(ret, pScale_Left, pScale_Top, pScale_Width, pScale_Height);
		
		setTextSize(ret, pScale_TextSize);
		ret.setText(pText);
		ret.setGravity(Gravity.CENTER);
		ret.setTextColor(Color.BLACK);
		ret.setDuplicateParentStateEnabled(pDuplicateState);
		
		return ret;
	}
	
	public EditText addNewEditText(float pScale_TextSize, float pScale_Left, float pScale_Top, float pScale_Width, float pScale_Height) {
		EditText ret = new EditText(getContext());
		addView(ret, pScale_Left, pScale_Top, pScale_Width, pScale_Height);
		
		setTextSize(ret, pScale_TextSize);
		ret.setGravity(Gravity.CENTER);
		ret.setTextColor(Color.BLACK);
		return ret;
	}
	
	public ImageView addNewImageView(float pScale_Left, float pScale_Top, float pScale_Width, float pScale_Height) {
		return addNewImageView((Drawable)null, pScale_Left, pScale_Top, pScale_Width, pScale_Height, false);
	}
	public ImageView addNewImageView(Bitmap pBitmap, float pScale_Left, float pScale_Top, float pScale_Width, float pScale_Height) {
		BitmapDrawable bm = new BitmapDrawable(getResources(), pBitmap);
		return addNewImageView(bm, pScale_Left, pScale_Top, pScale_Width, pScale_Height, false);
	}
	public ImageView addNewImageView(int pResID, float pScale_Left, float pScale_Top, float pScale_Width, float pScale_Height) {
		return addNewImageView(pResID, pScale_Left, pScale_Top, pScale_Width, pScale_Height, false);
	}
	public ImageView addNewImageView(int pResID, float pScale_Left, float pScale_Top, float pScale_Width, float pScale_Height, boolean pDuplicateState) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		return addNewImageView(
			new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), pResID, options)), 
			pScale_Left, pScale_Top, pScale_Width, pScale_Height, pDuplicateState);
	}
	public ImageView addNewImageView(Drawable drawable, float pScale_Left, float pScale_Top, float pScale_Width, float pScale_Height) {
		return addNewImageView(drawable, pScale_Left, pScale_Top, pScale_Width, pScale_Height, false);
	}
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
		// mViews.add(pChild);
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
	
	
	public void moveChildView(View pView, float pScale_Left, float pScale_Top) {
		ScalableLayout.LayoutParams rect = getChildLayoutParams(pView);
		rect.mScale_Left = pScale_Left;
		rect.mScale_Top = pScale_Top;
		postInvalidate();
	}
	public void moveChildView(View pView, float pScale_Left, float pScale_Top, float pScale_Width, float pScale_Height) {
		ScalableLayout.LayoutParams rect = getChildLayoutParams(pView);
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
			lBGHeight = Math.min(lBGWidth * mRatioOfWidthHeight, lHeightSize);
			
			log("  onMeasure Height Exactly "+lBGHeight+", "+lHeightSize);
			break;
		case MeasureSpec.AT_MOST:
			lBGHeight = Math.min(lBGWidth * mRatioOfWidthHeight, lHeightSize);
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
		
		//for (View pView : mViews){
		for (int i=0;i<getChildCount();i++) {
			View lView = getChildAt(i);
				
			ScalableLayout.LayoutParams lParams = getChildLayoutParams(lView);
			
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
}

