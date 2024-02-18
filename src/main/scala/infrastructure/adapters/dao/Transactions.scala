package infrastructure.adapters.dao

import infrastructure.adapters.dao.entities.TransactionEntity
import slick.jdbc.H2Profile.api._

import java.time.LocalDateTime

case class Transactions(tag: Tag) extends Table[TransactionEntity](tag, "TRANSACTIONS") {
  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
  def clientId = column[Int]("CLIENT_ID")
  def client = foreignKey("CLIENT_FK", clientId, TableQuery[Clients])(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)
  def money = column[Int]("MONEY")
  def transactionType = column[Char]("TRANSACTION_TYPE")
  def description = column[String]("DESCRIPTION")
  private def creation = column[LocalDateTime]("CREATION")
  override def * = (id.?, clientId, money, transactionType, description, creation) <> (TransactionEntity.tupled, TransactionEntity.unapply)
}
