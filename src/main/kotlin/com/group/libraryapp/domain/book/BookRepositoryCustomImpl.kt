package com.group.libraryapp.domain.book

import com.group.libraryapp.dto.book.response.BookStatResponse
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory

class BookRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : BookRepositoryCustom {

    override fun getStats(): List<BookStatResponse> {
        val book = QBook.book

        return queryFactory.select(
            Projections.constructor(
                BookStatResponse::class.java,
                book.type,
                book.id.count()
            ))
            .from(book)
            .groupBy(book.type)
            .fetch()
    }
}