package com.spider.composes.model

import org.koin.dsl.module

/**
 * @description:
 * @author: forjrking
 * @date: 2023/9/23 17:51
 */
val reducerModule = module {
    factory {
        GameReducer()
    }
}

class GameReducer {}