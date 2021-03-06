package com.xingray.sample.page.list

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xingray.recycleradapter.RecyclerAdapter
import com.xingray.sample.R
import com.xingray.sample.base.MvpActivity
import com.xingray.sample.data.Student
import com.xingray.sample.ui.ProgressDialog
import com.xingray.sample.ui.ViewUtil

/**
 * xxx
 *
 * @author : leixing
 * @version : 1.0.0
 * mail : leixing1012@qq.com
 * @date : 2019/7/11 11:38
 */
class StudentListActivity : MvpActivity<StudentListContract.Presenter>(), StudentListContract.View {

    companion object {

        private val TAG = StudentListActivity::class.java.simpleName

        fun start(context: Context) {
            val starter = Intent(context, StudentListActivity::class.java)
            if (context !is Activity) {
                starter.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(starter)
        }
    }

    private var rvList: RecyclerView? = null
    private var mPresenter: StudentListPresenter? = null
    private val mProgressDialog: ProgressDialog by lazy { ProgressDialog(this) }
    private var mAdapter: RecyclerAdapter? = null

    override fun initVariables() {
        setPresenterInterface(StudentListContract.Presenter::class.java)
    }

    override fun initView() {
        setContentView(R.layout.activity_student_list)
        rvList = findViewById(R.id.rv_list)

        findViewById<View>(R.id.bt_presenter).setOnClickListener {
            val p = StudentListPresenter()
            p.bindView(this@StudentListActivity)
            bindPresenter(p)
            mPresenter = p
        }

        findViewById<View>(R.id.bt_stop).setOnClickListener {
            presenter.onStop()
        }

        initList()
    }

    override fun loadData() {
        presenter.loadData()
    }

    override fun showLoading() {
        mProgressDialog.show()
    }

    override fun dismissLoading() {
        mProgressDialog.dismiss()
    }

    override fun showStudents(list: List<Student>) {
        mAdapter?.update(list)
    }

    private fun initList() {
        val list = rvList ?: return
        list.layoutManager = LinearLayoutManager(applicationContext)

        mAdapter = RecyclerAdapter(applicationContext)
            .addType(StudentViewHolder::class.java, null) { _, _, student ->
                ViewUtil.showToast(applicationContext, student.name)
            }

        list.adapter = mAdapter
    }

    override fun scrollTo(position: Int) {
        rvList?.scrollToPosition(position)
    }
}
