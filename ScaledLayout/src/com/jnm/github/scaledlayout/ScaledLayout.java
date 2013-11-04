package com.jnm.github.scaledlayout;

import java.util.Vector;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

 public class ScaledLayout extends FrameLayout {
	private static String sLogTag = null;
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
	
	public static final int Tag_TextSize_PX_Float = com.jnm.github.scaledlayout.R.id.ScaledLayout_TextSize_PX;
	public static final int Tag_DestRect_RectF = com.jnm.github.scaledlayout.R.id.ScaledLayout_RectF;
//	public static final int Tag_TextSize_PX_Float = 0;
//	public static final int Tag_DestRect_RectF = 1;
	
	private Vector<View> mViews = new Vector<View>();
	
	private float mScaleWidth 			= 100;
	private float mScaleHeight 		= 100;
	private float mRatioOfWidthHeight 	= mScaleHeight / mScaleWidth;
	public float getScaleWidth() { return mScaleWidth; }
	public float getScaleHeight() { return mScaleHeight; }
	
	public void setScaleWidth(float pWidth) {
		mScaleWidth = pWidth;
		mRatioOfWidthHeight = mScaleHeight / mScaleWidth;
		postInvalidate();
	}
	public void setScaleHeight(float pHeight) {
		mScaleHeight = pHeight;
		mRatioOfWidthHeight = mScaleHeight / mScaleWidth;
		postInvalidate();
	}
	
	public ScaledLayout(Context context) {
		this(context, 100, 100);
	}
	public ScaledLayout(Context context, float pScaleWidth, float pScaleHeight) {
		this(context, null, pScaleWidth, pScaleHeight);
	}
	public ScaledLayout(Context context, AttributeSet attrs) {
		//this(context, attrs, attrs.getAttributeFloatValue("scaledlayout", "scale_width", 100), attrs.getAttributeFloatValue("scaledlayout", "scale_height", 100));
		this(context, attrs, 
			context.obtainStyledAttributes(attrs, R.styleable.ScaledLayout).getFloat(R.styleable.ScaledLayout_scale_width, 100f), 
			context.obtainStyledAttributes(attrs, R.styleable.ScaledLayout).getFloat(R.styleable.ScaledLayout_scale_height, 100f));
//			context.obtainStyledAttributes(attrs, R.styleable.ScaledLayout).getFloat(R.styleable.ScaledLayout_ScaleWidth, 100f), 
//			context.obtainStyledAttributes(attrs, R.styleable.ScaledLayout).getFloat(R.styleable.ScaledLayout_ScaleHeight, 100f));
	}
	private ScaledLayout(Context context, AttributeSet attrs, float pScaleWidth, float pScaleHeight) {
		super(context, attrs);
		
		setScaleWidth(pScaleWidth);
		setScaleHeight(pScaleHeight);
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	}
	
	public void setTextSize(TextView pTextView, float pFontSize) {
		pTextView.setTag(Tag_TextSize_PX_Float, Float.valueOf(pFontSize));
	}
	public TextView addNewTextView(String pText, float pFontSize, float left, float top, float width, float height) {
		return addNewTextView(pText, pFontSize, left, top, width, height, false);
	}
	public TextView addNewTextView(String pText, float pFontSize, float left, float top, float width, float height, boolean pDuplicateState){
		TextView ret = new TextView(getContext());
		setTextSize(ret, pFontSize);
		ret.setText(pText);
		ret.setGravity(Gravity.CENTER);
		ret.setTextColor(Color.BLACK);
		ret.setDuplicateParentStateEnabled(pDuplicateState);
		
		addView(ret, left, top, width, height);
		return ret;
	}
	
	public EditText addNewEditText(float pFontSize, float left, float top, float width, float height) {
		EditText ret = new EditText(getContext());
		//ret.setTag(Tag_TextSize_PX_Float, Float.valueOf(pFontSize_UnitPX480));
		setTextSize(ret, pFontSize);
		ret.setGravity(Gravity.CENTER);
		ret.setTextColor(Color.BLACK);
		
		addView(ret, left, top, width, height);
		return ret;
	}
	
	public ImageView addNewImageView(float left, float top, float width, float height) {
		return addNewImageView((Drawable)null, left, top, width, height, false);
	}
	public ImageView addNewImageView(Bitmap pBitmap, float left, float top, float width, float height) {
		BitmapDrawable bm = new BitmapDrawable(getResources(), pBitmap);
		return addNewImageView(bm, left, top, width, height, false);
	}
	public ImageView addNewImageView(int pResID, float left, float top, float width, float height) {
		return addNewImageView(pResID, left, top, width, height, false);
	}
	public ImageView addNewImageView(int pResID, float left, float top, float width, float height, boolean pDuplicateState) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		return addNewImageView(
			new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), pResID, options)), 
			left, top, width, height, pDuplicateState);
	}
	public ImageView addNewImageView(Drawable drawable, float left, float top, float width, float height) {
		return addNewImageView(drawable, left, top, width, height, false);
	}
	public ImageView addNewImageView(Drawable drawable, float left, float top, float width, float height, boolean pDuplicateState) {
		ImageView lNewImageView = new ImageView(getContext());
		lNewImageView.setImageDrawable(drawable);
		lNewImageView.setScaleType(ScaleType.FIT_XY);
		lNewImageView.setDuplicateParentStateEnabled(pDuplicateState);
		
		addView(lNewImageView, left, top, width, height);
		return lNewImageView;
	}
	
	@Override
	@Deprecated
	public void addView(View pChild) { super.addView(pChild); }
	@Override
	@Deprecated
	public void addView(View pChild, int pIndex) { super.addView(pChild, pIndex); }
	@Override
	@Deprecated
	public void addView(View pChild, int pIndex, android.view.ViewGroup.LayoutParams pParams) { super.addView(pChild, pIndex, pParams); }
	@Override
	@Deprecated
	public void addView(View pChild, int pWidth, int pHeight) { super.addView(pChild, pWidth, pHeight); }
	@Override
	@Deprecated
	public void addView(View pChild, android.view.ViewGroup.LayoutParams pParams) { super.addView(pChild, pParams); }
	
	
	public void addView(View pView, float left, float top, float width, float height) {
		pView.setTag(Tag_DestRect_RectF, new RectF(left, top, width + left, height + top));
		mViews.add(pView);
		addView(pView, new FrameLayout.LayoutParams((int)width, (int)height, Gravity.LEFT | Gravity.TOP));
	}
	
	public void moveChildView(View pView, float left, float top, float width, float height) {
		RectF rect = (RectF) pView.getTag(Tag_DestRect_RectF);
		rect.left = left;
		rect.top = top;
		rect.right = left+width;
		rect.bottom = top+height;
		postInvalidate();
	}
	
	public void moveChildView(View pView, float left, float top){
		RectF rect = (RectF) pView.getTag(Tag_DestRect_RectF);
		rect.left = left;
		rect.top = top;
		postInvalidate();
	}
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		log("onMeasure ================ Start "+this.toString());
		float lBGWidth = 0;
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		float lBGHeight = 0;
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		switch (widthMode) {
		case MeasureSpec.EXACTLY:
			//log("  onMeasure Width Exactly "+lBGWidth+" = "+widthSize);
			log("  onMeasure Width Exactly "+lBGWidth+" = min("+mScaleWidth+", "+widthSize+")");
//			if(mInitialWidth < widthSize) {
//				lBGWidth = widthSize;
//			} else {
//				//lBGWidth = Math.max(mInitialWidth, widthSize);
//				lBGWidth = mInitialWidth;
//			}
			lBGWidth = widthSize;
			break;
		case MeasureSpec.AT_MOST:
			log("  onMeasure Width AtMost "+lBGWidth+" = min("+mScaleWidth+", "+widthSize+")");
//			lBGWidth = Math.max(mInitialWidth, widthSize);
//			lBGWidth = Math.min(mInitialWidth, widthSize);
//			if(mInitialWidth < widthSize) {
//				lBGWidth = widthSize;
//			} else {
				lBGWidth = widthSize;
//			}
			break;
		default:
			log("  onMeasure Width Unspecified "+lBGWidth+" = "+mScaleWidth);
			lBGWidth = mScaleWidth;
			break;
		}
		
		switch (heightMode) {
		case MeasureSpec.EXACTLY:
//			lBGHeight = heightSize;
			lBGHeight = Math.min(lBGWidth * mRatioOfWidthHeight, heightSize);
			
			log("  onMeasure Height Exactly "+lBGHeight+", "+heightSize);
			break;
		case MeasureSpec.AT_MOST:
			lBGHeight = Math.min(lBGWidth * mRatioOfWidthHeight, heightSize);
			log("  onMeasure Height AtMost "+lBGHeight+" = min("+(lBGWidth * mRatioOfWidthHeight)+", "+heightSize+")");
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
		
		
		float lScale = lBGWidth / mScaleWidth;
		
		float lTopMarginFromWeight = (lBGHeight - (lBGWidth * mRatioOfWidthHeight))/4;
		log("  onMeasure ("+lBGWidth+","+lBGHeight+") Ratio:"+mRatioOfWidthHeight+" Scale:"+lScale+" lTopMarginFromWeight:"+lTopMarginFromWeight);
		
		for (View pView : mViews){
			RectF lViewRect = (RectF) pView.getTag(Tag_DestRect_RectF);
			
			FrameLayout.LayoutParams lParams = (LayoutParams) pView.getLayoutParams();
			
			lParams.width = (int)(lScale * lViewRect.width())+1;
			lParams.height = (int)(lScale * lViewRect.height())+1;
			lParams.setMargins(Math.round(lScale * lViewRect.left), Math.round(lScale * lViewRect.top + lTopMarginFromWeight), 0, 0);
			log("  "+pView.toString()+" "+
					String.format("(%f, %f, %f, %f)", lViewRect.left, lViewRect.top, lViewRect.width(), lViewRect.height())+
					"->"+
					String.format("(%d, %d, %d, %d)", lParams.leftMargin, lParams.topMargin, lParams.width, lParams.height));
			pView.setLayoutParams(lParams);
			
			if(pView instanceof TextView) {
				if(pView.getTag(Tag_TextSize_PX_Float) != null){
					((TextView)pView).setTextSize(TypedValue.COMPLEX_UNIT_PX, ((Float)pView.getTag(Tag_TextSize_PX_Float)).floatValue() * lScale);
				}
			}
			else if(pView instanceof EditText) {
				if(pView.getTag(Tag_TextSize_PX_Float) != null){
					((EditText)pView).setTextSize(TypedValue.COMPLEX_UNIT_PX, ((Float)pView.getTag(Tag_TextSize_PX_Float)).floatValue() * lScale);
				}
			}
		}
		
		log("onMeasure ================ End   "+this.toString());
		super.onMeasure(MeasureSpec.makeMeasureSpec(Math.round(lBGWidth), widthMode), MeasureSpec.makeMeasureSpec(Math.round(lBGHeight), heightMode));
		setMeasuredDimension(Math.round(lBGWidth), Math.round(lBGHeight));
	}
}

