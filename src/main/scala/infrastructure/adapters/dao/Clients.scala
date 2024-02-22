package infrastructure.adapters.dao

import domain.{Client, ClientNotFoundException}
import infrastructure.adapters.dao.entities.ClientEntity
import slick.dbio.Effect
import slick.jdbc.H2Profile.api._
import slick.sql.FixedSqlAction

import scala.concurrent.ExecutionContext.Implicits.global


case class Clients(tag: Tag) extends Table[ClientEntity](tag, "CLIENTS") {
  def id = column[Int]("ID", O.PrimaryKey)
  def limit = column[Int]("LIMIT")
  def balance = column[Int]("BALANCE")
  override def * = (id, limit, balance) <> (ClientEntity.tupled, ClientEntity.unapply)
}

object ClientsDAO {
  val clientsTable = TableQuery[Clients]

  def update(clientId: Int, newBalance:Int): FixedSqlAction[Int, NoStream, Effect.Write] = {
      clientsTable
        .filter(_.id === clientId)
        .map(_.balance)
        .update(newBalance)
  }

  def findById(clientId: Int): DBIOAction[Client, NoStream, Effect.Read] = {
    clientsTable
      .filter(_.id === clientId)
      .result
      .map(s=> if (s.nonEmpty) s.head.toDomain else throw ClientNotFoundException("Not found client: " + clientId))
  }
}