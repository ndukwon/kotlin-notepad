package com.udacity.notepad.util

import android.content.Context
import android.view.LayoutInflater

/**
 * Created by dukwonnam on 2018. 4. 4..
 */

val Context.layoutInflater get() = LayoutInflater.from(this)