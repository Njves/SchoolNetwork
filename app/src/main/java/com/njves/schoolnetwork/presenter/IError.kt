package com.njves.schoolnetwork.presenter

interface IError : IView {
    fun onError(message : String)
    fun onFail(t : Throwable)
}