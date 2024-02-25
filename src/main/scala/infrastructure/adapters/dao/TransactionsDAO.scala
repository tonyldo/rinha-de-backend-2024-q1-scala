package infrastructure.adapters.dao

import slick.jdbc.JdbcProfile

case class TransactionsDAO(profile:JdbcProfile) {

  import domain.Transaction
  import infrastructure.adapters.dao.entities.{TransactionEntity, TransactionEntityUtil}
  import slick.dbio.Effect
  import profile.api._
  import slick.sql.FixedSqlAction

  import java.time.LocalDateTime
  import scala.concurrent.ExecutionContext.Implicits.global

  case class Transactions(tag: Tag) extends Table[TransactionEntity](tag, "TRANSACTIONS") {
    def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
    def clientId = column[Int]("CLIENT_ID")
    def money = column[Int]("MONEY")
    def transactionType = column[Char]("TRANSACTION_TYPE")
    def description = column[String]("DESCRIPTION")
    def creation = column[LocalDateTime]("CREATION")
    override def * = (id.?, clientId, money, transactionType, description, creation) <> (TransactionEntity.tupled, TransactionEntity.unapply)
  }

  val transactionTable = TableQuery[Transactions]

  def insert(clientId: Int, transaction: Transaction): FixedSqlAction[Int, NoStream, Effect.Write] = {
    transactionTable += TransactionEntityUtil.fromDomain(transaction, clientId)
  }

  def find10Lasts(clientId: Int): DBIOAction[Seq[Transaction], NoStream, Effect.Read] = {
  transactionTable.filter(_.clientId === clientId)
      .take(10)
      .sorted(_.creation.desc)
      .result
      .map(s=>s.
        map(e=>e.toDomain))
  }
}
