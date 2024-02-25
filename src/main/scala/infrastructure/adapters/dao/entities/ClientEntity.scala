package infrastructure.adapters.dao.entities

import domain.Client

case class ClientEntity(id:Int, limit:Int, balance:Int){
  def toDomain: Client = {
    Client(this.id,Some(this.limit),Some(this.balance))
  }
}

