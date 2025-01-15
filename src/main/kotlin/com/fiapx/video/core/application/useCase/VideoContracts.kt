package com.fiapx.video.core.application.useCase

interface Findable {
    fun find()
}

interface Listable {
    fun all()
}

interface Processable {
    fun execute()
}

interface Destroyable {
    fun execute()
}
