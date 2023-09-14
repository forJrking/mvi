package com.example.study.repo

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.recyclerview.widget.DiffUtil
import kotlinx.parcelize.Parcelize
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.util.Random
import kotlin.math.abs


/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 * TODO: Replace all uses of this class before publishing your app.
 */
object DataProviders {

    /**
     * An array of sample (placeholder) items.
     */
    val items: MutableList<Contact>
        get() = ArrayList<Contact>().apply {
            for (i in 1..COUNT) {
                add(createPlaceholderItem(i))
            }
        }

    /**
     * A map of sample (placeholder) items, by ID.
     */

    private const val COUNT = 25

    private fun createPlaceholderItem(position: Int): Contact {
        return Contact(
            position.toString(),
            "${getRandomHan(3)}-$position",
            makeDetails(position)
        )
    }

    private fun makeDetails(position: Int): String {
        val builder = StringBuilder()
        builder.append("Details about Item: ").append(position)
        for (i in 0 until position) {
            builder.append("\nMore details information here.")
        }
        return builder.toString()
    }

    private fun getRandomHan(len: Int): String? {
        var ret: String? = ""
        val random = Random()
        for (i in 0 until len) {
            var str: String? = null
            val hightPos: Int = 176 + abs(random.nextInt(39)) // 获取高位值
            val lowPos: Int = 161 + abs(random.nextInt(93)) // 定义高低位 // 获取低位值
            val b = ByteArray(2)
            b[0] = hightPos.toByte()
            b[1] = lowPos.toByte()
            try {
                str = String(b, Charset.forName("GBK")) // 转成中文
            } catch (ex: UnsupportedEncodingException) {
                ex.printStackTrace()
            }
            ret += str
        }
        return ret
    }
}

/**
 * A placeholder item representing a piece of content.
 */
@Keep
@Parcelize
data class Contact(val id: String, val content: String, val details: String) : Parcelable {
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Contact>() {

            override fun areItemsTheSame(
                oldItem: Contact,
                newItem: Contact
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: Contact,
                newItem: Contact
            ): Boolean = oldItem == newItem
        }
    }
}