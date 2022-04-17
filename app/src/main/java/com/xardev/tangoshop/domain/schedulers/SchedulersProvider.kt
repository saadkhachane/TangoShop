package com.xardev.tangoshop.domain.schedulers

import io.reactivex.rxjava3.core.Scheduler

interface SchedulersProvider {
    val IO: Scheduler
    val COMPUTATION: Scheduler
    val TRAMPOLINE: Scheduler
    val MAIN: Scheduler
}