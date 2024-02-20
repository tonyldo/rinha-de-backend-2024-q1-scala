package infrastructure.adapters.dao.entities

import domain.Client

case class ClientEntity(id:Int, limit:Int, balance:Int){
  def toDomain = {
    Client(this.id,this.limit,this.balance)
  }
}

