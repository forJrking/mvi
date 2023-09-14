package com.example.mvi.screen

import android.view.View
import com.example.mvi.R
import com.example.mvi.framework.base.BaseScreen
import io.github.kakaocup.kakao.common.assertions.BaseAssertions
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher

object ContactListScreen : BaseScreen<ContactListScreen>() {

    private class ContactItem(parent: Matcher<View>) : KRecyclerItem<ContactItem>(parent) {
        val avatar: KView = KView(parent) { withId(R.id.avatar) }
        val itemNumber: KTextView = KTextView(parent) { withId(R.id.item_number) }
        val content: KTextView = KTextView(parent) { withId(R.id.content) }
    }

    private val contactList = KRecyclerView({
        withId(R.id.list)
    }, itemTypeBuilder = {
        itemType(::ContactItem)
    })


    fun dataListShown() {
        contactList {
            firstChild<ContactItem> {
                isDisplayed()
                itemNumber { hasText("1") }
                content { containsText("1") }
            }
            childAt<ContactItem>(1) {
                isDisplayed()
                itemNumber { hasText("2") }
                content { containsText("2") }
            }
            scrollToEnd()
        }
    }

    override val specificViews: List<BaseAssertions>
        get() = listOf(contactList)
}
