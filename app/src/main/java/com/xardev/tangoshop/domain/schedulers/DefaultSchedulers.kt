package com.xardev.tangoshop.domain.schedulers

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers

object DefaultSchedulers : SchedulersProvider {
    override val IO: Scheduler
        get() = Schedulers.io()
    override val COMPUTATION: Scheduler
        get() = Schedulers.computation()
    override val TRAMPOLINE: Scheduler
        get() = Schedulers.trampoline()
    override val MAIN: Scheduler
        get() = AndroidSchedulers.mainThread()

}