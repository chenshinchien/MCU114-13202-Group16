package com.example.vocabularychallenge.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.vocabularychallenge.model.Feedback // 【新增】引入 Feedback
import com.example.vocabularychallenge.model.TestResult // 【修正】引入 TestRecord
import com.example.vocabularychallenge.model.Vocab

class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        private const val DB_NAME = "vocab_challenge.db"
        private const val DB_VERSION = 1

        const val TBL_VOCAB = "vocab"
        const val COL_ID = "_id"
        const val COL_EN = "english"
        const val COL_ZH = "chinese"
        const val COL_UNIT = "unit"

        const val TBL_USER = "users"
        const val COL_USER = "username"
        const val COL_PASS = "password"

        const val TBL_RECORD = "records"
        const val COL_USERFK = "username_fk"
        const val COL_SCORE = "score"
        const val COL_UNITREC = "unit"
        const val COL_TIME = "time_spent"
        const val COL_DATE = "date"

        // 【新增】回饋資料表的常數
        const val TBL_FEEDBACK = "feedback"
        const val COL_FEEDBACK_USER = "username"
        const val COL_FEEDBACK_CONTENT = "content"
        const val COL_FEEDBACK_DATE = "date"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createVocab = """
            CREATE TABLE $TBL_VOCAB(
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_EN TEXT,
                $COL_ZH TEXT,
                $COL_UNIT INTEGER
            )
        """.trimIndent()
        val createUser = """
            CREATE TABLE $TBL_USER(
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_USER TEXT UNIQUE,
                $COL_PASS TEXT
            )
        """.trimIndent()
        val createRecord = """
            CREATE TABLE $TBL_RECORD(
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_USERFK TEXT,
                $COL_UNITREC INTEGER,
                $COL_SCORE INTEGER,
                $COL_TIME INTEGER,
                $COL_DATE TEXT
            )
        """.trimIndent()

        // 【新增】建立回饋資料表的 SQL 語法
        val createFeedback = """
            CREATE TABLE $TBL_FEEDBACK(
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_FEEDBACK_USER TEXT,
                $COL_FEEDBACK_CONTENT TEXT,
                $COL_FEEDBACK_DATE TEXT
            )
        """.trimIndent()

        db.execSQL(createVocab)
        db.execSQL(createUser)
        db.execSQL(createRecord)
        db.execSQL(createFeedback) // 【新增】執行建立

        // 預載一些單字
        val sample = listOf(
            Vocab(english = "abandon", chinese = "放棄", unit = 1),
            Vocab(english = "abide", chinese = "遵守", unit = 1),
            Vocab(english = "beneath", chinese = "在...之下", unit = 1),
            Vocab(english = "candidate", chinese = "候選人", unit = 1),
            Vocab(english = "dedicate", chinese = "奉獻", unit = 1),
            Vocab(english = "attribute", chinese = "屬性", unit = 1),
            Vocab(english = "equate", chinese = "等同", unit = 1),
            Vocab(english = "connect the dots", chinese = "連結點", unit = 1),
            Vocab(english = "impractical", chinese = "不切實際的", unit = 1),
            Vocab(english = "insubstantial", chinese = "無實質的；幻想的", unit = 1),
            Vocab(english = "irrelevant", chinese = "不恰當的；無關係的", unit = 1),
            Vocab(english = "logical", chinese = "合邏輯的；合理的", unit = 1),
            Vocab(english = "merely", chinese = "僅僅", unit = 1),
            Vocab(english = "metaphor", chinese = "隱喻（⼀種修辭法）；象徵", unit = 1),
            Vocab(english = "mysterious", chinese = "神秘的；不可思議的", unit = 1),
            Vocab(english = "narrow", chinese = "狹隘的", unit = 1),
            Vocab(english = "pattern", chinese = "模式；形態", unit = 1),
            Vocab(english = "serendipitous", chinese = "意外發現的；偶得的", unit = 1),
            Vocab(english = "tap into", chinese = "利用；開發", unit = 1),
            Vocab(english = "trivial", chinese = "不重要的；微不足道的", unit = 1),
            Vocab(english = "eager", chinese = "渴望的", unit = 2),
            Vocab(english = "faint", chinese = "暈眩；微弱的", unit = 2),
            Vocab(english = "genuine", chinese = "真誠的", unit = 2),
            Vocab(english = "harvest", chinese = "收穫", unit = 2),
            Vocab(english = "icon", chinese = "圖示；象徵", unit = 2),
            Vocab(english = "accessory", chinese = "附件；配件；附加物件", unit = 2),
            Vocab(english = "aquarium", chinese = "養魚缸；水族館；水族槽", unit = 2),
            Vocab(english = "auction", chinese = "拍賣", unit = 2),
            Vocab(english = "bandana", chinese = "大頭巾", unit = 2),
            Vocab(english = "hint", chinese = "暗示", unit = 2),
            Vocab(english = "innovative", chinese = "創新的", unit = 2),
            Vocab(english = "jellyfish", chinese = "水母；海蜇", unit = 2),
            Vocab(english = "manufacture", chinese = "(大批)生產", unit = 2),
            Vocab(english = "profitable", chinese = "有盈利的；有益的", unit = 2),
            Vocab(english = "scavenger hunt", chinese = "尋寶遊戲", unit = 2),
            Vocab(english = "skateboarder", chinese = "滑板手；滑板運動者", unit = 2),
            Vocab(english = "smash", chinese = "打碎；摔碎", unit = 2),
            Vocab(english = "stomp", chinese = "跺腳；（生氣）怒氣衝衝地走", unit = 2),
            Vocab(english = "urban", chinese = "城市的；城鎮的", unit = 2),
            Vocab(english = "wholesale", chinese = "批發；成批賣", unit = 2)
        )
        sample.forEach { v ->
            val cv = ContentValues().apply {
                put(COL_EN, v.english)
                put(COL_ZH, v.chinese)
                put(COL_UNIT, v.unit)
            }
            db.insert(TBL_VOCAB, null, cv)
        }

        // 預載一個 demo user
        val cvu = ContentValues().apply {
            put(COL_USER, "demo")
            put(COL_PASS, "123")
        }
        db.insert(TBL_USER, null, cvu)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TBL_VOCAB")
        db.execSQL("DROP TABLE IF EXISTS $TBL_USER")
        db.execSQL("DROP TABLE IF EXISTS $TBL_RECORD")
        db.execSQL("DROP TABLE IF EXISTS $TBL_FEEDBACK") // 【新增】刪除舊的回饋資料表
        onCreate(db)
    }

    fun getAllResults(): List<TestResult> {
        val resultList = mutableListOf<TestResult>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TBL_RECORD ORDER BY $COL_ID DESC", null)

        cursor.use { c ->
            if (c.moveToFirst()) {
                do {
                    val record = TestResult(
                        id = c.getInt(c.getColumnIndexOrThrow(COL_ID)),
                        username = c.getString(c.getColumnIndexOrThrow(COL_USERFK)),
                        unit = c.getInt(c.getColumnIndexOrThrow(COL_UNITREC)),
                        score = c.getInt(c.getColumnIndexOrThrow(COL_SCORE)),
                        timeSpent = c.getInt(c.getColumnIndexOrThrow(COL_TIME)),
                        recordTime = c.getString(c.getColumnIndexOrThrow(COL_DATE))
                    )
                    resultList.add(record)
                } while (c.moveToNext())
            }
        }
        return resultList
    }

    fun getVocabList(unit: Int = 0): List<Vocab> {
        val db = readableDatabase
        val where = if (unit == 0) null else "$COL_UNIT = ?"
        val args = if (unit == 0) null else arrayOf(unit.toString())
        val curs = db.query(TBL_VOCAB, null, where, args, null, null, null)
        val list = mutableListOf<Vocab>()
        curs.use { c ->
            while (c.moveToNext()) {
                list.add(Vocab(
                    // 新增讀取 ID
                    id = c.getInt(c.getColumnIndexOrThrow(COL_ID)),
                    english = c.getString(c.getColumnIndexOrThrow(COL_EN)),
                    chinese = c.getString(c.getColumnIndexOrThrow(COL_ZH)),
                    unit = c.getInt(c.getColumnIndexOrThrow(COL_UNIT))
                ))
            }
        }
        return list
    }

    fun addUser(username: String, password: String): Boolean {
        val db = writableDatabase
        val cv = ContentValues().apply {
            put(COL_USER, username)
            put(COL_PASS, password)
        }
        val id = db.insert(TBL_USER, null, cv)
        return id != -1L
    }

    fun validateUser(username: String, password: String): Boolean {
        val db = readableDatabase
        val c = db.query(TBL_USER, arrayOf(COL_USER), "$COL_USER = ? AND $COL_PASS = ?", arrayOf(username, password), null, null, null)
        c.use { return it.count > 0 }
    }

    fun recordResult(username: String, unit: Int, score: Int, timeSpent: Int, date: String) {
        val db = writableDatabase
        val cv = ContentValues().apply {
            put(COL_USERFK, username)
            put(COL_UNITREC, unit)
            put(COL_SCORE, score)
            put(COL_TIME, timeSpent)
            put(COL_DATE, date)
        }
        db.insert(TBL_RECORD, null, cv)
    }

    fun getRecordsForUser(username: String): Cursor {
        val db = readableDatabase
        return db.query(TBL_RECORD, null, "$COL_USERFK = ?", arrayOf(username), null, null, "$COL_DATE DESC")
    }

    fun queryVocabCursor(unit: Int?): Cursor {
        val db = readableDatabase
        val where = if (unit == null || unit == 0) null else "$COL_UNIT = ?"
        val args = if (unit == null || unit == 0) null else arrayOf(unit.toString())
        return db.query(TBL_VOCAB, null, where, args, null, null, null)
    }

    fun getAllFeedback(): List<Feedback> {
        val feedbackList = mutableListOf<Feedback>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TBL_FEEDBACK ORDER BY $COL_ID DESC", null)

        cursor.use { c ->
            if (c.moveToFirst()) {
                do {
                    val feedback = Feedback(
                        id = c.getInt(c.getColumnIndexOrThrow(COL_ID)),
                        username = c.getString(c.getColumnIndexOrThrow(COL_FEEDBACK_USER)),
                        feedbackContent = c.getString(c.getColumnIndexOrThrow(COL_FEEDBACK_CONTENT)),
                        submissionDate = c.getString(c.getColumnIndexOrThrow(COL_FEEDBACK_DATE))
                    )
                    feedbackList.add(feedback)
                } while (c.moveToNext())
            }
        }
        return feedbackList
    }

    fun addFeedback(username: String, content: String, date: String) {
        val db = writableDatabase
        val cv = ContentValues().apply {
            put(COL_FEEDBACK_USER, username)
            put(COL_FEEDBACK_CONTENT, content)
            put(COL_FEEDBACK_DATE, date)
        }
        db.insert(TBL_FEEDBACK, null, cv)
    }

    fun getWordsByIds(ids: List<Int>): List<Vocab> {
        val wordList = mutableListOf<Vocab>()
        if (ids.isEmpty()) {
            return wordList
        }

        val db = this.readableDatabase
        val placeholders = ids.joinToString(separator = ", ") { "?" }
        val selection = "$COL_ID IN ($placeholders)"
        val selectionArgs = ids.map { it.toString() }.toTypedArray()
        val cursor = db.query(TBL_VOCAB, null, selection, selectionArgs, null, null, null)

        cursor.use {
            while (it.moveToNext()) {
                val vocab = Vocab(
                    // 新增讀取 ID
                    id = it.getInt(it.getColumnIndexOrThrow(COL_ID)),
                    english = it.getString(it.getColumnIndexOrThrow(COL_EN)),
                    chinese = it.getString(it.getColumnIndexOrThrow(COL_ZH)),
                    unit = it.getInt(it.getColumnIndexOrThrow(COL_UNIT))
                )
                wordList.add(vocab)
            }
        }
        return wordList
    }

    // 【新增】根據使用者和單元獲取所有測驗成績
    fun getResultsForUserByUnit(username: String, unit: Int): List<TestResult> {
        val resultList = mutableListOf<TestResult>()
        val db = this.readableDatabase

        // 建立查詢條件，如果 unit 為 0，則查詢該使用者的所有紀錄
        val selection = if (unit == 0) {
            "$COL_USERFK = ?"
        } else {
            "$COL_USERFK = ? AND $COL_UNITREC = ?"
        }

        val selectionArgs = if (unit == 0) {
            arrayOf(username)
        } else {
            arrayOf(username, unit.toString())
        }

        // 查詢資料庫，並依照日期(ID)排序
        val cursor = db.query(
            TBL_RECORD,
            null,
            selection,
            selectionArgs,
            null,
            null,
            "$COL_ID ASC" // 使用 ASC 確保是從最舊到最新的順序
        )

        cursor.use { c ->
            while (c.moveToNext()) {
                val record = TestResult(
                    id = c.getInt(c.getColumnIndexOrThrow(COL_ID)),
                    username = c.getString(c.getColumnIndexOrThrow(COL_USERFK)),
                    unit = c.getInt(c.getColumnIndexOrThrow(COL_UNITREC)),
                    score = c.getInt(c.getColumnIndexOrThrow(COL_SCORE)),
                    timeSpent = c.getInt(c.getColumnIndexOrThrow(COL_TIME)),
                    recordTime = c.getString(c.getColumnIndexOrThrow(COL_DATE))
                )
                resultList.add(record)
            }
        }
        return resultList
    }


}
