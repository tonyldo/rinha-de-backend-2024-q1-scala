package domain

import domain.TransactionType.{CREDIT, DEBIT}

case class Client(id: Int, limit: Option[Int], balance: Option[Int]) {

  def isValidDebit(transaction: Transaction):Boolean = {
    this.balance.get - transaction.money.value >= this.limit.get * -1
  }

  def doIt(transaction:Transaction):Int ={
    transaction.transactionType match {
      case DEBIT => if (isValidDebit(transaction)) this.balance.get-transaction.money.value else throw TransactionLimitException("Limit exceeded!")
      case CREDIT => this.balance.get+transaction.money.value
    }
  }
}