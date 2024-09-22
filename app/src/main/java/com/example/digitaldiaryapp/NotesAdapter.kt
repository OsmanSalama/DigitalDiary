package com.example.digitaldiaryapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NotesAdapter(
    private val notesList: List<Triple<String, String, String>>,
    private val onItemClickListener: (Triple<String, String, String>) -> Unit
) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val noteTitle: TextView = itemView.findViewById(R.id.textViewNoteTitle)
        val noteDate: TextView = itemView.findViewById(R.id.textViewNoteDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.grid_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notesList[position]
        holder.noteTitle.text = note.first // Note title
        holder.noteDate.text = note.third // Note creation date

        // Set the click listener for each note item
        holder.itemView.setOnClickListener {
            onItemClickListener(note)
        }
    }

    override fun getItemCount(): Int {
        return notesList.size
    }
}
