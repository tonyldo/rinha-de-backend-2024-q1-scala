package domain

import java.time.LocalDateTime

case class Transaction(money: TransactionValue, transactionType: TransactionType, description: TransactionDescription, date: LocalDateTime) {

  def this(money: TransactionValue, transactionType: TransactionType, description: TransactionDescription) = {
    this(money, transactionType, description, LocalDateTime.now())
  }

}
