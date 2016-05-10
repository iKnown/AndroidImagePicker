/**
 * 
 */
package com.iknow.imageselect.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.iknow.imageselect.R;
import com.iknow.imageselect.core.CoreActivity;
import com.iknow.imageselect.utils.DeviceInforHelper;

public class TitleView extends RelativeLayout implements View.OnClickListener{

	// ===========================================================
	// Constants
	// ===========================================================
	private static final int Left_Btn_View_Id = 0x1001;
	private static final int Title_View_Id = 0x1002;
	private static final int Right_Btn_View_Id = 0x1003;
	private static final int DEFAULT_TITLE_HEIGHT = DeviceInforHelper.getPixelFromDip(50);
	private static final int DEFAULT_ICON_SIZE = DeviceInforHelper.getPixelFromDip(22);

	private static final int TITLE_TEXT_DEFAULT_STYLE = R.style.text_18_ffffff;
	private static final int BTN_TEXT_DEFAULT_STYLE = R.style.text_16_ffffff;
	// ===========================================================
	// Fields
	// ===========================================================
	private TextView mTitleTextView;
	private TextView mSubTitleTextView;
	private String mTitleText;
	private int mTitleStyle;

	private View mLeftBtnTextView;
	private View mLeftBtnImgView;
	private String mLeftBtnText;
	private Drawable mLBtnDrawable;
	private int mLBtnStyle;
	private boolean isShowLBtn;

	private View mRightBtnTextView;
	private View mRightBtnImgView;
	private String mRightBtnText;
	private Drawable mRBtnBgDrawable;
	private int mRBtnStyle;
	private boolean isShowRBtn;

	private OnTitleClickListener mOnTitleClickListener;
	private OnRightBtnClickListener mOnRightBtnClickListener;
  private OnLeftBtnClickListener mOnLeftBtnClickListener;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	public TitleView(Context context){
		this(context,null);
	}

