package com.spider.composes.model.engine

import android.content.Context
import android.media.AudioManager
import android.media.SoundPool
import com.spider.composes.R
import com.spider.composes.di.IO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

val soundModule = module {
    single { MusicPlayer(androidContext(), get(qualifier = qualifier(IO) )) }
}

class MusicPlayer(private val mContext: Context, private val dispatcher: CoroutineDispatcher) {
    private val pool = HashMap<Int, Int>()
    private val mSp: SoundPool = SoundPool(10, AudioManager.STREAM_MUSIC, 100)

    init {
        MainScope().launch(dispatcher) {
            val deckId = mSp.load(mContext, R.raw.solitaire_deck, 1)
            pool[R.raw.solitaire_deck] = deckId
            val winId = mSp.load(mContext, R.raw.solitaire_win, 1)
            pool[R.raw.solitaire_win] = winId
            val flipId = mSp.load(mContext, R.raw.solitaire_flip, 1)
            pool[R.raw.solitaire_flip] = flipId
            val shuffle = mSp.load(mContext, R.raw.solitaire_shuffle, 1)
            pool[R.raw.solitaire_shuffle] = shuffle
        }
    }

    fun play(raw: Int) {
        val id = pool[raw]!!
        mSp.play(id, 1f, 1f, 0, 0, 1.8f)
    }
}