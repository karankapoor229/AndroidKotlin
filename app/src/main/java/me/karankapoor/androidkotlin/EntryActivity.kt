package me.karankapoor.androidkotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_entry.*
import kotlinx.android.synthetic.main.activity_entry.view.*

class EntryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry)

        img_gallery.setOnClickListener{
            MainClickListener()
        }
        img_contacts.setOnClickListener{
            ContactsClickListener()
        }
    }

    fun MainClickListener(){
        val intent = android.content.Intent(this, MainActivity::class.java)
        this.startActivity(intent)
    }
    fun ContactsClickListener(){
        val intent = android.content.Intent(this, ContactsActivity::class.java)
        this.startActivity(intent)
    }
}