	public TitleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context,attrs);
		setupView(context);
	}
	
	private void initView(Context context, AttributeSet attrs) {
		if (attrs != null){
			TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TitleView);
			mTitleText = a.getString(R.styleable.TitleView_title_text);
			mTitleStyle = a.getResourceId(R.styleable.TitleView_title_text_appearance, -1);
			
			isShowLBtn = a.getBoolean(R.styleable.TitleView_is_show_left_button, true);
			if(isShowLBtn){
				mLeftBtnText = a.getString(R.styleable.TitleView_title_btn_left_text);
				mLBtnStyle = a.getResourceId(R.styleable.TitleView_title_btn_left_text_appearance, -1);
				mLBtnDrawable = a.getDrawable(R.styleable.TitleView_title_btn_left_drawable);
				if(mLBtnDrawable == null)
					mLBtnDrawable = getResources().getDrawable(R.drawable.icon_back);
			}
			
			isShowRBtn = a.getBoolean(R.styleable.TitleView_is_show_right_button, true);
			if(isShowRBtn){
				mRightBtnText = a.getString(R.styleable.TitleView_title_btn_right_text);
				mRBtnStyle = a.getResourceId(R.styleable.TitleView_title_btn_right_text_appearance, -1);
				mRBtnBgDrawable = a.getDrawable(R.styleable.TitleView_title_btn_right_drawable);
			}
			
			a.recycle();
			if (getBackground() == null) {
				setBackgroundResource(android.R.color.white);
			}
			//this.setVisibility(View.GONE);
		}
	}
	
	private void setupView(Context context) {
//		final DisplayMetrics dm = context.getResources().getDisplayMetrics();
		int titleHight;

		/**new sub title view*/
		TextView subTitleView = new TextView(context);
		this.mSubTitleTextView = subTitleView;
		subTitleView.setVisibility(View.GONE);
		subTitleView.setTextColor(Color.parseColor("#444444"));
		subTitleView.setTextSize(13);
		subTitleView.setSingleLine();
		subTitleView.setGravity(Gravity.CENTER);
		subTitleView.setEllipsize(TextUtils.TruncateAt.END);
		subTitleView.setClickable(false);
		subTitleView.setText("Test subTitleView");
		
		/**new title view*/
		TextView titleView = new TextView(context);
		this.mTitleTextView = titleView;
		titleView.setId(Title_View_Id);
		titleView.setGravity(Gravity.CENTER);
		titleView.setEllipsize(TextUtils.TruncateAt.END);
		titleView.setSingleLine();
		titleView.setClickable(true);
		if(mTitleStyle != -1)
			titleView.setTextAppearance(context, mTitleStyle);
		else
			titleView.setTextAppearance(context, TITLE_TEXT_DEFAULT_STYLE);
		
		if (!TextUtils.isEmpty(mTitleText)){
			titleView.setText(mTitleText);
		}
		
		RelativeLayout mRelativeLayout = new RelativeLayout(context);
		//mRelativeLayout.setGravity(Gravity.CENTER_HORIZONTAL);

		/**titleView layoutParams*/
		if(View.VISIBLE == subTitleView.getVisibility())
			titleHight = LayoutParams.WRAP_CONTENT;
		else
			titleHight = DEFAULT_TITLE_HEIGHT;
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, titleHight);
		lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		mRelativeLayout.addView(titleView, lp);
		
		/**subTitleView layoutParams*/
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, DEFAULT_TITLE_HEIGHT);
		lp.addRule(BELOW, Title_View_Id);
		lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		mRelativeLayout.addView(subTitleView, lp);

		//LayoutParams parms = new LayoutParams(LayoutParams.MATCH_PARENT, DEFAULT_TITLE_HEIGHT);
		//parms.addRule(RelativeLayout.RIGHT_OF, Left_Btn_View_Id);
		//parms.addRule(RelativeLayout.LEFT_OF, Right_Btn_View_Id);
		//parms.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		//mRelativeLayout.setLayoutParams(parms);

		addView(mRelativeLayout);

		/**Add left button view*/
		if(isShowLBtn){
			if (TextUtils.isEmpty(mLeftBtnText)){
				mLeftBtnImgView = new View(context);
				mLeftBtnImgView.setBackground(mLBtnDrawable);
				FrameLayout containerLayout = new FrameLayout(context);
				FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(DEFAULT_ICON_SIZE,DEFAULT_ICON_SIZE);
				params.gravity = Gravity.CENTER|Gravity.LEFT;
				containerLayout.addView(mLeftBtnImgView, params);
				this.mLeftBtnTextView = containerLayout;

			}else{
				TextView leftBtnView = new TextView(context);
				leftBtnView.setGravity(Gravity.CENTER|Gravity.LEFT);
				leftBtnView.setSingleLine(true);
				leftBtnView.setEllipsize(TextUtils.TruncateAt.END);
				leftBtnView.setText(mLeftBtnText);
				if (mLBtnStyle != -1){
					leftBtnView.setTextAppearance(context, mLBtnStyle);
				} else {
					leftBtnView.setTextAppearance(context, BTN_TEXT_DEFAULT_STYLE);
				}
				this.mLeftBtnTextView = leftBtnView;
			}

			this.mLeftBtnTextView.setId(Left_Btn_View_Id);
			this.mLeftBtnTextView.setClickable(true);
			this.mLeftBtnTextView.setOnClickListener(this);
			LayoutParams lps = new LayoutParams(LayoutParams.WRAP_CONTENT, DEFAULT_TITLE_HEIGHT);
			lps.setMargins(DeviceInforHelper.getPixelFromDip(10), 0, 0, 0);
			lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
			lps.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
			addView(mLeftBtnTextView, lps);
		}

		/**Add right button view*/
		if(isShowRBtn){
			if(TextUtils.isEmpty(mRightBtnText)){
				mRightBtnImgView = new View(context);
				mRightBtnImgView.setBackground(mRBtnBgDrawable);
				FrameLayout containerLayout = new FrameLayout(context);
				FrameLayout.LayoutParams subLp = new FrameLayout.LayoutParams(DEFAULT_ICON_SIZE+15, DEFAULT_ICON_SIZE);
				subLp.gravity = Gravity.CENTER|Gravity.RIGHT;
				containerLayout.addView(mRightBtnImgView, subLp);
				this.mRightBtnTextView = containerLayout;
			}else{
				TextView rightBtnView = new TextView(getContext());
				rightBtnView.setGravity(Gravity.CENTER|Gravity.RIGHT);
				rightBtnView.setSingleLine(true);
				rightBtnView.setEllipsize(TextUtils.TruncateAt.END);
				rightBtnView.setText(mRightBtnText);
				if (mRBtnStyle != -1){
					rightBtnView.setTextAppearance(context, mRBtnStyle);
				} else {
					rightBtnView.setTextAppearance(context, BTN_TEXT_DEFAULT_STYLE);
				}

				this.mRightBtnTextView = rightBtnView;
			}

			mRightBtnTextView.setId(Right_Btn_View_Id);
			mRightBtnTextView.setClickable(true);
			mRightBtnTextView.setOnClickListener(this);
			LayoutParams p = new LayoutParams(LayoutParams.WRAP_CONTENT, DEFAULT_TITLE_HEIGHT);
			p.setMargins(0, 0, DeviceInforHelper.getPixelFromDip(10), 0);
			p.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
			p.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
			addView(mRightBtnTextView, p);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case Title_View_Id:
				if (mOnTitleClickListener != null)
					mOnTitleClickListener.onTitleClick(v);
				break;
			case Left_Btn_View_Id:
        if(mOnLeftBtnClickListener !=null)
          mOnLeftBtnClickListener.onRBtnClick(v);
        else
				  sendKeyBackEvent();
				break;
			case Right_Btn_View_Id:
				if (mOnRightBtnClickListener != null)
					mOnRightBtnClickListener.onRBtnClick(v);
				break;
			default:
				break;
		}
	}
	
	// ===========================================================
	// Methods
	// ===========================================================
	public interface OnTitleClickListener {
		void onTitleClick(View v);
	}
	
	public interface OnRightBtnClickListener {
		void onRBtnClick(View v);
	}

  public interface OnLeftBtnClickListener {
    void onRBtnClick(View v);
  }

	public void setOnTitleClickListener(OnTitleClickListener listener) {
		mOnTitleClickListener = listener;
	}
	
	public void setOnRightBtnClickListener(OnRightBtnClickListener listener) {
		mOnRightBtnClickListener = listener;
	}

  public void setOnLeftBtnClickListener(OnLeftBtnClickListener listener){
    mOnLeftBtnClickListener = listener;
  }

	public <T> void setTitleText(T obj) {
		if(obj == null)
			return;
		if(obj instanceof Integer)
			mTitleTextView.setText(getResources().getString((Integer) obj));
		else if(obj instanceof CharSequence)
			mTitleTextView.setText((CharSequence)obj);
	}
	
	public <T> void setRightBtnText(T obj) {
		if(obj == null)
			return;
		if(obj instanceof Integer)
			((TextView)mRightBtnTextView).setText(getResources().getString((Integer) obj));
		else if(obj instanceof CharSequence)
			((TextView)mRightBtnTextView).setText((CharSequence)obj);
	}
	
	public View getRBtnView(){
		return mRightBtnTextView;
	}
	
	public View getRBtnImgView(){
		return mRightBtnImgView;
	}
	
	public <T> void setRightBtnDrawable(T obj){
		if(obj == null  || mRightBtnImgView == null)
			return;
		if(obj instanceof Integer)
			mRightBtnImgView.setBackgroundResource((Integer) obj);
		else
			mLeftBtnImgView.setBackground((Drawable)obj);
	}
	
	public <T> void setLeftBtnText(T obj) {
		if(obj == null)
			return;
		if(obj instanceof Integer)
			((TextView)mLeftBtnTextView).setText(getResources().getString((Integer) obj));
		else if(obj instanceof CharSequence)
			((TextView)mLeftBtnTextView).setText((CharSequence)obj);
	}	
	
	public <T> void setLeftBtnDrawable(T obj){
		if(obj == null || mLeftBtnImgView == null)
			return;
		if(obj instanceof Integer)
			mLeftBtnImgView.setBackgroundResource((Integer) obj);
		else
			mLeftBtnImgView.setBackground((Drawable)obj);
	}
	
	public <T> void setSubTitleText(T obj){
		if(obj == null)
			return;
		if(obj instanceof Integer)
			((TextView)mSubTitleTextView).setText(getResources().getString((Integer) obj));
		else if(obj instanceof CharSequence)
			((TextView)mSubTitleTextView).setText((CharSequence)obj);
		setSubTitleVisibility(View.VISIBLE);
	}
	
	public void setSubTitleVisibility(int visibility){
		mSubTitleTextView.setVisibility(visibility);
		ViewGroup.LayoutParams p = mTitleTextView.getLayoutParams();
		if(visibility == View.GONE){
			p.height = DEFAULT_TITLE_HEIGHT;
		}else if(visibility == View.VISIBLE){
			p.height = LayoutParams.WRAP_CONTENT;
		}
	}
	
	private <T> void sendKeyBackEvent() {
		final Context context = getContext();
		if (context instanceof CoreActivity) {
			KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK);
			((CoreActivity) context).onKeyDown(KeyEvent.KEYCODE_BACK, keyEvent);
		} else if (context instanceof Activity) {
			KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK);
			((Activity) context).onKeyDown(KeyEvent.KEYCODE_BACK, keyEvent);
		}
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
