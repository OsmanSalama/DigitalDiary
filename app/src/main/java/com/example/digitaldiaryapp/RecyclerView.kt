import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.digitaldiaryapp.NoteWritingActivity
import com.example.digitaldiaryapp.R
import java.io.File

class NotesAdapter(private val notesList: List<String>) :
    RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val noteTitle: TextView = itemView.findViewById(R.id.textViewNoteTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.grid_item, parent, false)
        return NoteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notesList[position]

        // Remove ".txt" from the file name for the title
        val noteTitleWithoutExtension = if (note.endsWith(".txt")) {
            note.substring(0, note.length - 4)
        } else {
            note
        }

        holder.noteTitle.text = noteTitleWithoutExtension

        // Load the sample note content
        val context = holder.itemView.context
        val noteFile = File(context.filesDir, note)

        // Get a sample of the note content (first 100 characters or so)
        val noteSampleText = if (noteFile.exists()) {
            val fullText = noteFile.readText()
            if (fullText.length > 100) {
                fullText.substring(0, 100) + "..." // Show a sample of the note
            } else {
                fullText // Show the full text if it's short
            }
        } else {
            "No content"
        }


        // Handle click event for opening the note in the NoteWritingActivity
        holder.itemView.setOnClickListener {
            val intent = Intent(context, NoteWritingActivity::class.java).apply {
                putExtra("note_title", noteTitleWithoutExtension)
                putExtra("note_content", noteSampleText)
                putExtra("note_file_name", note)
            }
            context.startActivity(intent)
        }
    }



    override fun getItemCount() = notesList.size


}
