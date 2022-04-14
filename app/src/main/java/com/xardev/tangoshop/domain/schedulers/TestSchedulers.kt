package com.xardev.tangoshop.domain.schedulers

import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.schedulers.TestScheduler

object TestSchedulers : SchedulersProvider {
    private val testScheduler = Schedulers.trampoline()

    override val IO: Scheduler
        get() = testScheduler
    override val COMPUTATION: Scheduler
        get() = testScheduler
    override val TRAMPOLINE: Scheduler
        get() = testScheduler
    override val MAIN: Scheduler
        get() = testScheduler

}