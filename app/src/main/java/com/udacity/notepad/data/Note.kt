package com.udacity.notepad.data

import java.util.Date

data class Note (
    var id: Int = -1,
    var text: String? = null,
    var isPinned: Boolean = false,
    var createdAt: Date = Date(),
    var updatedAt: Date? = null
)

/*
    val note = Note(text = "This is a note")
    print(note == note.copy())  // True
    val aCopy = note.copy(text = "This is a copy", isPinned = true)
 */