package com.hfad.finalproject

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

class WorkDetailFragment : Fragment(){
    private lateinit var workImage: ImageView
    private lateinit var workName: TextView
    private lateinit var workDesc: TextView
    private lateinit var workDate:  TextView
    private lateinit var workArtist: TextView
    private lateinit var cursor: Cursor
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var sqLiteDatabase: SQLiteDatabase
    private var position = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_work_detail, container, false)

        workImage = view.findViewById(R.id.workImage)
        workName = view.findViewById(R.id.workName)
        workDesc = view.findViewById(R.id.workDesc)
        workDate = view.findViewById(R.id.workDate)
        workArtist = view.findViewById(R.id.workArtist)

        databaseHelper = DatabaseHelper(requireContext())
        sqLiteDatabase = databaseHelper.readableDatabase

        cursor = databaseHelper.getAllWorks(Artist.currentArtist)

        arguments?.let {
            position = it.getInt("POSITION", 0)
            Log.d("WorkDetailFragment", "Position: $position")
        }

        cursor.moveToPosition(position)

        val workName_col = cursor.getColumnIndexOrThrow("name")
        val workDesc_col = cursor.getColumnIndexOrThrow("description")
        val workPicture_col = cursor.getColumnIndexOrThrow("picture")
        val workDate_col = cursor.getColumnIndexOrThrow("date")
        val workArtist_col = cursor.getColumnIndexOrThrow("artist")

        val workNameDB = cursor.getString(workName_col)
        val workDescDB = cursor.getString(workDesc_col)
        val workDateDB = cursor.getString(workDate_col)
        val workArtistDB = cursor.getString(workArtist_col)
        val workPictureDB = cursor.getString(workPicture_col)

        val decodedBytes = Base64.decode(workPictureDB, Base64.DEFAULT)
        val bitmapPreview = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        workImage.setImageBitmap(bitmapPreview)

        val title_text = "Title: $workNameDB"
        val desc_text = "Description: $workDescDB"
        val artist_text = "Artist: $workArtistDB"
        workName.text = title_text
        workDesc.text = desc_text
        workDate.text = workDateDB
        workArtist.text = artist_text

        return view
    }
}