package com.example.cardb

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private var items: ArrayList<String> = ArrayList()
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var dbrw: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbrw = MyDBHelper(this).writableDatabase
        adapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, items)
        findViewById<ListView>(R.id.listView).adapter = adapter
        setListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        dbrw.close()
    }

    private fun setListener() {
        val edBrand = findViewById<EditText>(R.id.edBrand)
        val edYear = findViewById<EditText>(R.id.edYear)
        val edPrice = findViewById<EditText>(R.id.edPrice)

        findViewById<Button>(R.id.btnInsert).setOnClickListener {
            if (edBrand.length() < 1 || edYear.length() < 1 || edPrice.length() < 1)
                showToast("欄位請勿留空")
            else
                try {
                    dbrw.execSQL(
                        "INSERT INTO carTable(brand, year, price) VALUES(?,?,?)",
                        arrayOf(edBrand.text.toString(),
                            edYear.text.toString().toInt(),
                            edPrice.text.toString().toInt())
                    )
                    showToast("新增: ${edBrand.text}, 年份: ${edYear.text}, 價格: ${edPrice.text}")
                    cleanEditText()
                } catch (e: Exception) {
                    showToast("新增失敗:$e")
                }
        }

        findViewById<Button>(R.id.btnUpdate).setOnClickListener {
            if (edBrand.length() < 1 || edYear.length() < 1 || edPrice.length() < 1)
                showToast("欄位請勿留空")
            else
                try {
                    dbrw.execSQL(
                        "UPDATE carTable SET year = ?, price = ? WHERE brand LIKE ?",
                        arrayOf(edYear.text.toString().toInt(),
                            edPrice.text.toString().toInt(),
                            edBrand.text.toString())
                    )
                    showToast("更新: ${edBrand.text}, 年份: ${edYear.text}, 價格: ${edPrice.text}")
                    cleanEditText()
                } catch (e: Exception) {
                    showToast("更新失敗:$e")
                }
        }

        findViewById<Button>(R.id.btnDelete).setOnClickListener {
            if (edBrand.length() < 1)
                showToast("車廠請勿留空")
            else
                try {
                    dbrw.execSQL("DELETE FROM carTable WHERE brand LIKE ?",
                        arrayOf(edBrand.text.toString()))
                    showToast("刪除: ${edBrand.text}")
                    cleanEditText()
                } catch (e: Exception) {
                    showToast("刪除失敗:$e")
                }
        }

        findViewById<Button>(R.id.btnQuery).setOnClickListener {
            val queryString = if (edBrand.length() < 1)
                "SELECT * FROM carTable"
            else
                "SELECT * FROM carTable WHERE brand LIKE '${edBrand.text}'"

            val c = dbrw.rawQuery(queryString, null)
            c.moveToFirst()
            items.clear()
            showToast("共有 ${c.count} 筆資料")
            for (i in 0 until c.count) {
                items.add("廠牌:${c.getString(0)}\t\t\t\t年份:${c.getInt(1)}\t\t\t\t價格:${c.getInt(2)}")
                c.moveToNext()
            }
            adapter.notifyDataSetChanged()
            c.close()
        }
    }

    private fun showToast(text: String) =
        Toast.makeText(this,text, Toast.LENGTH_LONG).show()

    private fun cleanEditText() {
        findViewById<EditText>(R.id.edBrand).setText("")
        findViewById<EditText>(R.id.edYear).setText("")
        findViewById<EditText>(R.id.edPrice).setText("")
    }
}