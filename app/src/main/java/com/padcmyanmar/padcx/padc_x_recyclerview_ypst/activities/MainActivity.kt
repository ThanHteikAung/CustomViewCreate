package com.padcmyanmar.padcx.padc_x_recyclerview_ypst.activities

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.padcmyanmar.padcx.padc_x_recyclerview_ypst.R
import com.padcmyanmar.padcx.padc_x_recyclerview_ypst.adapters.NewsListAdapter
import com.padcmyanmar.padcx.padc_x_recyclerview_ypst.data.vos.NewsVO
import com.padcmyanmar.padcx.padc_x_recyclerview_ypst.mvp.presenters.MainPresenter
import com.padcmyanmar.padcx.padc_x_recyclerview_ypst.mvp.presenters.MainPresenterImpl
import com.padcmyanmar.padcx.padc_x_recyclerview_ypst.mvp.views.MainView
import com.padcmyanmar.padcx.padc_x_recyclerview_ypst.utils.EMPTY_IMAGE_URL
import com.padcmyanmar.padcx.padc_x_recyclerview_ypst.utils.EM_NO_NEWS_AVAILABLE
import com.padcmyanmar.padcx.padc_x_recyclerview_ypst.veiws.viewpods.EmptyViewPod
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), MainView {

    private lateinit var mAdapter: NewsListAdapter

    private lateinit var mViewPodEmpty: EmptyViewPod

    private lateinit var mPresenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpPresenter()

        /*hideEmptyView()*/
        setUpSwipeRefresh()
        setUpViewPod()
        setUpRecyclerView()

        mPresenter.onUiReady(this)

        flButton.setOnClickListener{
            startActivity(ModifyCustomViewActivity.newIntent(this))
        }
    }

    override fun displayNewsList(newsList: List<NewsVO>) {
        mAdapter.setNewData(newsList.toMutableList())
    }

    override fun navigateToNewsDetails(newsId: Int) {
        startActivity(NewsDetailActivity.newItent(this, newsId))
    }

    override fun displayEmptyView() {

    }

    /*override fun displayEmptyView() {
        showEmptyView()
    }*/

    override fun enableSwipeRefresh() {
        swipeRefreshLayout.isRefreshing = true
    }

    override fun disableSwipeRefresh() {
        swipeRefreshLayout.isRefreshing = false
    }

    private fun setUpPresenter() {
        mPresenter = ViewModelProvider(this)[MainPresenterImpl::class.java]
        mPresenter.initPresenter(this)
    }

    private fun setUpViewPod() {
        mViewPodEmpty = vpEmpty as EmptyViewPod
        mViewPodEmpty.setEmptyData(EM_NO_NEWS_AVAILABLE, EMPTY_IMAGE_URL)
        mViewPodEmpty.setDelegate(mPresenter)
    }

    private fun setUpSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener {
            mPresenter.onSwipeRefresh(this)
        }
    }

    private fun setUpRecyclerView() {
        mAdapter = NewsListAdapter(mPresenter)
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvNews.layoutManager = linearLayoutManager
        rvNews.adapter = mAdapter

        rvNews.setEmptyView(mViewPodEmpty)
    }

    /*private fun showEmptyView() {
        vpEmpty.visibility = View.VISIBLE
    }

    private fun hideEmptyView() {
        vpEmpty.visibility = View.GONE
    }*/

}
