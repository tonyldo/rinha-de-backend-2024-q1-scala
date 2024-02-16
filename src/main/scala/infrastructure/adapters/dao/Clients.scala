package infrastructure.adapters.dao

import infrastructure.adapters.dao.entities.ClientEntity
import slick.jdbc.H2Profile.api._


case class Clients(tag: Tag) extends Table[ClientEntity](tag, "CLIENTS") {
  def id = column[Int]("ID", O.PrimaryKey)
  def limit = column[Int]("LIMIT")
  def balance = column[Int]("BALANCE")
  override def * = (id, limit, balance) <> (ClientEntity.tupled, ClientEntity.unapply)
}
