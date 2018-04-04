package com.udacity.notepad

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem

import com.udacity.notepad.crud.CreateActivity
import com.udacity.notepad.recycler.NotesAdapter
import com.udacity.notepad.util.SpaceItemDecoration
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*
            Kotlin-android-extensions supports direct use
            of
            - R.id.toolbar in activity_main.xml
            - R.id.fab in activity_main.xml
            - R.id.recycler in content_main.xml
            with
            - import kotlinx.android.synthetic.main.activity_main.*
            - import kotlinx.android.synthetic.main.content_main.*
            without
            - Nullable check(: already known)

            Extension works during compile time:
            - Inject caching codes
              private HashMap _$_findViewCache;

              public View _$_findCachedViewById(int var1) {
                  if(this._$_findViewCache == null) {
                     this._$_findViewCache = new HashMap();
                  }

                  View var2 = (View)this._$_findViewCache.get(Integer.valueOf(var1));
                  if(var2 == null) {
                     var2 = this.findViewById(var1);
                     this._$_findViewCache.put(Integer.valueOf(var1), var2);
                  }

                  return var2;
               }

               public void _$_clearFindViewByIdCache() {
                  if(this._$_findViewCache != null) {
                     this._$_findViewCache.clear();
                  }
               }
            - Back fields into the activity class under the hood

            Weakness in case of
            - customized view
            - ViewHolder
         */
        setSupportActionBar(toolbar)
        fab.setOnClickListener { startActivity(CreateActivity.get(this@MainActivity)) }     // Kotlin SAM conversions(Lambda expression)

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.addItemDecoration(SpaceItemDecoration(this, R.dimen.margin_small))
        recycler.adapter = NotesAdapter(this)
    }

    override fun onResume() {
        super.onResume()
        refresh()
    }

    public override fun onDestroy() {
        super.onDestroy()
        recycler.adapter = null
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        when (item.itemId) {        // converted from Java 'switch' to Kotlin 'when'
//            R.id.action_settings -> return true
//            else -> return super.onOptionsItemSelected(item)
//        }
        return when (item.itemId) {         // converted from Java 'switch' to Kotlin 'when'
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }                                   // IF, WHILE, WHEN, TRY-CATCH and etc. can return values.
    }


    private fun refresh() {
        (recycler.adapter as NotesAdapter).refresh()
    }
}
