package com.ray.projectKotlin.base

import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.ray.projectKotlin.R
import com.ray.projectKotlin.commons.Logger
import com.ray.projectKotlin.commons.RelayoutTool
import com.ray.projectKotlin.config.ProjectApplication
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.ParameterizedType

/**
 * description ： fragment抽象类
 * @author : ray
 * @date : 2022/8/2
 */
abstract class BaseFragment<P : BasePresenter> : Fragment() {
    protected val rayTag = this.javaClass.simpleName!!

    protected var presenter: P? = null
    protected var appContext: ProjectApplication? = null

    protected var mActivity: BaseActivity<*>? = null


    /**
     * 是否透明化状态栏
     * @return 默认为不透明
     */
    open fun isTransparentStatus(): Boolean {
        return false
    }

    /**
     * 沉浸式菜单
     * @return 默认为不是
     */
    open fun isImmersiveStatus(): Boolean {
        return false
    }

    /**
     * 是否填充状态栏高度
     * @return
     */
    open fun isImmersiveStatusHeight(): Boolean {
        return false
    }

    /**
     * 界面是否全屏
     * @return 默认不全屏
     */
    open fun isFullScreen(): Boolean {
        return false
    }

    /**
     * 是否使用状态栏深色主题
     * @return 默认非深色主题
     */
    open fun isConvertStatusBarColor(): Boolean {
        return false
    }

    /**
     * 是否开启MVP模式
     * @return
     */
    open fun isMvp(): Boolean {
        return false
    }

    /**
     * 设置数据
     */
    abstract fun initData(savedInstanceState: Bundle);

    override fun onAttach(activity: Context?) {
        super.onAttach(activity)
        mActivity = activity as BaseActivity<*>?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    /**
     * 设置布局
     *
     * @return
     */
    abstract fun initLayout() : Int

    /**
     * 初始化布局
     */
    protected abstract fun initView()

    override fun onCreateView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = View.inflate(mActivity, R.layout.fragment_container, null)
        var container: LinearLayout = view.findViewById(R.id.containerLinearLayout)
        var mainView = View.inflate(mActivity, initLayout(), null)
        container.addView(mActivity?.measureView(mainView),
            ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT))
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // 初始化present view
        if(isMvp()) {
            initPresent();
        }
        savedInstanceState?.let { initData(it) };
    }
    private fun initPresent() {
        try {
            // 获取当前new对象的泛型的父类类型
            val parameterizedType = this.javaClass.genericSuperclass as ParameterizedType
            val clazz = parameterizedType.actualTypeArguments[0] as Class<P>
            Logger.d(rayTag, "clazz ==>> $clazz")
            //this.presenter = (P) clazz.newInstance();
            // 获取有参构造
            //val cons = clazz.getConstructors()
            val cons = clazz.constructors;
            val con = cons[0]
            val initArgs = this
            // this.presenter = con.newInstance(initArgs, initArgs) as P
            this.presenter = con.newInstance() as P
        } catch (e: ClassCastException) {
            e.printStackTrace()
        } catch (e: java.lang.InstantiationException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }

    }
}