package com.group.libraryapp.domain.user.loanhistory

import com.group.libraryapp.constant.UserLoanStatus
import org.springframework.data.jpa.repository.JpaRepository

interface UserLoanHistoryRepository : JpaRepository<UserLoanHistory, Long>, UserLoanRepositoryCustom {

    fun countAllByStatus(status: UserLoanStatus): Long
}