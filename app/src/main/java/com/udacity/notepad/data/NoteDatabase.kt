package com.udacity.notepad.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

import java.util.ArrayList
import java.util.Date

import android.provider.BaseColumns._ID
import com.udacity.notepad.data.NotesContract.NoteTable.CREATED_AT
import com.udacity.notepad.data.NotesContract.NoteTable.IS_PINNED
import com.udacity.notepad.data.NotesContract.NoteTable.TEXT
import com.udacity.notepad.data.NotesContract.NoteTable.UPDATED_AT
import com.udacity.notepad.data.NotesContract.NoteTable._TABLE_NAME
import org.jetbrains.anko.db.transaction

class NoteDatabase(context: Context) {

//    private val helper: NotesOpenHelper
//    init {
//        helper = NotesOpenHelper(context)
//    }
    private val helper: NotesOpenHelper = NotesOpenHelper(context)

//    val all: List<Note>
//        get() {
//            val cursor = helper.readableDatabase.query(_TABLE_NAME,
//                    null,
//                    null,
//                    null,
//                    null,
//                    null,
//                    CREATED_AT)
//            val retval = allFromCursor(cursor)
//            cursor.close()
//            return retval
//        }

    // all >> getAll(): Alt + Enter > Convert property to function
    fun getAll(): List<Note> {
        val cursor = helper.readableDatabase.query(_TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                CREATED_AT)

//        return cursor.use{ allFromCursor(it) }
        return cursor.use(this::allFromCursor)  // Alt + Enter > Convert lambda to reference
        /*
            "use()" : lambda based function
            - getting rid of "cursor.close()"
            - working with "closable" interface
            - preventing memory leak
            - do not use "use()" if SDK is under 16
              https://steemit.com/android/@kingori2/android-cursor-kotlin-closeable-use
         */
    }


//    fun loadAllByIds(vararg ids: Int): List<Note> {
//        val questionMarks = StringBuilder()
//        var i = 0
//        while (i++ < ids.size) {
//            questionMarks.append("?")
//            if (i <= ids.size - 1) {
//                questionMarks.append(", ")
//            }
//        }
//        val args = arrayOfNulls<String>(ids.size)
//        i = 0
//        while (i < ids.size) {
//            args[i] = Integer.toString(ids[i])
//            ++i
//        }
//        val selection = _ID + " IN (" + questionMarks.toString() + ")"
//        val cursor = helper.readableDatabase.query(_TABLE_NAME, null,
//                selection,
//                args, null, null,
//                CREATED_AT)
//        val retval = allFromCursor(cursor)
//        cursor.close()
//        return retval
//    }
    fun loadAllByIds(vararg ids: Int): List<Note> {
        val questionMarks = ids.map { "?" }.joinToString { "," }
        val args = ids.map { it.toString() }
        val selection = "$_ID IN (${questionMarks.toString()})"
        val cursor = helper.readableDatabase.query(_TABLE_NAME,
                null,
                selection,
                args.toTypedArray(),
                null,
                null,
                CREATED_AT)
        return cursor.use(this::allFromCursor)
    }


//    fun insert(vararg notes: Note) {
//        val values = fromNotes(notes)
//        val db = helper.writableDatabase
//        db.beginTransaction()
//        try {
//            for (value in values) {
//                db.insert(_TABLE_NAME, null, value)
//            }
//            db.setTransactionSuccessful()
//        } finally {
//            db.endTransaction()
//        }
//    }
    fun insert(vararg notes: Note) {
        val values = fromNotes(notes)
        val db = helper.writableDatabase
        db.transaction {
            for (value in values) {
                insert(_TABLE_NAME, null, value)
            }
        }
        /*
            Anko supports "transaction()"
            - simplify database transaction by wrapping them in a lambda
            - "try-catch" is supported automatically.
         */
    }

    fun update(note: Note) {
        val values = fromNote(note)
        helper.writableDatabase.update(_TABLE_NAME,
                values,
                "$_ID = ?",
                arrayOf(Integer.toString(note.id)))
    }

    fun delete(note: Note) {
        helper.writableDatabase.delete(_TABLE_NAME,
                "$_ID = ?",
                arrayOf(Integer.toString(note.id)))
    }

//    private fun fromCursor(cursor: Cursor): Note {
//        var col = 0
//        val note = Note()
//        note.id = cursor.getInt(col++)
//        note.text = cursor.getString(col++)
//        note.isPinned = cursor.getInt(col++) != 0
//        note.createdAt = Date(cursor.getLong(col++))
//        note.updatedAt = Date(cursor.getLong(col))
//        return note
//    }
    private fun fromCursor(cursor: Cursor): Note {
        var col = 0
        val note = Note().apply {
            id = cursor.getInt(col++)
            text = cursor.getString(col++)
            isPinned = cursor.getInt(col++) != 0
            createdAt = Date(cursor.getLong(col++))
            updatedAt = Date(cursor.getLong(col))
        }
        return note
    }

    private fun allFromCursor(cursor: Cursor): List<Note> {
        val retval = ArrayList<Note>()
        while (cursor.moveToNext()) {
            retval.add(fromCursor(cursor))
        }
        return retval
    }

//    private fun fromNote(note: Note): ContentValues {
//        val values = ContentValues()
//        val id = note.id
//        if (id != -1) {
//            values.put(_ID, id)
//        }
//        values.put(TEXT, note.text)
//        values.put(IS_PINNED, note.isPinned)
//        values.put(CREATED_AT, note.createdAt.time)
//        values.put(UPDATED_AT, note.updatedAt!!.time)
//        return values
//    }
    private fun fromNote(note: Note): ContentValues {
        return ContentValues().apply {
            val noteId = note.id
            if (noteId != -1) {
                put(_ID, noteId)
            }
            put(TEXT, note.text)
            put(IS_PINNED, note.isPinned)
            put(CREATED_AT, note.createdAt.time)
            put(UPDATED_AT, note.updatedAt!!.time)
        }
    }

//    private fun fromNotes(notes: Array<Note>): List<ContentValues> {
    private fun fromNotes(notes: Array<out Note>): List<ContentValues> {
        val values = ArrayList<ContentValues>()
        for (note in notes) {
            values.add(fromNote(note))
        }
        return values
    }
}
