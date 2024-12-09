package com.capstone.free.education.data.event

import java.util.concurrent.Executor
import java.util.concurrent.Executors


class AppExecutors {
    val diskIO: Executor = Executors.newSingleThreadExecutor()
}