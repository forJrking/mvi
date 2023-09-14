package com.example.mvi.framework.extensions

import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext
import com.kaspersky.kaspresso.testcases.models.info.StepInfo

fun <Data> TestContext<Data>.GIVEN(description: String, actions: (StepInfo) -> Unit) =
    step(description, actions)

fun <Data> TestContext<Data>.WHEN(description: String, actions: (StepInfo) -> Unit) =
    step(description, actions)

fun <Data> TestContext<Data>.THEN(description: String, actions: (StepInfo) -> Unit) =
    step(description, actions)

fun <Data> TestContext<Data>.AND(description: String, actions: (StepInfo) -> Unit) =
    step(description, actions)
