package de.binder_net.tapper_test

import android.os.SystemClock

class TimeProfiler {

    var profilingStartTime: Long = 0
    var profilingEndTime: Long = 0

    fun start() {

        profilingStartTime = SystemClock.currentThreadTimeMillis()
    }

    fun end(): Long {

        profilingEndTime = SystemClock.currentThreadTimeMillis()
        val delta = profilingEndTime - profilingStartTime

        reset()

        return delta
    }

    fun reset() {

        profilingStartTime = 0
        profilingEndTime = 0
    }
}
