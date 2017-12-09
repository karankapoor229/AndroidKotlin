package me.karankapoor.androidkotlin.network

import me.karankapoor.androidkotlin.model.Android
import me.karankapoor.androidkotlin.model.HeadClass
import io.reactivex.Observable
import retrofit2.http.GET

interface RequestInterface {

    @GET("jsonparsetutorial.txt")
    fun getData() : Observable<HeadClass>
}
