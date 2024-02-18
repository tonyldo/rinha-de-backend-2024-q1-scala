package infrastructure.adapters.dao.entities

import java.time.LocalDateTime

case class TransactionEntity(id: Option[Int], clientId:Int, money:Int, transactionType:Char, description: String, creation:LocalDateTime){
  def this(id: Option[Int], clientId:Int, money:Int, transactionType:Char, description: String) = {
    this(id, clientId, money, transactionType, description, LocalDateTime.now())
  }
}
