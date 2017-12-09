package me.karankapoor.androidkotlin

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Toast

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers

import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.Retrofit

import me.karankapoor.androidkotlin.network.RequestInterface
import me.karankapoor.androidkotlin.adapter.DataAdapter
import me.karankapoor.androidkotlin.model.Android
import me.karankapoor.androidkotlin.model.HeadClass

import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), DataAdapter.Listener {

    private val TAG = MainActivity::class.java.simpleName

    private val BASE_URL = "http://www.androidbegin.com/tutorial/"

    private var mCompositeDisposable: CompositeDisposable? = null

    private var mAndroidArrayList: ArrayList<Android>? = null

    private var mAdapter: DataAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mCompositeDisposable = CompositeDisposable()

        initRecyclerView()

        loadJSON()
    }

    private fun initRecyclerView() {

        rv_android_list.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        rv_android_list.layoutManager = layoutManager
    }

    private fun loadJSON() {

        val requestInterface = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RequestInterface::class.java)

        mCompositeDisposable?.add(requestInterface.getData()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError))

    }

    private fun handleResponse(HeadClass: HeadClass) {

        mAndroidArrayList = ArrayList(HeadClass.worldpopulation)
        mAdapter = DataAdapter(mAndroidArrayList!!, this)

        rv_android_list.adapter = mAdapter
    }

    private fun handleError(error: Throwable) {

        Log.d(TAG, error.localizedMessage)

        Toast.makeText(this, "Error ${error.localizedMessage}", Toast.LENGTH_SHORT).show()
    }

    override fun onItemClick(and: Android) {

//          Toast.makeText(this, "${android.rank} Clicked !", Toast.LENGTH_SHORT).show()
        val intent = android.content.Intent(this, FullScreenActivity::class.java)
        intent.putExtra("image_file", and.flag)
        this.startActivity(intent)

    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable?.clear()
    }
}
