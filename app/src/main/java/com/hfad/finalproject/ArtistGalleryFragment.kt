package com.hfad.finalproject

import Artist
import ItemAdapter
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.database.CursorWindow;
import android.util.Log
import java.lang.reflect.Field;

class ArtistGalleryFragment : Fragment(),  ItemAdapter.ItemAdapterListener {

    private lateinit var galleryRecycler: RecyclerView
    private lateinit var itemAdapter: ItemAdapter
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var sqLiteDatabase : SQLiteDatabase
    private lateinit var cursor: Cursor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        /*
        when i tried uploading a very large photo, the select statement from the db to load
        the image into the recycler view broke, and gave me this error:

        Process: com.hfad.finalproject, PID: 11698
        android.database.sqlite.SQLiteBlobTooBigException: Row too big to fit into CursorWindow
        requiredPos=1, totalRows=2

        I found the code below here:
        https://github.com/craftzdog/react-native-sqlite-2/issues/57
        https://github.com/andpor/react-native-sqlite-storage/issues/364

        Copied and pasted it in as the java code provided, and android studio asked if i wanted to convert it
        to kotlin , I clicked yes , and now the very large images are able to be pulled in.

        The code below changes the cursor window size to fit the large query size, and we change it to 100mb.

        It's not a recommended fix as it may allow your app to use TOO much memory. However, for this project, since
        I'm not putting it into the public for use , it'll have to do for now. Not many other quick solutions were offered
        when I searched.
         */

        try {
            val field: Field = CursorWindow::class.java.getDeclaredField("sCursorWindowSize")
            field.setAccessible(true)
            field.set(null, 100 * 1024 * 1024) //the 100MB is the new size
        } catch (e: Exception) {
            e.printStackTrace()
        }
        //-----------------------^^  end of code from comment ^^ ----------------------

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_artist_gallery, container, false)

        galleryRecycler = view.findViewById(R.id.galleryRecyclerView)

        databaseHelper = DatabaseHelper(requireContext())
        sqLiteDatabase = databaseHelper.readableDatabase

        cursor = databaseHelper.getAllWorks(Artist.currentArtist)

        itemAdapter = ItemAdapter(this, cursor)
        galleryRecycler.adapter = itemAdapter
        galleryRecycler.layoutManager = LinearLayoutManager(requireContext())

        return view
    }



    override fun click(position: Int) {
        val intent = Intent(requireContext(), WorkDetailActivity::class.java)
        intent.putExtra("ID", position)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cursor.close()
    }
}