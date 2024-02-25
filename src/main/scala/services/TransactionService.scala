package services
import slick.jdbc.{JdbcProfile, TransactionIsolation}

import scala.concurrent.Future

case class TransactionService(profile: JdbcProfile) {

  import domain.{BalanceReport, Client, Transaction}
  import infrastructure.adapters.dao.{ClientsDAO, TransactionsDAO}
  import profile.api._
  import scala.concurrent.ExecutionContext.Implicits.global
  import slick.jdbc.JdbcBackend.Database

  def insert(clientId: Int,transaction:Transaction,clientDao: ClientsDAO,transactionDao: TransactionsDAO, db:Database): Future[Client] = {
    val client = Client(clientId,None,None)
    val query = (for {
      c:Client <- clientDao.findById(client.id)
      _ <- clientDao.update(c.id,c.doIt(transaction))
      _ <- transactionDao.insert(c.id, transaction)
    } yield (c.copy(balance = Some(c.doIt(transaction))))).transactionally.withTransactionIsolation(TransactionIsolation.ReadCommitted)

    db.run(query)
  }

  def balanceReport (clientId:Int,clientDao: ClientsDAO,transactionDao: TransactionsDAO, db: Database): Future[BalanceReport] = {
    val findById = clientDao.findById(clientId)
    val queryTransactions = transactionDao.find10Lasts(clientId)
    val dbIO = findById.zip(queryTransactions).map(t=>BalanceReport.from(t))

    db.run(dbIO.transactionally.withTransactionIsolation(TransactionIsolation.ReadCommitted))
  }

}
