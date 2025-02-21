import android.content.Context
import android.database.Cursor
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hfad.finalproject.R


class ItemAdapter(private val itemAdapterListener: ItemAdapterListener, val cursor: Cursor): RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    interface ItemAdapterListener {
        fun click(position: Int)
    }

    class ViewHolder(itemView: View, private val itemAdapterListener: ItemAdapterListener): RecyclerView.ViewHolder(itemView) {

        private var textView: TextView = itemView.findViewById(R.id.workName)
        private var imageView: ImageView = itemView.findViewById(R.id.imageView)

        init {
            itemView.setOnClickListener{
                val position = adapterPosition
                itemAdapterListener.click(position)
            }
        }

        private var workID: Int = 0

        fun update(cursor: Cursor, context: Context) {
            val name_col = cursor.getColumnIndexOrThrow("name")
            val id_col = cursor.getColumnIndexOrThrow("_id")
            val img_col = cursor.getColumnIndexOrThrow("picture")

            val work_name = cursor.getString(name_col)
            val img_path = cursor.getString(img_col)

            workID = cursor.getInt(id_col)

            textView.text = work_name

            if (img_path.isNotEmpty()) {
                try {
                    val decodedBytes = Base64.decode(img_path, Base64.DEFAULT)
                    val bitmapPreview = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                    imageView.setImageBitmap(bitmapPreview)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                imageView.setImageResource(R.drawable.test)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)

        return ViewHolder(view, itemAdapterListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        cursor.moveToPosition(position)
        holder.update(cursor, holder.itemView.context)
    }

    override fun getItemCount(): Int {
        return cursor.count
    }
}
