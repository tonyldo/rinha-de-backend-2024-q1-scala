package domain.ports

import domain.Client

trait ClientRepository {
  def getClientById(id:Int):Client
}
