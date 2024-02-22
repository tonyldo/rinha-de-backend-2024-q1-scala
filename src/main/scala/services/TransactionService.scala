package services

import domain.{BalanceReport, Client, Transaction}
import infrastructure.adapters.dao.{ClientsDAO, TransactionsDAO}
import slick.jdbc.H2Profile.api._
import slick.jdbc.TransactionIsolation

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object TransactionService {
  def insert(clientId: Int,transaction:Transaction, db:Database): Future[Client] = {

    val query = (for {
      c:Client <- ClientsDAO.findById(clientId)
      _ <- ClientsDAO.update(c.id,c.doIt(transaction))
      _ <- TransactionsDAO.insert(c.id, transaction)
    } yield (c.copy(balance = c.doIt(transaction)))).transactionally.withTransactionIsolation(TransactionIsolation.ReadCommitted)

    db.run(query)
  }

  def balanceReport (clientId:Int, db: Database): Future[BalanceReport] = {
    val findById = ClientsDAO.findById(clientId)
    val queryTransactions = TransactionsDAO.find10Lasts(clientId)
    val dbIO = findById.zip(queryTransactions).map(t=>BalanceReport.from(t))

    db.run(dbIO.transactionally.withTransactionIsolation(TransactionIsolation.ReadCommitted))
  }

}
