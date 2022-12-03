package com.group.libraryapp.dto.book.request

import com.group.libraryapp.constant.BookType

data class BookRequest(
    val name: String,
    val type: BookType
)