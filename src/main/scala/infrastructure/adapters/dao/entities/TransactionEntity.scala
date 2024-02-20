package infrastructure.adapters.dao.entities

import domain.Transaction

import java.time.LocalDateTime

case class TransactionEntity(id: Option[Int], clientId:Int, money:Int, transactionType:Char, description: String, creation:LocalDateTime){
  def this(id: Option[Int], clientId:Int, money:Int, transactionType:Char, description: String) = {
    this(id, clientId, money, transactionType, description, LocalDateTime.now())
  }
}

object TransactionEntityUtil {
  def fromDomain(transaction: Transaction, clientId: Int): TransactionEntity = {
    new TransactionEntity(None, clientId, transaction.money.value, transaction.transactionTypeToChar, transaction.description.value)
  }
}
