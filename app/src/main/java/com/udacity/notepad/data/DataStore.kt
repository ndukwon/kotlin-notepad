package com.udacity.notepad.data

import android.content.Context
import org.jetbrains.anko.doAsync

import java.util.concurrent.Executor
import java.util.concurrent.Executors

object DataStore {

//    val EXEC: Executor = Executors.newSingleThreadExecutor()

//    var notes: NoteDatabase? = null
    @JvmStatic                          // help understand getter/setter will be generated
    lateinit var notes: NoteDatabase    // Initialized by the time to be used(Not nullable: cleaner and safer)
        private set

    fun init(context: Context) {
        notes = NoteDatabase(context)
    }

//    fun execute(runnable: Runnable) {
//        EXEC.execute(runnable)
//    }
    fun execute(runnable: Runnable) {
//        doAsync { runnable.run() }
        execute{  runnable.run()  }
    }

    fun execute(fn: () -> Unit) {
        /*
            doAsync() : handling thread pooling and execution
            - instead of "AsyncTask"
         */
        doAsync { fn() }
    }
}
