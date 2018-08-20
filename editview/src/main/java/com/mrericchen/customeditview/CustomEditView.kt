package com.ty4wd.xzxq.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.EditText
import com.mrericchen.customeditview.R

/**
 * @Module :
 * @Comments : 自定义EditText
 * @Author : eric.chen
 * @CreateDate : 2018-06-14
 * @ModifiedBy : eric.chen
 * @ModifiedDate: 2018-06-14
 * @Modified:
 */
@SuppressLint("AppCompatCustomView")
class CustomEditView : EditText {
    private var mContext: Context? = null
    private var mClearButton: Bitmap? = null
    private var mPaint: Paint? = null
    private var drawable: GradientDrawable? = null

    val isShowing: Boolean = false

    //按钮显示方式
    private var mClearButtonMode: ClearButtonMode? = null
    //初始化输入框右内边距
    private var mInitPaddingRight: Int = 0
    //按钮的左右内边距，默认为3dp
    private var mButtonPadding = dp2px(3f)


    /**
     * 按钮显示方式
     * NEVER   不显示清空按钮
     * ALWAYS  始终显示清空按钮
     * WHILEEDITING   输入框内容不为空且有获得焦点
     * UNLESSEDITING  输入框内容不为空且没有获得焦点
     */
    enum class ClearButtonMode {
        NEVER, ALWAYS, WHILEEDITING, UNLESSEDITING
    }


    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }


    /**
     * 初始化
     */
    private fun init(context: Context, attributeSet: AttributeSet?) {
        this.mContext = context
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.CustomEditView)

        when (typedArray.getInteger(R.styleable.CustomEditView_clearButtonMode, 0)) {
            1 -> mClearButtonMode = ClearButtonMode.ALWAYS
            2 -> mClearButtonMode = ClearButtonMode.WHILEEDITING
            3 -> mClearButtonMode = ClearButtonMode.UNLESSEDITING
            else -> mClearButtonMode = ClearButtonMode.NEVER
        }

        val clearButton = typedArray.getResourceId(R.styleable.CustomEditView_clearButtonDrawable, android.R.drawable.ic_delete)
        val solid = typedArray.getColor(R.styleable.CustomEditView_etSolidColor, Color.TRANSPARENT)
        val strokeColor = typedArray.getColor(R.styleable.CustomEditView_edStrokeColor, Color.TRANSPARENT)
        val radius = typedArray.getDimensionPixelSize(R.styleable.CustomEditView_etRadius, 0)
        val leftTopRadius = typedArray.getDimensionPixelSize(R.styleable.CustomEditView_etLeftTopRadius, 0)
        val leftBottomRadius = typedArray.getDimensionPixelSize(R.styleable.CustomEditView_etLeftBottomRadius, 0)
        val rightTopRadius = typedArray.getDimensionPixelSize(R.styleable.CustomEditView_etRightTopRadius, 0)
        val rightBottomRadius = typedArray.getDimensionPixelSize(R.styleable.CustomEditView_etRightBottomRadius, 0)
        val strokeWidth = typedArray.getDimensionPixelSize(R.styleable.CustomEditView_etStrokeWidth, 0)
        typedArray.recycle()

        // 按钮的背景
        drawable = GradientDrawable()
        drawable!!.setStroke(strokeWidth, strokeColor)
        drawable!!.setColor(solid)
        if (radius > 0) {
            drawable!!.cornerRadius = radius.toFloat()
        } else if (leftTopRadius > 0 || leftBottomRadius > 0 || rightTopRadius > 0 || rightBottomRadius > 0) {
            drawable!!.cornerRadii = floatArrayOf(leftTopRadius.toFloat(), leftTopRadius.toFloat(), rightTopRadius.toFloat(), rightTopRadius.toFloat(), rightBottomRadius.toFloat(), rightBottomRadius.toFloat(), leftBottomRadius.toFloat(), leftBottomRadius.toFloat())
        }

        background = drawable

        //按钮的图片
        mClearButton = (getDrawableCompat(clearButton) as BitmapDrawable).bitmap

        mPaint = Paint()
        mPaint!!.isAntiAlias = true

        mInitPaddingRight = paddingRight
    }


    /**
     * 按钮状态管理
     * @param canvas onDraw的Canvas
     */
    private fun buttonManager(canvas: Canvas) {
        when (mClearButtonMode) {
            CustomEditView.ClearButtonMode.ALWAYS -> drawBitmap(canvas, getRect(true))
            CustomEditView.ClearButtonMode.WHILEEDITING -> drawBitmap(canvas, getRect(hasFocus() && text.length > 0))
            CustomEditView.ClearButtonMode.UNLESSEDITING -> {
            }
            else -> drawBitmap(canvas, getRect(false))
        }
    }


    /**
     * 设置输入框的内边距
     * @param isShow  是否显示按钮
     */
    private fun setPadding(isShow: Boolean) {
        val paddingRight = mInitPaddingRight + if (isShow) mClearButton!!.width + mButtonPadding + mButtonPadding else 0

        setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
    }


    /**
     * 取得显示按钮与不显示按钮时的Rect
     * @param isShow  是否显示按钮
     */
    private fun getRect(isShow: Boolean): Rect {
        val left: Int
        val top: Int
        val right: Int
        val bottom: Int

        right = if (isShow) measuredWidth + scrollX - mButtonPadding - mButtonPadding else 0
        left = if (isShow) right - mClearButton!!.width else 0
        top = if (isShow) (measuredHeight - mClearButton!!.height) / 2 else 0
        bottom = if (isShow) top + mClearButton!!.height else 0


        //更新输入框内边距
        setPadding(isShow)


        return Rect(left, top, right, bottom)
    }


    /**
     * 绘制按钮图片
     * @param canvas onDraw的Canvas
     * @param rect   图片位置
     */
    private fun drawBitmap(canvas: Canvas, rect: Rect?) {
        if (rect != null) {
            canvas.drawBitmap(mClearButton!!, null, rect, mPaint)
        }
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.save()

        buttonManager(canvas)

        canvas.restore()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_UP ->
                //判断是否点击到按钮所在的区域
                if (event.x - (measuredWidth - paddingRight) >= 0) {
                    error = null
                    this.setText("")
                }
        }

        return super.onTouchEvent(event)
    }


    /**
     * 获取Drawable
     * @param resourseId  资源ID
     */
    private fun getDrawableCompat(resourseId: Int): Drawable {
        return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            resources.getDrawable(resourseId, mContext!!.theme)
        } else {
            resources.getDrawable(resourseId)
        }
    }

    /**
     * 设置按钮左右内边距
     * @param buttonPadding 单位为dp
     */
    fun setButtonPadding(buttonPadding: Int) {
        this.mButtonPadding = dp2px(buttonPadding.toFloat())
    }

    /**
     * 设置按钮显示方式
     * @param clearButtonMode 显示方式
     */
    fun setClearButtonMode(clearButtonMode: ClearButtonMode) {
        this.mClearButtonMode = clearButtonMode
    }

    fun dp2px(dipValue: Float): Int {
        val scale = resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }
}
