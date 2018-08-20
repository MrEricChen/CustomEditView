
## 前言
为避免频繁在drawable包下创建shape来实现EditText修改背景框(圆角、直角、线条颜色以及粗细)，节省res资源的创建，自定义EditText来实现相应的效果，并且为之添加可自定义的删除图片

## 自定义属性

``` stylus

    <declare-styleable name="CustomEditView">
        <!-- 清除按钮显示模式 -->
        <attr name="clearButtonMode">
            <!--不显示清空按钮-->
            <enum name="never" value="0" />
            <!--不为空，获得焦点与没有获得焦点都显示清空按钮-->
            <enum name="always" value="1" />
            <!--不为空，且在编辑状态时（及获得焦点）显示清空按钮-->
            <enum name="whileEditing" value="2" />
            <!--不为空，且不在编译状态时（焦点不在输入框上）显示清空按钮-->
            <enum name="unlessEditing" value="3" />
        </attr>
        <!-- 自定义删除按钮图片 -->
        <attr name="clearButtonDrawable" format="reference" />
        <!-- 背景色 -->
        <attr name="etSolidColor" format="color" />
        <!-- 边框颜色 -->
        <attr name="edStrokeColor" format="color" />
        <!-- 边框宽度 -->
        <attr name="etStrokeWidth" format="dimension" />
        <!-- 圆角度数 -->
        <attr name="etRadius" format="dimension" />
        <attr name="etLeftTopRadius" format="dimension" />
        <attr name="etLeftBottomRadius" format="dimension" />
        <attr name="etRightTopRadius" format="dimension" />
        <attr name="etRightBottomRadius" format="dimension" />
    </declare-styleable>
		
```

## 代码参考
导入Module，布局xml里引入控件
``` stylus

    <com.mrericchen.customeditview.CustomEditView
        android:layout_width="match_parent"
        android:layout_height="40dp"
        android:layout_margin="10dp"
        android:padding="10dp"
        android:text="111111"
        app:edStrokeColor="@color/colorAccent"
        app:etRadius="5dp"
        app:etStrokeWidth="1dp"
        app:clearButtonMode="whileEditing"
        android:foregroundGravity="center_vertical"
        app:clearButtonDrawable="@mipmap/icon_close_black"
        app:layout_constraintTop_toTopOf="parent" />

```

## 图例
![enter description here][1]


  [1]: ./images/1388B19460EBC6B54ED73F947D745B46.png "1388B19460EBC6B54ED73F947D745B46"