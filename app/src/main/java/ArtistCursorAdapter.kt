import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cursoradapter.widget.CursorAdapter

class ArtistCursorAdapter(
    context: Context,
    cursor: Cursor
) : CursorAdapter(context, cursor, 0) {

    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup?): View {
        // Inflate the layout for each item in the list
        return LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false)
    }

    override fun bindView(view: View, context: Context, cursor: Cursor) {
        val textView: TextView = view.findViewById(android.R.id.text1)

        val name_col = cursor.getColumnIndexOrThrow("artist_name")

        val artistName = cursor.getString(name_col)

        textView.text = artistName
    }
}