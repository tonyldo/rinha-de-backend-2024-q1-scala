package services

import domain.Transaction
import infrastructure.adapters.dao.{ClientsDAO, TransactionsDAO}
import slick.jdbc.H2Profile.api._
import slick.jdbc.TransactionIsolation

import scala.concurrent.Future

object TransactionService {
  def insert(clientId: Int,transaction:Transaction, db: Database): Future[Int] = {

    val clientsTable = ClientsDAO.getClientsDAO
    val transactionTable = TransactionsDAO.getTransactionsDAO

    val clientQuery = ClientsDAO.findAndUpdate(clientId, transaction, clientsTable)

    val insertTransactionQuery = TransactionsDAO.insert(clientId, transaction, transactionTable)

    val dbIO = clientQuery andThen insertTransactionQuery

    db.run(dbIO.transactionally.withTransactionIsolation(TransactionIsolation.RepeatableRead))
  }

}
