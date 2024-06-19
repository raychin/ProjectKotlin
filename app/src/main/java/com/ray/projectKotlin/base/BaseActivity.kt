package com.ray.projectKotlin.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.ray.projectKotlin.commons.Logger
import com.ray.projectKotlin.commons.RelayoutTool
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.ParameterizedType
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import com.ray.projectKotlin.config.AppConfig
import com.ray.projectKotlin.config.AppManager
import com.ray.projectKotlin.config.ProjectApplication
import android.content.Intent
import android.graphics.Color
import android.view.KeyEvent
import android.view.WindowManager
import com.ray.projectKotlin.R
import java.io.Serializable
import com.ray.projectKotlin.commons.ToastUtils

/**
 * activity抽象类
 * @create by ray on 2018/12/06
 */
abstract class BaseActivity<P : BasePresenter> : AppCompatActivity(), IBaseView {
    protected val rayTag:String = this.javaClass.simpleName

    protected var presenter: P? = null
    protected var appContext: ProjectApplication? = null
    protected var mFragmentManager: FragmentManager? = null


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
     * 沉浸式添加顶部占位颜色
     */
    open fun statusColor(): Int {
        return 0
    }

    private fun setImmersiveStatus() {
        val win = window
        win.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        win.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                //| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION     // 占用底部导航栏
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
        win.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        // 只对api21以上版本有效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            win.statusBarColor = Color.TRANSPARENT    // 透明状态栏
            //win.setNavigationBarColor(Color.TRANSPARENT);    // 透明底部导航栏
        }
    }

    private fun setTransparentStatus() {
        // 只对api21以上版本有效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val win = window
            win.statusBarColor = Color.TRANSPARENT    // 透明状态栏
            //win.setNavigationBarColor(Color.TRANSPARENT);    // 透明底部导航栏
        }
    }

    // 是否使用特殊的标题栏背景颜色，android5.0以上可以设置状态栏背景色，如果不使用则使用透明色值
    protected var useThemeStatusBarColor = false
    // 是否使用状态栏文字和图标为暗色，如果状态栏采用了白色系，则需要使状态栏和图标为暗色，android6.0以上可以设置
    protected var useStatusBarColor = true

    protected fun setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 5.0及以上
            val decorView = window.decorView
            val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            decorView.systemUiVisibility = option
            // 根据上面设置是否对状态栏单独设置颜色
            if (useThemeStatusBarColor) {
                window.statusBarColor = resources.getColor(R.color.black)
            } else {
                window.statusBarColor = Color.TRANSPARENT
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 4.4到5.0
            val localLayoutParams = window.attributes
            localLayoutParams.flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or localLayoutParams.flags
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // android6.0以后可以对状态栏文字颜色和图标进行修改
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }


    /**
     * 添加状态栏占位视图
     */
    private fun setStatusViewWithColor(color: Int) {
        val statusBarView: View = findViewById(R.id.status)
        val lp = statusBarView.layoutParams
        lp.height = if (isImmersiveStatusHeight()) ProjectApplication.get()!!.getStatusBarHeight() else 0
        if (color != 0) {
            statusBarView.setBackgroundColor(color)
        }
        statusBarView.layoutParams = lp
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 设置全屏
        if(isFullScreen()) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);    // 全屏，去掉状态栏
        }
        // 透明化状态栏
        if(isTransparentStatus()) { setTransparentStatus(); }
        // 沉浸式
        if(isImmersiveStatus()) { setImmersiveStatus(); }
        if(isConvertStatusBarColor()) { setStatusBar(); }

        AppManager.getAppManager().addActivity(this);
        AppManager.getAppManager().addActivity(this);
        mFragmentManager = supportFragmentManager;
        appContext = ProjectApplication.get();
        // 设置布局
        setContentView(initLayout())
        if(isImmersiveStatus()) { setStatusViewWithColor(statusColor()); }

        initView();

        if(isMvp()) {
            initPresent();
        }

        initData();
    }

    /******** 基准分辨率 **********/
    private val UI_STANDARD_WIDTH = 1080f
    /******** 缩放比例值 **********/
    var scale = 0f
    override fun setContentView(layoutResID: Int) {
        //var view = View.inflate(this, layoutResID, null)
        //this.setContentView(view)

        var view = View.inflate(this, R.layout.activity_container, null)
        this.setContentView(view)
        var container: LinearLayout = findViewById(R.id.containerLinearLayout)
        var mainView = View.inflate(this, layoutResID, null)
        container.addView(measureView(mainView),
            ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT))
    }

    fun measureView(mainView: View) : View {
        if (scale == 0f) {
            initScreenScale()
        }

        if (scale != 1f) {
            RelayoutTool.relayoutViewHierarchy(mainView, scale)
        }
        return mainView;
    }

    override fun setContentView(view: View) {
        if (scale == 0f) {
            initScreenScale()
        }

        if (scale != 1f) {
            RelayoutTool.relayoutViewHierarchy(view, scale)
        }
        super.setContentView(view)
    }

    override fun setContentView(view: View, params: ViewGroup.LayoutParams) {
        if (scale == 0f) {
            initScreenScale()
        }
        RelayoutTool.relayoutViewHierarchy(view, scale)
        RelayoutTool.scaleLayoutParams(params, scale)
        super.setContentView(view, params)
    }

    fun initScreenScale() {
        var displayMetrics = this.resources.displayMetrics;
        var width = displayMetrics.widthPixels
        scale = width / UI_STANDARD_WIDTH;
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
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }

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
    abstract fun initView()

    /**
     * 设置数据
     */
    abstract fun initData()

    override fun onRestart() {
        super.onRestart()
        if(presenter != null) {
            presenter!!.onRestart()
        }
    }
    override fun onStart() {
        super.onStart()
        if(presenter != null) {
            presenter!!.onStart()
        }
    }

    override fun onResume() {
        super.onResume()
        if(presenter != null) {
            presenter!!.onResume()
        }
    }

    override fun onPause() {
        super.onPause()
        if(presenter != null) {
            presenter!!.onPause()
        }
    }

    override fun onStop() {
        super.onStop()
        if(presenter != null) {
            presenter!!.onStop()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(presenter != null) {
            presenter!!.onDestroy()
        }
        AppManager.getAppManager().finishActivity(this)
    }

    fun nextActivity(clazz: Class<*>, isPlayAnim: Boolean, name: String?, s: Serializable?) {
        val intent = Intent()
        intent.setClass(this, clazz)
        if (null != name && name.trim { it <= ' ' } != "") {
            intent.putExtra(name, s)
        }
        startActivity(intent)
        if (isPlayAnim) {
            overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out)
        }
    }

    fun nextActivityByPackageName(packageName: String) {
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        startActivity(intent)
    }

    fun nextActivity(clazz: Class<*>) {
        nextActivity(clazz, true)
    }

    fun nextActivity(clazz: Class<*>, isPlayAnim: Boolean) {
        nextActivity(clazz, isPlayAnim, null, null)
    }

    fun nextActivity(clazz: Class<*>, name: String, s: Serializable) {
        nextActivity(clazz, true, name, s)
    }

    override fun finish() {
        super.finish()
        if (true) {
            overridePendingTransition(0, R.anim.push_right_out)
        }
    }


    /**
     * 单个授权申请
     * @param permission
     */
    fun checkPermission(permission: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查是否已经给了权限
            val checkPermission = ContextCompat.checkSelfPermission(
                applicationContext,
                permission
            )
            // 没有给权限
            if (checkPermission != PackageManager.PERMISSION_GRANTED) {
                // 参数分别是当前活动，权限字符串数组，requestCode
                ActivityCompat.requestPermissions(this, arrayOf(permission), 1)
            }
        }
    }

    /**
     * 多个授权申请
     * @param permissions
     */
    fun checkPermissions(permissions: Array<String>) {
        for (i in permissions.indices) {
            checkPermission(permissions[i])
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            Logger.d(rayTag + "onRequestPermissionsResult", grantResults.toString())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!grantResults.isEmpty()) {
                    return
                }
                // grantResults数组与权限字符串数组对应，里面存放权限申请结果
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    for (i in permissions.indices) {
                        // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                        val b = shouldShowRequestPermissionRationale(permissions[i])
                        if (!b) {
                            // 用户还是想用我的 APP 的
                            // 提示用户去应用设置界面手动开启权限
                            ToastUtils.showToast(this, "权限获取失败", 1)
                        } else {
                            finish()
                        }
                    }
                } else {
                    ToastUtils.showToast(this, "权限获取成功", 1)
                    AppConfig.getAppConfig(this);
                }
            }
        }
    }

    override fun updateView(event: ResultEvent) {
    }

    /**
     * 是否显示退出提示
     * @return
     */
    open fun isExit(): Boolean {
        return false
    }

    private var touchTime: Long = 0
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (isExit() && event.action === KeyEvent.ACTION_DOWN
            && KeyEvent.KEYCODE_BACK === keyCode) {
            val currentTime = System.currentTimeMillis()
            if (currentTime - touchTime >= 2000) {
                ToastUtils.showCustomToast(this, "再点一次退出", 1 / 2)
                touchTime = currentTime
            } else {
                finish()
                System.exit(0)
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}