package infrastructure.adapters.dao

import domain.{ClientNotFoundException, Transaction}
import infrastructure.adapters.dao.entities.ClientEntity
import slick.jdbc.H2Profile.api._

import scala.concurrent.ExecutionContext.Implicits.global


case class Clients(tag: Tag) extends Table[ClientEntity](tag, "CLIENTS") {
  def id = column[Int]("ID", O.PrimaryKey)
  def limit = column[Int]("LIMIT")
  def balance = column[Int]("BALANCE")
  override def * = (id, limit, balance) <> (ClientEntity.tupled, ClientEntity.unapply)
}

object ClientsDAO {
  def getClientsDAO = TableQuery[Clients]

  def findAndUpdate(clientId: Int, transaction: Transaction, clientsTable: TableQuery[Clients]): DBIOAction[Int, NoStream, Effect.Read with Effect.Write] = {
    clientsTable.filter(_.id === clientId).take(1).result.flatMap(c => {
      val clientFind = if (c.nonEmpty) c.head.toDomain else throw ClientNotFoundException("Not found client: " + clientId)
      clientsTable.filter(_.id === clientFind.id).map(_.balance).update(clientFind.doIt(transaction))
    })
  }
}