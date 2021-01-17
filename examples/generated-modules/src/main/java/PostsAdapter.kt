/*
 * Copyright 2020 Slawomir Czerwinski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package it.czerwinski.android.hilt.examples.generated

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Picasso
import it.czerwinski.android.hilt.examples.generated.model.Post

class PostsAdapter(
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<PostsAdapter.ViewHolder>() {

    var posts: List<Post> = emptyList()
        set(value) {
            val oldItems = field
            field = value
            DiffCallback(oldItems, value)
                .calculateDiff()
                .dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        LayoutInflater.from(parent.context)
            .inflate(R.layout.posts_list_item, parent, false)
            .let(::ViewHolder)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount(): Int = posts.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val card = itemView.findViewById<MaterialCardView>(R.id.card)
        private val image = itemView.findViewById<ImageView>(R.id.image)
        private val title = itemView.findViewById<TextView>(R.id.title)
        private val brief = itemView.findViewById<TextView>(R.id.brief)

        fun bind(post: Post) {
            post.image?.let { imageUrl ->
                Picasso.get()
                    .load(Uri.parse(imageUrl))
                    .placeholder(R.drawable.image_placeholder)
                    .into(image)
            } ?: image.setImageDrawable(null)
            title.text = post.title
            brief.text = post.brief
            card.setOnClickListener {
                listener.onItemClick(post)
            }
        }
    }

    private class DiffCallback(
        private val oldItems: List<Post>,
        private val newItems: List<Post>
    ) : DiffUtil.Callback() {

        fun calculateDiff(): DiffUtil.DiffResult = DiffUtil.calculateDiff(this)

        override fun getOldListSize(): Int = oldItems.size

        override fun getNewListSize(): Int = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldItems[oldItemPosition].id == newItems[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldItems[oldItemPosition] == newItems[newItemPosition]
    }

    fun interface OnItemClickListener {
        fun onItemClick(post: Post)
    }
}
